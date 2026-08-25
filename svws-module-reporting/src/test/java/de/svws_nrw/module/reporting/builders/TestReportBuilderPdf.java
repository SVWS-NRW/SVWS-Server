package de.svws_nrw.module.reporting.builders;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Fehlerweitergabe des {@link ReportBuilderPdf}.
 * <p>Der Builder darf den Status einer {@link ApiOperationException} des Renderers nicht verschärfen: Dort ist die Fehlerquelle bekannt und der Status
 * nach ihr festgelegt, der Builder kennt sie nicht besser. Verpackt er sie erneut, wird jeder bereits richtig klassifizierte Status auf dem Weg zum
 * API-Rand zu einem Serverfehler. Geprüft wird das an einem gemockten Renderer.</p>
 */
class TestReportBuilderPdf {

	/** Der Root-Pfad, den der Builder-Kontext in den Tests erhält. */
	private static final String ROOT_PFAD = ReportingReportvorlage.getRootPfad();

	/** Der HTML-Inhalt, aus dem der Builder die PDF-Datei erzeugt. */
	private static final String HTML = "<html><body><p>Testinhalt</p></body></html>";


	/**
	 * Erzeugt einen PDF-Builder, der den übergebenen Renderer verwendet.
	 *
	 * @param renderer Der Renderer, der die PDF-Datei erzeugen soll.
	 *
	 * @return Der PDF-Builder.
	 */
	private ReportBuilderPdf builder(final ReportRendererPdf renderer) {
		return new ReportBuilderPdf(new ReportBuilderContextPdf()
				.withHtmlInput(HTML)
				.withDateiname("Bescheinigung_Meier")
				.withRenderer(renderer)
				.withStatischerDateiname("Bescheinigungen")
				.withRootPfad(ROOT_PFAD));
	}

	// ##### Weitergabe des Status aus dem Renderer #####

	@Test
	void testDerStatusEinerFachlichenRessourceBleibtErhalten() {
		final ApiOperationException ursprung = new ApiOperationException(Status.NOT_FOUND, "FEHLER: Der Stundenplan zur ID 4711 wurde nicht gefunden.");
		final ReportRendererPdf renderer = mock(ReportRendererPdf.class);
		doThrow(ursprung).when(renderer).renderPdf(anyString(), anyString(), any(OutputStream.class));

		final ReportBuilderPdf reportBuilder = builder(renderer);
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, reportBuilder::generate);
		assertSame(ursprung, aoe, "Die ursprüngliche Exception muss unverändert durchgereicht werden.");
		assertEquals(Status.NOT_FOUND, aoe.getStatus());
	}


	// ##### Gegenprobe: der reguläre Weg #####

	@Test
	void testDasErgebnisDesRenderersWirdDurchgereicht() {
		final byte[] erwartet = "PDF-Inhalt".getBytes(StandardCharsets.UTF_8);
		final ReportRendererPdf renderer = mock(ReportRendererPdf.class);
		doAnswer(aufruf -> {
			((OutputStream) aufruf.getArgument(2)).write(erwartet);
			return null;
		}).when(renderer).renderPdf(anyString(), anyString(), any(OutputStream.class));

		assertArrayEquals(erwartet, builder(renderer).generate());
	}

}
