package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * <p> Testklasse für die Validatoren
 * <ul>
 *   <li> {@link ValidatorLsn08LehrerStammdatenNachname},
 * </ul>
 * </p>
 *
 * <p> Testdaten:
 *   <ul>
 *     <li> de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json
 *     <li> de/svws_nrw/asd/validate/lehrer/Testdaten_001_LehrerStammdaten.json
 *   </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 * CoreType: LehrerStammdaten
 */
@DisplayName("Tests ValidatorLsn08LehrerStammdatenNachname")
class TestValidatorLsn08LehrerStammdatenNachname {

	private static final String TESTDATEN_NACHNAME = """
			'Herrmann'     , true
			'Herr Müller'  , false
			'Frau Schmidt' , false
			'Herr'         , true
			'Frau'         , true
			'Meier'        , true
		""";


	/** Stammdaten des Lehrers */
	static final LehrerStammdaten lehrerTestdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/lehrer/Testdaten_001_LehrerStammdaten.json", LehrerStammdaten.class);

	/** Stammdaten der Schule */
	static final SchuleStammdaten schuleTestdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json", SchuleStammdaten.class);

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
	 * Test von ValidatorLsn08LehrerStammdatenNachname
	 *
	 * CoreType: LehrerStammdaten
	 *
	 * @param nachname   der Nachname, welcher bei den eingelesenen Testdaten ersetzt wird
	 * @param result     gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLehrerStammdatenNachname")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_NACHNAME)
	void testValidatorLsn08LehrerStammdatenNachname(final String nachname, final boolean result) {
		// Testdaten setzen
		lehrerTestdaten_001.nachname = nachname;

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLsn08LehrerStammdatenNachname validator = new ValidatorLsn08LehrerStammdatenNachname(lehrerTestdaten_001, kontext);
		assertEquals(result, validator.run());
	}


}
