package de.svws_nrw.module.reporting.builders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.interactive.viewerpreferences.PDViewerPreferences;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.builders.ReportPdfBroschuere.A3Seite;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests des Umbaus eines A4-Dokuments zu einer Broschüre im Format DIN A3 quer.
 * <p>Geprüft wird die Zusage, die den Druck trägt: Vier A4-Seiten liegen auf einem A3-Bogen, und zwar in der Folge, die ein beidseitiger Druck mit Wendung
 * über die kurze Seite zu einem lesbaren Heft faltet. Die Prüfung liest dafür den Text aus der linken und der rechten Hälfte jeder A3-Seite; die Zahl der
 * Seiten allein sagte über die Folge nichts.</p>
 * <p>Die Zahl der A4-Seiten wird auf ein Vielfaches von vier aufgerundet, und jedes Dokument füllt eigene Bogen. Nur so teilen sich in einer Sammelausgabe
 * zwei Personen kein Blatt, auch wenn ihre Dokumente unterschiedlich lang sind.</p>
 */
class TestReportPdfBroschuere {

	/** Die Breite einer A4-Seite in Punkten; sie ist die Breite einer Hälfte des A3-Bogens. */
	private static final float BREITE_A4 = PDRectangle.A4.getWidth();

	/** Die Toleranz in Punkten, innerhalb der zwei Seitenmaße im Test als gleich gelten. */
	private static final float TOLERANZ = 1.0f;

	/**
	 * Ein HTML-Inhalt mit vier Seiten. Die Seitenregel entspricht der der Zeugnisvorlagen, damit der Test an denselben Maßen prüft, mit denen der Renderer
	 * im Betrieb arbeitet.
	 */
	private static final String HTML_MIT_VIER_SEITEN = """
			<html><head><style>
			@page { size: A4 portrait; margin: 5mm; }
			.seite { page-break-before: always; }
			.seite:first-child { page-break-before: avoid; }
			</style></head><body>
			<div class="seite">Seite1</div>
			<div class="seite">Seite2</div>
			<div class="seite">Seite3</div>
			<div class="seite">Seite4</div>
			</body></html>""";


	// ##### Die Seitenfolge #####

	@Test
	void testVierSeitenErgebenEinenBogenInDerFolgeVierEinsZweiDrei() {
		assertEquals(List.of(new A3Seite(4, 1), new A3Seite(2, 3)), ReportPdfBroschuere.seitenfolge(4));
	}

	@Test
	void testAchtSeitenErgebenZweiIneinanderliegendeBogen() {
		// Der äußere Bogen trägt die Seiten am Rand der Folge, der innere die Seiten in ihrer Mitte. Gedruckt wird von außen nach innen.
		assertEquals(List.of(new A3Seite(8, 1), new A3Seite(2, 7), new A3Seite(6, 3), new A3Seite(4, 5)),
				ReportPdfBroschuere.seitenfolge(8));
	}

	@Test
	void testDieSeitenzahlWirdAufEinVielfachesVonVierAufgerundet() {
		// Drei Seiten füllen einen Bogen; die vierte Hälfte bleibt leer. Die Folge nennt sie dennoch, damit die Lage der echten Seiten erhalten bleibt.
		assertEquals(ReportPdfBroschuere.seitenfolge(4), ReportPdfBroschuere.seitenfolge(3));
		assertEquals(ReportPdfBroschuere.seitenfolge(8), ReportPdfBroschuere.seitenfolge(6));
		assertEquals(ReportPdfBroschuere.seitenfolge(12), ReportPdfBroschuere.seitenfolge(9));
	}

	@Test
	void testJedeSeiteDesDokumentsKommtGenauEinmalVor() {
		for (int anzahlSeiten = 1; anzahlSeiten <= 12; anzahlSeiten++) {
			final List<A3Seite> folge = ReportPdfBroschuere.seitenfolge(anzahlSeiten);
			for (int seite = 1; seite <= anzahlSeiten; seite++) {
				final int seitennummer = seite;
				final long treffer = folge.stream().filter(a3 -> (a3.links() == seitennummer) || (a3.rechts() == seitennummer)).count();
				assertEquals(1, treffer, "Die Seite %d muss bei %d Seiten genau einmal vorkommen.".formatted(seite, anzahlSeiten));
			}
		}
	}


	// ##### Das umgebaute Dokument #####

	@Test
	void testDerBogenTraegtZweiA4SeitenNebeneinanderAufA3Quer() throws IOException {
		try (PDDocument broschuere = Loader.loadPDF(ReportPdfBroschuere.umbauAufA3(a4Dokument(4)))) {
			assertEquals(2, broschuere.getNumberOfPages(), "Vier A4-Seiten ergeben einen Bogen mit zwei Seiten.");
			final PDRectangle format = broschuere.getPage(0).getMediaBox();
			assertEquals(2 * BREITE_A4, format.getWidth(), TOLERANZ);
			assertEquals(PDRectangle.A4.getHeight(), format.getHeight(), TOLERANZ);
			assertTrue(format.getWidth() > format.getHeight(), "Der Bogen liegt quer.");
		}
	}

