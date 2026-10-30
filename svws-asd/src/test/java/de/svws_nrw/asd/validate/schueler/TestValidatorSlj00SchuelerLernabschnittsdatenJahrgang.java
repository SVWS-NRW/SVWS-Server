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
 *   <li> {@link ValidatorSlj00SchuelerLernabschnittsdatenJahrgang}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorSlj00SchuelerLernabschnittsdatenJahrgang")
class TestValidatorSlj00SchuelerLernabschnittsdatenJahrgang {

	private static final String TESTDATEN_JAHRGANG = """
			jahrgangID , result
			null       , false
			1          , true
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
	 * Test von ValidatorSlj00SchuelerLernabschnittsdatenJahrgang
	 *
	 * @param jahrgangID   die ID des Jahrgangs des Schülers
	 * @param result       gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSlj00SchuelerLernabschnittsdatenJahrgang")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = TESTDATEN_JAHRGANG, nullValues = { "null" })
	void testValidatorSlj00SchuelerLernabschnittsdatenJahrgang(final Long jahrgangID, final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorSlj00SchuelerLernabschnittsdatenJahrgang validator =
				new ValidatorSlj00SchuelerLernabschnittsdatenJahrgang(() -> jahrgangID, () -> testdaten_001.jahrgaenge, kontext);
		assertEquals(result, validator.pruefe());
	}

}
