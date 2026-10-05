package de.svws_nrw.core.utils.reporting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.svws_nrw.core.data.reporting.ReportingEMailDaten;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.data.reporting.ReportingSortierungDefinition;
import de.svws_nrw.core.types.reporting.ReportingEMailEmpfaengerTyp;

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

	@Test
	void testErzeugeEmailParameterOhneZulassungVerwirftVorbelegung() {
		final ReportingEMailDaten daten = ReportingReportvorlageUtils.erzeugeEmailParameter(ReportingEMailEmpfaengerTyp.LEHRER, false, true, "", "");

		assertFalse(daten.istPrivateEmailAlternativeZulaessig);
		assertFalse(daten.istPrivateEmailAlternative, "Eine nicht zugelassene private E-Mail-Adresse darf nicht vorbelegt sein.");
	}

	@Test
	void testErzeugeEmailParameterMitZulassungBehaeltVorbelegung() {
		final ReportingEMailDaten daten = ReportingReportvorlageUtils.erzeugeEmailParameter(ReportingEMailEmpfaengerTyp.LEHRER, true, true, "", "");

		assertTrue(daten.istPrivateEmailAlternativeZulaessig);
		assertTrue(daten.istPrivateEmailAlternative);
	}

	@Test
	void testCloneReportingParameterUebernimmtZulassungDerPrivatenEmail() {
		final ReportingParameter quelle = new ReportingParameter();
		quelle.eMailDaten = ReportingReportvorlageUtils.erzeugeEmailParameter(ReportingEMailEmpfaengerTyp.LEHRER, false, false, "", "");

		final ReportingParameter kopie = ReportingReportvorlageUtils.cloneReportingParameter(quelle);

		assertFalse(kopie.eMailDaten.istPrivateEmailAlternativeZulaessig, "Die Kopie muss die Vorgabe der Vorlage behalten.");
	}

	private static ReportingSortierungDefinition definition(final String bezeichnung) {
		final ReportingSortierungDefinition sd = new ReportingSortierungDefinition();
		sd.bezeichnung = bezeichnung;
		sd.typ = "ReportingSchueler";
		return sd;
	}
}
