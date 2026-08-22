package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraumstunde;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungRaum;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungUnterrichtsrasterstunde;

/**
 * Tests der fehlenden Referenzen beim Aufbau eines Klausurraums: eine Zeitrasterstunde oder ein zugewiesener Raum, den der zum Termin gültige Stundenplan
 * nicht führt. Beide werden ausgelassen und als Ausgabeproblem gemeldet - die Vorlage zeigt eine Lücke, aber die Ausgabe meldet sich nicht als vollständig.
 * Ungeprüft bräche die Zeitrasterstunde die Sortierung mit einer NullPointerException ab.
 */
class TestProxyReportingGostKlausurplanungKlausurraum {

	/** Das Datum des Klausurtermins, zu dem der Stundenplan ermittelt wird. */
	private static final String DATUM = "2026-03-14";

	/** Der gemockte Context, den der Proxy erhält. */
	private ReportingContext reportingContext;

	/** Der gemockte Stundenplan zum Datum des Klausurtermins. */
	private ReportingStundenplanungStundenplan stundenplan;

	/** Der gemockte Klausurtermin des Raums. */
	private ReportingGostKlausurplanungKlausurtermin klausurtermin;


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		final ReportingRepositoryStundenplan repositoryStundenplan = mock(ReportingRepositoryStundenplan.class);
		stundenplan = mock(ReportingStundenplanungStundenplan.class);
		when(repositoryStundenplan.stundenplan(DATUM)).thenReturn(stundenplan);
		when(reportingContext.repositoryStundenplan()).thenReturn(repositoryStundenplan);
		klausurtermin = mock(ReportingGostKlausurplanungKlausurtermin.class);
		when(klausurtermin.datum()).thenReturn(DATUM);
	}


	@Test
	void testEinRaumAusEinemFremdenStundenplanWirdGemeldetUndDieRaumangabeBleibtLeer() {
		// Die Zuweisungsvalidierung prüft nur die globale Existenz des Raums; eine Raum-ID aus einem anderen Stundenplan bleibt deshalb möglich.
		final GostKlausurraum raum = new GostKlausurraum();
		raum.id = 5L;
		raum.idStundenplanRaum = 77L;

		final ProxyReportingGostKlausurplanungKlausurraum klausurraum =
				new ProxyReportingGostKlausurplanungKlausurraum(reportingContext, klausurtermin, raum, List.of());

		assertNull(klausurraum.raumdaten(), "Die Raumangabe bleibt leer; die Vorlage zeigt ihre Lücke.");
		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.NICHT_VORHANDEN),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(ReportingStundenplanungRaum.class, 77L)),
				anyString(), eq(null));
	}

	@Test
	void testEineUnbekannteZeitrasterstundeWirdAusgelassenUndGemeldet() {
		final GostKlausurraum raum = new GostKlausurraum();
		raum.id = 5L;
		final GostKlausurraumstunde stunde = new GostKlausurraumstunde();
		stunde.idZeitraster = 99L;
		when(stundenplan.unterrichtsrasterstunde(99L)).thenReturn(null);

		final ProxyReportingGostKlausurplanungKlausurraum klausurraum =
				new ProxyReportingGostKlausurplanungKlausurraum(reportingContext, klausurtermin, raum, List.of(stunde));

		assertTrue(klausurraum.aufsichten().isEmpty(), "Die Aufsichtsstunde entfällt; ungeprüft bräche die Sortierung mit einer NullPointerException ab.");
		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.NICHT_VORHANDEN),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN),
				eq(ReportingProblemSchluessel.fuer(ReportingStundenplanungUnterrichtsrasterstunde.class, 99L)), anyString(), eq(null));
	}

}
