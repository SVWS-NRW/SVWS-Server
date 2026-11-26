package de.svws_nrw.base.email;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

import de.svws_nrw.base.LogUtils;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import jakarta.mail.MessagingException;
import jakarta.mail.SendFailedException;
import jakarta.mail.internet.AddressException;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Manager verwaltet Jobs zum Versenden von E-Mails mithilfe eines Threads pro Datenbankschema und Benutzer.
 * Diese werden über den Thread nebenläufig versendet und der Status und ein Log werden dabei erstellt. Zur Steuerung,
 * dass nicht zu viele E-Mails versendet werden, kann eine Versandbegrenzung in Form eines Rate-Limits eingestellt werden.
 */
public final class EmailJobManager {

	/** Der Name des Datenbank-Schemas, dem dieser Manager zugeordnet ist */
	private final @NotNull String schema;

	/** Die ID des Benutzers aus dem Datenbank-Schema, dem dieser Manager zugeordnet ist. */
	private final long idUser;

	/** Wie oft der Worker abgelaufene Jobs aufräumt, solange keine neuen Jobs kommen. */
	private static final long CLEANUP_INTERVAL_MS = 1000;

	/** Eine Queue zur Verwaltung der Jobs, welche den Status QUEUED haben. */
	private final BlockingQueue<EmailJob> jobs = new LinkedBlockingQueue<>();

	/** Eine Hash-Map zum Speichern aller Jobs anhand ihrer ID. Hier sind alle Jobs unabhängig vom Status enthalten. */
	private final ConcurrentHashMap<Long, EmailJob> mapJobs = new ConcurrentHashMap<>();

	/** Eine Deque, um die Sendezeitpunkte für die letzten 60 Sekunden zu speichern, um damit die Senderate von E-Mails pro Minute zu begrenzen. */
	private final ArrayDeque<Long> sendTimestamps = new ArrayDeque<>();

	/** Schützt {@link #sendTimestamps}. Ein ReentrantLock, weil wait() in einem synchronized-Block unter Java 21 den Trägerthread blockiert. */
	private final ReentrantLock rateLimitLock = new ReentrantLock();

	/** Weckt wartende Threads, wenn sich die Zeitstempel ändern. */
	private final Condition rateLimitChanged = rateLimitLock.newCondition();

	/**
	 * Dieses Lock-Objekt stellt sicher, dass die Statusübergänge eines Jobs atomar ablaufen.
	 * Es schützt konkret die folgenden kritischen Übergänge:
	 *
	 * <ul>
	 *   <li><b>QUEUED → SENDING</b> (in {@link #processJob}): Der Worker-Thread prüft das
	 *       Abbruch-Flag und setzt den Status erst dann auf SENDING, wenn kein Abbruch
	 *       angefordert wurde. Ohne diesen Lock könnte {@link #cancelJob} gleichzeitig
	 *       den Status auf CANCELED setzen.</li>
	 *   <li><b>QUEUED → CANCELED</b> (in {@link #cancelJob}): Der Status-Check und das
	 *       Entfernen des Jobs aus der Queue laufen atomar ab. Ohne diesen Lock könnte
	 *       der Worker-Thread den Job zwischen dem Check und dem Entfernen bereits
	 *       übernehmen und auf SENDING setzen.</li>
	 *   <li><b>Eintragen in die Queue</b> (in {@link #enqueue}): Das Einfügen in
	 *       {@code mapJobs} und in die {@code jobs}-Queue erfolgt atomar, sodass
	 *       {@link #cancelJob} keinen Zwischenzustand sehen kann, in dem der Job bereits
	 *       in der Map, aber noch nicht in der Queue eingetragen ist.</li>
	 * </ul>
	 *
	 * Der Lock wird jeweils nur für den Statusübergang selbst gehalten und sofort
	 * danach freigegeben, um den eigentlichen Versandvorgang nicht zu blockieren.
	 */
	private final Object queueTransitionLock = new Object();

	/** Der Worker-Thread, der für diesen Job-Manager verantwortlich ist. */
	private final Thread thread;

	/** Der interne Status, ob dieser Job-Manager aktuell läuft. */
	private volatile boolean running = true;

