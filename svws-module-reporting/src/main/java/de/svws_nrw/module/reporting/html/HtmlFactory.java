package de.svws_nrw.module.reporting.html;

import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.core.data.reporting.ReportingAusgabedaten;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.reporting.ReportingErrorResponse;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextDruckparameter;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextSchueler;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextSchule;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.validierung.ReportingValidierung;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

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


/**
 * Diese Klasse beinhaltet den Code zur Erstellung von Html-Inhalten auf Basis der hinterlegten html-Vorlage und den übergebenen Daten.
 * Sie setzt voraus, dass zum übergebenen html-Template eine css-Datei mit gleichem Pfad und Namen existiert.
 */
public final class HtmlFactory {

	/** Die Verbindung zur Datenbank. */
	private final DBEntityManager conn;

	/** Die Daten für die Report-Ausgabe. */
	private final ReportingAusgabedaten reportingAusgabedaten;

	/** Die Template-Definition für die Erstellung der Html-Datei */
	private final HtmlTemplateDefinition htmlTemplateDefinition;

	/** Eine Map zum Sammeln der erstellten Html-Contexts. */
	final Map<String, HtmlContext> mapHtmlContexts = new HashMap<>();

	/** Liste, die Einträge aus dem Logger sammelt. */
	private LogConsumerList log;

	/** Logger, der den Ablauf protokolliert und Fehlerdaten sammelt */
	private Logger logger;


	/**
	 * Erzeugt eine neue Html-Factory, um eine Html-Datei aus einem html-Template zu erzeugen.
	 * @param conn Die Verbindung zur Datenbank.
	 * @param reportingAusgabedaten Das Objekt, welches die Angaben zu den Daten des Reports und den zugehörigen Einstellungen enthält.
	 * @param logger Logger, der die Erstellung der Reports protokolliert.
	 * @param log Log, das die Erstellung des Reports protokolliert.
	 */
	public HtmlFactory(final DBEntityManager conn, final ReportingAusgabedaten reportingAusgabedaten, final Logger logger, final LogConsumerList log) {

		this.logger = logger;
		this.log = log;

		if (logger == null || log == null) {
			this.logger = new Logger();
			this.log = new LogConsumerList();
			this.logger.addConsumer(this.log);
		}

		// Validiere Datenbankverbindung
		if (conn == null)
			throw OperationError.NOT_FOUND.exception("Es wurde keine Verbindung zur Datenbank übergeben.");

		this.conn = conn;

		// Validiere Reporting-Ausgabedaten
		if (reportingAusgabedaten == null)
			throw OperationError.NOT_FOUND.exception("Es wurde keine Daten zur Ausgabe im Report übergeben.");

		this.reportingAusgabedaten = reportingAusgabedaten;

		// Validiere die Angaben zur Vorlage für den Report.
		if (ReportingReportvorlage.getByBezeichnung(reportingAusgabedaten.reportvorlage) == null)
			throw OperationError.NOT_FOUND.exception("Es wurde keine gültige Report-Vorlage übergeben.");

		this.htmlTemplateDefinition = HtmlTemplateDefinition.getByType(ReportingReportvorlage.getByBezeichnung(reportingAusgabedaten.reportvorlage));

		if (this.htmlTemplateDefinition == null)
			throw OperationError.INTERNAL_SERVER_ERROR.exception("Template-Definitionen inkonsistent.");

		// Prüfe, ob die Rechte des Benutzers zu den in der TemplateDefinition hinterlegten Rechten passen.
		logger.logLn("Prüfe die Berechtigungen des Benutzers für den Zugriff auf die für die Ausgabe notwendigen Daten.");
		if (!conn.getUser().pruefeKompetenz(new HashSet<>(htmlTemplateDefinition.getBenutzerKompetenzen())))
			throw OperationError.FORBIDDEN.exception("Der Benutzer hat nicht die erforderlichen Rechte, um auf die Daten für die Erstellung der Ausgabe zu zugreifen.");

		// Validiere Hauptdaten-Angabe
		if (reportingAusgabedaten.idsHauptdaten == null || reportingAusgabedaten.idsHauptdaten.isEmpty())
			throw OperationError.NOT_FOUND.exception("Es wurden keine Daten zum Drucken übergeben.");

		// Setze Werte nach Validierung, falls null.
		if (reportingAusgabedaten.idsDetaildaten == null)
			this.reportingAusgabedaten.idsDetaildaten = new ArrayList<>();

		getContexts();
	}


