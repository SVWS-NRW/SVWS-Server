package de.svws_nrw.asd.validate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.schule.SchuleStatistikdatenGesamt;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;

/**
 * Testet die Validierung der Gesamt-Statistikdaten von Schulen
 */
@DisplayName("Teste den Validator für die Gesamt-Statistikdaten von Schulen")
class TestValidatorGesamt {

	static final SchuleStatistikdatenGesamt testdaten_001 = JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_SchuleStatistikdatenGesamt.json", SchuleStatistikdatenGesamt.class);

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
		final ValidatorKontext kontext = new ValidatorKontext(testdaten_001.stammdaten, true);
		final ValidatorGesamt validator = new ValidatorGesamt(testdaten_001, kontext);
		assertEquals(true, validator.run());
	}

}
