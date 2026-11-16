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
 * Testklasse für den Validator {@link ValidatorLplf11LehrerPersonaldatenLehramtFachrichtung}.
 *
 * Die Testdaten werden aus einer statischen JSON-Ressource eingelesen.
 */
@DisplayName("Tests ValidatorLplf11LehrerPersonaldatenLehramtFachrichtung")
class TestValidatorLplf11LehrerPersonaldatenLehramtFachrichtung {

	private static final String TESTDATEN_FACHRICHTUNG = """
			86 , 25 ,   true
			86 , null , false
			null, 25  , true
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
	 * Test von ValidatorLplf11LehrerPersonaldatenLehramtFachrichtung.
	 *
	 * @param idFachrichtung   die Katalog-ID der Fachrichtung
	 * @param idLehramt        die Katalog-ID des Lehramtes
	 * @param result           gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLplf11LehrerPersonaldatenLehramtFachrichtung")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_FACHRICHTUNG, nullValues = { "null" })
	void testValidatorLplf11LehrerPersonaldatenLehramtFachrichtung(final Long idLehramt, final Long idFachrichtung, final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorLplf11LehrerPersonaldatenLehramtFachrichtung validator =
				new ValidatorLplf11LehrerPersonaldatenLehramtFachrichtung(() -> 0, () -> idLehramt, () -> idFachrichtung, kontext);
		assertEquals(result, validator.pruefe());
	}

}
