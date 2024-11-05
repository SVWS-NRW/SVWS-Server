package de.svws_nrw.asd.types.fach;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link BilingualeSprache}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type BilingualeSprache")
class TestBilingualeSprache {

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
	 * Test des CoreTypes BilingualeSprache
	 *
	 * CoreType: BilingualeSprache
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 7
	 */
	@Test
	@DisplayName("Teste CoreType BilingualeSprache: Prüfe die Anzahl der möglichen Werte für BilingualeSprache.")
	void testBilingualeSprache_AnzahlEintraege() {
		assertEquals(7, BilingualeSprache.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes BilingualeSprache
	 *
	 * CoreType: BilingualeSprache
	 * Testfall: Prüft den gültigBis Wert beim ersten Eintrag für Englisch
	 * Ergebnis: Erwartetes Jahr - 2022
	 */
	@Test
	@DisplayName("Teste CoreType BilingualeSprache: Korrekter gueltigBis Wert für ersten Wert von Englisch")
	void testBilingualeSprache_GueltigBisBeiENGLISCH() {
		assertEquals(2022, BilingualeSprache.data().getHistorieByWert(BilingualeSprache.ENGLISCH).getFirst().gueltigBis);
	}

}
