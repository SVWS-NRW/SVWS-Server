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
 *   <li> {@link ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen")
class TestValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {

	// Katalog-IDs (historie.id):
	// LehrerMinderleistungsarten: 37 = 'ID_200', 40 = 'ID_230', 41 = 'ID_240', 42 = 'ID_250'
	// LehrerAnrechnungsgrund:     500000 = 'ID_500'
	private static final String TESTDATEN = """
			idMinderleistung , idAnrechnungsgrund , anzahlAnrechnung , result
			null             , 500000             , 2.0              , true
			37               , 500000             , 2.0              , true
			40               , null               , 0.0              , true
			40               , 500000             , 0.0              , false
			41               , 500000             , 2.0              , false
			42               , null               , 2.0              , false
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
	 * Test von ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen
	 *
	 * @param idMinderleistung     die Katalog-ID der Minderleistungsart
	 * @param idAnrechnungsgrund   die Katalog-ID des Anrechnungsgrundes
	 * @param anzahlAnrechnung     die Stundenanzahl der Anrechnung
	 * @param result               gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final Long idMinderleistung,
			final Long idAnrechnungsgrund,
			final double anzahlAnrechnung,
			final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> minderleistung = new ArrayList<>();
		if (idMinderleistung != null) {
			final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
			eintrag.idGrund = idMinderleistung;
			eintrag.anzahl = 25.5;
			minderleistung.add(eintrag);
		}

		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> anrechnungen = new ArrayList<>();
		final LehrerPersonalabschnittsdatenAnrechnungsstunden anrechnung = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
		anrechnung.idGrund = idAnrechnungsgrund;
		anrechnung.anzahl = anzahlAnrechnung;
		anrechnungen.add(anrechnung);

		final ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen validator =
				new ValidatorLppa21LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
						() -> anrechnungen,
						() -> minderleistung,
						kontext);
		assertEquals(result, validator.pruefe());
	}

}
