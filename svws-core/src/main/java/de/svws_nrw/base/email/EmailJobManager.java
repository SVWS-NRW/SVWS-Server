package de.svws_nrw.base.email;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

/**
 * Dieser Manager verwaltet Jobs zum Versenden von Emails mithilfe eines Threads pro Datenbankschema und Benutzer.
 * Diese werden über den Thread nebenläufig versendet und der Status und ein Log werden dabei erstellt. Zur Steuerung,
 * dass nicht zu viele Emails versendet werden kann eine Versandbegrenzung in Form eine Rate-Limit eingestellt werden.
 */
public final class EmailJobManager {

	/** Der Kontext mit Benutzerinformationen, der SMTP-Session und Manager-Konfigurationseinstellungen*/
	private final @NotNull EmailJobManagerContext context;

	/** Eine Hilfsklasse, welche die Verwaltung der abgeschlossen Jobs übernimmt */
	private final @NotNull EmailJobManagerCompletedJobs completedJobs;

	/** Eine Queue zur Verwaltung der Jobs, welche den Status QUEUED haben. */
	private final BlockingQueue<EmailJob> jobs = new LinkedBlockingQueue<>();

	/** Eine Hash-Map zum Speichern aller Jobs anhand ihrer ID. Hier sind alle Jobs unabhängig vom Status enthalten. */
	private final ConcurrentHashMap<Long, EmailJob> mapJobs = new ConcurrentHashMap<>();

	/** Eine Deque, um die Sendezeitpunkte für die letzten 60 Sekunden zu Speichern, um damit die Senderate von Emails pro Minute zu begrenzen. */
	private final ArrayDeque<Long> sendTimestamps = new ArrayDeque<>();

	/** Der Worker-Thread, der für diesen Job-Manager verantwortlich ist. */
	private final Thread thread;

	/** Der interne Status, ob dieser Job-Manager aktuell am laufen ist. */
	private volatile boolean running = true;

	/** Die nächste freie Job-ID zur Vergabe an den nächsten Job */
	private final AtomicLong nextJobId = new AtomicLong(1);


	/**
	 * Erstellt einen neuen Email-Job-Manager zum Versenden von Emails.
	 * Dieser verwendet einen Worker-Thread, um Emails asynchron zu versenden.
	 *
	 * @param context   der Kontext für den Manager mit Benutzerinformationen, der SMTP-Session
	 *                  und Manager-Konfigurationseinstellungen
	 */
	EmailJobManager(final @NotNull EmailJobManagerContext context) {
		this.context = context;
		this.completedJobs = new EmailJobManagerCompletedJobs(this);
		final @NotNull String threadName = "EmailJobManager_" + context.getDBSchema() + "_" + context.getUserId();
		this.thread = Thread.ofVirtual().name(threadName).start(this::run);
	}


	/**
	 * Fügt einen neuen Job zum Versenden von Emails zu der Warteschlange des Managers hinzu.
	 *
	 * @param job   der Email-Job
	 *
	 * @return die ID des neuen Jobs oder -1, wenn der Job bereits eine ID hatte und erneut versucht wurde diesen Job hinzuzufügen
	 */
	public long enqueue(final @NotNull EmailJob job) {
		// Setzt die Job-ID und füge den Job hinzu
		final boolean isNewJob = job.setId(nextJobId.getAndIncrement());
		if (!isNewJob)
			return -1;
		mapJobs.put(job.getId(), job);
		jobs.add(job);
		return job.getId();
	}


	/**
	 * Stoppt den Thread zum Versenden von Emails.
	 *
	 * Hierzu wird das interne Flag running auf false gesetzt und der Worker-Thread des Managers wird unterbrochen.
	 * Außerdem wird die Liste der abgeschlossenen Jobs sofort geleert.
	 * Diese Methode sollte aufgerufen werden, bevor die Anwendung heruntergefahren wird, um die verbleibenden
	 * Manager-Aktivitäten abzuschließen.
	 */
	public void shutdown() {
		running = false;
		thread.interrupt();
		completedJobs.removeAll();
	}


