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
import de.nrw.schule.svws.core.data.kurse.KursDaten;
import de.nrw.schule.svws.core.data.kurse.KursListeEintrag;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.data.kurse.DataKursdaten;
import de.nrw.schule.svws.data.kurse.DataKursliste;
import de.nrw.schule.svws.db.DBEntityManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Daten von Kursen aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/api/kurse/...
 */
@Path("/db/{schema}/kurse")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")	
public class APIKurse {


    /**
     * Die OpenAPI-Methode für die Abfrage der Liste der Kurse aller Schuljahresabschniite 
     * im angegebenen Schema.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste der Kurse aller Schuljahresabschniite 
     */
    @GET
    @Path("/")
    @Operation(summary = "Gibt eine Übersicht von allen Kursen zurück.",
               description = "Erstellt eine Liste aller in der Datenbank vorhanden Kurse unter Angabe der ID, des Kürzels, "
               		       + "der Parallelität, der Kürzel des Klassenlehrers und des zweiten Klassenlehrers, "
               		       + "einer Sortierreihenfolge und ob sie in der Anwendung sichtbar sein sollen. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Klassendaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Kurs-Listen-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KursListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Kursdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Kurs-Einträge gefunden")
    public Response getKurse(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKursliste(conn, null)).getList();
    	}
    }

    
    /**
     * Die OpenAPI-Methode für die Abfrage der Liste der Kurse im angegebenen Schema.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abschnitt     die ID des Schuljahresabschnitts
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return              die Liste der Kurse mit der jeweiligen ID im Datenbankschema
     */
    @GET
    @Path("/abschnitt/{abschnitt : \\d+}")
    @Operation(summary = "Gibt eine Übersicht von allen Kursen eines Schuljahresabschnittes zurück.",
    description = "Erstellt eine Liste aller in der Datenbank vorhanden Kurse eines Schuljahresabschnittes unter "
    		       + "Angabe der ID, des Kürzels, der Parallelität, der Kürzel des Klassenlehrers und des zweiten "
    		       + "Klassenlehrers, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar sein sollen. "
    		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Klassendaten "
    		       + "besitzt.")
	@ApiResponse(responseCode = "200", description = "Eine Liste von Kurs-Listen-Einträgen",
	      content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KursListeEintrag.class))))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Kursdaten anzusehen.")
	@ApiResponse(responseCode = "404", description = "Keine Kurs-Einträge gefunden")
    public Response getKurseFuerAbschnitt(@PathParam("schema") String schema, @PathParam("abschnitt") long abschnitt, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKursliste(conn, abschnitt)).getList();
    	}
    }
    
    
    /**
     * Die OpenAPI-Methode für die Abfrage der Daten eines Kurses.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation des Kurses
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die Daten des Kurses
     */
    @GET
    @Path("/{id : \\d+}")
    @Operation(summary = "Liefert zu der ID des Kurses die zugehörigen Daten.",
    description = "Liest die Daten des Kurses zu der angegebenen ID aus der Datenbank und liefert diese zurück. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Kursdaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Daten des Kurses",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = KursDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Kursdaten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Kein Kurs-Eintrag mit der angegebenen ID gefunden")
    public Response getKurs(@PathParam("schema") String schema, @PathParam("id") long id, 
    		                                    @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKursdaten(conn)).get(id);
    	}
    }
    
}
