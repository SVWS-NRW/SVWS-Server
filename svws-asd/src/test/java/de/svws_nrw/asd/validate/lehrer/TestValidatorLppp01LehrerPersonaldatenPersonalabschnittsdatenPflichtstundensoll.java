package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * Testklasse für den Validator LehrerStammdaten
 *
 * Testdaten 1: de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json
 * Testdaten 2: de/svws_nrw/asd/validate/lehrer/Testdaten_002_LehrerPersonalabschnittsdaten.json
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 * CoreType: LehrerStammdaten
 */
@DisplayName("Teste den Validator zu LehrerPersonalabschnittsdaten")
class TestValidatorLppp01LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll {

	/** Stammdaten der Schule */
	static final SchuleStammdaten schuleTestdaten_001 = JsonReader.fromResource(
			"de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json", SchuleStammdaten.class);
	/** Personalabschnittsdaten des Lehrers */
	static final LehrerPersonalabschnittsdaten LehrerPersonalabschnittsdaten_Plausibel = JsonReader.fromResource(
			"de/svws_nrw/asd/validate/lehrer/Testdaten_Plausibel_LehrerPersonalabschnittsdaten.json",
			LehrerPersonalabschnittsdaten.class);


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
	 * Test von ValidatorLppp01LehrerPersonaldatenPersonalabschnittsdatenPflichstundensoll mit validen Daten
	 *
	 * CoreType: LehrerPersonalabschnittsdaten
	 * Testfall: Daten zulässig
	 * Ergebnis: Validator soll TRUE liefern
	 */
	@DisplayName("ValidatorLppp01LehrerPersonaldatenPersonalabschnittsdatenPflichstundensoll: Test für Pflichtstundensoll")
	@Test
	void testValidatorLppp01LehrerPersonaldatenPersonalabschnittsdatenPflichstundensoll() {
		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLppp01LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll validator = new ValidatorLppp01LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(LehrerPersonalabschnittsdaten_Plausibel, kontext);

		// Testdaten setzen
		LehrerPersonalabschnittsdaten_Plausibel.pflichtstundensoll = 25.5;
		assertEquals(true, validator.run());

		// Testdaten setzen
		LehrerPersonalabschnittsdaten_Plausibel.pflichtstundensoll = 80.5;
		assertEquals(false, validator.run());
	}

}
