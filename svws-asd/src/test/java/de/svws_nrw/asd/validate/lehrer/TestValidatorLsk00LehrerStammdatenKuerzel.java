package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
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
 *     <li> de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json
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
class TestValidatorLsk00LehrerStammdatenKuerzel {

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

	/** Stammdaten des Lehrers */
	static final LehrerStammdaten lehrerTestdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/lehrer/Testdaten_001_LehrerStammdaten.json", LehrerStammdaten.class);

	/** Stammdaten der Schule */
	static final SchuleStammdaten schuleTestdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/schule/Testdaten_001_SchuleStammdaten.json", SchuleStammdaten.class);

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
		// Testdaten setzen
		lehrerTestdaten_001.kuerzel = kuerzel;

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLsk00LehrerStammdatenKuerzel validator = new ValidatorLsk00LehrerStammdatenKuerzel(lehrerTestdaten_001, kontext);

		assertEquals(result, validator.run());
	}

}
