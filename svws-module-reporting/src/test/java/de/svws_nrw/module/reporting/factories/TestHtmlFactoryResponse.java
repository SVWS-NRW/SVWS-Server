package de.svws_nrw.module.reporting.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.builders.ReportBuilderHtml;
import de.svws_nrw.module.reporting.diagnose.ReportingAuswahlergebnis;
import de.svws_nrw.module.reporting.diagnose.ReportingHinweisSerializer;
import de.svws_nrw.module.reporting.diagnose.ReportingProblem;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryLehrer;
import de.svws_nrw.module.reporting.repositories.ReportingRepositorySchule;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryStundenplan;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schule.ProxyReportingBenutzer;
import de.svws_nrw.module.reporting.types.schule.ReportingSchule;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Vertragstests der HTML-Antwort am produktiven Weg durch die {@link HtmlFactory}: Der Hinweis-Header ist eigens getestet, aber nur hier ist belegt, dass
 * die HTML-Ausgabe ihn auch setzt und dabei Body, Content-Type und Cache-Control unverändert lässt.
 * <p>Der Datenaufbau ist eine Sichtweise der Stundenplanung - einer der Pfade, deren Anbindung an den Hinweisvertrag mit der Auswahlpipeline hinzukam. Der
 * Stundenplan und die Auswahl kommen aus gemockten Repositories; das Rendern übernimmt ein Konstruktions-Mock des HTML-Builders, denn für die Antwort-Form
 * ist der gerenderte Inhalt ohne Belang.</p>
 */
class TestHtmlFactoryResponse {

	/** Die ID, unter der der Stundenplan angefordert wird. */
	private static final long ID_STUNDENPLAN = 42L;

	/** Das HTML, das der gemockte Builder liefert. */
	private static final String HTML = "<html>Stundenplan</html>";

	/** Der gemockte Context, den die Factory erhält. */
	private ReportingContext reportingContext;


	@BeforeEach
	void setUp() throws ApiOperationException {
		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(new Logger());
		when(reportingContext.ausgabeprobleme()).thenReturn(List.of());

		final ReportingParameterTypisiert reportingParameter = mock(ReportingParameterTypisiert.class);
		when(reportingParameter.reportVorlage()).thenReturn(ReportingReportvorlage.STUNDENPLANUNG_V_LEHRER_STUNDENPLAN);
		when(reportingParameter.idHauptdatenObjekt()).thenReturn(ID_STUNDENPLAN);
		when(reportingParameter.idsHauptdaten()).thenReturn(List.of(1L));
		when(reportingParameter.einzelausgabeDaten()).thenReturn(false);
		when(reportingContext.reportingParameter()).thenReturn(reportingParameter);

		final ProxyReportingBenutzer benutzer = mock(ProxyReportingBenutzer.class);
		when(benutzer.pruefeKompetenz(any())).thenReturn(true);
		when(reportingContext.benutzer()).thenReturn(benutzer);

		final ReportingRepositorySchule repositorySchule = mock(ReportingRepositorySchule.class);
		when(repositorySchule.schule()).thenReturn(mock(ReportingSchule.class));
		when(reportingContext.repositorySchule()).thenReturn(repositorySchule);

		final ReportingRepositoryStundenplan repositoryStundenplan = mock(ReportingRepositoryStundenplan.class);
		when(repositoryStundenplan.stundenplan(ID_STUNDENPLAN))
				.thenReturn(new ReportingStundenplanungStundenplan("", null, null, ID_STUNDENPLAN, null, null, null, 1, null, null));
		when(reportingContext.repositoryStundenplan()).thenReturn(repositoryStundenplan);

		final ReportingRepositoryLehrer repositoryLehrer = mock(ReportingRepositoryLehrer.class);
		when(repositoryLehrer.waehleAus(List.of(1L)))
				.thenReturn(ReportingAuswahlergebnis.aus(List.of(1L), Map.of(1L, mock(ReportingLehrer.class)), Map.of(), List.of()));
		when(repositoryLehrer.lehrer(anyList())).thenReturn(List.of());
		when(reportingContext.repositoryLehrer()).thenReturn(repositoryLehrer);
	}


	/**
	 * Erzeugt die HTML-Antwort auf dem produktiven Weg; allein das Rendern übernimmt der Konstruktions-Mock des Builders.
	 *
	 * @return Die HTML-Antwort.
	 *
	 * @throws ApiOperationException Falls der Aufbau der Factory oder der Antwort scheitert.
	 */
	private Response htmlResponse() throws ApiOperationException {
		final HtmlFactory factory = HtmlFactory.erzeuge(reportingContext);
		try (MockedConstruction<ReportBuilderHtml> builder = mockConstruction(ReportBuilderHtml.class,
				(builderMock, ctx) -> when(builderMock.generate()).thenReturn(HTML))) {
			return factory.createHtmlResponse();
		}
	}


	@Test
	void testDieHtmlAusgabeEinesAngebundenenStundenplanPfadsTraegtDenHinweisHeader() throws ApiOperationException {
		assertEquals("v=0, gesamt=0, leer=?0", htmlResponse().getHeaderString(ReportingHinweisSerializer.HEADER_NAME),
				"Die Sichtweisen der Stundenplanung sind angebunden; ihre HTML-Antwort muss den Header tragen.");
	}

	@Test
	void testDieGemeldetenProblemeErreichenDenHeaderDerHtmlAusgabe() throws ApiOperationException {
		when(reportingContext.ausgabeprobleme()).thenReturn(List.of(new ReportingProblem(ReportingProblemursache.NICHT_VORHANDEN,
				ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN, ReportingProblemSchluessel.fuer(ReportingLehrer.class, 2L))));

		assertEquals("v=0, gesamt=1, leer=?0, datensaetze=1", htmlResponse().getHeaderString(ReportingHinweisSerializer.HEADER_NAME));
	}

	@Test
	void testDieHtmlAntwortBehaeltBodyContentTypeUndCacheControl() throws ApiOperationException {
		// Der Header tritt neben die bestehende Antwort. Body, Content-Type samt Zeichensatz und das Cache-Verbot sind Bestand und bleiben unverändert.
		final Response response = htmlResponse();

		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(HTML, response.getEntity());
		assertTrue(response.getMediaType().isCompatible(jakarta.ws.rs.core.MediaType.TEXT_HTML_TYPE), "Content-Type: " + response.getMediaType());
		assertEquals("UTF-8", response.getMediaType().getParameters().get("charset"));
		assertEquals("no-store", response.getHeaderString("Cache-Control"));
	}

}
