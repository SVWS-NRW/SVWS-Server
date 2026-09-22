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
 *   <li> {@link ValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins")
class TestValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins {

	private static final String TESTDATEN = """
			null   , true
			-1     , false
			999999 , false
			3001   , true
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
	 * Test von ValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins
	 *
	 * @param idFoerderschwerpunkt1   die ID des ersten Förderschwerpunkts
	 * @param result                  gibt an, welches Ergebnis erwartet wird
	 */
	@DisplayName("Tests für ValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins(final Long idFoerderschwerpunkt1, final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins validator =
				new ValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins(
						() -> idFoerderschwerpunkt1,
						() -> null, // Zweiter Förderschwerpunkt ist hier irrelevant
						kontext);

		assertEquals(result, validator.pruefe());
	}

}
