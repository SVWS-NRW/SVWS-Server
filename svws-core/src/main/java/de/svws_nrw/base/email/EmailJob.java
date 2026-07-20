package de.svws_nrw.base.email;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beinhaltet einen Job zum Versenden von E-Mails und beschreibt einen Versandvorgang. Dieser
 * unterstützt das Versenden von Mails an einzelne Empfänger mit unterschiedlichen Anhängen.
 */
public final class EmailJob {

	/** Die eindeutige ID des Jobs, sofern diese schon von einem {@link EmailJobManager} zugeteilt wurde. */
	private volatile Long id = null;

	/** Die E-Mail-Adresse des Senders (from) */
	private final @NotNull String from;

	/** Der Kontext mit den versandbezogenen Einstellungen, mit denen dieser Job gestartet wurde. */
	private final @NotNull EmailJobContext context;

	/** Der Betreff der E-Mail */
	private @NotNull String subject = "";

	/** Der Body der E-Mail */
	private @NotNull String body = "";

	/** Eine Liste mit den Adressen der Empfänger und den ihnen zugeordneten Anhängen. */
	private final @NotNull List<EmailJobRecipient> recipients = new ArrayList<>();

	/** Aktueller Status des Jobs. */
	private volatile EmailJobStatus status = EmailJobStatus.QUEUED;

	/** Gibt an, ob der Job zum Abbrechen markiert wurde. */
	private volatile boolean cancellationRequested = false;

	/** Zeitstempel der letzten Statusänderung in Millisekunden */
	private volatile long timeLastChanged = System.currentTimeMillis();

	/** Die Anzahl der erfolgreich versendeten E-Mails. */
	private final AtomicInteger countSuccess = new AtomicInteger(0);

	/** Die Liste der Log-Informationen, wenn einzelne Empfänger des Jobs ausgelassen wurden. */
	private final @NotNull List<String> logSkipped = Collections.synchronizedList(new ArrayList<>());

	/** Die Liste mit dem Fehler-Log. */
	private final @NotNull List<String> logError = Collections.synchronizedList(new ArrayList<>());


	/**
	 * Erstellt einen neuen E-Mail-Job zum Versenden einer Mail von der übergebenen Adresse mit dem übergebenen
	 * Kontext für die versandbezogenen Einstellungen (u. a. die SMTP-Session).
	 *
	 * @param from      die E-Mail-Adresse des Senders
	 * @param context   der Kontext mit den versandbezogenen Einstellungen für diesen Job
	 */
	public EmailJob(final @NotNull String from, final @NotNull EmailJobContext context) {
		// @NotNull sichert nicht gegen die Übergabe von null. SonarQube denkt aber so und meldet bei Prüfung mittels "== null" immer
		// "java:S2589, Remove this expression which always evaluates to true/false.". Daher hier die Prüfung mittels Objects.requireNonNull.
		try {
			if (Objects.requireNonNull(from).isBlank()) {
				throw new IllegalArgumentException("Notwendiger Parameter Absender-E-Mail-Adresse für die Erzeugung eines E-Mail-Jobs ist leer.");
			}
			Objects.requireNonNull(context);
		} catch (final NullPointerException e) {
			throw new IllegalArgumentException("Notwendiger Parameter für die Erzeugung eines E-Mail-Jobs ist null.");
		}
		this.from = from;
		this.context = context;
	}


	/**
	 * Gibt den Kontext mit den versandbezogenen Einstellungen zurück, mit denen dieser Job gestartet wurde.
	 *
	 * @return der Kontext dieses Jobs
	 */
	public @NotNull EmailJobContext getContext() {
		return this.context;
	}


	/**
	 * Gibt die ID des E-Mail-Jobs zurück, sofern diese zuvor von einem {@link EmailJobManager}
	 * gesetzt wurde.
	 *
	 * @return die ID des Jobs
	 *
	 * @throws DeveloperNotificationException falls die ID nicht zuvor von einem Job-Manager gesetzt wurde
	 */
	public long getId() throws DeveloperNotificationException {
		if (id == null) {
			throw new DeveloperNotificationException("Für den Zugriff auf eine Job-ID muss diese zuvor von dem zugehörigen Job-Manager gesetzt sein.");
		}
		return id;
	}


