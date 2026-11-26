package de.svws_nrw.data.email;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.tika.Tika;

import de.svws_nrw.base.email.EmailJob;
import de.svws_nrw.base.email.EmailJobAttachment;
import de.svws_nrw.base.email.EmailJobContext;
import de.svws_nrw.base.email.EmailJobManager;
import de.svws_nrw.base.email.EmailJobManagerFactory;
import de.svws_nrw.base.email.EmailJobRecipient;
import de.svws_nrw.base.email.EmailUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.email.EmailAttachment;
import de.svws_nrw.core.data.email.EmailSendRequest;
import de.svws_nrw.data.benutzer.DataBenutzerEMailDaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Startet den E-Mail-Versand des angemeldeten Benutzers als Job im Hintergrund, dessen Status und Log über die Job-ID abrufbar sind. Jeder
 * Empfänger erhält eine eigene E-Mail mit seinen Anhängen. Ungültige Adressen werden übersprungen und im Log vermerkt.
 */
public final class DataEmailSend {

	/** Die Gesamtgröße aller Anhänge eines API-Auftrags in Bytes. Sie entspricht der Grenze des Job-Kontexts, sodass der Manager sie nie aufteilt. */
	static final long MAX_ATTACHMENT_SIZE = 8388608;

	/**
	 * Die höchste Zahl an Zeichen aller Anhänge vor dem Dekodieren. Base64 braucht vier Zeichen für drei Bytes, dazu kommt Spielraum für
	 * Zeilenumbrüche. Ohne diese Grenze zählte beliebig viel Leerraum nicht zur Größe der Anhänge.
	 */
	static final long MAX_ENCODED_LENGTH = MAX_ATTACHMENT_SIZE / 3 * 4 * 11 / 10;

	/** Die höchste Zahl an Empfängern eines API-Auftrags. Bei 20 E-Mails pro Minute dauert ein voller Auftrag etwa 25 Minuten. */
	static final int MAX_RECIPIENTS = 500;

	/** Die höchste Zahl an Jobs eines Benutzers, die warten oder versendet werden, bevor weitere Aufträge abgelehnt werden. */
	static final int MAX_ACTIVE_JOBS = 3;

	/** Die höchste Zahl an Anhängen eines API-Auftrags. Ohne sie passten Tausende winziger Anhänge in eine E-Mail. */
	static final int MAX_ATTACHMENTS = 20;

	/** Die Länge, auf die eine ungültige Adresse im Log gekürzt wird. */
	private static final int MAX_LOGGED_ADDRESS_LENGTH = 100;

	/**
	 * Die höchste Länge eines Dateinamens in Bytes nach UTF-8, wie sie die üblichen Dateisysteme erlauben. Die Grenze gilt für Bytes, weil die
	 * Kopfzeile des Anhangs jedes Byte außerhalb von ASCII mit drei Zeichen kodiert und sonst länger würde, als Mailserver annehmen.
	 */
	static final int MAX_FILENAME_LENGTH = 255;

	/** Die höchste Länge des Betreffs. */
	static final int MAX_SUBJECT_LENGTH = 255;

	/** Die höchste Länge des Textes. */
	static final int MAX_TEXT_LENGTH = 100000;

	/** Die zulässigen Dateiendungen eines API-Anhangs mit dem MIME-Type, den der Inhalt haben muss. Andere Typen könnten Schadsoftware enthalten. */
	private static final Map<String, String> ALLOWED_TYPES = Map.of("pdf", "application/pdf", "png", "image/png", "jpg", "image/jpeg", "jpeg", "image/jpeg");

	/** Bestimmt den MIME-Type aus den Daten, weil die Angabe des Clients nicht verlässlich ist. */
	private static final Tika TIKA = new Tika();

	/** Die Datenbank-Verbindung */
	private final @NotNull DBEntityManager conn;


	/**
	 * Erzeugt eine neue Instanz für die übergebene Datenbank-Verbindung.
	 *
	 * @param conn   die Datenbank-Verbindung
	 */
	public DataEmailSend(final @NotNull DBEntityManager conn) {
		this.conn = conn;
	}