	/** Erzeugte die Contexts für die html-Erstellung. */
	private void getContexts() {
		// Klasse für die Validierung der über die API übergebenen Daten.
		final ReportingValidierung reportingValidierung = new ReportingValidierung();

		logger.logLn("Erzeuge Repository");
		final ReportingRepository reportingRepository = new ReportingRepository(conn);

		logger.logLn("Erzeuge Datenkontext Schule");
		final HtmlContextSchule htmlContextSchule = new HtmlContextSchule(reportingRepository);
		mapHtmlContexts.put("Schule", htmlContextSchule);

		logger.logLn("Erzeuge Datenkontext Druckparameter");
		final HtmlContextDruckparameter htmlContextDruckparameter = new HtmlContextDruckparameter(reportingAusgabedaten.detailLevel, reportingAusgabedaten.idsDetaildaten);
		mapHtmlContexts.put("Druckparameter", htmlContextDruckparameter);

		// Betrachte die Html-Template-Definition und erzeuge damit die korrekten Contexts der Hauptdaten
		switch (htmlTemplateDefinition.name().substring(0, htmlTemplateDefinition.name().indexOf("_v_"))) {
			case "SCHUELER" :
				// Schüler-Context ist Hauptdatenquelle
				logger.logLn("Erzeuge Datenkontext Schüler - %d IDs von Schülern wurden übergeben für Template %s.".formatted(reportingAusgabedaten.idsHauptdaten.size(), htmlTemplateDefinition.name()));
				reportingValidierung.validiereSchuelerDaten(reportingRepository, reportingAusgabedaten.idsHauptdaten, htmlTemplateDefinition.name().startsWith("SCHUELER_v_GOST_LAUFBAHNPLANUNG_"), htmlTemplateDefinition.name().startsWith("SCHUELER_v_GOST_ABITUR_"), true);
				final HtmlContextSchueler htmlContextSchueler = new HtmlContextSchueler(reportingRepository, reportingAusgabedaten.idsHauptdaten);
				mapHtmlContexts.put("Schueler", htmlContextSchueler);
				break;
			case "GOST_KURSPLANUNG" :
				// GOSt-Kursplanung-Blockungsergebnis-Context ist Hauptdatenquelle
				logger.logLn("Erzeuge Datenkontext Kursplanung-Blockungsergebnis mit ID %s für Template %s.".formatted(reportingAusgabedaten.idsHauptdaten.getFirst(), htmlTemplateDefinition.name()));
				final HtmlContextGostKursplanungBlockungsergebnis htmlContextBlockung = new HtmlContextGostKursplanungBlockungsergebnis(conn, reportingAusgabedaten.idsHauptdaten.getFirst());
				mapHtmlContexts.put("Blockungsergebnis", htmlContextBlockung);
				break;
			default:
				break;
		}

		logger.logLn("Erzeugung der Kontexte abgeschlossen.");
	}


	/**
	 * Erzeugt auf Basis der gegebenen html-Vorlage und der übergebenen Daten die HtmlBuilder, aus denen die Html-Inhalte erzuegt werden können"
	 * @return Eine Liste mit HtmlBuilder.
	 */
	public List<HtmlBuilder> createHtmlBuilders() {
		return getHtmlBuilders();
	}


	/**
	 * Erstellt eine Response in Form einer einzelnen Html-Datei oder ZIP-Datei mit den mehreren generierten Html-Dateien.
	 * @return Im Falle eines Success enthält die HTTP-Response das Html-Dokument oder die ZIP-Datei. Im Fehlerfall wird eine WebApplicationException ausgelöst oder bei Fehlercode 500 eine SimpleOperationResponse mit Logdaten zurückgegeben.
	 */
	public Response createHtmlResponse() {

		try {
			final List<HtmlBuilder> htmlBuilders = getHtmlBuilders();
			if (!htmlBuilders.isEmpty()) {
				if (!reportingAusgabedaten.einzelausgabeHauptdaten || htmlBuilders.size() == 1) {
					final String encodedFilename = "filename*=UTF-8''" + URLEncoder.encode(htmlBuilders.getFirst().getDateiname(), StandardCharsets.UTF_8);
					return Response.ok(htmlBuilders.getFirst().getHtml(), "text/html")
						.header("Content-Disposition", "attachment; " + encodedFilename)
						.build();
				} else {
					if (htmlTemplateDefinition.getDateiname().isEmpty())
						throw OperationError.INTERNAL_SERVER_ERROR.exception("Die gewählte Vorlage kann nicht einzelne Html-Inhalte erstellen.");

					final byte[] zipData = createZIP(htmlBuilders);
					final String encodedFilename = "filename*=UTF-8''" + URLEncoder.encode(htmlTemplateDefinition.getDateiname() + ".zip", StandardCharsets.UTF_8);

					return Response.ok(zipData, "application/zip")
						.header("Content-Disposition", "attachment; " + encodedFilename)
						.build();
				}
			} else {
				throw OperationError.INTERNAL_SERVER_ERROR.exception("Es sind keine Html-Inhalte generiert worden.");
			}
		} catch (Exception e) {
			return new ReportingErrorResponse(e, logger, log).getResponse();
		}
	}


