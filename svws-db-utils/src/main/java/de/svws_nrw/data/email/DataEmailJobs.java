package de.svws_nrw.data.email;

import de.svws_nrw.base.crypto.AES;
import de.svws_nrw.base.crypto.AESException;
import de.svws_nrw.base.email.EmailJob;
import de.svws_nrw.base.email.EmailJobManager;
import de.svws_nrw.base.email.EmailJobManagerContext;
import de.svws_nrw.base.email.EmailJobManagerFactory;
import de.svws_nrw.base.email.EmailJobStatus;
import de.svws_nrw.base.email.MailSmtpSession;
import de.svws_nrw.base.email.MailSmtpSessionConfig;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.email.SMTPServerKonfiguration;
import de.svws_nrw.data.benutzer.DataBenutzerEMailDaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzerMail;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse dient dem Aggregieren von Informationen zu EMail-Jobs
 * über Datenbankzugriffe und den Zugriff auf die Email-Job-Manager
 */
public class DataEmailJobs {

	/** Die Datenbank-Verbindung */
	private final @NotNull DBEntityManager conn;


	/**
	 * Erzeugt eine neue Instanz für die übergebene Datenbank-Verbindung
	 *
	 * @param conn   die Datenbank-Verbindung
	 */
	public DataEmailJobs(final @NotNull DBEntityManager conn) {
		this.conn = conn;
	}


