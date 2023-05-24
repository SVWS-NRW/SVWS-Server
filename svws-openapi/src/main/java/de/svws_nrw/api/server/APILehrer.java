package de.svws_nrw.api.server;

import java.io.InputStream;

import de.svws_nrw.api.OpenAPIApplication;
import de.svws_nrw.core.data.lehrer.LehrerKatalogAbgangsgrundEintrag;
import de.svws_nrw.core.data.lehrer.LehrerKatalogAnrechnungsgrundEintrag;
import de.svws_nrw.core.data.lehrer.LehrerKatalogBeschaeftigungsartEintrag;
import de.svws_nrw.core.data.lehrer.LehrerKatalogEinsatzstatusEintrag;
import de.svws_nrw.core.data.lehrer.LehrerKatalogFachrichtungAnerkennungEintrag;
import de.svws_nrw.core.data.lehrer.LehrerKatalogFachrichtungEintrag;
import de.svws_nrw.core.data.lehrer.LehrerKatalogLehramtAnerkennungEintrag;
import de.svws_nrw.core.data.lehrer.LehrerKatalogLehramtEintrag;
import de.svws_nrw.core.data.lehrer.LehrerKatalogLehrbefaehigungAnerkennungEintrag;
import de.svws_nrw.core.data.lehrer.LehrerKatalogLehrbefaehigungEintrag;
import de.svws_nrw.core.data.lehrer.LehrerKatalogLeitungsfunktionenEintrag;
import de.svws_nrw.core.data.lehrer.LehrerKatalogMehrleistungsartEintrag;
import de.svws_nrw.core.data.lehrer.LehrerKatalogMinderleistungsartEintrag;
import de.svws_nrw.core.data.lehrer.LehrerKatalogRechtsverhaeltnisEintrag;
import de.svws_nrw.core.data.lehrer.LehrerKatalogZugangsgrundEintrag;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.core.data.lehrer.LehrerStammdaten;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
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
import de.svws_nrw.data.lehrer.DataLehrerPersonaldaten;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.data.lehrer.DataLehrerliste;
import de.svws_nrw.db.DBEntityManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


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
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerdaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Lehrer-Listen-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Lehrer-Einträge gefunden")
    public Response getLehrer(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.LEHRERDATEN_ANSEHEN)) {
    		return (new DataLehrerliste(conn)).getAll();
    	}
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Stammdaten des Lehrers",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = LehrerStammdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Lehrer-Eintrag mit der angegebenen ID gefunden")
    public Response getLehrerStammdaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		                                    @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.LEHRERDATEN_ANSEHEN)) {
    		return (new DataLehrerStammdaten(conn)).get(id);
    	}
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
    @Operation(summary = "Liefert zu der ID des Lehrers die zugehörigen Stammdaten.",
    description = "Passt die Lehrer-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrerdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Lehrer-Stammdaten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Lehrer-Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchLehrerStammdaten(
    		@PathParam("schema") final String schema,
    		@PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für die Lehrer-Stammdaten", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LehrerStammdaten.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.LEHRERDATEN_AENDERN)) {
    		return (new DataLehrerStammdaten(conn)).patch(id, is);
    	}
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerpersonaldaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Personaldaten des Lehrers",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = LehrerPersonaldaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Lehrer-Eintrag mit der angegebenen ID gefunden")
    public Response getLehrerPersonaldaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		                                          @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.LEHRERDATEN_DETAILDATEN_ANSEHEN)) {
    		return (new DataLehrerPersonaldaten(conn)).get(id);
    	}
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personaldaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Lehrer-Personaldaten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Lehrer-Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchLehrerPersonaldaten(
    		@PathParam("schema") final String schema,
    		@PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für die Lehrer-Personaldaten", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LehrerPersonaldaten.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.LEHRERDATEN_DETAILDATEN_AENDERN)) {
    		return (new DataLehrerPersonaldaten(conn)).patch(id, is);
    	}
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
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Lehrerleitungsfunktion-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerKatalogLeitungsfunktionenEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Lehrerleitungsfunktion-Katalog-Einträge gefunden")
    public Response getLehrerLeitungsfunktionen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KEINE)) {
            return (new DataKatalogLehrerLeitungsfunktionen()).getList();
        }
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
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerKatalogZugangsgrundEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Lehrerzugangsgrund-Katalog-Einträge gefunden")
    public Response getLehrerZugangsgruende(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KEINE);
    	return (new DataKatalogLehrerZugangsgruende()).getList();
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
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerKatalogAbgangsgrundEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Lehrerabgangsgrund-Katalog-Einträge gefunden")
    public Response getLehrerAbgangsgruende(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KEINE);
    	return (new DataKatalogLehrerAbgangsgruende()).getList();
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
    @ApiResponse(responseCode = "200", description = "Eine Liste von Beschäftigungsart-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerKatalogBeschaeftigungsartEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Beschäftigungsart-Katalog-Einträge gefunden")
    public Response getLehrerBeschaeftigungsarten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KEINE);
    	return (new DataKatalogLehrerBeschaeftigungsarten()).getList();
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
    @ApiResponse(responseCode = "200", description = "Eine Liste von Einsatzstatus-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerKatalogEinsatzstatusEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Einsatzstatus-Katalog-Einträge gefunden")
    public Response getLehrerEinsatzstatus(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KEINE);
    	return (new DataKatalogLehrerEinsatzstatus()).getList();
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
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerKatalogFachrichtungEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Fachrichtungs-Katalog-Einträge gefunden")
    public Response getLehrerFachrichtungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KEINE);
    	return (new DataKatalogLehrerFachrichtungen()).getList();
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
    @ApiResponse(responseCode = "200", description = "Eine Liste von Anerkennungs-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerKatalogFachrichtungAnerkennungEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Anerkennungs-Katalog-Einträge gefunden")
    public Response getLehrerFachrichtungAnerkennungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KEINE);
    	return (new DataKatalogLehrerFachrichtungAnerkennungen()).getList();
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
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerKatalogLehramtEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Lehramt-Katalog-Einträge gefunden")
    public Response getLehrerLehraemter(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KEINE);
    	return (new DataKatalogLehrerLehraemter()).getList();
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
    @ApiResponse(responseCode = "200", description = "Eine Liste von Anerkennungs-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerKatalogLehramtAnerkennungEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Anerkennungs-Katalog-Einträge gefunden")
    public Response getLehrerLehramtAnerkennungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KEINE);
    	return (new DataKatalogLehrerLehramtAnerkennungen()).getList();
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
    @ApiResponse(responseCode = "200", description = "Eine Liste von Lehrbefähigung-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerKatalogLehrbefaehigungEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Lehrbefähigung-Katalog-Einträge gefunden")
    public Response getLehrerLehrbefaehigungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KEINE);
    	return (new DataKatalogLehrerLehrbefaehigungen()).getList();
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
    @ApiResponse(responseCode = "200", description = "Eine Liste von Einsatzstatus-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerKatalogLehrbefaehigungAnerkennungEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Anerkennungs-Katalog-Einträge gefunden")
    public Response getLehrerLehrbefaehigungenAnerkennungen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KEINE);
    	return (new DataKatalogLehrerLehrbefaehigungAnerkennungen()).getList();
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
    @ApiResponse(responseCode = "200", description = "Eine Liste von Mehrleistungsart-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerKatalogMehrleistungsartEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Mehrleistungsart-Katalog-Einträge gefunden")
    public Response getLehrerMehrleistungsarten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KEINE);
    	return (new DataKatalogLehrerMehrleistungsarten()).getList();
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
    @ApiResponse(responseCode = "200", description = "Eine Liste von Minderleistungsart-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerKatalogMinderleistungsartEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Minderleistungsart-Katalog-Einträge gefunden")
    public Response getLehrerMinderleistungsarten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KEINE);
    	return (new DataKatalogLehrerMinderleistungsarten()).getList();
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
    @ApiResponse(responseCode = "200", description = "Eine Liste von Rechtsverhältnis-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerKatalogRechtsverhaeltnisEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Rechtsverhältnis-Katalog-Einträge gefunden")
    public Response getLehrerRechtsverhaeltnisse(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KEINE);
    	return (new DataKatalogLehrerRechtsverhaeltnis()).getList();
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
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LehrerKatalogAnrechnungsgrundEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getLehrerAnrechnungsgruende(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KEINE);
    	return (new DataKatalogLehrerAnrechnungsgruende()).getList();
    }

}
