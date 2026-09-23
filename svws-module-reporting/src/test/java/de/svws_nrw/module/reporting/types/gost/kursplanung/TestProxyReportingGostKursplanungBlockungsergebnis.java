package de.svws_nrw.module.reporting.types.gost.kursplanung;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.module.reporting.filterung.ReportingFilterService;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryGost;

/**
 * Prüft die Fachwahlstatistik des Blockungsergebnisses. Der Aufbau im Repository gibt dem Objekt seinen Manager mit; die Einzelausgabe erhält keinen und
 * übernimmt die Statistik der Quelle als endgültiges Ergebnis.
 */
@DisplayName("Die Fachwahlstatistik des Blockungsergebnisses")
class TestProxyReportingGostKursplanungBlockungsergebnis {

	/** Die ID des Blockungsergebnisses. */
	private static final long ID_BLOCKUNGSERGEBNIS = 3L;

	/** Der gemockte Context. */
	private ReportingContext reportingContext;

	/** Das Repository der gymnasialen Oberstufe, aus dem die Fachwahlen der Laufbahnplanung stammen. */
	private ReportingRepositoryGost repositoryGost;


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		when(reportingContext.filterService()).thenReturn(mock(ReportingFilterService.class));
		repositoryGost = mock(ReportingRepositoryGost.class);
		when(repositoryGost.fachwahlenOptional(anyInt())).thenReturn(new ArrayList<>());
		when(reportingContext.repositoryGost()).thenReturn(repositoryGost);
	}


	/**
	 * Erzeugt ein Blockungsergebnis, wie es der Aufbau des Repositorys liefert.
	 *
	 * @param ergebnisManager Der Manager des Blockungsergebnisses.
	 *
	 * @return Das Blockungsergebnis mit leerer Fachwahlstatistik.
	 */
	private ProxyReportingGostKursplanungBlockungsergebnis erzeugeVollaufbau(final GostBlockungsergebnisManager ergebnisManager) {
		return new ProxyReportingGostKursplanungBlockungsergebnis(reportingContext, ergebnisManager, 2025, 0, 0, 0, 0, 0,
				"Blockung", GostHalbjahr.Q11, ID_BLOCKUNGSERGEBNIS, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
	}


	@Test
	void testDieEinzelausgabeGibtDieLeereStatistikDerQuelleUnveraendertZurueck() {
		// Die leere Statistik der Quelle stammt bereits aus einem Aufbau und ist endgültig. Ein eigener Aufbau liefe hier in eine NullPointerException.
		final ProxyReportingGostKursplanungBlockungsergebnis quelle = erzeugeVollaufbau(mock(GostBlockungsergebnisManager.class));
		final ProxyReportingGostKursplanungBlockungsergebnis einzelausgabe =
				new ProxyReportingGostKursplanungBlockungsergebnis(reportingContext, quelle, s -> true, k -> true);

		// Der Konstruktor der Einzelausgabe stößt den Aufbau der Quelle an. Gezählt wird allein, was danach geschieht.
		clearInvocations(repositoryGost);

		final Map<Long, ReportingGostKursplanungFachwahlstatistik> ersterZugriff = einzelausgabe.fachwahlstatistik();
		final Map<Long, ReportingGostKursplanungFachwahlstatistik> zweiterZugriff = einzelausgabe.fachwahlstatistik();

		assertTrue(ersterZugriff.isEmpty(), "Die Einzelausgabe übernimmt die leere Statistik der Quelle.");
		assertSame(ersterZugriff, zweiterZugriff, "Wiederholte Zugriffe liefern dieselbe Statistik ohne neuen Aufbau.");
		verify(repositoryGost, never()).fachwahlenOptional(anyInt());
	}

	@Test
	void testDerVollaufbauFragtDieFachwahlenDerLaufbahnplanungAb() {
		// Die Gegenprobe. Ohne sie bliebe ein fehlender Manager im Vollaufbau unbemerkt, und die Vorlage zeigte nur Platzhalter.
		final ProxyReportingGostKursplanungBlockungsergebnis vollaufbau = erzeugeVollaufbau(mock(GostBlockungsergebnisManager.class));

		assertTrue(vollaufbau.fachwahlstatistik().isEmpty(), "Ohne Fachwahlen bleibt die Statistik leer.");
		verify(repositoryGost).fachwahlenOptional(2025);
	}

}