	/**
	 * Gibt den {@Link EmailJobManager} für den Benutzer der übergebenen Datenbank-Verbindung zurück.
	 *
	 * @param conn   die Datenbank-Verbindung
	 *
	 * @return der Job-Manager für die Datenbankverbindung
	 *
	 * @throws ApiOperationException falls kein Manager für den Benutzer der Datenbank-Verbindung vorhanden ist
	 */
	private static @NotNull EmailJobManager getJobManager(final @NotNull DBEntityManager conn) throws ApiOperationException {
		final EmailJobManager manager = EmailJobManagerFactory.getInstance().getManagerByUser(conn.getDBSchema(), conn.getUser().getId());
		if (manager == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es konnte kein Email-Job-Manager für den aktuellen Benutzer gefunden werden.");
		return manager;
	}


	/**
	 * Gibt den {@link EmailJob} für die übergebene Job-ID und den Benutzer der übergebenen Datenbank-Verbindung zurück.
	 *
	 * @param conn   die Datenbank-Verbindung
	 * @param id     die Job-ID
	 *
	 * @return der Email-Job
	 *
	 * @throws ApiOperationException falls die ID für den Benutzer der Datenbank-Verbindung ungültig ist
	 */
	private static EmailJob getJobByID(final @NotNull DBEntityManager conn, final long id) throws ApiOperationException {
		final EmailJob job = getJobManager(conn).getJob(id);
		if (job == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es konnte kein Email-Job mit der Job-ID %d beim aktuellen Benutzer gefunden werden.".formatted(id));
		return job;
	}


	/**
	 * Liefert eine {@link SimpleOperationResponse} mit der Information, dass
	 * die übergebene Job-ID ungültig ist.
	 *
	 * @param idJob   die Job-ID
	 *
	 * @return die Response
	 */
	private static @NotNull SimpleOperationResponse buildJobNotFoundResponse(final long idJob) {
		final SimpleOperationResponse resp = new SimpleOperationResponse();
		resp.success = false;
		resp.log.add("Job nicht gefunden mit Job-ID " + idJob);
		return resp;
	}


	/**
	 * Liefert den Job-Status zu der übergebenen Job-ID und erzeugt die zugehörige HTTP-Response.
	 *
	 * @param id   die Job-ID
	 *
	 * @return die HTTP-Response
	 */
	public @NotNull Response getEmailJobStatus(final long id) {
		final SimpleOperationResponse resp = new SimpleOperationResponse();
		try {
			final EmailJob job = getJobByID(conn, id);
			resp.success = (job.getStatus() == EmailJobStatus.COMPLETED_SUCCESSFULLY);
			resp.log.add("Job-ID = " + job.getId());
			resp.log.add("Status = " + job.getStatus());
			resp.log.add("Gesamtzahl-Empfänger = " + job.getRecipients().size());
			resp.log.add("Gesendet = " + job.getEmailsSent());
			resp.log.add("Übersprungen = " + job.logSkipped.size());
			resp.log.add("Fehler = " + job.logError.size());
			return Response.ok(resp).type(MediaType.APPLICATION_JSON).build();
		} catch (final ApiOperationException e) {
			resp.success = false;
			resp.log.add(e.getBody().toString());
			return Response.status(e.getStatus()).type(MediaType.APPLICATION_JSON).entity(resp).build();
		}
	}


	/**
	 * Liefert den Log für den Email-Job mit der übergebenen Job-ID und erzeugt die zugehörige HTTP-Response.
	 *
	 * @param id   die Job-ID
	 *
	 * @return die HTTP-Response
	 */
	public @NotNull Response getEmailJobLog(final long id) {
		final SimpleOperationResponse resp = new SimpleOperationResponse();
		try {
			final EmailJob job = getJobByID(conn, id);
			resp.success = true;
			resp.log.add("Job-ID = " + job.getId());
			resp.log.add("Status = " + job.getStatus());
			resp.log.addAll(job.logSkipped);
			resp.log.addAll(job.logError);
			if (resp.log.isEmpty())
				resp.log.add("Es ist noch kein Log vorhanden.");
			return Response.ok(resp).type(MediaType.APPLICATION_JSON).build();
		} catch (final ApiOperationException e) {
			resp.success = false;
			resp.log.add(e.getBody().toString());
			return Response.status(e.getStatus()).type(MediaType.APPLICATION_JSON).entity(resp).build();
		}
	}


	/**
	 * Ermittelt die EMail-Konfiguration des aktuellen Benutzer anhand der Datenbank
	 * und entschlüsselt mit dessen Anmeldedaten die SMTP-Server-Konfiguration
	 *
	 * @param conn   die Datenbank-Verbindung
	 *
	 * @return die E-Mail-Konfiguration
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static @NotNull MailSmtpSessionConfig getSMTPConfig(final DBEntityManager conn) throws ApiOperationException {
		try {
			final AES aes = conn.getUser().getAES();
			final SMTPServerKonfiguration daten = DataEmailSMTPServerKonfiguration.getOrCreateSMTPServerKonfiguration(conn);
			final DTOBenutzerMail benutzerDaten = DataBenutzerEMailDaten.getOrCreateDTO(conn);
			final String decodedPassword = new String(aes.decryptBase64(benutzerDaten.SMTPPassword));
			final MailSmtpSessionConfig config = new MailSmtpSessionConfig(daten.host, benutzerDaten.SMTPUsername, decodedPassword);
			config.setPort(daten.port);
			config.setTLS(daten.useTLS);
			config.setStartTLS(daten.useStartTLS);
			return config;
		} catch (@SuppressWarnings("unused") final AESException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Fehler beim Entschlüsseln des SMTP-Kennwortes, es kann keine SMTP-Session erstellt werden.");
		} catch (final Exception e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen der SMTP-Session: " + e.getMessage());
		}
	}


	/**
	 * Erstellt für die übergebene Datenbank-Verbindung einen Kontext mit Default-Einstellungen für die
	 * Verwendung in einem {@link EmailJobManager}. Dabei wird eine SMTP-Session ({@link MailSmtpSession})
	 * für den Kontext anhand der in der Datenbank gespeicherten SMTP-Konfiguration erzeugt.
	 *
	 * @param conn   die Datenbank-Verbindung
	 *
	 * @return der Kontext
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static @NotNull EmailJobManagerContext getDefaultJobManagerContext(final DBEntityManager conn) throws ApiOperationException {
		final MailSmtpSession session = new MailSmtpSession(getSMTPConfig(conn));
		return new EmailJobManagerContext(conn.getDBSchema(), conn.getUser().getId(), session)
				.withForceMaxAttachmentSize(false)
				.withMaxAttachmentSize(8388608) // 8 MByte
				.withMaxEmailsPerMinute(20)
				.withTimeToKeepCompletedJobs(60000); // 1 Minute
	}

}
