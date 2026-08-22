package de.svws_nrw.module.reporting.html.dialects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemmelder;
import de.svws_nrw.module.reporting.utils.ReportingBarcodeUtils;

/**
 * Tests der Barcode- und QR-Code-Ausgabe des {@code #convert}-Dialekts.
 * <p>Vorlagen erzeugen ihre Codes aus Fachdaten. Ein Wert, der sich nicht darstellen lässt — etwa ein Name mit Zeichen jenseits von ISO-8859-1 —, darf
 * nach den Konventionen des Moduls die Druckausgabe nicht abbrechen, sondern erscheint als saubere Lücke. Geprüft wird, dass an die Stelle des
 * Codes eine leere, transparente Fläche in den angeforderten Maßen tritt und der Grund als Ausgabeproblem an den laufenden Report gemeldet wird.</p>
 * <p>Die Maße der Lücke müssen denen des erfolgreichen Codes entsprechen, sonst verschiebt ein Fehlerfall das Layout. Das betrifft besonders die
 * Standardmaße ohne eigene Angabe: Ein QR-Code ist quadratisch, ein Code128-Barcode dagegen flacher.</p>
 * <p>Abgegrenzt davon bleibt {@link ReportingBarcodeUtils} selbst: Dort wird weiterhin geworfen, weil die Signatur-QR-Codes der Schulbescheinigung den
 * Fehler auswerten müssen — eine Bescheinigung mit leerer Prüffläche sähe sonst gültig aus.</p>
 */
class TestConvertExpressionHelperCodes {

	/** Ein Inhalt, den die Barcode-Bibliothek nicht kodieren kann. */
	private static final String NICHT_KODIERBAR = "学校证明";

	/** Das Präfix, mit dem jede erzeugte Grafik als Data-URI beginnt. */
	private static final String DATA_URI_PREFIX = "data:image/svg+xml;base64,";

	/** Die Meldefassade des laufenden Reports, die der Dialekt aus dem Context erhält. */
	private ReportingProblemmelder melder;

	/** Der Helper, den der Dialekt den Vorlagen bereitstellt. */
	private ConvertExpressionHelper helper;


	@BeforeEach
	void setUp() {
		melder = mock(ReportingProblemmelder.class);
		helper = new ConvertExpressionHelper(melder);
	}


