package de.nrw.schule.svws.api.server;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import de.nrw.schule.svws.api.OpenAPIApplication;
import de.nrw.schule.svws.core.data.SimpleOperationResponse;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.data.SimpleBinaryMultipartBody;
import de.nrw.schule.svws.data.lupo.DataLupo;
import de.nrw.schule.svws.db.Benutzer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Import-/Export von Laufbahnplanungsdaten 
 * aus der SVWS-Datenbank in Bezug auf die gymnasiale Oberstufe für das Program LuPO.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/gost/lupo...
 */
@Path("/db/{schema}/gost/lupo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APILupo {

    /**
     * Die OpenAPI-Methode für den Import einer LuPO-MDB-Datenbank in ein Schema mit dem angegebenen Namen.
     *  
     * @param multipart     LuPO-MDB-Datenbank im Binärformat, DB-Username und Passwort für das neue Schema
     * @param schemaname    Name des Schemas, in welches die LuPO-Daten importiert werden sollen
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return Rückmeldung, ob die Operation erfolgreich war mit dem Log der Operation
     */
    @POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/import/mdb/jahrgang")
    @Operation(summary = "Importiert die Laufbahndaten der übergebenen LuPO-Datenbank in das Schema mit dem angegebenen Namen.",
               description = "Importiert die Laufbahndaten der übergebenen LuPO-Datenbank in das Schema mit dem angegebenen Namen.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Import der Laufbahndaten",
    			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "409", description = "Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.",
    			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
    @ApiResponse(responseCode = "403", description = "Der Benutzer hat keine Berechtigung, um die Laufbahndaten zu importieren.")
    public Response setGostLupoImportMDBFuerJahrgang(@PathParam("schema") String schemaname,
    		@RequestBody(description = "Die LuPO-Datei", required = true, content = 
			@Content(mediaType = MediaType.MULTIPART_FORM_DATA)) @MultipartForm SimpleBinaryMultipartBody multipart,
    		@Context HttpServletRequest request) {
    	Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.IMPORT_EXPORT_DATEN_IMPORTIEREN);
    	return DataLupo.importMDB(user, multipart);
    }

    
    
    /**
     * Die OpenAPI-Methode für den Export einer einer LuPO-MDB-Datenbank aus dem aktuellen Schema. 
     *  
     * @param schemaname    Name des Schemas, in das hinein migriert werden soll
     * @param jahrgang      der Jahrgang, für den die LuPO-Datei erzeugt werden soll.
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return die LUPO-Datei im Erfolgsfall
     */
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/export/mdb/jahrgang/{jahrgang}")
    @Operation(summary = "Exportiert die Laufbahndaten für den übergebenen Jahrgang in eine LuPO-Lehrerdatei.",
               description = "Exportiert die Laufbahndaten für den übergebenen Jahrgang in eine LuPO-Lehrerdatei.")
    @ApiResponse(responseCode = "200", description = "Die LuPO-Lehrerdatei",
                 content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM,
                 schema = @Schema(type = "string", format = "binary", description = "LuPO-MDB-Datei")))
    @ApiResponse(responseCode = "403", description = "Der Benutzer hat keine Rechter zum Export der Laufbahndaten.")
    @ApiResponse(responseCode = "500", description = "Ein interner Server-Fehler beim Erzeugen der LuPO-Datei.")
    public Response getGostLupoExportMDBFuerJahrgang(@PathParam("schema") String schemaname,
    		@PathParam("jahrgang") String jahrgang,
    		@Context HttpServletRequest request) {
    	Benutzer user = OpenAPIApplication.getSVWSUser(request, BenutzerKompetenz.IMPORT_EXPORT_SCHUELERDATEN_EXPORTIEREN);
		return DataLupo.exportMDB(user, jahrgang);
    }
    
}
