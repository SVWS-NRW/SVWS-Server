package de.svws_nrw.asd.types.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Schulgliederung}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Schulgliederung")
class TestSchulgliederung {

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
	 * Test des CoreTypes Schulgliederung
	 *
	 * CoreType: Schulgliederung
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 92, 63
	 */
	@Test
	@DisplayName("Teste CoreType Schulgliederung: Anzahl der vorhandenen Werte.")
    void testSchulgliederung_AnzahlEintraege() {
    	assertEquals(92, Schulgliederung.data().getWerte().size());
    	assertEquals(63, Schulgliederung.data().getWerteBySchuljahr(2023).size());
    }

}
