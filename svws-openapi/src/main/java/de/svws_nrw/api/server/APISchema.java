package de.svws_nrw.api.server;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.schema.DBUtilsSchema;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.dto.current.schema.DTOSchemaStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf Schemaoperationen ohne Root-Rechte.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/api/update/...
 * oder https://{Hostname}/db/{schema}/api/revision
 */
@Path("/db/{schema}/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Schema")
public class APISchema {

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
    @Path("/update/{revision : \\d+}")
    @Operation(summary = "Aktualisiert das angegebene Schema auf die angegebene Revision.",
    		   description = "Prüft das Schema bezüglich der aktuellen Revision und aktualisiert das Schema ggf. auf die übergebene Revision, sofern "
    		   		       + "diese in der Schema-Definition existiert.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Verlauf des Updates",
    		     content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class))))
	@ApiResponse(responseCode = "400", description = "Es wurde ein ungültiger Schema-Name oder eine ungültige Revision angegeben.")
	@ApiResponse(responseCode = "404", description = "Die Schema-Datenbank konnte nicht geladen werden. Die Server-Konfiguration ist fehlerhaft.")
    public Response updateSchema(@PathParam("schema") final String schemaname, @PathParam("revision") final long revision, @Context final HttpServletRequest request) {
    	// Akzeptiere nur einen Datenbankzugriff als Administrator in Bezug auf Updates ...
    	final Benutzer user = DBBenutzerUtils.getSVWSUser(request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
    	// ... führe das Update aus ...
    	final LogConsumerList log = DBUtilsSchema.updateSchema(user, revision);
    	// ... und gebe den Log zurück
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(log.getStrings()).build();
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
    @Path("/update")
    @Operation(summary = "Aktualisiert das angegebene Schema auf die neueste Revision.",
	           description = "Prüft das Schema bezüglich der aktuellen Revision und aktualisiert das Schema ggf. auf die neueste Revision.")
    @ApiResponse(responseCode = "200", description = "Der Log vom Verlauf des Updates",
    			 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class))))
    @ApiResponse(responseCode = "400", description = "Es wurde ein ungültiger Schema-Name oder eine ungültige Revision angegeben.")
    @ApiResponse(responseCode = "404", description = "Die Schema-Datenbank konnte nicht geladen werden. Die Server-Konfiguration ist fehlerhaft.")
    public Response updateSchemaToCurrent(@PathParam("schema") final String schemaname, @Context final HttpServletRequest request) {
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
    @Path("/revision")
    @Operation(summary = "Liefert die aktuelle Revision des angegebenen Schemas.",
	           description = "Liefert die aktuelle Revision des angegebenen Schemas.")
    @ApiResponse(responseCode = "200", description = "Die Revision des Schemas",
    	content = @Content(mediaType = "application/json",
    	schema = @Schema(implementation = Long.class)))
    @ApiResponse(responseCode = "404", description = "Es konnte keine Revision für das Schema ermittelt werden.")
    public Response revision(@PathParam("schema") final String schemaname, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> {
	    	final DTOSchemaStatus version = conn.querySingle(DTOSchemaStatus.class);
	    	if (version == null)
	    		throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
	    	return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(version.Revision).build();
    	}, request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
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
    @Path("/isTainted")
    @Operation(summary = "Gibt zurück, ob es sich um ein \"verdorbenes\" Schema handelt oder nicht.",
	           description = "Gibt zurück, ob es sich um ein \"verdorbenes\" Schema handelt oder nicht. Eine Schema wird "
	           		+ " wird als \"verdorben\" bezeichnet, wenn es ggf. fehlerhaft ist, weil es mithilfe einer "
	           		+ "Entwicklerversion  des SVWS-Servers aktualisiert wurde.")
    @ApiResponse(responseCode = "200", description = "true, falls es sich um ein \"verdorbenes\" Schema handelt und ansonsten false",
    	content = @Content(mediaType = "application/json",
    	schema = @Schema(implementation = Boolean.class)))
    @ApiResponse(responseCode = "404", description = "Es konnte keine Revision für das Schema ermittelt werden.")
    public Response isTainted(@PathParam("schema") final String schemaname, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> {
	    	final DTOSchemaStatus version = conn.querySingle(DTOSchemaStatus.class);
	    	if (version == null)
	    		throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
	    	return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(version.IsTainted).build();
    	}, request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
    }

}
