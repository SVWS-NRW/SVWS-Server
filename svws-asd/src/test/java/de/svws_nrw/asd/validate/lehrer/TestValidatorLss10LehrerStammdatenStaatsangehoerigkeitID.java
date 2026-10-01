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
 *   <li> {@link ValidatorLss10LehrerStammdatenStaasangehoerigkeitID}
 * </ul>
 * </p>

 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 * CoreType: LehrerStammdaten
 */
@DisplayName("Tests ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID")
class TestValidatorLss10LehrerStammdatenStaatsangehoerigkeitID {

	private static final String TESTDATEN_STAATSANGEOERIGKEITID = """
		68090065, 2018, true
		68090065, 1960, false
		68069085, 1950, true
		68069085, 1940, false
		89085071, 1970, true
		89085071, 2021, false
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
	 * Test von ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID
	 *
	 * CoreType: LehrerStammdaten
	 *
	 * @param staatsangehoerigkeitID   die staatsangehoerigkeitID, welche bei den eingelesenen Testdaten ersetzt wird
	 * @param schuljahr  	          das Schuljahr
	 * @param result                   gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_STAATSANGEOERIGKEITID, nullValues = { "null" })
	void testValidatorLss10LehrerStammdatenStaatsangehoerigkeitID(final Long staatsangehoerigkeitID, final Integer schuljahr, final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		//		Setzen Schuljahr
		kontext.getSchuljahresabschnitt().schuljahr = schuljahr;

		final ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID validator =
				new ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID(() -> staatsangehoerigkeitID, kontext);
		assertEquals(result, validator.pruefe());
	}


}
