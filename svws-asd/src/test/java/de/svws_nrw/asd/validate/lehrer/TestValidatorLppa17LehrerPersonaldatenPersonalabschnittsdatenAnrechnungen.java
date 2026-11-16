package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

/**
 * <p> Testklasse für die Validatoren
 * <ul>
 *   <li> {@link ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen")
class TestValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {

	// Katalog-IDs (historie.id):
	// LehrerMinderleistungsarten: 37 = 'ID_200', 42 = 'ID_250', 53 = 'ID_255'
	// Geschlecht:                 3 = M, 4 = W, 5 = D, 6 = X
	private static final String TESTDATEN = """
			idGrund , geschlecht , result
			null    , 3          , true
			42      , null       , true
			42      , 4          , true
			53      , 4          , true
			42      , 5          , true
			42      , 6          , true
			37      , 3          , true
			42      , 3          , false
			53      , 3          , false
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
	 * Test von ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen
	 *
	 * @param idGrund      die Katalog-ID der Minderleistungsart
	 * @param geschlecht   das Geschlecht der Lehrkraft
	 * @param result       gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final Long idGrund,
			final Integer geschlecht,
			final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> minderleistung = new ArrayList<>();
		if (idGrund != null) {
			final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
			eintrag.idGrund = idGrund;
			eintrag.anzahl = 10.0;
			minderleistung.add(eintrag);
		}

		final ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen validator =
				new ValidatorLppa17LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
						() -> minderleistung,
						() -> geschlecht,
						kontext);
		assertEquals(result, validator.pruefe());
	}

}
