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
 *   <li> {@link ValidatorIaa01IntKatalogFaecherAsdKatalog}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorIaa01IntKatalogFaecherAsdKatalog")
class TestValidatorIaa01IntKatalogFaecherAsdKatalog {

	private static final String TESTDATEN = """
			VF    , false
			AB    , true
			ZZ    , false
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
	 * Test von ValidatorIaa01IntKatalogFaecherAsdKatalog
	 *
	 * CoreType: Fach
	 *
	 * @param kuerzelStatistik    Fach ASD-Kürzel
	 * @param result              gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorIaa01IntKatalogFaecherAsdKatalog")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorIaa01IntKatalogFaecherAsdKatalog(final String kuerzelStatistik, final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorIaa01IntKatalogFaecherAsdKatalog validator =
				new ValidatorIaa01IntKatalogFaecherAsdKatalog(() -> kuerzelStatistik, kontext);
		assertEquals(result, validator.pruefe());
	}


}
