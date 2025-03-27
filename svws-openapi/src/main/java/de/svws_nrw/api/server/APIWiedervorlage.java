package de.svws_nrw.api.server;

import java.io.InputStream;

import de.svws_nrw.core.data.schule.WiedervorlageEintrag;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.schule.DataWiedervorlage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die Wiedervorlage aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/wiedervorlage/...
 */
@Path("/db/{schema}/wiedervorlage")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIWiedervorlage {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIWiedervorlage() {
		// leer
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste Einträge in der Wiedervorlage des Benutzers,
	 * welche diese Anfrage gestellt hat.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den Einträge der Wiedervorlage
	 */
	@GET
	@Path("/liste")
	@Operation(summary = "Gibt die Liste der Wiedervorlage des angemeldeteten Benutzers zurück.",
			description = "Gibt die Liste der Wiedervorlage des angemeldeteten Benutzers zurück. "
					+ "Dabei werden auch die Einträge berücksichtigt, wo der angemeldete Benutzer in einer zugeordeten Benutzergruppe "
					+ "des Wiedervorlage-Eintrags enthalten ist.")
	@ApiResponse(responseCode = "200", description = "Eine Liste mit den Einträgen der Wiedervorlage.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = WiedervorlageEintrag.class))))
	public Response getWiedervorlageListe(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataWiedervorlage(conn).getListAsResponse(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage eines Wiedervorlage-Eintrags.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die ID des Wiedervorlage-Eintrags
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return der Wiedervorlage-Eintrag
	 */
	@GET
	@Path("/{id : \\d+}")
	@Operation(summary = "Liefert zu der ID den zugehörigen Wiedervorlage-Eintrag.",
			description = "Liefert zu der ID den zugehörigen Wiedervorlage-Eintrag. "
					+ "Dabei wird geprüft, ob der Benutzer auf den Eintrag zugreifen darf.")
	@ApiResponse(responseCode = "200", description = "Der Wiedervorlage-Eintrag",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = WiedervorlageEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den WiedervorlageEintrag anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein WiedervorlageEintrag mit der angegebenen ID gefunden")
	public Response getWiedervorlageEintrag(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataWiedervorlage(conn).getByIdAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen eines Wiedervorlage-Eintrags.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die ID des Wiedervorlage-Eintrags
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{id : \\d+}")
	@Operation(summary = "Passt den Wiedervorlage-Eintrag mit der angegebenen ID an.",
			description = "Passt den Wiedervorlage-Eintrag mit der angegebenen ID an. "
					+ "Dabei wird geprüft, ob der Benutzer auf den Eintrag zugreifen darf.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in den Wiedervorlage-Eintrag integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Wiedervorlage-Eintrag zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Wiedervorlage-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409",
			description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchWiedervorlageEintrag(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Wiedervorlage", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = WiedervorlageEintrag.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataWiedervorlage(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Erzeugt einen Wiedervorlage-Eintrag.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param is        der Input-Stream mit den Daten des Wiedervorlage-Eintrags
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Response im Erfolgsfall mit dem neuen Wiedervorlage-Eintrag
	 */
	@POST
	@Path("/neu")
	@Operation(summary = "Erstellt einen Wiedervorlage-Eintrag.",
			description = "Erstellt einen Wiedervorlage-Eintrag.")
	@ApiResponse(responseCode = "201", description = "Der Wiedervorlage-Eintrag",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = WiedervorlageEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Wiedervorlage-Eintrag anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addWiedervorlageEintrag(@PathParam("schema") final String schema, @RequestBody(description = "Der Wiedervorlage-Eintrag", required = true,
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = WiedervorlageEintrag.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataWiedervorlage(conn).addAsResponse(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen eines Wiedervorlage-Eintrags.
	 *
	 * @param schema    das Datenbankschema
	 * @param id        die ID des Wiedervorlage-Eintrags
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Wiedervorlage-Eintrag
	 */
	@DELETE
	@Path("/{id : \\d+}")
	@Operation(summary = "Entfernt einen Wiedervorlage-Eintrag.",
			description = "Entfernt einen Wiedervorlage-Eintrag. "
					+ "Dabei wird geprüft, ob der Benutzer auf den Eintrag zugreifen darf.")
	@ApiResponse(responseCode = "200", description = "Der Wiedervorlage-Eintrag wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = WiedervorlageEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um dem Wiedervorlage-Eintrag zu löschen.")
	@ApiResponse(responseCode = "404", description = "Kein Wiedervorlage-Eintrag mit der angegebenen ID gefunden.")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteWiedervorlageEintrag(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataWiedervorlage(conn).deleteAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Wiedervorlage-Einträge.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           die IDs der Wiedervorlage-Einträge
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. den gelöschten Wiedervorlage-Einträgen
	 */
	@DELETE
	@Path("/delete/multiple")
	@Operation(summary = "Entfernt mehrere Wiedervorlage-Einträge.",
			description = "Entfernt mehrere Wiedervorlage-Einträge. "
					+ "Dabei wird geprüft, ob der Benutzer auf die Einträge zugreifen darf.")
	@ApiResponse(responseCode = "200", description = "Die Wiedervorlage-Einträge wurden erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = WiedervorlageEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Wiedervorlage-Einträge zu entfernen.")
	@ApiResponse(responseCode = "404", description = "Wiedervorlage-Eintrag nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteWiedervorlageEintraege(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Wiedervorlage-Einträge", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataWiedervorlage(conn).deleteMultipleAsResponse(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Markiert einen Wiedervorlage-Eintrag als erledigt.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die ID des Wiedervorlage-Eintrags
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Response im Erfolgsfall mit dem angepassten Wiedervorlage-Eintrag
	 */
	@POST
	@Path("/{id : \\d+}/erledigt")
	@Operation(summary = "Markiert den Wiedervorlage-Eintrag mit der angegebenen ID als erledigt.",
			description = "Markiert den Wiedervorlage-Eintrag mit der angegebenen ID als erledigt."
					+ "Dabei wird geprüft, ob der Benutzer auf den Eintrag zugreifen darf.")
	@ApiResponse(responseCode = "201", description = "Der Wiedervorlage-Eintrag",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = WiedervorlageEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Wiedervorlage-Eintrag als erledigt zu markieren.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response setWiedervorlageEintragErledigt(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataWiedervorlage(conn).postErledigt(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}

}
