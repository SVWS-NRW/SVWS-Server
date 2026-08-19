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
 * Testklasse für den Validator {@link ValidatorKs01KlassenSchulgliederung}.
 */
@DisplayName("Tests ValidatorKs01KlassenSchulgliederung")
class TestValidatorKs01KlassenSchulgliederung {

	// Testfälle:
	// -1      -> false (Transpiler Prüfung)
	// 9999999 -> false (Wert existiert nicht im Katalog)
	// 0       -> true  (Gültiger Wert 'DEFAULT', passend für das Gymnasium in den Testdaten)
	private static final String TESTDATEN = """
            -1      , false
            9999999 , false
            0       , true
        """;

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("Tests für ValidatorKs01KlassenSchulgliederung")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorKs01KlassenSchulgliederung(final Long idSchulgliederung, final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);


		final ValidatorKs01KlassenSchulgliederung validator =
				new ValidatorKs01KlassenSchulgliederung(
						() -> idSchulgliederung,
				        kontext);

		assertEquals(result, validator.pruefe());
	}
}
