package de.svws_nrw.api.server;

import java.io.InputStream;

import de.svws_nrw.core.data.fach.BilingualeSpracheKatalogEintrag;
import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.fach.FachKatalogEintrag;
import de.svws_nrw.core.data.fach.FachgruppenKatalogEintrag;
import de.svws_nrw.core.data.fach.FaecherListeEintrag;
import de.svws_nrw.core.data.fach.SprachpruefungsniveauKatalogEintrag;
import de.svws_nrw.core.data.fach.SprachreferenzniveauKatalogEintrag;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.faecher.DataFachdaten;
import de.svws_nrw.data.faecher.DataFaecherliste;
import de.svws_nrw.data.faecher.DataKatalogBilingualeSprachen;
import de.svws_nrw.data.faecher.DataKatalogFachgruppen;
import de.svws_nrw.data.faecher.DataKatalogSprachpruefungsniveaus;
import de.svws_nrw.data.faecher.DataKatalogSprachreferenzniveaus;
import de.svws_nrw.data.faecher.DataKatalogZulaessigeFaecher;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Fächerdaten aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/faecher/...
 */
@Path("/db/{schema}/faecher")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIFaecher {


    /**
     * Die OpenAPI-Methode für die Abfrage der Liste der Fächer im angegebenen Schema.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Liste der Fächer mit ID des Datenbankschemas
     */
    @GET
    @Path("/")
    @Operation(summary = "Gibt eine Übersicht von allen Fächern zurück.",
               description = "Erstellt eine Liste aller in der Datenbank vorhanden Fächer unter Angabe der ID, des Kürzels, "
               		       + "des verwendeten Statistik-Kürzels, der Bezeichnung des Faches, ob es ein Fach der Oberstufe ist, "
               		       + "einer Sortierreihenfolge und ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Fächerdaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Fächer-Listen-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FaecherListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Fächerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Fächer-Einträge gefunden")
    public Response getFaecher(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataFaecherliste(conn).getAll(),
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Daten eines Faches.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation des Faches
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die Daten des Faches
     */
    @GET
    @Path("/{id : \\d+}")
    @Operation(summary = "Liefert zu der ID des Faches die zugehörigen Daten.",
    description = "Liest die Daten des Faches zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Fächerdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Daten des Faches",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = FachDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Fächerdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Fach-Eintrag mit der angegebenen ID gefunden")
    public Response getFach(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		                                    @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataFachdaten(conn).get(id),
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }


    /**
     * Die OpenAPI-Methode für das Patchen eines Faches.
     *
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation des Faches
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/{id : \\d+}")
    @Operation(summary = "Passt das Fach mit der angebenen ID an.",
    description = "Passt das Fach mit der angebenen ID an. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Fächern "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchFach(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für das Fach", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = FachDaten.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataFachdaten(conn).patch(id, is),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
    }



    /**
     * Die OpenAPI-Methode für das Hinzufügen eines neuen Faches.
     *
     * @param schema       das Datenbankschema
     * @param id           die ID des Faches
     * @param is           der Input-Stream mit den Daten des Faches
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit dem neuen Fach
     */
    @POST
    @Path("/create")
    @Operation(summary = "Erstellt ein neues Fach und gibt das zugehörige Objekt zurück.",
    description = "Erstellt ein neues Fach und gibt das zugehörige Objekt zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Faches besitzt.")
    @ApiResponse(responseCode = "201", description = "Das Fach wurde erfolgreich hinzugefügt.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = FachDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um ein Fach anzulegen.")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addFach(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		@RequestBody(description = "Die Daten des zu erstellenden Faches ohne ID, welche automatisch generiert wird", required = true, content =
			   @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = FachDaten.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataFachdaten(conn).add(is),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
    }


    /**
     * Die OpenAPI-Methode für das Entfernen des Faches.
     *
     * @param schema       das Datenbankschema
     * @param id           die ID des Faches
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit dem Status und ggf. dem gelöschten Fach
     */
    @DELETE
    @Path("/{id : \\d+}")
    @Operation(summary = "Entfernt ein Fach.",
    description = "Entfernt ein Fach. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen eines Faches hat.")
    @ApiResponse(responseCode = "200", description = "Das Fach wurde erfolgreich entfernt.",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = FachDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um ein Fach zu löschen.")
    @ApiResponse(responseCode = "404", description = "Kein Fach vorhanden")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response deleteFach(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataFachdaten(conn).delete(id),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Katalogs der zulässigen Fächer.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              der Katalog der zulässigen Fächer
     */
    @GET
    @Path("/allgemein/faecher")
    @Operation(summary = "Gibt den Katalog der zulässigen Fächer zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden zulässigen Fächer. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Fächer-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FachKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Fach-Katalog-Einträge gefunden")
    public Response getKatalogFaecher(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        DBBenutzerUtils.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
        return (new DataKatalogZulaessigeFaecher()).getAll();
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges aller Fachgruppen aller Schulformen.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return der Katalog aller Fachgruppen aller Schulformen
     */
    @GET
    @Path("/allgemein/fachgruppen")
    @Operation(summary = "Gibt den Katalog aller Fachgruppen aller Schulformen zurück.",
               description = "Gibt den Katalog aller Fachgruppen aller Schulformen zurück. "
           		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Katalog aller Fachgruppen aller Schulformen.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FachgruppenKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
    @ApiResponse(responseCode = "404", description = "Keine Fachgruppen gefunden.")
    public Response getKatalogFachgruppen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogFachgruppen(conn).getAll(),
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }

    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges der Fachgruppen für die Schulform dieser Schule.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return die Liste mit den Informationen zu den Fachgruppen für die Schulform dieser Schule
     */
    @GET
    @Path("/fachgruppen")
    @Operation(summary = "Gibt den Katalog der Fachgruppen für die Schulform dieser Schule zurück.",
               description = "Gibt den Katalog der Fachgruppen für die Schulform dieser Schule zurück. "
                       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Katalog der Fachgruppen für die Schulform dieser Schule.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FachgruppenKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
    @ApiResponse(responseCode = "404", description = "Keine Fachgruppen für die Schulform dieser Schule gefunden.")
    public Response getFachgruppen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogFachgruppen(conn).getList(),
        	request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }

    /**
     * Die OpenAPI-Methode für die Abfrage eines Fachgruppen-Katalog-Eintrags für die angegebene ID.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id        die ID des Fachgruppen-Katalog-Eintrags
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return der Fachgruppen-Katalog-Eintrag
     */
    @GET
    @Path("/allgemein/fachgruppe/{id : \\d+}")
    @Operation(summary = "Gibt den Fachgruppen-Katalog-Eintrag für die angegebene ID zurück.",
               description = "Gibt den Fachgruppen-Katalog-Eintrag für die angegebene ID zurück. "
                       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Fachgruppen-Katalog-Eintrag für die angegebene ID.",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = FachgruppenKatalogEintrag.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
    @ApiResponse(responseCode = "404", description = "Kein Fachgruppen-Katalog-Eintrag für die angegebene ID gefunden.")
    public Response getKatalogFachgruppenEintrag(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogFachgruppen(conn).get(id),
        	request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }

    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges aller bilingualen Sprachen aller Schulformen.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return der Katalog aller bilingualen Sprachen aller Schulformen
     */
    @GET
    @Path("/allgemein/sprachen/bilingual/alle")
    @Operation(summary = "Gibt den Katalog aller bilingualen Sprachen aller Schulformen zurück.",
               description = "Gibt den Katalog aller bilingualen Sprachen aller Schulformen zurück. "
                       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Katalog aller bilingualen Sprachen aller Schulformen.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BilingualeSpracheKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
    @ApiResponse(responseCode = "404", description = "Keine Fachgruppen gefunden.")
    public Response getKatalogBilingualeSprachenAlle(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogBilingualeSprachen(conn).getAll(),
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }

    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges der bilingualen Sprachen für die Schulform dieser Schule.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return die Liste mit den Informationen zu den bilingualen Sprachen für die Schulform dieser Schule
     */
    @GET
    @Path("/allgemein/sprachen/bilingual")
    @Operation(summary = "Gibt den Katalog der bilingualen Sprachen für die Schulform dieser Schule zurück.",
               description = "Gibt den Katalog der bilingualen Sprachen für die Schulform dieser Schule zurück. "
                       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Katalog der bilingualen Sprachen für die Schulform dieser Schule.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BilingualeSpracheKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
    @ApiResponse(responseCode = "404", description = "Keine bilingualen Sprachen für die Schulform dieser Schule gefunden.")
    public Response getKatalogBilingualeSprachen(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogBilingualeSprachen(conn).getList(),
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }

    /**
     * Die OpenAPI-Methode für die Abfrage des Katalog-Eintrags einer bilingualen Sprache für die angegebene ID.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id        die ID des Katalog-Eintrags der bilingualen Sprache
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return der Katalog-Eintrag einer bilingualen Sprache
     */
    @GET
    @Path("/allgemein/sprachen/bilingual/{id : \\d+}")
    @Operation(summary = "Gibt den Katalog-Eintrag einer bilingualen Sprache für die angegebene ID zurück.",
               description = "Gibt den Katalog-Eintrag einer bilingualen Sprache für die angegebene ID zurück. "
                       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Katalog-Eintrag einer bilingualen Sprache für die angegebene ID.",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = BilingualeSpracheKatalogEintrag.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
    @ApiResponse(responseCode = "404", description = "Kein Katalog-Eintrag einer bilingualen Sprache für die angegebene ID gefunden.")
    public Response getKatalogBilingualeSprachenEintrag(@PathParam("schema") final String schema, @PathParam("id") final long id, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogBilingualeSprachen(conn).get(id),
        	request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }

    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges der Sprachprüfungsniveaus.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return der Katalog der Sprachprüfungsniveaus
     */
    @GET
    @Path("/allgemein/sprachen/pruefungsniveaus")
    @Operation(summary = "Gibt den Katalog der Sprachprüfungsniveaus zurück.",
               description = "Gibt den Katalog der Sprachprüfungsniveaus zurück. "
                       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Katalog der Sprachprüfungsniveaus.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SprachpruefungsniveauKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
    @ApiResponse(responseCode = "404", description = "Keine Fachgruppen gefunden.")
    public Response getKatalogSprachpruefungsniveaus(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogSprachpruefungsniveaus().getAll(),
        	request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }

    /**
     * Die OpenAPI-Methode für die Abfrage des Kataloges der Sprachreferenzniveaus.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return der Katalog der Sprachreferenzniveaus
     */
    @GET
    @Path("/allgemein/sprachen/referenzniveaus")
    @Operation(summary = "Gibt den Katalog der Sprachreferenzniveaus zurück.",
               description = "Gibt den Katalog der Sprachreferenzniveaus zurück. "
                       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Katalog der Sprachreferenzniveaus.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SprachreferenzniveauKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
    @ApiResponse(responseCode = "404", description = "Keine Fachgruppen gefunden.")
    public Response getKatalogSprachreferenzniveaus(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKatalogSprachreferenzniveaus().getAll(),
        	request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }


    /**
     * Die OpenAPI-Methode für das Setzen der Sortierung bei der Liste der Fächer im angegebenen Schema
     * auf eine Standard-Sortierung für dei Sekundarstufe II. Für den Aufruf dieser API-Methode
     * muss die Schulform eine Gymnasiale Oberstufe besitzen.
     *
     * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request     die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Response
     */
    @POST
    @Path("/sortierung/setSekII")
    @Operation(summary = "Setzte eine Sortierung für die Fächer auf eine Standard-Sortierung für die Sekundarstufe II.",
               description = "Setzte eine Sortierung für die Fächer auf eine Standard-Sortierung für die Sekundarstufe II."
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen von Fächerdaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Sortierung wurde erfolgreich gesetzt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Fächerdaten anzupassen.")
    @ApiResponse(responseCode = "404", description = "Keine Fächer-Einträge gefunden")
    public Response setFaecherSortierungSekII(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> DataFaecherliste.setDefaultSortierungSekII(conn),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
    }

}
