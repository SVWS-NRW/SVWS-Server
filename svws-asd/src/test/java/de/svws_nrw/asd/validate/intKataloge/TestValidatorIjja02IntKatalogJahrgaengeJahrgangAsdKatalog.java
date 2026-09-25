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
 *   <li> {@link ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog},
 * </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * CoreType: Jahrgaenge
 */
@DisplayName("Tests ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog")
class TestValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog {

	private static final String TESTDATEN = """
		5500000, 2012, true
		5500000, 2026, false
		200000,  2014, true
		200000,  2015, false
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
	 * Test von ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog
	 *
	 * CoreType: Jahrgaenge
	 *
	 * @param idKatalog  KatalogID
	 * @param schuljahr  das Schuljahr
	 * @param result     gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN)
	void testValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog(final Long idKatalog, final Integer schuljahr,
			final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		//		Setzen Schuljahr
		kontext.getSchuljahresabschnitt().schuljahr = schuljahr;

		final ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog validator =
				new ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog(
						() -> idKatalog,
						kontext);

		assertEquals(result, validator.pruefe());

	}

}
