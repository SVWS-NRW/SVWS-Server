package de.svws_nrw.api.server;

import java.io.InputStream;

import de.svws_nrw.asd.data.schueler.SchuelerBetriebsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittBemerkungen;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchMerkmal;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.asd.data.schueler.Sprachpruefung;
import de.svws_nrw.asd.data.schueler.UebergangsempfehlungKatalogEintrag;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.betrieb.BetriebStammdaten;
import de.svws_nrw.core.data.erzieher.ErzieherStammdaten;
import de.svws_nrw.core.data.kataloge.KatalogEintrag;
import de.svws_nrw.core.data.schueler.SchuelerKAoADaten;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittListeEintrag;
import de.svws_nrw.core.data.schueler.SchuelerLernplattform;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.data.schueler.SchuelerTelefon;
import de.svws_nrw.core.data.schueler.SchuelerVermerke;
import de.svws_nrw.core.data.schueler.SchuelerEinwilligung;
import de.svws_nrw.core.data.schule.HerkunftKatalogEintrag;
import de.svws_nrw.core.data.schule.HerkunftsartKatalogEintrag;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.betriebe.DataBetriebsStammdaten;
import de.svws_nrw.data.erzieher.DataErzieherStammdaten;
import de.svws_nrw.data.schueler.DataKatalogHerkuenfte;
import de.svws_nrw.data.schueler.DataKatalogHerkunftsarten;
import de.svws_nrw.data.schueler.DataKatalogSchuelerFahrschuelerarten;
import de.svws_nrw.data.schueler.DataKatalogUebergangsempfehlung;
import de.svws_nrw.data.schueler.DataSchuelerBetriebsdaten;
import de.svws_nrw.data.schueler.DataSchuelerEinwilligungen;
import de.svws_nrw.data.schueler.DataSchuelerKAoADaten;
import de.svws_nrw.data.schueler.DataSchuelerLeistungsdaten;
import de.svws_nrw.data.schueler.DataSchuelerLernabschnittsdaten;
import de.svws_nrw.data.schueler.DataSchuelerLernabschnittsliste;
import de.svws_nrw.data.schueler.DataSchuelerLernplattformen;
import de.svws_nrw.data.schueler.DataSchuelerMerkmale;
import de.svws_nrw.data.schueler.DataSchuelerSchulbesuchSchule;
import de.svws_nrw.data.schueler.DataSchuelerSchulbesuchsdaten;
import de.svws_nrw.data.schueler.DataSchuelerSprachbelegung;
import de.svws_nrw.data.schueler.DataSchuelerSprachpruefung;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.data.schueler.DataSchuelerTelefon;
import de.svws_nrw.data.schueler.DataSchuelerVermerke;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Schülerdaten aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/schueler/...
 */
