package de.svws_nrw.module.reporting.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLNonTransientConnectionException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
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
 * Methode. Sie erreicht ihren Logger über den Getter {@code logger()}, weil der Mock kein Feld trägt; die Log-Zusicherungen hängen daran. Der hingenommene Zweig führt weiter zum Problemsammler, den ein Mock nicht besitzt - er ist über {@code TestReportingProblemSammler} und die
 * Rückfallwert-Tests der Repositories abgedeckt.</p>
 */
class TestReportingContextMeldefassade {

	/** Die ID des Schülers, dessen Teildaten in den Tests fehlen. */
	private static final long ID_SCHUELER = 42L;

	/** Der gemockte Context, dessen Meldefassade als echte Methode läuft. */
	private ReportingContext reportingContext;

	/** Die Liste, die die Einträge des Loggers sammelt. */
	private LogConsumerList log;


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		doCallRealMethod().when(reportingContext).meldeAusgabeproblem(any(), any(), any(), any(), any());
		final Logger logger = new Logger();
		log = new LogConsumerList();
		logger.addConsumer(log);
		when(reportingContext.logger()).thenReturn(logger);
	}

	/**
	 * Gibt die Texte der Log-Einträge mit dem Level ERROR zurück, ohne die Einrückung des Loggers.
	 *
	 * @return Die Texte in der Reihenfolge ihres Auftretens.
	 */
	private List<String> fehlermeldungenImLog() {
		return log.getLogData().stream().filter(eintrag -> eintrag.getLevel() == LogLevel.ERROR).map(eintrag -> eintrag.getText().strip()).toList();
	}


	@Test
	void testEineInfrastrukturstoerungBrichtDieAusgabeMitEinemServerfehlerAb() {
		final SQLNonTransientConnectionException fehler = new SQLNonTransientConnectionException("Verbindung zur Datenbank verloren.");

		final ReportingProblemSchluessel schluessel = ReportingProblemSchluessel.fuer(ReportingSchueler.class, ID_SCHUELER);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> reportingContext.meldeAusgabeproblem(ReportingProblemursache.INFRASTRUKTURSTOERUNG, ReportingProblemauswirkung.TEILDATEN_FEHLEN,
						schluessel, "Die Lernabschnitte fehlen.", fehler));

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus(), "Ein nicht arbeitsfähiger Server ist ein Serverfehler.");
		assertSame(fehler, aoe.getCause(), "Der auslösende Fehler gehört in den Abbruch: Er ist die einzige Spur der Störung.");
		final String meldung = assertInstanceOf(String.class, aoe.getBody(), "Ohne String-Body bliebe die Kopfzeile der Fehlerantwort ohne Abbruchgrund.");
		assertFalse(meldung.contains("ReportingSchueler"), "Der interne Schlüssel gehört nicht in die Meldung an den Anwender: " + meldung);
		assertEquals(List.of("Betroffen: Die Lernabschnitte fehlen. [ReportingSchueler " + ID_SCHUELER + "]"), fehlermeldungenImLog(),
				"Welcher Zugriff scheiterte und welcher Datensatz betroffen ist, trägt sonst niemand - die technische Zeile nennt beides.");
	}

	@Test
	void testEinVerbindungsfehlerBeimLadenVonTeildatenBrichtAb() {
		// Der Weg aller Repositories: Die gemeinsame Meldestelle übergibt den Fehler, die Klassifikation macht daraus die Infrastrukturstörung und die Fassade
		// beendet die Ausgabe. Die meldende Stelle selbst entscheidet darüber nichts.
		final Exception fehler = new IllegalStateException("Der Zugriff ist gescheitert.",
				new SQLNonTransientConnectionException("Verbindung zur Datenbank verloren."));

		final ReportingProblemSchluessel schluessel = ReportingProblemSchluessel.fuer(ReportingSchueler.class, ID_SCHUELER);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> ReportingRepositoryUtils.meldeTeildatenLadefehler(reportingContext, schluessel, "Die Lernabschnitte des Schülers", fehler));

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertSame(fehler, aoe.getCause());
		assertEquals(1, fehlermeldungenImLog().size(), "Genau eine technische Zeile: " + fehlermeldungenImLog());
		final String zeile = fehlermeldungenImLog().getFirst();
		assertTrue(zeile.contains("Die Lernabschnitte des Schülers"), "Die Zeile nennt den gescheiterten Zugriff: " + zeile);
		assertTrue(zeile.contains("[ReportingSchueler " + ID_SCHUELER + "]"), "Die Zeile nennt den betroffenen Datensatz: " + zeile);
	}

}
