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
 * Testklasse für den Validator {@link ValidatorKs00KlassenSchulgliederung}.
 */
@DisplayName("Tests ValidatorKs00KlassenSchulgliederung")
class TestValidatorKs00KlassenSchulgliederung {

	// Testfälle:
	// null -> false (Wenn im Validator (daten == null) einen Fehler wirft bzw. false zurückgibt)
	// 1000 -> true  (Ein Wert wurde eingetragen)
	private static final String TESTDATEN = """
            null , false
            1000 , true
        """;

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("Tests für ValidatorKs00KlassenSchulgliederung")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorKs00KlassenSchulgliederung(final Long idSchulgliederung, final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);


		final ValidatorKs00KlassenSchulgliederung validator =
				new ValidatorKs00KlassenSchulgliederung(
					 () -> idSchulgliederung,
				     kontext);

		assertEquals(result, validator.pruefe());
	}
}
