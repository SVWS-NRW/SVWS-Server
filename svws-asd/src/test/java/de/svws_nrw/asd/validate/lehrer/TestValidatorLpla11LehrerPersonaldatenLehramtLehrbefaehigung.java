package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * <p> Testklasse für die Validatoren
 * <ul>
 *   <li> {@link ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung")
class TestValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung {

	//idTest (laufende Nr./id der Lehrbefähigung), idLehramtTest, idLehrbefaehigungTest (id des Lehrbefähigungskatalogeintrages), idAnerkennungsgrundTest, result
	// Vor dem ersten Testfall sind noch keine Lehrbefähigungen vorhanden.
	// Bei dem ersten Fall ist die Lehrbefähigung 'BE' => true
	// Bei dem zweiten Fall ist die erste Lehrbefähigung 'BE' und die zweite <> 'BE' => Fehlerausgabe
	private static final String TESTDATEN_LEHRAMTLEHRBEFAEHIGUNG = """
			191,   47,   179,   1,   true
			192,   47,   253,   1,   false
		""";

	/** Stammdaten der Schule mit Lehrerpersonaldaten->Lehrämtern-Lehrbefähigungen*/
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung_SchuleStatistikdaten_LehrerPersonaldaten_Lehramt_Lehrbefaehigung.json", StatistikGesamt.class);

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
	 * Test von ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung
	 *
	 * CoreType: LehrerStammdaten
	 *
	 * @param idTest					die Liste der Lehrämter
	 * @param idLehramtTest				die Liste der Lehrämter
	 * @param idLehrbefaehigungTest		die Liste der Lehrämter
	 * @param idAnerkennungsgrundTest	die Liste der Lehrämter
	 * @param result        			gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_LEHRAMTLEHRBEFAEHIGUNG, nullValues = { "null" })
	void testValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung(final long idTest, final long idLehramtTest, final long idLehrbefaehigungTest, final Long idAnerkennungsgrundTest, final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
				testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		//Getestet wird aus der JSON-Datei nur der erste Lehramtssatz und mit deren Lehrbefähigungen,
		//auf welchen noch jeweils ein zusätzlicher Lehrbefaehigungssatz aus den oben angegebenen Testdaten
		//hinzugefügt wird.
		//
		List<LehrerLehramtEintrag> lehraemterAusJson = testdaten_001.lehrer.getFirst().lehraemter;

		LehrerLehrbefaehigungEintrag zusaetzlicherLehrerLehrbefaehigungEintrag = new LehrerLehrbefaehigungEintrag();
		zusaetzlicherLehrerLehrbefaehigungEintrag.id = idTest;
		zusaetzlicherLehrerLehrbefaehigungEintrag.idLehramt = idLehramtTest;
		zusaetzlicherLehrerLehrbefaehigungEintrag.idLehrbefaehigung = idLehrbefaehigungTest;
		zusaetzlicherLehrerLehrbefaehigungEintrag.idAnerkennungsgrund = idAnerkennungsgrundTest;

		lehraemterAusJson.getFirst().lehrbefaehigungen.add(zusaetzlicherLehrerLehrbefaehigungEintrag);

		final ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung validator = new ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung(() -> lehraemterAusJson, kontext);
		assertEquals(result, validator.pruefe());
	}

}
