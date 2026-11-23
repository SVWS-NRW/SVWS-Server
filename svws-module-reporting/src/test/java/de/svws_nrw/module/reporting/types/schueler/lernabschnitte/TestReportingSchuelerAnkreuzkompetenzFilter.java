package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import de.svws_nrw.core.data.reporting.ReportingFilterDefinitionGruppe;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.module.reporting.types.ankreuzkompetenz.ReportingAnkreuzkompetenz;

/**
 * Prüft die Filtergruppe "Ankreuzkompetenzfilter" des Grundschulzeugnisses Klasse 1 bis 3 gegen die Filter-Registry von
 * {@link ReportingSchuelerAnkreuzkompetenz}: Durchgelassen werden nur Kompetenzen mit Kreuz in einer der Stufen 1 bis 5 und mit eigenem
 * Kompetenztext nach der Unterkategorie.
 */
class TestReportingSchuelerAnkreuzkompetenzFilter {

	private static final ReportingFilterDefinitionGruppe GRUPPE =
			ReportingReportvorlage.SCHUELER_V_GRUNDSCHULZEUGNIS_KLASSE_1_BIS_3.getReportingParameter().filterDefinitionenGruppen.stream()
					.filter(g -> "ReportingSchuelerAnkreuzkompetenz".equals(g.typ))
					.findFirst()
					.orElseThrow(() -> new AssertionError("Die Vorlage bringt keine Filtergruppe für ReportingSchuelerAnkreuzkompetenz mit."));

	private final List<String> validierungsfehler = new ArrayList<>();

	private final Predicate<ReportingSchuelerAnkreuzkompetenz> filter = ReportingSchuelerAnkreuzkompetenz.FILTER.bedingung(GRUPPE, validierungsfehler);

	private static ReportingSchuelerAnkreuzkompetenz kompetenz(final String text, final Boolean... stufen) {
		final ReportingAnkreuzkompetenz katalog = (text == null) ? null
				: new ReportingAnkreuzkompetenz(0, null, 0, text, 1L, true, false, true, new HashMap<>(), "", 1);
		final Boolean[] s = new Boolean[5];
		System.arraycopy(stufen, 0, s, 0, stufen.length);
		return new ReportingSchuelerAnkreuzkompetenz(katalog, 1L, null, s[0], s[1], s[2], s[3], s[4]);
	}

	@Test
	void testDieFiltergruppeIstUnsichtbar() {
		assertFalse(GRUPPE.uiIstSichtbar);
	}

	@Test
	void testAttributeDerFiltergruppeSindRegistriert() {
		filter.test(kompetenz("arbeitet sauber", true));
		assertEquals(List.of(), validierungsfehler);
	}

	@Test
	void testBewertetInStufe1() {
		assertTrue(filter.test(kompetenz("arbeitet sauber", true)));
	}

	@Test
	void testBewertetInStufe5() {
		assertTrue(filter.test(kompetenz("arbeitet sauber", false, null, false, false, true)));
	}

	@Test
	void testMitUnterkategorie() {
		assertTrue(filter.test(kompetenz("Soziales Miteinander: hält sich an Regeln", false, true)));
	}

	@Test
	void testOhneKreuz() {
		assertFalse(filter.test(kompetenz("arbeitet sauber", false, false, false, false, false)));
		assertFalse(filter.test(kompetenz("arbeitet sauber", null, null, null, null, null)));
	}

	@Test
	void testUnterkategorieOhneKompetenz() {
		assertFalse(filter.test(kompetenz("-Bemerkungen:", true)));
		assertFalse(filter.test(kompetenz("Soziales Miteinander:", true)));
		assertFalse(filter.test(kompetenz("Soziales Miteinander: # ", true)));
	}

	@Test
	void testDoppelpunktImKompetenztext() {
		assertTrue(filter.test(kompetenz("Uhr: liest 3:15 ab.", true)));
	}

	@Test
	void testOhneText() {
		assertFalse(filter.test(kompetenz("", true)));
	}

	@Test
	void testOhneKatalogkompetenz() {
		assertFalse(filter.test(kompetenz(null, true)));
	}

	@Test
	void testOhneFiltergruppeBleibtAllesErhalten() {
		final Predicate<ReportingSchuelerAnkreuzkompetenz> ohne = ReportingSchuelerAnkreuzkompetenz.FILTER.bedingung(null, null);
		assertTrue(ohne.test(kompetenz("-Bemerkungen:", false)));
	}

}
