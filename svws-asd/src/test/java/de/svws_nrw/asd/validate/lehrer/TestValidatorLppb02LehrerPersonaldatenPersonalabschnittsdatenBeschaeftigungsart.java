package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * <p> Testklasse für den Validator
 * <ul>
 *   <li> {@link ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart},
 * </ul>
 * </p>
 *
 * <p> Testdaten:
 *   <ul>
 *	   <li>  de/svws_nrw/asd/validate/lehrer/Testdaten_002_LehrerPersonalabschnittsdaten.json
 *   </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 * CoreType: LehrerStammdaten
 */
@DisplayName("Tests zur Validierung der Lppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart")
class TestValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart {

	private static final String TESTDATEN_LPPB02 = """
		'ST', 2018, true
		'ST', 2025, false
		'X' , 2018, true
		'V' , 2018, true
		'T' , 2018, true
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
	 * Test von ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart2
	 *
	 * CoreType: LehrerPersonalabschnittsdaten
	 *
	 * @param beschaeftigungsart
	 * @param schuljahr
	 * @param result     gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_LPPB02)
	void testValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(final String beschaeftigungsart, final Integer schuljahr, final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart validator =
				new ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(
						() -> LehrerBeschaeftigungsart.data().getWertByBezeichnerOrNull(beschaeftigungsart),
						() -> schuljahr,
						null,
						null,
						kontext);

		assertEquals(result, validator.pruefe());

	}

}
