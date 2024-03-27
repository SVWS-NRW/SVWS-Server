package de.svws_nrw.module.reporting.pdf;

import de.svws_nrw.core.data.reporting.ReportingAusgabedaten;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.reporting.ReportingErrorResponse;
import de.svws_nrw.module.reporting.html.HtmlBuilder;
import de.svws_nrw.module.reporting.html.HtmlTemplateDefinition;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * Diese Klasse beinhaltet den Code zur Erstellung einer pdf-Datei auf Basis der übergebenen Html-Inhalte.
 */
public final class PdfFactory {

	/** Die Daten für die Report-Ausgabe. */
	private final ReportingAusgabedaten reportingAusgabedaten;

	/** Die Template-Definition für die Erstellung der Html-Datei */
	private final HtmlTemplateDefinition htmlTemplateDefinition;

	/** Map mit den Dateinamen und Html-Dateiinhalten, die in PDF-Dateien gewandelt werden sollen. */
	private List<HtmlBuilder> htmlBuilders = new ArrayList<>();

	/** Liste, die Einträge aus dem Logger sammelt. */
	private LogConsumerList log;

	/** Logger, der den Ablauf protokolliert und Fehlerdaten sammelt */
	private Logger logger;


	/**
	 * Erzeugt eine neue PdfFactory, um eine Pdf-Datei aus den übergebenen Html-Inhalten zu erzeugen.
	 * @param htmlBuilders Eine Map mit den Dateinamen und Html-Dateiinhalten.
	 * @param reportingAusgabedaten Das Objekt, welches die Angaben zu den Daten des Reports und den zugehörigen Einstellungen enthält.
	 * @param logger Logger, der die Erstellung der Reports protokolliert.
	 * @param log Log, das die Erstellung des Reports protokolliert.
	 */
	public PdfFactory(final List<HtmlBuilder> htmlBuilders, final ReportingAusgabedaten reportingAusgabedaten, final Logger logger, final LogConsumerList log) {

		this.logger = logger;
		this.log = log;

		if (logger == null || log == null) {
			this.logger = new Logger();
			this.log = new LogConsumerList();
			this.logger.addConsumer(this.log);
		}

		// Validiere die htmlBuilders
		if (htmlBuilders == null || htmlBuilders.isEmpty())
			throw OperationError.NOT_FOUND.exception("Die Html-Dateiinhalte sind nicht vorhanden.");

		this.htmlBuilders = htmlBuilders;

		// Validiere Reporting-Ausgabedaten
		if (reportingAusgabedaten == null)
			throw OperationError.NOT_FOUND.exception("Es wurde keine Daten zur Ausgabe im Report übergeben.");

		this.reportingAusgabedaten = reportingAusgabedaten;

		// Validiere die Angaben zur Vorlage für den Report
		if (ReportingReportvorlage.getByBezeichnung(reportingAusgabedaten.reportvorlage) == null)
			throw OperationError.NOT_FOUND.exception("Es wurde keine gültige Report-Vorlage übergeben.");

		this.htmlTemplateDefinition = HtmlTemplateDefinition.getByType(ReportingReportvorlage.getByBezeichnung(reportingAusgabedaten.reportvorlage));

		if (this.htmlTemplateDefinition == null)
			throw OperationError.INTERNAL_SERVER_ERROR.exception("Template-Definitionen inkonsistent.");
	}


	/**
	 * Erstellt eine Response in Form einer einzelnen PDF-Datei oder ZIP-Datei mit den mehreren generierten PDF-Dateien.
	 * @return Im Falle eines Success enthält die HTTP-Response das PDF-Dokument oder die ZIP-Datei. Im Fehlerfall wird eine WebApplicationException ausgelöst oder bei Fehlercode 500 eine SimpleOperationResponse mit Logdaten zurückgegeben.
	 */
	public Response createPdfResponse() {

		try {
			final List<PdfBuilder> pdfBuilders = getPdfBuilders();
			if (!pdfBuilders.isEmpty()) {
				if (!reportingAusgabedaten.einzelausgabeHauptdaten || pdfBuilders.size() == 1) {
					return pdfBuilders.getFirst().getPdfResponse();
				} else {
					final byte[] zipData = createZIP(pdfBuilders);
					final String encodedFilename = "filename*=UTF-8''" + URLEncoder.encode(htmlTemplateDefinition.getDateiname() + ".zip", StandardCharsets.UTF_8);
					return Response.ok(zipData, "application/zip")
						.header("Content-Disposition", "attachment; " + encodedFilename)
						.build();
				}
			} else {
				throw OperationError.INTERNAL_SERVER_ERROR.exception("Es sind keine PDF-Builder generiert worden.");
			}
		} catch (Exception e) {
			return new ReportingErrorResponse(e, logger, log).getResponse();
		}
	}


	/**
	 * Erzeugt auf Basis der hinterlegten html-Vorlage und der übergebenen Daten die PdfBuilder zur Erzeugung der PDF-Dateien.
	 * @return Ein oder mehrere PDF-Builder zur Erzeugung der PDF-Dateien.
	 */
	private List<PdfBuilder> getPdfBuilders() {

		final List<PdfBuilder> pdfBuilders = new ArrayList<>();

		for (final HtmlBuilder htmlBuilder : htmlBuilders) {
			logger.logLn("Erzeuge PDF-Builder mit finalem html für die PDF-Dateien");
			pdfBuilders.add(new PdfBuilder(htmlBuilder.getHtml(), htmlTemplateDefinition.getPfadCss(), htmlBuilder.getDateiname()));
		}

		return pdfBuilders;
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
						zos.putNextEntry(new ZipEntry(pdfBuilder.getDateinameMitEndung()));
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
}