	/**
	 * Setzt die Job-ID für den E-Mail-Job. Diese Methode ist package private,
	 * da sie nur vom zugehörigen {@link EmailJobManager} aufgerufen werden soll.
	 *
	 * @param id   die zu setzende Job-ID
	 *
	 * @return true, wenn die ID gesetzt werden konnte und zuvor keine ID gesetzt war, und ansonsten false
	 */
	boolean setId(final long id) {
		synchronized (this) {
			if (this.id != null) {
				return false;
			}
			this.id = id;
			return true;
		}
	}


	/**
	 * Gibt die Sender-E-Mail-Adresse zurück.
	 *
	 * @return die Sender-E-Mail-Adresse
	 */
	public @NotNull String getFrom() {
		return this.from;
	}


	/**
	 * Gibt den Betreff für die E-Mail zurück.
	 *
	 * @return der Betreff für die E-Mail
	 */
	public String getSubject() {
		return subject;
	}


	/**
	 * Gibt den Text für die E-Mail zurück.
	 *
	 * @return der Text für die E-Mail
	 */
	public String getBody() {
		return body;
	}


	/**
	 * Gibt die Empfängerliste mit den Anhängen für die Empfänger für die E-Mail zurück.
	 *
	 * @return die Empfängerliste mit den Anhängen für die Empfänger
	 */
	public List<EmailJobRecipient> getRecipients() {
		return recipients;
	}


	/**
	 * Setzt den Betreff auf den übergebenen Wert.
	 *
	 * @param subject   der neue Betreff
	 *
	 * @return dieser Job
	 */
	public @NotNull EmailJob withSubject(final @NotNull String subject) {
		// @NotNull sichert nicht gegen die Übergabe von null. SonarQube denkt aber so und meldet bei Prüfung mittels "== null" immer
		// "java:S2589, Remove this expression which always evaluates to true/false.". Daher hier die Prüfung mittels Objects.requireNonNull.
		this.subject = Objects.requireNonNullElse(subject, this.subject);
		return this;
	}


	/**
	 * Setzt den Text für die E-Mail auf den übergebenen Wert.
	 *
	 * @param body   der neue Text
	 *
	 * @return dieser Job
	 */
	public @NotNull EmailJob withBody(final @NotNull String body) {
		// @NotNull sichert nicht gegen die Übergabe von null. SonarQube denkt aber so und meldet bei Prüfung mittels "== null" immer
		// "java:S2589, Remove this expression which always evaluates to true/false.". Daher hier die Prüfung mittels Objects.requireNonNull.
		this.body = Objects.requireNonNullElse(body, this.body);
		return this;
	}


	/**
	 * Fügt einen weiteren Empfänger mit den ihm zugeordneten Anhängen hinzu
	 *
	 * @param recipient   der hinzuzufügende Empfänger mit den ihm zugeordneten E-Mail-Anhängen. null wird ignoriert.
	 *
	 * @return dieser Job
	 */
	public @NotNull EmailJob addRecipient(final EmailJobRecipient recipient) {
		if (recipient != null) {
			this.recipients.add(recipient);
		}
		return this;
	}


	/**
	 * Fügt mehrere Empfänger mit deren jeweils zugeordneten Anhängen hinzu
	 *
	 * @param recipients   die Liste der hinzuzufügenden Empfänger, mit den ihnen jeweils zugeordneten E-Mail-Anhängen. Leere Liste wird ignoriert.
	 *
	 * @return dieser Job
	 */
	public @NotNull EmailJob addRecipients(final List<EmailJobRecipient> recipients) {
		if ((recipients != null) && !recipients.isEmpty()) {
			this.recipients.addAll(recipients);
		}
		return this;
	}


