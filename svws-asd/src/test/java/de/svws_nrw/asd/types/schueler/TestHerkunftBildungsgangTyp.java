package de.svws_nrw.asd.types.schueler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link HerkunftBildungsgangTyp}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type HerkunftBildungsgangTyp")
class TestHerkunftBildungsgangTyp {

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
	 * Test des CoreTypes HerkunftBildungsgangTyp
	 *
	 * CoreType: HerkunftBildungsgangTyp
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 8
	 */
	@Test
	@DisplayName("Teste CoreType HerkunftBildungsgangTyp: Anzahl der vorhandenen Werte.")
	void testHerkunftBildungsgangTyp_AnzahlEintraege() {
		assertEquals(8, HerkunftBildungsgangTyp.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes HerkunftBildungsgangTyp
	 *
	 * CoreType: HerkunftBildungsgangTyp
	 * Testfall: Prüft den Text beim ersten Wert von KL
	 * Ergebnis: Erwarteter Wert - Kolleg
	 */
	@Test
	@DisplayName("Teste CoreType HerkunftBildungsgangTyp: Korrekter Text beim Wert KL.")
	void testHerkunftBildungsgangTyp_TextBeiKL() {
		assertEquals("KOLLEG", HerkunftBildungsgangTyp.data().getHistorieByWert(HerkunftBildungsgangTyp.KL).getFirst().text);
	}

}
