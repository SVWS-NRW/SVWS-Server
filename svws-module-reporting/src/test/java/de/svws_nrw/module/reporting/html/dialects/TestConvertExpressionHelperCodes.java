package de.svws_nrw.module.reporting.html.dialects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.module.reporting.utils.ReportingBarcodeUtils;

/**
 * Tests der Barcode- und QR-Code-Ausgabe des {@code #convert}-Dialekts.
 * <p>Vorlagen erzeugen ihre Codes aus Fachdaten. Ein Wert, der sich nicht darstellen lässt — etwa ein Name mit Zeichen jenseits von ISO-8859-1 —, darf
 * nach Abschnitt 4 der Reporting-Konventionen die Druckausgabe nicht abbrechen, sondern erscheint als saubere Lücke. Geprüft wird, dass an die Stelle des
 * Codes eine leere, transparente Fläche in den angeforderten Maßen tritt und der Grund im Log des laufenden Reports steht.</p>
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

	/** Der Logger des laufenden Reports, den der Dialekt aus dem Context erhält. */
	private Logger logger;

	/** Die Liste, die die Einträge des Loggers sammelt. */
	private LogConsumerList log;

	/** Der Helper, den der Dialekt den Vorlagen bereitstellt. */
	private ConvertExpressionHelper helper;


	@BeforeEach
	void setUp() {
		logger = new Logger();
		log = new LogConsumerList();
		logger.addConsumer(log);
		helper = new ConvertExpressionHelper(logger);
	}


	/**
	 * Gibt die Texte aller Logeinträge mit dem Level WARNING zurück, ohne die Einrückung des Loggers.
	 *
	 * @return Die Texte der WARNING-Einträge in der Reihenfolge ihres Auftretens.
	 */
	private List<String> warnungenImLog() {
		return log.getLogData().stream().filter(eintrag -> eintrag.getLevel() == LogLevel.WARNING).map(eintrag -> eintrag.getText().strip()).toList();
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


	// ##### Der Grund steht im Log des laufenden Reports #####

	@Test
	void testDieLueckeWirdImReportLogBegruendet() {
		helper.to2DCodeQRCodeAsSvgHtmlImageSource(NICHT_KODIERBAR, 50.0, 50.0);

		final List<String> warnungen = warnungenImLog();
		assertEquals(1, warnungen.size(), "Die Lücke muss genau einmal begründet werden: " + warnungen);
		assertTrue(warnungen.getFirst().contains(NICHT_KODIERBAR), "Die Warnung muss den betroffenen Inhalt benennen: " + warnungen.getFirst());
		assertTrue(warnungen.getFirst().contains("QR-Code"), "Die Warnung muss die Art des Codes benennen: " + warnungen.getFirst());
	}

	@Test
	void testEinErfolgreicherCodeWirdNichtProtokolliert() {
		helper.to2DCodeQRCodeAsSvgHtmlImageSource("Zeugnis 4711", 50.0, 50.0);
		assertEquals(List.of(), warnungenImLog(), "Ein erzeugter Code ist kein Anlass für einen Logeintrag.");
	}

	@Test
	void testOhneLoggerImContextEntstehtTrotzdemEineLuecke() {
		// Der parameterlose Konstruktor bildet den Fall ab, dass der Context keinen Logger mitführt - etwa beim Erzeugen eines Dateinamens.
		final String svg = new ConvertExpressionHelper().to2DCodeQRCodeAsSvgHtmlImageSource(NICHT_KODIERBAR, 50.0, 50.0);
		assertEquals(ReportingBarcodeUtils.leeresTransparentesSVG(50.0, 50.0), svg);
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
		assertEquals(List.of(), warnungenImLog(), "Ein von vornherein leerer Wert ist kein Fehlschlag.");
	}

}
