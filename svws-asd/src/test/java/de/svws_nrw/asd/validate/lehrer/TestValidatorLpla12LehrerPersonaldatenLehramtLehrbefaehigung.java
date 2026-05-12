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
 *   <li> {@link ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung")
class TestValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung {

	// Fehler soll geworfen werden, wenn ein Lehramt ID_30, ID32 oder ID35 und mindestens eine Lehrbefaehigung vorliegen.
	// ID_30 idLehramt = 19
	// ID_32 idLehramt = 21
	// ID_35 idLehramt = 22
	// Bei dem ersten Fall gibt es immer noch keine Lehrbefähigung  => true
	// Bei dem zweiten Fall liegt eine Lehrbefähigung vor => Fehlerausgabe
	//
	//boolean weitereLehrbefaehigungHinzuziehen, String result
	private static final String TESTDATEN_LEHRAMTLEHRBEFAEHIGUNG = """
			false,   false
			true ,   true
		""";

	/** Stammdaten der Schule mit Lehrerpersonaldaten->Lehrämtern-Lehrbefähigungen*/
	// Testdatendatei ist noch kaputt!!!!!!!!!!!!!!!!!!!!!!!!!!xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
	static final StatistikGesamt testdaten_Lpla12 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung_SchuleStatistikdaten_LehrerPersonaldaten_Lehramt_Lehrbefaehigung.json", StatistikGesamt.class);

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
	 * @param weitereLehrbefaehigungHinzuziehen		gibt an, ob eine Lehrbefaehigung hinzugezogen/erzeugt werden soll oder nicht
	 * @param result        						gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@SuppressWarnings("removal")
	@DisplayName("Tests für ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_LEHRAMTLEHRBEFAEHIGUNG, nullValues = { "null" })
	void testValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung(final boolean weitereLehrbefaehigungHinzuziehen, final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext = new ValidatorKontext(testdaten_Lpla12.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_Lpla12.schule.schulform),
				testdaten_Lpla12.schule.abschnitte, testdaten_Lpla12.schule.idSchuljahresabschnitt, true);

		//Getestet wird aus der JSON-Datei nur der erste Lehramtssatz und mit deren Lehrbefähigungen,
		//auf welchen noch jeweils ein zusätzlicher Lehrbefaehigungssatz aus den oben angegebenen Testdaten
		//hinzugefügt wird.
		//
		List<LehrerLehramtEintrag> lehraemterAusJson = testdaten_Lpla12.lehrer.getFirst().lehraemter;

		//LehrerLehrbefaehigungEintrag zusaetzlicherLehrerLehrbefaehigungEintrag;
		if (weitereLehrbefaehigungHinzuziehen) {
			LehrerLehrbefaehigungEintrag zusaetzlicherLehrerLehrbefaehigungEintrag = new LehrerLehrbefaehigungEintrag();
			zusaetzlicherLehrerLehrbefaehigungEintrag.id = 1111;
			zusaetzlicherLehrerLehrbefaehigungEintrag.idLehramt = 222;
			zusaetzlicherLehrerLehrbefaehigungEintrag.idLehrbefaehigung = 3333;
			zusaetzlicherLehrerLehrbefaehigungEintrag.idAnerkennungsgrund = new Long(1);

			lehraemterAusJson.getFirst().lehrbefaehigungen.add(zusaetzlicherLehrerLehrbefaehigungEintrag);
		}

		final ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung validator = new ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung(() -> lehraemterAusJson, kontext);
		assertEquals(result, validator.pruefe());
	}

}