	/**
	 * Gibt den Kontext des Managers mit den Benutzerinformationen, der SMTP-Session
	 * und den Konfigurationseinstellungen zurück.
	 *
	 * @return der Kontext
	 */
	public @NotNull EmailJobManagerContext getContext() {
		return this.context;
	}


	/**
	 * Liefert den Job zu der übergebenen Job-ID.
	 *
	 * @param idJob   die Job-ID
	 *
	 * @return Status oder null, wenn nicht gefunden
	 */
	public EmailJob getJob(final long idJob) {
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
		// Prüfe, ob die ID gültig ist
		final EmailJob job = mapJobs.get(idJob);
		if (job == null)
			return false;

		// Wenn ja, dann breche den Job ab
		job.requestCancellation();
		if (job.getStatus() != EmailJobStatus.QUEUED)
			return true;

		// Entferne den Job auch aus der Queue und markiere ihn auch als abgebrochen
		if (jobs.remove(job)) {
			job.logError.add("- ABBRUCH: Job " + idJob + " wurde vor dem Start abgebrochen.");
			completedJobs.add(job, EmailJobStatus.CANCELED);
		} else {
			job.logError.add("- FEHLER: Job " + idJob + " konnte vor dem Start nicht abgebrochen werden.");
			completedJobs.add(job, EmailJobStatus.FAILED);
		}
		return true;
	}


	/**
	 * Entfernt den Job mit der angegebenen ID aus dem Job-Manager. Diese Methode dient dem
	 * endgültigen Entfernen eines Jobs durch den {@link EmailJobManagerCompletedJobs}
	 *
	 * @param idJob   die ID des zu entfernenden Jobs
	 */
	void removeCompletedJob(final long idJob) {
		mapJobs.remove(idJob);
	}


	/**
	 * Die Methode des Worker-Threads des Job-Managers
	 */
	private void run() {
		while (running) {
			try {
				processJob(jobs.take());
			} catch (@SuppressWarnings("unused") final InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			} catch (@SuppressWarnings("unused") final Exception ignore) {
				// Der Worker läuft weiter, ohne Fehler zu verursachen.
			}
		}
	}


	/**
	 * Bearbeitet den übergebenen Job und startet das Versenden der Emails.
	 *
	 * @param job   der abzuarbeitende Job des Managers
	 */
	private void processJob(final EmailJob job) {
		// Wenn der Job als abgebrochen markiert wurde, dann kann er auch nicht weiter bearbeitet werden und der Status wird aus CANCELED gesetzt
		if (job.hasCancellationRequest()) {
			completedJobs.add(job, EmailJobStatus.CANCELED);
			return;
		}

		// Setze den Status auf Running ...
		job.setStatus(EmailJobStatus.SENDING);
		try {
			// ... und versende alle Mails des Jobs
			final boolean allSuccessful = this.sendAll(job);

			// Wenn der Job nicht vorher abgebrochen wurde, prüfe, ob alle Mails erfolgreich versandt wurden.
			if (!job.hasCancellationRequest()) {
				if (allSuccessful && job.logError.isEmpty() && job.logSkipped.isEmpty()) {
					// Der Job ist nur erfolgreich, wenn alle Mails erfolgreich versendet wurden und kein Fehler aufgetreten ist.
					completedJobs.add(job, EmailJobStatus.COMPLETED_SUCCESSFULLY);
				} else {
					if (job.getEmailsSent() > 0)
						completedJobs.add(job, EmailJobStatus.COMPLETED_WITH_ERRORS);
					else
						completedJobs.add(job, EmailJobStatus.FAILED);
				}
			}
		} catch (@SuppressWarnings("unused") final EmailJobCanceledException e) {
			// Diese Exception wird in dem Fall aufgerufen, dass der Job unterbrochen wurde -> Status CANCELED
			job.logError.add("- ABBRUCH: Job " + job.getId() + " wurde während des Versands abgebrochen.");
			completedJobs.add(job, EmailJobStatus.CANCELED);
		} catch (final Exception e) {
			// Bei einem unerwarteten Fehler wird der Status des Jobs auf FAILED gesetzt
			job.logError.add("- FEHLER: Unerwarteter Fehler während des Versands von Job " + job.getId() + ": "
					+ (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName()));
			completedJobs.add(job, EmailJobStatus.FAILED);
		}
	}


