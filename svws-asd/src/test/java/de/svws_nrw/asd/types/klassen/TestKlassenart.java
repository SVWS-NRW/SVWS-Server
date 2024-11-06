package de.svws_nrw.asd.types.klassen;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Klassenart}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Klassenart")
class TestKlassenart {

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
	 * Test des CoreTypes Klassenart
	 *
	 * CoreType: Klassenart
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 10
	 */
	@Test
	@DisplayName("Teste CoreType Klassenart: Anzahl der vorhandenen Werte.")
	void testKlassenart_AnzahlEintraege() {
		assertEquals(10, Klassenart.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes Klassenart
	 *
	 * CoreType: Klassenart
	 * Testfall: Prüft die richtige Bezeichnung des ersten Wertes von HA_1A
	 * Ergebnis: Erwartetr Wert - "Klasse 10 Typ A (Hauptschule)"
	 */
	@Test
	@DisplayName("Teste CoreType Klassenart: Korrekter Text bei HA_1A.")
	void testKlassenart_TextBeiHA_1A() {
		assertEquals("Klasse 10 Typ A (Hauptschule)", Klassenart.data().getHistorieByWert(Klassenart.HA_1A).getFirst().text);
	}

}
