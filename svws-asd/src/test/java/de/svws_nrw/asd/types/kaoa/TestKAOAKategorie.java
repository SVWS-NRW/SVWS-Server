package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link KAOAKategorie}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type KAOAKategorie")
class TestKAOAKategorie {

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
	 * Test des CoreTypes KAOAKategorie
	 *
	 * CoreType: KAOAKategorie
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 9
	 */
	@Test
	@DisplayName("Teste CoreType KAOAKategorie: Anzahl der vorhandenen Werte.")
	void testKAOAKategorie_AnzahlEintraege() {
		assertEquals(9, KAOAKategorie.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes KAOAKategorie
	 *
	 * CoreType: KAOAKategorie
	 * Testfall: Prüft den ersten eingetragenen Jahrgang in der Kategorie SBO_2
	 * Ergebnis: Erwarteter Eintrag - JAHRGANG_08
	 */
	@Test
	@DisplayName("Teste CoreType KAOAKategorie: Korrekter erster eingetragener Jahrgang bei Kategorie SBO_2.")
	void testKAOAKategorie_JahrgangBeiSBO_2() {
		assertEquals("JAHRGANG_08", KAOAKategorie.data().getHistorieByWert(KAOAKategorie.SBO_2).getFirst().jahrgaenge.getFirst());
	}

}
