package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * <p> Testklasse für die Validatoren
 * <ul>
 *   <li> {@link ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis}
 * </ul>
 * </p>

 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 * CoreType: LehrerStammdaten
 */
@DisplayName("Tests ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis")
class TestValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis {

	private static final String TESTDATEN_STAATSANGEOERIGKEITID = """
			'000', 'L', true
			'000', 'U', true
			'327', 'U', true
			'327', 'L', false
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
	 * Test von ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis
	 *
	 * CoreType: LehrerStammdaten
	 *
	 * @param staatsangehoerigkeitSchluessel   der staatsangehoerigkeitSchluessel, welcher bei den eingelesenen Testdaten ersetzt wird
	 * @param rechtsverhaeltnis                das Rechtsverhältnis, welches bei den eingelesenen Testdaten ersetzt wird
	 * @param result                           gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_STAATSANGEOERIGKEITID, nullValues = { "null" })
	void testValidatorLss11LehrerStammdatenStaatsangehoerigkeitID(final String staatsangehoerigkeitSchluessel, final LehrerRechtsverhaeltnis rechtsverhaeltnis,
			final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis validator =
				new ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(() -> staatsangehoerigkeitSchluessel, () -> rechtsverhaeltnis, kontext);
		assertEquals(result, validator.pruefe());
	}


}
