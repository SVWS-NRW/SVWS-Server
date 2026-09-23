package de.svws_nrw.module.reporting.html.contexts.initializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryGost;
import de.svws_nrw.module.reporting.repositories.ReportingRepositorySchule;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Stufen-Auswahl im Initializer der GOSt-Klausurplanung. Die Stufen sind Nutzlast: Formfehler der übergebenen kombinierten IDs sind
 * Client-Fehler, ein nicht vorhandener Abiturjahrgang wird dagegen ausgelassen und gemeldet. Bleibt keine Stufe übrig, meldet der Initializer den
 * bewussten Leerfall an die Ausgabe.
 * <p>Der Context-Erzeuger ist ein Stub, der die ausgewählten Stufen festhält - der Aufbau des Klausurplans selbst ist nicht Gegenstand dieser Tests.</p>
 */
class TestHtmlContextInitializerGostKlausurplanung {

	/** Der gemockte Context, den der Initializer erhält. */
	private ReportingContext reportingContext;

	/** Die gemockten Reporting-Parameter des Requests. */
	private ReportingParameterTypisiert reportingParameter;

	/** Die Liste, die die Einträge des Loggers sammelt. */
	private LogConsumerList log;


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		final Logger logger = new Logger();
		log = new LogConsumerList();
		logger.addConsumer(log);
		when(reportingContext.logger()).thenReturn(logger);

		reportingParameter = mock(ReportingParameterTypisiert.class);
		when(reportingParameter.reportVorlage()).thenReturn(ReportingReportvorlage.GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN);
		when(reportingContext.reportingParameter()).thenReturn(reportingParameter);

		final ReportingRepositorySchule repositorySchule = mock(ReportingRepositorySchule.class, RETURNS_DEEP_STUBS);
		when(repositorySchule.istSchuleMitGost()).thenReturn(true);
		when(repositorySchule.auswahlSchuljahresabschnitt().schuljahr()).thenReturn(2025);
		when(repositorySchule.auswahlSchuljahresabschnitt().abschnitt()).thenReturn(1);
		when(reportingContext.repositorySchule()).thenReturn(repositorySchule);

		final ReportingRepositoryGost repositoryGost = mock(ReportingRepositoryGost.class);
		when(repositoryGost.abiturjahrgaenge()).thenReturn(List.of(2026, 2027, 2028));
		when(reportingContext.repositoryGost()).thenReturn(repositoryGost);
	}


	/**
	 * Erzeugt den Initializer mit einem Context-Erzeuger, der einen leeren Context liefert.
	 *
	 * @return Der Initializer für die Tests.
	 */
	private HtmlContextInitializerGostKlausurplanung initializer() {
		final HtmlContextAufbauGostKlausurplanung aufbau =
				new HtmlContextAufbauGostKlausurplanung(ctx -> mock(HtmlContextGostKlausurplanungKlausurplan.class));
		return new HtmlContextInitializerGostKlausurplanung(reportingContext, new HashMap<>(), aufbau);
	}

	/**
	 * Verdrahtet die übergebenen kombinierten IDs als Hauptdaten des Requests.
	 *
	 * @param ids Die kombinierten IDs aus Abiturjahr und GOSt-Halbjahr.
	 */
	private void gebeHauptdatenIdsVor(final List<Long> ids) {
		when(reportingParameter.idsHauptdaten()).thenReturn(ids);
	}





	@Test
	void testDieMeldungDesAusgabeumfangsLiegtImContextAufbau() {
		// Die Deklaration benennt die Meldestelle des Ausgabeumfangs; deren Fehlen meldet die Ausgabefactory zur Laufzeit als Serverfehler.
		assertTrue(initializer().meldetAusgabeumfangImContextAufbau());
	}

	/**
	 * Gibt die Texte aller Logeinträge mit dem Level ERROR zurück, ohne die Einrückung des Loggers.
	 *
	 * @return Die Texte der ERROR-Logeinträge in der Reihenfolge ihres Auftretens.
	 */
	private List<String> fehlermeldungenImLog() {
		return log.getLogData().stream().filter(eintrag -> eintrag.getLevel() == LogLevel.ERROR).map(eintrag -> eintrag.getText().strip()).toList();
	}

	@Test
	void testEineZuLangeKombinierteIdBleibtEinClientFehler() {
		// Form vor Existenz: 202503 zerfällt zu Jahrgang 20250 und Halbjahr 3. Ohne Obergrenze würde die kaputte ID still als unbekannter Jahrgang
		// ausgelassen.
		gebeHauptdatenIdsVor(List.of(202503L));

		final HtmlContextInitializerGostKlausurplanung initializer = initializer();

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, initializer::init);

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
	}

	@Test
	void testEineZuKurzeKombinierteIdBleibtEinClientFehler() {
		// Der Gegenfall: 2026 zerfällt zu Jahrgang 202 und Halbjahr 6 und scheitert an der Untergrenze.
		gebeHauptdatenIdsVor(List.of(2026L));

		final HtmlContextInitializerGostKlausurplanung initializer = initializer();

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, initializer::init);

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
	}

	@Test
	void testEinUngueltigesHalbjahrBleibtEinClientFehler() {
		gebeHauptdatenIdsVor(List.of(20266L));

		final HtmlContextInitializerGostKlausurplanung initializer = initializer();

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, initializer::init);

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("### FEHLER: Ein angegebenes GOSt-Halbjahr ist ungültig.", aoe.getBody());
	}

	@Test
	void testDieBeanstandeteStufeStehtImLog() {
		// Die Meldung nennt weder die kombinierte ID noch das daraus abgeleitete Jahr, und das Eingangsprotokoll zeigt nur einen Auszug der Rohwerte.
		// Ohne diese Zeile bliebe unauffindbar, welche der übergebenen Stufen die Ausgabe beendet hat.
		gebeHauptdatenIdsVor(List.of(20261L, 20272L, 202503L));

		final HtmlContextInitializerGostKlausurplanung initializer = initializer();

		assertThrows(ApiOperationException.class, initializer::init);

		assertEquals(List.of("Beanstandete Stufe: 202503"), fehlermeldungenImLog(),
				"Die Zeile nennt allein die beanstandete Stufe und wiederholt die Meldung nicht.");
	}

	@Test
	void testDieSchleifeBrichtBeiDerErstenBeanstandetenStufeAb() {
		// Zwei ungültige IDs ergeben genau einen Eintrag: Die Prüfung endet beim ersten Befund, und das Log spiegelt das.
		gebeHauptdatenIdsVor(List.of(20266L, 20269L));

		final HtmlContextInitializerGostKlausurplanung initializer = initializer();

		assertThrows(ApiOperationException.class, initializer::init);

		assertEquals(List.of("Beanstandete Stufe: 20266"), fehlermeldungenImLog());
	}

	@Test
	void testEineGelungeneStufenauswahlHinterlaesstKeinenFehlereintrag() throws ApiOperationException {
		gebeHauptdatenIdsVor(List.of(20261L, 20272L));

		initializer().init();

		assertEquals(List.of(), fehlermeldungenImLog());
	}




}
