package de.svws_nrw.asd.validate.schueler;

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
 * Testklasse für den Validator SSN00 (Pflichtfeld Nachname)
 */
@DisplayName("Tests zur Validierung von Ssn00SchuelerStammdatenNachname")
class TestValidatorSsn00SchuelerStammdatenNachname {

	private static final String TESTDATEN_NACHNAME_PFLICHT = """
            null         , false
            ''           , false
            ' '          , false
            'Mustermann' , true
            """;

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

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
	 * Test von ValidatorSsn00SchuelerStammdatenNachname
	 *
	 * CoreType: SchuelerStammdaten
	 *
	 * @param nachname      der Wert für den Nachname
	 * @param result        gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSsn00SchuelerStammdatenNachname")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_NACHNAME_PFLICHT, nullValues = { "null" })
	void testValidatorSsn00SchuelerStammdatenNachname(final String nachname, final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorSsn00SchuelerStammdatenNachname validator =
				new ValidatorSsn00SchuelerStammdatenNachname(() -> nachname, kontext);

		assertEquals(result, validator.pruefe());
	}
}
