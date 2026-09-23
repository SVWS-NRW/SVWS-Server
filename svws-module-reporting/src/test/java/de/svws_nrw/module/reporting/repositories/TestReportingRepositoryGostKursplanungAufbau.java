package de.svws_nrw.module.reporting.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import de.svws_nrw.core.data.gost.GostBlockungsdaten;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.filterung.ReportingFilterService;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft, dass das Repository der GOSt-Kursplanung seinen Manager beim ersten Zugriff aufbaut und dass es keinen Zugriff auf ein nur teilweise aufgebautes
 * Repository gibt. Ein gescheiterter Aufbau wird gemerkt und an jedem Getter unverändert gemeldet.
 */
@DisplayName("Der Aufbau des Kursplanungs-Repositorys beim ersten Zugriff")
class TestReportingRepositoryGostKursplanungAufbau {

	/** Die ID des Blockungsergebnisses, das die Reportparameter benennen. */
	private static final long ID_BLOCKUNGSERGEBNIS = 3L;

	/** Die ID der Blockung, die das Blockungsergebnis nennt. */
	private static final long ID_BLOCKUNG = 7L;

	/** Ein Halbjahreswert, den die GOSt nicht kennt. */
	private static final int UNGUELTIGES_HALBJAHR = 9;

	/** Der gemockte Context. Er liefert bewusst keine Datenbankverbindung, sodass jeder Datenzugriff scheitert. */
	private ReportingContext reportingContext;

	/** Die Reportparameter, aus denen das Repository die ID des Blockungsergebnisses liest. */
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
		when(reportingParameter.idHauptdatenObjekt()).thenReturn(ID_BLOCKUNGSERGEBNIS);
		when(reportingContext.reportingParameter()).thenReturn(reportingParameter);
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
	void testEineFehlendeAuswahlWirdVorJedemDatenzugriffAbgewiesen() {
		// Der E-Mail-Versand erreicht das Repository ohne den Initializer. Ohne diese Schranke ginge sein Vorgabewert in den Ladeweg.
		when(reportingParameter.idHauptdatenObjekt()).thenReturn(-1L);
		final ReportingRepositoryGostKursplanung repository = new ReportingRepositoryGostKursplanung(reportingContext);

		try (MockedStatic<DataGostBlockungsergebnisse> dataErgebnisse = mockStatic(DataGostBlockungsergebnisse.class)) {
			final ApiOperationException aoe = assertThrows(ApiOperationException.class, repository::blockungsergebnis);

			assertEquals(Status.BAD_REQUEST, aoe.getStatus());
			dataErgebnisse.verifyNoInteractions();
		}
	}

	@Test
	void testDieReihenfolgeDerGetterAendertDenFehlerNicht() {
		// Jeder Getter stößt den Aufbau an, auch die vier ohne Aufrufer in der Ausgabe.
		final ApiOperationException ursache = new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Blockungsdaten sind fehlerhaft.");

		try (MockedStatic<DataGostBlockungsergebnisse> dataErgebnisse = mockStatic(DataGostBlockungsergebnisse.class)) {
			dataErgebnisse.when(() -> DataGostBlockungsergebnisse.getErgebnisFromID(any(), anyLong())).thenThrow(ursache);

			final ReportingRepositoryGostKursplanung ersterZugriffUeberKurse = new ReportingRepositoryGostKursplanung(reportingContext);
			final ApiOperationException ueberKurse = assertThrows(ApiOperationException.class, ersterZugriffUeberKurse::kurse);
			final ApiOperationException danachUeberSchienen = assertThrows(ApiOperationException.class, ersterZugriffUeberKurse::schienen);

			final ReportingRepositoryGostKursplanung ersterZugriffUeberSchienen = new ReportingRepositoryGostKursplanung(reportingContext);
			final ApiOperationException ueberSchienen = assertThrows(ApiOperationException.class, ersterZugriffUeberSchienen::schienen);

			assertEquals(ueberKurse.getStatus(), ueberSchienen.getStatus(), "Beide Reihenfolgen führen zum selben Ergebnis.");
			assertSame(ueberKurse, danachUeberSchienen, "Der zweite Getter meldet denselben Fehler.");
		}
	}

