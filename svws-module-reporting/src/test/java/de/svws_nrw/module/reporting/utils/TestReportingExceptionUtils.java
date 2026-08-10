package de.svws_nrw.module.reporting.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Fehlerprotokollierung aus {@link ReportingExceptionUtils}.
 * <p>Der Log-Block besteht aus vier Teilen: Beschreibung, Fehlertyp mit Meldung, Ursachenkette und Stacktrace. Jeder Teil trägt eine eigene Information;
 * dieselbe Meldung mehrfach auszugeben, hilft beim Auswerten eines Fehlers nicht. Geprüft wird deshalb, dass die Meldung im strukturierten Teil des Blocks
 * genau einmal erscheint - die Wiederholung in der Kopfzeile des Stacktrace gibt Java vor und bleibt außer Betracht.</p>
 * <p>Der Block wird auch an den Client ausgeliefert: {@code getLogAsSimpleOperationResponse} verwendet dieselben Einträge als Body der Fehlerantwort.</p>
 */
class TestReportingExceptionUtils {

	/** Die Beschreibung, die dem Fehler im Log vorangestellt wird. */
	private static final String BESCHREIBUNG = "### Fehler bei der Erzeugung der Response einer API-Anfrage für eine PDF-Generierung.";

	/** Die Meldung der geworfenen Exception. */
	private static final String MELDUNG = "### FEHLER: Der Root-Pfad zu den Ressourcen wurde nicht gefunden.";

	/** Die Überschrift des Abschnitts mit der Ursachenkette. */
	private static final String UEBERSCHRIFT_GRUENDE = "### FEHLERGRÜNDE:";

	/** Die Überschrift des Abschnitts mit dem Stacktrace. */
	private static final String UEBERSCHRIFT_STACKTRACE = "### STACKTRACE:";

	/** Der Logger, in den protokolliert wird. */
	private Logger logger;

	/** Die Liste, die die Einträge des Loggers sammelt. */
	private LogConsumerList log;


	@BeforeEach
	void setUp() {
		logger = new Logger();
		log = new LogConsumerList();
		logger.addConsumer(log);
	}


	/**
	 * Gibt die Einträge des Log-Blocks bis zum Beginn des Stacktrace zurück. Der Stacktrace bleibt außen vor: Er wiederholt die Meldung in seiner
	 * Kopfzeile, weil Java sie dort ausgibt, und diese Wiederholung ist nicht Gegenstand der Prüfung.
	 *
	 * @return Die Texte der Einträge vor dem Stacktrace, ohne die Einrückung des Loggers.
	 */
	private List<String> eintraegeOhneStacktrace() {
		final List<String> eintraege = log.getLogData().stream().map(eintrag -> eintrag.getText().strip()).toList();
		final int beginnStacktrace = eintraege.indexOf(UEBERSCHRIFT_STACKTRACE);
		return (beginnStacktrace < 0) ? eintraege : eintraege.subList(0, beginnStacktrace);
	}


	@Test
	void testOhneUrsacheErscheintDieMeldungGenauEinmal() {
		final ApiOperationException aoe = new ApiOperationException(Status.INTERNAL_SERVER_ERROR, MELDUNG);

		ReportingExceptionUtils.logException(BESCHREIBUNG, aoe, logger, LogLevel.ERROR, 0);

		final List<String> eintraege = eintraegeOhneStacktrace();
		assertEquals(1, eintraege.stream().filter(MELDUNG::equals).count(), "Die Meldung darf im Log-Block nur einmal stehen: " + eintraege);
		assertFalse(eintraege.contains(UEBERSCHRIFT_GRUENDE), "Ohne Ursache darf der Abschnitt mit der Ursachenkette nicht erscheinen.");
		assertTrue(eintraege.contains(BESCHREIBUNG), "Die Beschreibung ordnet den Fehler ein und bleibt erhalten.");
		assertTrue(eintraege.stream().anyMatch(eintrag -> eintrag.contains("Code: 500")), "Der Statuscode bleibt erhalten.");
	}

	@Test
	void testMitUrsacheZeigtDerAbschnittNurDieUrsachen() {
		final IllegalStateException ursache = new IllegalStateException("Die Schriftart konnte nicht geladen werden.");
		final ApiOperationException aoe = new ApiOperationException(Status.INTERNAL_SERVER_ERROR, ursache, MELDUNG);

		ReportingExceptionUtils.logException(BESCHREIBUNG, aoe, logger, LogLevel.ERROR, 0);

		final List<String> eintraege = eintraegeOhneStacktrace();
		assertTrue(eintraege.contains(UEBERSCHRIFT_GRUENDE), "Mit Ursache muss der Abschnitt erscheinen.");
		assertTrue(eintraege.contains("Die Schriftart konnte nicht geladen werden."), "Der Abschnitt muss die Ursache nennen.");
		assertEquals(1, eintraege.stream().filter(MELDUNG::equals).count(), "Die eigene Meldung gehört nicht in die Ursachenkette: " + eintraege);
	}

