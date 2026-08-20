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
 * Testklasse für den Validator {@link ValidatorIka01IntKatalogKonfessionenAsdKatalog}.
 *
 * Die Testdaten werden aus einer statischen JSON-Ressource eingelesen.
 */
@DisplayName("Tests ValidatorIka01IntKatalogKonfessionenAsdKatalog")
class TestValidatorIka01IntKatalogKonfessionenAsdKatalog {

	private static final String TESTDATEN = """
			2000 , true
			-1   , false
		""";

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	/**
	 * Initialisiert die Core-Types, damit die Tests ausgeführt werden können.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	/**
	 * Test von ValidatorIka01IntKatalogKonfessionenAsdKatalog.
	 *
	 * @param idKatalog   die Katalog-ID
	 * @param result      gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorIka01IntKatalogKonfessionenAsdKatalog(final Long idKatalog, final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorIka01IntKatalogKonfessionenAsdKatalog validator =
				new ValidatorIka01IntKatalogKonfessionenAsdKatalog(() -> idKatalog, kontext);
		assertEquals(result, validator.pruefe());
	}

}
