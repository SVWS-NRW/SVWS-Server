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
 * <p> Testklasse für den Validator ValidatorLpa01LehrerPersonaldatenAbgangsdatum
 * <ul>
 *   <li> {@link ValidatorLpa01LehrerPersonaldatenAbgangsdatum},
 * </ul>
 * </p>
 *
 * <p> Testdaten:
 *   <ul>
 *     <li> de/svws_nrw/asd/validate/lehrer/Testdaten_001_StatistikGesamt.json
 *   </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 * CoreType: LehrerPersonaldaten
 */
@DisplayName("Tests zur Validierung von Lpa01LehrerPersonaldatenAbgangsdatum")
class TestValidatorLpa01LehrerPersonaldatenAbgangsdatum {

	private static final String TESTDATEN_ABGANGSDATUM = """
	        '7  '            , false
	        'hugo'           , false
	        '2024-13-01'     , false
	        '2024-01-32'     , false
	        '2024-01-01'     , true
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
	 * Test von ValidatorLpa01LehrerPersonaldatenAbgangsdatum
	 *
	 * CoreType: LehrerPersonaldaten
	 *
	 * @param abgangsdatum  der Wert für das Abgangsdatum (2024-01-01)
	 * @param result        gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLpa01LehrerPersonaldatenAbgangsdatum")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_ABGANGSDATUM, nullValues = { "null" })
	void testValidatorLpa01LehrerPersonaldatenAbgangsdatum(final String abgangsdatum, final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorLpa01LehrerPersonaldatenAbgangsdatum validator =
				new ValidatorLpa01LehrerPersonaldatenAbgangsdatum(() -> abgangsdatum, kontext);

		assertEquals(result, validator.pruefe());

	}

}
