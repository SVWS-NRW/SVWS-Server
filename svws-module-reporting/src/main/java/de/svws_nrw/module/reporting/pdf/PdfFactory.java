package de.svws_nrw.module.reporting.pdf;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.reporting.html.base.HtmlBuilder;
import de.svws_nrw.module.reporting.html.base.HtmlContext;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextDruckparameter;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextSchueler;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextSchule;
import de.svws_nrw.module.reporting.html.contexts.gost.kursplanung.HtmlContextGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.validierung.ReportingValidierung;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.thymeleaf.exceptions.TemplateProcessingException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * Diese Klasse beinhaltet den Code zur Erstellung einer pdf-Datei auf Basis der hinterlegten html-Vorlage und den übergebenen Daten.
 * Sie setzt voraus, dass zum übergebenen html-Template eine css-Datei mit gleichem Pfad und Namen existiert.
 */
public final class PdfFactory {

	/**
	 * Eine Liste der unterstützten html-Vorlagen, die auf den GOSt-Kursplanungsdaten basieren und diese daher für die Generierung benötigen.
	 * ACHTUNG: Bei Änderung oder Anpassung muss auch Switch-Anweisung in getPdfDateiname angepasst werden.
	 */
	static final List<String> supportedGostKursplanungTemplates = List.of("GostKursplanungSchuelerMitSchienenKursen.html", "GostKursplanungSchuelerMitKursen.html", "GostKursplanungKursMitKursschuelern.html");

	/**
	 * Eine Liste der unterstützten html-Vorlagen, die auf den Schülerdaten basieren und diese daher für die Generierung benötigen.
	 * ACHTUNG: Bei Änderung oder Anpassung muss auch Switch-Anweisung in getPdfDateiname angepasst werden.
	 */
	static final List<String> supportedSchuelerTemplates = List.of("APOGOStAnlage12.html", "GostLaufbahnplanungWahlbogen.html", "GostLaufbahnplanungErgebnisuebersicht.html");



	/** Die Verbindung zur Datenbank. */
	private final DBEntityManager conn;

	/** Pfad und Dateiname mit der Thymeleaf-html-Dokumentvorlage, aus der später die PDF-Datei erzeugt wird. */
	private final String dateipfadHtmlTemplate;

	/** Pfad zur css-Datei, die in der html-Dokumentvorlage verlinkt wurde. Er wird vom PDF-Builder benötigt, um als baseURI für nachladbare Dateien zu fungieren. */
	private final String dateipfadCss;

	/** Legt fest, ob pro Datensatz der Detaildaten eine einzelne PDF-Datei erzeugt werden soll. */
	private final boolean einzelausgabeDetaildaten;

	/** Legt fest, ob pro Datensatz der Hauptdaten eine einzelne PDF-Datei erzeugt werden soll. */
	private final boolean einzelausgabeHauptdaten;

	/** Eine Liste von IDs für die Ausgabe von Detaildaten zu den Hauptdaten. */
	private final List<Long> idsDetaildaten;

	/** Eine Liste von IDs für die Hauptdatenquelle des zu erstellenden PDF. */
	private final List<Long> idsHauptdaten;

	/** Eine Map zum Sammeln der erstellten html-Contexts. */
	final Map<String, HtmlContext> mapHtmlContexts = new HashMap<>();

	/** Parameter (>= 0), der in Templates verwendet werden kann, um den Detailgrad der Darstellung zu steuern. */
	private final int parameterDetailLevel;



	// Interne Variablen für Stellungen.

	/** Der Dateiname des Templates, der aus dem übergebenen Dateipfad ermittelt wird. */
	private String dateinameHtmlTemplate = "";

	/** Der Dateiname der ZIP-Datei, wenn Einzel-PDFs erzeugt werden sollen. */
	private String dateinameZIP = "";

	/** Liste, die Einträge aus dem Logger sammelt. */
	private final LogConsumerList log = new LogConsumerList();

	/** Logger, der den Ablauf protokolliert und Fehlerdaten sammelt */
	private final Logger logger = new Logger();


