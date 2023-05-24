package de.svws_nrw.api.server;

import java.io.InputStream;

import de.svws_nrw.api.OpenAPIApplication;
import de.svws_nrw.core.data.betrieb.BetriebStammdaten;
import de.svws_nrw.core.data.erzieher.ErzieherStammdaten;
import de.svws_nrw.core.data.kataloge.KatalogEintrag;
import de.svws_nrw.core.data.schueler.SchuelerBetriebsdaten;
import de.svws_nrw.core.data.schueler.SchuelerKAoADaten;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittListeEintrag;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.data.schueler.UebergangsempfehlungKatalogEintrag;
import de.svws_nrw.core.data.schule.HerkunftKatalogEintrag;
import de.svws_nrw.core.data.schule.HerkunftsartKatalogEintrag;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.betriebe.DataBetriebsStammdaten;
import de.svws_nrw.data.erzieher.DataErzieherStammdaten;
import de.svws_nrw.data.schueler.DataKatalogHerkuenfte;
import de.svws_nrw.data.schueler.DataKatalogHerkunftsarten;
import de.svws_nrw.data.schueler.DataKatalogSchuelerFahrschuelerarten;
import de.svws_nrw.data.schueler.DataKatalogUebergangsempfehlung;
import de.svws_nrw.data.schueler.DataSchuelerBetriebsdaten;
import de.svws_nrw.data.schueler.DataSchuelerKAoADaten;
import de.svws_nrw.data.schueler.DataSchuelerLernabschnittsdaten;
import de.svws_nrw.data.schueler.DataSchuelerLernabschnittsliste;
import de.svws_nrw.data.schueler.DataSchuelerSchulbesuchsdaten;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.data.schueler.DataSchuelerliste;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Schülerdaten aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/schueler/...
 */
