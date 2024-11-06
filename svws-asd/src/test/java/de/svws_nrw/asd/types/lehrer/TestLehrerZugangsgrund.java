package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link LehrerZugangsgrund}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type LehrerZugangsgrund")
class TestLehrerZugangsgrund {

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
	 * Test des CoreTypes LehrerZugangsgrund
	 *
	 * CoreType: LehrerZugangsgrund
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 5
	 */
	@Test
	@DisplayName("Teste CoreType LehrerZugangsgrund: Anzahl der vorhandenen Werte.")
	void testLehrerZugangsgrund_AnzahlEintraege() {
		assertEquals(5, LehrerZugangsgrund.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes LehrerZugangsgrund
	 *
	 * CoreType: LehrerZugangsgrund
	 * Testfall: Prüft den Text beim ersten Wert von NEU
	 * Ergebnis: Erwarteter Wert - "Neueintritt in den Schuldienst mit abgelegter 2. Staatsprüfung oder anderweitig erfüllter Eingangsvoraussetzung"
	 */
	@Test
	@DisplayName("Teste CoreType LehrerZugangsgrund: Korrekter Text bei Wert NEU.")
	void testLehrerZugangsgrund_TextBeiNEU() {
		assertEquals("Neueintritt in den Schuldienst mit abgelegter 2. Staatsprüfung oder anderweitig erfüllter Eingangsvoraussetzung", LehrerZugangsgrund.data().getHistorieByWert(LehrerZugangsgrund.NEU).getFirst().text);
	}

}
