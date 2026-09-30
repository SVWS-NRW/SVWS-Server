package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * Testklasse für den Validator {@link ValidatorLsr10LehrerStammdatenRelevantFuerStatistik}.
 */
@DisplayName("Teste den Validator ValidatorLsr10LehrerStammdatenRelevantFuerStatistik")
class TestValidatorLsr10LehrerStammdatenRelevantFuerStatistik {

	/** Stammdaten der Schule für den Kontext. */
	private static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);


	private static final String LSR10_TESTDATEN = """
			true, '2001-05-31', false
			true, '2018-08-01', true
			true, '2018-07-31', false
			false, '2001-05-31', true
			true, null, true
			""";

	/**
	 * Initialisiert die Core-Types für die Tests.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("TestValidatorLsr10LehrerStammdatenRelevantFuerStatistik")
	@ParameterizedTest
	@CsvSource(textBlock = LSR10_TESTDATEN, nullValues = { "null" })
	void testValidatorLsr10LehrerStammdatenRelevantFuerStatistik(final Boolean istRelevantFuerStatistik, final String abgangsdatum, final boolean result) {

		// Erzeuge den Kontext für die Validierung
				final ValidatorKontext kontext = new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

       //	Setzen Schuljahr auf 2018
		kontext.getSchuljahresabschnitt().schuljahr = 2018;

		final ValidatorLsr10LehrerStammdatenRelevantFuerStatistik validator =
				new ValidatorLsr10LehrerStammdatenRelevantFuerStatistik(
						() -> istRelevantFuerStatistik,
						() -> abgangsdatum,
						kontext);

		assertEquals(result, validator.pruefe());
	}
}
