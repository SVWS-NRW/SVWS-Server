package de.svws_nrw.api.server;

import java.io.InputStream;

import de.svws_nrw.asd.adt.PairNN;
import de.svws_nrw.asd.data.lehrer.LehrerAbgangsgrundKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerAnrechnungsgrundKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerBeschaeftigungsartKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerEinsatzstatusKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungAnerkennungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtAnerkennungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungAnerkennungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLeitungsfunktionKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerMehrleistungsartKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerMinderleistungsartKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerRechtsverhaeltnisKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerZugangsgrundKatalogEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.lehrer.LehrerLernplattform;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenLehrerfunktion;
import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.core.data.lehrer.LehrerEinwilligung;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.lehrer.DataKatalogLehrerAbgangsgruende;
import de.svws_nrw.data.lehrer.DataKatalogLehrerAnrechnungsgruende;
import de.svws_nrw.data.lehrer.DataKatalogLehrerBeschaeftigungsarten;
import de.svws_nrw.data.lehrer.DataKatalogLehrerEinsatzstatus;
import de.svws_nrw.data.lehrer.DataKatalogLehrerFachrichtungAnerkennungen;
import de.svws_nrw.data.lehrer.DataKatalogLehrerFachrichtungen;
import de.svws_nrw.data.lehrer.DataKatalogLehrerLehraemter;
import de.svws_nrw.data.lehrer.DataKatalogLehrerLehramtAnerkennungen;
import de.svws_nrw.data.lehrer.DataKatalogLehrerLehrbefaehigungAnerkennungen;
import de.svws_nrw.data.lehrer.DataKatalogLehrerLehrbefaehigungen;
import de.svws_nrw.data.lehrer.DataKatalogLehrerLeitungsfunktionen;
import de.svws_nrw.data.lehrer.DataKatalogLehrerMehrleistungsarten;
import de.svws_nrw.data.lehrer.DataKatalogLehrerMinderleistungsarten;
import de.svws_nrw.data.lehrer.DataKatalogLehrerRechtsverhaeltnis;
import de.svws_nrw.data.lehrer.DataKatalogLehrerZugangsgruende;
import de.svws_nrw.data.lehrer.DataLehrerEinwilligungen;
import de.svws_nrw.data.lehrer.DataLehrerLehramt;
import de.svws_nrw.data.lehrer.DataLehrerLernplattformen;
import de.svws_nrw.data.lehrer.DataLehrerPersonalabschnittsdaten;
import de.svws_nrw.data.lehrer.DataLehrerPersonalabschnittsdatenAnrechungen;
import de.svws_nrw.data.lehrer.DataLehrerPersonalabschnittsdatenLehrerfunktionen;
import de.svws_nrw.data.lehrer.DataLehrerPersonalabschnittsdatenMehrleistungen;
import de.svws_nrw.data.lehrer.DataLehrerPersonalabschnittsdatenMinderleistungen;
import de.svws_nrw.data.lehrer.DataLehrerPersonaldaten;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.data.lehrer.DataLehrerliste;
import de.svws_nrw.db.utils.ApiOperationException;
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
import jakarta.ws.rs.core.Response.Status;


/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Lehrerdaten aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/lehrer/...
 */
