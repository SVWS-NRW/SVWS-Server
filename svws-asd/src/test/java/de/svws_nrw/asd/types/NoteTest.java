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
class NoteTest {

	/**
	 * Initialisiert die Core-Types, damit die Tests ausgeführt werden können.
	 * Beim Laden der Core-Type-Daten werden die JSON-Dateien auf Plausibilität
	 * geprüft.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}


	// TODO Schreiben von Tests

	/**
	 * Führt einfache Tests zu dem Core-Type Note aus.
	 */
	@Test
	@DisplayName("Prüfe die Anzahl der Anlagen am Berufskolleg.")
	void testNote() {
		assertEquals(26, Note.data().getWerte().size());
	}

	/**
	 * Führt einfache Tests zu dem Core-Type Note aus.
	 */
	@Test
	@DisplayName("Prüfe ob der richtige Name enthalten ist")
	void testNoteName() {
		assertEquals("mangelhaft", Note.data().getHistorieByWert(Note.MANGELHAFT).getFirst().textZeugnis);
	}

	/**
	 * Führt einfache Tests zu dem Core-Type Note aus.
	 */
	@Test
	@DisplayName("Prüfe ob die richtige Sortierung enthalten ist")
	void testNoteSort() {
		assertEquals(400, Note.data().getHistorieByWert(Note.AUSREICHEND).getFirst().sortierung);
	}

	/**
	 * Führt einfache Tests zu dem Core-Type Note aus.
	 */
	@Test
	@DisplayName("Prüfe ob die richtigen Notenpunkte enthalten sind")
	void testNoteNotenpunkte() {
		assertEquals(8, Note.data().getHistorieByWert(Note.BEFRIEDIGEND).getFirst().notenpunkte);
	}

	/**
	 * Führt einfache Tests zu dem Core-Type Note aus.
	 */
	@Test
	@DisplayName("Prüfe hatTendenz ")
	void testHatTendenz() {
		assertEquals(false, Note.fromNotenpunkte(11).hatTendenz(0));
	}


	/**
	 * Führt einfache Tests zu dem Core-Type Note aus.
	 */
	@Test
	@DisplayName("Prüfe getByKuerzel ")
	void testgetByKuerzel() {
		assertEquals(Note.SEHR_GUT_PLUS, Note.data().getWertByKuerzel("1+"));
	}
}
