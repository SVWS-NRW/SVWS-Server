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
 * <p> Testklasse für den Validator Geburtsdatum
 * <ul>
 *   <li> {@link ValidatorLsd01LehrerStammdatenGeburtsdatum},
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
@DisplayName("Tests zur Validierung von Lsd01LehrerStammdatenGeburtsdatum")
class TestValidatorLsd01LehrerStammdatenGeburtsdatum {

	private static final String TESTDATEN_GEBURTSDATUM = """
	        '7  '            , false
	        'hugo'           , false
	        '1990-13-01'     , false
	        '1990-01-32'     , false
	        '1990-01-01'     , true
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
	 * Test von ValidatorLsd01LehrerStammdatenGeburtsdatum
	 *
	 * CoreType: LehrerStammdaten
	 *
	 * @param geburtsdatum  der Wert für das Geburtsdatum (1990-01-01)
	 * @param result        gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLsd01LehrerStammdatenGeburtsdatum")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_GEBURTSDATUM, nullValues = { "null" })
	void testValidatorLsd01LehrerStammdatenGeburtsdatum(final String geburtsdatum, final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorLsd01LehrerStammdatenGeburtsdatum validator =
				new ValidatorLsd01LehrerStammdatenGeburtsdatum(() -> geburtsdatum, kontext);

		assertEquals(result, validator.pruefe());

	}

}
