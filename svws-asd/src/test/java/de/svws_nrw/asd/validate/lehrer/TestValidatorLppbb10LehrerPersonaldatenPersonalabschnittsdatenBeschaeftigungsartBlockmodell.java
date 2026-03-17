package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

@DisplayName("Validator LPPBB")
class TestValidatorLppbb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell {

	/** Stammdaten des Lehrers */
	static final LehrerStammdaten lehrerTestdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/lehrer/Testdaten_001_LehrerStammdaten.json", LehrerStammdaten.class);

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	/** Personalabschnittsdaten des Lehrers */
	static final LehrerPersonalabschnittsdaten LehrerPersonalabschnittsdaten_Plausibel = JsonReader.fromResource(
			"de/svws_nrw/asd/validate/lehrer/Testdaten_003_LehrerPersonalabschnittsdaten.json",
			LehrerPersonalabschnittsdaten.class);

	/**
	 * Testdaten
	 */
	private static final String LPPPBB_TESTDATEN = """
			18.5, 6,  , 100, true
			20.0, 6, 1, 290, true
			12.0, 6,  , 240, true
			15.0, 6, 1,    , false
			 0.0, 6,  ,    , true
			18.0, 6, 2,    , true
			18.0, 2,  ,    , true
			18.0, 6, 1,    , false
			18.0, 6,  ,    , false
			""";

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	private static void addGrund(final LehrerPersonalabschnittsdaten d, final Long idGrund) {
		if (idGrund == null) {
			return;
		}

		final var lpa = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
		lpa.idGrund = idGrund;

		if (idGrund == 100L && d.mehrleistung != null) {
			d.mehrleistung.add(lpa);
		} else if ((idGrund == 240L || idGrund == 290L) && d.minderleistung != null) {
			d.minderleistung.add(lpa);
		}
	}

	@DisplayName("Tests für ValidatorLppbb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell: pflichtstundensoll, beschaeftigungsart, einsatzstatus, idGrund, erwartet")
	@ParameterizedTest
	@CsvSource(textBlock = LPPPBB_TESTDATEN)
	void testLabb(final double pflichtstundensoll, final Long idBeschaeftigungsart, final Long idEinsatzstatus, final Long idGrund, final boolean result) {

		LehrerPersonalabschnittsdaten_Plausibel.pflichtstundensoll = pflichtstundensoll;
		LehrerPersonalabschnittsdaten_Plausibel.idBeschaeftigungsart = idBeschaeftigungsart;
		LehrerPersonalabschnittsdaten_Plausibel.idEinsatzstatus = idEinsatzstatus;

		if (LehrerPersonalabschnittsdaten_Plausibel.mehrleistung != null) {
			LehrerPersonalabschnittsdaten_Plausibel.mehrleistung.clear();
		}
		if (LehrerPersonalabschnittsdaten_Plausibel.minderleistung != null) {
			LehrerPersonalabschnittsdaten_Plausibel.minderleistung.clear();
		}

		addGrund(LehrerPersonalabschnittsdaten_Plausibel, idGrund);

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final var validator = new ValidatorLppbb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell(
				() -> LehrerPersonalabschnittsdaten_Plausibel.pflichtstundensoll,
				() -> LehrerPersonalabschnittsdaten_Plausibel.idBeschaeftigungsart,
				() -> LehrerPersonalabschnittsdaten_Plausibel.idEinsatzstatus,
				() -> LehrerPersonalabschnittsdaten_Plausibel.mehrleistung,
				() -> LehrerPersonalabschnittsdaten_Plausibel.minderleistung,
				kontext);
		assertEquals(result, validator.pruefe());
	}
}
