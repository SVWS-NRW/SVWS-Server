package de.svws_nrw.asd.validate.schueler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * <p> Testklasse für den Validator
 * <ul>
 *   <li> {@link ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart")
class TestValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart {

	private static final String TESTDATEN = """
			null   , true
			-1     , true
			999999 , true
			51     , true
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
	 * Test von ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart
	 *
	 * @param idEinschulungsart   die ID der Einschulungsart
	 * @param result              gibt an, welches Ergebnis erwartet wird
	 */
	@DisplayName("Tests für ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart(final Long idEinschulungsart, final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart validator =
				new ValidatorSbe02SchuelerSchulbesuchsdatenEinschulungsart(
						() -> {
							if (idEinschulungsart == null) {
								return null;
							}
							final SchuelerSchulbesuchsdaten daten = new SchuelerSchulbesuchsdaten();
							daten.idEinschulungsartGrundschule = idEinschulungsart;
							return daten;
						},
						kontext);

		assertEquals(result, validator.pruefe());
	}

}
