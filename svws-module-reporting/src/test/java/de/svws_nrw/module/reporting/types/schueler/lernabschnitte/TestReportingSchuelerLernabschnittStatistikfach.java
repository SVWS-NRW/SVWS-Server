package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.fach.ReportingStatistikFach;

/**
 * Tests der Suche nach Leistungsdaten über das Statistikfach in {@link ReportingSchuelerLernabschnitt}. Die Suche liefert alle Fächer der Schule zu einem
 * Statistikfach in der Reihenfolge der Leistungsdaten und übergeht Einträge ohne Fach oder ohne Statistikfach. Mit einem zusätzlichen Kürzel oder einer
 * Bezeichnung liefert sie unter diesen Fächern das erste passende, ohne Beachtung der Groß- und Kleinschreibung.
 */
class TestReportingSchuelerLernabschnittStatistikfach {

	@Test
	void testAlleFaecherZumStatistikfachInDerReihenfolgeDerLeistungsdaten() {
		final ReportingSchuelerLeistungsdaten deutsch = leistungsdatenMitStatistikfach("D");
		final ReportingSchuelerLeistungsdaten mathematik = leistungsdatenMitStatistikfach("M");
		final ReportingSchuelerLeistungsdaten deutschFoerderung = leistungsdatenMitStatistikfach("D");
		final ReportingSchuelerLernabschnitt lernabschnitt = lernabschnittMit(List.of(deutsch, mathematik, deutschFoerderung));

		assertEquals(List.of(deutsch, deutschFoerderung), lernabschnitt.leistungsdatenZumStatistikfach("D"));
	}

	@Test
	void testEintraegeOhneFachOderStatistikfachWerdenUebergangen() {
		final ReportingSchuelerLeistungsdaten ohneFach = mock(ReportingSchuelerLeistungsdaten.class);
		final ReportingSchuelerLeistungsdaten ohneStatistikfach = mock(ReportingSchuelerLeistungsdaten.class);
		when(ohneStatistikfach.fach()).thenReturn(mock(ReportingFach.class));
		final ReportingSchuelerLeistungsdaten deutsch = leistungsdatenMitStatistikfach("D");
		final ReportingSchuelerLernabschnitt lernabschnitt = lernabschnittMit(List.of(ohneFach, ohneStatistikfach, deutsch));

		assertEquals(List.of(deutsch), lernabschnitt.leistungsdatenZumStatistikfach("D"));
	}

	@Test
	void testOhneKuerzelIstDasErgebnisLeer() {
		final ReportingSchuelerLernabschnitt lernabschnitt = lernabschnittMit(List.of(leistungsdatenMitStatistikfach("D")));

		assertTrue(lernabschnitt.leistungsdatenZumStatistikfach(null).isEmpty());
		assertTrue(lernabschnitt.leistungsdatenZumStatistikfach("").isEmpty());
	}

	@Test
	void testDasFachLaesstSichUeberDasKuerzelDerSchuleWaehlen() {
		final ReportingSchuelerLeistungsdaten lesen = leistungsdatenMitFach("D", "LE", "Lesen", "");
		final ReportingSchuelerLernabschnitt lernabschnitt = lernabschnittMit(List.of(leistungsdatenMitFach("D", "SG", "Sprachgebrauch", ""), lesen,
				leistungsdatenMitFach("D", "RS", "Rechtschreibung", "")));

		assertSame(lesen, lernabschnitt.leistungsdatenZumStatistikfach("D", "le"));
	}

	@Test
	void testDasFachLaesstSichUeberDieZeugnisbezeichnungWaehlen() {
		final ReportingSchuelerLeistungsdaten lesen = leistungsdatenMitFach("D", "LE", "Lesen", "");
		final ReportingSchuelerLernabschnitt lernabschnitt = lernabschnittMit(List.of(leistungsdatenMitFach("D", "SG", "Sprachgebrauch", ""), lesen));

		assertSame(lesen, lernabschnitt.leistungsdatenZumStatistikfach("D", "LESEN"));
		assertSame(lesen, lernabschnitt.leistungsdatenZumStatistikfach("D", " Lesen "));
	}

