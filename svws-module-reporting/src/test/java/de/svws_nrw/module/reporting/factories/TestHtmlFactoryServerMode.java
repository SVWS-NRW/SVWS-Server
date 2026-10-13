package de.svws_nrw.module.reporting.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositorySchule;
import de.svws_nrw.module.reporting.types.schule.ProxyReportingBenutzer;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft, wie die HTML-Factory den ServerMode des Servers gegen den erforderlichen ServerMode der Reportvorlage hält. Gemessen wird an der Factory, weil ihr
 * Konstruktor der einzige Engpass aller Ausgabeformate ist.
 * <p>Die Auswahl der Vorlagen über ihren erforderlichen ServerMode statt über den Namen hält die Tests unabhängig davon, welche Vorlage gerade welchen
 * Modus verlangt. Der anschließende Aufbau der Daten-Contexts scheitert an den nicht gestellten Daten; geprüft wird allein die Prüfung davor.</p>
 */
class TestHtmlFactoryServerMode {

	/** Der gemockte Context, den die Factory erhält. */
	private ReportingContext reportingContext;

	/** Das gemockte Schul-Repository; sein Zugriff belegt, dass eine Vorlage die Prüfung passiert hat. */
	private ReportingRepositorySchule repositorySchule;

	/** Die gemockten Reporting-Parameter; die Tests stellen darüber die Reportvorlage ein. */
	private ReportingParameterTypisiert reportingParameter;


	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(new Logger());

		reportingParameter = mock(ReportingParameterTypisiert.class);
		when(reportingContext.reportingParameter()).thenReturn(reportingParameter);

		final ProxyReportingBenutzer benutzer = mock(ProxyReportingBenutzer.class);
		when(benutzer.pruefeKompetenz(any())).thenReturn(true);
		when(reportingContext.benutzer()).thenReturn(benutzer);

		repositorySchule = mock(ReportingRepositorySchule.class);
		when(reportingContext.repositorySchule()).thenReturn(repositorySchule);
	}


	/**
	 * Liefert eine Reportvorlage, die STABLE oder einen höheren ServerMode verlangt.
	 *
	 * @param stable true für eine Vorlage, die STABLE verlangt, false für eine mit höherem ServerMode.
	 *
	 * @return Die erste passende Reportvorlage.
	 */
	private static ReportingReportvorlage vorlage(final boolean stable) {
		for (final ReportingReportvorlage reportvorlage : ReportingReportvorlage.values()) {
			if ((reportvorlage.getErforderlicherServerMode() == ServerMode.STABLE) == stable) {
				return reportvorlage;
			}
		}
		throw new IllegalStateException("Keine Reportvorlage " + (stable ? "für STABLE" : "mit höherem ServerMode") + " vorhanden");
	}

	/**
	 * Stellt die übergebene Vorlage ein und lässt eine von ihr genannte Schulform zu, damit die Schulformprüfung nicht vor dem Aufbau der Contexts abbricht.
	 *
	 * @param reportvorlage Die einzustellende Reportvorlage.
	 */
	private void stelleVorlageEin(final ReportingReportvorlage reportvorlage) {
		when(reportingParameter.reportVorlage()).thenReturn(reportvorlage);
		if (!reportvorlage.getSchulformen().isEmpty()) {
			when(repositorySchule.schulform()).thenReturn(reportvorlage.getSchulformen().getFirst());
		}
	}


	@Test
	void testEineVorlageFuerStableFragtDenServerModeNichtAb() {
		stelleVorlageEin(vorlage(true));

		assertThrows(Exception.class, () -> HtmlFactory.erzeuge(reportingContext));

		verify(reportingContext, never()).serverMode();
		verify(repositorySchule).schule();
	}

	@Test
	void testImErforderlichenServerModeKommtDieVorlageDurchDiePruefung() {
		final ReportingReportvorlage reportvorlage = vorlage(false);
		stelleVorlageEin(reportvorlage);
		when(reportingContext.serverMode()).thenReturn(reportvorlage.getErforderlicherServerMode());

		assertThrows(Exception.class, () -> HtmlFactory.erzeuge(reportingContext));

		// Die Basisdaten holen sich als erster Schritt nach dem Konstruktor die Schule; der Zugriff belegt, dass die Vorlage die Prüfung passiert hat.
		verify(repositorySchule).schule();
	}

	@Test
	void testEinUnzureichenderServerModeIstEinParameterfehler() {
		final ReportingReportvorlage reportvorlage = vorlage(false);
		stelleVorlageEin(reportvorlage);
		when(reportingContext.serverMode()).thenReturn(ServerMode.STABLE);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> HtmlFactory.erzeuge(reportingContext));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertTrue(aoe.getMessage().contains(reportvorlage.getBezeichnung()), "Die Meldung nennt die angefragte Vorlage: " + aoe.getMessage());
		assertTrue(aoe.getMessage().contains(reportvorlage.getErforderlicherServerMode().text),
				"Die Meldung nennt den erforderlichen ServerMode: " + aoe.getMessage());
		verify(repositorySchule, never()).schule();
	}

}