	@Test
	void testEinAufbaufehlerTraegtAnJedemGetterDenselbenStatus() {
		final ApiOperationException ursache = new ApiOperationException(Status.NOT_FOUND, "Ungültige Blockungsergebnis-ID übergeben.");
		final ReportingRepositoryGostKursplanung repository = new ReportingRepositoryGostKursplanung(reportingContext);

		try (MockedStatic<DataGostBlockungsergebnisse> dataErgebnisse = mockStatic(DataGostBlockungsergebnisse.class)) {
			dataErgebnisse.when(() -> DataGostBlockungsergebnisse.getErgebnisFromID(any(), anyLong())).thenThrow(ursache);

			final ApiOperationException erster = assertThrows(ApiOperationException.class, repository::blockungsergebnis);
			final ApiOperationException zweiter = assertThrows(ApiOperationException.class, () -> repository.kurs(1L));

			assertEquals(Status.NOT_FOUND, erster.getStatus());
			assertEquals(Status.NOT_FOUND, zweiter.getStatus());
			assertSame(ursache, zweiter.getCause(), "Die Meldung der Datenschicht bleibt als Ursache erhalten.");
		}
	}

	@Test
	void testEinZweiterZugriffNachEinemAufbaufehlerLaedtNichtErneut() {
		// Ein zweiter Lauf verdoppelt die Kursbelegungen an den geteilten Schüler-Objekten.
		final ReportingRepositoryGostKursplanung repository = new ReportingRepositoryGostKursplanung(reportingContext);

		try (MockedStatic<DataGostBlockungsergebnisse> dataErgebnisse = mockStatic(DataGostBlockungsergebnisse.class)) {
			dataErgebnisse.when(() -> DataGostBlockungsergebnisse.getErgebnisFromID(any(), anyLong()))
					.thenThrow(new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Blockungsdaten sind fehlerhaft."));

			assertThrows(ApiOperationException.class, repository::blockungsergebnis);
			assertThrows(ApiOperationException.class, repository::kurse);

			dataErgebnisse.verify(() -> DataGostBlockungsergebnisse.getErgebnisFromID(any(), anyLong()), times(1));
		}
	}

	@Test
	void testEinUngepruefterFehlerBeendetDenAufbauEbenfallsEndgueltig() {
		// Die Core-Manager melden ihre Datenfehler als DeveloperNotificationException. Auch sie beendet den Aufbau endgültig.
		final ReportingRepositoryGostKursplanung repository = new ReportingRepositoryGostKursplanung(reportingContext);

		try (MockedStatic<DataGostBlockungsergebnisse> dataErgebnisse = mockStatic(DataGostBlockungsergebnisse.class)) {
			dataErgebnisse.when(() -> DataGostBlockungsergebnisse.getErgebnisFromID(any(), anyLong()))
					.thenThrow(new DeveloperNotificationException("Fehlerinjektion"));

			final ApiOperationException erster = assertThrows(ApiOperationException.class, repository::blockungsergebnis);
			final ApiOperationException zweiter = assertThrows(ApiOperationException.class, repository::kurse);

			assertEquals(Status.INTERNAL_SERVER_ERROR, erster.getStatus());
			assertSame(erster, zweiter, "Der gemerkte Fehler wird unverändert erneut gemeldet.");
			dataErgebnisse.verify(() -> DataGostBlockungsergebnisse.getErgebnisFromID(any(), anyLong()), times(1));
		}
	}

	@Test
	void testEinFehlerNachDerManagerErzeugungLiefertKeineHalbenObjekte() {
		// Der Manager steht bereits, erst der Aufbau der Reporting-Objekte scheitert.
		final ReportingRepositoryGostKursplanung repository = new ReportingRepositoryGostKursplanung(reportingContext);
		final GostBlockungsergebnis blockungsergebnis = new GostBlockungsergebnis();
		blockungsergebnis.id = ID_BLOCKUNGSERGEBNIS;
		blockungsergebnis.blockungID = ID_BLOCKUNG;

		final GostBlockungsdaten blockungsdaten = new GostBlockungsdaten();
		blockungsdaten.id = ID_BLOCKUNG;
		blockungsdaten.gostHalbjahr = UNGUELTIGES_HALBJAHR;
		blockungsdaten.abijahrgang = 2025;
		final GostBlockungsdatenManager datenManager = mock(GostBlockungsdatenManager.class);
		when(datenManager.daten()).thenReturn(blockungsdaten);
		when(datenManager.getID()).thenReturn(ID_BLOCKUNG);
		final GostFaecherManager faecherManager = mock(GostFaecherManager.class);
		when(faecherManager.faecher()).thenReturn(new ArrayList<>());
		when(datenManager.faecherManager()).thenReturn(faecherManager);

		try (MockedStatic<DataGostBlockungsergebnisse> dataErgebnisse = mockStatic(DataGostBlockungsergebnisse.class);
				MockedStatic<DataGostBlockungsdaten> dataBlockungsdaten = mockStatic(DataGostBlockungsdaten.class)) {
			dataErgebnisse.when(() -> DataGostBlockungsergebnisse.getErgebnisFromID(any(), anyLong())).thenReturn(blockungsergebnis);
			dataBlockungsdaten.when(() -> DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(any(), eq(ID_BLOCKUNG))).thenReturn(datenManager);

			final ApiOperationException erster = assertThrows(ApiOperationException.class, repository::blockungsergebnis);
			final ApiOperationException zweiter = assertThrows(ApiOperationException.class, repository::kurse);

			assertEquals(Status.INTERNAL_SERVER_ERROR, erster.getStatus());
			assertSame(erster, zweiter, "Der zweite Getter liefert keine halb aufgebauten Objekte, sondern denselben Fehler.");
			// Diese Logzeile entsteht allein im Aufbau der Reporting-Objekte. Sie belegt, dass der Manager zum Zeitpunkt des Fehlers stand.
			assertEquals(List.of("Blockung %d, GOSt-Halbjahr-Wert %d".formatted(ID_BLOCKUNG, UNGUELTIGES_HALBJAHR)), fehlermeldungenImLog());
			verify(reportingContext, times(0)).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
		}
	}

	/**
	 * Verdrahtet einen Aufbau, der durchläuft: eine Blockung ohne Schienen, Kurse und Schüler. Sie genügt, um den Manager und die Reporting-Objekte
	 * vollständig entstehen zu lassen.
	 *
	 * @param datenManager Der Mock der Blockungsdaten, in den die Vorgaben eingetragen werden.
	 */
	private void gebeLeereBlockungVor(final GostBlockungsdatenManager datenManager) {
		final GostBlockungsdaten blockungsdaten = new GostBlockungsdaten();
		blockungsdaten.id = ID_BLOCKUNG;
		blockungsdaten.gostHalbjahr = GostHalbjahr.Q11.id;
		blockungsdaten.abijahrgang = 2025;
		blockungsdaten.name = "Blockung ohne Kurse";
		when(datenManager.daten()).thenReturn(blockungsdaten);
		when(datenManager.getID()).thenReturn(ID_BLOCKUNG);
		final GostFaecherManager faecherManager = mock(GostFaecherManager.class);
		when(faecherManager.faecher()).thenReturn(new ArrayList<>());
		when(datenManager.faecherManager()).thenReturn(faecherManager);
		when(datenManager.schuelerGetListe()).thenReturn(new ArrayList<>());
		when(datenManager.schieneGetListe()).thenReturn(new ArrayList<>());
		when(datenManager.kursGetListeSortiertNachKursartFachNummer()).thenReturn(new ArrayList<>());
		when(reportingContext.filterService()).thenReturn(mock(ReportingFilterService.class));
		final ReportingRepositorySchule repositorySchule = mock(ReportingRepositorySchule.class, RETURNS_DEEP_STUBS);
		when(reportingContext.repositorySchule()).thenReturn(repositorySchule);
		final ReportingRepositorySchueler repositorySchueler = mock(ReportingRepositorySchueler.class);
		when(repositorySchueler.schueler(any())).thenReturn(new ArrayList<>());
		when(reportingContext.repositorySchueler()).thenReturn(repositorySchueler);
	}

	@Test
	void testDieReihenfolgeDerGetterAendertDieDatenNicht() {
		// Kein Getter gibt mehr ohne Aufbau eine leere Antwort.
		final GostBlockungsergebnis blockungsergebnis = new GostBlockungsergebnis();
		blockungsergebnis.id = ID_BLOCKUNGSERGEBNIS;
		blockungsergebnis.blockungID = ID_BLOCKUNG;
		final GostBlockungsdatenManager datenManager = mock(GostBlockungsdatenManager.class);
		gebeLeereBlockungVor(datenManager);

		try (MockedStatic<DataGostBlockungsergebnisse> dataErgebnisse = mockStatic(DataGostBlockungsergebnisse.class);
				MockedStatic<DataGostBlockungsdaten> dataBlockungsdaten = mockStatic(DataGostBlockungsdaten.class)) {
			dataErgebnisse.when(() -> DataGostBlockungsergebnisse.getErgebnisFromID(any(), anyLong())).thenReturn(blockungsergebnis);
			dataBlockungsdaten.when(() -> DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(any(), eq(ID_BLOCKUNG))).thenReturn(datenManager);

			final ReportingRepositoryGostKursplanung ueberSchienen = new ReportingRepositoryGostKursplanung(reportingContext);
			assertTrue(ueberSchienen.schienen().isEmpty(), "Die Blockung hat keine Schienen.");
			assertNotNull(ueberSchienen.blockungsergebnis(), "Auch nach einem anderen Getter steht das Blockungsergebnis.");

			final ReportingRepositoryGostKursplanung ueberErgebnis = new ReportingRepositoryGostKursplanung(reportingContext);
			assertNotNull(ueberErgebnis.blockungsergebnis(), "Der erste Zugriff baut auf.");
			assertEquals(ueberSchienen.blockungsergebnis().id(), ueberErgebnis.blockungsergebnis().id(),
					"Beide Reihenfolgen liefern dasselbe Blockungsergebnis.");
		}
	}

	@Test
	void testDerAufbauLaeuftBeiMehrfachemZugriffNurEinmal() {
		final GostBlockungsergebnis blockungsergebnis = new GostBlockungsergebnis();
		blockungsergebnis.id = ID_BLOCKUNGSERGEBNIS;
		blockungsergebnis.blockungID = ID_BLOCKUNG;
		final GostBlockungsdatenManager datenManager = mock(GostBlockungsdatenManager.class);
		gebeLeereBlockungVor(datenManager);

		try (MockedStatic<DataGostBlockungsergebnisse> dataErgebnisse = mockStatic(DataGostBlockungsergebnisse.class);
				MockedStatic<DataGostBlockungsdaten> dataBlockungsdaten = mockStatic(DataGostBlockungsdaten.class)) {
			dataErgebnisse.when(() -> DataGostBlockungsergebnisse.getErgebnisFromID(any(), anyLong())).thenReturn(blockungsergebnis);
			dataBlockungsdaten.when(() -> DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(any(), eq(ID_BLOCKUNG))).thenReturn(datenManager);

			final ReportingRepositoryGostKursplanung repository = new ReportingRepositoryGostKursplanung(reportingContext);
			repository.blockungsergebnis();
			repository.kurse();
			repository.schienen();

			dataErgebnisse.verify(() -> DataGostBlockungsergebnisse.getErgebnisFromID(any(), anyLong()), times(1));
			dataBlockungsdaten.verify(() -> DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(any(), anyLong()), times(1));
		}
	}

	@Test
	void testEinFehlerAusserhalbDerFehlermerkungLaesstKeineHalbenObjekteZurueck() {
		// Das Feld trägt den Manager erst nach dem vollständigen Aufbau, das Kennzeichen hält den Versuch fest. Ein Fehler außerhalb des Catch hinterlässt
		// deshalb weder halbe Bestände noch einen zweiten Aufbau.
		final ReportingRepositoryGostKursplanung repository = new ReportingRepositoryGostKursplanung(reportingContext);
		final GostBlockungsergebnis blockungsergebnis = new GostBlockungsergebnis();
		blockungsergebnis.id = ID_BLOCKUNGSERGEBNIS;
		blockungsergebnis.blockungID = ID_BLOCKUNG;
		final GostBlockungsdatenManager datenManager = mock(GostBlockungsdatenManager.class);
		gebeLeereBlockungVor(datenManager);
		when(datenManager.schuelerGetListe()).thenThrow(new StackOverflowError("Fehlerinjektion"));

		try (MockedStatic<DataGostBlockungsergebnisse> dataErgebnisse = mockStatic(DataGostBlockungsergebnisse.class);
				MockedStatic<DataGostBlockungsdaten> dataBlockungsdaten = mockStatic(DataGostBlockungsdaten.class)) {
			dataErgebnisse.when(() -> DataGostBlockungsergebnisse.getErgebnisFromID(any(), anyLong())).thenReturn(blockungsergebnis);
			dataBlockungsdaten.when(() -> DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(any(), eq(ID_BLOCKUNG))).thenReturn(datenManager);

			assertThrows(StackOverflowError.class, repository::blockungsergebnis);

			final ApiOperationException zweiter = assertThrows(ApiOperationException.class, repository::kurse,
					"Der nächste Getter meldet den Abbruch, statt halbe Objekte auszugeben oder erneut aufzubauen.");
			assertEquals(Status.INTERNAL_SERVER_ERROR, zweiter.getStatus());
		}
	}

}
