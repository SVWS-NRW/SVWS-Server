package de.svws_nrw.module.reporting.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositorySchule;
import de.svws_nrw.module.reporting.types.schule.ProxyReportingBenutzer;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft, wie die HTML-Factory die Schulform der Schule gegen die Schulformen der Reportvorlage hält. Gemessen wird an der Factory, weil ihr Konstruktor der
 * einzige Engpass aller Ausgabeformate ist.
 * <p>Die Auswahl der Vorlagen über die Eigenschaft "nennt Schulformen" statt über den Namen hält die Tests unabhängig davon, welche Schulformen eine
 * einzelne Vorlage nennt. Der anschließende Aufbau der Daten-Contexts scheitert an den nicht gestellten Daten; geprüft wird allein die Prüfung davor.</p>
 */
class TestHtmlFactorySchulform {

	/** Das Schuljahr, in dem der Report ausgegeben wird. Es bestimmt, unter welchen Namen der Katalog die Schulformen führt. */
	private static final int SCHULJAHR = 2026;

	/** Der gemockte Context, den die Factory erhält. */
	private ReportingContext reportingContext;

	/** Das gemockte Schul-Repository; die Tests stellen darüber die Schulform der Schule ein. */
	private ReportingRepositorySchule repositorySchule;

	/** Die gemockten Reporting-Parameter; die Tests stellen darüber die Reportvorlage ein. */
	private ReportingParameterTypisiert reportingParameter;

