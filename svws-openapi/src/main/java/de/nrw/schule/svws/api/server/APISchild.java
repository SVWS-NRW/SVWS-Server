package de.nrw.schule.svws.api.server;

import de.nrw.schule.svws.api.OpenAPIApplication;
import de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragAbiturInfos;
import de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragDQRNiveaus;
import de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragDatenart;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.data.schild3.DataSchildAbiturInfos;
import de.nrw.schule.svws.data.schild3.DataSchildDQRNiveaus;
import de.nrw.schule.svws.data.schild3.DataSchildDatenart;
import de.nrw.schule.svws.db.Benutzer;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf allgemeine Kataloge aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/api/kataloge/...
 */
@Path("/db/{schema}/schild3")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")	
public class APISchild {

    /**
     * Die OpenAPI-Methode für die Abfrage des Schild3-Kataloges AbiturInfos.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return der Katalog
     */
    @GET
    @Path("/abiturinfos")
    @Operation(summary = "Die Liste der Einträge aus dem Schild-Katalog AbiturInfos.",
               description = "Die Liste der Einträge aus dem Schild-Katalog AbiturInfos. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den Schild-Katalog AbiturInfos",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Schild3KatalogEintragAbiturInfos.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogSchild3AbiturInfos(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataSchildAbiturInfos()).getAll();
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Schild3-Kataloges Datenarten.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return der Katalog
     */
    @GET
    @Path("/datenarten")
    @Operation(summary = "Die Liste der Einträge aus dem Schild-Katalog Datenart.",
               description = "Die Liste der Einträge aus dem Schild-Katalog Datenart. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den Schild-Katalog Datenart",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Schild3KatalogEintragDatenart.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogSchild3Datenarten(@PathParam("schema") String schema, @Context HttpServletRequest request) {
        try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
            return (new DataSchildDatenart()).getAll();
        }
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des Schild3-Kataloges DQR-Niveaus.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return der Katalog
     */
    @GET
    @Path("/dqr_niveaus")
    @Operation(summary = "Die Liste der Einträge aus dem Schild-Katalog DQR-Niveaus.",
               description = "Die Liste der Einträge aus dem Schild-Katalog DQR-Niveaus. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den Schild-Katalog DQR-Niveaus",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Schild3KatalogEintragDQRNiveaus.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogSchild3DQRNiveaus(@PathParam("schema") String schema, @Context HttpServletRequest request) {
        try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
            return (new DataSchildDQRNiveaus()).getAll();
        }
    }

}
