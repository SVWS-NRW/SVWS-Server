package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * Testklasse für den Validator LehrerStammdaten
 *
 * Testdaten 2: de/svws_nrw/asd/validate/lehrer/Testdaten_002_LehrerPersonalabschnittsdaten.json
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * CoreType: LehrerPersonalabschnittsdaten
 */
@DisplayName("Teste den Validator zu Lpp01LehrerPersonalabschnittsdatenPflichstundensoll")
class TestValidatorLpp01LehrerPersonalabschnittsdatenPflichtstundensoll {

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	private static final String TESTDATEN_LPP1 = """
45, false
12, true
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
	 * Test von ValidatorLpp1LehrerPersonalabschnittsdatenPflichtstundensoll
	 *
	 * @param pflichtstundensoll der Doublewert für die Stunden (z. B. 27.5)
	 * @param result     gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Test für ValidatorLpp01LehrerPersonalabschnittsdatenPflichtstundensoll")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_LPP1)
	void testValidatorLpp1LehrerPersonalabschnittsdatenPflichtstundensoll(final double pflichtstundensoll, final boolean result) {
		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
				testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorLppp10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll validator = new ValidatorLppp10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(
				() -> pflichtstundensoll,
				kontext);
		assertEquals(result, validator.run());
	}

}
