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
 * Testklasse für den Validator {@link ValidatorIfa00IntKatalogFoerderschwerpunkteAsdKatalog}.
 */
@DisplayName("Tests ValidatorIfa00IntKatalogFoerderschwerpunkteAsdKatalog")
class TestValidatorIfa00IntKatalogFoerderschwerpunkteAsdKatalog {

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("Test für verschiedene Werte von 'idKataglog'")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, nullValues = "null", textBlock = """
			  ID     , RESULT
			  null   , false
			  1000   ,  true
			""")
	void testValidatorKoa00IfDatenNotNull(final Long idKatalog,
			final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorIfa00IntKatalogFoerderschwerpunkteAsdKatalog validator =
				new ValidatorIfa00IntKatalogFoerderschwerpunkteAsdKatalog(() -> idKatalog, kontext);

		assertEquals(result, validator.pruefe());
	}

}
