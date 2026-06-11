package de.svws_nrw.asd.validate.lehrer;

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
 * Testklasse für den Validator {@link ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung}.
 *
 * Die Testdaten werden aus einer statischen JSON-Ressource eingelesen.
 */
@DisplayName("Tests ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung")
class TestValidatorLplf01LehrerPersonaldatenLehramtFachrichtung {

	private static final String TESTDATEN_FACHRICHTUNG = """
			78   , true
			999  , false
		""";

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	/**
	 * Initialisiert die Core-Types, damit die Tests ausgeführt werden können.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	/**
	 * Test von ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung.
	 *
	 * @param idFachrichtung   die Katalog-ID der Fachrichtung
	 * @param result           gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_FACHRICHTUNG, nullValues = { "null" })
	void testValidatorLplf01LehrerPersonaldatenLehramtFachrichtung(final Long idFachrichtung, final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung validator =
				new ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung(() -> idFachrichtung, kontext);
		assertEquals(result, validator.pruefe());
	}

}
