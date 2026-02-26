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
 * <p> Testklasse für den Validator Kürzel
 * <ul>
 *   <li> {@link ValidatorLsk00LehrerStammdatenKuerzel},
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
 * CoreType: LehrerStammdaten
 */
@DisplayName("Tests zur Validierung von ValidatorLsk00LehrerStammdatenKuerzel")
class TestValidatorLsk00LehrerStammdatenKuerzel {

	private static final String TESTDATEN_KUERZEL = """
	        null             , false
	        ''               , false
	        'VH'             , true
	        'MUE'            , true
	        'klein'          , true
	        '12345678'       , true
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
	 * Test von ValidatorLsk00LehrerStammdatenKuerzel
	 *
	 * CoreType: LehrerStammdaten
	 *
	 * @param kuerzel       der Wert für das Lehrerkürzel
	 * @param result        gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLsk00LehrerStammdatenKuerzel")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_KUERZEL, nullValues = { "null" })
	void testValidatorLsk00LehrerStammdatenKuerzel(final String kuerzel, final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorLsk00LehrerStammdatenKuerzel validator =
				new ValidatorLsk00LehrerStammdatenKuerzel(() -> kuerzel, kontext);

		assertEquals(result, validator.pruefe());

	}

}
