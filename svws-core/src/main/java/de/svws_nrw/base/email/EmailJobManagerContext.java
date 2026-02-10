package de.svws_nrw.base.email;

import java.time.Duration;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert den Kontext, in welchem ein zugehöriger {@link EmailJobManager} betrieben wird.
 */
public class EmailJobManagerContext {

	/** Mindestlänge des Rate-Limit-Zeitfensters in Millisekunden. Wird nur bei Setzungen im Rahmen von Unit-Tests verwendet. */
	private static final long MIN_RATELIMIT_TIMEFRAME_MS = 2000L;

	/** Der Name des Datenbank-Schemas, dem dieser Manager zugeordnet ist */
	private final @NotNull String schema;

	/** Die ID des Benutzers aus dem Datenbank-Schema, dem dieser Manager zugeordnet ist. */
	private final long idUser;

	/** Die SMTP-Session, welche für den Versand genutzt wird. */
	private final @NotNull MailSmtpSession session;

	/** Das Zeitfenster in ms, auf dem das Rate-Limit aufgebaut wird, beträgt im Betrieb immer eine Minute, kann für Testzwecke geändert werden.
	 * werden. */
	private long rateLimitTimeframeMs = Duration.ofMinutes(1).toMillis();

	/** Die maximale Anzahl an E-Mails, die pro Minute versendet werden dürfen */
	private int maxEmailsPerMinute = 20;

	/** Die Zeit in Millisekunden, für welche abgeschlossene Jobs noch im Speicher gehalten werden, nachdem sie abgeschlossen wurden */
	private long timeToKeepCompletedJobs = 60000; // Default 1 min

	/** Gibt die Größe in Bytes an, die die E-Mail-Anhänge in Summe maximal haben dürfen. Ein Wert von 0 bedeutet, dass es kein Limit gibt. */
	private long maxAttachmentSize = 0;

	/** Legt fest, ob die maximale Größe der E-Mail-Anhänge auch für einzelne Dateianhänge eingehalten werden muss. */
	private boolean forceMaxAttachmentSize = false;

	/** Legt fest, ob E-Mails, für die keine Anhänge vorhanden sind, auch versendet werden sollen. Ist die Option true, werden keine E-Mails ohne Anhänge versendet. */
	private boolean filterMailsWithoutAttachments = false;


	/**
	 * Erstellt einen neuen Kontext für einen Job-Manager. Der Kontext umfasst den Benutzer, welcher eindeutig
	 * durch das SVWS-Datenbank-Schema und die Benutzer-ID aus dem SVWS-Datenbank-Schema festgelegt ist.
	 *
	 * @param schema    das SVWS-Datenbank-Schema
	 * @param idUser    die Benutzer-ID aus dem SVWS-Datenbank-Schema
	 * @param session   die Session für das Versenden von Email per SMTP
	 */
	public EmailJobManagerContext(final @NotNull String schema, final long idUser, final @NotNull MailSmtpSession session) {
		if ((schema == null) || schema.isBlank() || (session == null))
			throw new IllegalArgumentException("Notwendige Parameter für die Erzeugung eines E-Mail-Job-Manager-Contexts sind null oder leer.");
		this.schema = schema;
		this.idUser = idUser;
		this.session = session;
	}

	/**
	 * NUR für UNIT-TESTS gedacht! Daher auch Package-Privat.
	 * Setzt den Zeitraum in Millisekunden für das Ratenlimit, auf welchem die Anzahl der versendeten E-Mails beschränkt ist.
	 * Die Angabe erfolgt in ms und der Mindestwert wird durch MIN_RATELIMIT_TIMEFRAME_MS festgelegt.
	 * Kleinere Werte als MIN_RATELIMIT_TIMEFRAME_MS werden ignoriert.
	 * Anschließend wird dieser Kontext (this) zurückgegeben.
	 *
	 * @param rateLimitTimeframeMs der Zeitraum in Millisekunden für das Ratenlimit. Werte kleiner als MIN_RATELIMIT_TIMEFRAME_MS werden ignoriert.
	 *
	 * @return dieser Kontext
	 */
	@NotNull
	EmailJobManagerContext withRateLimitTimeframeMs(final long rateLimitTimeframeMs) {
		if (rateLimitTimeframeMs >= MIN_RATELIMIT_TIMEFRAME_MS)
			this.rateLimitTimeframeMs = rateLimitTimeframeMs;
		return this;
	}

	/**
	 * Setzt die maximale Anzahl von Emails, die pro Minute versendet werden dürfen.
	 * Anschließend wird dieser Kontext (this) zurückgegeben.
	 *
	 * @param maxEmailsPerMinute   die maximale Anzahl von Emails pro Minute, 0 und negative Werte werden ignoriert
	 *
	 * @return dieser Kontext
	 */
	public @NotNull EmailJobManagerContext withMaxEmailsPerMinute(final int maxEmailsPerMinute) {
		if (maxEmailsPerMinute > 0)
			this.maxEmailsPerMinute = maxEmailsPerMinute;
		return this;
	}


