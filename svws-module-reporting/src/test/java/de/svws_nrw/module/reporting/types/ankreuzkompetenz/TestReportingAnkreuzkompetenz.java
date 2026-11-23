package de.svws_nrw.module.reporting.types.ankreuzkompetenz;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Prüft die Zerlegung des Kompetenztextes: Die Unterkategorie steht vor dem ersten Doppelpunkt, ein „#“ trennt Zeilen.
 */
class TestReportingAnkreuzkompetenz {

	private static ReportingAnkreuzkompetenz kompetenz(final String text) {
		return new ReportingAnkreuzkompetenz(0, null, 0, text, 1L, true, false, true, new HashMap<>(), "", 1);
	}

	@Test
	void testOhneUnterkategorie() {
		final ReportingAnkreuzkompetenz k = kompetenz("- arbeitet selbstständig.");
		assertEquals("", k.unterkategorie());
		assertEquals("- arbeitet selbstständig.", k.kompetenzText());
	}

	@Test
	void testMitUnterkategorie() {
		final ReportingAnkreuzkompetenz k = kompetenz("Soziales Miteinander:  hält sich an Regeln.");
		assertEquals("Soziales Miteinander", k.unterkategorie());
		assertEquals("hält sich an Regeln.", k.kompetenzText());
	}

	@Test
	void testNurErsterDoppelpunktTrennt() {
		final ReportingAnkreuzkompetenz k = kompetenz("Uhr: liest 3:15 ab.");
		assertEquals("Uhr", k.unterkategorie());
		assertEquals("liest 3:15 ab.", k.kompetenzText());
	}

	@Test
	void testNurUnterkategorieOhneKompetenz() {
		final ReportingAnkreuzkompetenz k = kompetenz("-Bemerkungen:");
		assertEquals("-Bemerkungen", k.unterkategorie());
		assertEquals("", k.kompetenzText());
		assertEquals(List.of(), k.kompetenzZeilen());
	}

	@Test
	void testRauteTrenntZeilen() {
		final ReportingAnkreuzkompetenz k = kompetenz("Lesen: liest kurze Texte # und versteht sie.");
		assertEquals(List.of("liest kurze Texte", "und versteht sie."), k.kompetenzZeilen());
		assertEquals("liest kurze Texte und versteht sie.", k.kompetenzText());
	}

	@Test
	void testRauteInUnterkategorie() {
		assertEquals("Soziales Miteinander", kompetenz("Soziales#Miteinander: hält sich an Regeln.").unterkategorie());
	}

	@Test
	void testNurRautenErgebenKeinenText() {
		assertEquals("", kompetenz("Kategorie: # #").kompetenzText());
	}

	@Test
	void testLeererText() {
		final ReportingAnkreuzkompetenz k = kompetenz(null);
		assertEquals("", k.unterkategorie());
		assertEquals("", k.kompetenzText());
	}

}
