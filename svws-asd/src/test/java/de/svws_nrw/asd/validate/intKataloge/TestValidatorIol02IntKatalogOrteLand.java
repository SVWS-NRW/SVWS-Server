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
 *   <li> {@link ValidatorIol02IntKatalogOrteLand},
 * </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * CoreType: Foerderschwerpunkt
 */
@DisplayName("Tests ValidatorIol02IntKatalogOrteLand")
class TestValidatorIol02IntKatalogOrteLand {

//	Die JSON PrimarstufeSchuleingangsphaseBesuchsjahre beinhaltet leider keinen Fall, der zeitlich begrenzt ist
//	daher ist eine Prüfung auf false hier nicht möglich.
	private static final String TESTDATEN = """
		1010, 2012, true
		1000, 2026, false
		0, 2026, false
		1040, 2026, true
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
	 * Test von ValidatorIol02IntKatalogOrteLand
	 *
	 * CoreType: Laender
	 *
	 * @param idKatalog  KatalogID
	 * @param schuljahr  das Schuljahr
	 * @param result     gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorIol02IntKatalogOrteLand")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN)
	void testTestValidatorIol02IntKatalogOrteLand(final Long idKatalog, final Integer schuljahr,
			final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		//Setzen Schuljahr
		kontext.getSchuljahresabschnitt().schuljahr = schuljahr;

		final ValidatorIol02IntKatalogOrteLand validator =
				new ValidatorIol02IntKatalogOrteLand(
						() -> idKatalog,
						kontext);

		assertEquals(result, validator.pruefe());

	}

}
