package de.svws_nrw.api.server;

import java.io.InputStream;
import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerAbgangsgrundKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerAnrechnungsgrundKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerBeschaeftigungsartKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerEinsatzstatusKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungAnerkennungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerFunktion;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtAnerkennungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungAnerkennungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLeitungsfunktionKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerMehrleistungsartKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerMinderleistungsartKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.asd.data.lehrer.LehrerRechtsverhaeltnisKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.data.lehrer.LehrerZugangsgrundKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schulleitung;
import de.svws_nrw.controller.lehrer.LehrerControllerFactory;
import de.svws_nrw.controller.schule.schulleitung.SchulleitungControllerFactory;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.lehrer.LehrerEinwilligung;
import de.svws_nrw.core.data.lehrer.LehrerLernplattform;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.lehrer.LehrerUnterrichtsfach;
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
import de.svws_nrw.data.lehrer.DataLehrerPersonaldaten;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.data.lehrer.DataLehrerliste;
import de.svws_nrw.data.schule.DataEinwilligungsarten;
import de.svws_nrw.data.schule.DataLernplattformen;
import de.svws_nrw.service.lehrer.LehrerServiceFactory;
import de.svws_nrw.service.lehrer.anrechnung.LehrerAnrechnungsstundeCreateRequest;
import de.svws_nrw.service.lehrer.anrechnung.LehrerAnrechnungsstundePatchRequest;
import de.svws_nrw.service.lehrer.fachrichtung.LehrerFachrichtungCreateRequest;
import de.svws_nrw.service.lehrer.fachrichtung.LehrerFachrichtungPatchRequest;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionBatchPatchRequest;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionCreateRequest;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionPatchRequest;
import de.svws_nrw.service.lehrer.lehrbefaehigung.LehrerLehrbefaehigungCreateRequest;
import de.svws_nrw.service.lehrer.lehrbefaehigung.LehrerLehrbefaehigungPatchRequest;
import de.svws_nrw.service.lehrer.mehrleistung.LehrerMehrleistungCreateRequest;
import de.svws_nrw.service.lehrer.mehrleistung.LehrerMehrleistungPatchRequest;
import de.svws_nrw.service.lehrer.minderleistung.LehrerMinderleistungBatchPatchRequest;
import de.svws_nrw.service.lehrer.minderleistung.LehrerMinderleistungCreateRequest;
import de.svws_nrw.service.lehrer.minderleistung.LehrerMinderleistungPatchRequest;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenBatchPatchRequest;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenCreateRequest;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenPatchRequest;
import de.svws_nrw.service.lehrer.unterrichtsfach.LehrerUnterrichtsfachCreateRequest;
import de.svws_nrw.service.lehrer.unterrichtsfach.LehrerUnterrichtsfachPatchRequest;
import de.svws_nrw.service.schule.schulleitung.SchulleitungCreateRequest;
import de.svws_nrw.service.schule.schulleitung.SchulleitungPatchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
import org.jboss.resteasy.annotations.GZIP;


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
	@GZIP
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
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerliste(conn, null).getAllAsResponse(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.KEINE);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Liste aller Lehrer eines Schuljahresabschnittes.
	 *
	 * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param abschnitt   die ID des Schuljahresabschnitts
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste mit den Lehrern eines Schuljahresabschnittes
	 */
	@GET
	@GZIP
	@Path("/abschnitt/{abschnitt : \\d+}")
	@Operation(summary = "Gibt eine Übersicht von allen Lehrern eines Schuljahresabschnittes zurück.",
			description = "Erstellt eine Liste aller in der Datenbank vorhanden Lehrer eines Schuljahresabschnittes unter Angabe der ID, des Kürzels, "
					+ "des Vor- und Nachnamens, der sog. Personentyps, einer Sortierreihenfolge, ob sie in der Anwendung "
					+ "sichtbar bzw. änderbar sein sollen sowie ob sie für die Schulstatistik relevant sein sollen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Lehrer-Listen-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerListeEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Lehrer-Einträge gefunden")
	public Response getLehrerFuerAbschnitt(@PathParam("schema") final String schema, @PathParam("abschnitt") final long abschnitt,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataLehrerliste(conn, abschnitt).getListAsResponse(),
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
				conn -> new DataLehrerliste(conn, null).deleteMultipleAsSimpleResponseList(JSONMapper.toListOfLong(is)),
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
	@GZIP
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
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataLehrerStammdaten(conn, new DataLernplattformen(conn), new DataEinwilligungsarten(conn)).getByIdAsResponse(id),
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
	@GZIP
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
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataLehrerStammdaten(conn, new DataLernplattformen(conn), new DataEinwilligungsarten(conn))
						.getListByIdsAsResponse(JSONMapper.toListOfLong(is)),
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
				conn -> new DataLehrerStammdaten(conn, new DataLernplattformen(conn), new DataEinwilligungsarten(conn)).addAsResponse(is), request,
				ServerMode.STABLE, BenutzerKompetenz.LEHRERDATEN_AENDERN);
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
		return DBBenutzerUtils.runWithTransaction(
				conn -> new DataLehrerStammdaten(conn, new DataLernplattformen(conn), new DataEinwilligungsarten(conn)).patchAsResponse(id, is),
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
	 * Die OpenAPI-Methode für die Abfrage der Daten zu einem Lehramt eines Lehrers.
	 *
	 * @param schema      das Datenbankschema
	 * @param idLehramt   die ID des Lehramteintrags
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return die Daten zu dem Lehramt
	 */
	@GET
	@Path("/personaldaten/lehramt/{idLehramt : \\d+}")
	@Operation(summary = "Liefert zu der ID des Lehramtes eines Lehrers die zugehörigen Daten.",
			description = "Liest die Daten des Lehramtes eines Lehrers zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerpersonaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Daten zu dem Lehramt",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = LehrerLehramtEintrag.class)))
	@ApiResponse(responseCode = "400", description = "Die Anfrage ist fehlerhaft.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Lehramts-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getLehrerLehramt(@PathParam("schema") final String schema, @PathParam("idLehramt") final long idLehramt,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> {
					final var lehrerServiceFactory = LehrerServiceFactory.getNewInstance();
					final var lehrerFachrichtungService = lehrerServiceFactory.getLehrerFachrichtungService();
					final var lehrerLehrbefaehigungService = lehrerServiceFactory.getLehrerLehrbefaehigungService();
					return new DataLehrerLehramt(conn, null, lehrerFachrichtungService, lehrerLehrbefaehigungService).getByIdAsResponse(idLehramt);
				},
				request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Lehrämter eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Lehrers
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Lehrämter des Lehrers
	 */
	@GET
	@Path("/{id : \\d+}/personaldaten/lehraemter")
	@Operation(summary = "Liefert zu der ID des Lehrer die zugehörigen Daten zu den Lehrämtern.",
			description = "Liest zugehörigen Daten zu den Lehrämtern des Lehrers mit der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerpersonaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die zugehörigen Daten zu den Lehrämtern des Lehrers",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerLehramtEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerpersonaldaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Lehrer-Eintrag mit der angegebenen ID gefunden")
	public Response getLehrerLehraemter(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> {
					final var lehrerServiceFactory = LehrerServiceFactory.getNewInstance();
					final var lehrerFachrichtungService = lehrerServiceFactory.getLehrerFachrichtungService();
					final var lehrerLehrbefaehigungService = lehrerServiceFactory.getLehrerLehrbefaehigungService();
					return new DataLehrerLehramt(conn, id, lehrerFachrichtungService, lehrerLehrbefaehigungService).getListAsResponse();
				},
				request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen von Lehramtsdaten in den Personaldaten eines Lehrers.
	 *
	 * @param schema      das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param idLehramt   die ID des Lehramteintrags beim Lehrer
	 * @param is          der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/personaldaten/lehramt/{idLehramt : \\d+}")
	@Operation(summary = "Führt einen Patch auf einem Lehramtseintrag in den Personaldaten des Lehrers durch.",
			description = "Passt den Lehramtseintrag zu den angegebenen IDs an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Lehramts-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchLehrerLehramt(@PathParam("schema") final String schema, @PathParam("idLehramt") final long idLehramt,
			@RequestBody(description = "Der Patch für den Lehramtseintrag", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = LehrerLehramtEintrag.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> {
					final var lehrerServiceFactory = LehrerServiceFactory.getNewInstance();
					final var lehrerFachrichtungService = lehrerServiceFactory.getLehrerFachrichtungService();
					final var lehrerLehrbefaehigungService = lehrerServiceFactory.getLehrerLehrbefaehigungService();
					return new DataLehrerLehramt(conn, null, lehrerFachrichtungService, lehrerLehrbefaehigungService).patchAsResponse(idLehramt, is);
				},
				request, ServerMode.STABLE,
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
			@RequestBody(description = "Die Daten des Lehramtes", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = LehrerLehramtEintrag.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> {
					final var lehrerServiceFactory = LehrerServiceFactory.getNewInstance();
					final var lehrerFachrichtungService = lehrerServiceFactory.getLehrerFachrichtungService();
					final var lehrerLehrbefaehigungService = lehrerServiceFactory.getLehrerLehrbefaehigungService();
					return new DataLehrerLehramt(conn, null, lehrerFachrichtungService, lehrerLehrbefaehigungService).addAsResponse(is);
				},
				request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN
		);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen von Lehramtsdaten aus den Personaldaten eines Lehrers.
	 *
	 * @param schema      das Datenbankschema
	 * @param idLehramt   die ID des Lehramteintrags
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Datensatz
	 */
	@DELETE
	@Path("/personaldaten/lehramt/{idLehramt : \\d+}")
	@Operation(summary = "Entfernt den Lehramtseintrag in den Personaldaten des Lehrers.",
			description = "Entfernt den Lehramtseintrag in den Personaldaten des Lehrers aus der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Datensatz wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = LehrerLehramtEintrag.class)))
	@ApiResponse(responseCode = "400", description = "Die Anfrage ist fehlerhaft.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu entfernen.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit den angegebenen IDs gefunden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteLehrerLehramt(@PathParam("schema") final String schema, @PathParam("idLehramt") final long idLehramt,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> {
					final var lehrerServiceFactory = LehrerServiceFactory.getNewInstance();
					final var lehrerFachrichtungService = lehrerServiceFactory.getLehrerFachrichtungService();
					final var lehrerLehrbefaehigungService = lehrerServiceFactory.getLehrerLehrbefaehigungService();
					return new DataLehrerLehramt(conn, null, lehrerFachrichtungService, lehrerLehrbefaehigungService).deleteAsResponse(idLehramt);
				},
				request, ServerMode.STABLE,
				BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Lehrbefähigungen zu einem Lehramt eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idLehramt        die Datenbank-ID zur Identifikation des Lehramtes
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Lehrbefähigungen zu dem Lehramt eines Lehrers
	 */
	@GET
	@Path("/personaldaten/lehramt/{idLehramt : \\d+}/lehrbefaehigungen")
	@Operation(summary = "Liefert zu der ID des Lehramtes eines Lehrers die zugehörigen Daten zu den Lehrbefähigungen.",
			description = "Liest zugehörigen Daten zu den Lehrbefähigungen des Lehramtes mit der angegebenen ID eines Lehrers aus der Datenbank"
					+ " und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerpersonaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die zugehörigen Daten zu den Lehrbefähigungen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerLehrbefaehigungEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerpersonaldaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Lehramts-Eintrag mit der angegebenen ID gefunden")
	public Response getLehrerLehramtLehrbefaehigungen(@PathParam("schema") final String schema, @PathParam("idLehramt") final long idLehramt,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withReadAccess(request)
				.getLehrerLehrbefaehigungController()
				.getByIdLehramt(idLehramt);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen von Lehrbefähigungsdaten zu einem Lehramt eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die ID des Lehrbefähigungseintrags
	 * @param patch     das Patchobjekt
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/personaldaten/lehramt/lehrbefaehigung/{id : \\d+}")
	@Operation(summary = "Führt einen Patch auf einem Lehrbefähigungseintrags durch.",
			description = "Passt den Lehrbefähigungseintrags zu den angegebenen IDs an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Lehrbefähigungseintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchLehrerLehrbefaehigung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für den Lehrbefähigungseintrag", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = LehrerLehrbefaehigungEintrag.class))) final LehrerLehrbefaehigungPatchRequest patch,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withWriteAccess(request)
				.getLehrerLehrbefaehigungController()
				.patch(id, patch);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen einer Lehrbefähigung zu einem Lehramt bei den Personaldaten eines Lehrers.
	 *
	 * @param schema       das Datenbankschema
	 * @param dto          CreateObjekt
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem neuen Datensaz
	 */
	@POST
	@Path("/personaldaten/lehramt/lehrbefaehigung")
	@Operation(
			summary = "Erstellt einen neuen Datensatz für eine Lehrbefähigung zu einem Lehramt in den Personaldaten eines Lehrers.",
			description = "Erstellt einen neuen Datensatz für eine Lehrbefähigung zu einem Lehramt in den Personaldaten eines Lehrers"
					+ " und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Lehrbefähigung wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = LehrerLehrbefaehigungEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Lehrbefähigung hinzuzufügen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addLehrerLehrbefaehigung(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten zu der Lehrbefähigung", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = LehrerLehrbefaehigungEintrag.class))) final LehrerLehrbefaehigungCreateRequest dto,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withWriteAccess(request)
				.getLehrerLehrbefaehigungController()
				.create(dto);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Lehrbefähigungen.
	 *
	 * @param schema    das Datenbankschema
	 * @param ids       die IDs der Lehrbefähigungseinträge
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Datensatz
	 */
	@DELETE
	@Path("/personaldaten/lehramt/lehrbefaehigung")
	@Operation(summary = "Entfernt mehrere Lehrbefähigungseinträge.",
			description = "Entfernt mehrere Lehrbefähigungseinträge, insofern der SVWS-Benutzer die notwendige Berechtigung zum Ändern von "
					+ "Lehrer-Personaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Lösch-Operationen wurden ausgeführt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "400", description = "Die Anfrage ist fehlerhaft.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu entfernen.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit den angegebenen IDs gefunden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteLehrerLehrbefaehigungen(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der Lehrbefaehigungen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> ids,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withWriteAccess(request)
				.getLehrerLehrbefaehigungController()
				.delete(ids);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage der Fachrichtungen zu einem Lehramt eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param idLehramt die Datenbank-ID zur Identifikation des Lehramtes
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Fachrichtungen zu dem Lehramt eines Lehrers
	 */
	@GET
	@Path("/personaldaten/lehramt/fachrichtungen/{idLehramt : \\d+}")
	@Operation(summary = "Liefert zu der ID des Lehramtes die zugehörigen Daten zu den Fachrichtungen.",
			description = "Liest zugehörigen Daten zu den Fachrichtungen des Lehramtes mit der angegebenen ID eines Lehramtes aus der Datenbank"
					+ " und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerpersonaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die zugehörigen Daten zu den Fachrichtungen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerFachrichtungEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerpersonaldaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Lehramts-Eintrag mit der angegebenen ID gefunden")
	public Response getLehrerLehramtFachrichtungen(@PathParam("schema") final String schema, @PathParam("idLehramt") final long idLehramt,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withReadAccess(request)
				.getLehrerFachrichtungController()
				.getByIdLehramt(idLehramt);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen von Fachrichtungsdaten zu einem Lehramt eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die ID des Fachrichtungseintrags
	 * @param patch     PatchRequest
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/personaldaten/lehramt/fachrichtungen/{id : \\d+}")
	@Operation(summary = "Führt einen Patch auf einem Fachrichtungseintrags durch.",
			description = "Passt den Fachrichtungseintrags zu den angegebenen IDs an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Fachrichtungseintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchLehrerFachrichtung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@RequestBody(description = "Der Patch für den Fachrichtungseintrag", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = LehrerFachrichtungEintrag.class))) final LehrerFachrichtungPatchRequest patch,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withWriteAccess(request)
				.getLehrerFachrichtungController()
				.patch(id, patch);
	}

	/**
	 * Die OpenAPI-Methode für das Hinzufügen einer Fachrichtung zu einem Lehramt bei den Personaldaten eines Lehrers.
	 *
	 * @param schema       das Datenbankschema
	 * @param dto          CreateRequest
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem neuen Datensaz
	 */
	@POST
	@Path("/personaldaten/lehramt/fachrichtungen")
	@Operation(
			summary = "Erstellt einen neuen Datensatz für eine Fachrichtung zu einem Lehramt in den Personaldaten eines Lehrers.",
			description = "Erstellt einen neuen Datensatz für eine Fachrichtung zu einem Lehramt in den Personaldaten eines Lehrers"
					+ " und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Fachrichtung wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = LehrerFachrichtungEintrag.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Fachrichtung hinzuzufügen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addLehrerFachrichtung(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten zu der Fachrichtung", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = LehrerFachrichtungEintrag.class))) final LehrerFachrichtungCreateRequest dto,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withWriteAccess(request)
				.getLehrerFachrichtungController()
				.create(dto);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen einer Fachrichtung zu einem Lehramt eines Lehrers.
	 *
	 * @param schema    das Datenbankschema
	 * @param ids       die IDs des LehrerFachrichtungen
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Datensatz
	 */
	@DELETE
	@Path("/personaldaten/lehramt/fachrichtungen")
	@Operation(summary = "Entfernt den Fachrichtungseintrag eines Lehramtes eines Lehrers.",
			description = "Entfernt den Fachrichtungseintrag eines Lehramtes eines Lehrers aus der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Lösch-Operationen wurden ausgeführt.",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine LehrerFachrichtungen mit der angegebenen ID gefunden.")
	public Response deleteLehrerFachrichtungen(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der LehrerFachrichtungen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> ids,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withWriteAccess(request)
				.getLehrerFachrichtungController()
				.delete(ids);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage einer LehrerPersonalabschnittsdaten anhand ihrer ID.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id      die Datenbank-ID der LehrerPersonalabschnittsdaten
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die LehrerPersonalabschnittsdaten
	 */
	@GET
	@Path("/personalabschnittsdaten/{id : \\d+}")
	@Operation(summary = "Liefert die LehrerPersonalabschnittsdaten mit der angegebenen ID.",
			description = "Liest die LehrerPersonalabschnittsdaten mit der angegebenen ID aus der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die LehrerPersonalabschnittsdaten", content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = LehrerPersonalabschnittsdaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine LehrerPersonalabschnittsdaten mit der angegebenen ID gefunden.")
	public Response getLehrerPersonalabschnittsdaten(@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withReadAccess(request)
				.getLehrerPersonalabschnittsdatenController()
				.get(id);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage mehrerer LehrerPersonalabschnittsdaten anhand ihrer IDs.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param ids     die Datenbank-IDs der LehrerPersonalabschnittsdaten
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der LehrerPersonalabschnittsdaten
	 */
	@POST
	@Path("/personalabschnittsdaten/multiple")
	@Operation(summary = "Liefert die LehrerPersonalabschnittsdaten zu den angegebenen IDs.",
			description = "Liest die LehrerPersonalabschnittsdaten zu den angegebenen IDs aus der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Das Ergebnis der Löschoperation", content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten anzusehen.")
	public Response getLehrerPersonalabschnittsdatenMultiple(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der LehrerPersonalabschnittsdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> ids,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withReadAccess(request)
				.getLehrerPersonalabschnittsdatenController()
				.getList(ids);
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen einer neuen LehrerPersonalabschnittsdaten.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param dto     die Daten der neuen LehrerPersonalabschnittsdaten
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die erstellten LehrerPersonalabschnittsdaten
	 */
	@POST
	@Path("/personalabschnittsdaten")
	@Operation(summary = "Erstellt neue LehrerPersonalabschnittsdaten.",
			description = "Erstellt neue LehrerPersonalabschnittsdaten und speichert diese in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "201", description = "Die erstellten LehrerPersonalabschnittsdaten", content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = LehrerPersonalabschnittsdaten.class)))
	@ApiResponse(responseCode = "400", description = "Die Eingabedaten sind fehlerhaft.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten zu ändern.")
	public Response createLehrerPersonalabschnittsdaten(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der neuen LehrerPersonalabschnittsdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LehrerPersonalabschnittsdaten.class))) final LehrerPersonalabschnittsdatenCreateRequest dto,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withWriteAccess(request)
				.getLehrerPersonalabschnittsdatenController()
				.create(dto);
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen mehrerer neuer LehrerPersonalabschnittsdaten.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param dtos    die Daten der neuen LehrerPersonalabschnittsdaten
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die erstellten LehrerPersonalabschnittsdaten
	 */
	@POST
	@Path("/personalabschnittsdaten/multiple")
	@Operation(summary = "Erstellt mehrere neue LehrerPersonalabschnittsdaten.",
			description = "Erstellt mehrere neue LehrerPersonalabschnittsdaten und speichert diese in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "201", description = "Die erstellten LehrerPersonalabschnittsdaten", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerPersonalabschnittsdaten.class))))
	@ApiResponse(responseCode = "400", description = "Die Eingabedaten sind fehlerhaft.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten zu ändern.")
	public Response createLehrerPersonalabschnittsdatenMultiple(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der neuen LehrerPersonalabschnittsdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(
									implementation = LehrerPersonalabschnittsdaten.class)))) final List<LehrerPersonalabschnittsdatenCreateRequest> dtos,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withWriteAccess(request)
				.getLehrerPersonalabschnittsdatenController()
				.createMultiple(dtos);
	}

	/**
	 * Die OpenAPI-Methode für das teilweise Aktualisieren einer LehrerPersonalabschnittsdaten.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id      die Datenbank-ID der zu aktualisierenden LehrerPersonalabschnittsdaten
	 * @param dto     die zu aktualisierenden Felder
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die aktualisierten LehrerPersonalabschnittsdaten
	 */
	@PATCH
	@Path("/personalabschnittsdaten/{id : \\d+}")
	@Operation(summary = "Aktualisiert bestehende LehrerPersonalabschnittsdaten.",
			description = "Aktualisiert die LehrerPersonalabschnittsdaten mit der angegebenen ID. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die aktualisierten LehrerPersonalabschnittsdaten", content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = LehrerPersonalabschnittsdaten.class)))
	@ApiResponse(responseCode = "400", description = "Die Eingabedaten sind fehlerhaft.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine LehrerPersonalabschnittsdaten mit der angegebenen ID gefunden.")
	public Response patchLehrerPersonalabschnittsdaten(@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@RequestBody(description = "Die zu aktualisierenden Felder der LehrerPersonalabschnittsdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LehrerPersonalabschnittsdaten.class))) final LehrerPersonalabschnittsdatenPatchRequest dto,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withWriteAccess(request)
				.getLehrerPersonalabschnittsdatenController()
				.patch(id, dto);
	}

	/**
	 * Die OpenAPI-Methode für das teilweise Aktualisieren mehrerer LehrerPersonalabschnittsdaten.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param dtos    die zu aktualisierenden LehrerPersonalabschnittsdaten
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die aktualisierten LehrerPersonalabschnittsdaten
	 */
	@PATCH
	@Path("/personalabschnittsdaten/multiple")
	@Operation(summary = "Aktualisiert mehrere bestehende LehrerPersonalabschnittsdaten.",
			description = "Aktualisiert die LehrerPersonalabschnittsdaten mit den angegebenen IDs. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die aktualisierten LehrerPersonalabschnittsdaten", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerPersonalabschnittsdaten.class))))
	@ApiResponse(responseCode = "400", description = "Die Eingabedaten sind fehlerhaft.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Eine LehrerPersonalabschnittsdaten mit einer angegebenen ID wurde nicht gefunden.")
	public Response patchLehrerPersonalabschnittsdatenMultiple(@PathParam("schema") final String schema,
			@RequestBody(description = "Die zu aktualisierenden LehrerPersonalabschnittsdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(
									implementation = LehrerPersonalabschnittsdaten.class)))) final List<LehrerPersonalabschnittsdatenBatchPatchRequest> dtos,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withWriteAccess(request)
				.getLehrerPersonalabschnittsdatenController()
				.patchMultiple(dtos);
	}

	/**
	 * Die OpenAPI-Methode für das Löschen einer LehrerPersonalabschnittsdaten.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id      die Datenbank-ID der zu löschenden LehrerPersonalabschnittsdaten
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Löschoperation
	 */
	@DELETE
	@Path("/personalabschnittsdaten/{id : \\d+}")
	@Operation(summary = "Löscht eine LehrerPersonalabschnittsdaten.",
			description = "Löscht die LehrerPersonalabschnittsdaten mit der angegebenen ID. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Das Ergebnis der Löschoperation", content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine LehrerPersonalabschnittsdaten mit der angegebenen ID gefunden.")
	public Response deleteLehrerPersonalabschnittsdaten(@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withWriteAccess(request)
				.getLehrerPersonalabschnittsdatenController()
				.delete(id);
	}

	/**
	 * Die OpenAPI-Methode für das Löschen mehrerer LehrerPersonalabschnittsdaten.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param ids     die Datenbank-IDs der zu löschenden LehrerPersonalabschnittsdaten
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die Ergebnisse der Löschoperationen
	 */
	@DELETE
	@Path("/personalabschnittsdaten/multiple")
	@Operation(summary = "Löscht mehrere LehrerPersonalabschnittsdaten.",
			description = "Löscht die LehrerPersonalabschnittsdaten mit den angegebenen IDs. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Ergebnisse der Löschoperationen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten zu ändern.")
	public Response deleteLehrerPersonalabschnittsdatenMultiple(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden LehrerPersonalabschnittsdaten", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> ids,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withWriteAccess(request)
				.getLehrerPersonalabschnittsdatenController()
				.deleteMultiple(ids);
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
		return LehrerControllerFactory.withReadAccess(request)
				.getLehrerMinderleistungController()
				.get(id);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen einer Minderleistung zu den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema  das Datenbankschema
	 * @param input   der Create Request
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem neuen Datensaz für Entlastungsstunden
	 */
	@POST
	@Path("/personalabschnittsdaten/minderleistung/add")
	@Operation(
			summary = "Erstellt einen neuen Datensatz für eine Minderleistung in den Personalabschnittsdaten eines Lehrers und gibt das zugehörige Objekt zurück.",
			description =
					"Erstellt einen neuen Datensatz für für eine Minderleistung in den Personalabschnittsdaten eines Lehrers und gibt das zugehörige Objekt zurück. "
							+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen neuer Entlastungsstunden besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Minderleistung wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Minderleistungen anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addLehrerPersonalabschnittsdatenMinderleistung(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden Minderleistung ohne ID, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(
									implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class))) final LehrerMinderleistungCreateRequest input,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory.withWriteAccess(request)
				.getLehrerMinderleistungController()
				.create(input);
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
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Minderleistung zu löschen.")
	@ApiResponse(responseCode = "404", description = "Keine Minderleistung mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteLehrerPersonalabschnittsdatenMinderleistung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory.withWriteAccess(request)
				.getLehrerMinderleistungController()
				.delete(id);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen einer Minderleistung in den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Minderleistung
	 * @param input     der Patch Request
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
					schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class))) final LehrerMinderleistungPatchRequest input,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory.withWriteAccess(request)
				.getLehrerMinderleistungController()
				.patch(input, id);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen mehrerer Minderleistungen von Lehrern.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param input     Liste der Patch Requests
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/personalabschnittsdaten/minderleistung")
	@Operation(summary = "Passt die Minderleistungen an.",
			description = "Passt die Minderleistungen an und speichert die Ergebnisse in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personalabschnittsdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Minderleistung integriert.")
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten zu ändern.")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchLehrerPersonalabschnittsdatenMinderleistungen(@PathParam("schema") final String schema,
			@RequestBody(description = "Der Patch für die Minderleistung", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(
							implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class)))) final List<LehrerMinderleistungBatchPatchRequest> input,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory.withWriteAccess(request)
				.getLehrerMinderleistungController()
				.patchMultiple(input);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Mehrleistungen in den Personalabschnittsdaten eines Lehrers.
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
		return LehrerControllerFactory.withReadAccess(request)
				.getLehrerMehrleistungController().get(id);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen einer Mehrleistung zu den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema       das Datenbankschema
	 * @param body         die Daten der zu erstellenden Mehrleistung
	 * @param request      die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der neuen Mehrleistung
	 */
	@POST
	@Path("/personalabschnittsdaten/mehrleistung/add")
	@Operation(
			summary = "Erstellt einen neuen Datensatz für eine Mehrleistung in den Personalabschnittsdaten eines Lehrers und gibt das zugehörige Objekt zurück.",
			description =
					"Erstellt einen neuen Datensatz für für eine Mehrleistung in den Personalabschnittsdaten eines Lehrers und gibt das zugehörige Objekt zurück. "
							+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen neuer Entlastungsstunden besitzt.")
	@ApiResponse(responseCode = "201", description = "Die Mehrleistung wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Mehrleistungen anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addLehrerPersonalabschnittsdatenMehrleistung(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der zu erstellenden Mehrleistung ohne ID, welche automatisch generiert wird", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(
									implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class))) final LehrerMehrleistungCreateRequest body,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory.withWriteAccess(request)
				.getLehrerMehrleistungController().create(body);
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
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Mehrleistung zu löschen.")
	@ApiResponse(responseCode = "404", description = "Keine Mehrleistung mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteLehrerPersonalabschnittsdatenMehrleistung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory.withWriteAccess(request)
				.getLehrerMehrleistungController().delete(id);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen einer Mehrleistung in den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der Mehrleistung
	 * @param patch      der Patch für die Mehrleistung
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
							schema = @Schema(
									implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class))) final LehrerMehrleistungPatchRequest patch,
			@Context final HttpServletRequest request) {
		patch.id = id;
		return LehrerControllerFactory.withWriteAccess(request)
				.getLehrerMehrleistungController().patch(patch);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen mehrerer Mehrleistungen in den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param patches   die Patches
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/personalabschnittsdaten/mehrleistungen")
	@Operation(summary = "Passt die Mehrleistungen eines Lehrers an.",
			description = "Passt die Mehrleistungen eines Lehrers an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personalabschnittsdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Patches wurden erfolgreich in die allgemeinen Anrechnungsstunden integriert.",
			content = @Content(mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class))))
	@ApiResponse(responseCode = "400", description = "Die Patches sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine Mehrleistung mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchLehrerPersonalabschnittsdatenMehrleistungen(@PathParam("schema") final String schema,
			@Valid @RequestBody(description = "Der Patch für die allgemeine Anrechnung", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(
									implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class)))) final List<LehrerMehrleistungPatchRequest> patches,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory.withWriteAccess(request)
				.getLehrerMehrleistungController()
				.patchMultiple(patches);
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
		return LehrerControllerFactory.withReadAccess(request)
				.getLehrerAnrechnungsstundenController()
				.get(id);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen einer allgemeinen Anrechnung zu den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema
	 * @param patch     die Daten der allgemeinen Anrechnung
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der neuen allgemeinen Anrechnung
	 */
	@POST
	@Path("/personalabschnittsdaten/anrechnung/add")
	@Operation(
			summary = "Erstellt einen neuen Datensatz für eine allgemeine Anrechnung in den Personalabschnittsdaten eines Lehrers und gibt das zugehörige Objekt zurück.",
			description =
					"Erstellt einen neuen Datensatz für für eine Mehrleistung in den Personalabschnittsdaten eines Lehrers und gibt das zugehörige Objekt zurück. "
							+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen neuer Entlastungsstunden besitzt.")
	@ApiResponse(responseCode = "201", description = "Die allgemeine Anrechnung wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine allgemeine Anrechnung anzulegen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addLehrerPersonalabschnittsdatenAllgemeineAnrechnung(@PathParam("schema") final String schema,
			@Valid @RequestBody(description = "Die Daten der zu erstellenden allgemeinen Anrechnung ohne ID, welche automatisch generiert wird",
					required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(
									implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class))) final LehrerAnrechnungsstundeCreateRequest patch,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory.withWriteAccess(request)
				.getLehrerAnrechnungsstundenController()
				.create(patch);
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
		return LehrerControllerFactory.withWriteAccess(request)
				.getLehrerAnrechnungsstundenController()
				.delete(id);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen einer allgemeinen Anrechnung in den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation der allgemeinen Anrechnung
	 * @param patch     der Patch
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/personalabschnittsdaten/anrechnung/{id : \\d+}")
	@Operation(summary = "Passt die allgemeine Anrechnung zu der angegebenen ID an.",
			description = "Passt die allgemeine Anrechnung zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personalabschnittsdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die allgemeine Anrechnung integriert.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class)))
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine allgemeine Anrechnung mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchLehrerPersonalabschnittsdatenAllgemeineAnrechnung(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Valid @RequestBody(description = "Der Patch für die allgemeine Anrechnung", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(
									implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class))) final LehrerAnrechnungsstundePatchRequest patch,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory.withWriteAccess(request)
				.getLehrerAnrechnungsstundenController()
				.patch(patch);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen mehrerer allgemeinen Anrechnungsstunden in den Personalabschnittsdaten eines Lehrers.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param patches   die Patches
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/personalabschnittsdaten/anrechnungen")
	@Operation(summary = "Passt die allgemeinen Anrechnungsstunden eines Lehrers an.",
			description = "Passt die allgemeinen Anrechnungsstunden eines Lehrers an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personalabschnittsdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Patches wurden erfolgreich in die allgemeinen Anrechnungsstunden integriert.",
			content = @Content(mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class))))
	@ApiResponse(responseCode = "400", description = "Die Patches sind fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine allgemeine Anrechnung mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. eine negative ID)")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchLehrerPersonalabschnittsdatenAllgemeineAnrechnungen(@PathParam("schema") final String schema,
			@Valid @RequestBody(description = "Der Patch für die allgemeine Anrechnung", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(
									implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class)))) final List<LehrerAnrechnungsstundePatchRequest> patches,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory.withWriteAccess(request)
				.getLehrerAnrechnungsstundenController()
				.patchMultiple(patches);
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Unterrichtsfächer eines Lehrers.
	 *
	 * @param schema    das Datenbankschema
	 * @param id        die ID des Lehrers
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Unterrichtsfächer des Lehrers
	 */
	@GET
	@Path("/{id : \\d+}/personaldaten/unterrichtsfach")
	@Operation(summary = "Liefert die Unterrichtsfächer eines Lehrers.",
			description = "Liest die Unterrichtsfächer des Lehrers mit der angegebenen ID aus der Datenbank und liefert diese zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerpersonaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Unterrichtsfächer des Lehrers",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerUnterrichtsfach.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerpersonaldaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Lehrer-Eintrag mit der angegebenen ID gefunden")
	public Response getLehrerUnterrichtsfaecher(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory.withReadAccess(request)
				.getLehrerUnterrichtsfachController()
				.getListByLehrerId(id);
	}


	/**
	 * Die OpenAPI-Methode für das Hinzufügen eines Unterrichtsfachs zu einem Lehrer.
	 *
	 * @param schema    das Datenbankschema
	 * @param patch     die Daten für den neuen Eintrag
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem neuen Datensatz
	 */
	@POST
	@Path("/personaldaten/unterrichtsfach")
	@Operation(summary = "Erstellt einen neuen Datensatz für ein Unterrichtsfach eines Lehrers.",
			description = "Erstellt einen neuen Datensatz für ein Unterrichtsfach eines Lehrers und gibt das zugehörige Objekt zurück. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personaldaten besitzt.")
	@ApiResponse(responseCode = "201", description = "Das Unterrichtsfach wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LehrerUnterrichtsfach.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addLehrerUnterrichtsfach(@PathParam("schema") final String schema,
			@Valid @RequestBody(description = "Die Daten des neuen Unterrichtsfachs", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LehrerUnterrichtsfach.class))) final LehrerUnterrichtsfachCreateRequest patch,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory.withWriteAccess(request)
				.getLehrerUnterrichtsfachController()
				.create(patch);
	}


	/**
	 * Die OpenAPI-Methode für das Patchen eines Unterrichtsfachs eines Lehrers.
	 *
	 * @param schema   das Datenbankschema
	 * @param id       die ID des Unterrichtsfach-Eintrags
	 * @param patch    der Patch
	 * @param request  die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/personaldaten/unterrichtsfach/{id : \\d+}")
	@Operation(summary = "Führt einen Patch auf einem Unterrichtsfach-Eintrag eines Lehrers durch.",
			description = "Passt den Unterrichtsfach-Eintrag mit der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LehrerUnterrichtsfach.class)))
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Unterrichtsfach-Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchLehrerUnterrichtsfach(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Valid @RequestBody(description = "Der Patch für den Unterrichtsfach-Eintrag", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LehrerUnterrichtsfach.class))) final LehrerUnterrichtsfachPatchRequest patch,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory.withWriteAccess(request)
				.getLehrerUnterrichtsfachController()
				.patch(id, patch);
	}


	/**
	 * Die OpenAPI-Methode für das Entfernen eines Unterrichtsfachs eines Lehrers.
	 *
	 * @param schema   das Datenbankschema
	 * @param id       die ID des Unterrichtsfach-Eintrags
	 * @param request  die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Datensatz
	 */
	@DELETE
	@Path("/personaldaten/unterrichtsfach/{id : \\d+}")
	@Operation(summary = "Entfernt den Unterrichtsfach-Eintrag eines Lehrers.",
			description = "Entfernt den Unterrichtsfach-Eintrag mit der angegebenen ID aus der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personaldaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Datensatz wurde erfolgreich entfernt.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = LehrerUnterrichtsfach.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu entfernen.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteLehrerUnterrichtsfach(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory.withWriteAccess(request)
				.getLehrerUnterrichtsfachController()
				.delete(id);
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
	@GZIP
	@Path("/allgemein/zugangsgruende")
	@Operation(summary = "Gibt den Katalog der Lehrerzugangsgründe zurück.",
			description =
					"Erstellt eine Liste aller in dem Katalog vorhanden Lehrerzugangsgründe unter Angabe der ID, der Bezeichnung und des Statistikschlüssels. "
							+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Lehrerzugangsgrund-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerZugangsgrundKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Lehrerzugangsgrund-Katalog-Einträge gefunden")
	public Response getKatalogLehrerZugangsgruende(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
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
	@GZIP
	@Path("/allgemein/abgangsgruende")
	@Operation(summary = "Gibt den Katalog der Lehrerabgangsgründe zurück.",
			description =
					"Erstellt eine Liste aller in dem Katalog vorhanden Lehrerabgangsgründe unter Angabe der ID, der Bezeichnung und des Statistikschlüssels. "
							+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Lehrerabgangsgrund-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerAbgangsgrundKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Lehrerabgangsgrund-Katalog-Einträge gefunden")
	public Response getKatalogLehrerAbgangsgruende(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
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
	@GZIP
	@Path("/allgemein/beschaeftigungsarten")
	@Operation(summary = "Gibt den Katalog der Beschäftigungsarten zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Beschäftigungsarten unter Angabe der ID, eines Kürzels und der Bezeichnung. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Beschäftigungsart-Katalog-Einträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerBeschaeftigungsartKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Beschäftigungsart-Katalog-Einträge gefunden")
	public Response getKatalogLehrerBeschaeftigungsarten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
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
	@GZIP
	@Path("/allgemein/einsatzstatus")
	@Operation(summary = "Gibt den Katalog des Einsatzstatus zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Einsatzstatusarten unter Angabe der ID, eines Kürzels und der Bezeichnung. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Einsatzstatus-Katalog-Einträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerEinsatzstatusKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Einsatzstatus-Katalog-Einträge gefunden")
	public Response getKatalogLehrerEinsatzstatus(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
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
	@GZIP
	@Path("/allgemein/fachrichtungen")
	@Operation(summary = "Gibt den Katalog der Fachrichtungen von Lehrern zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Fachrichtungen von Lehrern. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Fachrichtungens-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerFachrichtungKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Fachrichtungs-Katalog-Einträge gefunden")
	public Response getKatalogLehrerFachrichtungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
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
	@GZIP
	@Path("/allgemein/fachrichtungen_anerkennungen")
	@Operation(summary = "Gibt den Katalog des Anerkennungen von Fachrichtungen für Lehrer zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Anerkennungen von Fachrichtungen für Lehrer. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Anerkennungs-Katalog-Einträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerFachrichtungAnerkennungKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Anerkennungs-Katalog-Einträge gefunden")
	public Response getKatalogLehrerFachrichtungAnerkennungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
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
	@GZIP
	@Path("/allgemein/lehraemter")
	@Operation(summary = "Gibt den Katalog der Lehrämter zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Lehrämter. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Lehramt-Katalog-Einträgen",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerLehramtKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Lehramt-Katalog-Einträge gefunden")
	public Response getKatalogLehrerLehraemter(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
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
	@GZIP
	@Path("/allgemein/lehraemter_anerkennungen")
	@Operation(summary = "Gibt den Katalog der Anerkennungen von Lehrämtern zurück.",
			description = "Erstellt eine Liste aller Anerkennungen von Lehrämtern. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Anerkennungs-Katalog-Einträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerLehramtAnerkennungKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Anerkennungs-Katalog-Einträge gefunden")
	public Response getKatalogLehrerLehramtAnerkennungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
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
	@GZIP
	@Path("/allgemein/lehrbefaehigungen")
	@Operation(summary = "Gibt den Katalog des Lehrbefähigungen von Lehrern zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Lehrbefähigungen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Lehrbefähigung-Katalog-Einträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerLehrbefaehigungKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Lehrbefähigung-Katalog-Einträge gefunden")
	public Response getKatalogLehrerLehrbefaehigungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
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
	@GZIP
	@Path("/allgemein/lehrbefaehigungen_anerkennungen")
	@Operation(summary = "Gibt den Katalog der Anerkennungen von Lehrbefähigungen zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Anerkennungen von Lehrbefähigungen. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Einsatzstatus-Katalog-Einträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerLehrbefaehigungAnerkennungKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Anerkennungs-Katalog-Einträge gefunden")
	public Response getKatalogLehrerLehrbefaehigungenAnerkennungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
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
	@GZIP
	@Path("/allgemein/mehrleistungsarten")
	@Operation(summary = "Gibt den Katalog der Arten von Mehrleistungen durch Lehrer zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden die Arten von Mehrleistungen durch Lehrer. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Mehrleistungsart-Katalog-Einträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerMehrleistungsartKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Mehrleistungsart-Katalog-Einträge gefunden")
	public Response getKatalogLehrerMehrleistungsarten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
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
	@GZIP
	@Path("/allgemein/minderleistungsarten")
	@Operation(summary = "Gibt den Katalog der Arten von Minderleistungen durch Lehrer zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Arten von Minderleistungen durch Lehrer. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Minderleistungsart-Katalog-Einträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerMinderleistungsartKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Minderleistungsart-Katalog-Einträge gefunden")
	public Response getKatalogLehrerMinderleistungsarten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
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
	@GZIP
	@Path("/allgemein/rechtsverhaeltnisse")
	@Operation(summary = "Gibt den Katalog des Rechtsverhältnisse zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Rechtsverhältnisse unter Angabe der ID, eines Kürzels und der Bezeichnung. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Rechtsverhältnis-Katalog-Einträgen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerRechtsverhaeltnisKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Rechtsverhältnis-Katalog-Einträge gefunden")
	public Response getKatalogLehrerRechtsverhaeltnisse(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
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
	@GZIP
	@Path("/allgemein/anrechnungsgruende")
	@Operation(summary = "Gibt den Katalog der Gründe für Anrechnungsstunden von Lehrern zurück.",
			description = "Erstellt eine Liste aller in dem Katalog vorhanden Gründe für Anrechnungsstunden von Lehrern."
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Einträgen für Gründe von Anrechnungsstunden von Lehrern",
			content = @Content(mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = LehrerAnrechnungsgrundKatalogEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
	public Response getKatalogLehrerAnrechnungsgruende(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
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
				BenutzerKompetenz.LEHRERDATEN_ANSEHEN);
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
				BenutzerKompetenz.LEHRERDATEN_AENDERN);
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
				BenutzerKompetenz.LEHRERDATEN_ANSEHEN);
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
				BenutzerKompetenz.LEHRERDATEN_AENDERN);
	}

	/**
	 * Die OpenAPI-Methode für das Abrufen aller Schulleitungseinträge.
	 *
	 * @param schema    das Datenbankschema
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der Liste aller Schulleitungseinträge
	 */
	@GET
	@Path("/schulleitung")
	@Operation(summary = "Gibt alle Schulleitungseinträge zurück.",
			description = "Gibt alle Schulleitungseinträge zurück, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Schulleitungseinträge wurden erfolgreich abgerufen.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Schulleitung.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schulleitungseinträge abzurufen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getAllSchulleitungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return SchulleitungControllerFactory
				.withReadAccess(request)
				.getSchulleitungController()
				.getAll();
	}

	/**
	 * Die OpenAPI-Methode für das Abrufen aller Schulleitungseinträge eines Lehrers.
	 *
	 * @param schema      das Datenbankschema
	 * @param idLehrer    die ID des Lehrers
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit der Liste der Schulleitungseinträge des Lehrers
	 */
	@GET
	@Path("/schulleitung/{idLehrer : \\d+}")
	@Operation(summary = "Gibt alle Schulleitungseinträge eines Lehrers zurück.",
			description = "Gibt alle Schulleitungseinträge des angegebenen Lehrers zurück,"
					+ " insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Schulleitungseinträge des Lehrers wurden erfolgreich abgerufen.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = Schulleitung.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schulleitungseinträge abzurufen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response getAllSchulleitungenByLehrer(@PathParam("schema") final String schema, @PathParam("idLehrer") final long idLehrer,
			@Context final HttpServletRequest request) {
		return SchulleitungControllerFactory
				.withReadAccess(request)
				.getSchulleitungController()
				.getAllByIdLehrer(idLehrer);
	}

	/**
	 * Die OpenAPI-Methode für das Hinzufügen eines Schulleitungseintrags.
	 *
	 * @param schema    das Datenbankschema
	 * @param input     die Daten des neuen Schulleitungseintrags
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem neu erstellten Schulleitungseintrag
	 */
	@POST
	@Path("/schulleitung")
	@Operation(summary = "Erstellt einen neuen Schulleitungseintrag und gibt das zugehörige Objekt zurück.",
			description = "Erstellt einen neuen Schulleitungseintrag, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.")
	@ApiResponse(responseCode = "201", description = "Der Schulleitungseintrag wurde erfolgreich hinzugefügt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Schulleitung.class)))
	@ApiResponse(responseCode = "400", description = "Die Eingabedaten sind fehlerhaft (z.B. ungültiges Datumsformat oder unbekannte Leitungsfunktion).")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schulleitungseinträge hinzuzufügen.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response addSchulleitung(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten des zu erstellenden Schulleitungseintrags ohne ID, die automatisch generiert wird",
					required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = Schulleitung.class))) final SchulleitungCreateRequest input,
			@Context final HttpServletRequest request) {
		return SchulleitungControllerFactory
				.withWriteAccess(request)
				.getSchulleitungController()
				.create(input);
	}

	/**
	 * Die OpenAPI-Methode für das Patchen eines Schulleitungseintrags.
	 *
	 * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
	 * @param id        die Datenbank-ID zur Identifikation des Schulleitungseintrags
	 * @param patch     das partielle Update als {@link SchulleitungPatchRequest}
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Patch-Operation
	 */
	@PATCH
	@Path("/schulleitung/{id : \\d+}")
	@Operation(summary = "Patcht und persistiert den zur ID zugehörigen Schulleitungseintrag.",
			description = "Patcht und persistiert den Schulleitungseintrag, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.")
	@ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich ausgeführt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Schulleitung.class)))
	@ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schulleitungseinträge zu ändern.")
	@ApiResponse(responseCode = "404", description = "Kein Schulleitungseintrag mit der angegebenen ID gefunden.")
	@ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde"
			+ " (z.B. Enddatum liegt vor Startdatum).")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response patchSchulleitung(@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@RequestBody(description = "Der Patch für den Schulleitungseintrag", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = Schulleitung.class))) final SchulleitungPatchRequest patch,
			@Context final HttpServletRequest request) {
		return SchulleitungControllerFactory
				.withWriteAccess(request)
				.getSchulleitungController()
				.patch(id, patch);
	}

	/**
	 * Die OpenAPI-Methode für das Entfernen mehrerer Schulleitungseinträge.
	 *
	 * @param schema    das Datenbankschema
	 * @param ids       die IDs der zu löschenden Schulleitungseinträge
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die HTTP-Antwort mit dem Status und den Ergebnissen der Löschoperationen
	 */
	@DELETE
	@Path("/schulleitung/multiple")
	@Operation(summary = "Entfernt mehrere Schulleitungseinträge.",
			description = "Entfernt die angegebenen Schulleitungseinträge, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Schulleitungseinträge wurden erfolgreich entfernt.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schulleitungseinträge zu entfernen.")
	@ApiResponse(responseCode = "404", description = "Mindestens ein Schulleitungseintrag ist nicht vorhanden.")
	@ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft.")
	@ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
	public Response deleteSchulleitungen(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Schulleitungseinträge", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> ids,
			@Context final HttpServletRequest request) {
		return SchulleitungControllerFactory
				.withDeleteAccess(request)
				.getSchulleitungController()
				.delete(ids);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage einer Lehrerfunktion anhand ihrer ID.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id      die Datenbank-ID der Lehrerfunktion
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die Lehrerfunktion
	 */
	@GET
	@Path("/funktionen/{id : \\d+}")
	@Operation(summary = "Liefert die Lehrerfunktion mit der angegebenen ID.",
			description = "Liest die Lehrerfunktion mit der angegebenen ID aus der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Lehrerfunktion", content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = LehrerFunktion.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Lehrerfunktion mit der angegebenen ID gefunden.")
	public Response getLehrerFunktion(@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withReadAccess(request)
				.getLehrerFunktionController()
				.get(id);
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage aller Lehrerfunktionen.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste aller Lehrerfunktionen
	 */
	@GET
	@Path("/funktionen")
	@Operation(summary = "Liefert alle Lehrerfunktionen.",
			description = "Liest alle Lehrerfunktionen aus der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Lehrerfunktionen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerFunktion.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten anzusehen.")
	public Response getLehrerFunktionen(@PathParam("schema") final String schema,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withReadAccess(request)
				.getLehrerFunktionController()
				.getAll();
	}

	/**
	 * Die OpenAPI-Methode für die Abfrage aller Lehrerfunktionen eines Lehrerabschnitts.
	 *
	 * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id          die Datenbank-ID des Lehrers
	 * @param idAbschnitt die Datenbank-ID der Lehrerabschnittsdaten
	 * @param request     die Informationen zur HTTP-Anfrage
	 *
	 * @return die Liste der Lehrerfunktionen des Abschnitts
	 */
	@GET
	@Path("/{id : \\d+}/abschnittsdaten/{idAbschnitt : \\d+}/funktionen")
	@Operation(summary = "Liefert alle Lehrerfunktionen zu einem Lehrerabschnitt.",
			description = "Liest alle Lehrerfunktionen zum angegebenen Lehrerabschnitt aus der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Liste der Lehrerfunktionen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerFunktion.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Lehrerabschnitt mit der angegebenen ID gefunden.")
	public Response getLehrerFunktionenByAbschnitt(@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@PathParam("idAbschnitt") final long idAbschnitt,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withReadAccess(request)
				.getLehrerFunktionController()
				.getListByIdAbschnitt(idAbschnitt);
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen einer neuen Lehrerfunktion.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param dto     die Daten der neuen Lehrerfunktion
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die erstellte Lehrerfunktion
	 */
	@POST
	@Path("/funktionen")
	@Operation(summary = "Erstellt eine neue Lehrerfunktion.",
			description = "Erstellt eine neue Lehrerfunktion und speichert diese in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "201", description = "Die erstellte Lehrerfunktion", content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = LehrerFunktion.class)))
	@ApiResponse(responseCode = "400", description = "Die Kombination aus Abschnitt und Funktion existiert bereits.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten zu ändern.")
	public Response createLehrerFunktion(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der neuen Lehrerfunktion", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LehrerFunktion.class))) final LehrerFunktionCreateRequest dto,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withWriteAccess(request)
				.getLehrerFunktionController()
				.create(dto);
	}

	/**
	 * Die OpenAPI-Methode für das Erstellen mehrerer neuer Lehrerfunktionen.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param dtos    die Daten der neuen Lehrerfunktionen
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die erstellten Lehrerfunktionen
	 */
	@POST
	@Path("/funktionen/multiple")
	@Operation(summary = "Erstellt mehrere neue Lehrerfunktionen.",
			description = "Erstellt mehrere neue Lehrerfunktionen und speichert diese in der Datenbank. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "201", description = "Die erstellten Lehrerfunktionen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerFunktion.class))))
	@ApiResponse(responseCode = "400", description = "Eine Kombination aus Abschnitt und Funktion existiert bereits.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten zu ändern.")
	public Response createLehrerFunktionen(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten der neuen Lehrerfunktionen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = LehrerFunktion.class)))) final List<LehrerFunktionCreateRequest> dtos,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withWriteAccess(request)
				.getLehrerFunktionController()
				.createMultiple(dtos);
	}

	/**
	 * Die OpenAPI-Methode für das teilweise Aktualisieren einer Lehrerfunktion.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id      die Datenbank-ID der zu aktualisierenden Lehrerfunktion
	 * @param dto     die zu aktualisierenden Felder
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die aktualisierte Lehrerfunktion
	 */
	@PATCH
	@Path("/funktionen/{id : \\d+}")
	@Operation(summary = "Aktualisiert eine bestehende Lehrerfunktion.",
			description = "Aktualisiert die Lehrerfunktion mit der angegebenen ID. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die aktualisierte Lehrerfunktion", content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = LehrerFunktion.class)))
	@ApiResponse(responseCode = "400", description = "Die Kombination aus Abschnitt und Funktion existiert bereits.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Keine Lehrerfunktion mit der angegebenen ID gefunden.")
	public Response patchLehrerFunktion(@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@RequestBody(description = "Die zu aktualisierenden Felder der Lehrerfunktion", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = LehrerFunktion.class))) final LehrerFunktionPatchRequest dto,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withWriteAccess(request)
				.getLehrerFunktionController()
				.patch(id, dto);
	}

	/**
	 * Die OpenAPI-Methode für das teilweise Aktualisieren mehrerer Lehrerfunktionen.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param dtos    die zu aktualisierenden Lehrerfunktionen
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die aktualisierten Lehrerfunktionen
	 */
	@PATCH
	@Path("/funktionen/multiple")
	@Operation(summary = "Aktualisiert mehrere bestehende Lehrerfunktionen.",
			description = "Aktualisiert die Lehrerfunktionen mit den angegebenen IDs. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die aktualisierten Lehrerfunktionen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = LehrerFunktion.class))))
	@ApiResponse(responseCode = "400", description = "Eine Kombination aus Abschnitt und Funktion existiert bereits.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten zu ändern.")
	@ApiResponse(responseCode = "404", description = "Eine Lehrerfunktion mit einer angegebenen ID wurde nicht gefunden.")
	public Response patchLehrerFunktionen(@PathParam("schema") final String schema,
			@RequestBody(description = "Die zu aktualisierenden Lehrerfunktionen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = LehrerFunktion.class)))) final List<LehrerFunktionBatchPatchRequest> dtos,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withWriteAccess(request)
				.getLehrerFunktionController()
				.patchMultiple(dtos);
	}

	/**
	 * Die OpenAPI-Methode für das Löschen einer Lehrerfunktion.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param id      die Datenbank-ID der zu löschenden Lehrerfunktion
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Löschoperation
	 */
	@DELETE
	@Path("/funktionen/{id : \\d+}")
	@Operation(summary = "Löscht eine Lehrerfunktion.",
			description = "Löscht die Lehrerfunktion mit der angegebenen ID. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Das Ergebnis der Löschoperation", content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten zu ändern.")
	public Response deleteLehrerFunktion(@PathParam("schema") final String schema,
			@PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withWriteAccess(request)
				.getLehrerFunktionController()
				.delete(id);
	}

	/**
	 * Die OpenAPI-Methode für das Löschen mehrerer Lehrerfunktionen.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param ids     die Datenbank-IDs der zu löschenden Lehrerfunktionen
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die Ergebnisse der Löschoperationen
	 */
	@DELETE
	@Path("/funktionen/multiple")
	@Operation(summary = "Löscht mehrere Lehrerfunktionen.",
			description = "Löscht die Lehrerfunktionen mit den angegebenen IDs. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrerdaten besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Ergebnisse der Löschoperationen", content = @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = SimpleOperationResponse.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten zu ändern.")
	public Response deleteLehrerFunktionen(@PathParam("schema") final String schema,
			@RequestBody(description = "Die IDs der zu löschenden Lehrerfunktionen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> ids,
			@Context final HttpServletRequest request) {
		return LehrerControllerFactory
				.withWriteAccess(request)
				.getLehrerFunktionController()
				.deleteMultiple(ids);
	}

}
