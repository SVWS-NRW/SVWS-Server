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
 * Testklasse für den Validator {@link ValidatorLplaa02LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung}.
 *
 * Die Testdaten werden aus einer statischen JSON-Ressource eingelesen.
 */
@DisplayName("Tests ValidatorLplaa02LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung")
class TestValidatorLplaa02LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung {

	// Testdaten:
	// 2      -> true (ID 2 "AL" ist im Schuljahr 2018 historisch gültig)
	// 5      -> true (ID 5 "OH" ist im Schuljahr 2018 historisch gültig)
	// 99999  -> false (Ungültige/Nicht-existente ID schlägt bei der Historienprüfung fehl)
	private static final String TESTDATEN = """
            ID     , RESULT
            2      , true
            5      , true
            99999  , false
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
	 * Test von ValidatorLplaa02LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung.
	 *
	 * @param idAnerkennungsgrund   die Katalog-ID des Anerkennungsgrunds
	 * @param result                gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLplaa02LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorLplaa02LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung(final Long idAnerkennungsgrund, final boolean result) {

		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorLplaa02LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung validator =
				new ValidatorLplaa02LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung(() -> idAnerkennungsgrund, kontext);

		assertEquals(result, validator.pruefe());
	}

}
