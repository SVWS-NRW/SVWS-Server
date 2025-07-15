package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link LehrerBeschaeftigungsart}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type LehrerBeschaeftigungsart")
class TestLehrerBeschaeftigungsart {

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
	 * Test des CoreTypes LehrerBeschaeftigungsart
	 *
	 * CoreType: LehrerBeschaeftigungsart
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 14
	 */
	@Test
	@DisplayName("Teste CoreType LehrerBeschaeftigungsart: Anzahl der vorhandenen Werte.")
	void testLehrerBeschaeftigungsart_AnzahlEintraege() {
		assertEquals(14, LehrerBeschaeftigungsart.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes LehrerBeschaeftigungsart
	 *
	 * CoreType: LehrerBeschaeftigungsart
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwarteter Wert - "Nebenberufliche Beschäftigung"
	 */
	@Test
	@DisplayName("Teste CoreType LehrerBeschaeftigungsart: Korrekter Text bei Wert SB.")
	void testLehrerBeschaeftigungsart_TextBeiSb() {
		assertEquals("Angestellte, nebenberuflich", LehrerBeschaeftigungsart.data().getHistorieByWert(LehrerBeschaeftigungsart.SB).getFirst().text);
	}
}
