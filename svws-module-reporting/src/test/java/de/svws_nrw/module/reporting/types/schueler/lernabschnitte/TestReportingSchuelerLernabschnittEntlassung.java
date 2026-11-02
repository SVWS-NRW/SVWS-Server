package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 * Tests des voraussichtlichen Entlassjahres in {@link ReportingSchuelerLernabschnitt}. Die Restabschnitte eines Jahrgangs gelten zu Beginn des Schuljahres.
 * Beide Halbjahre eines Schuljahres ergeben daher dasselbe Jahr.
 */
class TestReportingSchuelerLernabschnittEntlassung {

	/** Das Schuljahr 2026/27, in dem die Fälle der Tests liegen. */
	private static final int SCHULJAHR = 2026;


	@ParameterizedTest
	@CsvSource({
		"EF, 6, 1, 2029",
		"EF, 6, 2, 2029",
		"Q1, 4, 1, 2028",
		"Q1, 4, 2, 2028",
		"Q2, 2, 1, 2027",
		"Q2, 2, 2, 2027"
	})
	void testBeideHalbjahreErgebenDasselbeEntlassjahr(final String jahrgang, final int restabschnitte, final int abschnitt, final String erwartet) {
		assertEquals(erwartet, lernabschnittMit(jahrgangMit(restabschnitte), abschnittMit(abschnitt)).jahrVoraussichtlicheEntlassung(), jahrgang);
	}

	@Test
	void testOhneJahrgangIstDasErgebnisLeer() {
		assertEquals("", lernabschnittMit(null, abschnittMit(1)).jahrVoraussichtlicheEntlassung());
	}

	@Test
	void testOhneSchuljahresabschnittIstDasErgebnisLeer() {
		assertEquals("", lernabschnittMit(jahrgangMit(6), null).jahrVoraussichtlicheEntlassung());
	}

	@Test
	void testOhneRestabschnitteIstDasErgebnisLeer() {
		assertEquals("", lernabschnittMit(jahrgangMit(null), abschnittMit(1)).jahrVoraussichtlicheEntlassung());
	}

	private static ReportingJahrgang jahrgangMit(final Integer restabschnitte) {
		final ReportingJahrgang jahrgang = mock(ReportingJahrgang.class);
		when(jahrgang.anzahlRestabschnitte()).thenReturn(restabschnitte);
		return jahrgang;
	}

	private static ReportingSchuljahresabschnitt abschnittMit(final int abschnitt) {
		final ReportingSchuljahresabschnitt schuljahresabschnitt = mock(ReportingSchuljahresabschnitt.class);
		when(schuljahresabschnitt.schuljahr()).thenReturn(SCHULJAHR);
		when(schuljahresabschnitt.abschnitt()).thenReturn(abschnitt);
		return schuljahresabschnitt;
	}

	private static ReportingSchuelerLernabschnitt lernabschnittMit(final ReportingJahrgang jahrgang, final ReportingSchuljahresabschnitt schuljahresabschnitt) {
		final ReportingSchuelerLernabschnitt lernabschnitt = mock(ReportingSchuelerLernabschnitt.class, CALLS_REAL_METHODS);
		lernabschnitt.jahrgang = jahrgang;
		lernabschnitt.schuljahresabschnitt = schuljahresabschnitt;
		return lernabschnitt;
	}

}
