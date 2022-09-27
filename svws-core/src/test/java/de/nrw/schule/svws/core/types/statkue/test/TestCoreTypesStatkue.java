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
    	assertEquals(Schulform.G, Schulform.getByKuerzel("G"));
    	assertEquals(Schulform.H, Schulform.getByKuerzel("H"));
    	assertEquals(Schulform.V, Schulform.getByKuerzel("V"));
    	assertEquals(Schulform.S, Schulform.getByKuerzel("S"));
    	assertEquals(Schulform.PS, Schulform.getByKuerzel("PS"));
    	assertEquals(Schulform.SK, Schulform.getByKuerzel("SK"));
    	assertEquals(Schulform.R, Schulform.getByKuerzel("R"));
    	assertEquals(Schulform.GE, Schulform.getByKuerzel("GE"));
    	assertEquals(Schulform.GM, Schulform.getByKuerzel("GM"));
    	assertEquals(Schulform.FW, Schulform.getByKuerzel("FW"));
    	assertEquals(Schulform.HI, Schulform.getByKuerzel("HI"));
    	assertEquals(Schulform.WF, Schulform.getByKuerzel("WF"));
    	assertEquals(Schulform.GY, Schulform.getByKuerzel("GY"));
    	assertEquals(Schulform.WB, Schulform.getByKuerzel("WB"));
    	assertEquals(Schulform.BK, Schulform.getByKuerzel("BK"));
    	assertEquals(Schulform.KS, Schulform.getByKuerzel("KS"));
    	assertEquals(Schulform.SR, Schulform.getByKuerzel("SR"));
    	assertEquals(Schulform.SG, Schulform.getByKuerzel("SG"));
    	assertEquals(Schulform.SB, Schulform.getByKuerzel("SB"));
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
    	if (Schulgliederung.get(Schulform.G).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform G gefunden.");
    	if (Schulgliederung.get(Schulform.H).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform H gefunden.");
    	if (Schulgliederung.get(Schulform.V).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform V gefunden.");
    	if (Schulgliederung.get(Schulform.S).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform S gefunden.");
    	if (Schulgliederung.get(Schulform.PS).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform PS gefunden.");
    	if (Schulgliederung.get(Schulform.SK).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform SK gefunden.");
    	if (Schulgliederung.get(Schulform.R).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform R gefunden.");
    	if (Schulgliederung.get(Schulform.GE).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform GE gefunden.");
    	if (Schulgliederung.get(Schulform.GM).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform GM gefunden.");
    	if (Schulgliederung.get(Schulform.FW).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform FW gefunden.");
    	if (Schulgliederung.get(Schulform.HI).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform HI gefunden.");
    	if (Schulgliederung.get(Schulform.WF).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform WF gefunden.");
    	if (Schulgliederung.get(Schulform.GY).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform GY gefunden.");
    	if (Schulgliederung.get(Schulform.WB).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform WB gefunden.");
    	if (Schulgliederung.get(Schulform.BK).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform BK gefunden.");
    	if (Schulgliederung.get(Schulform.KS).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform KS gefunden.");
    	if (Schulgliederung.get(Schulform.SR).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform SR gefunden.");
    	if (Schulgliederung.get(Schulform.SG).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform SG gefunden.");
    	if (Schulgliederung.get(Schulform.SB).size() <= 0)
    		fail("Keine Schulgliederungen für die Schulform GB gefunden.");
    	
    }
    
}
