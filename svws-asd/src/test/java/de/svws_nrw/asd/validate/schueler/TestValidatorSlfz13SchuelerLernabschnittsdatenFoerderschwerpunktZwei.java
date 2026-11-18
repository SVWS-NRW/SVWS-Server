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
 * <p> Testklasse für die Validatoren
 * <ul>
 *   <li> {@link ValidatorSlfz13SchuelerLernabschnittsdatenFoerderschwerpunktZwei}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorSlfz13SchuelerLernabschnittsdatenFoerderschwerpunktZwei")
class TestValidatorSlfz13SchuelerLernabschnittsdatenFoerderschwerpunktZwei {

	// 7000 = LB, 3000 = GB, 4000 = GH
	private static final String TESTDATEN = """
			idFoerderschwerpunkt1 , idFoerderschwerpunkt2 , result
			null                  , null                  , true
			7000                  , null                  , true
			null                  , 3000                  , true
			7000                  , 4000                  , true
			4000                  , 3000                  , true
			7000                  , 3000                  , false
			3000                  , 7000                  , false
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
	 * Test von ValidatorSlfz13SchuelerLernabschnittsdatenFoerderschwerpunktZwei
	 *
	 * @param idFoerderschwerpunkt1   die Katalog-ID des ersten Förderschwerpunkts
	 * @param idFoerderschwerpunkt2   die Katalog-ID des weiteren Förderschwerpunkts
	 * @param result                  gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSlfz13SchuelerLernabschnittsdatenFoerderschwerpunktZwei")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorSlfz13SchuelerLernabschnittsdatenFoerderschwerpunktZwei(
			final Long idFoerderschwerpunkt1,
			final Long idFoerderschwerpunkt2,
			final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorSlfz13SchuelerLernabschnittsdatenFoerderschwerpunktZwei validator =
				new ValidatorSlfz13SchuelerLernabschnittsdatenFoerderschwerpunktZwei(
						() -> idFoerderschwerpunkt1,
						() -> idFoerderschwerpunkt2,
						kontext);
		assertEquals(result, validator.pruefe());
	}

}
