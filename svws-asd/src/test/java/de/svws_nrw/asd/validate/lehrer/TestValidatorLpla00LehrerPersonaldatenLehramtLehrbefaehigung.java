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
 * <p> Testklasse für den Validator Lehrbefaehigung
 * <ul>
 *   <li> {@link ValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung},
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
@DisplayName("Tests zur Validierung von ValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung")
class TestValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung {

	private static final String TESTDATEN_LEHRBEFAEHIGUNG = """
	        null  , false
	        4     , true
	        7     , true
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
	 * Test von ValidatorLpla00LehrerPersonaldatenLehrbefaehigung
	 *
	 * CoreType: LehrerStammdaten
	 *
	 * @param idLehrbefaehigung  die ID der Lehrbefaehigung
	 * @param result             gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_LEHRBEFAEHIGUNG, nullValues = { "null" })
	void testValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung(final Long idLehrbefaehigung, final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung validator =
				new ValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung(() -> idLehrbefaehigung, null, kontext);

		assertEquals(result, validator.pruefe());

	}

}
