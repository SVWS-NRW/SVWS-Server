package de.svws_nrw.module.reporting;

import de.svws_nrw.core.data.reporting.ReportingAusgabedaten;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingAusgabeformat;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.reporting.html.HtmlFactory;
import de.svws_nrw.module.reporting.pdf.PdfFactory;
import jakarta.ws.rs.core.Response;


/**
 * Diese Klasse beinhaltet den Code zur Erstellung von Html-Inhalten auf Basis der hinterlegten html-Vorlage und den übergebenen Daten.
 * Sie setzt voraus, dass zum übergebenen html-Template eine css-Datei mit gleichem Pfad und Namen existiert.
 */
public final class ReportingFactory {

	/** Die Verbindung zur Datenbank. */
	private final DBEntityManager conn;

	/** Die Daten für die Report-Ausgabe. */
	private final ReportingAusgabedaten reportingAusgabedaten;

	/** Liste, die Einträge aus dem Logger sammelt. */
	private final LogConsumerList log = new LogConsumerList();

	/** Logger, der den Ablauf protokolliert und Fehlerdaten sammelt */
	private final Logger logger = new Logger();


	/**
	 * Erzeugt eine neue Reporting-Factory, um einen Report zu erzeugen.
	 * @param conn Die Verbindung zur Datenbank.
	 * @param reportingAusgabedaten Das Objekt, welches die Angaben zu den Daten des Reports und den zugehörigen Einstellungen enthält.
	 */
	public ReportingFactory(final DBEntityManager conn, final ReportingAusgabedaten reportingAusgabedaten) {

		this.logger.addConsumer(log);

		// Validiere Datenbankverbindung
		if (conn == null)
			throw OperationError.NOT_FOUND.exception("Es wurde keine Verbindung zur Datenbank übergeben.");

		this.conn = conn;

		// Validiere Reporting-Ausgabedaten
		if (reportingAusgabedaten == null)
			throw OperationError.NOT_FOUND.exception("Es wurde keine Daten zur Ausgabe im Report übergeben.");

		this.reportingAusgabedaten = reportingAusgabedaten;
	}


	/**
	 * Erstellt eine Response in Form einer einzelnen Datei oder ZIP-Datei mit den mehreren generierten Report-Dateien.
	 * @return Im Falle eines Success enthält die HTTP-Response das Dokument oder die ZIP-Datei.
	 * Im Fehlerfall wird eine WebApplicationException ausgelöst oder bei Fehlercode 500 eine SimpleOperationResponse mit Logdaten zurückgegeben.
	 */
	public Response createReportResponse() {

		try {
            return switch (ReportingAusgabeformat.getByID(reportingAusgabedaten.ausgabeformat)) {
                case ReportingAusgabeformat.HTML ->	new HtmlFactory(conn, reportingAusgabedaten, logger, log).createHtmlResponse();
                case ReportingAusgabeformat.PDF ->	{
						final HtmlFactory htmlFactory = new HtmlFactory(conn, reportingAusgabedaten, logger, log);
						yield new PdfFactory(htmlFactory.createHtmlBuilders(), reportingAusgabedaten, logger, log).createPdfResponse();
					}
                case null -> throw OperationError.INTERNAL_SERVER_ERROR.exception("Kein bekanntes Ausgabeformat übergeben.");
            };
		} catch (Exception e) {
			return new ReportingErrorResponse(e, logger, log).getResponse();
		}
	}
}
