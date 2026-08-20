package de.svws_nrw.asd.validate.klassen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.statistik.KlassenStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorKontext;

@DisplayName("Tests ValidatorKckp10KlassenKombinationKlassenjahrgangParallelitaet")
class TestValidatorKckp10KlassenKombinationKlassenjahrgangParallelitaet {

	/** Stammdaten der Schule */
	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/validate/Testdaten_001_StatistikGesamt.json", StatistikGesamt.class);

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@DisplayName("Tests ValidatorKckp10KlassenKombinationKlassenjahrgangParallelitaet")
	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, nullValues = "null", textBlock = """
			JG_TEXT,  PARALL,  PARALL2,  EXPECTED
			05     ,  a,       b,        true
			05     ,  a,       a,        false
		""")
	void testValidatorKckp10KlassenKlassenart(final Long jg, final String parallelitaet, final String parallelitaet2, final boolean expected) {
		final List<KlassenStatistikGesamt> listKlassenStatistikGesamt = new ArrayList<>();

		final KlassenStatistikGesamt klasse1 = new KlassenStatistikGesamt();
		klasse1.idJahrgang = jg;
		klasse1.parallelitaet = parallelitaet;
		listKlassenStatistikGesamt.add(klasse1);

		final KlassenStatistikGesamt klasse2 = new KlassenStatistikGesamt();
		klasse2.idJahrgang = jg;
		klasse2.parallelitaet = parallelitaet2;
		listKlassenStatistikGesamt.add(klasse2);

		final ValidatorKontext kontext =
				new ValidatorKontext(testdaten_001.schule.schulNr, Schulform.data().getWertByKuerzelOrException(testdaten_001.schule.schulform),
						testdaten_001.schule.abschnitte, testdaten_001.schule.idSchuljahresabschnitt, true);

		final ValidatorKckp10KlassenKombinationKlassenjahrgangParallelitaet validator =
				new ValidatorKckp10KlassenKombinationKlassenjahrgangParallelitaet(
						() -> listKlassenStatistikGesamt,
						kontext);

		assertEquals(expected, validator.pruefe());
	}
}
