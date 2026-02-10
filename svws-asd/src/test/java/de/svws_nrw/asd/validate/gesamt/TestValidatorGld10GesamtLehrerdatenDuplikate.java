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
@DisplayName("Teste den Validator Gld10 für die Duplikatprüfung bei Lehrerdaten von Schulen")
class TestValidatorGld10GesamtLehrerdatenDuplikate {

	/** Statistikdaten der Schule*/
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	private static final String TESTDATEN_LEHRERDATEN_DUPLIKATE = """
			1  ,	false
			2  ,	true
			3  ,	false
			4  ,	true
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
	 * Test von ValidatorGld10GesamtLehrerdatenDuplikate
	 *
	 * CoreType: LehrerStammdaten
	 *
	 * @param id              die ID, welche bei den eingelesenen Testdaten ersetzt wird
	 * @param result     	  gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorGld10GesamtLehrerdatenDuplikate")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_LEHRERDATEN_DUPLIKATE)
	void testValidatorGld0GesamtLehrerdatenDuplikate(final long id, final boolean result) {

		final LehrerStatistikGesamt lehrerStatistikGesamt1 = new LehrerStatistikGesamt();
		final LehrerStatistikGesamt lehrerStatistikGesamt2 = new LehrerStatistikGesamt();
		final LehrerStatistikGesamt lehrerStatistikGesamt3 = new LehrerStatistikGesamt();

		lehrerStatistikGesamt1.id = 1;
		lehrerStatistikGesamt2.id = 3;
		lehrerStatistikGesamt3.id = id;

		final List<LehrerStatistikGesamt> listLehrerStatistikGesamt = new ArrayList<>();
		listLehrerStatistikGesamt.add(lehrerStatistikGesamt1);
		listLehrerStatistikGesamt.add(lehrerStatistikGesamt2);
		listLehrerStatistikGesamt.add(lehrerStatistikGesamt3);

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorGld10GesamtLehrerdatenDuplikate validator =
				new ValidatorGld10GesamtLehrerdatenDuplikate(
						() -> listLehrerStatistikGesamt,
						kontext);
		assertEquals(result, validator.run());
	}

}
