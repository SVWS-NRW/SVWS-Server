package de.svws_nrw.core.types.statkue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.types.schule.Religion;


/**
 * Diese Klasse enthält die Testroutinen für den Core-Type Religion.
 */
@DisplayName("Teste den Core-Type Religion")
class TestCoreTypeReligion {

	/**
	 * Führt grundlegende Tests zu dem Core-Type Religion aus.
	 */
	@Test
	@DisplayName("Teste den Typ Religion")
	void testReligion() {
		assertEquals(Religion.AR, Religion.data().getWertByKuerzel("AR"));
		assertEquals(Religion.ER, Religion.data().getWertByKuerzel("ER"));
		assertEquals(Religion.HR, Religion.data().getWertByKuerzel("HR"));
		assertEquals(Religion.IR, Religion.data().getWertByKuerzel("IR"));
		assertEquals(Religion.KR, Religion.data().getWertByKuerzel("KR"));
		assertEquals(Religion.ME, Religion.data().getWertByKuerzel("ME"));
		assertEquals(Religion.OH, Religion.data().getWertByKuerzel("OH"));
		assertEquals(Religion.OR, Religion.data().getWertByKuerzel("OR"));
		assertEquals(Religion.SO, Religion.data().getWertByKuerzel("SO"));
		assertEquals(Religion.XO, Religion.data().getWertByKuerzel("XO"));
		assertEquals(Religion.XR, Religion.data().getWertByKuerzel("XR"));

		assertEquals("alevitisch", Religion.AR.daten(2024).text);
		assertEquals("evangelisch", Religion.ER.daten(2024).text);
		assertEquals("jüdisch", Religion.HR.daten(2024).text);
		assertEquals("islamisch", Religion.IR.daten(2024).text);
		assertEquals("katholisch", Religion.KR.daten(2024).text);
		assertEquals("mennonitische BG NRW", Religion.ME.daten(2024).text);
		assertEquals("ohne Bekenntnis", Religion.OH.daten(2024).text);
		assertEquals("griechisch-orthodox", Religion.OR.daten(2024).text);
		assertEquals("syrisch-orthodox", Religion.SO.daten(2024).text);
		assertEquals("sonstige orthodoxe", Religion.XO.daten(2024).text);
		assertEquals("andere Religionen", Religion.XR.daten(2024).text);
	}

	// TODO add additional tests

}
