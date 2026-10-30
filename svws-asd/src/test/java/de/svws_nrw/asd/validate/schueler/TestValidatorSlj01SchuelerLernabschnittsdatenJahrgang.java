package de.svws_nrw.asd.validate.schueler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.statistik.JahrgaengeStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
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
			1000000    , true
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
		final ValidatorKontext kontext = new ValidatorKontext(
				testdaten_001.schule.schulNr,
				Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
				testdaten_001.schule.abschnitte,
				testdaten_001.schule.idSchuljahresabschnitt,
				true);

		// Wir greifen auf die regulären Jahrgangs-Werte aus den Core-Types über den Manager zu
		final List<JahrgaengeStatistikGesamt> jahrgaengeList = Jahrgaenge.data().getWerte().stream()
				.map(j -> {
					final JahrgaengeStatistikGesamt js = new JahrgaengeStatistikGesamt();
					// Nutzen des Managers, um den ersten Historieneintrag sicher zu ermitteln
					js.id = Jahrgaenge.data().getHistorieByWert(j).get(0).id;
					js.kuerzel = Jahrgaenge.data().getHistorieByWert(j).get(0).kuerzel;
					return js;
				}).toList();

		final ValidatorSlj01SchuelerLernabschnittsdatenJahrgang validator =
				new ValidatorSlj01SchuelerLernabschnittsdatenJahrgang(() -> jahrgangID, () -> jahrgaengeList, kontext);

		assertEquals(result, validator.pruefe());
	}

}
