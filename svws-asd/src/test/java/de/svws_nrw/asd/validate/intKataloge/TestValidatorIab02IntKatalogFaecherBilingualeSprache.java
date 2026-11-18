package de.svws_nrw.asd.validate.intKataloge;

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
 *   <li> {@link ValidatorIab02IntKatalogFaecherBilingualeSprache},
 * </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * CoreType: Jahrgaenge
 */
@DisplayName("Tests ValidatorIab02IntKatalogFaecherBilingualeSprache")
class TestValidatorIab02IntKatalogFaecherBilingualeSprache {

	private static final String TESTDATEN = """
		1000, 2032, false
		1001, 2026, true
		5,    2014, false
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
	 * Test von ValidatorIab02IntKatalogFaecherBilingualeSprache
	 *
	 * CoreType: Jahrgaenge
	 *
	 * @param idBilingualeSprache    idBilingualeSprache
	 * @param schuljahr              das Schuljahr
	 * @param result                 gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorIab02IntKatalogFaecherBilingualeSprache")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN)
	void testValidatorIab02IntKatalogFaecherBilingualeSprache(final Long idBilingualeSprache, final Integer schuljahr,
			final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		//		Setzen Schuljahr
		kontext.getSchuljahresabschnitt().schuljahr = schuljahr;

		final ValidatorIab02IntKatalogFaecherBilingualeSprache validator =
				new ValidatorIab02IntKatalogFaecherBilingualeSprache(() -> idBilingualeSprache, kontext);

		assertEquals(result, validator.pruefe());

	}

}
