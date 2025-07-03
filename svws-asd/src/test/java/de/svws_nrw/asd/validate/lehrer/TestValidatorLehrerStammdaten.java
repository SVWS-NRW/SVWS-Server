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
 *   <li> {@link ValidatorLehrerStammdaten},
 *   <li> {@link ValidatorLehrerStammdatenNachname}
 *   <li> {@link ValidatorLehrerStammdatenVorname}
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
@DisplayName("Tests zur Validierung der LehrerStammdaten")
class TestValidatorLehrerStammdaten {

	/** Stammdaten der Schule */
	static final SchuleStammdaten schuleTestdaten_001 = JsonReader.fromResource("de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json", SchuleStammdaten.class);
	/** Stammdaten des Lehrers */
	static final LehrerStammdaten lehrerTestdaten_001 = JsonReader.fromResource("de/svws_nrw/asd/validate/lehrer/Testdaten_001_LehrerStammdaten.json", LehrerStammdaten.class);

	/**
	 * Initialisiert die Core-Types, damit die Tests ausgeführt werden können.
	 * Beim Laden der Core-Type-Daten werden die JSON-Dateien auf Plausibilität
	 * geprüft.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}


	private static final String TESTDATEN_NACHNAME = """
			''          , 'Frank', false
			'Müller'    , 'Frank', true
			' Müller'   , 'Frank', false
			'MÜller'    , 'Frank', false
			'von Müller', 'Frank', true
			'von MÜller', 'Frank', false
			'mÜller'    , 'Frank', false
			'von mÜller', 'Frank', false
			'Thor'      , 'Frank', true
			'thor'      , 'Frank', false
		""";

	private static final String TESTDATEN_VORNAME = """
			'Müller', ''              , false
			'Müller', 'Frank'         , true
			'Müller', ' Linksbündig'  , false
			'Müller', 'GRoßbuchstaben', false
			'Müller', 'GROßbuchstaben', false
			'Müller', 'GrOßbuchstaben', false
			'Müller', 'kleinbuchstabe', false
			'Müller', 'Thor Hendrik'  , true
		""";


	/**
	 * Test von ValidatorLehrerStammdatenNachname
	 *
	 * CoreType: LehrerStammdaten
	 *
	 * @param nachname   der Nachname, welcher bei den eingelesenen Testdaten ersetzt wird
	 * @param vorname    der Vorname, welcher bei den eingelesenen Testdaten ersetzt wird
	 * @param result     gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLehrerStammdatenNachname")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_NACHNAME)
	void testValidatorLehrerStammdatenNachname(final String nachname, final String vorname, final boolean result) {
		// Testdaten setzen
		lehrerTestdaten_001.nachname = nachname;
		lehrerTestdaten_001.vorname = vorname;

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdatenNachname validator = new ValidatorLehrerStammdatenNachname(lehrerTestdaten_001, kontext);
		assertEquals(result, validator.run());
	}

	/**
	 * Test von ValidatorLehrerStammdatenVorname
	 *
	 * CoreType: LehrerStammdaten
	 *
	 * @param nachname   der Nachname, welcher bei den eingelesenen Testdaten ersetzt wird
	 * @param vorname    der Vorname, welcher bei den eingelesenen Testdaten ersetzt wird
	 * @param result     gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLehrerStammdatenVorname")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_VORNAME)
	void testValidatorLehrerStammdatenVorname(final String nachname, final String vorname, final boolean result) {
		// Testdaten setzen
		lehrerTestdaten_001.nachname = nachname;
		lehrerTestdaten_001.vorname = vorname;

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdatenVorname validator = new ValidatorLehrerStammdatenVorname(lehrerTestdaten_001, kontext);
		assertEquals(result, validator.run());
	}


	/**
	 * Test von ValidatorLehrerStammdaten
	 *
	 * @param nachname   der Nachname, welcher bei den eingelesenen Testdaten ersetzt wird
	 * @param vorname    der Vorname, welcher bei den eingelesenen Testdaten ersetzt wird
	 * @param result     gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLehrerStammdaten")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_VORNAME + TESTDATEN_NACHNAME)
	void testValidatorLehrerStammdaten(final String nachname, final String vorname, final boolean result) {
		// Testdaten setzen
		lehrerTestdaten_001.nachname = nachname;
		lehrerTestdaten_001.vorname = vorname;

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdaten validator = new ValidatorLehrerStammdaten(lehrerTestdaten_001, kontext);
		assertEquals(result, validator.run());
	}

}
