package de.svws_nrw.module.reporting.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;

import java.sql.SQLNonTransientConnectionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft die Meldefassade {@code ReportingContext.meldeAusgabeproblem(...)} an der Stelle, an der sie nicht mehr meldet: Eine Ursache, die die Ausgabe
 * abbricht, wird als Serverfehler geworfen statt gesammelt. Der Fehler wird dafür gezielt injiziert.
 * <p>Der Context wird gemockt, weil sein einziger Konstruktor eine Datenbankverbindung und alle neun Repositories aufbaut; allein die Fassade läuft als echte
 * Methode. Der hingenommene Zweig führt weiter zum Problemsammler, den ein Mock nicht besitzt - er ist über {@code TestReportingProblemSammler} und die
 * Rückfallwert-Tests der Repositories abgedeckt.</p>
 */
class TestReportingContextMeldefassade {

	/** Die ID des Schülers, dessen Teildaten in den Tests fehlen. */
	private static final long ID_SCHUELER = 42L;

	/** Der gemockte Context, dessen Meldefassade als echte Methode läuft. */
	private ReportingContext reportingContext;


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		doCallRealMethod().when(reportingContext).meldeAusgabeproblem(any(), any(), any(), any(), any());
	}


	@Test
	void testEineInfrastrukturstoerungBrichtDieAusgabeMitEinemServerfehlerAb() {
		final SQLNonTransientConnectionException fehler = new SQLNonTransientConnectionException("Verbindung zur Datenbank verloren.");

		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> reportingContext.meldeAusgabeproblem(ReportingProblemursache.INFRASTRUKTURSTOERUNG, ReportingProblemauswirkung.TEILDATEN_FEHLEN,
						ReportingProblemSchluessel.fuer(ReportingSchueler.class, ID_SCHUELER), "Die Lernabschnitte fehlen.", fehler));

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus(), "Ein nicht arbeitsfähiger Server ist ein Serverfehler.");
		assertSame(fehler, aoe.getCause(), "Der auslösende Fehler gehört in den Abbruch: Er ist die einzige Spur der Störung.");
		assertTrue(aoe.getBody().toString().contains("ReportingSchueler " + ID_SCHUELER),
				"Der betroffene Datensatz gehört in die Meldung des Abbruchs: " + aoe.getBody());
	}

	@Test
	void testEinVerbindungsfehlerBeimLadenVonTeildatenBrichtAb() {
		// Der Weg aller Repositories: Die gemeinsame Meldestelle übergibt den Fehler, die Klassifikation macht daraus die Infrastrukturstörung und die Fassade
		// beendet die Ausgabe. Die meldende Stelle selbst entscheidet darüber nichts.
		final Exception fehler = new IllegalStateException("Der Zugriff ist gescheitert.",
				new SQLNonTransientConnectionException("Verbindung zur Datenbank verloren."));

		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> ReportingRepositoryUtils.meldeTeildatenLadefehler(reportingContext,
						ReportingProblemSchluessel.fuer(ReportingSchueler.class, ID_SCHUELER), "Die Lernabschnitte des Schülers", fehler));

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertSame(fehler, aoe.getCause());
	}

}