	/** Die nächste freie Job-ID zur Vergabe an den nächsten Job */
	private final AtomicLong nextJobId = new AtomicLong(1);


	/**
	 * Erstellt einen neuen E-Mail-Job-Manager zum Versenden von E-Mails.
	 * Dieser verwendet einen Worker-Thread, um E-Mails asynchron zu versenden.
	 *
	 * @param schema   das SVWS-Datenbank-Schema, dem dieser Manager zugeordnet ist
	 * @param idUser   die Benutzer-ID aus dem SVWS-Datenbank-Schema, der dieser Manager zugeordnet ist
	 */
	EmailJobManager(final @NotNull String schema, final long idUser) {
		this.schema = schema;
		this.idUser = idUser;
		final @NotNull String threadName = "EmailJobManager_" + schema + "_" + idUser;
		this.thread = Thread.ofVirtual().name(threadName).start(this::run);
	}


	/**
	 * Fügt einen neuen Job zum Versenden von E-Mails zu der Warteschlange des Managers hinzu.
	 *
	 * @param job   der E-Mail-Job
	 *
	 * @return die ID des neuen Jobs oder -1, wenn der Job bereits eine ID hatte und erneut versucht wurde diesen Job hinzuzufügen
	 */
	public long enqueue(final @NotNull EmailJob job) {
		removeExpiredJobs();
		// Setzt die Job-ID und fügt den Job hinzu.
		final boolean isNewJob = job.setId(nextJobId.getAndIncrement());
		if (!isNewJob) {
			return -1;
		}
		// Erst in mapJobs eintragen, dann in die Queue – beide Schritte unter dem Lock, damit cancelJob() keinen inkonsistenten Zustand sieht.
		synchronized (queueTransitionLock) {
			mapJobs.put(job.getId(), job);
			jobs.add(job);
		}
		return job.getId();
	}


	/**
	 * Fügt einen neuen Job nur hinzu, solange weniger als die angegebene Zahl an Jobs wartet oder versendet wird. Zählen und Hinzufügen laufen
	 * unter derselben Sperre, damit parallele Aufträge die Grenze nicht gemeinsam überschreiten.
	 *
	 * @param job             der E-Mail-Job
	 * @param maxActiveJobs   die höchste Zahl an Jobs, die gleichzeitig warten oder versendet werden
	 *
	 * @return die ID des neuen Jobs oder -1, wenn die Grenze erreicht ist oder der Job bereits eine ID hatte
	 */
	public long enqueue(final @NotNull EmailJob job, final int maxActiveJobs) {
		synchronized (queueTransitionLock) {
			if (countActiveJobs() >= maxActiveJobs) {
				return -1;
			}
			return enqueue(job);
		}
	}


	/**
	 * Zählt die Jobs, die warten oder versendet werden. Wer einen Auftrag aufwendig vorbereitet, prüft damit vorher, ob er noch Platz hat. Die
	 * verbindliche Prüfung macht {@link #enqueue(EmailJob, int)}.
	 *
	 * @return die Zahl der wartenden und laufenden Jobs
	 */
	public long countActiveJobs() {
		return mapJobs.values().stream()
				.filter(j -> (j.getStatus() == EmailJobStatus.QUEUED) || (j.getStatus() == EmailJobStatus.SENDING))
				.count();
	}


	/**
	 * Stoppt den Thread zum Versenden von E-Mails.
	 * Hierzu wird das interne Flag running auf false gesetzt und der Worker-Thread des Managers wird unterbrochen.
	 * Außerdem werden die beendeten Jobs sofort entfernt.
	 * Diese Methode sollte aufgerufen werden, bevor die Anwendung heruntergefahren wird, um die verbleibenden
	 * Manager-Aktivitäten abzuschließen.
	 */
	public void shutdown() {
		running = false;
		thread.interrupt();
		mapJobs.values().removeIf(job -> isFinished(job.getStatus()));
	}


	/**
	 * Gibt den Namen des SVWS-Datenbank-Schemas zurück, dem dieser Manager zugeordnet ist.
	 *
	 * @return der Name des SVWS-Datenbank-Schemas
	 */
	@NotNull
	String getSchema() {
		return this.schema;
	}