	/**
	 * Erzeugt eine neue PdfFactory, um eine Pdf-Datei aus einem html-Template zu erzeugen.
	 * @param conn Die Verbindung zur Datenbank.
	 * @param dateipfadHtmlTemplate Pfad und Dateiname mit der Thymeleaf-html-Dokumentvorlage, aus der später die PDF-Datei erzeugt wird.
	 * @param idsHauptdaten Eine Liste von IDs für die Hauptdatenquelle des zu erstellenden PDF.
	 * @param einzelausgabeHauptdaten Legt fest, ob pro Datensatz der Hauptdaten eine einzelne PDF-Datei erzeugt werden soll.
	 * @param idsDetaildaten Eine Liste von IDs für die Ausgabe von Detaildaten zu den Hauptdaten.
	 * @param einzelausgabeDetaildaten Legt fest, ob pro Datensatz der Detaildaten eine einzelne PDF-Datei erzeugt werden soll.
	 * @param parameterDetailLevel Parameter, der in Templates verwendet werden kann, um den Detailgrad der Darstellung zu steuern.
	 */
	public PdfFactory(final DBEntityManager conn, final String dateipfadHtmlTemplate, final List<Long> idsHauptdaten, final boolean einzelausgabeHauptdaten, final List<Long> idsDetaildaten, final boolean einzelausgabeDetaildaten, final int parameterDetailLevel) {

		logger.addConsumer(log);

		// Validiere Datenbankverbindung
		if (conn == null)
			throw OperationError.NOT_FOUND.exception("Es wurde keine Verbindung zur Datenbank übergeben.");

		this.conn = conn;

		// Validiere html-Template-Angabe
		if (dateipfadHtmlTemplate == null || dateipfadHtmlTemplate.isEmpty() || dateipfadHtmlTemplate.isBlank() || !dateipfadHtmlTemplate.toLowerCase().endsWith(".html"))
			throw OperationError.NOT_FOUND.exception("Es wurde kein gültiger Pfad zu einem html-Template übergeben.");

		this.dateipfadHtmlTemplate = dateipfadHtmlTemplate;
		this.dateinameHtmlTemplate = dateipfadHtmlTemplate.substring(dateipfadHtmlTemplate.lastIndexOf('/') + 1);

		if (supportedSchuelerTemplates.stream().noneMatch(t -> t.equals(dateinameHtmlTemplate)) && supportedGostKursplanungTemplates.stream().noneMatch(t -> t.equals(dateinameHtmlTemplate)))
			throw OperationError.NOT_FOUND.exception("Keine für die Erstellung der PDF-Datei unterstützte Vorlage gefunden.");

		this.dateipfadCss = dateipfadHtmlTemplate.substring(0, dateipfadHtmlTemplate.lastIndexOf('.') + 1) + "css";

		// Validiere Hauptdaten-Angabe
		if (idsHauptdaten == null || idsHauptdaten.isEmpty())
			throw OperationError.NOT_FOUND.exception("Es wurden keine Daten zum Drucken übergeben.");

		this.idsHauptdaten = idsHauptdaten;

		// Setze weitere Werte.
		this.einzelausgabeHauptdaten = einzelausgabeHauptdaten;
		this.idsDetaildaten = (idsDetaildaten == null) ? new ArrayList<>() : idsDetaildaten;
		this.einzelausgabeDetaildaten = einzelausgabeDetaildaten;
		this.parameterDetailLevel = parameterDetailLevel;

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
		final HtmlContextDruckparameter htmlContextDruckparameter = new HtmlContextDruckparameter(this.parameterDetailLevel, this.idsDetaildaten);
		mapHtmlContexts.put("Druckparameter", htmlContextDruckparameter);

		if (supportedSchuelerTemplates.stream().anyMatch(t -> t.equals(dateinameHtmlTemplate))) {
			logger.logLn("Erzeuge Datenkontext Schüler - %d IDs von Schülern wurden übergeben für Template %s.".formatted(idsHauptdaten.size(), this.dateinameHtmlTemplate));
			switch (dateinameHtmlTemplate) {
				case "APOGOStAnlage12.html" :
					reportingValidierung.validiereSchuelerDaten(reportingRepository, idsHauptdaten, false, true, true);
					break;
				case "GostLaufbahnplanungWahlbogen.html", "GostLaufbahnplanungErgebnisuebersicht.html" :
					reportingValidierung.validiereSchuelerDaten(reportingRepository, idsHauptdaten, true, false, true);
					break;
				default :
					throw OperationError.NOT_FOUND.exception("Keine für die Erstellung der PDF-Datei notwendigen Schüler angegeben.");
			}
			final HtmlContextSchueler htmlContextSchueler = new HtmlContextSchueler(reportingRepository, idsHauptdaten);
			mapHtmlContexts.put("Schueler", htmlContextSchueler);
		}

		if (supportedGostKursplanungTemplates.stream().anyMatch(t -> t.equals(dateinameHtmlTemplate))) {
			logger.logLn("Erzeuge Datenkontext Kursplanung-Blockungsergebnis mit ID %s für Template %s.".formatted(idsHauptdaten.getFirst(), this.dateinameHtmlTemplate));
			final HtmlContextGostKursplanungBlockungsergebnis htmlContextBlockung;
            if (dateinameHtmlTemplate.equals("GostKursplanungSchuelerMitSchienenKursen.html") || dateinameHtmlTemplate.equals("GostKursplanungSchuelerMitKursen.html") || dateinameHtmlTemplate.equals("GostKursplanungKursMitKursschuelern.html")) {
                htmlContextBlockung = new HtmlContextGostKursplanungBlockungsergebnis(conn, idsHauptdaten.getFirst());
                mapHtmlContexts.put("Blockungsergebnis", htmlContextBlockung);
            } else {
                throw OperationError.NOT_FOUND.exception("Kein für die Erstellung der PDF-Datei notwendiges Blockungsergebnis angegeben.");
            }
		}

		logger.logLn("Erzeugung der Kontexte abgeschlossen.");
	}


