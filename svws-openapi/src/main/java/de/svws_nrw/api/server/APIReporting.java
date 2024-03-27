package de.svws_nrw.api.server;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.reporting.ReportingAusgabedaten;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.module.reporting.ReportingFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die Reporting-Funktionen des SVWS-Servers.
 */
@Path("/db/{schema}/reporting")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIReporting {

    /**
     * Die OpenAPI-Methode für die Erstellung eines Reports im geforderten Format. Je nach übergebenen Parametern wird eine
	 * einzige Report-Datei oder eine ZIP-Datei mit einzelnen Dateien zurückgegeben.
     *
     * @param schema das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param reportingAusgabedaten Objekt mit den Daten und Einstellungen für den zu erstellenden Report.
     * @param request die Informationen zur HTTP-Anfrage
     *
     * @return Der Report (bzw. ZIP-Datei mit einzelnen Dateien) mit den angeforderten Daten
     */
	@POST
	@Produces("application/pdf")
    @Path("/ausgabe")
	@Operation(
		summary = "Erstellt einen Report als PDF-Datei gemäß den übergebenen Daten.",
		description = "Erstellt die Wahlbogen für die Laufbahnplanung der gymnasialen Oberstufe zu den Schülern mit der "
			+ "angegebenen IDs als PDF-Datei. "
			+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Reports besitzt. "
			+ "Weitergehende Berechtigungen werden im Vorfeld der Reporterstellung überprüft.")
	@ApiResponse(responseCode = "200", description = "Der Report mit den übergebenen Daten wurde erfolgreich erstellt.",
		content = @Content(mediaType = "application/pdf", schema = @Schema(type = "string", format = "binary", description = "Report")))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den geforderten Report zu erstellen.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag zu den übergebenen Daten gefunden.")
	@ApiResponse(responseCode = "500", description = "Es ist ein unbekannter Fehler aufgetreten.",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response pdfReport(
		@PathParam("schema") final String schema,
		@RequestBody(description = "Die Daten und Einstellungen, mit denen der Report erstellt werden soll.", required = true,
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ReportingAusgabedaten.class)))
		final ReportingAusgabedaten reportingAusgabedaten, @Context final HttpServletRequest request) {
			return DBBenutzerUtils.runWithTransaction(
				conn ->	new ReportingFactory(conn, reportingAusgabedaten).createReportResponse(),
				request,
				ServerMode.STABLE,
				BenutzerKompetenz.BERICHTE_STANDARDFORMULARE_DRUCKEN,
				BenutzerKompetenz.BERICHTE_ALLE_FORMULARE_DRUCKEN);
    	}
}
