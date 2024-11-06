package de.svws_nrw.asd.types.schueler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link HerkunftBildungsgang}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type HerkunftBildungsgang")
class TestHerkunftBildungsgang {

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
	 * Test des CoreTypes HerkunftBildungsgang
	 *
	 * CoreType: HerkunftBildungsgang
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 41
	 */
	@Test
	@DisplayName("Teste CoreType HerkunftBildungsgang: Anzahl der vorhandenen Werte.")
	void testHerkunftBildungsgang_AnzahlEintraege() {
		assertEquals(41, HerkunftBildungsgang.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes HerkunftBildungsgang
	 *
	 * CoreType: HerkunftBildungsgang
	 * Testfall: Prüft den Text beim ersten Wert von E07
	 * Ergebnis: Erwarteter Wert - "Fachschule für Sozialwesen (mit Berufspraktikum/6-jährig, Teilzeit)"
	 */
	@Test
	@DisplayName("Teste CoreType HerkunftBildungsgang: Korrekter Text beim Wert E07.")
	void testHerkunftBildungsgang_TextBeiE07() {
		assertEquals("Fachschule für Sozialwesen (mit Berufspraktikum/6-jährig, Teilzeit)", HerkunftBildungsgang.data().getHistorieByWert(HerkunftBildungsgang.E07).getFirst().text);
	}

}