	/** Die Liste, die die Einträge des Loggers sammelt. */
	private LogConsumerList log;


	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}


	@BeforeEach
	void setUp() {
		final Logger logger = new Logger();
		log = new LogConsumerList();
		logger.addConsumer(log);

		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(logger);

		reportingParameter = mock(ReportingParameterTypisiert.class);
		when(reportingParameter.reportVorlage()).thenReturn(vorlage(true));
		when(reportingContext.reportingParameter()).thenReturn(reportingParameter);

		final ProxyReportingBenutzer benutzer = mock(ProxyReportingBenutzer.class);
		when(benutzer.pruefeKompetenz(any())).thenReturn(true);
		when(reportingContext.benutzer()).thenReturn(benutzer);

		final ReportingSchuljahresabschnitt abschnitt = mock(ReportingSchuljahresabschnitt.class);
		when(abschnitt.schuljahr()).thenReturn(SCHULJAHR);

		repositorySchule = mock(ReportingRepositorySchule.class);
		when(repositorySchule.auswahlSchuljahresabschnitt()).thenReturn(abschnitt);
		when(reportingContext.repositorySchule()).thenReturn(repositorySchule);
	}


	/**
	 * Liefert eine Reportvorlage mit oder ohne genannte Schulformen.
	 *
	 * @param mitSchulformen true für eine Vorlage mit genannten Schulformen, false für eine ohne.
	 *
	 * @return Die erste passende Reportvorlage.
	 */
	private static ReportingReportvorlage vorlage(final boolean mitSchulformen) {
		for (final ReportingReportvorlage reportvorlage : ReportingReportvorlage.values()) {
			if (reportvorlage.getSchulformen().isEmpty() != mitSchulformen) {
				return reportvorlage;
			}
		}
		throw new IllegalStateException("Keine Reportvorlage " + (mitSchulformen ? "mit" : "ohne") + " genannte Schulformen vorhanden");
	}

	/**
	 * Liefert eine Schulform, die die übergebene Reportvorlage nicht nennt.
	 *
	 * @param reportvorlage Die Reportvorlage.
	 *
	 * @return Die erste nicht genannte Schulform.
	 */
	private static Schulform nichtGenannteSchulform(final ReportingReportvorlage reportvorlage) {
		for (final Schulform schulform : Schulform.values()) {
			if (!reportvorlage.getSchulformen().contains(schulform)) {
				return schulform;
			}
		}
		throw new IllegalStateException("Die Reportvorlage " + reportvorlage.name() + " nennt jede Schulform");
	}

	/**
	 * Gibt die Texte der Log-Einträge mit dem Level ERROR zurück.
	 *
	 * @return Die Texte der Einträge, ohne die Einrückung des Loggers.
	 */
	private List<String> fehlermeldungenImLog() {
		return log.getLogData().stream().filter(eintrag -> eintrag.getLevel() == LogLevel.ERROR).map(eintrag -> eintrag.getText().strip()).toList();
	}


	@Test
	void testEineVorlageOhneSchulformenFragtDieSchulformNichtAb() {
		// Die vorgezogene Abfrage hält eine Schule mit fehlerhaftem Schulform-Eintrag für alle Vorlagen ohne Schulformen ausgabefähig.
		when(reportingParameter.reportVorlage()).thenReturn(vorlage(false));

		assertThrows(Exception.class, () -> HtmlFactory.erzeuge(reportingContext));

		verify(repositorySchule, never()).schulform();
	}

	@Test
	void testEineGenannteSchulformKommtDurchDiePruefung() {
		when(repositorySchule.schulform()).thenReturn(vorlage(true).getSchulformen().getFirst());

		assertThrows(Exception.class, () -> HtmlFactory.erzeuge(reportingContext));

		// Die Basisdaten holen sich als erster Schritt nach dem Konstruktor die Schule; der Zugriff belegt, dass die Vorlage die Prüfung passiert hat.
		verify(repositorySchule).schule();
	}

	@Test
	void testEineNichtGenannteSchulformIstEinParameterfehler() {
		final ReportingReportvorlage reportvorlage = vorlage(true);
		when(repositorySchule.schulform()).thenReturn(nichtGenannteSchulform(reportvorlage));

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> HtmlFactory.erzeuge(reportingContext));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus(),
				"Die Schulform gehört der Schule und nicht dem Benutzer; ein FORBIDDEN schickte ihn eine Berechtigung suchen, die es nicht gibt.");
		assertTrue(aoe.getMessage().contains(reportvorlage.getBezeichnung()),
				"Die Meldung nennt die angefragte Vorlage: " + aoe.getMessage());
	}

	@Test
	void testDieMeldungNenntDieZulaessigenSchulformenBeimNamen() {
		// Die Aufzählung entsteht erst zur Laufzeit aus dem Katalog; der Test deckt auf, wenn sie leer beim Anwender ankommt.
		final ReportingReportvorlage reportvorlage = vorlage(true);
		when(repositorySchule.schulform()).thenReturn(nichtGenannteSchulform(reportvorlage));

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> HtmlFactory.erzeuge(reportingContext));

		for (final Schulform schulform : reportvorlage.getSchulformen()) {
			final SchulformKatalogEintrag eintrag = Schulform.data().getEintragBySchuljahrUndWert(SCHULJAHR, schulform);
			assertTrue(aoe.getMessage().contains(eintrag.text), "Die Meldung nennt " + eintrag.text + ": " + aoe.getMessage());
		}
	}

	@Test
	void testEineNichtErmittelbareSchulformIstEinServerfehler() {
		// Gegenprobe zum Parameterfehler: Nicht die Anfrage ist falsch, sondern die Schule trägt keine auflösbare Schulform.
		when(repositorySchule.schulform()).thenReturn(null);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> HtmlFactory.erzeuge(reportingContext));

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		// Meldung und Zugriffsprüfung trennen diesen Serverfehler von einem späteren Schritt, der ebenfalls mit 500 endete.
		assertTrue(aoe.getMessage().contains("Schulform"), "Die Meldung benennt die nicht ermittelbare Schulform: " + aoe.getMessage());
		verify(repositorySchule, never()).schule();
	}

	@Test
	void testNebenDemWurfStehtKeineFehlermeldungImLog() {
		when(repositorySchule.schulform()).thenReturn(nichtGenannteSchulform(vorlage(true)));

		assertThrows(ApiOperationException.class, () -> HtmlFactory.erzeuge(reportingContext));

		assertEquals(List.of(), fehlermeldungenImLog(), "Den einen ERROR-Eintrag schreibt die Abschlussgrenze: " + fehlermeldungenImLog());
	}

}
