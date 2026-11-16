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
 *   <li> {@link ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen")
class TestValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {

	// Katalog-IDs (historie.id):
	// LehrerBeschaeftigungsart:   1 = 'V', 6 = 'TS'
	// LehrerEinsatzstatus:        1 = 'A', 2 = 'B'
	// LehrerMinderleistungsarten: 37 = 'ID_200', 46 = 'ID_290'
	private static final String TESTDATEN = """
			idBeschaeftigungsart , idEinsatzstatus , idGrund , anzahl , result
			null                 , 1               , 46      , 0.0    , true
			1                    , 1               , 46      , 0.0    , true
			6                    , 2               , 46      , 0.0    , true
			6                    , 1               , 37      , 0.0    , true
			6                    , 1               , 46      , 5.0    , true
			6                    , 1               , 46      , 0.0    , false
			6                    , null            , 46      , 0.0    , false
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
	 * Test von ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen
	 *
	 * @param idBeschaeftigungsart   die Katalog-ID der Beschäftigungsart
	 * @param idEinsatzstatus        die Katalog-ID des Einsatzstatus
	 * @param idGrund                die Katalog-ID der Minderleistungsart
	 * @param anzahl                 die Stundenanzahl
	 * @param result                 gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final Long idBeschaeftigungsart,
			final Long idEinsatzstatus,
			final Long idGrund,
			final double anzahl,
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

		final ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen validator =
				new ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
						() -> minderleistung,
						() -> idBeschaeftigungsart,
						() -> idEinsatzstatus,
						kontext);
		assertEquals(result, validator.pruefe());
	}

}
