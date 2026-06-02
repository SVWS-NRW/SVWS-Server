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
 *   <li> {@link ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart}
 * </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * Für jeden Testfall ist eine Methode vorgesehen, in der mittels setzeTestdaten(...) die zugehörigen Testfälle erzeugt werden.
 */
@DisplayName("Tests ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart")
class TestValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart {

	private static final String TESTDATEN_PFLICHTSTUNDENSOLL = """
			-1          , false
			1           , true
			2           , true
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
	 * Test von ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart
	 *
	 * @param beschaeftigungsart  die Beschaeftigungsart
	 * @param result     gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_PFLICHTSTUNDENSOLL, nullValues = { "null" })
	void testValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(final Long beschaeftigungsart,
			final boolean result) {
		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart validator =
				new ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(
						() -> beschaeftigungsart, null, null, kontext);
		assertEquals(result, validator.pruefe());
	}


}
