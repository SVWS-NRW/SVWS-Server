package de.svws_nrw.asd.types.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link BerufskollegBerufsebene3}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type BerufskollegBerufsebene3")
class TestBerufskollegBerufsebene3 {

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
	 * Test des CoreTypes BerufskollegBerufsebene3
	 *
	 * CoreType: BerufskollegBerufsebene3
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 76
	 */
	@Test
	@DisplayName("Teste CoreType BerufskollegBerufsebene3: Anzahl der vorhandenen Werte.")
	void testBerufskollegBerufsebene3_AnzahlEintraege() {
		assertEquals(76, BerufskollegBerufsebene3.data().getWerte().size());
	}


}
