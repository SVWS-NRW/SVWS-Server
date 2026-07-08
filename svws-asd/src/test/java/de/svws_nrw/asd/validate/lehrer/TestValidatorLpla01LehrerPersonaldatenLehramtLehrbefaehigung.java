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
 * <p> Testklasse für die Validatoren
 * <ul>
 *   <li> {@link ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung")
class TestValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung {

	private static final String TESTDATEN_LEHRAMTLEHRBEFAEHIGUNG = """
			-1          , false
			216         , true
		""";

	/** Stammdaten der Schule mit Lehrerpersonaldaten->Lehrämtern-Lehrbefähigungen*/
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung_SchuleStatistikdaten_LehrerPersonaldaten_Lehramt_Lehrbefaehigung.json", StatistikGesamt.class);

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
	 * Test von ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung
	 *
	 * CoreType: LehrerStammdaten
	 *
	 * @param idLehrbefaehigung   die LehrbefaehigungsID
	 * @param result              gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_LEHRAMTLEHRBEFAEHIGUNG, nullValues = { "null" })
	void testValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung(final long idLehrbefaehigung, final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
				testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung validator = new ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung(() -> idLehrbefaehigung, null, kontext);
		assertEquals(result, validator.pruefe());
	}

}