@Path("/db/{schema}/lehrer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APILehrer {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APILehrer() {
		// leer
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste aller Lehrer.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den einzelnen Lehrern
	 */
	@GET
	@Path("/")
	@Operation(summary = "Gibt eine Übersicht von allen Lehrern zurück.",
			description = "Erstellt eine Liste aller in der Datenbank vorhanden Lehrer unter Angabe der ID, des Kürzels, "
					+ "des Vor- und Nachnamens, der sog. Personentyps, einer Sortierreihenfolge, ob sie in der Anwendung "
					+ "sichtbar bzw. änderbar sein sollen sowie ob sie für die Schulstatistik relevant sein sollen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Lehrer-Listen-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerListeEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Lehrer-Einträge gefunden")
	public Response getLehrer(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerliste(conn).getAllAsResponse(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Lehrer.
	 *
	 * @param schema    das Datenbankschema
	 * @param is        der InputStream, mit der Liste der zu löschenden IDs
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Lösch-Operationen
	 */
	@DELETE
	@Path("/delete/multiple")
	@Operation(summary = "Entfernt mehrere Lehrer.", description = "Entfernt mehrere Lehrer. Dabei wird geprüft, ob alle Vorbedingungen zum Entfernen"
			+ "der Lehrer erfüllt sind und der SVWS-Benutzer die notwendige Berechtigung hat.")
	@ApiResponse(responseCode = "200", description = "Die Lösch-Operationen wurden ausgeführt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrer zu entfernen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteLehrer(@PathParam("schema") final String schema, @RequestBody(description = "Die IDs der zu löschenden Lehrer", required = true,
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransactionOnErrorSimpleResponse(
				conn -> new DataLehrerliste(conn).deleteMultipleAsSimpleResponseList(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRERDATEN_LOESCHEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Stammdaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Lehrers
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Stammdaten des Lehrers
	 */
	@GET
	@Path("/{id : \\d+}/stammdaten")
	@Operation(summary = "Liefert zu der ID des Lehrer die zugehörigen Stammdaten.",
			description = "Liest die Stammdaten des Lehrers zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Stammdaten des Lehrers",
			content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = LehrerStammdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Lehrer-Eintrag mit der angegebenen ID gefunden")
	public Response getLehrerStammdaten(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerStammdaten(conn).getByIdAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRERDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Stammdaten mehrerer Lehrer.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param is        Inputstream mit einer Liste von Lehrer IDs
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Stammdaten der Lehrer
	 */
	@POST
	@Path("stammdaten")
	@Operation(summary = "Liefert zu den Lehrer IDs die zugehörigen Stammdaten.",
			description = "Liest die Stammdaten der Lehrer zu der angegebenen IDs aus der Datenbank und liefert diese zurück."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Stammdaten der Lehrer",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerStammdaten.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Lehrer-Eintrag mit der angegebenen ID gefunden")
	public Response getLehrerStammdatenMultiple(@PathParam("schema") final String schema, @RequestBody(description = "Die IDs der Lehrer", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerStammdaten(conn).getListByIdsAsResponse(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE, BenutzerKompetenz.LEHRERDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen neuer LehrerStammdaten.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten der LehrerStammdaten
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den erstellen LehrerStammdaten
	 */
	@POST
	@Path("/create")
	@Operation(summary = "Erstellt neue LehrerStammdaten und gibt das erstellte Objekt zurück.",
			description = "Erstellt neue LehrerStammdaten und gibt das erstellte Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen neuer LehrerStammdaten besitzt.")
	@ApiResponse(responseCode = "201", description = "Die LehrerStammdaten wurden erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LehrerStammdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um LehrerStammdaten anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addLehrerStammdaten(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden LehrerStammdaten ohne ID, da diese automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LehrerStammdaten.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataLehrerStammdaten(conn).addAsResponse(is), request, ServerMode.STABLE, BenutzerKompetenz.LEHRERDATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen der Stammdaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Lehrers
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{id : \\d+}/stammdaten")
	@Operation(summary = "Patched Lehrer Stammdaten.",
			description = "Passt die Lehrer-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Lehrer-Stammdaten integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Lehrer-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchLehrerStammdaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Lehrer-Stammdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LehrerStammdaten.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerStammdaten(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRERDATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Personaldaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Lehrers
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Personaldaten des Lehrers
	 */
	@GET
	@Path("/{id : \\d+}/personaldaten")
	@Operation(summary = "Liefert zu der ID des Lehrer die zugehörigen Personaldaten.",
			description = "Liest die Personaldaten des Lehrers zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerpersonaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Personaldaten des Lehrers",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = LehrerPersonaldaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Lehrer-Eintrag mit der angegebenen ID gefunden")
	public Response getLehrerPersonaldaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerPersonaldaten(conn).getByIdAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen der Personaldaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Lehrers
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{id : \\d+}/personaldaten")
	@Operation(summary = "Liefert zu der ID des Lehrers die zugehörigen Personaldaten.",
			description = "Passt die Lehrer-Personaldaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Lehrer-Personaldaten integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Lehrer-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchLehrerPersonaldaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Lehrer-Personaldaten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = LehrerPersonaldaten.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerPersonaldaten(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen von Lehramtsdaten in den Personaldaten eines Lehrers.
	 *
	 * @param schema      das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id          die Datenbank-ID zur Identifikation des Lehrers
	 * @param idLehramt   die ID des Lehramtes
	 * @param is          der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{id : \\d+}/personaldaten/lehramt/{idLehramt : \\d+}")
	@Operation(summary = "Führt einen Patch auf einem Lehramtseintrag in den Personaldaten des Lehrers durch.",
			description = "Passt den Lehramtseintrag zu den angegebenen IDs an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Lehrer-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchLehrerLehramt(@PathParam("schema") final String schema, @PathParam("id") final long id, @PathParam("idLehramt") final long idLehramt,
			@RequestBody(description = "Der Patch für den Lehramtseintrag", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = LehrerLehramtEintrag.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> {
			final LehrerLehramtKatalogEintrag tmpLehramt = LehrerLehramt.data().getEintragByID(idLehramt);
			if (tmpLehramt == null)
				throw new ApiOperationException(Status.BAD_REQUEST, "Es existiert kein Lehramt mit der ID %d.".formatted(idLehramt));
			return new DataLehrerLehramt(conn, id).patchAsResponse(new PairNN<>(id, tmpLehramt.schluessel), is);
		}, request, ServerMode.STABLE,
			BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen eines Lehramtes zu den Personaldaten eines Lehrers.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem neuen Datensaz
	 */
	@POST
	@Path("/personaldaten/lehramt")
	@Operation(
			summary = "Erstellt einen neuen Datensatz für ein Lehramt in den Personaldaten eines Lehrers und gibt das zugehörige Objekt zurück.",
			description = "Erstellt einen neuen Datensatz für ein Lehramt in den Personaldaten eines Lehrers und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen besitzt.")
	@ApiResponse(responseCode = "201", description = "Das Lehramt wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = LehrerLehramtEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um ein Lehramt hinzuzufügen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addLehrerLehramt(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten des Lehramtes", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LehrerLehramtEintrag.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataLehrerLehramt(conn, null).addAsResponse(is),
				request,
				ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN
		);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen von Lehramtsdaten aus den Personaldaten eines Lehrers.
	 *
	 * @param schema      das Datenbankschema
	 * @param id          die Datenbank-ID zur Identifikation des Lehrers
	 * @param idLehramt   die ID des Lehramtes
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Datensatz
	 */
	@DELETE
	@Path("/{id : \\d+}/personaldaten/lehramt/{idLehramt : \\d+}")
	@Operation(summary = "Entfernt den Lehramtseintrag in den Personaldaten des Lehrers.",
			description = "Entfernt den Lehramtseintrag in den Personaldaten des Lehrers aus der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Datensatz wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = LehrerLehramtEintrag.class)))
	@ApiResponse(responseCode = "400", description = "Die Anfrage ist fehlerhaft.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu entfernen.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit den angegebenen IDs gefunden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteLehrerLehramt(@PathParam("schema") final String schema, @PathParam("id") final long id, @PathParam("idLehramt") final long idLehramt,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> {
			final LehrerLehramtKatalogEintrag tmpLehramt = LehrerLehramt.data().getEintragByID(idLehramt);
			if (tmpLehramt == null)
				throw new ApiOperationException(Status.BAD_REQUEST, "Es existiert kein Lehramt mit der ID %d.".formatted(idLehramt));
			return new DataLehrerLehramt(conn, id).deleteAsResponse(new PairNN<>(id, tmpLehramt.schluessel));
		}, request, ServerMode.STABLE,
			BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Abschnittsdaten
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Personalabschnittsdaten eines Lehrers zu einem Schuljahresabschnitt
	 */
	@GET
	@Path("/personalabschnittsdaten/{id : \\d+}")
	@Operation(summary = "Liefert zu der ID des Abschnittes die zugehörigen Personalabschnittsdaten.",
			description = "Liest die Personalabschnittsdaten zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerpersonaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Personalabschnittsdaten", content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = LehrerPersonalabschnittsdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Lehrer-Personalabschnittsdaten mit der angegebenen ID gefunden")
	public Response getLehrerPersonalabschnittsdaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerPersonalabschnittsdaten(conn).getByIdAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen der Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Abschnittsdaten
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/personalabschnittsdaten/{id : \\d+}")
	@Operation(summary = "Passt die Lehrer-Personalabschnittsdaten zu der angegebenen ID an.",
			description = "Passt die Lehrer-Personalabschnittsdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personalabschnittsdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Lehrer-Personalabschnittsdaten integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine Personalabschnittsdaten mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchLehrerPersonalabschnittsdaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Lehrer-Personalabschnittsdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LehrerPersonalabschnittsdaten.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerPersonalabschnittsdaten(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Minderleistungen in den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Minderleistung
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Minderleistung aus den Personalabschnittsdaten eines Lehrers
	 */
	@GET
	@Path("/personalabschnittsdaten/minderleistung/{id : \\d+}")
	@Operation(summary = "Liefert zu der ID die zugehörige Minderleistung.",
			description = "Liest die Minderleistung zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerpersonaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Minderleistung", content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Minderleistung mit der angegebenen ID gefunden")
	public Response getLehrerPersonalabschnittsdatenMinderleistung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerPersonalabschnittsdatenMinderleistungen(conn).getByIdAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen einer Minderleistung zu den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten der Minderleistung
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem neuen Datensaz für Entlastungsstunden
	 */
	@POST
	@Path("/personalabschnittsdaten/minderleistung/add")
	@Operation(
			summary = "Erstellt einen neuen Datensatz für eine Minderleistung in den Personalabschnittsdaten eines Lehrers und gibt das zugehörige Objekt zurück.",
			description = "Erstellt einen neuen Datensatz für für eine Minderleistung in den Personalabschnittsdaten eines Lehrers und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen neuer Entlastungsstunden besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Minderleistung wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Minderleistungen anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addLehrerPersonalabschnittsdatenMinderleistung(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden Minderleistung ohne ID, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataLehrerPersonalabschnittsdatenMinderleistungen(conn).addAsResponse(is),
				request,
				ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN
		);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen einer Minderleistung in den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema       das Datenbankschema
	 * @param id           die Datenbank-ID zur Identifikation der Minderleistung
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Datensatz für die Minderleistung
	 */
	@DELETE
	@Path("/personalabschnittsdaten/minderleistung/{id : \\d+}")
	@Operation(summary = "Entfernt die Minderleistung zu der angegebenen ID.",
			description = "Entfernt die Minderleistung zu der angegebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen der Minderleistung hat.")
	@ApiResponse(responseCode = "200", description = "Die Minderleistung wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Minderleistung zu löschen.")
	@ApiResponse(responseCode = "404", description = "Keine Minderleistung mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteLehrerPersonalabschnittsdatenMinderleistung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataLehrerPersonalabschnittsdatenMinderleistungen(conn).deleteAsResponse(id), request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen einer Minderleistung in den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Minderleistung
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/personalabschnittsdaten/minderleistung/{id : \\d+}")
	@Operation(summary = "Passt die Minderleistung zu der angegebenen ID an.",
			description = "Passt die Minderleistung zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personalabschnittsdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Minderleistung integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine Minderleistung mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchLehrerPersonalabschnittsdatenMinderleistung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Minderleistung", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerPersonalabschnittsdatenMinderleistungen(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Minderleistungen in den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Mehrleistung
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Mehrleistung aus den Personalabschnittsdaten eines Lehrers
	 */
	@GET
	@Path("/personalabschnittsdaten/mehrleistung/{id : \\d+}")
	@Operation(summary = "Liefert zu der ID die zugehörige Mehrleistung.",
			description = "Liest die Mehrleistung zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerpersonaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Mehrleistung", content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Mehrleistung mit der angegebenen ID gefunden")
	public Response getLehrerPersonalabschnittsdatenMehrleistung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerPersonalabschnittsdatenMehrleistungen(conn).getByIdAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen einer Mehrleistung zu den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten der Mehrleistung
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der neuen Mehrleistung
	 */
	@POST
	@Path("/personalabschnittsdaten/mehrleistung/add")
	@Operation(
			summary = "Erstellt einen neuen Datensatz für eine Mehrleistung in den Personalabschnittsdaten eines Lehrers und gibt das zugehörige Objekt zurück.",
			description = "Erstellt einen neuen Datensatz für für eine Mehrleistung in den Personalabschnittsdaten eines Lehrers und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen neuer Entlastungsstunden besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Mehrleistung wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Mehrleistungen anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addLehrerPersonalabschnittsdatenMehrleistung(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden Mehrleistung ohne ID, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataLehrerPersonalabschnittsdatenMehrleistungen(conn).addAsResponse(is),
				request,
				ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN
		);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen einer Mehrleistung in den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema       das Datenbankschema
	 * @param id           die Datenbank-ID zur Identifikation der Mehrleistung
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Datensatz für die Mehrleistung
	 */
	@DELETE
	@Path("/personalabschnittsdaten/mehrleistung/{id : \\d+}")
	@Operation(summary = "Entfernt die Mehrleistung zu der angegebenen ID.",
			description = "Entfernt die Mehrleistung zu der angegebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen der Mehrleistung hat.")
	@ApiResponse(responseCode = "200", description = "Die Mehrleistung wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Mehrleistung zu löschen.")
	@ApiResponse(responseCode = "404", description = "Keine Mehrleistung mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteLehrerPersonalabschnittsdatenMehrleistung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataLehrerPersonalabschnittsdatenMehrleistungen(conn).deleteAsResponse(id), request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen einer Mehrleistung in den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Mehrleistung
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/personalabschnittsdaten/mehrleistung/{id : \\d+}")
	@Operation(summary = "Passt die Mehrleistung zu der angegebenen ID an.",
			description = "Passt die Mehrleistung zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personalabschnittsdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Mehrleistung integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine Mehrleistung mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchLehrerPersonalabschnittsdatenMehrleistung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Mehrleistung", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerPersonalabschnittsdatenMehrleistungen(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der allgemeinen Anrechnungen in den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der allgemeinen Anrechnung
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die allgemeine Anrechnung aus den Personalabschnittsdaten eines Lehrers
	 */
	@GET
	@Path("/personalabschnittsdaten/anrechnung/{id : \\d+}")
	@Operation(summary = "Liefert zu der ID die zugehörige allgemeine Anrechnung.",
			description = "Liest die allgemeine Anrechnung zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerpersonaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die allgemeine Anrechnung", content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine allgemeine Anrechnung mit der angegebenen ID gefunden")
	public Response getLehrerPersonalabschnittsdatenAllgemeineAnrechnung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerPersonalabschnittsdatenAnrechungen(conn).getByIdAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen einer allgemeinen Anrechnung zu den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten der allgemeinen Anrechnung
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der neuen allgemeinen Anrechnung
	 */
	@POST
	@Path("/personalabschnittsdaten/anrechnung/add")
	@Operation(
			summary = "Erstellt einen neuen Datensatz für eine allgemeine Anrechnung in den Personalabschnittsdaten eines Lehrers und gibt das zugehörige Objekt zurück.",
			description = "Erstellt einen neuen Datensatz für für eine Mehrleistung in den Personalabschnittsdaten eines Lehrers und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen neuer Entlastungsstunden besitzt.")
	@ApiResponse(responseCode = "201", description = "Die allgemeine Anrechnung wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine allgemeine Anrechnung anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addLehrerPersonalabschnittsdatenAllgemeineAnrechnung(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden allgemeinen Anrechnung ohne ID, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataLehrerPersonalabschnittsdatenAnrechungen(conn).addAsResponse(is),
				request,
				ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN
		);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen einer allgemeinen Anrechnung in den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema       das Datenbankschema
	 * @param id           die Datenbank-ID zur Identifikation der allgemeinen Anrechnung
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Datensatz für die allgemeine Anrechnung
	 */
	@DELETE
	@Path("/personalabschnittsdaten/anrechnung/{id : \\d+}")
	@Operation(summary = "Entfernt die allgemeine Anrechnung zu der angegebenen ID.",
			description = "Entfernt die allgemeine Anrechnung zu der angegebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen der allgemeinen Anrechnun hat.")
	@ApiResponse(responseCode = "200", description = "Die allgemeine Anrechnung wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine allgemeine Anrechnung zu löschen.")
	@ApiResponse(responseCode = "404", description = "Keine allgemeine Anrechnung mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteLehrerPersonalabschnittsdatenAllgemeineAnrechnung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataLehrerPersonalabschnittsdatenAnrechungen(conn).deleteAsResponse(id), request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen einer allgemeinen Anrechnung in den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der allgemeinen Anrechnung
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/personalabschnittsdaten/anrechnung/{id : \\d+}")
	@Operation(summary = "Passt die allgemeine Anrechnung zu der angegebenen ID an.",
			description = "Passt die allgemeine Anrechnung zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personalabschnittsdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die allgemeine Anrechnung integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine allgemeine Anrechnung mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchLehrerPersonalabschnittsdatenAllgemeineAnrechnung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die allgemeine Anrechnung", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerPersonalabschnittsdatenAnrechungen(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Funktionen in den Personalabschnittdaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Abschnittsdaten
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Lehrerfunktion aus den Personalabschnittsdaten eines Lehrers
	 */
	@GET
	@Path("/personalabschnittsdaten/lehrerfunktionen/{id : \\d+}")
	@Operation(summary = "Liefert zu der ID die zugehörige Lehrerfunktion.",
			description = "Liest die Lehrerfunktion zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerpersonaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Funktion", content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = LehrerPersonalabschnittsdatenLehrerfunktion.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Lehrerfunktion mit der angegebenen ID gefunden")
	public Response getLehrerPersonalabschnittsdatenLehrerfunktionen(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerPersonalabschnittsdatenLehrerfunktionen(conn).getByIdAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN);
	}

	/**
	 * Die OpenAPI-Methode für das Hinzufügen einer Lehrerfunktion zu den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten der Lehrefunktion
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der neuen Lehrerfunktion
	 */
	@POST
	@Path("/personalabschnittsdaten/lehrerfunktionen/add")
	@Operation(summary = "Erstellt einen neuen Datensatz für eine Lehrerfunktion und gibt das zugehörige Objekt zurück.",
			description = "Erstellt einen neuen Datensatz für eine Lehrerfunktion und gibt das zugehörige Objekt zurück.. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen neuer Lehrerfunktion besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Lehrerfunktion wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LehrerPersonalabschnittsdatenLehrerfunktion.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Lehrerfunktion anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addLehrerPersonalabschnittsdatenLehrerfunktionen(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten des zu erstellenden Lehrerfunktion ohne ID, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LehrerPersonalabschnittsdatenLehrerfunktion.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataLehrerPersonalabschnittsdatenLehrerfunktionen(conn).addAsResponse(is),
				request,
				ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN
		);

	}

	/**
	 * Die OpenAPI-Methode für das Patchen einer Lehrerfunktion in den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation einer Lehrerfunktion
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/personalabschnittsdaten/lehrerfunktionen/{id : \\d+}")
	@Operation(summary = "Passt die Lehrerfunktion zu der angegebenen ID an. ",
			description = "Passt die Lehrerfunktion zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personalabschnittsdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Lehrerfunktion integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine Lehrerfunktion mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchLehrerPersonalabschnittsdatenLehrerfunktionen(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Lehrerfunktion", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LehrerPersonalabschnittsdatenLehrerfunktion.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerPersonalabschnittsdatenLehrerfunktionen(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen einer Lehrerfunktion in den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema       das Datenbankschema
	 * @param id           die Datenbank-ID zur Identifikation der Lehrefunktion
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. der gelöschten Lehrerfunktion
	 */
	@DELETE
	@Path("/personalabschnittsdaten/lehrerfunktionen/{id : \\d+}")
	@Operation(summary = "Entfernt die Lehrerfunktion zu der angegebenen ID an.",
			description = "Entfernt die Lehrerfunktion zu der angegebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen einer Lehrerfunktion hat.")
	@ApiResponse(responseCode = "200", description = "Die Lehrerfunktion wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = LehrerPersonalabschnittsdatenLehrerfunktion.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Lehrerfunktion zu löschen.")
	@ApiResponse(responseCode = "404", description = "Keine Lehrerfunktion mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteLehrerPersonalabschnittsdatenLehrerfunktionen(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataLehrerPersonalabschnittsdatenLehrerfunktionen(conn).deleteAsResponse(id), request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRERDATEN_LOESCHEN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für der Lehrer-Leitungsfunktionen.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Lehrer-Leitungsfunktionen
	 */
	@GET
	@Path("/leitungsfunktionen")
	@Operation(summary = "Gibt den Katalog der Lehrerleitungsfunktionen zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Lehrerleitungsfunktionen unter Angabe der ID und der Bezeichnung. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Lehrerleitungsfunktion-Katalog-Einträgen",
			content = @Content(mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = LehrerLeitungsfunktionKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Lehrerleitungsfunktion-Katalog-Einträge gefunden")
	public Response getLehrerLeitungsfunktionen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogLehrerLeitungsfunktionen().getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für der Lehrerzugangsgründe.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Lehrerzugangsgründe
	 */
	@GET
	@Path("/allgemein/zugangsgruende")
	@Operation(summary = "Gibt den Katalog der Lehrerzugangsgründe zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Lehrerzugangsgründe unter Angabe der ID, der Bezeichnung und des Statistikschlüssels. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Lehrerzugangsgrund-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerZugangsgrundKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Lehrerzugangsgrund-Katalog-Einträge gefunden")
	public Response getLehrerZugangsgruende(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> (new DataKatalogLehrerZugangsgruende()).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für der Lehrerabgangsgründe.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Lehrerabgangsgründe
	 */
	@GET
	@Path("/allgemein/abgangsgruende")
	@Operation(summary = "Gibt den Katalog der Lehrerabgangsgründe zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Lehrerabgangsgründe unter Angabe der ID, der Bezeichnung und des Statistikschlüssels. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Lehrerabgangsgrund-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerAbgangsgrundKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Lehrerabgangsgrund-Katalog-Einträge gefunden")
	public Response getLehrerAbgangsgruende(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> (new DataKatalogLehrerAbgangsgruende()).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die Lehrer Beschäftigungsarten.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Lehrer Beschäftigungsarten
	 */
	@GET
	@Path("/allgemein/beschaeftigungsarten")
	@Operation(summary = "Gibt den Katalog der Beschäftigungsarten zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Beschäftigungsarten unter Angabe der ID, eines Kürzels und der Bezeichnung. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Beschäftigungsart-Katalog-Einträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerBeschaeftigungsartKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Beschäftigungsart-Katalog-Einträge gefunden")
	public Response getLehrerBeschaeftigungsarten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> (new DataKatalogLehrerBeschaeftigungsarten()).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die Lehrer Einsatzstatusarten.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Lehrer Einsatzstatusarten
	 */
	@GET
	@Path("/allgemein/einsatzstatus")
	@Operation(summary = "Gibt den Katalog des Einsatzstatus zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Einsatzstatusarten unter Angabe der ID, eines Kürzels und der Bezeichnung. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Einsatzstatus-Katalog-Einträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerEinsatzstatusKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Einsatzstatus-Katalog-Einträge gefunden")
	public Response getLehrerEinsatzstatus(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> (new DataKatalogLehrerEinsatzstatus()).getList(), request,
				ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die Fachrichtungen von Lehrern.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Fachrichtungen von Lehrern
	 */
	@GET
	@Path("/allgemein/fachrichtungen")
	@Operation(summary = "Gibt den Katalog der Fachrichtungen von Lehrern zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Fachrichtungen von Lehrern. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Fachrichtungens-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerFachrichtungKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Fachrichtungs-Katalog-Einträge gefunden")
	public Response getLehrerFachrichtungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> (new DataKatalogLehrerFachrichtungen()).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die Anerkennungen von Fachrichtungen für Lehrer.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Anerkennungen von Fachrichtungen für Lehrer
	 */
	@GET
	@Path("/allgemein/fachrichtungen_anerkennungen")
	@Operation(summary = "Gibt den Katalog des Anerkennungen von Fachrichtungen für Lehrer zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Anerkennungen von Fachrichtungen für Lehrer. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Anerkennungs-Katalog-Einträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerFachrichtungAnerkennungKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Anerkennungs-Katalog-Einträge gefunden")
	public Response getLehrerFachrichtungAnerkennungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> (new DataKatalogLehrerFachrichtungAnerkennungen()).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die Lehrämter.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Lehrämter
	 */
	@GET
	@Path("/allgemein/lehraemter")
	@Operation(summary = "Gibt den Katalog der Lehrämter zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Lehrämter. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Lehramt-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerLehramtKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Lehramt-Katalog-Einträge gefunden")
	public Response getLehrerLehraemter(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> (new DataKatalogLehrerLehraemter()).getList(), request,
				ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die Anerkennungen von Lehrämtern.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Anerkennungen von Lehrämtern
	 */
	@GET
	@Path("/allgemein/lehraemter_anerkennungen")
	@Operation(summary = "Gibt den Katalog der Anerkennungen von Lehrämtern zurück.",
			description = "Erstellt eine Liste aller Anerkennungen von Lehrämtern. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Anerkennungs-Katalog-Einträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerLehramtAnerkennungKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Anerkennungs-Katalog-Einträge gefunden")
	public Response getLehrerLehramtAnerkennungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> (new DataKatalogLehrerLehramtAnerkennungen()).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die Lehrbefähigungen.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Lehrbefähigungen
	 */
	@GET
	@Path("/allgemein/lehrbefaehigungen")
	@Operation(summary = "Gibt den Katalog des Lehrbefähigungen von Lehrern zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Lehrbefähigungen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Lehrbefähigung-Katalog-Einträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerLehrbefaehigungKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Lehrbefähigung-Katalog-Einträge gefunden")
	public Response getLehrerLehrbefaehigungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> (new DataKatalogLehrerLehrbefaehigungen()).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die Anerkennungen von Lehrbefähigungen.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Anerkennungen von Lehrbefähigungen
	 */
	@GET
	@Path("/allgemein/lehrbefaehigungen_anerkennungen")
	@Operation(summary = "Gibt den Katalog der Anerkennungen von Lehrbefähigungen zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Anerkennungen von Lehrbefähigungen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Einsatzstatus-Katalog-Einträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerLehrbefaehigungAnerkennungKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Anerkennungs-Katalog-Einträge gefunden")
	public Response getLehrerLehrbefaehigungenAnerkennungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> (new DataKatalogLehrerLehrbefaehigungAnerkennungen()).getList(), request,
				ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die Arten von Mehrleistungen durch Lehrer.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Arten von Mehrleistungen durch Lehrer
	 */
	@GET
	@Path("/allgemein/mehrleistungsarten")
	@Operation(summary = "Gibt den Katalog der Arten von Mehrleistungen durch Lehrer zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden die Arten von Mehrleistungen durch Lehrer. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Mehrleistungsart-Katalog-Einträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerMehrleistungsartKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Mehrleistungsart-Katalog-Einträge gefunden")
	public Response getLehrerMehrleistungsarten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> (new DataKatalogLehrerMehrleistungsarten()).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die Arten von Minderleistungen durch Lehrer.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Arten von Minderleistungen durch Lehrer
	 */
	@GET
	@Path("/allgemein/minderleistungsarten")
	@Operation(summary = "Gibt den Katalog der Arten von Minderleistungen durch Lehrer zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Arten von Minderleistungen durch Lehrer. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Minderleistungsart-Katalog-Einträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerMinderleistungsartKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Minderleistungsart-Katalog-Einträge gefunden")
	public Response getLehrerMinderleistungsarten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> (new DataKatalogLehrerMinderleistungsarten()).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die Lehrer Rechtsverhältnisse.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Lehrer Rechtsverhältnisse
	 */
	@GET
	@Path("/allgemein/rechtsverhaeltnisse")
	@Operation(summary = "Gibt den Katalog des Rechtsverhältnisse zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Rechtsverhältnisse unter Angabe der ID, eines Kürzels und der Bezeichnung. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Rechtsverhältnis-Katalog-Einträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerRechtsverhaeltnisKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Rechtsverhältnis-Katalog-Einträge gefunden")
	public Response getLehrerRechtsverhaeltnisse(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> (new DataKatalogLehrerRechtsverhaeltnis()).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für der Gründe für Anrechnungsstunden von Lehrern.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Gründe für Anrechnungsstunden von Lehrern
	 */
	@GET
	@Path("/allgemein/anrechnungsgruende")
	@Operation(summary = "Gibt den Katalog der Gründe für Anrechnungsstunden von Lehrern zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Gründe für Anrechnungsstunden von Lehrern."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Einträgen für Gründe von Anrechnungsstunden von Lehrern",
			content = @Content(mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = LehrerAnrechnungsgrundKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getLehrerAnrechnungsgruende(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> (new DataKatalogLehrerAnrechnungsgruende()).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Einwilligungen eines Lehrers.
	 *
	 * @param schema       das Datenbankschema, auf welchem die Abfrage ausgeführt werden soll
	 * @param idLehrer     die Datenbank-ID zur Identifikation des Lehrers
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die Einwilligungen des Lehrers
	 */
	@GET
	@Path("/{idLehrer : \\d+}/einwilligungen")
	@Operation(summary = "Liefert zu der ID des Lehrers die zugehörigen Einwilligungen.",
			description = "Liest die Einwilligungen des Lehrers zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Einwilligungen des Lehrers",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerEinwilligung.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Lehrerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Einwilligungen für den Lehrer mit der angegebenen ID gefunden")
	public Response getLehrerEinwilligungen(@PathParam("schema") final String schema, @PathParam("idLehrer") final long idLehrer,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerEinwilligungen(conn, idLehrer).getListAsResponse(),
				request, ServerMode.DEV,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen einer Lehrereinwilligung.
	 *
	 * @param schema               das Datenbankschema, auf welchem der Patch ausgeführt werden soll
	 * @param idLehrer             die Lehrer-ID
	 * @param idEinwilligungsart   die ID der Einwilligungsart, zu welcher die zu patchende Lehrereinwilligung gehört
	 * @param is                   der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request              die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{idLehrer : \\d+}/einwilligungen/{idEinwilligungsart : \\d+}")
	@Operation(summary = "Passt die Lehrereinwilligung zu der angegebenen Lehrer- und Einwilligungsart-ID an.",
			description = "Passt die Einwilligung zu der angegebenen Lehrer- und Einwilligungsart-ID an und speichert das Ergebnis in der Datenbank."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrereinwilligungen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Lehrereinwilligung integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Einwilligungen der Lehrer zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Lehrer oder keine Einwilligung der angegebenen Art gefunden.")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde."
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchLehrerEinwilligung(@PathParam("schema") final String schema, @PathParam("idLehrer") final long idLehrer,
			@PathParam("idEinwilligungsart") final long idEinwilligungsart,
			@RequestBody(description = "Der Patch für die Lehrereinwilligung", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LehrerEinwilligung.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerEinwilligungen(conn, idLehrer)
				.patchAsResponse(new Long[] { idLehrer, idEinwilligungsart }, is),
				request, ServerMode.DEV,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Lernplattformen eines Lehrers.
	 *
	 * @param schema       das Datenbankschema, auf welchem die Abfrage ausgeführt werden soll
	 * @param idLehrer     die Datenbank-ID zur Identifikation des Lehrers
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die Lernplattformen des Lehrers
	 */
	@GET
	@Path("/{id : \\d+}/lernplattformen")
	@Operation(summary = "Liefert zu der ID des Lehrers die zugehörigen Lernplattformen.",
			description = "Liest die Lernplattformen des Lehrers zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Lernplattformen des Lehrers",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerLernplattform.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Lehrerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Lernplattform für den Lehrer mit der angegebenen ID gefunden")
	public Response getLehrerLernplattformen(@PathParam("schema") final String schema, @PathParam("id") final long idLehrer,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerLernplattformen(conn, idLehrer).getListAsResponse(),
				request, ServerMode.DEV,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen einer Lernplattform.
	 *
	 * @param schema               das Datenbankschema, auf welchem der Patch ausgeführt werden soll
	 * @param idLehrer             die Lehrer-ID
	 * @param idLernplattform      die ID der Lernplattform, zu welcher die zu patchende Einwilligung gehört
	 * @param is                   der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request              die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/{id : \\d+}/lernplattformen/{idLernplattform : \\d+}")
	@Operation(summary = "Passt die Einwilligung zu der angegebenen Lehrer- und Einwilligungsart-ID an.",
			description = "Passt die Einwilligung zu der angegebenen Lehrer- und Einwilligungsart-ID an und speichert das Ergebnis in der Datenbank."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Einwilligungen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Lernplattform integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lernplattform der Lehrer zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Lehrer oder keine Lernplattform der angegebenen Art gefunden.")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde."
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchLehrerLernplattform(@PathParam("schema") final String schema, @PathParam("id") final long idLehrer,
			@PathParam("idLernplattform") final long idLernplattform,
			@RequestBody(description = "Der Patch für die Lernplattformen eines Lehrers", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LehrerLernplattform.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataLehrerLernplattformen(conn, idLehrer).patchAsResponse(new Long[] { idLehrer, idLernplattform },
						is),
				request, ServerMode.DEV,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN);
	}

}
