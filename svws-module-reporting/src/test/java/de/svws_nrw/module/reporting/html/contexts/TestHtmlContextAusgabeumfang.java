package de.svws_nrw.module.reporting.html.contexts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import de.svws_nrw.module.reporting.diagnose.ReportingAusgabeumfang;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.gost.fachwahlstatistik.ProxyReportingGostFachwahlstatistikenAbiturjahrgang;
import de.svws_nrw.module.reporting.types.gost.fachwahlstatistik.ReportingGostFachwahlstatistik;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

/**
 * Prüft die Zählweise des Ausgabeumfangs der beiden Datenaufbauten, deren Zahlen erst im Context-Aufbau feststehen.
 * <p>Beide Fälle sehen im Erfolgsfall unauffällig aus und wären deshalb ohne eigenen Test nicht bemerkt worden: Die Kursplanung erhält ihre Schülerliste
 * bereits verkürzt, und die Fachwahlstatistik legt eine nach Fächern eingeschränkte Liste in den Context, über die keine Vorlage iteriert. Aus der falschen
 * Quelle gezählt, meldete der Header in beiden Fällen eine vollständige Ausgabe.</p>
 */
class TestHtmlContextAusgabeumfang {

	/** Das Abiturjahr, für das die Fachwahlstatistiken angefordert werden. */
	private static final long ABITURJAHR = 2026L;


	/**
	 * Erzeugt ein Blockungsergebnis mit der angegebenen Zahl aus den Blockungsdaten und der angegebenen Zahl geladener Schüler.
	 *
	 * @param anzahlLautBlockungsdaten Die Zahl der Schüler, die die Blockungsdaten führen.
	 * @param anzahlGeladen            Die Zahl der Schüler, die das Repository geliefert hat.
	 *
	 * @return Das Blockungsergebnis.
	 */
	private static ReportingGostKursplanungBlockungsergebnis blockungsergebnis(final int anzahlLautBlockungsdaten, final int anzahlGeladen) {
		final List<ReportingSchueler> geladene = new ArrayList<>();
		for (int i = 0; i < anzahlGeladen; i++) {
			geladene.add(mock(ReportingSchueler.class));
		}
		return new ReportingGostKursplanungBlockungsergebnis(2026, 0, 0, 0, 0, anzahlLautBlockungsdaten, "Blockung",
				Map.of(), null, 1L, List.of(), List.of(), geladene, null, null);
	}


	@Test
	void testDieKursplanungZaehltDieAngefordertenSchuelerAusDenBlockungsdaten() {
		// Die Schülerliste des Ergebnisses ist bereits um ausgefilterte und nicht ladbare Schüler verkürzt. Würde sie als angeforderte Zahl dienen, meldete
		// der Header 115 von 115 - der Anwender erführe nie, dass fünf Schüler fehlen.
		final ReportingGostKursplanungBlockungsergebnis ergebnis = blockungsergebnis(120, 115);

		final HtmlContextGostKursplanungBlockungsergebnisSchueler context =
				mock(HtmlContextGostKursplanungBlockungsergebnisSchueler.class, org.mockito.Mockito.CALLS_REAL_METHODS);

		assertEquals(new ReportingAusgabeumfang(120, 115, false), context.ermittleAusgabeumfang(ergebnis));
	}

	@Test
	void testDieKursplanungMeldetDieZulaessigLeereAusgabeWennKeinSchuelerUebrigBleibt() {
		final ReportingGostKursplanungBlockungsergebnis ergebnis = blockungsergebnis(120, 0);

		final HtmlContextGostKursplanungBlockungsergebnisSchueler context =
				mock(HtmlContextGostKursplanungBlockungsergebnisSchueler.class, org.mockito.Mockito.CALLS_REAL_METHODS);

		assertEquals(new ReportingAusgabeumfang(120, 0, true), context.ermittleAusgabeumfang(ergebnis));
	}

	@Test
	void testDieFachwahlstatistikZaehltDieAusgegebenenFaecherUndNichtDieFachauswahl() {
		// Die Vorlage iteriert über die vollständige Liste. Die nach idsDetaildaten eingeschränkte Liste steht zwar im Context, wird aber von keiner Vorlage
		// gelesen. Aus ihr gezählt, meldete der Header bei einer Auswahl von einem Fach "1 von 1", während die Ausgabe alle vier Fächer zeigt.
		final ReportingContext reportingContext = mock(ReportingContext.class);
		final ReportingParameterTypisiert reportingParameter = mock(ReportingParameterTypisiert.class);
		when(reportingParameter.idsHauptdaten()).thenReturn(List.of(ABITURJAHR));
		when(reportingParameter.idsDetaildaten()).thenReturn(List.of(4711L));
		when(reportingContext.reportingParameter()).thenReturn(reportingParameter);

		try (MockedConstruction<ProxyReportingGostFachwahlstatistikenAbiturjahrgang> proxy =
				mockConstruction(ProxyReportingGostFachwahlstatistikenAbiturjahrgang.class, (proxyMock, ctx) -> {
					when(proxyMock.fachwahlstatistiken()).thenReturn(fachwahlstatistiken(4));
					when(proxyMock.fachwahlstatistikenByIds(List.of(4711L))).thenReturn(fachwahlstatistiken(1));
					when(proxyMock.fachwahlstatistikenHalbjahreByIds(org.mockito.ArgumentMatchers.anyList(), org.mockito.ArgumentMatchers.anyList()))
							.thenReturn(List.of());
				})) {
			new HtmlContextGostLaufbahnplanungAbiturjahrgangFachwahlstatistiken(reportingContext);
		}

		verify(reportingContext).meldeAusgabeumfang(new ReportingAusgabeumfang(4, 4, false));
	}

	/**
	 * Erzeugt die angegebene Zahl von Fachwahlstatistiken.
	 *
	 * @param anzahl Die Zahl der Statistiken.
	 *
	 * @return Die Liste der Statistiken.
	 */
	private static List<ReportingGostFachwahlstatistik> fachwahlstatistiken(final int anzahl) {
		final List<ReportingGostFachwahlstatistik> statistiken = new ArrayList<>();
		for (int i = 0; i < anzahl; i++) {
			statistiken.add(mock(ReportingGostFachwahlstatistik.class));
		}
		return statistiken;
	}

}
