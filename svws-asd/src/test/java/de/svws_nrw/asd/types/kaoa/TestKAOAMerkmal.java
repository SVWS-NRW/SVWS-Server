package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link KAOAMerkmal}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type KAOAMerkmal")
class TestKAOAMerkmal {

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
	 * Test des CoreTypes KAOAMerkmal
	 *
	 * CoreType: KAOAMerkmal
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 35
	 */
	@Test
	@DisplayName("Teste CoreType KAOAMerkmal: Anzahl der vorhandenen Werte.")
	void testKAOAMerkmal_AnzahlEintraege() {
		assertEquals(35, KAOAMerkmal.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes KAOAMerkmal
	 *
	 * CoreType: KAOAMerkmal
	 * Testfall: Prüft die Kategorie des ersten Eintrags des Merkmals SBO_2_1
	 * Ergebnis: Erwartete Anzahl - SBO_2
	 */
	@Test
	@DisplayName("Teste CoreType KAOAMerkmal: Korrekte Kategorie des ersten Eintrags für Merkmal SBO_2_1.")
	void testKAOAMerkmal_KategorieBeiSBO_2_1() {
		assertEquals("SBO_2", KAOAMerkmal.data().getHistorieByWert(KAOAMerkmal.SBO_2_1).getFirst().kategorie);
	}

	/**
	 * Test des CoreTypes KAOAMerkmal
	 *
	 * CoreType: KAOAMerkmal
	 * Testfall: Prüft die Optionsart des ersten Eintrags des Merkmals SBO_2_1
	 * Ergebnis: Erwartete Anzahl - KEINE
	 */
	@Test
	@DisplayName("Teste CoreType KAOAMerkmal: Korrekte Optionsart des ersten Eintrags für Merkmal SBO_2_1.")
	void testKAOAMerkmal_OptionsartBeiSBO_2_1() {
		assertEquals("KEINE", KAOAMerkmal.data().getHistorieByWert(KAOAMerkmal.SBO_2_1).getFirst().optionsart);
	}

	/**
	 * Test des CoreTypes KAOAMerkmal
	 *
	 * CoreType: KAOAMerkmal
	 * Testfall: Prüft die erste Anlage des ersten Eintrags des Merkmals SBO_2_1
	 * Ergebnis: Erwartete Anlage - A12
	 */
	@Test
	@DisplayName("Teste CoreType KAOAMerkmal: Korrekte erste Anlage des ersten Eintrags für Merkmal SBO_2_1.")
	void testKAOAMerkmal_BkanlageBeiSBO_2_1() {
		assertEquals("A12", KAOAMerkmal.data().getHistorieByWert(KAOAMerkmal.SBO_2_1).getFirst().bkAnlagen.getFirst());
	}

}
