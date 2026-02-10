package de.svws_nrw.base.email;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beinhaltet einen Job zum Versenden von E-Mails und beschreibt einen Versandvorgang. Dieser
 * unterstützt das Versenden von Mails an einzelne Empfänge mit unterschiedlichen Anhängen.
 */
public final class EmailJob {

	/** Die eindeutige ID des Jobs, sofern diese schon von einem {@link EmailJobManager} zugeteilt wurde. */
	private Long id = null;

	/** Die E-Mail-Adresse des Senders (from) */
	private final @NotNull String from;

	/** Der Betreff der E-Mail */
	private @NotNull String subject = "";

	/** Der Body der E-Mail */
	private @NotNull String body = "";

	/** Eine Liste mit den Adressen der Empfänger und den ihnen zugeordneten Anhängen. */
	private final @NotNull List<EmailJobRecipient> recipients = new ArrayList<>();


	/** Aktueller Status des Jobs. */
	private EmailJobStatus status = EmailJobStatus.QUEUED;

	/** Gibt an, ob der Job zum Abbrechen markiert wurde. */
	private volatile boolean cancellationRequested = false;

	/** Zeitstempel der letzten Statusänderung in Millisekunden */
	private long timeLastChanged = System.currentTimeMillis();


	/** Die Anzahl der erfolgreich versendeten E-Mails. */
	private int countSuccess = 0;

	/** Die Liste der Log-Informationen, wenn einzelne Empfänger des Jobs ausgelassen wurden. */
	public final @NotNull List<String> logSkipped = Collections.synchronizedList(new ArrayList<>());

	/** Die Liste mit dem Fehler-Log. */
	public final @NotNull List<String> logError = Collections.synchronizedList(new ArrayList<>());


	/**
	 * Erstellt einen neuen E-Mail-Job zum Versenden einer Mail von der übergebenen Adresse
	 *
	 * @param from   die E-Mail-Adresse des Senders
	 */
	public EmailJob(final @NotNull String from) {
		if ((from == null) || from.isBlank())
			throw new IllegalArgumentException("Notwendiger Parameter Absender-E-Mail-Adresse für die Erzeugung eines E-Mail-Jobs ist null oder leer.");
		this.from = from;
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
		if (id == null)
			throw new DeveloperNotificationException("Für den Zugriff auf eine Job-ID muss diese zuvor von dem zugehörigen Job-Manager gesetzt sein.");
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
		if (this.id != null)
			return false;
		this.id = id;
		return true;
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
		if (subject != null)
			this.subject = subject;
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
		if (body != null)
			this.body = body;
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
		if (recipient != null)
			this.recipients.add(recipient);
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
		if ((recipients != null) && !recipients.isEmpty())
			this.recipients.addAll(recipients);
		return this;
	}


	/**
	 * Gibt den Zeitpunkt zurück, zu welchem zuletzt eine Änderung des Job-Status
	 * vorgenommen wurde.
	 *
	 * @return der Zeitpunkt in Millisekunden ab Mitternacht am 1. Januar 1970 (siehe auch {@link System#currentTimeMillis()})
	 */
	public long getTimeLastChanged() {
		return timeLastChanged;
	}


	/**
	 * Gibt den aktuellen Status des Jobs zurück.
	 *
	 * @return der aktuelle Status des Jobs
	 */
	public @NotNull EmailJobStatus getStatus() {
		return status;
	}


	/**
	 * Setzt den Status des Jobs. Dies erfolgt durch den {@link EmailJobManager}, weshalb
	 * diese Methode package private ist.
	 *
	 * @param status  der Job-Status
	 */
	void setStatus(final @NotNull EmailJobStatus status) {
		this.status = status;
		this.timeLastChanged = System.currentTimeMillis();
	}


	/**
	 * Diese Methode wird vom {@link EmailJobManager} aufgerufen, um diesen über
	 * das erfolgreiche Versenden einer E-Mail zu informieren.
	 * Daher ist diese Methode package private.
	 */
	void notifyEmailSent() {
		this.countSuccess++;
	}


	/**
	 * Gibt die Anzahl der versendeten E-Mails aus diesem Job zurück.
	 *
	 * @return die Anzahl der versendeten E-Mails aus diesem Job
	 */
	public int getEmailsSent() {
		return this.countSuccess;
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

}
