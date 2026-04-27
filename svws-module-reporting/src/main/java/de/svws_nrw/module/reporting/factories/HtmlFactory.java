package de.svws_nrw.module.reporting.factories;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.builders.ReportBuilderHtml;
import de.svws_nrw.module.reporting.builders.ReportBuilderContextHtml;
import de.svws_nrw.module.reporting.filterung.ReportingFilterDataType;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextKlassen;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextKurse;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextLehrer;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextSchueler;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextBasisdaten;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextStundenplanungFachStundenplan;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextStundenplanungKlassenStundenplan;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextStundenplanungLehrerStundenplan;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextStundenplanungRaumStundenplan;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextStundenplanungSchuelerStundenplan;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextAufteilbar;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.validierung.ReportingValidierung;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * <p>Diese Klasse erstellt HTML-Inhalte auf Basis des in den Reporting-Parametern übergebenen HTML-Templates und der übergebenen Daten.</p>
 * <p>Dabei erstellt die Factory bei der Initialisierung zunächst die Contexts mit den Daten gemäß dem HTML-Template.
 * Zum Erstellen der HTML-Inhalte generiert die Factory einen oder mehrere HTML-Builder, die aus dem Template das fertige HTML erzeugen.</p>
 * <p>Die HTML-Builder können extern weiter verarbeitet werden oder es kann intern eine Response im HTML-Format erzeugt werden.</p>
 */
public class HtmlFactory {

	/** Konstante für die Bezeichnung des Basisdaten-Kontextes in der Map der HTML-Kontexte. */
	private static final String CONTEXT_BASISDATEN = "Basisdaten";

	/** Konstante für die Bezeichnung des Schüler-Kontextes in der Map der HTML-Kontexte. */
	private static final String CONTEXT_SCHUELER = "Schueler";

	/** Konstante für die Bezeichnung des Klassen-Kontextes in der Map der HTML-Kontexte. */
	private static final String CONTEXT_KLASSEN = "Klassen";

	/** Konstante für die Bezeichnung des Kurse-Kontextes in der Map der HTML-Kontexte. */
	private static final String CONTEXT_KURSE = "Kurse";

	/** Konstante für die Bezeichnung des Lehrer-Kontextes in der Map der HTML-Kontexte. */
	private static final String CONTEXT_LEHRER = "Lehrer";

	/** Konstante für die Bezeichnung des GostLaufbahnplanungAbiturjahrgangFachwahlStatistiken-Kontextes in der Map der HTML-Kontexte. */
	private static final String CONTEXT_GOST_LAUFBAHNPLANUNG_FACHWAHLSTATISTIKEN = "GostLaufbahnplanungAbiturjahrgangFachwahlStatistiken";

	/** Konstante für die Bezeichnung des GostBlockungsergebnis-Kontextes in der Map der HTML-Kontexte. */
	private static final String CONTEXT_GOST_BLOCKUNGSERGEBNIS = "GostBlockungsergebnis";

	/** Konstante für die Bezeichnung des GostKlausurplan-Kontextes in der Map der HTML-Kontexte. */
	private static final String CONTEXT_GOST_KLAUSURPLAN = "GostKlausurplan";

	/** Konstante für die Bezeichnung des FaecherStundenplaene-Kontextes in der Map der HTML-Kontexte. */
	private static final String CONTEXT_STUNDENPLANUNG_FAECHER = "FaecherStundenplaene";

	/** Konstante für die Bezeichnung des KlassenStundenplaene-Kontextes in der Map der HTML-Kontexte. */
	private static final String CONTEXT_STUNDENPLANUNG_KLASSEN = "KlassenStundenplaene";

	/** Konstante für die Bezeichnung des LehrerStundenplaene-Kontextes in der Map der HTML-Kontexte. */
	private static final String CONTEXT_STUNDENPLANUNG_LEHRER = "LehrerStundenplaene";

	/** Konstante für die Bezeichnung des RaeumeStundenplaene-Kontextes in der Map der HTML-Kontexte. */
	private static final String CONTEXT_STUNDENPLANUNG_RAEUME = "RaeumeStundenplaene";

