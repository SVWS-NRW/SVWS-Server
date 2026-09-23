package de.svws_nrw.module.reporting.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurenAlleKlausurdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenHalbjahresdaten;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.filterung.ReportingFilterService;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.service.gost.klausuren.GostKlausurenAllDataService;
import de.svws_nrw.service.gost.klausuren.GostKlausurenServiceFactory;
import de.svws_nrw.service.gost.klausuren.GostKlausurenServiceFactoryBuilder;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft die Stufenauswahl des Klausurplanungs-Repositorys. Es nimmt die übergebenen Stufen oder leitet ohne Übergabe drei Stufen aus dem ausgewählten
 * Schuljahresabschnitt ab. Eine formal ungültige Angabe wird abgewiesen; eine Stufe ohne vorhandenen Abiturjahrgang und ein abgeleitetes Paar ohne
 * GOSt-Halbjahr werden ausgelassen und gemeldet.
 * <p>Der Ladedienst der Klausurdaten ist gemockt. Die an ihn übergebene Auswahl ist der Gegenstand dieser Tests, nicht der Klausurplan selbst.</p>
 */
@DisplayName("Die Stufenauswahl des Klausurplanungs-Repositorys")
class TestReportingRepositoryGostKlausurplanungAuswahl {

	/** Das Schuljahr des ausgewählten Schuljahresabschnitts. */
	private static final int SCHULJAHR = 2025;

	/** Der Schuljahresabschnitt, aus dem sich drei GOSt-Halbjahre ableiten lassen. */
	private static final int ABSCHNITT_MIT_GOST_HALBJAHREN = 1;

	/** Ein Schuljahresabschnitt jenseits der beiden Schulhalbjahre. Er erzeugt eine abgeleitete Stufe ohne GOSt-Halbjahr. */
	private static final int ABSCHNITT_OHNE_DRITTES_GOST_HALBJAHR = 3;

	/** Die Abiturjahrgänge, die es in der Schule gibt. */
	private static final List<Integer> VORHANDENE_ABITURJAHRGAENGE = List.of(2026, 2027, 2028);

	/** Eine kombinierte ID mit einem Abiturjahr außerhalb des Wertebereichs. */
	private static final long ID_MIT_UNGUELTIGER_FORM = 202503L;

	/** Eine kombinierte ID zu einem Abiturjahrgang, den es in der Schule nicht gibt. */
	private static final long ID_OHNE_VORHANDENEN_JAHRGANG = 20343L;

	/** Der gemockte Context, über den das Repository seine Umgebung erreicht. */
	private ReportingContext reportingContext;

	/** Die Reportparameter, aus denen das Repository die gewählten Stufen liest. */
	private ReportingParameterTypisiert reportingParameter;

	/** Das GOSt-Repository, das die vorhandenen Abiturjahrgänge liefert. */
	private ReportingRepositoryGost repositoryGost;

	/** Der gemockte Ladedienst der Klausurdaten. Er nimmt die ausgewählten Stufen entgegen. */
	private GostKlausurenAllDataService allDataService;


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(new Logger());
		when(reportingContext.filterService()).thenReturn(mock(ReportingFilterService.class));

		reportingParameter = mock(ReportingParameterTypisiert.class);
		when(reportingParameter.idsHauptdaten()).thenReturn(List.of());
		when(reportingContext.reportingParameter()).thenReturn(reportingParameter);

		final ReportingRepositorySchule repositorySchule = mock(ReportingRepositorySchule.class, RETURNS_DEEP_STUBS);
		when(repositorySchule.auswahlSchuljahresabschnitt().schuljahr()).thenReturn(SCHULJAHR);
		when(repositorySchule.auswahlSchuljahresabschnitt().abschnitt()).thenReturn(ABSCHNITT_MIT_GOST_HALBJAHREN);
		when(reportingContext.repositorySchule()).thenReturn(repositorySchule);

		repositoryGost = mock(ReportingRepositoryGost.class);
		when(repositoryGost.abiturjahrgaenge()).thenReturn(VORHANDENE_ABITURJAHRGAENGE);
		when(reportingContext.repositoryGost()).thenReturn(repositoryGost);

