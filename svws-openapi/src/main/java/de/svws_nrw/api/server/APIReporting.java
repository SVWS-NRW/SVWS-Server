package de.svws_nrw.api.server;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.reporting.ReportingAusgabeformat;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.module.reporting.factories.ReportingFactory;
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
import org.jboss.resteasy.annotations.GZIP;


/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für den Zugriff auf die Reporting-Funktionen des SVWS-Servers.
 */
@Path("/db/{schema}/reporting")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APIReporting {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APIReporting() {
		// leer
	}

	/**
	 * Die OpenAPI-Methode für die Erstellung eines Reports im HTML-Format zur Anzeige im Browser.
	 *
	 * @param schema das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param reportingParameter Objekt mit den Daten und Einstellungen für den zu erstellenden Report.
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return Der Report als HTML-Dokument mit den angeforderten Daten
	 */
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/html")
	@GZIP
	@Operation(summary = "Erstellt einen Report als HTML-Dokument für eine direkte Anzeige im Browser.",
			description = "Erstellt den angeforderten Report gemäß den Reporting-Parametern als HTML-Dokument. "
					+ "Das HTML ist selbsttragend (CSS inline) und kann unmittelbar angezeigt werden. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Reports besitzt. "
					+ "Weitergehende Berechtigungen werden im Vorfeld der Reporterstellung überprüft.")
	@ApiResponse(responseCode = "200", description = "Der Report wurde erfolgreich als HTML erzeugt.",
			content = @Content(mediaType = MediaType.TEXT_HTML, schema = @Schema(implementation = String.class)))
	// TODO Diese Beschreibung wieder aufnehmen, sobald die Auslieferung des Hinweis-Headers freigegeben ist. Der Server setzt ihn derzeit nur im
	// Modus DEV (siehe ReportingHinweiseHeader); eine stabile Installation liefert ihn nicht, deshalb darf die veröffentlichte Schnittstelle ihn
	// nicht zusagen. Zum Freischalten die folgenden zwei Zeilen in die ApiResponse oben aufnehmen und die Importe für Header und
	// ReportingHinweisSerializer ergänzen:
	// headers = @Header(name = ReportingHinweisSerializer.HEADER_NAME,
	//         description = "Begleitet eine erfolgreiche Ausgabe mit Hinweisen auf ihre Vollständigkeit. Dictionary nach RFC 9651, etwa `v=1, angefordert=120, ausgegeben=117, hinweise=3, datensaetze=3`. `v` ist die Vertragsversion, `angefordert` die Anzahl der bestellten Einheiten des Datenaufbaus (etwa die angeforderten IDs einer Liste), `ausgegeben` die Anzahl der Einheiten in der Ausgabe und `hinweise` die Zahl der Hinweise; weitere Einträge nennen Anzahlen je Kategorie und fehlen, wenn sie null wären. Die Differenz aus `angefordert` und `ausgegeben` ist nicht zwingend durch Hinweise erklärbar - auch der Benutzerfilter reduziert die Ausgabe, ohne ein Hinweis zu sein. Der Header enthält keine IDs, Namen oder Freitexte. **Fehlt der Header, ist die Vollständigkeit unbekannt**; das bedeutet nie `nachweislich vollständig`. Unbekannte Einträge und Versionen sind zu ignorieren.",
	//         schema = @Schema(implementation = String.class))
	@ApiResponse(responseCode = "400", description = "Die übergebenen Parameter sind fehlerhaft.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den geforderten Report zu erstellen.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag zu den übergebenen Daten gefunden.")
	@ApiResponse(responseCode = "500", description = "Es ist ein unbekannter Fehler aufgetreten.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response htmlReport(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten und Einstellungen, mit denen der Report erstellt werden soll.", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ReportingParameter.class))) final ReportingParameter reportingParameter,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new ReportingFactory(conn, reportingParameter, ReportingAusgabeformat.HTML).createReportResponse(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.BERICHTE_STANDARDFORMULARE_DRUCKEN,
				BenutzerKompetenz.BERICHTE_ALLE_FORMULARE_DRUCKEN);
	}

	/**
	 * Die OpenAPI-Methode für die Erstellung eines Reports im geforderten Format. Je nach übergebenen Parametern wird eine
	 * einzige Report-Datei oder eine ZIP-Datei mit einzelnen Dateien zurückgegeben.
	 *
	 * @param schema das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param reportingParameter Objekt mit den Daten und Einstellungen für den zu erstellenden Report.
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return Der Report (bzw. ZIP-Datei mit einzelnen Dateien) mit den angeforderten Daten
	 */
	@POST
	@Produces("application/pdf")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/ausgabe")
	@GZIP
	@Operation(summary = "Erstellt einen Report als PDF-Datei gemäß den übergebenen Daten.",
			description = "Erstellt den angeforderten Report gemäß den in den Reporting-Parametern angegebenen Daten und Einstellungen und bietet ihn als PDF-Datei zum Download an. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Reports besitzt. "
					+ "Weitergehende Berechtigungen werden im Vorfeld der Reporterstellung überprüft.")
	@ApiResponse(responseCode = "200", description = "Der Report mit den übergebenen Daten wurde erfolgreich erstellt.",
			content = @Content(mediaType = "application/pdf", schema = @Schema(type = "string", format = "binary", description = "Report")))
	// TODO Diese Beschreibung wieder aufnehmen, sobald die Auslieferung des Hinweis-Headers freigegeben ist. Der Server setzt ihn derzeit nur im
	// Modus DEV (siehe ReportingHinweiseHeader); eine stabile Installation liefert ihn nicht, deshalb darf die veröffentlichte Schnittstelle ihn
	// nicht zusagen. Zum Freischalten die folgenden zwei Zeilen in die ApiResponse oben aufnehmen und die Importe für Header und
	// ReportingHinweisSerializer ergänzen:
	// headers = @Header(name = ReportingHinweisSerializer.HEADER_NAME,
	//         description = "Begleitet eine erfolgreiche Ausgabe mit Hinweisen auf ihre Vollständigkeit. Dictionary nach RFC 9651, etwa `v=1, angefordert=120, ausgegeben=117, hinweise=3, datensaetze=3`. `v` ist die Vertragsversion, `angefordert` die Anzahl der bestellten Einheiten des Datenaufbaus (etwa die angeforderten IDs einer Liste), `ausgegeben` die Anzahl der Einheiten in der Ausgabe und `hinweise` die Zahl der Hinweise; weitere Einträge nennen Anzahlen je Kategorie und fehlen, wenn sie null wären. Die Differenz aus `angefordert` und `ausgegeben` ist nicht zwingend durch Hinweise erklärbar - auch der Benutzerfilter reduziert die Ausgabe, ohne ein Hinweis zu sein. Der Header enthält keine IDs, Namen oder Freitexte. **Fehlt der Header, ist die Vollständigkeit unbekannt**; das bedeutet nie `nachweislich vollständig`. Unbekannte Einträge und Versionen sind zu ignorieren.",
	//         schema = @Schema(implementation = String.class))
	@ApiResponse(responseCode = "400", description = "Die übergebenen Parameter sind fehlerhaft.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den geforderten Report zu erstellen.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag zu den übergebenen Daten gefunden.")
	@ApiResponse(responseCode = "500", description = "Es ist ein unbekannter Fehler aufgetreten.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response pdfReport(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten und Einstellungen, mit denen der Report erstellt werden soll.", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ReportingParameter.class))) final ReportingParameter reportingParameter,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new ReportingFactory(conn, reportingParameter, ReportingAusgabeformat.PDF).createReportResponse(),
				request, ServerMode.STABLE,
				BenutzerKompetenz.BERICHTE_STANDARDFORMULARE_DRUCKEN,
				BenutzerKompetenz.BERICHTE_ALLE_FORMULARE_DRUCKEN);
	}

	/**
	 * Die OpenAPI-Methode für die Erstellung eines Reports im geforderten Format. Dieser Report wird dann an die zugehörigen E-Mail-Adressen versendet.
	 *
	 * @param schema das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param reportingParameter Objekt mit den Daten und Einstellungen für den zu erstellenden Report.
	 * @param request die Informationen zur HTTP-Anfrage
	 *
	 * @return Informationen zum Versand der E-Mails.
	 */
	@POST
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/email")
	@Operation(summary = "Erstellt einen Report als PDF-Datei gemäß den übergebenen Daten und versendet ihn per E-Mail.",
			description = "Erstellt den angeforderten Report gemäß den in den Reporting-Parametern angegebenen Daten und Einstellungen und versendet ihn als PDF-Datei per E-Mail. "
					+ "Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Reports besitzt. "
					+ "Weitergehende Berechtigungen werden im Vorfeld der Reporterstellung überprüft.")
	@ApiResponse(responseCode = "200", description = "Der Report mit den übergebenen Daten wurde erfolgreich erstellt und als E-Mail versendet.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "400", description = "Die übergebenen Parameter sind fehlerhaft.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um den geforderten Report zu erstellen.")
	@ApiResponse(responseCode = "404", description = "Kein Eintrag zu den übergebenen Daten gefunden.")
	@ApiResponse(responseCode = "500", description = "Es ist ein unbekannter Fehler aufgetreten.",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleOperationResponse.class)))
	public Response emailReport(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Daten und Einstellungen, mit denen der Report erstellt und versendet werden soll.", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ReportingParameter.class))) final ReportingParameter reportingParameter,
			@Context final HttpServletRequest request) {
		return DBBenutzerUtils.runWithTransaction(conn -> new ReportingFactory(conn, reportingParameter, ReportingAusgabeformat.EMAIL).createReportResponse(),
				request, ServerMode.ALPHA,
				BenutzerKompetenz.BERICHTE_STANDARDFORMULARE_DRUCKEN,
				BenutzerKompetenz.BERICHTE_ALLE_FORMULARE_DRUCKEN);
	}

}