	/** Konstante für die Bezeichnung des SchuelerStundenplaene-Kontextes in der Map der HTML-Kontexte. */
	private static final String CONTEXT_STUNDENPLANUNG_SCHUELER = "SchuelerStundenplaene";

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	private final ReportingRepository reportingRepository;

	/** Einstellungen und Daten zum Steuern der Report-Generierung. */
	private final ReportingParameterTypisiert reportingParameter;

	/** Reporting-Reportvorlage für die Erstellung der HTML-Datei. */
	private final ReportingReportvorlage reportingReportvorlage;

	/** Eine Map zum Sammeln der erstellten HTML-Contexts. */
	final Map<String, HtmlContext<?>> mapHtmlContexts = new HashMap<>();


	/**
	 * Erzeugt eine neue HTML-Factory, um eine HTML-Datei aus einem HTML-Template zu erzeugen.
	 *
	 * @param reportingRepository        Repository für das Reporting, welches verschiedene Daten aus der Datenbank zwischenspeichert.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	protected HtmlFactory(final ReportingRepository reportingRepository)
			throws ApiOperationException {

		this.reportingRepository = reportingRepository;
		this.reportingParameter = this.reportingRepository.reportingParameter();

		this.reportingRepository.logger().logLn(LogLevel.DEBUG, 0,
				">>> Beginn der Initialisierung der HTML-Factory und der Validierung der übergebenen Daten.");

		// Validiere die Angaben zur HTML-Vorlage.
		this.reportingReportvorlage = this.reportingParameter.reportVorlage();
		if (this.reportingReportvorlage == null) {
			this.reportingRepository.logger()
					.logLn(LogLevel.ERROR, 4, "FEHLER: Die Template-Definitionen für die HTML-Factory sind inkonsistent.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "FEHLER: Die Template-Definitionen für die HTML-Factory sind inkonsistent.");
		}

		// Prüfe, ob die Rechte des Benutzers zu den in der TemplateDefinition hinterlegten Rechten passen.
		this.reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				"Prüfe die Berechtigungen des Benutzers für den Zugriff auf die für die Ausgabe notwendigen Daten.");
		if (!this.reportingRepository.conn().getUser().pruefeKompetenz(new HashSet<>(reportingReportvorlage.getBenutzerKompetenzen()))) {
			this.reportingRepository.logger()
					.logLn(LogLevel.ERROR, 4,
							"FEHLER: Der Benutzer hat nicht die erforderlichen Rechte, um auf die Daten für die Erstellung der Ausgabe zu zugreifen.");
			throw new ApiOperationException(Status.FORBIDDEN,
					"FEHLER: Der Benutzer hat nicht die erforderlichen Rechte, um auf die Daten für die Erstellung der Ausgabe zu zugreifen.");
		}

		this.reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Initialisierung der HTML-Factory und der Validierung der übergebenen Daten.");

		getContexts();
	}


	/**
	 * Erzeugte die notwendigen Contexts für die HTML-Erstellung auf Basis des angegebenen HTML-Templates.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private void getContexts() throws ApiOperationException {

		reportingRepository.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der Datenkontexte für die HTML-Generierung.");
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Erzeuge Datenkontext Schule für die HTML-Generierung.");

		final HtmlContextBasisdaten htmlContextBasisdaten = new HtmlContextBasisdaten(reportingRepository);
		mapHtmlContexts.put(CONTEXT_BASISDATEN, htmlContextBasisdaten);

		// Betrachte die HTML-Template-Definition und erzeuge damit die korrekten Contexts der Hauptdaten
		switch (reportingReportvorlage.getReportingReportvorlageDatenContext()) {
			case SCHUELER:
				// Schüler-Context ist Hauptdatenquelle
				initContextSchueler();
				break;
			case KLASSEN:
				// Klassen-Context ist Hauptdatenquelle
				initContextKlassen();
				break;
			case KURSE:
				// Kurse-Context ist Hauptdatenquelle
				initContextKurse();
				break;
			case LEHRER:
				// Lehrer-Context ist Hauptdatenquelle
				initContextLehrer();
				break;
			case GOST_KURSPLANUNG:
				// GOSt-Kursplanung-Blockungsergebnis-Context ist Hauptdatenquelle
				initContextGostKursplanung();
				break;
			case GOST_KLAUSURPLANUNG:
				// GOSt-Klausurplanung-Klausurplan-Context ist Hauptdatenquelle
				initContextGostKlausurplanung();
				break;
			case GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG:
				// GOSt-Laufbahnplanung-Abiturjahrgang-Fachwahlstatistiken-Context ist Hauptdatenquelle
				initContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken();
				break;
			case STUNDENPLANUNG:
				// Stundenplan-Context ist Hauptdatenquelle
				initContextStundenplanung();
				break;
			default:
				break;
		}

		reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der Datenkontexte für die HTML-Generierung.");
	}

	/**
	 * Initialisiert den Context für Schüler.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextSchueler() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für Schüler für die HTML-Generierung.");

		final boolean istGostLaufbahnplanung =
				((reportingReportvorlage == ReportingReportvorlage.SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN)
						|| (reportingReportvorlage == ReportingReportvorlage.SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT));
		final boolean istGostAbitur =
				((reportingReportvorlage == ReportingReportvorlage.SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3)
						|| (reportingReportvorlage == ReportingReportvorlage.SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4));

		ReportingValidierung.validiereDatenFuerSchueler(reportingRepository, reportingParameter.idsHauptdaten(), istGostLaufbahnplanung, istGostAbitur);
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				("Erzeuge Datenkontext Schüler für die HTML-Generierung - %d IDs von Schülern wurden übergeben für Template %s.")
						.formatted(reportingParameter.idsHauptdaten().size(), reportingReportvorlage.name()));
		final HtmlContextSchueler htmlContextSchueler = new HtmlContextSchueler(reportingRepository);
		mapHtmlContexts.put(CONTEXT_SCHUELER, htmlContextSchueler);
	}

	/**
	 * Initialisiert den Context für Klassen.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextKlassen() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für Klassen für die HTML-Generierung.");
		ReportingValidierung.validiereDatenFuerKlassen(reportingRepository, reportingParameter.idsHauptdaten());
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				("Erzeuge Datenkontext Klassen für die HTML-Generierung - %d IDs von Klassen wurden übergeben für Template %s.")
						.formatted(reportingParameter.idsHauptdaten().size(), reportingReportvorlage.name()));
		final HtmlContextKlassen htmlContextKlassen = new HtmlContextKlassen(reportingRepository);
		mapHtmlContexts.put(CONTEXT_KLASSEN, htmlContextKlassen);
	}

	/**
	 * Initialisiert den Context für Kurse.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextKurse() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für Kurse für die HTML-Generierung.");
		ReportingValidierung.validiereDatenFuerKurse(reportingRepository, reportingParameter.idsHauptdaten());
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				("Erzeuge Datenkontext Kurse für die HTML-Generierung - %d IDs von Kursen wurden übergeben für Template %s.")
						.formatted(reportingParameter.idsHauptdaten().size(), reportingReportvorlage.name()));
		final HtmlContextKurse htmlContextKurse = new HtmlContextKurse(reportingRepository);
		mapHtmlContexts.put(CONTEXT_KURSE, htmlContextKurse);
	}

	/**
	 * Initialisiert den Context für Lehrer.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextLehrer() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für Lehrer für die HTML-Generierung.");
		ReportingValidierung.validiereDatenFuerLehrer(reportingRepository, reportingParameter.idsHauptdaten());
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				("Erzeuge Datenkontext Lehrer für die HTML-Generierung - %d IDs von Lehrern wurden übergeben für Template %s.")
						.formatted(reportingParameter.idsHauptdaten().size(), reportingReportvorlage.name()));
		final HtmlContextLehrer htmlContextLehrer = new HtmlContextLehrer(reportingRepository);
		mapHtmlContexts.put(CONTEXT_LEHRER, htmlContextLehrer);
	}

	/**
	 * Initialisiert die Fachwahlstatistiken für den Context der GOSt-Laufbahnplanung eines Abiturjahrgangs.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für einen Gost-Laufbahnplan eines Abiturjahrgangs und dessen "
				+ "Fachwahlstatistiken für die HTML-Generierung.");
		ReportingValidierung.validiereDatenFuerGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken(reportingRepository);
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Gost-Laufbahnplan-Abiturjahrgang-Fachwahlstatistiken für die HTML-Generierung mit Template %s."
						.formatted(reportingReportvorlage.name()));
		final HtmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken htmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken =
				new HtmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken(reportingRepository);
		mapHtmlContexts.put(CONTEXT_GOST_LAUFBAHNPLANUNG_FACHWAHLSTATISTIKEN, htmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken);
	}

	/**
	 * Initialisiert den Context für die GOSt-Kursplanung.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextGostKursplanung() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für ein Gost-Blockungsergebnis für die HTML-Generierung.");
		ReportingValidierung.validiereDatenFuerGostKursplanungBlockungsergebnis(reportingRepository);
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Gost-Kursplanung-Blockungsergebnis für die HTML-Generierung mit ID %s für Template %s."
						.formatted(reportingParameter.idHauptdatenObjekt(), reportingReportvorlage.name()));
		final List<Long> idsFilter = this.reportingRepository.reportingParameter().idsHauptdaten();
		final ReportingFilterDataType idsFilterDataType = switch (reportingReportvorlage) {
			case GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN -> ReportingFilterDataType.KURSE;
			case GOST_KURSPLANUNG_V_SCHUELER_MIT_KURSEN, GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN -> ReportingFilterDataType.SCHUELER;
			default -> ReportingFilterDataType.UNDEFINED;
		};
		final HtmlContextGostKursplanungBlockungsergebnis htmlContextGostBlockung =
				new HtmlContextGostKursplanungBlockungsergebnis(reportingRepository, idsFilter, idsFilterDataType);
		mapHtmlContexts.put(CONTEXT_GOST_BLOCKUNGSERGEBNIS, htmlContextGostBlockung);
	}

	/**
	 * Initialisiert den Context für die GOSt-Klausurplanung.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextGostKlausurplanung() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für einen Gost-Klausurplan für die HTML-Generierung.");
		ReportingValidierung.validiereDatenFuerGostKlausurplanungKlausurplan(reportingRepository);
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Gost-Klausurplanung für die HTML-Generierung mit Template %s.".formatted(reportingReportvorlage.name()));
		final List<Long> idsFilter = this.reportingRepository.reportingParameter().idsDetaildaten();
		final ReportingFilterDataType idsFilterDataType = switch (reportingReportvorlage) {
			case GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN -> ReportingFilterDataType.SCHUELER;
			default -> ReportingFilterDataType.UNDEFINED;
		};
		final HtmlContextGostKlausurplanungKlausurplan htmlContextGostKlausurplan = new HtmlContextGostKlausurplanungKlausurplan(reportingRepository,
				idsFilter, idsFilterDataType);
		mapHtmlContexts.put(CONTEXT_GOST_KLAUSURPLAN, htmlContextGostKlausurplan);
	}

	/**
	 * Initialisiert den Context zur Stundenplanung.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextStundenplanung() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für einen Stundenplan für die HTML-Generierung.");
		ReportingValidierung.validiereDatenFuerStundenplanung(reportingRepository);
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Stundenplan für die HTML-Generierung mit Template %s.".formatted(reportingReportvorlage.name()));
		switch (reportingReportvorlage) {
			case STUNDENPLANUNG_V_FACH_STUNDENPLAN -> {
				final HtmlContextStundenplanungFachStundenplan htmlContextFachStundenplan =
						new HtmlContextStundenplanungFachStundenplan(reportingRepository,
								reportingRepository.repositoryStundenplan().stundenplan(reportingParameter.idHauptdatenObjekt()),
								reportingParameter.idsHauptdaten());
				mapHtmlContexts.put(CONTEXT_STUNDENPLANUNG_FAECHER, htmlContextFachStundenplan);
			}
			case STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN -> {
				reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten der Klassen für einen Stundenplan für die HTML-Generierung.");
				ReportingValidierung.validiereDatenFuerKlassen(reportingRepository, reportingParameter.idsHauptdaten());
				final HtmlContextStundenplanungKlassenStundenplan htmlContextKlassenStundenplan =
						new HtmlContextStundenplanungKlassenStundenplan(reportingRepository,
								reportingRepository.repositoryStundenplan().stundenplan(reportingParameter.idHauptdatenObjekt()),
								reportingParameter.idsHauptdaten());
				mapHtmlContexts.put(CONTEXT_STUNDENPLANUNG_KLASSEN, htmlContextKlassenStundenplan);
			}
			case STUNDENPLANUNG_V_LEHRER_STUNDENPLAN, STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT -> {
				reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten der Lehrkräfte für einen Stundenplan für die HTML-Generierung.");
				ReportingValidierung.validiereDatenFuerLehrer(reportingRepository, reportingParameter.idsHauptdaten());
				final HtmlContextStundenplanungLehrerStundenplan htmlContextLehrerStundenplan =
						new HtmlContextStundenplanungLehrerStundenplan(reportingRepository,
								reportingRepository.repositoryStundenplan().stundenplan(reportingParameter.idHauptdatenObjekt()),
								reportingParameter.idsHauptdaten());
				mapHtmlContexts.put(CONTEXT_STUNDENPLANUNG_LEHRER, htmlContextLehrerStundenplan);
			}
			case STUNDENPLANUNG_V_RAUM_STUNDENPLAN -> {
				final HtmlContextStundenplanungRaumStundenplan htmlContextRaeumeStundenplan =
						new HtmlContextStundenplanungRaumStundenplan(reportingRepository,
								reportingRepository.repositoryStundenplan().stundenplan(reportingParameter.idHauptdatenObjekt()),
								reportingParameter.idsHauptdaten());
				mapHtmlContexts.put(CONTEXT_STUNDENPLANUNG_RAEUME, htmlContextRaeumeStundenplan);
			}
			case STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN -> {
				reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten der Schüler für einen Stundenplan für die HTML-Generierung.");
				ReportingValidierung.validiereDatenFuerSchueler(reportingRepository, reportingParameter.idsHauptdaten(), false, false);
				final HtmlContextStundenplanungSchuelerStundenplan htmlContextSchuelerStundenplan =
						new HtmlContextStundenplanungSchuelerStundenplan(reportingRepository,
								reportingRepository.repositoryStundenplan().stundenplan(reportingParameter.idHauptdatenObjekt()),
								reportingParameter.idsHauptdaten());
				mapHtmlContexts.put(CONTEXT_STUNDENPLANUNG_SCHUELER, htmlContextSchuelerStundenplan);
			}
			default -> {
				// Weitere Formate müssen bei der Stundenplanung nicht initiiert werden.
			}
		}
	}


	/**
	 * Erzeugt auf Basis des gegebenen HTML-Templates und der übergebenen Daten die HTML-Builder, aus denen die HTML-Inhalte erzeugt werden können.
	 *
	 * @return Eine Liste mit ReportBuilderHtml-Instanzen.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	protected List<ReportBuilderHtml> createHtmlBuilders() throws ApiOperationException {
		return getHtmlBuilders();
	}


	/**
	 * Erstellt eine Response in Form einer einzelnen HTML-Datei oder eine einzelne ZIP-Datei, die mehrere generierte HTML-Dateien enthält.
	 *
	 * @return Im Falle eines Success enthält die HTTP-Response das HTML-Dokument oder die ZIP-Datei.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	protected Response createHtmlResponse() throws ApiOperationException {
		try {
			reportingRepository.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung.");
			final List<ReportBuilderHtml> htmlBuilders = getHtmlBuilders();
			if (!htmlBuilders.isEmpty()) {
				final ReportBuilderHtml firstHtmlBuilder = htmlBuilders.getFirst();
				if (htmlBuilders.size() == 1) {
					final String html = firstHtmlBuilder.generate();
					final String encodedFilename = "filename*=UTF-8''" + URLEncoder.encode(firstHtmlBuilder.getDateinameMitEndung(), StandardCharsets.UTF_8);
					reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung.");
					return Response.ok(html, firstHtmlBuilder.getContentType()).header("Content-Disposition", "attachment; " + encodedFilename).build();
				} else {
					final byte[] data = createZIP(htmlBuilders);
					final String encodedFilename =
							"filename*=UTF-8''" + URLEncoder.encode(firstHtmlBuilder.getStatischerDateiname() + ".zip", StandardCharsets.UTF_8);
					reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung.");
					return Response.ok(data, "application/zip").header("Content-Disposition", "attachment; " + encodedFilename).build();
				}

			}
			reportingRepository.logger().logLn(LogLevel.ERROR, 0,
					"### Fehler bei der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung. Es sind keine HTML-Inhalte generiert worden.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"### Fehler bei der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung. Es sind keine HTML-Inhalte generiert worden.");
		} catch (final Exception e) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 0, "### Fehler bei der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung.");
			throw e;
		}
	}


	/**
	 * Erzeugt auf Basis der übergebenen HTML-Vorlage und Daten die HTML-Inhalte der Dateien und legt diese Inhalte in einer Liste ab.
	 *
	 * @return Eine Liste mit ReportBuilderHtml-Instanzen.
	 *
	 * @throws ApiOperationException Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private List<ReportBuilderHtml> getHtmlBuilders() throws ApiOperationException {

		reportingRepository.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der HTML-Builder.");

		// Lade den Inhalt des HTML-Codes aus dem Template.
		final String htmlTemplateCode = ResourceUtils.text(reportingReportvorlage.getRootPfadHtmlTemplate());
		if (htmlTemplateCode == null) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "### FEHLER: Die HTML-Template-Datei für die HTML-Erzeugung konnte nicht eingelesen werden.");
			throw new ApiOperationException(Status.NOT_FOUND, "### FEHLER: Die HTML-Template-Datei für die HTML-Erzeugung konnte nicht eingelesen werden.");
		}

		final List<ReportBuilderHtml> htmlBuilders = new ArrayList<>();

		if (reportingParameter.einzelausgabeDaten()) {
			erzeugeEinzelContexts(htmlBuilders, htmlTemplateCode);
		} else {
			htmlBuilders.add(getReportBuilderHtml(htmlTemplateCode));
		}
		reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der HTML-Builder.");
		return htmlBuilders;
	}

	/**
	 * Erstellt einzelne Haupt-Kontexte auf Basis der gegebenen Hauptdatenquelle, um separate HTML-Dateien zu generieren.
	 *
	 * @param htmlBuilders     Eine Liste von {@code ReportBuilderHtml}-Objekten, in die die erzeugten HTML-Inhalte gespeichert werden.
	 * @param htmlTemplateCode Der HTML-Template-Code, der beim Generieren der HTML-Inhalte verwendet wird.
	 *
	 * @throws ApiOperationException Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private void erzeugeEinzelContexts(final List<ReportBuilderHtml> htmlBuilders, final String htmlTemplateCode) throws ApiOperationException {
		final String contextBezeichnung = ermittleEinzelContextBezeichnung();
		final HtmlContext<?> baseContext = mapHtmlContexts.get(contextBezeichnung);

		if (baseContext instanceof final HtmlContextAufteilbar<?> aufteilbarerContext) {
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
					"Erzeuge einzelne Kontexte für " + contextBezeichnung + " für Template " + reportingReportvorlage.name());

			final List<? extends HtmlContext<?>> einzelContexts = aufteilbarerContext.getEinzelContexts();

			for (final HtmlContext<?> einzelContext : einzelContexts) {
				mapHtmlContexts.put(contextBezeichnung, einzelContext);
				htmlBuilders.add(getReportBuilderHtml(htmlTemplateCode));
			}
		} else {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"FEHLER: Der Kontext " + contextBezeichnung + " unterstützt das Aufteilen in Einzeldokumente nicht.");
		}
	}

	/**
	 * Ermittelt die Bezeichnung für den einzelnen Kontext basierend auf der ReportingReportvorlage.
	 *
	 * @return Die Bezeichnung des einzelnen Kontextes.
	 */
	private String ermittleEinzelContextBezeichnung() {
		return switch (this.reportingReportvorlage.getReportingReportvorlageDatenContext()) {
			case SCHUELER -> CONTEXT_SCHUELER;
			case KLASSEN -> CONTEXT_KLASSEN;
			case KURSE -> CONTEXT_KURSE;
			case LEHRER -> CONTEXT_LEHRER;
			case GOST_KURSPLANUNG -> CONTEXT_GOST_BLOCKUNGSERGEBNIS;
			case GOST_KLAUSURPLANUNG -> CONTEXT_GOST_KLAUSURPLAN;
			case STUNDENPLANUNG -> switch (this.reportingReportvorlage) {
				// Bei der Stundenplanung erfolgt die Zuordnung anhand der Reportvorlage.
				case STUNDENPLANUNG_V_FACH_STUNDENPLAN -> CONTEXT_STUNDENPLANUNG_FAECHER;
				case STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN -> CONTEXT_STUNDENPLANUNG_KLASSEN;
				case STUNDENPLANUNG_V_LEHRER_STUNDENPLAN, STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT -> CONTEXT_STUNDENPLANUNG_LEHRER;
				case STUNDENPLANUNG_V_RAUM_STUNDENPLAN -> CONTEXT_STUNDENPLANUNG_RAEUME;
				case STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN -> CONTEXT_STUNDENPLANUNG_SCHUELER;
				default -> throw new ApiOperationException(Status.BAD_REQUEST,
						"FEHLER: Die Vorlage " + this.reportingReportvorlage.getBezeichnung() + " unterstützt keine Einzelausgabe.");
			};
			default ->
				throw new ApiOperationException(Status.BAD_REQUEST,
						"FEHLER: Die Vorlage " + this.reportingReportvorlage.getBezeichnung() + " unterstützt keine Einzelausgabe.");
		};
	}

