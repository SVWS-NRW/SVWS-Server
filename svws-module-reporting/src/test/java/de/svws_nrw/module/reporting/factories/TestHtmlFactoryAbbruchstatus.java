package de.svws_nrw.module.reporting.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryGost;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryGostKlausurplanung;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryGostKursplanung;
import de.svws_nrw.module.reporting.repositories.ReportingRepositorySchule;
import de.svws_nrw.module.reporting.types.schule.ProxyReportingBenutzer;
import de.svws_nrw.module.reporting.types.schule.ReportingSchule;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft am API-Rand der HTML-Factory, mit welchem Status ein GOSt-Report abbricht, wenn der Zugriff auf die Datenschicht scheitert.
 * <p>Die Statustests an Repository und Validierung sehen die Ebenen darüber nicht; erst die Messung an der Factory belegt, dass keine davon den Status
 * ersetzt: Ein Serverfehler der Datenschicht kommt als Serverfehler an, eine nicht vorhandene Hauptressource bleibt die Auskunft "gibt es nicht".</p>
 * <p>Der Fehler wird so tief wie möglich injiziert und läuft von dort den produktiven Weg über Context und Initializer bis zur Factory. Für die
 * GOSt-Kursplanung sitzt die Injektion in der Datenschicht unter dem echten Repository; für die Fachwahlstatistiken am statustragenden Repository-Zugriff.</p>
 */
class TestHtmlFactoryAbbruchstatus {

	/** Die ID, unter der das Blockungsergebnis angefordert wird. */
	private static final long ID_BLOCKUNGSERGEBNIS = 3L;

	/** Das Abiturjahr, das in den Tests als vorhanden gilt. */
	private static final int ABITURJAHR = 2025;

	/** Der gemockte Context, den die Factory erhält. */
	private ReportingContext reportingContext;

	/** Die gemockten Reporting-Parameter; einzelne Tests stellen darüber die Reportvorlage und die Hauptdaten um. */
	private ReportingParameterTypisiert reportingParameter;


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(new Logger());

		reportingParameter = mock(ReportingParameterTypisiert.class);
		when(reportingParameter.reportVorlage()).thenReturn(ReportingReportvorlage.GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN);
		when(reportingParameter.idHauptdatenObjekt()).thenReturn(ID_BLOCKUNGSERGEBNIS);
		when(reportingContext.reportingParameter()).thenReturn(reportingParameter);

		final ProxyReportingBenutzer benutzer = mock(ProxyReportingBenutzer.class);
		when(benutzer.pruefeKompetenz(any())).thenReturn(true);
		when(reportingContext.benutzer()).thenReturn(benutzer);

		final ReportingRepositorySchule repositorySchule = mock(ReportingRepositorySchule.class);
		when(repositorySchule.schule()).thenReturn(mock(ReportingSchule.class));
		when(repositorySchule.istSchuleMitGost()).thenReturn(true);
		// Die GOSt-Vorlagen sind an Schulformen mit gymnasialer Oberstufe gebunden.
		when(repositorySchule.schulform()).thenReturn(Schulform.GY);
		when(reportingContext.repositorySchule()).thenReturn(repositorySchule);

