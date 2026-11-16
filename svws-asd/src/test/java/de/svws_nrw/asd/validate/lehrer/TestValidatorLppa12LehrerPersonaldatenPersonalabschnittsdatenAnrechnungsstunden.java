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
 *   <li> {@link ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden")
class TestValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden {

	// Katalog-IDs (historie.id):
	// LehrerMehrleistungsarten:   1 = 'ID_100', 2 = 'ID_110'
	// LehrerMinderleistungsarten: 37 = 'ID_200', 40 = 'ID_230', 41 = 'ID_240', 42 = 'ID_250'
	private static final String TESTDATEN = """
			idMehrleistung , idMinderleistung , result
			null           , null             , true
			1              , 40               , true
			2              , null             , true
			2              , 37               , true
			2              , 40               , false
			2              , 41               , false
			2              , 42               , false
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
	 * Test von ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden
	 *
	 * @param idMehrleistung     die Katalog-ID der Mehrleistungsart
	 * @param idMinderleistung   die Katalog-ID der Minderleistungsart
	 * @param result             gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden(
			final Long idMehrleistung,
			final Long idMinderleistung,
			final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> mehrleistung = new ArrayList<>();
		if (idMehrleistung != null) {
			final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
			eintrag.idGrund = idMehrleistung;
			eintrag.anzahl = 2.0;
			mehrleistung.add(eintrag);
		}

		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> minderleistung = new ArrayList<>();
		if (idMinderleistung != null) {
			final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
			eintrag.idGrund = idMinderleistung;
			eintrag.anzahl = 2.0;
			minderleistung.add(eintrag);
		}

		final ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden validator =
				new ValidatorLppa12LehrerPersonaldatenPersonalabschnittsdatenAnrechnungsstunden(
						() -> mehrleistung,
						() -> minderleistung,
						kontext);
		assertEquals(result, validator.pruefe());
	}

}
