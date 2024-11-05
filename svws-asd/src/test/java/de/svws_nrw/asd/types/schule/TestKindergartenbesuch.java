package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Kindergartenbesuch}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Kindergartenbesuch")
class TestKindergartenbesuch {

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
	 * Test des CoreTypes Kindergartenbesuch
	 *
	 * CoreType: Kindergartenbesuch
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 5
	 */
	@Test
	@DisplayName("Teste CoreType Kindergartenbesuch: Anzahl der vorhandenen Werte.")
	void testKindergartenbesuch_AnzahlEintraege() {
		assertEquals(5, Kindergartenbesuch.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes Kindergartenbesuch
	 *
	 * CoreType: Kindergartenbesuch
	 * Testfall: Prüft den Text beim ersten Wert von KEINER
	 * Ergebnis: Erwarteter Wert - "kein Kindergarten"
	 */
	@Test
	@DisplayName("Teste CoreType Kindergartenbesuch: Korrekter Text beim Wert KEINER.")
	void testKindergartenbesuch_TextBeiKEINER() {
		assertEquals("kein Kindergarten", Kindergartenbesuch.data().getHistorieByWert(Kindergartenbesuch.KEINER).getFirst().text);
	}

}
