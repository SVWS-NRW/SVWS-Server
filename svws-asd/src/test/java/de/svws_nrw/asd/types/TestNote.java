package de.svws_nrw.asd.types;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für den Core-Type {@link Note}
 * zur Verfügung.
 */
@DisplayName("Teste den Core-Type Note")
class TestNote {

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
	 * Test des CoreTypes Note
	 *
	 * CoreType: Note
	 * Testfall: Prüft die Anzahl der möglichen Werte
	 * Ergebnis: Erwartete Anzahl - 26
	 */
	@Test
	@DisplayName("Teste CoreType Note: Anzahl der vorhandenen Werte.")
	void testNote_AnzahlEintraege() {
		assertEquals(26, Note.data().getWerte().size());
	}

	/**
	 * Test des CoreTypes Note
	 *
	 * CoreType: Note
	 * Testfall: Prüft auf korrekten Notentext von MANGELHAFT
	 * Ergebnis: Erwarteter Wert - mangelhaft
	 */
	@Test
	@DisplayName("Teste CoreType Note: Korrekter Zeugnistext für mangelhaft")
	void testNote_ZeugnistextBeiMANGELHAFT() {
		assertEquals("mangelhaft", Note.data().getHistorieByWert(Note.MANGELHAFT).getFirst().textZeugnis);
	}

	/**
	 * Test des CoreTypes Note
	 *
	 * CoreType: Note
	 * Testfall: Prüft auf korrekten Sortierwert bei Note ausreichend
	 * Ergebnis: Erwarteter Wert - 400
	 */
	@Test
	@DisplayName("Teste CoreType Note: Korrekter Sortierwert für ausreichend")
	void testNote_WertSortierungBeiAUSREICHEND() {
		assertEquals(400, Note.data().getHistorieByWert(Note.AUSREICHEND).getFirst().sortierung);
	}

	/**
	 * Test des CoreTypes Note
	 *
	 * CoreType: Note
	 * Testfall: Prüft auf korrekten Punktwert der Note befriedigend
	 * Ergebnis: Erwarteter Wert - 8
	 */
	@Test
	@DisplayName("Teste CoreType Note: Korrekter Wert für Notenpunkte bei befridigend")
	void testNote_NotenpunkteBeiBEFRIEDIGEND() {
		assertEquals(8, Note.data().getHistorieByWert(Note.BEFRIEDIGEND).getFirst().notenpunkte);
	}

	/**
	 * Test des CoreTypes Note
	 *
	 * CoreType: Note
	 * Testfall: Prüft auf nicht gesetzte Tendenz bei Punktwert 11
	 * Ergebnis: Erwarteter Wert - false
	 */
	@Test
	@DisplayName("Teste CoreType Note: Eintrag für Punktwert 11 hat keine Tendenz")
	void testNote_hatTendenz() {
		assertEquals(false, Note.fromNotenpunkte(11).hatTendenz(0));
	}


	/**
	 * Test des CoreTypes Note
	 *
	 * CoreType: Note
	 * Testfall: Prüft auf richtigen Wert für Kürzel
	 * Ergebnis: Erwarteter Wert - SEHR_GUT_PLUS
	 */
	@Test
	@DisplayName("Teste CoreType Note: Prüfe getByKuerzel für 1+")
	void testNote_WertBeiKuerzel1P() {
		assertEquals(Note.SEHR_GUT_PLUS, Note.data().getWertByKuerzel("1+"));
	}

}
