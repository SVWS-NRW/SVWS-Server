package de.svws_nrw.core.types.statkue;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.Schulgliederung;


/**
 * Diese Klasse enthält die Testroutinen für Core-Type Schulgliederung.
 */
@DisplayName("Teste den Core-Type Schulgliederung")
public class TestCoreTypeSchulgliederung {

    /**
     * Prüft, ob die Schulformen mindestens eine Schulgliederungen haben.
     */
    @Test
    @DisplayName("Teste ob die Schulformen mindestens eine Schulgliederungen haben")
    void testSchulformMindestensEineSchulgliederung() {
    	for (final Schulform sf : Schulform.values()) {
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
    	for (final Schulgliederung gl : Schulgliederung.values()) {
    		if (gl.getSchulformen().size() <= 0)
    			fail("Keine Schulform für die Schulgliederung " + gl.name() + "angegeben.");
    	}
    }

    // TODO weitere Tests

}
