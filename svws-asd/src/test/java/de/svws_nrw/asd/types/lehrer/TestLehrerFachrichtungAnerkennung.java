package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link LehrerFachrichtungAnerkennung}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type LehrerFachrichtungAnerkennung")
class TestLehrerFachrichtungAnerkennung {

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
	 * Test des CoreTypes LehrerFachrichtungAnerkennung
	 *
	 * CoreType: LehrerFachrichtungAnerkennung
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 4
	 */
	@Test
	@DisplayName("Teste CoreType LehrerFachrichtungAnerkennung: Anzahl der vorhandenen Werte.")
	void testLehrerFachrichtungAnerkennung_AnzahlEintraege() {
		assertEquals(4, LehrerFachrichtungAnerkennung.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes LehrerFachrichtungAnerkennung
	 *
	 * CoreType: LehrerFachrichtungAnerkennung
	 * Testfall: Prüft den Text beim ersten Wert von ID7
	 * Ergebnis: Erwarteter Wert - sonstige
	 */
	@Test
	@DisplayName("Teste CoreType LehrerFachrichtungAnerkennung: Korrekter Text bei Wert ID7.")
	void testLehrerFachrichtungAnerkennung_TextBeiID7() {
		assertEquals("sonstige", LehrerFachrichtungAnerkennung.data().getHistorieByWert(LehrerFachrichtungAnerkennung.ID7).getFirst().text);
	}

}
