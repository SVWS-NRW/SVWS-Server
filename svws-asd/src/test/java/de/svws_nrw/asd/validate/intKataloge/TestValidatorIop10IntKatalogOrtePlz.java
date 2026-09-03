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
 * Testklasse für den Validator {@link ValidatorIop10IntKatalogOrtePlz}.
 */
@DisplayName("Tests ValidatorIop10IntKatalogOrtePlz")
class TestValidatorIop10IntKatalogOrtePlz {

	private static final String TESTDATEN = """
			  'ab12345' , 1040   , true
			  'ab12345' , null   , true
			  '123ab'   , 1050   , true
			  '33189'   , 1050   , true
			  '33189'   , 1040   , false
		""";

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("Test für verschiedene Werte von 'PLZ'")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorIop10IntKatalogOrtePlz(final String plz, final Long idLand, final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorIop10IntKatalogOrtePlz validator =
				new ValidatorIop10IntKatalogOrtePlz(() ->  plz, () -> idLand, kontext);

		assertEquals(result, validator.pruefe());
	}

}