	/**
	 * Fügt mehrere Einträge zum Übersprungen-Log hinzu.
	 *
	 * @param messages   die Log-Nachrichten
	 *
	 * @return dieser Job
	 */
	public @NotNull EmailJob addLogSkipped(final @NotNull List<String> messages) {
		this.logSkipped.addAll(messages);
		return this;
	}


	/**
	 * Fügt mehrere Einträge zum Error-Log hinzu.
	 *
	 * @param messages   die Log-Nachrichten
	 *
	 * @return dieser Job
	 */
	public @NotNull EmailJob addLogError(final @NotNull List<String> messages) {
		this.logError.addAll(messages);
		return this;
	}


	/**
	 * Gibt den Zeitpunkt zurück, zu welchem zuletzt eine Änderung des Job-Status
	 * vorgenommen wurde.
	 *
	 * @return der Zeitpunkt in Millisekunden ab Mitternacht am 1. Januar 1970 (siehe auch {@link System#currentTimeMillis()})
	 */
	public synchronized long getTimeLastChanged() {
		return timeLastChanged;
	}


	/**
	 * Gibt den aktuellen Status des Jobs zurück.
	 *
	 * @return der aktuelle Status des Jobs
	 */
	public synchronized @NotNull EmailJobStatus getStatus() {
		return status;
	}


	/**
	 * Setzt den Status des Jobs. Dies erfolgt durch den {@link EmailJobManager}, weshalb
	 * diese Methode package private ist.
	 *
	 * @param status  der Job-Status
	 */
	void setStatus(final @NotNull EmailJobStatus status) {
		synchronized (this) {
			this.status = status;
			this.timeLastChanged = System.currentTimeMillis();
		}
	}


	/**
	 * Diese Methode wird vom {@link EmailJobManager} aufgerufen, um diesen über
	 * das erfolgreiche Versenden einer E-Mail zu informieren.
	 * Daher ist diese Methode package private.
	 */
	void notifyEmailSent() {
		this.countSuccess.incrementAndGet();
	}


	/**
	 * Gibt die Anzahl der versendeten E-Mails aus diesem Job zurück.
	 *
	 * @return die Anzahl der versendeten E-Mails aus diesem Job
	 */
	public int getEmailsSent() {
		return this.countSuccess.get();
	}


	/**
	 * Setzt bei diesem Job den Wert so, dass ein Abbruch des Jobs angefordert wurde.
	 */
	void requestCancellation() {
		this.cancellationRequested = true;
	}


	/**
	 * Gibt zurück, ob ein Abbruch des Jobs angefordert wurde
	 *
	 * @return true, wenn ein Abbruch angefordert wurde, und ansonsten false
	 */
	boolean hasCancellationRequest() {
		return this.cancellationRequested;
	}


	/**
	 * Fügt einen Eintrag in das Übersprungen-Log hinzu. Paketprivat, da nur der Manager schreiben darf.
	 *
	 * @param message   die Log-Nachricht
	 */
	void addLogSkipped(final @NotNull String message) {
		this.logSkipped.add(message);
	}

	/**
	 * Fügt einen Eintrag in das Fehler-Log hinzu. Paketprivat, da nur der Manager schreiben darf.
	 *
	 * @param message   die Log-Nachricht
	 */
	void addLogError(final @NotNull String message) {
		this.logError.add(message);
	}

	/**
	 * Gibt eine unveränderliche Kopie des Übersprungen-Logs zurück.
	 *
	 * @return die Log-Einträge der übersprungenen Empfänger
	 */
	public @NotNull List<String> getLogSkipped() {
		synchronized (logSkipped) {
			return List.copyOf(logSkipped);
		}
	}

	/**
	 * Gibt eine unveränderliche Kopie des Fehler-Logs zurück.
	 *
	 * @return die Fehler-Log-Einträge
	 */
	public @NotNull List<String> getLogError() {
		synchronized (logError) {
			return List.copyOf(logError);
		}
	}
}