		when(reportingContext.repositoryGostKursplanung()).thenReturn(new ReportingRepositoryGostKursplanung(reportingContext));
	}


	/**
	 * Stellt die Vorlage auf den Fachwahlstatistik-Report des Abiturjahrgangs um und verdrahtet das GOSt-Repository mit dem vorhandenen Abiturjahrgang.
	 *
	 * @param angefordertesAbiturjahr Das Abiturjahr, das der Request als Hauptobjekt benennt.
	 *
	 * @return Das gemockte GOSt-Repository zur weiteren Konfiguration.
	 */
	private ReportingRepositoryGost gebeFachwahlstatistikReportVor(final long angefordertesAbiturjahr) {
		when(reportingParameter.reportVorlage()).thenReturn(ReportingReportvorlage.GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_V_FACHWAHLSTATISTIKEN);
		when(reportingParameter.idsHauptdaten()).thenReturn(List.of(angefordertesAbiturjahr));
		when(reportingParameter.idsDetaildaten()).thenReturn(List.of());

		final ReportingRepositoryGost repositoryGost = mock(ReportingRepositoryGost.class);
		when(repositoryGost.abiturjahrgaenge()).thenReturn(List.of(ABITURJAHR));
		when(reportingContext.repositoryGost()).thenReturn(repositoryGost);
		return repositoryGost;
	}


	/**
	 * Prüft, ob die übergebene Ursache in der Ursachenkette der Exception enthalten ist.
	 *
	 * @param aoe     Die am API-Rand angekommene Exception.
	 * @param ursache Die in der Datenschicht ausgelöste Ursache.
	 *
	 * @return true, wenn die Ursache in der Kette enthalten ist, sonst false.
	 */
	private static boolean enthaeltUrsache(final Throwable aoe, final Throwable ursache) {
		for (Throwable glied = aoe.getCause(); glied != null; glied = glied.getCause()) {
			if (glied == ursache) {
				return true;
			}
		}
		return false;
	}


	@Test
	void testEinServerfehlerDerDatenschichtKommtAlsServerfehlerAmApiRandAn() {
		final ApiOperationException ursache = new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Blockungsdaten sind fehlerhaft.");

		try (MockedStatic<DataGostBlockungsergebnisse> dataErgebnisse = mockStatic(DataGostBlockungsergebnisse.class)) {
			dataErgebnisse.when(() -> DataGostBlockungsergebnisse.getErgebnisFromID(any(), anyLong())).thenThrow(ursache);

			final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> HtmlFactory.erzeuge(reportingContext));

			assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus(),
					"Ein Serverfehler der Datenschicht behält seinen Status; ein NOT_FOUND meldete dem Anwender, sein Blockungsergebnis gebe es nicht.");
			assertTrue(enthaeltUrsache(aoe, ursache), "Die Ursache der Datenschicht bleibt über alle Ebenen in der Ursachenkette erhalten.");
		}
	}

	@Test
	void testEineUnbekannteBlockungsergebnisIdBleibtNotFoundAmApiRand() {
		// Gegenprobe: Der erhaltene Status ist keine pauschale Ersetzung durch 500 - eine unbekannte ID bleibt die Auskunft "gibt es nicht".
		try (MockedStatic<DataGostBlockungsergebnisse> dataErgebnisse = mockStatic(DataGostBlockungsergebnisse.class)) {
			dataErgebnisse.when(() -> DataGostBlockungsergebnisse.getErgebnisFromID(any(), anyLong()))
					.thenThrow(new ApiOperationException(Status.NOT_FOUND, "Ungültige Blockungsergebnis-ID übergeben."));

			final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> HtmlFactory.erzeuge(reportingContext));

			assertEquals(Status.NOT_FOUND, aoe.getStatus());
		}
	}

	@Test
	void testEinLadefehlerDerFachwahlstatistikKommtAlsServerfehlerAmApiRandAn() {
		// Die Statistik ist der Hauptinhalt dieses Reports; der strikte Repository-Zugriff wirft statustragend. Weder Proxy noch Context noch Initializer
		// dürfen daraus einen anderen Status oder einen leeren Report machen.
		final ApiOperationException ladefehler = new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
				"### FEHLER: Die GOSt-Fachwahlstatistik des Abiturjahrgangs %d konnte nicht ermittelt werden.".formatted(ABITURJAHR));
		when(gebeFachwahlstatistikReportVor(ABITURJAHR).fachwahlen(ABITURJAHR)).thenThrow(ladefehler);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> HtmlFactory.erzeuge(reportingContext));

		assertSame(ladefehler, aoe, "Der statustragende Wurf des Repositorys erreicht den API-Rand unverändert.");
	}

	@Test
	void testEinServerfehlerDesKlausurplanAufbausKommtAlsServerfehlerAmApiRandAn() {
		// Der Context der Klausurplanung reicht den Status der Datenschicht durch und reichert nur die Meldung an; ein pauschaler Status gäbe einen
		// Serverfehler als fehlende Klausurdaten aus.
		when(reportingParameter.reportVorlage()).thenReturn(ReportingReportvorlage.GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN);
		when(reportingParameter.idsHauptdaten()).thenReturn(List.of(20253L));
		final ReportingRepositoryGost repositoryGost = mock(ReportingRepositoryGost.class);
		when(repositoryGost.abiturjahrgaenge()).thenReturn(List.of(2025));
		when(reportingContext.repositoryGost()).thenReturn(repositoryGost);
		final ReportingRepositoryGostKlausurplanung repositoryKlausurplanung = mock(ReportingRepositoryGostKlausurplanung.class);
		final ApiOperationException ursache = new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Klausurdaten sind nicht lesbar.");
		doThrow(ursache).when(repositoryKlausurplanung).initManager(any());
		when(reportingContext.repositoryGostKlausurplanung()).thenReturn(repositoryKlausurplanung);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> HtmlFactory.erzeuge(reportingContext));

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus(), "Der Status der Datenschicht bleibt am API-Rand erhalten.");
		assertTrue(enthaeltUrsache(aoe, ursache), "Die Ursache der Datenschicht bleibt in der Ursachenkette erhalten.");
	}

	@Test
	void testEinNichtVorhandenerAbiturjahrgangDerFachwahlstatistikErgibtNotFoundAmApiRand() {
		// Der Abiturjahrgang ist die adressierte Hauptressource dieses Reports; die Validierung des Initializers antwortet mit "gibt es nicht".
		gebeFachwahlstatistikReportVor(2026L);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> HtmlFactory.erzeuge(reportingContext));

		assertEquals(Status.NOT_FOUND, aoe.getStatus());
	}

}
