package de.svws_nrw.module.reporting.factories;

import java.util.ArrayList;
import java.util.Objects;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingAusgabeformat;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * <p>Diese Klasse stellt nach dem Aufruf über die API den Einstiegspunkt in die Report-Generierung dar.</p>
 * <p>Über die Reporting-Parameter werden unter anderem das Report-Format, das zu verwendende Template und die zu druckenden Daten definiert.</p>
 * <p>Rückgabe ist eine dem Zielformat entsprechende Response oder im Fehlerfall eine SimpleOperationResponse mit Log-Informationen.</p>
 */
public final class ReportingFactory {

	/** Die Verbindung zur Datenbank. */
	private final DBEntityManager conn;

	/** Einstellungen und Daten zum Steuern der Report-Generierung. */
	private final ReportingParameter reportingParameter;

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	private final ReportingRepository reportingRepository;

	/** Logger, der den Ablauf protokolliert und Fehlerdaten sammelt. Dieser wird in das Reporting-Repository übergeben, um auch während der Generierung der Ausgabe Fehler festzuhalten und auszugeben. */
	private final Logger logger = new Logger();

	/** Liste, die Einträge aus dem Logger sammelt. */
	private final LogConsumerList log = new LogConsumerList();


	/**
	 * Erzeugt eine neue Reporting-Factory, um einen Report zu erzeugen.
	 *
	 * @param conn 						Die Verbindung zur Datenbank.
	 * @param reportingParameter 		Einstellungen und Daten zum Steuern der Report-Generierung.
	 *
	 * @throws ApiOperationException   	im Fehlerfall
	 */
	public ReportingFactory(final DBEntityManager conn, final ReportingParameter reportingParameter) throws ApiOperationException {

		// Initialisiere Log für Status- und Fehlermeldungen
		this.logger.addConsumer(log);

		this.logger.logLn(LogLevel.DEBUG, 0, ">>> Beginn des Initialisierens der Reporting-Factory und des Validierens übergebener Daten.");

		// Validiere Datenbankverbindung
		if (conn == null) {
			this.logger.logLn(LogLevel.ERROR, 4, "FEHLER: Es wurde keine Verbindung zur Datenbank für die Initialisierung der Reporting-Factory übergeben.");
			throw new ApiOperationException(Status.NOT_FOUND,
					"FEHLER: Es wurde keine Verbindung zur Datenbank für die Initialisierung der Reporting-Factory übergeben.");
		}
		this.conn = conn;

		// Validiere Reporting-Parameter
		if (reportingParameter == null) {
			this.logger.logLn(LogLevel.ERROR, 4, "FEHLER: Es wurden keine Reporting-Parameter für die Initialisierung der Reporting-Factory übergeben.");
			throw new ApiOperationException(Status.NOT_FOUND,
					"FEHLER: Es wurden keine Reporting-Parameter für die Initialisierung der Reporting-Factory übergeben.");
		}
		this.reportingParameter = reportingParameter;

		// Validiere die Angaben zur Vorlage für den Report.
		if (ReportingReportvorlage.getByBezeichnung(this.reportingParameter.reportvorlage) == null) {
			this.logger.logLn(LogLevel.ERROR, 4, "FEHLER: Es wurde keine gültige Report-Vorlage für die Initialisierung der Reporting-Factory übergeben.");
			throw new ApiOperationException(Status.NOT_FOUND,
					"FEHLER: Es wurde keine gültige Report-Vorlage für die Initialisierung der Reporting-Factory übergeben.");
		}

		// Validiere Hauptdaten-Angabe
		if (this.reportingParameter.idsHauptdaten == null) {
			this.reportingParameter.idsHauptdaten = new ArrayList<>();
		} else {
			this.reportingParameter.idsHauptdaten.removeIf(Objects::isNull);
		}
		if (this.reportingParameter.idsHauptdaten.isEmpty())
			this.logger.logLn(LogLevel.INFO, 4, "HINWEIS: Die Liste der Hauptdaten ist leer an die Reporting-Factory übergeben worden.");

		// Stelle sicher, dass bei nicht vorhandenen Detaildaten eine leere Liste statt null vorhanden ist.
		if (this.reportingParameter.idsDetaildaten == null)
			this.reportingParameter.idsDetaildaten = new ArrayList<>();
		else
			this.reportingParameter.idsDetaildaten.removeIf(Objects::isNull);

		this.logger.logLn(LogLevel.DEBUG, 4, "Erzeugung des Reporting-Repository");
		this.reportingRepository = new ReportingRepository(this.conn, this.reportingParameter, this.logger, this.log);

		this.logger.logLn(LogLevel.DEBUG, 0, "<<< Ende des Initialisierens der Reporting-Factory und des Validierens übergebener Daten.");
	}

	/**
	 * Erstellt eine Response in Form einer einzelnen Datei oder ZIP-Datei mit den mehreren generierten Report-Dateien.
	 *
	 * @return Im Falle eines Success enthält die HTTP-Response das Dokument oder die ZIP-Datei.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public Response createReportResponse() throws ApiOperationException {

		try {
			this.logger.logLn(LogLevel.DEBUG, 0, "### Beginn der Erzeugung einer API-Response zur Report-Generierung.");

			return switch (ReportingAusgabeformat.getByID(reportingParameter.ausgabeformat)) {
				case ReportingAusgabeformat.HTML -> new HtmlFactory(reportingRepository).createHtmlResponse();
				case ReportingAusgabeformat.PDF -> {
					final HtmlFactory htmlFactory = new HtmlFactory(reportingRepository);
					yield new PdfFactory(htmlFactory.createHtmlBuilders(), reportingRepository).createPdfResponse();
				}
				case null -> {
					logger.logLn(LogLevel.ERROR, 4, "FEHLER: Kein bekanntes Ausgabeformat für die Report-Generierung übergeben.");
					final SimpleOperationResponse sop = ReportingExceptionUtils.getSimpleOperationResponse(null, logger, log);
					throw new ApiOperationException(Status.NOT_FOUND, null, sop, MediaType.APPLICATION_JSON);
				}
			};
		} catch (final Exception e) {
			logger.logLn(LogLevel.ERROR, 0, "### Fehler während der Erzeugung einer API-Response zur Report-Generierung. Fehlerdaten folgen.");
			final SimpleOperationResponse sop = ReportingExceptionUtils.getSimpleOperationResponse(e, logger, log);
			// Gebe das Log, dass in der SimpleOperationResponse für Entwicklungszwecke auf der Console aus.
			sop.log.forEach(System.out::println);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, sop, MediaType.APPLICATION_JSON);
		}
	}
}
