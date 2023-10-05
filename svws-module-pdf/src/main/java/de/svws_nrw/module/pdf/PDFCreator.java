package de.svws_nrw.module.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.util.XRLog;

/**
 * Diese Klasse dient der Erzeugung von PDF-Dokumenten.
 */
public class PDFCreator {

	/** Das HTML-Dokument der Vorlage mit Platzhaltern, aus der die PDF-Datei generiert wird */
	private final String htmlDoc;

	/** Die Daten zum Ersetzen von Platzhaltern im HTML-Dokument der Vorlage */
	protected HashMap<String, String> htmlData = new HashMap<>();


	/** Ein nachfolgender PDF-Creator, der für Folgeseiten genutzt wird (nur body). Dieser kann wiederum einen Nachfolger haben. */
	private PDFCreator next = null;


	/**
	 * Erstellt einen neuen Manager für PDF-Dokumente.
	 *
	 * @param html	das HTML-Dokument mit Platzhaltern
	 */
	public PDFCreator(final String html) {
		this.htmlDoc = html;
	}


	/**
	 * Setzt den PDF-Creator, der für Folgeseiten genutzt wird (nur body).
	 *
	 * @param next   der nachfolgende PDF-Creator
	 */
	public void setNext(final PDFCreator next) {
		this.next = next;
	}


	/**
	 * Erzeugt das PDF-Dokument und schreibt in den übergebenen Output-Stream.
	 *
	 * @param 	os   			the {@link OutputStream} to be used
	 *
	 * @throws 	IOException   	wenn der HTML-Code nicht erzeugt werden kann oder bei einem Fehler beim Rendern (siehe auch {@link PdfRendererBuilder#run()}
	 */
	private void runBuilder(final OutputStream os) throws IOException {
		final String html = getFinalHtmlDoc();
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
			builder.useFont(() -> PDDocument.class.getClassLoader().getResourceAsStream("de/svws_nrw/module/pdf/fonts/liberation/LiberationSans-Regular.ttf"), "liberation");
			builder.useFont(() -> PDDocument.class.getClassLoader().getResourceAsStream("de/svws_nrw/module/pdf/fonts/liberation/LiberationSans-Bold.ttf"), "liberation", 700, BaseRendererBuilder.FontStyle.NORMAL, true);
			builder.useFont(() -> PDDocument.class.getClassLoader().getResourceAsStream("de/svws_nrw/module/pdf/fonts/liberation/LiberationSans-Italic.ttf"), "liberation", 400, BaseRendererBuilder.FontStyle.ITALIC, true);
			builder.useFont(() -> PDDocument.class.getClassLoader().getResourceAsStream("de/svws_nrw/module/pdf/fonts/liberation/LiberationSans-BoldItalic.ttf"), "liberation", 700, BaseRendererBuilder.FontStyle.ITALIC, true);
			builder.useFastMode();
			builder.usePDDocument(doc);
			builder.withHtmlContent(html, null);
			builder.toStream(os);
			builder.run();
		}
	}


	/**
	 * Ersetzt im übergebenen HTML-Dokument die vorhandenen Platzhalter und gibt das finale HTML-Dokument mit Daten zurück
	 *
	 * @return 	das finale HTML-Dokument, in dem die Platzhalter ersetzt worden sind.
	 *
	 * @throws 	IOException, wenn die Platzhalter im HTML-Dokument nicht vollständig und richtig ersetzt werden können.
	 */
	private String getFinalHtmlDoc() throws IOException {

		final StringBuilder result = new StringBuilder();
		final String[] parts = htmlDoc.split("@@");

		for (int i = 0; i < parts.length; i++) {
			if (i % 2 == 0) {
				result.append(parts[i]);
			} else {
				final String mapped = htmlData.get(parts[i]);
				if (mapped != null)
					result.append(mapped);
				else
					throw new IOException("Fehlerhaftes HTML-Template in Klasse " + this.getClass().getCanonicalName());
			}
		}

		if (next != null) {
			result.append("<div style=\"page-break-after: always;\"></div>");
			result.append(next.getFinalHtmlDoc());
		}
		return result.toString();
	}


	/**
	 * Speichert das PDF-Dokument in einem byte[].
	 *
	 * @return das Byte-Array mit dem PDF-Dokument oder null im Fehlerfall
	 */
	public byte[] toByteArray() {
		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream(32768);
			runBuilder(baos);
			return baos.toByteArray();
		} catch (@SuppressWarnings("unused") final IOException e) {
			return new byte[0];
		}
	}


	/**
	 * Speichert das PDF-Dokument in der Datei mit dem
	 * übergebenen Dateinamen.
	 *
	 * @param filename   der Dateiname
	 *
	 * @throws IOException im Fehlerfall (siehe auch {@link PdfRendererBuilder#run()}
	 */
	public void save(final String filename) throws IOException {
		final File file = new File(filename);
		// TODO Create directory if it does not exist
		try (FileOutputStream fos = new FileOutputStream(file)) {
			runBuilder(fos);
		}
	}


	/**
	 * Erzeugt eine unordered list (ul) aus der übergebenen String-Liste
	 * und schreibt das Ergebnis in den übergebenen {@link StringBuilder}.
	 * Ist die Liste null oder leer, so wird keine unordered list erzeugt.
	 *
	 * @param sb     der StringBuilder
	 * @param list   die Liste
	 */
	public static void ul(final StringBuilder sb, final List<String> list) {
		if ((list == null) || (list.isEmpty()))
			return;
		sb.append("<ul>");
		for (final String s : list) {
			sb.append("<li>");
			sb.append(s);
			sb.append("</li>");
		}
		sb.append("</ul>");
	}
}
