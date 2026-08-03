package de.svws_nrw.module.reporting.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingAusgabeformat;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.builders.ReportBuilderHtml;
import de.svws_nrw.module.reporting.parameter.ReportingParameterBuilder;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.utils.ReportingServerUtils;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * <p>Diese Klasse stellt nach dem Aufruf über die API den Einstiegspunkt in die Report-Generierung dar.</p>
 * <p>Über die Reporting-Parameter werden unter anderem das Report-Format, das zu verwendende Template und die zu druckenden Daten definiert.</p>
 * <p>Rückgabe ist eine dem Zielformat entsprechende Response oder im Fehlerfall eine SimpleOperationResponse mit Log-Informationen.</p>
 */
public final class ReportingFactory {

	/** Meldung für Fehler, die während der Initialisierung und Validierung der Factory auftreten. */
	private static final String FEHLER_INITIALISIERUNG =
			"### FEHLER: Während der Initialisierung und Validierung der Daten der Reporting-Factory ist ein Fehler aufgetreten.";

	/** Meldung für Fehler, die während der Erzeugung der API-Response auftreten. */
	private static final String FEHLER_RESPONSE =
			"### FEHLER: Während der Erzeugung einer API-Response zur Report-Generierung ist ein Fehler aufgetreten.";

	/** Einstellungen und Daten zum Steuern der Report-Generierung. */
	private final ReportingParameter reportingParameter;

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	private final ReportingContext reportingContext;

	/** Logger, der den Ablauf protokolliert und Fehlerdaten sammelt. Dieser wird in den Reporting-Context übergeben, um auch während der Generierung der Ausgabe Fehler festzuhalten und auszugeben. */
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

			this.logger.logLn(LogLevel.DEBUG, 0, ">>> Aufruf des Reporting in SVWS-Server-Version %s - Modus: %s".formatted(ReportingServerUtils.serverversion(), ReportingServerUtils.servermodetext()));
			this.logger.logLn(LogLevel.DEBUG, 0, ">>> Beginn des Initialisierens der Reporting-Factory und des Validierens übergebener Daten.");

