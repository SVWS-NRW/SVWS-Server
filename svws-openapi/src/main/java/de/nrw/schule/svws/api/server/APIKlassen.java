package de.nrw.schule.svws.api.server;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.nrw.schule.svws.api.OpenAPIApplication;
import de.nrw.schule.svws.core.data.klassen.KlassenDaten;
import de.nrw.schule.svws.core.data.klassen.KlassenListeEintrag;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.data.klassen.DataKlassendaten;
import de.nrw.schule.svws.data.klassen.DataKlassenlisten;
import de.nrw.schule.svws.db.DBEntityManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

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
    public Response getKlassenFuerAbschnitt(@PathParam("schema") String schema, @PathParam("abschnitt") long abschnitt, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
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
    public Response getKlasse(@PathParam("schema") String schema, @PathParam("id") long id, 
    		                                    @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKlassendaten(conn)).get(id);
    	}
    }
    
}
