package de.svws_nrw.asd.types.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link BerufskollegAnlage}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type BerufskollegAnlage")
class TestBerufskollegAnlage {

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
	 * Test des CoreTypes BerufskollegAnlage
	 *
	 * CoreType: BerufskollegAnlage
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 8
	 */
	@Test
	@DisplayName("Teste CoreType BerufskollegAnlage: Anzahl der vorhandenen Werte.")
    void testBerufskollegAnlage_AnzahlEintraege() {
    	assertEquals(8, BerufskollegAnlage.data().getWerte().size());
    }

	/**
	 * Test des CoreTypes BerufskollegAnlage
	 *
	 * CoreType: BerufskollegAnlage
	 * Testfall: Prüft den Text beim Ersten Eintrag von D
	 * Ergebnis: Erwarteter Wert - "Berufliches Gymnasium und Fachoberschule"
	 */
	@Test
	@DisplayName("Teste CoreType BerufskollegAnlage: Korrekter Text beim Wert D.")
	void testBerufskollegAnlage_TextBeiD() {
		assertEquals("Berufliches Gymnasium und Fachoberschule", BerufskollegAnlage.data().getHistorieByWert(BerufskollegAnlage.D).getFirst().text);
	}

}
