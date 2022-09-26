package de.nrw.schule.svws.api.server;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import de.nrw.schule.svws.api.OpenAPIApplication;
import de.nrw.schule.svws.core.data.fach.FachDaten;
import de.nrw.schule.svws.core.data.fach.FaecherListeEintrag;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.core.types.statkue.Fachgruppe;
import de.nrw.schule.svws.core.types.statkue.ZulaessigesFach;
import de.nrw.schule.svws.data.faecher.DataFachdaten;
import de.nrw.schule.svws.data.faecher.DataFaecherliste;
import de.nrw.schule.svws.data.faecher.DataKatalogFachgruppen;
import de.nrw.schule.svws.data.faecher.DataKatalogZulaessigeFaecher;
import de.nrw.schule.svws.db.DBEntityManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

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
    public Response getFaecher(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataFaecherliste(conn)).getAll();
    	}
    }
    
    
    // TODO Erstelle Fächerliste mit Filter-Optionen und markiere anschließend die obige Methode als deprecated
    
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
    public Response getFach(@PathParam("schema") String schema, @PathParam("id") long id, 
    		                                    @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataFachdaten(conn)).get(id);
    	}
    }

// TODO die beiden nachfolgenden Methode sollten mit speziellen DTOs unter core.data realisiert werden und nicht die Typen aus core.types direkt verwenden
    
//    /**
//     * Die OpenAPI-Methode für die Abfrage der laut der amtlichen Schulstatistik zulässigen Fächer.
//     *  
//     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
//     * @param request       die Informationen zur HTTP-Anfrage
//     * 
//     * @return die Liste mit den Informationen zu den zulässigen Fächern
//     */
//    @GET
//    @Path("/zulaessige")
//    @Operation(summary = "Gibt eine Übersicht aller für die amtliche Schulstatistik zulässigen Fächer für die Schulform dieser "
//    		+ "Schule zurück.",
//               description = "Erstellt eine Liste aller für die amtliche Schulstatistik für die Schulform zulässigen Fächer. "
//           		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen "
//           		       + "besitzt.")
//    @ApiResponse(responseCode = "200", description = "Eine Liste aller für die amtliche Schulstatistik zulässigen Fächer",
//                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ZulaessigesFach.class))))
//    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
//    @ApiResponse(responseCode = "404", description = "Keine Fächer gefunden oder konnte nicht auf die Informationen der Schule zugreifen.")
//    public Response getFaecherZulaessige(@PathParam("schema") String schema, @Context HttpServletRequest request) {
//    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
//    		return (new DataKatalogZulaessigeFaecher(conn)).getList();
//    	}
//    }
//	
//	
//	
//    /**
//     * Die OpenAPI-Methode für die Abfrage der Fachgruppen für die Schulform dieser Schule.
//     *  
//     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
//     * @param request       die Informationen zur HTTP-Anfrage
//     * 
//     * @return die Liste mit den Informationen zu den Fachgruppen für die Schulform dieser Schule
//     */
//    @GET
//    @Path("/fachgruppen")
//    @Operation(summary = "Gibt eine Übersicht aller Fachgruppen für die Schulform dieser Schule zurück.",
//               description = "Erstellt eine Liste aller Fachgruppen für die Schulform dieser Schule. "
//           		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen "
//           		       + "besitzt.")
//    @ApiResponse(responseCode = "200", description = "Eine Liste aller Fachgruppen für die Schulform dieser Schule.",
//                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Fachgruppe.class))))
//    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine gültige Anmeldung.")
//    @ApiResponse(responseCode = "404", description = "Keine Fachgruppen für die Schulform dieser Schule gefunden.")
//    public Response getFaecherFachgruppen(@PathParam("schema") String schema, @Context HttpServletRequest request) {
//    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
//    		return (new DataKatalogFachgruppen(conn)).getList();
//    	}
//    }
    
}
