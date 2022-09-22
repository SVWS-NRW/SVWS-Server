package de.nrw.schule.svws.core.types.statkue.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.nrw.schule.svws.core.types.statkue.Schulform;
import de.nrw.schule.svws.core.types.statkue.Schulgliederung;


/**
 * Diese Klasse enthält die Testroutinen für Core-Types Stakue-Packages.
 */
@DisplayName("Teste Core-Types des Stakue-Packages")
public class TestCoreTypesStatkue {
	
    /**
     * Initialisiert den Test
     */
    @BeforeAll
    static void setup() {
    }

    
    @Test
    @DisplayName("Teste den Typ Schulform")
    void testSchulform() {
    	assertTrue(Schulform.GY.daten.hatGymOb);
    	assertEquals(Schulform.R, Schulform.getByKuerzel("R"));
    	assertEquals(Schulform.GE, Schulform.getByNummer("15"));
    	// TODO add additional tests
    }
    
    @Test
    @DisplayName("Teste den Typ Schulgliederung")
    void testSchulgliederung() {
    	if (Schulgliederung.get(Schulform.GY).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform GY gefunden.");
    }
    
}
