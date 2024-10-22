package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

@DisplayName("Teste den Validator zu LehrerStammdaten")
class TestValidatorLehrerStammdaten {

	static final SchuleStammdaten schuleTestdaten_001 = JsonReader.fromResource("de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json", SchuleStammdaten.class);
	static final LehrerStammdaten lehrerTestdaten_001 = JsonReader.fromResource("de/svws_nrw/asd/validate/lehrer/Testdaten_001_LehrerStammdaten.json", LehrerStammdaten.class);
	static final LehrerStammdaten lehrerTestdaten_002 = JsonReader.fromResource("de/svws_nrw/asd/validate/lehrer/Testdaten_002_LehrerStammdaten_Fehler_LeitungsID.json", LehrerStammdaten.class);

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
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdaten validator = new ValidatorLehrerStammdaten(lehrerTestdaten_001, kontext);
		assertEquals(true, validator.run());
	}


	@Test
	void test002() {
		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLehrerStammdaten validator = new ValidatorLehrerStammdaten(lehrerTestdaten_002, kontext);
		assertEquals(true, validator.run());
	}

}
