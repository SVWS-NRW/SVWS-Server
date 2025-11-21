package de.svws_nrw.asd.validate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Teste die Klasse DateManager")
class TestDateManager {

	@Test
	@DisplayName("Teste das Erstellen eines Datums anhand eines ISO-Strings.")
	void testDateManagerFromIsoDate() {
		try {
			final DateManager date = DateManager.from("2026-07-01");
			assertEquals(2026, date.getJahr());
			assertEquals(7, date.getMonat());
			assertEquals(1, date.getTag());
		} catch (final InvalidDateException e) {
			fail("Fehler beim Lesen des Datums: " + e.getMessage());
		}
	}

	@Test
	@DisplayName("Teste das Erstellen eines Datums anhand eines ISO-Strings.")
	void testDateManagerGetAlter() {
		final DateManager geburtsdatum;
		final DateManager datum1;
		final DateManager datum2;
		try {
			geburtsdatum = DateManager.from("2030-05-13");
			datum1 = DateManager.from("2031-05-12");
			datum2 = DateManager.from("2031-05-13");
		} catch (final InvalidDateException e) {
			fail("Fehler beim Lesen des Datums: " + e.getMessage());
			return;
		}
		try {
			assertEquals(0, geburtsdatum.getAlter(datum1));
		} catch (final InvalidDateException e) {
			fail("Fehler beim der Altersbestimmung: " + e.getMessage());
		}
		try {
			assertEquals(1, geburtsdatum.getAlter(datum2));
		} catch (final InvalidDateException e) {
			fail("Fehler beim der Altersbestimmung: " + e.getMessage());
		}
	}

}
