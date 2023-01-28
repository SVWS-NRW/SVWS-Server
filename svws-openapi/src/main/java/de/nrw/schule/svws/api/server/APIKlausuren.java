package de.nrw.schule.svws.api.server;

import de.nrw.schule.svws.api.OpenAPIApplication;
import de.nrw.schule.svws.core.data.gost.klausuren.GostKlausurtermin;
import de.nrw.schule.svws.core.data.gost.klausuren.GostKursklausur;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.data.gost.klausurplan.DataGostKlausurenKursklausuren;
import de.nrw.schule.svws.data.gost.klausurplan.DataGostKlausurenTermine;
import de.nrw.schule.svws.db.DBEntityManager;
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
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die grundlegenden Gost-Klausurdaten aus der SVWS-Datenbank.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/db/{schema}/gost/klausuren/...
 */
@Path("/db/{schema}/gost/klausuren")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")	
public class APIKlausuren {

	
    /**
     * Die OpenAPI-Methode für die Abfrage der Klausuren eines Abiturjahrgangs in einem bestimmten Halbjahr 
     * der Gymnasialen Oberstufe.
     *  
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
     * @param halbjahr   das Gost-Halbjahr
     * @param request    die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste der Gost-Kursklausuren
     */
    @GET
    @Path("/kursklausuren/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}")
    @Operation(summary = "Liest eine Liste der Kursklausuren eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus.",
               description = "List eine Liste der Kursklausuren eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Liste der Kursklausuren.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostKursklausur.class))))    
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.")
    @ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.")
    public Response getKursklausurenJahrgangHalbjahr(@PathParam("schema") String schema, @PathParam("abiturjahr") int abiturjahr,
    		@PathParam("halbjahr") long halbjahr, @Context HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN)) {
    		return (new DataGostKlausurenKursklausuren(conn, abiturjahr)).get(halbjahr);
    	}
    }
    
    /**
     * Die OpenAPI-Methode für die Abfrage der Klausurtermine eines Abiturjahrgangs in einem bestimmten Halbjahr 
     * der Gymnasialen Oberstufe.
     *  
     * @param schema     das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
     * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
     * @param halbjahr   das Gost-Halbjahr
     * @param request    die Informationen zur HTTP-Anfrage
     * 
     * @return die Liste der Gost-Klausurtermine
     */
    @GET
    @Path("/termine/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}")
    @Operation(summary = "Liest eine Liste der Kurstermine eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus.",
               description = "List eine Liste der Kurstermine eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. "
    		    + "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.")
    @ApiResponse(responseCode = "200", description = "Die Liste der Klausurtermine.",
                 content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GostKlausurtermin.class))))    
    @ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Klausurtermine auszulesen.")
    @ApiResponse(responseCode = "404", description = "Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.")
    public Response getKlausurtermineJahrgangHalbjahr(@PathParam("schema") String schema, @PathParam("abiturjahr") int abiturjahr,
    		@PathParam("halbjahr") long halbjahr, @Context HttpServletRequest request) {
    	// TODO Anpassung der Benutzerkompetenz / Einführung eines neuen Benutzerkompetenz für den Zugriff auf allgemeine Oberstufeninformationen
    	try (DBEntityManager conn = OpenAPIApplication.getDBConnection(request, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN)) {
    		return (new DataGostKlausurenTermine(conn, abiturjahr)).get(halbjahr);
    	}
    }
	



}
