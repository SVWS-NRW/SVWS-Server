package de.svws_nrw.core.types.statkue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.types.lehrer.LehrerRechtsverhaeltnis;


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
    	assertEquals(LehrerRechtsverhaeltnis.L, LehrerRechtsverhaeltnis.getByKuerzel("L"));
    	assertEquals(LehrerRechtsverhaeltnis.P, LehrerRechtsverhaeltnis.getByKuerzel("P"));
    	assertEquals(LehrerRechtsverhaeltnis.A, LehrerRechtsverhaeltnis.getByKuerzel("A"));
    	assertEquals(LehrerRechtsverhaeltnis.N, LehrerRechtsverhaeltnis.getByKuerzel("N"));
    	assertEquals(LehrerRechtsverhaeltnis.W, LehrerRechtsverhaeltnis.getByKuerzel("W"));
    	assertEquals(LehrerRechtsverhaeltnis.U, LehrerRechtsverhaeltnis.getByKuerzel("U"));
    	assertEquals(LehrerRechtsverhaeltnis.B, LehrerRechtsverhaeltnis.getByKuerzel("B"));
    	assertEquals(LehrerRechtsverhaeltnis.J, LehrerRechtsverhaeltnis.getByKuerzel("J"));
    	assertEquals(LehrerRechtsverhaeltnis.S, LehrerRechtsverhaeltnis.getByKuerzel("S"));
    	assertEquals(LehrerRechtsverhaeltnis.X, LehrerRechtsverhaeltnis.getByKuerzel("X"));

    	assertEquals("Beamter auf Lebenszeit", LehrerRechtsverhaeltnis.L.daten.text);
    	assertEquals("Beamter auf Probe", LehrerRechtsverhaeltnis.P.daten.text);
    	assertEquals("Beamter auf Probe zur Anstellung", LehrerRechtsverhaeltnis.A.daten.text);
    	assertEquals("Beamter, nebenamtlich (nicht hauptamtlich im Schuldienst)", LehrerRechtsverhaeltnis.N.daten.text);
    	assertEquals("Beamter auf Widerruf (LAA)", LehrerRechtsverhaeltnis.W.daten.text);
    	assertEquals("Angestellte, unbefristet (BAT-Vertrag)", LehrerRechtsverhaeltnis.U.daten.text);
    	assertEquals("Angestellte, befristet (BAT-Vertrag)", LehrerRechtsverhaeltnis.B.daten.text);
    	assertEquals("Angestellte, nicht BAT-Vertrag", LehrerRechtsverhaeltnis.J.daten.text);
    	assertEquals("Gestellungsvertrag", LehrerRechtsverhaeltnis.S.daten.text);
    	assertEquals("Unentgeltlich Beschäftigte", LehrerRechtsverhaeltnis.X.daten.text);
    }

	// TODO add additional tests

}
