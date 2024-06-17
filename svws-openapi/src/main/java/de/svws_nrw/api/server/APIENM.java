package de.svws_nrw.api.server;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import de.svws_nrw.core.data.enm.ENMDaten;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.SimpleBinaryMultipartBody;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.enm.DataENMDaten;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für die Arbeit mit den
 * grundlegenden Daten des Externen Notenmoduls (ENM).
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/enm/...
 */
@Path("/db/{schema}/enm")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIENM {


	/**
	 * Die OpenAPI-Methode für die Abfrage der Daten für das Externe Datenmodul (ENM) in Bezug auf alle Lehrer
	 * des aktuellen Schuljahresabschnitts der Schule.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die ENM-Daten
	 */
	@GET
	@Path("/alle")
	@Operation(summary = "Liefert die Daten des Externen Notenmoduls (ENM).",
			description = "Liest die Daten des Externen Notenmoduls (ENM) aus der Datenbank "
					+ "und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten des Externen Notenmoduls (ENM)",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ENMDaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.")
	@ApiResponse(responseCode = "404", description = "Es wurden nicht alle benötigten Daten für das Erstellen der ENM-Daten gefunden.")
	public Response getENMDaten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataENMDaten(conn).getAll(),
				request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Daten für das Externe Datenmodul (ENM) in Bezug auf alle Lehrer
	 * des aktuellen Schuljahresabschnitts der Schule als GZIP-Json.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die ENM-Daten
	 */
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/alle/gzip")
	@Operation(summary = "Liefert die Daten des Externen Notenmoduls (ENM) GZip-komprimiert.",
			description = "Liest die Daten des Externen Notenmoduls (ENM) aus der Datenbank "
					+ "und liefert diese GZip-komprimiert zurück. Dabei wird geprüft, ob der SVWS-Benutzer die "
					+ "notwendige Berechtigung zum Auslesen der Notendaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die GZip-komprimierte ENM-JSON-Datei", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM,
			schema = @Schema(type = "string", format = "binary", description = "Die GZip-komprimierte ENM-JSON-Datei")))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.")
	@ApiResponse(responseCode = "404", description = "Es wurden nicht alle benötigten Daten für das Erstellen der ENM-Daten gefunden.")
	public Response getENMDatenGZip(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataENMDaten(conn).getAllGZip(),
				request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN);
	}



	/**
	 * Die OpenAPI-Methode für die Abfrage der Daten für das Externe Datenmodul (ENM) in Bezug auf einen Lehrer.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Lehrers
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Daten für das ENM des Lehrers
	 */
	@GET
	@Path("/lehrer/{id : \\d+}")
	@Operation(summary = "Liefert zu der ID des Lehrer die zugehörigen Daten des Externen Notenmoduls (ENM).",
			description = "Liest die Daten des Externen Notenmoduls (ENM) des Lehrers zu der angegebenen ID aus der Datenbank "
					+ "und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten des Externen Notenmoduls (ENM) des Lehrers",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ENMDaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.")
	@ApiResponse(responseCode = "404", description = "Kein Lehrer-Eintrag mit der angegebenen ID gefunden")
	public Response getLehrerENMDaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataENMDaten(conn).get(id),
				request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_FUNKTION, BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN);
	}


	/**
	 * Die OpenAPI-Methode zum Importieren von ENM-Daten in die SVWS-Datenbank. Dabei wird die
	 * Aktualität der zu importierenden Daten anhand der Zeitstempel in den ENM-Daten geprüft.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param enmDaten   die ENM-Daten
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Reponse
	 */
	@POST
	@Path("/import")
	@Operation(summary = "Importiert die übergebenen ENM-Daten.",
			description = "Importiert die übergebenen ENM-Daten. Dabei wird die Aktualität der zu importierenden Daten anhand "
					+ "der Zeitstempel in den ENM-Daten geprüft.")
	@ApiResponse(responseCode = "204", description = "Die ENM-Daten wurden erfolgreich importiert.")
	@ApiResponse(responseCode = "400", description = "Die ENM-Daten sind nicht korrekt.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte zum importieren.")
	@ApiResponse(responseCode = "404", description = "Es wurden nicht alle benötigten Daten für den Abgleich in der DB gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response importENMDaten(@PathParam("schema") final String schema, @RequestBody(description = "Die ENM-Daten", required = true,
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ENMDaten.class))) final ENMDaten enmDaten,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> {
			DataENMDaten.importDaten(conn, enmDaten);
			return Response.status(Status.NO_CONTENT).build();
		}, request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
	}


	/**
	 * Die OpenAPI-Methode zum Importieren von ENM-Daten in die SVWS-Datenbank. Dabei wird die
	 * Aktualität der zu importierenden Daten anhand der Zeitstempel in den ENM-Daten geprüft.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param multipart  enthält die ENM-Daten
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Reponse
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/import/gzip")
	@Operation(summary = "Importiert die übergebenen ENM-Daten.",
			description = "Importiert die übergebenen ENM-Daten. Dabei wird die Aktualität der zu importierenden Daten anhand der Zeitstempel in den ENM-Daten "
					+ "geprüft.")
	@ApiResponse(responseCode = "204", description = "Die ENM-Daten wurden erfolgreich importiert.")
	@ApiResponse(responseCode = "400", description = "Die ENM-Daten sind nicht korrekt.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte zum importieren.")
	@ApiResponse(responseCode = "404", description = "Es wurden nicht alle benötigten Daten für den Abgleich in der DB gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response importENMDatenGZip(@PathParam("schema") final String schema,
			@RequestBody(description = "Die ENM-Daten", required = true,
					content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA)) @MultipartForm final SimpleBinaryMultipartBody multipart,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> {
			DataENMDaten.importDatenGZip(conn, multipart.data);
			return Response.status(Status.NO_CONTENT).build();
		}, request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
	}


	/**
	 * Die OpenAPI-Methode für die Synchronisation der Daten für das Externe Datenmodul (ENM) in Bezug auf alle Lehrer.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return Die HTTP-Response
	 */
	@GET
	@Path("/synchronize")
	@Operation(summary = "Synchronisiert die Daten des Externen Notenmoduls (ENM).",
			description = "Liest die Daten des Externen Notenmoduls (ENM) aller Lehrer aus der Datenbank "
					+ "und lädt diese als ZIP beim ENM hoch, lädt danach die Daten des ENM runter und speichert diese in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten des Externen Notenmoduls (ENM) wurden synchronisiert",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
	@ApiResponse(responseCode = "500", description = "Interner Serverfehler, bspw. beim Lesen in der DB oder Erstellen des Zipfiles.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.")
	@ApiResponse(responseCode = "404", description = "Keine WeNoM-Serverdaten gefunden.")
	@ApiResponse(responseCode = "502", description = "Fehler bei der Verbindung zum WeNoM-Server")
	public Response synchronizeENMDaten(@PathParam("schema") final String schema,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(DataENMDaten::synchronize, request,
				ServerMode.STABLE,
				BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
	}


	/**
	 * Die OpenAPI-Methode für den Upload von Daten an das Externe Datenmodul (ENM) in Bezug auf alle Lehrer.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return Die HTTP-Response
	 */
	@GET
	@Path("/upload")
	@Operation(summary = "Lädt die ENM-Daten beim Externen Notenmodul (ENM) hoch.",
			description = "Liest die Daten des Externen Notenmoduls (ENM) aller Lehrer aus der Datenbank und lädt diese als ZIP beim ENM hoch."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten des Externen Notenmoduls (ENM) wurden hochgeladen",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
	@ApiResponse(responseCode = "500", description = "Interner Serverfehler, bspw. beim Lesen in der DB oder Erstellen des Zipfiles.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.")
	@ApiResponse(responseCode = "404", description = "Keine WeNoM-Serverdaten gefunden.")
	@ApiResponse(responseCode = "502", description = "Fehler bei der Verbindung zum WeNoM-Server")
	public Response uploadENMDaten(@PathParam("schema") final String schema,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(DataENMDaten::upload, request,
				ServerMode.STABLE,
				BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
	}


	/**
	 * Die OpenAPI-Methode für den Upload von Daten an das Externe Datenmodul (ENM) in Bezug auf alle Lehrer.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return true, wenn der Datenimport erfolgreich war
	 */
	@GET
	@Path("/download")
	@Operation(summary = "Lädt die Daten vom Externen Notenmodul (ENM).",
			description = "Importiert die Daten des Externen Notenmoduls und speichert diese in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten des Externen Notenmoduls (ENM) wurden heruntergeladen",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
	@ApiResponse(responseCode = "500", description = "Interner Serverfehler, bspw. beim Lesen in der DB oder Erstellen des Zipfiles.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.")
	@ApiResponse(responseCode = "404", description = "Keine WeNoM-Serverdaten gefunden.")
	@ApiResponse(responseCode = "502", description = "Fehler bei der Verbindung zum WeNoM-Server")
	public Response downloadENMDaten(@PathParam("schema") final String schema,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(DataENMDaten::download, request,
				ServerMode.STABLE,
				BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
	}


	/**
	 * Die OpenAPI-Methode für den Upload von Daten an das Externe Datenmodul (ENM) in Bezug auf alle Lehrer.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return eine Response mit Boolean True, wenn das Truncate erfolgreich war
	 */
	@GET
	@Path("/truncate")
	@Operation(summary = "Leert die Daten des Externen Notenmoduls (ENM).", description = "Leert die Daten des Externen Notenmoduls."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten des Externen Notenmoduls (ENM) wurden geleert.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.")
	@ApiResponse(responseCode = "404", description = "Keine WeNoM-Serverdaten gefunden.")
	@ApiResponse(responseCode = "502", description = "Fehler bei der Verbindung zum WeNoM-Server, u.U. auch fehlende OAuth-Daten.")
	public Response truncateENMServer(@PathParam("schema") final String schema,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(DataENMDaten::truncate, request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
	}

}
