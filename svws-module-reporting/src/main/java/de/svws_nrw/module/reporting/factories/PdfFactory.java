package de.svws_nrw.module.reporting.factories;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.builders.ReportBuilderContextPdf;
import de.svws_nrw.module.reporting.builders.ReportBuilderHtml;
import de.svws_nrw.module.reporting.builders.ReportBuilderPdf;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * <p>Diese Klasse erstellt PDF-Dateien auf Basis von HTML.</p>
 * <p>Dabei werden bei der Initialisierung HTML-Builder übergeben, die die HTML-Inhalte erzeugen. Diese Inhalte werden genutzt, um daraus pdf-Builder zu
 * erzeugen.</p>
 */
public class PdfFactory {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	private final ReportingRepository reportingRepository;

	/** Einstellungen und Daten zum Steuern der Report-Generierung. */
	private final ReportingParameterTypisiert reportingParameter;

	/** Map mit den Dateinamen und HTML-Dateiinhalten, die in PDF-Dateien gewandelt werden sollen. */
	private final List<ReportBuilderHtml> htmlBuilders;


	/**
	 * Erzeugt eine neue PdfFactory, um eine Pdf-Datei aus den übergebenen HTML-Inhalten zu erzeugen.
	 *
	 * @param htmlBuilders 				Eine Map mit den Dateinamen und HTML-Dateiinhalten.
	 * @param reportingRepository		Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	protected PdfFactory(final List<ReportBuilderHtml> htmlBuilders, final ReportingRepository reportingRepository)
			throws ApiOperationException {

		this.reportingRepository = reportingRepository;
		this.reportingParameter = this.reportingRepository.reportingParameter();

		this.reportingRepository.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Initialisierung der PDF-Factory und der Validierung der übergebenen Daten.");

		// Validiere die HTML-Builders
		if ((htmlBuilders == null) || htmlBuilders.isEmpty()) {
			this.reportingRepository.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Die Html-Dateiinhalte für die PDF-Erzeugung sind nicht vorhanden.");
			throw new ApiOperationException(Status.NOT_FOUND, "FEHLER: Die Html-Dateiinhalte für die PDF-Erzeugung sind nicht vorhanden.");
		}
		this.htmlBuilders = htmlBuilders;

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
			final List<ReportBuilderPdf> pdfBuilders = getPdfBuilders();
			if (!pdfBuilders.isEmpty()) {
				final ReportBuilderPdf firstPdfBuilder = pdfBuilders.getFirst();
				if (pdfBuilders.size() == 1) {
					final byte[] data = firstPdfBuilder.generate();
					final String encodedFilename = "filename*=UTF-8''" + URLEncoder.encode(firstPdfBuilder.getDateinameMitEndung(), StandardCharsets.UTF_8);
					reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der Response einer API-Anfrage für eine PDF-Generierung.");
					return Response.ok(data, firstPdfBuilder.getContentType()).header("Content-Disposition", "attachment; " + encodedFilename).build();
				} else {
					final byte[] data = createZIP(pdfBuilders);
					final String encodedFilename =
							"filename*=UTF-8''" + URLEncoder.encode(firstPdfBuilder.getStatischerDateiname() + ".zip", StandardCharsets.UTF_8);
					reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der Response einer API-Anfrage für eine PDF-Generierung.");
					return Response.ok(data, "application/zip").header("Content-Disposition", "attachment; " + encodedFilename).build();
				}
			}
			reportingRepository.logger().logLn(LogLevel.ERROR, 0,
					"### Fehler bei der Erzeugung der Response einer API-Anfrage für eine PDF-Generierung. Es sind keine PDF-Inhalte generiert worden.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"### Fehler bei der Erzeugung der Response einer API-Anfrage für eine PDF-Generierung. Es sind keine PDF-Inhalte generiert worden.");
		} catch (final Exception e) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 0, "### Fehler bei der Erzeugung der Response einer API-Anfrage für eine PDF-Generierung.");
			throw e;
		}
	}


	/**
	 * Erzeugt auf Basis der übergebenen HTML-Builder die PDF-Builder zur Erzeugung der PDF-Dateien.
	 *
	 * @return Ein oder mehrere ReportBuilderPdf-Instanzen zur Erzeugung der PDF-Dateien.
	 *
	 * @throws ApiOperationException Wird geworden, wenn ein Fehler bei der Generierung der PDF-Dateien auftritt.
	 */
	public List<ReportBuilderPdf> getPdfBuilders() throws ApiOperationException {

		reportingRepository.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginn der Erzeugung der PDF-Builder.");
		final List<ReportBuilderPdf> pdfBuilders = new ArrayList<>();

		for (final ReportBuilderHtml htmlBuilder : this.htmlBuilders) {
			pdfBuilders.add(getReportBuilderPdf(htmlBuilder));
		}

		reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Ende der Erzeugung der PDF-Builder.");
		return pdfBuilders;
	}

