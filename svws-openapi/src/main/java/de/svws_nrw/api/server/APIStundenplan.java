package de.svws_nrw.api.server;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import de.svws_nrw.api.OpenAPIApplication;
import de.svws_nrw.core.data.stundenplan.SchuelerStundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.stundenplan.DataSchuelerStundenplan;
import de.svws_nrw.data.stundenplan.DataStundenplanListe;
import de.svws_nrw.data.stundenplan.DataStundenplanZeitraster;
import de.svws_nrw.db.DBEntityManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Stundenplandaten aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/stundenplan/...
 */
@Path("/db/{schema}/stundenplan")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")	
public class APIStundenplan {

    /**
     * Die OpenAPI-Methode für die Abfrage der Liste aller Stundenpläne.
     *  
     * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request     die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste mit den Stundenplänen
     */
    @GET
    @Path("/liste")
    @Operation(summary = "Gibt eine sortierte Übersicht aller Stundenpläne zurück.",
               description = "Erstellt eine Liste der Stundenpläne. Die Stundenpläne sind anhand des Schuljahresabschnitt "
               		       + "und der Gültigkeit sortiert."
               		       + "Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplanlisten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste der Stundenpläne",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StundenplanListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Stundenplanlisten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Stundenpläne gefunden", content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    public Response getStundenplanliste(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	// TODO Benutzerkompetenz anpassen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataStundenplanListe(conn).getList());
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Liste aller Stundenpläne eines Schuljahresabschnitts.
     *  
     * @param schema      das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abschnitt   die ID des Schuljahresabschnitts 
     * @param request     die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste mit den Stundenplänen
     */
    @GET
    @Path("/liste/{abschnitt : \\d+}")
    @Operation(summary = "Gibt eine sortierte Übersicht der Stundenpläne des angegebenen Schuljahresabschnitts zurück.",
               description = "Erstellt eine Liste der Stundenpläne des angegebenen Schuljahresabschnitts. Die Stundenpläne "
               		       + "sind anhand der Gültigkeit sortiert."
               		       + "Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplanlisten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste der Stundenpläne",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StundenplanListeEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Stundenplanlisten anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Stundenpläne gefunden")
    public Response getStundenplanlisteFuerAbschnitt(@PathParam("schema") String schema, @PathParam("abschnitt") long abschnitt, @Context HttpServletRequest request) {
    	// TODO Benutzerkompetenz anpassen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataStundenplanListe(conn).get(abschnitt));
    	}
    }



    /**
     * Die OpenAPI-Methode für die Abfrage des Zeitrasters zu einem Stundenplan.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id            die ID des Stundenplans
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return              die Liste der Zeitraster-Einträge für den angegebenen Stundenplan
     */
    @GET
    @Path("/{id : \\d+}/zeitraster")
    @Operation(summary = "Gibt die Einträge aus dem Zeitraster des angegebenen Stundeplans zurück.",
               description = "Erstellt eine Liste der Einträge aus dem Zeitraster des angegebenen Stundeplans. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Zeitraster-Einträgen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StundenplanZeitraster.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um das Zeitraster anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Zeitraster-Einträge gefunden")
    public Response getStundenplanZeitraster(@PathParam("schema") String schema, @PathParam("id") long id, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataStundenplanZeitraster(conn, id)).getList();
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Stundenplans eines Schülers.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param id            die ID des Stundenplans
     * @param schuelerID    die ID des Schülers
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return              der Stundenplan des Schülers
     */
    @GET
    @Path("/{id : \\d+}/schueler/{schueler_id : \\d+}")
    @Operation(summary = "Gibt den Stundeplan des Schülers zurück.",
               description = "Erstellt den angebebenen Stundeplan in Bezug auf den angegebenen Schüler. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten "
               		       + "besitzt.")
    @ApiResponse(responseCode = "200", description = "Der Schüler-Stundenplan",
                 content = @Content(mediaType = "application/json", 
                 schema = @Schema(implementation = SchuelerStundenplan.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keinen Stundenplan gefunden")
    public Response getSchuelerStundenplan(@PathParam("schema") String schema, @PathParam("id") long id, @PathParam("schueler_id") long schuelerID, @Context HttpServletRequest request) {
    	// TODO Berechtigungen anpassen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)) {
    		return (new DataSchuelerStundenplan(conn, id)).get(schuelerID);
    	}
    }

}
