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
 * Diese Klasse beinhaltet den Code zur Erstellung einer pdf-Datei auf Basis der übergebenen Html-Inhalte.
 */
public class PdfFactory {

	/** Die Daten für die Report-Ausgabe. */
	private final ReportingParameter reportingParameter;

	/** Die Template-Definition für die Erstellung der Html-Datei */
	private final HtmlTemplateDefinition htmlTemplateDefinition;

	/** Repository für die Reporting */
	private final ReportingRepository reportingRepository;

	/** Map mit den Dateinamen und Html-Dateiinhalten, die in PDF-Dateien gewandelt werden sollen. */
	private List<HtmlBuilder> htmlBuilders = new ArrayList<>();


	/**
	 * Erzeugt eine neue PdfFactory, um eine Pdf-Datei aus den übergebenen Html-Inhalten zu erzeugen.
	 *
	 * @param htmlBuilders 			Eine Map mit den Dateinamen und Html-Dateiinhalten.
	 * @param reportingRepository	Repository für das Reporting, welches verschiedene Daten aus der Datenbank zwischenspeichert.
	 * @param reportingParameter 	Das Objekt, welches die Angaben zu den Daten des Reports und den zugehörigen Einstellungen enthält.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	protected PdfFactory(final List<HtmlBuilder> htmlBuilders, final ReportingRepository reportingRepository, final ReportingParameter reportingParameter)
			throws ApiOperationException {

		this.reportingParameter = reportingParameter;
		this.reportingRepository = reportingRepository;

		this.reportingRepository.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Initialisierung der PDF-Factory und der Validierung der übergebenen Daten.");

		// Validiere die htmlBuilders
		if ((htmlBuilders == null) || htmlBuilders.isEmpty()) {
			this.reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Die Html-Dateiinhalte für die PDF-Erzeugung sind nicht vorhanden.");
			throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Die Html-Dateiinhalte für die PDF-Erzeugung sind nicht vorhanden.");
		}
		this.htmlBuilders = htmlBuilders;

		// Validiere die Angaben zur html-Vorlage.
		this.htmlTemplateDefinition = HtmlTemplateDefinition.getByType(ReportingReportvorlage.getByBezeichnung(reportingParameter.reportvorlage));
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
	 * @return Im Falle eines Success enthält die HTTP-Response das PDF-Dokument oder die ZIP-Datei. Im Fehlerfall wird eine ApiOperationException ausgelöst oder bei Fehlercode 500 eine SimpleOperationResponse mit Logdaten zurückgegeben.
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
	 * Erzeugt auf Basis der hinterlegten html-Vorlage und der übergebenen Daten die PdfBuilder zur Erzeugung der PDF-Dateien.
	 * @return Ein oder mehrere PDF-Builder zur Erzeugung der PDF-Dateien.
	 */
	private List<PdfBuilder> getPdfBuilders() {

		reportingRepository.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der PDF-Builder.");
		final List<PdfBuilder> pdfBuilders = new ArrayList<>();

		for (final HtmlBuilder htmlBuilder : htmlBuilders) {
			pdfBuilders.add(new PdfBuilder(htmlBuilder.getHtml(), htmlTemplateDefinition.getPfadCss(), htmlBuilder.getDateiname()));
		}

		reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der PDF-Builder.");
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