	/**
	 * Gibt die ID des Benutzers aus dem SVWS-Datenbank-Schema zurück, dem dieser Manager zugeordnet ist.
	 *
	 * @return die Benutzer-ID
	 */
	long getIdUser() {
		return this.idUser;
	}


	/**
	 * Liefert den Job zu der übergebenen Job-ID.
	 *
	 * @param idJob   die Job-ID
	 *
	 * @return Status oder null, wenn nicht gefunden
	 */
	public EmailJob getJob(final long idJob) {
		removeExpiredJobs();
		return mapJobs.get(idJob);
	}


	/**
	 * Bricht einen Job ab. Wenn der Job noch in der Warteschlange ist, wird er entfernt und als CANCELED markiert.
	 * Läuft der Job bereits, wird er zum nächstmöglichen Zeitpunkt beendet.
	 *
	 * @param idJob   die Job-ID
	 *
	 * @return true, wenn der Job gefunden wurde (unabhängig davon, ob er sofort gestoppt werden konnte), und ansonsten false
	 */
	public boolean cancelJob(final long idJob) {
		removeExpiredJobs();
		final EmailJob job = mapJobs.get(idJob);
		if (job == null) {
			return false;
		}

		synchronized (queueTransitionLock) {
			job.requestCancellation();
			if (job.getStatus() != EmailJobStatus.QUEUED) {
				// Job läuft bereits – processJob() wertet cancellationRequested aus.
				return true;
			}
			if (jobs.remove(job)) {
				job.addLogError("- ABBRUCH: Job %d wurde vor dem Start abgebrochen.".formatted(job.getId()));
				// Status noch unter dem Lock setzen.
				job.setStatus(EmailJobStatus.CANCELED);
				// Der Worker sieht diesen Job nicht mehr und kann die Anhänge daher nicht freigeben.
				job.releaseAttachments();
			}
			// Wenn remove() false liefert, hat der Worker den Job bereits entnommen,
			// aber den Status noch nicht auf SENDING gesetzt. cancellationRequested ist
			// jedoch bereits true, sodass processJob() den Job als CANCELED behandelt.
		}
		return true;
	}


	/**
	 * Prüft, ob ein Job mit diesem Status beendet ist und nicht mehr verändert wird.
	 *
	 * @param status   der Status des Jobs
	 *
	 * @return true, wenn der Job beendet ist, und ansonsten false
	 */
	private static boolean isFinished(final @NotNull EmailJobStatus status) {
		return (status != EmailJobStatus.QUEUED) && (status != EmailJobStatus.SENDING);
	}


	/**
	 * Entfernt beendete Jobs, deren Haltezeit abgelaufen ist. Bis dahin kann der Benutzer noch Status und Log abfragen.
	 */
	private void removeExpiredJobs() {
		final long now = System.currentTimeMillis();
		mapJobs.values().removeIf(job -> isFinished(job.getStatus())
				&& ((now - job.getTimeLastChanged()) >= job.getContext().getTimeToKeepCompletedJobs()));
	}


