package de.svws_nrw.api.server;

import de.svws_nrw.api.OpenAPIApplication;
import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.core.data.klassen.KlassenListeEintrag;
import de.svws_nrw.core.data.klassen.KlassenartKatalogEintrag;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.klassen.DataKatalogKlassenarten;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.klassen.DataKlassenlisten;
import de.svws_nrw.db.DBEntityManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
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
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KEINE)) {
    		return (new DataKlassenlisten(conn, abschnitt)).getList();
    	}
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
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KEINE)) {
    		return (new DataKlassendaten(conn)).get(id);
    	}
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
        OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KEINE);
        return (new DataKatalogKlassenarten()).getAll();
    }

}
