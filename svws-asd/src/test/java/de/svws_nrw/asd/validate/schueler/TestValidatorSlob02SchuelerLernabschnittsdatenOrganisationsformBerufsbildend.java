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
 * <p> Testklasse für den Validator
 * <ul>
 *   <li> {@link ValidatorSlob02SchuelerLernabschnittsdatenOrganisationsformBerufsbildend},
 * </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * CoreType: SchuelerStammdaten
 */
@DisplayName("Tests zur Validierung der ValidatorSlob02SchuelerLernabschnittsdatenOrganisationsformBerufsbildend")
class TestValidatorSlob02SchuelerLernabschnittsdatenOrganisationsformBerufsbildend {

	private static final String TESTDATEN = """
		1004000, 2018, true
		1004000, 1960, true
		1016000, 1950, true
		1016000, 1940, true
		123,     1970, false
		-123,    2021, false
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
	 * Test von ValidatorSlob02SchuelerLernabschnittsdatenOrganisationsformBerufsbildend
	 *
	 * CoreType: Klassenart
	 *
	 * @param idOrganisationsform    Organisationsform ID
	 * @param schuljahr  	         das Schuljahr
	 * @param result                 gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSlob02SchuelerLernabschnittsdatenOrganisationsformBerufsbildend")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN)
	void testValidatorSlob02SchuelerLernabschnittsdatenOrganisationsformBerufsbildend(final Long idOrganisationsform, final Integer schuljahr,
			final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorSlob02SchuelerLernabschnittsdatenOrganisationsformBerufsbildend validator =
				new ValidatorSlob02SchuelerLernabschnittsdatenOrganisationsformBerufsbildend(
						() -> schuljahr,
						() -> idOrganisationsform,
						kontext);

		assertEquals(result, validator.pruefe());

	}

}
