package de.svws_nrw.asd.types.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link LehrerRechtsverhaeltnis}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type LehrerRechtsverhaeltnis")
class TestLehrerRechtsverhaeltnis {

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
	 * Test des CoreTypes LehrerRechtsverhaeltnis
	 *
	 * CoreType: LehrerRechtsverhaeltnis
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 10
	 */
	@Test
	@DisplayName("Teste CoreType LehrerRechtsverhaeltnis: Anzahl der vorhandenen Werte.")
	void testLehrerRechtsverhaeltnis_AnzahlEintraege() {
		assertEquals(10, LehrerRechtsverhaeltnis.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes LehrerRechtsverhaeltnis
	 *
	 * CoreType: LehrerRechtsverhaeltnis
	 * Testfall: Prüft den Text beim ersten Wert von P
	 * Ergebnis: Erwarteter Wert - "Beamter auf Probe"
	 */
	@Test
	@DisplayName("Teste CoreType LehrerRechtsverhaeltnis: Korrekter Text bei Wert P.")
	void testLehrerRechtsverhaeltnis_NameBeiP() {
		assertEquals("Beamter auf Probe", LehrerRechtsverhaeltnis.data().getHistorieByWert(LehrerRechtsverhaeltnis.P).getFirst().text);
	}

}
