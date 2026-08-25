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
 * Testklasse für den Validator {@link ValidatorUw10UnterrichtsverteilungsdatenWochenstunden}.
 */
@DisplayName("Teste den Validator UW10: Wochenstunden des Kurses")
class TestValidatorUw10UnterrichtsverteilungsdatenWochenstunden {

	/** Stammdaten der Schule für den Kontext. */
	private static final StatistikGesamt testdaten_001 = JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	/**
	 * Testdaten für die Wochenstunden des Kurses:
	 * Spalte 1: wochenstunden (Double-Wert oder null)
	 * Spalte 2: erwartetes Ergebnis (boolean)
	 */
	private static final String UW10_TESTDATEN = """
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

	@DisplayName("UW10: Test der Wochenstunden des Kurses")
	@ParameterizedTest(name = "wochenstunden={0} -> erwartet {1}")
	@CsvSource(textBlock = UW10_TESTDATEN, nullValues = { "null" })
	void testValidatorUw10UnterrichtsverteilungsdatenWochenstunden(final Double wochenstunden, final boolean result) {
		final ValidatorKontext kontext = new ValidatorKontext(
				testdaten_001.schule.schulNr,
				Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
				testdaten_001.schule.abschnitte,
				testdaten_001.schule.idSchuljahresabschnitt,
				true);

		final ValidatorUw10UnterrichtsverteilungsdatenWochenstunden validator =
				new ValidatorUw10UnterrichtsverteilungsdatenWochenstunden(
						() -> wochenstunden,
						kontext);

		assertEquals(result, validator.pruefe());
	}
}
