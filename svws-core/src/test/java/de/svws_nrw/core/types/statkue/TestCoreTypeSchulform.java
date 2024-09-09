package de.svws_nrw.core.types.statkue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.types.schule.Schulform;


/**
 * Diese Klasse enthält die Testroutinen für den Core-Type Schulform.
 */
@DisplayName("Teste den Core-Type Schulform")
class TestCoreTypeSchulform {

	/**
	 * Führt grundlegende Tests zu dem Core-Type Schulform aus.
	 */
	@Test
	@DisplayName("Teste den Typ Schulform")
	void testSchulform() {
		assertTrue(Schulform.GY.daten(2024).hatGymOb);
		assertEquals(Schulform.G, Schulform.data().getWertByKuerzel("G"));
		assertEquals(Schulform.H, Schulform.data().getWertByKuerzel("H"));
		assertEquals(Schulform.V, Schulform.data().getWertByKuerzel("V"));
		assertEquals(Schulform.S, Schulform.data().getWertByKuerzel("S"));
		assertEquals(Schulform.PS, Schulform.data().getWertByKuerzel("PS"));
		assertEquals(Schulform.SK, Schulform.data().getWertByKuerzel("SK"));
		assertEquals(Schulform.R, Schulform.data().getWertByKuerzel("R"));
		assertEquals(Schulform.GE, Schulform.data().getWertByKuerzel("GE"));
		assertEquals(Schulform.GM, Schulform.data().getWertByKuerzel("GM"));
		assertEquals(Schulform.FW, Schulform.data().getWertByKuerzel("FW"));
		assertEquals(Schulform.HI, Schulform.data().getWertByKuerzel("HI"));
		assertEquals(Schulform.WF, Schulform.data().getWertByKuerzel("WF"));
		assertEquals(Schulform.GY, Schulform.data().getWertByKuerzel("GY"));
		assertEquals(Schulform.WB, Schulform.data().getWertByKuerzel("WB"));
		assertEquals(Schulform.BK, Schulform.data().getWertByKuerzel("BK"));
		assertEquals(Schulform.KS, Schulform.data().getWertByKuerzel("KS"));
		assertEquals(Schulform.SR, Schulform.data().getWertByKuerzel("SR"));
		assertEquals(Schulform.SG, Schulform.data().getWertByKuerzel("SG"));
		assertEquals(Schulform.SB, Schulform.data().getWertByKuerzel("SB"));
		assertEquals(Schulform.G, Schulform.data().getWertBySchluessel("02"));
		assertEquals(Schulform.H, Schulform.data().getWertBySchluessel("04"));
		assertEquals(Schulform.V, Schulform.data().getWertBySchluessel("06"));
		assertEquals(Schulform.S, Schulform.data().getWertBySchluessel("08"));
		assertEquals(Schulform.PS, Schulform.data().getWertBySchluessel("13"));
		assertEquals(Schulform.SK, Schulform.data().getWertBySchluessel("14"));
		assertEquals(Schulform.R, Schulform.data().getWertBySchluessel("10"));
		assertEquals(Schulform.GE, Schulform.data().getWertBySchluessel("15"));
		assertEquals(Schulform.GM, Schulform.data().getWertBySchluessel("16"));
		assertEquals(Schulform.FW, Schulform.data().getWertBySchluessel("17"));
		assertEquals(Schulform.HI, Schulform.data().getWertBySchluessel("18"));
		assertEquals(Schulform.WF, Schulform.data().getWertBySchluessel("19"));
		assertEquals(Schulform.GY, Schulform.data().getWertBySchluessel("20"));
		assertEquals(Schulform.WB, Schulform.data().getWertBySchluessel("25"));
		assertEquals(Schulform.BK, Schulform.data().getWertBySchluessel("30"));
		assertEquals(Schulform.KS, Schulform.data().getWertBySchluessel("83"));
		assertEquals(Schulform.SR, Schulform.data().getWertBySchluessel("85"));
		assertEquals(Schulform.SG, Schulform.data().getWertBySchluessel("87"));
		assertEquals(Schulform.SB, Schulform.data().getWertBySchluessel("88"));
	}

	// TODO add additional tests

}
