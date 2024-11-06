package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link WeiterbildungskollegBildungsgangTyp}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type WeiterbildungskollegBildungsgangTyp")
class TestWeiterbildungskollegBildungsgangTyp {

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
	 * Test des CoreTypes WeiterbildungskollegBildungsgangTyp
	 *
	 * CoreType: WeiterbildungskollegBildungsgangTyp
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 3
	 */
	@Test
	@DisplayName("Teste CoreType WeiterbildungskollegBildungsgangTyp: Anzahl der vorhandenen Werte.")
	void testWeiterbildungskollegBildungsgangTyp_AnzahlEintraege() {
		assertEquals(3, WeiterbildungskollegBildungsgangTyp.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes WeiterbildungskollegBildungsgangTyp
	 *
	 * CoreType: WeiterbildungskollegBildungsgangTyp
	 * Testfall: Prüft den Text beim ersten Wert von ABENDGYMNASIUM
	 * Ergebnis: Erwarteter Wert - Abendgymnasium
	 */
	@Test
	@DisplayName("Teste CoreType WeiterbildungskollegBildungsgangTyp: Korrekter Text beim Wert ABENDGYMNASIUM.")
	void testWeiterbildungskollegBildungsgangTyp_TextBeiABENDGYMNASIUM() {
		assertEquals("Abendgymnasium", WeiterbildungskollegBildungsgangTyp.data().getHistorieByWert(WeiterbildungskollegBildungsgangTyp.ABENDGYMNASIUM).getFirst().text);
	}

}
