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
 * <p> Testklasse für den Validator
 * <ul>
 *   <li> {@link ValidatorSlb02SchuelerLernabschnittsdatenFachklassen},
 * </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * CoreType: SchuelerLernabschnittsdaten
 */
@DisplayName("Tests zur Validierung der ValidatorSlb02SchuelerLernabschnittsdatenFachklassen")
class TestValidatorSlb02SchuelerLernabschnittsdatenFachklassen {

	private static final String TESTDATEN_FACHKLASSE = """
		12000, 2002, true
		12000, 2021, false
		14000, 2018, true
		11111, 2021, false
		11111, 2000, false
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
	 * Test von ValidatorSlb02SchuelerLernabschnittsdatenFachklassen
	 *
	 * CoreType: SchuelerLernabschnittsdaten
	 *
	 * @param fachklasseID    die Fachklassen-ID, welche bei den eingelesenen Testdaten ersetzt wird
	 * @param schuljahr  	  das Schuljahr
	 * @param result          gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSlb02SchuelerLernabschnittsdatenFachklassen")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_FACHKLASSE)
	void testValidatorSlb02SchuelerLernabschnittsdatenFachklassen(final Long fachklasseID, final Integer schuljahr,
			final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorSlb02SchuelerLernabschnittsdatenFachklassen validator =
				new ValidatorSlb02SchuelerLernabschnittsdatenFachklassen(
						() -> schuljahr,
						() -> fachklasseID,
						kontext);

		assertEquals(result, validator.pruefe());

	}

}
