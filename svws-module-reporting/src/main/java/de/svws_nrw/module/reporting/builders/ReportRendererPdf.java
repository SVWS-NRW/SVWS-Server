package de.svws_nrw.module.reporting.builders;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Calendar;

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import com.openhtmltopdf.util.XRLog;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

/**
 * Diese Klasse ist verantwortlich für das Rendern von PDF-Dokumenten aus HTML-Inhalten.
 * Die Klasse konfiguriert den PDF-Renderer, registriert Schriftarten und erstellt das finale PDF-Dokument.
 */
// Der Calendar ist von PDFBox vorgegeben: PDDocumentInformation.setCreationDate(...) und setModificationDate(...) erwarten diesen Typ.
public final class ReportRendererPdf {

	/**
	 * Erstellt eine neue Instanz des ReportingRendererPDF.
	 * <p>Der Renderer protokolliert nicht: Ein Abbruch trägt seinen Grund als Meldung der Exception, und ausgegeben wird er an der Abschlussgrenze.</p>
	 */
	public ReportRendererPdf() {
		// Der Renderer hält keinen Zustand; der Konstruktor besteht allein, damit die Klasse ihren Zweck über ihren Namen benennt.
	}

	private static final String SVWSSERVER = "SVWS-Server";
	private static final String LIBERATION = "liberation";

	/**
	 * Rendert ein PDF-Dokument basierend auf dem gegebenen HTML-Inhalt sowie spezifischen Konfigurationen
	 * und schreibt das generierte Dokument in den bereitgestellten OutputStream.
	 *
	 * @param html Der HTML-Inhalt, der in das PDF-Dokument gerendert werden soll.
	 * @param rootPfad Der Wurzelpfad, der zur Auflösung von Ressourcen wie Fonts dient.
	 * @param outputStream Der OutputStream, in den das generierte PDF-Dokument geschrieben wird.
	 *
	 * @throws ApiOperationException Wird geworfen, wenn ein I/O-Fehler während des Renderings auftritt. Der Root-Pfad und die Schriftarten sind interne
	 *                               Ressourcen des Servers, ihr Fehlen wird daher mit {@code INTERNAL_SERVER_ERROR} gemeldet. Eine bereits klassifizierte
	 *                               {@link ApiOperationException} behält ihren Status.
	 */
	public void renderPdf(final String html, final String rootPfad, final OutputStream outputStream)
			throws ApiOperationException {

		// Logging-Level der OpenHtmlToPdf-Komponente setzen
		XRLog.listRegisteredLoggers().forEach(xrLogger -> XRLog.setLevel(xrLogger, java.util.logging.Level.WARNING));

		final Calendar now = Calendar.getInstance();

		try (PDDocument doc = new PDDocument()) {
			// Dokument-Metadaten setzen
			final PDDocumentInformation info = doc.getDocumentInformation();
			info.setAuthor(SVWSSERVER);
			info.setCreationDate(now);
			info.setCreator(SVWSSERVER);
			info.setModificationDate(now);
			info.setProducer(SVWSSERVER);

			// Renderer konfigurieren
			final PdfRendererBuilder builder = new PdfRendererBuilder();
			final URL baseRes = PDDocument.class.getClassLoader().getResource(rootPfad);
			if (baseRes == null) {
				throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR,
						"### FEHLER: Das Ressourcenverzeichnis für die PDF-Erzeugung fehlt auf dem Server.");
			}
			final String baseURI = baseRes.toString();

			// Fonts registrieren
			registerFonts(builder, rootPfad);

			// Builder konfigurieren
			builder.usePDDocument(doc);
			builder.withHtmlContent(html, baseURI);
			builder.useSVGDrawer(new BatikSVGDrawer());
			builder.toStream(outputStream);

			// PDF generieren
			builder.run();
		} catch (final ApiOperationException e) {
			// Bereits klassifizierte Fehler behalten ihren Status - sonst ersetzte der allgemeine Catch die Meldung durch seine eigene, unspezifische.
			throw e;
		} catch (final Exception e) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, e, "### FEHLER: Das PDF-Dokument konnte aus dem HTML nicht erzeugt werden.");
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
		registerFont(builder, fontsPfad + "LiberationSans-Regular.ttf", LIBERATION, 400, BaseRendererBuilder.FontStyle.NORMAL, false);
		registerFont(builder, fontsPfad + "LiberationSans-Bold.ttf", LIBERATION, 700, BaseRendererBuilder.FontStyle.NORMAL, true);
		registerFont(builder, fontsPfad + "LiberationSans-Italic.ttf", LIBERATION, 400, BaseRendererBuilder.FontStyle.ITALIC, true);
		registerFont(builder, fontsPfad + "LiberationSans-BoldItalic.ttf", LIBERATION, 700, BaseRendererBuilder.FontStyle.ITALIC, true);
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
	 *                               Die Schriftarten liegen im Ressourcenverzeichnis des Servers, ihr Fehlen ist daher {@code INTERNAL_SERVER_ERROR}.
	 */
	private void registerFont(final PdfRendererBuilder builder, final String path, final String family, final int weight,
			final BaseRendererBuilder.FontStyle style, final boolean embed) throws ApiOperationException {

		try (InputStream is = PDDocument.class.getClassLoader().getResourceAsStream(path)) {
			if (is == null) {
				throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR,
						"### FEHLER: Eine Schriftart für die PDF-Erzeugung fehlt auf dem Server.");
			}
		} catch (final ApiOperationException e) {
			// Die eigene Meldung oben liegt innerhalb des try-Blocks; ohne diesen Zweig ersetzte der allgemeine Catch sie durch die des Ladefehlers.
			throw e;
		} catch (final Exception e) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, e,
					"### FEHLER: Eine Schriftart für die PDF-Erzeugung konnte auf dem Server nicht geladen werden.");
		}
		builder.useFont(() -> PDDocument.class.getClassLoader().getResourceAsStream(path), family, weight, style, embed);
	}
}
