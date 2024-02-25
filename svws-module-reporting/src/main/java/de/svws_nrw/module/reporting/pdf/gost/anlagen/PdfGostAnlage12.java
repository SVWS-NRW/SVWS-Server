package de.svws_nrw.module.reporting.pdf.gost.anlagen;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.reporting.html.base.HtmlBuilder;
import de.svws_nrw.module.reporting.html.base.HtmlContext;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextSchueler;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextSchule;
import de.svws_nrw.module.reporting.pdf.base.PdfBuilder;
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
public final class PdfGostAnlage12 {

	/** Pfad und Dateiname mit der Thymeleaf-html-Dokumentvorlage, aus der später die PDF-Datei erzeugt wird. */
	private static final String htmlVorlageDateipfad = "de/svws_nrw/module/reporting/gost/apogostanlagen/APOGOStAnlage12.html";

	/** Pfad zur css-Datei, die in der html-Dokumentvorlage verlinkt wurde. Er wird vom PDF-Builder benötigt, um als baseURI für nachladbare Dateien zu fungieren. */
	private static final String cssDateipfad = "de/svws_nrw/module/reporting/gost/apogostanlagen/APOGOStAnlage12.css";

	private PdfGostAnlage12() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/**
	 * Erstellt das PDF-Dokument und gibt es als Response zum Download zurück.
	 *
	 * @param conn          		Datenbank-Verbindung
	 * @param schuelerIDs           Liste der IDs der Schüler, deren Wahlbögen erstellt werden sollen.
	 *
	 * @return 						HTTP-Response mit dem PDF-Dokument oder bei Fehler eine WebApplicationException-Response
	 */
	public static Response query(final DBEntityManager conn, final List<Long> schuelerIDs) {

		final LogConsumerList log = new LogConsumerList();
		final Logger logger = new Logger();
		logger.addConsumer(log);

		try {
			final PdfBuilder pdfBuilder = getPdfBuilder(conn, schuelerIDs, logger);
			return pdfBuilder.getPdfResponse();
		} catch (Exception e) {
			String htmlTemplate = "";

			if (e instanceof TemplateProcessingException tPE)
				htmlTemplate = tPE.getTemplateName();

			logger.logLn("FEHLER  #################################################");

			for (Throwable cause = e; cause != null; cause = cause.getCause()) {
				String message = cause.getMessage();
				message = message.replace("(template: \"" + htmlTemplate + "\"", "(");
				logger.logLn(4, message);
			}

			if (e instanceof TemplateProcessingException && !htmlTemplate.isEmpty()) {
				logger.logLn(0, "");
				logger.logLn(0, "Verwendetes html-Template  ##############################");
				logger.logLn(0, htmlTemplate);
			}

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
	 *
	 * @param conn          		Datenbank-Verbindung
	 * @param schuelerIDs           Liste der IDs der Schüler, deren Wahlbögen erstellt werden sollen.
	 * @param logger                der Logger
	 *
	 * @return						Ein PDF-Builder zur Erzeugung der PDF-Datei
	 */
	public static PdfBuilder getPdfBuilder(final DBEntityManager conn, final List<Long> schuelerIDs, final Logger logger) {
		// html-Daten-Contexts erstellen und in Liste sammeln
		final ReportingRepository reportingRepository = new ReportingRepository(conn);
		final HtmlContextSchueler htmlContextSchueler = new HtmlContextSchueler(reportingRepository, schuelerIDs, false, true);
		final HtmlContextSchule htmlContextSchule = new HtmlContextSchule(reportingRepository);

		final List<HtmlContext> htmlContexts = new ArrayList<>();
		htmlContexts.add(htmlContextSchueler);
		htmlContexts.add(htmlContextSchule);

		// Einzelne Variablen für den finalen html-Daten-Context sammeln
		final Map<String, Object> variables = new HashMap<>();

		// Dateiname der PDF-Datei aus den Daten erzeugen.
		String pdfDateiname = "Anlage12.pdf";
		if (htmlContextSchueler.getSchueler().size() == 1) {
			final ProxyReportingSchueler ersterSchueler = htmlContextSchueler.getSchueler().getFirst();
			if (htmlContextSchueler.getSchueler().size() == 1) {
				pdfDateiname = "Anlage12_%s_%s_(%d).pdf".formatted(
						ersterSchueler.nachname().replace(' ', '_').replace('.', '_'),
						ersterSchueler.vorname().replace(' ', '_').replace('.', '_'),
						ersterSchueler.id());
			}
		}

		// html-Builder erstellen und damit das html mit Daten für die PDF-Datei erzeugen
		final HtmlBuilder htmlBuilder = new HtmlBuilder(htmlVorlageDateipfad, htmlContexts, variables);
		final String html = htmlBuilder.getHtml();

		return new PdfBuilder(html, cssDateipfad, pdfDateiname);
	}
}
