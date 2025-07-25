package de.svws_nrw.api.server;

import java.io.InputStream;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.enm.ENMConfigResponse;
import de.svws_nrw.core.data.enm.ENMDaten;
import de.svws_nrw.core.data.enm.ENMLehrerInitialKennwort;
import de.svws_nrw.core.data.enm.ENMLeistung;
import de.svws_nrw.core.data.enm.ENMLeistungBemerkungen;
import de.svws_nrw.core.data.enm.ENMLernabschnitt;
import de.svws_nrw.core.data.enm.ENMServerConfigElement;
import de.svws_nrw.core.data.enm.ENMTeilleistung;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.SimpleBinaryMultipartBody;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.enm.DataENMDaten;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
	 * Leerer Standardkonstruktor.
	 */
	public APIENM() {
		// leer
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage einer leeren Datendatei für das Externe Datenmodul (ENM).
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die leeren ENM-Daten
	 */
	@GET
	@Path("/empty")
	@Operation(summary = "Liefert leere Daten des Externen Notenmoduls (ENM).",
			description = "Liefert leere Daten des Externen Notenmoduls (ENM).")
	@ApiResponse(responseCode = "200", description = "Die Daten des Externen Notenmoduls (ENM)",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ENMDaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um auf die API zuzugreifen.")
	public Response getENMDatenLeer(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(new ENMDaten()).build();
	}



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
	 * Die OpenAPI-Methode für das Patchen von ENM-Leistungsdaten direkt auf dem SVWS-Server.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/leistung")
	@Operation(summary = "Patch für die ENM-Leistungsdaten.",
			description = "Passt die Leistungsdaten eines Schüler anhand der ENM-Daten an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung im Rahmen der Notenmodul-Konfiguration besitzt.")
	@ApiResponse(responseCode = "204", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der, im Patch angegebenen, ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchENMLeistung(@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = ENMLeistung.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataENMDaten(conn).patchENMLeistung(is),
				request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN, BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_FUNKTION);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen von ENM-Teilleistungsdaten direkt auf dem SVWS-Server.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/teilleistung")
	@Operation(summary = "Patch für die ENM-Teilleistungsdaten.",
			description = "Passt die Teilleistungsdaten eines Schüler anhand der ENM-Daten an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung im Rahmen der Notenmodul-Konfiguration besitzt.")
	@ApiResponse(responseCode = "204", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der, im Patch angegebenen, ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchENMTeilleistung(@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = ENMTeilleistung.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataENMDaten(conn).patchENMTeilleistung(is),
				request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN, BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_FUNKTION);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen von ENM-Bemerkungen zu einem Schülerlernabschnitt direkt auf dem SVWS-Server.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die ID des Schülers
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/bemerkungen/{id : \\d+}")
	@Operation(summary = "Patch für die Bemerkungen eines Schülers anhand der ENM-Daten.",
			description = "Passt die Bemerkungen eines Schülers anhand der ENM-Daten an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung im Rahmen der Notenmodul-Konfiguration besitzt.")
	@ApiResponse(responseCode = "204", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der, im Patch angegebenen, ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchENMSchuelerBemerkungen(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = ENMLeistungBemerkungen.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataENMDaten(conn).patchENMSchuelerBemerkungen(id, is),
				request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN, BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_FUNKTION);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen von ENM-Lernabschnittsdaten direkt auf dem SVWS-Server.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/lernabschnitt")
	@Operation(summary = "Patch für die ENM-Lernabschnittsdaten.",
			description = "Passt die Lernabschnittsdaten eines Schüler anhand der ENM-Daten an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung im Rahmen der Notenmodul-Konfiguration besitzt.")
	@ApiResponse(responseCode = "204", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der, im Patch angegebenen, ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchENMSchuelerLernabschnitt(@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = ENMLernabschnitt.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataENMDaten(conn).patchENMSchuelerlernabschnitt(is),
				request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN, BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_FUNKTION);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Initialkennwörter für die Lehrer, welche bei den Daten für das Externe Datenmodul (ENM)
	 * vorkommen.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den Initialkennwörtern
	 */
	@GET
	@Path("/alle/initial_kennwoerter")
	@Operation(summary = "Liefert eine Liste der Lehrer-IDs mit den zugehörigen Initialkennwörtern.",
			description = "Liefert eine Liste der Lehrer-IDs mit den zugehörigen Initialkennwörtern für Lehrer zurück, welche bei den "
					+ "Daten für das Externe Datenmodul (ENM) vorkommen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zur Administration der Notenmodul-Daten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste mit den Initialkennwörtern",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ENMLehrerInitialKennwort.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Initialkennwörter des ENM zu verwalten.")
	public Response getENMLehrerInitialKennwoerter(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataENMDaten(conn).getLehrerInitialkennwoerter(),
				request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
	}


	/**
	 * Die OpenAPI-Methode zum Generieren von Initial-Kennwörtern für Lehrer für das externe Notenmodul, sofern diese noch keine haben.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Reponse
	 */
	@POST
	@Path("/credentials/generate")
	@Operation(summary = "Generiert Initial-Kennwörter für Lehrer für das externe Notenmodul, sofern diese noch keine haben.",
			description = "Generiert Initial-Kennwörter für Lehrer für das externe Notenmodul, sofern diese noch keine haben.")
	@ApiResponse(responseCode = "204", description = "Die Kennwörter und die Password-Hashes wurden erzeugt oder es war nichts zu tun.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte zum Erzeugen der Credentials.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response generateENMLehrerCredentials(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> {
			DataENMDaten.generateInitialCredentials(conn);
			return Response.status(Status.NO_CONTENT).build();
		}, request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
	}


	/**
	 * Die OpenAPI-Methode zum Zurücksetzen eines Kennwortes für Lehrer für das externe Notenmodul. Hat der Lehrer
	 * noch kein Initialkennwort, so wird dieses neu erzeugt.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die ID des Lehrers
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Reponse
	 */
	@POST
	@Path("/credentials/reset/{id : \\d+}")
	@Operation(summary = "Setzt das Kennwort des Lehrers für das externe Notenmodul auf das Initial-Kennwort zurück.",
			description = "Setzt das Kennwort des Lehrers für das externe Notenmodul auf das Initial-Kennwort zurück. "
					+ "Ist noch kein Initialkennwort gesetzt, so wird ein neues erzeugt.")
	@ApiResponse(responseCode = "204", description = "Das Initial-Kennwort wurde gesetzt.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte zum Setzen des Kennwortes.")
	@ApiResponse(responseCode = "404", description = "Die ID des Lehrers ist in der DB nicht vorhanden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response resetENMLehrerPasswordToInitial(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> {
			DataENMDaten.resetInitialPassword(conn, id);
			return Response.status(Status.NO_CONTENT).build();
		}, request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
	}


	/**
	 * Die OpenAPI-Methode zum Setzen eines Kennwortes für Lehrer für das externe Notenmodul.
	 * Hat der Lehrer noch kein Initialkennwort, so wird dieses zusätzlich neu erzeugt, allerdings
	 * das übergebene aktiviert.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id         die ID des Lehrers
	 * @param is         der Input-Stream mit dem zu setzenden Kennwort
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Reponse
	 */
	@POST
	@Path("/credentials/set/{id : \\d+}")
	@Operation(summary = "Setzt das Kennwort des Lehrers für das externe Notenmodul auf das übergebene Kennwort.",
			description = "Setzt das Kennwort des Lehrers für das externe Notenmodul auf das übergebene Kennwort. "
					+ "Ist noch kein Initialkennwort gesetzt, so wird ein neues erzeugt, allerdings das übergebene Kennwort gesetzt.")
	@ApiResponse(responseCode = "204", description = "Das Kennwort wurde gesetzt.")
	@ApiResponse(responseCode = "400", description = "Das Kennwort ist leer oder entspricht nicht den Minimal-Anforderungen.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte zum Setzen des Kennwortes.")
	@ApiResponse(responseCode = "404", description = "Die ID des Lehrers ist in der DB nicht vorhanden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response setENMLehrerPassword(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Das Kennwort", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = String.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> {
			final String password = JSONMapper.toString(is);
			DataENMDaten.setPassword(conn, id, password);
			return Response.status(Status.NO_CONTENT).build();
		}, request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
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
	 * @return die HTTP-Response mit einer SimpleOperationResponse
	 */
	@GET
	@Path("/synchronize")
	@Operation(summary = "Synchronisiert die Daten des Externen Notenmoduls (ENM).",
			description = "Liest die Daten des Externen Notenmoduls (ENM) aller Lehrer aus der Datenbank "
					+ "und lädt diese als ZIP beim ENM hoch, lädt danach die Daten des ENM runter und speichert diese in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten des Externen Notenmoduls (ENM) wurden synchronisiert",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Interner Serverfehler",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "401", description = "Die Authorisierung beim ENM-Server ist fehlgeschlagen.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.")
	@ApiResponse(responseCode = "404", description = "Keine ENM-Serverdaten gefunden.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "502", description = "Fehler bei der Verbindung zum ENM-Server",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
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
	 * @return die HTTP-Response mit einer SimpleOperationResponse
	 */
	@GET
	@Path("/upload")
	@Operation(summary = "Lädt die ENM-Daten beim Externen Notenmodul (ENM) hoch.",
			description = "Liest die Daten des Externen Notenmoduls (ENM) aller Lehrer aus der Datenbank und lädt diese als ZIP beim ENM hoch."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten des Externen Notenmoduls (ENM) wurden hochgeladen",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Interner Serverfehler",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "401", description = "Die Authorisierung beim ENM-Server ist fehlgeschlagen.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.")
	@ApiResponse(responseCode = "404", description = "Keine ENM-Serverdaten gefunden.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "502", description = "Fehler bei der Verbindung zum ENM-Server",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response uploadENMDaten(@PathParam("schema") final String schema,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(DataENMDaten::upload, request,
				ServerMode.STABLE,
				BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
	}


	/**
	 * Die OpenAPI-Methode für den Download von Daten aus dem Externe Datenmodul (ENM) in Bezug auf alle Lehrer.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit einer SimpleOperationResponse
	 */
	@GET
	@Path("/download")
	@Operation(summary = "Lädt die Daten vom Externen Notenmodul (ENM).",
			description = "Importiert die Daten des Externen Notenmoduls und speichert diese in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten des Externen Notenmoduls (ENM) wurden heruntergeladen",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Interner Serverfehler",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "401", description = "Die Authorisierung beim ENM-Server ist fehlgeschlagen.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.")
	@ApiResponse(responseCode = "404", description = "Keine ENM-Serverdaten gefunden.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "502", description = "Fehler bei der Verbindung zum ENM-Server",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response downloadENMDaten(@PathParam("schema") final String schema,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(DataENMDaten::download, request,
				ServerMode.STABLE,
				BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen aller ENM-Daten aus dem Externen Datenmodul (ENM) - einschließlich der Benutzerdaten.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit einer SimpleOperationResponse
	 */
	@GET
	@Path("/truncate")
	@Operation(summary = "Leert die Daten des Externen Notenmoduls (ENM), einschließlich der Benutzerdaten.",
			description = "Leert die Daten des Externen Notenmoduls (ENM), einschließlich der Benutzerdaten."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten des Externen Notenmoduls (ENM) wurden geleert.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Interner Serverfehler",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "401", description = "Die Authorisierung beim ENM-Server ist fehlgeschlagen.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.")
	@ApiResponse(responseCode = "404", description = "Keine ENM-Serverdaten gefunden.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "502", description = "Fehler bei der Verbindung zum ENM-Server, u.U. auch fehlende OAuth-Daten.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response truncateENMServer(@PathParam("schema") final String schema,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(DataENMDaten::truncate, request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen aller ENM-Daten aus dem Externen Datenmodul (ENM).
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit einer SimpleOperationResponse
	 */
	@GET
	@Path("/reset")
	@Operation(summary = "Leert die Daten des Externen Notenmoduls (ENM).", description = "Leert die Daten des Externen Notenmoduls (ENM)."
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten des Externen Notenmoduls (ENM) wurden geleert.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Interner Serverfehler",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "401", description = "Die Authorisierung beim ENM-Server ist fehlgeschlagen.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.")
	@ApiResponse(responseCode = "404", description = "Keine ENM-Serverdaten gefunden.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "502", description = "Fehler bei der Verbindung zum ENM-Server, u.U. auch fehlende OAuth-Daten.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response resetENMServer(@PathParam("schema") final String schema,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(DataENMDaten::reset, request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
	}


	/**
	 * Die OpenAPI-Methode für das Überprüfen, ob der ENM-Server mit den hinterlegten Verbindungsdaten erreichbar ist.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit einer SimpleOperationResponse
	 */
	@GET
	@Path("/check")
	@Operation(summary = "Prüft, ob der ENM-Server mit den hinterlegten Verbindungsdaten erreichbar ist.",
			description = "Prüft, ob der ENM-Server mit den hinterlegten Verbindungsdaten erreichbar ist."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der ENM-Server ist erreichbar.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Interner Serverfehler",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "401", description = "Die Authorisierung beim ENM-Server ist fehlgeschlagen.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Operation auszuführen.")
	@ApiResponse(responseCode = "404", description = "Keine ENM-Serverdaten gefunden.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "502", description = "Fehler bei der Verbindung zum ENM-Server, u.U. auch fehlende OAuth-Daten.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response checkENMServer(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(DataENMDaten::check, request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
	}


	/**
	 * Die OpenAPI-Methode für das Holen der ENM-Serverkonfiguration.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit einer ENMConfigResponse
	 */
	@GET
	@Path("/config")
	@Operation(summary = "Holt die Konfiguration.",
			description = "Ein Getter für die ENM-Server-Konfiguration.")
	@ApiResponse(responseCode = "200", description = "Die Konfiguration konnte erfolgreich abgerufen werden.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ENMConfigResponse.class)))
	@ApiResponse(responseCode = "500", description = "Interner Serverfehler",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ENMConfigResponse.class)))
	@ApiResponse(responseCode = "401", description = "Die Authorisierung beim ENM-Server ist fehlgeschlagen.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ENMConfigResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Operation auszuführen.")
	@ApiResponse(responseCode = "404", description = "Keine ENM-Serverdaten gefunden.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ENMConfigResponse.class)))
	@ApiResponse(responseCode = "502", description = "Fehler bei der Verbindung zum ENM-Server, u.U. auch fehlende OAuth-Daten.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ENMConfigResponse.class)))
	public Response getENMServerConfig(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(DataENMDaten::getENMServerConfig, request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
	}


	/**
	 * Die OpenAPI-Methode für das Setzen eines Eintrages in dem ENM-Serverkonfiguration, bzw. der globalen
	 * ENM-Clientkonfiguration.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param daten   der InputStream mit einem Konfigurationseintrag, der gesetzt werden soll
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit einer SimpleOperationResponse
	 */
	@POST
	@Path("/config")
	@Operation(summary = "Schreibt den Konfigurationseintrag für den angebenen Schlüsselwert in die Konfiguration",
			description = "Schreibt den Konfigurationseintrag für den angebenen Schlüsselwert in die Konfiguration.")
	@ApiResponse(responseCode = "204", description = "Der Konfigurationseintrag wurde erfolgreich geschrieben",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "500", description = "Interner Serverfehler",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "401", description = "Die Authorisierung beim ENM-Server ist fehlgeschlagen.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Operation auszuführen.")
	@ApiResponse(responseCode = "404", description = "Keine ENM-Serverdaten gefunden.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "502", description = "Fehler bei der Verbindung zum ENM-Server, u.U. auch fehlende OAuth-Daten.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response setENMServerConfigElement(@PathParam("schema") final String schema,
			@RequestBody(description = "Der zu setzende Konfigurationseintrag", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ENMServerConfigElement.class))) final InputStream daten,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> DataENMDaten.setENMServerConfigElement(conn, daten), request, ServerMode.STABLE,
				BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
	}


	/**
	 * Die OpenAPI-Methode für das Ausführen des initialen Setups eines ENM-Servers. Gibt bei Erfolg true zurück und wenn
	 * der Server bereits initialisiert wurde, false. Im Rahmen dieses Aufrufs wird auch das TLS-Zertifikat des Servers
	 * geprüft und ggf. in der Datenbank aktualisiert.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Response mit einem boolean
	 */
	@GET
	@Path("/setup")
	@Operation(summary = "Führt das initiale Setup des ENM-Servers durch",
			description = "Dieser Aufruf initialisert den ENM-Server beim ersten Aufruf. Weitere Aufrufe führen zu einem Fehler.")
	@ApiResponse(responseCode = "200", description = "Der Stand des Setups, true, wurde initialisiert, false ist bereits initialisiert",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
	@ApiResponse(responseCode = "500", description = "Interner Serverfehler",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "401", description = "Die Authorisierung beim ENM-Server ist fehlgeschlagen.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Operation auszuführen.")
	@ApiResponse(responseCode = "404", description = "Keine ENM-Serverdaten gefunden.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "409", description = "Der TLS-Zertifikat des ENM-Server wird nicht vertraut.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "502", description = "Fehler bei der Verbindung zum ENM-Server, u.U. auch fehlende OAuth-Daten.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response setupENMServer(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(DataENMDaten::setup, request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
	}

}