	/**
	 * Die Methode des Worker-Threads des Job-Managers.
	 * Der Worker wartet immer nur begrenzt auf neue Jobs und räumt zwischendurch auf. Ein Job kann auch ohne den Worker enden, etwa durch einen
	 * Abbruch in der Warteschlange.
	 * Die Schleife endet, wenn {@link #shutdown()} aufgerufen wird: Entweder durch
	 * das Interrupt-Signal (beim Warten auf einen neuen Job)
	 * oder nach Abschluss des aktuell laufenden Jobs, da dieser über
	 * {@link EmailJob#hasCancellationRequest()} das Abbruch-Flag auswertet.
	 * {@link #processJob} fängt reguläre Fehler beim Versand bereits selbst ab und markiert den
	 * Job entsprechend als fehlgeschlagen. Sollte dennoch eine unerwartete Exception bis hierher durchschlagen,
	 * wurde der Job bereits aus der Warteschlange entnommen und kann nicht mehr regulär
	 * abgeschlossen werden. Die zugehörigen E-Mails gehen damit unwiederbringlich verloren.
	 * Damit die Ursache im Fehlerfall nachvollzogen werden kann, wird ein solcher Fehler geloggt,
	 * bevor der Worker mit dem nächsten Job fortfährt.
	 */
	private void run() {
		while (running) {
			EmailJob job = null;
			try {
				removeExpiredJobs();
				job = jobs.poll(CLEANUP_INTERVAL_MS, TimeUnit.MILLISECONDS);
				if (job != null) {
					processJob(job);
				}
			} catch (@SuppressWarnings("unused") final InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			} catch (final Exception e) {
				final String jobInfo = (job == null) ? "unbekannt" : String.valueOf(job.getId());
				final String meldung = "- FEHLER: Unerwarteter Fehler bei der Verarbeitung von Job " + jobInfo + ". "
						+ "Der Job konnte nicht abgeschlossen werden, die zugehörigen E-Mails gehen verloren: "
						+ ((e.getMessage() != null) ? e.getMessage() : e.getClass().getSimpleName());
				// Job-internes Fehler-Log (Paket-Konvention), damit der Fehler über den Job-Status abrufbar bleibt.
				if (job != null) {
					job.addLogError(meldung);
				}
				// Zusätzlich server-seitig inklusive Stacktrace loggen, damit die Ursache diagnostiziert werden kann.
				Logger.global().logLn(LogLevel.ERROR, "EmailJobManager (Schema: " + schema + ", Benutzer-ID: " + idUser + "): " + meldung);
				LogUtils.logStacktrace(Logger.global(), e);
			}
		}
	}


	/**
	 * Verarbeitet einen einzelnen Job aus der Warteschlange. Diese Methode wird vom Worker-Thread aufgerufen.
	 *
	 * <p>Zunächst wird unter dem {@code queueTransitionLock} atomar geprüft, ob ein Abbruch des Jobs
	 * angefordert wurde, bevor der Versand beginnt. Ist dies der Fall, wird der Job als {@code CANCELED}
	 * markiert und aus der Verarbeitung entfernt, ohne dass E-Mails versendet werden.
	 * Andernfalls wechselt der Status auf {@code SENDING} und der eigentliche Versand beginnt.</p>
	 *
	 * <p>Der Versandvorgang selbst läuft außerhalb des Locks, da er zeitintensiv sein kann und andere
	 * Threads – insbesondere {@link #cancelJob} – nicht blockiert werden sollen. Ein während des Versands
	 * eingehender Abbruch wird durch regelmäßige Prüfung des Abbruch-Flags in {@link #sendInternal}
	 * berücksichtigt und führt zum Auslösen einer {@link EmailJobCanceledException}.</p>
	 *
	 * <p>Nach dem Versand – unabhängig davon, ob er erfolgreich war, abgebrochen oder durch eine
	 * Exception unterbrochen wurde – wird der abschließende Status des Jobs in
	 * {@link #setStatusAfterProcessJob} gesetzt. Ein gesetztes Abbruch-Flag hat dabei immer Vorrang
	 * und führt zum Status {@code CANCELED}, auch wenn bereits E-Mails versendet wurden.</p>
	 *
	 * @param job   der zu verarbeitende E-Mail-Job
	 */
	private void processJob(final EmailJob job) {
		// Sichere den Übergang von QUEUED zu SENDING atomar ab, damit cancelJob() nicht gleichzeitig von QUEUED zu CANCELED wechseln kann.
		final boolean canceledBeforeStart;
		synchronized (queueTransitionLock) {
			canceledBeforeStart = job.hasCancellationRequest();
			if (canceledBeforeStart) {
				// Protokolliere den Abbruch. Dies greift nur, falls cancelJob() den Job nicht mehr aus der Queue entfernen konnte.
				if (job.getLogError().isEmpty()) {
					job.addLogError("- ABBRUCH: Job %d wurde vor dem Start abgebrochen.".formatted(job.getId()));
				}
				job.setStatus(EmailJobStatus.CANCELED);
				job.releaseAttachments();
			} else {
				// Status-Wechsel unter dem Lock, damit cancelJob() ihn korrekt sieht.
				job.setStatus(EmailJobStatus.SENDING);
			}
		}
		if (canceledBeforeStart) {
			return;
		}

		try {
			final boolean allSuccessful = this.sendAll(job);
			setStatusAfterProcessJob(job, allSuccessful);
		} catch (@SuppressWarnings("unused") final EmailJobCanceledException e) {
			setStatusAfterProcessJob(job, false);
		} catch (final Exception e) {
			job.addLogError("- FEHLER: Unerwarteter Fehler während des Versands von Job " + job.getId() + ": "
					+ (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName()));
			setStatusAfterProcessJob(job, false);
		}
	}

