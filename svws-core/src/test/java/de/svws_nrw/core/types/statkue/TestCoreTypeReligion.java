package de.svws_nrw.core.types.statkue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.types.schule.Religion;


/**
 * Diese Klasse enthält die Testroutinen für den Core-Type Religion.
 */
@DisplayName("Teste den Core-Type Religion")
class TestCoreTypeReligion {

	/**
	 * Führt grundlegende Tests zu dem Core-Type Religion aus.
	 */
    @Test
    @DisplayName("Teste den Typ Religion")
    void testReligion() {
    	assertEquals(Religion.AR, Religion.getByKuerzel("AR"));
    	assertEquals(Religion.ER, Religion.getByKuerzel("ER"));
    	assertEquals(Religion.HR, Religion.getByKuerzel("HR"));
    	assertEquals(Religion.IR, Religion.getByKuerzel("IR"));
    	assertEquals(Religion.KR, Religion.getByKuerzel("KR"));
    	assertEquals(Religion.ME, Religion.getByKuerzel("ME"));
    	assertEquals(Religion.OH, Religion.getByKuerzel("OH"));
    	assertEquals(Religion.OR, Religion.getByKuerzel("OR"));
    	assertEquals(Religion.SO, Religion.getByKuerzel("SO"));
    	assertEquals(Religion.XO, Religion.getByKuerzel("XO"));
    	assertEquals(Religion.XR, Religion.getByKuerzel("XR"));

    	assertEquals("alevitisch", Religion.AR.daten.bezeichnung);
    	assertEquals("evangelisch", Religion.ER.daten.bezeichnung);
    	assertEquals("jüdisch", Religion.HR.daten.bezeichnung);
    	assertEquals("islamisch", Religion.IR.daten.bezeichnung);
    	assertEquals("katholisch", Religion.KR.daten.bezeichnung);
    	assertEquals("mennonitische BG NRW", Religion.ME.daten.bezeichnung);
    	assertEquals("ohne Bekenntnis", Religion.OH.daten.bezeichnung);
    	assertEquals("griechisch-orthodox", Religion.OR.daten.bezeichnung);
    	assertEquals("syrisch-orthodox", Religion.SO.daten.bezeichnung);
    	assertEquals("sonstige orthodoxe", Religion.XO.daten.bezeichnung);
    	assertEquals("andere Religionen", Religion.XR.daten.bezeichnung);
    }

	// TODO add additional tests

}
