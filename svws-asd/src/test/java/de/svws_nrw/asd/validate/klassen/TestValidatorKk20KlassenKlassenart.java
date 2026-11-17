package de.svws_nrw.asd.validate.klassen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

@DisplayName("Tests ValidatorKk20KlassenKlassenart")
class TestValidatorKk20KlassenKlassenart {

	// idKlassenart = 6001 = Bezeichner 'PK'
	// idKlassenart, idKlassenartSchueler1, idKlassenartSchueler2, idKlassenartSchueler3, result
	private static final String TESTDATEN = """
            1000, 6001, 7000, 7001, false
            6001, 6001, 7000, 7001, true
            null, 6001, 7000, 7001, false
            null, 2000, 7000, 7001, true
            1000, null, 7000, null, true
            1000, null, null, 7000, true
            1000, 2000, null, null, true
            1000, 2000, null, 7001, true
            1000, 6001, 7000, null, false
            1000, null, null, null, true
            1000, 2000, 7000, 7001, true
            1000,    1,    2,    3, true
               1, 6001, 7000, 7001, false
        """;

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("Tests für ValidatorKk20KlassenKlassenart")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorKk20KlassenKlassenart(final Long idKlassenart, final Long idKlassenartSchueler1, final Long idKlassenartSchueler2, final Long idKlassenartSchueler3, final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		List<Long> listIdKlassenartSchueler = new ArrayList<>();
		if (idKlassenartSchueler1 != null || idKlassenartSchueler2 != null || idKlassenartSchueler3 != null) {
			listIdKlassenartSchueler.add(idKlassenartSchueler1);
			listIdKlassenartSchueler.add(idKlassenartSchueler2);
			listIdKlassenartSchueler.add(idKlassenartSchueler3);
		}

		final ValidatorKk20KlassenKlassenart validator =
				new ValidatorKk20KlassenKlassenart(
						() -> idKlassenart,
						() -> listIdKlassenartSchueler.isEmpty() ? null : listIdKlassenartSchueler,
				        kontext);

		assertEquals(result, validator.pruefe());
	}
}
