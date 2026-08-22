package de.svws_nrw.module.reporting.builders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Statusklassifikation des {@link ReportRendererHtml}.
 * <p>Template-Engine und Template baut der Server selbst auf; der API-Client übergibt kein HTML-Template. Fehlt eines von beiden, ist das deshalb ein
 * serverseitiges Problem und kein fehlerhafter Client-Input - der Renderer muss dafür {@code INTERNAL_SERVER_ERROR} melden.</p>
 * <p>Ebenso wird geprüft, dass eine bereits klassifizierte {@link ApiOperationException} den Renderer unverändert verlässt: Sie entsteht in den
 * Daten-Zugriffen, die die Vorlage während des Renderns auslöst, und wird vom allgemeinen Catch sonst zu einem Serverfehler verschluckt.</p>
 */
class TestReportRendererHtml {

	/** Ein Template, das die Daten des Test-Contexts ausgibt. */
	private static final String TEMPLATE = "<html><body><span th:text=\"${Daten[0]}\">Platzhalter</span></body></html>";

	/** Die Meldung, die bei fehlender Template-Engine oder fehlendem Template erwartet wird. */
	private static final String MELDUNG_KEIN_TEMPLATE = "### FEHLER: Die HTML-Template-Engine oder das HTML-Template wurden nicht übergeben.";

	/** Der Logger, den der Renderer in den Tests verwendet. */
	private Logger logger;

	/** Die Liste, die die Einträge des Loggers sammelt. */
	private LogConsumerList log;


	/**
	 * Ein minimaler Context, der eine Liste von Texten unter einem Variablennamen für die Vorlage bereitstellt. Er ersetzt die produktiven Contexts, die
	 * für ihren Aufbau jeweils Repositories und damit eine Datenbankverbindung benötigen.
	 */
	private static final class TestHtmlContext extends HtmlContext<String> {

		/**
		 * Erzeugt den Context mit den übergebenen Daten.
		 *
		 * @param variablenname Der Name der Thymeleaf-Variablen.
		 * @param daten         Die Daten, die unter diesem Namen bereitstehen.
		 */
		private TestHtmlContext(final String variablenname, final List<String> daten) {
			super(null);
			erzeugeContext(variablenname, daten);
		}
	}


	@BeforeEach
	void setUp() {
		logger = new Logger();
		log = new LogConsumerList();
		logger.addConsumer(log);
	}


	/**
	 * Gibt die Texte aller Logeinträge mit dem Level ERROR zurück. Die Einrückung, die der Logger dem Text voranstellt, wird dabei entfernt: Geprüft wird
	 * die Meldung, nicht ihre Formatierung.
	 *
	 * @return Die Texte der ERROR-Logeinträge in der Reihenfolge ihres Auftretens.
	 */
	private List<String> fehlermeldungenImLog() {
		return log.getLogData().stream().filter(eintrag -> eintrag.getLevel() == LogLevel.ERROR).map(eintrag -> eintrag.getText().strip()).toList();
	}

	/**
	 * Gibt die Contexts zurück, mit denen der Renderer in den Tests aufgerufen wird.
	 *
	 * @return Eine Liste mit einem Context, der die Variable "Daten" bereitstellt.
	 */
	private static List<HtmlContext<?>> contexts() {
		return List.of(new TestHtmlContext("Daten", List.of("Testinhalt")));
	}

	/**
	 * Prüft, dass die übergebene Exception das fehlende Template als Serverfehler meldet, ihre eigene Meldung behält und genau einmal im Log erscheint.
	 * <p>Neben dem Status wird die Meldung geprüft, weil sie die Ursache benennt: Fängt der allgemeine Catch die eigene Exception, ersetzt er Status
	 * <b>und</b> Meldung durch die unspezifische Angabe, das HTML habe nicht gerendert werden können.</p>
	 * <p>Der Log wird als vollständige Liste verglichen und nicht nur auf ein Vorkommen geprüft: Fängt der allgemeine Catch die eigene Exception, gibt er
	 * über {@code ReportingExceptionUtils} zusätzlich Typ, Fehlergründe und Stacktrace als ERROR aus. Genau diese zweite Ausgabe soll ausbleiben.</p>
	 *
	 * @param aoe Die geworfene Exception.
	 */
	private void pruefeMeldungFehlendesTemplate(final ApiOperationException aoe) {
		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertEquals(MELDUNG_KEIN_TEMPLATE, aoe.getBody(), "Die eigene Meldung darf nicht von der allgemeinen Fehlerbehandlung überschrieben werden.");
		assertEquals(List.of(MELDUNG_KEIN_TEMPLATE), fehlermeldungenImLog(), "Der Fehler muss genau einmal mit dem Level ERROR protokolliert werden.");
	}


	// ##### Fehlende interne Ressourcen: Template-Engine und Template #####

