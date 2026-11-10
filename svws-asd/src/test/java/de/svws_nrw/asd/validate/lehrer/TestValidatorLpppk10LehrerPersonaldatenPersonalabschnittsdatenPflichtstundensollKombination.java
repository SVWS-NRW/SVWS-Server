package de.svws_nrw.asd.validate.lehrer;

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
 *   <li> {@link ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination")
class TestValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination {

	// Katalog-IDs (historie.id):
	// LehrerEinsatzstatus:      1 = 'A', 2 = 'B', 3 = 'DEFAULT' ('*')
	// LehrerBeschaeftigungsart: 1 = 'V', 2 = 'T', 16 = 'WT', 17 = 'WV'
	private static final String TESTDATEN = """
			pflichtstundensoll , idEinsatzstatus , idBeschaeftigungsart , result
			null               , 3               , 1                    , true
			25.5               , 3               , 1                    , true
			0.0                , 1               , 1                    , true
			0.0                , 3               , 16                   , true
			0.0                , 3               , 17                   , true
			0.0                , 3               , 1                    , false
			0.0                , 2               , 2                    , false
			0.0                , null            , null                 , false
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
	 * Test von ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination
	 *
	 * @param pflichtstundensoll     das Pflichtstundensoll des Lehrers
	 * @param idEinsatzstatus        die Katalog-ID des Einsatzstatus des Lehrers
	 * @param idBeschaeftigungsart   die Katalog-ID der Beschäftigungsart des Lehrers
	 * @param result                 gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination(
			final Double pflichtstundensoll,
			final Long idEinsatzstatus,
			final Long idBeschaeftigungsart,
			final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination validator =
				new ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination(
						() -> pflichtstundensoll,
						() -> idEinsatzstatus,
						() -> idBeschaeftigungsart,
						kontext);
		assertEquals(result, validator.pruefe());
	}

}
