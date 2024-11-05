package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link LehrerLehramt}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type LehrerLehramt")
class TestLehrerLehramt {

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
	 * Test des CoreTypes LehrerLehramt
	 *
	 * CoreType: LehrerLehramt
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 50
	 */
	@Test
	@DisplayName("Teste CoreType LehrerLehramt: Anzahl der vorhandenen Werte.")
	void testLehrerLehramt_AnzahlEintraege() {
		assertEquals(50, LehrerLehramt.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes LehrerLehramt
	 *
	 * CoreType: LehrerLehramt
	 * Testfall: Prüft den Text beim ersten Wert von ID_01
	 * Ergebnis: Erwarteter Wert - "an der Grund- und Hauptschule (Stufenschwerpunkt I)"
	 */
	@Test
	@DisplayName("Teste CoreType LehrerLehramt: Korrekter Text beim Wert ID_01.")
	void testLehrerLehramt_TextBeiID_01() {
		assertEquals("an der Grund- und Hauptschule (Stufenschwerpunkt I)", LehrerLehramt.data().getHistorieByWert(LehrerLehramt.ID_01).getFirst().text);
	}

}
