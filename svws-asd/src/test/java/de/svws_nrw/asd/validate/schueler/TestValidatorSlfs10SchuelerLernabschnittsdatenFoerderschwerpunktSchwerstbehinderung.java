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
 *   <li> {@link ValidatorSlfs10SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorSlfs10SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung")
class TestValidatorSlfs10SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung {

	private static final String TESTDATEN = """
			null   , true    , false
			null   , false   , true
			1      , true    , true
			2      , false   , true
			3      , null    , true
			null   , null    , true
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
	 * Test von ValidatorSlfs10SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung
	 *
	 * @param idFoerderschwerpunkt1           die ID des ersten Förderschwerpunkts
	 * @param hatSchwerbehinderungsNachweis   Schwerbehinderungsnachweis vorhanden
	 * @param result                          gibt an, welches Ergebnis erwartet wird
	 */
	@DisplayName("Tests für ValidatorSlfs10SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorSlfs10SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung(final Long idFoerderschwerpunkt1, final Boolean hatSchwerbehinderungsNachweis,
			final boolean result) {

		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorSlfs10SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung validator =
				new ValidatorSlfs10SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung(
						() -> idFoerderschwerpunkt1,
						() -> hatSchwerbehinderungsNachweis,
						kontext);

		assertEquals(result, validator.pruefe());
	}

}