	/**
	 * Ermittelt die IDs aus den in der HTML-Factory vorhandenen HTML-Kontexten. Die Methode iteriert über die Einträge der Map, sammelt die IDs aus den
	 * Kontexten, entfernt Duplikate und gibt die eindeutigen IDs zurück.
	 *
	 * @return Eine Liste von eindeutigen {@code Long}-IDs, die aus den HTML-Kontexten gesammelt wurden.
	 */
	private List<Long> getContextsIds() {
		final List<Long> ids = new ArrayList<>();

		for (final Map.Entry<String, HtmlContext<?>> entry : mapHtmlContexts.entrySet()) {
			final HtmlContext<?> context = entry.getValue();
			if (context == null) {
				continue;
			}
			ids.addAll(context.getIds());
		}

		return ids.stream().filter(Objects::nonNull).distinct().toList();
	}

	/**
	 * Erstellt eine Instanz von ReportBuilderHtml basierend auf dem angegebenen HTML-Template-Code und spezifischen Kontextinformationen.
	 *
	 * @param htmlTemplateCode Der Code des HTML-Templates, das für den Reportbuilder verwendet werden soll.
	 *
	 * @return Eine Instanz von ReportBuilderHtml, die auf der Basis des angegebenen HTML-Templates erstellt wurde.
	 *
	 * @throws ApiOperationException Eine Ausnahme wird geworfen, wenn ein Fehler beim Erstellen des ReportBuilderHtml auftritt.
	 */
	private ReportBuilderHtml getReportBuilderHtml(final String htmlTemplateCode) throws ApiOperationException {
		final ReportBuilderContextHtml reportBuilderContext =
				new ReportBuilderContextHtml()
						.withHtmlTemplate(htmlTemplateCode)
						.addHtmlContexts(mapHtmlContexts.values().stream().toList())
						.addIds(getContextsIds())
						.withDateinamensvorlage(ladeDateinamensvorlageAusDatei(reportingReportvorlage.getPfadDateinamensvorlage()))
						.withStatischerDateiname(reportingReportvorlage.getDateiname())
						.withRootPfad(ReportingReportvorlage.getRootPfad())
						.withLogger(reportingRepository.logger());
		return new ReportBuilderHtml(reportBuilderContext);
	}

