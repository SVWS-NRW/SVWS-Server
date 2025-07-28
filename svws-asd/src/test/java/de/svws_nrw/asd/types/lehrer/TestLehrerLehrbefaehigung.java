package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link LehrerLehrbefaehigung}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type LehrerLehrbefaehigung")
class TestLehrerLehrbefaehigung {

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
	 * Test des CoreTypes LehrerLehrbefaehigung
	 *
	 * CoreType: LehrerLehrbefaehigung
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 121
	 */
	@Test
	@DisplayName("Teste CoreType LehrerLehrbefaehigung: Anzahl der vorhandenen Werte.")
	void testLehrerLehrbefaehigung_AnzahlEintraege() {
		assertEquals(121, LehrerLehrbefaehigung.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes LehrerLehrbefaehigung
	 *
	 * CoreType: LehrerLehrbefaehigung
	 * Testfall: Prüft den Text beim ersten Wert von AL
	 * Ergebnis: Erwarteter Wert - Arbeitslehre
	 */
	@Test
	@DisplayName("Teste CoreType LehrerLehrbefaehigung: Korrekter Text bei AL.")
	void testLehrerLehrbefaehigung_TextBeiAL() {
		assertEquals("Arbeitslehre", LehrerLehrbefaehigung.data().getHistorieByWert(LehrerLehrbefaehigung.AL).getFirst().text);
	}

}
