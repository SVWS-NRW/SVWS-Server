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
@DisplayName("Teste den Validator Gld02 für die Duplikatprüfung bei Lehrerdaten von Schulen")
class TestValidatorGld02GesamtLehrerdatenDuplikate {

	/** Statistikdaten der Schule für ValidatorGesamtLehrerdatenDuplikate*/
	static final SchuleStatistikdatenGesamt testdaten_001_Lehrerduplikate =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_SchuleStatistikdatenGesamt_Lehrerduplikate.json", SchuleStatistikdatenGesamt.class);

	private static final String TESTDATEN_LEHRERDATEN_DUPLIKATE = """
			1  ,'Müller'   ,'Frauke'   ,'1994-05-04'	,4,	false
			2  ,'Gertner'  ,'Klars'    ,'1980-04-14'	,3,	false
			3  ,'Knioba'   ,'Franze'   ,'2000-12-07'	,3,	false
			4  ,'Müller'   ,'Frauke'   ,'1994-05-04'	,4,	false
			4  ,'Gertner'  ,'Klars'    ,'1980-04-14'	,3,	false
			4  ,'Knioba'   ,'Franze'   ,'2000-12-07'	,3,	false
			4  ,'Müllerli' ,'Frauke'   ,'1994-05-04'	,4,	true
			4  ,'Gertner'  ,'Klara'    ,'1980-04-14'	,3,	true
			4  ,'Knioba'   ,'Franze'   ,'1992-12-07'	,3,	true
			4  ,'Knioba'   ,'Franze'   ,'2000-12-07'	,4,	true
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
	 * Test von ValidatorGesamtLehrerdatenDuplikate
	 *
	 * CoreType: LehrerStammdaten
	 *
	 * @param id              die ID, welche bei den eingelesenen Testdaten ersetzt wird
	 * @param nachname        der Nachname, welcher bei den eingelesenen Testdaten ersetzt wird
	 * @param vorname         der Vorname, welcher bei den eingelesenen Testdaten ersetzt wird
	 * @param geburtsdatum    das Geburtsdatum, welches bei den eingelesenen Testdaten ersetzt wird
	 * @param geschlecht      das Geschlecht, welches bei den eingelesenen Testdaten ersetzt wird
	 * @param result     	  gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorGld02GesamtLehrerdatenDuplikate")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_LEHRERDATEN_DUPLIKATE)
	void testValidatorGld02GesamtLehrerdatenDuplikate(final long id, final String nachname, final String vorname, final String geburtsdatum,
			final int geschlecht, final boolean result) {
		// Testdatensatz setzen
		final LehrerStammdaten lehrerTestdaten_001 = new LehrerStammdaten();
		lehrerTestdaten_001.id = id;
		lehrerTestdaten_001.nachname = nachname;
		lehrerTestdaten_001.vorname = vorname;
		lehrerTestdaten_001.geburtsdatum = geburtsdatum;
		lehrerTestdaten_001.geschlecht = geschlecht;

		testdaten_001_LehrerduplikateErweitert = JsonReader
				.fromResource("de/svws_nrw/asd/validate/Testdaten_001_SchuleStatistikdatenGesamt_Lehrerduplikate.json", SchuleStatistikdatenGesamt.class);
		testdaten_001_LehrerduplikateErweitert.lehrerStammdaten.add(lehrerTestdaten_001);

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(testdaten_001_LehrerduplikateErweitert.stammdaten, true);
		final ValidatorGld02GesamtLehrerdatenDuplikate validator =
				new ValidatorGld02GesamtLehrerdatenDuplikate(testdaten_001_LehrerduplikateErweitert.lehrerStammdaten, kontext);
		assertEquals(result, validator.run());
	}

}
