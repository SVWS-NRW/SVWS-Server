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
import de.svws_nrw.module.reporting.html.contexts.HtmlContextSchueler;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextSchule;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.validierung.ReportingValidierung;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse beinhaltet den Code zur Erstellung von Html-Inhalten auf Basis der hinterlegten html-Vorlage und den übergebenen Daten.
 * Sie setzt voraus, dass zum übergebenen html-Template eine css-Datei mit gleichem Pfad und Namen existiert.
 */
public class HtmlFactory {

	/** Repository für die Reporting */
	private final ReportingRepository reportingRepository;

	/** Die Daten für die Report-Ausgabe. */
	private final ReportingParameter reportingParameter;

	/** Die Template-Definition für die Erstellung der Html-Datei */
	private final HtmlTemplateDefinition htmlTemplateDefinition;

	/** Eine Map zum Sammeln der erstellten Html-Contexts. */
	final Map<String, HtmlContext> mapHtmlContexts = new HashMap<>();


	/**
	 * Erzeugt eine neue Html-Factory, um eine Html-Datei aus einem html-Template zu erzeugen.
	 *
	 * @param reportingParameter 	Das Objekt, welches die Angaben zu den Daten des Reports und den zugehörigen Einstellungen enthält.
	 * @param reportingRepository	Repository für das Reporting, welches verschiedene Daten aus der Datenbank zwischenspeichert.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	protected HtmlFactory(final ReportingRepository reportingRepository, final ReportingParameter reportingParameter)
			throws ApiOperationException {

		this.reportingRepository = reportingRepository;
		this.reportingParameter = reportingParameter;

		this.reportingRepository.logger().logLn(LogLevel.DEBUG, 0,
				">>> Beginn der Initialisierung der html-Factory und der Validierung der übergebenen Daten.");

		// Validiere die Angaben zur html-Vorlage.
		this.htmlTemplateDefinition = HtmlTemplateDefinition.getByType(ReportingReportvorlage.getByBezeichnung(reportingParameter.reportvorlage));
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
	 * Erzeugte die Contexts für die html-Erstellung.
	 *
	 * @throws ApiOperationException   im Fehlerfall
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
				reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für Schüler für die html-Generierung.");
				ReportingValidierung.validiereDatenFuerSchueler(reportingRepository, reportingParameter.idsHauptdaten,
						htmlTemplateDefinition.name().startsWith("SCHUELER_v_GOST_LAUFBAHNPLANUNG_"),
						htmlTemplateDefinition.name().startsWith("SCHUELER_v_GOST_ABITUR_"), true);
				reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
						("Erzeuge Datenkontext Schüler für die html-Generierung - %d IDs von Schülern wurden übergeben für Template %s.")
								.formatted(reportingParameter.idsHauptdaten.size(), htmlTemplateDefinition.name()));
				final HtmlContextSchueler htmlContextSchueler = new HtmlContextSchueler(reportingRepository);
				mapHtmlContexts.put("Schueler", htmlContextSchueler);
				break;
			case "GOST_KURSPLANUNG":
				// GOSt-Kursplanung-Blockungsergebnis-Context ist Hauptdatenquelle
				reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für ein Gost-Blockungsergebnis für die html-Generierung.");
				ReportingValidierung.validiereDatenFuerGostKursplanungBlockungsergebnis(reportingRepository);
				reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
						"Erzeuge Datenkontext Gost-Kursplanung-Blockungsergebnis für die html-Generierung mit ID %s für Template %s."
								.formatted(reportingParameter.idsHauptdaten.getFirst(), htmlTemplateDefinition.name()));
				final HtmlContextGostKursplanungBlockungsergebnis htmlContextGostBlockung =
						new HtmlContextGostKursplanungBlockungsergebnis(reportingRepository);
				mapHtmlContexts.put("GostBlockungsergebnis", htmlContextGostBlockung);
				break;
			case "GOST_KLAUSURPLANUNG":
				// GOSt-Klausurplanung-Klausurplan-Context ist Hauptdatenquelle
				reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Validiere die Daten für einen Gost-Klausurplan für die html-Generierung.");
				ReportingValidierung.validiereDatenFuerGostKlausurplanungKlausurplan(reportingRepository);
				reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
						"Erzeuge Datenkontext Gost-Klausurplanung für die html-Generierung mit Template %s.".formatted(htmlTemplateDefinition.name()));
				final HtmlContextGostKlausurplanungKlausurplan htmlContextGostKlausurplan =
						new HtmlContextGostKlausurplanungKlausurplan(reportingRepository,
								htmlTemplateDefinition.name().startsWith("GOST_KLAUSURPLANUNG_v_SCHUELER_") ? reportingParameter.idsDetaildaten
										: new ArrayList<>());
				mapHtmlContexts.put("GostKlausurplan", htmlContextGostKlausurplan);
				break;
			default:
				break;
		}

		reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der Datenkontexte für die html-Generierung.");
	}


	/**
	 * Erzeugt auf Basis der gegebenen html-Vorlage und der übergebenen Daten die HtmlBuilder, aus denen die Html-Inhalte erzeugt werden können.
	 *
	 * @return Eine Liste mit HtmlBuilder.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	protected List<HtmlBuilder> createHtmlBuilders() throws ApiOperationException {
		return getHtmlBuilders();
	}


	/**
	 * Erstellt eine Response in Form einer einzelnen Html-Datei oder ZIP-Datei mit den mehreren generierten Html-Dateien.
	 *
	 * @return Im Falle eines Success enthält die HTTP-Response das Html-Dokument oder die ZIP-Datei. Im Fehlerfall wird
	 *     eine ApiOperationException ausgelöst oder bei Fehlercode 500 eine SimpleOperationResponse mit Logdaten zurückgegeben.
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
	 * Erzeugt auf Basis der gegebenen html-Vorlage und der übergebenen Daten die Html-Dateiinhalte in Form einer Map "Dateiname > Dateiinhalt"
	 *
	 * @return Eine Map mit den Dateinamen und Html-Dateiinhalten.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private List<HtmlBuilder> getHtmlBuilders() throws ApiOperationException {

		reportingRepository.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der html-Builder.");
		final List<HtmlBuilder> htmlBuilders = new ArrayList<>();

		if (!reportingParameter.einzelausgabeHauptdaten && !reportingParameter.einzelausgabeDetaildaten) {
			// Dateiname der Dateien aus den Daten erzeugen.
			final String dateiname = getDateiname(mapHtmlContexts);

			// Html-Builder erstellen und damit das html mit Daten für die Html-Datei erzeugen
			reportingRepository.logger()
					.logLn(LogLevel.DEBUG, 4,
							"Verarbeite Template (%s) und Daten aus den Kontexten zum finalen html-Dateiinhalt.".formatted(htmlTemplateDefinition.name()));
			htmlBuilders.add(
					new HtmlBuilder(ResourceUtils.text(htmlTemplateDefinition.getPfadHtmlTemplate()), mapHtmlContexts.values().stream().toList(), dateiname));
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

					// Html-Builder erstellen und damit das html mit Daten für die Html-Datei erzeugen
					htmlBuilders.add(
							new HtmlBuilder(ResourceUtils.text(htmlTemplateDefinition.getPfadHtmlTemplate()), mapHtmlContexts.values().stream().toList(),
									dateiname));
				}
			}
		} else {
			// Die Detaildatenquelle soll in einzelne Kontexte für Einzeldateien zerlegt werden. Die Hauptdatenquelle ist dabeii für alle Einzelkontexte gleich.
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

					// Html-Builder erstellen und damit das html mit Daten für die Html-Datei erzeugen
					htmlBuilders.add(
							new HtmlBuilder(ResourceUtils.text(htmlTemplateDefinition.getPfadHtmlTemplate()), mapHtmlContexts.values().stream().toList(),
									dateiname));
				}
			}
		}

		reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der html-Builder.");
		return htmlBuilders;
	}


	/**
	 * Erstellt den Dateinamen gemäß der in der Template-Definition hinterlegten Vorlage für den Dateinamen. Dabei können die Daten den Contexts entnommen werden.
	 *
	 * @param mapHtmlContexts Map mit den bereits erzeugten html-Datenkontexten, um daraus Daten für den Dateinamen entnehmen zu können.
	 *
	 * @return Der fertige Dateiname.
	 *
	 * @throws ApiOperationException   im Fehlerfall
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
		} catch (final Exception e) {
			// Rückgabewert von getCanonicalFile wird ignoriert. Diese Funktion prüft aber den Dateinamen und wirft eine Exception, wenn unzulässiger Name.
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Der generierte html-Dateiname enthält ungültige Zeichen.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "FEHLER: Der generierte html-Dateiname enthält ungültige Zeichen.");
		}

		return dateiname;
	}


	/**
	 * Erstellt eine ZIP-Datei, die alle Html-Dateien aus der übergebenen Map enthält.
	 *
	 * @param htmlBuilders Eine Liste mit den HtmlBuilders, die die Html-Inhalte erzeugen.
	 *
	 * @return Gibt das ZIP in Form eines ByteArrays zurück.
	 *
	 * @throws ApiOperationException  im Fehlerfall
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
		} catch (@SuppressWarnings("unused") final IOException e) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Die erzeugten Html-Inhalte konnten nicht als ZIP-Datei zusammengestellt werden.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"FEHLER: Die erzeugten Html-Inhalte konnten nicht als ZIP-Datei zusammengestellt werden.");
		}
		return zipData;
	}

}