	/**
	 * Lädt die Vorlage für den Dateinamen aus einer ".name.tpl" Datei.
	 *
	 * @param pfadNameTemplate Der relative Pfad zur Namensvorlage-Datei
	 * @return Die geladene Vorlage oder ein leerer String bei einem Fehler.
	 */
	private static String ladeDateinamensvorlageAusDatei(final String pfadNameTemplate) {
		final String vollPfad = ReportingReportvorlage.getRootPfad() + pfadNameTemplate;
		try {
			final String content = ResourceUtils.text(vollPfad);
			if (content == null) {
				return "";
			}
			return content;
		} catch (final Exception e) {
			return "";
		}
	}


	/**
	 * Erstellt eine ZIP-Datei, die alle HTML-Dateien aus der übergebenen Liste enthält.
	 *
	 * @param htmlBuilders                Eine Liste mit den ReportBuilderHtml-Instanzen, die die HTML-Inhalte erzeugen.
	 *
	 * @return Gibt das ZIP in Form eines ByteArrays zurück.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private byte[] createZIP(final List<ReportBuilderHtml> htmlBuilders) throws ApiOperationException {
		final byte[] zipData;
		try {
			try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
				try (ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream)) {
					for (final ReportBuilderHtml htmlBuilder : htmlBuilders) {
						addHtmlToZip(htmlBuilder, zos);
					}
					byteArrayOutputStream.flush();
				}
				zipData = byteArrayOutputStream.toByteArray();
			}
		} catch (final IOException e) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Die erzeugten HTML-Inhalte konnten nicht als ZIP-Datei zusammengestellt werden.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e,
					"FEHLER: Die erzeugten HTML-Inhalte konnten nicht als ZIP-Datei zusammengestellt werden.");
		}
		return zipData;
	}

	/**
	 * Fügt eine HTML-Datei in den angegebenen ZipOutputStream ein. Dabei wird der Name und Inhalt der Datei aus dem übergebenen ReportBuilderHtml-Objekt gelesen.
	 *
	 * @param htmlBuilder Das Objekt, das die HTML-Daten und den Dateinamen bereitstellt
	 * @param zos Der ZipOutputStream, in den die HTML-Datei eingefügt wird
	 *
	 * @throws ApiOperationException Wird geworfen, wenn ein Fehler beim Generieren der HTML-Datei vorliegt oder ein Laufzeitfehler auftritt
	 */
	private void addHtmlToZip(final ReportBuilderHtml htmlBuilder, final ZipOutputStream zos) throws ApiOperationException {
		try {
			zos.putNextEntry(new ZipEntry(htmlBuilder.getDateinameMitEndung()));
			zos.write(htmlBuilder.getByteArray());
			zos.closeEntry();
		} catch (final Exception e) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4,
					"FEHLER: HTML-Datei '" + htmlBuilder.getDateiname() + "' konnte mit folgender Fehlermeldung nicht generiert werden: " + e.getMessage());
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e,
					"## FEHLER: HTML-Datei '" + htmlBuilder.getDateiname() + "' konnte mit folgender Fehlermeldung nicht generiert werden: " + e.getMessage());
		}
	}

}
