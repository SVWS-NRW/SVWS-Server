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
 * Testklasse für den Validator {@link ValidatorKoa02KlassenOrganisationsformAllgemeinbildend}.
 */
@DisplayName("Tests ValidatorKoa02KlassenOrganisationsformAllgemeinbildend")
class TestValidatorKoa02KlassenOrganisationsformAllgemeinbildend {

	private ValidatorKoa02KlassenOrganisationsformAllgemeinbildend validator;

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("Validator gibt 'false' zurück, wenn Ao für Schuljahr ungültig ist, sonst 'true'")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, nullValues = "null", textBlock = """
			  ID     , SCHULJAHR, RESULT
			  3001000,      2026, false
			  3001000,      2022, true
			""")
	void testValidatorKoa02(final Long idAllgemeinbildendOrganisationsform, final int schuljahr,
			final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		kontext.getSchuljahresabschnitt().schuljahr = schuljahr;

		validator = new ValidatorKoa02KlassenOrganisationsformAllgemeinbildend(() -> idAllgemeinbildendOrganisationsform, kontext);

		assertEquals(result, validator.pruefe());
	}

}
