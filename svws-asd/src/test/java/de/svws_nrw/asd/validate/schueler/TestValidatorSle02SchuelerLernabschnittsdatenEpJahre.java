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
 *   <li> {@link ValidatorSle02SchuelerLernabschnittsdatenEpJahre},
 * </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * CoreType: Klassenart
 */
@DisplayName("Tests zur Validierung der ValidatorSle02SchuelerLernabschnittsdatenEpJahre")
class TestValidatorSle02SchuelerLernabschnittsdatenEpJahre {

//	Die JSON PrimarstufeSchuleingangsphaseBesuchsjahre beinhaltet leider keinen Fall, der zeitlich begrenzt ist
//	daher ist eine Prüfung auf false hier nicht möglich.
	private static final String TESTDATEN_KLASSENART = """
		1, 1950, true
		1, 2018, true
		2, 1950, true
		2, 2018, true
		3, 1950, true
		3, 2018, true
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
	 * Test von ValidatorSle02SchuelerLernabschnittsdatenEpJahre
	 *
	 * CoreType: Klassenart
	 *
	 * @param idEpJahre  ID EPJahre
	 * @param schuljahr  das Schuljahr
	 * @param result     gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSle02SchuelerLernabschnittsdatenEpJahre")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_KLASSENART)
	void testValidatorSle02SchuelerLernabschnittsdatenEpJahre(final Long idEpJahre, final Integer schuljahr,
			final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		//		Setzen Schuljahr
		kontext.getSchuljahresabschnitt().schuljahr = schuljahr;

		final ValidatorSle02SchuelerLernabschnittsdatenEpJahre validator =
				new ValidatorSle02SchuelerLernabschnittsdatenEpJahre(
						() -> idEpJahre,
						kontext);

		assertEquals(result, validator.pruefe());

	}

}
