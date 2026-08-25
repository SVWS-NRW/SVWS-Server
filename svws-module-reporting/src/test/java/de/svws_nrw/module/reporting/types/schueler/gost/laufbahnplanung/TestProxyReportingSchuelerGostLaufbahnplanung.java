package de.svws_nrw.module.reporting.types.schueler.gost.laufbahnplanung;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLNonTransientConnectionException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryGost;
import de.svws_nrw.module.reporting.repositories.ReportingRepositorySchule;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Fehlerpfade beim Aufbau der GOSt-Laufbahnplanung eines Schülers.
 * <p>Zwei Stellen sind abgesichert: Ein Ladefehler der Jahrgangsdaten oder Fächer lässt den Bogen leer, wird aber über die Fassade erfasst - je nach
 * Ursache als hingenommener Befund oder als Abbruch durch die Fassade. Und eine Katalogreferenz, die im ausgewählten Schuljahr fehlt, wird als fehlende
 * Angabe gemeldet statt das Rendern mit einer NullPointerException zu beenden.</p>
 * <p>Der Test der Katalogreferenz nutzt das Fach MB, dessen Katalog-Historie erst ab 2023 beginnt; er benötigt die initialisierten Core-Type-Daten.</p>
 */
class TestProxyReportingSchuelerGostLaufbahnplanung {

	/** Die ID des Schülers, dessen Laufbahnplanung aufgebaut wird. */
	private static final long ID_SCHUELER = 17L;

	/** Das Abiturjahr des Schülers. */
	private static final int ABITURJAHR = 2027;

	/** Der gemockte Context, den der Proxy erhält. */
	private ReportingContext reportingContext;


	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);

		final ReportingRepositorySchule repositorySchule = mock(ReportingRepositorySchule.class, RETURNS_DEEP_STUBS);
		when(repositorySchule.auswahlSchuljahresabschnitt().schuljahr()).thenReturn(2025);
		when(reportingContext.repositorySchule()).thenReturn(repositorySchule);
	}


	/**
	 * Verdrahtet das GOSt-Repository mit Beratungs-Abiturdaten des Schülers und gibt es zur weiteren Konfiguration zurück.
	 *
	 * @return Das gemockte GOSt-Repository.
	 */
	private ReportingRepositoryGost gebeRepositoryGostMitAbiturdatenVor() {
		final ReportingRepositoryGost repositoryGost = mock(ReportingRepositoryGost.class);
		final Abiturdaten abiturdaten = new Abiturdaten();
		abiturdaten.abiturjahr = ABITURJAHR;
		when(repositoryGost.beratungsdatenAbiturdaten(ID_SCHUELER)).thenReturn(abiturdaten);
		when(reportingContext.repositoryGost()).thenReturn(repositoryGost);
		return repositoryGost;
	}

	/**
	 * Erzeugt den Schüler-Mock, dessen Laufbahnplanung aufgebaut wird.
	 *
	 * @return Der Schüler als Mock, der allein seine ID kennt.
	 */
	private static ReportingSchueler schueler() {
		final ReportingSchueler schueler = mock(ReportingSchueler.class);
		when(schueler.id()).thenReturn(ID_SCHUELER);
		return schueler;
	}


	// ##### Ladefehler der Jahrgangsdaten und Fächer #####

	@Test
	void testEinLadefehlerDerJahrgangsdatenWirdGemeldetUndDerBogenBleibtLeer() {
		final ApiOperationException fehler = new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehlerinjektion");
		when(gebeRepositoryGostMitAbiturdatenVor().jahrgangsdaten(ABITURJAHR)).thenThrow(fehler);

		final ProxyReportingSchuelerGostLaufbahnplanung laufbahnplanung = new ProxyReportingSchuelerGostLaufbahnplanung(reportingContext, schueler());

		assertEquals(ABITURJAHR, laufbahnplanung.abiturjahr(), "Das Abiturjahr ist vor dem Fehler gesetzt und bleibt erhalten.");
		assertTrue(laufbahnplanung.fachwahlen().isEmpty(), "Der Bogen bleibt leer; die Vorlage zeigt ihre Leerwerte.");
		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(ReportingSchuelerGostLaufbahnplanung.class, ID_SCHUELER)),
				anyString(), eq(fehler));
	}

	@Test
	void testEinVerbindungsfehlerDerJahrgangsdatenTraegtDieAbbrechendeUrsache() {
		// Dass die Fassade aus dieser Ursache einen Serverfehler macht, prüft TestReportingContextMeldefassade - der Mock hier kann nicht werfen.
		final ApiOperationException fehler = new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
				new SQLNonTransientConnectionException("Verbindung zur Datenbank verloren."));
		when(gebeRepositoryGostMitAbiturdatenVor().jahrgangsdaten(ABITURJAHR)).thenThrow(fehler);

		new ProxyReportingSchuelerGostLaufbahnplanung(reportingContext, schueler());

		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.INFRASTRUKTURSTOERUNG),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(ReportingSchuelerGostLaufbahnplanung.class, ID_SCHUELER)),
				anyString(), eq(fehler));
	}


	// ##### Katalogreferenz im ausgewählten Schuljahr #####

	@Test
	void testEineFehlendeKatalogreferenzWirdAlsFehlendeAngabeGemeldet() {
		final GostFach fach = new GostFach();
		fach.id = 42L;
		fach.kuerzel = "MB";

		final var katalogdaten = ProxyReportingSchuelerGostLaufbahnplanung.katalogdatenOderMelde(reportingContext, Fach.MB, fach, 2020);

		assertNull(katalogdaten, "Vor Beginn der Katalog-Historie gibt es keinen Eintrag; die Sprachdaten der Fachwahl bleiben leer.");
		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.NICHT_VORHANDEN),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(ReportingFach.class, 42L)), anyString(), eq(null));
	}

	@Test
	void testEineVorhandeneKatalogreferenzWirdOhneMeldungGeliefert() {
		final GostFach fach = new GostFach();
		fach.id = 42L;
		fach.kuerzel = "MB";

		assertNotNull(ProxyReportingSchuelerGostLaufbahnplanung.katalogdatenOderMelde(reportingContext, Fach.MB, fach, 2023));
		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
	}

}
