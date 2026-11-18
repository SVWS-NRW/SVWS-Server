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
 * <p> Testklasse für die Validatoren
 * <ul>
 *   <li> {@link ValidatorIab01IntKatalogFaecherBilingualeSprache}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorIab01IntKatalogFaecherBilingualeSprache")
class TestValidatorIab01IntKatalogFaecherBilingualeSprache {

	private static final String TESTDATEN = """
			1001      , true
			5         , false
			null      , false
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
	 * Test von ValidatorIab01IntKatalogFaecherBilingualeSprache
	 *
	 * CoreType: Jahrgaenge
	 *
	 * @param idBilingualeSprache    die idBilingualeSprache
	 * @param result                 gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorIab01IntKatalogFaecherBilingualeSprache")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorIab01IntKatalogFaecherBilingualeSprache(final Long idBilingualeSprache, final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorIab01IntKatalogFaecherBilingualeSprache validator =
				new ValidatorIab01IntKatalogFaecherBilingualeSprache(() -> idBilingualeSprache, kontext);
		assertEquals(result, validator.pruefe());
	}


}
