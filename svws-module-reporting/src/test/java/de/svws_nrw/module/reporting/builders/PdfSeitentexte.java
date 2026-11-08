package de.svws_nrw.module.reporting.builders;

import java.awt.geom.Rectangle2D;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;

/**
 * Liest den Text aus der linken und der rechten Hälfte einer A3-Seite. Erst damit ist prüfbar, welche A4-Seite auf welcher Hälfte eines Bogens liegt; die
 * Zahl der Seiten allein sagt über die Folge nichts.
 * <p>Geprüft wird allein die waagerechte Lage, denn sie entscheidet über die Hälfte. Als Grenze dient die Mitte der Seite, sodass die Hilfsklasse ohne
 * Kenntnis des Seitenformats auskommt.</p>
 */
public final class PdfSeitentexte {

	private PdfSeitentexte() {
		// Utility
	}

	/**
	 * Der Text in der linken Hälfte einer A3-Seite.
	 *
	 * @param dokument    Das Dokument im Format DIN A3 quer.
	 * @param seitenindex Der Index der Seite, beginnend bei 0.
	 *
	 * @return Der gefundene Text ohne umgebende Leerzeichen.
	 *
	 * @throws IOException Wenn der Text nicht gelesen werden kann.
	 */
	public static String links(final PDDocument dokument, final int seitenindex) throws IOException {
		return text(dokument.getPage(seitenindex), true);
	}

	/**
	 * Der Text in der rechten Hälfte einer A3-Seite.
	 *
	 * @param dokument    Das Dokument im Format DIN A3 quer.
	 * @param seitenindex Der Index der Seite, beginnend bei 0.
	 *
	 * @return Der gefundene Text ohne umgebende Leerzeichen.
	 *
	 * @throws IOException Wenn der Text nicht gelesen werden kann.
	 */
	public static String rechts(final PDDocument dokument, final int seitenindex) throws IOException {
		return text(dokument.getPage(seitenindex), false);
	}

	/**
	 * Der Text in einer Hälfte eines Bogens.
	 *
	 * @param bogen        Die A3-Seite.
	 * @param linkeHaelfte true für die linke, false für die rechte Hälfte.
	 *
	 * @return Der gefundene Text ohne umgebende Leerzeichen.
	 *
	 * @throws IOException Wenn der Text nicht gelesen werden kann.
	 */
	private static String text(final PDPage bogen, final boolean linkeHaelfte) throws IOException {
		final float breite = bogen.getMediaBox().getWidth() / 2.0f;
		final PDFTextStripperByArea leser = new PDFTextStripperByArea();
		leser.addRegion("haelfte", new Rectangle2D.Float(linkeHaelfte ? 0.0f : breite, 0.0f, breite, bogen.getMediaBox().getHeight()));
		leser.extractRegions(bogen);
		return leser.getTextForRegion("haelfte").trim();
	}

}