	/**
	 * Erstellt eine Response in Form einer einzelnen PDF-Datei oder ZIP-Datei mit den mehreren generierten PDF-Dateien.
	 * @return Im Falle eines Success enthält die HTTP-Response das PDF-Dokument oder die ZIP-Datei. Im Fehlerfall wird eine WebApplicationException ausgelöst oder bei Fehlercode 500 eine SimpleOperationResponse mit Logdaten zurückgegeben.
	 */
	public Response create() {

		try {
			final List<PdfBuilder> pdfBuilders = getPdfBuilders();
			if (!pdfBuilders.isEmpty()) {
				if (!einzelausgabeHauptdaten || pdfBuilders.size() == 1) {
					return pdfBuilders.getFirst().getPdfResponse();
				} else {
					if (dateinameZIP.isEmpty())
						throw OperationError.INTERNAL_SERVER_ERROR.exception("Die gewählte Vorlage kann nicht einzelne PDFs erstellen.");

					final byte[] zipData = createZIP(pdfBuilders);
					final String encodedFilename = "filename*=UTF-8''" + URLEncoder.encode(dateinameZIP, StandardCharsets.UTF_8);

					return Response.ok(zipData, "application/zip")
						.header("Content-Disposition", "attachment; " + encodedFilename)
						.build();
				}
			} else {
				throw OperationError.INTERNAL_SERVER_ERROR.exception("Es sind keine PDF-Builder generiert worden.");
			}
		} catch (Exception e) {
			return errorResponse(e);
		}
	}


