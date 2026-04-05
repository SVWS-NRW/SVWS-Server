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


	@Test
	@DisplayName("Kalenderwochensonderfall. KW vom 01.01.2021 ist die 53. KW des Vorjahres.")
	void testDateManagerKalenderwocheJahresanfangMit53KwImVorjahr() {
		// So sieht die 53. Kalenderwoche des Jahres 2020 aus.
		// Mo       Di      Mi      Do      Fr      Sa      So
		// 28.12.   29.12.  30.12.  31.12.  01.01.  02.01.  03.01.

		try {
			final DateManager date = DateManager.from("2021-01-01");
			assertEquals(2020, date.getKalenderwochenjahr());
			assertEquals(53, date.getKalenderwoche());
		} catch (final InvalidDateException e) {
			fail("Fehler beim Lesen des Datums: " + e.getMessage());
		}
	}


	@Test
	@DisplayName("Teste die Kalenderwoche am Jahresanfang mit Bezug auf das Vorjahr.")
	void testDateManagerKalenderwocheJahresanfang() {
		try {
			final DateManager date = DateManager.from("2022-01-01");
			assertEquals(2021, date.getKalenderwochenjahr());
			assertEquals(52, date.getKalenderwoche());
		} catch (final InvalidDateException e) {
			fail("Fehler beim Lesen des Datums: " + e.getMessage());
		}
	}


	@Test
	@DisplayName("Teste die erste ISO-Kalenderwoche eines Jahres.")
	void testDateManagerKalenderwocheErsteKwDesJahres() {
		try {
			final DateManager date = DateManager.from("2021-01-04");
			assertEquals(2021, date.getKalenderwochenjahr());
			assertEquals(1, date.getKalenderwoche());
		} catch (final InvalidDateException e) {
			fail("Fehler beim Lesen des Datums: " + e.getMessage());
		}
	}


	@Test
	@DisplayName("Teste die Kalenderwoche am Jahresende mit Bezug auf das Folgejahr.")
	void testDateManagerKalenderwocheJahresende() {
		try {
			final DateManager date = DateManager.from("2018-12-31");
			assertEquals(2019, date.getKalenderwochenjahr());
			assertEquals(1, date.getKalenderwoche());
		} catch (final InvalidDateException e) {
			fail("Fehler beim Lesen des Datums: " + e.getMessage());
		}
	}


}
