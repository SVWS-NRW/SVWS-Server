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
 *   <li> {@link ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit},
 * </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * CoreType: Klassenart
 */
@DisplayName("Tests zur Validierung der ValidatorSses02SchuelerStammdatenErsteStaatsangehoerigkeit")
class TestValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit {

	private static final String TESTDATEN_KLASSENART = """
		68090065, 2018, true
		68090065, 1960, false
		68069085, 1950, true
		68069085, 1940, false
		89085071, 1970, true
		89085071, 2021, false
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
	 * Test von ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit
	 *
	 * CoreType: Klassenart
	 *
	 * @param idStaatsangehoerigkeit  ID Staatsangehörigkeit
	 * @param schuljahr  	          das Schuljahr
	 * @param result                  gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_KLASSENART)
	void testValidatorSlk02SchuelerLernabschnittsdatenKlassenart(final Long idStaatsangehoerigkeit, final Integer schuljahr,
			final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		//		Setzen Schuljahr
		kontext.getSchuljahresabschnitt().schuljahr = schuljahr;

		final ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit validator =
				new ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit(
						() -> idStaatsangehoerigkeit,
						kontext);

		assertEquals(result, validator.pruefe());

	}

}
