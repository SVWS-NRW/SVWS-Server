package de.svws_nrw.asd.validate.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

@DisplayName("Validator LPPBB")
class TestValidatorLppbb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell {

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	/**
	 * Testdaten
	 */
	private static final String LPPPBB_TESTDATEN = """
		18.5, 'TS', 'DEFAULT', 100, true
		20.0, 'TS', 'A'      , 290, true
		12.0, 'TS', 'DEFAULT', 240, true
		15.0, 'TS', 'A'      , 999, false
		0.0 , 'TS', 'DEFAULT', 999, true
		18.0, 'TS', 'B'      , 999, true
		18.0, 'T' , 'DEFAULT', 999, true
		18.0, 'TS', 'A'      , 999, false
		18.0, 'TS', 'DEFAULT', 999, true
		""";

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("Tests für ValidatorLppbb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell: pflichtstundensoll, beschaeftigungsart, einsatzstatus, idGrund, erwartet")
	@ParameterizedTest
	@CsvSource(textBlock = LPPPBB_TESTDATEN)
	void testLabb(final double pflichtstundensoll, final LehrerBeschaeftigungsart beschaeftigungsart, final LehrerEinsatzstatus einsatzstatus, final Long grund,
			final boolean result) {

		// Erzeuge den Kontext für die Validierung
		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);
		final LehrerPersonalabschnittsdatenAnrechnungsstunden lpa = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
		lpa.idGrund = grund;
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> mehrleistungList = new LinkedList<>();
		mehrleistungList.add(lpa);
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> minderleistungList = new LinkedList<>();
		minderleistungList.add(lpa);

		final var validator = new ValidatorLppbb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell(
				() -> pflichtstundensoll,
				() -> beschaeftigungsart,
				() -> einsatzstatus,
				() -> mehrleistungList,
				() -> minderleistungList,
				kontext);
		assertEquals(result, validator.pruefe());
	}
}
