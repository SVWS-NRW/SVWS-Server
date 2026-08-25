package de.svws_nrw.module.reporting.builders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
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
	private static final String MELDUNG_KEIN_TEMPLATE = "### FEHLER: Für die HTML-Erzeugung fehlt die Vorlage oder ihre Verarbeitung.";


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

	/** Ein Context, der einen Reporting-Context mitführt, aber keine fachliche Variable - die Lage, in der die Meldefassade allein im Context läge. */
	private static final class ContextOhneVariablen extends HtmlContext<String> {

		/**
		 * Erzeugt den Context mit dem übergebenen Reporting-Context und ohne Variablen.
		 *
		 * @param reportingContext Der Reporting-Context, dessen Meldefassade der zusammengeführte Context erhält.
		 */
		private ContextOhneVariablen(final ReportingContext reportingContext) {
			super(reportingContext);
		}
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
	 * Prüft, dass die übergebene Exception das fehlende Template als Serverfehler meldet und die Ursache benennt.
	 * <p>Neben dem Status wird die Meldung geprüft, weil sie zur Kopfzeile der Fehlerantwort wird: Steht dort die unspezifische Angabe, das HTML habe nicht
	 * gerendert werden können, erfährt der Leser den Grund des Abbruchs nicht mehr.</p>
	 * <p>Das Log bleibt außen vor: Ein Abbruch hat eine Meldungsquelle - die Meldung der Exception -, und protokolliert wird an der Abschlussgrenze.</p>
	 *
	 * @param aoe Die geworfene Exception.
	 */
	private void pruefeMeldungFehlendesTemplate(final ApiOperationException aoe) {
		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertEquals(MELDUNG_KEIN_TEMPLATE, aoe.getBody(), "Die eigene Meldung darf nicht von der allgemeinen Fehlerbehandlung überschrieben werden.");
	}


	// ##### Fehlende interne Ressourcen: Template-Engine und Template #####

	@Test
	void testEineFehlendeTemplateEngineIstEinServerfehler() {
		final ReportRendererHtml renderer = new ReportRendererHtml(null);
		final List<HtmlContext<?>> contexts = contexts();
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> renderer.renderHtml(TEMPLATE, contexts));
		pruefeMeldungFehlendesTemplate(aoe);
	}

	@Test
	void testEinFehlendesTemplateIstEinServerfehler() {
		final ReportRendererHtml renderer = new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine());
		final List<HtmlContext<?>> contexts = contexts();
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> renderer.renderHtml(null, contexts));
		pruefeMeldungFehlendesTemplate(aoe);
	}

	@Test
	void testEinLeeresTemplateIstEinServerfehler() {
		final ReportRendererHtml renderer = new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine());
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

		final ReportRendererHtml renderer = new ReportRendererHtml(templateEngine);
		final List<HtmlContext<?>> contexts = contexts();
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> renderer.renderHtml(TEMPLATE, contexts));
		assertSame(ursprung, aoe, "Die ursprüngliche Exception muss unverändert durchgereicht werden.");
		assertEquals(Status.NOT_FOUND, aoe.getStatus());
	}

	@Test
	void testEinUnerwarteterFehlerNenntSeineUrsache() {
		// Eine zur Laufzeit scheiternde Expression: Thymeleaf meldet sie als TemplateProcessingException, die als Ursache erhalten bleiben muss.
		final ReportRendererHtml renderer = new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine());
		final List<HtmlContext<?>> contexts = contexts();
		final String template = "<html><body><span th:text=\"${1/0}\">Platzhalter</span></body></html>";
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> renderer.renderHtml(template, contexts));
		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertNotNull(aoe.getCause(), "Der unerwartete Fehler muss als Ursache erhalten bleiben.");
	}


	// ##### Gegenproben: der reguläre Weg #####

	@Test
	void testEinTemplateWirdMitDenContextDatenGerendert() {
		final ReportRendererHtml renderer = new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine());
		final String html = renderer.renderHtml(TEMPLATE, contexts());
		assertTrue(html.contains("Testinhalt"), "Das gerenderte HTML muss die Daten des Contexts enthalten.");
	}

	@Test
	void testOhneContextvariablenEntstehtEinLeeresErgebnisUndKeinFehler() {
		final ReportRendererHtml renderer = new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine());
		assertEquals("", renderer.renderHtml(TEMPLATE, List.of()), "Fehlende Daten sind kein Fehlerfall, sondern ergeben ein leeres Ergebnis.");
	}

	@Test
	void testDieMeldefassadeAlleinZaehltNichtAlsInhalt() {
		// mergeHtmlContexts legt die Meldefassade in den Context, sobald ein Daten-Context einen Reporting-Context führt. Sie ist eine interne Variable:
		// Ohne fachliche Variablen bleibt das Ergebnis leer, und die Engine läuft nicht an.
		final TemplateEngine templateEngine = mock(TemplateEngine.class);
		final ReportRendererHtml renderer = new ReportRendererHtml(templateEngine);
		final List<HtmlContext<?>> contexts = List.of(new ContextOhneVariablen(mock(ReportingContext.class)));

		assertEquals("", renderer.renderHtml(TEMPLATE, contexts), "Die Meldefassade allein ist kein Inhalt.");
		verify(templateEngine, never()).process(anyString(), any(IContext.class));
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
		final ReportRendererHtml renderer = new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine());
		final List<HtmlContext<?>> contexts = List.of(new ObjektContext("Werfer", List.of(new WerfenderWert())));
		final String template = "<html><body><span th:text=\"${Werfer[0].wert()}\">x</span></body></html>";

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> renderer.renderHtml(template, contexts));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals(MELDUNG_KLASSIFIZIERT, aoe.getBody(), "Die Meldung des klassifizierten Fehlers bleibt erhalten.");
	}

}
