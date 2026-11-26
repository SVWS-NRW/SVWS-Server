package de.svws_nrw.api.server;

import java.io.InputStream;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.email.EmailSendRequest;
import de.svws_nrw.core.data.email.SMTPServerKonfiguration;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.email.DataEmailJobs;
import de.svws_nrw.data.email.DataEmailSMTPServerKonfiguration;
import de.svws_nrw.data.email.DataEmailSend;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die Email-Funktionen des SVWS-Servers.
 */
@Path("/db/{schema}/email")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIEmail {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIEmail() {
		// leer
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der SMTP-Server-Konfiguration der Schule.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die SMTP-Server-Konfiguration
	 */
	@GET
	@Path("/smtp/server/konfiguration")
	@Operation(summary = "Gibt die SMTP-Server-Konfiguration der Schule zurück.",
			description = "Gibt die SMTP-Server-Konfiguration der Schule zurück.")
	@ApiResponse(responseCode = "200", description = "Die SMTP-Server-Konfiguration",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SMTPServerKonfiguration.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuldaten zu anzusehen.")
	public Response getSMTPServerKonfiguration(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> DataEmailSMTPServerKonfiguration.get(conn),
				request, ServerMode.STABLE, BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Anpassen der SMTP-Server-Konfiguration der Schule. Sie verlangt das Admin-Recht, weil die
	 * Konfiguration bestimmt, an welchen Server alle Benutzer ihre SMTP-Zugangsdaten senden.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/smtp/server/konfiguration")
	@Operation(summary = "Passt die SMTP-Server-Konfiguration an und speichert das Ergebnis in der Datenbank.",
			description = "Passt die SMTP-Server-Konfiguration an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer Admin-Rechte besitzt.")
	@ApiResponse(responseCode = "204", description = "Der Patch wurde erfolgreich in die SMTP-Server-Konfiguration integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Admin-Rechte.")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchSMTPServerKonfiguration(
			@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für die SMTP-Server-Konfiguration", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = SMTPServerKonfiguration.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataEmailSMTPServerKonfiguration(conn).patch(is),
				request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
	}



	/**
	 * Die OpenAPI-Methode für den Versand einer E-Mail an eine Liste von Empfängern. Jede Adresse erhält eine eigene E-Mail.
	 *
	 * @param schema    das Datenbankschema
	 * @param data      der Auftrag mit Betreff, Text, Empfängern und Anhängen
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return SimpleOperationResponse mit der Job-ID
	 */
	@POST
	@Path("/send")
	@Operation(summary = "Startet den Versand einer E-Mail an eine Liste von Empfängern.",
			description = "Startet den Versand einer E-Mail an eine Liste von Empfängern als Job im Hintergrund. Jede Adresse erhält eine eigene E-Mail "
					+ "mit demselben Betreff, Text und denselben Anhängen. Absender ist der angemeldete Benutzer mit seinen SMTP-Zugangsdaten. "
					+ "Ungültige Adressen werden übersprungen und im Log des Jobs vermerkt. Ein Auftrag hat höchstens 500 Empfänger. Erlaubt sind höchstens "
					+ "20 Anhänge vom Typ PDF, PNG und JPEG mit zusammen höchstens 8 MB. Nach einem Fehler, der nicht nur einen Empfänger betrifft, "
					+ "etwa bei Verbindung oder Anmeldung, endet der Job, und die übrigen Empfänger erhalten keine E-Mail. Status und Log des Jobs sind bis eine Minute nach "
					+ "seinem Ende abrufbar.")
	@ApiResponse(responseCode = "202", description = "Der Job wurde gestartet. Die Antwort enthält die Job-ID.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "400", description = "Der Auftrag ist fehlerhaft, enthält keine gültige Adresse oder dem Benutzer fehlt eine Absenderadresse.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um E-Mails zu versenden.")
	@ApiResponse(responseCode = "429", description = "Es warten oder laufen bereits drei E-Mail-Versand-Jobs des Benutzers.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. fehlende SMTP-Konfiguration)")
	public Response sendEmail(@PathParam("schema") final String schema,
			@RequestBody(description = "Der Auftrag für den E-Mail-Versand", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = EmailSendRequest.class))) final EmailSendRequest data,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataEmailSend(conn).send(data), request, ServerMode.DEV,
				BenutzerKompetenz.EMAIL_VERSAND_API);
	}

	/**
	 * Liefert den Status eines gestarteten E-Mail-Jobs.
	 *
	 * @param schema das Datenbankschema
	 * @param jobId  die Job-ID
	 * @param request HTTP-Request
	 *
	 * @return SimpleOperationResponse mit Status-Informationen oder NOT_FOUND
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/email-jobs/{jobId}/status")
	@Operation(summary = "Gibt den Status eines E-Mail-Versandjobs zurück.",
			description = "Liefert den aktuellen Status und Kennzahlen (Gesamt, Gesendete, Übersprungene, Fehler) des angegebenen E-Mail-Jobs.")
	@ApiResponse(responseCode = "200", description = "Status wurde ermittelt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "404", description = "Job nicht gefunden.")
	public Response getEmailJobStatus(@PathParam("schema") final String schema,
			@PathParam("jobId") final long jobId,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataEmailJobs(conn).getEmailJobStatus(jobId), request, ServerMode.DEV,
				BenutzerKompetenz.BERICHTE_STANDARDFORMULARE_DRUCKEN,
				BenutzerKompetenz.BERICHTE_ALLE_FORMULARE_DRUCKEN,
				BenutzerKompetenz.EMAIL_VERSAND_API);
	}

	/**
	 * Liefert das Log eines gestarteten E-Mail-Jobs.
	 *
	 * @param schema das Datenbankschema
	 * @param jobId  die Job-ID
	 * @param request HTTP-Request
	 *
	 * @return SimpleOperationResponse mit Logeinträgen oder NOT_FOUND
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/email-jobs/{jobId}/log")
	@Operation(summary = "Gibt das Log eines E-Mail-Versandjobs zurück.",
			description = "Liefert die gesammelten Logeinträge (Hinweise, Fehler) des angegebenen E-Mail-Jobs.")
	@ApiResponse(responseCode = "200", description = "Log wurde ermittelt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "404", description = "Job nicht gefunden.")
	public Response getEmailJobLog(@PathParam("schema") final String schema,
			@PathParam("jobId") final long jobId,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataEmailJobs(conn).getEmailJobLog(jobId), request, ServerMode.DEV,
				BenutzerKompetenz.BERICHTE_STANDARDFORMULARE_DRUCKEN,
				BenutzerKompetenz.BERICHTE_ALLE_FORMULARE_DRUCKEN,
				BenutzerKompetenz.EMAIL_VERSAND_API);
	}

	/**
	 * Bricht einen gestarteten E-Mail-Job ab.
	 *
	 * @param schema das Datenbankschema
	 * @param jobId  die Job-ID
	 * @param request HTTP-Request
	 *
	 * @return SimpleOperationResponse mit dem Ergebnis des Abbruchs
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/email-jobs/{jobId}/cancel")
	@Operation(summary = "Bricht einen E-Mail-Versandjob ab.",
			description = "Bricht den angegebenen E-Mail-Job ab. Ein wartender Job endet sofort, ein laufender nach der aktuell versendeten E-Mail. "
					+ "War der Job bereits beendet, wird dies im Log gemeldet.")
	@ApiResponse(responseCode = "200", description = "Der Abbruch wurde veranlasst oder der Job war bereits beendet.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "404", description = "Job nicht gefunden.")
	@ApiResponse(responseCode = "409", description = "Der Abbruch ist fehlgeschlagen.")
	public Response cancelEmailJob(@PathParam("schema") final String schema,
			@PathParam("jobId") final long jobId,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataEmailJobs(conn).cancelEmailJob(jobId), request, ServerMode.DEV,
				BenutzerKompetenz.BERICHTE_STANDARDFORMULARE_DRUCKEN,
				BenutzerKompetenz.BERICHTE_ALLE_FORMULARE_DRUCKEN,
				BenutzerKompetenz.EMAIL_VERSAND_API);
	}

}
