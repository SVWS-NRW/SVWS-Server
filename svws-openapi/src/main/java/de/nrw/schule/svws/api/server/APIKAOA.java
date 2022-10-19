package de.nrw.schule.svws.api.server;

import de.nrw.schule.svws.api.OpenAPIApplication;
import de.nrw.schule.svws.core.data.kaoa.KAOAAnschlussoptionEintrag;
import de.nrw.schule.svws.core.data.kaoa.KAOABerufsfeldEintrag;
import de.nrw.schule.svws.core.data.kaoa.KAOAEbene4Eintrag;
import de.nrw.schule.svws.core.data.kaoa.KAOAKategorieEintrag;
import de.nrw.schule.svws.core.data.kaoa.KAOAMerkmalEintrag;
import de.nrw.schule.svws.core.data.kaoa.KAOAZusatzmerkmalEintrag;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.data.kaoa.DataKAoAAnschlussoptionen;
import de.nrw.schule.svws.data.kaoa.DataKAoABerufsfelder;
import de.nrw.schule.svws.data.kaoa.DataKAoAEbene4;
import de.nrw.schule.svws.data.kaoa.DataKAoAKategorien;
import de.nrw.schule.svws.data.kaoa.DataKAoAMerkmale;
import de.nrw.schule.svws.data.kaoa.DataKAoAZusatzmerkmale;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff KAoA-Daten aus der SVWS-Datenbank.
 */
@Path("/db/{schema}/kaoa")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")	
public class APIKAOA {

    /**
     * Die OpenAPI-Methode für die Abfrage des KAoA-Kataloges Kategorien.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return der Katalog
     */
    @GET
    @Path("/allgemein/kategorien")
    @Operation(summary = "Die Liste der Einträge aus dem KAoA-Katalog Kategorien.",
               description = "Die Liste der Einträge aus dem KAoA-Katalog Kategorien. "
               		       + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den KAoA-Katalog Kategorien",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KAOAKategorieEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogKAoAKategorien(@PathParam("schema") String schema, @Context HttpServletRequest request) {
    	try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
    		return (new DataKAoAKategorien()).getAll();
    	}
    }

    /**
     * Die OpenAPI-Methode für die Abfrage des KAoA-Kataloges Merkmale.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return der Katalog
     */
    @GET
    @Path("/allgemein/merkmale")
    @Operation(summary = "Die Liste der Einträge aus dem KAoA-Katalog Merkmale.",
               description = "Die Liste der Einträge aus dem KAoA-Katalog Merkmale. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den KAoA-Katalog Merkmale",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KAOAMerkmalEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogKAoAMerkmale(@PathParam("schema") String schema, @Context HttpServletRequest request) {
        try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
            return (new DataKAoAMerkmale()).getAll();
        }
    }


    /**
     * Die OpenAPI-Methode für die Abfrage des KAoA-Kataloges Zusatzmerkmale.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return der Katalog
     */
    @GET
    @Path("/allgemein/zusatzmerkmale")
    @Operation(summary = "Die Liste der Einträge aus dem KAoA-Katalog Zusatzmerkmale.",
               description = "Die Liste der Einträge aus dem KAoA-Katalog Zusatzmerkmale. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den KAoA-Katalog Zusatzmerkmale",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KAOAZusatzmerkmalEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogKAoAZusatzmerkmale(@PathParam("schema") String schema, @Context HttpServletRequest request) {
        try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
            return (new DataKAoAZusatzmerkmale()).getAll();
        }
    }

    /**
     * Die OpenAPI-Methode für die Abfrage des KAoA-Kataloges Einträge der SBO Ebene 4.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return der Katalog
     */
    @GET
    @Path("/allgemein/ebene4")
    @Operation(summary = "Die Liste der Einträge aus dem KAoA-Katalog Einträge der SBO Ebene 4.",
               description = "Die Liste der Einträge aus dem KAoA-Katalog Einträge der SBO Ebene 4. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den KAoA-Katalog Einträge der SBO Ebene 4",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KAOAEbene4Eintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogKAoAEbene4(@PathParam("schema") String schema, @Context HttpServletRequest request) {
        try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
            return (new DataKAoAEbene4()).getAll();
        }
    }

    /**
     * Die OpenAPI-Methode für die Abfrage des KAoA-Kataloges Anschlussoptionen.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return der Katalog
     */
    @GET
    @Path("/allgemein/anschlussoptionen")
    @Operation(summary = "Die Liste der Einträge aus dem KAoA-Katalog Anschlussoptionen.",
               description = "Die Liste der Einträge aus dem KAoA-Katalog Anschlussoptionen. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den KAoA-Katalog Anschlussoptionen",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KAOAAnschlussoptionEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogKAoAAnschlussoptionen(@PathParam("schema") String schema, @Context HttpServletRequest request) {
        try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
            return (new DataKAoAAnschlussoptionen()).getAll();
        }
    }

    /**
     * Die OpenAPI-Methode für die Abfrage des KAoA-Kataloges Berufsfelder.
     *  
     * @param schema        das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return der Katalog
     */
    @GET
    @Path("/allgemein/berufsfelder")
    @Operation(summary = "Die Liste der Einträge aus dem KAoA-Katalog Berufsfelder.",
               description = "Die Liste der Einträge aus dem KAoA-Katalog Berufsfelder. "
                           + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von Katalog-Einträgen für den KAoA-Katalog Berufsfelder",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = KAOABerufsfeldEintrag.class))))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.")
    @ApiResponse(responseCode = "404", description = "Keine Katalog-Einträge gefunden")
    public Response getKatalogKAoABerufsfelder(@PathParam("schema") String schema, @Context HttpServletRequest request) {
        try (Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN)) {
            return (new DataKAoABerufsfelder()).getAll();
        }
    }
    
}
