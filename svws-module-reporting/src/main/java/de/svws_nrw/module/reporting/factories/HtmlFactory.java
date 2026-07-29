package de.svws_nrw.module.reporting.factories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.builders.ReportBuilderHtml;
import de.svws_nrw.module.reporting.builders.ReportBuilderContextHtml;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostKlausurplanungKlausurplanSchueler;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostKlausurplanungKlausurplanTermine;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostKursplanungBlockungsergebnisKurse;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostKursplanungBlockungsergebnisSchueler;
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
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
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

	private static final String SCHUELER_IDS = "Schüler-IDs";

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	private final ReportingContext reportingContext;

	/** Einstellungen und Daten zum Steuern der Report-Generierung. */
	private final ReportingParameterTypisiert reportingParameter;

	/** Reporting-Reportvorlage für die Erstellung der HTML-Datei. */
	private final ReportingReportvorlage reportingReportvorlage;

	/** Eine Map zum Sammeln der erstellten HTML-Contexts. */
	final Map<String, HtmlContext<?>> mapHtmlContexts = new HashMap<>();


	/**
	 * Erzeugt eine neue HTML-Factory, um eine HTML-Datei aus einem HTML-Template zu erzeugen.
	 *
	 * @param reportingContext        Repository für das Reporting, welches verschiedene Daten aus der Datenbank zwischenspeichert.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	protected HtmlFactory(final ReportingContext reportingContext)
			throws ApiOperationException {

		this.reportingContext = reportingContext;
		this.reportingParameter = this.reportingContext.reportingParameter();

		this.reportingContext.logger().logLn(LogLevel.DEBUG, 0,
				">>> Beginn der Initialisierung der HTML-Factory und der Validierung der übergebenen Daten.");

		// Validiere die Angaben zur HTML-Vorlage.
		this.reportingReportvorlage = this.reportingParameter.reportVorlage();
		if (this.reportingReportvorlage == null) {
			this.reportingContext.logger()
					.logLn(LogLevel.ERROR, 4, "FEHLER: Die Template-Definitionen für die HTML-Factory sind inkonsistent.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "FEHLER: Die Template-Definitionen für die HTML-Factory sind inkonsistent.");
		}

		// Prüfe, ob die Rechte des Benutzers zu den in der TemplateDefinition hinterlegten Rechten passen.
		this.reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				"Prüfe die Berechtigungen des Benutzers für den Zugriff auf die für die Ausgabe notwendigen Daten.");
		if (!this.reportingContext.benutzer().pruefeKompetenz(reportingReportvorlage.getBenutzerKompetenzen())) {
			this.reportingContext.logger()
					.logLn(LogLevel.ERROR, 4,
							"FEHLER: Der Benutzer hat nicht die erforderlichen Rechte, um auf die Daten für die Erstellung der Ausgabe zu zugreifen.");
			throw new ApiOperationException(Status.FORBIDDEN,
					"FEHLER: Der Benutzer hat nicht die erforderlichen Rechte, um auf die Daten für die Erstellung der Ausgabe zu zugreifen.");
		}

		this.reportingContext.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Initialisierung der HTML-Factory und der Validierung der übergebenen Daten.");

		getContexts();
	}


	/**
	 * Erzeugte die notwendigen Contexts für die HTML-Erstellung auf Basis des angegebenen HTML-Templates.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private void getContexts() throws ApiOperationException {

		reportingContext.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der Datenkontexte für die HTML-Generierung.");
		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Erzeuge Datenkontext Schule für die HTML-Generierung.");

		final HtmlContextBasisdaten htmlContextBasisdaten = new HtmlContextBasisdaten(reportingContext);
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

		reportingContext.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der Datenkontexte für die HTML-Generierung.");
	}

	/**
	 * Initialisiert den Context für Schüler.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextSchueler() throws ApiOperationException {
		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für Schüler für die HTML-Generierung.");

		final List<Long> idsSchueler = reportingParameter.idsHauptdaten();
		validiereIds(idsSchueler, reportingContext.repositorySchueler().schueler(idsSchueler, false), ReportingSchueler::id,
				SCHUELER_IDS, "FEHLER: Es wurden ungültige Schüler-IDs übergeben.");

		final boolean istGostLaufbahnplanung =
				((reportingReportvorlage == ReportingReportvorlage.SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN)
						|| (reportingReportvorlage == ReportingReportvorlage.SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT));
		final boolean istGostAbitur =
				((reportingReportvorlage == ReportingReportvorlage.SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3)
						|| (reportingReportvorlage == ReportingReportvorlage.SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4));

		if (istGostLaufbahnplanung || istGostAbitur) {
			validiereSchuleMitGost();
		}
		if (istGostLaufbahnplanung) {
			validiereIds(idsSchueler, reportingContext.repositoryGost().beratungsdaten(idsSchueler),
					SCHUELER_IDS, "FEHLER: Es wurden Schüler-IDs übergeben, die nicht zur GOSt gehören.");
			validiereIds(idsSchueler, reportingContext.repositoryGost().beratungsdatenAbiturdaten(idsSchueler),
					SCHUELER_IDS, "FEHLER: Es wurden Schüler-IDs übergeben, für die keine Abiturdaten in der GOSt-Laufbahnplanung existieren.");
		}
		if (istGostAbitur) {
			validiereIds(idsSchueler, reportingContext.repositoryGost().schuelerAbiturdaten(idsSchueler),
					SCHUELER_IDS,
					"FEHLER: Es wurden Schüler-IDs übergeben, für die keine Abiturdaten in der GOSt existieren.");
		}

		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				("Erzeuge Datenkontext Schüler für die HTML-Generierung - %d IDs von Schülern wurden übergeben für Template %s.")
						.formatted(idsSchueler.size(), reportingReportvorlage.name()));
		final HtmlContextSchueler htmlContextSchueler = new HtmlContextSchueler(reportingContext);
		mapHtmlContexts.put(CONTEXT_SCHUELER, htmlContextSchueler);
	}

	/**
	 * Initialisiert den Context für Klassen.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextKlassen() throws ApiOperationException {
		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für Klassen für die HTML-Generierung.");
		final List<Long> idsKlassen = reportingParameter.idsHauptdaten();
		validiereIds(idsKlassen, reportingContext.repositoryLerngruppen().klassen(idsKlassen, false), ReportingKlasse::id,
				"Klassen-IDs", "FEHLER: Es wurden ungültige Klassen-IDs übergeben.");
		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				("Erzeuge Datenkontext Klassen für die HTML-Generierung - %d IDs von Klassen wurden übergeben für Template %s.")
						.formatted(idsKlassen.size(), reportingReportvorlage.name()));
		final HtmlContextKlassen htmlContextKlassen = new HtmlContextKlassen(reportingContext);
		mapHtmlContexts.put(CONTEXT_KLASSEN, htmlContextKlassen);
	}

	/**
	 * Initialisiert den Context für Kurse.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextKurse() throws ApiOperationException {
		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für Kurse für die HTML-Generierung.");
		final List<Long> idsKurse = reportingParameter.idsHauptdaten();
		validiereIds(idsKurse, reportingContext.repositoryLerngruppen().kurse(idsKurse, false), ReportingKurs::id,
				"Kurs-IDs", "FEHLER: Es wurden ungültige Kurs-IDs übergeben.");
		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				("Erzeuge Datenkontext Kurse für die HTML-Generierung - %d IDs von Kursen wurden übergeben für Template %s.")
						.formatted(idsKurse.size(), reportingReportvorlage.name()));
		final HtmlContextKurse htmlContextKurse = new HtmlContextKurse(reportingContext);
		mapHtmlContexts.put(CONTEXT_KURSE, htmlContextKurse);
	}

	/**
	 * Initialisiert den Context für Lehrer.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextLehrer() throws ApiOperationException {
		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für Lehrer für die HTML-Generierung.");
		final List<Long> idsLehrer = reportingParameter.idsHauptdaten();
		validiereIds(idsLehrer, reportingContext.repositoryLehrer().lehrer(idsLehrer, false), ReportingLehrer::id,
				"Lehrer-IDs", "FEHLER: Es wurden ungültige Lehrer-IDs übergeben.");
		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				("Erzeuge Datenkontext Lehrer für die HTML-Generierung - %d IDs von Lehrern wurden übergeben für Template %s.")
						.formatted(idsLehrer.size(), reportingReportvorlage.name()));
		final HtmlContextLehrer htmlContextLehrer = new HtmlContextLehrer(reportingContext);
		mapHtmlContexts.put(CONTEXT_LEHRER, htmlContextLehrer);
	}

	/**
	 * Initialisiert die Fachwahlstatistiken für den Context der GOSt-Laufbahnplanung eines Abiturjahrgangs.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken() throws ApiOperationException {
		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				"Validiere die Daten für einen Gost-Laufbahnplan eines Abiturjahrgangs und dessen Fachwahlstatistiken für die HTML-Generierung.");
		validiereSchuleMitGost();
		validiereParameterFuerAbiturjahrgangUndHalbjahre(false);
		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Gost-Laufbahnplan-Abiturjahrgang-Fachwahlstatistiken für die HTML-Generierung mit Template %s."
						.formatted(reportingReportvorlage.name()));
		final HtmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken htmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken =
				new HtmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken(reportingContext);
		mapHtmlContexts.put(CONTEXT_GOST_LAUFBAHNPLANUNG_FACHWAHLSTATISTIKEN, htmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken);
	}

	/**
	 * Initialisiert den Context für die GOSt-Kursplanung.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextGostKursplanung() throws ApiOperationException {
		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für ein Gost-Blockungsergebnis für die HTML-Generierung.");
		validiereSchuleMitGost();
		if (reportingParameter.idHauptdatenObjekt() < 0) {
			reportingContext.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurde keine ID für ein Blockungsergebnis übergeben.");
			throw new ApiOperationException(Status.BAD_REQUEST, "FEHLER: Es wurde keine ID für ein Blockungsergebnis übergeben.");
		}
		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Gost-Kursplanung-Blockungsergebnis für die HTML-Generierung mit ID %s für Template %s."
						.formatted(reportingParameter.idHauptdatenObjekt(), reportingReportvorlage.name()));
		final HtmlContextGostKursplanungBlockungsergebnis htmlContextGostBlockung = switch (reportingReportvorlage) {
			case GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN, GOST_KURSPLANUNG_V_KURSE_MIT_STATISTIKWERTEN ->
				new HtmlContextGostKursplanungBlockungsergebnisKurse(reportingContext);
			case GOST_KURSPLANUNG_V_SCHUELER_MIT_KURSEN, GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN ->
				new HtmlContextGostKursplanungBlockungsergebnisSchueler(reportingContext);
			default -> throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR,
					"FEHLER: Für die Reportvorlage %s ist im Datenkontext GOSt-Kursplanung kein HTML-Context implementiert.".formatted(
							reportingReportvorlage.name()));
		};
		mapHtmlContexts.put(CONTEXT_GOST_BLOCKUNGSERGEBNIS, htmlContextGostBlockung);
	}

	/**
	 * Initialisiert den Context für die GOSt-Klausurplanung.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextGostKlausurplanung() throws ApiOperationException {
		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für einen Gost-Klausurplan für die HTML-Generierung.");
		validiereSchuleMitGost();
		if (!reportingParameter.idsHauptdaten().isEmpty()) {
			validiereParameterFuerAbiturjahrgangUndHalbjahre(true);
		}
		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Gost-Klausurplanung für die HTML-Generierung mit Template %s.".formatted(reportingReportvorlage.name()));
		final HtmlContextGostKlausurplanungKlausurplan htmlContextGostKlausurplan = switch (reportingReportvorlage) {
			case GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN -> new HtmlContextGostKlausurplanungKlausurplanSchueler(reportingContext);
			case GOST_KLAUSURPLANUNG_V_KLAUSURTERMINE_MIT_KURSEN -> new HtmlContextGostKlausurplanungKlausurplanTermine(reportingContext);
			default -> throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR,
					"FEHLER: Für die Reportvorlage %s ist im Datenkontext GOSt-Klausurplanung kein HTML-Context implementiert.".formatted(
							reportingReportvorlage.name()));
		};
		mapHtmlContexts.put(CONTEXT_GOST_KLAUSURPLAN, htmlContextGostKlausurplan);
	}

	/**
	 * Initialisiert den Context zur Stundenplanung.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextStundenplanung() throws ApiOperationException {
		reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für einen Stundenplan für die HTML-Generierung.");
		final ReportingStundenplanungStundenplan stundenplan = reportingContext.repositoryStundenplan().stundenplan(reportingParameter.idHauptdatenObjekt());
		if (stundenplan == null) {
			this.reportingContext.logger().logLn(LogLevel.ERROR, 4,
					"FEHLER: Mit der angegebenen Stundenplan-ID konnte kein Stundenplan ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Mit der angegebenen Stundenplan-ID konnte kein Stundenplan ermittelt werden.");
		}
		reportingContext.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Stundenplan für die HTML-Generierung mit Template %s.".formatted(reportingReportvorlage.name()));
		switch (reportingReportvorlage) {
			case STUNDENPLANUNG_V_FACH_STUNDENPLAN -> {
				final HtmlContextStundenplanungFachStundenplan htmlContextFachStundenplan =
						new HtmlContextStundenplanungFachStundenplan(reportingContext, stundenplan, reportingParameter.idsHauptdaten());
				mapHtmlContexts.put(CONTEXT_STUNDENPLANUNG_FAECHER, htmlContextFachStundenplan);
			}
			case STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN -> {
				reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten der Klassen für einen Stundenplan für die HTML-Generierung.");
				validiereIds(reportingParameter.idsHauptdaten(),
						reportingContext.repositoryLerngruppen().klassen(reportingParameter.idsHauptdaten(), false), ReportingKlasse::id,
						"Klassen-IDs", "FEHLER: Es wurden ungültige Klassen-IDs übergeben.");
				final HtmlContextStundenplanungKlassenStundenplan htmlContextKlassenStundenplan =
						new HtmlContextStundenplanungKlassenStundenplan(reportingContext, stundenplan, reportingParameter.idsHauptdaten());
				mapHtmlContexts.put(CONTEXT_STUNDENPLANUNG_KLASSEN, htmlContextKlassenStundenplan);
			}
			case STUNDENPLANUNG_V_LEHRER_STUNDENPLAN, STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT -> {
				reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten der Lehrkräfte für einen Stundenplan für die HTML-Generierung.");
				validiereIds(reportingParameter.idsHauptdaten(),
						reportingContext.repositoryLehrer().lehrer(reportingParameter.idsHauptdaten(), false), ReportingLehrer::id,
						"Lehrer-IDs", "FEHLER: Es wurden ungültige Lehrer-IDs übergeben.");
				final HtmlContextStundenplanungLehrerStundenplan htmlContextLehrerStundenplan =
						new HtmlContextStundenplanungLehrerStundenplan(reportingContext, stundenplan, reportingParameter.idsHauptdaten());
				mapHtmlContexts.put(CONTEXT_STUNDENPLANUNG_LEHRER, htmlContextLehrerStundenplan);
			}
			case STUNDENPLANUNG_V_RAUM_STUNDENPLAN -> {
				final HtmlContextStundenplanungRaumStundenplan htmlContextRaeumeStundenplan =
						new HtmlContextStundenplanungRaumStundenplan(reportingContext, stundenplan, reportingParameter.idsHauptdaten());
				mapHtmlContexts.put(CONTEXT_STUNDENPLANUNG_RAEUME, htmlContextRaeumeStundenplan);
			}
			case STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN -> {
				reportingContext.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten der Schüler für einen Stundenplan für die HTML-Generierung.");
				validiereIds(reportingParameter.idsHauptdaten(),
						reportingContext.repositorySchueler().schueler(reportingParameter.idsHauptdaten(), false), ReportingSchueler::id,
						SCHUELER_IDS, "FEHLER: Es wurden ungültige Schüler-IDs übergeben.");
				final HtmlContextStundenplanungSchuelerStundenplan htmlContextSchuelerStundenplan =
						new HtmlContextStundenplanungSchuelerStundenplan(reportingContext, stundenplan, reportingParameter.idsHauptdaten());
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
	 * Erstellt eine Response in Form einer einzelnen HTML-Datei, die für die Anzeige in einem Browser verwendet werden kann.
	 *
	 * @return Im Falle eines Success enthält die HTTP-Response das HTML-Dokument.
	 *
	 * @throws ApiOperationException    Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	protected Response createHtmlResponse() throws ApiOperationException {
		try {
			reportingContext.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung.");
			final List<ReportBuilderHtml> htmlBuilders = getHtmlBuilders();
			if (!htmlBuilders.isEmpty()) {
				final ReportBuilderHtml firstHtmlBuilder = htmlBuilders.getFirst();
				if (htmlBuilders.size() == 1) {
					final String html = firstHtmlBuilder.generate();
					reportingContext.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung.");
					return Response.ok(html, "text/html; charset=UTF-8").header("Cache-Control", "no-store").build();
				} else {
					// Reine Absicherung: Der Zweig ist unerreichbar, seit ReportingParameterBuilder die Aufteilung in Einzeldateien für die
					// HTML-Ausgabe auf dem fertig kombinierten Parametersatz auf false festlegt - ohne Aufteilung entsteht genau ein Builder.
					reportingContext.logger().logLn(LogLevel.ERROR, 0,
							"### Fehler bei der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung. Es wurde mehr als ein Builder übergeben.");
					throw new ApiOperationException(Status.BAD_REQUEST,
							"### Fehler bei der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung. Es wurde mehr als ein Builder übergeben.");
				}
			}
			reportingContext.logger().logLn(LogLevel.ERROR, 0,
					"### Fehler bei der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung. Es sind keine HTML-Inhalte generiert worden.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"### Fehler bei der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung. Es sind keine HTML-Inhalte generiert worden.");
		} catch (final Exception e) {
			reportingContext.logger().logLn(LogLevel.ERROR, 0, "### Fehler bei der Erzeugung der Response einer API-Anfrage für eine HTML-Generierung.");
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

		reportingContext.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der HTML-Builder.");

		// Lade den Inhalt des HTML-Codes aus dem Template.
		final String htmlTemplateCode = ResourceUtils.text(reportingReportvorlage.getRootPfadHtmlTemplate());
		if (htmlTemplateCode == null) {
			reportingContext.logger().logLn(LogLevel.ERROR, 4, "### FEHLER: Die HTML-Template-Datei für die HTML-Erzeugung konnte nicht eingelesen werden.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"### FEHLER: Die HTML-Template-Datei für die HTML-Erzeugung konnte nicht eingelesen werden.");
		}

		final List<ReportBuilderHtml> htmlBuilders = new ArrayList<>();

		if (reportingParameter.einzelausgabeDaten()) {
			erzeugeEinzelContexts(htmlBuilders, htmlTemplateCode);
		} else {
			htmlBuilders.add(getReportBuilderHtml(htmlTemplateCode));
		}
		reportingContext.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der HTML-Builder.");
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
			reportingContext.logger().logLn(LogLevel.DEBUG, 4,
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

		for (final HtmlContext<?> context : mapHtmlContexts.values()) {
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
						.withLogger(reportingContext.logger());
		return new ReportBuilderHtml(reportBuilderContext);
	}

	/**
	 * Lädt die Vorlage für den Dateinamen aus einer ".name.tpl" Datei.
	 *
	 * @param pfadNameTemplate Der relative Pfad zur Namensvorlage-Datei
	 * @return Die geladene Vorlage oder ein leerer String bei einem Fehler.
	 */
	private static String ladeDateinamensvorlageAusDatei(final String pfadNameTemplate) {
		return ResourceUtils.textOrEmpty(ReportingReportvorlage.getRootPfad() + pfadNameTemplate);
	}

	/**
	 * Bereinigt die übergebene Liste von IDs (null-Einträge und Duplikate entfernen) und prüft anschließend, dass die bereinigte Liste nicht leer ist
	 * und zu jeder enthaltenen ID ein passendes Objekt in {@code geladeneObjekte} existiert.
	 * Die IDs der geladenen Objekte werden mittels des übergebenen {@code idExtractor} bestimmt.
	 * Im Fehlerfall wird die zugehörige Meldung geloggt und eine {@link ApiOperationException} mit Status {@link Status#BAD_REQUEST} geworfen.
	 *
	 * @param <T>                         Typ der geladenen Objekte.
	 * @param idsUebergeben               Liste der übergebenen IDs (kann null-Einträge und Duplikate enthalten).
	 * @param geladeneObjekte             Die zu den IDs geladenen Objekte.
	 * @param idExtractor                 Funktion zur Bestimmung der ID eines geladenen Objekts.
	 * @param fehlermeldungIdTyp          Fehlermeldung, falls die bereinigte ID-Liste leer ist.
	 * @param fehlermeldungUnvollstaendig Fehlermeldung, falls eine bereinigte ID nicht in {@code geladeneObjekte} enthalten ist.
	 *
	 * @throws ApiOperationException Falls die bereinigte Liste leer oder unvollständig ist.
	 */
	private <T> void validiereIds(final List<Long> idsUebergeben, final Collection<T> geladeneObjekte, final ToLongFunction<T> idExtractor,
			final String fehlermeldungIdTyp, final String fehlermeldungUnvollstaendig) throws ApiOperationException {
		final Set<Long> idsVorhanden = geladeneObjekte.stream().mapToLong(idExtractor).boxed().collect(Collectors.toSet());
		validiereIds(idsUebergeben, idsVorhanden, fehlermeldungIdTyp, fehlermeldungUnvollstaendig);
	}

	/**
	 * Wie {@link #validiereIds(List, Collection, ToLongFunction, String, String)}, jedoch für Map-basierte Lade-Ergebnisse: eine ID gilt als
	 * vorhanden, wenn der zugehörige Map-Eintrag einen Wert ungleich {@code null} besitzt.
	 *
	 * @param <V>                         Typ der Map-Werte.
	 * @param idsUebergeben               Liste der übergebenen IDs (kann null-Einträge und Duplikate enthalten).
	 * @param geladeneObjekte             Map mit ID als Schlüssel und dem geladenen Objekt als Wert (Wert {@code null} bedeutet "nicht vorhanden").
	 * @param fehlermeldungIdTyp          Fehlermeldung, falls die bereinigte ID-Liste leer ist.
	 * @param fehlermeldungUnvollstaendig Fehlermeldung, falls für eine bereinigte ID kein Eintrag mit Wert ungleich {@code null} existiert.
	 *
	 * @throws ApiOperationException Falls die bereinigte Liste leer oder unvollständig ist.
	 */
	private <V> void validiereIds(final List<Long> idsUebergeben, final Map<Long, V> geladeneObjekte,
			final String fehlermeldungIdTyp, final String fehlermeldungUnvollstaendig) throws ApiOperationException {
		final Set<Long> idsVorhanden = geladeneObjekte.entrySet().stream()
				.filter(e -> e.getValue() != null).map(Map.Entry::getKey).collect(Collectors.toSet());
		validiereIds(idsUebergeben, idsVorhanden, fehlermeldungIdTyp, fehlermeldungUnvollstaendig);
	}

	/**
	 * Bereinigt die übergebene Roh-Liste von IDs (null-Einträge und Duplikate entfernen) und prüft anschließend, dass die bereinigte Liste nicht leer ist
	 * und jede enthaltene ID in {@code idsVorhanden} existiert. Im Fehlerfall wird die zugehörige Meldung geloggt und eine
	 * {@link ApiOperationException} mit Status {@link Status#BAD_REQUEST} geworfen.
	 *
	 * @param idsUebergeben               Liste der übergebenen IDs (kann null-Einträge und Duplikate enthalten).
	 * @param idsVorhanden                Menge der tatsächlich vorhandenen IDs.
	 * @param fehlermeldungIdTyp          Fehlermeldung, falls die bereinigte ID-Liste leer ist.
	 * @param fehlermeldungUnvollstaendig Fehlermeldung, falls eine bereinigte ID nicht in {@code idsVorhanden} enthalten ist.
	 *
	 * @throws ApiOperationException Falls die bereinigte Liste leer oder unvollständig ist.
	 */
	private void validiereIds(final List<Long> idsUebergeben, final Set<Long> idsVorhanden,
			final String fehlermeldungIdTyp, final String fehlermeldungUnvollstaendig) throws ApiOperationException {
		final List<Long> idsBereinigt = idsUebergeben.stream().filter(Objects::nonNull).distinct().toList();
		if (idsBereinigt.isEmpty()) {
			reportingContext.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Es wurden keine %s übergeben.".formatted(fehlermeldungIdTyp));
			throw new ApiOperationException(Status.BAD_REQUEST, "FEHLER: Es wurden keine %s übergeben.".formatted(fehlermeldungIdTyp));
		}
		for (final Long id : idsBereinigt) {
			if (!idsVorhanden.contains(id)) {
				reportingContext.logger().logLn(LogLevel.ERROR, 4, fehlermeldungUnvollstaendig);
				throw new ApiOperationException(Status.BAD_REQUEST, fehlermeldungUnvollstaendig);
			}
		}
	}

	/**
	 * Prüft, ob die Schule eine gymnasiale Oberstufe (GOSt) besitzt, wenn dies für Datenquellen relevant ist.
	 *
	 * @throws ApiOperationException Falls die Schule keine gymnasiale Oberstufe besitzt.
	 */
	private void validiereSchuleMitGost() throws ApiOperationException {
		if (!reportingContext.repositorySchule().istSchuleMitGost()) {
			reportingContext.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Die Schule besitzt keine gymnasiale Oberstufe (GOSt).");
			throw new ApiOperationException(Status.BAD_REQUEST, "FEHLER: Die Schule besitzt keine gymnasiale Oberstufe (GOSt).");
		}
	}

	/**
	 * Validiert die Parameter für Gost-Daten.
	 *
	 * @param paarweise Gibt an, ob die Daten paarweise (Abiturjahrgang+GOSt-Halbjahr, Abiturjahrgang+GOSt-Halbjahr, ...) vorliegen müssen.
	 *                  Ist der Wert false, wird ein einzelner Abiturjahrgang gefolgt von beliebigen Halbjahren erwartet.
	 *
	 * @throws ApiOperationException Falls die Parameter ungültig sind.
	 */
	private void validiereParameterFuerAbiturjahrgangUndHalbjahre(final boolean paarweise) throws ApiOperationException {
		final List<Long> parameterDaten = reportingParameter.idsHauptdaten();
		if (parameterDaten.isEmpty()) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"FEHLER: Die Parameter für Abiturjahrgang und GOSt-Halbjahr wurden nicht übergeben.");
		}
		try {
			final List<Integer> vorhandeneAbiturjahrgaenge = reportingContext.repositoryGost().abiturjahrgaenge();

			if (paarweise) {
				validiereParameterPaarweise(parameterDaten, vorhandeneAbiturjahrgaenge);
			} else {
				validiereParameterEinzeln(parameterDaten, vorhandeneAbiturjahrgaenge);
			}
		} catch (final ApiOperationException aoe) {
			throw aoe;
		} catch (final Exception e) {
			throw new ApiOperationException(Status.BAD_REQUEST, e,
					"FEHLER: Die Parameter für Abiturjahrgang und GOSt-Halbjahr konnten nicht gelesen werden oder sind außerhalb des Wertebereichs.");
		}
	}

	/**
	 * Validiert ein einzelnes Abiturjahr.
	 *
	 * @param abiturjahr                 das zu prüfende Abiturjahr
	 * @param vorhandeneAbiturjahrgaenge Liste der vorhandenen Abiturjahrgänge
	 *
	 * @throws ApiOperationException Falls das Abiturjahr ungültig ist.
	 */
	private void validiereAbiturjahr(final int abiturjahr, final List<Integer> vorhandeneAbiturjahrgaenge)
			throws ApiOperationException {
		if ((abiturjahr < 1900) || !vorhandeneAbiturjahrgaenge.contains(abiturjahr)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "FEHLER: Ein Abiturjahr liegt außerhalb des Wertebereichs.");
		}
	}

	/**
	 * Validiert ein einzelnes GOSt-Halbjahr.
	 *
	 * @param halbjahrId die ID des Halbjahres
	 *
	 * @throws ApiOperationException Falls das GOSt-Halbjahr ungültig ist.
	 */
	private void validiereHalbjahr(final int halbjahrId) throws ApiOperationException {
		if (GostHalbjahr.fromID(halbjahrId) == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "FEHLER: Ein GOSt-Halbjahr liegt außerhalb des Wertebereichs.");
		}
	}

	/**
	 * Validiert die Parameter für Gost-Daten paarweise, d. h. am Abiturjahr ist direkt das Gost-Halbjahr angehängt (beispielsweise 20253).
	 *
	 * @param parameterDaten             Liste der Parameter
	 * @param vorhandeneAbiturjahrgaenge Liste der vorhandenen Abiturjahrgänge
	 *
	 * @throws ApiOperationException Falls die Parameter ungültig sind.
	 */
	private void validiereParameterPaarweise(final List<Long> parameterDaten, final List<Integer> vorhandeneAbiturjahrgaenge) throws ApiOperationException {
		for (final Long kombinierteId : parameterDaten) {
			if (kombinierteId != null) {
				final int abiturjahr = (int) (kombinierteId / 10);
				validiereAbiturjahr(abiturjahr, vorhandeneAbiturjahrgaenge);
				final int idGostHalbjahr = (int) (kombinierteId % 10);
				validiereHalbjahr(idGostHalbjahr);
			}
		}
	}

	/**
	 * Validiert die Parameter für Gost-Daten einzeln (ein Abiturjahrgang gefolgt von beliebigen Halbjahren).
	 *
	 * @param parameterDaten             Liste der Parameter
	 * @param vorhandeneAbiturjahrgaenge Liste der vorhandenen Abiturjahrgänge
	 *
	 * @throws ApiOperationException Falls die Parameter ungültig sind.
	 */
	private void validiereParameterEinzeln(final List<Long> parameterDaten, final List<Integer> vorhandeneAbiturjahrgaenge) throws ApiOperationException {
		validiereAbiturjahr(Math.toIntExact(parameterDaten.getFirst()), vorhandeneAbiturjahrgaenge);
		for (int i = 1; i < parameterDaten.size(); i++) {
			validiereHalbjahr(Math.toIntExact(parameterDaten.get(i)));
		}
	}
}