	/**
	 * Erzeugt auf Basis der hinterlegten html-Vorlage und der übergebenen Daten die PdfBuilder zur Erzeugung der PDF-Dateien.
	 * @return Ein oder mehrere PDF-Builder zur Erzeugung der PDF-Dateien.
	 */
	private List<PdfBuilder> getPdfBuilders() {

		final List<PdfBuilder> pdfBuilders = new ArrayList<>();

		if (einzelausgabeHauptdaten && (mapHtmlContexts.containsKey("Schueler") && !(((HtmlContextSchueler) mapHtmlContexts.get("Schueler")).getSchueler().isEmpty()))) {
			logger.logLn("Erzeuge einzelne Kontexte für jeden Schüler, da Einzel-PDFs angefordert wurden.");
			// Zerlege den Gesamt-Schüler-Context in einzelne Contexts mit jeweils einen Schüler
			final List<HtmlContextSchueler> schuelerContexts = ((HtmlContextSchueler) mapHtmlContexts.get("Schueler")).getEinzelSchuelerContexts();

			for (final HtmlContextSchueler schuelerContext : schuelerContexts) {
				mapHtmlContexts.put("Schueler", schuelerContext);

				// Dateiname der PDF-Datei aus den Daten erzeugen.
				logger.logLn("Erzeuge PDF-Dateinamen.");
				String pdfDateiname = getPdfDateiname(mapHtmlContexts);

				// html-Builder erstellen und damit das html mit Daten für die PDF-Datei erzeugen
				logger.logLn("Verarbeite Template (%s) und Daten aus den Kontexten zum finalen html für die PDF-Dateien.".formatted(dateipfadHtmlTemplate));
				final HtmlBuilder htmlBuilder = new HtmlBuilder(dateipfadHtmlTemplate, mapHtmlContexts.values().stream().toList());
				final String html = htmlBuilder.getHtml();

				logger.logLn("Erzeuge PDF-Builder mit finalem html für die PDF-Dateien");
				pdfBuilders.add(new PdfBuilder(html, dateipfadCss, pdfDateiname));
			}
		} else {
			// Dateiname der PDF-Datei aus den Daten erzeugen.
			logger.logLn("Erzeuge PDF-Dateinamen.");
			String pdfDateiname = getPdfDateiname(mapHtmlContexts);

			// html-Builder erstellen und damit das html mit Daten für die PDF-Datei erzeugen
			logger.logLn("Verarbeite Template (%s) und Daten aus den Kontexten zum finalen html für die PDF-Datei.".formatted(dateipfadHtmlTemplate));
			final HtmlBuilder htmlBuilder = new HtmlBuilder(dateipfadHtmlTemplate, mapHtmlContexts.values().stream().toList());
			final String html = htmlBuilder.getHtml();

			logger.logLn("Erzeuge PDF-Builder mit finalem html für die PDF-Datei.");
			pdfBuilders.add(new PdfBuilder(html, dateipfadCss, pdfDateiname));
		}

		return pdfBuilders;
	}


