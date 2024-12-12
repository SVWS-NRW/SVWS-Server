package de.svws_nrw.asd.validate.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

@DisplayName("Teste den Validator zu SchuleStammdatenSchulform")
class TestValidatorSchuleStammdatenSchulform {

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	final SchuleStammdaten schuleStammdaten = JsonReader.fromResource(
			"de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json", SchuleStammdaten.class);
	private ValidatorSchuleStammdatenSchulform validatorSchuleStammdatenSchulform;

	private ValidatorKontext kontext;

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
		schuleStammdaten.schulform = schulform;

		// Erzeuge den Kontext für die Validierung ...
		// Wir erwarten eine CoreTypeException bei der Initialisierung des Kontextes.
		try {
			kontext = new ValidatorKontext(schuleStammdaten, true);
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
		schuleStammdaten.schulform = schulform;

		// Erzeuge den Kontext für die Validierung ...
		// Wir erwarten eine CoreTypeException bei der Initialisierung des Kontextes.
		try {
			kontext = new ValidatorKontext(schuleStammdaten, true);
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
		schuleStammdaten.schulform = schulform;

		// Erzeuge den Kontext für die Validierung ...
		kontext = new ValidatorKontext(schuleStammdaten, true);
		validatorSchuleStammdatenSchulform = new ValidatorSchuleStammdatenSchulform(kontext);

		assertEquals(expected, validatorSchuleStammdatenSchulform.run());
	}

}
