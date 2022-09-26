package de.nrw.schule.svws.api.server;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.api.OpenAPIApplication;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.data.schema.DataSQLite;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.svws.db.DTODBVersion;
import de.nrw.schule.svws.db.schema.DBSchemaDefinition;
import de.nrw.schule.svws.db.utils.schema.DBSchemaManager;
import de.nrw.schule.svws.logger.LogConsumerVector;
import de.nrw.schule.svws.logger.Logger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf Schemaoperationen ohne Root-Rechte.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/api/update/...
 * oder https://{Hostname}/db/{schema}/api/revision
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Schema")	
public class APISchema {
    
	
    /**
     * Die OpenAPI-Methode für den Export einer SQLite-Datenbank aus dem aktuellen Schema. Der Aufruf erfordert
     * administrative Rechte.
     *  
     * @param schemaname    Name des Schemas, in das hinein migriert werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return Die SQLite-Datenbank
     */
    @GET
    @Produces("application/vnd.sqlite3")
    @Path("/db/{schema}/api/export/sqlite")
    @Operation(summary = "Exportiert das aktuelle Schema in eine neu erstellte SQLite-Datenbank.",
               description = "Exportiert das aktuelle Schema in eine neu erstellte SQLite-Datenbank. Der Aufruf erfordert "
               		       + "administrative Rechte.")
    @ApiResponse(responseCode = "200", description = "Der Export der SQLite-Datenbank", 
    			 content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM,
    			 schema = @Schema(type = "string", format = "binary", description = "LuPO-MDB-Datei")))
    @ApiResponse(responseCode = "403", description = "Das Schema darf nicht exportiert werden.")
    public Response exportSQLite(@PathParam("schema") String schemaname, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADMIN)) {
    		return DataSQLite.exportSQLite(conn, schemaname);
    	}
    }



    /**
     * Die OpenAPI-Methode um ein Datenbankschema auf eine bestimmte Revision upzudaten.
     *  
     * @param schemaname    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param revision      die Revision, auf die angehoben werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return Logmeldung über den Updatevorgang
     */
    @POST
    @Path("/db/{schema}/api/update/{revision : \\d+}")
    @Operation(summary = "Aktualisiert das angegebene Schema auf die angegebene Revision.",
    		   description = "Prüft das Schema bezüglich der aktuellen Revision und aktualisiert das Schema ggf. auf die übergebene Revision, sofern "
    		   		       + "diese in der Schema-Definition existiert.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Verlauf des Updates",
    		     content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class))))
	@ApiResponse(responseCode = "400", description = "Es wurde ein ungültiger Schema-Name oder eine ungültige Revision angegeben.")
	@ApiResponse(responseCode = "404", description = "Die Schema-Datenbank konnte nicht geladen werden. Die Server-Konfiguration ist fehlerhaft.")
    public List<String> updateSchema(@PathParam("schema") String schemaname, @PathParam("revision") int revision, @Context HttpServletRequest request) {
    	// Akzeptiere nur einen Datenbankzugriff als Administrator in Bezug auf Updates
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.ADMIN)) {
			// Ermittle die Revision, auf die aktualisiert werden soll. Hier wird ggf. eine negative Revision als neueste Revision interpretiert
			int max_revision = DBSchemaDefinition.getInstance().maxRevision;
			if (revision < 0)
				revision = max_revision;
			if (revision > max_revision)
				throw new WebApplicationException(Status.BAD_REQUEST.getStatusCode());
	
			// Erzeuge einen Logger für das Update
			Logger logger = new Logger();
			LogConsumerVector log = new LogConsumerVector();
			logger.addConsumer(log);
			
			// Erzeuge einen Schema-Manager, der die Aktualisierung des DB-Schema durchführt
			DBSchemaManager manager = DBSchemaManager.create(conn, true, logger); 
			if (manager == null)
				throw new WebApplicationException(Status.FORBIDDEN.getStatusCode());
	
			// Prüfe, ob das Schema aktuell ist
			if (manager.updater.isUptodate(revision, false))
				return log.getStrings();
			
			// Prüfe, ob die angegebene Revision überhaupt ein Update erlaubt -> wenn nicht, dann liegt ein BAD_REQUEST (400) vor
			if (!manager.updater.isUpdatable(revision, false))
				throw new WebApplicationException(Status.BAD_REQUEST.getStatusCode());
	
			// Führe die Aktualisierung durch 
			boolean success = manager.updater.update(revision, false, true);
			if (!success)
				throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR.getStatusCode());
			
			// return log from logger
			return log.getStrings();
    	}
    }

    
    
    /**
     * Die OpenAPI-Methode um ein Datenbankschema auf die neueste Revision upzudaten.
     *  
     * @param schemaname    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return Logmeldung über den Updatevorgang
     */
    @POST
    @Path("/db/{schema}/api/update")
    @Operation(summary = "Aktualisiert das angegebene Schema auf die neueste Revision.",
	           description = "Prüft das Schema bezüglich der aktuellen Revision und aktualisiert das Schema ggf. auf die neueste Revision.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Verlauf des Updates",
    			 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class))))
    @ApiResponse(responseCode = "400", description = "Es wurde ein ungültiger Schema-Name oder eine ungültige Revision angegeben.")
    @ApiResponse(responseCode = "404", description = "Die Schema-Datenbank konnte nicht geladen werden. Die Server-Konfiguration ist fehlerhaft.")
    public List<String> updateSchemaToCurrent(@PathParam("schema") String schemaname, @Context HttpServletRequest request) {
    	return updateSchema(schemaname, -1, request);
    }
    

  
    /**
     * Die OpenAPI-Methode für die Abfrage der Revision des Datenbankschemas.
     *  
     * @param schemaname    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return die Revisionsnummer des Datenbankschemas
     */
    @GET
    @Path("/db/{schema}/api/revision")
    @Operation(summary = "Liefert die aktuelle Revision des angegebenen Schemas.",
	           description = "Liefert die aktuelle Revision des angegebenen Schemas.")
    @ApiResponse(responseCode = "200", description = "Die Revision des Schemas",
    	content = @Content(mediaType = "application/json",
    	schema = @Schema(implementation = Integer.class)))
    @ApiResponse(responseCode = "404", description = "Es konnte keine Revision für das Schema ermittelt werden.")
    public Integer revision(@PathParam("schema") String schemaname, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KEINE)) {
	    	DTODBVersion version = conn.querySingle(DTODBVersion.class);
	    	if ((version == null) || (version.Revision == null))
	    		throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
	    	return version.Revision;
    	}
    }

    
    /**
     * Die OpenAPI-Methode für die Abfrage ob es sich bei dem Datenbankschemas um
     * ein "verdorbenes" Schema handelt. Eine Schema wird als "verdorben" bezeichnet,
     * wenn es ggf. fehlerhaft ist, weil es mithilfe einer Entwicklerversion
     * des SVWS-Servers aktualisiert wurde.
     *  
     * @param schemaname    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return true, falls es sich um ein "verdorbenes" Schema handelt und ansonsten false
     */
    @GET
    @Path("/db/{schema}/api/isTainted")
    @Operation(summary = "Gibt zurück, ob es sich um ein \"verdorbenes\" Schema handelt oder nicht.",
	           description = "Gibt zurück, ob es sich um ein \"verdorbenes\" Schema handelt oder nicht. Eine Schema wird "
	           		+ " wird als \"verdorben\" bezeichnet, wenn es ggf. fehlerhaft ist, weil es mithilfe einer "
	           		+ "Entwicklerversion  des SVWS-Servers aktualisiert wurde.")
    @ApiResponse(responseCode = "200", description = "true, falls es sich um ein \"verdorbenes\" Schema handelt und ansonsten false",
    	content = @Content(mediaType = "application/json",
    	schema = @Schema(implementation = Boolean.class)))
    @ApiResponse(responseCode = "404", description = "Es konnte keine Revision für das Schema ermittelt werden.")
    public boolean isTainted(@PathParam("schema") String schemaname, @Context HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.KEINE)) {
	    	DTODBVersion version = conn.querySingle(DTODBVersion.class);
	    	if ((version == null) || (version.Revision == null))
	    		throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
	    	return version.IsTainted;
    	}
    }
    
    
}
