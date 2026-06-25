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
 * Testklasse für den Validator {@link ValidatorKow00KlassenOrganisationsformWeiterbildung}.
 */
@DisplayName("Tests ValidatorKow00KlassenOrganisationsformWeiterbildend")
class TestValidatorKow00KlassenOrganisationsformWeiterbildung {

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("Test für verschiedene Werte von 'idWeiterbildendOrganisationsform'")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, nullValues = "null", textBlock = """
			  ID     , RESULT
			  null   , false
			  3001000, true
			""")
	void testValidatorKow00IfDatenNotNull(final Long idWeiterbildendOrganisationsform,
			final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorKow00KlassenOrganisationsformWeiterbildung validator =
				new ValidatorKow00KlassenOrganisationsformWeiterbildung(() -> idWeiterbildendOrganisationsform, kontext);

		assertEquals(result, validator.pruefe());
	}

}