	/**
	 * Erzeugt eine Zuordnung von IDs zu einer Liste von {@code ReportBuilderPdf}-Objekten, basierend
	 * auf den verfügbaren {@code ReportBuilderHtml}-Objekten. Jedem {@code ReportBuilderHtml} können eine oder
	 * mehrere IDs zugeordnet sein. Falls keine IDs vorhanden sind, wird der zugehörige
	 * {@code ReportBuilderPdf} optional der neutralen Gruppe (-1) hinzugefügt.
	 *
	 * @return Eine Map, die IDs (Schlüssel vom Typ {@code Long}) den jeweiligen Listen
	 *         von {@code ReportBuilderPdf}-Objekten (Werte vom Typ {@code List<ReportBuilderPdf>})
	 *         zuordnet. Die Schlüssel repräsentieren die IDs der zugehörigen {@code ReportBuilderHtml}.
	 *
	 * @throws ApiOperationException Wird geworfen, wenn ein Fehler bei der Generierung der PDF-Dateien auftritt.
	 */
	public Map<Long, List<ReportBuilderPdf>> getPdfBuildersById() throws ApiOperationException {
		final Map<Long, List<ReportBuilderPdf>> result = new LinkedHashMap<>();

		for (final ReportBuilderHtml htmlBuilder : this.htmlBuilders) {
			final ReportBuilderPdf pdf = getReportBuilderPdf(htmlBuilder);

			final Set<Long> ids = htmlBuilder.getIds();

			// Falls kein Bezug hinterlegt ist, werden die Inhalte optional in neutraler Gruppe (-1) gesammelt.
			if (ids.isEmpty()) {
				result.computeIfAbsent(-1L, k -> new ArrayList<>()).add(pdf);
				continue;
			}

			for (final Long id : ids) {
				if (id == null)
					continue;
				result.computeIfAbsent(id, k -> new ArrayList<>()).add(pdf);
			}
		}
		return result;

	}


	/**
	 * Konvertiert einen HTML-ReportBuilder in einen PDF-ReportBuilder.
	 *
	 * @param reportBuilderHtml Der HTML-ReportBuilder, der die Eingabedaten und Konfigurationen für die PDF-Erstellung enthält.
	 *
	 * @return Eine Instanz von ReportBuilderPdf, die zur Generierung eines PDF-Dokuments verwendet werden kann.
	 *
	 * @throws ApiOperationException Wird geworfen, wenn ein Fehler während der Erstellung des ReportBuilderPdf auftritt.
	 */
	private ReportBuilderPdf getReportBuilderPdf(final ReportBuilderHtml reportBuilderHtml) throws ApiOperationException {
		final ReportBuilderContextPdf reportBuilderContext =
				new ReportBuilderContextPdf()
						.withHtmlInput(reportBuilderHtml.generate())
						.withDateiname(reportBuilderHtml.getDateiname())
						.withStatischerDateiname(reportBuilderHtml.getStatischerDateiname())
						.withRootPfad(reportBuilderHtml.getRootPfad())
						.withLogger(reportingRepository.logger());
		return new ReportBuilderPdf(reportBuilderContext);
	}


	/**
	 * Erstellt eine ZIP-Datei, die alle PDF-Dateien der übergebenen PDF-Builder enthält.
	 *
	 * @param pdfBuilders Eine Liste mit ReportBuilderPdf, die die einzelnen PDF-Dateien erzeugen.
	 *
	 * @return Gibt das ZIP in Form eines ByteArrays zurück.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private byte[] createZIP(final List<ReportBuilderPdf> pdfBuilders) throws ApiOperationException {
		final byte[] zipData;
		try {
			try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
				try (ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream)) {
					for (final ReportBuilderPdf pdfBuilder : pdfBuilders) {
						addPdfToZip(pdfBuilder, zos);
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

	/**
	 * Fügt eine generierte PDF-Datei zum ZIP-Output-Stream hinzu.
	 *
	 * @param pdfBuilder Der {@code ReportBuilderPdf}, der die PDF-Datei erzeugt
	 * @param zos Der {@code ZipOutputStream}, zu dem die PDF-Datei hinzugefügt wird
	 *
	 * @throws ApiOperationException Wird geworfen, wenn ein Fehler bei der Generierung der PDF-Datei auftritt
	 */
	private void addPdfToZip(final ReportBuilderPdf pdfBuilder, final ZipOutputStream zos) throws ApiOperationException {
		try {
			zos.putNextEntry(new ZipEntry(pdfBuilder.getDateinameMitEndung()));
			zos.write(pdfBuilder.generate());
			zos.closeEntry();
		} catch (final Exception e) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4,
					"### FEHLER: PDF-Datei '" + pdfBuilder.getDateiname() + "' konnte mit folgender Fehlermeldung nicht generiert werden: " + e.getMessage());
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e,
					"### FEHLER: PDF-Datei '" + pdfBuilder.getDateiname() + "' konnte mit folgender Fehlermeldung nicht generiert werden: " + e.getMessage());
		}
	}
}
