package de.svws_nrw.asd.types.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Religion}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Religion")
class TestReligion {

	/**
	 * Initialisiert die Core-Types, damit die Tests ausgeführt werden können.
	 * Beim Laden der Core-Type-Daten werden die JSON-Dateien auf Plausibilität
	 * geprüft.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}


	/**
	 * Test des CoreTypes Religion
	 *
	 * CoreType: Religion
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 11
	 */
	@Test
	@DisplayName("Teste CoreType Religion: Anzahl der vorhandenen Werte.")
	void testReligion_AnzahlEintraege() {
		assertEquals(11, Religion.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes Religion
	 *
	 * CoreType: Religion
	 * Testfall: Prüft den Text beim ersten Wert von ER
	 * Ergebnis: Erwarteter Wert - evangelisch
	 */
	@Test
	@DisplayName("Teste CoreType Religion: Korrekter Text beim Wert von ER.")
	void testReligion_TextBeiER() {
		assertEquals("evangelisch", Religion.data().getHistorieByWert(Religion.ER).getFirst().text);
	}

}
