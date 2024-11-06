package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link KAOAZusatzmerkmaleOptionsarten}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type KAOAZusatzmerkmaleOptionsarten")
class TestKAOAZusatzmerkmaleOptionsarten {

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
	 * Test des CoreTypes KAOAZusatzmerkmaleOptionsarten
	 *
	 * CoreType: KAOAZusatzmerkmaleOptionsarten
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 6
	 */
	@Test
	@DisplayName("Teste CoreType KAOAZusatzmerkmaleOptionsarten: Anzahl der vorhandenen Werte.")
	void testKAOAZusatzmerkmaleOptionsarten_AnzahlEintraege() {
		assertEquals(6, KAOAZusatzmerkmaleOptionsarten.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes KAOAZusatzmerkmaleOptionsarten
	 *
	 * CoreType: KAOAZusatzmerkmaleOptionsarten
	 * Testfall: Prüft das Kürzel beim ersten Eintrag von ANSCHLUSSOPTION
	 * Ergebnis: Erwarteter Wert - Anschlussoption
	 */
	@Test
	@DisplayName("Teste CoreType KAOAZusatzmerkmaleOptionsarten: Korrektes Kürzel beim Wert ANSCHLUSSOPTION.")
	void testKAOAZusatzmerkmaleOptionsarten_KuerzelBeiANSCHLUSSOPTION() {
		assertEquals("Anschlussoption", KAOAZusatzmerkmaleOptionsarten.data().getHistorieByWert(KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION).getFirst().kuerzel);
	}

}