	/**
	 * Versendet einen Auftrag der API. Jede Adresse der Empfängerliste erhält dieselben Anhänge.
	 *
	 * @param request   der Auftrag mit Betreff, Text, Empfängern und Anhängen
	 *
	 * @return die Response mit der Job-ID (HTTP 202 Accepted)
	 *
	 * @throws ApiOperationException   bei einem fehlerhaften Auftrag, fehlender Absenderadresse oder fehlender SMTP-Konfiguration und mit
	 *                                 TOO_MANY_REQUESTS, wenn bereits zu viele Jobs des Benutzers warten oder versendet werden
	 */
	public @NotNull Response send(final EmailSendRequest request) throws ApiOperationException {
		if (request == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für den E-Mail-Versand wurden keine Daten übergeben.");
		}
		checkSubjectAndText(request.subject, request.text);
		if ((request.recipients != null) && (request.recipients.size() > MAX_RECIPIENTS)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Ein Auftrag darf höchstens %d Empfänger enthalten.".formatted(MAX_RECIPIENTS));
		}
		final List<String> skipped = new ArrayList<>();
		final List<EmailJobRecipient> recipients = new ArrayList<>();
		if (request.recipients != null) {
			for (final String address : request.recipients) {
				if ((address == null) || address.isBlank()) {
					skipped.add("- Die E-Mail-Adresse '' ist ungültig und wird übersprungen.");
				} else {
					recipients.add(new EmailJobRecipient(address));
				}
			}
		}
		// Empfänger und freie Job-Plätze werden vor den Anhängen geprüft, damit der Server die Anhänge nicht umsonst dekodiert. Verbindlich prüft
		// erst das Einreihen die Grenze der aktiven Jobs.
		final List<EmailJobRecipient> validRecipients = mergeRecipients(recipients, skipped);
		final EmailJobManager manager = EmailJobManagerFactory.getInstance().getManagerByUser(conn.getDBSchema(), conn.getUser().getId());
		if ((manager != null) && (manager.countActiveJobs() >= MAX_ACTIVE_JOBS)) {
			throw tooManyActiveJobs();
		}
		final List<EmailJobAttachment> attachments = createAttachments(request.attachments);
		for (final EmailJobRecipient recipient : validRecipients) {
			recipient.attachments.addAll(attachments);
		}
		return send(request.subject, request.text, validRecipients, skipped, false);
	}


	/**
	 * Prüft die Empfänger und reiht den Versand als Job beim Job-Manager des Benutzers ein. Die Prüfungen laufen vor dem ersten Zugriff auf
	 * Datenbank und Job-Manager, sodass ein fehlerhafter Auftrag ohne Nebenwirkung scheitert. Die Methode dient Aufrufern, bei denen jeder
	 * Empfänger eigene Anhänge erhält. Die Grenze der aktiven Jobs zählt alle Jobs des Benutzers, weil sie den Speicher des Servers schützt.
	 *
	 * @param subject                            der Betreff der E-Mail
	 * @param text                               der Text der E-Mail als reiner Text
	 * @param recipients                         die Empfänger mit ihren Anhängen
	 * @param skipped                            die Hinweise auf übersprungene Empfänger, die Methode ergänzt sie um ihre eigenen
	 * @param skipRecipientsWithoutAttachments   gibt an, ob Empfänger ohne Anhang keine E-Mail erhalten
	 *
	 * @return die Response mit der Job-ID (HTTP 202 Accepted)
	 *
	 * @throws ApiOperationException   bei fehlendem Betreff oder Text, ohne gültigen Empfänger, ohne Absenderadresse oder SMTP-Konfiguration und
	 *                                 mit TOO_MANY_REQUESTS, wenn bereits zu viele Jobs des Benutzers warten oder versendet werden
	 */
	public @NotNull Response send(final String subject, final String text, final @NotNull Collection<EmailJobRecipient> recipients,
			final @NotNull List<String> skipped, final boolean skipRecipientsWithoutAttachments) throws ApiOperationException {
		checkSubjectAndText(subject, text);
		final List<EmailJobRecipient> validRecipients = mergeRecipients(recipients, skipped);
		final String from = getSender();
		final EmailJobContext context = DataEmailJobs.createDefaultJobContext(conn).withFilterMailsWithoutAttachments(skipRecipientsWithoutAttachments);

		final EmailJob job = new EmailJob(from, context)
				.withSubject(cleanSubject(subject))
				.withBody(EmailUtils.createHtmlBody(text))
				.addRecipients(validRecipients)
				.addLogSkipped(skipped);
		final long idJob = EmailJobManagerFactory.getInstance().getManager(conn.getDBSchema(), conn.getUser().getId()).enqueue(job, MAX_ACTIVE_JOBS);
		if (idJob < 0) {
			throw tooManyActiveJobs();
		}

		final SimpleOperationResponse response = new SimpleOperationResponse();
		response.id = idJob;
		response.success = true;
		response.log.add("Ein E-Mail-Versand-Job an %d Empfänger wurde gestartet mit Job-ID %d".formatted(validRecipients.size(), idJob));
		response.log.addAll(skipped);
		return Response.status(Status.ACCEPTED).type(MediaType.APPLICATION_JSON).entity(response).build();
	}


