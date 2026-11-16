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
 *   <li> {@link ValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen")
class TestValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {

	// Katalog-IDs (historie.id):
	// LehrerMinderleistungsarten: 37 = 'ID_200', 42 = 'ID_250'
	private static final String TESTDATEN = """
			idGrund , anzahl , pflichtstundensoll , result
			null    , 10.0   , 25.5               , true
			42      , 10.0   , null               , true
			37      , 10.0   , 25.5               , true
			42      , 25.5   , 25.5               , true
			42      , 10.0   , 25.5               , false
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
	 * Test von ValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen
	 *
	 * @param idGrund              die Katalog-ID der Minderleistungsart
	 * @param anzahl               die Stundenanzahl der Minderleistung
	 * @param pflichtstundensoll   das Pflichtstundensoll der Lehrkraft
	 * @param result               gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final Long idGrund,
			final double anzahl,
			final Double pflichtstundensoll,
			final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> minderleistung = new ArrayList<>();
		if (idGrund != null) {
			final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
			eintrag.idGrund = idGrund;
			eintrag.anzahl = anzahl;
			minderleistung.add(eintrag);
		}

		final ValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen validator =
				new ValidatorLppa20LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
						() -> minderleistung,
						() -> pflichtstundensoll,
						kontext);
		assertEquals(result, validator.pruefe());
	}

}
