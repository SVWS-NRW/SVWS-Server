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
 * <p> Testklasse für die Validatoren
 * <ul>
 *   <li> {@link ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit}
 * </ul>
 * </p>

 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 * CoreType: LehrerStammdaten
 */
@DisplayName("Tests ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit")
class TestValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit {

	private static final String TESTDATEN_STAATSANGEOERIGKEITID = """
			-1    , -1    , true
			123   , 123   , true
			null  , 123   , false
			null  , null  , false
			123   , null  , false
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
	 * Test von ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit
	 *
	 * CoreType: SchuelerStammdaten
	 *
	 * @param idStaatsangehoerigkeit2  die idStaatsangehoerigkeit2, welche bei den eingelesenen Testdaten ersetzt wird
	 * @param idStaatsangehoerigkeit   die idStaatsangehoerigkeit, welche bei den eingelesenen Testdaten ersetzt wird
	 * @param result                   gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_STAATSANGEOERIGKEITID, nullValues = { "null" })
	void testValidatorSses00SchuelerStammdatenErsteStaatsangehoerigkeit(final Long idStaatsangehoerigkeit2, final Long idStaatsangehoerigkeit, final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit validator =
				new ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit(() -> idStaatsangehoerigkeit2, () -> idStaatsangehoerigkeit, kontext);
		assertEquals(result, validator.pruefe());
	}


}
