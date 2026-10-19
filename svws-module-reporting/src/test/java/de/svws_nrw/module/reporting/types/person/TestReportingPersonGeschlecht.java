package de.svws_nrw.module.reporting.types.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.types.Geschlecht;

/**
 * Prüft das Geschlechtssymbol der Person. Männlich ist das Marssymbol (&#9794;), weiblich das Venussymbol (&#9792;).
 */
class TestReportingPersonGeschlecht {

	/**
	 * Erzeugt eine Person mit dem übergebenen Geschlecht. Der Konstruktor bleibt außen vor, weil er alle Personendaten verlangt.
	 *
	 * @param geschlecht Das Geschlecht der Person.
	 *
	 * @return Die Person.
	 */
	private static ReportingPerson personMitGeschlecht(final Geschlecht geschlecht) {
		final ReportingPerson person = mock(ReportingPerson.class, CALLS_REAL_METHODS);
		person.geschlecht = geschlecht;
		return person;
	}


	@ParameterizedTest
	@CsvSource({ "M, &#9794;", "W, &#9792;", "X, -", "," })
	void testDasSymbolPasstZumGeschlecht(final Geschlecht geschlecht, final String symbol) {
		assertEquals((symbol == null) ? "" : symbol, personMitGeschlecht(geschlecht).geschlechtDruckSymbolHtml());
	}

	@ParameterizedTest
	@CsvSource({ "M, (&#9794;)", "W, (&#9792;)" })
	void testDasSymbolInKlammernPasstZumGeschlecht(final Geschlecht geschlecht, final String symbol) {
		assertEquals(symbol, personMitGeschlecht(geschlecht).geschlechtDruckSymbolInKlammernHtml());
	}

}
