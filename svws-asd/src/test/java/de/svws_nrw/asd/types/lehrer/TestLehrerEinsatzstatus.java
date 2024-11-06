package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link LehrerEinsatzstatus}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type LehrerEinsatzstatus")
class TestLehrerEinsatzstatus {

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
	 * Test des CoreTypes LehrerEinsatzstatus
	 *
	 * CoreType: LehrerEinsatzstatus
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 3
	 */
	@Test
	@DisplayName("Teste CoreType LehrerEinsatzstatus: Anzahl der vorhandenen Werte.")
	void testLehrerEinsatzstatus_AnzahlEintraege() {
		assertEquals(3, LehrerEinsatzstatus.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes LehrerEinsatzstatus
	 *
	 * CoreType: LehrerEinsatzstatus
	 * Testfall: Prüft den Text beim ersten Wert von A
	 * Ergebnis: Erwarteter Wert - "Stammschule, ganz oder teilweise auch an anderen Schulen tätig"
	 */
	@Test
	@DisplayName("Teste CoreType LehrerEinsatzstatus: Korrekter Text bei Wert A.")
	void testLehrerEinsatzstatus_TextBeiA() {
		assertEquals("Stammschule, ganz oder teilweise auch an anderen Schulen tätig", LehrerEinsatzstatus.data().getHistorieByWert(LehrerEinsatzstatus.A).getFirst().text);
	}

}
