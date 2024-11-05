package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link KAOAEbene4}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type KAOAEbene4")
class TestKAOAEbene4 {

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
	 * Test des CoreTypes KAOAEbene4
	 *
	 * CoreType: KAOAEbene4
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 24
	 */
	@Test
	@DisplayName("Teste CoreType KAOAEbene4: Anzahl der vorhandenen Werte.")
	void testKAOAEbene4_AnzahlEintraege() {
		assertEquals(24, KAOAEbene4.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes KAOAEbene4
	 *
	 * CoreType: KAOAEbene4
	 * Testfall: Prüft beim ersten Eintrag von SBO_9_2_1_1 auf das Zusatzmerkmal
	 * Ergebnis: Erwarteter Wert - SBO_9_2_1
	 */
	@Test
	@DisplayName("Teste CoreType KAOAEbene4: Korrektes Zusatzmerkmal beim Wert SBO_9_2_1_1.")
	void testKAOAEbene4_ZusatzmerkmalBeiSBO_9_2_1_1() {
		assertEquals("SBO_9_2_1", KAOAEbene4.data().getHistorieByWert(KAOAEbene4.SBO_9_2_1_1).getFirst().zusatzmerkmal);
	}

}
