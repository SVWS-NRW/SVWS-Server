package de.svws_nrw.asd.validate.klassen;

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
 * Testklasse für den Validator {@link ValidatorKs02KlassenSchulgliederung}.
 */
@DisplayName("Tests ValidatorKs02KlassenSchulgliederung")
class TestValidatorKs02KlassenSchulgliederung {

	// Testfälle:
	// 0       -> true  (ID für 'DEFAULT' hat kein Ablaufdatum, ist historisch gültig)
	// 2001000 -> false (ID für 'B01' ist abgelaufen, Testschuljahr ist jünger als 2004)
	// 1005000 -> false (ID für 'A05' ist abgelaufen, Testschuljahr ist gültig bis 2015)
	// 1005000 -> true  (ID für 'A05' ist abgelaufen, Testschuljahr ist gültig bis 2015)
	private static final String TESTDATEN = """

            0       , 2026, true
            2001000 , 2026, false
            1005000 , 2024, false
            1005000 , 2013, true
        """;

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("Tests für ValidatorKs02KlassenSchulgliederung")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorKs02KlassenSchulgliederung(final Long idSchulgliederung, final Integer schuljahr, final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);


//		Setzen Schuljahr
		kontext.getSchuljahresabschnitt().schuljahr = schuljahr;

		final ValidatorKs02KlassenSchulgliederung validator =
				new ValidatorKs02KlassenSchulgliederung(
						() -> idSchulgliederung,
				        kontext);

		assertEquals(result, validator.pruefe());
	}
}
