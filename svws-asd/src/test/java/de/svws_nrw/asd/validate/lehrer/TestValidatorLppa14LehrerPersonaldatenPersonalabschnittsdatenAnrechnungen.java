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
 *   <li> {@link ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen}
 * </ul>
 * </p>
 */
@DisplayName("Tests ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen")
class TestValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {

	// Katalog-IDs (historie.id):
	// LehrerBeschaeftigungsart: 1 = 'V', 6 = 'TS'
	// LehrerEinsatzstatus:      1 = 'A', 2 = 'B'
	// LehrerMehrleistungsarten: 1 = 'ID_100', 2 = 'ID_110'
	private static final String TESTDATEN = """
			idBeschaeftigungsart , idEinsatzstatus , idGrund , anzahl , result
			null                 , 1               , 1       , 0.0    , true
			1                    , 1               , 1       , 0.0    , true
			6                    , 2               , 1       , 0.0    , true
			6                    , 1               , 2       , 0.0    , true
			6                    , 1               , 1       , 4.0    , true
			6                    , 1               , 1       , 0.0    , false
			6                    , null            , 1       , 0.0    , false
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
	 * Test von ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen
	 *
	 * @param idBeschaeftigungsart   die Katalog-ID der Beschäftigungsart
	 * @param idEinsatzstatus        die Katalog-ID des Einsatzstatus
	 * @param idGrund                die Katalog-ID der Mehrleistungsart
	 * @param anzahl                 die Stundenanzahl
	 * @param result                 gibt an, welches Ergebnis bei den Testdaten erwartet wird
	 */
	@DisplayName("Tests für ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = TESTDATEN, nullValues = { "null" })
	void testValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final Long idBeschaeftigungsart,
			final Long idEinsatzstatus,
			final Long idGrund,
			final double anzahl,
			final boolean result) {
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> mehrleistung = new ArrayList<>();
		if (idGrund != null) {
			final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
			eintrag.idGrund = idGrund;
			eintrag.anzahl = anzahl;
			mehrleistung.add(eintrag);
		}

		final ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen validator =
				new ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
						() -> mehrleistung,
						() -> idBeschaeftigungsart,
						() -> idEinsatzstatus,
						kontext);
		assertEquals(result, validator.pruefe());
	}

}
