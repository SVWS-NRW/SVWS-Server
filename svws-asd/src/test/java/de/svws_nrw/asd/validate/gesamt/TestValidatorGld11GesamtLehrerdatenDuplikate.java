package de.svws_nrw.asd.validate.gesamt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * Testklasse für den Validator Gesamt Lehrerdaten-Duplikate
 */
@DisplayName("Teste den Validator Gld11 für die Duplikatprüfung bei Lehrerdaten von Schulen")
class TestValidatorGld11GesamtLehrerdatenDuplikate {

	/** Statistikdaten der Schule*/
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

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
	@DisplayName("Tests für ValidatorGld11GesamtLehrerdatenDuplikate")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_LEHRERDATEN_DUPLIKATE)
	void testValidatorGld11GesamtLehrerdatenDuplikate(final long id, final String nachname, final String vorname, final String geburtsdatum,
			final int geschlecht, final boolean result) {

		// 1. Statische Basis-Daten erstellen
		final LehrerStatistikGesamt lehrerStatistikGesamt1 = new LehrerStatistikGesamt();
		final LehrerStatistikGesamt lehrerStatistikGesamt2 = new LehrerStatistikGesamt();
		final LehrerStatistikGesamt lehrerStatistikGesamt3 = new LehrerStatistikGesamt();

		// Lehrer 1
		lehrerStatistikGesamt1.id = 1;
		lehrerStatistikGesamt1.nachname = "Müller";
		lehrerStatistikGesamt1.vorname = "Frauke";
		lehrerStatistikGesamt1.geburtsdatum = "1994-05-04";
		lehrerStatistikGesamt1.geschlecht = 4;

		// Lehrer 2
		lehrerStatistikGesamt2.id = 2;
		lehrerStatistikGesamt2.nachname = "Gertner";
		lehrerStatistikGesamt2.vorname = "Klars";
		lehrerStatistikGesamt2.geburtsdatum = "1980-04-14";
		lehrerStatistikGesamt2.geschlecht = 3;

		// Lehrer 3
		lehrerStatistikGesamt3.id = 3;
		lehrerStatistikGesamt3.nachname = "Knioba";
		lehrerStatistikGesamt3.vorname = "Franze";
		lehrerStatistikGesamt3.geburtsdatum = "2000-12-07";
		lehrerStatistikGesamt3.geschlecht = 3;

		// Test-Objekt aus Parametern erstellen
		final LehrerStatistikGesamt lehrerTest = new LehrerStatistikGesamt();
		lehrerTest.id = id;
		lehrerTest.nachname = nachname;
		lehrerTest.vorname = vorname;
		lehrerTest.geburtsdatum = geburtsdatum;
		lehrerTest.geschlecht = geschlecht;

		// 3. Liste befüllen
		final List<LehrerStatistikGesamt> listLehrerStatistikGesamt = new ArrayList<>();
		listLehrerStatistikGesamt.add(lehrerStatistikGesamt1);
		listLehrerStatistikGesamt.add(lehrerStatistikGesamt2);
		listLehrerStatistikGesamt.add(lehrerStatistikGesamt3);

		// Das Test-Objekt hinzufügen, um zu prüfen, ob es Duplikate auslöst
		listLehrerStatistikGesamt.add(lehrerTest);

		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorGld11GesamtLehrerdatenDuplikate validator =
				new ValidatorGld11GesamtLehrerdatenDuplikate(
						() -> listLehrerStatistikGesamt,
						kontext);

		assertEquals(result, validator.run());
	}

}
