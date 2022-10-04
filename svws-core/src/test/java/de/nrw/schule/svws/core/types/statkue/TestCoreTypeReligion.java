package de.nrw.schule.svws.core.types.statkue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/**
 * Diese Klasse enthält die Testroutinen für den Core-Type Religion.
 */
@DisplayName("Teste den Core-Type Religion")
public class TestCoreTypeReligion {
	
    /**
     * Initialisiert den Test
     */
    @BeforeAll
    static void setup() {
    }

    
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
    }

	// TODO add additional tests
    
}
