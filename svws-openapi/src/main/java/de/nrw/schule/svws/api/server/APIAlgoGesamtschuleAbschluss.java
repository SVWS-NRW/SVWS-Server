package de.nrw.schule.svws.api.server;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import de.nrw.schule.svws.core.abschluss.ge.ServiceAbschlussHA10;
import de.nrw.schule.svws.core.abschluss.ge.ServiceAbschlussHA9;
import de.nrw.schule.svws.core.abschluss.ge.ServiceAbschlussMSA;
import de.nrw.schule.svws.core.abschluss.ge.ServiceBerechtigungMSAQ;
import de.nrw.schule.svws.core.abschluss.ge.ServicePrognose;
import de.nrw.schule.svws.core.data.abschluss.AbschlussErgebnis;
import de.nrw.schule.svws.core.data.abschluss.GEAbschlussFaecher;
import de.nrw.schule.svws.db.utils.OperationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die Abschlussberechnung an der Gesamtschule.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/api/gesamtschule/abschluss/...
 */
@Path("/api/gesamtschule/abschluss")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "ServerAlgorithmen")	
public class APIAlgoGesamtschuleAbschluss {

    /**
     * Die OpenAPI-Methode für die Prüfung auf den HA9.
     *  
     * @param faecher       zu übergebende Fächerdaten für die Prüfung auf HA9
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return              das Ergebis der Prüfung auf HA9
     */
    @POST
    @Path("/ha9")
    @Operation(summary = "Führt eine Prüfung auf einen Hauptschulabschluss nach der Klasse 9 an einer Gesamtschule durch.",
               description = "Prüft anhand der übergeben Fächerdaten, ob ein Hauptschulabschluss der Klasse 9 an einer Gesamtschule "
               		       + "erreicht wird oder nicht. Im Falle, dass er nicht erreicht wird, werden ggf. Nachprüfungsfächer zurückgegeben.")
    @ApiResponse(responseCode = "400", description = "Es wurden ungültige Werte übergeben, so dass kein Abschluss berechnet werden kann.")
    @ApiResponse(responseCode = "200", description = "Das Ergebnis der Abschlussberechnung, ggf. mit Nachprüfungsmöglichkeiten",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                 schema = @Schema(implementation = AbschlussErgebnis.class)))
    public AbschlussErgebnis getGesamtschuleAbschlussHA9(
    		@RequestBody(description = "Die Fächerdaten für die Abschlussberechnung", required = true, 
    		             content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                         schema = @Schema(implementation = GEAbschlussFaecher.class))) GEAbschlussFaecher faecher, 
    		@Context HttpServletRequest request) {
        ServiceAbschlussHA9 ha9 = new ServiceAbschlussHA9();
        AbschlussErgebnis ergebnis = ha9.handle(faecher);
        if (ergebnis == null)
        	throw OperationError.BAD_REQUEST.exception();
		ergebnis.log = ha9.getLog().getStrings();
    	return ergebnis;
    }

    
    
    /**
     * Die OpenAPI-Methode für die Prüfung auf den HA10.
     *  
     * @param faecher       zu übergebende Fächerdaten für die Prüfung auf HA10
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return              das Ergebis der Prüfung auf HA10
     */
    @POST
    @Path("/ha10")
    @Operation(summary = "Führt eine Prüfung auf einen Hauptschulabschluss nach der Klasse 10 an einer Gesamtschule durch.",
               description = "Prüft anhand der übergeben Fächerdaten, ob ein Hauptschulabschluss der Klasse 10 an einer Gesamtschule "
               		       + "erreicht wird oder nicht. Im Falle, dass er nicht erreicht wird, werden ggf. Nachprüfungsfächer zurückgegeben.")
    @ApiResponse(responseCode = "200", description = "Das Ergebnis der Abschlussberechnung, ggf. mit Nachprüfungsmöglichkeiten",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                 schema = @Schema(implementation = AbschlussErgebnis.class)))
    public AbschlussErgebnis getGesamtschuleAbschlussHA10(
    		@RequestBody(description = "Die Fächerdaten für die Abschlussberechnung", required = true, 
    		             content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                         schema = @Schema(implementation = GEAbschlussFaecher.class))) GEAbschlussFaecher faecher, 
    		@Context HttpServletRequest request) {
        ServiceAbschlussHA10 ha10 = new ServiceAbschlussHA10();
        AbschlussErgebnis ergebnis = ha10.handle(faecher);
        if (ergebnis == null)
        	throw OperationError.BAD_REQUEST.exception();
		ergebnis.log = ha10.getLog().getStrings();
    	return ergebnis;
    }
    

    
    /**
     * Die OpenAPI-Methode für die Prüfung auf den MSA.
     *  
     * @param faecher       zu übergebende Fächerdaten für die Prüfung auf MSA
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return              das Ergebis der Prüfung auf MSA
     */
    @POST
    @Path("/msa")
    @Operation(summary = "Führt eine Prüfung auf einen Mittleren Schulabschluss nach der Klasse 10 an einer Gesamtschule durch.",
               description = "Prüft anhand der übergeben Fächerdaten, ob ein Mittlerer Schulabschluss nach der Klasse 10 an einer Gesamtschule "
               		       + "erreicht wird oder nicht. Im Falle, dass er nicht erreicht wird, werden ggf. Nachprüfungsfächer zurückgegeben.")
    @ApiResponse(responseCode = "200", description = "Das Ergebnis der Abschlussberechnung, ggf. mit Nachprüfungsmöglichkeiten",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                 schema = @Schema(implementation = AbschlussErgebnis.class)))
    public AbschlussErgebnis getGesamtschuleAbschlussMSA(
    		@RequestBody(description = "Die Fächerdaten für die Abschlussberechnung", required = true, 
    		             content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                         schema = @Schema(implementation = GEAbschlussFaecher.class))) GEAbschlussFaecher faecher, 
    		@Context HttpServletRequest request) {
        ServiceAbschlussMSA msa = new ServiceAbschlussMSA();
        AbschlussErgebnis ergebnis = msa.handle(faecher);
        if (ergebnis == null)
        	throw OperationError.BAD_REQUEST.exception();
		ergebnis.log = msa.getLog().getStrings();
    	return ergebnis;
    }
    

    /**
     * Die OpenAPI-Methode für die Prüfung auf den MSA_Q.
     *  
     * @param faecher       zu übergebende Fächerdaten für die Prüfung auf MSA_Q
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return              das Ergebis der Prüfung auf MSA_Q
     */
    @POST
    @Path("/msaq")
    @Operation(summary = "Führt eine Prüfung auf die Berechtigung zum Besuch der gymnasialen Oberstufe im Rahmen eines Mittleren "
    		             + "Schulabschluss nach der Klasse 10 an einer Gesamtschule durch.",
               description = "Prüft anhand der übergeben Fächerdaten, ob die Berechtigung zum Besuch der gymnasialen Oberstufe im Rahmen eines Mittlerer "
               		       + "Schulabschlusses nach der Klasse 10 an einer Gesamtschule erreicht wird oder nicht.")
    @ApiResponse(responseCode = "200", description = "Das Ergebnis der Abschlussberechnung, ggf. mit Nachprüfungsmöglichkeiten",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                 schema = @Schema(implementation = AbschlussErgebnis.class)))
    public AbschlussErgebnis getGesamtschuleAbschlussMSAQ(
    		@RequestBody(description = "Die Fächerdaten für die Prüfung der Berechtigung", required = true, 
    		             content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                         schema = @Schema(implementation = GEAbschlussFaecher.class))) GEAbschlussFaecher faecher, 
    		@Context HttpServletRequest request) {
        ServiceBerechtigungMSAQ msaq = new ServiceBerechtigungMSAQ();
        AbschlussErgebnis ergebnis = msaq.handle(faecher);
        if (ergebnis == null)
        	throw OperationError.BAD_REQUEST.exception();
		ergebnis.log = msaq.getLog().getStrings();
    	return ergebnis;
    }

    
    /**
     * Die OpenAPI-Methode für die Prüfung auf eine Abschlussprognose für die Gesamtschule.
     *  
     * @param faecher       zu übergebende Fächerdaten für die Prüfung auf eine Abschlussprognose für die Gesamtschule
     * @param request       die Informationen zur HTTP-Anfrage
     * 
     * @return              das Ergebis der Prüfung der Abschlussprognose für die Gesamtschule
     */
    @POST
    @Path("/prognose")
    @Operation(summary = "Führt eine Abschlussprognose für die Gesamtschule aus.",
               description = "Führt anhand der übergeben Fächerdaten eine Abschlussprognose für den Gesamtschulabschluss nach Klasse 9 bzw. Klasse 10 durch."
               		       + "Wird der Jahrgang 10 angegeben, so findet keine Prüfung auf den HA9 statt.")
    @ApiResponse(responseCode = "200", description = "Das Ergebnis der Prognoseberechnung, ggf. mit Nachprüfungsmöglichkeiten",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                 schema = @Schema(implementation = AbschlussErgebnis.class)))
    public AbschlussErgebnis getGesamtschuleAbschlussPrognose(
    		@RequestBody(description = "Der Jahrgang und die Fächerdaten für die Prognose", required = true, 
    		             content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                         schema = @Schema(implementation = GEAbschlussFaecher.class))) GEAbschlussFaecher faecher, 
    		@Context HttpServletRequest request) {
        ServicePrognose prognose = new ServicePrognose();
        AbschlussErgebnis ergebnis = prognose.handle(faecher);
        if (ergebnis == null)
        	throw OperationError.BAD_REQUEST.exception();
		ergebnis.log = prognose.getLog().getStrings();
    	return ergebnis;
    }
    
}