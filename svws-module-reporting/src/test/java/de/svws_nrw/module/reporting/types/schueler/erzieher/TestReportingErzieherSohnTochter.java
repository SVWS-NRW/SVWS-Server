package de.svws_nrw.module.reporting.types.schueler.erzieher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

/**
 * Prüft die Anrede „Ihr Sohn/Ihre Tochter“ im Brief an den Erzieher. Die Form richtet sich nach dem Geschlecht des Schülers und dem Kasus.
 */
class TestReportingErzieherSohnTochter {

	/**
	 * Erzeugt einen Erzieher zu einem Schüler mit dem übergebenen Geschlecht. Der Konstruktor bleibt außen vor, weil er alle Personendaten verlangt.
	 *
	 * @param geschlecht Das Geschlecht des Schülers.
	 *
	 * @return Der Erzieher.
	 */
	private static ReportingErzieher erzieherZuSchueler(final Geschlecht geschlecht) {
		final ReportingSchueler schueler = mock(ReportingSchueler.class);
		when(schueler.geschlecht()).thenReturn(geschlecht);
		final ReportingErzieher erzieher = mock(ReportingErzieher.class, CALLS_REAL_METHODS);
		erzieher.schueler = schueler;
		return erzieher;
	}


	@ParameterizedTest
	@CsvSource({ "M, Ihres Sohnes", "W, Ihrer Tochter", "X, Ihres Kindes", ", Ihres Kindes" })
	void testDerGenitivPasstZumGeschlecht(final Geschlecht geschlecht, final String ausdruck) {
		assertEquals(ausdruck, erzieherZuSchueler(geschlecht).sohnTochterGenitiv());
	}

	@ParameterizedTest
	@CsvSource({ "M, Ihren Sohn", "W, Ihre Tochter", "X, Ihr Kind", ", Ihr Kind" })
	void testDerAkkusativPasstZumGeschlecht(final Geschlecht geschlecht, final String ausdruck) {
		assertEquals(ausdruck, erzieherZuSchueler(geschlecht).sohnTochterAkkusativ());
	}

}