@Path("/db/{schema}/schueler")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APISchueler {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APISchueler() {
		// leer
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste aller Schüler.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den einzelnen Schülern
	 */
	@GET
	@Path("/aktuell")
	@Operation(summary = "Gibt eine sortierte Übersicht von allen Schülern des aktuellen Schuljahresabschnitts zurück.",
			description = "Erstellt eine Liste aller im aktuellen Schuljahresabschnitt vorhanden Schüler unter Angabe der ID, des Vor- und Nachnamens, "
					+ "der Klasse, des Jahrgangs, sein Status (z.B. aktiv), einer Sortierreihenfolge, ob sie in der Anwendung "
					+ "sichtbar bzw. änderbar sein sollen. Die schüler sind anhand der Klasse, des Nachnamens und des Vornamens sortiert."
					+ "Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Schüler-Listen-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerListeEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Schüler-Einträge gefunden")
	public Response getSchuelerAktuell(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerliste(conn, null).getAll(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}



	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste aller Schüler.
	 *
	 * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param abschnitt   die ID des Schuljahresabschnitts dessen Schüler zurückgegeben werden sollen
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den einzelnen Schülern
	 */
	@GET
	@Path("/abschnitt/{abschnitt : \\d+}")
	@Operation(summary = "Gibt eine sortierte Übersicht von allen Schülern des angegebenen Schuljahresabschnitts zurück.",
			description = "Erstellt eine Liste aller Schüler des angegebenen Schuljahresabschnitts unter Angabe der ID, des Vor- und Nachnamens, "
					+ "der Klasse, des Jahrgangs, sein Status (z.B. aktiv), einer Sortierreihenfolge, ob sie in der Anwendung "
					+ "sichtbar bzw. änderbar sein sollen. Die schüler sind anhand der Klasse, des Nchnamens und des Vornamens sortiert."
					+ "Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Schüler-Listen-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerListeEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Schüler-Einträge gefunden")
	public Response getSchuelerFuerAbschnitt(@PathParam("schema") final String schema, @PathParam("abschnitt") final long abschnitt,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerliste(conn, abschnitt).getAll(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Informationen zur Verwaltung einer Schüler-Auswahlliste
	 * mit Filterfunktionen in Bezug auf einen Schuljahresabschnitt (GZip-komprimiert).
	 *
	 * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param abschnitt   die ID des Schuljahresabschnitts
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return die GZip-komprimierten Daten zur Schüler-Auswahlliste
	 */
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/abschnitt/{abschnitt : \\d+}/auswahlliste")
	@Operation(
			summary = "Gibt die Informationen zur Verwaltung einer Schüler-Auswahlliste mit Filterfunktionen in Bezug auf einen Schuljahresabschnitt zurück.",
			description = "Gibt die Informationen zur Verwaltung einer Schüler-Auswahlliste mit Filterfunktionen in Bezug auf einen Schuljahresabschnitt zurück."
					+ "Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die GZip-komprimierten Daten zur Schüler-Auswahlliste",
			content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM,
					schema = @Schema(type = "string", format = "binary", description = "Die GZip-komprimierten Daten zur Schüler-Auswahlliste")))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Nicht alle Daten wurden gefunden, z.B. Schüler-Einträge")
	public Response getSchuelerAuswahllisteFuerAbschnitt(@PathParam("schema") final String schema, @PathParam("abschnitt") final long abschnitt,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> JSONMapper.gzipFileResponseFromObject(DataSchuelerliste.getSchuelerListe(conn, abschnitt),
						"auswahlliste_%d.json.gz".formatted(abschnitt)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Stammdaten eines Schülers.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Schülers
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Stammdaten des Schülers
	 */
	@GET
	@Path("/{id : \\d+}/stammdaten")
	@Operation(summary = "Liefert zu der ID des Schülers die zugehörigen Stammdaten.",
			description = "Liest die Stammdaten des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Stammdaten des Schülers",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchuelerStammdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
	public Response getSchuelerStammdaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerStammdaten(conn).get(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen der Stammdaten eines Schülers.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Schülers
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{id : \\d+}/stammdaten")
	@Operation(summary = "Liefert zu der ID des Schülers die zugehörigen Stammdaten.",
			description = "Passt die Schüler-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Schülerstammdaten integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchSchuelerStammdaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Schüler-Stammdaten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = SchuelerStammdaten.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerStammdaten(conn).patch(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Schüler.
	 *
	 * @param schema    das Datenbankschema
	 * @param is        der InputStream, mit der Liste von zu löschenden IDs
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Lösch-Operationen
	 */
	@DELETE
	@Path("/delete/multiple")
	@Operation(summary = "Entfernt mehrere Schüler durch setzen eines Löschvermerks.",
			description = "Entfernt mehrere Schüler durch setzen eines Löschvermerks. Dabei wird geprüft, ob alle Vorbedingungen zum Entfernen"
					+ "der Schüler erfüllt sind und der SVWS-Benutzer die notwendige Berechtigung hat.")
	@ApiResponse(responseCode = "200", description = "Die Lösch-Operationen wurden ausgeführt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schüler zu entfernen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteSchueler(@PathParam("schema") final String schema, @RequestBody(description = "Die IDs der zu löschenden Schüler", required = true,
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransactionOnErrorSimpleResponse(conn -> new DataSchuelerStammdaten(conn).deleteMultipleAsResponse(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LOESCHEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der bisherigen Schulen eines Schülers.
	 *
	 * @param schema     das Datenbankschema, auf das die Abfrage ausgeführt werden soll
	 * @param id         die Datenbank-ID zur Identifikation eines Schulbesuchs
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return das SchuelerMerkmal
	 */
	@GET
	@Path("/bisherigeSchule/{id : \\d+}")
	@Operation(summary = "Liefert die zur ID zugehörigen bisher besuchte Schule",
		description = "Gibt die bisher besuchte Schule zurück, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.")
	@ApiResponse(responseCode = "200", description = "Die bisher besuchte Schule", content = @Content(mediaType = "application/json",
		schema = @Schema(implementation = SchuelerSchulbesuchSchule.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine bisher besuchte Schule mit der angegebenen ID gefunden.")
	public Response getBisherigeSchule(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerSchulbesuchSchule(conn).getByIdAsResponse(id),
				request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen der bisher besuchten Schule eines Schülers
	 *
	 * @param schema     das Datenbankschema
	 * @param idSchueler   die ID des Schülers bei dem das Merkmal hinzugefügt werden soll
	 * @param is         der Input-Stream mit den Daten einer bisher besuchten Schule
	 * @param request    die Information zur HTTP-Anfrage
	 *
	 * @return die HTTP-Anwort mit einer neuen bisher besuchen Schule
	 */
	@POST
	@Path("/{idSchueler : \\d+}/bisherigeSchule")
	@Operation(summary = "Erstellt eine neue bisherige Schule und gibt das zugehörige Objekt zurück.",
			description = "Erstellt eine bisherige Schule, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "Die bisher besuchte Schule wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuelerSchulbesuchSchule.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um bisherige Schulen hinzuzufügen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addBisherigeSchule(@PathParam("schema") final String schema, @PathParam("idSchueler") final long idSchueler,
			@RequestBody(description = "Die Daten der zu erstellenden Schule ohne die ID, da diese automatisch generiert wird.", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = SchuelerSchulbesuchSchule.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerSchulbesuchSchule(conn, idSchueler).addAsResponse(is),
				request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen von bisher besuchten Schulen.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der bisher besuchten Schule
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/bisherigeSchule/{id : \\d+}")
	@Operation(summary = "Patcht und Persistiert die zur ID zugehörige bisher besuchte Schule.",
			description = "Patcht und Persistiert eine bisher besuchte Schule, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.")
	@ApiResponse(responseCode = "204", description = "Der Patch wurde erfolgreich ausgeführt.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um bisher besuchte Schulen zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine bisher besuchte Schule mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchBisherigeSchule(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die bisher besuchte Schule", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = SchuelerSchulbesuchSchule.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerSchulbesuchSchule(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
	}

		/**
	 * Die OpenAPI-Methode für das Entfernen von bisher besuchten Schulen.
	 *
	 * @param schema       das Datenbankschema
	 * @param ids          die IDs der bisher besuchten Schulen
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. der gelöschten bisherigen Schule
	 */
	@DELETE
	@Path("/bisherigeSchule/multiple")
	@Operation(summary = "Entfernt bisher besuchte Schulen.",
			description = "Entfernt bisher besuchte Schulen, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine bisher besuchte Schule wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerSchulbesuchSchule.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um bisher besuchte Schulen zu entfernen.")
	@ApiResponse(responseCode = "404", description = "Die bisher besuchten Schulen sind nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteBisherigeSchulen(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden bisher besuchten Schulen", required = true,
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream ids,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataSchuelerSchulbesuchSchule(conn).deleteMultipleAsResponse(JSONMapper.toListOfLong(ids)),
				request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage von SchuelerMerkmalen.
	 *
	 * @param schema     das Datenbankschema, auf das die Abfrage ausgeführt werden soll
	 * @param id         die Datenbank-ID zur Identifikation eines SchülerMerkmals
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return das SchuelerMerkmal
	 */
	@GET
	@Path("/merkmal/{id : \\d+}")
	@Operation(summary = "Liefert das zur ID zugehörige SchuelerMerkmal.",
			description = "Gibt das SchuelerMerkmal zurück, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.")
	@ApiResponse(responseCode = "200", description = "Das SchuelerMerkmal", content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = SchuelerSchulbesuchMerkmal.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein SchuelerMerkmal mit der angegebenen ID gefunden.")
	public Response getSchuelerMerkmal(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerMerkmale(conn).getByIdAsResponse(id),
				request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}

	/**
	 * Die OpenAPI-Methode für das Hinzufügen von SchuelerMerkmalen.
	 *
	 * @param schema       das Datenbankschema
	 * @param idSchueler   die ID des Schülers bei dem das Merkmal hinzugefügt werden soll
	 * @param is           der Input-Stream mit den Daten des SchuelerMerkmals
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem neuen SchuelerMerkmal
	 */
	@POST
	@Path("{idSchueler : \\d+}/merkmal")
	@Operation(summary = "Erstellt ein neues SchuelerMerkmal und gibt das zugehörige Objekt zurück.",
			description = "Erstellt neue SchuelerMerkmale, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "Das SchuelerMerkmal wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuelerSchulbesuchMerkmal.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um SchuelerMerkmale hinzuzufügen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addSchuelerMerkmal(@PathParam("schema") final String schema,  @PathParam("idSchueler") final long idSchueler,
			@RequestBody(description = "Die Daten des zu erstellenden SchuelerMerkmal ohne ID, die automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = SchuelerSchulbesuchMerkmal.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerMerkmale(conn, idSchueler).addAsResponse(is),
				request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen von SchuelerMerkmalen.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Schülermerkmals
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/merkmal/{id : \\d+}")
	@Operation(summary = "Patcht und Persistiert das zur ID zugehörige SchuelerMerkmal.",
			description = "Patcht und Persistiert SchuelerMerkmale, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.")
	@ApiResponse(responseCode = "204", description = "Der Patch wurde erfolgreich ausgeführt.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um SchuelerMerkmale zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein SchuelerMerkmal mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchSchuelerMerkmal(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für das SchuelerMerkmal", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = SchuelerSchulbesuchMerkmal.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerMerkmale(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen von SchuelerMerkmalen.
	 *
	 * @param schema       das Datenbankschema
	 * @param id           die ID des SchuelerMerkmals
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten SchuelerMerkmal
	 */
	@DELETE
	@Path("/merkmal/{id : \\d+}")
	@Operation(summary = "Entfernt SchuelerMerkmale.",
			description = "Entfernt SchuelerMerkmale, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.")
	@ApiResponse(responseCode = "200", description = "Ein SchuelerMerkmal wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchuelerSchulbesuchMerkmal.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um SchuelerMerkmale zu entfernen.")
	@ApiResponse(responseCode = "404", description = "Das SchuelerMerkmal ist nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteSchuelerMerkmal(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerMerkmale(conn).deleteAsResponse(id),
				request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Schulbesuchsdaten eines Schülers.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Schülers
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Schulbesuchsdaten des Schülers
	 */
	@GET
	@Path("/{id : \\d+}/schulbesuch")
	@Operation(summary = "Liefert zu der ID des Schülers die zugehörigen Schulbesuchsdaten.",
			description = "Liest die Schulbesuchsdaten des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Schulbesuchsdaten des Schülers", content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = SchuelerSchulbesuchsdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden.")
	public Response getSchuelerSchulbesuch(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerSchulbesuchsdaten(conn).getByIdAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen der Stammdaten eines Schülers.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Schülers
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{id : \\d+}/schulbesuch")
	@Operation(summary = "Liefert zu der ID des Schülers die zugehörigen Schulbesuchsdatendaten.",
			description = "Passt die Schüler-Schulbesuchsdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Schüler-Schulbesuchsdaten integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchSchuelerSchulbesuch(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Schulbesuchsdaten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = SchuelerSchulbesuchsdaten.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerSchulbesuchsdaten(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
	}



	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste von Lernabschnitten eines Schülers.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id         die Datenbank-ID zur Identifikation des Schülers
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Lernabschnittsdaten des Schülers
	 */
	@GET
	@Path("/{id : \\d+}/lernabschnitte")
	@Operation(summary = "Liefert zu der ID des Schülers eine Liste der zugehörigen Lernabschnitte.",
			description = "Liest eine Lister der Lernabschnitte des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Schüler-Lernabschnitt-Listeneinträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = SchuelerLernabschnittListeEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
	public Response getSchuelerLernabschnittsliste(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerLernabschnittsliste(conn).get(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Lernabschnittsdaten eines Schülers.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id         die Datenbank-ID zur Identifikation des Schülers
	 * @param abschnitt  der Schuljahresabschnitt des auszulesenden Lernabschnitts
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Lernabschnittsdaten des Schülers
	 */
	@GET
	@Path("/{id : \\d+}/abschnitt/{abschnitt : \\d+}/lernabschnittsdaten")
	@Operation(summary = "Liefert zu der ID des Schülers und dem Schuljahresabschnitt die zugehörigen Lernabschnittsdaten.",
			description = "Liest die Lernabschnittsdaten des Schülers zu der angegebenen ID und dem angegeben Schuljahresabschnitt aus der Datenbank "
					+ "und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Lernabschnittsdaten des Schülers", content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = SchuelerLernabschnittsdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
	public Response getSchuelerLernabschnittsdaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@PathParam("abschnitt") final long abschnitt, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerLernabschnittsdaten(conn).get(id, abschnitt),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Lernabschnittsdaten eines Schülers.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param abschnitt  die ID des Schülerlernabschnitts
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Lernabschnittsdaten des Schülers
	 */
	@GET
	@Path("/lernabschnittsdaten/{abschnitt : \\d+}")
	@Operation(summary = "Liefert zu der ID des Schülerlernabschnittes die zugehörigen Lernabschnittsdaten.",
			description = "Liest die Schüler-Lernabschnittsdaten zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Lernabschnittsdaten des Schülers", content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = SchuelerLernabschnittsdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit Schüler-Lernabschnittsdaten mit der angegebenen ID gefunden")
	public Response getSchuelerLernabschnittsdatenByID(@PathParam("schema") final String schema, @PathParam("abschnitt") final long abschnitt,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerLernabschnittsdaten(conn).getByIdAsResponse(abschnitt),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen von Schülerlernabschnittsdaten.
	 *
	 * @param schema      das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param abschnitt   die ID des Schülerlernabschnitts
	 * @param is          der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/lernabschnittsdaten/{abschnitt : \\d+}")
	@Operation(summary = "Passt die Schülerlernabschnittsdaten mit der angebenen ID an.",
			description = "Passt die Schülerlernabschnittsdaten mit der angebenen ID an. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schülerlernabschnittsdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchSchuelerLernabschnittsdaten(@PathParam("schema") final String schema, @PathParam("abschnitt") final long abschnitt,
			@RequestBody(description = "Der Patch für die Schülerlernabschnittsdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = SchuelerLernabschnittsdaten.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerLernabschnittsdaten(conn).patchAsResponse(abschnitt, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen von Bemerkungen in Schülerlernabschnittsdaten.
	 *
	 * @param schema      das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param abschnitt   die ID des Schülerlernabschnitts
	 * @param is          der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/lernabschnittsdaten/{abschnitt : \\d+}/bemerkungen")
	@Operation(summary = "Passt die Bemerkungen von Schülerlernabschnittsdaten mit der angebenen ID an.",
			description = "Passt die Bemerkungen von Schülerlernabschnittsdaten mit der angebenen ID an. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schülerlernabschnittsdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchSchuelerLernabschnittsdatenBemerkungen(@PathParam("schema") final String schema, @PathParam("abschnitt") final long abschnitt,
			@RequestBody(description = "Der Patch für die Schülerlernabschnittsdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = SchuelerLernabschnittBemerkungen.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerLernabschnittsdaten(conn).patchBemerkungen(abschnitt, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Leistungsdaten eines Schülers.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id         die Datenbank-ID der Schülerleistungsdaten
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Leistungsdaten des Schülers
	 */
	@GET
	@Path("/leistungsdaten/{id : \\d+}")
	@Operation(summary = "Liefert die Schülerleistungsdaten zu der angegebenen ID.",
			description = "Liest die Schülerleistungsdaten zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerleistungsdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Leistungsdaten des Schülers",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchuelerLeistungsdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerleistungsdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Schülerleistungsdaten-Eintrag mit der angegebenen ID gefunden")
	public Response getSchuelerLeistungsdatenByID(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerLeistungsdaten(conn).getByIdAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen von Schülerleistungsdaten.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Schülerleistungsdaten
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/leistungsdaten/{id : \\d+}")
	@Operation(summary = "Passt die Schülerleistungsdaten mit der angebenen ID an.",
			description = "Passt die Schülerleistungsdaten mit der angebenen ID an. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schülerleistungsdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchSchuelerLeistungsdaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Schülerleistungsdaten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = SchuelerLeistungsdaten.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerLeistungsdaten(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen von Leistungsdaten.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten der Leistungsdaten
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den neuen Leistungsdaten
	 */
	@POST
	@Path("/leistungsdaten/create")
	@Operation(summary = "Erstellt neue Leistungsdaten und gibt das zugehörige Objekt zurück.",
			description = "Erstellt neue Leistungsdaten und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Hinzufügen von Leistungsdaten besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Leistungsdaten wurden erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuelerLeistungsdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Leistungsdaten hinzuzufügen.")
	@ApiResponse(responseCode = "404", description = "Daten für die Leistungsdaten (z.B. Fächer) wurden nicht gefunden und konnten nicht zugeordnet werden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addSchuelerLeistungsdaten(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden Leistungsdaten ohne ID, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = SchuelerLeistungsdaten.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerLeistungsdaten(conn).addAsResponse(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen mehrerer Leistungsdaten.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten der Räume
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der Liste der neuen Leistungsdaten
	 */
	@POST
	@Path("/leistungsdaten/create/multiple")
	@Operation(summary = "Erstellt mehrere Leistungsdaten und gibt die zugehörigen Objekte zurück.",
			description = "Erstellt mehrere Leistungsdaten und gibt die zugehörigen Objekte zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum zum Hinzufügen von Leistungsdaten besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Leistungsdaten wurden erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = SchuelerLeistungsdaten.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Leistungsdaten hinzuzufügen.")
	@ApiResponse(responseCode = "404", description = "Daten für die Leistungsdaten (z.B. Fächer) wurden nicht gefunden und konnten nicht zugeordnet werden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addSchuelerLeistungsdatenMultiple(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden Leistungsdaten ohne IDs, welche automatisch generiert werden", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = SchuelerLeistungsdaten.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerLeistungsdaten(conn).addMultipleAsResponse(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen von Leistungsdaten.
	 *
	 * @param schema       das Datenbankschema
	 * @param id           die ID der Leistungsdaten
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. den gelöschten Leistungsdaten
	 */
	@DELETE
	@Path("/leistungsdaten/{id : \\d+}")
	@Operation(summary = "Entfernt Leistungsdaten.",
			description = "Entfernt Leistungsdaten. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen von Leistungsdaten hat.")
	@ApiResponse(responseCode = "200", description = "Die Leistungsdaten wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchuelerLeistungsdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Leistungsdaten zu entfernen.")
	@ApiResponse(responseCode = "404", description = "Die Leistungsdaten sind nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteSchuelerLeistungsdaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerLeistungsdaten(conn).deleteAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Leistungsdaten.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           die IDs der Leistungsdaten
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. den gelöschten Leistungsdaten
	 */
	@DELETE
	@Path("/leistungsdaten/delete/multiple")
	@Operation(summary = "Entfernt mehrere Leistungsdaten.",
			description = "Entfernt mehrere Leistungsdaten."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen von Leistungsdaten hat.")
	@ApiResponse(responseCode = "200", description = "Die Leistungsdaten wurden erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerLeistungsdaten.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Leistungsdaten zu entfernen.")
	@ApiResponse(responseCode = "404", description = "Die Leistungsdaten sind zumindest nicht alle vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteSchuelerLeistungsdatenMultiple(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Leistungsdaten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerLeistungsdaten(conn).deleteMultipleAsResponse(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die Fahrschülerarten.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Fahrschülerarten
	 */
	@GET
	@Path("fahrschuelerarten")
	@Operation(summary = "Gibt den Katalog der Fahrschülerarten zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Fahrschülerarten unter Angabe der ID, eines Kürzels und der Bezeichnung. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Fahrschülerarten-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Fahrschülerart-Katalog-Einträge gefunden")
	public Response getSchuelerFahrschuelerarten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogSchuelerFahrschuelerarten(conn).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste aller Erzieher.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 * @param id		die ID des Schülers, dessen Erzieher zurückegeben werden.
	 *
	 * @return die Liste mit den einzelnen Erziehern
	 */
	@GET
	@Path("/{id : \\d+}/erzieher")
	@Operation(summary = "Gibt die Stammdaten der Erzieher zurück.",
			description = "Erstellt eine Liste aller in der Datenbank vorhandenen Erzieher unter Angabe der Schüler-ID"
					+ "des Vor- und Nachnamens, Erzieherart, Kontaktdaten, ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Erzieherdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Erzieherstammdaten",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ErzieherStammdaten.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Erzieherdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Erzieher-Einträge gefunden")
	public Response getSchuelerErzieher(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> (new DataErzieherStammdaten(conn)).getListFromSchueler(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste aller Betriebe.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 * @param id		die ID des Schülers, dessen Betriebe zurückegeben werden.
	 *
	 * @return die Liste mit den einzelnen Betrieben
	 */
	@GET
	@Path("/{id : \\d+}/betriebe")
	@Operation(summary = "Gibt die Betriebe eines Schülers zurück.",
			description = "Erstellt eine Liste aller in der Datenbank vorhandenen Betriebe unter Angabe der Schüler-ID"
					+ "des Vor- und Nachnamens, Erzieherart, Kontaktdaten, ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Betriebsdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Schülerbetrieben",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerBetriebsdaten.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Erzieherdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Erzieher-Einträge gefunden")
	public Response getSchuelerBetriebe(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerBetriebsdaten(conn).getListFromSchueler(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste aller Betriebsstammdaten eines Schülers.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 * @param id        die ID des Schülers
	 *
	 * @return die Liste der Betriebsstammdaten eines Schülers
	 */
	@GET
	@Path("/{id : \\d+}/betriebsstammdaten")
	@Operation(summary = "Gibt eine Liste der Betriebsstammdaten eines Schülers zurück.",
			description = "Erstellt eine Liste aller in der Datenbank vorhandenen Betriebe eines Schülers unter Angabe der ID," // TODO Beschreibung anpassen.
					+ "ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Betriebsdaten des Schülers besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von von Betriebsstammdaten eines Schülers",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = BetriebStammdaten.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Betriebdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Betrieb-Einträge gefunden")
	public Response getSchuelerBetriebsstammdaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataBetriebsStammdaten(conn).getSchuelerBetriebe(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Katalogs der Übergangsempfehlungen der
	 * Grundschule für die Sekundarstufe I.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Übergangsempfehlungen der Grundschule für die Sekundarstufe I.
	 */
	@GET
	@Path("/allgemein/uebergangsempfehlung")
	@Operation(summary = "Gibt den Katalog der Übergangsempfehlungen der Grundschule für die Sekundarstufe I zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhandenen Übergangsempfehlungen der Grundschule für die Sekundarstufe I. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = UebergangsempfehlungKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getKatalogUebergangsempfehlung(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogUebergangsempfehlung().getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Katalogs der Herkünfte von Schülern.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Herkünfte von Schülern.
	 */
	@GET
	@Path("/allgemein/herkuenfte")
	@Operation(summary = "Gibt den Katalog der Herkünfte von Schülern zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhandenen Herkünfte von Schülern. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = HerkunftKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getKatalogHerkuenfte(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogHerkuenfte().getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Katalogs der Herkunftsarten bei Schülern.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Herkunftsarten bei Schülern.
	 */
	@GET
	@Path("/allgemein/herkunftsarten")
	@Operation(summary = "Gibt den Katalog der Herkunftsarten bei Schülern zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhandenen Herkunftsarten bei Schülern. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = HerkunftsartKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getKatalogHerkunftsarten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogHerkunftsarten().getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Vermerke eines Schülers.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden
	 *                soll
	 * @param id      die Datenbank-ID zur Identifikation des Schülers
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die Vermerkdaten des Schülers
	 */
	@GET
	@Path("/{id : \\d+}/vermerke")
	@Operation(summary = "Liefert zu der ID des Schülers die zugehörigen Vermerkdaten.",
			description = "Liest die Vermerkdaten des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten und Vermerke besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Vermerkdaten des Schülers",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerVermerke.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-Vermerk-Eintrag mit der angegebenen ID gefunden")
	public Response getVermerkdaten(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerVermerke(conn).getBySchuelerIdAsResponse(id),
				request, ServerMode.DEV,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Vermerke eines Schülers.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden
	 *                soll
	 * @param vermerkArt      die Datenbank-ID zur Identifikation des Vermerart
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die Vermerkdaten des Schülers
	 */
	@GET
	@Path("/vermerke/vermerkart/{vermerkArt : \\d+}")
	@Operation(summary = "Liefert zu der ID des Schülers die zugehörigen Vermerkdaten.",
			description = "Liest die Vermerkdaten des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten und Vermerke besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Vermerkdaten des Schülers",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerVermerke.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-Vermerk-Eintrag mit der angegebenen ID gefunden")
	public Response getVermerkdatenByVermerkArt(@PathParam("schema") final String schema, @PathParam("vermerkArt") final long vermerkArt,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerVermerke(conn).getListByVermerkartIdAsResponse(vermerkArt),
				request, ServerMode.DEV,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen der Vermerke eines Schülers.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param vid       die ID des Vermerkes welcher gepatches wird
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/vermerke/{vid : \\d+}")
	@Operation(summary = "Liefert zu der ID des Schülers.",
			description = "Passt die Vermerke zu der angegebenen Schüler-ID und der angegeben VermerkeId an und speichert das Ergebnis in der Datenbank."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Sprachbelegungen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Vermerke integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Vermerkdaten der Schüler zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden oder keine Sprachbelegung für die Sprache gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchSchuelerVermerke(@PathParam("schema") final String schema, @PathParam("vid") final long vid,
			@RequestBody(description = "Der Patch für die Vermerke", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuelerVermerke.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerVermerke(conn).patchAsResponse(vid, is),
				request, ServerMode.DEV,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_VERMERKE_AENDERN);
	}


	/**
	 *
	 * Erzeugt einen Schpler-Vermerk und gibt diesen zurück
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param is 		der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return HTTP_201 und der angelegte Schueler-Vermerk, wenn erfolgreich. <br>
	 *         HTTP_400, wenn Fehler bei der Validierung auftreten HTTP_403 bei fehlender Berechtigung,<br>
	 *         HTTP_404, wenn der Eintrag nicht gefunden wurde
	 */
	@POST
	@Path("/vermerke")
	@Operation(summary = "Erstellt einen neuen Vermerk Eintrag", description = "Erstellt einen neuen Vermerk Eintrag"
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Vermerkdaten besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Vermerke des Schülers",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchuelerVermerke.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Vermerke anzulegen.")
	public Response addVermerk(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten des Vermerks", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuelerVermerke.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerVermerke(conn).addAsResponse(is),
				request, ServerMode.DEV,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_VERMERKE_AENDERN);
	}


	/**
	 * Löscht ein SchülerVermerk-Eintrag anhand dessen Id
	 *
	 * @param schema         das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param schuelerID     die Schueler-ID
	 * @param idVermerk      die Datenbank-ID des Schüler-Vermerks
	 * @param request        die Informationen zur HTTP-Anfrage
	 *
	 * @return HTTP_204, wenn erfolgreich. <br>
	 *         HTTP_403 bei fehlender Berechtigung,<br>
	 *         HTTP_404, wenn der Eintrag nicht gefunden wurde
	 */
	@DELETE
	@Path("/{id : \\d+}/vermerke/{idVermerk : \\d+}")
	@Operation(summary = "Löscht einen Schueler-Vermerk", description = "Löscht einen Schueler-Vermerk"
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schüler-Vermerken besitzt.")
	@ApiResponse(responseCode = "204", description = "Der Vermerk des Schülers wurde gelöscht")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzulegen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-Vermerk mit der angegebenen ID gefunden")
	public Response deleteSchuelerVermerk(@PathParam("schema") final String schema, @PathParam("id") final long schuelerID,
			@PathParam("idVermerk") final long idVermerk, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerVermerke(conn).deleteAsResponse(idVermerk),
				request, ServerMode.DEV,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_VERMERKE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Einwilligungen eines Schülers.
	 *
	 * @param schema       das Datenbankschema, auf welchem die Abfrage ausgeführt werden soll
	 * @param idSchueler   die Datenbank-ID zur Identifikation des Schülers
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die Einwilligungen des Schülers
	 */
	@GET
	@Path("/{idSchueler : \\d+}/einwilligungen")
	@Operation(summary = "Liefert zu der ID des Schülers die zugehörigen Einwilligungen.",
			description = "Liest die Einwilligungen des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Einwilligungen des Schülers",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerEinwilligung.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Einwilligungen für den Schüler mit der angegebenen ID gefunden")
	public Response getSchuelerEinwilligungen(@PathParam("schema") final String schema, @PathParam("idSchueler") final long idSchueler,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerEinwilligungen(conn, idSchueler).getListAsResponse(),
				request, ServerMode.DEV,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen einer Einwilligung.
	 *
	 * @param schema               das Datenbankschema, auf welchem der Patch ausgeführt werden soll
	 * @param idSchueler           die Schueler-ID
	 * @param idEinwilligungsart   die ID der Einwilligungsart, zu welcher die zu patchende Einwilligung gehört
	 * @param is                   der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request              die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{idSchueler : \\d+}/einwilligungen/{idEinwilligungsart : \\d+}")
	@Operation(summary = "Passt die Einwilligung zu der angegebenen Schüler- und Einwilligungsart-ID an.",
			description = "Passt die Einwilligung zu der angegebenen Schüler- und Einwilligungsart-ID an und speichert das Ergebnis in der Datenbank."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schüler-Einwilligungen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Einwilligung integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Einwilligungen der Schüler zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler oder keine Einwilligung der angegebenen Art gefunden.")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde."
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchSchuelerEinwilligung(@PathParam("schema") final String schema, @PathParam("idSchueler") final long idSchueler,
			@PathParam("idEinwilligungsart") final long idEinwilligungsart,
			@RequestBody(description = "Der Patch für die Einwilligung", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuelerEinwilligung.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerEinwilligungen(conn, idSchueler).patchAsResponse(new Long[]{idSchueler, idEinwilligungsart},
						is),
				request, ServerMode.DEV,
				//TODO: Benutzerkompetenz hinzufügen
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der KAOADaten eines Schülers.
	 *
	 * @param schema       das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idSchueler   die Datenbank-ID zur Identifikation des Schülers
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die KAOADaten des Schülers
	 */
	@GET
	@Path("/{id : \\d+}/kaoa")
	@Operation(summary = "Liefert zu der ID des Schülers die zugehörigen KAOADaten.",
			description = "Liest die KAOADaten des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die KAOADaten des Schülers",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerKAoADaten.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-KAoA-Eintrag mit der angegebenen ID gefunden")
	public Response getKAoAdaten(@PathParam("schema") final String schema, @PathParam("id") final long idSchueler,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerKAoADaten(conn, idSchueler).getAllAsResponse(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}


	/**
	 *
	 * Erzeugt einen SchuelerKAoADaten-Eintrag und gibt diesen zurück
	 *
	 * @param schema       das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idSchueler   die Schueler-ID
	 * @param is           der InputStream, mit dem JSON-Patch-Objekt der KAoA-Klassendaten
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return HTTP_201 und die angelegten SchuelerKAoADaten, wenn erfolgreich. <br>
	 *         HTTP_400, wenn Fehler bei der Validierung auftreten HTTP_403 bei
	 *         fehlender Berechtigung,<br>
	 *         HTTP_404, wenn der Eintrag nicht gefunden wurde
	 *
	 */
	@POST
	@Path("/{id : \\d+}/kaoa/create")
	@Operation(summary = "Erstellt einen neuen SchuelerKAoADaten Eintrag", description = "Erstellt einen neuen SchuelerKAoADaten Eintrag"
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von SchülerKAoADaten besitzt.")
	@ApiResponse(responseCode = "201", description = "Die KAOADaten des Schülers",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchuelerKAoADaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzulegen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-KAoA-Eintrag mit der angegebenen ID gefunden")
	public Response addKAoAdaten(@PathParam("schema") final String schema, @PathParam("id") final long idSchueler,
			@RequestBody(description = "Die KAoa Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = SchuelerKAoADaten.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerKAoADaten(conn, idSchueler).addAsResponse(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_KAOA_DATEN_AENDERN);
	}


	/**
	 * Ändert einen SchuelerKAoADaten-Eintrag anhand seiner ID
	 *
	 * @param schema           das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idSchueler       die Schueler-ID
	 * @param schuelerKAoAId   die Datenbank-ID der Schüler-KAoA-Daten
	 * @param is			   der InputStream
	 * @param request          die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 *
	 */
	@PATCH
	@Path("/{id : \\d+}/kaoa/{skid : \\d+}")
	@Operation(summary = "Ändert einen SchuelerKAoADaten Eintrag", description = "Ändert einen SchuelerKAoADaten Eintrag"
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von SchülerKAoADaten besitzt.")
	@ApiResponse(responseCode = "204", description = "Die KAOADaten des Schülers wurden erfolgreich gepatcht")
	@ApiResponse(responseCode = "400", description = "Fehler bei der Datenvalidierung")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzulegen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-KAoA-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchKAoADaten(@PathParam("schema") final String schema, @PathParam("id") final long idSchueler,
			@PathParam("skid") final long schuelerKAoAId,
			@RequestBody(description = "Die KAoA Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = SchuelerKAoADaten.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerKAoADaten(conn, idSchueler).patchAsResponse(schuelerKAoAId, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_KAOA_DATEN_AENDERN);
	}


	/**
	 * Löscht ein SchuelerKAoADaten-Eintrag anhand dessen Id
	 *
	 * @param schema           das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idSchueler       die Schueler-ID
	 * @param schuelerKAoAID   die Datenbank-ID der Schüler-KAoA-Daten
	 * @param request          die Informationen zur HTTP-Anfrage
	 *
	 * @return HTTP_204, wenn erfolgreich. <br>
	 *         HTTP_403 bei fehlender Berechtigung,<br>
	 *         HTTP_404, wenn der Eintrag nicht gefunden wurde
	 */
	@DELETE
	@Path("/{id : \\d+}/kaoa/{skid : \\d+}")
	@Operation(summary = "Löscht einen SchuelerKAoADaten Eintrag", description = "Löscht einen SchuelerKAoADaten Eintrag"
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von SchülerKAoADaten besitzt.")
	@ApiResponse(responseCode = "204", description = "Die KAOADaten des Schülers wurden gelöscht")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzulegen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-KAoA-Eintrag mit der angegebenen ID gefunden")
	public Response deleteKAoAdaten(@PathParam("schema") final String schema, @PathParam("id") final long idSchueler,
			@PathParam("skid") final long schuelerKAoAID, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerKAoADaten(conn, idSchueler).deleteAsResponse(schuelerKAoAID),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_KAOA_DATEN_AENDERN);
	}



	/**
	 * Die OpenAPI-Methode für die Abfrage der Sprachbelegungen eines Schülers.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id      die Datenbank-ID zur Identifikation des Schülers
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die Spachbelegungen des Schülers
	 */
	@GET
	@Path("/{id : \\d+}/sprachen/belegungen")
	@Operation(summary = "Liefert zu der ID des Schülers die zugehörigen Sprachbelegungen.",
			description = "Liest die Spachbelegungen des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Spachbelegungen des Schülers",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Sprachbelegung.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Spachbelegungen anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler mit der angegebenen ID gefunden")
	public Response getSchuelerSprachbelegungen(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerSprachbelegung(conn, id).getListAsResponse(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Sprachbelegung eines Schülers in einer Sprache.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Schülers
	 * @param sprache   das Sprachkürzel der Sprache
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Spachbelegungen des Schülers
	 */
	@GET
	@Path("/{id : \\d+}/sprache/{sprache : [A-Z]+}/belegung")
	@Operation(summary = "Liefert zu der ID des Schülers und dem Sprachkürzel die zugehörige Sprachbelegung.",
			description = "Liest die Spachbelegungen zu der Sprache mit dem angegebenen Sprachkürzel des Schülers mit der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Spachbelegung des Schülers",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Sprachbelegung.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Spachbelegung anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler mit der angegebenen ID gefunden")
	public Response getSchuelerSprachbelegung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@PathParam("sprache") final @NotNull String sprache, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerSprachbelegung(conn, id).getByKuerzelAsResponse(sprache),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen der Sprachbelegung eines Schülers in einer Sprache.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Schülers
	 * @param sprache   das Sprachkürzel der Sprache
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{id : \\d+}/sprache/{sprache : [A-Z]+}/belegung")
	@Operation(summary = "Liefert zu der ID des Schülers und dem Sprachkürzel die zugehörige Sprachbelegung.",
			description = "Passt die Sprachbelegung zu der angegebenen Schüler-ID und dem angegebenen Sprachkürzel an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Sprachbelegungen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Sprachbelegung integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Sprachbelegungen zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden oder keine Sprachbelegung für die Sprache gefunden")
	@ApiResponse(responseCode = "409",
			description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchSchuelerSprachbelegung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@PathParam("sprache") final @NotNull String sprache, @RequestBody(description = "Der Patch für die Sprachbelegung", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Sprachbelegung.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerSprachbelegung(conn, id).patchByKuerzelAsResponse(sprache, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN);
	}


	/**
	 * Erzeugt einen Sprachbelegung für den Schüler mit der angebenen ID.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Schueler-ID
	 * @param is        der Input-Stream mit den Daten der Sprachbelegung
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Response im Erfolgsfall mit der neuen Sprachbelegung
	 */
	@POST
	@Path("/{id : \\d+}/sprachen/belegungen")
	@Operation(summary = "Erstellt einen neuen Sprachbelegung für einen Schüler", description = "Erstellt eine neuen Sprachbelegung "
			+ "für den Schüler mit der angebenen ID. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Sprachbelegungen besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Sprachbelegung des Schülers",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Sprachbelegung.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Sprachbelegung anzulegen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler mit der angegebenen ID oder keine Sprache mit dem Kürzel gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addSchuelerSprachbelegung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Die Sprachbelegung", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Sprachbelegung.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerSprachbelegung(conn, id).addAsResponse(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen der Sprachbelegung eines Schülers.
	 *
	 * @param schema    das Datenbankschema
	 * @param id        die Schueler-ID
	 * @param sprache   das Sprachkürzel der Sprache
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. der gelöschten Sprachbelegung
	 */
	@DELETE
	@Path("/{id : \\d+}/sprache/{sprache : [A-Z]+}/belegung")
	@Operation(summary = "Entfernt eine Sprachbelegung eines Schülers.",
			description = "Entfernt eine Sprachbelegung eines Schülers."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Sprachbelegungen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Sprachbelegung wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Sprachbelegung.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Sprachbelegung anzulegen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler mit der angegebenen ID oder keine Sprache mit dem Kürzel gefunden.")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteSchuelerSprachbelegung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@PathParam("sprache") final @NotNull String sprache, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerSprachbelegung(conn, id).deleteByKuerzelAsResponse(sprache),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Sprachprüfungen eines Schülers.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id      die Datenbank-ID zur Identifikation des Schülers
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die Spachprüfungen des Schülers
	 */
	@GET
	@Path("/{id : \\d+}/sprachen/pruefungen")
	@Operation(summary = "Liefert zu der ID des Schülers die zugehörigen Sprachprüfungen.",
			description = "Liest die Sprachprüfungen des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Sprachprüfungen des Schülers",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Sprachpruefung.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Sprachprüfungen anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler mit der angegebenen ID gefunden")
	public Response getSchuelerSprachpruefungen(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerSprachpruefung(conn, id).getListAsResponse(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Sprachprüfung eines Schülers in einer Sprache.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Schülers
	 * @param sprache   das Sprachkürzel der Sprache
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Spachprüfung des Schülers
	 */
	@GET
	@Path("/{id : \\d+}/sprache/{sprache : [A-Z]+}/pruefung")
	@Operation(summary = "Liefert zu der ID des Schülers und dem Sprachkürzel die zugehörige Sprachprüfung.",
			description = "Liest die Sprachprüfung zu der Sprache mit dem angegebenen Sprachkürzel des Schülers mit der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Sprachprüfung des Schülers",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Sprachpruefung.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Sprachprüfung anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler mit der angegebenen ID gefunden")
	public Response getSchuelerSprachpruefung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@PathParam("sprache") final @NotNull String sprache, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerSprachpruefung(conn, id).getByKuerzelAsResponse(sprache),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen der Sprachprüfung eines Schülers in einer Sprache.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Schülers
	 * @param sprache   das Sprachkürzel der Sprache
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{id : \\d+}/sprache/{sprache : [A-Z]+}/pruefung")
	@Operation(summary = "Liefert zu der ID des Schülers und dem Sprachkürzel die zugehörige Sprachprüfung.",
			description = "Passt die Sprachprüfung zu der angegebenen Schüler-ID und dem angegebenen Sprachkürzel an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Sprachprüfungen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Sprachprüfung integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Sprachprüfungen zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden oder keine Sprachprüfung für die Sprache gefunden")
	@ApiResponse(responseCode = "409",
			description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchSchuelerSprachpruefung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@PathParam("sprache") final @NotNull String sprache, @RequestBody(description = "Der Patch für die Sprachprüfung", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Sprachpruefung.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerSprachpruefung(conn, id).patchByKuerzelAsResponse(sprache, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN);
	}


	/**
	 * Erzeugt einen Sprachprüfung für den Schüler mit der angebenen ID.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Schueler-ID
	 * @param is        der Input-Stream mit den Daten der Sprachprüfung
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Response im Erfolgsfall mit der neuen Sprachprüfung
	 */
	@POST
	@Path("/{id : \\d+}/sprachen/pruefungen")
	@Operation(summary = "Erstellt eine neuen Sprachprüfung für einen Schüler", description = "Erstellt eine neuen Sprachprüfung "
			+ "für den Schüler mit der angebenen ID. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Sprachprüfungen besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Sprachprüfung des Schülers",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Sprachpruefung.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Sprachprüfung anzulegen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler mit der angegebenen ID gefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addSchuelerSprachpruefung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Die Sprachpruefung", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Sprachpruefung.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerSprachpruefung(conn, id).addAsResponse(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen der Sprachprüfung eines Schülers.
	 *
	 * @param schema    das Datenbankschema
	 * @param id        die Schueler-ID
	 * @param sprache   das Sprachkürzel der Sprache
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. der gelöschten Sprachprüfung
	 */
	@DELETE
	@Path("/{id : \\d+}/sprache/{sprache : [A-Z]+}/pruefung")
	@Operation(summary = "Entfernt eine Sprachprüfung eines Schülers.",
			description = "Entfernt eine Sprachprüfung eines Schülers."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Sprachprüfungen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Sprachprüfung wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Sprachpruefung.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Sprachbelegung anzulegen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler mit der angegebenen ID oder keine Sprache mit dem Kürzel gefunden.")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteSchuelerSprachpruefung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@PathParam("sprache") final @NotNull String sprache, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerSprachpruefung(conn, id).deleteByKuerzelAsResponse(sprache),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Lernplattformen eines Schülers.
	 *
	 * @param schema       das Datenbankschema, auf welchem die Abfrage ausgeführt werden soll
	 * @param idSchueler   die Datenbank-ID zur Identifikation des Schülers
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die Lernplattformen des Schülers
	 */
	@GET
	@Path("/{id : \\d+}/lernplattformen")
	@Operation(summary = "Liefert zu der ID des Schülers die zugehörigen Lernplattformen.",
			description = "Liest die Lernplattformen des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Lernplattformen des Schülers",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerLernplattform.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Lernplattform für den Schüler mit der angegebenen ID gefunden")
	public Response getSchuelerLernplattformen(@PathParam("schema") final String schema, @PathParam("id") final long idSchueler,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerLernplattformen(conn, idSchueler).getListAsResponse(),
				request, ServerMode.DEV,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen einer Lernplattform.
	 *
	 * @param schema               das Datenbankschema, auf welchem der Patch ausgeführt werden soll
	 * @param idSchueler           die Schueler-ID
	 * @param idLernplattform      die ID der Lernplattform, zu welcher die zu patchende Einwilligung gehört
	 * @param is                   der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request              die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{id : \\d+}/lernplattformen/{idLernplattform : \\d+}")
	@Operation(summary = "Passt die Einwilligung zu der angegebenen Schüler- und Einwilligungsart-ID an.",
			description = "Passt die Einwilligung zu der angegebenen Schüler- und Einwilligungsart-ID an und speichert das Ergebnis in der Datenbank."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schüler-Einwilligungen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Lernplattform integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lernplattform der Schüler zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler oder keine Lernplattform der angegebenen Art gefunden.")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde."
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchSchuelerLernplattform(@PathParam("schema") final String schema, @PathParam("id") final long idSchueler,
			@PathParam("idLernplattform") final long idLernplattform,
			@RequestBody(description = "Der Patch für die Lernplattformen eines Schülers", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuelerLernplattform.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerLernplattformen(conn, idSchueler).patchAsResponse(new Long[]{idSchueler, idLernplattform},
						is),
				request, ServerMode.DEV,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Telefonneinträge eines Schülers.
	 *
	 * @param schema       das Datenbankschema, auf welchem die Abfrage ausgeführt werden soll
	 * @param idSchueler   die Datenbank-ID zur Identifikation des Schülers
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die Telefoneinträge des Schülers
	 */
	@GET
	@Path("/{id : \\d+}/telefone")
	@Operation(summary = "Liefert zu der ID des Schülers die zugehörigen Telefoneinträge.",
			description = "Liest die Telefoneinträge des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Telefoneinträge des Schülers",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerTelefon.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine TelefonArt für den Schüler mit der angegebenen ID gefunden")
	public Response getSchuelerTelefone(@PathParam("schema") final String schema, @PathParam("id") final long idSchueler,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerTelefon(conn, idSchueler).getListAsResponse(),
				request, ServerMode.DEV, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage eines Schülertelefons.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idSchueler        die Datenbank-ID zur Identifikation des Schülertelefons
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Daten zum Schülertelefon
	 */
	@GET
	@Path("/telefon/{id : \\d+}")
	@Operation(summary = "Liefert zu der ID des Schülertelefons die zugehörigen Daten.",
			description = "Liest die Daten des Schülertelefons zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten des Schülertelefons",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchuelerTelefon.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Schülertelefon mit der angegebenen ID gefunden")
	public Response getSchuelerTelefon(@PathParam("schema") final String schema, @PathParam("id") final long idSchueler, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerTelefon(conn, idSchueler).getByIdAsResponse(idSchueler),
				request, ServerMode.DEV, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}

	/**
	 *
	 * Erzeugt einen Schülertelefoneintrag und gibt diesen zurück
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idSchueler die ID des Schülers bei dem das der Eintrag gemacht werden soll
	 * @param is 		 der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return HTTP_201 und der angelegte Schülertelefoneintrag, wenn erfolgreich. <br>
	 *         HTTP_400, wenn Fehler bei der Validierung auftreten HTTP_403 bei fehlender Berechtigung,<br>
	 *         HTTP_404, wenn der Eintrag nicht gefunden wurde
	 */
	@POST
	@Path("/{idSchueler : \\d+}/telefon")
	@Operation(summary = "Erstellt einen neuen Schülertelefoneintrag", description = "Erstellt einen neuen Schülertelefoneintrag"
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Telefone des Schülers",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchuelerTelefon.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülertelefone anzulegen.")
	public Response addSchuelerTelefon(@PathParam("schema") final String schema, @PathParam("idSchueler") final long idSchueler,
			@RequestBody(description = "Die Telefone des Schülers", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = SchuelerTelefon.class))) final InputStream is, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerTelefon(conn, idSchueler).addAsResponse(is),
				request, ServerMode.DEV, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen der Telefoneinträge eines Schülers.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param idSchueler die ID des Schülers bei dem das der Eintrag gepatcht werden soll
	 * @param idTelefon       die ID des Telefoneintrags welcher gepatcht wird
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{idSchueler : \\d+}/telefon/{idTelefon : \\d+}")
	@Operation(summary = "Liefert zu der ID des Schülers.",
			description = "Passt die Vermerke zu der angegebenen Schüler-ID und der angegeben Telefon-ID an und speichert das Ergebnis in der Datenbank."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Sprachbelegungen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Schülertelefoneinträge integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Telefondaten der Schüler zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Schülertelefoneintrag mit der angegebenen ID gefunden oder keine Sprachbelegung für die Sprache "
			+ "gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchSchuelerTelefon(@PathParam("schema") final String schema, @PathParam("idSchueler") final long idSchueler,
			@PathParam("idTelefon") final long idTelefon,
			@RequestBody(description = "Der Patch für die Schülertelefoneinträge", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuelerTelefon.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerTelefon(conn, idSchueler).patchAsResponse(idTelefon, is),
				request, ServerMode.DEV, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
	}

	/**
	 * Löscht ein SchülerTelefoneintrag anhand der Id
	 *
	 * @param schema         das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idSchueler     die Schueler-ID
	 * @param idTelefon      die Datenbank-ID des Schülertelefons
	 * @param request        die Informationen zur HTTP-Anfrage
	 *
	 * @return HTTP_204, wenn erfolgreich. <br>
	 *         HTTP_403 bei fehlender Berechtigung,<br>
	 *         HTTP_404, wenn der Eintrag nicht gefunden wurde
	 */
	@DELETE
	@Path("/{idSchueler : \\d+}/telefon/{idTelefon : \\d+}")
	@Operation(summary = "Löscht einen Schuelertelefoneintrag", description = "Löscht einen Schuelertelefoneintrag"
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "204", description = "Der Telefoneintrag des Schülers wurde gelöscht")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzulegen.")
	@ApiResponse(responseCode = "404", description = "Kein Schülertelefoneinträge mit der angegebenen ID gefunden")
	public Response deleteSchuelerTelefon(@PathParam("schema") final String schema, @PathParam("idSchueler") final long idSchueler,
			@PathParam("idTelefon") final long idTelefon, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerTelefon(conn, idSchueler).deleteAsResponse(idTelefon),
				request, ServerMode.DEV, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
	}

}
