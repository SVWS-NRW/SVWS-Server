package de.svws_nrw.asd.validate.schueler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

@DisplayName("Tests ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart")
class TestValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart {

	private static final String TESTDATEN = """
			null   , false
			-1     , false
			1000   , true
		""";

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("Tests für ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart(final Long id, final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart validator =
				new ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart(() -> {
					if (id == null) {
						return null;
					}
					final SchuelerSchulbesuchsdaten daten = new SchuelerSchulbesuchsdaten();
					daten.idEinschulungsartGrundschule = id;
					return daten;
				}, kontext);

		assertEquals(result, validator.pruefe());
	}
}
