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
@DisplayName("Tests ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung")
class TestValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung {

	//idTest (laufende Nr./id der Lehrbefähigung), idLehramtTest, idLehrbefaehigungTest (id des Lehrbefähigungskatalogeintrages), idAnerkennungsgrundTest, result
	// Für den zweiten Fall gilt: Wert gueltigBis = 2017 => false
	private static final String TESTDATEN_LEHRAMTLEHRBEFAEHIGUNG = """
		216,  2018, true
		253,  2012, true
		253,  2026, false
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
	 * Test von ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung
	 *
	 * CoreType: LehrerStammdaten
	 *
	 * @param idLehrbefaehigung   die LehrbefaehigungsID
	 * @param schuljahr           das Schuljahr
	 * @param result              gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_LEHRAMTLEHRBEFAEHIGUNG, nullValues = { "null" })
	void testValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung(final long idLehrbefaehigung, final int schuljahr, final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
				testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

//		Setzen Schuljahr
		kontext.getSchuljahresabschnitt().schuljahr = schuljahr;

		final ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung validator = new ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung(() -> idLehrbefaehigung, null, kontext);
		assertEquals(result, validator.pruefe());
	}

}
