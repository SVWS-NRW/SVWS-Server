package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Foerderschwerpunkt}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Foerderschwerpunkt")
class TestFoerderschwerpunkt {

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
	 * Test des CoreTypes Foerderschwerpunkt
	 *
	 * CoreType: Foerderschwerpunkt
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 16
	 */
	@Test
	@DisplayName("Teste CoreType Note: Anzahl der vorhandenen Werte.")
	void testFoerderschwerpunkt_AnzahlEintraege() {
		assertEquals(16, Foerderschwerpunkt.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes Foerderschwerpunkt
	 *
	 * CoreType: Foerderschwerpunkt
	 * Testfall: Prüft den Text beim ersten Wert von KB
	 * Ergebnis: Erwarteter Wert - "Körperliche und motorische Entwicklung"
	 */
	@Test
	@DisplayName("Teste CoreType Note: Korrekter Text beim Wert KB.")
	void testFoerderschwerpunkt_TextBeiKB() {
		assertEquals("Körperliche und motorische Entwicklung", Foerderschwerpunkt.data().getHistorieByWert(Foerderschwerpunkt.KB).getFirst().text);
	}

}
