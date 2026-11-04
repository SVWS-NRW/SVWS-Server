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
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.service.gost.GostSchuelerService;
import de.svws_nrw.service.gost.GostServiceFactory;
import de.svws_nrw.service.gost.GostServiceFactoryBuilder;
import de.svws_nrw.service.gost.klausuren.GostKlausurenServiceFactory;
import de.svws_nrw.service.gost.klausuren.GostKlausurenServiceFactoryBuilder;
import de.svws_nrw.service.gost.klausuren.GostKlausurenVorgabeService;

/**
 * Prüft die GKL-Abfrage des GOSt-Repositorys. Eine Vorgabe zählt, wenn sie zum Abiturjahrgang gehört, Halbjahr und Fach passen und sie im Wahlfeld der
 * Phase des Halbjahres steht; ihre Kursart spielt keine Rolle. Die Vorgaben eines Abiturjahrgangs werden einmal gemeinsam geladen, auch für Schüler, die
 * erst später geladen werden. Die GKL-Wahlen gelten je Abiturjahrgang, weil ein Wiederholer im neuen Jahrgang eigene Wahlen hat.
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

	/** Der gemockte Schüler-Service, über den die GKL-Wahlen geladen werden. */
	private GostSchuelerService schuelerService;

	/** Der gemockte Service für die Klausurvorgaben. */
	private GostKlausurenVorgabeService service;

	/** Die IDs der Schüler, die das Schüler-Repository als geladen meldet. */
	private List<Long> idsGeladenerSchueler;

	/** Die GKL-Wahlen je Schüler-ID, die der gemockte Schüler-Service kennt. */
	private Map<Long, GostSchuelerGKLWahl> wahlen;

	/** Die GKL-Wahlen der Wiederholung je Schüler-ID. */
	private Map<Long, GostSchuelerGKLWahl> wahlenWiederholung;

	/** Der Abiturjahrgang der Wiederholung je Schüler-ID. */
	private Map<Long, Integer> jahrgaengeWiederholung;

	/** Die Abiturjahrgänge, zu denen der Service GKL-Wahlen geladen hat, in der Reihenfolge der Abfragen. */
	private List<Integer> abfragenWahlen;

	/** Die Klausurvorgaben, die der gemockte Service kennt. */
	private Map<Long, GostKlausurvorgabe> vorgaben;

	/** Die Abiturjahrgänge, zu denen der Service Klausurvorgaben geladen hat, in der Reihenfolge der Abfragen. */
	private List<Integer> abfragenVorgaben;

	/** Der statische Mock der Service-Factory für die Klausurvorgaben. */
	private MockedStatic<GostKlausurenServiceFactoryBuilder> factoryBuilder;

	/** Der statische Mock der Service-Factory für den Schüler-Service. */
	private MockedStatic<GostServiceFactoryBuilder> serviceFactoryBuilder;

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

		wahlen = new HashMap<>();
		wahlenWiederholung = new HashMap<>();
		jahrgaengeWiederholung = new HashMap<>();
		abfragenWahlen = new ArrayList<>();
		schuelerService = mock(GostSchuelerService.class);
		// Wie der Service: Die Wahlen der Wiederholung gelten im Jahrgang der Wiederholung, sonst die Wahlen aus Gost_Schueler.
		when(schuelerService.getMapGKLWahlen(any(), any())).thenAnswer(inv -> {
			final Collection<Long> ids = inv.getArgument(0);
			final Integer abiturjahrgang = inv.getArgument(1);
			abfragenWahlen.add(abiturjahrgang);
			final Map<Long, GostSchuelerGKLWahl> result = new HashMap<>();
			for (final Long id : ids) {
				final Integer jahrgangWiederholung = jahrgaengeWiederholung.get(id);
				final boolean wiederholungGilt = (jahrgangWiederholung != null) && ((abiturjahrgang == null) || abiturjahrgang.equals(jahrgangWiederholung));
				result.put(id, wiederholungGilt ? wahlenWiederholung.get(id) : wahlen.getOrDefault(id, new GostSchuelerGKLWahl()));
			}
			return result;
		});
		final GostServiceFactory serviceFactory = mock(GostServiceFactory.class);
		when(serviceFactory.getGostSchuelerService()).thenReturn(schuelerService);
		serviceFactoryBuilder = mockStatic(GostServiceFactoryBuilder.class);
		serviceFactoryBuilder.when(GostServiceFactoryBuilder::getGostServiceFactory).thenReturn(serviceFactory);

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
		serviceFactoryBuilder.close();
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
	 * Legt für den Schüler GKL-Wahlen an, die in der Q-Phase im gesellschaftswissenschaftlichen Bereich die übergebene Vorgabe wählen.
	 *
	 * @param idSchueler Die ID des Schülers.
	 * @param idVorgabe  Die ID der gewählten Vorgabe oder null für keine Wahl.
	 */
	private void wahl(final long idSchueler, final Long idVorgabe) {
		final GostSchuelerGKLWahl wahl = new GostSchuelerGKLWahl();
		wahl.idSchueler = idSchueler;
		wahl.idKlausurvorgabeQ_GW = idVorgabe;
		wahlen.put(idSchueler, wahl);
	}

	/**
	 * Legt für den Schüler GKL-Wahlen an, die in der EF im gesellschaftswissenschaftlichen Bereich die übergebene Vorgabe wählen.
	 *
	 * @param idSchueler Die ID des Schülers.
	 * @param idVorgabe  Die ID der gewählten Vorgabe.
	 */
	private void wahlEF(final long idSchueler, final long idVorgabe) {
		final GostSchuelerGKLWahl wahl = new GostSchuelerGKLWahl();
		wahl.idSchueler = idSchueler;
		wahl.idKlausurvorgabeEF_GW = idVorgabe;
		wahlen.put(idSchueler, wahl);
	}

	/**
	 * Legt für den Schüler eine Wiederholung im übergebenen Abiturjahrgang an, deren GKL-Wahlen in der Q-Phase im gesellschaftswissenschaftlichen Bereich
	 * die übergebene Vorgabe wählen.
	 *
	 * @param idSchueler     Die ID des Schülers.
	 * @param abiturjahrgang Der Abiturjahrgang der Wiederholung.
	 * @param idVorgabe      Die ID der gewählten Vorgabe oder null für leere Wahlen.
	 */
	private void wiederholung(final long idSchueler, final int abiturjahrgang, final Long idVorgabe) {
		final GostSchuelerGKLWahl wahl = new GostSchuelerGKLWahl();
		wahl.idSchueler = idSchueler;
		wahl.idKlausurvorgabeQ_GW = idVorgabe;
		wahlenWiederholung.put(idSchueler, wahl);
		jahrgaengeWiederholung.put(idSchueler, abiturjahrgang);
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
		assertFalse(repository.hatGkl(11L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH), "Schüler ohne GKL-Wahlen");
		assertTrue(abfragenVorgaben.isEmpty());
	}

	@Test
	void testVorDemAbiturjahrgang2030GibtEsKeinenGklUndKeinenDatenzugriff() {
		vorgabe(1L, 2029, GostHalbjahr.Q11, ID_FACH, GostKursart.GK);
		wahl(10L, 1L);

		assertFalse(repository.hatGkl(10L, 2029, GostHalbjahr.Q11, ID_FACH));
		verifyNoInteractions(schuelerService);
		assertTrue(abfragenVorgaben.isEmpty());
	}

	@Test
	void testEineNichtLadbareWahlErgibtKeinenGklUndWirdGemeldet() {
		doThrow(new IllegalStateException("Fehlerinjektion")).when(schuelerService).getMapGKLWahlen(any(), any());

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

	@Test
	void testEinWiederholerHatJeAbiturjahrgangEigeneWahlen() {
		vorgabe(1L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH, GostKursart.GK);
		vorgabe(2L, ABITURJAHR + 1, GostHalbjahr.Q11, ID_FACH, GostKursart.GK);
		wahl(10L, 1L);
		wiederholung(10L, ABITURJAHR + 1, 2L);

		assertTrue(repository.hatGkl(10L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH), "alter Jahrgang: Wahl aus Gost_Schueler");
		assertTrue(repository.hatGkl(10L, ABITURJAHR + 1, GostHalbjahr.Q11, ID_FACH), "neuer Jahrgang: Wahl der Wiederholung");
		assertTrue(repository.hatGkl(10L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH), "erneuter Zugriff auf den alten Jahrgang");

		assertEquals(List.of(ABITURJAHR, ABITURJAHR + 1), abfragenWahlen);
	}

	@Test
	void testLeereWahlenDerWiederholungErgebenKeinenGkl() {
		vorgabe(2L, ABITURJAHR + 1, GostHalbjahr.Q11, ID_FACH, GostKursart.GK);
		wahl(10L, 2L);
		wiederholung(10L, ABITURJAHR + 1, null);

		assertFalse(repository.hatGkl(10L, ABITURJAHR + 1, GostHalbjahr.Q11, ID_FACH));
	}

	@Test
	void testEinLadefehlerDerWahlenGiltNurFuerSeinenAbiturjahrgang() {
		vorgabe(2L, ABITURJAHR + 1, GostHalbjahr.Q11, ID_FACH, GostKursart.GK);
		wiederholung(10L, ABITURJAHR + 1, 2L);
		doThrow(new IllegalStateException("Fehlerinjektion")).when(schuelerService).getMapGKLWahlen(any(), eq(ABITURJAHR));

		assertFalse(repository.hatGkl(10L, ABITURJAHR, GostHalbjahr.Q11, ID_FACH));
		assertTrue(repository.hatGkl(10L, ABITURJAHR + 1, GostHalbjahr.Q11, ID_FACH));

		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(GostSchuelerGKLWahl.class, 10L)), anyString(), any());
	}

}
