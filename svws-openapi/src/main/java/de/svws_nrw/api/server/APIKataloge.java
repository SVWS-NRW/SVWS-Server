package de.svws_nrw.api.server;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.kataloge.KatalogEintragOrte;
import de.svws_nrw.core.data.kataloge.KatalogEintragOrtsteile;
import de.svws_nrw.core.data.kataloge.KatalogEintragStrassen;
import de.svws_nrw.core.data.kataloge.KatalogEntlassgrund;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag;
import de.svws_nrw.core.data.schule.Haltestelle;
import de.svws_nrw.core.data.schule.Kindergarten;
import de.svws_nrw.core.data.schule.Merkmal;
import de.svws_nrw.core.data.schule.Sportbefreiung;
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
import de.svws_nrw.data.kataloge.DataSportbefreiungen;
import de.svws_nrw.data.kataloge.DataStrassen;
import de.svws_nrw.data.schueler.DataKatalogSchuelerFoerderschwerpunkte;
import de.svws_nrw.data.schule.DataKatalogKindergaerten;
import de.svws_nrw.data.schule.DataMerkmale;
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
	 * Die OpenAPI-Methode für die Abfrage der Liste der Entlassgruende.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Entlassgruende
	 */
	@GET
	@Path("/entlassgruende")
	@Operation(summary = "Gibt eine Übersicht der Entlassgründe im Katalog zurück.",
			description = "Gibt die Entlassgründe zurück, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste der Entlassgründe.",
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
	@Path("/entlassgruende/{id : \\d+}")
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
				conn -> new DataKatalogEntlassgruende(conn).patchAsResponse(id, is), request, ServerMode.STABLE,
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
	@Path("/entlassgruende/create")
	@Operation(summary = "Erstellt einen neuen Entlassgrund und gibt das erstellte Objekt zurück.",
			description = "Erstellt einen neuen Entlassgrund, insofern die notwendigen Berechtigungen vorliegen")
	@ApiResponse(responseCode = "201", description = "Der Entlassgrund wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = KatalogEntlassgrund.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Entlassgründe anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addEntlassgrund(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten des zu erstellenden Entlassgrundes ohne ID, da diese automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = KatalogEntlassgrund.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataKatalogEntlassgruende(conn).addAsResponse(is), request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
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
	@Path("/entlassgruende/delete/multiple")
	@Operation(summary = "Entfernt mehrere Entlassgründe.", description = "Entfernt mehrere Entlassgründe, insofern die notwendigen Berechtigungen vorhanden sind.")
	@ApiResponse(responseCode = "200", description = "Die Lösch-Operationen wurden ausgeführt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Entlassgründe zu entfernen.")
	@ApiResponse(responseCode = "404", description = "Entlassgründe nicht vorhanden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteEntlassgruende(@PathParam("schema") final String schema, @RequestBody(description = "Die IDs der zu löschenden Entlassgründe",
					required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransactionOnErrorSimpleResponse(
				conn -> new DataKatalogEntlassgruende(conn).deleteMultipleAsSimpleResponseList(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Merkmale.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Merkmale
	 */
	@GET
	@Path("/merkmale")
	@Operation(summary = "Gibt eine Übersicht der Merkmale im Katalog zurück.",
			description = "Gibt die Merkmale zurück, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Merkmalen.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Merkmal.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Merkmale gefunden")
	public Response getMerkmale(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataMerkmale(conn).getAllAsResponse(),
				request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen eines Merkmals.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Merkmals
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/merkmale/{id : \\d+}")
	@Operation(summary = "Patched das Merkmal mit der angegebenen ID.",
			description = "Patched das Merkmal mit der angegebenen ID, insofern die notwendigen Berechtigungen vorliegen.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response patchMerkmal(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch eines Merkmals", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Merkmal.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataMerkmale(conn).patchAsResponse(id, is), request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Hinzufügen eines Merkmals.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten des Merkmals
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem erstellten Merkmal
	 */
	@POST
	@Path("/merkmale/create")
	@Operation(summary = "Erstellt ein neues Merkmal und gibt das erstellte Objekt zurück.",
			description = "Erstellt ein neues Merkmal, insofern die notwendigen Berechtigungen vorliegen")
	@ApiResponse(responseCode = "201", description = "Das Merkmal wurden erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Merkmal.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Merkmale anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addMerkmal(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten des zu erstellenden Merkmals ohne ID, da diese automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Merkmal.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataMerkmale(conn).addAsResponse(is), request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Merkmale.
	 *
	 * @param schema    das Datenbankschema
	 * @param is        der InputStream, mit der Liste der zu löschenden IDs
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Lösch-Operationen
	 */
	@DELETE
	@Path("/merkmale/delete/multiple")
	@Operation(summary = "Entfernt mehrere Merkmale.", description = "Entfernt mehrere Merkmale, insofern die notwendigen Berechtigungen vorhanden sind.")
	@ApiResponse(responseCode = "200", description = "Die Lösch-Operationen wurden ausgeführt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Merkmale zu entfernen.")
	@ApiResponse(responseCode = "404", description = "Merkmale nicht vorhanden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteMerkmale(@PathParam("schema") final String schema, @RequestBody(description = "Die IDs der zu löschenden Merkmale",
			required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
			array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransactionOnErrorSimpleResponse(
				conn -> new DataMerkmale(conn).deleteMultipleAsSimpleResponseList(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage des schulspezifischen Kataloges für die Kindergärten.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return          die Liste mit dem Katalog der Kindergärten
	 */
	@GET
	@Path("/kindergaerten")
	@Operation(summary = "Gibt eine Übersicht aller Kindergärten im Katalog zurück.",
			description = "Gibt die Kindergärten zurück, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Kindergarten.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getKindergaerten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogKindergaerten(conn).getAllAsResponse(),
				request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für das Hinzufügen neuer Kindergärten.
	 *
	 * @param schema       das Datenbankschema, in welchem der Kindergarten erstellt wird
	 * @param request      die Informationen zur HTTP-Anfrage
	 * @param is           das JSON-Objekt
	 *
	 * @return die HTTP-Antwort mit neuem Kindergarten
	 */
	@POST
	@Path("/kindergarten/create")
	@Operation(summary = "Erstellt einen neuen Kindergarten und gibt diesen zurück.",
			description = "Erstellt einen neuen Kindergarten, insofern die notwendigen Berechtigungen vorliegen")
	@ApiResponse(responseCode = "201", description = "Kindergarten wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Kindergarten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Telefonart anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addKindergarten(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten des zu erstellenden Kindergartens ohne ID, da diese automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Kindergarten.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataKatalogKindergaerten(conn).addAsResponse(is), request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen eines Kindergartens im angegebenen Schema
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Kindergartens
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/kindergarten/{id : \\d+}")
	@Operation(summary = "Patched den Kindergarten mit der angegebenen ID.",
			description = "Patched den Kindergarten mit der angegebenen ID, insofern die notwendigen Berechtigungen vorliegen.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Kindergarten-Daten integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Kindergarten-Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Kindergarten mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchKindergarten(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch eines Kindergartens", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Kindergarten.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataKatalogKindergaerten(conn).patchAsResponse(id, is), request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Kindergärten.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           die IDs der Kindergärten
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Lösch-Operationen
	 */
	@DELETE
	@Path("/kindergaerten/delete/multiple")
	@Operation(summary = "Entfernt mehrere Kindergärten.",
			description = "Entfernt mehrere Kindergärten, insofern, die notwendigen Berechtigungen vorhanden sind.")
	@ApiResponse(responseCode = "200", description = "Die Kindergärten wurden erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Kindergärten nicht vorhanden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteKindergaerten(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Kindergärten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogKindergaerten(conn).deleteMultipleAsSimpleResponseList(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die Förderschwerpunkte.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Förderschwerpunkte
	 */
	@GET
	@Path("/foerderschwerpunkte")
	@Operation(summary = "Gibt den Katalog der Förderschwerpunkte zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Förderschwerpunkte unter Angabe der ID, eines Kürzels und der Bezeichnung. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Förderschwerpunkte-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FoerderschwerpunktEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	public Response getKatalogFoerderschwerpunkte(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogSchuelerFoerderschwerpunkte(conn).getAllAsResponse(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für das Hinzufügen eines neuen Förderschwerpunktes.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten des Förderschwerpunktes
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem erstellten Förderschwerpunkt
	 */
	@POST
	@Path("/foerderschwerpunkte/create")
	@Operation(summary = "Erstellt einen neuen Förderschwerpunkt und gibt das erstellte Objekt zurück.",
			description = "Erstellt einen neuen Förderschwerpunkt und gibt das erstellte Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen neuer Förderschwerpunkte besitzt.")
	@ApiResponse(responseCode = "201", description = "Der Förderschwerpunkt wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = FoerderschwerpunktEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Förderschwerpunkte anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addKatalogFoerderschwerpunkt(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden Förderschwerpunkte ohne ID, da diese automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = FoerderschwerpunktEintrag.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogSchuelerFoerderschwerpunkte(conn).addAsResponse(is),
				request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen eines Förderschwerpunktes.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Förderschwerpunktes
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{id : \\d+}/foerderschwerpunkte")
	@Operation(summary = "Patched Förderschwerpunkte.",
			description = "Passt den Förderschwerpunkt zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Förderschwerpunkten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Förderschwerpunkte zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Förderschwerpunkt mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchKatalogFoerderschwerpunkt(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für den Förderschwerpunkt", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = FoerderschwerpunktEintrag.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogSchuelerFoerderschwerpunkte(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Förderschwerpunkte.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           die IDs der Förderschwerpunkte
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Lösch-Operationen
	 */
	@DELETE
	@Path("/foerderschwerpunkte/delete/multiple")
	@Operation(summary = "Entfernt mehrere Förderschwerpunkte.",
			description = "Entfernt mehrere Förderschwerpunkte, insofern, die notwendigen Berechtigungen vorhanden sind.")
	@ApiResponse(responseCode = "200", description = "Die Förderschwerpunkte wurden erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Förderschwerpunkte nicht vorhanden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteKatalogFoerderschwerpunkte(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Förderschwerpunkte", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogSchuelerFoerderschwerpunkte(conn).deleteMultipleAsSimpleResponseList(
				JSONMapper.toListOfLong(is)), request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage eines Förderschwerpunktes.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Förderschwerpunktes
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Daten zum Förderschwerpunkt
	 */
	@GET
	@Path("/foerderschwerpunkte/{id : \\d+}")
	@Operation(summary = "Liefert zu der ID des Förderschwerpunktes die zugehörigen Daten.",
			description = "Liest die Daten des Förderschwerpunktes zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten des Förderschwerpunktes",
			content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = FoerderschwerpunktEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Förderschwerpunkt-Eintrag mit der angegebenen ID gefunden")
	public Response getKatalogFoerderschwerpunkt(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogSchuelerFoerderschwerpunkte(conn).getByIdAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste der Sportbefreiungen.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Sportbefreiungen
	 */
	@GET
	@Path("/sportbefreiungen")
	@Operation(summary = "Gibt eine Liste der Sportbefreiungen im Katalog zurück.",
			description = "Gibt die Sportbefreiungen zurück, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste der Sportbefreiungen.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Sportbefreiung.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getSportbefreiungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSportbefreiungen(conn).getAllAsResponse(),
				request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen einer Sportbefreiung.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Sportbefreiung
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/sportbefreiungen/{id : \\d+}")
	@Operation(summary = "Patched die Sportbefreiung mit der angegebenen ID.",
			description = "Patched die Sportbefreiung mit der angegebenen ID, insofern die notwendigen Berechtigungen vorliegen.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response patchSportbefreiung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch einer Sportbefreiung", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Sportbefreiung.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataSportbefreiungen(conn).patchAsResponse(id, is), request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Hinzufügen einer Sportbefreiung.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten der Sportbefreiung
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der erstellten Sportbefreiung
	 */
	@POST
	@Path("/sportbefreiungen/create")
	@Operation(summary = "Erstellt einer neue Sportbefreiung und gibt das erstellte Objekt zurück.",
			description = "Erstellt eine neue Sportbefreiung, insofern die notwendigen Berechtigungen vorliegen")
	@ApiResponse(responseCode = "201", description = "Die Sportbefreiung wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Sportbefreiung.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Sportbefreiung anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addSportbefreiung(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden Sportbefreiung ohne ID, da diese automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Sportbefreiung.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataSportbefreiungen(conn).addAsResponse(is), request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Sportbefreiungen.
	 *
	 * @param schema    das Datenbankschema
	 * @param is        der InputStream, mit der Liste der zu löschenden IDs
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Lösch-Operationen
	 */
	@DELETE
	@Path("/sportbefreiungen/delete/multiple")
	@Operation(summary = "Entfernt mehrere Sportbefreiungen.", description = "Entfernt mehrere Sportbefreiungen, insofern die notwendigen Berechtigungen vorhanden sind.")
	@ApiResponse(responseCode = "200", description = "Die Lösch-Operationen wurden ausgeführt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Sportbefreiungen zu entfernen.")
	@ApiResponse(responseCode = "404", description = "Sportbefreiungen nicht vorhanden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteSportbefreiungen(@PathParam("schema") final String schema, @RequestBody(description = "Die IDs der zu löschenden Sportbefreiungen",
			required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
			array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransactionOnErrorSimpleResponse(
				conn -> new DataSportbefreiungen(conn).deleteMultipleAsSimpleResponseList(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste der Haltestellen.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Haltestellen
	 */
	@GET
	@Path("/haltestellen")
	@Operation(summary = "Gibt eine Liste der Haltestellen im Katalog zurück.",
			description = "Gibt die Haltestellen zurück, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste der Haltestellen.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Haltestelle.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getHaltestellen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataHaltestellen(conn).getAllAsResponse(),
				request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen einer Haltestelle.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Haltestelle
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/haltestellen/{id : \\d+}")
	@Operation(summary = "Patched die Haltestelle mit der angegebenen ID.",
			description = "Patched die Haltestelle mit der angegebenen ID, insofern die notwendigen Berechtigungen vorliegen.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response patchHaltestelle(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch einer Haltestelle", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Haltestelle.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataHaltestellen(conn).patchAsResponse(id, is), request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Hinzufügen einer Haltestelle.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten der Haltestelle
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der erstellten Haltestelle
	 */
	@POST
	@Path("/haltestellen/create")
	@Operation(summary = "Erstellt einer neue Haltestelle und gibt das erstellte Objekt zurück.",
			description = "Erstellt eine neue Haltestelle, insofern die notwendigen Berechtigungen vorliegen")
	@ApiResponse(responseCode = "201", description = "Die Haltestelle wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Haltestelle.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Haltestellen anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addHaltestelle(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden Haltestelle ohne ID, da diese automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Haltestelle.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataHaltestellen(conn).addAsResponse(is), request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Haltestellen.
	 *
	 * @param schema    das Datenbankschema
	 * @param is        der InputStream, mit der Liste der zu löschenden IDs
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Lösch-Operationen
	 */
	@DELETE
	@Path("/haltestellen/delete/multiple")
	@Operation(summary = "Entfernt mehrere Haltestellen.", description = "Entfernt mehrere Haltestellen, insofern die notwendigen Berechtigungen vorhanden sind.")
	@ApiResponse(responseCode = "200", description = "Die Lösch-Operationen wurden ausgeführt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Haltestellen zu entfernen.")
	@ApiResponse(responseCode = "404", description = "Haltestellen nicht vorhanden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteHaltestellen(@PathParam("schema") final String schema, @RequestBody(description = "Die IDs der zu löschenden Haltestellen",
			required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
			array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransactionOnErrorSimpleResponse(
				conn -> new DataHaltestellen(conn).deleteMultipleAsSimpleResponseList(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	}
}
