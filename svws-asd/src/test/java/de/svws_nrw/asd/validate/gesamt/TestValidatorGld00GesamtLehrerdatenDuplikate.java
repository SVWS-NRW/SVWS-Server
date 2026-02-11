package de.svws_nrw.asd.validate.gesamt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.data.schule.SchuleStatistikdatenGesamt;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * Testklasse für den Validator Gesamt Lehrerdaten-Duplikate
 */
@DisplayName("Teste den Validator Gld00 für die Duplikatprüfung bei Lehrerdaten von Schulen")
class TestValidatorGld00GesamtLehrerdatenDuplikate {

	/** Statistikdaten der Schule für ValidatorGesamtLehrerdatenDuplikate*/
	static final SchuleStatistikdatenGesamt testdaten_001_Lehrerduplikate =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_SchuleStatistikdatenGesamt_Lehrerduplikate.json", SchuleStatistikdatenGesamt.class);

	private static final String TESTDATEN_LEHRERDATEN_DUPLIKATE = """
			1  ,	false
			2  ,	false
			3  ,	false
			4  ,	true
		""";



	/** JSON-Testdatei mit drei Lehrerstammdatensätzen, welche jeweils um den einzelnen zu prüfenden Testfall erweitert wird */
	private SchuleStatistikdatenGesamt testdaten_001_LehrerduplikateErweitert;


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
	 * Test von ValidatorGld00GesamtLehrerdatenDuplikate
	 *
	 * CoreType: LehrerStammdaten
	 *
	 * @param id              die ID, welche bei den eingelesenen Testdaten ersetzt wird
	 * @param result     	  gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorGld00GesamtLehrerdatenDuplikate")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_LEHRERDATEN_DUPLIKATE)
	void testValidatorGld0GesamtLehrerdatenDuplikate(final long id, final boolean result) {
		// Testdatensatz setzen
		final LehrerStammdaten lehrerTestdaten_001 = new LehrerStammdaten();
		lehrerTestdaten_001.id = id;

		testdaten_001_LehrerduplikateErweitert = JsonReader
				.fromResource("de/svws_nrw/asd/validate/Testdaten_001_SchuleStatistikdatenGesamt_Lehrerduplikate.json", SchuleStatistikdatenGesamt.class);
		testdaten_001_LehrerduplikateErweitert.lehrerStammdaten.add(lehrerTestdaten_001);

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(testdaten_001_LehrerduplikateErweitert.stammdaten, true);
		final ValidatorGld00GesamtLehrerdatenDuplikate validator =
				new ValidatorGld00GesamtLehrerdatenDuplikate(testdaten_001_LehrerduplikateErweitert.lehrerStammdaten, kontext);
		assertEquals(result, validator.run());
	}

}
