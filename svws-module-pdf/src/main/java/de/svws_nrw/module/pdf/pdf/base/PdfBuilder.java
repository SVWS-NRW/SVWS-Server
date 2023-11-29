package de.svws_nrw.module.pdf.pdf.base;

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.util.XRLog;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Objects;


/**
 * Diese Klasse dient der Erzeugung von PDF-Dokumenten aus html.
 */
public class PdfBuilder {

	/** Das finale html (also keine Datei oder Vorlage), aus dem die PDF-Datei erzeugt werden soll. */
	private final String html;

	/**
	 * Pfad und Name zu einer zum html gehörenden Datei in Projekt-Resources, z. B. die zum html gehörende CSS-Datei.
	 * Daraus gewinnt der PDF-Builder die baseURI für weitere Dateien zum html.
	 */
	private final String ressourcenDateipfad;

	/** Dateiname der PDF-Datei. */
	private final String pdfDateiname;


	/**
	 * Erstellt einen neuen Builder für die Erzeugung des PDF-Dokumentes aus dem übergebenen html-Inhalt.
	 * In dieser Vorlage müssen die Daten bereits eingearbeitet worden sein.
	 *
	 * @param html					Das html, aus dem schließlich die PDF-Datei erzeugt wird.
	 * @param ressourcenDateipfad 	Pfad im Projekt, an dem der Builder die CSS-Datei finden kann.
	 * @param pdfDateiname 			Dateiname der PDF_Datei.
	 */
	public PdfBuilder(final String html, final String ressourcenDateipfad, final String pdfDateiname) {
		this.html = html;
		this.ressourcenDateipfad = ressourcenDateipfad;
		this.pdfDateiname = pdfDateiname;
	}


	/**
	 * Gibt den Dateinamen der PDF-Datei zurück.
	 * @return Dateiname der PDF-Datei.
	 */
	public String getPdfDateiname() {
		return pdfDateiname;
	}


	/**
	 * Erzeugt die PDF-Datei in Form eines Byte-Arrays.
	 *
	 * @return 	das Byte-Array mit der PDF-Datei oder null im Fehlerfall
	 */
	public byte[] getPdfByteArray() {
		try {
			final ByteArrayOutputStream baoStream = new ByteArrayOutputStream(32768);
			erzeugePDF(baoStream);
			return baoStream.toByteArray();
		} catch (@SuppressWarnings("unused") final IOException e) {
			return new byte[0];
		}
	}


	/**
	 * Erzeugt eine Response mit der PDF-Datei als Content
	 *
	 * @return Response mit der PDF-Datei als Content oder im Fehlerfall eine Response als WebApplicationException
	 */
	public Response getPdfResponse() {

		try {
			final byte[] data = getPdfByteArray();
			if (data == null)
				return OperationError.INTERNAL_SERVER_ERROR.getResponse("Fehler bei der Generierung der PDF-Datei.");

			final String encodedFilename = "filename*=UTF-8''" + URLEncoder.encode(pdfDateiname, StandardCharsets.UTF_8);

			return Response.ok(data, "application/pdf")
					.header("Content-Disposition", "attachment; " + encodedFilename)
					.build();
		} catch (WebApplicationException ex) {
			return ex.getResponse();
		}
	}


	/**
	 * Erzeugt das PDF-Dokument mit der Hilfe des PdfRendererBuilder und schreibt in den übergebenen Output-Stream.
	 *
	 * @param 	oStream			der {@link OutputStream}, der die Daten der PDF-Datei enthält.
	 *
	 * @throws 	IOException   	wenn der HTML-Code nicht erzeugt werden kann oder bei einem Fehler beim Erzeugen (siehe auch {@link PdfRendererBuilder#run()}
	 */
	private void erzeugePDF(final OutputStream oStream) throws IOException {

		XRLog.listRegisteredLoggers().forEach(logger -> XRLog.setLevel(logger, java.util.logging.Level.WARNING));

		final Calendar now = Calendar.getInstance();

		try (PDDocument doc = new PDDocument()) {

			final PDDocumentInformation info = doc.getDocumentInformation();

			info.setAuthor("SVWSServer");
			info.setCreationDate(now);
			info.setCreator("SVWSServer");
			info.setModificationDate(now);
			info.setProducer("SVWSServer");

			final PdfRendererBuilder builder = new PdfRendererBuilder();
			final String baseURI = Objects.requireNonNull(PDDocument.class.getClassLoader().getResource(ressourcenDateipfad)).toString();

			builder.useFont(() -> PDDocument.class.getClassLoader().getResourceAsStream("de/svws_nrw/module/pdf/fonts/liberation/LiberationSans-Regular.ttf"), "liberation");
			builder.useFont(() -> PDDocument.class.getClassLoader().getResourceAsStream("de/svws_nrw/module/pdf/fonts/liberation/LiberationSans-Bold.ttf"), "liberation", 700, BaseRendererBuilder.FontStyle.NORMAL, true);
			builder.useFont(() -> PDDocument.class.getClassLoader().getResourceAsStream("de/svws_nrw/module/pdf/fonts/liberation/LiberationSans-Italic.ttf"), "liberation", 400, BaseRendererBuilder.FontStyle.ITALIC, true);
			builder.useFont(() -> PDDocument.class.getClassLoader().getResourceAsStream("de/svws_nrw/module/pdf/fonts/liberation/LiberationSans-BoldItalic.ttf"), "liberation", 700, BaseRendererBuilder.FontStyle.ITALIC, true);

			builder.useFastMode();
			builder.usePDDocument(doc);
			builder.withHtmlContent(html, baseURI);
			builder.toStream(oStream);

			builder.run();
		}
	}
}