	/** Erstellt den Dateinamen der Pdf-Datei auf Basis des html-Templates und evtl. der übergebenen Daten
	 * @param mapHtmlContexts	Map mit den bereits erzeugten html-Datenkontexten, um daraus Daten für den Dateinamen entnehmen zu können.
	 * @return Der Dateiname für die Pdf-Datei.
	 */
	private String getPdfDateiname(final Map<String, HtmlContext> mapHtmlContexts) {

		String dateinamePdf;

		// Für Dateinamen mit schülerbezogenen Daten ermittle die Anzahl der Schüler und den ersten Schüler des Kontextes Schueler.
		ReportingSchueler ersterSchueler = null;
		int anzahlContextSchueler = 0;
		if (mapHtmlContexts.containsKey("Schueler") && !(((HtmlContextSchueler) mapHtmlContexts.get("Schueler")).getSchueler().isEmpty())) {
			logger.logLn("> Schülerkontext vorhanden, ermittle den ersten Schüler.");
			ersterSchueler = ((HtmlContextSchueler) mapHtmlContexts.get("Schueler")).getSchueler().getFirst();
			anzahlContextSchueler = ((HtmlContextSchueler) mapHtmlContexts.get("Schueler")).getSchueler().size();
			if (ersterSchueler != null)
				logger.logLn("> Ersten Schüler von insgesamt %d ermittelt. Schüler-ID: %d".formatted(anzahlContextSchueler, ersterSchueler.id()));
			else
				logger.logLn("> Schülerkontext ist ohne Schüler.");
		} else {
			logger.logLn("> Kein Schülerkontext vorhanden.");
		}

		// Bei schülerbasierten Vorlagen muss es mindestens einen Schüler geben, wenn nicht, gib einen Fehler aus.
		if (ersterSchueler == null && supportedSchuelerTemplates.stream().anyMatch(t -> t.equals(dateinameHtmlTemplate))) {
			throw OperationError.NOT_FOUND.exception("Keine für die Erstellung der PDF-Datei notwendigen Schüler gefunden.");
		}

		// Für Dateinamen mit blockungsbasierten Daten ermittle das genutzte Ergebnis.
		ReportingGostKursplanungBlockungsergebnis blockungsergebnis = null;
		if (mapHtmlContexts.containsKey("Blockungsergebnis")) {
			logger.logLn("> Blockungsergebnis vorhanden, ermittle dessen Daten.");
			blockungsergebnis = (ReportingGostKursplanungBlockungsergebnis) mapHtmlContexts.get("Blockungsergebnis").getContext().getVariable("Blockungsergebnis");
		}

		// Bei blockungsbasierten Vorlagen muss es ein Blockungsergebnis geben, wenn nicht, gib einen Fehler aus.
		if (blockungsergebnis == null && supportedGostKursplanungTemplates.stream().anyMatch(t -> t.equals(dateinameHtmlTemplate))) {
			throw OperationError.NOT_FOUND.exception("Keine für die Erstellung der PDF-Datei notwendiges Blockungsergebnis gefunden.");
		}

		// Erzeuge den Dateinamen der pdf mit Daten.
		logger.logLn("> Dateiname für %s erzeugen.".formatted(dateinameHtmlTemplate));
		switch (dateinameHtmlTemplate) {
			case "APOGOStAnlage12.html" :
				assert ersterSchueler != null; // Schülerbasierte Vorlage, daher vorher schon ersterSchueler != null geprüft.
				dateinameZIP = "APO-GOSt-Anlage12.zip";
				dateinamePdf = "APO-GOSt-Anlage12.pdf";
				if (anzahlContextSchueler == 1) {
                    dateinamePdf = "APO-GOSt-Anlage12_%s_%s_(%d).pdf".formatted(
						ersterSchueler.nachname().replace(' ', '_').replace('.', '_'),
						ersterSchueler.vorname().replace(' ', '_').replace('.', '_'),
						ersterSchueler.id());
				}
				break;
			case "GostLaufbahnplanungWahlbogen.html" :
				assert ersterSchueler != null; // Schülerbasierte Vorlage, daher vorher schon ersterSchueler != null geprüft.
				dateinameZIP = "GostLaufbahnplanung.zip";
				if (anzahlContextSchueler == 1) {
					dateinamePdf = "GostLaufbahnplanung_Abitur_%d_%s_%s_%s_(%d).pdf".formatted(
						ersterSchueler.gostLaufbahnplanung().abiturjahr(),
						ersterSchueler.gostLaufbahnplanung().folgeGOStHalbjahr().replace(".", ""),
						ersterSchueler.nachname().replace(' ', '_').replace('.', '_'),
						ersterSchueler.vorname().replace(' ', '_').replace('.', '_'),
						ersterSchueler.id());
				} else {
					dateinamePdf = "GostLaufbahnplanung_Abitur_%d_%s.pdf".formatted(
						ersterSchueler.gostLaufbahnplanung().abiturjahr(),
						ersterSchueler.gostLaufbahnplanung().folgeGOStHalbjahr().replace('.', '_'));
				}
				break;
			case "GostLaufbahnplanungErgebnisuebersicht.html" :
				assert ersterSchueler != null; // Schülerbasierte Vorlage, daher vorher schon ersterSchueler != null geprüft.
				dateinamePdf = "Laufbahnplanung_Prüfungsergebnisse_Abitur-%d_%s.pdf".formatted(
					ersterSchueler.gostLaufbahnplanung().abiturjahr(),
					ersterSchueler.gostLaufbahnplanung().folgeGOStHalbjahr().replace('.', '_'));
				break;
			case "GostKursplanungSchuelerMitSchienenKursen.html" :
				assert blockungsergebnis != null; // Blockungsbasierte Vorlage, daher vorher schon blockungsergebnis != null geprüft.
				dateinameZIP = "Schueler-Schienen-Kurse-Zuordnung.zip";
				dateinamePdf = "Schueler-Schienen-Kurse-Zuordnung_%d-%s_(Erg-ID%d).pdf".formatted(
					blockungsergebnis.abiturjahr(),
					blockungsergebnis.gostHalbjahr().kuerzel.replace(".", "-"),
					blockungsergebnis.id());
				break;
			case "GostKursplanungSchuelerMitKursen.html" :
				assert blockungsergebnis != null; // Blockungsbasierte Vorlage, daher vorher schon blockungsergebnis != null geprüft.
				dateinameZIP = "Schueler-Schienen-Kurse-Zuordnung.zip";
				dateinamePdf = "Schueler-Kurse-Liste_%d-%s_(Erg-ID%d).pdf".formatted(
					blockungsergebnis.abiturjahr(),
					blockungsergebnis.gostHalbjahr().kuerzel.replace(".", "-"),
					blockungsergebnis.id());
				break;
			case "GostKursplanungKursMitKursschuelern.html" :
				assert blockungsergebnis != null; // Blockungsbasierte Vorlage, daher vorher schon blockungsergebnis != null geprüft.
				dateinameZIP = "Kursliste.zip";
				dateinamePdf = "Kursliste_%d-%s_(Erg-ID%d).pdf".formatted(
					blockungsergebnis.abiturjahr(),
					blockungsergebnis.gostHalbjahr().kuerzel.replace(".", "-"),
					blockungsergebnis.id());
				break;
			default :
				throw OperationError.NOT_FOUND.exception("Die übergebene html-Vorlage konnte nicht in den bekannten Vorlagen gefunden werden.");
		}

		if (!dateinamePdf.isEmpty())
			return dateinamePdf;
		else
			throw OperationError.NOT_FOUND.exception("Die übergebene html-Vorlage konnte nicht in den bekannten Vorlagen gefunden werden.");
	}



