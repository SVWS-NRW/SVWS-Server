package de.svws_nrw.asd.validate.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * Testklasse für den Validator Sss00SchuleStammdatenSchulform
 *
 * Testdaten 1: de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 * CoreType: SchuleStammdaten
 */
@DisplayName("Teste den Validator zu Sss00SchuleStammdatenSchulform")
class TestValidatorSss00SchuleStammdatenSchulform {

	/** Stammdaten der Schule */
	static final SchuleStammdaten testdaten_001 = JsonReader.fromResource("de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json", SchuleStammdaten.class);

	private static final String TESTDATEN_SSS00 = """
			'GY'  , true
	        """;

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
	 * Test von ValidatorSchuleStammdaten mit validen Daten
	 *
	 * @param schulform 	die Schulform
	 * @param result     	gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("TestValidatorSss00SchuleStammdatenSchulform: Test mit gültigen Daten auf TRUE")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_SSS00)

	void testValidatorSchuleStammdaten_ValideDaten(final String schulform, final boolean result) {
		// Testdaten setzen
		testdaten_001.schulform = schulform;

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(testdaten_001, true);
		final ValidatorSss00SchuleStammdatenSchulform validator = new ValidatorSss00SchuleStammdatenSchulform(kontext);

		assertEquals(result, validator.run());
	}



}
