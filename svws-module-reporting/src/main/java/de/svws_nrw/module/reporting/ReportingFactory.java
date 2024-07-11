package de.svws_nrw.module.reporting;

import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingAusgabeformat;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.HtmlFactory;
import de.svws_nrw.module.reporting.pdf.PdfFactory;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse beinhaltet den Code zur Erstellung von Html-Inhalten auf Basis der hinterlegten html-Vorlage und den übergebenen Daten.
 * Sie setzt voraus, dass zum übergebenen html-Template eine css-Datei mit gleichem Pfad und Namen existiert.
 */
public final class ReportingFactory {

	/** Die Verbindung zur Datenbank. */
	private final DBEntityManager conn;

	/** Die Daten für die Report-Ausgabe. */
	private final ReportingParameter reportingParameter;

	/** Liste, die Einträge aus dem Logger sammelt. */
	private final LogConsumerList log = new LogConsumerList();

	/** Logger, der den Ablauf protokolliert und Fehlerdaten sammelt */
	private final Logger logger = new Logger();


	/**
	 * Erzeugt eine neue Reporting-Factory, um einen Report zu erzeugen.
	 *
	 * @param conn Die Verbindung zur Datenbank.
	 * @param reportingParameter Das Objekt, welches die Angaben zu den Daten des Reports und den zugehörigen Einstellungen enthält.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public ReportingFactory(final DBEntityManager conn, final ReportingParameter reportingParameter) throws ApiOperationException {

		this.logger.addConsumer(log);

		// Validiere Datenbankverbindung
		if (conn == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Verbindung zur Datenbank übergeben.");

		this.conn = conn;

		// Validiere Reporting-Parameter
		if (reportingParameter == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Daten zur Ausgabe im Report übergeben.");

		this.reportingParameter = reportingParameter;
	}


	/**
	 * Erstellt eine Response in Form einer einzelnen Datei oder ZIP-Datei mit den mehreren generierten Report-Dateien.
	 *
	 * @return Im Falle eines Success enthält die HTTP-Response das Dokument oder die ZIP-Datei.
	 *     Im Fehlerfall wird eine ApiOperationException ausgelöst oder bei Fehlercode 500 eine SimpleOperationResponse mit Logdaten zurückgegeben.
	 */
	public Response createReportResponse() throws ApiOperationException {

		try {
			return switch (ReportingAusgabeformat.getByID(reportingParameter.ausgabeformat)) {
				case ReportingAusgabeformat.HTML -> new HtmlFactory(conn, reportingParameter, logger, log).createHtmlResponse();
				case ReportingAusgabeformat.PDF -> {
					final HtmlFactory htmlFactory = new HtmlFactory(conn, reportingParameter, logger, log);
					yield new PdfFactory(htmlFactory.createHtmlBuilders(), reportingParameter, logger, log).createPdfResponse();
				}
				case null -> {
					logger.logLn("## Fehler");
					logger.logLn(4, "Kein bekanntes Ausgabeformat für die Reporterstellung übergeben.");
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, new ReportingErrorResponse(null, logger, log).getSimpleOperationResponse());
				}
			};
		} catch (final Exception e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, new ReportingErrorResponse(e, logger, log).getSimpleOperationResponse());
		}
	}
}
