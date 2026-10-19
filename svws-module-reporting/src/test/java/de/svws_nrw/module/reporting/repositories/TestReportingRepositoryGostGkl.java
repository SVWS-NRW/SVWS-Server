package de.svws_nrw.module.reporting.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import de.svws_nrw.core.data.gost.GostSchuelerGKLWahl;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.service.gost.klausuren.GostKlausurenServiceFactory;
import de.svws_nrw.service.gost.klausuren.GostKlausurenServiceFactoryBuilder;
import de.svws_nrw.service.gost.klausuren.GostKlausurenVorgabeService;

/**
 * Prüft die GKL-Abfrage des GOSt-Repositorys. Eine Vorgabe zählt, wenn sie zum Abiturjahrgang gehört, Halbjahr und Fach passen und sie im Wahlfeld der
 * Phase des Halbjahres steht; ihre Kursart spielt keine Rolle. Die Vorgaben eines Abiturjahrgangs werden einmal gemeinsam geladen, auch für Schüler, die
 * erst später geladen werden.
 */
@DisplayName("Die GKL-Abfrage des GOSt-Repositorys")
class TestReportingRepositoryGostGkl {

	/** Ein Abiturjahrgang, für den es GKL gibt. */
	private static final int ABITURJAHR = 2030;

	/** Die ID des Faches der Belegung. */
	private static final long ID_FACH = 4L;

	/** Die ID eines anderen Faches. */
	private static final long ID_FACH_ANDERS = 5L;

	/** Der gemockte Context, den das Repository erhält. */
	private ReportingContext reportingContext;

	/** Die gemockte Datenbankverbindung, über die die GKL-Wahlen geladen werden. */
	private DBEntityManager conn;

	/** Der gemockte Service für die Klausurvorgaben. */
	private GostKlausurenVorgabeService service;

	/** Die IDs der Schüler, die das Schüler-Repository als geladen meldet. */
	private List<Long> idsGeladenerSchueler;

	/** Die Datensätze der Oberstufe je Schüler-ID, aus denen die GKL-Wahlen entstehen. */
	private Map<Long, DTOGostSchueler> gostSchueler;

	/** Die Klausurvorgaben, die der gemockte Service kennt. */
	private Map<Long, GostKlausurvorgabe> vorgaben;

	/** Die Abiturjahrgänge, zu denen der Service Klausurvorgaben geladen hat, in der Reihenfolge der Abfragen. */
	private List<Integer> abfragenVorgaben;

	/** Der statische Mock der Service-Factory für die Klausurvorgaben. */
	private MockedStatic<GostKlausurenServiceFactoryBuilder> factoryBuilder;

	/** Das geprüfte Repository. */
	private ReportingRepositoryGost repository;


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(new Logger());

		idsGeladenerSchueler = new ArrayList<>();
		final ReportingRepositorySchueler repositorySchueler = mock(ReportingRepositorySchueler.class);
		when(repositorySchueler.idsGeladenerSchueler()).thenAnswer(inv -> new ArrayList<>(idsGeladenerSchueler));
		when(reportingContext.repositorySchueler()).thenReturn(repositorySchueler);

		gostSchueler = new HashMap<>();
		conn = mock(DBEntityManager.class);
		when(conn.queryList(eq(DTOGostSchueler.QUERY_LIST_BY_SCHUELER_ID), eq(DTOGostSchueler.class), any())).thenAnswer(inv -> {
			final Collection<?> ids = inv.getArgument(2);
			return ids.stream().map(gostSchueler::get).filter(Objects::nonNull).toList();
		});
		when(reportingContext.conn()).thenReturn(conn);

		vorgaben = new HashMap<>();
		abfragenVorgaben = new ArrayList<>();
		service = mock(GostKlausurenVorgabeService.class);
		when(service.getListByAbiturjahr(anyInt(), anyInt(), anyBoolean())).thenAnswer(inv -> {
			final int abiturjahr = inv.getArgument(0);
			abfragenVorgaben.add(abiturjahr);
			return vorgaben.values().stream().filter(v -> v.abiturjahrgang == abiturjahr).toList();
		});
		final GostKlausurenServiceFactory factory = mock(GostKlausurenServiceFactory.class);
		when(factory.getGostKlausurenVorgabeService()).thenReturn(service);
		factoryBuilder = mockStatic(GostKlausurenServiceFactoryBuilder.class);
		factoryBuilder.when(GostKlausurenServiceFactoryBuilder::getGostKlausurenServiceFactory).thenReturn(factory);

