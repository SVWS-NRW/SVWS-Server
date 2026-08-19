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
 * Testklasse für den Validator {@link ValidatorUzw10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden}.
 */
@DisplayName("Teste den Validator UZW10: Wochenstunden für zusätzliche Lehrkräfte")
class TestValidatorUzw10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden {

	/** Stammdaten der Schule für den Kontext. */
	private static final StatistikGesamt testdaten_001 = JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	/**
	 * Testdaten für die Wochenstunden für zusätzliche Lehrkräfte :
	 * Spalte 1: wochenstunden (Double-Wert oder null)
	 * Spalte 2: erwartetes Ergebnis (boolean)
	 */
	private static final String UZW10_TESTDATEN = """
			0.2, true
			-0.2, false
			0.0, true
			""";

	/**
	 * Initialisiert die Core-Types für die Tests.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("UZW10: Test der Wochenstunden für zusätzliche Lehrkräfte")
	@ParameterizedTest(name = "wochenstunden={0} -> erwartet {1}")
	@CsvSource(textBlock = UZW10_TESTDATEN, nullValues = { "null" })
	void testValidatorUzw10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden(final Double wochenstunden, final boolean result) {
		final ValidatorKontext kontext = new ValidatorKontext(
				testdaten_001.schule.schulNr,
				Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
				testdaten_001.schule.abschnitte,
				testdaten_001.schule.idSchuljahresabschnitt,
				true);

		final ValidatorUzw10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden validator =
				new ValidatorUzw10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden(
						() -> wochenstunden,
						kontext);

		assertEquals(result, validator.pruefe());
	}
}