	@Test
	void testDasFachLaesstSichUeberDieAllgemeineBezeichnungWaehlen() {
		final ReportingSchuelerLeistungsdaten lesen = leistungsdatenMitFach("D", "LS", "", "");
		final ReportingFach fachLesen = lesen.fach();
		when(fachLesen.bezeichnung()).thenReturn("Lesen");
		final ReportingSchuelerLernabschnitt lernabschnitt = lernabschnittMit(List.of(leistungsdatenMitFach("D", "SG", "Sprachgebrauch", ""), lesen));

		assertSame(lesen, lernabschnitt.leistungsdatenZumStatistikfach("D", "lesen"));
	}

	@Test
	void testDasFachLaesstSichUeberDieBezeichnungFuerDasUeberweisungszeugnisWaehlen() {
		final ReportingSchuelerLeistungsdaten lesen = leistungsdatenMitFach("D", "LE", "Lesen", "Lesen und Textverständnis");
		final ReportingSchuelerLernabschnitt lernabschnitt = lernabschnittMit(List.of(leistungsdatenMitFach("D", "SG", "Sprachgebrauch", ""), lesen));

		assertSame(lesen, lernabschnitt.leistungsdatenZumStatistikfach("D", "lesen und textverständnis"));
	}

	@Test
	void testBeiMehrerenPassendenFaechernKommtDasErste() {
		final ReportingSchuelerLeistungsdaten erstes = leistungsdatenMitFach("D", "D1", "Deutsch", "");
		final ReportingSchuelerLernabschnitt lernabschnitt = lernabschnittMit(List.of(erstes, leistungsdatenMitFach("D", "D2", "Deutsch", "")));

		assertSame(erstes, lernabschnitt.leistungsdatenZumStatistikfach("D", "Deutsch"));
	}

	@Test
	void testEinFachZuEinemAnderenStatistikfachWirdNichtGewaehlt() {
		final ReportingSchuelerLeistungsdaten lesen = leistungsdatenMitFach("D", "LE", "Lesen", "");
		final ReportingSchuelerLernabschnitt lernabschnitt = lernabschnittMit(List.of(leistungsdatenMitFach("SU", "LE", "Lernen im Sachunterricht", ""), lesen));

		assertSame(lesen, lernabschnitt.leistungsdatenZumStatistikfach("D", "LE"));
	}

	@Test
	void testOhneTrefferOderOhneSuchbegriffKommtNull() {
		final ReportingSchuelerLernabschnitt lernabschnitt = lernabschnittMit(List.of(leistungsdatenMitFach("D", "LE", "Lesen", "")));

		assertNull(lernabschnitt.leistungsdatenZumStatistikfach("D", "Mathematik"));
		assertNull(lernabschnitt.leistungsdatenZumStatistikfach("M", "LE"));
		assertNull(lernabschnitt.leistungsdatenZumStatistikfach("D", null));
		assertNull(lernabschnitt.leistungsdatenZumStatistikfach("D", " "));
	}

	private static ReportingSchuelerLeistungsdaten leistungsdatenMitStatistikfach(final String kuerzelASD) {
		return leistungsdatenMitFach(kuerzelASD, "", "", "");
	}

	private static ReportingSchuelerLeistungsdaten leistungsdatenMitFach(final String kuerzelASD, final String kuerzel, final String bezeichnungZeugnis,
			final String bezeichnungUeberweisungszeugnis) {
		final ReportingStatistikFach statistikfach = mock(ReportingStatistikFach.class);
		when(statistikfach.kuerzelASD()).thenReturn(kuerzelASD);
		final ReportingFach fach = mock(ReportingFach.class);
		when(fach.statistikfach()).thenReturn(statistikfach);
		when(fach.kuerzel()).thenReturn(kuerzel);
		when(fach.bezeichnungZeugnis()).thenReturn(bezeichnungZeugnis);
		when(fach.bezeichnungUeberweisungszeugnis()).thenReturn(bezeichnungUeberweisungszeugnis);
		final ReportingSchuelerLeistungsdaten leistungsdaten = mock(ReportingSchuelerLeistungsdaten.class);
		when(leistungsdaten.fach()).thenReturn(fach);
		return leistungsdaten;
	}

	private static ReportingSchuelerLernabschnitt lernabschnittMit(final List<ReportingSchuelerLeistungsdaten> leistungsdaten) {
		final ReportingSchuelerLernabschnitt lernabschnitt = mock(ReportingSchuelerLernabschnitt.class, CALLS_REAL_METHODS);
		doReturn(leistungsdaten).when(lernabschnitt).leistungsdaten();
		return lernabschnitt;
	}

}
