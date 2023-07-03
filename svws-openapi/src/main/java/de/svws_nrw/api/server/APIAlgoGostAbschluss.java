package de.svws_nrw.api.server;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungErgebnis;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.data.gost.GostBelegpruefungsdaten;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff rund um die Abschlussberechnung in der Gymnasialen Oberstufe.
 * Ein Zugriff erfolgt über den Pfad https://{Hostname}/api/gost/...
 */
@Path("/api/gost")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "ServerAlgorithmen")
public class APIAlgoGostAbschluss {


    /**
     * Die OpenAPI-Methode für die Prüfung der Belegprüfung der Abiturdaten.
     *
     * @param daten         die Abiturdaten eines Schülers und die Fächerdaten für die Belegprüfung
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              das Ergebis der Prüfung der Belegprüfung der Abiturdaten
     */
    @POST
    @Path("/belegpruefung/gesamt")
    @Operation(summary = "Führt eine Belegprüfung anhand der übergebenen Abiturdaten durch.",
               description = "Prüft anhand der übergeben Abiturdaten, ob die Belegung in den Abiturdaten korrekt ist oder nicht. Es werden ggf. auch "
               		+ "Belegungsfehler und Hinweise zur Belegung zurückgegeben.")
    @ApiResponse(responseCode = "200", description = "Das Ergebnis der Belegprüfung, ggf. mit Belegungsfehlern",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON,
                 schema = @Schema(implementation = GostBelegpruefungErgebnis.class)))
    public GostBelegpruefungErgebnis getGostBelegpruefungGesamt(
    		@RequestBody(description = "Die Abiturdaten für die Belegprüfung", required = true,
    					 content = @Content(mediaType = MediaType.APPLICATION_JSON,
    					 schema = @Schema(implementation = GostBelegpruefungsdaten.class))) final GostBelegpruefungsdaten daten,
    		@Context final HttpServletRequest request) {
    	// Prüfe die Belegung der Kurse mithilfe des Abiturdaten-Managers und gib das Ergebnis der Belegprüfung zurück.
    	final GostFaecherManager faecherManager = new GostFaecherManager(daten.gostFaecher, daten.gostFaecherKombinationen);
		final AbiturdatenManager manager = new AbiturdatenManager(daten.abiturdaten, daten.gostJahrgang, faecherManager, GostBelegpruefungsArt.GESAMT);
		return manager.getBelegpruefungErgebnis();
    }


    /**
     * Die OpenAPI-Methode für die Prüfung der Belegprüfung der Abiturdaten nur für die EF_1.
     *
     * @param daten         die Abiturdaten eines Schülers und die Fächerdaten für die Belegprüfung
     * @param request       die Informationen zur HTTP-Anfrage
     *
     * @return              das Ergebis der Prüfung der Belegprüfung nur für die EF_1
     */
    @POST
    @Path("/belegpruefung/EF1")
    @Operation(summary = "Führt eine Belegprüfung nur für die EF.1 anhand der übergebenen Abiturdaten durch.",
               description = "Prüft anhand der übergeben Abiturdaten, ob die Belegung in den Abiturdaten korrekt ist oder nicht. Es werden ggf. auch "
               		+ "Belegungsfehler und Hinweise zur Belegung zurückgegeben.")
    @ApiResponse(responseCode = "200", description = "Das Ergebnis der Belegprüfung, ggf. mit Belegungsfehlern",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON,
                 schema = @Schema(implementation = GostBelegpruefungErgebnis.class)))
    public GostBelegpruefungErgebnis getGostBelegpruefungEF1(
    		@RequestBody(description = "Die Abiturdaten für die Belegprüfung", required = true,
    					 content = @Content(mediaType = MediaType.APPLICATION_JSON,
    					 schema = @Schema(implementation = GostBelegpruefungsdaten.class))) final GostBelegpruefungsdaten daten,
    		@Context final HttpServletRequest request) {
    	// Prüfe die Belegung der Kurse mithilfe des Abiturdaten-Managers und gib das Ergebnis der Belegprüfung zurück.
    	final GostFaecherManager faecherManager = new GostFaecherManager(daten.gostFaecher, daten.gostFaecherKombinationen);
		final AbiturdatenManager manager = new AbiturdatenManager(daten.abiturdaten, daten.gostJahrgang, faecherManager, GostBelegpruefungsArt.EF1);
		return manager.getBelegpruefungErgebnis();
    }

}
