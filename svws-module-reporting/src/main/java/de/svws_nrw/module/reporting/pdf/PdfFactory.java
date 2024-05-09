package de.svws_nrw.module.reporting.pdf;

import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.ReportingErrorResponse;
import de.svws_nrw.module.reporting.html.HtmlBuilder;
import de.svws_nrw.module.reporting.html.HtmlTemplateDefinition;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

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
	private final ReportingParameter reportingParameter;

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
	 *
	 * @param htmlBuilders Eine Map mit den Dateinamen und Html-Dateiinhalten.
	 * @param reportingParameter Das Objekt, welches die Angaben zu den Daten des Reports und den zugehörigen Einstellungen enthält.
	 * @param logger Logger, der die Erstellung der Reports protokolliert.
	 * @param log Log, das die Erstellung des Reports protokolliert.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public PdfFactory(final List<HtmlBuilder> htmlBuilders, final ReportingParameter reportingParameter, final Logger logger, final LogConsumerList log) throws ApiOperationException {

		this.logger = logger;
		this.log = log;

		if (logger == null || log == null) {
			this.logger = new Logger();
			this.log = new LogConsumerList();
			this.logger.addConsumer(this.log);
		}

		// Validiere die htmlBuilders
		if (htmlBuilders == null || htmlBuilders.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "Die Html-Dateiinhalte sind nicht vorhanden.");

		this.htmlBuilders = htmlBuilders;

		// Validiere Reporting-Parameter
		if (reportingParameter == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Daten zur Ausgabe im Report übergeben.");

		this.reportingParameter = reportingParameter;

		// Validiere die Angaben zur Vorlage für den Report
		if (ReportingReportvorlage.getByBezeichnung(reportingParameter.reportvorlage) == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine gültige Report-Vorlage übergeben.");

		this.htmlTemplateDefinition = HtmlTemplateDefinition.getByType(ReportingReportvorlage.getByBezeichnung(reportingParameter.reportvorlage));

		if (this.htmlTemplateDefinition == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Template-Definitionen inkonsistent.");
	}


	/**
	 * Erstellt eine Response in Form einer einzelnen PDF-Datei oder ZIP-Datei mit den mehreren generierten PDF-Dateien.
	 *
	 * @return Im Falle eines Success enthält die HTTP-Response das PDF-Dokument oder die ZIP-Datei. Im Fehlerfall wird eine ApiOperationException ausgelöst oder bei Fehlercode 500 eine SimpleOperationResponse mit Logdaten zurückgegeben.
	 */
	public Response createPdfResponse() {

		try {
			final List<PdfBuilder> pdfBuilders = getPdfBuilders();
			if (!pdfBuilders.isEmpty()) {
				if (!reportingParameter.einzelausgabeHauptdaten || pdfBuilders.size() == 1) {
					return pdfBuilders.getFirst().getPdfResponse();
				}
				final byte[] zipData = createZIP(pdfBuilders);
				final String encodedFilename = "filename*=UTF-8''" + URLEncoder.encode(htmlTemplateDefinition.getDateiname() + ".zip", StandardCharsets.UTF_8);
				return Response.ok(zipData, "application/zip").header("Content-Disposition", "attachment; " + encodedFilename).build();
			}
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Es sind keine PDF-Builder generiert worden.");
		} catch (final Exception e) {
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
	 *
	 * @param pdfBuilders Liste mit PdfBuilder, die die einzelnen PDF-Dateien erzeugen.
	 *
	 * @return Gibt das ZIP in Form eines ByteArrays zurück.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private static byte[] createZIP(final List<PdfBuilder> pdfBuilders) throws ApiOperationException {
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
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die erzeugten PDF-Dateien konnten nicht als ZIP-Datei zusammengestellt werden.");
		}
		return zipData;
	}
}
