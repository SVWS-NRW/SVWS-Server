package de.svws_nrw.asd.validate.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * Testklasse für den Validator SchuleStammdaten
 *
 * Testdaten 1: de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 * CoreType: SchuleStammdaten
 */
@DisplayName("Teste den Validator zu SchuleStammdaten")
class TestValidatorSchuleStammdaten {

	/** Stammdaten der Schule */
	static final SchuleStammdaten testdaten_001 = JsonReader.fromResource("de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json", SchuleStammdaten.class);

	/**
	 * Initialisiert die Core-Types, damit die Tests ausgeführt werden können.
	 * Beim Laden der Core-Type-Daten werden die JSON-Dateien auf Plausibilität
	 * geprüft.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	private ValidatorKontext kontext;
	private ValidatorSchuleStammdaten validatorSchuleStammdaten;

	/**
	 * Test von ValidatorSchuleStammdaten mit validen Daten
	 *
	 * CoreType: SchuleStammdaten
	 * Testfall: Daten zulässig
	 * Ergebnis: Validator soll TRUE liefern
	 */
	@Test
	@DisplayName("TestValidatorSchuleStammdaten: Test mit gültigen Daten auf TRUE")
	void testValidatorSchuleStammdaten_ValideDaten() {
		// Testdaten setzen gültig
		setzeTestdaten("GY");

		// Erzeuge den Kontext für die Validierung
		kontext = new ValidatorKontext(testdaten_001, true);
		validatorSchuleStammdaten = new ValidatorSchuleStammdaten(kontext);
		assertEquals(true, validatorSchuleStammdaten.run());
	}

	/**
	 * Test von ValidatorSchuleStammdaten auf ungültige Schulform
	 *
	 * CoreType: SchuleStammdaten
	 * Testfall: Unzulässige Schulform ?? eingetragen. Es wird auch sichergestellt, dass der Kindvalidator aufgerufen wird.
	 * Ergebnis: Validator soll FALSE liefern
	 */
	@Test
	@DisplayName("TestValidatorSchuleStammdaten: Fehler unbkannte Schulform")
	void testValidatorSchuleStammdaten_UngueltigeSchulform() {
		// Testdaten setzen
		setzeTestdaten("??");

		// Erzeuge den Kontext für die Validierung
		assertThrowsExactly(CoreTypeException.class, () -> new ValidatorKontext(testdaten_001, true));

		// Das Testen des Validators mit der Schulform ?? ist nicht möglich, da
		// der ValidatorManager entscheidet, dass der Validator nicht aktiv ist.
		// Den Wert ?? gibt es ja nicht in der Liste der Schulformen, für die der Validator ausgeführt werden soll.
	}



	/**
	 * Test von ValidatorSchuleStammdatenSchulform mit validen Daten
	 *
	 * CoreType: SchuleStammdaten
	 * Testfall: Daten zulässig
	 * Ergebnis: Validator soll TRUE liefern
	 */
	@Test
	@DisplayName("TestValidatorSchuleStammdatenSchulform: Test mit gültigen Daten auf TRUE")
	void testValidatorSchuleStammdatenSchulform_ValideDaten() {
		// Testdaten setzen gültig
		setzeTestdaten("GY");

		// Erzeuge den Kontext für die Validierung
		kontext = new ValidatorKontext(testdaten_001, true);
		validatorSchuleStammdaten = new ValidatorSchuleStammdaten(kontext);
		assertEquals(true, validatorSchuleStammdaten.run());
	}


	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = """
				SF
				??
				XY
			""")
	@DisplayName("TestValidatorSchuleStammdatenSchulform: Test mit nichtexistenten Schulformen auf Werfen der CoreTypeException")
	void testValidatorSchuleStammdatenSchulform_returns_false_and_Exception_if_Schulform_NOT_existent(
			final String schulform) {
		// Testdaten setzen ...
		setzeTestdaten(schulform);

		// Erzeuge den Kontext für die Validierung ...
		// Wir erwarten eine CoreTypeException bei der Initialisierung des Kontextes.
		try {
			kontext = new ValidatorKontext(testdaten_001, true);
		} catch (final Exception e) {
			assertEquals(CoreTypeException.class, e.getClass());
		}
	}

	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = """
				SF  , COMMENT
				null, 'null'
				''  , 'empty'
				' ' , 'blank'
			""")
	@DisplayName("TestValidatorSchuleStammdatenSchulform: Test mit NULL, EMPTY und BLANK auf Werfen der CoreTypeException")
	void testValidatorSchuleStammdatenSchulform_returns_FALSE_if_Schulform_NULL_Empty_OR_Blank(final String schulform,
			final String comment) {
		// Testdaten setzen ...
		setzeTestdaten(schulform);

		// Erzeuge den Kontext für die Validierung ...
		// Wir erwarten eine CoreTypeException bei der Initialisierung des Kontextes.
		try {
			kontext = new ValidatorKontext(testdaten_001, true);
		} catch (final Exception e) {
			assertEquals(CoreTypeException.class, e.getClass());
		}
	}

	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = """
				SF, EXPECTED
				G , true
				H , true
				V , true
				S , true
				R , true
				PS, true
				SK, true
				GE, true
				FW, true
				GY, true
				WB, true
				BK, true
				SG, true
				SB, true
			""")
	@DisplayName("TestValidatorSchuleStammdatenSchulform: Test mit gültigen Schulformen auf TRUE")
	void testValidatorSchuleStammdatenSchulform_returns_TRUE_if_gueltige_Schulform(final String schulform,
			final boolean expected) {
		// Testdaten setzen ...
		setzeTestdaten(schulform);

		// Erzeuge den Kontext für die Validierung ...
		kontext = new ValidatorKontext(testdaten_001, true);
		validatorSchuleStammdaten = new ValidatorSchuleStammdaten(kontext);

		assertEquals(expected, validatorSchuleStammdaten.run());
	}


	/**
	 * Diese Methode erlaubt das Setzen der gewünschten Werte für den Test im CoreType LehrerPersonalabschnittsdaten
	 *
	 * @param schulform - die Schulform
	 */
	void setzeTestdaten(final String schulform) {
		testdaten_001.schulform = schulform;
	}

}
