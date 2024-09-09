package de.svws_nrw.core.types.statkue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;


/**
 * Diese Klasse enthält die Testroutinen für den Core-Type LehrerRechtsverhaeltnis.
 */
@DisplayName("Teste den Core-Type LehrerRechtsverhaeltnis")
class TestCoreTypeLehrerRechtsverhaeltnis {

	/**
	 * Führt grundlegende Tests zu dem Core-Type LehrerRechtsverhaeltnis aus.
	 */
	@Test
	@DisplayName("Teste den Typ LehrerRechtsverhaeltnis")
	void testLehrerRechtsverhaeltnis() {
		assertEquals(LehrerRechtsverhaeltnis.L, LehrerRechtsverhaeltnis.data().getWertByKuerzel("L"));
		assertEquals(LehrerRechtsverhaeltnis.P, LehrerRechtsverhaeltnis.data().getWertByKuerzel("P"));
		assertEquals(LehrerRechtsverhaeltnis.A, LehrerRechtsverhaeltnis.data().getWertByKuerzel("A"));
		assertEquals(LehrerRechtsverhaeltnis.N, LehrerRechtsverhaeltnis.data().getWertByKuerzel("N"));
		assertEquals(LehrerRechtsverhaeltnis.W, LehrerRechtsverhaeltnis.data().getWertByKuerzel("W"));
		assertEquals(LehrerRechtsverhaeltnis.U, LehrerRechtsverhaeltnis.data().getWertByKuerzel("U"));
		assertEquals(LehrerRechtsverhaeltnis.B, LehrerRechtsverhaeltnis.data().getWertByKuerzel("B"));
		assertEquals(LehrerRechtsverhaeltnis.J, LehrerRechtsverhaeltnis.data().getWertByKuerzel("J"));
		assertEquals(LehrerRechtsverhaeltnis.S, LehrerRechtsverhaeltnis.data().getWertByKuerzel("S"));
		assertEquals(LehrerRechtsverhaeltnis.X, LehrerRechtsverhaeltnis.data().getWertByKuerzel("X"));

		assertEquals("Beamter auf Lebenszeit", LehrerRechtsverhaeltnis.L.daten(2024).text);
		assertEquals("Beamter auf Probe", LehrerRechtsverhaeltnis.P.daten(2024).text);
		assertEquals("Beamter auf Probe zur Anstellung", LehrerRechtsverhaeltnis.A.daten(2024).text);
		assertEquals("Beamter, nebenamtlich (nicht hauptamtlich im Schuldienst)", LehrerRechtsverhaeltnis.N.daten(2024).text);
		assertEquals("Beamter auf Widerruf (LAA)", LehrerRechtsverhaeltnis.W.daten(2024).text);
		assertEquals("Angestellte, unbefristet (BAT-Vertrag)", LehrerRechtsverhaeltnis.U.daten(2024).text);
		assertEquals("Angestellte, befristet (BAT-Vertrag)", LehrerRechtsverhaeltnis.B.daten(2024).text);
		assertEquals("Angestellte, nicht BAT-Vertrag", LehrerRechtsverhaeltnis.J.daten(2024).text);
		assertEquals("Gestellungsvertrag", LehrerRechtsverhaeltnis.S.daten(2024).text);
		assertEquals("Unentgeltlich Beschäftigte", LehrerRechtsverhaeltnis.X.daten(2024).text);
	}

	// TODO add additional tests

}
