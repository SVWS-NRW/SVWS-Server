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
 *   <li> {@link ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei")
class TestValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei {

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
	 * Test von ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei
	 *
	 * @param idFoerderschwerpunkt2   die ID des zweiten Förderschwerpunkts
	 * @param result                  gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei(final Long idFoerderschwerpunkt2, final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei validator =
				new ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei(
						() -> null, // Erster Förderschwerpunkt ist für diesen Validator irrelevant
						() -> idFoerderschwerpunkt2,
						kontext);

		assertEquals(result, validator.pruefe());
	}

}
