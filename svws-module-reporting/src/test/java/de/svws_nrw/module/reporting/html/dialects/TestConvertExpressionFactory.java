package de.svws_nrw.module.reporting.html.dialects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.thymeleaf.context.IExpressionContext;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.module.reporting.builders.ReportBuilderUtils;
import de.svws_nrw.module.reporting.builders.ReportRendererHtml;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.utils.ReportingBarcodeUtils;

/**
 * Prüft, dass der Logger des laufenden Reports vom Renderer bis in die Hilfsmethoden des {@code #convert}-Dialekts durchgereicht wird.
 * <p>Die Dialekte werden einmalig an der geteilten TemplateEngine registriert und können deshalb keinen Logger als Feld halten. Stattdessen reist er als
 * Context-Variable mit. Ohne diesen Weg schriebe der Dialekt in den globalen Log, und ein Hinweis auf eine Lücke wäre keinem Reporting-Aufruf mehr
 * zuzuordnen — entgegen der Konvention, nach der ausschließlich über den Logger des Reporting-Contexts protokolliert wird.</p>
 * <p>Weil {@code isCacheable} des Dialekts {@code true} liefert, wird eigens geprüft, dass zwei nacheinander gerenderte Reports jeweils ihren eigenen
 * Logger erhalten: Ein über Reports hinweg zwischengespeichertes Expression-Objekt würde die Einträge dem falschen Aufruf zuordnen.</p>
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

		/** Erzeugt den Context mit einem einzelnen Datensatz. */
		private TestHtmlContext() {
			super(null);
			erzeugeContext("Daten", List.of("Testinhalt"));
		}
	}


	/**
	 * Erzeugt einen Logger samt Sammelliste.
	 *
	 * @param log Die Liste, die an den Logger gehängt wird.
	 *
	 * @return Der Logger.
	 */
	private static Logger loggerMit(final LogConsumerList log) {
		final Logger logger = new Logger();
		logger.addConsumer(log);
		return logger;
	}

	/**
	 * Gibt die Texte aller WARNING-Einträge der übergebenen Sammelliste zurück.
	 *
	 * @param log Die Sammelliste.
	 *
	 * @return Die Texte der WARNING-Einträge.
	 */
	private static List<String> warnungen(final LogConsumerList log) {
		return log.getLogData().stream().filter(eintrag -> eintrag.getLevel() == LogLevel.WARNING).map(eintrag -> eintrag.getText().strip()).toList();
	}

	/**
	 * Erzeugt einen Expression-Context, der die angegebene Logger-Variable führt.
	 *
	 * @param logger Der Logger oder {@code null}, wenn der Context keinen führt.
	 *
	 * @return Der gemockte Expression-Context.
	 */
	private static IExpressionContext expressionContextMit(final Logger logger) {
		final IExpressionContext context = mock(IExpressionContext.class);
		when(context.getVariable(anyString())).thenReturn(null);
		when(context.getVariable(ReportBuilderUtils.VARIABLE_LOGGER)).thenReturn(logger);
		return context;
	}


	@Test
	void testDerLoggerAusDemContextErreichtDenHelper() {
		final LogConsumerList log = new LogConsumerList();
		final Object helper = new ConvertExpressionFactory().buildObject(expressionContextMit(loggerMit(log)), "convert");

		assertNotNull(helper, "Der Dialekt muss ein Expression-Objekt liefern.");
		((ConvertExpressionHelper) helper).to2DCodeQRCodeAsSvgHtmlImageSource(NICHT_KODIERBAR, 50.0, 50.0);

		assertEquals(1, warnungen(log).size(), "Die Warnung muss im Log des Reports stehen, nicht im globalen: " + warnungen(log));
	}

	@Test
	void testOhneLoggerImContextBleibtDerDialektArbeitsfaehig() {
		final Object helper = new ConvertExpressionFactory().buildObject(expressionContextMit(null), "convert");

		assertNotNull(helper);
		final String svg = ((ConvertExpressionHelper) helper).to2DCodeQRCodeAsSvgHtmlImageSource(NICHT_KODIERBAR, 50.0, 50.0);
		assertEquals(ReportingBarcodeUtils.leeresTransparentesSVG(50.0, 50.0), svg, "Die Lücke entsteht auch ohne Logger.");
	}

	@Test
	void testDerRendererReichtSeinenLoggerBisInDenDialekt() {
		final LogConsumerList log = new LogConsumerList();
		final String html = new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine(), loggerMit(log))
				.renderHtml(TEMPLATE, List.of(new TestHtmlContext()));

		// Der Report wird trotz des nicht darstellbaren Codes fertig gerendert, und die Begründung steht im Log genau dieses Reports.
		assertTrue(html.contains("Testinhalt"), "Die Ausgabe muss vollständig entstehen.");
		assertEquals(1, warnungen(log).size(), "Die Warnung des Dialekts muss im Log des Reports ankommen: " + warnungen(log));
	}

	@Test
	void testZweiReportsErhaltenJeweilsIhrenEigenenLogger() {
		// Die geteilte TemplateEngine und das zwischenspeicherbare Expression-Objekt dürfen den Logger nicht über Reports hinweg festhalten.
		final LogConsumerList logErster = new LogConsumerList();
		final LogConsumerList logZweiter = new LogConsumerList();

		new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine(), loggerMit(logErster)).renderHtml(TEMPLATE, List.of(new TestHtmlContext()));
		new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine(), loggerMit(logZweiter)).renderHtml(TEMPLATE, List.of(new TestHtmlContext()));

		assertEquals(1, warnungen(logErster).size(), "Der erste Report muss genau seine eigene Warnung erhalten: " + warnungen(logErster));
		assertEquals(1, warnungen(logZweiter).size(), "Der zweite Report muss genau seine eigene Warnung erhalten: " + warnungen(logZweiter));
	}

}
