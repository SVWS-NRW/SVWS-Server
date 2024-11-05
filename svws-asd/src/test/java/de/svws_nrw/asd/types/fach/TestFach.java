package de.svws_nrw.asd.types.fach;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Fach}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Fach")
class TestFach {

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
	 * Test des CoreTypes Fach
	 *
	 * CoreType: Fach
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 292
	 */
	@Test
	@DisplayName("Teste CoreType Fach: Anzahl der vorhandenen Fächer.")
	void testFach_AnzahlEintraege() {
		assertEquals(292, Fach.data().getWerte().size());
	}

}
