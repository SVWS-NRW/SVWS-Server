package de.svws_nrw.asd.validate.klassen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * Testklasse für den Validator {@link ValidatorKow01KlassenOrganisationsformWeiterbildung}.
 */
@DisplayName("Tests ValidatorKow01KlassenOrganisationsformWeiterbildend")
class TestValidatorKow01KlassenOrganisationsformWeiterbildung {

	private ValidatorKow01KlassenOrganisationsformWeiterbildung validator;

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("Validator gibt 'false' zurück, wenn ungültige Ao geliefert wird, sonst 'true'")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, nullValues = "null", textBlock = """
			  ID     , RESULT
			  -1     , false
			  2002000, true
			""")
	void testValidatorKoa01(final Long idWeiterbildendOrganisationsform,
			final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		validator = new ValidatorKow01KlassenOrganisationsformWeiterbildung(() -> idWeiterbildendOrganisationsform, kontext);

		assertEquals(result, validator.pruefe());
	}

	@DisplayName("Validator gibt 'false' zurück, wenn 'idWeiterbildungOrganisationsform' keine Ao liefert")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, nullValues = "null", textBlock = """
			  ID  , RESULT
			  -1  , false
			""")
	void testValidatorKow01ReturnsFalseIfAoIsNull(final Long idWeiterbildendOrganisationsform,
			final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		KlassenDaten daten = new KlassenDaten();
		daten.idWeiterbildungOrganisationsform = idWeiterbildendOrganisationsform;
		assertNull(AllgemeinbildendOrganisationsformen.data().getWertByIDOrNull(daten.idWeiterbildungOrganisationsform));

		validator = new ValidatorKow01KlassenOrganisationsformWeiterbildung(() -> idWeiterbildendOrganisationsform, kontext);

		assertEquals(result, validator.pruefe());
	}

}
