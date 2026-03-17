package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * <p> Testklasse für den Validator
 * <ul>
 *   <li> {@link ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart},
 * </ul>
 * </p>
 *
 * <p> Testdaten:
 *   <ul>
 *	   <li>  de/svws_nrw/asd/validate/lehrer/Testdaten_002_LehrerPersonalabschnittsdaten.json
 *   </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 * CoreType: LehrerStammdaten
 */
@DisplayName("Tests zur Validierung der Lppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart")
class TestValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart {

	private static final String TESTDATEN_LPPB11 = """
			13,  1, 0, false
			13,  2, 0, true
			13,  1, 1, true
			 1,  1, 0, true
			 1,  2, 7, true
			""";

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	/** Personalabschnittsdaten des Lehrers */
	static final LehrerPersonalabschnittsdaten LehrerPersonalabschnittsdaten_Plausibel = JsonReader.fromResource(
			"de/svws_nrw/asd/validate/lehrer/Testdaten_002_LehrerPersonalabschnittsdaten.json",
			LehrerPersonalabschnittsdaten.class);

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
	 * Test von ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart3
	 *
	 * CoreType: LehrerPersonalabschnittsdaten
	 *
	 * @param idBeschaeftigungsart
	 * @param idEinsatzstatus
	 * @param pflichtstundensoll
	 * @param result     gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_LPPB11)
	void testValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(final Long idBeschaeftigungsart, final Long idEinsatzstatus,
			final double pflichtstundensoll, final boolean result) {
		// Testdaten setzen
		LehrerPersonalabschnittsdaten_Plausibel.idBeschaeftigungsart = idBeschaeftigungsart;
		LehrerPersonalabschnittsdaten_Plausibel.idEinsatzstatus = idEinsatzstatus;
		LehrerPersonalabschnittsdaten_Plausibel.pflichtstundensoll = pflichtstundensoll;

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart validator =
				new ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(
						() -> idBeschaeftigungsart,
						() -> idEinsatzstatus,
						() -> pflichtstundensoll,
						kontext);

		assertEquals(result, validator.pruefe());

	}

}
