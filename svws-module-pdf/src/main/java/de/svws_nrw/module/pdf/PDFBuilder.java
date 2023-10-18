package de.svws_nrw.module.pdf;

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.util.XRLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.Calendar;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;


/**
 * Diese Klasse dient der Erzeugung von PDF-Dokumenten.
 */
public class PDFBuilder {

	/**
	 * Der Inhalt des finalen html-Dokumentes, um daraus die PDF-Datei zu generieren.
	 * Hierbei wurden bereits die Daten in das html-Template eingearbeitet.
	 */
	private final String htmlFuerPDF;

	/**
	 * Pfad im Projekt, an dem der Builder die CSS-Datei finden kann. Pfad wird vom PDF-Builder benötigt,
	 * um als baseURI für nachladbare Dateien zu fungieren.
	 */
	private final String cssDateipfad;

	/**
	 * Erstellt einen neuen Builder für die Erzeugung des PDF-Dokumentes aus dem übergebenen html-Inhalt.
	 * In dieser Vorlage müssen die Daten bereits eingearbeitet worden sein.
	 *
	 * @param htmlFuerPDF	Der finale html-Inhalt, aus dem die PDF-Datei erzeugt werden soll.
	 * @param cssDateipfad 	Pfad im Projekt, an dem der Builder die CSS-Datei finden kann.
	 */
	public PDFBuilder(final String htmlFuerPDF, final String cssDateipfad) {

		this.htmlFuerPDF = htmlFuerPDF;
		this.cssDateipfad = cssDateipfad;
	}


	/**
	 * Speichert das PDF-Dokument in einem byte-Array.
	 *
	 * @return 	das Byte-Array mit dem PDF-Dokument oder null im Fehlerfall
	 */
	public byte[] getPDFAlsByteArray() {
		try {
			final ByteArrayOutputStream baoStream = new ByteArrayOutputStream(32768);
			erzeugePDF(baoStream);
			return baoStream.toByteArray();
		} catch (@SuppressWarnings("unused") final IOException e) {
			return new byte[0];
		}
	}


	/**
	 * Erzeugt das PDF-Dokument mit der Hilfe des PdfRendererBuilder und schreibt in den übergebenen Output-Stream.
	 *
	 * @param oStream		der {@link OutputStream}, der die Daten der PDF-Datei enthält.
	 *
	 * @throws 	IOException   	wenn der HTML-Code nicht erzeugt werden kann oder bei einem Fehler beim Rendern (siehe auch {@link PdfRendererBuilder#run()}
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
			final String baseURI = Objects.requireNonNull(PDDocument.class.getClassLoader().getResource(cssDateipfad)).toString();

			builder.useFont(() -> PDDocument.class.getClassLoader().getResourceAsStream("de/svws_nrw/module/pdf/fonts/liberation/LiberationSans-Regular.ttf"), "liberation");
			builder.useFont(() -> PDDocument.class.getClassLoader().getResourceAsStream("de/svws_nrw/module/pdf/fonts/liberation/LiberationSans-Bold.ttf"), "liberation", 700, BaseRendererBuilder.FontStyle.NORMAL, true);
			builder.useFont(() -> PDDocument.class.getClassLoader().getResourceAsStream("de/svws_nrw/module/pdf/fonts/liberation/LiberationSans-Italic.ttf"), "liberation", 400, BaseRendererBuilder.FontStyle.ITALIC, true);
			builder.useFont(() -> PDDocument.class.getClassLoader().getResourceAsStream("de/svws_nrw/module/pdf/fonts/liberation/LiberationSans-BoldItalic.ttf"), "liberation", 700, BaseRendererBuilder.FontStyle.ITALIC, true);

			builder.useFastMode();
			builder.usePDDocument(doc);
			builder.withHtmlContent(htmlFuerPDF, baseURI);
			builder.toStream(oStream);

			builder.run();
		}
	}


}
