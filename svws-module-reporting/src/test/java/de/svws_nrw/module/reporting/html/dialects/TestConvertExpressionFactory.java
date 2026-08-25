package de.svws_nrw.module.reporting.html.dialects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.thymeleaf.context.IExpressionContext;

import de.svws_nrw.module.reporting.builders.ReportBuilderUtils;
import de.svws_nrw.module.reporting.builders.ReportRendererHtml;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemmelder;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.utils.ReportingBarcodeUtils;

/**
 * Prüft, dass die Meldefassade des laufenden Reports vom Renderer bis in die Hilfsmethoden des {@code #convert}-Dialekts durchgereicht wird.
 * <p>Die Dialekte werden einmalig an der geteilten TemplateEngine registriert und können deshalb keine Meldefassade als Feld halten. Stattdessen reist
 * sie als Context-Variable mit — bewusst als schmaler {@code ReportingProblemmelder} und nicht als Reporting-Context, denn der wäre per OGNL für jede
 * Vorlage erreichbar und öffnete deren Zugriff auf die Repositories. Ohne diesen Weg meldete der Dialekt seine Befunde an niemanden, und eine Lücke in
 * der Ausgabe wäre keinem Reporting-Aufruf mehr zuzuordnen.</p>
 * <p>Weil {@code isCacheable} des Dialekts {@code true} liefert, wird eigens geprüft, dass zwei nacheinander gerenderte Reports jeweils an ihren eigenen
 * Aufruf melden: Ein über Reports hinweg zwischengespeichertes Expression-Objekt würde die Befunde dem falschen Aufruf zuordnen.</p>
 */
class TestConvertExpressionFactory {

	/** Ein Inhalt, den die Barcode-Bibliothek nicht kodieren kann. */
	private static final String NICHT_KODIERBAR = "学校证明";

	/** Ein Template, das den nicht darstellbaren Code einbindet und zusätzlich Daten ausgibt. */
	private static final String TEMPLATE =
			"<html><body><img th:src=\"${#convert.to2DCodeQRCodeAsSvgHtmlImageSource('" + NICHT_KODIERBAR + "', 50, 50)}\" alt=\"\"/>"
					+ "<span th:text=\"${Daten[0]}\">x</span></body></html>";


	/** Ein minimaler Context, der eine Vorlagenvariable bereitstellt, damit das Rendern nicht vorzeitig endet. */
	private static final class TestHtmlContext extends HtmlContext<String> {

		/**
		 * Erzeugt den Context mit einem einzelnen Datensatz.
		 *
		 * @param reportingContext Der Reporting-Context des Aufrufs oder {@code null}.
		 */
		private TestHtmlContext(final ReportingContext reportingContext) {
			super(reportingContext);
			erzeugeContext("Daten", List.of("Testinhalt"));
		}
	}


	/**
	 * Prüft, dass der Reporting-Context genau eine nicht darstellbare Lücke erhalten hat.
	 *
	 * @param reportingContext Der zu prüfende Reporting-Context.
	 */
	private static void hatEineLueckeGemeldet(final ReportingContext reportingContext) {
		verify(reportingContext).meldeAusgabeproblem(eq(ReportingProblemursache.NICHT_DARSTELLBAR),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), any(), anyString(), any(Exception.class));
	}

	/**
	 * Erzeugt einen Expression-Context, der die angegebene Melder-Variable führt.
	 *
	 * @param melder Die Meldefassade oder {@code null}, wenn der Context keine führt.
	 *
	 * @return Der gemockte Expression-Context.
	 */
	private static IExpressionContext expressionContextMit(final ReportingProblemmelder melder) {
		final IExpressionContext context = mock(IExpressionContext.class);
		when(context.getVariable(anyString())).thenReturn(null);
		when(context.getVariable(ReportBuilderUtils.VARIABLE_PROBLEMMELDER)).thenReturn(melder);
		return context;
	}


	@Test
	void testDerMelderAusDemContextErreichtDenHelper() {
		final ReportingProblemmelder melder = mock(ReportingProblemmelder.class);
		final Object helper = new ConvertExpressionFactory().buildObject(expressionContextMit(melder), "convert");

		assertNotNull(helper, "Der Dialekt muss ein Expression-Objekt liefern.");
		((ConvertExpressionHelper) helper).to2DCodeQRCodeAsSvgHtmlImageSource(NICHT_KODIERBAR, 50.0, 50.0);

		verify(melder).melde(eq(ReportingProblemursache.NICHT_DARSTELLBAR),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), any(), anyString(), any(Exception.class));
	}

	@Test
	void testOhneMelderBleibtDerDialektArbeitsfaehig() {
		final Object helper = new ConvertExpressionFactory().buildObject(expressionContextMit(null), "convert");

		assertNotNull(helper);
		final String svg = ((ConvertExpressionHelper) helper).to2DCodeQRCodeAsSvgHtmlImageSource(NICHT_KODIERBAR, 50.0, 50.0);
		assertEquals(ReportingBarcodeUtils.leeresTransparentesSVG(50.0, 50.0), svg, "Die Lücke entsteht auch ohne Meldefassade.");
	}

	@Test
	void testDerRendererReichtDenReportingContextBisInDenDialekt() {
		final ReportingContext reportingContext = mock(ReportingContext.class);
		final String html = new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine())
				.renderHtml(TEMPLATE, List.of(new TestHtmlContext(reportingContext)));

		// Der Report wird trotz des nicht darstellbaren Codes fertig gerendert, und der Befund erreicht genau diesen Aufruf.
		assertTrue(html.contains("Testinhalt"), "Die Ausgabe muss vollständig entstehen.");
		hatEineLueckeGemeldet(reportingContext);
	}

	@Test
	void testOhneReportingContextInDenDatenRendertDerReportTrotzdem() {
		final String html = new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine())
				.renderHtml(TEMPLATE, List.of(new TestHtmlContext(null)));

		assertTrue(html.contains("Testinhalt"), "Ein Datenaufbau ohne Reporting-Context darf die Ausgabe nicht verhindern.");
	}

	@Test
	void testZweiReportsErhaltenJeweilsIhrenEigenenReportingContext() {
		// Die geteilte TemplateEngine und das zwischenspeicherbare Expression-Objekt dürfen den Reporting-Context nicht über Reports hinweg festhalten.
		final ReportingContext ersterReport = mock(ReportingContext.class);
		final ReportingContext zweiterReport = mock(ReportingContext.class);

		new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine()).renderHtml(TEMPLATE, List.of(new TestHtmlContext(ersterReport)));
		new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine()).renderHtml(TEMPLATE, List.of(new TestHtmlContext(zweiterReport)));

		// Jeder Aufruf erhält genau eine Meldung: Ein festgehaltener Context brächte dem ersten Report zwei und dem zweiten keine.
		hatEineLueckeGemeldet(ersterReport);
		hatEineLueckeGemeldet(zweiterReport);
	}

}