			// Validiere Datenbankverbindung
			this.logger.logLn(LogLevel.DEBUG, 4, "Validiere Datenbankverbindung.");
			if (conn == null) {
				this.logger.logLn(LogLevel.ERROR, 4, "### FEHLER: Es wurde keine Verbindung zur Datenbank für die Initialisierung der Reporting-Factory "
						+ "übergeben.");
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"### FEHLER: Es wurde keine Verbindung zur Datenbank für die Initialisierung der Reporting-Factory übergeben.");
			}

			// Validiere Reporting-Parameter
			this.logger.logLn(LogLevel.DEBUG, 4, "Validiere Reporting-Parameter.");
			if (reportingParameter == null) {
				this.logger.logLn(LogLevel.ERROR, 4, "### FEHLER: Es wurden keine Reporting-Parameter für die Initialisierung der Reporting-Factory übergeben"
						+ ".");
				throw new ApiOperationException(Status.BAD_REQUEST,
						"### FEHLER: Es wurden keine Reporting-Parameter für die Initialisierung der Reporting-Factory übergeben.");
			}
			this.reportingParameter = reportingParameter;

			// Validiere das Ausgabeformat, insbesondere, ob dieses mit dem von der API vorgegebenen Ausgabeformat übereinstimmt.
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
			if (this.reportingParameter.idsHauptdaten.isEmpty()) {
				this.logger.logLn(LogLevel.INFO, 4, "HINWEIS: Die Liste der Hauptdaten ist leer an die Reporting-Factory übergeben worden.");
			}

			// Stelle sicher, dass bei nicht vorhandenen Detaildaten eine leere Liste statt null vorhanden ist.
			this.logger.logLn(LogLevel.DEBUG, 4, "Validiere Detaildaten.");
			if (this.reportingParameter.idsDetaildaten == null) {
				this.reportingParameter.idsDetaildaten = new ArrayList<>();
			} else {
				// Evtl. vorhandene null-Elemente in der Liste entfernen.
				this.reportingParameter.idsDetaildaten =
						new ArrayList<>(reportingParameter.idsDetaildaten.stream().filter(Objects::nonNull).distinct().toList());
			}

			// Baue die Vorlage-Parameter über den ReportingParameterBuilder auf: Er kombiniert die Parameter aus dem Vorlagen-Katalog und dem benutzerweiten
			// Katalog mit den gespeicherten und übermittelten Werten (Katalog-Default < gespeicherter Wert < übermittelt). Als gespeicherter Wert gilt je
			// Katalog eine eigene Config-Ebene: für den Vorlagen-Katalog das gespeicherte Vorlagen-Preset des Benutzers zu dieser Reportvorlage, für den
			// benutzerweiten Katalog dessen benutzerweite Einstellungen. Der Builder prüft alle überlagernden Werte auf Typkonformität und setzt
			// Einstellungen zurück, die der aktuelle ServerMode oder die Benutzerkompetenzen nicht zulassen. Dies geschieht vor der Erzeugung des
			// Reporting-Context, damit dessen flache Parameter-Liste den vollständigen und bereinigten Parameter-Satz enthält und fehlende Boolean-Parameter
			// nicht still wie false wirken.
			this.logger.logLn(LogLevel.DEBUG, 4, "Baue und prüfe die Vorlage-Parameter und die benutzerweiten Parameter.");
			final ReportingParameterBuilder parameterBuilder =
					new ReportingParameterBuilder(this.logger, SVWSKonfiguration.get().getServerMode(), conn.getUser());
			parameterBuilder.baue(this.reportingParameter, reportvorlage, conn);

			this.logger.logLn(LogLevel.DEBUG, 4, "Erzeugung des Reporting-Context");
			this.reportingContext = new ReportingContext(conn, this.reportingParameter, this.logger, this.log);

			this.logger.logLn(LogLevel.DEBUG, 0, "<<< Ende des Initialisierens der Reporting-Factory und des Validierens übergebener Daten.");
		} catch (final ApiOperationException aoe) {
			// Die ApiOperationException wird mit ihrem ursprünglichen Status-Code weitergereicht, damit dieser nach außen erhalten bleibt.
			throw zuApiOperationException(aoe, FEHLER_INITIALISIERUNG, aoe.getStatus());
		} catch (final Exception e) {
			throw zuApiOperationException(e, FEHLER_INITIALISIERUNG, Status.INTERNAL_SERVER_ERROR);
		}
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
					final HtmlFactory htmlFactory = erzeugeHtmlFactory();
					reportResponse = erzeugeResponse(htmlFactory::createHtmlResponse,
							"### FEHLER: Während der Erzeugung einer HTML-Response zur Report-Generierung ist ein Fehler geloggt worden.");
				}
				case ReportingAusgabeformat.PDF -> {
					this.logger.logLn(LogLevel.DEBUG, 4, "PDF als Ausgabeformat für die Report-Generierung gewählt.");
					final PdfFactory pdfFactory = new PdfFactory(erzeugeHtmlBuilders(), reportingContext);
					reportResponse = erzeugeResponse(pdfFactory::createPdfResponse,
							"### FEHLER: Während der Erzeugung einer PDF-Response zur Report-Generierung ist ein Fehler geloggt worden.");
				}
				case ReportingAusgabeformat.EMAIL -> {
					this.logger.logLn(LogLevel.DEBUG, 4, "EMAIL als Ausgabeformat für die Report-Generierung gewählt.");
					final PdfFactory pdfFactory = new PdfFactory(erzeugeHtmlBuilders(), reportingContext);
					final EmailFactory emailFactory = new EmailFactory(reportingContext);
					reportResponse = erzeugeResponse(() -> emailFactory.sendEmails(pdfFactory),
							"### FEHLER: Während des E-Mail-Versands (Response) wurde ein Fehler geloggt.");
				}
				case null, default -> {
					logger.logLn(LogLevel.ERROR, 4, "FEHLER: Kein bekanntes Ausgabeformat für die Report-Generierung übergeben.");
					final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, null, sop, MediaType.APPLICATION_JSON);
				}
			}
			// Prüfe nun, ob während der Report-Generierung ein Fehler aufgetreten ist, der als Error ins Log geschrieben wurde, aber nicht als Fehler
			// geworfen wurde.
			pruefeLogAufFehler("### FEHLER: Während der Erzeugung einer API-Response zur Report-Generierung ist ein Fehler geloggt worden.");
			// Wenn kein Fehler vermerkt wurde, kann der Report zurückgegeben werden.
			this.logger.logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung einer API-Response zur Report-Generierung.");
			return reportResponse;
		} catch (final ApiOperationException aoe) {
			// Die ApiOperationException wird mit ihrem ursprünglichen Status-Code weitergereicht, damit dieser nach außen erhalten bleibt.
			throw zuApiOperationException(aoe, FEHLER_RESPONSE, aoe.getStatus());
		} catch (final Exception e) {
			throw zuApiOperationException(e, FEHLER_RESPONSE, Status.INTERNAL_SERVER_ERROR);
		}
	}


	/**
	 * Erzeugt die HtmlFactory für diesen Report. Einziger Ort der Konstruktion, damit ein Umbau der HtmlFactory — insbesondere das Lösen der
	 * Datenladung aus ihrem Konstruktor — nur hier ansetzen muss.
	 *
	 * @return Die HtmlFactory zum Reporting-Context dieses Aufrufs.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	private HtmlFactory erzeugeHtmlFactory() throws ApiOperationException {
		return HtmlFactory.erzeuge(reportingContext);
	}

	/**
	 * Erzeugt die HTML-Builder, aus denen die PDF- und die E-Mail-Ausgabe ihre Dokumente erstellen.
	 *
	 * @return Die Liste der HTML-Builder.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	private List<ReportBuilderHtml> erzeugeHtmlBuilders() throws ApiOperationException {
		final List<ReportBuilderHtml> htmlBuilders = erzeugeHtmlFactory().createHtmlBuilders();
		this.logger.logLn(LogLevel.DEBUG, 4, "HTML-Builder wurden erzeugt.");
		return htmlBuilders;
	}

	/**
	 * Erzeugt die Response eines Ausgabeformats und prüft anschließend, ob während der Erzeugung ein Fehler ins Log geschrieben wurde.
	 * Die Response entsteht innerhalb eines try-with-resources, damit sie im Fehlerfall automatisch geschlossen wird (SonarQube-Vorgabe);
	 * zurückgegeben wird ein Klon, damit die Antwort nicht auf der bereits geschlossenen Ressource sitzt.
	 *
	 * @param erzeuger      Die Methode des jeweiligen Ausgabeformats, die die Response erzeugt. Sie darf eine
	 *                      {@link ApiOperationException} werfen; diese ist ungeprüft und wird unverändert weitergereicht.
	 * @param fehlermeldung Die Meldung, die bei einem im Log vermerkten Fehler protokolliert und geworfen wird.
	 *
	 * @return Die geklonte Response des Ausgabeformats.
	 *
	 * @throws ApiOperationException Im Fehlerfall oder wenn während der Erzeugung ein Fehler geloggt wurde.
	 */
	private Response erzeugeResponse(final Supplier<Response> erzeuger, final String fehlermeldung) throws ApiOperationException {
		try (Response autocloseResponse = erzeuger.get()) {
			pruefeLogAufFehler(fehlermeldung);
			return Response.fromResponse(autocloseResponse).build();
		}
	}

	/**
	 * Prüft, ob im Log ein Fehler vermerkt ist, und bricht die Report-Generierung in diesem Fall mit dem gesammelten Log als Body ab.
	 * Damit werden auch Fehler nach außen sichtbar, die zwar geloggt, aber nicht geworfen wurden.
	 *
	 * @param fehlermeldung Die Meldung, die vor dem Abbruch protokolliert wird.
	 *
	 * @throws ApiOperationException Wenn das Log mindestens einen Eintrag mit {@link LogLevel#ERROR} enthält.
	 */
	private void pruefeLogAufFehler(final String fehlermeldung) throws ApiOperationException {
		if (log.getText(LogLevel.ERROR).isEmpty()) {
			return;
		}
		logger.logLn(LogLevel.ERROR, 0, fehlermeldung);
		final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
		throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, null, sop, MediaType.APPLICATION_JSON);
	}

	/**
	 * Protokolliert einen aufgetretenen Fehler samt Stacktrace, gibt das gesammelte Log für Entwicklungszwecke auf der Konsole aus und
	 * erzeugt die nach außen zu werfende Exception mit dem vollständigen Log als Body.
	 *
	 * @param fehlerursache Die aufgetretene Exception.
	 * @param meldung       Die Meldung, die protokolliert wird.
	 * @param status        Der HTTP-Status der zu erzeugenden Exception. Bei einer durchgereichten {@link ApiOperationException} ist das
	 *                      deren ursprünglicher Status, damit 400/403/404 am API-Rand nicht zu 500 werden.
	 *
	 * @return Die zu werfende {@link ApiOperationException}.
	 */
	private ApiOperationException zuApiOperationException(final Exception fehlerursache, final String meldung, final Status status) {
		ReportingExceptionUtils.logException(meldung, fehlerursache, logger, LogLevel.ERROR, 0);
		final SimpleOperationResponse sop = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log);
		sop.log.forEach(Logger.global()::logLn);
		return new ApiOperationException(status, fehlerursache, sop, MediaType.APPLICATION_JSON);
	}
}
