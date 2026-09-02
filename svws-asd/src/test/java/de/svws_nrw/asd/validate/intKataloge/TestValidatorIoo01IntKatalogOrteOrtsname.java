package de.svws_nrw.asd.validate.intKataloge;

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
 * Testklasse für den Validator {@link ValidatorIoo01IntKatalogOrteOrtsname}.
 */
@DisplayName("Tests ValidatorIoo01IntKatalogOrteOrtsname")
class TestValidatorIoo01IntKatalogOrteOrtsname {

	private static final String TESTDATEN = """
			  'abc', 1040   ,  true
			  'aaa', null   ,  true
			  'abc', 1050   ,  false
			  'Bonn', 1050  ,  true
		""";

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("Test für verschiedene Werte von 'Orte'")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorIoo01IntKatalogOrteOrtsname(final String ortsname, final Long idLand, final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorIoo01IntKatalogOrteOrtsname validator =
				new ValidatorIoo01IntKatalogOrteOrtsname(() -> ortsname, () -> idLand, kontext);

		assertEquals(result, validator.pruefe());
	}

}
