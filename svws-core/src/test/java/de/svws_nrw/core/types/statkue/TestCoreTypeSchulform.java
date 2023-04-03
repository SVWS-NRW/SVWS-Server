package de.svws_nrw.core.types.statkue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.types.schule.Schulform;


/**
 * Diese Klasse enthält die Testroutinen für den Core-Type Schulform.
 */
@DisplayName("Teste den Core-Type Schulform")
public class TestCoreTypeSchulform {

	/**
	 * Führt grundlegende Tests zu dem Core-Type Schulform aus.
	 */
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
    }

	// TODO add additional tests

}