	/**
	 * Setzt den abschließenden Status des Jobs nach dem Versandvorgang.
	 *
	 * <p>Der endgültige Status wird unter dem {@code queueTransitionLock} bestimmt und gesetzt,
	 * um sicherzustellen, dass ein parallel laufendes {@link #cancelJob} das Abbruch-Flag nicht
	 * zwischen dem Status-Check und dem Setzen des endgültigen Status verändern kann.</p>
	 *
	 * <p>Ein gesetztes Abbruch-Flag hat dabei immer Vorrang vor dem Versandergebnis: Auch wenn
	 * alle E-Mails bereits erfolgreich versendet wurden, wird der Job als {@code CANCELED} markiert,
	 * sobald {@link #cancelJob} das Flag vor Abschluss dieser Methode gesetzt hat.</p>
	 *
	 * @param job            der abgeschlossene Job
	 * @param allSuccessful  true, wenn alle E-Mails ohne Fehler versendet wurden
	 */
	private void setStatusAfterProcessJob(final EmailJob job, final boolean allSuccessful) {
		synchronized (queueTransitionLock) {
			final EmailJobStatus finalStatus;
			// Ein Abbruch-Request hat immer Vorrang, unabhängig davon, wann er gesetzt wurde.
			if (job.hasCancellationRequest()) {
				job.addLogError("- ABBRUCH: Job %d wurde während des Versands abgebrochen.".formatted(job.getId()));
				finalStatus = EmailJobStatus.CANCELED;
			} else if (allSuccessful && job.getLogError().isEmpty() && job.getLogSkipped().isEmpty()) {
				finalStatus = EmailJobStatus.COMPLETED_SUCCESSFULLY;
			} else {
				finalStatus = (job.getEmailsSent() > 0) ? EmailJobStatus.COMPLETED_WITH_ERRORS : EmailJobStatus.FAILED;
			}
			// Status wird noch unter dem Lock gesetzt, damit kein anderer Thread einen Zwischenstatus sieht.
			job.setStatus(finalStatus);
		}
		job.releaseAttachments();
	}


	/**
	 * Diese Methode dient der Einhaltung des Limits für die Anzahl der E-Mails pro Minute.
	 * Sie wartet blockierend, bis wieder genügend Zeit für das Versenden einer weiteren E-Mail vergangen ist.
	 * Die Rate-Limit-Werte werden dabei aus dem Kontext des übergebenen Jobs gelesen, während die Zeitstempel
	 * der zuletzt versendeten Mails jobübergreifend im Manager verwaltet werden (siehe {@link #sendTimestamps}).
	 *
	 * @param job   der Job, dessen Kontext die Rate-Limit-Werte für diesen Versand vorgibt
	 *
	 * @throws EmailJobCanceledException   falls der wartende Thread durch Thread.interrupt() unterbrochen wurde (siehe shutdown-Methode)
	 */
	private void awaitRateLimit(final @NotNull EmailJob job) {
		// Die Größe des Zeitfensters für den Versand: Anzahl an Millisekunden für eine Minute
		final long zeitfenster = job.getContext().getRateLimitTimeframeMs();

		rateLimitLock.lock();
		try {

			// Eine Endlosschleife, welche entweder das Senden durch "return" erlaubt oder blockierend auf ein freies Zeitfenster wartet.
			while (true) {
				final long now = System.currentTimeMillis();

				// Entferne alle Einträge in der Queue der Zeitstempel, die aus dem Zeitfenster von einer Minute herausfallen
				while (!sendTimestamps.isEmpty() && ((now - sendTimestamps.peekFirst()) >= zeitfenster)) {
					sendTimestamps.pollFirst();
				}

				// Prüfe, ob die maximale Rate das Senden einer E-Mail erlaubt
				final Long oldest = sendTimestamps.peekFirst();
				if ((oldest == null) || (sendTimestamps.size() < job.getContext().getMaxEmailsPerMinute())) {
					// Füge den Zeitstempel in die Queue ein, wecken dann ggf. noch wartende andere Threads und verlasse die Methode zum Senden ...
					sendTimestamps.addLast(now);
					rateLimitChanged.signalAll();
					return;
				}

				// Kein Versenden erlaubt. Bestimme daher die Wartezeit anhand des ältesten Eintrags, warte aber mindestens 50ms, um ein Busy-Wait zu vermeiden.
				final long wartezeit = Math.max(50, zeitfenster - (now - oldest));
				try {
					// Warte so lange, bis die Wartezeit abgelaufen ist oder ein anderer Thread diesen Thread wieder aufweckt ...
					rateLimitChanged.await(wartezeit, TimeUnit.MILLISECONDS);
					// ... und dann der nächste Schleifendurchlauf zum Prüfen, ob eine Berechtigung zum Senden besteht
				} catch (@SuppressWarnings("unused") final InterruptedException ie) {
					// Reagiere auf den Abbruch des Jobs von der shutdown-Methode und signalisiere dies an die Threads mithilfe einer Exception.
					Thread.currentThread().interrupt();
					throw new EmailJobCanceledException();
				}
			}
		} finally {
			rateLimitLock.unlock();
		}
	}


