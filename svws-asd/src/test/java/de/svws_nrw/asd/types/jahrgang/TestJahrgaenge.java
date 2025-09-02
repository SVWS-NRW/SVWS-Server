package de.svws_nrw.asd.types.jahrgang;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Jahrgaenge}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Jahrgaenge")
class TestJahrgaenge {

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
	 * Test des CoreTypes Jahrgaenge
	 *
	 * CoreType: Jahrgaenge
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 34
	 */
	@Test
	@DisplayName("Teste CoreType Jahrgaenge: Anzahl der vorhandenen Werte.")
	void testJahrgaenge_AnzahlEintraege() {
		assertEquals(34, Jahrgaenge.data().getWerte().size());
	}

}
