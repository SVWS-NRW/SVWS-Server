package de.svws_nrw.api.server;

import java.io.InputStream;

import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.core.data.klassen.KlassenListeEintrag;
import de.svws_nrw.core.data.klassen.KlassenartKatalogEintrag;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.klassen.DataKatalogKlassenarten;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.klassen.DataKlassenlisten;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Klassendaten aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/klassen/...
 */
@Path("/db/{schema}/klassen")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIKlassen {

    /**
     * Die OpenAPI-Methode für die Abfrage der Liste der Klassen im angegebenen Schema.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abschnitt     die ID des Schuljahresabschnitts
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              die Liste der Klassen mit ID des Datenbankschemas
     */
    @GET
    @Path("/abschnitt/{abschnitt : \\d+}")
    @Operation(summary = "Gibt eine Übersicht von allen Klassen zurück.",
               description = "Erstellt eine Liste aller in der Datenbank vorhanden Klassen unter Angabe der ID, des Kürzels, "
               		       + "der Parallelität, der Kürzel des Klassenlehrers und des zweiten Klassenlehrers, "
               		       + "einer Sortierreihenfolge und ob sie in der Anwendung sichtbar sein sollen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Klassendaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Klassen-Listen-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KlassenListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Klassendaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Klassen-Einträge gefunden")
    public Response getKlassenFuerAbschnitt(@PathParam("schema") final String schema, @PathParam("abschnitt") final long abschnitt, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKlassenlisten(conn, abschnitt).getList(),
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Daten einer Klasse.
     *
     * @param schema       das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id           die Datenbank-ID zur Identifikation der Klasse
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die Daten der Klasse
     */
    @GET
    @Path("/{id : \\d+}")
    @Operation(summary = "Liefert zu der ID der Klasse die zugehörigen Daten.",
    description = "Liest die Daten der Klasse zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Klassendaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Daten der Klasse",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = KlassenDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Klassendaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Klassen-Eintrag mit der angegebenen ID gefunden")
    public Response getKlasse(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		                                    @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKlassendaten(conn).get(id),
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }


    /**
     * Die OpenAPI-Methode für das Patchen einer Klasse.
     *
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation der Klasse
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/{id : \\d+}")
    @Operation(summary = "Passt die Daten der Klasse mit der angebenen ID an.",
    description = "Passt die Daten der Klasse mit der angebenen ID an. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Klassendaten"
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Patch wurde erfolgreich integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.")
    @ApiResponse(responseCode = "404", description = "Kein Eintrag mit der angegebenen ID gefunden")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchKlasse(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		@RequestBody(description = "Der Patch für die Daten der Klasse", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = KlassenDaten.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKlassendaten(conn).patch(id, is),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
    }


    /**
     * Die OpenAPI-Methode für das Erstellen einer neuen Klasse mit den angegebenen Klassendaten.
     *
     * @param schema                   das Datenbankschema
     * @param is                       der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request                  die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit den Daten der Klasse
     */
    @POST
    @Path("/create")
    @Operation(summary = "Erstellt eine neue Klasse und gibt die zugehörigen Daten zurück.",
    description = "Erstellt eine neue Klasse und gibt die zugehörigen Daten zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Klasse besitzt.")
    @ApiResponse(responseCode = "201", description = "Die Klasse wurde erfolgreich erstellt.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = KlassenDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Klasse anzulegen.")
    @ApiResponse(responseCode = "404", description = "Benötigte Daten wurden nicht gefunden")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response addKlasse(@PathParam("schema") final String schema,
    		@RequestBody(description = "Die Daten der Klasse", required = true, content =
				@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = KlassenDaten.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKlassendaten(conn).add(is),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
    }


    /**
     * Die OpenAPI-Methode für das Entfernen einer Klasse.
     *
     * @param schema       das Datenbankschema
     * @param id           die ID der Klasse
     * @param request      die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Antwort mit dem Status
     */
    @DELETE
    @Path("/{id : \\d+}")
    @Operation(summary = "Entfernt eine Klasse.",
    description = "Entfernt eine Klasse."
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen der Klasse hat.")
    @ApiResponse(responseCode = "204", description = "Die Klasse wurde erfolgreich entfernt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um eine Klasse zu entfernen.")
    @ApiResponse(responseCode = "404", description = "Die Klasse ist nicht vorhanden")
    @ApiResponse(responseCode = "409", description = "Die übergebenen Daten sind fehlerhaft")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response deleteKlasse(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataKlassendaten(conn).delete(id),
    		request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Katalogs der gültigen Klassenarten.
     *
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              der Katalog der gültigen Klassenarten
     */
    @GET
    @Path("/allgemein/klassenarten")
    @Operation(summary = "Gibt den Katalog der gültigen Klassenarten zurück.",
               description = "Erstellt eine Liste aller in dem Katalog vorhanden gültigen Klassenarten. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Klassenart-Katalog-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KlassenartKatalogEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Klassenart-Katalog-Einträge gefunden")
    public Response getKatalogKlassenarten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
        DBBenutzerUtils.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
        return (new DataKatalogKlassenarten()).getAll();
    }


    /**
     * Die OpenAPI-Methode für das Setzen einer Default-Sortierung bei der Liste der Klassen im angegebenen Schema.
     *
     * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abschnitt   die ID des Schuljahresabschnitts
     * @param request     die Informationen zur HTTP-Anfrage
     *
     * @return die HTTP-Response
     */
    @POST
    @Path("/abschnitt/{abschnitt : \\d+}/sortierung/setdefault")
    @Operation(summary = "Setzte eine Default-Sortierung für die Klassen.",
               description = "Setzte eine Default-Sortierung für die Klassen anhand der Sortierung der Jahrgänge und der Parallelität der Klassen."
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen von Klassendaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "204", description = "Die Default-Sortierung wurde erfolgreich gesetzt.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Klassendaten anzupassen.")
    @ApiResponse(responseCode = "404", description = "Keine Jahrgangs- oder Klassen-Einträge gefunden")
    public Response setKlassenSortierungFuerAbschnitt(@PathParam("schema") final String schema, @PathParam("abschnitt") final long abschnitt, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> DataKlassenlisten.setDefaultSortierung(conn, abschnitt),
    		request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }

}
