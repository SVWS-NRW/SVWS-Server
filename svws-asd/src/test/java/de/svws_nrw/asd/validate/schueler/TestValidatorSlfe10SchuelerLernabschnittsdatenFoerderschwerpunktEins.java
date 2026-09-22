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
 *   <li> {@link ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins")
class TestValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins {

	private static final String TESTDATEN = """
			idFsp1 , idFsp2 , result
			null   , null   , true
			3001   , null   , true
			null   , 3001   , true
			3001   , 4001   , true
			3001   , 3001   , false
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
	 * Test von ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins
	 *
	 * @param idFoerderschwerpunkt1   die ID des ersten Förderschwerpunkts
	 * @param idFoerderschwerpunkt2   die ID des zweiten Förderschwerpunkts
	 * @param result                  gibt an, welches Ergebnis erwartet wird
	 */
	@DisplayName("Tests für ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins(final Long idFoerderschwerpunkt1, final Long idFoerderschwerpunkt2,
			final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins validator =
				new ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins(
						() -> idFoerderschwerpunkt1,
						() -> idFoerderschwerpunkt2,
						kontext);

		assertEquals(result, validator.pruefe());
	}

}
