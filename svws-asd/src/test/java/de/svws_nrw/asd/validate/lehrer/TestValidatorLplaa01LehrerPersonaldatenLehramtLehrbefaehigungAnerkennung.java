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
 * Testklasse für den Validator {@link ValidatorLplaa01LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung}.
 *
 * Die Testdaten werden aus einer statischen JSON-Ressource eingelesen.
 */
@DisplayName("Tests ValidatorLplaa01LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung")
class TestValidatorLplaa01LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung {

	// Testdaten:
	// 1      -> true (ID 1 ist "ST" / Zweite Staatsprüfung)
	// 3      -> true (ID 3 ist "AP" / Anerkennung geeignete Prüfung)
	// 99999  -> false (Diese ID existiert im Katalog nicht)
	private static final String TESTDATEN = """
            ID     , RESULT
            1      , true
            3      , true
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
	 * Test von ValidatorLplaa01LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung.
	 *
	 * @param idAnerkennungsgrund   die Katalog-ID des Anerkennungsgrunds
	 * @param result                gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLplaa01LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorLplaa01LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung(final Long idAnerkennungsgrund, final boolean result) {

		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorLplaa01LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung validator =
				new ValidatorLplaa01LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung(() -> idAnerkennungsgrund, kontext);

		assertEquals(result, validator.pruefe());
	}

}
