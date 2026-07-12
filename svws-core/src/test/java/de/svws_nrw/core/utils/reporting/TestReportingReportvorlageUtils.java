package de.svws_nrw.core.utils.reporting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.svws_nrw.core.data.reporting.ReportingSortierungDefinition;

/**
 * Testklasse für die Hilfsmethoden aus {@link ReportingReportvorlageUtils}.
 */
class TestReportingReportvorlageUtils {

	@Test
	void testWaehleGespeicherteAuswahlLiefertSelbeInstanzenInReihenfolge() {
		final ReportingSortierungDefinition a = definition("A");
		final ReportingSortierungDefinition b = definition("B");
		final ReportingSortierungDefinition c = definition("C");

		final List<ReportingSortierungDefinition> ergebnis =
				ReportingReportvorlageUtils.waehleGespeicherteAuswahl(List.of(a, b, c), sd -> sd.bezeichnung, List.of("C", "A"));

		assertEquals(2, ergebnis.size());
		assertSame(c, ergebnis.get(0), "Die Reihenfolge der gespeicherten Bezeichnungen muss erhalten bleiben.");
		assertSame(a, ergebnis.get(1), "Es muss die Original-Options-Instanz zurückgegeben werden (Objektidentität).");
	}

	@Test
	void testWaehleGespeicherteAuswahlUeberspringtUnbekannteBezeichnungen() {
		final ReportingSortierungDefinition a = definition("A");

		final List<ReportingSortierungDefinition> ergebnis =
				ReportingReportvorlageUtils.waehleGespeicherteAuswahl(List.of(a), sd -> sd.bezeichnung, List.of("Weggefallen", "A"));

		assertEquals(List.of("A"), ergebnis.stream().map(sd -> sd.bezeichnung).toList(),
				"Eine nicht mehr im Katalog vorhandene Bezeichnung wird übersprungen.");
	}

	@Test
	void testWaehleGespeicherteAuswahlBeiLeererAuswahlLiefertLeereListe() {
		final List<ReportingSortierungDefinition> ergebnis =
				ReportingReportvorlageUtils.waehleGespeicherteAuswahl(List.of(definition("A")), sd -> sd.bezeichnung, new ArrayList<>());

		assertTrue(ergebnis.isEmpty(), "Ohne gespeicherte Bezeichnungen ist die Auswahl leer.");
	}

	private static ReportingSortierungDefinition definition(final String bezeichnung) {
		final ReportingSortierungDefinition sd = new ReportingSortierungDefinition();
		sd.bezeichnung = bezeichnung;
		sd.typ = "ReportingSchueler";
		return sd;
	}
}