	@Test
	void testDieSeitenLiegenInDerFolgeDerBroschuereAufDemBogen() throws IOException {
		try (PDDocument broschuere = Loader.loadPDF(ReportPdfBroschuere.umbauAufA3(a4Dokument(4)))) {
			assertEquals(kennung(4), textLinks(broschuere, 0));
			assertEquals(kennung(1), textRechts(broschuere, 0));
			assertEquals(kennung(2), textLinks(broschuere, 1));
			assertEquals(kennung(3), textRechts(broschuere, 1));
		}
	}

	@Test
	void testBeiSechsSeitenBleibenDieLetztenZweiHaelftenLeer() throws IOException {
		// Sechs Seiten füllen zwei Bogen. Die beiden fehlenden Seiten sind die letzten der Folge und liegen damit auf der Rückseite des äußeren Bogens.
		try (PDDocument broschuere = Loader.loadPDF(ReportPdfBroschuere.umbauAufA3(a4Dokument(6)))) {
			assertEquals(4, broschuere.getNumberOfPages());
			assertEquals("", textLinks(broschuere, 0));
			assertEquals(kennung(1), textRechts(broschuere, 0));
			assertEquals(kennung(2), textLinks(broschuere, 1));
			assertEquals("", textRechts(broschuere, 1));
			assertEquals(kennung(6), textLinks(broschuere, 2));
			assertEquals(kennung(3), textRechts(broschuere, 2));
			assertEquals(kennung(4), textLinks(broschuere, 3));
			assertEquals(kennung(5), textRechts(broschuere, 3));
		}
	}

	@Test
	void testDasDokumentNenntDieDruckeinstellungen() throws IOException {
		try (PDDocument broschuere = Loader.loadPDF(ReportPdfBroschuere.umbauAufA3(a4Dokument(4)))) {
			final PDViewerPreferences einstellungen = broschuere.getDocumentCatalog().getViewerPreferences();
			assertEquals(PDViewerPreferences.DUPLEX.DuplexFlipShortEdge.toString(), einstellungen.getDuplex());
			assertEquals(PDViewerPreferences.PRINT_SCALING.None.toString(), einstellungen.getPrintScaling());
			assertTrue(einstellungen.getCOSObject().getBoolean(COSName.getPDFName("PickTrayByPDFSize"), false),
					"Das Papier wird nach dem Seitenformat gewählt.");
		}
	}

