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
 * <p> Testklasse für den Validator
 * <ul>
 *   <li> {@link ValidatorUf02UnterrichtsverteilungsdatenFach},
 * </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 */
@DisplayName("Tests zur Validierung der ValidatorUf02UnterrichtsverteilungsdatenFach")
class TestValidatorUf02UnterrichtsverteilungsdatenFach {

	private static final String TESTDATEN_KLASSENART = """
		39000000, 1950, true
		39000000, 2025, false
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
	 * Test von ValidatorUf02UnterrichtsverteilungsdatenFach
	 *
	 * CoreType: Fach
	 *
	 * @param idfach     ID Fach
	 * @param schuljahr  das Schuljahr
	 * @param result     gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorUf02UnterrichtsverteilungsdatenFach")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_KLASSENART)
	void testValidatorSle02SchuelerLernabschnittsdatenEpJahre(final Long idfach, final Integer schuljahr,
			final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		//		Setzen Schuljahr
		kontext.getSchuljahresabschnitt().schuljahr = schuljahr;

		final ValidatorUf02UnterrichtsverteilungsdatenFach validator =
				new ValidatorUf02UnterrichtsverteilungsdatenFach(
						() -> idfach,
						kontext);

		assertEquals(result, validator.pruefe());

	}

}
