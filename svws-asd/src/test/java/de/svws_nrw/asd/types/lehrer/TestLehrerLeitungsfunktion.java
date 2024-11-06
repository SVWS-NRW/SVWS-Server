package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link LehrerLeitungsfunktion}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type LehrerLeitungsfunktion")
class TestLehrerLeitungsfunktion {

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
	 * Test des CoreTypes LehrerLeitungsfunktion
	 *
	 * CoreType: LehrerLeitungsfunktion
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 2
	 */
	@Test
	@DisplayName("Teste CoreType LehrerLeitungsfunktion: Anzahl der vorhandenen Werte.")
	void testLehrerLeitungsfunktion_AnzahlEintraege() {
		assertEquals(2, LehrerLeitungsfunktion.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes LehrerLeitungsfunktion
	 *
	 * CoreType: LehrerLeitungsfunktion
	 * Testfall: Prüft den Text beim ersten Wert von SL
	 * Ergebnis: Erwarteter Wert - Schulleitung
	 */
	@Test
	@DisplayName("Teste CoreType LehrerLeitungsfunktion: Korrekter Text beim Wert SL.")
	void testLehrerLeitungsfunktion_TextBeiSL() {
		assertEquals("Schulleitung", LehrerLeitungsfunktion.data().getHistorieByWert(LehrerLeitungsfunktion.SL).getFirst().text);
	}

}
