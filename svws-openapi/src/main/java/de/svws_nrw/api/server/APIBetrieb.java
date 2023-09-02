package de.svws_nrw.api.server;

import java.io.InputStream;
import java.util.List;

import de.svws_nrw.api.OpenAPIApplication;
import de.svws_nrw.core.data.betrieb.BetriebAnsprechpartner;
import de.svws_nrw.core.data.betrieb.BetriebListeEintrag;
import de.svws_nrw.core.data.betrieb.BetriebStammdaten;
import de.svws_nrw.core.data.kataloge.KatalogEintrag;
import de.svws_nrw.core.data.schueler.SchuelerBetriebsdaten;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.betriebe.DataBetriebAnsprechpartner;
import de.svws_nrw.data.betriebe.DataBetriebsStammdaten;
import de.svws_nrw.data.betriebe.DataBetriebsliste;
import de.svws_nrw.data.betriebe.DataKatalogBeschaeftigunsarten;
import de.svws_nrw.data.betriebe.DataKatalogBetriebsarten;
import de.svws_nrw.data.schueler.DataSchuelerBetriebsdaten;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Betriebsdaten aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/betrieb/...
 */
@Path("/db/{schema}/betriebe")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIBetrieb {

    /**
     * Die OpenAPI-Methode für die Abfrage der Liste aller Betriebe.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die Liste mit den einzelnen Betrieben
     */
    @GET
    @Path("/")
    @Operation(summary = "Gibt eine Übersicht von allen Betrieben zurück.",
               description = "Erstellt eine Liste aller in der Datenbank vorhandenen Betriebe unter Angabe der ID, der Betriebsart , " // TOD0 Beschreibung anpassen.
               		       + "des Betriebnamens, Kontaktdaten, ob sie in der Anwendung "
               		       + "sichtbar bzw. änderbar sein sollen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Betriebsdaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Betrieb-Listen-Einträgen",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = BetriebListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Betriebdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Betrieb-Einträge gefunden")
    public Response getBetriebe(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataBetriebsliste(conn).getList(),
        	request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }

   /**
     * Die OpenAPI-Methode für die Abfrage der Stammdaten eines Betriebs.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation des Betriebs
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die Stammdaten eines Betriebs
     */

    @GET
    @Path("/{id : \\d+}/stammdaten")
    @Operation(summary = "Liefert zu der ID des Betriebs die zugehörigen Stammdaten.",
    description = "Liest die Stammdaten des Betriebs zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Stammdaten eines Betriebs",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON,
                 schema = @Schema(implementation = BetriebStammdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Betriebsdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Betrieb-Eintrag mit der angegebenen ID gefunden")
    public Response getBetriebStammdaten(@PathParam ("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataBetriebsStammdaten(conn).get(id),
        	request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
    }

    /**
     * Die OpenAPI-Methode für das Erstellen einer neuen Betriebes.
     *
     * @param schema       das Datenbankschema, in welchem der Betriebsansprechpartner erstellt wird
     * @param is           das JSON-Objekt
      * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit der neuen Betriebsart
     */
    @POST
    @Path("/new")
    @Operation(summary = "Erstellt einen neuen Betrieb und gibt den neuen Datensatz zurück.",
    description = "Erstellt einen neuen Betrieb und gibt den neuen Datensatz zurück."
                + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Betriebes "
                + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Betieb wurde erfolgreich angelegt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = BetriebStammdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Betrieb anzulegen.")
    @ApiResponse(responseCode = "404", description = "Keine Betriebart oder kein Ort  mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response createBetrieb(
            @PathParam("schema") final String schema,
            @RequestBody(description = "Der Post für die Betrieb-Daten", required = true, content =
            @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BetriebStammdaten.class))) final InputStream is,
            @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataBetriebsStammdaten(conn).create(is),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
    }



    /**
     * Die OpenAPI-Methode für das Patchen der Stammdaten eines Betriebs.
     *
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation des Erziehers
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/{id : \\d+}/stammdaten")
    @Operation(summary = "Passt die zu der ID des Betriebs zugehörigen Stammdaten an.",
    description = "Passt die Betrieb-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Erzieherdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Betrieb-Stammdaten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Betriebdaten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Betrieb-Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")

    public Response patchBetriebStammdaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
                                        @RequestBody(description = "Der Patch für die Betrieb-Stammdaten", required = true,
                                        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BetriebStammdaten.class))) final
                                        InputStream is, @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataBetriebsStammdaten(conn).patch(id, is),
        	request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
    }

    /**
     * Die OpenAPI-Methode zum Löschen von einer oder mehreren Betriebsansprechpartner
     *
     * @param schema    das Datenbankschema
     * @param bids      die IDs des Benutzer
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @DELETE
    @Path("/remove")
    @Operation(summary = "Löscht einen oder mehrere Betriebe.", description = "Löscht einen oder mehrere Betriebe."
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Betriebe wurden erfolgreich gelöscht.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Betriebe zu löschen.")
    @ApiResponse(responseCode = "404", description = "Benötigte Information zum Betrieb wurden nicht in der DB gefunden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response removeBetrieb(@PathParam("schema") final String schema,
            @RequestBody(description = "Die IDs der Betriebe", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> bids,
            @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataBetriebsStammdaten(conn).remove(bids),
        	request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
    }

    /**
     * Die OpenAPI-Methode für das Erstellen eines neuen Schülerbetriebs.
     *
     * @param schema     		 	das Datenbankschema, in welchem der Schülerbetrieb erstellt wird
     * @param request      			die Informationen zur HTTP-Anfrage
     * @param schueler_id		    die Datanbenk_ID des Schülers des neuen Schülebetriebs
     * @param betrieb_id			die Datenbank_ID  des Betriebes des neuen Schülerbetriebs.
     * @param is							JSON-Objekt mit den Daten
     * @return die HTTP-Antwort mit der neuen Blockung
     */
    @POST
    @Path("/schuelerbetrieb/new/schueler/{schueler_id : \\d+}/betrieb/{betrieb_id: \\d+}")
    @Operation(summary = "Erstellt einen neuen Schülerbetrieb und gibt ihn zurück.",
    description = "Erstellt einen neuen Schülerbetrieb und gibt ihn zurück."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Schülerbetriebs "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Schülerbetrieb wurde erfolgreich angelegt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = SchuelerBetriebsdaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Schülerbetrieb anzulegen.")
    @ApiResponse(responseCode = "404", description = "Kein Betrieb  mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response createSchuelerbetrieb(
    		@PathParam("schema") final String schema,
    		@PathParam("schueler_id") final long schueler_id,
    		@PathParam("betrieb_id") final long betrieb_id,
    		@RequestBody(description = "Der Post für die Schülerbetrieb-Daten", required = true, content =
			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuelerBetriebsdaten.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataSchuelerBetriebsdaten(conn).create(schueler_id, betrieb_id, is),
    		request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
    }



    /**
     * Die OpenAPI-Methode für das Patchen der Stammdaten eines Schülerbetriebs.
     *
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation des Schülerbetriebs
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/{id : \\d+}/betrieb")
    @Operation(summary = "Passt die Schüler-Betriebsdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank.",
    description = "Passt die Schüler-Betriebsdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schülerbetreibsdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Schüler-Betriebsdaten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerdaten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Schülerbetrieb-Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchSchuelerBetriebsdaten(
    		@PathParam("schema") final String schema,
    		@PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für die Schüler-Schulbesuchsdaten", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchuelerBetriebsdaten.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataSchuelerBetriebsdaten(conn).patch(id, is),
    		request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
    }

     /**
     * Die OpenAPI-Methode für die Abfrage eines Schülerbetriebs im angegebenen Schema.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * @param id        die Datenbank-ID zur Identifikation des Schülerbetriebs
     * @return              der Schülerbetrieb mit ID des Datenbankschemas
     */
    @GET
    @Path("/{id : \\d+}/betrieb")
    @Operation(summary = "Liefert zu der ID des Schülerbetriebs die zugehörigen Daten..",
               description = "Liest die Daten des Schülerbetriebs zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
                            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen vom Schülerbetrieb"
                            + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Stammdaten des Schülerbetriebs.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SchuelerBetriebsdaten.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schülerbetreibe anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Schülerbetrieb gefunden")
    public Response getSchuelerBetriebsdaten(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataSchuelerBetriebsdaten(conn).get(id),
    		request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
    }



    /**
     * Die OpenAPI-Methode für die Abfrage der Liste aller Betriebe.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param betrieb_id   die ID des Betriebs
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die Liste mit den einzelnen Betrieben
     */
    @GET
    @Path("/{id : \\d+}betriebansprechpartnerliste")
    @Operation(summary = "Gibt eine Übersicht von allen Betriebansprechpartnern zurück.",
               description = "Erstellt eine Liste aller in der Datenbank vorhandenen Betriebansprechpartner , "
                           + "des Ansprechpartnername, Kontaktdaten, ob sie in der Anwendung "
                           + "sichtbar bzw. änderbar sein sollen. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Betriebsansprechpartnern "
                           + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Betriebansprechpartnern",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = BetriebAnsprechpartner.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Betriebdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Betrieb-Einträge gefunden")
    public Response getBetriebAnsprechpartner(@PathParam("schema") final String schema,  @PathParam("id") final long betrieb_id, @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataBetriebAnsprechpartner(conn).getBetriebansprechpartner(betrieb_id),
        	request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Liste aller Betriebe.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die Liste mit den einzelnen Betrieben
     */
    @GET
    @Path("/betriebansprechpartner")
    @Operation(summary = "Gibt eine Übersicht von allen Betriebansprechpartnern zurück.",
               description = "Erstellt eine Liste aller in der Datenbank vorhandenen Betriebansprechpartner , "
               		       + "des Ansprechpartnername, Kontaktdaten, ob sie in der Anwendung "
               		       + "sichtbar bzw. änderbar sein sollen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Betriebsansprechpartnern "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Betriebansprechpartnern",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = BetriebAnsprechpartner.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Betriebdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Betrieb-Einträge gefunden")
    public Response getBetriebeAnsprechpartner(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataBetriebAnsprechpartner(conn).getList(),
        	request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
    }

    /**
     * Die OpenAPI-Methode für die Abfrage eines Betriebanpsrechpartners im angegebenen Schema.
     *
     * @param schema       das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request      	die Informationen zur HTTP-Anfrage
     * @param id        			die Datenbank-ID zur Identifikation des Schülerbetriebs
     * @return              		der Betriebansprechpartner mit ID des Datenbankschemas
     */
    @GET
    @Path("/{id : \\d+}/betriebansprechpartner")
    @Operation(summary = "Liefert zu der ID des  Betriebanpsrechpartners die zugehörigen Daten..",
               description = "Liest die Daten des Betriebanpsrechpartners zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
                            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen vom Betriebanpsrechpartner"
                            + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Stammdaten des Betriebanpsrechpartners.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BetriebAnsprechpartner.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Betriebanpsrechpartner anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Betriebanpsrechpartner gefunden")
    public Response getBetriebAnsprechpartnerdaten(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataBetriebAnsprechpartner(conn).get(id),
    		request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
    }

    /**
     * Die OpenAPI-Methode für das Patchen der Stammdaten eines Schülerbetriebs.
     *
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation des Schülerbetriebs
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/{id : \\d+}/betriebansprechpartner")
    @Operation(summary = "Passt die Betriebanpsrechpartner-Daten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank.",
    description = "Passt die Betriebanpsrechpartner-Daten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern vom Betriebanpsrechpartner "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Betriebanpsrechpartner-Daten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Betriebanpsrechpartner-Datenn zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Betriebanpsrechpartner-Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchBetriebanpsrechpartnerdaten(
    		@PathParam("schema") final String schema,
    		@PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für die Betriebanpsrechpartner-Daten", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BetriebAnsprechpartner.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataBetriebAnsprechpartner(conn).patch(id, is),
    		request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
    }

    /**
     * Die OpenAPI-Methode für das Erstellen eines neuen Betriebansprechpartners.
     *
     * @param schema       das Datenbankschema, in welchem der Betriebsansprechpartner erstellt wird
     * @param betrieb_id   des Betrieb
     * @param request      die Informationen zur HTTP-Anfrage
     * @param is           das JSON-Objekt
     * @return die HTTP-Antwort mit der neuen Blockung
     */
    @POST
    @Path("/{betrieb_id : \\d+}/ansprechpartner/new")
    @Operation(summary = "Erstellt einen neuen Betriebansprechpartner und gibt die dazugehörige ID zurück.",
    description = "Erstellt einen neuen Betriebansprechpartner und gibt die dazugehörige ID zurück."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Betriebansprechpartners "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Ansprechpartner wurde erfolgreich angelegt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = BetriebAnsprechpartner.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um einen Ansprechpartner anzulegen.")
    @ApiResponse(responseCode = "404", description = "Kein Betrieb  mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response createBetriebansprechpartner(
    		@PathParam("schema") final String schema, @PathParam("betrieb_id") final long betrieb_id,
    		@RequestBody(description = "Der Post für die Betriebanpsrechpartner-Daten", required = true, content =
			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BetriebAnsprechpartner.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataBetriebAnsprechpartner(conn).create(betrieb_id, is),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
    }


    /**
     * Die OpenAPI-Methode zum Löschen von einer oder mehreren Betriebsansprechpartner
     *
     * @param schema    das Datenbankschema
     * @param bids      die IDs des Benutzer
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort
     */
    @DELETE
    @Path("/betriebansprechpartner/remove")
    @Operation(summary = "Löscht einen oder mehrere Benutzer.", description = "Löscht einen oder mehrere Betriebsansprechpartner."
            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Betriebsansprechpartner wurden erfolgreich gelöscht.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Betriebsansprechpartner zu löschen.")
    @ApiResponse(responseCode = "404", description = "Benötigte Information zum Betriebsansprechpartner wurden nicht in der DB gefunden.")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response removeBetriebansprechpartner(
            @PathParam("schema") final String schema,
            @RequestBody(description = "Die IDs der Betriebsansprechpartner", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = Long.class)))) final List<Long> bids,
            @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataBetriebAnsprechpartner(conn).remove(bids),
        	request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
    }


     /**
     * Die OpenAPI-Methode für die Abfrage der Liste der Beschäftigungsarten im angegebenen Schema.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Liste der Beschäftigungsarten mit ID des Datenbankschemas
     */
    @GET
    @Path("/beschaeftigungsart")
    @Operation(summary = "Gibt eine Übersicht aller Beschäftigungsarten im Katalog zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhandenen Beschäftigungsarten unter Angabe der ID, eines Kürzels und der "
                           + "textuellen Beschreibung sowie der Information, ob der Eintrag in der Anwendung sichtbar bzw. änderbar sein soll, und"
                           + "gibt diese zurück. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen "
                           + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen zu den Beschäftigungsarten.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogBeschaeftigungsart(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataKatalogBeschaeftigunsarten(conn).getList(),
        	request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }

    /**
     * Die OpenAPI-Methode für die Abfrage einer Beschäftigungsart im angegebenen Schema.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * @param id            die Datenbank-ID zur Identifikation der Beschäftigungsart
     * @return              die Liste der Beschäftigungsarten mit ID des Datenbankschemas
     */
    @GET
    @Path("/beschaeftigungsart/{id : \\d+}")
    @Operation(summary = "Liefert zu der ID der Beshäftigungsart die zugehörigen Daten..",
               description = "Liest die Daten der Beschäftigunsart zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
                            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Beschäftigungsart"
                            + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Katalog-Eintrag zu den Beschäftigungsarten.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogBeschaeftigungsartmitID(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataKatalogBeschaeftigunsarten(conn).get(id),
        	request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }

    /**
     * Die OpenAPI-Methode für das Erstellen einer neuen Beschäftigungsart.
     *
     * @param schema       das Datenbankschema, in welchem die Beschäftigungsart erstellt wird
     * @param request      die Informationen zur HTTP-Anfrage
     * @param is           das JSON-Objekt
     * @return die HTTP-Antwort mit der neuen Beschäftigungsart
     */
    @POST
    @Path("/beschaeftigungsart/new")
    @Operation(summary = "Erstellt eine neue Beschäftigungsart und gibt sie zurück.",
    description = "Erstellt eine neue Beschäftigungsart und gibt sie zurück."
                + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Beschäftigungsart "
                + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Beschäftigungsart wurde erfolgreich angelegt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = KatalogEintrag.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Beschäftigungsart anzulegen.")
    @ApiResponse(responseCode = "409", description = "Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response createBeschaeftigungsArt(
            @PathParam("schema") final String schema,
            @RequestBody(description = "Der Post für die Beschäftigungsart-Daten", required = true, content =
            @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = KatalogEintrag.class))) final InputStream is,
            @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataKatalogBeschaeftigunsarten(conn).create(is),
        	request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
    }


     /**
     * Die OpenAPI-Methode für das Patchen einer Beschäftigungsart im angegebenen Schema
     *
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation der Beschäftigungsart
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/beschaeftigungsart/{id : \\d+}")
    @Operation(summary = "Passt die zu der ID der Beschäftigungsart zugehörigen Stammdaten an.",
    description = "Passt die Beschäftigungsart-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
                + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Beschäftigungsart "
                + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Beschäftigungsart-Daten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Beschäftigungsart-Daten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Keine Beschäftigungsart mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")

    public Response patchBeschaeftigungsart(@PathParam("schema") final String schema, @PathParam("id") final long id,
                                        @RequestBody(description = "Der Patch für die Betrieb-Stammdaten", required = true,
                                        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = KatalogEintrag.class))) final
                                        InputStream is, @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataKatalogBeschaeftigunsarten(conn).patch(id, is),
        	request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
    }

    /**
     * Die OpenAPI-Methode für die Abfrage der Liste der Beschäftigungsarten im angegebenen Schema.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Liste der Beschäftigungsarten mit ID des Datenbankschemas
     */
    @GET
    @Path("/betriebsart")
    @Operation(summary = "Gibt eine Übersicht aller Betriebsarten im Katalog zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhandenen Betriebsarten unter Angabe der ID, eines Kürzels und der "
                           + "textuellen Beschreibung sowie der Information, ob der Eintrag in der Anwendung sichtbar bzw. änderbar sein soll, und"
                           + "gibt diese zurück. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen "
                           + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen zu den Betriebsarten.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogBetriebsart(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataKatalogBetriebsarten(conn).getList(),
        	request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }

    /**
     * Die OpenAPI-Methode für die Abfrage einer Betriebsart im angegebenen Schema.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * @param id        die Datenbank-ID zur Identifikation der Betriebsart
     * @return              die Betriebsart mit ID des Datenbankschemas
     */
    @GET
    @Path("/beschaeftigungsart/{id : \\d+}")
    @Operation(summary = "Liefert zu der ID der Betriebsart die zugehörigen Daten..",
               description = "Liest die Daten der Betriebsart zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
                            + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Betriebsarten"
                            + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Katalog-Eintrag zu den Betriebsarten.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogBetriebsartmitID(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataKatalogBetriebsarten(conn).get(id),
        	request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }


    /**
     * Die OpenAPI-Methode für das Erstellen einer neuen Betriebsart.
     *
     * @param schema       das Datenbankschema, in welchem der Betriebsansprechpartner erstellt wird
     * @param is           das JSON-Objekt
      * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit der neuen Betriebsart
     */
    @POST
    @Path("/betriebsart/new")
    @Operation(summary = "Erstellt einen neue Betriebsart und gibt den neuen Datensatz zurück.",
    description = "Erstellt eine neue Betriebart und gibt den neuen Datensatz zurück."
                + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eine Betriebsart "
                + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Betiebsart wurde erfolgreich angelegt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = KatalogEintrag.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Betriebsart anzulegen.")
    @ApiResponse(responseCode = "404", description = "Kein Betrieb  mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response createBetriebsart(
            @PathParam("schema") final String schema,
            @RequestBody(description = "Der Post für die Betriebanpsrechpartner-Daten", required = true, content =
            @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = KatalogEintrag.class))) final InputStream is,
            @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataKatalogBetriebsarten(conn).create(is),
        	request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
    }


    /**
     * Die OpenAPI-Methode für das Patchen einer Betriebsart im angegebenen Schema
     *
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation der Betriebsart
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/betriebsart/{id : \\d+}")
    @Operation(summary = "Passt die zu der ID der Betriebsart zugehörigen Stammdaten an.",
    description = "Passt die Beschäftigungsart-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. "
                + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Betriebssart "
                + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich in die Beschäftigungsart-Daten integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Beschäftigungsart-Daten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Keine Beschäftigungsart mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")

    public Response patchBetriebsart(@PathParam("schema") final String schema, @PathParam("id") final long id,
                                        @RequestBody(description = "Der Patch für die Betrieb-Stammdaten", required = true,
                                        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = KatalogEintrag.class))) final
                                        InputStream is, @Context final HttpServletRequest request) {
    	return OpenAPIApplication.runWithTransaction(conn -> new DataKatalogBetriebsarten(conn).patch(id, is),
        	request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
    }

}
