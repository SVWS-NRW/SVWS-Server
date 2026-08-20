package de.svws_nrw.asd.validate.kurse;

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
 * <p> Testklasse für den Validator ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft
 */
@DisplayName("Tests zur Validierung von ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft")
class TestValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft {

	private static final String TESTDATEN = """
	        null  , false
	        4     , true
	        7     , true
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
	 * Test von ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft
	 *
	 * CoreType: Kurse
	 *
	 * @param idLehrkraft  die ID der Lehrkraft
	 * @param result       gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft(final Long idLehrkraft, final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft validator =
				new ValidatorUll00UnterrichtsverteilungsdatenLehrkraefteLehrkraft(() -> idLehrkraft, null, kontext);

		assertEquals(result, validator.pruefe());

	}

}
