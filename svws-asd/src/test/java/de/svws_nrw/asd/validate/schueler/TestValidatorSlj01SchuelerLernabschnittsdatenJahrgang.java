package de.svws_nrw.asd.validate.schueler;

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

/**
 * Testklasse für den Validator ValidatorSlj01SchuelerLernabschnittsdatenJahrgang
 */
@DisplayName("Tests ValidatorSlj01SchuelerLernabschnittsdatenJahrgang")
class TestValidatorSlj01SchuelerLernabschnittsdatenJahrgang {

	private static final String TESTDATEN = """
			jahrgangID , result
			null       , true
			-1         , false
			999999     , false
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
	 * Test von ValidatorSlj01SchuelerLernabschnittsdatenJahrgang
	 *
	 * @param jahrgangID   die ID des Jahrgangs des Schülers
	 * @param result       gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSlj01SchuelerLernabschnittsdatenJahrgang")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorSlj01SchuelerLernabschnittsdatenJahrgang(final Long jahrgangID, final boolean result) {
		final List<Long> idListe = new ArrayList<Long>();
		idListe.add(1L);
		idListe.add(2L);

		final ValidatorKontext kontext = new ValidatorKontext(
				testdaten_001.schule.schulNr,
				Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
				testdaten_001.schule.abschnitte,
				testdaten_001.schule.idSchuljahresabschnitt,
				true);

		final ValidatorSlj01SchuelerLernabschnittsdatenJahrgang validator =
				new ValidatorSlj01SchuelerLernabschnittsdatenJahrgang(() -> jahrgangID, () -> idListe, kontext);

		assertEquals(result, validator.pruefe());
	}

}
