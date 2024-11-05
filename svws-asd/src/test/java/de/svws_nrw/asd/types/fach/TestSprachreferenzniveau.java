package de.svws_nrw.asd.types.fach;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Sprachreferenzniveau}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Sprachreferenzniveau")
class TestSprachreferenzniveau {

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
	 * Test des CoreTypes Sprachreferenzniveau
	 *
	 * CoreType: Sprachreferenzniveau
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 13
	 */
	@Test
	@DisplayName("Teste CoreType Sprachreferenzniveau: Anzahl der vorhandenen Einträge.")
	void testSprachreferenzniveau_AnzahlEintraege() {
		assertEquals(13, Sprachreferenzniveau.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes Sprachreferenzniveau
	 *
	 * CoreType: Sprachreferenzniveau
	 * Testfall: Prüft auf korrektes Kürzel im ersten Eintrag der Historie von Wert A1P
	 * Ergebnis: Erwartete Anzahl - A1+
	 */
	@Test
	@DisplayName("Teste CoreType Sprachreferenzniveau: Korrektes Kürzel für A1P")
	void testSprachreferenzniveau_KuerzelBeiA1P() {
		assertEquals("A1+", Sprachreferenzniveau.data().getHistorieByWert(Sprachreferenzniveau.A1P).getFirst().kuerzel);
	}

}