	/**
	 * Sendet die E-Mails des übergebenen Jobs. Der Versand findet an alle Empfänger des Jobs statt.
	 * Vor jeder einzelnen E-Mail wird geprüft, ob das Versandlimit für E-Mails erreicht wurde, und der Versand wird ggf. für einen Zeitraum ausgesetzt.
	 * Nach einem Fehler, der alle Empfänger betrifft, endet der Versand. Sonst wartete der Job bei jedem Empfänger erneut auf einen hängenden
	 * Mailserver oder versuchte eine abgelehnte Anmeldung so oft, bis der Mailserver das Konto sperrt.
	 *
	 * @param job   der E-Mail-Job
	 *
	 * @return true, wenn alle E-Mails erfolgreich versendet wurden, andernfalls false
	 */
	private boolean sendAll(final EmailJob job) {
		boolean allSuccessful = true;
		final List<EmailJobRecipient> recipients = job.getRecipients();
		for (int i = 0; i < recipients.size(); i++) {
			try {
				if (!sendToRecipient(job, recipients.get(i))) {
					allSuccessful = false;
				}
			} catch (@SuppressWarnings("unused") final FehlerFuerAlleEmpfaenger e) {
				job.addLogError("- ABBRUCH: Der Versand endet nach diesem Fehler. %d weitere Empfänger erhalten keine E-Mail."
						.formatted(recipients.size() - i - 1));
				return false;
			}
		}
		return allSuccessful;
	}


	/**
	 * Prüft, ob ein Fehler beim Versand alle Empfänger betrifft. Lehnt der Mailserver einen Empfänger oder die Nachricht ab, betrifft das
	 * nur diese E-Mail. Scheitern dagegen Verbindung, Verschlüsselung oder Anmeldung, scheitert auch jede weitere E-Mail.
	 *
	 * @param e   der Fehler beim Versand
	 *
	 * @return true, wenn der Fehler alle Empfänger betrifft, und ansonsten false
	 */
	private static boolean betrifftAlleEmpfaenger(final @NotNull Exception e) {
		return (e instanceof MessagingException) && !(e instanceof SendFailedException) && !(e instanceof AddressException);
	}


