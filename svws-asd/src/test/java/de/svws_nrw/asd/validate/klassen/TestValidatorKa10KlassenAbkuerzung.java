package de.svws_nrw.asd.validate.klassen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

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
 *   <li> {@link ValidatorKa10KlassenAbkuerzung},
 * </ul>
 * </p>
 */
@DisplayName("Tests für ValidatorKa10KlassenAbkuerzung")
class TestValidatorKa10KlassenAbkuerzung {
	private static final String TESTDATEN_KUERZEL = """
			05AA02      , false
			10KL01      , true
			10K         , true
		""";

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	private static final List<String> listKuerzel = Arrays.asList("05AA01", "05AA02", "05AB01", "10KG01", "05AA02");

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
	 * Test von ValidatorKa10KlassenAbkuerzung
	 *
	 * @param kuerzel     das Kürzel der Klasse
	 * @param result      gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorKa10KlassenAbkuerzung")
	@ParameterizedTest
	@CsvSource(textBlock = TESTDATEN_KUERZEL, nullValues = { "null" })
	void testValidatorKa10KlassenAbkuerzung(final String kuerzel, final boolean result) {
		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final ValidatorKa10KlassenAbkuerzung validator =
				new ValidatorKa10KlassenAbkuerzung(
						() -> kuerzel, () -> listKuerzel, kontext);
		assertEquals(result, validator.pruefe());
	}


}

