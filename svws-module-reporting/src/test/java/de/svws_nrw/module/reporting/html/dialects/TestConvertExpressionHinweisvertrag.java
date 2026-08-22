package de.svws_nrw.module.reporting.html.dialects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.module.reporting.builders.ReportBuilderUtils;
import de.svws_nrw.module.reporting.builders.ReportRendererHtml;
import de.svws_nrw.module.reporting.diagnose.ReportingAusgabeumfang;
import de.svws_nrw.module.reporting.diagnose.ReportingHinweisSerializer;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSammler;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 * Prüft die Zusage des Hinweisvertrags über die ganze Kette: Ein Code, der beim Rendern einer Vorlage scheitert, erreicht als gemeldetes
 * Ausgabeproblem den Sammler und erscheint im Headerwert als Hinweis der Kategorie {@code darstellung}.
 * <p>Die Einzelteile - Meldung des Dialekts, Deduplizierung des Sammlers, Projektion auf die Kategorie und Serialisierung - sind je für sich
 * getestet. Erst ihr Zusammenspiel ist jedoch der Vertrag: Diese Klasse rendert ein Template mit fehlschlagenden Codes gegen einen echten
 * Sammler und liest anschließend den Headerwert, den eine Dokumentantwort tragen würde.</p>
 */
class TestConvertExpressionHinweisvertrag {

	/** Ein Inhalt, den die Barcode-Bibliothek nicht kodieren kann. */
	private static final String NICHT_KODIERBAR = "学校证明";

	/** Ein Template, in dem ein QR-Code und ein Barcode scheitern - der QR-Code an den Zeichen, der Barcode an der Länge. */
	private static final String TEMPLATE_ZWEI_FEHLSCHLAEGE =
			"<html><body><img th:src=\"${#convert.to2DCodeQRCodeAsSvgHtmlImageSource('" + NICHT_KODIERBAR + "', 50, 50)}\" alt=\"\"/>"
					+ "<img th:src=\"${#convert.toBarcodeCode128AsSvgHtmlImageSource('" + "X".repeat(65) + "', 50, 30)}\" alt=\"\"/>"
					+ "<span th:text=\"${Daten[0]}\">x</span></body></html>";

	/** Der echte Sammler, an den die Meldungen des gemockten Reporting-Contexts weitergegeben werden. */
	private ReportingProblemSammler sammler;

	/** Der Reporting-Context des Aufrufs; nur seine Meldefassade ist mit dem Sammler verbunden. */
	private ReportingContext reportingContext;


	/** Ein minimaler Context, der eine Vorlagenvariable bereitstellt, damit das Rendern nicht vorzeitig endet. */
	private static final class TestHtmlContext extends HtmlContext<String> {

		/**
		 * Erzeugt den Context mit einem einzelnen Datensatz.
		 *
		 * @param reportingContext Der Reporting-Context des Aufrufs.
		 */
		private TestHtmlContext(final ReportingContext reportingContext) {
			super(reportingContext);
			erzeugeContext("Daten", List.of("Testinhalt"));
		}
	}


	@BeforeEach
	void setUp() {
		sammler = new ReportingProblemSammler(new Logger());
		reportingContext = mock(ReportingContext.class);
		doAnswer(aufruf -> {
			sammler.melde(aufruf.getArgument(0, ReportingProblemursache.class), aufruf.getArgument(1, ReportingProblemauswirkung.class),
					aufruf.getArgument(2, ReportingProblemSchluessel.class), aufruf.getArgument(3, String.class), aufruf.getArgument(4, Exception.class));
			return null;
		}).when(reportingContext).meldeAusgabeproblem(any(), any(), any(), any(), any());
	}

	/**
	 * Rendert das übergebene Template mit dem verbundenen Reporting-Context.
	 *
	 * @param template Das zu rendernde Template.
	 *
	 * @return Das gerenderte HTML.
	 */
	private String rendere(final String template) {
		return new ReportRendererHtml(ReportBuilderUtils.getHtmlTemplateEngine(), new Logger())
				.renderHtml(template, List.of(new TestHtmlContext(reportingContext)));
	}


	@Test
	void testZweiGescheiterteCodesErgebenImHeaderEinenDarstellungsHinweis() {
		final String html = rendere(TEMPLATE_ZWEI_FEHLSCHLAEGE);

		// Die Ausgabe entsteht trotz der beiden Fehlschläge vollständig.
		assertTrue(html.contains("Testinhalt"), "Die Ausgabe muss trotz der gescheiterten Codes vollständig entstehen.");

		// Beide Fehlschläge teilen sich den Schlüssel und zählen als ein Hinweis; er erscheint im Headerwert als Kategorie darstellung.
		final String headerwert = ReportingHinweisSerializer.headerwert(new ReportingAusgabeumfang(120, 117, false), sammler.probleme());
		assertEquals("v=1, angefordert=120, ausgegeben=117, hinweise=1, darstellung=1", headerwert,
				"Der Headerwert muss die beiden Fehlschläge als genau einen Hinweis der Kategorie darstellung führen.");
	}

	@Test
	void testOhneFehlschlagBleibtDerHeaderOhneHinweis() {
		rendere("<html><body><img th:src=\"${#convert.toBarcodeCode128AsSvgHtmlImageSource('4711', 50, 30)}\" alt=\"\"/>"
				+ "<span th:text=\"${Daten[0]}\">x</span></body></html>");

		final String headerwert = ReportingHinweisSerializer.headerwert(new ReportingAusgabeumfang(120, 120, false), sammler.probleme());
		assertEquals("v=1, angefordert=120, ausgegeben=120, hinweise=0", headerwert,
				"Ein erfolgreicher Code darf keinen Hinweis erzeugen, und Kategorien ohne Befund fehlen im Wert.");
	}

}
