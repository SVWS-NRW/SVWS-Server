package de.svws_nrw.asd.types.fach;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;


/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Fachgruppe}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Fachgruppe")
class TestFachgruppe {

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
	 * Test des CoreTypes Fachgruppe
	 *
	 * CoreType: Fachgruppe
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 29
	 */
	@Test
	@DisplayName("Teste CoreType Fachgruppe: Anzahl der vorhandenen Fachgruppen.")
	void testFachgruppe_AnzahlEintraege() {
		assertEquals(29, Fachgruppe.data().getWerte().size());
	}

}