		repository = new ReportingRepositoryGost(reportingContext);
	}

	@AfterEach
	void tearDown() {
		factoryBuilder.close();
	}


	/**
	 * Legt eine Klausurvorgabe an, die der gemockte Service kennt.
	 *
	 * @param id             Die ID der Vorgabe.
	 * @param abiturjahrgang Der Abiturjahrgang der Vorgabe.
	 * @param halbjahr       Das Halbjahr der Vorgabe.
	 * @param idFach         Die ID des Faches der Vorgabe.
	 * @param kursart        Die Kursart der Vorgabe.
	 */
	private void vorgabe(final long id, final int abiturjahrgang, final GostHalbjahr halbjahr, final long idFach, final GostKursart kursart) {
		final GostKlausurvorgabe vorgabe = new GostKlausurvorgabe();
		vorgabe.id = id;
		vorgabe.abiturjahrgang = abiturjahrgang;
		vorgabe.halbjahr = halbjahr.id;
		vorgabe.quartal = 1;
		vorgabe.idFach = idFach;
		vorgabe.kursart = kursart.kuerzel;
		vorgaben.put(id, vorgabe);
	}

	/**
	 * Legt für den Schüler einen Datensatz der Oberstufe an, der in der Q-Phase im gesellschaftswissenschaftlichen Bereich die übergebene Vorgabe wählt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 * @param idVorgabe  Die ID der gewählten Vorgabe oder null für keine Wahl.
	 */
	private void wahl(final long idSchueler, final Long idVorgabe) {
		final DTOGostSchueler dto = new DTOGostSchueler(idSchueler, false);
		dto.GKL_Q_AF2_Klausurvorgabe_ID = idVorgabe;
		gostSchueler.put(idSchueler, dto);
	}

	/**
	 * Legt für den Schüler einen Datensatz der Oberstufe an, der in der EF im gesellschaftswissenschaftlichen Bereich die übergebene Vorgabe wählt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 * @param idVorgabe  Die ID der gewählten Vorgabe.
	 */
	private void wahlEF(final long idSchueler, final long idVorgabe) {
		final DTOGostSchueler dto = new DTOGostSchueler(idSchueler, false);
		dto.GKL_EF_AF2_Klausurvorgabe_ID = idVorgabe;
		gostSchueler.put(idSchueler, dto);
	}


	@Test
	void testEinePassendeVorgabeErgibtEinenGkl() {
		vorgabe(1L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH, GostKursart.GK);
		wahl(10L, 1L);

		assertTrue(repository.hatGkl(10L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH));
	}

	@Test
	void testEineVorgabeZaehltNurBeiPassendemFachHalbjahrUndAbiturjahrgang() {
		vorgabe(1L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH, GostKursart.GK);
		wahl(10L, 1L);

		assertFalse(repository.hatGkl(10L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH_ANDERS), "anderes Fach");
		assertFalse(repository.hatGkl(10L, ABITURJAHR, GostHalbjahr.Q12, ID_FACH), "anderes Halbjahr");
		assertFalse(repository.hatGkl(10L, ABITURJAHR + 1, GostHalbjahr.Q11, ID_FACH), "anderer Abiturjahrgang");
	}

	@Test
	void testDieKursartDerVorgabeSpieltKeineRolle() {
		vorgabe(1L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH, GostKursart.LK);
		wahl(10L, 1L);

		assertTrue(repository.hatGkl(10L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH));
	}

	@Test
	void testEineVorgabeZaehltNurImWahlfeldIhrerPhase() {
		vorgabe(1L, ABITURJAHR, GostHalbjahr.EF1, ID_FACH, GostKursart.GK);
		vorgabe(2L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH, GostKursart.GK);
		wahlEF(10L, 1L);
		wahlEF(11L, 2L);
		wahl(12L, 1L);
		wahl(13L, 2L);

		assertTrue(repository.hatGkl(10L, ABITURJAHR, GostHalbjahr.EF1, ID_FACH), "EF-Vorgabe im EF-Wahlfeld");
		assertFalse(repository.hatGkl(11L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH), "Q-Vorgabe im EF-Wahlfeld");
		assertFalse(repository.hatGkl(12L, ABITURJAHR, GostHalbjahr.EF1, ID_FACH), "EF-Vorgabe im Q-Wahlfeld");
		assertTrue(repository.hatGkl(13L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH), "Q-Vorgabe im Q-Wahlfeld");
	}

	@Test
	void testOhneWahlGibtEsKeinenGklUndKeineAbfrageDerVorgaben() {
		wahl(10L, null);

		assertFalse(repository.hatGkl(10L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH), "Wahl ohne gesetzte Vorgabe");
		assertFalse(repository.hatGkl(11L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH), "Schüler ohne Datensatz der Oberstufe");
		assertTrue(abfragenVorgaben.isEmpty());
	}

	@Test
	void testVorDemAbiturjahrgang2030GibtEsKeinenGklUndKeinenDatenzugriff() {
		vorgabe(1L, 2029, GostHalbjahr.Q11, ID_FACH, GostKursart.GK);
		wahl(10L, 1L);

		assertFalse(repository.hatGkl(10L, 2029, GostHalbjahr.Q11, ID_FACH));
		verifyNoInteractions(conn);
		assertTrue(abfragenVorgaben.isEmpty());
	}

	@Test
	void testEineNichtLadbareWahlErgibtKeinenGklUndWirdGemeldet() {
		doThrow(new IllegalStateException("Fehlerinjektion")).when(conn)
				.queryList(eq(DTOGostSchueler.QUERY_LIST_BY_SCHUELER_ID), eq(DTOGostSchueler.class), any());

		assertFalse(repository.hatGkl(10L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH));
		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(GostSchuelerGKLWahl.class, 10L)), anyString(), any());
	}

	@Test
	void testNichtLadbareVorgabenDesJahrgangsErgebenKeinenGklUndWerdenNichtErneutAngefragt() {
		vorgabe(1L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH, GostKursart.GK);
		wahl(10L, 1L);
		wahl(11L, 1L);
		idsGeladenerSchueler.addAll(List.of(10L, 11L));
		doThrow(new IllegalStateException("Fehlerinjektion")).when(service).getListByAbiturjahr(anyInt(), anyInt(), anyBoolean());

		assertFalse(repository.hatGkl(10L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH));
		assertFalse(repository.hatGkl(11L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH));

		verify(service, times(1)).getListByAbiturjahr(anyInt(), anyInt(), anyBoolean());
		// Der Schlüssel ist bei beiden Aufrufen derselbe; den Befund zählt der Problemsammler einmal, den der gemockte Context nicht besitzt.
		verify(reportingContext, times(2)).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(GostKlausurvorgabe.class)), anyString(), any());
	}

	@Test
	void testDieVorgabenEinesJahrgangsWerdenEinmalGeladenAuchFuerSpaeterGeladeneSchueler() {
		vorgabe(1L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH, GostKursart.GK);
		vorgabe(2L, ABITURJAHR, GostHalbjahr.Q12, ID_FACH_ANDERS, GostKursart.GK);
		wahl(10L, 1L);
		wahl(11L, 2L);
		idsGeladenerSchueler.add(10L);

		assertTrue(repository.hatGkl(10L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH));
		idsGeladenerSchueler.add(11L);
		assertTrue(repository.hatGkl(11L, ABITURJAHR, GostHalbjahr.Q12, ID_FACH_ANDERS));

		assertEquals(List.of(ABITURJAHR), abfragenVorgaben);
	}

	@Test
	void testJederAbiturjahrgangWirdEinmalGeladen() {
		vorgabe(1L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH, GostKursart.GK);
		vorgabe(2L, ABITURJAHR + 1, GostHalbjahr.Q11, ID_FACH, GostKursart.GK);
		wahl(10L, 1L);
		wahl(11L, 2L);
		idsGeladenerSchueler.addAll(List.of(10L, 11L));

		assertTrue(repository.hatGkl(10L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH));
		assertTrue(repository.hatGkl(11L, ABITURJAHR + 1, GostHalbjahr.Q11, ID_FACH));
		assertFalse(repository.hatGkl(10L, ABITURJAHR + 1, GostHalbjahr.Q11, ID_FACH), "Vorgabe 1 gehört nicht zum Jahrgang " + (ABITURJAHR + 1));

		assertEquals(List.of(ABITURJAHR, ABITURJAHR + 1), abfragenVorgaben);
	}

}
