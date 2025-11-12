package de.svws_nrw.asd.types.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link BerufskollegBerufsebene1}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type BerufskollegBerufsebene1")
class TestBerufskollegBerufsebene1 {

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
	 * Test des CoreTypes BerufskollegBerufsebene1
	 *
	 * CoreType: BerufskollegBerufsebene1
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 18
	 */
	@Test
	@DisplayName("Teste CoreType BerufskollegBerufsebene1: Anzahl der vorhandenen Werte.")
	void testBerufskollegBerufsebene1_AnzahlEintraege() {
		assertEquals(18, BerufskollegBerufsebene1.data().getWerte().size());
	}


}
