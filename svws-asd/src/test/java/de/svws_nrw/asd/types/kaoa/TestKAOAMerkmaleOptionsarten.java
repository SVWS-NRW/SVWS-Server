package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link KAOAMerkmaleOptionsarten}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type KAOAMerkmaleOptionsarten")
class TestKAOAMerkmaleOptionsarten {

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
	 * Test des CoreTypes KAOAMerkmaleOptionsarten
	 *
	 * CoreType: KAOAMerkmaleOptionsarten
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 1
	 */
	@Test
	@DisplayName("Teste CoreType KAOAMerkmaleOptionsarten: Anzahl der vorhandenen Werte.")
	void testKAOAMerkmaleOptionsarten_AnzahlEintraege() {
		assertEquals(1, KAOAMerkmaleOptionsarten.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes KAOAMerkmaleOptionsarten
	 *
	 * CoreType: KAOAMerkmaleOptionsarten
	 * Testfall: Prüft den Text des ersten Eintrags der Optionsart KEINE
	 * Ergebnis: Erwarteter Text - keine
	 */
	@Test
	@DisplayName("Teste CoreType KAOAMerkmaleOptionsarten: Korrekter Text beim Wert KEINE.")
	void testKAOAMerkmaleOptionsarten_TextBeiKEINE() {
		assertEquals("keine", KAOAMerkmaleOptionsarten.data().getHistorieByWert(KAOAMerkmaleOptionsarten.KEINE).getFirst().text);
	}

}
