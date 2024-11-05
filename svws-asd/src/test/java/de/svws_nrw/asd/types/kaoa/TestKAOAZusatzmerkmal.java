package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link KAOAZusatzmerkmal}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type KAOAZusatzmerkmal")
class TestKAOAZusatzmerkmal {

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
	 * Test des CoreTypes KAOAZusatzmerkmal
	 *
	 * CoreType: KAOAZusatzmerkmal
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 117
	 */
	@Test
	@DisplayName("Teste CoreType KAOAZusatzmerkmal: Anzahl der vorhandenen Werte.")
	void testKAOAZusatzmerkmal_AnzahlEintraege() {
		assertEquals(117, KAOAZusatzmerkmal.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes KAOAZusatzmerkmal
	 *
	 * CoreType: KAOAZusatzmerkmal
	 * Testfall: Prüft das richtige Merkmal beim ersten Eintrag von SBO_2_2_2
	 * Ergebnis: Erwarteter Wert - SBO_2_2
	 */
	@Test
	@DisplayName("Teste CoreType KAOAZusatzmerkmal: Korrektes Merkmal beim Wert SBO_2_2_2.")
	void testKAOAZusatzmerkmal_MerkmalBeiSBO_2_2_2() {
		assertEquals("SBO_2_2", KAOAZusatzmerkmal.data().getHistorieByWert(KAOAZusatzmerkmal.SBO_2_2_2).getFirst().merkmal);
	}

	/**
	 * Test des CoreTypes KAOAZusatzmerkmal
	 *
	 * CoreType: KAOAZusatzmerkmal
	 * Testfall: Prüft die richtige Optionsart beim ersten Eintrag von SBO_2_2_2
	 * Ergebnis: Erwarteter Wert - FREITEXT
	 */
	@Test
	@DisplayName("Teste CoreType KAOAZusatzmerkmal: Korrekte Optionsart beim Wert SBO_2_2_2.")
	void testKAOAZusatzmerkmal_OptionsartBeiSBO_2_2_2() {
		assertEquals("FREITEXT", KAOAZusatzmerkmal.data().getHistorieByWert(KAOAZusatzmerkmal.SBO_2_2_2).getFirst().optionsart);
	}

}
