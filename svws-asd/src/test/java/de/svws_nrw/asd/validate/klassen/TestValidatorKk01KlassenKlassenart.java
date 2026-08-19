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

@DisplayName("Tests ValidatorKk01KlassenKlassenart")
class TestValidatorKk01KlassenKlassenart {

	// -1      -> false (käme so vom 00er Validator, wenn ID Null wäre)
	// 9999999 -> false (Ungültig / nicht im Katalog)
	// 7000    -> true  (ID für 'RK' (Regelklasse), für GY zugelassen)
	private static final String TESTDATEN = """
            -1      , false
            9999999 , false
            7000    , true
        """;

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("Tests für ValidatorKk01KlassenKlassenart")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorKk01KlassenKlassenart(final Long idKlassenart, final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorKk01KlassenKlassenart validator =
				new ValidatorKk01KlassenKlassenart(
						() -> idKlassenart,
				        kontext);

		assertEquals(result, validator.pruefe());
	}
}
