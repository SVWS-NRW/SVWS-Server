package de.svws_nrw.module.reporting.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.svws_nrw.db.utils.ApiOperationException;
import de.vwsoft.barcodelib4j.twod.QRCodeErrorCorrection;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der QR-Code-Erzeugung aus {@link ReportingBarcodeUtils}.
 * <p>Der Inhalt eines QR-Codes stammt aus den Reportdaten und nicht aus dem Request; ein nicht darstellbarer Wert ist deshalb ein serverseitiges Problem
 * und kein Client-Fehler. Geprüft wird zugleich, dass die Meldung den betroffenen Inhalt benennt: Sie entsteht innerhalb des {@code try}-Blocks und wird
 * ohne vorgezogenen Catch von der allgemeinen Fehlerbehandlung überschrieben.</p>
 * <p><b>Diese Ebene wirft bewusst.</b> Die Signatur-QR-Codes der Schulbescheinigung werten den Fehler aus und markieren die Bescheinigung als nicht
 * erzeugbar; eine still eingesetzte leere Fläche ergäbe ein Dokument, das ohne Prüfcode gültig aussieht. Für die Verwendung in Vorlagen fängt der
 * {@code #convert}-Dialekt den Fehler dagegen ab und stellt eine Lücke dar — geprüft in {@code TestConvertExpressionHelperCodes}.</p>
 * <p>Als nicht kodierbarer Inhalt dient ein Text mit chinesischen Schriftzeichen. Die Bibliothek beantwortet {@code canEncode()} dafür mit {@code false},
 * weil ohne festen Zeichensatz nur der Zeichenvorrat bis ISO-8859-1 zur Verfügung steht.</p>
 */
class TestReportingBarcodeUtils {

	/** Ein Inhalt, den die Bibliothek nicht kodieren kann. */
	private static final String NICHT_KODIERBAR = "学校证明";


	@Test
	void testEinNichtKodierbarerInhaltIstEinServerfehler() {
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> ReportingBarcodeUtils.erzeuge2DCodeQRCode(NICHT_KODIERBAR, 50.0, 50.0, QRCodeErrorCorrection.L));
		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertEquals("Der Inhalt kann nicht als QR-Code enkodiert werden: '" + NICHT_KODIERBAR + "'", aoe.getBody(),
				"Die Meldung muss den betroffenen Inhalt benennen und darf nicht von der allgemeinen Fehlerbehandlung ersetzt werden.");
	}

	@Test
	void testEinLeererInhaltErgibtEinLeeresSvgUndKeinenFehler() {
		// Bestandsverhalten: Ein fehlender Wert wird als leere Fläche dargestellt, nicht als Fehler.
		final String svg = ReportingBarcodeUtils.erzeuge2DCodeQRCode("   ", 50.0, 50.0, QRCodeErrorCorrection.L);
		assertTrue(svg.startsWith("data:image/svg+xml;base64,"), "Auch die leere Fläche wird als Data-URI geliefert.");
	}

	@Test
	void testEinRegulaererInhaltErgibtEinenQrCode() {
		final String svg = ReportingBarcodeUtils.erzeuge2DCodeQRCode("Zeugnis 4711", 50.0, 50.0, QRCodeErrorCorrection.L);
		assertTrue(svg.startsWith("data:image/svg+xml;base64,"), "Der QR-Code wird als Data-URI geliefert.");
	}

}
