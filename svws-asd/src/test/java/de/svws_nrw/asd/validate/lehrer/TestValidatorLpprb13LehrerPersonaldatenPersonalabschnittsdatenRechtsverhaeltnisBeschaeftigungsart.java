package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * Testklasse für ValidatorLpprb13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart
 *
 * CoreType: LehrerStammdaten
 */
@DisplayName("Teste ValidatorLpprb13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart")
class TestValidatorLpprb13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart {

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	private static final String TESTDATEN = """
			'P', 'V', true
			'P', 'X', false
			'L', 'V', true
			'L', 'X', true
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
	 * Test von ValidatorLpprb13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart
	 *
	 * @param rechtsverhaeltnis    Rechtsverhältnis
	 * @param beschaeftigungsart   Beschäftigungsart
	 * @param result               gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLpprb13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorLpprb13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(final String rechtsverhaeltnis, final String beschaeftigungsart, final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final LehrerRechtsverhaeltnis lehrerRechtsverhaeltnis = LehrerRechtsverhaeltnis.getBySchluessel(rechtsverhaeltnis);
		final LehrerBeschaeftigungsart lehrerBeschaeftigungsart = LehrerBeschaeftigungsart.data().getWertBySchluessel(beschaeftigungsart);

		final ValidatorLpprb13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart validator =
				new ValidatorLpprb13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(() -> lehrerRechtsverhaeltnis, () -> lehrerBeschaeftigungsart,  kontext);
		assertEquals(result, validator.pruefe());
	}
}
