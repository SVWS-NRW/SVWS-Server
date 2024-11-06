package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link KAOABerufsfeld}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type KAOABerufsfeld")
class TestKAOABerufsfeld {

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
	 * Test des CoreTypes KAOABerufsfeld
	 *
	 * CoreType: KAOABerufsfeld
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 16
	 */
	@Test
	@DisplayName("Teste CoreType KAOABerufsfeld: Anzahl der vorhandenen Werte.")
	void testKAOABerufsfeld_AnzahlEintraege() {
		assertEquals(16, KAOABerufsfeld.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes KAOABerufsfeld
	 *
	 * CoreType: KAOABerufsfeld
	 * Testfall: Prüft die richtige Bezeichnung beim Eintrag WIVE
	 * Ergebnis: Erwarteter Wert - "Wirtschaft, Verwaltung"
	 */
	@Test
	@DisplayName("Teste CoreType KAOABerufsfeld: Korrekter Text beim Eintrag WIVE")
	void testKAOABerufsfeld_TextBeiWive() {
		assertEquals("Wirtschaft, Verwaltung", KAOABerufsfeld.data().getHistorieByWert(KAOABerufsfeld.WIVE).getFirst().text);
	}

}
