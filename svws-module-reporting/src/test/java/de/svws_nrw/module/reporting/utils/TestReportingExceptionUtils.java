package de.svws_nrw.module.reporting.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thymeleaf.exceptions.TemplateProcessingException;

import de.svws_nrw.core.data.SimpleOperationResponse;
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

	/** Ein Satz aus der Vorlage, an dem sich der Quelltext im Log erkennen lässt. */
	private static final String KENNSATZ_DER_VORLAGE = "Klassenliste mit Fotos";

	/**
	 * Der Quelltext einer Vorlage, wie Thymeleaf ihn als Namen des Templates führt. Die Vorlage wird der Engine als Zeichenkette übergeben, deshalb steht
	 * ihr ganzer Inhalt in den Meldungen der Thymeleaf-Exceptions.
	 */
	private static final String TEMPLATE_QUELLTEXT = """
			<!DOCTYPE html>
			<html lang="de" xmlns:th="http://www.thymeleaf.org">
				<head><title>%s</title></head>
				<body><div th:each="klasse : ${Klassen}" th:text="${klasse.kuerzel()}">06C</div></body>
			</html>""".formatted(KENNSATZ_DER_VORLAGE);

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
		// Ein Aufrufer, der den Fehler mit einem Rückfallwert auffängt, darf keinen ERROR-Eintrag hinterlassen: Dieses Level ist dem Abbruch vorbehalten.
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


	@Test
	void testDerTemplateQuelltextVerschwindetAuchAusDerTiefeDerUrsachenkette() {
		// Der Renderer verpackt die Thymeleaf-Exception in eine ApiOperationException, damit der Status des Abbruchs erhalten bleibt. Der Quelltext steht
		// damit nicht mehr in der äußersten Exception, sondern erst in der Ursachenkette.
		final TemplateProcessingException tpe =
				new TemplateProcessingException("An error happened during template parsing", TEMPLATE_QUELLTEXT, new IllegalStateException("Ursache"));
		final ApiOperationException aoe = new ApiOperationException(Status.BAD_REQUEST, tpe, MELDUNG);

		ReportingExceptionUtils.logException(BESCHREIBUNG, aoe, logger, LogLevel.ERROR, 0);

		final List<String> eintraege = log.getLogData().stream().map(eintrag -> eintrag.getText().strip()).toList();
		assertTrue(eintraege.stream().noneMatch(eintrag -> eintrag.contains(KENNSATZ_DER_VORLAGE)),
				"Der Quelltext der Vorlage darf weder im Log noch in der Fehlerantwort stehen: " + eintraege);
		assertTrue(eintraege.stream().anyMatch(eintrag -> eintrag.contains("REMOVED TEMPLATE FROM LOG")),
				"An der Stelle des Quelltexts muss der Platzhalter stehen, sonst wäre unklar, was fehlt.");
	}

	@Test
	void testAuchEineFremdeExceptionVerliertDenTemplateQuelltextAusIhrerMeldung() {
		// Der reale Fall: Zwischen den Thymeleaf-Exceptions liegt eine ParseException des Parsers. Sie trägt den Quelltext in ihrer Meldung, ist aber
		// selbst keine Thymeleaf-Klasse und liefert deshalb keinen Templatenamen. Maskiert wird sie nur, weil die Namen der ganzen Kette gelten.
		final TemplateProcessingException tpe = new TemplateProcessingException("Fehler beim Auswerten", TEMPLATE_QUELLTEXT, null);
		final IllegalStateException fremde = new IllegalStateException("Parserfehler (template: \"" + TEMPLATE_QUELLTEXT + "\" - line 4, col 12)", tpe);
		final ApiOperationException aoe = new ApiOperationException(Status.INTERNAL_SERVER_ERROR, fremde, MELDUNG);

		ReportingExceptionUtils.logException(BESCHREIBUNG, aoe, logger, LogLevel.ERROR, 0);

		final List<String> eintraege = log.getLogData().stream().map(eintrag -> eintrag.getText().strip()).toList();
		assertTrue(eintraege.stream().noneMatch(eintrag -> eintrag.contains(KENNSATZ_DER_VORLAGE)),
				"Der Quelltext muss auch aus der Meldung einer fremden Exception verschwinden: " + eintraege);
	}

	@Test
	void testAuchEineKurzeVorlageGiltAlsQuelltext() {
		// Eine Vorlage aus einem Test ist kurz und einzeilig. Sie bleibt Quelltext und gehört nicht ins Log.
		final String kurzeVorlage = "<html><body><span th:text=\"${1/0}\">Platzhalter</span></body></html>";
		final TemplateProcessingException tpe = new TemplateProcessingException("Division durch null", kurzeVorlage, new ArithmeticException("/ by zero"));

		ReportingExceptionUtils.logException(BESCHREIBUNG, tpe, logger, LogLevel.ERROR, 0);

		final List<String> eintraege = log.getLogData().stream().map(eintrag -> eintrag.getText().strip()).toList();
		assertTrue(eintraege.stream().noneMatch(eintrag -> eintrag.contains("Platzhalter")),
				"Auch eine kurze Vorlage ist Quelltext und wird ersetzt: " + eintraege);
	}

	@Test
	void testDieKopfzeileUebergehtEinKettengliedOhneMeldung() {
		// Eine ApiOperationException mit einer Fehlerantwort als Body trägt keine Meldung. Der Abbruchgrund steht dann erst ein Glied tiefer.
		final ApiOperationException grund = new ApiOperationException(Status.BAD_REQUEST, "FEHLER: Betreff und Text der E-Mail fehlen.");
		final ApiOperationException ohneMeldung = new ApiOperationException(Status.BAD_REQUEST, grund, new SimpleOperationResponse(), "application/json");

		final String kopfzeile = ReportingExceptionUtils.abbruchKopfzeile(400, ohneMeldung, MELDUNG);

		assertTrue(kopfzeile.contains("Betreff und Text der E-Mail fehlen."),
				"Ein Glied ohne Meldung darf den Abbruchgrund nicht verdecken: " + kopfzeile);
	}

	@Test
	void testDerNameEinesFragmentsBleibtErhalten() {
		// Ein Fragment wird über den Klassenpfad aufgelöst; sein Name ist kurz und eine nützliche Angabe zur Fehlerstelle.
		final TemplateProcessingException tpe =
				new TemplateProcessingException("Fehler im Fragment", "fragments/reportHtmlHead", new IllegalStateException("Ursache"));
		final ApiOperationException aoe = new ApiOperationException(Status.INTERNAL_SERVER_ERROR, tpe, MELDUNG);

		ReportingExceptionUtils.logException(BESCHREIBUNG, aoe, logger, LogLevel.ERROR, 0);

		final List<String> eintraege = log.getLogData().stream().map(eintrag -> eintrag.getText().strip()).toList();
		assertTrue(eintraege.stream().anyMatch(eintrag -> eintrag.contains("fragments/reportHtmlHead")),
				"Ein kurzer Templatename ist kein Quelltext und bleibt stehen: " + eintraege);
	}

	@Test
	void testEineThymeleafExceptionOhneTemplatenamenBrichtNichtAb() {
		final TemplateProcessingException tpe = new TemplateProcessingException("Fehler ohne Templatenamen");
		final ApiOperationException aoe = new ApiOperationException(Status.INTERNAL_SERVER_ERROR, tpe, MELDUNG);

		ReportingExceptionUtils.logException(BESCHREIBUNG, aoe, logger, LogLevel.ERROR, 0);

		assertTrue(eintraegeOhneStacktrace().contains(BESCHREIBUNG), "Ohne Templatenamen muss der Block wie gewohnt entstehen.");
	}

	@Test
	void testDieKopfzeileNenntStatusUndDenAbbruchgrundAusDerUrsachenkette() {
		final ApiOperationException grund = new ApiOperationException(Status.BAD_REQUEST, "FEHLER: Die Anmeldung am Signierdienst ist fehlgeschlagen.");
		final IllegalStateException verpackt = new IllegalStateException("Method failed", grund);

		final String kopfzeile = ReportingExceptionUtils.abbruchKopfzeile(400, verpackt, MELDUNG);

		assertTrue(kopfzeile.contains("Status 400"), "Die Kopfzeile muss den Statuscode nennen: " + kopfzeile);
		assertTrue(kopfzeile.contains("Die Anmeldung am Signierdienst ist fehlgeschlagen."),
				"Die Kopfzeile muss den fachlichen Abbruchgrund nennen und nicht die Stelle, an der er auffiel: " + kopfzeile);
	}

	@Test
	void testDieKopfzeileTraegtKeinenMarkerAusDerMeldung() {
		final ApiOperationException grund = new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "### FEHLER: Es sind keine PDF-Dokumente entstanden.");

		final String kopfzeile = ReportingExceptionUtils.abbruchKopfzeile(500, grund, MELDUNG);

		assertEquals("ABBRUCH (Status 500): FEHLER: Es sind keine PDF-Dokumente entstanden.", kopfzeile,
				"Der Marker der Meldung gehört nicht mitten in die Kopfzeile.");
	}

	@Test
	void testDieKopfzeileTraegtKeinenTemplateQuelltext() {
		// Einzelne Stellen übernehmen die Meldung eines fremden Fehlers in ihre eigene. Stammt sie von Thymeleaf, trägt sie den Quelltext der Vorlage.
		final TemplateProcessingException tpe = new TemplateProcessingException("Fehler beim Auswerten", TEMPLATE_QUELLTEXT, null);
		final ApiOperationException grund =
				new ApiOperationException(Status.INTERNAL_SERVER_ERROR, tpe, "### FEHLER: Das PDF wurde nicht erzeugt: " + tpe.getMessage());

		final String kopfzeile = ReportingExceptionUtils.abbruchKopfzeile(500, grund, MELDUNG);

		assertFalse(kopfzeile.contains(KENNSATZ_DER_VORLAGE), "Der Quelltext der Vorlage gehört nicht in die Kopfzeile: " + kopfzeile);
		assertTrue(kopfzeile.contains("REMOVED TEMPLATE FROM LOG"), "An seiner Stelle muss der Platzhalter stehen: " + kopfzeile);
	}

	@Test
	void testOhneKlassifizierteUrsacheGiltDieUebergebeneMeldung() {
		final String kopfzeile = ReportingExceptionUtils.abbruchKopfzeile(500, new IllegalStateException("Technischer Fehler"), MELDUNG);

		assertTrue(kopfzeile.contains("Der Root-Pfad zu den Ressourcen wurde nicht gefunden."),
				"Ohne ApiOperationException in der Kette bleibt die übergebene Meldung: " + kopfzeile);
	}

	@Test
	void testDieKopfzeileStehtAnErsterStelleDerFehlerantwort() {
		logger.logLn(LogLevel.WARNING, "Ein Befund des Laufs, der mit dem Abbruch nichts zu tun hat.");
		ReportingExceptionUtils.logException(BESCHREIBUNG, exceptionMitUrsache(), logger, LogLevel.ERROR, 0);

		final List<String> ausgabe = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log, "ABBRUCH (Status 400): Der Grund").log;

		assertEquals("ABBRUCH (Status 400): Der Grund", ausgabe.get(0).strip(),
				"Der Abbruchgrund steht sonst am Ende des Logs, unter unbeteiligten Befunden.");
		assertTrue(ausgabe.get(1).isBlank(), "Eine Leerzeile trennt die Kopfzeile vom Ablauf darunter: " + ausgabe.get(1));
		assertTrue(ausgabe.size() > 2, "Das übrige Log muss erhalten bleiben.");
	}

	@Test
	void testDieGanzeFehlerantwortTraegtKeinenTemplateQuelltext() {
		// Die Fehlerantwort besteht aus Kopfzeile und Log. Beide entstehen getrennt, tragen aber denselben Text - geprüft wird deshalb das Ganze.
		final TemplateProcessingException tpe = new TemplateProcessingException("Fehler beim Auswerten", TEMPLATE_QUELLTEXT, null);
		final ApiOperationException abbruch =
				new ApiOperationException(Status.INTERNAL_SERVER_ERROR, tpe, "### FEHLER: Das PDF wurde nicht erzeugt: " + tpe.getMessage());
		ReportingExceptionUtils.logException(BESCHREIBUNG, abbruch, logger, LogLevel.ERROR, 0);

		final List<String> ausgabe = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log,
				ReportingExceptionUtils.abbruchKopfzeile(500, abbruch, MELDUNG)).log;

		assertTrue(ausgabe.stream().noneMatch(zeile -> zeile.contains(KENNSATZ_DER_VORLAGE)),
				"Keine Zeile der Fehlerantwort darf den Quelltext der Vorlage tragen: "
						+ ausgabe.stream().filter(zeile -> zeile.contains(KENNSATZ_DER_VORLAGE)).toList());
		assertTrue(ausgabe.get(0).contains("REMOVED TEMPLATE FROM LOG"), "Auch die Kopfzeile trägt den Platzhalter: " + ausgabe.get(0));
	}

	@Test
	void testOhneKopfzeileBleibtDasLogUnveraendert() {
		ReportingExceptionUtils.logException(BESCHREIBUNG, exceptionMitUrsache(), logger, LogLevel.ERROR, 0);

		final List<String> ohneKopfzeile = ReportingExceptionUtils.getLogAsSimpleOperationResponse(log, "").log;

		assertEquals(log.getStrings().size(), ohneKopfzeile.size(), "Eine leere Kopfzeile darf keinen Eintrag erzeugen.");
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
