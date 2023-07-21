package de.svws_nrw.api.server;

import de.svws_nrw.api.OpenAPIApplication;
import de.svws_nrw.core.data.enm.ENMDaten;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.enm.DataENMDaten;
import de.svws_nrw.db.DBEntityManager;
import io.swagger.v3.oas.annotations.Operation;
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
     * Die OpenAPI-Methode für die Abfrage der Daten für das Externe Datenmodul (ENM) in Bezug auf alle Lehrer
     * des aktuellen Schuljahresabschnitts der Schule.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die ENM-Daten
     */
    @GET
    @Path("/alle")
    @Operation(summary = "Liefert die Daten des Externen Notenmoduls (ENM).",
    	description = "Liest die Daten des Externen Notenmoduls (ENM) aus der Datenbank "
    			+ "und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum "
    			+ "Auslesen der Notendaten besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Daten des Externen Notenmoduls (ENM)",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ENMDaten.class)))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.")
    @ApiResponse(responseCode = "404", description = "Es wurden nicht alle benötigten Daten für das Erstellen der ENM-Daten gefunden.")
    public Response getENMDaten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN)) {
	    	return (new DataENMDaten(conn)).getAll();
    	}
    }


    /**
     * Die OpenAPI-Methode für die Abfrage der Daten für das Externe Datenmodul (ENM) in Bezug auf alle Lehrer
     * des aktuellen Schuljahresabschnitts der Schule als GZIP-Json.
     *
     * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param request   die Informationen zur HTTP-Anfrage
     *
     * @return die ENM-Daten
     */
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/alle/gzip")
    @Operation(summary = "Liefert die Daten des Externen Notenmoduls (ENM) GZip-komprimiert.",
    	description = "Liest die Daten des Externen Notenmoduls (ENM) aus der Datenbank "
    			+ "und liefert diese GZip-komprimiert zurück. Dabei wird geprüft, ob der SVWS-Benutzer die "
    			+ "notwendige Berechtigung zum Auslesen der Notendaten besitzt.")
    @ApiResponse(responseCode = "200", description = "Die GZip-komprimierte ENM-JSON-Datei",
                 content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM,
                 schema = @Schema(type = "string", format = "binary", description = "Die GZip-komprimierte ENM-JSON-Datei")))
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.")
    @ApiResponse(responseCode = "404", description = "Es wurden nicht alle benötigten Daten für das Erstellen der ENM-Daten gefunden.")
    public Response getENMDatenGZip(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN)) {
	    	return (new DataENMDaten(conn)).getAllGZip();
    	}
    }



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
    public Response getLehrerENMDaten(@PathParam("schema") final String schema, @PathParam("id") final long id,
    		                                    @Context final HttpServletRequest request) {
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_FUNKTION, BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN)) {
	    	return (new DataENMDaten(conn)).get(id);
    	}
    }

}
