package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
 * CoreType: LehrerPersonalabschnittsdaten
 */

@DisplayName("Teste den Validator zu Lpp03LehrerPersonalabschnittsdatenPflichtstundensoll")
class TestValidatorLpp03LehrerPersonalabschnittsdatenPflichtstundensoll {

	/** Stammdaten der Schule */
	static final SchuleStammdaten schuleTestdaten_001 = JsonReader.fromResource(
			"de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json", SchuleStammdaten.class);
	/** Personalabschnittsdaten des Lehrers */
	static final LehrerPersonalabschnittsdaten LehrerPersonalabschnittsdaten_Plausibel = JsonReader.fromResource(
			"de/svws_nrw/asd/validate/lehrer/Testdaten_Plausibel_LehrerPersonalabschnittsdaten.json",
			LehrerPersonalabschnittsdaten.class);

	private static final String TESTDATEN_LPP03 = """
	        0, 'A', 'WV'  , true
	        0, 'A', 'WT'  , true
	        1.5, 'A', 'WT'  , true
	        0, 'X', 'WT'  , true
	        0, 'A', 'XX'  , true
	        0, 'X', 'XX'  , false
	        0, 'X', 'XX'  , false
	        0, 'X', 'wr'  , false
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
	 * Test von ValidatorLehrerStammdatenPflichtstundensoll
	 *
	 * CoreType: LehrerPersonalabschnittsdaten
	 *
	 * @param pflichtstundensoll der Doublewert für die Stunden (z. B. 27.5)
	 * @param einsatzstatus
	 * @param beschaeftigungsart
	 * @param result     gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLpp03LehrerPersonalabschnittsdatenPflichtstundensoll")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_LPP03)
	void testValidatorLpp3LehrerPersonalabschnittsdaten(final double pflichtstundensoll, final String einsatzstatus, final String beschaeftigungsart, final boolean result) {
		// Testdaten setzen
		LehrerPersonalabschnittsdaten_Plausibel.pflichtstundensoll = pflichtstundensoll;
		LehrerPersonalabschnittsdaten_Plausibel.einsatzstatus = einsatzstatus;
		LehrerPersonalabschnittsdaten_Plausibel.beschaeftigungsart = beschaeftigungsart;

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLppp03LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll validator =
				new ValidatorLppp03LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(LehrerPersonalabschnittsdaten_Plausibel, kontext);

		assertEquals(result, validator.run());

	}

}
