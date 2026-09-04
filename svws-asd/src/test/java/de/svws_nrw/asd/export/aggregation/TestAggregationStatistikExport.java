package de.svws_nrw.asd.export.aggregation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.asd.utils.json.JsonReader;

/**
 * Testklasse für die Aggregation StatistikExport
 *
 * Testdaten: de/svws_nrw/asd/export/aggregation/StatistikGesamt.json
 *
 * Die Testdaten sind fehlerfrei und werden mit Jackson in die entsprechende statische Datenstruktur eingelesen.
 *
 */
@DisplayName("Teste die Aggregation der Gesamt-Statistikdaten einer Schule in das Format StatistikExport")
class TestAggregationStatistikExport {

	/** Statistikdaten der Schule */
//	static final StatistikGesamt testdaten_001 =
//			JsonReader.fromResource("de/svws_nrw/asd/export/aggregation/StatistikGesamt_BK.json", StatistikGesamt.class);

	static final StatistikGesamt testdaten_001 =
			JsonReader.fromResource("de/svws_nrw/asd/export/aggregation/StatistikGesamt_GY.json", StatistikGesamt.class);

//	static final StatistikGesamt testdaten_001 =
//			JsonReader.fromResource("de/svws_nrw/asd/export/aggregation/gesamt_Gym_neu_26-08-20.json", StatistikGesamt.class);

//	static final StatistikGesamt testdaten_001 =
//			JsonReader.fromResource("de/svws_nrw/asd/export/aggregation/StatistikGesamt_S.json", StatistikGesamt.class);

//	static final StatistikGesamt testdaten_001 =
//			JsonReader.fromResource("de/svws_nrw/asd/export/aggregation/StatistikGesamt_G.json", StatistikGesamt.class);


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
	 * Test von Aggregation StatistikExport mit validen Daten
	 *
	 * Eingabedaten: StatistikGesamt
	 * Testfall: Daten zulässig
	 * Ergebnis: Validator soll TRUE liefern
	 */
	@Test
	@DisplayName("Teste die Aggregation der Gesamt-Statistikdaten einer Schule in das Format StatistikExport.")
	void test001() {
		final AggregationStatistikExport aggregationStatistikExport = new AggregationStatistikExport(testdaten_001);
		assertEquals(true, aggregationStatistikExport.run());
		aggregationStatistikExport.getFehlermeldungen().stream().forEach(System.out::println);
	}

}
