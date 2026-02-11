package de.svws_nrw.module.reporting.builders;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Calendar;

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import com.openhtmltopdf.util.XRLog;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

/**
 * Diese Klasse ist verantwortlich für das Rendern von PDF-Dokumenten aus HTML-Inhalten.
 * Die Klasse konfiguriert den PDF-Renderer, registriert Schriftarten und erstellt das finale PDF-Dokument.
 */
public final class ReportRendererPdf {

	private final Logger logger;

	/**
	 * Erstellt eine neue Instanz des ReportingRendererPDF.
	 *
	 * @param logger Der Logger, der für die Fehlerausgabe verwendet werden soll
	 */
	public ReportRendererPdf(final Logger logger) {
		this.logger = logger;
	}

	/**
	 * Rendert ein PDF-Dokument basierend auf dem gegebenen HTML-Inhalt sowie spezifischen Konfigurationen
	 * und schreibt das generierte Dokument in den bereitgestellten OutputStream.
	 *
	 * @param html Der HTML-Inhalt, der in das PDF-Dokument gerendert werden soll.
	 * @param rootPfad Der Wurzelpfad, der zur Auflösung von Ressourcen wie Fonts dient.
	 * @param outputStream Der OutputStream, in den das generierte PDF-Dokument geschrieben wird.
	 *
	 * @throws ApiOperationException Wird geworfen, wenn ein I/O-Fehler während des Renderings auftritt.
	 */
	public void renderPdf(final String html, final String rootPfad, final OutputStream outputStream)
			throws ApiOperationException {

		// Logging-Level der OpenHtmlToPdf-Komponente setzen
		XRLog.listRegisteredLoggers().forEach(xrLogger -> XRLog.setLevel(xrLogger, java.util.logging.Level.WARNING));

		final Calendar now = Calendar.getInstance();

		try (PDDocument doc = new PDDocument()) {
			// Dokument-Metadaten setzen
			final PDDocumentInformation info = doc.getDocumentInformation();
			info.setAuthor("SVWSServer");
			info.setCreationDate(now);
			info.setCreator("SVWSServer");
			info.setModificationDate(now);
			info.setProducer("SVWSServer");

			// Renderer konfigurieren
			final PdfRendererBuilder builder = new PdfRendererBuilder();
			final URL baseRes = PDDocument.class.getClassLoader().getResource(rootPfad);
			if (baseRes == null) {
				if (logger != null)
					logger.logLn(LogLevel.ERROR, 4, "### FEHLER: Der Root-Pfad zu den Ressourcen wurde nicht gefunden. Angegebener Pfad: " + rootPfad);
				throw new ApiOperationException(Response.Status.NOT_FOUND,
						"### FEHLER: Der Root-Pfad zu den Ressourcen wurde nicht gefunden. Angegebener Pfad: " + rootPfad);
			}
			final String baseURI = baseRes.toString();

			// Fonts registrieren
			registerFonts(builder, rootPfad);

			// Builder konfigurieren
			builder.useFastMode();
			builder.usePDDocument(doc);
			builder.withHtmlContent(html, baseURI);
			builder.useSVGDrawer(new BatikSVGDrawer());
			builder.toStream(outputStream);

			// PDF generieren
			builder.run();
		} catch (final Exception e) {
			if (logger != null)
				logger.logLn(LogLevel.ERROR, 4, "### FEHLER: Das PDF konnte aufgrund des folgenden Fehlers nicht gerendert werden: " + e.getMessage());
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, e,
					"### FEHLER: Das PDF konnte aufgrund der folgenden Fehlers nicht gerendert werden: " + e.getMessage());
		}
	}

	/**
	 * Registriert die Fonts für den angegebenen PdfRendererBuilder. Die Methode lädt mehrere Schriftarten aus dem spezifizierten Root-Pfad, die dann beim
	 * Erstellen des PDF-Dokuments verwendet werden können.
	 *
	 * @param builder Der PdfRendererBuilder, dem die Fonts hinzugefügt werden sollen.
	 * @param rootPfad Der Wurzelpfad, in dem sich die Schriftartendateien befinden.
	 *
	 * @throws ApiOperationException Wird ausgelöst, wenn die Schriftartendatei nicht gefunden wird oder ein I/O-Fehler beim Laden der Schriftart auftritt.
	 */
	private void registerFonts(final PdfRendererBuilder builder, final String rootPfad) throws ApiOperationException {
		final String fontsPfad = rootPfad + "fonts/liberation/";
		registerFont(builder, fontsPfad + "LiberationSans-Regular.ttf", "liberation", 400, BaseRendererBuilder.FontStyle.NORMAL, false);
		registerFont(builder, fontsPfad + "LiberationSans-Bold.ttf", "liberation", 700, BaseRendererBuilder.FontStyle.NORMAL, true);
		registerFont(builder, fontsPfad + "LiberationSans-Italic.ttf", "liberation", 400, BaseRendererBuilder.FontStyle.ITALIC, true);
		registerFont(builder, fontsPfad + "LiberationSans-BoldItalic.ttf", "liberation", 700, BaseRendererBuilder.FontStyle.ITALIC, true);
	}

	/**
	 * Registriert eine Schriftart für den angegebenen PdfRendererBuilder.
	 * Die Methode lädt die Schriftart aus einem angegebenen Datei- oder Ressourcenpfad und stellt sie für die PDF-Erstellung bereit.
	 *
	 * @param builder Der PdfRendererBuilder, der die Schriftart verwenden soll.
	 * @param path Der Pfad zur Schriftartendatei bezogen auf den Classpath.
	 * @param family Der Name der Schriftartfamilie, beispielsweise "Arial".
	 * @param weight Das Gewicht der Schriftart (z. B. 400 für normal, 700 für Fett).
	 * @param style Der Stil der Schriftart (z. B. normal oder kursiv).
	 * @param embed Gibt an, ob die Schriftart in das PDF eingebettet werden soll (true für Einbettung, false für Referenzierung).
	 *
	 * @throws ApiOperationException Wird ausgelöst, wenn die Schriftartendatei nicht gefunden wird oder ein I/O-Fehler beim Laden der Schriftart auftritt.
	 */
	private void registerFont(final PdfRendererBuilder builder, final String path, final String family, final int weight,
			final BaseRendererBuilder.FontStyle style, final boolean embed) throws ApiOperationException {

		try (InputStream is = PDDocument.class.getClassLoader().getResourceAsStream(path)) {
			if (is == null) {
				if (logger != null)
					logger.logLn(LogLevel.ERROR, 4, "### FEHLER: Schriftart nicht gefunden: " + path);
				throw new ApiOperationException(Response.Status.NOT_FOUND, "### FEHLER: Schriftart nicht gefunden: " + path);
			}
		} catch (final Exception e) {
			if (logger != null)
				logger.logLn(LogLevel.ERROR, 4, "### FEHLER: Schriftart nicht gefunden: " + path);
			throw new ApiOperationException(Response.Status.NOT_FOUND, e, "### FEHLER: Schriftart nicht gefunden: " + path);
		}
		builder.useFont(() -> PDDocument.class.getClassLoader().getResourceAsStream(path), family, weight, style, embed);
	}
}
