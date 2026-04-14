package de.svws_nrw.asd.validate.gesamt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * Testklasse für den Validator Gesamt Schülerdaten-Duplikate
 */
@DisplayName("Teste den Validator Gsd10 für die Duplikatprüfung bei Schülerdaten von Schulen")
class TestValidatorGsd10GesamtSchuelerdatenDuplikate {

	/** Statistikdaten der Schule*/
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	private static final String TESTDATEN_SCHUELERDATEN_DUPLIKATE = """
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
	 * Test von ValidatorGsd10GesamtSchuelerdatenDuplikate
	 *
	 * CoreType: SchuelerStammdaten
	 *
	 * @param id              die ID, welche bei den eingelesenen Testdaten ersetzt wird
	 * @param result     	  gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorGsd10GesamtSchuelerdatenDuplikate")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_SCHUELERDATEN_DUPLIKATE)
	void testValidatorGsd10GesamtSchuelerdatenDuplikate(final long id, final boolean result) {

		final SchuelerStatistikGesamt schuelerStatistikGesamt1 = new SchuelerStatistikGesamt();
		final SchuelerStatistikGesamt schuelerStatistikGesamt2 = new SchuelerStatistikGesamt();
		final SchuelerStatistikGesamt schuelerStatistikGesamt3 = new SchuelerStatistikGesamt();

		schuelerStatistikGesamt1.id = 1;
		schuelerStatistikGesamt2.id = 3;
		schuelerStatistikGesamt3.id = id;

		final List<SchuelerStatistikGesamt> listSchuelerStatistikGesamt = new ArrayList<>();
		listSchuelerStatistikGesamt.add(schuelerStatistikGesamt1);
		listSchuelerStatistikGesamt.add(schuelerStatistikGesamt2);
		listSchuelerStatistikGesamt.add(schuelerStatistikGesamt3);

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorGsd10GesamtSchuelerdatenDuplikate validator =
				new ValidatorGsd10GesamtSchuelerdatenDuplikate(
						() -> listSchuelerStatistikGesamt,
						kontext);
		assertEquals(result, validator.pruefe());
	}

}
