package de.svws_nrw.asd.validate.schueler;

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
 * <p> Testklasse für die Validatoren
 * <ul>
 *   <li> {@link ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung}
 * </ul>
 * </p>
 *
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 * CoreType: SchuelerLernabschnittsdaten
 */
@DisplayName("Tests ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung")
class TestValidatorSls00SchuelerLernabschnittsdatenSchulgliederung {

	private static final String TESTDATEN_SCHULGLIEDERUNG = """
			null  , false
			123   , true
			-1    , true
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
	 * Test von ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung
	 *
	 * CoreType: SchuelerLernabschnittsdaten
	 *
	 * @param idSchulgliederung    die ID der Schulgliederung, welche bei den eingelesenen Testdaten ersetzt wird
	 * @param result               gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_SCHULGLIEDERUNG, nullValues = { "null" })
	void testValidatorSls00SchuelerLernabschnittsdatenSchulgliederung(final Long idSchulgliederung, final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung validator =
				new ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung(() -> kontext.getSchuljahr(), () -> idSchulgliederung, kontext);
		assertEquals(result, validator.pruefe());
	}


}