	/**
	 * Setzt die Zeit in Millisekunden, für welche abgeschlossenene Jobs noch im Speicher
	 * gehalten werden nachdem sie abgeschlossen wurden. Anschließend wird dieser Kontext (this) zurückgegeben.
	 *
	 * @param time   die Zeit in Millisekunden, negative Werte und 0 werden ignoriert
	 *
	 * @return dieser Kontext
	 */
	public @NotNull EmailJobManagerContext withTimeToKeepCompletedJobs(final long time) {
		if (time > 0)
			this.timeToKeepCompletedJobs = time;
		return this;
	}


	/**
	 * Setzt die maximale Größe in Bytes, die die E-Mail-Anhänge in Summe maximal haben dürfen. Ein Wert von 0 bedeutet,
	 * dass es kein Limit gibt. Anschließend wird dieser Kontext (this) zurückgegeben.
	 *
	 * @param maxAttachmentSize   die maximale Größe von Dateianhängen oder 0, negative Werte werden ignoriert
	 *
	 * @return dieser Kontext
	 */
	public @NotNull EmailJobManagerContext withMaxAttachmentSize(final long maxAttachmentSize) {
		if (maxAttachmentSize >= 0)
			this.maxAttachmentSize = maxAttachmentSize;
		return this;
	}


	/**
	 * Legt fest, ob die maximale Größe der E-Email-Anhänge auch für einzelne Dateianhänge eingehalten werden muss
	 * und gibt anschließend diesen Kontext (this) zurück.
	 *
	 * @param forceMaxAttachmentSize   gibt an, ob die maximale Größer immer eingehalten werden muss
	 *
	 * @return dieser Kontext
	 */
	public @NotNull EmailJobManagerContext withForceMaxAttachmentSize(final boolean forceMaxAttachmentSize) {
		this.forceMaxAttachmentSize = forceMaxAttachmentSize;
		return this;
	}

	/**
	 * Legt fest, ob E-Mails ohne Anhänge herausgefiltert werden sollen, und gibt anschließend diesen Kontext (this) zurück.
	 *
	 * @param filterMailsWithoutAttachments gibt an, ob E-Mails ohne Anhänge herausgefiltert werden sollen
	 *
	 * @return dieser Kontext
	 */
	public @NotNull EmailJobManagerContext withFilterMailsWithoutAttachments(final boolean filterMailsWithoutAttachments) {
		this.filterMailsWithoutAttachments = filterMailsWithoutAttachments;
		return this;
	}


	/**
	 * Gibt den Namen des SVWS-Datenbank-Schema zurück, in welchem der Benutzer angelegt ist, welcher
	 * die Kontext zugeordnet ist.
	 *
	 * @return der Name des SVWS-Datenbank-Schemas.
	 */
	public String getDBSchema() {
		return schema;
	}


	/**
	 * Gibt die ID des Benutzers aus dem SVWS-Datenbank-Schema zurück, welcher diesem Kontext
	 * zugeordnet ist.
	 *
	 * @return die Benutzer-ID
	 */
	public long getUserId() {
		return idUser;
	}


	/**
	 * Git die SMTP-Session zurück, die in von dem Manager zum Versenden von Emails verwendet wird.
	 *
	 * @return die SMTP-Session
	 */
	public MailSmtpSession getSmtpSession() {
		return session;
	}


	/**
	 * Gibt den Zeitraum in Millisekunden zurück, in welchem die Anzahl der versendeten E-Mails beschränkt ist.
	 *
	 * @return der Zeitraum für das Ratenlimit in Millisekunden
	 */
	public long getRateLimitTimeframeMs() {
		return rateLimitTimeframeMs;
	}

	/**
	 * Gibt die maximale Anzahl an E-Mails zurück, die pro Minute versendet werden darf
	 *
	 * @return die maximale Anzahl an E-Mails, die pro Minute versendet werden darf
	 */
	public int getMaxEmailsPerMinute() {
		return maxEmailsPerMinute;
	}


	/**
	 * Gibt die Zeit in Millisekunden zurück, für welche abgeschlossenene Jobs noch im Speicher
	 * gehalten werden nachdem sie abgeschlossen wurden
	 *
	 * @return die Zeit in Millisekunden
	 */
	public long getTimeToKeepCompletedJobs() {
		return timeToKeepCompletedJobs;
	}


	/**
	 * Gibt die maximale Größe in Bytes zurück, die die E-Mail-Anhänge in Summe maximal haben dürfen. Ein Wert von 0 bedeutet,
	 * dass es kein Limit gibt.
	 *
	 * @return die maximale Größe in Bytes oder 0
	 */
	public long getMaxAttachmentSize() {
		return maxAttachmentSize;
	}


	/**
	 * Gibt an, ob die maximale Größe der E-Email-Anhänge auch für einzelne Dateianhänge eingehalten werden muss.
	 *
	 * @return true, wenn immer - auch für einzelne Anhänge - eingehalten werden muss, und ansonsten false
	 */
	public boolean isForceMaxAttachmentSize() {
		return forceMaxAttachmentSize;
	}

	/**
	 * Gibt an, ob E-Mails ohne Anhänge herausgefiltert werden sollen.
	 *
	 * @return true, wenn E-Mails ohne Anhänge herausgefiltert werden, sonst false
	 */
	public boolean isFilterMailsWithoutAttachments() {
		return filterMailsWithoutAttachments;
	}
}
