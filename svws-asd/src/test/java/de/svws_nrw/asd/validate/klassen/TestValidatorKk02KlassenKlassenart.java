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

@DisplayName("Tests ValidatorKk02KlassenKlassenart")
class TestValidatorKk02KlassenKlassenart {

	// 9999999 -> false (Unbekannte ID)
	// 7002    -> true  (Regelklasse ist ohne gültigBis gesetzt)
	// 1000    -> false (HA_1A ist laut Katalog nur bis 2010 gültig gewesen)
	private static final String TESTDATEN = """
            9999999 , 2026, false
            7002    , 2026, true
            1000    , 2009, true
            1000    , 2026, false
        """;

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("Tests für ValidatorKk02KlassenKlassenart")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorKk02KlassenKlassenart(final Long idKlassenart, final Integer schuljahr, final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		//		Setzen Schuljahr
		kontext.getSchuljahresabschnitt().schuljahr = schuljahr;

		final ValidatorKk02KlassenKlassenart validator =
				new ValidatorKk02KlassenKlassenart(
						() -> idKlassenart,
				        kontext);

		assertEquals(result, validator.pruefe());
	}
}
