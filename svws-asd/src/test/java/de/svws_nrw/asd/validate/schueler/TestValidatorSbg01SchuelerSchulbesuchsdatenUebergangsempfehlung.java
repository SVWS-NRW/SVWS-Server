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
 *   <li> {@link ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung}
 * </ul>
 * </p>
 *
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 *
 * CoreType: SchuelerSchulbesuchsdaten
 */
@DisplayName("Tests ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung")
class TestValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung {

	private static final String TESTDATEN = """
			null  , false
			1     , true
			2     , true
			999   , false
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
	 * Test von ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung
	 *
	 * CoreType: SchuelerSchulbesuchsdaten
	 *
	 * @param idUebergangsempfehlungGrundschule    die ID der Übergangsempfehlung Grundschule des Schülers
	 * @param result                               gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung(final Long idUebergangsempfehlungGrundschule, final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung validator =
				new ValidatorSbg01SchuelerSchulbesuchsdatenUebergangsempfehlung(() -> idUebergangsempfehlungGrundschule, kontext);
		assertEquals(result, validator.pruefe());
	}

}
