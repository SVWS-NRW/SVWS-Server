package de.nrw.schule.svws.api.server;

import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.config.SVWSKonfiguration;
import de.nrw.schule.svws.core.data.db.DBSchemaListeEintrag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die Konfiguration 
 * des SVWS-Servers.
 * Die Config-API stellt neben einer Schnittstelle zum Prüfen, ob ein SVWS-Server vorhanden 
 * ist (alive), weitere Konfigurationsmöglichkteiten zur Verfügung.   
 */
@Path("")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
@Tag(name = "Server")	
public class APIConfig {

	
	/**
	 * Diese Methode kann genutzt werden, um die Verfügbarkeit des SVWS-Server zu testen.
	 * 
	 * @return die HTTP-Response
	 */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/status/alive")
    @Operation(summary = "Eine Test-Methode zum Prüfen, ob der Server erreichbar ist.", 
    	       description = "Eine Test-Methode zum Prüfen, ob der Server erreichbar ist.")
    @ApiResponse(responseCode = "200", description = "Der Server ist erreichbar!",
    		     content = @Content(mediaType = MediaType.TEXT_PLAIN, schema = @Schema(implementation = String.class)))
    public Response isAlive() {
    	return Response.ok("SVWS-Server erreichbar").build();
    }
    
    
    /**
     * Diese Methode liefert den öffentlichen Schlüssel des Servers in Base64-Kodierung
     * 
     * @return der öffentlichen Schlüssel des Servers in Base64-Kodierung
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/config/publickey_base64")
    @Operation(summary = "Gibt den öffentlichen Schlüssel des Server in Base64-Kodierung zurück.", 
               description = "Gibt den öffentlichen Schlüssel des Server in Base64-Kodierung zurück.")
	@ApiResponse(responseCode = "200", description = "Der Base-64-kodierte, öffentliche Schlüssel des Servers",
			     content = @Content(mediaType = MediaType.TEXT_PLAIN, schema = @Schema(implementation = String.class)))
	@ApiResponse(responseCode = "500", description = "Der öffentliche Schlüssel wurde nicht gefunden")
    public Response getConfigPublicKeyBase64() {
        try {
			return Response.ok(SVWSKonfiguration.getPublicKeyBase64()).build();
		} catch (KeyStoreException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
    }


    /**
     * Diese Methode liefert das Zertifikat des Servers in Base64-Kodierung
     * 
     * @return das Zertifikat des Servers in Base64-Kodierung
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/config/certificate_base64")
    @Operation(summary = "Gibt das Zertifikat des Server in Base64-Kodierung zurück.", 
               description = "Gibt das Zertifikat des Server in Base64-Kodierung zurück.")
	@ApiResponse(responseCode = "200", description = "Das Base-64-kodierte Zertifikat des Servers",
			     content = @Content(mediaType = MediaType.TEXT_PLAIN, schema = @Schema(implementation = String.class)))
	@ApiResponse(responseCode = "500", description = "Das Zertifikat wurde nicht gefunden")
    public Response getConfigCertificateBase64() {
        try {
			return Response.ok(SVWSKonfiguration.getCertificateBase64()).build();
		} catch (KeyStoreException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
    }
	
    
    /**
     * Diese Methode liefert das Zertifikat des Servers
     * 
     * @return das Zertifikat des Servers
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/config/certificate")
    @Operation(summary = "Gibt das Zertifikat des Server zurück.", 
               description = "Gibt das Zertifikat des Server zurück.")
	@ApiResponse(responseCode = "200", description = "Das Zertifikat des Servers", 
				 content = @Content(mediaType = MediaType.TEXT_PLAIN, schema = @Schema(implementation = String.class)))
	@ApiResponse(responseCode = "500", description = "Das Zertifikat wurde nicht gefunden")
    public Response getConfigCertificate() {
        try {
        	Certificate cert = SVWSKonfiguration.getCertificate();
			return Response.ok(cert.toString()).build();
		} catch (KeyStoreException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
    }
	

    
    /**
     * Die OpenAPI-Methode für die Abfrage der Liste aller konfigurierten DB-Schemata.
     *  
     * @return die Liste mit den einzelnen DB-Schemata
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/config/db/schemata")
    @Operation(summary = "Gibt eine sortierte Übersicht von allen konfigurierten DB-Schemata zurück.",
               description = "Gibt eine sortierte Übersicht von allen konfigurierten DB-Schemata zurück.")
    @ApiResponse(responseCode = "200", description = "Eine Liste von DB-Schema-Listen-Einträgen",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                 array = @ArraySchema(schema = @Schema(implementation = DBSchemaListeEintrag.class))))
    public List<DBSchemaListeEintrag> getConfigDBSchemata() {
    	return SVWSKonfiguration.get().getSchemaList();
    }
    
}
