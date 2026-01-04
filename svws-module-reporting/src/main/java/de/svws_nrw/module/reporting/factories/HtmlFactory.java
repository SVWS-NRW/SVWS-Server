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
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.builders.ReportBuilderHtml;
import de.svws_nrw.module.reporting.builders.ReportBuilderContextHtml;
import de.svws_nrw.module.reporting.filterung.ReportingFilterDataType;
import de.svws_nrw.module.reporting.html.HtmlTemplateDefinition;
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

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	private final ReportingRepository reportingRepository;

	/** Einstellungen und Daten zum Steuern der Report-Generierung. */
	private final ReportingParameterTypisiert reportingParameter;

	/** Die Template-Definition für die Erstellung der HTML-Datei. */
	private final HtmlTemplateDefinition htmlTemplateDefinition;

	/** Eine Map zum Sammeln der erstellten HTML-Contexts. */
	final Map<String, HtmlContext<?>> mapHtmlContexts = new HashMap<>();


	/**
	 * Erzeugt eine neue HTML-Factory, um eine HTML-Datei aus einem HTML-Template zu erzeugen.
	 *
	 * @param reportingRepository		Repository für das Reporting, welches verschiedene Daten aus der Datenbank zwischenspeichert.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	protected HtmlFactory(final ReportingRepository reportingRepository)
			throws ApiOperationException {

		this.reportingRepository = reportingRepository;
		this.reportingParameter = this.reportingRepository.reportingParameter();

		this.reportingRepository.logger().logLn(LogLevel.DEBUG, 0,
				">>> Beginn der Initialisierung der HTML-Factory und der Validierung der übergebenen Daten.");

		// Validiere die Angaben zur HTML-Vorlage.
		this.htmlTemplateDefinition = this.reportingParameter.htmlTemplateDefinition();
		if (this.htmlTemplateDefinition == null) {
			this.reportingRepository.logger()
					.logLn(LogLevel.ERROR, 4, "FEHLER: Die Template-Definitionen für die HTML-Factory sind inkonsistent.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "FEHLER: Die Template-Definitionen für die HTML-Factory sind inkonsistent.");
		}

		// Prüfe, ob die Rechte des Benutzers zu den in der TemplateDefinition hinterlegten Rechten passen.
		this.reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				"Prüfe die Berechtigungen des Benutzers für den Zugriff auf die für die Ausgabe notwendigen Daten.");
		if (!this.reportingRepository.conn().getUser().pruefeKompetenz(new HashSet<>(htmlTemplateDefinition.getBenutzerKompetenzen()))) {
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
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private void getContexts() throws ApiOperationException {

		reportingRepository.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der Datenkontexte für die HTML-Generierung.");
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Erzeuge Datenkontext Schule für die HTML-Generierung.");

		final HtmlContextBasisdaten htmlContextBasisdaten = new HtmlContextBasisdaten(reportingRepository);
		mapHtmlContexts.put("Basisdaten", htmlContextBasisdaten);

		// Betrachte die HTML-Template-Definition und erzeuge damit die korrekten Contexts der Hauptdaten
		switch (htmlTemplateDefinition.getHauptdatenContextDefinition()) {
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
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextSchueler() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für Schüler für die HTML-Generierung.");

		final boolean istGostLaufbahnplanung =
				((htmlTemplateDefinition == HtmlTemplateDefinition.SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN)
						|| (htmlTemplateDefinition == HtmlTemplateDefinition.SCHUELER_V_GOST_LAUFBAHNPLANUNG_ERGEBNISUEBERSICHT));
		final boolean istGostAbitur =
				((htmlTemplateDefinition == HtmlTemplateDefinition.SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A3)
						|| (htmlTemplateDefinition == HtmlTemplateDefinition.SCHUELER_V_GOST_ABITUR_APO_ANLAGE_12_A4));

		ReportingValidierung.validiereDatenFuerSchueler(reportingRepository, reportingParameter.idsHauptdaten(), istGostLaufbahnplanung, istGostAbitur);
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				("Erzeuge Datenkontext Schüler für die HTML-Generierung - %d IDs von Schülern wurden übergeben für Template %s.")
						.formatted(reportingParameter.idsHauptdaten().size(), htmlTemplateDefinition.name()));
		final HtmlContextSchueler htmlContextSchueler = new HtmlContextSchueler(reportingRepository);
		mapHtmlContexts.put("Schueler", htmlContextSchueler);
	}

	/**
	 * Initialisiert den Context für Klassen.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextKlassen() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für Klassen für die HTML-Generierung.");
		ReportingValidierung.validiereDatenFuerKlassen(reportingRepository, reportingParameter.idsHauptdaten());
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				("Erzeuge Datenkontext Klassen für die HTML-Generierung - %d IDs von Klassen wurden übergeben für Template %s.")
						.formatted(reportingParameter.idsHauptdaten().size(), htmlTemplateDefinition.name()));
		final HtmlContextKlassen htmlContextKlassen = new HtmlContextKlassen(reportingRepository);
		mapHtmlContexts.put("Klassen", htmlContextKlassen);
	}

	/**
	 * Initialisiert den Context für Kurse.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextKurse() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für Kurse für die HTML-Generierung.");
		ReportingValidierung.validiereDatenFuerKurse(reportingRepository, reportingParameter.idsHauptdaten());
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				("Erzeuge Datenkontext Kurse für die HTML-Generierung - %d IDs von Kursen wurden übergeben für Template %s.")
						.formatted(reportingParameter.idsHauptdaten().size(), htmlTemplateDefinition.name()));
		final HtmlContextKurse htmlContextKurse = new HtmlContextKurse(reportingRepository);
		mapHtmlContexts.put("Kurse", htmlContextKurse);
	}

	/**
	 * Initialisiert den Context für Lehrer.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextLehrer() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für Lehrer für die HTML-Generierung.");
		ReportingValidierung.validiereDatenFuerLehrer(reportingRepository, reportingParameter.idsHauptdaten());
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				("Erzeuge Datenkontext Lehrer für die HTML-Generierung - %d IDs von Lehrern wurden übergeben für Template %s.")
						.formatted(reportingParameter.idsHauptdaten().size(), htmlTemplateDefinition.name()));
		final HtmlContextLehrer htmlContextLehrer = new HtmlContextLehrer(reportingRepository);
		mapHtmlContexts.put("Lehrer", htmlContextLehrer);
	}

	/**
	 * Initialisiert die Fachwahlstatistiken für den Context der GOSt-Laufbahnplanung eines Abiturjahrgangs.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für einen Gost-Laufbahnplan eines Abiturjahrgangs und dessen "
				+ "Fachwahlstatistiken für die HTML-Generierung.");
		ReportingValidierung.validiereDatenFuerGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken(reportingRepository);
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Gost-Laufbahnplan-Abiturjahrgang-Fachwahlstatistiken für die HTML-Generierung mit Template %s."
						.formatted(htmlTemplateDefinition.name()));
		final HtmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken htmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken =
				new HtmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken(reportingRepository);
		mapHtmlContexts.put("GostLaufbahnplanungAbiturjahrgangFachwahlStatistiken", htmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken);
	}

	/**
	 * Initialisiert den Context für die GOSt-Kursplanung.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextGostKursplanung() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für ein Gost-Blockungsergebnis für die HTML-Generierung.");
		ReportingValidierung.validiereDatenFuerGostKursplanungBlockungsergebnis(reportingRepository);
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Gost-Kursplanung-Blockungsergebnis für die HTML-Generierung mit ID %s für Template %s."
						.formatted(reportingParameter.idsHauptdaten().getFirst(), htmlTemplateDefinition.name()));
		final List<Long> idsFilter = this.reportingRepository.reportingParameter().idsDetaildaten();
		final ReportingFilterDataType idsFilterDataType = switch (htmlTemplateDefinition) {
			case GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN -> ReportingFilterDataType.KURSE;
			case GOST_KURSPLANUNG_V_SCHUELER_MIT_KURSEN, GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN -> ReportingFilterDataType.SCHUELER;
			default -> ReportingFilterDataType.UNDEFINED;
		};
		final HtmlContextGostKursplanungBlockungsergebnis htmlContextGostBlockung =
				new HtmlContextGostKursplanungBlockungsergebnis(reportingRepository, idsFilter, idsFilterDataType);
		mapHtmlContexts.put("GostBlockungsergebnis", htmlContextGostBlockung);
	}

	/**
	 * Initialisiert den Context für die GOSt-Klausurplanung.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextGostKlausurplanung() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für einen Gost-Klausurplan für die HTML-Generierung.");
		ReportingValidierung.validiereDatenFuerGostKlausurplanungKlausurplan(reportingRepository);
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Gost-Klausurplanung für die HTML-Generierung mit Template %s.".formatted(htmlTemplateDefinition.name()));
		final List<Long> idsFilter = this.reportingRepository.reportingParameter().idsDetaildaten();
		final ReportingFilterDataType idsFilterDataType = switch (htmlTemplateDefinition) {
			case GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN -> ReportingFilterDataType.SCHUELER;
			default -> ReportingFilterDataType.UNDEFINED;
		};
		final HtmlContextGostKlausurplanungKlausurplan htmlContextGostKlausurplan = new HtmlContextGostKlausurplanungKlausurplan(reportingRepository,
				idsFilter, idsFilterDataType);
		mapHtmlContexts.put("GostKlausurplan", htmlContextGostKlausurplan);
	}

	/**
	 * Initialisiert den Context zur Stundenplanung.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextStundenplanung() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für einen Stundenplan für die HTML-Generierung.");
		ReportingValidierung.validiereDatenFuerStundenplanung(reportingRepository);
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Stundenplan für die HTML-Generierung mit Template %s.".formatted(htmlTemplateDefinition.name()));
		switch (htmlTemplateDefinition) {
			case STUNDENPLANUNG_V_FACH_STUNDENPLAN -> {
				final HtmlContextStundenplanungFachStundenplan htmlContextFachStundenplan =
						new HtmlContextStundenplanungFachStundenplan(reportingRepository,
								reportingRepository.stundenplan(reportingParameter.idsHauptdaten().getFirst()),
								reportingParameter.idsDetaildaten());
				mapHtmlContexts.put("FaecherStundenplaene", htmlContextFachStundenplan);
			}
			case STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN -> {
				reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten der Klassen für einen Stundenplan für die HTML-Generierung.");
				ReportingValidierung.validiereDatenFuerKlassen(reportingRepository, reportingParameter.idsDetaildaten());
				final HtmlContextStundenplanungKlassenStundenplan htmlContextKlassenStundenplan =
						new HtmlContextStundenplanungKlassenStundenplan(reportingRepository,
								reportingRepository.stundenplan(reportingParameter.idsHauptdaten().getFirst()),
								reportingParameter.idsDetaildaten());
				mapHtmlContexts.put("KlassenStundenplaene", htmlContextKlassenStundenplan);
			}
			case STUNDENPLANUNG_V_LEHRER_STUNDENPLAN, STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT -> {
				reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten der Lehrkräfte für einen Stundenplan für die HTML-Generierung.");
				ReportingValidierung.validiereDatenFuerLehrer(reportingRepository, reportingParameter.idsDetaildaten());
				final HtmlContextStundenplanungLehrerStundenplan htmlContextLehrerStundenplan =
						new HtmlContextStundenplanungLehrerStundenplan(reportingRepository,
								reportingRepository.stundenplan(reportingParameter.idsHauptdaten().getFirst()),
								reportingParameter.idsDetaildaten());
				mapHtmlContexts.put("LehrerStundenplaene", htmlContextLehrerStundenplan);
			}
			case STUNDENPLANUNG_V_RAUM_STUNDENPLAN -> {
				final HtmlContextStundenplanungRaumStundenplan htmlContextRaeumeStundenplan =
						new HtmlContextStundenplanungRaumStundenplan(reportingRepository,
								reportingRepository.stundenplan(reportingParameter.idsHauptdaten().getFirst()),
								reportingParameter.idsDetaildaten());
				mapHtmlContexts.put("RaeumeStundenplaene", htmlContextRaeumeStundenplan);
			}
			case STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN -> {
				reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten der Schüler für einen Stundenplan für die HTML-Generierung.");
				ReportingValidierung.validiereDatenFuerSchueler(reportingRepository, reportingParameter.idsDetaildaten(), false, false);
				final HtmlContextStundenplanungSchuelerStundenplan htmlContextSchuelerStundenplan =
						new HtmlContextStundenplanungSchuelerStundenplan(reportingRepository,
								reportingRepository.stundenplan(reportingParameter.idsHauptdaten().getFirst()),
								reportingParameter.idsDetaildaten());
				mapHtmlContexts.put("SchuelerStundenplaene", htmlContextSchuelerStundenplan);
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
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	protected List<ReportBuilderHtml> createHtmlBuilders() throws ApiOperationException {
		return getHtmlBuilders();
	}


	/**
	 * Erstellt eine Response in Form einer einzelnen HTML-Datei oder eine einzelne ZIP-Datei, die mehrere generierte HTML-Dateien enthält.
	 *
	 * @return Im Falle eines Success enthält die HTTP-Response das HTML-Dokument oder die ZIP-Datei.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
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
		final String htmlTemplateCode = ResourceUtils.text(htmlTemplateDefinition.getRootPfadHtmlTemplate());
		if (htmlTemplateCode == null) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "### FEHLER: Die HTML-Template-Datei für die HTML-Erzeugung konnte nicht eingelesen werden.");
			throw new ApiOperationException(Status.NOT_FOUND, "### FEHLER: Die HTML-Template-Datei für die HTML-Erzeugung konnte nicht eingelesen werden.");
		}

		final List<ReportBuilderHtml> htmlBuilders = new ArrayList<>();

		if (reportingParameter.einzelausgabeHauptdaten())
			erzeugeHauptEinzelContexts(htmlBuilders, htmlTemplateCode);
		else if (reportingParameter.einzelausgabeDetaildaten())
			erzeugeDetailEinzelContexts(htmlBuilders, htmlTemplateCode);
		else {
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
	private void erzeugeHauptEinzelContexts(final List<ReportBuilderHtml> htmlBuilders, final String htmlTemplateCode) throws ApiOperationException {
		if (htmlTemplateDefinition.name().startsWith("SCHUELER_V_")) {
			// Zerlege den Gesamt-Schüler-Context in einzelne Contexts mit jeweils einem Schüler
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Erzeuge einzelne Haupt-Kontexte für jeden Schüler, da einzelne Dateien angefordert wurden.");
			final List<HtmlContextSchueler> schuelerContexts = ((HtmlContextSchueler) mapHtmlContexts.get("Schueler")).getEinzelContexts();
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
					"Verarbeite Template (%s) und Daten aus den einzelnen Kontexten zu finalen HTML-Dateiinhalten."
							.formatted(htmlTemplateDefinition.name()));
			for (final HtmlContextSchueler schuelerContext : schuelerContexts) {
				mapHtmlContexts.put("Schueler", schuelerContext);
				htmlBuilders.add(getReportBuilderHtml(htmlTemplateCode));
			}
		}
		if (htmlTemplateDefinition.name().startsWith("LEHRER_V_")) {
			// Zerlege den Gesamt-Lehrer-Context in einzelne Contexts mit jeweils einem Lehrer
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Erzeuge einzelne Haupt-Kontexte für jeden Lehrer, da einzelne Dateien angefordert wurden.");
			final List<HtmlContextLehrer> lehrerContexts = ((HtmlContextLehrer) mapHtmlContexts.get("Lehrer")).getEinzelContexts();
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
					"Verarbeite Template (%s) und Daten aus den einzelnen Kontexten zu finalen HTML-Dateiinhalten."
							.formatted(htmlTemplateDefinition.name()));
			for (final HtmlContextLehrer lehrerContext : lehrerContexts) {
				mapHtmlContexts.put("Lehrer", lehrerContext);
				htmlBuilders.add(getReportBuilderHtml(htmlTemplateCode));
			}
		}
		if (htmlTemplateDefinition.name().startsWith("KLASSEN_V_")) {
			// Zerlege den Gesamt-Klassen-Context in einzelne Contexts mit jeweils einer Klasse
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Erzeuge einzelne Haupt-Kontexte für jede Klasse, da einzelne Dateien angefordert wurden.");
			final List<HtmlContextKlassen> klassenContexts = ((HtmlContextKlassen) mapHtmlContexts.get("Klassen")).getEinzelContexts();

			reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
					"Verarbeite Template (%s) und Daten aus den einzelnen Kontexten zu finalen HTML-Dateiinhalten."
							.formatted(htmlTemplateDefinition.name()));
			for (final HtmlContextKlassen klasseContext : klassenContexts) {
				mapHtmlContexts.put("Klassen", klasseContext);
				htmlBuilders.add(getReportBuilderHtml(htmlTemplateCode));
			}
		}
		if (htmlTemplateDefinition.name().startsWith("KURSE_V_")) {
			// Zerlege den Gesamt-Kurse-Context in einzelne Contexts mit jeweils einem Kurs
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Erzeuge einzelne Haupt-Kontexte für jeden Kurs, da einzelne Dateien angefordert wurden.");
			final List<HtmlContextKurse> kurseContexts = ((HtmlContextKurse) mapHtmlContexts.get("Kurse")).getEinzelContexts();

			reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
					"Verarbeite Template (%s) und Daten aus den einzelnen Kontexten zu finalen HTML-Dateiinhalten."
							.formatted(htmlTemplateDefinition.name()));
			for (final HtmlContextKurse kursContext : kurseContexts) {
				mapHtmlContexts.put("Kurse", kursContext);
				htmlBuilders.add(getReportBuilderHtml(htmlTemplateCode));
			}
		}
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
			if (context == null)
				continue;
			ids.addAll(context.getIds());
		}

		return ids.stream().filter(Objects::nonNull).distinct().toList();
	}


	/**
	 * Erzeugt einzelne Detail-Kontexte basierend auf der definierten Darstellungsvorlage und den verfügbaren Datenquellen. Die Methode verarbeitet
	 * verschiedene Vorlagentypen und erstellt individuelle HTML-Kontexte entsprechend den Anforderungen des Vorlagentyps.
	 *
	 * @param htmlBuilders     Liste der ReportBuilderHtml, die zur Erstellung der Inhalte verwendet werden
	 * @param htmlTemplateCode Der Code der HTML-Vorlage, der die Art der zu erstellenden Kontexte definiert
	 *
	 * @throws ApiOperationException Wird ausgelöst, wenn ein Fehler bei der Verarbeitung der Vorlagentypen oder der verfügbaren Kontexte auftritt.
	 */
	private void erzeugeDetailEinzelContexts(final List<ReportBuilderHtml> htmlBuilders, final String htmlTemplateCode) throws ApiOperationException {
		switch (htmlTemplateDefinition) {
			case GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN -> splitteDetailContexts("GostKlausurplan",
					((HtmlContextGostKlausurplanungKlausurplan) mapHtmlContexts.get("GostKlausurplan"))::getEinzelContexts,
					htmlBuilders, htmlTemplateCode, "Erzeuge einzelne Detail-Kontexte des Klausurplans für jeden Schüler.");
			case GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN -> splitteDetailContexts("GostBlockungsergebnis",
					((HtmlContextGostKursplanungBlockungsergebnis) mapHtmlContexts.get("GostBlockungsergebnis"))::getEinzelContexts,
					htmlBuilders, htmlTemplateCode, "Erzeuge einzelne Detail-Kontexte der Kursplanung für jeden Kurs.");
			case STUNDENPLANUNG_V_FACH_STUNDENPLAN -> splitteDetailContexts("FaecherStundenplaene",
					((HtmlContextStundenplanungFachStundenplan) mapHtmlContexts.get("FaecherStundenplaene"))::getEinzelContexts,
					htmlBuilders, htmlTemplateCode, "Erzeuge einzelne Detail-Kontexte der Fachstundenpläne für jedes Fach.");
			case STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN -> splitteDetailContexts("KlassenStundenplaene",
					((HtmlContextStundenplanungKlassenStundenplan) mapHtmlContexts.get("KlassenStundenplaene"))::getEinzelContexts,
					htmlBuilders, htmlTemplateCode, "Erzeuge einzelne Detail-Kontexte der Klassenstundenpläne für jede Klasse.");
			case STUNDENPLANUNG_V_LEHRER_STUNDENPLAN, STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT -> splitteDetailContexts("LehrerStundenplaene",
					((HtmlContextStundenplanungLehrerStundenplan) mapHtmlContexts.get("LehrerStundenplaene"))::getEinzelContexts,
					htmlBuilders, htmlTemplateCode, "Erzeuge einzelne Detail-Kontexte der Lehrerstundenpläne für jeden Lehrer.");
			case STUNDENPLANUNG_V_RAUM_STUNDENPLAN -> splitteDetailContexts("RaeumeStundenplaene",
					((HtmlContextStundenplanungRaumStundenplan) mapHtmlContexts.get("RaeumeStundenplaene"))::getEinzelContexts,
					htmlBuilders, htmlTemplateCode, "Erzeuge einzelne Detail-Kontexte der Raumstundenpläne für jeden Raum.");
			case STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN -> splitteDetailContexts("SchuelerStundenplaene",
					((HtmlContextStundenplanungSchuelerStundenplan) mapHtmlContexts.get("SchuelerStundenplaene"))::getEinzelContexts,
					htmlBuilders, htmlTemplateCode, "Erzeuge einzelne Detail-Kontexte der Schülerstundenpläne für jeden Schüler.");
			default -> throw new ApiOperationException(Status.BAD_REQUEST,
					"FEHLER: Es wurden Einzeldaten-Kontexte für eine Vorlage angefordert, die dies nicht Unterstützung. Erstellung wird abgebrochen.");
		}
	}

	/**
	 * Teilt die Detailkontexte anhand der bereitgestellten Kontextspezifikationen auf und erstellt HTML-Inhalte für jeden Kontext basierend auf dem
	 * angegebenen Template.
	 *
	 * @param <C>                        Der Typ des HTML-Kontextes, der erzeugt werden soll.
	 * @param bezeichnungContext         Der Name des Kontextes, der verarbeitet wird.
	 * @param functionErmittleEinzelContexts  Eine Funktion, die eine Liste von Einzelkontexten bereitstellt.
	 * @param htmlBuilders               Die HTML-Builder-Liste, in die die erzeugten Inhalte eingefügt werden.
	 * @param htmlTemplateCode           Der Template-Code, der für die HTML-Generierung verwendet wird.
	 * @param logText                    Ein Text, der zur Protokollierung des Prozesses verwendet wird.
	 *
	 * @throws ApiOperationException Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private <C extends HtmlContext<?>> void splitteDetailContexts(final String bezeichnungContext, final Supplier<List<C>> functionErmittleEinzelContexts,
			final List<ReportBuilderHtml> htmlBuilders, final String htmlTemplateCode, final String logText) throws ApiOperationException {

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, logText);
		final List<C> einzelContexts = functionErmittleEinzelContexts.get();

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				"Verarbeite Template (%s) und Daten aus den einzelnen Kontexten zu finalen HTML-Dateiinhalten.".formatted(htmlTemplateDefinition.name()));

		for (final C einzelContext : einzelContexts) {
			mapHtmlContexts.put(bezeichnungContext, einzelContext);
			htmlBuilders.add(getReportBuilderHtml(htmlTemplateCode));
		}
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
						.withDateinamensvorlage(htmlTemplateDefinition.getDateinamensvorlage())
						.withStatischerDateiname(htmlTemplateDefinition.getDateiname())
						.withRootPfad(HtmlTemplateDefinition.getRootPfad())
						.withLogger(reportingRepository.logger());
		return new ReportBuilderHtml(reportBuilderContext);
	}


	/**
	 * Erstellt eine ZIP-Datei, die alle HTML-Dateien aus der übergebenen Liste enthält.
	 *
	 * @param htmlBuilders 				Eine Liste mit den ReportBuilderHtml-Instanzen, die die HTML-Inhalte erzeugen.
	 *
	 * @return Gibt das ZIP in Form eines ByteArrays zurück.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
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
