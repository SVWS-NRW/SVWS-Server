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

    	assertEquals(LehrerRechtsverhaeltnis.L.daten.text, "Beamter auf Lebenszeit");
    	assertEquals(LehrerRechtsverhaeltnis.P.daten.text, "Beamter auf Probe");
    	assertEquals(LehrerRechtsverhaeltnis.A.daten.text, "Beamter auf Probe zur Anstellung");
    	assertEquals(LehrerRechtsverhaeltnis.N.daten.text, "Beamter, nebenamtlich (nicht hauptamtlich im Schuldienst)");
    	assertEquals(LehrerRechtsverhaeltnis.W.daten.text, "Beamter auf Widerruf (LAA)");
    	assertEquals(LehrerRechtsverhaeltnis.U.daten.text, "Angestellte, unbefristet (BAT-Vertrag)");
    	assertEquals(LehrerRechtsverhaeltnis.B.daten.text, "Angestellte, befristet (BAT-Vertrag)");
    	assertEquals(LehrerRechtsverhaeltnis.J.daten.text, "Angestellte, nicht BAT-Vertrag");
    	assertEquals(LehrerRechtsverhaeltnis.S.daten.text, "Gestellungsvertrag");
    	assertEquals(LehrerRechtsverhaeltnis.X.daten.text, "Unentgeltlich Beschäftigte");
    }

	// TODO add additional tests

}