	/**
	 * Prüft, dass genau eine Lücke als nicht darstellbarer Wert gemeldet wurde, und gibt deren Beschreibung zurück.
	 *
	 * @return Die Beschreibung der gemeldeten Lücke.
	 */
	private String gemeldeteLuecke() {
		final ArgumentCaptor<String> beschreibung = ArgumentCaptor.forClass(String.class);
		verify(melder).melde(eq(ReportingProblemursache.NICHT_DARSTELLBAR),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(ReportingBarcodeUtils.class)),
				beschreibung.capture(), any(Exception.class));
		return beschreibung.getValue();
	}


	// ##### Die Lücke tritt an die Stelle des Codes #####

	@Test
	void testEinNichtDarstellbarerQrInhaltErgibtEineLeereFlaeche() {
		final String svg = helper.to2DCodeQRCodeAsSvgHtmlImageSource(NICHT_KODIERBAR, 50.0, 50.0);
		assertEquals(ReportingBarcodeUtils.leeresTransparentesSVG(50.0, 50.0), svg,
				"An die Stelle des QR-Codes muss eine leere Fläche in den angeforderten Maßen treten.");
	}

	@Test
	void testEinNichtDarstellbarerBarcodeInhaltErgibtEineLeereFlaeche() {
		final String svg = helper.toBarcodeCode128AsSvgHtmlImageSource(NICHT_KODIERBAR, 50.0, 30.0);
		assertEquals(ReportingBarcodeUtils.leeresTransparentesSVG(50.0, 30.0), svg,
				"An die Stelle des Barcodes muss eine leere Fläche in den angeforderten Maßen treten.");
	}


	// ##### Die Maße der Lücke entsprechen denen des erfolgreichen Codes #####

	@Test
	void testOhneMassangabeUebernimmtDieLueckeDieStandardmasseDesBarcodes() {
		// Ein Code128-Barcode ist ohne eigene Angabe 50 x 30 mm groß - nicht quadratisch wie ein QR-Code.
		final String svg = helper.toBarcodeCode128AsSvgHtmlImageSource(NICHT_KODIERBAR, 0.0, 0.0);
		assertEquals(ReportingBarcodeUtils.leeresTransparentesSVG(
				ReportingBarcodeUtils.STANDARD_BREITE_MM, ReportingBarcodeUtils.STANDARD_HOEHE_BARCODE_MM), svg,
				"Die Lücke muss die Standardmaße des Barcodes übernehmen und das Layout nicht verändern.");
	}

	@Test
	void testOhneMassangabeUebernimmtDieLueckeDieStandardmasseDesQrCodes() {
		final String svg = helper.to2DCodeQRCodeAsSvgHtmlImageSource(NICHT_KODIERBAR, 0.0, 0.0);
		assertEquals(ReportingBarcodeUtils.leeresTransparentesSVG(
				ReportingBarcodeUtils.STANDARD_BREITE_MM, ReportingBarcodeUtils.STANDARD_HOEHE_QRCODE_MM), svg,
				"Die Lücke muss die Standardmaße des QR-Codes übernehmen.");
	}


	// ##### Der Grund erreicht die Meldefassade des laufenden Reports #####

	@Test
	void testDieLueckeWirdAlsNichtDarstellbarerWertGemeldet() {
		helper.to2DCodeQRCodeAsSvgHtmlImageSource(NICHT_KODIERBAR, 50.0, 50.0);

		final String beschreibung = gemeldeteLuecke();
		assertTrue(beschreibung.contains(NICHT_KODIERBAR), "Die Meldung muss den betroffenen Inhalt benennen: " + beschreibung);
		assertTrue(beschreibung.contains("QR-Code"), "Die Meldung muss die Art des Codes benennen: " + beschreibung);
	}

	@Test
	void testAuchDerBarcodeMeldetSeineArt() {
		helper.toBarcodeCode128AsSvgHtmlImageSource(NICHT_KODIERBAR, 50.0, 30.0);

		final String beschreibung = gemeldeteLuecke();
		assertTrue(beschreibung.contains("Barcode"), "Die Meldung muss die Art des Codes benennen: " + beschreibung);
	}

	@Test
	void testEinErfolgreicherCodeWirdNichtGemeldet() {
		helper.to2DCodeQRCodeAsSvgHtmlImageSource("Zeugnis 4711", 50.0, 50.0);
		verify(melder, never()).melde(any(), any(), any(), anyString(), any());
	}

	@Test
	void testOhneMeldefassadeEntstehtTrotzdemEineLuecke() {
		// Der parameterlose Konstruktor bildet den Fall ab, dass der Context keine Meldefassade mitführt - etwa beim Erzeugen eines Dateinamens.
		final String svg = new ConvertExpressionHelper().to2DCodeQRCodeAsSvgHtmlImageSource(NICHT_KODIERBAR, 50.0, 50.0);
		assertEquals(ReportingBarcodeUtils.leeresTransparentesSVG(50.0, 50.0), svg);
	}

	@Test
	void testEinZuLangerBarcodeInhaltWirdZurGemeldetenLueckeStattStillGekuerzt() {
		// Ein Barcode mit mehr als 64 Zeichen wäre nicht mehr zuverlässig lesbar. Ein stilles Abschneiden erzeugte einen Code mit anderem Inhalt
		// als den Daten - deshalb entsteht stattdessen die gemeldete Lücke.
		final String zuLang = "X".repeat(65);
		final String svg = helper.toBarcodeCode128AsSvgHtmlImageSource(zuLang, 50.0, 30.0);

		assertEquals(ReportingBarcodeUtils.leeresTransparentesSVG(50.0, 30.0), svg,
				"An die Stelle des zu langen Barcodes muss eine leere Fläche treten, kein gekürzter Code.");
		final String beschreibung = gemeldeteLuecke();
		assertTrue(beschreibung.contains("Barcode"), "Die Meldung muss die Art des Codes benennen: " + beschreibung);
	}

	@Test
	void testVierundsechzigZeichenBleibenEinGueltigerBarcode() {
		// Die Gegenprobe zur Längengrenze: Genau 64 Zeichen sind erlaubt.
		final String svg = helper.toBarcodeCode128AsSvgHtmlImageSource("X".repeat(64), 50.0, 30.0);
		assertTrue(svg.startsWith(DATA_URI_PREFIX), "64 Zeichen liegen innerhalb der Grenze und ergeben einen Barcode.");
		verify(melder, never()).melde(any(), any(), any(), anyString(), any());
	}


	// ##### Gegenproben: der reguläre Weg #####

	@Test
	void testEinRegulaererQrInhaltErgibtEinenQrCode() {
		final String svg = helper.to2DCodeQRCodeAsSvgHtmlImageSource("Zeugnis 4711", 50.0, 50.0);
		assertTrue(svg.startsWith(DATA_URI_PREFIX), "Der QR-Code wird als Data-URI geliefert.");
		assertTrue(svg.length() > ReportingBarcodeUtils.leeresTransparentesSVG(50.0, 50.0).length(), "Der erzeugte Code ist mehr als eine leere Fläche.");
	}

	@Test
	void testEinRegulaererBarcodeInhaltErgibtEinenBarcode() {
		final String svg = helper.toBarcodeCode128AsSvgHtmlImageSource("4711", 50.0, 30.0);
		assertTrue(svg.startsWith(DATA_URI_PREFIX), "Der Barcode wird als Data-URI geliefert.");
		assertTrue(svg.length() > ReportingBarcodeUtils.leeresTransparentesSVG(50.0, 30.0).length(), "Der erzeugte Code ist mehr als eine leere Fläche.");
	}

	@Test
	void testEinLeererInhaltErgibtEbenfallsEineLeereFlaeche() {
		// Bestandsverhalten: Der fehlende Wert und der nicht darstellbare Wert führen zum selben Ergebnis - eine Lücke bleibt eine Lücke.
		assertEquals(ReportingBarcodeUtils.leeresTransparentesSVG(50.0, 50.0), helper.to2DCodeQRCodeAsSvgHtmlImageSource("   ", 50.0, 50.0));
		verify(melder, never()).melde(any(), any(), any(), anyString(), any());
	}

}