	/**
	 * Prüft, ob der angemeldete Benutzer versenden kann: Er braucht eine gültige Absenderadresse, und aus Schul- und Benutzerdaten muss sich eine
	 * SMTP-Konfiguration bilden lassen. Wer die Anhänge aufwendig erzeugt, ruft diese Methode vorher auf, damit ein fehlender Zugang nicht erst
	 * nach getaner Arbeit auffällt.
	 *
	 * @throws ApiOperationException   wenn die Absenderadresse fehlt oder die SMTP-Konfiguration nicht gebildet werden kann
	 */
	public void checkSenderAndSmtpConfig() throws ApiOperationException {
		getSender();
		DataEmailJobs.getSMTPConfig(conn);
	}


	/**
	 * Erzeugt die Meldung, dass bereits zu viele Jobs des Benutzers warten oder versendet werden.
	 *
	 * @return die Exception mit TOO_MANY_REQUESTS
	 */
	private static @NotNull ApiOperationException tooManyActiveJobs() {
		return new ApiOperationException(Status.TOO_MANY_REQUESTS, "Es warten oder laufen bereits %d E-Mail-Versand-Jobs. Ein weiterer Auftrag ist "
				.formatted(MAX_ACTIVE_JOBS) + "erst möglich, wenn einer davon abgeschlossen ist.");
	}


