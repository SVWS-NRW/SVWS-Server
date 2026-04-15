package de.svws_nrw.api.server;

import java.io.InputStream;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Ankreuzkompetenz;
import de.svws_nrw.core.data.schule.AnkreuzkompetenzJahrgangszuordnung;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.schule.DataAnkreuzkompetenzJahrgangszuordnungen;
import de.svws_nrw.data.schule.DataAnkreuzkompetenzen;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Daten der Ankreuzkompetenzen aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/ankreuzkompetenzen/...
 */
@Path("/db/{schema}/schule/ankreuzkompetenzen")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIAnkreuzkompetenzen {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIAnkreuzkompetenzen() {
		// leer
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste der Ankreuzkompetenzen im angegebenen Schema.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Ankreuzkompetenzen des Datenbankschemas
	 */
	@GET
	@Path("/")
	@Operation(summary = "Gibt eine Übersicht von allen Ankreuzkompetenzen zurück.",
			description = "Erstellt eine Liste aller in der Datenbank vorhanden Ankreuzkompetenzen."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Ankreuzkompetenzen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Ankreuzkompetenz-Listen-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ankreuzkompetenz.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Ankreuzkompetenzen anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Ankreuzkompetenz-Einträge gefunden")
	public Response getAnkreuzkompetenzen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataAnkreuzkompetenzen(conn, new DataAnkreuzkompetenzJahrgangszuordnungen(conn)).getListAsResponse(),
				request, ServerMode.STABLE, BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN);
	}

	/**
	 * Die OpenAPI-Methode für das Hinzufügen einer Ankreuzkompetenz.
	 *
	 * @param schema    das Datenbankschema, auf welches der Create ausgeführt werden soll
	 * @param is        der Input-Stream mit den Daten der Ankreuzkompetenz
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der neuen Ankreuzkompetenz
	 */
	@POST
	@Path("/create")
	@Operation(summary = "Erstellt eine neue Ankreuzkompetenz und gibt das zugehörige Objekt zurück.",
			description = "Erstellt eine neue Ankreuzkompetenz und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Ankreuzkompetenzen besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Ankreuzkompetenz wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Ankreuzkompetenz.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response addAnkreuzkompetenz(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten des zu erstellenden Ankreuzkompetenz ohne ID, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Ankreuzkompetenz.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataAnkreuzkompetenzen(conn, null).addAsResponse(is), request,
				ServerMode.STABLE,
				BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen einer Ankreuzkompetenz.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Ankreuzkompetenz
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{id : \\d+}")
	@Operation(summary = "Passt die Ankreuzkompetenz mit der angegebenen ID an.",
			description = "Passt die Ankreuzkompetenz mit der angegebenen ID an. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Ankreuzkompetenzen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response patchAnkreuzkompetenz(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Ankreuzkompetenz", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Ankreuzkompetenz.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataAnkreuzkompetenzen(conn, null).patchAsResponse(id, is), request, ServerMode.STABLE,
				BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen der Ankreuzkompetenz.
	 *
	 * @param schema    das Datenbankschema
	 * @param is        der InputStream, mit der Liste von zu löschenden IDs
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. der gelöschten Ankreuzkompetenz
	 */
	@DELETE
	@Path("/delete/multiple")
	@Operation(summary = "Entfernt mehrere Ankreuzkompetenzen.", description = "Entfernt Ankreuzkompetenzen, insofern die Berechtigungen vorhanden sind")
	@ApiResponse(responseCode = "200", description = "Die Ankreuzkompetenzen wurden erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "400", description = "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Ankreuzkompetenzen zu löschen.")
	@ApiResponse(responseCode = "404", description = "Es wurden keine Entitäten zu den IDs gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response deleteAnkreuzkompetenzen(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Klassen", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransactionOnErrorSimpleResponse(
				conn -> new DataAnkreuzkompetenzen(conn, null).deleteMultipleAsSimpleResponseList(JSONMapper.toListOfLong(is)), request, ServerMode.STABLE,
				BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Hinzufügen einer AnkreuzkompetenzJahrgangszuordnung.
	 *
	 * @param schema    das Datenbankschema, auf welches der Create ausgeführt werden soll
	 * @param is        der Input-Stream mit den Daten der AnkreuzkompetenzJahrgangszuordnung
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der neuen AnkreuzkompetenzJahrgangszuordnung
	 */
	@POST
	@Path("/jahrgangzuordnung")
	@Operation(summary = "Erstellt eine neue AnkreuzkompetenzJahrgangszuordnungen und gibt das zugehörige Objekt zurück.",
			description = "Erstellt eine neue AnkreuzkompetenzJahrgangszuordnungen und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von AnkreuzkompetenzJahrgangszuordnungen besitzt.")
	@ApiResponse(responseCode = "201", description = "Die AnkreuzkompetenzJahrgangszuordnungen wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = AnkreuzkompetenzJahrgangszuordnung.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response addAnkreuzkompetenzJahrgangszuordnung(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden AnkreuzkompetenzJahrgangszuordnungen ohne ID, die automatisch generiert werden",
					required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = AnkreuzkompetenzJahrgangszuordnung.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataAnkreuzkompetenzJahrgangszuordnungen(conn).addMultipleAsResponse(is), request, ServerMode.STABLE,
				BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen von AnkreuzkompetenzJahrgangszuordnungen.
	 *
	 * @param schema    das Datenbankschema
	 * @param is        der InputStream, mit der Liste der zu löschenden IDs
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. der gelöschten AnkreuzkompetenzJahrgangszuordnungen
	 */
	@DELETE
	@Path("/jahrgangzuordnung/delete/multiple")
	@Operation(summary = "Entfernt AnkreuzkompetenzJahrgangszuordnungen.",
			description = "Entfernt AnkreuzkompetenzJahrgangszuordnungen, insofern der SVWS-Benutzer die notwendige Berechtigung hat.")
	@ApiResponse(responseCode = "200", description = "Die AnkreuzkompetenzJahrgangszuordnungen wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = AnkreuzkompetenzJahrgangszuordnung.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine AnkreuzkompetenzJahrgangszuordnung vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response deleteAnkreuzkompetenzJahrgangszuordnungen(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Ids der zu löschenden Objekte", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataAnkreuzkompetenzJahrgangszuordnungen(conn).deleteMultipleAsResponse(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE, BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}
}