	/**
	 * Erzeugt auf Basis der gegebenen html-Vorlage und der übergebenen Daten die Html-Dateiinhalte in Form einer Map "Dateiname > Dateiinhalt"
	 * @return Eine Map mit den Dateinamen und Html-Dateiinhalten.
	 */
	private List<HtmlBuilder> getHtmlBuilders() {

		final List<HtmlBuilder> htmlBuilders = new ArrayList<>();

		if (!reportingAusgabedaten.einzelausgabeHauptdaten) {
			// Dateiname der Dateien aus den Daten erzeugen.
			logger.logLn("Erzeuge Dateinamen.");
			String dateiname = getDateiname(mapHtmlContexts);

			// Html-Builder erstellen und damit das html mit Daten für die Html-Datei erzeugen
			logger.logLn("Verarbeite Template (%s) und Daten aus den Kontexten zum finalen html-Dateiinhalt.".formatted(htmlTemplateDefinition.name()));
			htmlBuilders.add(new HtmlBuilder(ResourceUtils.text(htmlTemplateDefinition.getPfadHtmlTemplate()), mapHtmlContexts.values().stream().toList(), dateiname));
		} else if (htmlTemplateDefinition.name().startsWith("SCHUELER_v_")) {
			// Zerlege den Gesamt-Schüler-Context in einzelne Contexts mit jeweils einen Schüler
			logger.logLn("Erzeuge einzelne Kontexte für jeden Schüler, da einzelne Dateien angefordert wurden.");
			final List<HtmlContextSchueler> schuelerContexts = ((HtmlContextSchueler) mapHtmlContexts.get("Schueler")).getEinzelSchuelerContexts();

			for (final HtmlContextSchueler schuelerContext : schuelerContexts) {
				mapHtmlContexts.put("Schueler", schuelerContext);

				// Dateiname der Dateien aus den Daten erzeugen.
				logger.logLn("Erzeuge Dateinamen.");
				String dateiname = getDateiname(mapHtmlContexts);

				// Html-Builder erstellen und damit das html mit Daten für die Html-Datei erzeugen
				logger.logLn("Verarbeite Template (%s) und Daten aus den Kontexten zum finalen html-Dateiinhalt.".formatted(htmlTemplateDefinition.name()));
				htmlBuilders.add(new HtmlBuilder(ResourceUtils.text(htmlTemplateDefinition.getPfadHtmlTemplate()), mapHtmlContexts.values().stream().toList(), dateiname));
			}
		}

		return htmlBuilders;
	}


	/** Erstellt den Dateinamen gemäß der in der Template-Definition hinterlegten Vorlage für den Dateinamen. Dabei können die Daten den Contexts entnommen werden.
	 * @param mapHtmlContexts Map mit den bereits erzeugten html-Datenkontexten, um daraus Daten für den Dateinamen entnehmen zu können.
	 * @return Der fertige Dateiname.
	 */
	private String getDateiname(final Map<String, HtmlContext> mapHtmlContexts) {

		logger.logLn("Erzeuge den Dateinamen zum Template %s.".formatted(htmlTemplateDefinition.name()));

		String dateiname = htmlTemplateDefinition.getDateiname();

		if (!htmlTemplateDefinition.getDateinamensvorlage().isEmpty() && !htmlTemplateDefinition.getDateinamensvorlage().isBlank()) {
			final HtmlBuilder htmlBuilder = new HtmlBuilder(htmlTemplateDefinition.getDateinamensvorlage(), mapHtmlContexts.values().stream().toList(), dateiname);
			final String html = htmlBuilder.getHtml();

			if (html == null || html.isEmpty() || html.isBlank() || !html.contains("<p>") || !html.contains("</p>") || (html.indexOf("<p>") + 3) >= html.indexOf("</p>"))
				throw OperationError.INTERNAL_SERVER_ERROR.exception("Der Dateiname konnte nicht gemäß des angegebenen Musters aus den Daten generiert werden.");

			dateiname = html.substring(html.indexOf("<p>") + 3, html.indexOf("</p>"));
		}

		try {
			// Prüfe, ob der erzeugte Dateiname konform zu System ist. Andernfalls wird hier eine Exception ausgelöst.
			(new File(dateiname + ".html")).getCanonicalFile();
		} catch (Exception e) {
			throw OperationError.INTERNAL_SERVER_ERROR.exception("Der generierte Dateiname enthält ungültige Zeichen.");
		}

		return dateiname;
	}


	/**
	 * Erstellt eine ZIP-Datei, die alle Html-Dateien aus der übergebenen Map enthält.
	 * @param htmlBuilders Eine Liste mit den HtmlBuilders, die die Html-Inhalte erzeugen.
	 * @return Gibt das ZIP in Form eines ByteArrays zurück.
	 */
	private byte[] createZIP(final List<HtmlBuilder> htmlBuilders) throws WebApplicationException {
		final byte[] zipData;
		try {
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				try (ZipOutputStream zos = new ZipOutputStream(baos)) {
					for (final HtmlBuilder htmlBuilder: htmlBuilders) {
						zos.putNextEntry(new ZipEntry(htmlBuilder.getDateinameMitEndung()));
						zos.write(htmlBuilder.getHtmlByteArray());
						zos.closeEntry();
					}
					baos.flush();
				}
				zipData = baos.toByteArray();
			}
		} catch (@SuppressWarnings("unused") final IOException e) {
			throw OperationError.INTERNAL_SERVER_ERROR.exception("Die erzeugten Html-Inhalte konnten nicht als ZIP-Datei zusammengestellt werden.");
		}
		return zipData;
	}
}