	/**
	 * Diese Methode dient der Einhaltung des Limit für die Anzahl der Emails pro Minute.
	 * Sie wartet blockierend, bis wieder genüg Zeit für das Versenden einer weiteren Email vergangen ist.
	 *
	 * @throws EmailJobCanceledException   falls der wartende Thread durch Thread.interrupt() unterbrochen wurde (siehe shutdown-Methode)
	 */
	private void awaitRateLimit() {
		// Die Größe des Zeitfensters für den Versand: Anzahl an Millisekunden für eine Minute
		final long zeitfenster = context.getRateLimitTimeframeMs();

		// Synchronisiert den Zugriff auf der Deque bei dem Zugriff durch mehrere Threads. wait und notifyAll benötigen dieselbe Monitor-Instanz.
		synchronized (sendTimestamps) {

			// Eine Endlosschleife, welche entweder das Senden durch "return" erlaubt oder blockierend auf ein freies Zeitfenster wartet.
			while (true) {
				final long now = System.currentTimeMillis();

				// Entferne alle Einträge in der Queue der Zeitstempel, die aus dem Zeitfenster von einer Minute herausfallen
				while (!sendTimestamps.isEmpty() && ((now - sendTimestamps.peekFirst()) >= zeitfenster))
					sendTimestamps.pollFirst();

				// Prüfe, ob die maximale Rate das Senden eine Email erlaubt
				final Long oldest = sendTimestamps.peekFirst();
				if ((oldest == null) || (sendTimestamps.size() < context.getMaxEmailsPerMinute())) {
					// Füge den Zeitstempel in die Queue ein, wecken dann ggf. noch wartende andere Threads und verlasse die Methode zum Senden...
					sendTimestamps.addLast(now);
					sendTimestamps.notifyAll();
					return;
				}

				// Kein Versenden erlaubt, bestimme die Wartezeit anhand des ältesten Eintrags, warte aber mindestens 50ms um ein Busy-Wait zu vermeiden
				final long wartezeit = Math.max(50, zeitfenster - (now - oldest));
				try {
					// Warte solange, bis die Wartezeit abgelaufen ist oder ein notifyAll durch einen anderen Thread diesen Thread wieder aufweckt ...
					sendTimestamps.wait(wartezeit);
					// ... und dann der nächste Schleifendurchlauf zum Prüfen, ob eine Berechtigung zum Senden besteht
				} catch (@SuppressWarnings("unused") final InterruptedException ie) {
					// Reagiere auf den Abbruch des Jobs von der shutdown-Methode und signalisiere dies an die Threads mithilfe eine Exception
					Thread.currentThread().interrupt();
					throw new EmailJobCanceledException();
				}
			}
		}
	}


	/**
	 * Sendet die E-Mails des übergebenen Jobs. Der Versand findet an alle Empfänger des Jobs statt.
	 * Vor jeder einzelnen Email wird geprüft, ob das Versandlimit für Emails erreicht wurde und der Versand wird
	 * ggf. für einen Zeitraum ausgesetzt.
	 *
	 * @param job   der Email-Job
	 *
	 * @return true, wenn alle E-Mails erfolgreich versendet wurden, andernfalls false
	 */
	private boolean sendAll(final EmailJob job) {
		boolean allSuccessful = true;
		for (final EmailJobRecipient recipient : job.getRecipients()) {
			if (!sendToRecipient(job, recipient)) {
				allSuccessful = false;
			}
		}
		return allSuccessful;
	}


