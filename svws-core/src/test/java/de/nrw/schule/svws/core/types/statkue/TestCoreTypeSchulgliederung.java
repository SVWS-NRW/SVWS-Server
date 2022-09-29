package de.nrw.schule.svws.core.types.statkue;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/**
 * Diese Klasse enthält die Testroutinen für Core-Type Schulgliederung.
 */
@DisplayName("Teste den Core-Type Schulgliederung")
public class TestCoreTypeSchulgliederung {
	
    /**
     * Initialisiert den Test
     */
    @BeforeAll
    static void setup() {
    }

    /**
     * Prüft, ob die Schulformen mindestens eine Schulgliederungen haben.
     */
    @Test
    @DisplayName("Teste ob die Schulformen mindestens eine Schulgliederungen haben")
    void testSchulformMindestensEineSchulgliederung() {
    	for (Schulform sf : Schulform.values()) {
        	// TODO Pseudoschulformen entfernen und dann die nachfolgende Überprüfung entfernen
    		if (switch (sf) { case AS, HU, LB, SO, SP, WZ, XB, XS -> true; default -> false; }) 
    			continue;
    		if (Schulgliederung.get(sf).size() <= 0)
        		fail("Keine Schulgliederungen für die Schulform " + sf.name() + " gefunden.");
    	}
    }

    /**
     * Prüft, ob die Schulgliederungen jeweils mindestens eine Schulform haben.
     */
    @Test
    @DisplayName("Teste ob die Schulgliederungen jeweils mindestens eine Schulform haben")
    void testSchulgliederungMindestensEineSchulform() {
    	for (Schulgliederung gl : Schulgliederung.values()) {
    		if (gl.getSchulformen().size() <= 0)
    			fail("Keine Schulform für die Schulgliederung " + gl.name() + "angegeben.");
    	}
    }
    
    // TODO weitere Tests
    
}
