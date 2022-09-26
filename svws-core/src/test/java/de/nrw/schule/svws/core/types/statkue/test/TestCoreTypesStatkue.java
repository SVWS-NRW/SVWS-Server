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
    	assertEquals(Schulform.G, Schulform.getByNummer("02"));
    	assertEquals(Schulform.H, Schulform.getByNummer("04"));
    	assertEquals(Schulform.V, Schulform.getByNummer("06"));
    	assertEquals(Schulform.S, Schulform.getByNummer("08"));
    	assertEquals(Schulform.PS, Schulform.getByNummer("13"));
    	assertEquals(Schulform.SK, Schulform.getByNummer("14"));
    	assertEquals(Schulform.R, Schulform.getByNummer("10"));
    	assertEquals(Schulform.GE, Schulform.getByNummer("15"));
    	assertEquals(Schulform.GM, Schulform.getByNummer("16"));
    	assertEquals(Schulform.FW, Schulform.getByNummer("17"));
    	assertEquals(Schulform.HI, Schulform.getByNummer("18"));
    	assertEquals(Schulform.FW, Schulform.getByNummer("17"));
    	assertEquals(Schulform.WF, Schulform.getByNummer("19"));
    	assertEquals(Schulform.GY, Schulform.getByNummer("20"));
    	assertEquals(Schulform.WB, Schulform.getByNummer("25"));
    	assertEquals(Schulform.BK, Schulform.getByNummer("30"));
    	assertEquals(Schulform.KS, Schulform.getByNummer("83"));
    	assertEquals(Schulform.SR, Schulform.getByNummer("85"));
    	assertEquals(Schulform.SG, Schulform.getByNummer("87"));
    	assertEquals(Schulform.SB, Schulform.getByNummer("88"));
    	// TODO add additional tests
    }
    
    @Test
    @DisplayName("Teste den Typ Schulgliederung")
    void testSchulgliederung() {
    	if (Schulgliederung.get(Schulform.GY).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform GY gefunden.");
    }
    
}
