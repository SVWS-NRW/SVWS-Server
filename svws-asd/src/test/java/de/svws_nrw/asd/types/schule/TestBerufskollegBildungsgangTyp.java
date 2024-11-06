package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link BerufskollegBildungsgangTyp}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type BerufskollegBildungsgangTyp")
class TestBerufskollegBildungsgangTyp {

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
	 * Test des CoreTypes BerufskollegBildungsgangTyp
	 *
	 * CoreType: BerufskollegBildungsgangTyp
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 5
	 */
	@Test
	@DisplayName("Teste CoreType BerufskollegBildungsgangTyp: Anzahl der vorhandenen Werte.")
	void testBerufskollegBildungsgangTyp() {
		assertEquals(5, BerufskollegBildungsgangTyp.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes BerufskollegBildungsgangTyp
	 *
	 * CoreType: BerufskollegBildungsgangTyp
	 * Testfall: Prüft den Text beim ersten Wert von BERUFSSCHULE
	 * Ergebnis: Erwarteter Wert - Berufsschule
	 */
	@Test
	@DisplayName("Teste CoreType BerufskollegBildungsgangTyp: Korrekter Text beim Wert BERUFSSCHULE.")
	void testBerufskollegBildungsgangTyp_TextBeiBERUFSSCHULE() {
		assertEquals("Berufsschule", BerufskollegBildungsgangTyp.data().getHistorieByWert(BerufskollegBildungsgangTyp.BERUFSSCHULE).getFirst().text);
	}

}