	@Test
	void testEinDokumentMitUnterschiedlichGrossenSeitenWirdAbgelehnt() throws IOException {
		final byte[] gemischt = dokument(PDRectangle.A4, PDRectangle.A5);
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> ReportPdfBroschuere.umbauAufA3(gemischt));
		assertEquals(Status.BAD_REQUEST.getStatusCode(), aoe.getStatus().getStatusCode());
	}


	// ##### Zusammenspiel mit dem Renderer #####

	@Test
	void testEinGerendertesDokumentFuelltDieHaelftenDesBogensOhneSkalierung() throws IOException {
		// Der Umbau skaliert nicht. Er setzt damit voraus, dass der Renderer Seiten in genau dem Maß liefert, das eine Hälfte des Bogens aufnimmt.
		final ByteArrayOutputStream gerendert = new ByteArrayOutputStream();
		new ReportRendererPdf().renderPdf(HTML_MIT_VIER_SEITEN, ReportingReportvorlage.getRootPfad(), gerendert);

		try (PDDocument broschuere = Loader.loadPDF(ReportPdfBroschuere.umbauAufA3(gerendert.toByteArray()))) {
			assertEquals(2, broschuere.getNumberOfPages(), "Die vier gerenderten Seiten ergeben einen Bogen.");
			assertEquals(2 * BREITE_A4, broschuere.getPage(0).getMediaBox().getWidth(), TOLERANZ);
			assertEquals(kennung(4), textLinks(broschuere, 0));
			assertEquals(kennung(1), textRechts(broschuere, 0));
		}
	}


	// ##### Die Sammelausgabe #####

	@Test
	void testJedesDokumentDerSammelausgabeBelegtEigeneBogen() throws IOException {
		// Drei Dokumente mit 3, 4 und 6 Seiten ergeben 1 + 1 + 2 Bogen, also 8 A3-Seiten. Jedes Dokument steuert eine gerade Zahl an A3-Seiten bei; nur
		// dadurch fällt die Rückseite eines Blattes nie auf das Dokument der nächsten Person.
		final byte[] sammlung = ReportPdfBroschuere.fuegeZusammen(List.of(
				ReportPdfBroschuere.umbauAufA3(a4Dokument(3)),
				ReportPdfBroschuere.umbauAufA3(a4Dokument(4)),
				ReportPdfBroschuere.umbauAufA3(a4Dokument(6))));
		try (PDDocument broschuere = Loader.loadPDF(sammlung)) {
			assertEquals(8, broschuere.getNumberOfPages());
			assertEquals(kennung(1), textRechts(broschuere, 0), "Das erste Dokument beginnt auf dem ersten Bogen.");
			assertEquals(kennung(4), textLinks(broschuere, 2), "Das zweite Dokument beginnt auf einem eigenen Bogen.");
			assertEquals(kennung(1), textRechts(broschuere, 4), "Das dritte Dokument beginnt auf einem eigenen Bogen.");
		}
	}

	@Test
	void testDieSammelausgabeNenntDieDruckeinstellungen() throws IOException {
		final byte[] sammlung = ReportPdfBroschuere.fuegeZusammen(List.of(
				ReportPdfBroschuere.umbauAufA3(a4Dokument(4)),
				ReportPdfBroschuere.umbauAufA3(a4Dokument(4))));
		try (PDDocument broschuere = Loader.loadPDF(sammlung)) {
			final PDViewerPreferences einstellungen = broschuere.getDocumentCatalog().getViewerPreferences();
			assertEquals(PDViewerPreferences.DUPLEX.DuplexFlipShortEdge.toString(), einstellungen.getDuplex());
			assertEquals(PDViewerPreferences.PRINT_SCALING.None.toString(), einstellungen.getPrintScaling());
		}
	}


	// ##### Hilfsmittel der Tests #####

	/**
	 * Der Text, den eine Seite des Testdokuments trägt. Er benennt die Seite und macht damit ihre Lage auf dem Bogen prüfbar.
	 *
	 * @param seitennummer Die Nummer der A4-Seite, beginnend bei 1.
	 *
	 * @return Die Kennung der Seite.
	 */
	private static String kennung(final int seitennummer) {
		return "Seite" + seitennummer;
	}

	/**
	 * Erzeugt ein Testdokument im Format DIN A4, dessen Seiten ihre Nummer tragen.
	 *
	 * @param anzahlSeiten Die Zahl der Seiten.
	 *
	 * @return Das Dokument.
	 *
	 * @throws IOException Wenn das Dokument nicht geschrieben werden kann.
	 */
	private static byte[] a4Dokument(final int anzahlSeiten) throws IOException {
		final PDRectangle[] seitenformate = new PDRectangle[anzahlSeiten];
		Arrays.fill(seitenformate, PDRectangle.A4);
		return dokument(seitenformate);
	}

	/**
	 * Erzeugt ein Testdokument mit den übergebenen Seitenformaten; jede Seite trägt ihre Nummer.
	 *
	 * @param seitenformate Die Formate der Seiten in ihrer Reihenfolge.
	 *
	 * @return Das Dokument.
	 *
	 * @throws IOException Wenn das Dokument nicht geschrieben werden kann.
	 */
	private static byte[] dokument(final PDRectangle... seitenformate) throws IOException {
		try (PDDocument dokument = new PDDocument();
				ByteArrayOutputStream ausgabe = new ByteArrayOutputStream()) {
			for (int nummer = 1; nummer <= seitenformate.length; nummer++) {
				final PDPage seite = new PDPage(seitenformate[nummer - 1]);
				dokument.addPage(seite);
				try (PDPageContentStream inhalt = new PDPageContentStream(dokument, seite)) {
					inhalt.beginText();
					inhalt.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 36);
					inhalt.newLineAtOffset(100, 400);
					inhalt.showText(kennung(nummer));
					inhalt.endText();
				}
			}
			dokument.save(ausgabe);
			return ausgabe.toByteArray();
		}
	}

	/**
	 * Der Text in der linken Hälfte einer A3-Seite.
	 *
	 * @param broschuere  Das umgebaute Dokument.
	 * @param seitenindex Der Index der A3-Seite, beginnend bei 0.
	 *
	 * @return Der gefundene Text ohne umgebende Leerzeichen.
	 *
	 * @throws IOException Wenn der Text nicht gelesen werden kann.
	 */
	private static String textLinks(final PDDocument broschuere, final int seitenindex) throws IOException {
		return PdfSeitentexte.links(broschuere, seitenindex);
	}

	/**
	 * Der Text in der rechten Hälfte einer A3-Seite.
	 *
	 * @param broschuere  Das umgebaute Dokument.
	 * @param seitenindex Der Index der A3-Seite, beginnend bei 0.
	 *
	 * @return Der gefundene Text ohne umgebende Leerzeichen.
	 *
	 * @throws IOException Wenn der Text nicht gelesen werden kann.
	 */
	private static String textRechts(final PDDocument broschuere, final int seitenindex) throws IOException {
		return PdfSeitentexte.rechts(broschuere, seitenindex);
	}

}
