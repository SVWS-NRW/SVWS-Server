package de.svws_nrw.api.server;

import java.io.InputStream;

import de.svws_nrw.core.data.email.SMTPServerKonfiguration;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.data.email.DataEmailSMTPServerKonfiguration;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die Email-Funktionen des SVWS-Servers.
 */
@Path("/db/{schema}/email")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIEmail {

    /**
     * Die OpenAPI-Methode für die Abfrage der SMTP-Server-Konfiguration der Schule.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die SMTP-Server-Konfiguration
     */
    @GET
    @Path("/smtp/server/konfiguration")
    @Operation(summary = "Gibt die SMTP-Server-Konfiguration der Schule zurück.",
               description = "Gibt die SMTP-Server-Konfiguration der Schule zurück.")
    @ApiResponse(responseCode = "200", description = "Die SMTP-Server-Konfiguration",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON,
                 schema = @Schema(implementation = SMTPServerKonfiguration.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuldaten zu anzusehen.")
    public Response getSMTPServerKonfiguration(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> DataEmailSMTPServerKonfiguration.get(conn),
    		request, ServerMode.STABLE, BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN);
    }



    /**
     * Die OpenAPI-Methode für das Anpassen der SMTP-Server-Konfiguration der Schule.
     *
     * @param schema    das Datenbankschema, auf welches der Patch ausgeführt werden soll
     * @param is        der InputStream, mit dem JSON-Patch-Objekt nach RFC 7386
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return das Ergebnis der Patch-Operation
     */
    @PATCH
    @Path("/smtp/server/konfiguration")
    @Operation(summary = "Passt die SMTP-Server-Konfiguration an und speichert das Ergebnis in der Datenbank.",
    description = "Passt die SMTP-Server-Konfiguration an und speichert das Ergebnis in der Datenbank. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schuldaten "
    		    + "besitzt.")
    @ApiResponse(responseCode = "204", description = "Der Patch wurde erfolgreich in die SMTP-Server-Konfiguration integriert.")
    @ApiResponse(responseCode = "400", description = "Der Patch ist fehlerhaft aufgebaut.")
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um Schuldaten zu ändern.")
    @ApiResponse(responseCode = "409", description = "Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)")
    @ApiResponse(responseCode = "500", description = "Unspezifizierter Fehler (z.B. beim Datenbankzugriff)")
    public Response patchSMTPServerKonfiguration(
    		@PathParam("schema") final String schema,
    		@RequestBody(description = "Der Patch für die SMTP-Server-Konfiguration", required = true, content =
    			@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SMTPServerKonfiguration.class))) final InputStream is,
    		@Context final HttpServletRequest request) {
    	return DBBenutzerUtils.runWithTransaction(conn -> new DataEmailSMTPServerKonfiguration(conn).patch(is),
    		request, ServerMode.STABLE, BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
    }

}
