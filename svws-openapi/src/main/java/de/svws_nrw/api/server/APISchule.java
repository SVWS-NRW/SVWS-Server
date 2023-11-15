package de.svws_nrw.api.server;

import java.io.InputStream;

import de.svws_nrw.core.data.schule.AbgangsartKatalog;
import de.svws_nrw.core.data.schule.AllgemeineMerkmaleKatalogEintrag;
import de.svws_nrw.core.data.schule.Aufsichtsbereich;
import de.svws_nrw.core.data.schule.BerufskollegAnlageKatalogEintrag;
import de.svws_nrw.core.data.schule.BerufskollegBerufsebeneKatalogEintrag;
import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalog;
import de.svws_nrw.core.data.schule.EinschulungsartKatalogEintrag;
import de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag;
import de.svws_nrw.core.data.schule.FoerderschwerpunktKatalogEintrag;
import de.svws_nrw.core.data.schule.HerkunftsschulnummerKatalogEintrag;
import de.svws_nrw.core.data.schule.KindergartenbesuchKatalogEintrag;
import de.svws_nrw.core.data.schule.NationalitaetenKatalogEintrag;
import de.svws_nrw.core.data.schule.NotenKatalogEintrag;
import de.svws_nrw.core.data.schule.OrganisationsformKatalogEintrag;
import de.svws_nrw.core.data.schule.PruefungsordnungKatalogEintrag;
import de.svws_nrw.core.data.schule.Raum;
import de.svws_nrw.core.data.schule.ReformpaedagogikKatalogEintrag;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.core.data.schule.ReligionKatalogEintrag;
import de.svws_nrw.core.data.schule.SchuelerstatusKatalogEintrag;
import de.svws_nrw.core.data.schule.SchulabschlussAllgemeinbildendKatalogEintrag;
import de.svws_nrw.core.data.schule.SchulabschlussBerufsbildendKatalogEintrag;
import de.svws_nrw.core.data.schule.SchuleStammdaten;
import de.svws_nrw.core.data.schule.SchulenKatalogEintrag;
import de.svws_nrw.core.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.core.data.schule.SchulgliederungKatalogEintrag;
import de.svws_nrw.core.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.schule.SchulstufeKatalogEintrag;
import de.svws_nrw.core.data.schule.SchultraegerKatalogEintrag;
import de.svws_nrw.core.data.schule.VerkehrsspracheKatalogEintrag;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenzeit;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.kataloge.DataKatalogAufsichtsbereiche;
import de.svws_nrw.data.kataloge.DataKatalogPausenzeiten;
import de.svws_nrw.data.kataloge.DataKatalogRaeume;
import de.svws_nrw.data.kataloge.DataKatalogZeitraster;
import de.svws_nrw.data.schueler.DataKatalogSchuelerFoerderschwerpunkte;
import de.svws_nrw.data.schule.DataKatalogAbgangsartenAllgemeinbildend;
import de.svws_nrw.data.schule.DataKatalogAbgangsartenBerufsbildend;
import de.svws_nrw.data.schule.DataKatalogAllgemeineMerkmale;
import de.svws_nrw.data.schule.DataKatalogBerufskollegAnlagen;
import de.svws_nrw.data.schule.DataKatalogBerufskollegBerufsebenen;
import de.svws_nrw.data.schule.DataKatalogBerufskollegFachklassen;
import de.svws_nrw.data.schule.DataKatalogEinschulungsarten;
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
import de.svws_nrw.data.schule.DataSchuelerStatus;
import de.svws_nrw.data.schule.DataSchuleStammdaten;
import de.svws_nrw.data.schule.DataSchulstufen;
import de.svws_nrw.db.DBEntityManager;
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
@SqlResultSetMapping(name = "SQLMappingSchuljahresabschnitte",
	entities = { @EntityResult(entityClass = Schuljahresabschnitt.class,
		fields = {
			@FieldResult(name = "schuljahr", column = "schuljahr"),
			@FieldResult(name = "abschnitt", column = "abschnitt")
		}
	)}
)
public class APISchule {

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
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = SchuleStammdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schule anzulegen.")
    @ApiResponse(responseCode = "404", description = "Keine Schule mit der angegebenen Schulnummer gefunden")
    @ApiResponse(responseCode = "409", description = "Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde, dies ist z.B. der Fall, falls zuvor schon eine Schule angelegt wurde.")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response initSchule(@PathParam("schema") final String schema, @PathParam("schulnummer") final int schulnummer, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuleStammdaten(conn).init(schulnummer),
    		request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
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
               description = "Liefert die Schulnummer der Schule. Es wird geprüft, ob der SVWS-Benutzer die "
               		       + "notwendige Berechtigung zum Ansehen der Schuldaten besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Schulnummer",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON,
                 schema = @Schema(implementation = Integer.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuldaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Schule in der Datenbank vorhanden")
    public Response getSchuleNummer(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuleStammdaten(conn).getSchulnummerResponse(),
    		request, ServerMode.STABLE, BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN);
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schuldaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Stammdaten der Schule",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON,
                 schema = @Schema(implementation = SchuleStammdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuldaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag mit dem angegebenen Schema gefunden")
    public Response getSchuleStammdaten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuleStammdaten(conn).get(),
			request, ServerMode.STABLE, BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN);
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schuldaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Schul-Stammdaten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuldaten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchSchuleStammdaten(
    		@PathParam("schema") final String schema,
    		@RequestBody(description = "Der Patch für die Schul-Stammdaten", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuleStammdaten.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuleStammdaten(conn).patch(null, is),
    		request, ServerMode.STABLE, BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schuldaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Das Logo der Schule",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON,
                 schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuldaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag mit dem angegebenen Schema gefunden")
    public Response getSchullogo(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuleStammdaten(conn).getSchullogo(),
    		request, ServerMode.STABLE, BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN);
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
    description = "Setzt das Logo der Schule. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schuldaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Das Logo der Schule wurde gesetzt")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuldaten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag für die Schule gefunden")
    public Response putSchullogo(@PathParam("schema") final String schema,
    		@RequestBody(description = "Das Logo der Schule", required = false, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new DataSchuleStammdaten(conn).putSchullogo(is),
				request, ServerMode.STABLE, BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
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
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchulabschlussAllgemeinbildendKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Abschlussart-Katalog-Einträge gefunden")
    public Response getSchulabschluesseAllgemeinbildend(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogSchulabschluesseAllgemeinbildend(null).getList(),
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
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
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchulabschlussBerufsbildendKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Abschlussart-Katalog-Einträge gefunden")
    public Response getSchulabschluesseBerufsbildend(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogSchulabschluesseBerufsbildend(null).getList(),
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
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
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
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
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
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
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
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
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BerufskollegBerufsebeneKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Berufskolleg-Berufsebenen-Katalog-Einträge gefunden")
    public Response getBerufskollegBerufsebenen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogBerufskollegBerufsebenen(null).getList(),
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
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
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
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
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges der Verkehrssprachen in einer Familie.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die Liste mit dem Katalog der Verkehrssprachen in einer Familie
     */
    @GET
    @Path("/allgemein/verkehrssprachen")
    @Operation(summary = "Gibt den Katalog der Verkehrssprachen in einer Familie zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden der Verkehrssprachen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = VerkehrsspracheKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getVerkehrssprachen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogVerkehrssprachen(null).getList(),
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
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
    	DBBenutzerUtils.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
		return (new DataKatalogNationalitaeten(null)).getAll();
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
    	DBBenutzerUtils.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
		return (new DataKatalogEinschulungsarten(null)).getAll();
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
    	DBBenutzerUtils.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
		return (new DataKatalogAllgemeineMerkmale(null)).getAll();
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
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogFoerderschwerpunkte().getList(),
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
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
    @ApiResponse(responseCode = "404", description = "Keine Förderschwerpunkt-Katalog-Einträge gefunden")
    public Response getSchuelerFoerderschwerpunkte(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogSchuelerFoerderschwerpunkte(conn).getList(),
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
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
    @Path("/foerderschwerpunkt/{id : \\d+}")
    @Operation(summary = "Liefert zu der ID des Förderschwerpunktes die zugehörigen Daten.",
    description = "Liest die Daten des Förderschwerpunktes zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Daten des Förderschwerpunktes",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = FoerderschwerpunktEintrag.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Förderschwerpunkt-Eintrag mit der angegebenen ID gefunden")
    public Response getSchuelerFoerderschwerpunkt(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		                                        @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogSchuelerFoerderschwerpunkte(conn).get(id),
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
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
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Daten der Religion",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ReligionEintrag.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Religion mit der angegebenen ID gefunden")
    public Response getReligion(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		                                        @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataReligionen(conn).get(id),
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Religion "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Religion wurde erfolgreich angelegt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ReligionEintrag.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Religion anzulegen.")
    @ApiResponse(responseCode = "404", description = "Keine Religion  mit dem eingegebenen Kuerzel gefunden")
    @ApiResponse(responseCode = "409", description = "Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response createReligion(
    		@PathParam("schema") final String schema,
    		@RequestBody(description = "Der Post für die Religion-Daten", required = true, content =
			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ReligionEintrag.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataReligionen(conn).create(is),
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Religion "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Religion-Daten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Religion-Daten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Keine Religion mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")

    public Response patchReligion(@PathParam("schema") final String schema, @PathParam("id") final long id,
                                        @RequestBody(description = "Der Patch für die Religion-Stammdaten", required = true,
                                        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ReligionEintrag.class))) final
                                        InputStream is, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataReligionen(conn).patch(id, is),
        	request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
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
               		       + "welche auf dem Zeugnis erscheint, einem Statistik-Kürzel, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar bzw. "
               		       + "änderbar sein sollen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReligionEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getReligionen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataReligionen(conn).getAll(),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
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
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
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
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
    }



    /**
     * Die OpenAPI-Methode für die Abfrage des Katalogs der gültigen Organisationsformen.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              der Katalog der gültigen Organisationsformen
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
        DBBenutzerUtils.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
        return (new DataKatalogOrganisationsformen()).getAll();
    }




    /**
     * Die OpenAPI-Methode für die Abfrage des Katalogs der gültigen Schulstufen.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              der Katalog der gültigen Schulstufen
     */
    @GET
    @Path("/allgemein/schulstufen")
    @Operation(summary = "Gibt den Katalog der gültigen Schulstufen zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden gültigen Schulstufen. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Schulstufen-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchulstufeKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getSchulstufen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        DBBenutzerUtils.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
        return (new DataSchulstufen()).getAll();
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Schulen-Kataloges.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
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
        DBBenutzerUtils.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
        return (new DataKatalogSchulen()).getAll();
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Schulträger-Kataloges.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
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
        DBBenutzerUtils.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
        return (new DataKatalogSchultraeger()).getAll();
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
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerstatusKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogSchuelerStatus(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        DBBenutzerUtils.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
        return (new DataSchuelerStatus()).getAll();
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
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = HerkunftsschulnummerKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogHerkunftsschulnummern(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        DBBenutzerUtils.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
        return (new DataKatalogHerkunftsschulnummern()).getAll();
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
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
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
        	request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
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
    public Response getKatalogReformpaedagogikEintrag(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogReformpaedagogik(conn).get(id),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
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
        	request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
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
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = NotenKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
    @ApiResponse(responseCode = "404", description = "Keine Noten-Einträge gefunden.")
    public Response getKatalogNoten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogNoten().getList(),
        	request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
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
        	request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
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
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Raum der Schule",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = Raum.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Raum bei der Schule gefunden")
    public Response getRaum(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogRaeume(conn).get(id),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Katalog-Daten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchRaum(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für den Raum der Schule", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Raum.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogRaeume(conn).patch(id, is),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs "
    		    + "besitzt.")
    @ApiResponse(responseCode = "201", description = "Der Raum wurde erfolgreich hinzugefügt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Raum.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Raum für die Schule anzulegen.")
    @ApiResponse(responseCode = "404", description = "Die Katalogdaten wurden nicht gefunden")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addRaum(@PathParam("schema") final String schema,
    		@RequestBody(description = "Die Daten des zu erstellenden Raumes ohne ID, welche automatisch generiert wird", required = true, content =
			   @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Raum.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogRaeume(conn).add(is),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs "
    		    + "besitzt.")
    @ApiResponse(responseCode = "201", description = "Die Räume wurden erfolgreich hinzugefügt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            array = @ArraySchema(schema = @Schema(implementation = Raum.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Räume für die Schule anzulegen.")
    @ApiResponse(responseCode = "404", description = "Die Katalogdaten wurden nicht gefunden")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addRaeume(@PathParam("schema") final String schema,
    		@RequestBody(description = "Die Daten der zu erstellenden Räume ohne IDs, welche automatisch generiert wird", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Raum.class)))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogRaeume(conn).addMultiple(is),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
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
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
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
        	request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
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
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Aufsichtsbereich der Schule",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = Aufsichtsbereich.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Aufsichtsbereich bei der Schule gefunden")
    public Response getAufsichtsbereich(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogAufsichtsbereiche(conn).get(id),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Katalog-Daten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchAufsichtsbereich(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für den Aufsichtsbereich der Schule", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Aufsichtsbereich.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogAufsichtsbereiche(conn).patch(id, is),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
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
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Aufsichtsbereich.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Aufsichtsbereich für die Schule anzulegen.")
    @ApiResponse(responseCode = "404", description = "Die Katalogdaten wurden nicht gefunden")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addAufsichtsbereich(@PathParam("schema") final String schema,
    		@RequestBody(description = "Die Daten des zu erstellenden Aufsichtsbereichs ohne ID, welche automatisch generiert wird", required = true, content =
			   @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Aufsichtsbereich.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogAufsichtsbereiche(conn).add(is),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs "
    		    + "besitzt.")
    @ApiResponse(responseCode = "201", description = "Die Aufsichtsbereiche wurden erfolgreich hinzugefügt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            array = @ArraySchema(schema = @Schema(implementation = Aufsichtsbereich.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Aufsichtsbereiche für die Schule anzulegen.")
    @ApiResponse(responseCode = "404", description = "Die Katalogdaten wurden nicht gefunden")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addAufsichtsbereiche(@PathParam("schema") final String schema,
    		@RequestBody(description = "Die Daten der zu erstellenden Aufsichtsbereiche ohne ID, welche automatisch generiert wird", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Aufsichtsbereich.class)))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogAufsichtsbereiche(conn).addMultiple(is),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
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
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
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
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
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
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Pausenzeit der Schule",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = StundenplanPausenzeit.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Pausenzeit mit der angegebenen ID bei der Schule gefunden")
    public Response getPausenzeit(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogPausenzeiten(conn).get(id),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
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
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchPausenzeit(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für die Pausenzeit der Schule", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StundenplanPausenzeit.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogPausenzeiten(conn).patch(id, is),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs "
    		    + "besitzt.")
    @ApiResponse(responseCode = "201", description = "Die Pausenzeit wurde erfolgreich hinzugefügt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = StundenplanPausenzeit.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Pausenzeit für die Schule anzulegen.")
    @ApiResponse(responseCode = "404", description = "Die Katalogdaten wurden nicht gefunden")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addPausenzeit(@PathParam("schema") final String schema,
    		@RequestBody(description = "Die Daten der zu erstellenden Pausenzeit ohne ID, welche automatisch generiert wird", required = true, content =
			   @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StundenplanPausenzeit.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN)) {
    		return (new DataKatalogPausenzeiten(conn)).add(is);
    	}
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
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
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
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
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
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Zeitraster-Eintrag der Schule",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = StundenplanZeitraster.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Zeitraster-Eintrag bei der Schule gefunden")
    public Response getZeitrasterEintrag(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogZeitraster(conn).get(id),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN);
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Katalog-Daten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchZeitrasterEintrag(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für den Zeitraster-Eintrag der Schule", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StundenplanZeitraster.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogZeitraster(conn).patch(id, is),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs "
    		    + "besitzt.")
    @ApiResponse(responseCode = "201", description = "Der Zeitraster-Eintrag wurde erfolgreich hinzugefügt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = StundenplanZeitraster.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Zeitraster-Eintrag für die Schule anzulegen.")
    @ApiResponse(responseCode = "404", description = "Die Katalogdaten wurden nicht gefunden")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addZeitrasterEintrag(@PathParam("schema") final String schema,
    		@RequestBody(description = "Die Daten des zu erstellenden Zeitraster-Eintrags ohne ID, welche automatisch generiert wird", required = true, content =
			   @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StundenplanZeitraster.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogZeitraster(conn).add(is),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
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
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
    }

}
