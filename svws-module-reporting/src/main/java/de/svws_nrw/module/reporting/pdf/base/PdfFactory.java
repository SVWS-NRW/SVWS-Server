package de.svws_nrw.module.reporting.pdf.base;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.reporting.html.base.HtmlBuilder;
import de.svws_nrw.module.reporting.html.base.HtmlContext;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextSchueler;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextSchule;
import de.svws_nrw.module.reporting.html.contexts.gost.kursplanung.HtmlContextGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.proxytypes.gost.kursplanung.ProxyReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.proxytypes.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.thymeleaf.exceptions.TemplateProcessingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Diese Klasse beinhaltet den Code zur Erstellung einer pdf-Datei auf Basis der hinterlegten html-Vorlage und den übergebenen Daten.
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

	/** Legt fest, ob die IDs-Liste der Kurse als Filter dient. */
	private final boolean filterKurse;

	/** Legt fest, ob die IDs-Liste der Schüler als Filter dient. */
	private final boolean filterSchueler;

	/** Die ID des Blockungsergebnisses, aus dem die Daten für die PDF_Erzeugung genutzt werden sollen. */
	private final Long idBlockungsergebnis;

	/** Liste der IDs der Kurse, für die eine Ausgabe erfolgen soll. */
	private List<Long> idsKurse;

	/** Liste der IDs der Schüler, für die eine Ausgabe erfolgen soll. */
	private List<Long> idsSchueler;

	/** Legt fest, ob für die Schüler auch die Abiturdaten der GOSt mit geladen werden sollen. */
	private final boolean ladeSchuelerMitAbiturDaten;

	/** Legt fest, ob für die Schüler auch die Laufbahnplanungsdaten der GOSt mit geladen werden sollen. */
	private final boolean ladeSchuelerMitGostDaten;


	/** Parameter (>= 0), der in Templates verwendet werden kann, um den Detailgrad der Darstellung zu steuern. */
	private final int parameterDetailLevel;



	/** Der Dateiname des Templates, der aus dem übergebenen Dateipfad ermittelt wird. */
	private String dateinameHtmlTemplate = "";

	/** Liste, die Einträge aus dem Logger sammelt. */
	private final LogConsumerList log = new LogConsumerList();

	/** Logger, der den Ablauf protokolliert und Fehlerdaten sammelt */
	private final Logger logger = new Logger();



	/**
	 * Erzeugt eine neue PdfFactory, um eine Pdf-Datei aus einem html-Template zu erzeugen.
	 * @param conn Die Verbindung zur Datenbank.
	 * @param dateipfadHtmlTemplate Pfad und Dateiname mit der Thymeleaf-html-Dokumentvorlage, aus der später die PDF-Datei erzeugt wird.
	 * @param dateipfadCss Pfad zur css-Datei, die in der html-Dokumentvorlage verlinkt wurde. Er wird vom PDF-Builder benötigt, um als baseURI für nachladbare Dateien zu fungieren.
	 * @param filterKurse Legt fest, ob auf die Kurse aus der IDs-Filterliste gefiltert werden soll.
	 * @param filterSchueler Legt fest, ob auf die Schüler aus der IDs-Filterliste gefiltert werden soll.
	 * @param idBlockungsergebnis Die ID des Blockungsergebnisses, aus dem die Daten für die PDF_Erzeugung genutzt werden sollen.
	 * @param idsKurse Liste der IDs der Kurse, für die eine Ausgabe erfolgen soll.
	 * @param idsSchueler Liste der IDs der Schüler, für die eine Ausgabe erfolgen soll.
	 * @param ladeSchuelerMitAbiturDaten Legt fest, ob für die Schüler auch die Abiturdaten der GOSt mit geladen werden sollen.
	 * @param ladeSchuelerMitGostDaten Legt fest, ob für die Schüler auch die Laufbahnplanungsdaten der GOSt mit geladen werden sollen.
	 * @param parameterDetailLevel Parameter, der in Templates verwendet werden kann, um den Detailgrad der Darstellung zu steuern.
	 */
	public PdfFactory(final DBEntityManager conn, final String dateipfadHtmlTemplate, final String dateipfadCss, final boolean filterKurse, final boolean filterSchueler, final Long idBlockungsergebnis, final List<Long> idsKurse, final List<Long> idsSchueler, final boolean ladeSchuelerMitAbiturDaten, final boolean ladeSchuelerMitGostDaten, final int parameterDetailLevel) {
		this.conn = conn;
		this.dateipfadHtmlTemplate = dateipfadHtmlTemplate;
		this.dateipfadCss = dateipfadCss;
		this.filterKurse = filterKurse;
		this.filterSchueler = filterSchueler;
		this.idBlockungsergebnis = idBlockungsergebnis;
		this.idsKurse = idsKurse;
		this.idsSchueler = idsSchueler;
		this.ladeSchuelerMitAbiturDaten = ladeSchuelerMitAbiturDaten;
		this.ladeSchuelerMitGostDaten = ladeSchuelerMitGostDaten;
		this.parameterDetailLevel = parameterDetailLevel;

		logger.addConsumer(log);

		// Evtl. Einträge von null für Listen abfangen.
		if (idsKurse == null)
			this.idsKurse = new ArrayList<>();
		if (idsSchueler == null)
			this.idsSchueler = new ArrayList<>();
	}

	/**
	 * Erstellt das PDF-Dokument und gibt es als Response zum Download zurück.
	 * @return Im Falle eines Success enthält die HTTP-Response das PDF-Dokument. Im Fehlerfall wird eine WebApplicationException ausgelöst oder bei Fehlercode 500 eine SimpleOperationResponse mit Logdaten zurückgegeben.
	 */
	public Response createPdf() {

		try {
			final PdfBuilder pdfBuilder = getPdfBuilder();
			return pdfBuilder.getPdfResponse();
		} catch (Exception e) {
			String htmlTemplate = "";

			// Wenn ein Fehler in der Template-Verarbeitung auftritt, speichere das verwendete html-Template für einen späteren Log-Eintrag.
			if (e instanceof TemplateProcessingException tPE)
				htmlTemplate = tPE.getTemplateName();

			logger.logLn("FEHLER  #################################################");

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
				logger.logLn(0, "Verwendetes html-Template  ##############################");
				logger.logLn(0, htmlTemplate);
			}

			// Erstelle eine SimpleOperationResponse mit dem Log zum Fehler und gebe diese zurück.
			SimpleOperationResponse simpleOperationResponse = new SimpleOperationResponse();
			simpleOperationResponse.success = false;
			simpleOperationResponse.log = log.getStrings();

			int code = OperationError.INTERNAL_SERVER_ERROR.getCode();
			if (e instanceof WebApplicationException wae)
				code = wae.getResponse().getStatus();

			return Response.status(code).type(MediaType.APPLICATION_JSON).entity(simpleOperationResponse).build();
		}
	}


	/**
	 * Erzeugt auf Basis der hinterlegten html-Vorlage und der übergebenen Daten den PdfBuilder zur Erzeugung der PDF-Datei.
	 * @return						Ein PDF-Builder zur Erzeugung der PDF-Datei
	 */
	private PdfBuilder getPdfBuilder() {

		// Ermittele den Namen der übergebenen html-Vorlagendatei, um damit die Dateinamensgenerierung zu steuern.
		dateinameHtmlTemplate = dateipfadHtmlTemplate.substring(dateipfadHtmlTemplate.lastIndexOf('/') + 1);
		if (supportedSchuelerTemplates.stream().noneMatch(t -> t.equals(dateinameHtmlTemplate)) && supportedGostKursplanungTemplates.stream().noneMatch(t -> t.equals(dateinameHtmlTemplate)))
			throw OperationError.NOT_FOUND.exception("Keine für die Erstellung der PDF-Datei unterstützte Vorlage gefunden.");

		// html-Daten-Contexts erstellen und in Liste sammeln.
		final Map<String, HtmlContext> mapHtmlContexts = new HashMap<>();

		logger.logLn("Erzeuge Repository");
		final ReportingRepository reportingRepository = new ReportingRepository(conn);

		logger.logLn("Erzeuge Datenkontext Schule");
		final HtmlContextSchule htmlContextSchule = new HtmlContextSchule(reportingRepository);
		mapHtmlContexts.put("Schule", htmlContextSchule);

		logger.logLn("Erzeuge Datenkontext Schüler - %d IDs von Schülern wurden übergeben.".formatted(idsSchueler.size()));
		if (!idsSchueler.isEmpty()) {
			final HtmlContextSchueler htmlContextSchueler = new HtmlContextSchueler(reportingRepository, idsSchueler, ladeSchuelerMitGostDaten, ladeSchuelerMitAbiturDaten);
			mapHtmlContexts.put("Schueler", htmlContextSchueler);
		} else {
			if (supportedSchuelerTemplates.stream().anyMatch(t -> t.equals(dateinameHtmlTemplate))) {
				throw OperationError.NOT_FOUND.exception("Keine für die Erstellung der PDF-Datei notwendigen Schüler angegeben.");
			}
		}

		if (idBlockungsergebnis != null) {
			logger.logLn("Erzeuge Datenkontext Blockungsergebnis für Erg.-ID " +  idBlockungsergebnis);
			final HtmlContextGostKursplanungBlockungsergebnis htmlContextBlockung = new HtmlContextGostKursplanungBlockungsergebnis(conn, idBlockungsergebnis, filterSchueler, idsSchueler, filterKurse, idsKurse);
			mapHtmlContexts.put("Blockungsergebnis", htmlContextBlockung);
		} else {
			if (supportedGostKursplanungTemplates.stream().anyMatch(t -> t.equals(dateinameHtmlTemplate))) {
				throw OperationError.NOT_FOUND.exception("Kein für die Erstellung der PDF-Datei notwendiges Blockungsergebnis angegeben.");
			}
		}

		// Einzelne Variablen für den finalen html-Daten-Context sammeln
		logger.logLn("Füge Parameter für den Druck hinzu.");
		final Map<String, Object> variables = new HashMap<>();
		variables.put("parameterDetailLevel", parameterDetailLevel);

		// Dateiname der PDF-Datei aus den Daten erzeugen.
		logger.logLn("Erzeuge PDF-Dateiname.");
		String pdfDateiname = getPdfDateiname(mapHtmlContexts);

		// html-Builder erstellen und damit das html mit Daten für die PDF-Datei erzeugen
		logger.logLn("Verarbeite Template (%s) und Daten aus den Kontexten zum finalen html für die PDF-Datei.".formatted(dateipfadHtmlTemplate));
		final HtmlBuilder htmlBuilder = new HtmlBuilder(dateipfadHtmlTemplate, mapHtmlContexts.values().stream().toList(), variables);
		final String html = htmlBuilder.getHtml();

		logger.logLn("Erzeuge PDF-Builder mit finalem html.");
		return new PdfBuilder(html, dateipfadCss, pdfDateiname);
	}


	/** Erstellt den Dateinamen der Pdf-Datei auf Basis des html-Templates und evtl. der übergebenen Daten
	 * @param mapHtmlContexts	Map mit den bereits erzeugten html-Datenkontexten, um daraus Daten für den Dateinamen entnehmen zu können.
	 * @return Der Dateiname für die Pdf-Datei.
	 */
	private String getPdfDateiname(final Map<String, HtmlContext> mapHtmlContexts) {

		String dateinamePdf;

		// Für Dateinamen mit schülerbezogenen Daten ermittle die Anzahl der Schüler und den ersten Schüler des Kontextes Schueler.
		ProxyReportingSchueler ersterSchueler = null;
		int anzahlContextSchueler = 0;
		if (mapHtmlContexts.containsKey("Schueler") && !(((HtmlContextSchueler) mapHtmlContexts.get("Schueler")).getSchueler().isEmpty())) {
			ersterSchueler = ((HtmlContextSchueler) mapHtmlContexts.get("Schueler")).getSchueler().getFirst();
			anzahlContextSchueler = ((HtmlContextSchueler) mapHtmlContexts.get("Schueler")).getSchueler().size();
		}

		// Bei schülerbasierten Vorlagen muss es mindestens einen Schüler geben, wenn nicht, gib einen Fehler aus.
		if (ersterSchueler == null && supportedSchuelerTemplates.stream().anyMatch(t -> t.equals(dateinameHtmlTemplate))) {
			throw OperationError.NOT_FOUND.exception("Keine für die Erstellung der PDF-Datei notwendigen Schüler gefunden.");
		}

		// Für Dateinamen mit blockungsbasierten Daten ermittle das genutzte Ergebnis.
		ProxyReportingGostKursplanungBlockungsergebnis blockungsergebnis = null;
		if (mapHtmlContexts.containsKey("Blockungsergebnis")) {
			blockungsergebnis = (ProxyReportingGostKursplanungBlockungsergebnis) mapHtmlContexts.get("Blockungsergebnis").getContext().getVariable("Blockungsergebnis");
		}

		// Bei blockungsbasierten Vorlagen muss es ein Blockungsergebnis geben, wenn nicht, gib einen Fehler aus.
		if (blockungsergebnis == null && supportedGostKursplanungTemplates.stream().anyMatch(t -> t.equals(dateinameHtmlTemplate))) {
			throw OperationError.NOT_FOUND.exception("Keine für die Erstellung der PDF-Datei notwendiges Blockungsergebnis gefunden.");
		}

		// Erzeuge den Dateinamen der pdf mit Daten.
		switch (dateinameHtmlTemplate) {
			case "APOGOStAnlage12.html" :
				assert ersterSchueler != null; // Schülerbasierte Vorlage, daher vorher schon ersterSchueler != null geprüft.
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
				dateinamePdf = "Schueler-Schienen-Kurse-Zuordnung_%d-%s_(Erg-ID%d).pdf".formatted(
					blockungsergebnis.abiturjahr(),
					blockungsergebnis.gostHalbjahr().replace(".", "-"),
					blockungsergebnis.id());
				break;
			case "GostKursplanungSchuelerMitKursen.html" :
				assert blockungsergebnis != null; // Blockungsbasierte Vorlage, daher vorher schon blockungsergebnis != null geprüft.
				dateinamePdf = "Schueler-Kurse-Liste_%d-%s_(Erg-ID%d).pdf".formatted(
					blockungsergebnis.abiturjahr(),
					blockungsergebnis.gostHalbjahr().replace(".", "-"),
					blockungsergebnis.id());
				break;
			case "GostKursplanungKursMitKursschuelern.html" :
				assert blockungsergebnis != null; // Blockungsbasierte Vorlage, daher vorher schon blockungsergebnis != null geprüft.
				dateinamePdf = "Kursliste_%d-%s_(Erg-ID%d).pdf".formatted(
					blockungsergebnis.abiturjahr(),
					blockungsergebnis.gostHalbjahr().replace(".", "-"),
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


}
