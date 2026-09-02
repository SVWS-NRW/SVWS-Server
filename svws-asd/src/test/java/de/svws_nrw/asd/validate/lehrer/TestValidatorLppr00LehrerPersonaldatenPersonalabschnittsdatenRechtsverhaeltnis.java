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
 * Testklasse für den Validator {@link ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis}.
 *
 * Die Testdaten werden aus einer statischen JSON-Ressource eingelesen.
 */
@DisplayName("Tests TestValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis")
class TestValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis {

	private static final String TESTDATEN = """
			null , false
			3    , true
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
	 * Test von ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis.
	 *
	 * @param idRechtsverhaeltnis   die Katalog-ID des Rechtsverhältnisses
	 * @param result                gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(final Long idRechtsverhaeltnis, final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis validator =
				new ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(() -> null, () -> null, () -> idRechtsverhaeltnis, () -> null, kontext);
		assertEquals(result, validator.pruefe());
	}

}
