package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link LehrerLehramtAnerkennung}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type LehrerLehramtAnerkennung")
class TestLehrerLehramtAnerkennung {

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
	 * Test des CoreTypes LehrerLehramtAnerkennung
	 *
	 * CoreType: LehrerLehramtAnerkennung
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 5
	 */
	@Test
	@DisplayName("Teste CoreType LehrerLehramtAnerkennung: Anzahl der vorhandenen Werte.")
	void testLehrerLehramtAnerkennung_AnzahlEintraege() {
		assertEquals(5, LehrerLehramtAnerkennung.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes LehrerLehramtAnerkennung
	 *
	 * CoreType: LehrerLehramtAnerkennung
	 * Testfall: Prüft Text beim ersten Wert von AP
	 * Ergebnis: Erwarteter Wert - "Anerkennung geeignete Prüfung"
	 */
	@Test
	@DisplayName("Teste CoreType LehrerLehramtAnerkennung: Korrekter Text bei Wert AP.")
	void testLehrerLehramtAnerkennung_TextBeiAP() {
		assertEquals("Anerkennung geeignete Prüfung", LehrerLehramtAnerkennung.data().getHistorieByWert(LehrerLehramtAnerkennung.AP).getFirst().text);
	}

}
