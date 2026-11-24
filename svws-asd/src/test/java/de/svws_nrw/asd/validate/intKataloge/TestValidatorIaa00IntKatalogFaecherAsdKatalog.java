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
 * Testklasse für den Validator {@link ValidatorIaa00IntKatalogFaecherAsdKatalog}.
 */
@DisplayName("Tests ValidatorIaa00IntKatalogFaecherAsdKatalog")
class TestValidatorIaa00IntKatalogFaecherAsdKatalog {

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("Test für verschiedene Werte von 'kuerzelStatistik'")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, nullValues = "null", textBlock = """
			  ID     , RESULT
			  null   , false
			  AB     ,  true
			  ZZ     ,  true
			  LEER   , false
			""")
	void testValidatorIaa00IntKatalogFaecherAsdKatalog(final String kuerzelStatistik,
			final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorIaa00IntKatalogFaecherAsdKatalog validator =
				new ValidatorIaa00IntKatalogFaecherAsdKatalog(() -> "LEER".equals(kuerzelStatistik) ? "" : kuerzelStatistik, kontext);

		assertEquals(result, validator.pruefe());
	}

}
