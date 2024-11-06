package de.svws_nrw.asd.types.kurse;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link ZulaessigeKursart}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type ZulaessigeKursart")
class TestZulaessigeKursart {

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
	 * Test des CoreTypes ZulaessigeKursart
	 *
	 * CoreType: ZulaessigeKursart
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 74
	 */
	@Test
	@DisplayName("Teste CoreType ZulaessigeKursart: Anzahl der vorhandenen Werte.")
	void testZulaessigeKursart_AnzahlEintraege() {
		assertEquals(74, ZulaessigeKursart.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes ZulaessigeKursart
	 *
	 * CoreType: ZulaessigeKursart
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwarteter Wert - "3. Abiturfach"
	 */
	@Test
	@DisplayName("Teste CoreType ZulaessigeKursart: Korrekter Text bei AB3.")
	void testZulaessigeKursart_TextBeiAB3() {
		assertEquals("3. Abiturfach", ZulaessigeKursart.data().getHistorieByWert(ZulaessigeKursart.AB3).getFirst().text);
	}

}
