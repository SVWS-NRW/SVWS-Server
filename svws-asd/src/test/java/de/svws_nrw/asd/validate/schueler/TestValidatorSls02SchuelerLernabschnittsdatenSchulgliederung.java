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
 *   <li> {@link ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung},
 * </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * CoreType: SchuelerLernabschnittsdaten
 */
@DisplayName("Tests zur Validierung der ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung")
class TestValidatorSls02SchuelerLernabschnittsdatenSchulgliederung {

	private static final String TESTDATEN_SCHULGLIEDERUNG = """
		1001000, 2002, true
		1002000, 2021, true
		2002002, 2018, false
		2002001, 2021, false
		1005000, 2018, false
		-1     , 2018, false
		""";

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	/**
	 * Initialisiert die Core-Types, damit die Tests ausgeführt werden können.
	 * Beim Laden der Core-Type-Daten werden die JSON-Dateien auf Plausibilität
	 * geprüft.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	/**
	 * Test von ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung
	 *
	 * CoreType: SchuelerLernabschnittsdaten
	 *
	 * @param idSchulgliederung    die ID der Schulgliederung, welche bei den eingelesenen Testdaten ersetzt wird
	 * @param schuljahr  	       das Schuljahr
	 * @param result               gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_SCHULGLIEDERUNG)
	void testValidatorSls02SchuelerLernabschnittsdatenSchulgliederung(final Long idSchulgliederung, final Integer schuljahr,
			final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung validator =
				new ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung(
						() -> schuljahr,
						() -> idSchulgliederung,
						kontext);

		assertEquals(result, validator.pruefe());

	}

}
