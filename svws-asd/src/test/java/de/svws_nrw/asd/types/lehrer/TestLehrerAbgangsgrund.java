package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link LehrerAbgangsgrund}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type LehrerAbgangsgrund")
class TestLehrerAbgangsgrund {

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
	 * Test des CoreTypes LehrerAbgangsgrund
	 *
	 * CoreType: LehrerAbgangsgrund
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 7
	 */
	@Test
	@DisplayName("Teste CoreType LehrerAbgangsgrund: Anzahl der vorhandenen Werte.")
	void testLehrerAbgangsgrund_AnzahlEintraege() {
		assertEquals(7, LehrerAbgangsgrund.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes LehrerAbgangsgrund
	 *
	 * CoreType: LehrerAbgangsgrund
	 * Testfall: Prüft den Text für den ersten Wert von WECHSEL
	 * Ergebnis: Erwartete Anzahl - "Wechsel innerhalb des Landes von der berichtenden Schule an eine andere Schule"
	 */
	@Test
	@DisplayName("Teste CoreType LehrerAbgangsgrund: Korrekter Text beim Wert WECHSEL.")
	void testLehrerAbgangsgrund_TextBeiWECHSEL() {
		assertEquals("Wechsel innerhalb des Landes von der berichtenden Schule an eine andere Schule", LehrerAbgangsgrund.data().getHistorieByWert(LehrerAbgangsgrund.WECHSEL).getFirst().text);
	}

}