	/**
	 * Sendet die E-Mails des übergebenen Jobs und des übergebenen Empfängers. Es wird vor dem Versand geprüft,
	 * das Versandlimit für Emails erreicht wurde und der Versand wird ggf. für einen Zeitraum ausgesetzt.
	 *
	 * @param job         der Email-Job
	 * @param recipient   der Empfänger der Email mit den zugehörigen Anhängen
	 *
	 * @return true, wenn alle E-Mails erfolgreich versendet wurden, andernfalls false
	 */
	private boolean sendToRecipient(final @NotNull EmailJob job, final @NotNull EmailJobRecipient recipient) {
		// Prüfe, ob E-Mails ohne Anhänge herausgefiltert werden sollen und der Empfänger keine Anhänge erhält.
		if (recipient.attachments.isEmpty() && context.isFilterMailsWithoutAttachments()) {
			job.logSkipped.add("- Für Empfänger %s konnten keine versendbaren Anhänge ermittelt werden. Er wird beim Versand übersprungen."
					.formatted(recipient.email));
			return false;
		}

		// Prüfe, ob die Anhänge direkt versendet werden können, da kein Größen-Limit gesetzt ist
		if (context.getMaxAttachmentSize() <= 0) {
			return sendInternal(job, recipient, recipient.attachments);
		}

		// Wenn ein Größen-Limit gesetzt ist, dann unterteile die Anhänge, bilde geeignete Pakete ...
		final List<List<Integer>> pakete = groupAttachments(job, recipient.attachments, recipient.email);
		if (pakete.isEmpty()) {
			if (context.isFilterMailsWithoutAttachments()) {
				job.logSkipped.add("- Für Empfänger %s konnten keine versendbaren Anhänge ermittelt werden. Er wird beim Versand übersprungen."
						.formatted(recipient.email));
				return false;
			} else {
				return sendInternal(job, recipient, new ArrayList<>());
			}
		}

		// ... und versende diese in einzelnen Emails
		boolean allSuccessful = true;
		for (final List<Integer> paket : pakete) {
			final List<EmailJobAttachment> paketData = new ArrayList<>(paket.size());
			for (final Integer index : paket)
				paketData.add(recipient.attachments.get(index));
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
	 * @param job           der Email-Job
	 * @param recipient     die Empfänger-Email-Adresse
	 * @param attachments   eine Liste mit den Email-Anhängen
	 *
	 * @return true, wenn die E-Mail erfolgreich versendet wurde, andernfalls false.
	 */
	private boolean sendInternal(final EmailJob job, final EmailJobRecipient recipient, final @NotNull List<EmailJobAttachment> attachments) {
		// Prüfe das Rate-Limit für den Versand
		awaitRateLimit();
		if (job.hasCancellationRequest())
			throw new EmailJobCanceledException();

		// Versuche die nächste Email an den übergebenen Empfänger und den übergebenen Anhängen zu versenden
		try {
			context.getSmtpSession().sendTextMessageWithAttachments(job.getFrom(), recipient.email, job.getSubject(), job.getBody(), attachments);
		} catch (final Exception e) {
			job.logError.add("- Fehler beim Versand an Empfänger " + recipient + ": " + e.getMessage());
			return false;
		}
		job.notifyEmailSent();
		return true;
	}


	/**
	 * Bildet für eine Liste von Anhängen Gruppen, welche eine im Job definierte maximale Größe für die Anhänge der Emails einhalten.
	 * Es kann dabei im Job definiert werden, ob auch einzelne Anhänge diese maximale Größe einhalten müssen.
	 * Im letzteren Fall werden dann zu große Anhänge verworfen und als Fehler geloggt.
	 *
	 * @param job           der Email-Job
	 * @param attachments   die Liste mit den einzelnen Datei-Anhängen zur Gruppierung.
	 * @param recipient     die E-Mail-Adresse des Empfängers, welche ggf. im Logging beim Fehlern angegeben wird.
	 *
	 * @return eine Liste von Listen mit Indizes, welche jeweils Gruppen von Anhängen darstellen, die gemeinsam
	 *         versendet werden. Die Indizes beziehen sich dabei auf die übergebene Liste von Datei-Anhängen.
	 */
	private @NotNull List<List<Integer>> groupAttachments(final @NotNull EmailJob job, final @NotNull List<EmailJobAttachment> attachments,
			final @NotNull String recipient) {
		// Erstellt die Ergebnis-Liste für die Gruppen
		final List<List<Integer>> groups = new ArrayList<>();
		if (attachments.isEmpty() || (context.getMaxAttachmentSize() <= 0))
			return groups;

		// Lese die maximale Anhangsgröße aus der Job-Definition aus
		final long maxSize = context.getMaxAttachmentSize();

		// Bestimme die Größen der Anhänge und speichere diese in einem Array für den schnellen Zugriff auf die Größen
		final int[] attachmentSizes = new int[attachments.size()];
		for (int i = 0; i < attachments.size(); i++)
			attachmentSizes[i] = Optional.of(attachments.get(i)).map(d -> d.data.length).orElse(0);

		// Erstelle für die spätere Gruppenbildung zunächst eine Liste mit den Indizes, welche anhand der Größe der Anhänge sortiert ist
		final List<Integer> sortedAttachmentIndizes =
				IntStream.range(0, attachments.size()).boxed().sorted((a, b) -> Integer.compare(attachmentSizes[b], attachmentSizes[a])).toList();

		// Befülle die Gruppen einzeln mit den Anhängen, versuche dabei immer mit den nächstgrößeren Anhängen die zuerst erzeugten Gruppen weiter zu befüllen
		final List<Long> groupSizes = new ArrayList<>();
		for (final int index : sortedAttachmentIndizes) {
			final long size = attachmentSizes[index];

			// Fall 1: Die Größe überschreitet das Limit und dies ist laut Job-Konfiguration untersagt
			if ((size > maxSize) && (context.isForceMaxAttachmentSize())) {
				// Die maximale Paketgröße darf nicht überschritten werden, verwerfe daher den Anhang und logge das Problem.
				job.logSkipped
						.add(("- Für Empfänger %s wurde ein Anhang nicht versendet. Grund: Der Anhang überschreitet die maximale Größe für E-Mail-Anhänge.")
								.formatted(recipient));
				continue;
			}

			// Fall 2: Die Größe überschreitet das Limit und dies ist laut Job-Konfiguration erlaubt -> Erzeuge ein Einzelpaket am Ende der Ergebnisliste
			if (size > maxSize) {
				final List<Integer> einzelanhang = new ArrayList<>(1);
				einzelanhang.add(index);
				groups.add(einzelanhang);
				groupSizes.add(size);
				continue;
			}

			// Fall 3: Versuche den aktuellen Anhang in einer bestehenden Gruppe unterzubringen
			final OptionalInt indexGroup = IntStream.range(0, groups.size()).filter(i -> ((groupSizes.get(i) + size) <= maxSize)).findFirst();
			if (indexGroup.isPresent()) {
				final int i = indexGroup.getAsInt();
				groups.get(i).add(index);
				groupSizes.set(i, groupSizes.get(i) + size);
				continue;
			}

			// Fall 4: Erstelle eine neue Gruppe am Ende der Ergebnisliste, wenn zuvor kein Platz für den Anhang gefunden wurde
			final List<Integer> group = new ArrayList<>();
			group.add(index);
			groups.add(group);
			groupSizes.add(size);
		}
		return groups;
	}

}
