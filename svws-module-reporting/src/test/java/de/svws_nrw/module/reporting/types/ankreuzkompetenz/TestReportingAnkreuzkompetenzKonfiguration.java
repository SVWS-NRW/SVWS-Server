package de.svws_nrw.module.reporting.types.ankreuzkompetenz;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Prüft Stufenzahl und Stufentexte der Ankreuzkompetenz-Konfiguration. Die Stufen werden ab Stufe 1 gezählt, bis zum ersten fehlenden Text.
 * Ein Text fehlt, wenn er {@code null}, leer oder nur aus Leerzeichen ist.
 */
class TestReportingAnkreuzkompetenzKonfiguration {

	private static ReportingAnkreuzkompetenzKonfiguration konfiguration(final String... textStufen) {
		return new ReportingAnkreuzkompetenzKonfiguration(textStufen, "Sonstiges");
	}

	@Test
	void testAlleFuenfTexte() {
		assertEquals(5, konfiguration("a", "b", "c", "d", "e").anzahlStufen());
	}

	@Test
	void testTextFuenfFehlt() {
		assertEquals(4, konfiguration("a", "b", "c", "d", null).anzahlStufen());
	}

	@Test
	void testAbbruchBeimErstenFehlendenText() {
		assertEquals(2, konfiguration("a", "b", null, "d", "e").anzahlStufen());
	}

	@Test
	void testLeererStringZaehltAlsFehlend() {
		assertEquals(4, konfiguration("a", "b", "c", "d", "").anzahlStufen());
	}

	@Test
	void testNurLeerzeichenZaehltAlsFehlend() {
		assertEquals(2, konfiguration("a", "b", "   ", "d", "e").anzahlStufen());
	}

	@Test
	void testLeereKonfiguration() {
		assertEquals(0, new ReportingAnkreuzkompetenzKonfiguration(new String[5], null).anzahlStufen());
		assertEquals(0, new ReportingAnkreuzkompetenzKonfiguration(null, null).anzahlStufen());
	}

	@Test
	void testTextStufeGetrimmt() {
		assertEquals("a", konfiguration(" a ", "b", null, null, null).textStufe(1));
	}

	@Test
	void testRauteWirdZuLeerzeichen() {
		assertEquals("sehr sicher", konfiguration("sehr#sicher", "b", null, null, null).textStufe(1));
	}

	@Test
	void testNurRauteZaehltAlsFehlend() {
		assertEquals(2, konfiguration("a", "b", " # ", "d", "e").anzahlStufen());
	}

	@Test
	void testTextStufeUngueltig() {
		final ReportingAnkreuzkompetenzKonfiguration k = konfiguration("a", "b", null, "d", "e");
		assertEquals("", k.textStufe(0));
		assertEquals("", k.textStufe(6));
		assertEquals("", k.textStufe(3));
		assertEquals("", k.textStufe(4));
	}

	@Test
	void testTextSonstigesNull() {
		assertEquals("", new ReportingAnkreuzkompetenzKonfiguration(new String[5], null).textSonstiges());
	}

}