	/**
	 * Erstellt eine ZIP-Datei, die alle PDF-Dateien der übergebenen PDF-Builder enthält.
	 * @param pdfBuilders Liste mit PdfBuilder, die die einzelnen PDF-Dateien erzeugen.
	 * @return Gibt das ZIP in Form eines ByteArrays zurück.
	 */
	private byte[] createZIP(final List<PdfBuilder> pdfBuilders) throws WebApplicationException {
		final byte[] zipData;
		try {
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				try (ZipOutputStream zos = new ZipOutputStream(baos)) {
					for (final PdfBuilder pdfBuilder : pdfBuilders) {
						zos.putNextEntry(new ZipEntry(pdfBuilder.getPdfDateiname()));
						zos.write(pdfBuilder.getPdfByteArray());
						zos.closeEntry();
					}
					baos.flush();
				}
				zipData = baos.toByteArray();
			}
		} catch (@SuppressWarnings("unused") final IOException e) {
			throw OperationError.INTERNAL_SERVER_ERROR.exception("Die erzeugten PDF-Dateien konnten nicht als ZIP-Datei zusammengestellt werden.");
		}
		return zipData;
	}



	/**
	 *  Erzeugt eine Fehlerausgabe im Logger und eine SimpleOperationResponse mit den Fehlerinformationen
	 * @param e Die aufgetretene Exception
	 * @return Die Response mit der Fehlerdaten.
	 */
	private Response errorResponse(final Exception e) {
		String htmlTemplate = "";
		String webAppExBody = "";

		logger.logLn("###  FEHLER  ####################################");

		if (e instanceof TemplateProcessingException tPE) {
			// Wenn ein Fehler in der Template-Verarbeitung auftritt, speichere das verwendete html-Template für einen späteren Log-Eintrag.
			htmlTemplate = tPE.getTemplateName();
		}

		// Sammle alle Fehlerursachen im Log.
		for (Throwable cause = e; cause != null; cause = cause.getCause()) {
			String message = cause.getMessage();
			// Entferne das html-Template, falls es in der Message enthalten ist.
			if (e instanceof TemplateProcessingException && !htmlTemplate.isEmpty())
				message = message.replace("(template: \"" + htmlTemplate + "\"", "(");
			logger.logLn(4, message);
		}

		// Hänge das html-Template als weiteren Eintrag hinten an, wenn ein Fehler bei der Template-Verarbeitung aufgetreten ist.
		if (e instanceof TemplateProcessingException && !htmlTemplate.isEmpty()) {
			logger.logLn(0, "");
			logger.logLn(0, "###  Verwendetes html-Template  #################");
			logger.logLn(0, htmlTemplate);
		}

		// Erstelle eine SimpleOperationResponse mit dem Log zum Fehler und gebe diese zurück.
		SimpleOperationResponse simpleOperationResponse = new SimpleOperationResponse();
		simpleOperationResponse.success = false;
		simpleOperationResponse.log = log.getStrings();

		int code = OperationError.INTERNAL_SERVER_ERROR.getCode();
		if (e instanceof WebApplicationException wAE) {
			code = wAE.getResponse().getStatus();
			// Wenn eine WebApplicationException auftritt, gebe den Body der Response als Text aus.
			webAppExBody = wAE.getResponse().readEntity(String.class);
			if (webAppExBody != null)
				logger.logLn(4, "WebApplicationException - Code: %d - Message: %s".formatted(wAE.getResponse().getStatus(), webAppExBody));
			else
				logger.logLn(4, "WebApplicationException - Code: %d".formatted(wAE.getResponse().getStatus()));
		}

		return Response.status(code).type(MediaType.APPLICATION_JSON).entity(simpleOperationResponse).build();
	}
}
