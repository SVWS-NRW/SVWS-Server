package de.svws_nrw.asd.types.schueler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link SchuelerStatus}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type SchuelerStatus")
class TestSchuelerStatus {

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
	 * Test des CoreTypes SchuelerStatus
	 *
	 * CoreType: SchuelerStatus
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 8
	 */
	@Test
	@DisplayName("Teste CoreType SchuelerStatus: Anzahl der vorhandenen Werte.")
	void testSchuelerStatus_AnzahlEintraege() {
		assertEquals(8, SchuelerStatus.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes SchuelerStatus
	 *
	 * CoreType: SchuelerStatus
	 * Testfall: Prüft den Text beim ersten Wert von BEURLAUBT
	 * Ergebnis: Erwarteter Wert - Beurlaubt
	 */
	@Test
	@DisplayName("Teste CoreType SchuelerStatus: Korrekter Text bei Wert BEURLAUBT.")
	void testSchuelerStatus_TextBeiBEURLAUBT() {
		assertEquals("Beurlaubt", SchuelerStatus.data().getHistorieByWert(SchuelerStatus.BEURLAUBT).getFirst().text);
	}

}