@Path("/db/{schema}/schueler")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APISchueler {

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
               		       + "Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Schüler-Listen-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Schüler-Einträge gefunden")
    public Response getSchuelerAktuell(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)) {
    		return (new DataSchuelerliste(conn, null).getAll());
    	}
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
               		       + "Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Schüler-Listen-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Schüler-Einträge gefunden")
    public Response getSchuelerFuerAbschnitt(@PathParam("schema") final String schema, @PathParam("abschnitt") final long abschnitt, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)) {
    		return (new DataSchuelerliste(conn, abschnitt).getAll());
    	}
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Stammdaten des Schülers",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = SchuelerStammdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
    public Response getSchuelerStammdaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		                                        @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)) {
    		return (new DataSchuelerStammdaten(conn).get(id));
    	}
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schülerdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Schülerstammdaten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchSchuelerStammdaten(
    		@PathParam("schema") final String schema,
    		@PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für die Schüler-Stammdaten", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuelerStammdaten.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN)) {
    		return (new DataSchuelerStammdaten(conn).patch(id, is));
    	}
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Schulbesuchsdaten des Schülers",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = SchuelerSchulbesuchsdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
    public Response getSchuelerSchulbesuch(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		                                                      @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)) {
    		return (new DataSchuelerSchulbesuchsdaten(conn).get(id));
    	}
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schülerdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Schüler-Schulbesuchsdaten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchSchuelerSchulbesuch(
    		@PathParam("schema") final String schema,
    		@PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für die Schüler-Schulbesuchsdaten", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuelerSchulbesuchsdaten.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN)) {
    		return (new DataSchuelerSchulbesuchsdaten(conn).patch(id, is));
    	}
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
    description = "Liest eine Lister der Lernabschnitte des Schülers zu der angegebenen ID aus der Datenbank "
    		    + "und liefert diese zurück."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Schüler-Lernabschnitt-Listeneinträgen",
    			 content = @Content(mediaType = "application/json",
    			 array = @ArraySchema(schema = @Schema(implementation = SchuelerLernabschnittListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
    public Response getSchuelerLernabschnittsliste(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)) {
    		return (new DataSchuelerLernabschnittsliste(conn).get(id));
    	}
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
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Lernabschnittsdaten des Schülers",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = SchuelerLernabschnittsdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Schüler-Eintrag mit der angegebenen ID gefunden")
    public Response getSchuelerLernabschnittsdaten(@PathParam("schema") final String schema, @PathParam("id") final long id, @PathParam("abschnitt") final long abschnitt,
    		                                        @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, 
    			BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN)) {
    		return (new DataSchuelerLernabschnittsdaten(conn).get(id, abschnitt));
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Lernabschnittsdaten eines Schülers.
     *
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abschnitt  die id des Schülerlernabschnitts
     * @param request    die Informationen zur HTTP-Anfrage
     *
     * @return die Lernabschnittsdaten des Schülers
     */
    @GET
    @Path("/lernabschnittsdaten/{abschnitt : \\d+}")
    @Operation(summary = "Liefert zu der ID des Schülerlernabschnittes die zugehörigen Lernabschnittsdaten.",
    description = "Liest die Schüler-Lernabschnittsdaten zu der angegebenen ID aus der Datenbank "
    		    + "und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Lernabschnittsdaten des Schülers",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = SchuelerLernabschnittsdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag mit Schüler-Lernabschnittsdaten mit der angegebenen ID gefunden")
    public Response getSchuelerLernabschnittsdatenByID(@PathParam("schema") final String schema, @PathParam("abschnitt") final long abschnitt,
    		                                        @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN)) {
    		return (new DataSchuelerLernabschnittsdaten(conn).get(abschnitt));
    	}
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
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KEINE)) {
    		return (new DataKatalogSchuelerFahrschuelerarten(conn).getList());
    	}
    }


	/**
     * Die OpenAPI-Methode für die Abfrage der Liste aller Erzieher.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * @param id		die ID des Schülers, dessen Erzieher zurückegeben werden.
     * @return die Liste mit den einzelnen Erziehern
     */
    @GET
    @Path("/{id : \\d+}/erzieher")
    @Operation(summary = "Gibt die Stammdaten der Erzieher zurück.",
               description = "Erstellt eine Liste aller in der Datenbank vorhandenen Erzieher unter Angabe der Schüler-ID"
               		       + "des Vor- und Nachnamens, Erzieherart, Kontaktdaten, ob sie in der Anwendung "
               		       + "sichtbar bzw. änderbar sein sollen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Erzieherdaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Erzieherstammdaten",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ErzieherStammdaten.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Erzieherdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Erzieher-Einträge gefunden")
    public Response getSchuelerErzieher(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)) {
    		return (new DataErzieherStammdaten(conn)).getListFromSchueler(id);
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Liste aller Betriebe.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * @param id		die ID des Schülers, dessen Betriebe zurückegeben werden.
     * @return die Liste mit den einzelnen Betrieben
     */
    @GET
    @Path("/{id : \\d+}/betriebe")
    @Operation(summary = "Gibt die Betriebe eines Schülers zurück.",
               description = "Erstellt eine Liste aller in der Datenbank vorhandenen Betriebe unter Angabe der Schüler-ID"
               		       + "des Vor- und Nachnamens, Erzieherart, Kontaktdaten, ob sie in der Anwendung "
               		       + "sichtbar bzw. änderbar sein sollen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Betriebsdaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Schülerbetrieben",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerBetriebsdaten.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Erzieherdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Erzieher-Einträge gefunden")
    public Response getSchuelerBetriebe(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)) {
    		return (new DataSchuelerBetriebsdaten(conn)).getListFromSchueler(id);
        }
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Liste aller Betriebsstammdaten eines Schülers.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     * @param id        die ID des Schülers
     * @return die Liste der Betriebsstammdaten eines Schülers
     */
    @GET
    @Path("/{id : \\d+}/betriebsstammdaten")
    @Operation(summary = "Gibt eine Liste der Betriebsstammdaten eines Schülers zurück.",
               description = "Erstellt eine Liste aller in der Datenbank vorhandenen Betriebe eines Schülers unter Angabe der ID," // TOD0 Beschreibung anpassen.
               		       + "ob sie in der Anwendung "
               		       + "sichtbar bzw. änderbar sein sollen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Betriebsdaten des Schülers "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von von Betriebsstammdaten eines Schülers",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = BetriebStammdaten.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Betriebdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Betrieb-Einträge gefunden")
    public Response getSchuelerBetriebsstammdaten(@PathParam("schema") final String schema,  @PathParam("id") final long id, @Context final HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)) {
            return (new DataBetriebsStammdaten(conn)).getSchuelerBetriebe(id);
        }
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
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UebergangsempfehlungKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogUebergangsempfehlung(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KEINE)) {
            return (new DataKatalogUebergangsempfehlung()).getList();
        }
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
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KEINE)) {
            return (new DataKatalogHerkuenfte()).getList();
        }
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
        try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KEINE)) {
            return (new DataKatalogHerkunftsarten()).getList();
        }
    }


	/**
	 * Die OpenAPI-Methode für die Abfrage der KAOADaten eines Schülers.
	 *
	 * @param schema  das Datenbankschema, auf welches die Abfrage ausgeführt werden
	 *                soll
	 * @param id      die Datenbank-ID zur Identifikation des Schülers
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return die KAOADaten des Schülers
	 */
	@GET
	@Path("/{id : \\d+}/kaoa")
	@Operation(summary = "Liefert zu der ID des Schülers die zugehörigen KAOADaten.", description = "Liest die KAOADaten des Schülers zu der angegebenen ID aus der Datenbank "
			+ "und liefert diese zurück. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten "
			+ "besitzt.")
	@ApiResponse(responseCode = "200", description = "Die KAOADaten des Schülers", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchuelerKAoADaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-KAoA-Eintrag mit der angegebenen ID gefunden")
	public Response getKAOAdaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)) {
			return (new DataSchuelerKAoADaten(conn).getBySchuelerIDAsResponse(id));
		}
	}

	/**
	 *
	 * Erzeugt einen SchuelerKAoADaten-Eintrag und gibt diesen zurück
	 *
	 * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                   werden soll
	 * @param schuelerid die Schueler-ID
	 * @param daten      die neuen Daten
	 * @param request    die Informationen zur HTTP-Anfrage
	 *
	 * @return HTTP_200 und die angelegten SchuelerKAoADaten, wenn erfolgreich. <br>
	 *         HTTP_400, wenn Fehler bei der Validierung auftreten HTTP_403 bei
	 *         fehlender Berechtigung,<br>
	 *         HTTP_404, wenn der Eintrag nicht gefunden wurde
	 *
	 */
	@POST
	@Path("/{id : \\d+}/kaoa")
	@Operation(summary = "Erstellt einen neuen SchuelerKAoADaten Eintrag", description = "Erstellt einen neuen SchuelerKAoADaten Eintrag"
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von SchülerKAoADaten "
			+ "besitzt.")
	@ApiResponse(responseCode = "200", description = "Die KAOADaten des Schülers", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchuelerKAoADaten.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzulegen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-KAoA-Eintrag mit der angegebenen ID gefunden")
	public Response createKAOAdaten(@PathParam("schema") final String schema,
			@PathParam("id") final long schuelerid,
			@RequestBody(description = "Die KAoa Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuelerKAoADaten.class))) final SchuelerKAoADaten daten,

			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_KAOA_DATEN_AENDERN)) {
			return (new DataSchuelerKAoADaten(conn).createBySchuelerIDAsResponse(schuelerid, daten));
		}
	}

	/**
	 * Ändert einen SchuelerKAoADaten-Eintrag anhand seiner ID
	 *
	 * @param schema         das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                       werden soll
	 * @param schuelerID     die Schueler-ID
	 * @param schuelerKAoAID die Datenbank-ID der Schüler-KAoA-Daten
	 * @param daten          die neuen Daten
	 * @param request        die Informationen zur HTTP-Anfrage
	 *
	 * @return HTTP_200, wenn erfolgreich. <br>
	 *         HTTP_400, wenn Fehler bei der Validierung auftreten HTTP_403 bei
	 *         fehlender Berechtigung,<br>
	 *         HTTP_404, wenn der Eintrag nicht gefunden wurde
	 *
	 */
	@PUT
	@Path("/{id : \\d+}/kaoa/{skid : \\d+}")
	@Operation(summary = "Ändert einen SchuelerKAoADaten Eintrag", description = "Ändert einen SchuelerKAoADaten Eintrag"
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von SchülerKAoADaten "
			+ "besitzt.")
	@ApiResponse(responseCode = "200", description = "Die KAOADaten des Schülers")
	@ApiResponse(responseCode = "400", description = "Fehler bei der Datenvalidierung")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzulegen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-KAoA-Eintrag mit der angegebenen ID gefunden")
	public Response putKAOAdaten(@PathParam("schema") final String schema,
			@PathParam("id") final long schuelerID,
			@PathParam("skid") final long schuelerKAoAID,
			@RequestBody(description = "Die KAoa Daten", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuelerKAoADaten.class))) final SchuelerKAoADaten daten,

			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_KAOA_DATEN_AENDERN)) {
			return (new DataSchuelerKAoADaten(conn).putBySchuelerIDAsResponse(schuelerID, daten, schuelerKAoAID));
		}
	}

	/**
	 * Löscht ein SchuelerKAoADaten-Eintrag anhand dessen Id
	 *
	 * @param schema         das Datenbankschema, auf welches die Abfrage ausgeführt
	 *                       werden soll
	 * @param schuelerID     die Schueler-ID
	 * @param schuelerKAoAID die Datenbank-ID der Schüler-KAoA-Daten
	 * @param request        die Informationen zur HTTP-Anfrage
	 *
	 * @return HTTP_204, wenn erfolgreich. <br>
	 *         HTTP_403 bei fehlender Berechtigung,<br>
	 *         HTTP_404, wenn der Eintrag nicht gefunden wurde
	 */
	@DELETE
	@Path("/{id : \\d+}/kaoa/{skid : \\d+}")
	@Operation(summary = "Löscht einen SchuelerKAoADaten Eintrag", description = "Löscht einen SchuelerKAoADaten Eintrag"
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von SchülerKAoADaten "
			+ "besitzt.")
	@ApiResponse(responseCode = "204", description = "Die KAOADaten des Schülers wurden gelöscht")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzulegen.")
	@ApiResponse(responseCode = "404", description = "Kein Schüler-KAoA-Eintrag mit der angegebenen ID gefunden")
	public Response deleteKAOAdaten(@PathParam("schema") final String schema,
			@PathParam("id") final long schuelerID,
			@PathParam("skid") final long schuelerKAoAID,
			@Context final HttpServletRequest request) {
		try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request,
				BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_KAOA_DATEN_AENDERN)) {
			return (new DataSchuelerKAoADaten(conn).deleteBySchuelerKAoAIDAsResponse(schuelerKAoAID));
		}
	}

}