	/**
	 * Prüft, ob Betreff und Text vorliegen und ihre Höchstlänge einhalten.
	 *
	 * @param subject   der Betreff
	 * @param text      der Text
	 *
	 * @throws ApiOperationException   wenn Betreff oder Text leer oder zu lang sind
	 */
	static void checkSubjectAndText(final String subject, final String text) throws ApiOperationException {
		if ((subject == null) || cleanSubject(subject).isEmpty() || (text == null) || text.isBlank()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für die E-Mail fehlt der Betreff oder der Text. Beide sind anzugeben.");
		}
		if (subject.length() > MAX_SUBJECT_LENGTH) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Betreff darf höchstens %d Zeichen lang sein.".formatted(MAX_SUBJECT_LENGTH));
		}
		if (text.length() > MAX_TEXT_LENGTH) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Text darf höchstens %d Zeichen lang sein.".formatted(MAX_TEXT_LENGTH));
		}
	}


	/**
	 * Ersetzt Steuerzeichen wie Zeilenumbrüche im Betreff durch Leerzeichen, weil E-Mail-Programme sie unterschiedlich darstellen. Unsichtbare
	 * Formatzeichen entfallen, weil sie die Leserichtung umkehren und so den angezeigten Text verfälschen können.
	 *
	 * @param subject   der Betreff
	 *
	 * @return der Betreff ohne Steuer- und Formatzeichen und ohne Leerraum am Rand
	 */
	static @NotNull String cleanSubject(final @NotNull String subject) {
		final StringBuilder result = new StringBuilder(subject.length());
		subject.codePoints().forEach(c -> {
			if (Character.isISOControl(c)) {
				result.append(' ');
			} else if (Character.getType(c) != Character.FORMAT) {
				result.appendCodePoint(c);
			}
		});
		return result.toString().strip();
	}


	/**
	 * Prüft, ob ein Zeichen ein Steuerzeichen oder ein unsichtbares Formatzeichen wie die Umkehr der Leserichtung ist.
	 *
	 * @param c   der Codepoint des Zeichens
	 *
	 * @return true, wenn das Zeichen ein Steuer- oder Formatzeichen ist, und ansonsten false
	 */
	private static boolean isControlOrFormat(final int c) {
		return Character.isISOControl(c) || (Character.getType(c) == Character.FORMAT);
	}


	/**
	 * Dekodiert die Anhänge eines API-Auftrags und bestimmt ihren MIME-Type. Ein fehlerhafter Anhang lässt den ganzen Auftrag scheitern, weil sonst
	 * alle Empfänger eine unvollständige E-Mail erhielten.
	 *
	 * @param attachments   die Anhänge aus dem Auftrag, null steht für keine Anhänge
	 *
	 * @return die Anhänge für den E-Mail-Job
	 *
	 * @throws ApiOperationException   wenn ein Anhang keinen zulässigen Dateinamen hat, leer oder nicht Base64-kodiert ist, sein Typ nicht erlaubt ist
	 *                                 oder nicht zur Endung passt oder es zu viele oder zusammen zu große Anhänge sind
	 */
	static @NotNull List<EmailJobAttachment> createAttachments(final List<EmailAttachment> attachments) throws ApiOperationException {
		final List<EmailJobAttachment> result = new ArrayList<>();
		if (attachments == null) {
			return result;
		}
		if (attachments.size() > MAX_ATTACHMENTS) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine E-Mail darf höchstens %d Anhänge haben.".formatted(MAX_ATTACHMENTS));
		}
		// Namen und Größe aller Anhänge werden vor dem ersten Dekodieren geprüft, damit zu große Daten nicht erst kopiert und dekodiert werden.
		long size = 0;
		long encodedLength = 0;
		for (final EmailAttachment attachment : attachments) {
			checkFilename(attachment);
			if (attachment.data != null) {
				encodedLength += attachment.data.length();
				size += decodedLength(attachment.data);
			}
			if ((encodedLength > MAX_ENCODED_LENGTH) || (size > MAX_ATTACHMENT_SIZE)) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Die Anhänge sind zusammen größer als %d MB.".formatted(MAX_ATTACHMENT_SIZE / 1048576));
			}
		}
		for (final EmailAttachment attachment : attachments) {
			final String filename = checkFilename(attachment);
			final String data = (attachment.data == null) ? "" : attachment.data;
			final byte[] bytes;
			try {
				// Der MIME-Decoder überginge ungültige Zeichen stillschweigend, daher nur Leerraum entfernen und streng dekodieren.
				bytes = Base64.getDecoder().decode(data.replaceAll("\\s", ""));
			} catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Der Anhang '%s' ist nicht Base64-kodiert.".formatted(filename));
			}
			if (bytes.length == 0) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Der Anhang '%s' ist leer.".formatted(filename));
			}
			result.add(new EmailJobAttachment(filename, bytes, checkType(filename, bytes)));
		}
		return result;
	}


	/**
	 * Prüft den Dateinamen eines Anhangs. Steuerzeichen würden im Anhang eigene Header-Zeilen erzeugen, unsichtbare Formatzeichen können die
	 * angezeigte Endung verfälschen, Pfadangaben könnten beim Empfänger in andere Verzeichnisse zeigen.
	 *
	 * @param attachment   der Anhang
	 *
	 * @return der Dateiname ohne Leerraum am Rand
	 *
	 * @throws ApiOperationException   wenn der Dateiname fehlt, zu lang ist oder Steuerzeichen oder Pfadangaben enthält
	 */
	private static @NotNull String checkFilename(final EmailAttachment attachment) throws ApiOperationException {
		if ((attachment == null) || (attachment.filename == null) || attachment.filename.isBlank()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Ein Anhang der E-Mail hat keinen Dateinamen.");
		}
		final String filename = attachment.filename.strip();
		if ((filename.getBytes(StandardCharsets.UTF_8).length > MAX_FILENAME_LENGTH) || filename.codePoints().anyMatch(c -> isControlOrFormat(c) || (c == '/') || (c == '\\'))) {
			// Der Name wird nicht in die Meldung übernommen, weil er Steuer- oder Formatzeichen enthalten kann.
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Dateiname eines Anhangs ist zu lang oder enthält Steuerzeichen oder Pfadangaben.");
		}
		return filename;
	}


	/**
	 * Ermittelt die Größe der dekodierten Daten aus der Base64-Kodierung, ohne sie zu dekodieren. Bei gültigen Daten ist das Ergebnis genau.
	 *
	 * @param data   die Base64-kodierten Daten, eventuell mit Leerraum
	 *
	 * @return die Größe der dekodierten Daten in Bytes
	 */
	static long decodedLength(final @NotNull String data) {
		long chars = 0;
		long padding = 0;
		for (int i = 0; i < data.length(); i++) {
			final char c = data.charAt(i);
			if (c == '=') {
				chars++;
				padding++;
			} else if (" \t\n\u000B\f\r".indexOf(c) < 0) {
				chars++;
				padding = 0;
			}
		}
		// Gültiges Base64 endet mit höchstens zwei Füllzeichen. Mehr dürfen die Schätzung nicht verkleinern.
		return ((chars * 3) / 4) - Math.min(padding, 2);
	}


	/**
	 * Bestimmt den MIME-Type allein aus den Daten und prüft ihn gegen die Positivliste. Die Endung muss zum Inhalt passen, damit eine Datei nicht
	 * unter falschem Namen versendet wird.
	 *
	 * @param filename   der Dateiname
	 * @param bytes      die Daten
	 *
	 * @return der MIME-Type
	 *
	 * @throws ApiOperationException   wenn der Typ nicht erlaubt ist oder nicht zur Endung passt
	 */
	private static @NotNull String checkType(final @NotNull String filename, final byte[] bytes) throws ApiOperationException {
		final int dot = filename.lastIndexOf('.');
		final String expected = (dot < 0) ? null : ALLOWED_TYPES.get(filename.substring(dot + 1).toLowerCase(Locale.ROOT));
		if (expected == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Anhang '%s' hat keinen zulässigen Dateityp. Erlaubt sind PDF, PNG und JPEG."
					.formatted(filename));
		}
		final String detected = TIKA.detect(bytes);
		if (!expected.equals(detected)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Inhalt des Anhangs '%s' passt nicht zu seiner Dateiendung.".formatted(filename));
		}
		return detected;
	}


	/**
	 * Bereinigt die Empfänger. Ungültige und gesperrte Adressen werden übersprungen und vermerkt. Empfänger mit derselben Adresse werden zu
	 * einem zusammengefasst, damit niemand mehrere E-Mails erhält. Ein Anhang, der dabei mehrfach auftritt, wird nur einmal übernommen.
	 *
	 * @param recipients   die Empfänger mit ihren Anhängen
	 * @param skipped      die Liste, in die die Hinweise auf übersprungene Adressen geschrieben werden
	 *
	 * @return die gültigen Empfänger mit Adressen in Kleinbuchstaben und in der übergebenen Reihenfolge
	 *
	 * @throws ApiOperationException   wenn kein gültiger Empfänger übrig bleibt
	 */
	static @NotNull List<EmailJobRecipient> mergeRecipients(final @NotNull Collection<EmailJobRecipient> recipients, final @NotNull List<String> skipped)
			throws ApiOperationException {
		final Map<String, EmailJobRecipient> result = new LinkedHashMap<>();
		for (final EmailJobRecipient recipient : recipients) {
			if (recipient == null) {
				continue;
			}
			final String address = EmailUtils.normalizeAddress(recipient.email);
			if (!EmailUtils.isValidEmail(address)) {
				final String logged = (address.length() > MAX_LOGGED_ADDRESS_LENGTH) ? (address.substring(0, MAX_LOGGED_ADDRESS_LENGTH) + "…") : address;
				skipped.add("- Die E-Mail-Adresse '%s' ist ungültig und wird übersprungen.".formatted(logged));
			} else if (EmailUtils.isBlockedDomain(address)) {
				skipped.add("- Die E-Mail an %s wird nicht versendet, da die Domain als unzulässig markiert ist.".formatted(address));
			} else {
				// Die ASCII-Form der Domain dient als Schlüssel, weil verschiedene Unicode-Schreibweisen zur selben Domain führen können.
				final EmailJobRecipient merged = result.computeIfAbsent(EmailUtils.toAsciiDomain(address), key -> new EmailJobRecipient(address));
				for (final EmailJobAttachment attachment : recipient.attachments) {
					if (!merged.attachments.contains(attachment)) {
						merged.attachments.add(attachment);
					}
				}
			}
		}
		if (result.isEmpty()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für den E-Mail-Versand wurde keine gültige Empfängeradresse übergeben.");
		}
		return new ArrayList<>(result.values());
	}


	/**
	 * Ermittelt die Absenderadresse aus den E-Mail-Daten des angemeldeten Benutzers.
	 *
	 * @return die Absenderadresse in Kleinbuchstaben
	 *
	 * @throws ApiOperationException   wenn für den Benutzer keine gültige Adresse hinterlegt ist
	 */
	private @NotNull String getSender() throws ApiOperationException {
		final String address = EmailUtils.normalizeAddress(DataBenutzerEMailDaten.getOrCreateDTO(conn).Email);
		if (!EmailUtils.isValidEmail(address)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für den angemeldeten Benutzer ist keine gültige Absender-E-Mail-Adresse hinterlegt. "
					+ "Die E-Mail-Einstellungen des Benutzers sind zu prüfen.");
		}
		return address;
	}

}
