package de.svws_nrw.asd.validate.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

@DisplayName("Teste den Validator zu SchuleStammdaten")
class TestValidatorSchuleStammdaten {

	static final SchuleStammdaten testdaten_001 = JsonReader.fromResource("de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json", SchuleStammdaten.class);
	static final SchuleStammdaten testdaten_002 = JsonReader.fromResource("de/svws_nrw/asd/validate/schule/Testdaten_002_SchuleStammdaten_Fehler_Schulform.json", SchuleStammdaten.class);

	/**
	 * Initialisiert die Core-Types, damit die Tests ausgeführt werden können.
	 * Beim Laden der Core-Type-Daten werden die JSON-Dateien auf Plausibilität
	 * geprüft.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}


	@Test
	void test001() {
		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(testdaten_001, true);
		final ValidatorSchuleStammdaten validator = new ValidatorSchuleStammdaten(testdaten_001, kontext);
		assertEquals(true, validator.run());
	}


	@Test
	void test002() {
		// Erzeuge den Kontext für die Validierung
		assertThrowsExactly(CoreTypeException.class, () -> new ValidatorKontext(testdaten_002, true));
	}

}
