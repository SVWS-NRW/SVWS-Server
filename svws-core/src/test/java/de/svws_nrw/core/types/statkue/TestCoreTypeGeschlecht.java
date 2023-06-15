package de.svws_nrw.core.types.statkue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.types.Geschlecht;


/**
 * Diese Klasse enthält die Testroutinen für den Core-Type Geschlecht.
 */
@DisplayName("Teste den Core-Type Geschlecht")
class TestCoreTypeGeschlecht {

    /**
     * Führt grundlegende Tests zu der Definition zu dem Core-Type-Geschlecht durch.
     */
    @Test
    @DisplayName("Teste den Typ Geschlecht")
    void testGeschlecht() {
    	assertEquals(Geschlecht.M, Geschlecht.fromStringValue("M"));
    	assertEquals(Geschlecht.W, Geschlecht.fromStringValue("W"));
    	assertEquals(Geschlecht.D, Geschlecht.fromStringValue("D"));
    	assertEquals(Geschlecht.X, Geschlecht.fromStringValue("X"));

    	assertEquals(Geschlecht.M, Geschlecht.fromValue(3));
    	assertEquals(Geschlecht.W, Geschlecht.fromValue(4));
    	assertEquals(Geschlecht.D, Geschlecht.fromValue(5));
    	assertEquals(Geschlecht.X, Geschlecht.fromValue(6));
    }

	// TODO add additional tests

}
