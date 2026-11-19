package de.svws_nrw.asd.validate.schueler;

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
 * <p> Testklasse für den Validator
 * <ul>
 *   <li> {@link ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart}
 * </ul>
 * </p>
 */
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

	/**
	 * Initialisiert die Core-Types, damit die Tests ausgeführt werden können.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	/**
	 * Test von ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart
	 *
	 * @param idEinschulungsartGrundschule   die ID der Einschulungsart an der Grundschule
	 * @param result                         gibt an, welches Ergebnis erwartet wird
	 */
	@DisplayName("Tests für ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart(final Long idEinschulungsartGrundschule, final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart validator =
				new ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart(() -> idEinschulungsartGrundschule, kontext);

		assertEquals(result, validator.pruefe());
	}

}
