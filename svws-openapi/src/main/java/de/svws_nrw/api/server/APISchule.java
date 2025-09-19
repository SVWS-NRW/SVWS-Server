package de.svws_nrw.api.server;

import de.svws_nrw.core.data.erzieher.Erzieherart;
import de.svws_nrw.core.data.schule.Lernplattform;
import de.svws_nrw.core.data.schule.TelefonArt;
import de.svws_nrw.data.erzieher.DataErzieherarten;
import de.svws_nrw.data.schule.DataKatalogLernplattformen;
import de.svws_nrw.data.schule.DataKatalogTelefonArten;
import java.io.InputStream;

import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.data.schule.Schulleitung;
import de.svws_nrw.asd.data.schule.VerkehrsspracheKatalogEintrag;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.kataloge.SchulEintrag;
import de.svws_nrw.core.data.schueler.SchuelerEinwilligungsartenZusammenfassung;
import de.svws_nrw.core.data.schueler.SchuelerVermerkartZusammenfassung;
import de.svws_nrw.core.data.schule.AbgangsartKatalog;
import de.svws_nrw.core.data.schule.AllgemeineMerkmaleKatalogEintrag;
import de.svws_nrw.core.data.schule.Aufsichtsbereich;
import de.svws_nrw.asd.data.schule.BerufskollegAnlageKatalogEintrag;
import de.svws_nrw.core.data.schule.BerufskollegBerufsebeneKatalogEintrag;
import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalog;
import de.svws_nrw.core.data.schule.Einwilligungsart;
import de.svws_nrw.asd.data.schule.FoerderschwerpunktKatalogEintrag;
import de.svws_nrw.core.data.schule.HerkunftsschulnummerKatalogEintrag;
import de.svws_nrw.asd.data.schule.KindergartenbesuchKatalogEintrag;
import de.svws_nrw.asd.data.schule.NationalitaetenKatalogEintrag;
import de.svws_nrw.asd.data.NoteKatalogEintrag;
import de.svws_nrw.asd.data.schule.OrganisationsformKatalogEintrag;
import de.svws_nrw.core.data.schule.PruefungsordnungKatalogEintrag;
import de.svws_nrw.core.data.schule.Raum;
import de.svws_nrw.core.data.schule.ReformpaedagogikKatalogEintrag;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.core.data.schule.VermerkartEintrag;
import de.svws_nrw.asd.data.schule.ReligionKatalogEintrag;
import de.svws_nrw.asd.data.schueler.EinschulungsartKatalogEintrag;
import de.svws_nrw.asd.data.schueler.SchuelerStatusKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulabschlussAllgemeinbildendKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulabschlussBerufsbildendKatalogEintrag;
import de.svws_nrw.core.data.schule.SchulenKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulgliederungKatalogEintrag;
import de.svws_nrw.core.data.schule.SchultraegerKatalogEintrag;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenzeit;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.kataloge.DataKatalogAufsichtsbereiche;
import de.svws_nrw.data.kataloge.DataKatalogPausenzeiten;
import de.svws_nrw.data.kataloge.DataKatalogRaeume;
import de.svws_nrw.data.kataloge.DataKatalogZeitraster;
import de.svws_nrw.data.schueler.DataSchuelerEinwilligungsartenZusammenfassung;
import de.svws_nrw.data.schueler.DataSchuelerVermerkartenZusammenfassung;
import de.svws_nrw.data.schule.DataKatalogAbgangsartenAllgemeinbildend;
import de.svws_nrw.data.schule.DataKatalogAbgangsartenBerufsbildend;
import de.svws_nrw.data.schule.DataKatalogAllgemeineMerkmale;
import de.svws_nrw.data.schule.DataKatalogBerufskollegAnlagen;
import de.svws_nrw.data.schule.DataKatalogBerufskollegBerufsebenen;
import de.svws_nrw.data.schule.DataKatalogBerufskollegFachklassen;
import de.svws_nrw.data.schule.DataKatalogEinschulungsarten;
import de.svws_nrw.data.schule.DataKatalogEinwilligungsarten;
import de.svws_nrw.data.schule.DataKatalogFoerderschwerpunkte;
import de.svws_nrw.data.schule.DataKatalogHerkunftsschulnummern;
import de.svws_nrw.data.schule.DataKatalogKindergartenbesuch;
import de.svws_nrw.data.schule.DataKatalogNationalitaeten;
import de.svws_nrw.data.schule.DataKatalogNoten;
import de.svws_nrw.data.schule.DataKatalogOrganisationsformen;
import de.svws_nrw.data.schule.DataKatalogPruefungsordnungen;
import de.svws_nrw.data.schule.DataKatalogReformpaedagogik;
import de.svws_nrw.data.schule.DataKatalogReligionen;
import de.svws_nrw.data.schule.DataKatalogSchulabschluesseAllgemeinbildend;
import de.svws_nrw.data.schule.DataKatalogSchulabschluesseBerufsbildend;
import de.svws_nrw.data.schule.DataKatalogSchulen;
import de.svws_nrw.data.schule.DataKatalogSchulformen;
import de.svws_nrw.data.schule.DataKatalogSchulgliederungen;
import de.svws_nrw.data.schule.DataKatalogSchultraeger;
import de.svws_nrw.data.schule.DataKatalogVerkehrssprachen;
import de.svws_nrw.data.schule.DataReligionen;
import de.svws_nrw.data.schule.DataVermerkarten;
import de.svws_nrw.data.schule.DataSchuelerStatus;
import de.svws_nrw.data.schule.DataSchuleStammdaten;
import de.svws_nrw.data.schule.DataSchulen;
import de.svws_nrw.data.schule.DataSchulleitung;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FieldResult;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Daten der Schule aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/schule/...
 */
@Path("/db/{schema}/schule")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
@SqlResultSetMapping(name = "SQLMappingSchuljahresabschnitte", entities = { @EntityResult(entityClass = Schuljahresabschnitt.class,
		fields = {
				@FieldResult(name = "schuljahr", column = "schuljahr"),
				@FieldResult(name = "abschnitt", column = "abschnitt")
		}) })
public class APISchule {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APISchule() {
		// leer
	}

