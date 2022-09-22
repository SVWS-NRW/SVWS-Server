package de.nrw.schule.svws.api.server;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import de.nrw.schule.svws.api.OpenAPIApplication;
import de.nrw.schule.svws.core.data.enm.ENMDaten;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.utils.enm.ENMDatabaseReader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für die Arbeit mit den 
 * grundlegenden Daten des Externen Notenmoduls (ENM).
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/enm/...
 */
@Path("/db/{schema}/enm")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")	
public class APIENM {

	
    /**
     * Die OpenAPI-Methode für die Abfrage der Daten für das Externe Datenmodul (ENM) in Bezug auf einen Lehrer.
     *  
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id        die Datenbank-ID zur Identifikation des Lehrers
     * @param request   die Informationen zur HTTP-Anfrage
     * 
     * @return die Daten für das ENM des Lehrers
     */
    @GET
    @Path("/lehrer/{id : \\d+}")
    @Operation(summary = "Liefert zu der ID des Lehrer die zugehörigen Daten des Externen Notenmoduls (ENM).",
    description = "Liest die Daten des Externen Notenmoduls (ENM) des Lehrers zu der angegebenen ID aus der Datenbank "
    			+ "und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum "
    			+ "Auslesen der Notendaten besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Daten des Externen Notenmoduls (ENM) des Lehrers",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ENMDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.")
    @ApiResponse(responseCode = "404", description = "Kein Lehrer-Eintrag mit der angegebenen ID gefunden")
    public ENMDaten getLehrerENMDaten(@PathParam("schema") String schema, @PathParam("id") long id, 
    		                                    @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.IMPORT_EXPORT_DATEN_IMPORTIEREN)) {
	    	return (new ENMDatabaseReader(conn, id)).getENMDaten();
    	}
    }
	
}
