package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
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
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 * CoreType: LehrerStammdaten
 */
@DisplayName("Teste den Validator zu LehrerPersonalabschnittsdaten")
class TestValidatorLppp10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll {

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	/** Personalabschnittsdaten des Lehrers */
	static final LehrerPersonalabschnittsdaten LehrerPersonalabschnittsdaten_Plausibel = JsonReader.fromResource(
			"de/svws_nrw/asd/validate/lehrer/Testdaten_Plausibel_LehrerPersonalabschnittsdaten.json",
			LehrerPersonalabschnittsdaten.class);


	/**
	 * Testdaten
	 */
	private static final String LPPP10_TESTDATEN = """
		25.5, true
		80.5, false
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


	@DisplayName("ValidatorLppp10LehrerPersonaldatenPersonalabschnittsdatenPflichstundensoll: Test für Pflichtstundensoll")
	@ParameterizedTest
	@CsvSource(textBlock = LPPP10_TESTDATEN)
	void testValidatorLppp10LehrerPersonaldatenPersonalabschnittsdatenPflichstundensoll(final double pflichtstundensoll, final boolean result) {
		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorLppp10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll validator =
				new ValidatorLppp10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(
						() -> pflichtstundensoll,
						kontext);
		assertEquals(result, validator.run());
	}

}
