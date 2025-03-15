package de.svws_nrw.module.reporting.factories;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.HtmlBuilder;
import de.svws_nrw.module.reporting.html.HtmlTemplateDefinition;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextKlassen;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextKurse;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextLehrer;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextSchueler;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextSchule;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextStundenplanungKlassenStundenplan;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextStundenplanungLehrerStundenplan;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextStundenplanungSchuelerStundenplan;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.validierung.ReportingValidierung;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * <p>Diese Klasse erstellt html-Inhalte auf Basis des in den Reporting-Parametern übergebenen html-Templates und der übergebenen Daten.</p>
 * <p>Dabei erstellt die Factory bei der Initialisierung zunächst die Contexts mit den Daten gemäß dem html-Template.
 * Zum Erstellen der html-Inhalte generiert die Factory einen oder mehrere html-Builder, die aus dem Template das fertige html erzeugen.</p>
 * <p>Die html-Builder können extern weiter verarbeitet werden oder es kann intern eine Response im html-Format erzeugt werden.</p>
 */
public class HtmlFactory {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	private final ReportingRepository reportingRepository;

	/** Einstellungen und Daten zum Steuern der Report-Generierung. */
	private final ReportingParameter reportingParameter;

	/** Die Template-Definition für die Erstellung der html-Datei. */
	private final HtmlTemplateDefinition htmlTemplateDefinition;

	/** Eine Map zum Sammeln der erstellten Html-Contexts. */
	final Map<String, HtmlContext> mapHtmlContexts = new HashMap<>();


