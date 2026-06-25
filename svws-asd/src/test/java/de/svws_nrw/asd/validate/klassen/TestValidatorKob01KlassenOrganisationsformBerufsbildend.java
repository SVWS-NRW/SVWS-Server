package de.svws_nrw.asd.validate.klassen;

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
 *   <li> {@link ValidatorKob01KlassenOrganisationsformBerufsbildend},
 * </ul>
 * </p>
 */
@DisplayName("Tests für ValidatorKob01KlassenOrganisationsformBerufsbildend")
class TestValidatorKob01KlassenOrganisationsformBerufsbildend {
	private static final String TESTDATEN_ORGAFORM = """
			-1          , false
			1005000     , true
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
	 * Test von ValidatorKob01KlassenOrganisationsformBerufsbildend
	 *
	 * @param idOrgaForm  ID Organisationsform
	 * @param result      gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorKob01KlassenOrganisationsformBerufsbildend")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_ORGAFORM, nullValues = { "null" })
	void testValidatorSlk00SchuelerLernabschnittsdatenKlassenart(final Long idOrgaForm, final boolean result) {
		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorKob01KlassenOrganisationsformBerufsbildend validator =
				new ValidatorKob01KlassenOrganisationsformBerufsbildend(
						() -> idOrgaForm, kontext);
		assertEquals(result, validator.pruefe());
	}


}

