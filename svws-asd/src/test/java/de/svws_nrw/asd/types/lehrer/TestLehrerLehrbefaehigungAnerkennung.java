package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link LehrerLehrbefaehigungAnerkennung}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type LehrerLehrbefaehigungAnerkennung")
class TestLehrerLehrbefaehigungAnerkennung {

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
	 * Test des CoreTypes LehrerLehrbefaehigungAnerkennung
	 *
	 * CoreType: LehrerLehrbefaehigungAnerkennung
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 4
	 */
	@Test
	@DisplayName("Teste CoreType LehrerLehrbefaehigungAnerkennung: Anzahl der vorhandenen Werte.")
	void testLehrerLehrbefaehigungAnerkennung_AnzahlEintraege() {
		assertEquals(4, LehrerLehrbefaehigungAnerkennung.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes LehrerLehrbefaehigungAnerkennung
	 *
	 * CoreType: LehrerLehrbefaehigungAnerkennung
	 * Testfall: Prüft den Text bei ersten Wert von ID_2
	 * Ergebnis: Erwarteter Wert - "Unterrichtserlaubnis (z. B. Zertifikatskurs)"
	 */
	@Test
	@DisplayName("Teste CoreType LehrerLehrbefaehigungAnerkennung: Korrekter Text bei Wert ID_2.")
	void testLehrerLehrbefaehigungAnerkennung_TextBeiID_2() {
		assertEquals("Unterrichtserlaubnis (z. B. Zertifikatskurs)", LehrerLehrbefaehigungAnerkennung.data().getHistorieByWert(LehrerLehrbefaehigungAnerkennung.ID_2).getFirst().text);
	}

}
