package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link KAOAAnschlussoptionen}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type KAOAAnschlussoptionen")
class TestKAOAAnschlussoptionen {

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
	 * Test des CoreTypes KAOAAnschlussoptionen
	 *
	 * CoreType: KAOAAnschlussoptionen
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 37
	 */
	@Test
	@DisplayName("Teste CoreType KAOAAnschlussoptionen: Anzahl der vorhandenen Werte.")
	void testKAOAAnschlussoptionen() {
		assertEquals(37, KAOAAnschlussoptionen.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes KAOAAnschlussoptionen
	 *
	 * CoreType: KAOAAnschlussoptionen
	 * Testfall: Prüft das erste Zusatzmerkmal des ersten Eintrags für die Anschlussoption BBA
	 * Ergebnis: Erwartets Merkmal - SBO_10_7_1
	 */
	@Test
	@DisplayName("Teste CoreType KAOAAnschlussoptionen: Korrektes erstes Zusatzmerkmal für ersten Eintrag der Option BBA.")
	void testKAOAAnschlussoptionenAnzeigeZusatzmerkmal() {
		assertEquals("SBO_10_7_1", KAOAAnschlussoptionen.data().getHistorieByWert(KAOAAnschlussoptionen.BBA).getFirst().anzeigeZusatzmerkmal.getFirst());
	}


	/**
	 * Test des CoreTypes KAOAAnschlussoptionen
	 *
	 * CoreType: KAOAAnschlussoptionen
	 * Testfall: Prüft die erste Stufe des ersten Eintrags für die Anschlussoption BBA
	 * Ergebnis: Erwartets Merkmal - SEKUNDARSTUFE_I
	 */
	@Test
	@DisplayName("Teste CoreType KAOAAnschlussoptionen: Korrekte erste Stufe für ersten Eintrag der Option BBA.")
	void testKAOAAnschlussoptionenStufen() {
		assertEquals("SEKUNDARSTUFE_I", KAOAAnschlussoptionen.data().getHistorieByWert(KAOAAnschlussoptionen.BBA).getFirst().stufen.getFirst());
	}

}