	/**
	 * Die OpenAPI-Methode für das Initialisieren des Schema mit einer Schulnummer.
	 * Es wird vorausgesetzt, dass bisher keine Schulnummer in dem Schema festgelegt wurde.
	 *
	 * @param schema       das Datenbankschema, in welchem die Schule angelegt wird
	 * @param schulnummer  die Schulnummer
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den neuen Schulstammdaten
	 */
	@POST
	@Path("/init/{schulnummer : \\d+}")
	@Operation(summary = "Legt die Daten für eine neue Schule an und gibt anschließend die Schulstammdaten zurück.",
			description = "Legt die Daten für eine neue Schule an und gibt anschließend die Schulstammdaten zurück."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anlegen der Schule besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Schule wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuleStammdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schule anzulegen.")
	@ApiResponse(responseCode = "404", description = "Keine Schule mit der angegebenen Schulnummer gefunden")
	@ApiResponse(responseCode = "409", description = "Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde, dies ist z.B. der "
			+ "Fall, falls zuvor schon eine Schule angelegt wurde.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response initSchule(@PathParam("schema") final String schema, @PathParam("schulnummer") final int schulnummer,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuleStammdaten(conn).init(schulnummer),
				request, ServerMode.STABLE,
				BenutzerKompetenz.ADMIN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Schulnummer der Schule.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Schulnummer
	 */
	@GET
	@Path("/nummer")
	@Operation(summary = "Gibt die Schulnummer der Schule zurück.",
			description = "Liefert die Schulnummer der Schule. Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Schuldaten"
					+ " besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Schulnummer", content = @Content(mediaType = MediaType.APPLICATION_JSON,
			schema = @Schema(implementation = Integer.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuldaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Schule in der Datenbank vorhanden")
	public Response getSchuleNummer(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuleStammdaten(conn).getSchulnummerResponse(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Stammdaten der Schule.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den Stammdaten
	 */
	@GET
	@Path("/stammdaten")
	@Operation(summary = "Liefert zu der Schule mit dem angegebenen Schema die zugehörigen Stammdaten.",
			description = "Liest die Stammdaten der Schule zum angegebenen Schema aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schuldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Stammdaten der Schule", content = @Content(mediaType = MediaType.APPLICATION_JSON,
			schema = @Schema(implementation = SchuleStammdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuldaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit dem angegebenen Schema gefunden")
	public Response getSchuleStammdaten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuleStammdaten(conn).get(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für das Anpassen der Stammdaten der Schule.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/stammdaten")
	@Operation(summary = "Liefert zu der Schule des Datenbank-Schemas die zugehörigen Stammdaten.",
			description = "Passt die Schul-Stammdaten an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schuldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Schul-Stammdaten integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuldaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchSchuleStammdaten(@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für die Schul-Stammdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuleStammdaten.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuleStammdaten(conn).patch(null, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Schullogos der Schule.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Schullogo als Base64-kodierter String
	 */
	@GET
	@Path("/logo")
	@Operation(summary = "Liefert zu der Schule mit dem angegebenen Schema das zugehörige Schullogo.",
			description = "Liest das Logo der Schule zum angegebenen Schema aus der Datenbank und liefert dieses zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schuldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Das Logo der Schule",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuldaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit dem angegebenen Schema gefunden")
	public Response getSchullogo(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuleStammdaten(conn).getSchullogo(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode zum Setzen des Schullogos der Schule.
	 *
	 * @param schema    das Datenbankschema, auf welches die Anfrage ausgeführt werden soll
	 * @param is        der JSON-String mit dem Schullogo als Base64-kodiertem String
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Schullogo als Base64-kodierter String
	 */
	@PUT
	@Path("/logo")
	@Operation(summary = "Setzt das Logo der Schule.",
			description = "Setzt das Logo der Schule. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schuldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Das Logo der Schule wurde gesetzt")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuldaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag für die Schule gefunden")
	public Response putSchullogo(@PathParam("schema") final String schema, @RequestBody(description = "Das Logo der Schule", required = false,
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuleStammdaten(conn).putSchullogo(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die allgemeinbildenden Schulabschlüsse.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der allgemeinbildenden Schulabschlüssen
	 */
	@GET
	@Path("/allgemein/schulabschluesse/allgemeinbildend")
	@Operation(summary = "Gibt den Katalog der allgemeinbildenden Schulabschlüsse zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden allgemeinbildenden Schulabschlüsse. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von allgemeinbildenden Abschlussart-Katalog-Einträgen",
			content = @Content(mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = SchulabschlussAllgemeinbildendKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Abschlussart-Katalog-Einträge gefunden")
	public Response getSchulabschluesseAllgemeinbildend(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogSchulabschluesseAllgemeinbildend(null).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die berufsbildenden Schulabschlüsse.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der berufsbildenden Schulabschlüssen
	 */
	@GET
	@Path("/allgemein/schulabschluesse/berufsbildend")
	@Operation(summary = "Gibt den Katalog der berufsbildenden Schulabschlüsse zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden berufsbildenden Schulabschlüsse. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von berufsbildenden Abschlussart-Katalog-Einträgen",
			content = @Content(mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = SchulabschlussBerufsbildendKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Abschlussart-Katalog-Einträge gefunden")
	public Response getSchulabschluesseBerufsbildend(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogSchulabschluesseBerufsbildend(null).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die Schulformen.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Schulformen
	 */
	@GET
	@Path("/allgemein/schulformen")
	@Operation(summary = "Gibt den Katalog der Schulformen zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Schulformen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Schulform-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchulformKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Schulform-Katalog-Einträge gefunden")
	public Response getSchulformen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogSchulformen(null).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die Schulgliederungen.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Schulgliederungen
	 */
	@GET
	@Path("/allgemein/schulgliederungen")
	@Operation(summary = "Gibt den Katalog der Schulgliederungen zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Schulgliederungen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Schulgliederung-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchulgliederungKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Schulform-Katalog-Einträge gefunden")
	public Response getSchulgliederungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogSchulgliederungen(null).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die Anlagen am Berufskolleg.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Anlagen am Berufskolleg
	 */
	@GET
	@Path("/allgemein/berufskolleg/anlagen")
	@Operation(summary = "Gibt den Katalog der Anlagen am Berufskolleg zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Anlagen am Berufskolleg. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Berufskolleg-Anlagen-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BerufskollegAnlageKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Berufskolleg-Anlagen-Katalog-Einträge gefunden")
	public Response getBerufskollegAnlagen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogBerufskollegAnlagen(null).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die Berufsebenen am Berufskolleg.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Berufsebenen am Berufskolleg
	 */
	@GET
	@Path("/allgemein/berufskolleg/berufsebenen")
	@Operation(summary = "Gibt den Katalog der Berufsebenen am Berufskolleg zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Berufsebenen am Berufskolleg. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Berufskolleg-Berufsebenen-Katalog-Einträgen",
			content = @Content(mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = BerufskollegBerufsebeneKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Berufskolleg-Berufsebenen-Katalog-Einträge gefunden")
	public Response getBerufskollegBerufsebenen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogBerufskollegBerufsebenen(null).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die Fachklassen am Berufskolleg.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return der Katalog der Fachklassen am Berufskolleg
	 */
	@GET
	@Path("/allgemein/berufskolleg/fachklassen")
	@Operation(summary = "Gibt den Katalog der Fachklassen am Berufskolleg zurück.",
			description = "Gibt den Katalog der Fachklassen am Berufskolleg zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Fachklassen-Katalog für berufsbildende Schulen",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = BerufskollegFachklassenKatalog.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Berufskolleg-Fachklassen-Katalog gefunden")
	public Response getBerufskollegFachklassen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogBerufskollegFachklassen(null).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges der Ausbildungs- und Prüfungsordnungen.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Ausbildungs- und Prüfungsordnungen
	 */
	@GET
	@Path("/allgemein/pruefungsordnungen")
	@Operation(summary = "Gibt den Katalog der Ausbildungs- und Prüfungsordnungen zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Ausbildungs- und Prüfungsordnungen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PruefungsordnungKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getPruefungsordnungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogPruefungsordnungen(null).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * DEPRECATED:
	 * Die OpenAPI-Methode für die Abfrage des Kataloges der Verkehrssprachen in einer Familie.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Verkehrssprachen in einer Familie
	 */
	@GET
	@Path("/allgemein/verkehrssprachen")
	@Operation(summary = "DEPRECATED: Gibt den Katalog der Verkehrssprachen in einer Familie zurück.",
			description = "DEPRECATED: Erstellt eine Liste aller in dem Katalog vorhanden der Verkehrssprachen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = VerkehrsspracheKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	@Deprecated
	public Response getVerkehrssprachen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogVerkehrssprachen(null).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste der Nationalitäten im angegebenen Schema.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return              die Liste der Nationalitäten mit ID des Datenbankschemas
	 */
	@GET
	@Path("/allgemein/nationalitaeten")
	@Operation(summary = "Gibt den Katalog der Nationalitäten zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Nationalitäten. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Nationalitäten-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = NationalitaetenKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Nationalitäten-Katalog-Einträge gefunden")
	public Response getNationaelitaeten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> new DataKatalogNationalitaeten(null).getAll(), request,
				ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Katalogs der Einschulungsarten.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return der Katalog der Einschulungsarten
	 */
	@GET
	@Path("/allgemein/einschulungsarten")
	@Operation(summary = "Gibt den Katalog der Einschulungsarten zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Einschulungsarten. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Einschulungsart-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EinschulungsartKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Einschulungsart-Katalog-Einträge gefunden")
	public Response getEinschulungsarten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> new DataKatalogEinschulungsarten(null).getAll(), request,
				ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Katalogs der Allgemeinen Merkmale bei Schulen und Schülern.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return der Katalog der Allgemeinen Merkmale bei Schulen und Schülern
	 */
	@GET
	@Path("/allgemein/allgemeine_merkmale")
	@Operation(summary = "Gibt den Katalog der Allgemeinen Merkmale bei Schulen und Schülern zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Allgemeinen Merkmale bei Schulen und Schülern. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Allgemeinen-Merkmal-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AllgemeineMerkmaleKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Allgemeine-Merkmal-Katalog-Einträge gefunden")
	public Response getAllgemeineMerkmale(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> new DataKatalogAllgemeineMerkmale(null).getAll(), request,
				ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
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
	@Path("/allgemein/foerderschwerpunkte")
	@Operation(summary = "Gibt den Katalog der Förderschwerpunkte zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Förderschwerpunkte. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Förderschwerpunkt-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FoerderschwerpunktKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Förderschwerpunkt-Katalog-Einträge gefunden")
	public Response getFoerderschwerpunkte(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> new DataKatalogFoerderschwerpunkte().getList(), request,
				ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für die Relgionen bzw. Konfessionen,
	 * welche im Rahmen der amtlichen Schulstatistik verwendet werden.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog der Relgionen bzw. Konfessionen
	 */
	@GET
	@Path("/allgemein/religionen")
	@Operation(summary = "Gibt den Katalog der Relgionen bzw. Konfessionen zurück, welche im Rahmen der amtlichen Schulstatistik verwendet werden.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Relgionen bzw. Konfessionen, welche "
					+ "im Rahmen der amtlichen Schulstatistik verwendet werden. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReligionKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getKatalogReligionen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogReligionen().getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage einer Religion.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id              die Datenbank-ID zur Identifikation der Religion
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Daten zur Religion
	 */
	@GET
	@Path("/religionen/{id : \\d+}")
	@Operation(summary = "Liefert zu der ID der Religion die zugehörigen Daten.",
			description = "Liest die Daten der Religion zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten der Religion",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReligionEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Religion mit der angegebenen ID gefunden")
	public Response getReligion(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataReligionen(conn).getByIdAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen einer neuen Religion.
	 *
	 * @param schema       das Datenbankschema, in welchem die Religion erstellt wird
	 * @param request      die Informationen zur HTTP-Anfrage
	 * @param is           das JSON-Objekt
	 * @return die HTTP-Antwort mit der neuen Religion
	 */
	@POST
	@Path("/religionen/new")
	@Operation(summary = "Erstellt eine neue Religion und gibt sie zurück.",
			description = "Erstellt eine neue Religion und gibt sie zurück."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Religion besitzt.")
	@ApiResponse(responseCode = "201", description = "Religion wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ReligionEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Religion anzulegen.")
	@ApiResponse(responseCode = "404", description = "Keine Religion  mit dem eingegebenen Kuerzel gefunden")
	@ApiResponse(responseCode = "409", description = "Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createReligion(@PathParam("schema") final String schema, @RequestBody(description = "Der Post für die Religion-Daten", required = true,
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ReligionEintrag.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataReligionen(conn).addAsResponse(is),
				request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen einer Betriebsart im angegebenen Schema
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Religion
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/religionen/{id : \\d+}")
	@Operation(summary = "Passt die zu der ID der Religion zugehörigen Stammdaten an.",
			description = "Passt die Religion-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Religion besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Religion-Daten integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Religion-Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine Religion mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchReligion(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Religion-Stammdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ReligionEintrag.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataReligionen(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des schulspezifischen Kataloges für die Religionen bzw. Konfessionen.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return          die Liste mit dem Katalog der Religionen bzw. Konfessionen
	 */
	@GET
	@Path("/religionen")
	@Operation(summary = "Gibt eine Übersicht aller Religionen bzw. Konfessionen im Katalog zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Religionen bzw. Konfessionen unter Angabe der ID, der Bezeichnung sowie der Bezeichnung, "
					+ "welche auf dem Zeugnis erscheint, einem Statistik-Kürzel, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar bzw. änderbar "
					+ "sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReligionEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getReligionen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataReligionen(conn).getAllAsResponse(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen eines Religion-Katalog-Eintrags der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param id           die ID des Religion-Katalog-Eintrags
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Religion-Katalog-Eintrag
	 */
	@DELETE
	@Path("/religionen/{id : \\d+}")
	@Operation(summary = "Entfernt einen Religion-Katalog-Eintrag der Schule.",
			description = "Entfernt einen Religion-Katalog-Eintrag der Schule. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum "
					+ "Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Der Religion-Katalog-Eintrag wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReligionEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Kein Religion-Katalog-Eintrag vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteReligionEintrag(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataReligionen(conn).deleteAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Religion-Katalog-Einträge der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           die IDs der Religion-Katalog-Einträge
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. den gelöschten Religion-Katalog-Einträgen
	 */
	@DELETE
	@Path("/religionen/delete/multiple")
	@Operation(summary = "Entfernt mehrere Religion-Katalog-Einträge der Schule.",
			description = "Entfernt mehrere Religion-Katalog-Einträge der Schule."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Die Religion-Katalog-Einträge wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Religion-Katalog-Einträge nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteReligionEintraege(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Religion-Katalog-Einträge", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataReligionen(conn).deleteMultipleAsSimpleResponseList(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des schulspezifischen Kataloges für die Vermerkarten.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return          die Liste mit dem Katalog der Vermerkarten
	 */
	@GET
	@Path("/vermerkarten")
	@Operation(summary = "Gibt eine Übersicht aller Vermerkarten im Katalog zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Vermerkarten unter Angabe der ID, der Bezeichnung sowie der Bezeichnung, "
					+ "einer Sortierreihenfolge und ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = VermerkartEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getVermerkarten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataVermerkarten(conn).getAllAsResponse(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste aller Schüler.
	 *
	 * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param vermerkart   die ID des Schuljahresabschnitts dessen Schüler zurückgegeben werden sollen
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den einzelnen Schülern
	 */
	@GET
	@Path("/vermerkart/{vermerkart : \\d+}/schuelerinfos")
	@Operation(summary = "Gibt eine Übersicht von allen Schülern welche einen Vermerk mit der angegebenen Vermerkart haben zurück.",
			description = "Erstellt eine Liste aller Schüler der angegebenen Vermerkart unter Angabe der ID."
					+ "Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Schüler-Listen-Einträgen",
			content = @Content(mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = SchuelerVermerkartZusammenfassung.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Schüler-Einträge gefunden")
	public Response getSchuelerByVermerkartID(@PathParam("schema") final String schema, @PathParam("vermerkart") final long vermerkart,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerVermerkartenZusammenfassung(conn).getListByVermerkartIdAsResponse(vermerkart),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage einer Vermerkart.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Vermerkart
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Daten zur Vermerkart
	 */
	@GET
	@Path("/vermerkarten/{id : \\d+}")
	@Operation(summary = "Liefert zu der ID der Vermerkarten die zugehörigen Daten.",
			description = "Liest die Daten der Vermerkart zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten "
					+ "besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten der Vermerkart",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = VermerkartEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Vermerkart mit der angegebenen ID gefunden")
	public Response getVermerkart(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataVermerkarten(conn).getByIdAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für das Erstellen einer neuen Vermerkart.
	 *
	 * @param schema       das Datenbankschema, in welchem die Vermerkart erstellt wird
	 * @param request      die Informationen zur HTTP-Anfrage
	 * @param is           das JSON-Objekt
	 *
	 * @return die HTTP-Antwort mit der neuen Vermerkart
	 */
	@POST
	@Path("/vermerkarten/new")
	@Operation(summary = "Erstellt eine neue Vermerkart und gibt sie zurück.",
			description = "Erstellt eine neue Vermerkart und gibt sie zurück."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Vermerkart besitzt.")
	@ApiResponse(responseCode = "201", description = "Vermerkart wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = VermerkartEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Vermerkart anzulegen.")
	@ApiResponse(responseCode = "404", description = "Keine Vermerkart  mit dem eingegebenen Kuerzel gefunden")
	@ApiResponse(responseCode = "409", description = "Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createVermerkart(@PathParam("schema") final String schema, @RequestBody(description = "Der Post für die Vermerkart-Daten", required = true,
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = VermerkartEintrag.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataVermerkarten(conn).addAsResponse(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen einer Vermerkart im angegebenen Schema
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Vermerkart
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/vermerkarten/{id : \\d+}")
	@Operation(summary = "Passt die zu der ID der Vermerkart zugehörigen Stammdaten an.",
			description = "Passt die Vermerkart-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Vermerkart besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Vermerkart-Daten integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Vermerkart-Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine Vermerkart mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchVermerkart(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Vermerkart-Stammdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = VermerkartEintrag.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataVermerkarten(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen eines Vermerkart-Katalog-Eintrags der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param id           die ID des Vermerkart-Katalog-Eintrags
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Vermerkart-Katalog-Eintrag
	 */
	@DELETE
	@Path("/vermerkarten/{id : \\d+}")
	@Operation(summary = "Entfernt einen Vermerkart-Katalog-Eintrag der Schule.",
			description = "Entfernt einen Vermerkart-Katalog-Eintrag der Schule."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Der Vermerkart-Katalog-Eintrag wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = VermerkartEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Kein Vermerkart-Katalog-Eintrag vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteVermerkartEintrag(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataVermerkarten(conn).deleteAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Vermerkart-Katalog-Einträge der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           die IDs der Vermerkart-Katalog-Einträge
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Lösch-Operationen
	 */
	@DELETE
	@Path("/vermerkarten/delete/multiple")
	@Operation(summary = "Entfernt mehrere Vermerkart-Katalog-Einträge der Schule.",
			description = "Entfernt mehrere Vermerkart-Katalog-Einträge der Schule."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Die Lösch-Operationen wurden ausgeführt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Vermerkart-Katalog-Einträge nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteVermerkartEintraege(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Vermerkart-Katalog-Einträge", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataVermerkarten(conn).deleteMultipleAsResponse(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage des schulspezifischen Kataloges für die Einwilligungsarten.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return          die Liste mit dem Katalog der Einwilligungsarten
	 */
	@GET
	@Path("/einwilligungsarten")
	@Operation(summary = "Gibt eine Übersicht aller Einwilligungsarten im Katalog zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Einwilligungsarten unter Angabe der ID, der Bezeichnung sowie des Schlüssels, "
					+ "einer Sortierreihenfolge und des zugehörigen Personentyps (Schüler, Lehrer, Erzieher). "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen "
					+ "besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Einwilligungsart.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getEinwilligungsarten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogEinwilligungsarten(conn).getAllAsResponse(),
				request, ServerMode.DEV, BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage einer Einwilligungsart.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Einwilligungsart
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Daten zur Einwilligungsart
	 */
	@GET
	@Path("/einwilligungsarten/{id : \\d+}")
	@Operation(summary = "Liefert zu der ID der Einwilligungsart die zugehörigen Daten.",
			description = "Liest die Daten der Einwilligungsart zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten "
					+ "besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten der Einwilligungsart",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Einwilligungsart.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Einwilligungsart mit der angegebenen ID gefunden")
	public Response getEinwilligungsart(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogEinwilligungsarten(conn).getByIdAsResponse(id),
				request, ServerMode.DEV,
				BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen einer neuen Einwilligungsart.
	 *
	 * @param schema       das Datenbankschema, in welchem die Einwilligungsart erstellt wird
	 * @param request      die Informationen zur HTTP-Anfrage
	 * @param is           das JSON-Objekt
	 *
	 * @return die HTTP-Antwort mit der neuen Einwilligungsart
	 */
	@POST
	@Path("/einwilligungsarten/new")
	@Operation(summary = "Erstellt eine neue Einwilligungsart und gibt sie zurück.",
			description = "Erstellt eine neue Einwilligungsart und gibt sie zurück."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Einwilligungsart besitzt.")
	@ApiResponse(responseCode = "201", description = "Einwilligungsart wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Einwilligungsart.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Einwilligungsart anzulegen.")
	@ApiResponse(responseCode = "409", description = "Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response createEinwilligungsart(@PathParam("schema") final String schema,
			@RequestBody(description = "Der initiale Patch für die neue Einwilligungsart", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Einwilligungsart.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogEinwilligungsarten(conn).addAsResponse(is),
				request, ServerMode.DEV,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen einer Einwilligungsart im angegebenen Schema
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Einwilligungsart
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/einwilligungsarten/{id : \\d+}")
	@Operation(summary = "Passt die zu der ID der Einwilligungsart zugehörigen Stammdaten an.",
			description = "Passt die Einwilligungsart-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Einwilligungsart besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Einwilligungsart-Daten integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Einwilligungsart-Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine Einwilligungsart mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchEinwilligungsart(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Einwilligungsart", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = Einwilligungsart.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogEinwilligungsarten(conn).patchAsResponse(id, is),
				request, ServerMode.DEV,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen einer Einwilligungsart der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param id           die ID der Einwilligungsart
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. der gelöschten Einwilligungsart
	 */
	@DELETE
	@Path("/einwilligungsarten/{id : \\d+}")
	@Operation(summary = "Entfernt eine Einwilligungsart der Schule.",
			description = "Entfernt eine Einwilligungsart der Schule."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Die Einwilligungsart wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Einwilligungsart.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Einwilligungsart nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteEinwilligungsart(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogEinwilligungsarten(conn).deleteAsResponse(id),
				request, ServerMode.DEV,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Einwilligungsarten der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           die IDs der Einwilligungsarten
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Lösch-Operationen
	 */
	@DELETE
	@Path("/einwilligungsarten/delete/multiple")
	@Operation(summary = "Entfernt mehrere Einwilligungsarten der Schule.",
			description = "Entfernt mehrere Einwilligungsarten der Schule."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Die Einwilligungsarten wurden erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Einwilligungsarten nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteEinwilligungsarten(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Einwilligungsarten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogEinwilligungsarten(conn).deleteMultipleAsSimpleResponseList(JSONMapper.toListOfLong(is)),
				request, ServerMode.DEV,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste aller Schüler.
	 *
	 * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param einwilligungsart   die ID des Schuljahresabschnitts dessen Schüler zurückgegeben werden sollen
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den einzelnen Schülern
	 */
	@GET
	@Path("/einwilligungsart/{einwilligungsart : \\d+}/schuelerinfos")
	@Operation(summary = "Gibt eine Übersicht von allen Schülern welche einen Einwilligung mit der angegebenen Einwilligungsart haben zurück.",
			description = "Erstellt eine Liste aller Schüler der angegebenen Einwilligungsart unter Angabe der ID."
					+ "Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Schüler-Listen-Einträgen",
			content = @Content(mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = SchuelerEinwilligungsartenZusammenfassung.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Schüler-Einträge gefunden")
	public Response getSchuelerByEinwilligungsartID(@PathParam("schema") final String schema, @PathParam("einwilligungsart") final long einwilligungsart,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuelerEinwilligungsartenZusammenfassung(conn).getListByEinwilligungsartIdAsResponse(einwilligungsart),
				request, ServerMode.DEV,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges der Abgangsarten für
	 * allgemeinbildende Schulformen, welche im Rahmen der amtlichen
	 * Schulstatistik zulässig sind.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return der Katalog der Abgangsarten für allgemeinbildende Schulformen
	 */
	@GET
	@Path("/allgemein/abgangsarten/allgemeinbildend")
	@Operation(summary = "Gibt den Katalog der Katalog der Abgangsarten für allgemeinbildende Schulformen zurück.",
			description = "Gibt den Katalog der Abgangsarten für allgemeinbildende Schulformen zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Katalog",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = AbgangsartKatalog.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.")
	@ApiResponse(responseCode = "404", description = "Katalog nicht gefunden")
	public Response getKatalogAbgangsartenAllgemeinbildend(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogAbgangsartenAllgemeinbildend().getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges der Abgangsarten für
	 * berufsbildende Schulformen, welche im Rahmen der amtlichen
	 * Schulstatistik zulässig sind.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return der Katalog der Abgangsarten für berufsbildende Schulformen
	 */
	@GET
	@Path("/allgemein/abgangsarten/berufsbildend")
	@Operation(summary = "Gibt den Katalog der Katalog der Abgangsarten für berufsbildende Schulformen zurück.",
			description = "Gibt den Katalog der Abgangsarten für berufsbildende Schulformen zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Katalog",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = AbgangsartKatalog.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.")
	@ApiResponse(responseCode = "404", description = "Katalog nicht gefunden")
	public Response getKatalogAbgangsartenBerufsbildend(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogAbgangsartenBerufsbildend().getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}



	/**
	 * Die OpenAPI-Methode für die Abfrage des Katalogs der gültigen Organisationsformen.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return der Katalog der gültigen Organisationsformen
	 */
	@GET
	@Path("/allgemein/organisationsformen")
	@Operation(summary = "Gibt den Katalog der gültigen Organisationsformen zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden gültigen Organisationsformen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Organisationsform-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrganisationsformKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getKatalogOrganisationsformen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> new DataKatalogOrganisationsformen().getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Schulen-Kataloges.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Schulen
	 */
	@GET
	@Path("/allgemein/schulen")
	@Operation(summary = "Erstellt eine Liste aller in dem Katalog vorhandenen Schulen.",
			description = "Erstellt eine Liste aller in dem Katalog vorhandenen Schulen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Schulen-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchulenKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Schulen-Katalog-Einträge gefunden")
	public Response getKatalogSchulen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> new DataKatalogSchulen().getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Schulträger-Kataloges.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Schulträger
	 */
	@GET
	@Path("/allgemein/schultraeger")
	@Operation(summary = "Erstellt eine Liste aller in dem Katalog vorhandenen Schulträger.",
			description = "Erstellt eine Liste aller in dem Katalog vorhandenen Schulträger. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Schulträger-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchultraegerKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Schulträger-Katalog-Einträge gefunden")
	public Response getKatalogSchultraeger(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> new DataKatalogSchultraeger().getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges Schüler-Status.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return der Katalog
	 */
	@GET
	@Path("/schueler/status")
	@Operation(summary = "Die Liste der Einträge aus dem Katalog Schüler-Status.",
			description = "Die Liste der Einträge aus dem Katalog Schüler-Status. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den Katalog Schüler-Status",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerStatusKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getKatalogSchuelerStatus(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> new DataSchuelerStatus().getAll(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges der zusätzlichen Herkunftsschulnummern
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return der Katalog
	 */
	@GET
	@Path("/allgemein/herkunftsschulnummern")
	@Operation(summary = "Die Liste der Einträge aus dem Katalog der zusätzlichen Herkunftsschulnummern.",
			description = "Die Liste der Einträge aus dem Katalog der zusätzlichen Herkunftsschulnummern. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den Katalog der zusätzlichen Herkunftsschulnummern",
			content = @Content(mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = HerkunftsschulnummerKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getKatalogHerkunftsschulnummern(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.run(() -> new DataKatalogHerkunftsschulnummern().getAll(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Reformpädagogik-Kataloges aller Schulformen.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return der Reformpädagogik-Katalog aller Schulformen
	 */
	@GET
	@Path("/allgemein/reformpaedagogik/alle")
	@Operation(summary = "Gibt den Reformpädagogik-Katalog aller Schulformen zurück.",
			description = "Gibt den Reformpädagogik-Katalog aller Schulformen zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Reformpädagogik-Katalog aller Schulformen.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReformpaedagogikKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
	@ApiResponse(responseCode = "404", description = "Keine Fachgruppen gefunden.")
	public Response getKatalogReformpaedagogikAlle(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogReformpaedagogik(conn).getAll(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Reformpädagogik-Kataloges für die Schulform dieser Schule.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den Einträgen des Reformpädagogik-Kataloges für die Schulform dieser Schule
	 */
	@GET
	@Path("/allgemein/reformpaedagogik")
	@Operation(summary = "Gibt den Reformpädagogik-Katalog für die Schulform dieser Schule zurück.",
			description = "Gibt den Reformpädagogik-Katalog für die Schulform dieser Schule zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Reformpädagogik-Katalog für die Schulform dieser Schule.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReformpaedagogikKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
	@ApiResponse(responseCode = "404", description = "Keine Reformpädagogik-Einträge für die Schulform dieser Schule gefunden.")
	public Response getKatalogReformpaedagogik(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogReformpaedagogik(conn).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Reformpädagogik-Katalog-Eintrags für die angegebene ID.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die ID des Reformpädagogik-Katalog-Eintrags
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return der Reformpädagogik-Katalog-Eintrag
	 */
	@GET
	@Path("/allgemein/reformpaedagogik/{id : \\d+}")
	@Operation(summary = "Gibt den Reformpädagogik-Katalog-Eintrag für die angegebene ID zurück.",
			description = "Gibt den Reformpädagogik-Katalog-Eintrag für die angegebene ID zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Reformpädagogik-Katalog-Eintrag für die angegebene ID.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReformpaedagogikKatalogEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
	@ApiResponse(responseCode = "404", description = "Kein Reformpädagogik-Katalog-Eintrag für die angegebene ID gefunden.")
	public Response getKatalogReformpaedagogikEintrag(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogReformpaedagogik(conn).get(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges für Dauer des Kindergartenbesuchs.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit dem Katalog für die Dauer des Kindergartenbesuchs
	 */
	@GET
	@Path("/allgemein/kindergartenbesuch")
	@Operation(summary = "Gibt den Katalog für die Dauer des Kindergartenbesuchs.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Dauern des Kindergartenbesuchs, welche erfasst werden. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KindergartenbesuchKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getKatalogKindergartenbesuchsdauer(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogKindergartenbesuch().getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Kataloges der Noten.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den Einträgen des Noten-Kataloges
	 */
	@GET
	@Path("/allgemein/noten")
	@Operation(summary = "Gibt den Noten-Katalog zurück.",
			description = "Gibt den Noten-Katalog zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Noten-Katalog.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = NoteKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
	@ApiResponse(responseCode = "404", description = "Keine Noten-Einträge gefunden.")
	public Response getKatalogNoten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogNoten().getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Kataloges der Räume der Schule.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den Einträgen des Raum-Kataloges
	 */
	@GET
	@Path("/raeume")
	@Operation(summary = "Gibt den Katalog der Räume der Schule zurück.",
			description = "Gibt den Katalog der Räume der Schule zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Katalog der Räume der Schule.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Raum.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
	@ApiResponse(responseCode = "404", description = "Keine Raum-Einträge gefunden.")
	public Response getRaeume(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogRaeume(conn).getList(),
				request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage eines Raumes der Schule.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id            die ID des Raumes
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return              der Raum der Schule
	 */
	@GET
	@Path("/raeume/{id : \\d+}")
	@Operation(summary = "Gibt den Raum der Schule zurück.",
			description = "Gibt den Raum der Schule zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Raum der Schule",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Raum.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Raum bei der Schule gefunden")
	public Response getRaum(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogRaeume(conn).get(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen eines Raumes der Schule.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Raumes
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/raeume/{id : \\d+}")
	@Operation(summary = "Passt den Raum der Schule mit der angebenen ID an.",
			description = "Passt den Raum der Schule mit der angebenen ID an. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Katalog-Daten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ "(z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchRaum(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für den Raum der Schule", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Raum.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogRaeume(conn).patch(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen eines neuen Raumes zu der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten des Raums
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem neuen Raum
	 */
	@POST
	@Path("/raeume/create")
	@Operation(summary = "Erstellt einen neuen Raum für die Schule und gibt das zugehörige Objekt zurück.",
			description = "Erstellt einen neuen Raum für die Schule und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs besitzt.")
	@ApiResponse(responseCode = "201", description = "Der Raum wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Raum.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Raum für die Schule anzulegen.")
	@ApiResponse(responseCode = "404", description = "Die Katalogdaten wurden nicht gefunden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addRaum(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten des zu erstellenden Raumes ohne ID, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Raum.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogRaeume(conn).add(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen mehrerer neuer Räume zu der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten der Räume
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den neuen Räumen
	 */
	@POST
	@Path("/raeume/create/multiple")
	@Operation(summary = "Erstellt neue Räume für die Schule und gibt die zugehörigen Objekt zurück.",
			description = "Erstellt neue Räume für die Schule und gibt die zugehörigen Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Räume wurden erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Raum.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Räume für die Schule anzulegen.")
	@ApiResponse(responseCode = "404", description = "Die Katalogdaten wurden nicht gefunden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addRaeume(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden Räume ohne IDs, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Raum.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogRaeume(conn).addMultiple(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen eines Raums der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param id           die ID des Raums
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Raum
	 */
	@DELETE
	@Path("/raeume/{id : \\d+}")
	@Operation(summary = "Entfernt einen Raum der Schule.",
			description = "Entfernt einen Raum der Schule."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Der Raum wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Raum.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Kein Raum vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteRaum(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogRaeume(conn).delete(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Räume der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           die IDs der Räume
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. den gelöschten Räumen
	 */
	@DELETE
	@Path("/raeume/delete/multiple")
	@Operation(summary = "Entfernt mehrere Räume der Schule.",
			description = "Entfernt mehrere Räume der Schule."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Die Räume wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Raum.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Räume nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteRaeume(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Räume", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogRaeume(conn).deleteMultiple(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Kataloges der Aufsichtsbereiche der Schule.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den Einträgen des Aufsichtsbereichs-Kataloges
	 */
	@GET
	@Path("/aufsichtsbereiche")
	@Operation(summary = "Gibt den Katalog der Aufsichtsbereiche der Schule zurück.",
			description = "Gibt den Katalog der Aufsichtsbereiche der Schule zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Katalog der Aufsichtsbereiche der Schule.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Aufsichtsbereich.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
	@ApiResponse(responseCode = "404", description = "Keine Aufsichtsbereichs-Einträge gefunden.")
	public Response getAufsichtsbereiche(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogAufsichtsbereiche(conn).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}



	/**
	 * Die OpenAPI-Methode für die Abfrage eines Aufsichtsbereichs der Schule.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id            die ID des Aufsichtsbereichs
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return              der Aufsichtsbereich der Schule
	 */
	@GET
	@Path("/aufsichtsbereiche/{id : \\d+}")
	@Operation(summary = "Gibt den Aufsichtsbereich der Schule zurück.",
			description = "Gibt den Aufsichtsbereich der Schule zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Aufsichtsbereich der Schule",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Aufsichtsbereich.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Aufsichtsbereich bei der Schule gefunden")
	public Response getAufsichtsbereich(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogAufsichtsbereiche(conn).get(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen eines Aufsichtsbereichs der Schule.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Aufsichtsbereichs
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/aufsichtsbereiche/{id : \\d+}")
	@Operation(summary = "Passt den Aufsichtsbereich der Schule mit der angebenen ID an.",
			description = "Passt den Aufsichtsbereich der Schule mit der angebenen ID an. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Katalog-Daten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchAufsichtsbereich(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für den Aufsichtsbereich der Schule", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Aufsichtsbereich.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogAufsichtsbereiche(conn).patch(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen eines neuen Aufsichtsbereichs zu der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten des Aufsichtsbereichs
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem neuen Aufsichtsbereich
	 */
	@POST
	@Path("/aufsichtsbereiche/create")
	@Operation(summary = "Erstellt einen neuen Aufsichtsbereich für die Schule und gibt das zugehörige Objekt zurück.",
			description = "Erstellt einen neuen Aufsichtsbereich für die Schule und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs "
					+ "besitzt.")
	@ApiResponse(responseCode = "201", description = "Der Aufsichtsbereich wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Aufsichtsbereich.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Aufsichtsbereich für die Schule anzulegen.")
	@ApiResponse(responseCode = "404", description = "Die Katalogdaten wurden nicht gefunden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addAufsichtsbereich(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten des zu erstellenden Aufsichtsbereichs ohne ID, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Aufsichtsbereich.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogAufsichtsbereiche(conn).add(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen mehrere neuer Aufsichtsbereiche zu der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten der Aufsichtsbereiche
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den neuen Aufsichtsbereichen
	 */
	@POST
	@Path("/aufsichtsbereiche/create/multiple")
	@Operation(summary = "Erstellt neue Aufsichtsbereiche für die Schule und gibt die zugehörigen Objekte zurück.",
			description = "Erstellt neue Aufsichtsbereiche für die Schule und gibt die zugehörigen Objekte zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Aufsichtsbereiche wurden erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Aufsichtsbereich.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Aufsichtsbereiche für die Schule anzulegen.")
	@ApiResponse(responseCode = "404", description = "Die Katalogdaten wurden nicht gefunden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addAufsichtsbereiche(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden Aufsichtsbereiche ohne ID, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Aufsichtsbereich.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogAufsichtsbereiche(conn).addMultiple(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen eines Aufsichtsbereichs der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param id           die ID des Aufsichtsbereichs
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Aufsichtsbereich
	 */
	@DELETE
	@Path("/aufsichtsbereiche/{id : \\d+}")
	@Operation(summary = "Entfernt einen Aufsichtsbereich der Schule.",
			description = "Entfernt einen Aufsichtsbereich der Schule."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Der Aufsichtsbereich wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Aufsichtsbereich.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Kein Aufsichtsbereich vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteAufsichtsbereich(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogAufsichtsbereiche(conn).delete(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Aufsichtsbereiche der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           die IDs der Aufsichtsbereiche
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. den gelöschten Aufsichtsbereichen
	 */
	@DELETE
	@Path("/aufsichtsbereiche/delete/multiple")
	@Operation(summary = "Entfernt mehrere Aufsichtsbereiche der Schule.",
			description = "Entfernt mehrere Aufsichtsbereiche der Schule."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Die Aufsichtsbereiche wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Aufsichtsbereich.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Aufsichtsbereich nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteAufsichtsbereiche(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Aufsichtsbereiche", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogAufsichtsbereiche(conn).deleteMultiple(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Kataloges der Pausenzeiten der Schule.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den Einträgen des Pausenzeiten-Kataloges
	 */
	@GET
	@Path("/pausenzeiten")
	@Operation(summary = "Gibt den Katalog der Pausenzeiten der Schule zurück.",
			description = "Gibt den Katalog der Pausenzeiten der Schule zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Katalog der Pausenzeiten der Schule.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StundenplanPausenzeit.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
	@ApiResponse(responseCode = "404", description = "Keine Pausenzeit-Einträge gefunden.")
	public Response getPausenzeiten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogPausenzeiten(conn).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}



	/**
	 * Die OpenAPI-Methode für die Abfrage einer Pausenzeit der Schule.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id            die ID der Pausenzeit
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die Pausenzeit der Schule
	 */
	@GET
	@Path("/pausenzeiten/{id : \\d+}")
	@Operation(summary = "Gibt die Pausenzeit der Schule zurück.",
			description = "Gibt die Pausenzeit der Schule zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Pausenzeit der Schule",
			content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = StundenplanPausenzeit.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Pausenzeit mit der angegebenen ID bei der Schule gefunden")
	public Response getPausenzeit(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogPausenzeiten(conn).get(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen einer Pausenzeit der Schule.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Pausenzeit
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/pausenzeiten/{id : \\d+}")
	@Operation(summary = "Passt die Pausenzeit der Schule mit der angebenen ID an.",
			description = "Passt die Pausenzeit der Schule mit der angebenen ID an. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Katalog-Daten "
					+ "besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchPausenzeit(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Pausenzeit der Schule", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = StundenplanPausenzeit.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogPausenzeiten(conn).patch(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen einer neuen Pausenzeit zu der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten der Pausenzeit
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der neuen Pausenzeit
	 */
	@POST
	@Path("/pausenzeiten/create")
	@Operation(summary = "Erstellt eine neue Pausenzeit für die Schule und gibt das zugehörige Objekt zurück.",
			description = "Erstellt eine neue Pausenzeit für die Schule und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Pausenzeit wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StundenplanPausenzeit.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Pausenzeit für die Schule anzulegen.")
	@ApiResponse(responseCode = "404", description = "Die Katalogdaten wurden nicht gefunden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addPausenzeit(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden Pausenzeit ohne ID, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = StundenplanPausenzeit.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogPausenzeiten(conn).add(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen mehrerer neuer Pausenzeiten zu der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten der Pausenzeiten
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den neuen Pausenzeiten
	 */
	@POST
	@Path("/pausenzeiten/create/multiple")
	@Operation(summary = "Erstellt neue Pausenzeiten für die Schule und gibt die zugehörigen Objekt zurück.",
			description = "Erstellt neue Pausenzeiten für die Schule und gibt die zugehörigen Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Pausenzeiten wurden erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = StundenplanPausenzeit.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Pausenzeiten für die Schule anzulegen.")
	@ApiResponse(responseCode = "404", description = "Die Katalogdaten wurden nicht gefunden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addPausenzeiten(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden Pausenzeiten ohne IDs, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = StundenplanPausenzeit.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogPausenzeiten(conn).addMultiple(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen einer Pausenzeit der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param id           die ID der Pausenzeit
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. der gelöschten Pausenzeit
	 */
	@DELETE
	@Path("/pausenzeiten/{id : \\d+}")
	@Operation(summary = "Entfernt eine Pausenzeit der Schule.",
			description = "Entfernt eine Pausenzeit der Schule."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Die Pausenzeit wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = StundenplanPausenzeit.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Keine Pausenzeit vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deletePausenzeit(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogPausenzeiten(conn).delete(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Pausenzeiten der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           die IDs der Pausenzeiten
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. den gelöschten Pausenzeiten
	 */
	@DELETE
	@Path("/pausenzeiten/delete/multiple")
	@Operation(summary = "Entfernt mehrere Pausenzeiten der Schule.",
			description = "Entfernt mehrere Pausenzeiten der Schule."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Die Pausenzeiten wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StundenplanPausenzeit.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Räume nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deletePausenzeiten(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Pausenzeiten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogPausenzeiten(conn).deleteMultiple(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des Zeitraster-Kataloges der Schule.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den Einträgen des Zeitraster-Kataloges
	 */
	@GET
	@Path("/zeitraster")
	@Operation(summary = "Gibt den Zeitraster-Katalog der Schule zurück.",
			description = "Gibt den Zeitraster-Katalog der Schule zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Zeitraster-Katalog der Schule.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StundenplanZeitraster.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
	@ApiResponse(responseCode = "404", description = "Keine Zeitraster-Einträge gefunden.")
	public Response getZeitraster(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogZeitraster(conn).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}



	/**
	 * Die OpenAPI-Methode für die Abfrage eines Zeitraster-Eintrags der Schule.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id            die ID des Zeitraster-Eintrags
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return der Zeitraster-Eintrags der Schule
	 */
	@GET
	@Path("/zeitraster/{id : \\d+}")
	@Operation(summary = "Gibt den Zeitraster-Eintrags der Schule zurück.",
			description = "Gibt den Zeitraster-Eintrag der Schule zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Zeitraster-Eintrag der Schule",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = StundenplanZeitraster.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Zeitraster-Eintrag bei der Schule gefunden")
	public Response getZeitrasterEintrag(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogZeitraster(conn).get(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen eines Zeitraster-Eintrags der Schule.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Zeitraster-Eintrags
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/zeitraster/{id : \\d+}")
	@Operation(summary = "Passt den Zeitraster-Eintrag der Schule mit der angebenen ID an.",
			description = "Passt den Zeitraster-Eintrag der Schule mit der angebenen ID an. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Katalog-Daten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchZeitrasterEintrag(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für den Zeitraster-Eintrag der Schule", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = StundenplanZeitraster.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogZeitraster(conn).patch(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen mehrerer Zeitraster-Einträge. Die IDs in dem Patch
	 * müssen vorhanden sein, damit die zu patchenden Daten in der DB gefunden werden können.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/zeitraster/patch/multiple")
	@Operation(summary = "Passt die Zeitrastereinträge an.",
			description = "Passt die Zeitrastereinträge an. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag für mindestens eine der IDs der Daten gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchZeitrasterEintraege(@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für den Zeitrastereintrag", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = StundenplanZeitraster.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogZeitraster(conn).patchMultiple(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen eines neuen Zeitraster-Eintrags zu der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten des Zeitraster-Eintrags
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem neuen Zeitraster-Eintrag
	 */
	@POST
	@Path("/zeitraster/create")
	@Operation(summary = "Erstellt einen neue Zeitraster-Eintrag für die Schule und gibt das zugehörige Objekt zurück.",
			description = "Erstellt einen neue Zeitraster-Eintrag für die Schule und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs besitzt.")
	@ApiResponse(responseCode = "201", description = "Der Zeitraster-Eintrag wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StundenplanZeitraster.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Zeitraster-Eintrag für die Schule anzulegen.")
	@ApiResponse(responseCode = "404", description = "Die Katalogdaten wurden nicht gefunden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addZeitrasterEintrag(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten des zu erstellenden Zeitraster-Eintrags ohne ID, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = StundenplanZeitraster.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogZeitraster(conn).add(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen mehrerer neuer Zeitraster-Einträge zu der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten der Zeitraster-Einträge
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit den neuen Zeitraster-Einträgen
	 */
	@POST
	@Path("/zeitraster/create/multiple")
	@Operation(summary = "Erstellt neue Zeitraster-Einträge für die Schule und gibt die zugehörigen Objekt zurück.",
			description = "Erstellt neue Zeitraster-Einträge für die Schule und gibt die zugehörigen Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Pausenzeiten wurden erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = StundenplanZeitraster.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Zeitraster-Einträge für die Schule anzulegen.")
	@ApiResponse(responseCode = "404", description = "Die Katalogdaten wurden nicht gefunden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addZeitrasterEintraege(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden Zeitraster-Einträge ohne IDs, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = StundenplanZeitraster.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogZeitraster(conn).addMultiple(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen eines Zeitraster-Eintrags der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param id           die ID des Zeitraster-Eintrags
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. des gelöschten Zeitraster-Eintrags
	 */
	@DELETE
	@Path("/zeitraster/{id : \\d+}")
	@Operation(summary = "Entfernt einen Zeitraster-Eintrag der Schule.",
			description = "Entfernt einen Zeitraster-Eintrag der Schule."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Der Zeitraster-Eintrag wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = StundenplanZeitraster.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Kein Zeitraster-Eintrag vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteZeitrasterEintrag(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogZeitraster(conn).delete(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Zeitraster-Einträge der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           die IDs der Zeitraster-Einträge
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. den gelöschten Zeitraster-Einträgen
	 */
	@DELETE
	@Path("/zeitraster/delete/multiple")
	@Operation(summary = "Entfernt mehrere Zeitraster-Einträge der Schule.",
			description = "Entfernt mehrere Zeitraster-Einträge der Schule."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Die Zeitraster-Einträge wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StundenplanZeitraster.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Zeitraster-Einträge nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteZeitrasterEintraege(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Zeitraster-Einträge", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogZeitraster(conn).deleteMultiple(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des schulspezifischen Kataloges für die Schulen.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return          die Liste mit dem Katalog der Schulen
	 */
	@GET
	@Path("/schulen")
	@Operation(summary = "Gibt eine Übersicht aller Schulen im schul-spezifischen Katalog zurück.",
			description = "Erstellt eine Liste aller in dem schul-spezifischen Katalog vorhanden Schulen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchulEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getSchulen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchulen(conn).getAllAsResponse(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des schulspezifischen Kataloges für die Schulen,
	 * welche ein Kürzel gesetzt haben.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return          die Liste mit dem Katalog der Schulen, welche ein Kürzel gesetzt haben
	 */
	@GET
	@Path("/schulen/kuerzel")
	@Operation(summary = "Gibt eine Übersicht aller Schulen im schul-spezifischen Katalog zurück, welche ein Kürzel gesetzt haben.",
			description = "Erstellt eine Liste aller in dem schul-spezifischen Katalog vorhanden Schulen, welche ein Kürzel gesetzt haben. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchulEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getSchulenMitKuerzel(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchulen(conn).getListAsResponse(), request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage eines Eintrags des schulspezifischen Kataloges
	 * für die Schulen.
	 *
	 * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id            die ID des Eintrags
	 * @param request       die Informationen zur HTTP-Anfrage
	 *
	 * @return die Pausenzeit der Schule
	 */
	@GET
	@Path("/schulen/{id : \\d+}")
	@Operation(summary = "Gibt den Eintrag im schulspezifischen Katalog der Schulen zurück.",
			description = "Gibt den Eintrag im schulspezifischen Katalog der Schulen zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Eintrag im schulspezifischen Katalog der Schulen",
			content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = SchulEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	public Response getSchuleAusKatalog(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchulen(conn).getByIdAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen eines Eintrags des schulspezifischen Kataloges.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Eintrags
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/schulen/{id : \\d+}")
	@Operation(summary = "Passt den Eintrag des schulspezifischen Kataloges der Schulen mit der angebenen ID an.",
			description = "Passt den Eintrag des schulspezifischen Kataloges der Schulen mit der angebenen ID an. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Katalog-Daten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchSchuleAusKatalog(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für den Eintrag", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchulEintrag.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchulen(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}



	/**
	 * Die OpenAPI-Methode für das Hinzufügen eines Eintrags zum schulspezifischen Katalog der Schulen.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten des Eintrags
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem neuen Eintrag
	 */
	@POST
	@Path("/schulen/create")
	@Operation(summary = "Erstellt einen neuen Eintrag für den schulspezifischen Katalog der Schulen und gibt das zugehörige Objekt zurück.",
			description = "Erstellt einen neuen Eintrag für den schulspezifischen Katalog der Schulen und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs besitzt.")
	@ApiResponse(responseCode = "201", description = "Der Eintrag wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchulEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Eintrag für die Schule anzulegen.")
	@ApiResponse(responseCode = "404", description = "Die Katalogdaten wurden nicht gefunden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addSchuleZuKatalog(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten des zu erstellenden Eintrags ohne ID, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchulEintrag.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchulen(conn).addAsResponse(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen eines Eintrags aus dem schulspezifischen Katalog der Schulen.
	 *
	 * @param schema       das Datenbankschema
	 * @param id           die ID des Eintrags
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Eintrag
	 */
	@DELETE
	@Path("/schulen/{id : \\d+}")
	@Operation(summary = "Entfernt einen Eintrag aus dem schulspezifischen Katalog der Schulen.",
			description = "aus dem schulspezifischen Katalog der Schulen."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Der Eintrag wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchulEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteSchuleVonKatalog(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchulen(conn).deleteAsResponse(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Einträge aus dem schulspezifischen Katalog der Schulen.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           die IDs der Einträge
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. den gelöschten Einträgen
	 */
	@DELETE
	@Path("/schulen/delete/multiple")
	@Operation(summary = "Entfernt mehrere Einträge aus dem schulspezifischen Katalog der Schulen.",
			description = "Entfernt mehrere Einträge aus dem schulspezifischen Katalog der Schulen."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Die Einträge wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchulEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Räume nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteSchulenVonKatalog(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Einträge", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchulen(conn).deleteMultipleAsResponse(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage aller Leitungsfunktionen der Schule.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Leitungsfunktionen der Schule
	 */
	@GET
	@Path("/leitungsfunktion/alle")
	@Operation(summary = "Gibt die Leitungsfunktionen der Schule zurück.",
			description = "Gibt die Leitungsfunktionen der Schule zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Leitungsfunktionen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Schulleitung.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Leitungsfunktionen der Schule anzusehen.")
	public Response getSchulleitungsfunktionen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchulleitung(conn, null).getList(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage einer Leitungsfunktion der Schule.
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id         die ID der Leitungsfunktion
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return die Leitungsfunktion der Schule
	 */
	@GET
	@Path("/leitungsfunktion/{id : \\d+}")
	@Operation(summary = "Gibt die Leitungsfunktion der Schule zurück.",
			description = "Gibt die Leitungsfunktion der Schule zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Leitungsfunktion",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Schulleitung.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Leitungsfunktion der Schule anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Leitungsfunktion der Schule gefunden")
	public Response getSchulleitungsfunktion(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchulleitung(conn, null).get(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen einer Leitungsfunktion der Schule.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Leitungsfunktion der Schule
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/leitungsfunktion/{id : \\d+}")
	@Operation(summary = "Passt die Leitungsfunktion der Schule mit der angebenen ID an.",
			description = "Passt die Leitungsfunktion der Schule mit der angebenen ID an. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchSchulleitungsfunktion(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Leitungsfunktion der Schule", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Schulleitung.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchulleitung(conn, null).patch(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen einer Leitungsfunktion zu der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten der Leitungsfunktion der Schule
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der neuen Leitungsfunktion der Schule
	 */
	@POST
	@Path("/leitungsfunktion/create")
	@Operation(summary = "Erstellt einen neue Leitungsfunktion der Schule und gibt das zugehörige Objekt zurück.",
			description = "Erstellt einen neue Leitungsfunktion der Schule und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Hinzufügen besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Leitungsfunktion der Schule wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Schulleitung.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Leitungsfunktion für die Schule anzulegen.")
	@ApiResponse(responseCode = "404", description = "Der Lehrer wurde nichtgefunden.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addSchulleitungsfunktion(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden Leitungsfunktion der Schule ohne ID, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Schulleitung.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchulleitung(conn, null).add(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen mehrerer Leitungsfunktionen zu der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           der Input-Stream mit den Daten der Leitungsfunktionen
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der Liste der neuen Leitungsfunktionen
	 */
	@POST
	@Path("/leitungsfunktion/create/multiple")
	@Operation(summary = "Erstellt mehrere neue Leitungsfunktion für die Schule und gibt das zugehörige Objekt zurück.",
			description = "Erstellt mehrere neue Leitungsfunktion für die Schule und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Hinzufügen besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Leitungsfunktionen wurden erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Schulleitung.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Leitungsfunktion für die Schule anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addSchulleitungsfunktionen(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden Leitungsfunktionen ohne IDs, welche automatisch generiert werden", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Schulleitung.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchulleitung(conn, null).addMultiple(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen einer Leitungsfunktion der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param id           die ID der Leitungsfunktion der Schule
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. der gelöschten Leitungsfunktion der Schule
	 */
	@DELETE
	@Path("/leitungsfunktion/{id : \\d+}")
	@Operation(summary = "Entfernt eine Leitungsfunktion der Schule.",
			description = "Entfernt eine Leitungsfunktion der Schule."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen hat.")
	@ApiResponse(responseCode = "200", description = "Die Leitungsfunktion der Schule wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Schulleitung.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Leitungsfunktion der Schule zu löschen.")
	@ApiResponse(responseCode = "404", description = "Die Leitungsfunktion der Schule ist nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteSchulleitungsfunktion(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchulleitung(conn, null).delete(id),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Leitungsfunktionen der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           die IDs der Leitungsfunktionen
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. den gelöschten Leitungsfunktionen der Schule
	 */
	@DELETE
	@Path("/leitungsfunktion/delete/multiple")
	@Operation(summary = "Entfernt mehrere Leitungsfunktionen der Schule.",
			description = "Entfernt mehrere Leitungsfunktionen der Schule."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen hat.")
	@ApiResponse(responseCode = "200", description = "Die Leitungsfunktionen der Schule wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Schulleitung.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Leitungsfunktion der Schule zu löschen.")
	@ApiResponse(responseCode = "404", description = "Mindestens eine Leitungsfunktion der Schule ist nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteSchulleitungsfunktionen(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Leitungsfunktionen der Schule", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchulleitung(conn, null).deleteMultiple(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE,
				BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage des schulspezifischen Kataloges für die Lernplattformen.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return          die Liste mit dem Katalog der Lernplattformen
	 */
	@GET
	@Path("/lernplattformen")
	@Operation(summary = "Gibt eine Übersicht aller Lernplattformen im Katalog zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Lernplattformen unter Angabe der ID und der Bezeichnung. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Lernplattform.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getLernplattformen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogLernplattformen(conn).getAllAsResponse(),
				request, ServerMode.DEV, BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage einer Lernplattform.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Lernplattform
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Daten zur Lernplattform
	 */
	@GET
	@Path("/lernplattform/{id : \\d+}")
	@Operation(summary = "Liefert zu der ID der Lernplattform die zugehörigen Daten.",
			description = "Liest die Daten der Lernplattform zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten der Lernplattform",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Lernplattform.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Lernplattform mit der angegebenen ID gefunden")
	public Response getLernplattform(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogLernplattformen(conn).getByIdAsResponse(id),
				request, ServerMode.DEV,
				BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen einer neuen Lernplattform.
	 *
	 * @param schema       das Datenbankschema, in welchem die Einwilligungsart erstellt wird
	 * @param request      die Informationen zur HTTP-Anfrage
	 * @param is           das JSON-Objekt
	 *
	 * @return die HTTP-Antwort mit der neuen Lernplattform
	 */
	@POST
	@Path("/lernplattform/new")
	@Operation(summary = "Erstellt eine neue Lernplattform und gibt sie zurück.",
			description = "Erstellt eine neue Lernplattform und gibt sie zurück."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Lernplattform besitzt.")
	@ApiResponse(responseCode = "201", description = "Lernplattform wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Lernplattform.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Lernplattform anzulegen.")
	@ApiResponse(responseCode = "409", description = "Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addLernplattform(@PathParam("schema") final String schema,
			@RequestBody(description = "Der initiale Patch für die neue Lernplattform", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Lernplattform.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogLernplattformen(conn).addAsResponse(is),
				request, ServerMode.DEV,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen einer Lernplattform im angegebenen Schema
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Lernplattform
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/lernplattform/{id : \\d+}")
	@Operation(summary = "Passt die zu der ID der Lernplattform zugehörigen Stammdaten an.",
			description = "Passt die Lernplattform-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Lernplattform besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Lernplattform-Daten integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lernplattform-Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine Lernplattform mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchLernplattform(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Lernplattform", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = Lernplattform.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogLernplattformen(conn).patchAsResponse(id, is),
				request, ServerMode.DEV,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen einer Lernplattform der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param id           die ID der Lernplattform
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. der gelöschten Lernplattform
	 */
	@DELETE
	@Path("/lernplattform/{id : \\d+}")
	@Operation(summary = "Entfernt eine Lernplattform der Schule.",
			description = "Entfernt eine Lernplattform der Schule."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Die Lernplattform wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Lernplattform.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Lernplattform nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteLernplattform(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogLernplattformen(conn).deleteAsResponse(id),
				request, ServerMode.DEV,
				BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Lernplattformen der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           die IDs der Lernplattformen
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Lösch-Operationen
	 */
	@DELETE
	@Path("/lernplattformen/delete/multiple")
	@Operation(summary = "Entfernt mehrere Lernplattformen der Schule.",
			description = "Entfernt mehrere Lernplattformen der Schule."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Die Lernplattformen wurden erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Lernplattformen nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteLernplattformen(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Lernplattformen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogLernplattformen(conn).deleteMultipleAsSimpleResponseList(JSONMapper.toListOfLong(is)),
				request, ServerMode.DEV,
				BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage des schulspezifischen Kataloges für die Telefonarten.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return          die Liste mit dem Katalog der Telefonarten
	 */
	@GET
	@Path("/Telefonarten")
	@Operation(summary = "Gibt eine Übersicht aller Telefonarten im Katalog zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Telefonarten unter Angabe der ID und der Bezeichnung. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TelefonArt.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getTelefonarten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogTelefonArten(conn).getAllAsResponse(),
				request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage einer Telefonart.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Telefonart
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Daten zur Telefonart
	 */
	@GET
	@Path("/telefonart/{id : \\d+}")
	@Operation(summary = "Liefert zu der ID der Telefonart die zugehörigen Daten.",
			description = "Liest die Daten der Telefonart zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten der Telefonart",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = TelefonArt.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Telefonart mit der angegebenen ID gefunden")
	public Response getTelefonart(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogTelefonArten(conn).getByIdAsResponse(id),
				request, ServerMode.DEV,
				BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen einer neuen Telefonart.
	 *
	 * @param schema       das Datenbankschema, in welchem die Telefonart erstellt wird
	 * @param request      die Informationen zur HTTP-Anfrage
	 * @param is           das JSON-Objekt
	 *
	 * @return die HTTP-Antwort mit der neuen Telefonart
	 */
	@POST
	@Path("/telefonart/new")
	@Operation(summary = "Erstellt eine neue Telefonart und gibt sie zurück.",
			description = "Erstellt eine neue Telefonart und gibt sie zurück."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Telefonart besitzt.")
	@ApiResponse(responseCode = "201", description = "Telefonart wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = TelefonArt.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Telefonart anzulegen.")
	@ApiResponse(responseCode = "409", description = "Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addTelefonart(@PathParam("schema") final String schema,
			@RequestBody(description = "Der initiale Patch für die neue Telefonart", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = TelefonArt.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogTelefonArten(conn).addAsResponse(is),
				request, ServerMode.DEV,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen einer Telefonart im angegebenen Schema
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Telefonart
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/Telefonart/{id : \\d+}")
	@Operation(summary = "Passt die zu der ID der Telefonart zugehörigen Stammdaten an.",
			description = "Passt die Telefonart-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Telefonart besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Telefonart-Daten integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Telefonart-Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine Telefonart mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchTelefonart(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Telefonart", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = TelefonArt.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogTelefonArten(conn).patchAsResponse(id, is),
				request, ServerMode.DEV,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Telefonarten der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           die IDs der Telefonarten
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Lösch-Operationen
	 */
	@DELETE
	@Path("/telefonarten/delete/multiple")
	@Operation(summary = "Entfernt mehrere Telefonarten der Schule.",
			description = "Entfernt mehrere Telefonarten der Schule."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Die Telefonarten wurden erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Telefonarten nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteTelefonarten(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Telefonarten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogTelefonArten(conn).deleteMultipleAsSimpleResponseList(JSONMapper.toListOfLong(is)),
				request, ServerMode.DEV, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste aller Erzieherarten.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den einzelnen Erzieherarten
	 */
	@GET
	@Path("/erzieherarten")
	@Operation(summary = "Gibt eine Übersicht von allen Erzieherarten zurück.",
			description = "Erstellt eine Liste aller in der Datenbank vorhandenen Erzieherarten unter Angabe der ID, der Beschreibung, "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Erzieherarten",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Erzieherart.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Erzieherart-Einträge gefunden")
	public Response getErzieherArten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataErzieherarten(conn).getAllAsResponse(),
				request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage einer Erzieherart.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Erzieherart
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Daten zur Erzieherart
	 */
	@GET
	@Path("/erzieherart/{id : \\d+}")
	@Operation(summary = "Liefert zu der ID der Erzieherart die zugehörigen Daten.",
			description = "Liest die Daten der Erzieherart zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten der Erzieherart",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Erzieherart.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Erzieherart mit der angegebenen ID gefunden")
	public Response getErzieherart(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataErzieherarten(conn).getByIdAsResponse(id),
				request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen einer neuen Erzieherart.
	 *
	 * @param schema       das Datenbankschema, in welchem die Erzieherart erstellt wird
	 * @param request      die Informationen zur HTTP-Anfrage
	 * @param is           das JSON-Objekt
	 *
	 * @return die HTTP-Antwort mit der neuen Erzieherart
	 */
	@POST
	@Path("/erzieherart/new")
	@Operation(summary = "Erstellt eine neue Erzieherart und gibt sie zurück.",
			description = "Erstellt eine neue Erzieherart und gibt sie zurück."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Erzieherart besitzt.")
	@ApiResponse(responseCode = "201", description = "Telefonart wurde erfolgreich angelegt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Erzieherart.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Erzieherart anzulegen.")
	@ApiResponse(responseCode = "409", description = "Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addErzieherart(@PathParam("schema") final String schema,
			@RequestBody(description = "Der initiale Patch für die neue Erzieherart", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Erzieherart.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataErzieherarten(conn).addAsResponse(is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen einer Erzieherart im angegebenen Schema
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Erzieherart
	 * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/erzieherart/{id : \\d+}")
	@Operation(summary = "Passt die zu der ID der Erzieherart zugehörigen Stammdaten an.",
			description = "Passt die Erzieherart-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Erzieherart besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Erzieherart-Daten integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Erzieherart-Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine Erzieherart mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchErzieherart(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für die Erzieherart", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = Erzieherart.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataErzieherarten(conn).patchAsResponse(id, is),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Erzieherarten der Schule.
	 *
	 * @param schema       das Datenbankschema
	 * @param is           die IDs der Erzieherarten
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status der Lösch-Operationen
	 */
	@DELETE
	@Path("/erzieherarten/delete/multiple")
	@Operation(summary = "Entfernt mehrere Erzieherarten der Schule.",
			description = "Entfernt mehrere Erzieherarten der Schule."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.")
	@ApiResponse(responseCode = "200", description = "Die Erzieherarten wurden erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.")
	@ApiResponse(responseCode = "404", description = "Erzieherarten nicht vorhanden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteErzieherarten(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Erzieherarten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final InputStream is, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataErzieherarten(conn).deleteMultipleAsSimpleResponseList(JSONMapper.toListOfLong(is)),
				request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	}
}
