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
 * Testklasse für den Validator {@link ValidatorKow02KlassenOrganisationsformWeiterbildung}.
 */
@DisplayName("Tests ValidatorKow02KlassenOrganisationsformWeiterbildend")
class TestValidatorKow02KlassenOrganisationsformWeiterbildung {

	private ValidatorKow02KlassenOrganisationsformWeiterbildung validator;

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("Validator gibt 'false' zurück, wenn Ao für Schuljahr ungültig ist, sonst 'true'")
	@ParameterizedTest
// hier kein Test auf false möglich, da keine entsprechenden Daten in der JSON vorhanden sind
	@CsvSource(useHeadersInDisplayName = true, nullValues = "null", textBlock = """
			  ID     , SCHULJAHR, RESULT
			  2001000,      2026, true
			  2002000,      2022, true
			""")
	void testValidatorKow02(final Long idWeiterbildendOrganisationsform, final int schuljahr,
			final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		kontext.getSchuljahresabschnitt().schuljahr = schuljahr;

		validator = new ValidatorKow02KlassenOrganisationsformWeiterbildung(() -> idWeiterbildendOrganisationsform, kontext);

		assertEquals(result, validator.pruefe());
	}

}
