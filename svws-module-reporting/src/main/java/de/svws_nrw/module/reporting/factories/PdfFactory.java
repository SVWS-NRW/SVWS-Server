package de.svws_nrw.module.reporting.factories;

import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.HtmlBuilder;
import de.svws_nrw.module.reporting.html.HtmlTemplateDefinition;
import de.svws_nrw.module.reporting.pdf.PdfBuilder;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
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
 * <p>Diese Klasse erstellt PDF-Dateien auf Basis von html.</p>
 * <p>Dabei werden bei der Initialisierung html-Builder übergebenen, die die html-Inhalte erzeugen. Diese Inhalte werden genutzt, um daraus pdf-Builder zu
 * erzeugen.</p>
 */
public class PdfFactory {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	private final ReportingRepository reportingRepository;

	/** Einstellungen und Daten zum Steuern der Report-Generierung. */
	private final ReportingParameter reportingParameter;

	/** Die Template-Definition für die Erstellung der Html-Datei */
	private final HtmlTemplateDefinition htmlTemplateDefinition;

	/** Map mit den Dateinamen und Html-Dateiinhalten, die in PDF-Dateien gewandelt werden sollen. */
	private final List<HtmlBuilder> htmlBuilders;


	/**
	 * Erzeugt eine neue PdfFactory, um eine Pdf-Datei aus den übergebenen Html-Inhalten zu erzeugen.
	 *
	 * @param htmlBuilders 				Eine Map mit den Dateinamen und Html-Dateiinhalten.
	 * @param reportingRepository		Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	protected PdfFactory(final List<HtmlBuilder> htmlBuilders, final ReportingRepository reportingRepository)
			throws ApiOperationException {

		this.reportingRepository = reportingRepository;
		this.reportingParameter = this.reportingRepository.reportingParameter();

		this.reportingRepository.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Initialisierung der PDF-Factory und der Validierung der übergebenen Daten.");

		// Validiere die html-Builders
		if ((htmlBuilders == null) || htmlBuilders.isEmpty()) {
			this.reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Die Html-Dateiinhalte für die PDF-Erzeugung sind nicht vorhanden.");
			throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Die Html-Dateiinhalte für die PDF-Erzeugung sind nicht vorhanden.");
		}
		this.htmlBuilders = htmlBuilders;

		// Validiere die Angaben zur html-Vorlage.
		this.htmlTemplateDefinition = HtmlTemplateDefinition.getByType(ReportingReportvorlage.getByBezeichnung(this.reportingParameter.reportvorlage));
		if (this.htmlTemplateDefinition == null) {
			this.reportingRepository.logger()
					.logLn(LogLevel.ERROR, 4, "FEHLER: Die Template-Definitionen für die PDF-Factory sind inkonsistent.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "FEHLER: Die Template-Definitionen für die PDF-Factory sind inkonsistent.");
		}

		this.reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Initialisierung der PDF-Factory und der Validierung der übergebenen Daten.");
	}


	/**
	 * Erstellt eine Response in Form einer einzelnen PDF-Datei oder ZIP-Datei mit den mehreren generierten PDF-Dateien.
	 *
	 * @return Im Falle eines Success enthält die HTTP-Response das PDF-Dokument oder die ZIP-Datei.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	protected Response createPdfResponse() throws ApiOperationException {

		try {
			reportingRepository.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der Response einer API-Anfrage für eine PDF-Generierung.");
			final List<PdfBuilder> pdfBuilders = getPdfBuilders();
			if (!pdfBuilders.isEmpty()) {
				if (pdfBuilders.size() == 1) {
					reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der Response einer API-Anfrage für eine PDF-Generierung.");
					return pdfBuilders.getFirst().getPdfResponse();
				}
				final byte[] zipData = createZIP(pdfBuilders);
				final String encodedFilename = "filename*=UTF-8''" + URLEncoder.encode(htmlTemplateDefinition.getDateiname() + ".zip", StandardCharsets.UTF_8);

				reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der Response einer API-Anfrage für eine PDF-Generierung.");
				return Response.ok(zipData, "application/zip").header("Content-Disposition", "attachment; " + encodedFilename).build();
			}
			reportingRepository.logger().logLn(LogLevel.ERROR, 0,
					"### Fehler bei der Erzeugung der Response einer API-Anfrage für eine PDF-Generierung. Es sind keine PDF-Inhalte generiert worden.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Fehler bei der Erzeugung der Response einer API-Anfrage für eine PDF-Generierung. Es sind keine PDF-Inhalte generiert worden.");
		} catch (final Exception e) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 0,
					"### Fehler bei der Erzeugung der Response einer API-Anfrage für eine PDF-Generierung.");
			throw e;
		}
	}


	/**
	 * Erzeugt auf Basis der übergebenen html-Builder die PDF-Builder zur Erzeugung der PDF-Dateien.
	 *
	 * @return Ein oder mehrere PDF-Builder zur Erzeugung der PDF-Dateien.
	 */
	private List<PdfBuilder> getPdfBuilders() {

		reportingRepository.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der PDF-Builder.");
		final List<PdfBuilder> pdfBuilders = new ArrayList<>();

		for (final HtmlBuilder htmlBuilder : this.htmlBuilders) {
			pdfBuilders.add(new PdfBuilder(htmlBuilder.getHtml(), htmlTemplateDefinition.getRootPfad(), htmlBuilder.getDateiname()));
		}

		reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der PDF-Builder.");
		return pdfBuilders;
	}


	/**
	 * Erstellt eine ZIP-Datei, die alle PDF-Dateien der übergebenen PDF-Builder enthält.
	 *
	 * @param pdfBuilders 				Liste mit PdfBuilder, die die einzelnen PDF-Dateien erzeugen.
	 *
	 * @return Gibt das ZIP in Form eines ByteArrays zurück.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private byte[] createZIP(final List<PdfBuilder> pdfBuilders) throws ApiOperationException {
		final byte[] zipData;
		try {
			try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
				try (ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream)) {
					for (final PdfBuilder pdfBuilder : pdfBuilders) {
						zos.putNextEntry(new ZipEntry(pdfBuilder.getDateinameMitEndung()));
						zos.write(pdfBuilder.getPdfByteArray());
						zos.closeEntry();
					}
					byteArrayOutputStream.flush();
				}
				zipData = byteArrayOutputStream.toByteArray();
			}
		} catch (final IOException e) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Die erzeugten PDF-Inhalte konnten nicht als ZIP-Datei zusammengestellt werden.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e,
					"FEHLER: Die erzeugten PDF-Inhalte konnten nicht als ZIP-Datei zusammengestellt werden.");
		}
		return zipData;
	}
}
