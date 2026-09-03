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
 * Testklasse für den Validator {@link ValidatorUlw10UnterrichtsverteilungsdatenLehrkraefteWochenstunden}.
 */
@DisplayName("Teste den Validator ULW10: Wochenstunden für Lehrkräfte")
class TestValidatorUlw10UnterrichtsverteilungsdatenLehrkraefteWochenstunden {

	/** Stammdaten der Schule für den Kontext. */
	private static final StatistikGesamt testdaten_001 = JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	/**
	 * Testdaten für die Wochenstunden für zusätzliche Lehrkräfte :
	 * Spalte 1: wochenstunden (Integer-Wert oder null)
	 * Spalte 2: erwartetes Ergebnis (boolean)
	 */
	private static final String ULW10_TESTDATEN = """
			2, true
			-2, false
			0, true
			""";

	/**
	 * Initialisiert die Core-Types für die Tests.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("ULW10: Test der Wochenstunden für Lehrkräfte")
	@ParameterizedTest(name = "wochenstunden={0} -> erwartet {1}")
	@CsvSource(textBlock = ULW10_TESTDATEN, nullValues = { "null" })
	void testValidatorUlw10UnterrichtsverteilungsdatenLehrkraefteWochenstunden(final Integer wochenstunden, final boolean result) {
		final ValidatorKontext kontext = new ValidatorKontext(
				testdaten_001.schule.schulNr,
				Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
				testdaten_001.schule.abschnitte,
				testdaten_001.schule.idSchuljahresabschnitt,
				true);

		final ValidatorUlw10UnterrichtsverteilungsdatenLehrkraefteWochenstunden validator =
				new ValidatorUlw10UnterrichtsverteilungsdatenLehrkraefteWochenstunden(
						() -> wochenstunden,
						kontext);

		assertEquals(result, validator.pruefe());
	}
}
