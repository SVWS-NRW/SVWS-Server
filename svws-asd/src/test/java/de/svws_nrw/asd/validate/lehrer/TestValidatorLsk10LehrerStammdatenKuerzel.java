package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * <p> Testklasse für die Validatoren
 * <ul>
 *   <li> {@link ValidatorLsLehrerStammdaten},
 * </ul>
 * </p>
 *
 * <p> Testdaten:
 *   <ul>
 *     <li> de/svws_nrw/asd/validate/lehrer/Testdaten_001_LehrerStammdaten.json
 *   </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 * CoreType: LehrerStammdaten
 */
@DisplayName("Tests zur Validierung der LehrerStammdaten")
class TestValidatorLsk10LehrerStammdatenKuerzel {

	private static final String TESTDATEN_KUERZEL = """
			'A100'  , true
			'FRED'  , true
			'X56Ö'  , true
			'Ä   '  , true
			'KRZ'   , true
			'K-B'   , true
			'K B1'  , true
			'F5g5'  , false
			'ABCDEF' , false
			'9898'  , false
			'hgft'  , false
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
	 * Test von ValidatorLehrerStammdatenKuerzel
	 *
	 * CoreType: LehrerStammdaten
	 *
	 * @param kuerzel   die Lehrerabkürzung (früher LEHK)
	 * @param result    gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 *
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 */
	@DisplayName("Tests für ValidatorLehrerStammdatenGeschlecht")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_KUERZEL)
	void testValidatorLehrerStammdatenGeschlecht(final String kuerzel, final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
				testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorLsk10LehrerStammdatenKuerzel validator = new ValidatorLsk10LehrerStammdatenKuerzel(() -> kuerzel, kontext);

		assertEquals(result, validator.run());
	}

}
