package de.svws_nrw.module.reporting.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.builders.ReportBuilderContextPdf;
import de.svws_nrw.module.reporting.builders.ReportBuilderHtml;
import de.svws_nrw.module.reporting.builders.ReportBuilderPdf;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft die Fehlerprotokollierung über die <b>echte</b> Kette Renderer → {@link ReportBuilderPdf} → {@link PdfFactory} → Fehlerausgabe der obersten Ebene.
 * <p>Die übrigen Tests des Moduls prüfen je Ebene isoliert und mocken die darunterliegende. Damit lässt sich zwar zeigen, dass eine Ebene ihren Fehler
 * genau einmal protokolliert, nicht aber, wie oft dieselbe Meldung am Ende im Log steht. Genau das ist hier Gegenstand: Der Log-Block wird als
 * {@code SimpleOperationResponse} an den Client ausgeliefert.</p>
 * <p>Jede Ebene trägt genau eine Information bei: der Renderer die Fehlerbeschreibung, die Sammelausgabe den Dateinamen des betroffenen Dokuments, die
 * oberste Ebene den Vorgang samt Statuscode und Stacktrace. Der Builder trägt nichts bei und protokolliert deshalb nicht.</p>
 * <p>Die oberste Ebene wird durch den direkten Aufruf von {@link ReportingExceptionUtils#logException} nachgebildet — so verfährt die
 * {@code ReportingFactory} mit dem bei ihr ankommenden Fehler. Ein Aufbau der Factory selbst käme ohne Datenbankverbindung nicht aus.</p>
 */
class TestFehlerProtokollierungUeberEbenen {

	/** Ein Ressourcenpfad, den es nicht gibt - er lässt den Renderer als unterste Ebene scheitern. */
	private static final String ROOT_PFAD_UNBEKANNT = "de/svws_nrw/module/reporting/gibt-es-nicht/";

	/** Die Meldung, die der Renderer als Fehlerquelle erzeugt. */
	private static final String MELDUNG_QUELLE = "### FEHLER: Der Root-Pfad zu den Ressourcen wurde nicht gefunden. Angegebener Pfad: " + ROOT_PFAD_UNBEKANNT;

	/** Die Beschreibung, mit der die oberste Ebene den Vorgang einordnet. */
	private static final String BESCHREIBUNG = "### Fehler bei der Erzeugung der Response einer API-Anfrage für eine PDF-Generierung.";

	/** Die Überschrift des Abschnitts mit dem Stacktrace. */
	private static final String UEBERSCHRIFT_STACKTRACE = "### STACKTRACE:";

	/** Der Logger, den alle Ebenen gemeinsam verwenden. */
	private Logger logger;

	/** Die Liste, die die Einträge des Loggers sammelt. */
	private LogConsumerList log;

	/** Der Context, den die Factory erhält. */
	private ReportingContext reportingContext;


	@BeforeEach
	void setUp() {
		logger = new Logger();
		log = new LogConsumerList();
		logger.addConsumer(log);
		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(logger);
	}


	/**
	 * Gibt die Texte aller ERROR-Einträge bis zum Beginn des Stacktrace zurück. Der Stacktrace selbst bleibt außen vor: Seine Zeilen hängen von der
	 * Aufrufumgebung ab, und dass er die Meldung in seiner Kopfzeile wiederholt, gibt Java vor.
	 *
	 * @return Die Texte der ERROR-Einträge vor dem Stacktrace, ohne die Einrückung des Loggers.
	 */
	private List<String> fehlermeldungenVorStacktrace() {
		final List<String> eintraege =
				log.getLogData().stream().filter(eintrag -> eintrag.getLevel() == LogLevel.ERROR).map(eintrag -> eintrag.getText().strip()).toList();
		final int beginnStacktrace = eintraege.indexOf(UEBERSCHRIFT_STACKTRACE);
		return (beginnStacktrace < 0) ? eintraege : eintraege.subList(0, beginnStacktrace);
	}

	/**
	 * Lässt einen Fehler durch die echte Kette laufen: Der Renderer findet seine Ressourcen nicht, der Builder gibt den Fehler weiter, die Sammelausgabe
	 * ergänzt den Dateinamen, und die oberste Ebene erzeugt ihre Fehlerausgabe.
	 *
	 * @return Die Exception, die am Ende der Kette ankommt.
	 */
	private ApiOperationException fehlerDurchDieKette() {
		final PdfFactory pdfFactory = new PdfFactory(List.of(mock(ReportBuilderHtml.class)), false, reportingContext);
		final ReportBuilderPdf pdfBuilder = new ReportBuilderPdf(new ReportBuilderContextPdf()
				.withHtmlInput("<html><body><p>Testinhalt</p></body></html>")
				.withDateiname("Bescheinigung_Meier")
				.withStatischerDateiname("Bescheinigungen")
				.withRootPfad(ROOT_PFAD_UNBEKANNT)
				.withLogger(logger));

		final List<ReportBuilderPdf> pdfBuilders = List.of(pdfBuilder);
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> pdfFactory.createZIP(pdfBuilders));
		ReportingExceptionUtils.logException(BESCHREIBUNG, aoe, logger, LogLevel.ERROR, 0);
		return aoe;
	}


	@Test
	void testJedeEbeneProtokolliertGenauIhrenEigenenBeitrag() {
		fehlerDurchDieKette();

		assertEquals(List.of(
				MELDUNG_QUELLE,
				"### FEHLER: Die PDF-Datei 'Bescheinigung_Meier' konnte nicht erzeugt werden.",
				BESCHREIBUNG,
				"### FEHLER: Fehler vom Typ ApiOperationException - Code: 500",
				MELDUNG_QUELLE),
				fehlermeldungenVorStacktrace(), "Der Log-Block muss genau diese Einträge enthalten.");
	}

	@Test
	void testDieMeldungDerFehlerquelleErscheintNichtOefterAlsNoetig() {
		fehlerDurchDieKette();

		// Zweimal: einmal am Ort ihrer Entstehung und einmal im strukturierten Block der obersten Ebene. Letzterer muss auch für Fehler funktionieren,
		// die zuvor nirgends protokolliert wurden, und kann deshalb nicht entfallen.
		assertEquals(2, fehlermeldungenVorStacktrace().stream().filter(MELDUNG_QUELLE::equals).count(),
				"Die Meldung der Fehlerquelle darf im Log-Block nur zweimal stehen: " + fehlermeldungenVorStacktrace());
	}

	@Test
	void testKeinEintragWiederholtDieMeldungEinerAnderenEbene() {
		fehlerDurchDieKette();

		// Die Einträge der Zwischenebenen dürfen die Meldung der Quelle nicht einbetten - sonst wächst dieselbe Meldung mit jeder Ebene mit.
		final List<String> eigeneEintraege = fehlermeldungenVorStacktrace().stream().filter(eintrag -> !MELDUNG_QUELLE.equals(eintrag)).toList();
		assertTrue(eigeneEintraege.stream().noneMatch(eintrag -> eintrag.contains(MELDUNG_QUELLE)),
				"Kein Eintrag darf die Meldung einer anderen Ebene mitführen: " + eigeneEintraege);
	}

	@Test
	void testDerStatusUeberlebtDieGesamteKette() {
		final ApiOperationException aoe = fehlerDurchDieKette();

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertEquals(MELDUNG_QUELLE, aoe.getBody(), "Die Meldung der Fehlerquelle muss den API-Rand unverändert erreichen.");
	}

}
