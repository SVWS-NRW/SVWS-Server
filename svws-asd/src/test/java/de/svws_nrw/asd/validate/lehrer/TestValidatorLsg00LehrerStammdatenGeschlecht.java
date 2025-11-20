package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * <p> Testklasse für den Validator Geschlecht
 * <ul>
 *   <li> {@link ValidatorLsg00LehrerStammdatenGeschlecht},
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
@DisplayName("Tests zur Validierung von Lsg00LehrerStammdatenGeschlecht")
class TestValidatorLsg00LehrerStammdatenGeschlecht {

	private static final String TESTDATEN_GESCHLECHT = """
	        3  , true
	        4  , true
	        5  , true
	        6  , true
	        0  , false
	        1  , false
	        2  , false
	        7  , false
	        -1 , false
	        999, false
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
	 * Test von ValidatorLsg0LehrerStammdatenGeschlecht
	 *
	 * CoreType: LehrerStammdaten
	 *
	 * @param geschlecht der Integerwert für das Geschlecht (z. B. 3 = M)
	 * @param result     gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLsg00LehrerStammdatenGeschlecht")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_GESCHLECHT)
	void testValidatorLehrerStammdatenGeschlecht(final int geschlecht, final boolean result) {
		// Testdaten setzen
		lehrerTestdaten_001.geschlecht = geschlecht;

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(schuleTestdaten_001, true);
		final ValidatorLsg00LehrerStammdatenGeschlecht validator =
				new ValidatorLsg00LehrerStammdatenGeschlecht(lehrerTestdaten_001, kontext);

		assertEquals(result, validator.run());

	}

}
