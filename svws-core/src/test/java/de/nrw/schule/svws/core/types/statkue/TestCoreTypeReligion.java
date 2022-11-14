package de.nrw.schule.svws.core.types.statkue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.nrw.schule.svws.core.types.schule.Religion;


/**
 * Diese Klasse enthält die Testroutinen für den Core-Type Religion.
 */
@DisplayName("Teste den Core-Type Religion")
public class TestCoreTypeReligion {
	
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
    	
    	assertEquals(Religion.AR.daten.bezeichnung,("alevitisch"));
    	assertEquals(Religion.ER.daten.bezeichnung,("evangelisch"));
    	assertEquals(Religion.HR.daten.bezeichnung,("jüdisch"));
    	assertEquals(Religion.IR.daten.bezeichnung,("islamisch"));
    	assertEquals(Religion.KR.daten.bezeichnung,("katholisch"));
    	assertEquals(Religion.ME.daten.bezeichnung,("mennonitische BG NRW"));
    	assertEquals(Religion.OH.daten.bezeichnung,("ohne Bekenntnis"));
    	assertEquals(Religion.OR.daten.bezeichnung,("griechisch-orthodox"));
    	assertEquals(Religion.SO.daten.bezeichnung,("syrisch-orthodox"));
    	assertEquals(Religion.XO.daten.bezeichnung,("sonstige orthodoxe"));
    	assertEquals(Religion.XR.daten.bezeichnung,("andere Religionen"));
    }

	// TODO add additional tests
    
}