		allDataService = mock(GostKlausurenAllDataService.class);
	}


	/**
	 * Hängt den gemockten Ladedienst in den statischen Zugang der Service-Factory ein und lässt ihn Klausurdaten ohne Inhalt liefern.
	 *
	 * @param serviceFactoryBuilder Der statische Mock des Zugangs zur Service-Factory.
	 */
	private void gebeLeereKlausurdatenVor(final MockedStatic<GostKlausurenServiceFactoryBuilder> serviceFactoryBuilder) {
		final GostKlausurenServiceFactory serviceFactory = mock(GostKlausurenServiceFactory.class);
		when(serviceFactory.getGostKlausurenAllDataService()).thenReturn(allDataService);
		when(allDataService.getAllData(any())).thenReturn(new GostKlausurenAlleKlausurdaten());
		serviceFactoryBuilder.when(GostKlausurenServiceFactoryBuilder::getGostKlausurenServiceFactory).thenReturn(serviceFactory);
	}

	/**
	 * Fängt die Stufen ab, die das Repository an den Ladedienst übergibt.
	 *
	 * @return Die übergebenen Stufen als Paare aus Abiturjahr und Halbjahres-ID im Format {@code Abiturjahr/Halbjahr}.
	 */
	private List<String> uebergebeneStufen() {
		final ArgumentCaptor<List<GostKlausurenHalbjahresdaten>> stufen = ArgumentCaptor.captor();
		verify(allDataService, times(1)).getAllData(stufen.capture());
		return stufen.getValue().stream().map(stufe -> "%d/%d".formatted(stufe.abiturjahrgang, stufe.gostHalbjahr)).toList();
	}

	/**
	 * Prüft, dass genau eine Stufe als Ausgabeproblem gemeldet ist.
	 *
	 * @param kombinierteId Die kombinierte ID der ausgelassenen Stufe.
	 */
	private void erwarteGemeldeteStufe(final long kombinierteId) {
		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.NICHT_VORHANDEN),
				eq(ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN),
				eq(ReportingProblemSchluessel.fuer(GostKlausurenHalbjahresdaten.class, kombinierteId)), anyString(), eq(null));
	}


	@Test
	void testGueltigeUebergebeneStufenWerdenAusgewaehlt() {
		when(reportingParameter.idsHauptdaten()).thenReturn(List.of(20263L, 20271L));

		try (MockedStatic<GostKlausurenServiceFactoryBuilder> serviceFactoryBuilder = mockStatic(GostKlausurenServiceFactoryBuilder.class)) {
			gebeLeereKlausurdatenVor(serviceFactoryBuilder);

			new ReportingRepositoryGostKlausurplanung(reportingContext).klausurtermine();

			assertEquals(List.of("2026/3", "2027/1"), uebergebeneStufen(), "Beide übergebenen Stufen erreichen den Ladedienst unverändert.");
			verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
		}
	}

	@Test
	void testEinNichtVorhandenerAbiturjahrgangWirdAusgelassenUndGemeldet() {
		// Die Stufen sind Nutzlast wie die IDs eines Listenreports. Für einen Abiturjahrgang, den es nicht gibt, gibt es keine Ausgabe.
		when(reportingParameter.idsHauptdaten()).thenReturn(List.of(20263L, ID_OHNE_VORHANDENEN_JAHRGANG));

		try (MockedStatic<GostKlausurenServiceFactoryBuilder> serviceFactoryBuilder = mockStatic(GostKlausurenServiceFactoryBuilder.class)) {
			gebeLeereKlausurdatenVor(serviceFactoryBuilder);

			new ReportingRepositoryGostKlausurplanung(reportingContext).klausurtermine();

			assertEquals(List.of("2026/3"), uebergebeneStufen(), "Allein die Stufe mit vorhandenem Abiturjahrgang erreicht den Ladedienst.");
			erwarteGemeldeteStufe(ID_OHNE_VORHANDENEN_JAHRGANG);
		}
	}

	@Test
	void testOhneVorhandeneStufeLaeuftDerAufbauMitLeererAuswahl() {
		// Der Aufbau läuft auch ohne verbliebene Stufe. Nur so entsteht die leere Ausgabe statt eines Fehlers.
		when(reportingParameter.idsHauptdaten()).thenReturn(List.of(ID_OHNE_VORHANDENEN_JAHRGANG));

		try (MockedStatic<GostKlausurenServiceFactoryBuilder> serviceFactoryBuilder = mockStatic(GostKlausurenServiceFactoryBuilder.class)) {
			gebeLeereKlausurdatenVor(serviceFactoryBuilder);

			assertTrue(new ReportingRepositoryGostKlausurplanung(reportingContext).klausurtermine().isEmpty(), "Ohne Stufe gibt es keine Klausurtermine.");

			assertTrue(uebergebeneStufen().isEmpty(), "Der Ladedienst wird mit leerer Auswahl aufgerufen, nicht übersprungen.");
			erwarteGemeldeteStufe(ID_OHNE_VORHANDENEN_JAHRGANG);
		}
	}

	@Test
	void testOhneUebergebeneStufenWerdenAlleDreiStufenAbgeleitet() {
		// Der Grundfall des Clients: Abschnitt 1 des Schuljahres 2025 ergibt EF.1 für Abitur 2028, Q1.1 für 2027 und Q2.1 für 2026.
		try (MockedStatic<GostKlausurenServiceFactoryBuilder> serviceFactoryBuilder = mockStatic(GostKlausurenServiceFactoryBuilder.class)) {
			gebeLeereKlausurdatenVor(serviceFactoryBuilder);

			new ReportingRepositoryGostKlausurplanung(reportingContext).klausurtermine();

			assertEquals(List.of("2028/0", "2027/2", "2026/4"), uebergebeneStufen(), "Die Ableitung erhöht das Abiturjahr und senkt das GOSt-Halbjahr.");
			verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
		}
	}

	@Test
	void testEinAbgeleitetesPaarOhneGostHalbjahrWirdAusgelassenUndGemeldet() {
		// Ein Abschnitt jenseits der beiden Schulhalbjahre erzeugt die Halbjahre 2, 4 und 6; nur 2 und 4 sind GOSt-Halbjahre. Abweisen lässt sich hier
		// nichts, denn der Anwender hat nichts übergeben.
		when(reportingContext.repositorySchule().auswahlSchuljahresabschnitt().abschnitt()).thenReturn(ABSCHNITT_OHNE_DRITTES_GOST_HALBJAHR);

		try (MockedStatic<GostKlausurenServiceFactoryBuilder> serviceFactoryBuilder = mockStatic(GostKlausurenServiceFactoryBuilder.class)) {
			gebeLeereKlausurdatenVor(serviceFactoryBuilder);

			new ReportingRepositoryGostKlausurplanung(reportingContext).klausurtermine();

			assertEquals(List.of("2028/2", "2027/4"), uebergebeneStufen(), "Die dritte abgeleitete Stufe trägt kein GOSt-Halbjahr.");
			erwarteGemeldeteStufe(20266L);
		}
	}

	@Test
	void testEinLadefehlerDerAbiturjahrgaengeBleibtEinServerfehler() {
		// Der Existenzabgleich braucht die vorhandenen Abiturjahrgänge. Ohne sie ist die Auswahl unbekannt, nicht leer.
		when(reportingParameter.idsHauptdaten()).thenReturn(List.of(20263L));
		final ApiOperationException ladefehler = new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
				"### FEHLER: Die vorhandenen Abiturjahrgänge konnten nicht ermittelt werden.");
		when(repositoryGost.abiturjahrgaenge()).thenThrow(ladefehler);

		try (MockedStatic<GostKlausurenServiceFactoryBuilder> serviceFactoryBuilder = mockStatic(GostKlausurenServiceFactoryBuilder.class)) {
			gebeLeereKlausurdatenVor(serviceFactoryBuilder);
			final ReportingRepositoryGostKlausurplanung repository = new ReportingRepositoryGostKlausurplanung(reportingContext);

			final ApiOperationException aoe = assertThrows(ApiOperationException.class, repository::klausurtermine);

			assertSame(ladefehler, aoe, "Der Ladefehler wird unverändert durchgereicht.");
			serviceFactoryBuilder.verifyNoInteractions();
		}
	}

	@Test
	void testEineFormalUngueltigeStufeWirdVorJedemDatenzugriffAbgewiesen() {
		// 202503 zerfällt zu Abiturjahr 20250 und Halbjahr 3. Ohne die Formprüfung liefe die kaputte Angabe in den Existenzabgleich und würde still
		// ausgelassen.
		when(reportingParameter.idsHauptdaten()).thenReturn(List.of(ID_MIT_UNGUELTIGER_FORM));

		try (MockedStatic<GostKlausurenServiceFactoryBuilder> serviceFactoryBuilder = mockStatic(GostKlausurenServiceFactoryBuilder.class)) {
			gebeLeereKlausurdatenVor(serviceFactoryBuilder);
			final ReportingRepositoryGostKlausurplanung repository = new ReportingRepositoryGostKlausurplanung(reportingContext);

			final ApiOperationException aoe = assertThrows(ApiOperationException.class, repository::klausurtermine);

			assertEquals(Status.BAD_REQUEST, aoe.getStatus());
			serviceFactoryBuilder.verifyNoInteractions();
			verify(repositoryGost, never()).abiturjahrgaenge();
		}
	}

}
