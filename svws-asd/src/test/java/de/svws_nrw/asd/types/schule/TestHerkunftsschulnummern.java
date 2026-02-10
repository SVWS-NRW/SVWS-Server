package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Herkunftsschulnummer}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Herkunftsschulnummern")
class TestHerkunftsschulnummern {

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
	 * Test des CoreTypes TestHerkunftsschulnummern
	 *
	 * CoreType: TestHerkunftsschulnummern
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 21
	 */
	@Test
	@DisplayName("Teste CoreType Herkunftsschulnummern: Anzahl der vorhandenen Werte.")
	void testHerkunftsschulnummern_AnzahlEintraege() {
		assertEquals(21, Herkunftsschulnummer.data().getWerte().size());
	}

}