	@Test
	void testEineFehlendeTemplateEngineIstEinServerfehler() {
		final ReportRendererHtml renderer = new ReportRendererHtml(null, logger);
		final List<HtmlContext<?>> contexts = contexts();
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> renderer.renderHtml(TEMPLATE, contexts));
		pruefeMeldungFehlendesTemplate(aoe);
	}

	@Test
	void testEinFehlendesTemplateIstEinServerfehler() {
		final ReportRendererHtml renderer = new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine(), logger);
		final List<HtmlContext<?>> contexts = contexts();
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> renderer.renderHtml(null, contexts));
		pruefeMeldungFehlendesTemplate(aoe);
	}

	@Test
	void testEinLeeresTemplateIstEinServerfehler() {
		final ReportRendererHtml renderer = new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine(), logger);
		final List<HtmlContext<?>> contexts = contexts();
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> renderer.renderHtml("   ", contexts));
		pruefeMeldungFehlendesTemplate(aoe);
	}


	// ##### Durchreichen bereits klassifizierter Fehler und Erhalt der Ursache #####

	@Test
	void testEineApiOperationExceptionAusDemRendernBehaeltIhrenStatus() {
		// Die Daten der Vorlage werden während des Renderns nachgeladen. Ein dabei entstehender Fehler ist bereits klassifiziert und darf nicht zu einem
		// Serverfehler werden.
		final ApiOperationException ursprung = new ApiOperationException(Status.NOT_FOUND, "FEHLER: Der Stundenplan zur ID 4711 wurde nicht gefunden.");
		final TemplateEngine templateEngine = mock(TemplateEngine.class);
		when(templateEngine.process(anyString(), any(IContext.class))).thenThrow(ursprung);

		final ReportRendererHtml renderer = new ReportRendererHtml(templateEngine, logger);
		final List<HtmlContext<?>> contexts = contexts();
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> renderer.renderHtml(TEMPLATE, contexts));
		assertSame(ursprung, aoe, "Die ursprüngliche Exception muss unverändert durchgereicht werden.");
		assertEquals(Status.NOT_FOUND, aoe.getStatus());
	}

	@Test
	void testEinUnerwarteterFehlerNenntSeineUrsache() {
		// Eine zur Laufzeit scheiternde Expression: Thymeleaf meldet sie als TemplateProcessingException, die als Ursache erhalten bleiben muss.
		final ReportRendererHtml renderer = new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine(), logger);
		final List<HtmlContext<?>> contexts = contexts();
		final String template = "<html><body><span th:text=\"${1/0}\">Platzhalter</span></body></html>";
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> renderer.renderHtml(template, contexts));
		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertNotNull(aoe.getCause(), "Der unerwartete Fehler muss als Ursache erhalten bleiben.");
	}


	// ##### Gegenproben: der reguläre Weg #####

	@Test
	void testEinTemplateWirdMitDenContextDatenGerendert() {
		final ReportRendererHtml renderer = new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine(), logger);
		final String html = renderer.renderHtml(TEMPLATE, contexts());
		assertTrue(html.contains("Testinhalt"), "Das gerenderte HTML muss die Daten des Contexts enthalten.");
		assertTrue(fehlermeldungenImLog().isEmpty(), "Ein erfolgreicher Lauf darf keine Fehler protokollieren.");
	}

	@Test
	void testOhneContextvariablenEntstehtEinLeeresErgebnisUndKeinFehler() {
		final ReportRendererHtml renderer = new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine(), logger);
		assertEquals("", renderer.renderHtml(TEMPLATE, List.of()));
		assertTrue(fehlermeldungenImLog().isEmpty(), "Fehlende Daten sind kein Fehlerfall.");
	}

	// ##### Statustragende Ursachen aus den Datenzugriffen der Vorlage #####

	/** Die Meldung des klassifizierten Fehlers, der beim Datenzugriff der Vorlage geworfen wird. */
	private static final String MELDUNG_KLASSIFIZIERT = "FEHLER: Die Anmeldung am Signierdienst ist fehlgeschlagen.";

	/**
	 * Ein Wert, dessen Zugriff mit einer statustragenden Exception scheitert - wie ein Datenzugriff der Vorlage, etwa der Abbruch des Signier-Batches.
	 */
	public static final class WerfenderWert {
		/**
		 * Wirft beim Zugriff einen klassifizierten Client-Fehler.
		 *
		 * @return Kehrt nie zurück.
		 */
		public String wert() {
			throw new ApiOperationException(Status.BAD_REQUEST, MELDUNG_KLASSIFIZIERT);
		}
	}

	/**
	 * Ein minimaler Context für beliebige Objekte, damit die Vorlage auf deren Methoden zugreifen kann.
	 */
	private static final class ObjektContext extends HtmlContext<Object> {
		/**
		 * Erzeugt den Context mit den übergebenen Daten.
		 *
		 * @param variablenname Der Name der Thymeleaf-Variablen.
		 * @param daten         Die Daten des Contexts.
		 */
		private ObjektContext(final String variablenname, final List<Object> daten) {
			super(null);
			erzeugeContext(variablenname, daten);
		}
	}

	@Test
	void testEineStatustragendeUrsacheAusDemDatenzugriffBehaeltStatusUndMeldung() {
		// Thymeleaf wickelt den Wurf eines Getters in eigene Exceptions; die Statuszuordnung entpackt die Ursachenkette. Ohne das käme etwa der
		// klassifizierte Anmeldefehler des Signierdienstes als undifferenzierter Serverfehler am API-Rand an.
		final ReportRendererHtml renderer = new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine(), logger);
		final List<HtmlContext<?>> contexts = List.of(new ObjektContext("Werfer", List.of(new WerfenderWert())));
		final String template = "<html><body><span th:text=\"${Werfer[0].wert()}\">x</span></body></html>";

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> renderer.renderHtml(template, contexts));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals(MELDUNG_KLASSIFIZIERT, aoe.getBody(), "Die Meldung des klassifizierten Fehlers bleibt erhalten.");
	}

}
