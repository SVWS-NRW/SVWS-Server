package de.svws_nrw.module.reporting.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.svws_nrw.db.utils.ApiOperationException;
import de.vwsoft.barcodelib4j.twod.QRCodeErrorCorrection;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Barcode- und QR-Code-Erzeugung aus {@link ReportingBarcodeUtils}.
 * <p>Der Inhalt eines Codes stammt aus den Reportdaten und nicht aus dem Request; ein nicht darstellbarer Wert ist deshalb ein serverseitiges Problem
 * und kein Client-Fehler. Die Meldung nennt den Inhalt nicht - den trägt der Dialekt in seinem Hinweis. Sie entsteht innerhalb des {@code try}-Blocks:
 * Ohne vorgezogenen Catch ersetzte die allgemeine Fehlerbehandlung sie durch ihre eigene. Geprüft werden deshalb je Zweig Status, Wortlaut und Ursache.</p>
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
		final String meldung = assertInstanceOf(String.class, aoe.getBody());
		assertEquals("### FEHLER: Der Inhalt lässt sich nicht als QR-Code darstellen.", meldung,
				"Die eigene Meldung darf nicht von der allgemeinen Fehlerbehandlung ersetzt werden.");
		assertFalse(meldung.contains(NICHT_KODIERBAR), "Den Inhalt nennt der Dialekt in seinem Hinweis; die Meldung bleibt ohne ihn: " + meldung);
	}

	@Test
	void testEinZuLangerQrInhaltIstEinServerfehlerMitUrsache() {
		// Die Kapazität eines QR-Codes endet bei Version 40; die Bibliothek wirft erst beim Aufbau des Symbols. Das ist der Weg der allgemeinen
		// Fehlerbehandlung, und die Ursache muss mitreisen - sonst nennt der Fehlerblock keinen Grund.
		final String inhalt = "X".repeat(8000);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> ReportingBarcodeUtils.erzeuge2DCodeQRCode(inhalt, 50.0, 50.0, QRCodeErrorCorrection.L));
		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertEquals("### FEHLER: Der QR-Code konnte nicht erzeugt werden.", aoe.getBody());
		assertNotNull(aoe.getCause(), "Die Ausnahme der Bibliothek reist als Ursache mit.");
	}

	@Test
	void testEinNichtKodierbarerBarcodeInhaltIstEinServerfehlerMitUrsache() {
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> ReportingBarcodeUtils.erzeugeBarcodeCode128(NICHT_KODIERBAR, 50.0, 30.0));
		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertEquals("### FEHLER: Der Barcode konnte nicht erzeugt werden.", aoe.getBody());
		assertNotNull(aoe.getCause(), "Die Ausnahme der Bibliothek reist als Ursache mit.");
	}

	@Test
	void testEinZuLangerBarcodeInhaltIstEinServerfehler() {
		// Mehr als 64 Zeichen ergäben keinen zuverlässig lesbaren Code128; die Prüfung steht vor der Bibliothek und nennt die Länge, nicht den Inhalt.
		final String inhalt = "X".repeat(65);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> ReportingBarcodeUtils.erzeugeBarcodeCode128(inhalt, 50.0, 30.0));
		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertEquals("### FEHLER: Der Inhalt ist mit 65 Zeichen zu lang für einen lesbaren Barcode; erlaubt sind höchstens 64 Zeichen.", aoe.getBody());
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
