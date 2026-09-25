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
 *   <li> {@link ValidatorIjja01IntKatalogJahrgaengeJahrgangAsdKatalog}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorIjja01IntKatalogJahrgaengeJahrgangAsdKatalog")
class TestValidatorIjja01IntKatalogJahrgaengeJahrgangAsdKatalog {

	private static final String TESTDATEN = """
			1000000   , true
			512       , false
			-1        , false
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
	 * Test von ValidatorIjja01IntKatalogJahrgaengeJahrgangAsdKatalog
	 *
	 * CoreType: Jahrgaenge
	 *
	 * @param idKatalog   die KatalogID, welche bei den eingelesenen Testdaten ersetzt wird
	 * @param result      gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorIjja01IntKatalogJahrgaengeJahrgangAsdKatalog")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorIjja01IntKatalogFoerderschwerpunkteAsdKatalog(final Long idKatalog, final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorIjja01IntKatalogJahrgaengeJahrgangAsdKatalog validator =
				new ValidatorIjja01IntKatalogJahrgaengeJahrgangAsdKatalog(() -> idKatalog, kontext);
		assertEquals(result, validator.pruefe());
	}


}