	/**
	 * Sendet die E-Mails des übergebenen Jobs und des übergebenen Empfängers. Es wird vor dem Versand geprüft,
	 * das Versandlimit für E-Mails erreicht wurde und der Versand wird ggf. für einen Zeitraum ausgesetzt.
	 *
	 * @param job         der E-Mail-Job
	 * @param recipient   der Empfänger der E-Mail mit den zugehörigen Anhängen
	 *
	 * @return true, wenn alle E-Mails erfolgreich versendet wurden, andernfalls false
	 */
	private boolean sendToRecipient(final @NotNull EmailJob job, final @NotNull EmailJobRecipient recipient) {
		// Prüfe, ob E-Mails ohne Anhänge herausgefiltert werden sollen und der Empfänger keine Anhänge erhält.
		if (recipient.attachments.isEmpty() && job.getContext().isFilterMailsWithoutAttachments()) {
			job.addLogSkipped("- Für Empfänger %s konnten keine Anhänge für den Versand ermittelt werden. Er wird beim Versand übersprungen."
					.formatted(recipient.email));
			return false;
		}

		// Prüfe, ob die Anhänge direkt versendet werden können, da kein Größen-Limit gesetzt ist
		if (job.getContext().getMaxAttachmentSize() <= 0) {
			return sendInternal(job, recipient, recipient.attachments);
		}

		// Wenn ein Größen-Limit gesetzt ist, unterteile die Anhänge, bilde geeignete Pakete ...
		final List<List<Integer>> pakete = groupAttachments(job, recipient.attachments, recipient.email);
		if (pakete.isEmpty()) {
			if (job.getContext().isFilterMailsWithoutAttachments()) {
				job.addLogSkipped("- Für Empfänger %s konnten keine Anhänge für den Versand ermittelt werden. Er wird beim Versand übersprungen."
						.formatted(recipient.email));
				return false;
			}
			return sendInternal(job, recipient, new ArrayList<>());
		}

		// ... und versende diese in einzelnen E-Mails
		boolean allSuccessful = true;
		for (final List<Integer> paket : pakete) {
			final List<EmailJobAttachment> paketData = new ArrayList<>(paket.size());
			for (final Integer index : paket) {
				paketData.add(recipient.attachments.get(index));
			}
			if (!sendInternal(job, recipient, paketData)) {
				allSuccessful = false;
			}
		}
		return allSuccessful;
	}


	/**
	 * Versendet eine einzelne E-Mail des Jobs mit den zugehörigen Anhängen und behandelt eventuell auftretende Fehler.
	 * Außerdem wird überprüft, ob das Rate-Limit für den Versand bereits erreicht wurde.
	 *
	 * @param job           der E-Mail-Job
	 * @param recipient     die Empfänger-E-Mail-Adresse
	 * @param attachments   eine Liste mit den E-Mail-Anhängen
	 *
	 * @return true, wenn die E-Mail erfolgreich versendet wurde, andernfalls false.
	 */
	private boolean sendInternal(final EmailJob job, final EmailJobRecipient recipient, final @NotNull List<EmailJobAttachment> attachments) {
		// Prüfe das Rate-Limit für den Versand
		awaitRateLimit(job);
		if (job.hasCancellationRequest()) {
			throw new EmailJobCanceledException();
		}

		// Versuche die nächste E-Mail an den übergebenen Empfänger und den übergebenen Anhängen zu versenden
		try {
			final MailSmtpSession session = job.getContext().getSmtpSession();
			// Während die SMTP-Sitzung erstellt wurde, kann der Job abgebrochen worden sein. Prüfe daher hier erneut auf einen Abbruch.
			if (job.hasCancellationRequest()) {
				throw new EmailJobCanceledException();
			}
			// Wenn bis hier kein Abbruch erfolgt ist, kann der Versand einer E-Mail nicht mehr verhindert werden.
			session.sendTextMessageWithAttachments(job.getFrom(), recipient.email, job.getSubject(), job.getBody(), attachments);
		} catch (final EmailJobCanceledException e) {
			// Wenn der Versand abgebrochen wurde, wird die Exception weitergeleitet.
			throw e;
		} catch (final Exception e) {
			job.addLogError("- Fehler beim Versand an Empfänger " + recipient.email + ": " + e.getMessage());
			if (betrifftAlleEmpfaenger(e)) {
				throw new FehlerFuerAlleEmpfaenger();
			}
			return false;
		}
		job.notifyEmailSent();
		return true;
	}


	/**
	 * Beendet den Versand eines Jobs nach einem Fehler, der alle Empfänger betrifft. Den Fehler selbst hat bereits das Log des Jobs.
	 */
	private static final class FehlerFuerAlleEmpfaenger extends RuntimeException {

		private static final long serialVersionUID = 1L;

	}


