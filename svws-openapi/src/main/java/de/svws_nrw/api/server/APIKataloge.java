package de.svws_nrw.api.server;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.kataloge.KatalogEintrag;
import de.svws_nrw.core.data.kataloge.KatalogEintragOrte;
import de.svws_nrw.core.data.kataloge.KatalogEintragOrtsteile;
import de.svws_nrw.core.data.kataloge.KatalogEintragStrassen;
import de.svws_nrw.core.data.kataloge.KatalogEntlassgrund;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.kataloge.DataHaltestellen;
import de.svws_nrw.data.kataloge.DataKatalogEntlassgruende;
import de.svws_nrw.data.kataloge.DataKatalogOrte;
import de.svws_nrw.data.kataloge.DataKatalogOrtsteile;
import de.svws_nrw.data.kataloge.DataOrte;
import de.svws_nrw.data.kataloge.DataOrtsteile;
import de.svws_nrw.data.kataloge.DataStrassen;
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
import java.io.InputStream;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf allgemeine Kataloge aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/api/kataloge/...
 */
@Path("/db/{schema}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIKataloge {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIKataloge() {
		// leer
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage des Strassen-Kataloges.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Strassen
	 */
	@GET
	@Path("/allgemein/strassen")
	@Operation(summary = "Erstellt eine Liste aller in dem Katalog vorhandenen Strassen.",
			description = "Erstellt eine Liste aller in dem Katalog vorhandenen Strassen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Straßen-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KatalogEintragStrassen.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Straßen-Katalog-Einträge gefunden")
	public Response getKatalogStrassen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> (new DataStrassen()).getAll(), request,
				ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Orts-Kataloges von IT.NRW.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die die Orts-Katalog-Einträge
	 */
	@GET
	@Path("/allgemein/orte")
	@Operation(summary = "Erstellt eine Liste aller in dem Katalog vorhandenen Orte.",
			description = "Erstellt eine Liste aller in dem Katalog vorhandenen Orte. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Orts-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KatalogEintragOrte.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Orts-Katalog-Einträge gefunden")
	public Response getKatalogOrte(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> (new DataKatalogOrte()).getAll(), request,
				ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste der Orte im angegebenen Schema.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return              die Liste der Orte mit ID des Datenbankschemas
	 */
	@GET
	@Path("/orte")
	@Operation(summary = "Gibt eine Übersicht alle Orte im Katalog zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Orte unter Angabe der ID, der PLZ, des Ortes, "
					+ "ggf. des Kreises, dem Bundesland, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Orts-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrtKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Ort-Katalog-Einträge gefunden")
	public Response getOrte(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataOrte(conn).getAll(),
				request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Ortsteil-Kataloges von IT.NRW.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die die Ortsteil-Katalog-Einträge
	 */
	@GET
	@Path("/allgemein/ortsteile")
	@Operation(summary = "Erstellt eine Liste aller in dem Katalog vorhandenen Ortsteile.",
			description = "Erstellt eine Liste aller in dem Katalog vorhandenen Ortsteile. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Ortsteil-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KatalogEintragOrtsteile.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Ortsteil-Katalog-Einträge gefunden")
	public Response getKatalogOrtsteile(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> (new DataKatalogOrtsteile()).getAll(), request,
				ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste der Ortsteile im angegebenen Schema.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return              die Liste der Ortsteile mit ID des Datenbankschemas
	 */
	@GET
	@Path("/ortsteile")
	@Operation(summary = "Gibt eine Übersicht aller Ortsteile im Katalog zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Ortsteile unter Angabe der ID, der zugehörigen"
					+ "Ort-ID, dem Namen des Ortsteils, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Ortsteil-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrtsteilKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Ortsteil-Katalog-Einträge gefunden")
	public Response getOrtsteile(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataOrtsteile(conn).getAll(),
				request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste der Haltestellen im angegebenen Schema.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return              die Liste der Haltestellen mit ID des Datenbankschemas
	 */
	@GET
	@Path("/haltestellen")
	@Operation(summary = "Gibt eine Übersicht der Haltestellen im Katalog zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Haltestellen unter Angabe der ID, eines Kürzels und der textuellen Beschreibung "
					+ "sowie der Information, ob der Eintrag in der Anwendung sichtbar bzw. änderbar sein soll, und gibt diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen zu den Haltestellen.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getHaltestellen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataHaltestellen(conn).getList(),
				request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste der Entlassgruende.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Entlassgruende
	 */
	@GET
	@Path("/entlassgruende")
	@Operation(summary = "Gibt eine Übersicht der Entlassgruende im Katalog zurück.",
			description = "Gibt die Entlassgründe zurück, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Entlassgründen.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KatalogEntlassgrund.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getEntlassgruende(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogEntlassgruende(conn).getAllAsResponse(),
				request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen eines Entlassgrundes.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Entlassgrundes
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{id : \\d+}")
	@Operation(summary = "Patched den Entlassgrund mit der angegebenen ID.",
			description = "Patched den Entlassgrund mit der angegebenen ID, insofern die notwendigen Berechtigungen vorliegen.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response patchEntlassgrund(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch eines Entlassgrundes", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = KatalogEntlassgrund.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataKatalogEntlassgruende(conn).patchAsResponse(id, is), request, ServerMode.DEV,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Hinzufügen eines Entlassgrundes.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten der Entlassgründe
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den erstellen Entlassgründe
	 */
	@POST
	@Path("/create")
	@Operation(summary = "Erstellt neue Entlassgründe und gibt das erstellte Objekt zurück.",
			description = "Erstellt neue Entlassgründe, insofern die notwendigen Berechtigungen vorliegen")
	@ApiResponse(responseCode = "201", description = "Die Entlassgründe wurden erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = KatalogEntlassgrund.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Entlassgründe anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addEntlassgrund(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden Entlassgründe ohne ID, da diese automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = KatalogEntlassgrund.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataKatalogEntlassgruende(conn).addAsResponse(is), request, ServerMode.DEV, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Entlassgründe.
	 *
	 * @param schema    das Datenbankschema
	 * @param is        der InputStream, mit der Liste der zu löschenden IDs
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Lösch-Operationen
	 */
	@DELETE
	@Path("/delete/multiple")
	@Operation(summary = "Entfernt mehrere Entlassgründe.", description = "Entfernt mehrere Entlassgründe, insofern die notwendigen Berechtigungen vorhanden sind.")
	@ApiResponse(responseCode = "200", description = "Die Lösch-Operationen wurden ausgeführt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Entlassgründe zu entfernen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteEntlassgruende(@PathParam("schema") final String schema, @RequestBody(description = "Die IDs der zu löschenden Entlassgründe",
					required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransactionOnErrorSimpleResponse(
				conn -> new DataKatalogEntlassgruende(conn).deleteMultipleAsSimpleResponseList(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	}

}
