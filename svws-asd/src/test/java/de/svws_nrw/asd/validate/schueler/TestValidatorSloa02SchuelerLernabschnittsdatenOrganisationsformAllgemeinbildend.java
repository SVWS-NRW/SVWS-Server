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
 *   <li> {@link ValidatorSloa02SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend},
 * </ul>
 * </p>
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 * CoreType: SchuelerStammdaten
 */
@DisplayName("Tests zur Validierung der ValidatorSloa02SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend")
class TestValidatorSloa02SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend {

	private static final String TESTDATEN = """
		3003001, 2018, false
		3003001, 2023, true
		3002000, 1950, true
		3002000, 2024, false
		3002001, 2024, true
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
	 * Test von ValidatorSloa02SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend
	 *
	 * CoreType: Klassenart
	 *
	 * @param idOrganisationsform    Organisationsform ID
	 * @param schuljahr  	         das Schuljahr
	 * @param result                 gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorSloa02SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN)
	void testValidatorSloa02SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend(final Long idOrganisationsform, final Integer schuljahr,
			final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorSloa02SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend validator =
				new ValidatorSloa02SchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend(
						() -> schuljahr,
						() -> idOrganisationsform,
						kontext);

		assertEquals(result, validator.pruefe());

	}

}