	@Test
	void testDieMeldungEinerFremdenExceptionWirdProtokolliert() {
		// Ohne diese Ausgabe stünde die Fehlerbeschreibung eines nicht als ApiOperationException geworfenen Fehlers allein im Stacktrace.
		final IllegalArgumentException exception = new IllegalArgumentException("Der Wert liegt außerhalb des zulässigen Bereichs.");

		ReportingExceptionUtils.logException(BESCHREIBUNG, exception, logger, LogLevel.ERROR, 0);

		final List<String> eintraege = eintraegeOhneStacktrace();
		assertTrue(eintraege.contains("Der Wert liegt außerhalb des zulässigen Bereichs."), "Die Meldung muss auch ohne ApiOperationException erscheinen.");
		assertTrue(eintraege.stream().anyMatch(eintrag -> eintrag.contains("Fehler vom Typ IllegalArgumentException")), "Der Fehlertyp bleibt erhalten.");
	}

	@Test
	void testDerStacktraceWirdWeiterhinAusgegeben() {
		final ApiOperationException aoe = new ApiOperationException(Status.INTERNAL_SERVER_ERROR, MELDUNG);

		ReportingExceptionUtils.logException(BESCHREIBUNG, aoe, logger, LogLevel.ERROR, 0);

		final List<String> eintraege = log.getLogData().stream().map(eintrag -> eintrag.getText().strip()).toList();
		assertTrue(eintraege.contains(UEBERSCHRIFT_STACKTRACE), "Der Stacktrace ist die genaueste Angabe zur Fehlerstelle und bleibt erhalten.");
		assertTrue(eintraege.stream().anyMatch(eintrag -> eintrag.startsWith("at ")), "Der Stacktrace muss seine Aufrufkette enthalten.");
	}

	@Test
	void testMitInfoEntstehenNurInfoEintraege() {
		// Ein Aufrufer, der den Fehler mit einem Rückfallwert auffängt, darf keinen ERROR-Eintrag hinterlassen: Die Prüfung des Logs in der
		// ReportingFactory bricht die Ausgabe allein wegen des Eintrags ab, auch ohne Wurf.
		ReportingExceptionUtils.logException(BESCHREIBUNG, exceptionMitUrsache(), logger, LogLevel.INFO, 0);

		assertAlleEintraegeAufLevel(LogLevel.INFO);
	}

	@Test
	void testMitWarningEntstehenNurWarningEintraege() {
		ReportingExceptionUtils.logException(BESCHREIBUNG, exceptionMitUrsache(), logger, LogLevel.WARNING, 0);

		assertAlleEintraegeAufLevel(LogLevel.WARNING);
	}

	@Test
	void testMitErrorBleibenAlleEintraegeAufError() {
		ReportingExceptionUtils.logException(BESCHREIBUNG, exceptionMitUrsache(), logger, LogLevel.ERROR, 0);

		assertAlleEintraegeAufLevel(LogLevel.ERROR);
	}

	@Test
	void testOhneExceptionGiltDasUebergebeneLevelEbenfalls() {
		ReportingExceptionUtils.logException(BESCHREIBUNG, null, logger, LogLevel.WARNING, 0);

		assertAlleEintraegeAufLevel(LogLevel.WARNING);
	}

	@Test
	void testDerEinzugDesLoggersBleibtUnveraendert() {
		// Ohne Wiederherstellung summiert sich der Einzug über jeden weiteren Log-Eintrag des Reports auf, sobald ein Aufruf nicht mit einem Wurf endet.
		final int einzugVorher = 8;
		logger.setIndent(einzugVorher);

		for (final LogLevel loglevel : List.of(LogLevel.INFO, LogLevel.WARNING, LogLevel.ERROR)) {
			ReportingExceptionUtils.logException(BESCHREIBUNG, exceptionMitUrsache(), logger, loglevel, 4);
			assertEquals(einzugVorher, logger.getIndent(), "Der Einzug muss nach dem Aufruf mit " + loglevel + " derselbe sein wie davor.");
		}

		ReportingExceptionUtils.logException(BESCHREIBUNG, null, logger, LogLevel.WARNING, 4);
		assertEquals(einzugVorher, logger.getIndent(), "Auch ohne Exception muss der Einzug wiederhergestellt werden.");
	}


	/**
	 * Erzeugt eine Exception mit Ursache, damit der Log-Block alle vier Teile enthält - Beschreibung, Fehlertyp mit Meldung, Ursachenkette und Stacktrace.
	 *
	 * @return Die Exception mit Ursache.
	 */
	private static ApiOperationException exceptionMitUrsache() {
		final IllegalStateException ursache = new IllegalStateException("Die Schriftart konnte nicht geladen werden.");
		return new ApiOperationException(Status.INTERNAL_SERVER_ERROR, ursache, MELDUNG);
	}


	/**
	 * Prüft, dass sämtliche Einträge des Logs auf dem erwarteten Level stehen.
	 *
	 * @param erwartet Das Level, das für den gesamten Block gilt.
	 */
	private void assertAlleEintraegeAufLevel(final LogLevel erwartet) {
		final List<String> abweichende = log.getLogData().stream().filter(eintrag -> eintrag.getLevel() != erwartet)
				.map(eintrag -> eintrag.getLevel() + ": " + eintrag.getText().strip()).toList();
		assertTrue(abweichende.isEmpty(), "Das übergebene Level gilt für den gesamten Block, abweichend sind: " + abweichende);
		assertFalse(log.getLogData().isEmpty(), "Der Block darf nicht leer sein, sonst prüft der Test nichts.");
	}

}