	/**
	 * Erzeugt eine neue html-Factory, um eine html-Datei aus einem html-Template zu erzeugen.
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
				">>> Beginn der Initialisierung der html-Factory und der Validierung der übergebenen Daten.");

		// Validiere die Angaben zur html-Vorlage.
		this.htmlTemplateDefinition = HtmlTemplateDefinition.getByType(ReportingReportvorlage.getByBezeichnung(this.reportingParameter.reportvorlage));
		if (this.htmlTemplateDefinition == null) {
			this.reportingRepository.logger()
					.logLn(LogLevel.ERROR, 4, "FEHLER: Die Template-Definitionen für die html-Factory sind inkonsistent.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "FEHLER: Die Template-Definitionen für die html-Factory sind inkonsistent.");
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

		this.reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Initialisierung der html-Factory und der Validierung der übergebenen Daten.");

		getContexts();
	}


	/**
	 * Erzeugte die notwendigen Contexts für die html-Erstellung auf Basis des angegebenen html-Templates.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private void getContexts() throws ApiOperationException {

		reportingRepository.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der Datenkontexte für die html-Generierung.");
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Erzeuge Datenkontext Schule für die html-Generierung.");

		final HtmlContextSchule htmlContextSchule = new HtmlContextSchule(reportingRepository);
		mapHtmlContexts.put("Schule", htmlContextSchule);

		// Betrachte die Html-Template-Definition und erzeuge damit die korrekten Contexts der Hauptdaten
		switch (htmlTemplateDefinition.name().substring(0, htmlTemplateDefinition.name().indexOf("_v_"))) {
			case "SCHUELER":
				// Schüler-Context ist Hauptdatenquelle
				initContextSchueler();
				break;
			case "KLASSEN":
				// Klassen-Context ist Hauptdatenquelle
				initContextKlassen();
				break;
			case "KURSE":
				// Kurse-Context ist Hauptdatenquelle
				initContextKurse();
				break;
			case "LEHRER":
				// Lehrer-Context ist Hauptdatenquelle
				initContextLehrer();
				break;
			case "GOST_KURSPLANUNG":
				// GOSt-Kursplanung-Blockungsergebnis-Context ist Hauptdatenquelle
				initContextGostKursplanung();
				break;
			case "GOST_KLAUSURPLANUNG":
				// GOSt-Klausurplanung-Klausurplan-Context ist Hauptdatenquelle
				initContextGostKlausurplanung();
				break;
			case "STUNDENPLANUNG":
				// Stundenplan-Context ist Hauptdatenquelle
				initContextStundenplanung();
				break;
			default:
				break;
		}

		reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der Datenkontexte für die html-Generierung.");
	}

	/**
	 * Initialisiert den Context für Schüler.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextSchueler() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für Schüler für die html-Generierung.");
		// Entferne null-Elemente und evtl. Duplikate in der Liste der Hauptdaten-Ids
		reportingParameter.idsHauptdaten = reportingParameter.idsHauptdaten.stream().filter(Objects::nonNull).distinct().toList();
		ReportingValidierung.validiereDatenFuerSchueler(reportingRepository, reportingParameter.idsHauptdaten,
				htmlTemplateDefinition.name().startsWith("SCHUELER_v_GOST_LAUFBAHNPLANUNG_"),
				htmlTemplateDefinition.name().startsWith("SCHUELER_v_GOST_ABITUR_"));
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				("Erzeuge Datenkontext Schüler für die html-Generierung - %d IDs von Schülern wurden übergeben für Template %s.")
						.formatted(reportingParameter.idsHauptdaten.size(), htmlTemplateDefinition.name()));
		final HtmlContextSchueler htmlContextSchueler = new HtmlContextSchueler(reportingRepository);
		mapHtmlContexts.put("Schueler", htmlContextSchueler);
	}

	/**
	 * Initialisiert den Context für Klassen.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextKlassen() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für Klassen für die html-Generierung.");
		// Entferne null-Elemente und evtl. Duplikate in der Liste der Hauptdaten-Ids
		reportingParameter.idsHauptdaten = reportingParameter.idsHauptdaten.stream().filter(Objects::nonNull).distinct().toList();
		ReportingValidierung.validiereDatenFuerKlassen(reportingRepository, reportingParameter.idsHauptdaten);
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				("Erzeuge Datenkontext Klassen für die html-Generierung - %d IDs von Klassen wurden übergeben für Template %s.")
						.formatted(reportingParameter.idsHauptdaten.size(), htmlTemplateDefinition.name()));
		final HtmlContextKlassen htmlContextKlassen = new HtmlContextKlassen(reportingRepository);
		mapHtmlContexts.put("Klassen", htmlContextKlassen);
	}

	/**
	 * Initialisiert den Context für Kurse.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextKurse() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für Kurse für die html-Generierung.");
		// Entferne null-Elemente und evtl. Duplikate in der Liste der Hauptdaten-Ids
		reportingParameter.idsHauptdaten = reportingParameter.idsHauptdaten.stream().filter(Objects::nonNull).distinct().toList();
		ReportingValidierung.validiereDatenFuerKurse(reportingRepository, reportingParameter.idsHauptdaten, true);
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				("Erzeuge Datenkontext Kurse für die html-Generierung - %d IDs von Kursen wurden übergeben für Template %s.")
						.formatted(reportingParameter.idsHauptdaten.size(), htmlTemplateDefinition.name()));
		final HtmlContextKurse htmlContextKurse = new HtmlContextKurse(reportingRepository);
		mapHtmlContexts.put("Kurse", htmlContextKurse);
	}

	/**
	 * Initialisiert den Context für Lehrer.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextLehrer() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für Lehrer für die html-Generierung.");
		// Entferne null-Elemente und evtl. Duplikate in der Liste der Hauptdaten-Ids
		reportingParameter.idsHauptdaten = reportingParameter.idsHauptdaten.stream().filter(Objects::nonNull).distinct().toList();
		ReportingValidierung.validiereDatenFuerLehrer(reportingRepository, reportingParameter.idsHauptdaten);
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				("Erzeuge Datenkontext Lehrer für die html-Generierung - %d IDs von Lehrern wurden übergeben für Template %s.")
						.formatted(reportingParameter.idsHauptdaten.size(), htmlTemplateDefinition.name()));
		final HtmlContextLehrer htmlContextLehrer = new HtmlContextLehrer(reportingRepository);
		mapHtmlContexts.put("Lehrer", htmlContextLehrer);
	}

	/**
	 * Initialisiert den Context für die GOSt-Kursplanung.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextGostKursplanung() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für ein Gost-Blockungsergebnis für die html-Generierung.");
		ReportingValidierung.validiereDatenFuerGostKursplanungBlockungsergebnis(reportingRepository);
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Gost-Kursplanung-Blockungsergebnis für die html-Generierung mit ID %s für Template %s."
						.formatted(reportingParameter.idsHauptdaten.getFirst(), htmlTemplateDefinition.name()));
		final HtmlContextGostKursplanungBlockungsergebnis htmlContextGostBlockung =
				new HtmlContextGostKursplanungBlockungsergebnis(reportingRepository);
		mapHtmlContexts.put("GostBlockungsergebnis", htmlContextGostBlockung);
	}

	/**
	 * Initialisiert den Context für die GOSt-Klausurplanung.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextGostKlausurplanung() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für einen Gost-Klausurplan für die html-Generierung.");
		ReportingValidierung.validiereDatenFuerGostKlausurplanungKlausurplan(reportingRepository);
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Gost-Klausurplanung für die html-Generierung mit Template %s.".formatted(htmlTemplateDefinition.name()));
		final HtmlContextGostKlausurplanungKlausurplan htmlContextGostKlausurplan =
				new HtmlContextGostKlausurplanungKlausurplan(reportingRepository,
						htmlTemplateDefinition.name().startsWith("GOST_KLAUSURPLANUNG_v_SCHUELER_") ? reportingParameter.idsDetaildaten
								: new ArrayList<>());
		mapHtmlContexts.put("GostKlausurplan", htmlContextGostKlausurplan);
	}

	/**
	 * Initialisiert den Context zur Stundenplanung.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public void initContextStundenplanung() throws ApiOperationException {
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für einen Stundenplan für die html-Generierung.");
		ReportingValidierung.validiereDatenFuerStundenplanung(reportingRepository);
		reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
				"Erzeuge Datenkontext Stundenplan für die html-Generierung mit Template %s.".formatted(htmlTemplateDefinition.name()));
		switch (htmlTemplateDefinition) {
			case STUNDENPLANUNG_v_KLASSEN_STUNDENPLAN -> {
				reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten der Klassen für einen Stundenplan für die html-Generierung.");
				ReportingValidierung.validiereDatenFuerKlassen(reportingRepository, reportingParameter.idsDetaildaten);
				final HtmlContextStundenplanungKlassenStundenplan htmlContextKlassenStundenplan =
						new HtmlContextStundenplanungKlassenStundenplan(reportingRepository,
								reportingRepository.stundenplan(reportingParameter.idsHauptdaten.getFirst()),
								reportingParameter.idsDetaildaten);
				mapHtmlContexts.put("KlassenStundenplaene", htmlContextKlassenStundenplan);
			}
			case STUNDENPLANUNG_v_LEHRER_STUNDENPLAN, STUNDENPLANUNG_v_LEHRER_STUNDENPLAN_KOMBINIERT -> {
				reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten der Lehrkräfte für einen Stundenplan für die html-Generierung.");
				ReportingValidierung.validiereDatenFuerLehrer(reportingRepository, reportingParameter.idsDetaildaten);
				final HtmlContextStundenplanungLehrerStundenplan htmlContextLehrerStundenplan =
						new HtmlContextStundenplanungLehrerStundenplan(reportingRepository,
								reportingRepository.stundenplan(reportingParameter.idsHauptdaten.getFirst()),
								reportingParameter.idsDetaildaten);
				mapHtmlContexts.put("LehrerStundenplaene", htmlContextLehrerStundenplan);
			}
			case STUNDENPLANUNG_v_SCHUELER_STUNDENPLAN -> {
				reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten der Schüler für einen Stundenplan für die html-Generierung.");
				ReportingValidierung.validiereDatenFuerSchueler(reportingRepository, reportingParameter.idsDetaildaten, false, false);
				final HtmlContextStundenplanungSchuelerStundenplan htmlContextSchuelerStundenplan =
						new HtmlContextStundenplanungSchuelerStundenplan(reportingRepository,
								reportingRepository.stundenplan(reportingParameter.idsHauptdaten.getFirst()),
								reportingParameter.idsDetaildaten);
				mapHtmlContexts.put("SchuelerStundenplaene", htmlContextSchuelerStundenplan);
			}
			default -> {
				break;
			}
		}
	}


	/**
	 * Erzeugt auf Basis des gegebenen html-Templates und der übergebenen Daten die html-Builder, aus denen die html-Inhalte erzeugt werden können.
	 *
	 * @return Eine Liste mit htmlBuilder.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	protected List<HtmlBuilder> createHtmlBuilders() throws ApiOperationException {
		return getHtmlBuilders();
	}


	/**
	 * Erstellt eine Response in Form einer einzelnen html-Datei oder Z eine einzelne ZIP-Datei, die mehrere generierte html-Dateien enthält.
	 *
	 * @return Im Falle eines Success enthält die HTTP-Response das html-Dokument oder die ZIP-Datei.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	protected Response createHtmlResponse() throws ApiOperationException {
		try {
			reportingRepository.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der Response einer API-Anfrage für eine html-Generierung.");
			final List<HtmlBuilder> htmlBuilders = getHtmlBuilders();
			if (!htmlBuilders.isEmpty()) {
				if (htmlBuilders.size() == 1) {
					final String encodedFilename = "filename*=UTF-8''" + URLEncoder.encode(htmlBuilders.getFirst().getDateiname(), StandardCharsets.UTF_8);

					reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der Response einer API-Anfrage für eine html-Generierung.");
					return Response.ok(htmlBuilders.getFirst().getHtml(), "text/html").header("Content-Disposition", "attachment; " + encodedFilename).build();
				}
				if (htmlTemplateDefinition.getDateiname().isEmpty()) {
					reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Die gewählte Vorlage kann nicht einzelne html-Inhalte erstellen.");
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "FEHLER: Die gewählte Vorlage kann nicht einzelne html-Inhalte erstellen.");
				}

				final byte[] zipData = createZIP(htmlBuilders);
				final String encodedFilename = "filename*=UTF-8''" + URLEncoder.encode(htmlTemplateDefinition.getDateiname() + ".zip", StandardCharsets.UTF_8);

				reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der Response einer API-Anfrage für eine html-Generierung.");
				return Response.ok(zipData, "application/zip").header("Content-Disposition", "attachment; " + encodedFilename).build();
			}
			reportingRepository.logger().logLn(LogLevel.ERROR, 0,
					"### Fehler bei der Erzeugung der Response einer API-Anfrage für eine html-Generierung. Es sind keine Html-Inhalte generiert worden.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Fehler bei der Erzeugung der Response einer API-Anfrage für eine html-Generierung. Es sind keine Html-Inhalte generiert worden.");
		} catch (final Exception e) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 0,
					"### Fehler bei der Erzeugung der Response einer API-Anfrage für eine html-Generierung.");
			throw e;
		}
	}


	/**
	 * Erzeugt auf Basis der übergebenen html-Vorlage und Daten die html-Inhalte der Dateien und legt diese Inhalte in einer Map zum Dateinamen ab.
	 *
	 * @return Eine Map mit den Dateinamen und html-Dateiinhalten.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private List<HtmlBuilder> getHtmlBuilders() throws ApiOperationException {

		reportingRepository.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der html-Builder.");
		final List<HtmlBuilder> htmlBuilders = new ArrayList<>();

		// Lade den in Inhalt des html-Codes aus dem Template.
		final String htmlTemplateCode = ResourceUtils.text(htmlTemplateDefinition.getRootPfadHtmlTemplate());

		if (!reportingParameter.einzelausgabeHauptdaten && !reportingParameter.einzelausgabeDetaildaten) {
			// Dateiname der Dateien aus den Daten erzeugen.
			final String dateiname = getDateiname(mapHtmlContexts);

			// html-Builder erstellen und damit das html mit Daten für die html-Datei erzeugen
			reportingRepository.logger()
					.logLn(LogLevel.DEBUG, 4,
							"Verarbeite Template (%s) und Daten aus den Kontexten zum finalen html-Dateiinhalt.".formatted(htmlTemplateDefinition.name()));
			htmlBuilders.add(new HtmlBuilder(htmlTemplateCode, mapHtmlContexts.values().stream().toList(), dateiname));
		} else if (reportingParameter.einzelausgabeHauptdaten) {
			// Die Hauptdatenquelle soll in einzelne Kontexte für Einzeldateien zerlegt werden.
			if (htmlTemplateDefinition.name().startsWith("SCHUELER_v_")) {
				// Zerlege den Gesamt-Schüler-Context in einzelne Contexts mit jeweils einen Schüler
				reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Erzeuge einzelne Haupt-Kontexte für jeden Schüler, da einzelne Dateien angefordert "
						+ "wurden.");
				final List<HtmlContextSchueler> schuelerContexts = ((HtmlContextSchueler) mapHtmlContexts.get("Schueler")).getEinzelSchuelerContexts();

				reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
						"Verarbeite Template (%s) und Daten aus den einzelnen Kontexten zu finalen html-Dateiinhalten.".formatted(
								htmlTemplateDefinition.name()));
				for (final HtmlContextSchueler schuelerContext : schuelerContexts) {
					mapHtmlContexts.put("Schueler", schuelerContext);

					// Dateiname der Dateien aus den Daten erzeugen.
					final String dateiname = getDateiname(mapHtmlContexts);

					// html-Builder erstellen und damit das html mit Daten für die html-Datei erzeugen
					htmlBuilders.add(new HtmlBuilder(htmlTemplateCode, mapHtmlContexts.values().stream().toList(), dateiname));
				}
			}
		} else {
			// Die Detaildatenquelle soll in einzelne Kontexte für Einzeldateien zerlegt werden. Die Hauptdatenquelle ist dabei für alle Einzelkontexte gleich.
			if (htmlTemplateDefinition.name().startsWith("GOST_KLAUSURPLANUNG_v_SCHUELER_")) {
				// Zerlege den Klausurplan-Context gemäß der anzuzeigenden Schüler in einzelne Contexts mit jeweils einen Schüler. Die Plandaten sind bei allen SuS gleich.
				reportingRepository.logger().logLn(
						LogLevel.DEBUG, 4, "Erzeuge einzelne Detail-Kontexte des Klausurplans für jeden Schüler, da einzelne Dateien angefordert wurden.");
				final List<HtmlContextGostKlausurplanungKlausurplan> klausurplanSchuelerContexts =
						((HtmlContextGostKlausurplanungKlausurplan) mapHtmlContexts.get("GostKlausurplan")).getEinzelSchuelerContexts();

				reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
						"Verarbeite Template (%s) und Daten aus den einzelnen Kontexten zu finalen html-Dateiinhalten.".formatted(
								htmlTemplateDefinition.name()));
				for (final HtmlContextGostKlausurplanungKlausurplan klausurplanSchuelerContext : klausurplanSchuelerContexts) {
					mapHtmlContexts.put("GostKlausurplan", klausurplanSchuelerContext);

					// Dateiname der Dateien aus den Daten erzeugen.
					final String dateiname = getDateiname(mapHtmlContexts);

					// html-Builder erstellen und damit das html mit Daten für die html-Datei erzeugen
					htmlBuilders.add(new HtmlBuilder(htmlTemplateCode, mapHtmlContexts.values().stream().toList(), dateiname));
				}
			}
			if (htmlTemplateDefinition == HtmlTemplateDefinition.STUNDENPLANUNG_v_KLASSEN_STUNDENPLAN) {
				// Zerlege den Context des Lehrerstundenplans gemäß der anzuzeigenden Lehrer in einzelne Contexts mit jeweils einen Lehrer.
				reportingRepository.logger().logLn(
						LogLevel.DEBUG, 4, "Erzeuge einzelne Detail-Kontexte der Klassenstundenpläne für jede Klasse, da einzelne Dateien angefordert wurden.");
				final List<HtmlContextStundenplanungKlassenStundenplan> klassenStundenplanContexts =
						((HtmlContextStundenplanungKlassenStundenplan) mapHtmlContexts.get("KlassenStundenplaene")).getEinzelContexts();

				reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
						"Verarbeite Template (%s) und Daten aus den einzelnen Kontexten zu finalen html-Dateiinhalten.".formatted(
								htmlTemplateDefinition.name()));
				for (final HtmlContextStundenplanungKlassenStundenplan klassenStundenplanContext : klassenStundenplanContexts) {
					mapHtmlContexts.put("KlassenStundenplaene", klassenStundenplanContext);

					// Dateiname der Dateien aus den Daten erzeugen.
					final String dateiname = getDateiname(mapHtmlContexts);

					// html-Builder erstellen und damit das html mit Daten für die html-Datei erzeugen
					htmlBuilders.add(new HtmlBuilder(htmlTemplateCode, mapHtmlContexts.values().stream().toList(), dateiname));
				}
			}
			if (htmlTemplateDefinition == HtmlTemplateDefinition.STUNDENPLANUNG_v_LEHRER_STUNDENPLAN) {
				// Zerlege den Context des Lehrerstundenplans gemäß der anzuzeigenden Lehrer in einzelne Contexts mit jeweils einen Lehrer.
				reportingRepository.logger().logLn(
						LogLevel.DEBUG, 4, "Erzeuge einzelne Detail-Kontexte der Lehrerstundenpläne für jeden Lehrer, da einzelne Dateien angefordert wurden.");
				final List<HtmlContextStundenplanungLehrerStundenplan> lehrerStundenplanContexts =
						((HtmlContextStundenplanungLehrerStundenplan) mapHtmlContexts.get("LehrerStundenplaene")).getEinzelContexts();

				reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
						"Verarbeite Template (%s) und Daten aus den einzelnen Kontexten zu finalen html-Dateiinhalten.".formatted(
								htmlTemplateDefinition.name()));
				for (final HtmlContextStundenplanungLehrerStundenplan lehrerStundenplanContext : lehrerStundenplanContexts) {
					mapHtmlContexts.put("LehrerStundenplaene", lehrerStundenplanContext);

					// Dateiname der Dateien aus den Daten erzeugen.
					final String dateiname = getDateiname(mapHtmlContexts);

					// html-Builder erstellen und damit das html mit Daten für die html-Datei erzeugen
					htmlBuilders.add(new HtmlBuilder(htmlTemplateCode, mapHtmlContexts.values().stream().toList(), dateiname));
				}
			}
			if (htmlTemplateDefinition == HtmlTemplateDefinition.STUNDENPLANUNG_v_SCHUELER_STUNDENPLAN) {
				// Zerlege den Context des Lehrerstundenplans gemäß der anzuzeigenden Lehrer in einzelne Contexts mit jeweils einen Lehrer.
				reportingRepository.logger().logLn(
						LogLevel.DEBUG, 4, "Erzeuge einzelne Detail-Kontexte der Schülerstundenpläne für jeden Schüler, da einzelne Dateien angefordert wurden.");
				final List<HtmlContextStundenplanungSchuelerStundenplan> schuelerStundenplanContexts =
						((HtmlContextStundenplanungSchuelerStundenplan) mapHtmlContexts.get("SchuelerStundenplaene")).getEinzelContexts();

				reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
						"Verarbeite Template (%s) und Daten aus den einzelnen Kontexten zu finalen html-Dateiinhalten.".formatted(
								htmlTemplateDefinition.name()));
				for (final HtmlContextStundenplanungSchuelerStundenplan schuelerStundenplanContext : schuelerStundenplanContexts) {
					mapHtmlContexts.put("SchuelerStundenplaene", schuelerStundenplanContext);

					// Dateiname der Dateien aus den Daten erzeugen.
					final String dateiname = getDateiname(mapHtmlContexts);

					// html-Builder erstellen und damit das html mit Daten für die html-Datei erzeugen
					htmlBuilders.add(new HtmlBuilder(htmlTemplateCode, mapHtmlContexts.values().stream().toList(), dateiname));
				}
			}
		}

		reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der html-Builder.");
		return htmlBuilders;
	}


	/**
	 * Erstellt den Dateinamen gemäß der in der Template-Definition hinterlegten Vorlage für den Dateinamen. Dabei können die Daten den Contexts entnommen werden.
	 *
	 * @param mapHtmlContexts 			Map mit den bereits erzeugten html-Datenkontexten, um daraus Daten für den Dateinamen entnehmen zu können.
	 *
	 * @return Der fertige Dateiname.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private String getDateiname(final Map<String, HtmlContext> mapHtmlContexts) throws ApiOperationException {

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Erzeuge den Dateinamen zum Template %s.".formatted(htmlTemplateDefinition.name()));

		String dateiname = htmlTemplateDefinition.getDateiname();

		if (!htmlTemplateDefinition.getDateinamensvorlage().isEmpty() && !htmlTemplateDefinition.getDateinamensvorlage().isBlank()) {
			final HtmlBuilder htmlBuilder =
					new HtmlBuilder(htmlTemplateDefinition.getDateinamensvorlage(), mapHtmlContexts.values().stream().toList(), dateiname);
			final String html = htmlBuilder.getHtml();

			if ((html == null) || html.isEmpty() || html.isBlank() || !html.contains("<p>") || !html.contains("</p>")
					|| ((html.indexOf("<p>") + 3) >= html.indexOf("</p>"))) {
				reportingRepository.logger().logLn(LogLevel.ERROR, 4,
						("FEHLER: Erzeugung des Dateinamens zum Template %s. fehlgeschlagen. Der Dateiname konnte nicht gemäß des angegebenen Musters aus den "
								+ "Daten generiert werden.")
								.formatted(htmlTemplateDefinition.name()));
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						("FEHLER: Erzeugung des Dateinamens zum Template %s. fehlgeschlagen. Der Dateiname konnte nicht gemäß des angegebenen Musters aus den "
								+ "Daten generiert werden.")
								.formatted(htmlTemplateDefinition.name()));
			}
			dateiname = html.substring(html.indexOf("<p>") + 3, html.indexOf("</p>"));
		}

		try {
			//noinspection ResultOfMethodCallIgnored
			(new File(dateiname + ".html")).getCanonicalFile();
		} catch (@SuppressWarnings("unused") final Exception e) {
			// Rückgabewert von getCanonicalFile wird ignoriert. Diese Funktion prüft aber den Dateinamen und wirft eine Exception, wenn unzulässiger Name.
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Der generierte html-Dateiname enthält ungültige Zeichen.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "FEHLER: Der generierte html-Dateiname enthält ungültige Zeichen.");
		}

		return dateiname;
	}


	/**
	 * Erstellt eine ZIP-Datei, die alle html-Dateien aus der übergebenen Map enthält.
	 *
	 * @param htmlBuilders 				Eine Liste mit den htmlBuilders, die die html-Inhalte erzeugen.
	 *
	 * @return Gibt das ZIP in Form eines ByteArrays zurück.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private byte[] createZIP(final List<HtmlBuilder> htmlBuilders) throws ApiOperationException {
		final byte[] zipData;
		try {
			try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
				try (ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream)) {
					for (final HtmlBuilder htmlBuilder : htmlBuilders) {
						zos.putNextEntry(new ZipEntry(htmlBuilder.getDateinameMitEndung()));
						zos.write(htmlBuilder.getHtmlByteArray());
						zos.closeEntry();
					}
					byteArrayOutputStream.flush();
				}
				zipData = byteArrayOutputStream.toByteArray();
			}
		} catch (final IOException e) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Die erzeugten html-Inhalte konnten nicht als ZIP-Datei zusammengestellt werden.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e,
					"FEHLER: Die erzeugten html-Inhalte konnten nicht als ZIP-Datei zusammengestellt werden.");
		}
		return zipData;
	}

}
