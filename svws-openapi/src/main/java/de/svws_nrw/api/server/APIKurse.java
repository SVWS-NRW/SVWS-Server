package de.svws_nrw.api.server;

import de.svws_nrw.asd.data.kurse.KursLehrer;
import de.svws_nrw.data.kurse.DataKursLehrer;
import java.io.InputStream;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.asd.data.kurse.ZulaessigeKursartKatalogEintrag;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.kurse.DataKatalogKursarten;
import de.svws_nrw.data.kurse.DataKurse;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Daten von Kursen aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/api/kurse/...
 */
@Path("/db/{schema}/kurse")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIKurse {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIKurse() {
		// leer
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste der Kurse aller Schuljahresabschniite
	 * im angegebenen Schema.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Kurse aller Schuljahresabschniite
	 */
	@GET
	@Path("/")
	@Operation(summary = "Gibt eine Übersicht von allen Kursen zurück.",
			description = "Erstellt eine Liste aller in der Datenbank vorhanden Kurse unter Angabe der ID, des Kürzels, "
					+ "der Parallelität, der Kürzel des Klassenlehrers und des zweiten Klassenlehrers, "
					+ "einer Sortierreihenfolge und ob sie in der Anwendung sichtbar sein sollen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Klassendaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Kurs-Listen-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KursDaten.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Kursdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Kurs-Einträge gefunden")
	public Response getKurse(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> DataKurse.getKurslistenFuerAbschnittAsResponse(conn, null, true),
				request, ServerMode.STABLE,
				BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste der Kurse im angegebenen Schema.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param abschnitt     die ID des Schuljahresabschnitts
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return              die Liste der Kurse mit der jeweiligen ID im Datenbankschema
	 */
	@GET
	@Path("/abschnitt/{abschnitt : \\d+}")
	@Operation(summary = "Gibt eine Übersicht von allen Kursen eines Schuljahresabschnittes zurück.",
			description = "Erstellt eine Liste aller in der Datenbank vorhanden Kurse eines Schuljahresabschnittes unter "
					+ "Angabe der ID, des Kürzels, der Parallelität, der Kürzel des Klassenlehrers und des zweiten "
					+ "Klassenlehrers, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar sein sollen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Klassendaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Kurs-Listen-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KursDaten.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Kursdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Kurs-Einträge gefunden")
	public Response getKurseFuerAbschnitt(@PathParam("schema") final String schema, @PathParam("abschnitt") final long abschnitt,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> DataKurse.getKurslistenFuerAbschnittAsResponse(conn, abschnitt, true),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Daten eines Kurses.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Kurses
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Daten des Kurses
	 */
	@GET
	@Path("/{id : \\d+}")
	@Operation(summary = "Liefert zu der ID des Kurses die zugehörigen Daten.",
			description = "Liest die Daten des Kurses zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Kursdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten des Kurses",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = KursDaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Kursdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Kurs-Eintrag mit der angegebenen ID gefunden")
	public Response getKurs(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKurse(conn).getByIdAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen eines Kurses.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Kurses
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{id : \\d+}")
	@Operation(summary = "Passt die Daten des Kurses mit der angebenen ID an.",
			description = "Passt die Daten des Kurses mit der angebenen ID an. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Kursdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchKurs(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Daten des Kurses", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = KursDaten.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKurse(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE, BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Erstellen eines neuen Kurses mit den angegebenen Kursdaten.
	 *
	 * @param schema                   das Datenbankschema
	 * @param is                       der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request                  die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den Daten des Kurses
	 */
	@POST
	@Path("/create")
	@Operation(summary = "Erstellt einen neuen Kurs und gibt die zugehörigen Daten zurück.",
			description = "Erstellt einen neuen Kurs und gibt die zugehörigen Daten zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Kurses besitzt.")
	@ApiResponse(responseCode = "201", description = "Der Kurs wurde erfolgreich erstellt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = KursDaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Kurs anzulegen.")
	@ApiResponse(responseCode = "404", description = "Der Schuljahresabschnitt, das Fach oder der Lehrer wurde nicht gefunden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addKurs(@PathParam("schema") final String schema, @RequestBody(description = "Die Daten des Kurses", required = true,
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = KursDaten.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKurse(conn).addAsResponse(is),
				request, ServerMode.STABLE, BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Kurse.
	 *
	 * @param schema    das Datenbankschema
	 * @param is        der InputStream, mit der Liste von zu löschenden IDs
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Lösch-Operationen
	 */
	@DELETE
	@Path("/delete/multiple")
	@Operation(summary = "Entfernt mehrere Kurse.", description = "Entfernt mehrere Kurse. Dabei wird geprüft, ob alle Vorbedingungen zum Entfernen"
			+ "der Kurse erfüllt sind und der SVWS-Benutzer die notwendige Berechtigung hat.")
	@ApiResponse(responseCode = "200", description = "Die Lösch-Operationen wurden ausgeführt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Kurse zu entfernen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteKurse(@PathParam("schema") final String schema, @RequestBody(description = "Die IDs der zu löschenden Kurse", required = true,
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransactionOnErrorSimpleResponse(
				conn -> new DataKurse(conn).deleteMultipleAsSimpleResponseList(JSONMapper.toListOfLong(is)),
				request, ServerMode.DEV,
				BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Katalogs der gültigen Kursarten.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return              der Katalog der gültigen Kursarten
	 */
	@GET
	@Path("/allgemein/kursarten")
	@Operation(summary = "Gibt den Katalog der gültigen Kursarten zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden gültigen Kursarten. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Kursart-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ZulaessigeKursartKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Kursart-Katalog-Einträge gefunden")
	public Response getKatalogKursarten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> (new DataKatalogKursarten()).getAll(), request,
				ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Kurslehrer.
	 *
	 * @param schema       das Datenbankschema, auf welchem die Abfrage ausgeführt werden soll
	 * @param idKurs	   die Datenbank-ID zur Identifikation des Kurses
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die Kurslehrer
	 */
	@GET
	@Path("/{idKurs : \\d+}/kursLehrer")
	@Operation(summary = "Liefert zu der ID des Kurses die zugehörigen Kurslehrer.",
			description = "Liefert zu der ID des Kurses die zugehörigen Kurslehrer, insofern der Benutzer die notwendigen Berechtigungen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Kurslehrer des Kurses",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KursLehrer.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kursdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Kurslehrer für den Kurs mit der angegebenen ID gefunden")
	public Response getKursLehrer(@PathParam("schema") final String schema, @PathParam("idKurs") final long idKurs, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataKursLehrer(conn, idKurs).getListAsResponse(), request, ServerMode.STABLE, BenutzerKompetenz.KEINE
		);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen eines Kurslehrers.
	 *
	 * @param schema		das Datenbankschema, auf welchem der Patch ausgeführt werden soll
	 * @param idKurs		Kurs ID
	 * @param idLehrer		Lehrer ID
	 * @param is			der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request		die Informationen zur HTTP-Anfrage
	 *
	 * @return				das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{idKurs: \\d+}/kursLehrer/{idLehrer: \\d+}")
	@Operation(summary = "Patched den Kurslehrer mit der angegebenen ID.",
			description = "Patched den Kurslehrer mit der angegeben ID, insofern die notwendigen Berechtigungen vorliegen.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z. B. beim Datenbankzugriff)")
	public Response patchKursLehrer(@PathParam("schema") final String schema, @PathParam("idKurs") final long idKurs, @PathParam("idLehrer") final long idLehrer,
			@RequestBody(description = "Der Patch des KursLehrers", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = KursLehrer.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKursLehrer(conn, idKurs).patchAsResponse(new Long[]{idKurs, idLehrer}, is),
				request, ServerMode.DEV, BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN);

	}

	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer KursLehrer.
	 *
	 * @param schema		das Datenbankschema
	 * @param idKurs		Kurs ID
	 * @param is			der Input-Stream mit den Daten des KursLehrers
	 * @param request		die Informationen zur HTTP-Anfrage
	 * @return				die HTTP-Antwort mit dem erstellten KursLehrer
	 */
	@POST
	@Path("/{idKurs: \\d+}/kursLehrer/create")
	@Operation(summary = "Erstellt einen neuen KursLehrer und gibt das erstellte Objekt zurück.",
			description = "Erstellt einen neuen Kurslehrer, insofern die notwendigen Berechtigungen vorliegen.")
	@ApiResponse(responseCode = "201", description = "Der KursLehrer wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = KursLehrer.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um KursLehrer anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addKursLehrer(@PathParam("schema") final String schema, @PathParam("idKurs") final long idKurs,
			@RequestBody(description = "Die Daten des zu erstellenden KursLehrers.", required = true,
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = KursLehrer.class))) final InputStream is,
	@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKursLehrer(conn, idKurs).addAsResponse(is),
				request, ServerMode.DEV, BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer KursLehrer
	 *
	 * @param schema		das Datenbankschema
	 * @param idKurs 		Kurs ID
	 * @param is 			der InputStream, mit der Liste der zu löschenden IDs
	 * @param request 		die Informationen zur HTTP-Anfrage
	 * @return 				die HTTP-Antwort mit dem Status der Lösch-Operationen
	 */
	@DELETE
	@Path("/{idKurs: \\d+}/kursLehrer/delete/multiple")
	@Operation(summary = "Entfernt mehrere KursLehrer.", description = "Entfernt mehrere KursLehrer, insofern die notwendigen Berechtigungen vorhanden sind.")
	@ApiResponse(responseCode = "200", description = "Die Lösch-Operationen wurden ausgeführt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um KursLehrer zu entfernen.")
	@ApiResponse(responseCode = "404", description = "KursLehrer nicht vorhanden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteKursLehrer(@PathParam("schema") final String schema, @PathParam("idKurs") final long idKurs,
			@RequestBody(description = "Die IDs der zu löschenden Kurslehrer",
			required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
			array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransactionOnErrorSimpleResponse(
				conn -> new DataKursLehrer(conn, idKurs).deleteMultipleAsSimpleResponseList(
						JSONMapper
								.toListOfLong(is)
								.stream()
								.map(v -> new Long[]{idKurs, v})
								.toList()),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	}

}
