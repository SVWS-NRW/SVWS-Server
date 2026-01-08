package de.svws_nrw.module.reporting.factories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.data.reporting.ReportingVorlageParameter;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingAusgabeformat;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.builders.ReportBuilderHtml;
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

	/** Die Liste, die Einträge aus dem Logger sammelt. */
	private final LogConsumerList log = new LogConsumerList();


	/**
	 * Erzeugt eine neue Reporting-Factory, um einen Report zu erzeugen.
	 *
	 * @param conn 						Die Verbindung zur Datenbank.
	 * @param reportingParameter 		Einstellungen und Daten zum Steuern der Report-Generierung.
	 * @param reportingAusgabeformat	Ds Ausgabeformat, das verwendet werden soll. Dient zum Abgleich mit den ReportingParameter-Werten.
	 *
	 * @throws ApiOperationException   	im Fehlerfall
	 */
	public ReportingFactory(final DBEntityManager conn, final ReportingParameter reportingParameter, final ReportingAusgabeformat reportingAusgabeformat)
			throws ApiOperationException {

		try {
			// Initialisiere Log für Status- und Fehlermeldungen
			this.logger.addConsumer(log);

			this.logger.logLn(LogLevel.DEBUG, 0, ">>> Beginn des Initialisierens der Reporting-Factory und des Validierens übergebener Daten.");

			// Validiere Datenbankverbindung
			this.logger.logLn(LogLevel.DEBUG, 4, "Validiere Datenbankverbindung.");
			if (conn == null) {
				this.logger.logLn(LogLevel.ERROR, 4, "### FEHLER: Es wurde keine Verbindung zur Datenbank für die Initialisierung der Reporting-Factory "
						+ "übergeben.");
				throw new ApiOperationException(Status.NOT_FOUND,
						"### FEHLER: Es wurde keine Verbindung zur Datenbank für die Initialisierung der Reporting-Factory übergeben.");
			}
			this.conn = conn;

			// Validiere Reporting-Parameter
			this.logger.logLn(LogLevel.DEBUG, 4, "Validiere Reporting-Parameter.");
			if (reportingParameter == null) {
				this.logger.logLn(LogLevel.ERROR, 4, "### FEHLER: Es wurden keine Reporting-Parameter für die Initialisierung der Reporting-Factory übergeben"
						+ ".");
				throw new ApiOperationException(Status.NOT_FOUND,
						"### FEHLER: Es wurden keine Reporting-Parameter für die Initialisierung der Reporting-Factory übergeben.");
			}
			this.reportingParameter = reportingParameter;

			if ((reportingAusgabeformat == null) || reportingAusgabeformat.equals(ReportingAusgabeformat.UNDEFINED)
					|| (ReportingAusgabeformat.getByID(this.reportingParameter.ausgabeformat) == ReportingAusgabeformat.UNDEFINED)
					|| (ReportingAusgabeformat.getByID(this.reportingParameter.ausgabeformat) != reportingAusgabeformat)) {
				this.logger.logLn(LogLevel.ERROR, 4, "### FEHLER: Es wurde kein gültiges Ausgabeformat definiert oder in den Reporting-Parametern ist ein "
						+ "anderes als für den API-Aufruf gültiges Ausgabeformat definiert worden.");
				throw new ApiOperationException(Status.BAD_REQUEST,
						"### FEHLER: Es wurde kein gültiges Ausgabeformat definiert oder in den Reporting-Parametern ist ein anderes als für den API-Aufruf "
								+ "gültiges Ausgabeformat definiert worden.");
			}

			// Validiere die Angaben zur Vorlage für den Report.
			this.logger.logLn(LogLevel.DEBUG, 4, "Validiere Report-Vorlage.");
			final ReportingReportvorlage reportvorlage = ReportingReportvorlage.getByBezeichnung(this.reportingParameter.reportvorlage);
			if (reportvorlage == null) {
				this.logger.logLn(LogLevel.ERROR, 4, "FEHLER: Es wurde keine gültige Report-Vorlage für die Initialisierung der Reporting-Factory übergeben.");
				throw new ApiOperationException(Status.BAD_REQUEST,
						"### FEHLER: Es wurde keine gültige Report-Vorlage für die Initialisierung der Reporting-Factory übergeben.");
			}

			// Logge für einen evtl. späteren Fehlerfall das Format und das Template.
			this.logger.logLn(LogLevel.DEBUG, 4, "Übergebenes und validiertes Ausgabeformat: " + reportingAusgabeformat.name());
			this.logger.logLn(LogLevel.DEBUG, 4, "Übergebene und validierte Report-Vorlage: " + reportvorlage.getBezeichnung());

			// Validiere Hauptdaten-Angabe
			this.logger.logLn(LogLevel.DEBUG, 4, "Validiere Hauptdaten.");
			if (this.reportingParameter.idsHauptdaten == null) {
				this.reportingParameter.idsHauptdaten = new ArrayList<>();
			} else {
				// Evtl. vorhandene null-Elemente in der Liste entfernen.
				this.reportingParameter.idsHauptdaten = new ArrayList<>(reportingParameter.idsHauptdaten.stream().filter(Objects::nonNull).distinct().toList());
			}
			if (this.reportingParameter.idsHauptdaten.isEmpty())
				this.logger.logLn(LogLevel.INFO, 4, "HINWEIS: Die Liste der Hauptdaten ist leer an die Reporting-Factory übergeben worden.");

			// Stelle sicher, dass bei nicht vorhandenen Detaildaten eine leere Liste statt null vorhanden ist.
			this.logger.logLn(LogLevel.DEBUG, 4, "Validiere Detaildaten.");
			if (this.reportingParameter.idsDetaildaten == null)
				this.reportingParameter.idsDetaildaten = new ArrayList<>();
			else {
				// Evtl. vorhandene null-Elemente in der Liste entfernen.
				this.reportingParameter.idsDetaildaten =
						new ArrayList<>(reportingParameter.idsDetaildaten.stream().filter(Objects::nonNull).distinct().toList());
			}

			// Validiere die Liste mit den Vorlage-Parametern. Lade dazu die definierten Vorlagen aus der ReportingReportvorlage-Klasse und weise diesen
			// definierten Parametern evtl. übergebene Werte zu. So ist sichergestellt, dass imm die richtigen Vorlagen-Parameter vorhanden sind.
			this.logger.logLn(LogLevel.DEBUG, 4, "Validiere Vorlage-Parameter.");
			validiereVorlageParameter(reportingParameter);

			this.logger.logLn(LogLevel.DEBUG, 4, "Erzeugung des Reporting-Repository");
			this.reportingRepository = new ReportingRepository(this.conn, this.reportingParameter, this.logger, this.log);

			this.logger.logLn(LogLevel.DEBUG, 0, "<<< Ende des Initialisierens der Reporting-Factory und des Validierens übergebener Daten.");
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					"### FEHLER: Während der Initialisierung und Validierung der Daten der Reporting-Factory ist ein Fehler aufgetreten.", e, logger,
					LogLevel.ERROR, 0);
			final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
			// Gebe das Log, das in der SimpleOperationResponse für Entwicklungszwecke auf der Console aus.
			sop.log.forEach(Logger.global()::logLn);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, sop, MediaType.APPLICATION_JSON);
		}
	}

	/**
	 * Validiert die übergebenen Vorlage-Parameter gegen die definierten Vorlage-Parameter einer Reportvorlage und kombiniert diese bei Übereinstimmung.
	 * Fehlende oder ungültige Parameter werden ignoriert. Die kombinierten Parameter werden anschließend zurückgeschrieben.
	 *
	 * @param reportingParameter Das ReportingParameter-Objekt, welches die zu validierenden und kombinierten Vorlage-Parameter beinhaltet.
	 */
	private void validiereVorlageParameter(final ReportingParameter reportingParameter) {
		final List<ReportingVorlageParameter> uebergebeneVorlageParameter = (reportingParameter.vorlageParameter == null) ? new ArrayList<>()
				: new ArrayList<>(reportingParameter.vorlageParameter.stream().filter(Objects::nonNull)
						.filter(reportingVorlageParameter -> ((reportingVorlageParameter.name != null) && !reportingVorlageParameter.name.isBlank()))
						.distinct().toList());

		// Map der übergebenen Parameter nach Namen zur schnellen Suche anlegen.
		final java.util.Map<String, ReportingVorlageParameter> uebergebeneVorlageParameterMap = new HashMap<>();
		uebergebeneVorlageParameter.forEach(p -> uebergebeneVorlageParameterMap.put(p.name, p));

		final List<ReportingVorlageParameter> definierteVorlageParameter =
				ReportingReportvorlage.getByBezeichnung(this.reportingParameter.reportvorlage).getVorlageParameterList();

		final List<ReportingVorlageParameter> vorlageParameter = new ArrayList<>();

		for (final ReportingVorlageParameter definierteVorlageParameterEintrag : definierteVorlageParameter) {
			// Neuen Parameter auf Basis des definierten erstellen, aber den Wert aus dem übergebenen setzen
			final ReportingVorlageParameter kombinierterEintrag = new ReportingVorlageParameter();
			// Name aus der Definition
			kombinierterEintrag.name = definierteVorlageParameterEintrag.name;
			// Typ aus der Definition
			kombinierterEintrag.typ = definierteVorlageParameterEintrag.typ;
			// Zunächst den Standardwert der Definition setzen.
			kombinierterEintrag.wert = definierteVorlageParameterEintrag.wert;

			// Suche den gleichnamigen Parameter in den übergebenen Einträgen und setze dessen Wert auf den kombinierten Wert.
			final ReportingVorlageParameter uebergebenerEintrag = uebergebeneVorlageParameterMap.get(definierteVorlageParameterEintrag.name);
			if ((uebergebenerEintrag != null) && (uebergebenerEintrag.wert != null))
				kombinierterEintrag.wert = uebergebenerEintrag.wert;

			vorlageParameter.add(kombinierterEintrag);
		}

		// Ergebnis in die Reporting-Parameter zurückschreiben
		this.reportingParameter.vorlageParameter = vorlageParameter;
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
			this.logger.logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung einer API-Response zur Report-Generierung.");

			final Response reportResponse;

			switch (ReportingAusgabeformat.getByID(reportingParameter.ausgabeformat)) {
				case ReportingAusgabeformat.UNDEFINED -> {
					logger.logLn(LogLevel.ERROR, 4, "FEHLER: Das Ausgabeformat UNDEFINIERT wurde für die Report-Generierung übergeben.");
					final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
					throw new ApiOperationException(Status.BAD_REQUEST, null, sop, MediaType.APPLICATION_JSON);
				}
				case ReportingAusgabeformat.HTML -> {
					this.logger.logLn(LogLevel.DEBUG, 4, "HTML als Ausgabeformat für die Report-Generierung gewählt.");
					final HtmlFactory htmlFactory = new HtmlFactory(reportingRepository);
					// Erzeuge im try-Block eine temporäre Response, die bei einem Fehler automatisch geschlossen wird (SonarCube-Angabe)
					try (Response autocloseResponse = htmlFactory.createHtmlResponse()) {
						if (!log.getText(LogLevel.ERROR).isEmpty()) {
							logger.logLn(LogLevel.ERROR, 0,
									"### FEHLER: Während der Erzeugung einer HTML-Response zur Report-Generierung ist ein Fehler geloggt worden.");
							final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
							throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, null, sop, MediaType.APPLICATION_JSON);
						}
						// Response klonen, damit die zurückgegebene Response nicht die Auto-Close-Ressource ist
						reportResponse = Response.fromResponse(autocloseResponse).build();
					}
				}
				case ReportingAusgabeformat.PDF -> {
					this.logger.logLn(LogLevel.DEBUG, 4, "PDF als Ausgabeformat für die Report-Generierung gewählt.");
					final HtmlFactory htmlFactory = new HtmlFactory(reportingRepository);
					final List<ReportBuilderHtml> htmlBuilders = htmlFactory.createHtmlBuilders();
					this.logger.logLn(LogLevel.DEBUG, 4, "HTML-Builder wurden erzeugt.");
					final PdfFactory pdfFactory = new PdfFactory(htmlBuilders, reportingRepository);
					// Erzeuge im try-Block eine temporäre Response, die bei einem Fehler automatisch geschlossen wird (SonarCube-Angabe)
					try (Response autocloseResponse = pdfFactory.createPdfResponse()) {
						if (!log.getText(LogLevel.ERROR).isEmpty()) {
							logger.logLn(LogLevel.ERROR, 0,
									"### FEHLER: Während der Erzeugung einer PDF-Response zur Report-Generierung ist ein Fehler geloggt worden.");
							final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
							throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, null, sop, MediaType.APPLICATION_JSON);
						}
						// Response klonen, damit die zurückgegebene Response nicht die Auto-Close-Ressource ist
						reportResponse = Response.fromResponse(autocloseResponse).build();
					}
				}
				case ReportingAusgabeformat.EMAIL -> {
					this.logger.logLn(LogLevel.DEBUG, 4, "EMAIL als Ausgabeformat für die Report-Generierung gewählt.");
					final HtmlFactory htmlFactory = new HtmlFactory(reportingRepository);
					final List<ReportBuilderHtml> htmlBuilders = htmlFactory.createHtmlBuilders();
					this.logger.logLn(LogLevel.DEBUG, 4, "HTML-Builder wurden erzeugt.");
					final PdfFactory pdfFactory = new PdfFactory(htmlBuilders, reportingRepository);
					final EmailFactory emailFactory = new EmailFactory(reportingRepository);
					// Erzeuge im try-Block eine temporäre Response, die bei einem Fehler automatisch geschlossen wird (SonarQube-Angabe)
					try (Response autocloseResponse = emailFactory.sendEmails(pdfFactory)) {
						if (!log.getText(LogLevel.ERROR).isEmpty()) {
							logger.logLn(LogLevel.ERROR, 0, "### FEHLER: Während des E-Mail-Versands (Response) wurde ein Fehler geloggt.");
							final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
							throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, null, sop, MediaType.APPLICATION_JSON);
						}
						// Response klonen, damit die zurückgegebene Response nicht die Auto-Close-Ressource ist
						reportResponse = Response.fromResponse(autocloseResponse).build();
					}
				}
				case null, default -> {
					logger.logLn(LogLevel.ERROR, 4, "FEHLER: Kein bekanntes Ausgabeformat für die Report-Generierung übergeben.");
					final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
					throw new ApiOperationException(Status.NOT_FOUND, null, sop, MediaType.APPLICATION_JSON);
				}
			}
			// Prüfe nun, ob während der Report-Generierung ein Fehler aufgetreten ist, der als Error ins Log geschrieben wurde, aber nicht als Fehler
			// geworfen wurde.
			if (!log.getText(LogLevel.ERROR).isEmpty()) {
				logger.logLn(LogLevel.ERROR, 0, "### FEHLER: Während der Erzeugung einer API-Response zur Report-Generierung ist ein Fehler geloggt worden.");
				final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, null, sop, MediaType.APPLICATION_JSON);
			}
			// Wenn kein Fehler vermerkt wurde, kann der Report zurückgegeben werden.
			this.logger.logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung einer API-Response zur Report-Generierung.");
			return reportResponse;
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					"### FEHLER: Während der Erzeugung einer API-Response zur Report-Generierung ist ein Fehler aufgetreten.", e, logger,
					LogLevel.ERROR, 0);
			final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
			// Gebe das Log, das in der SimpleOperationResponse für Entwicklungszwecke auf der Console aus.
			sop.log.forEach(Logger.global()::logLn);
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, sop, MediaType.APPLICATION_JSON);
		}
	}
}