	/**
	 * Bildet für eine Liste von Anhängen Gruppen, welche eine im Job definierte maximale Größe für die Anhänge der E-Mails einhalten.
	 * Es kann dabei im Job definiert werden, ob auch einzelne Anhänge diese maximale Größe einhalten müssen.
	 * Im letzteren Fall werden dann zu große Anhänge verworfen und als Fehler geloggt.
	 *
	 * @param job           der E-Mail-Job
	 * @param attachments   die Liste mit den einzelnen Datei-Anhängen zur Gruppierung.
	 * @param recipient     die E-Mail-Adresse des Empfängers, welche ggf. im Logging bei Fehlern angegeben wird.
	 *
	 * @return eine Liste von Listen mit Indizes, welche jeweils Gruppen von Anhängen darstellen, die gemeinsam
	 *         versendet werden. Die Indizes beziehen sich dabei auf die übergebene Liste von Datei-Anhängen.
	 */
	private @NotNull List<List<Integer>> groupAttachments(final @NotNull EmailJob job, final @NotNull List<EmailJobAttachment> attachments,
			final @NotNull String recipient) {
		// Erstellt die Ergebnis-Liste für die Gruppen
		final List<List<Integer>> groups = new ArrayList<>();
		if (attachments.isEmpty() || (job.getContext().getMaxAttachmentSize() <= 0)) {
			return groups;
		}

		// Lese die maximale Anhangsgröße aus der Job-Definition aus
		final long maxSize = job.getContext().getMaxAttachmentSize();

		// Bestimme die Größen der Anhänge und speichere diese in einem Array für den schnellen Zugriff auf die Größen
		final int[] attachmentSizes = new int[attachments.size()];
		for (int i = 0; i < attachments.size(); i++) {
			attachmentSizes[i] = Optional.of(attachments.get(i)).map(d -> d.data.length).orElse(0);
		}

		// Erstelle für die spätere Gruppenbildung zunächst eine Liste mit den Indizes, welche anhand der Größe der Anhänge sortiert ist
		final List<Integer> sortedAttachmentIndizes =
				IntStream.range(0, attachments.size()).boxed().sorted((a, b) -> Integer.compare(attachmentSizes[b], attachmentSizes[a])).toList();

		// Befülle die Gruppen einzeln mit den Anhängen, versuche dabei immer mit den nächstgrößeren Anhängen die zuerst erzeugten Gruppen weiter zu befüllen
		final List<Long> groupSizes = new ArrayList<>();
		for (final int index : sortedAttachmentIndizes) {
			final long size = attachmentSizes[index];

			if (size > maxSize) {
				// Fall 1: Die Größe überschreitet das Limit und dies ist laut Job-Konfiguration untersagt
				if (job.getContext().isForceMaxAttachmentSize()) {
					// Die maximale Paketgröße darf nicht überschritten werden, verwerfe daher den Anhang und logge das Problem.
					job.addLogSkipped(
							("- Für Empfänger %s wurde ein Anhang nicht versendet. Grund: Der Anhang überschreitet die maximale Größe für E-Mail-Anhänge.")
									.formatted(recipient));
				} else {
					// Fall 2: Die Größe überschreitet das Limit und dies ist laut Job-Konfiguration erlaubt. Erzeuge daher ein Einzelpaket am Ende der Ergebnisliste
					final List<Integer> einzelanhang = new ArrayList<>(1);
					einzelanhang.add(index);
					groups.add(einzelanhang);
					groupSizes.add(size);
				}
			} else {
				// Fall 3: Versuche, den aktuellen Anhang in einer bestehenden Gruppe unterzubringen.
				final OptionalInt indexGroup = IntStream.range(0, groups.size()).filter(i -> ((groupSizes.get(i) + size) <= maxSize)).findFirst();
				if (indexGroup.isPresent()) {
					final int i = indexGroup.getAsInt();
					groups.get(i).add(index);
					groupSizes.set(i, groupSizes.get(i) + size);
				} else {
					// Fall 4: Erstelle eine neue Gruppe am Ende der Ergebnisliste, wenn zuvor kein Platz für den Anhang gefunden wurde
					final List<Integer> group = new ArrayList<>();
					group.add(index);
					groups.add(group);
					groupSizes.add(size);
				}
			}
		}
		return groups;
	}

}
