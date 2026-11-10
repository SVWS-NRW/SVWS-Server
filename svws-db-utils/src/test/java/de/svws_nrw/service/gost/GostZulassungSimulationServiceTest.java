package de.svws_nrw.service.gost;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import de.svws_nrw.core.data.gost.GostZulassungSimulation;
import de.svws_nrw.core.data.gost.GostZulassungSimulationNote;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerWiederholung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.benutzer.BenutzerAllgemeinRepository;
import de.svws_nrw.repo.gost.GostSchuelerRepository;
import de.svws_nrw.repo.gost.GostSchuelerWiederholungRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft die Fehlerfälle beim Zugriff auf die Simulation der Abiturzulassung, dass vollzogene Wechsel vor dem Speichern übernommen werden und dass der Stand
 * derselben Weiche folgt wie die GKL-Wahlen: In der aktuellen Planung gehört er zu den Schülerdaten der Oberstufe, außerhalb davon bei einer bestehenden
 * Wiederholung zu dieser.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Tests für den Service zur Simulation der Abiturzulassung")
class GostZulassungSimulationServiceTest {

	private static final long ID_SCHUELER = 1L;

	@Mock
	private BenutzerAllgemeinRepository benutzerRepository;

	@Mock
	private SchuelerRepository schuelerRepository;

	@Mock
	private GostSchuelerRepository gostSchuelerRepository;

	@Mock
	private GostSchuelerWiederholungRepository gostSchuelerWiederholungRepository;

	@Mock
	private GostSchuelerService gostSchuelerService;

	private GostZulassungSimulationService service;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeEach
	void setUp() {
		service = erzeugeService(false);
		transactionSupportMock = mockStatic(TransactionSupport.class);
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.any(Runnable.class)))
				.thenAnswer(invocation -> {
					invocation.getArgument(0, Runnable.class).run();
					return null;
				});
		mockSchule(true);
		when(schuelerRepository.findById(ID_SCHUELER)).thenReturn(Optional.of(mock(DTOSchueler.class)));
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}

	private GostZulassungSimulationService erzeugeService(final boolean aktuellePlanung) {
		return new GostZulassungSimulationService(benutzerRepository, schuelerRepository, gostSchuelerRepository, gostSchuelerWiederholungRepository,
				gostSchuelerService, aktuellePlanung);
	}

	private static String erzeugeJson(final int notenpunkte) {
		return "{\"noten\":[{\"idFach\":42,\"halbjahr\":4,\"notenpunkte\":" + notenpunkte + "}],"
				+ "\"abiturfaecher\":[],\"besondereLernleistung\":\"K\",\"besondereLernleistungNotenpunkte\":null}";
	}

	private static GostZulassungSimulation erzeugeSimulation(final int notenpunkte) {
		final GostZulassungSimulation simulation = new GostZulassungSimulation();
		final GostZulassungSimulationNote note = new GostZulassungSimulationNote();
		note.idFach = 42;
		note.halbjahr = GostHalbjahr.Q21.id;
		note.notenpunkte = notenpunkte;
		simulation.noten.add(note);
		return simulation;
	}

	private static DTOGostSchueler erzeugeGostSchueler(final String json) {
		final DTOGostSchueler gostSchueler = new DTOGostSchueler(ID_SCHUELER, false);
		gostSchueler.ZulassungSimulation = json;
		return gostSchueler;
	}

	private static DTOGostSchuelerWiederholung erzeugeWiederholung(final String json) {
		final DTOGostSchuelerWiederholung wiederholung = new DTOGostSchuelerWiederholung(ID_SCHUELER, 2029, GostHalbjahr.Q11);
		wiederholung.ZulassungSimulation = json;
		return wiederholung;
	}

	private static void pruefeLeer(final GostZulassungSimulation simulation) {
		assertThat(simulation.noten).isEmpty();
		assertThat(simulation.abiturfaecher).isEmpty();
		assertThat(simulation.besondereLernleistung).isEqualTo("K");
	}

	private void mockSchule(final boolean hatGymOb) {
		final Benutzer benutzer = mock(Benutzer.class);
		when(benutzer.schuleHatGymOb()).thenReturn(hatGymOb);
		when(benutzerRepository.getAktuellerBenutzer()).thenReturn(benutzer);
	}

	@Test
	@DisplayName("get: Ein unbekannter Schüler führt zu 404")
	void testGetUnbekannt() {
		when(schuelerRepository.findById(ID_SCHUELER)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.get(ID_SCHUELER)).isInstanceOfSatisfying(ApiOperationException.class,
				e -> assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND));
	}

	@Test
	@DisplayName("get: Eine Schule ohne gymnasiale Oberstufe führt zu 400")
	void testGetOhneGymOb() {
		mockSchule(false);

		assertThatThrownBy(() -> service.get(ID_SCHUELER)).isInstanceOfSatisfying(ApiOperationException.class,
				e -> assertThat(e.getStatus()).isEqualTo(Status.BAD_REQUEST));
	}

	@Test
	@DisplayName("put: Ein vollzogener Wechsel wird zuerst übernommen, danach wird der Stand geschrieben")
	void testPutUebernimmtZuerst() {
		final GostZulassungSimulation simulation = new GostZulassungSimulation();

		service.put(ID_SCHUELER, simulation);

		final InOrder reihenfolge = inOrder(gostSchuelerService, gostSchuelerRepository);
		reihenfolge.verify(gostSchuelerService).uebernehmeWiederholungen(List.of(ID_SCHUELER));
		reihenfolge.verify(gostSchuelerRepository).update(any(DTOGostSchueler.class));
	}

	@Test
	@DisplayName("put: Ist die aktuelle Planung nach dem Wechsel gesperrt (409), so wird nichts geschrieben")
	void testPutNachWechselGesperrt() {
		doThrow(new ApiOperationException(Status.CONFLICT, "bereits im neuen Jahrgang")).when(gostSchuelerService).uebernehmeWiederholungen(List.of(ID_SCHUELER));

		assertThatThrownBy(() -> service.put(ID_SCHUELER, new GostZulassungSimulation())).isInstanceOfSatisfying(ApiOperationException.class,
				e -> assertThat(e.getStatus()).isEqualTo(Status.CONFLICT));
		verify(gostSchuelerRepository, never()).update(any(DTOGostSchueler.class));
	}

	@Test
	@DisplayName("put: Ein unbekannter Schüler führt zu 404 und es wird nichts geschrieben")
	void testPutUnbekannt() {
		when(schuelerRepository.findById(ID_SCHUELER)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.put(ID_SCHUELER, new GostZulassungSimulation())).isInstanceOfSatisfying(ApiOperationException.class,
				e -> assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND));
		verify(gostSchuelerRepository, never()).update(any(DTOGostSchueler.class));
	}

	@Test
	@DisplayName("get: Ohne Eintrag zum Schüler ist der Stand leer")
	void testGetOhneEintrag() {
		when(gostSchuelerWiederholungRepository.findById(ID_SCHUELER)).thenReturn(Optional.empty());
		when(gostSchuelerRepository.findById(ID_SCHUELER)).thenReturn(Optional.empty());

		pruefeLeer(service.get(ID_SCHUELER));
	}

	@Test
	@DisplayName("get: Ohne abgelegten Text ist der Stand leer")
	void testGetOhneText() {
		when(gostSchuelerWiederholungRepository.findById(ID_SCHUELER)).thenReturn(Optional.empty());
		when(gostSchuelerRepository.findById(ID_SCHUELER)).thenReturn(Optional.of(erzeugeGostSchueler(null)));

		pruefeLeer(service.get(ID_SCHUELER));
	}

	@Test
	@DisplayName("get: Ein abgelegtes JSON-null führt zu einem leeren Stand")
	void testGetJsonNull() {
		when(gostSchuelerWiederholungRepository.findById(ID_SCHUELER)).thenReturn(Optional.empty());
		when(gostSchuelerRepository.findById(ID_SCHUELER)).thenReturn(Optional.of(erzeugeGostSchueler("null")));

		pruefeLeer(service.get(ID_SCHUELER));
	}

	@Test
	@DisplayName("get: Beschädigter Text führt zu einem leeren Stand statt zu einem Fehler")
	void testGetBeschaedigt() {
		when(gostSchuelerWiederholungRepository.findById(ID_SCHUELER)).thenReturn(Optional.empty());
		when(gostSchuelerRepository.findById(ID_SCHUELER)).thenReturn(Optional.of(erzeugeGostSchueler("{kein json")));

		pruefeLeer(service.get(ID_SCHUELER));
	}

	@Test
	@DisplayName("get: Ohne Wiederholung gilt der Stand aus den Schülerdaten der Oberstufe")
	void testGetOhneWiederholung() {
		when(gostSchuelerWiederholungRepository.findById(ID_SCHUELER)).thenReturn(Optional.empty());
		when(gostSchuelerRepository.findById(ID_SCHUELER)).thenReturn(Optional.of(erzeugeGostSchueler(erzeugeJson(7))));

		assertThat(service.get(ID_SCHUELER).noten.get(0).notenpunkte).isEqualTo(7);
	}

	@Test
	@DisplayName("get: Außerhalb der aktuellen Planung gilt der Stand der Wiederholung")
	void testGetWiederholung() {
		when(gostSchuelerWiederholungRepository.findById(ID_SCHUELER)).thenReturn(Optional.of(erzeugeWiederholung(erzeugeJson(9))));

		assertThat(service.get(ID_SCHUELER).noten.get(0).notenpunkte).isEqualTo(9);
		verifyNoInteractions(gostSchuelerRepository);
	}

	@Test
	@DisplayName("get: Eine neue Wiederholung startet leer, auch wenn die aktuelle Planung einen Stand hat")
	void testGetNeueWiederholung() {
		when(gostSchuelerWiederholungRepository.findById(ID_SCHUELER)).thenReturn(Optional.of(new DTOGostSchuelerWiederholung(ID_SCHUELER, 2029, GostHalbjahr.Q11)));

		pruefeLeer(service.get(ID_SCHUELER));
		verifyNoInteractions(gostSchuelerRepository);
	}

	@Test
	@DisplayName("get: In der aktuellen Planung gilt der Stand aus den Schülerdaten der Oberstufe")
	void testGetAktuellePlanung() {
		when(gostSchuelerRepository.findById(ID_SCHUELER)).thenReturn(Optional.of(erzeugeGostSchueler(erzeugeJson(7))));

		assertThat(erzeugeService(true).get(ID_SCHUELER).noten.get(0).notenpunkte).isEqualTo(7);
		verifyNoInteractions(gostSchuelerWiederholungRepository);
	}

	@Test
	@DisplayName("put: Ohne Wiederholung werden die Schülerdaten der Oberstufe geschrieben und das Geschriebene lässt sich wieder lesen")
	void testSetOhneWiederholung() {
		final DTOGostSchueler gostSchueler = erzeugeGostSchueler(erzeugeJson(1));
		when(gostSchuelerWiederholungRepository.findById(ID_SCHUELER)).thenReturn(Optional.empty());
		when(gostSchuelerRepository.findById(ID_SCHUELER)).thenReturn(Optional.of(gostSchueler));

		service.put(ID_SCHUELER, erzeugeSimulation(11));

		verify(gostSchuelerRepository).update(gostSchueler);
		assertThat(service.get(ID_SCHUELER).noten.get(0).notenpunkte).isEqualTo(11);
	}

	@Test
	@DisplayName("put: Fehlen die Schülerdaten der Oberstufe, so werden sie mit dem Stand angelegt")
	void testSetOhneGostSchueler() {
		when(gostSchuelerWiederholungRepository.findById(ID_SCHUELER)).thenReturn(Optional.empty());
		when(gostSchuelerRepository.findById(ID_SCHUELER)).thenReturn(Optional.empty());

		service.put(ID_SCHUELER, erzeugeSimulation(11));

		final ArgumentCaptor<DTOGostSchueler> captor = ArgumentCaptor.forClass(DTOGostSchueler.class);
		verify(gostSchuelerRepository).update(captor.capture());
		assertThat(captor.getValue().Schueler_ID).isEqualTo(ID_SCHUELER);
		assertThat(captor.getValue().ZulassungSimulation).contains("\"notenpunkte\":11");
	}

	@Test
	@DisplayName("put: Ein leerer Stand ersetzt den bisherigen Stand")
	void testSetLeer() {
		final DTOGostSchueler gostSchueler = erzeugeGostSchueler(erzeugeJson(1));
		when(gostSchuelerWiederholungRepository.findById(ID_SCHUELER)).thenReturn(Optional.empty());
		when(gostSchuelerRepository.findById(ID_SCHUELER)).thenReturn(Optional.of(gostSchueler));

		service.put(ID_SCHUELER, new GostZulassungSimulation());

		pruefeLeer(service.get(ID_SCHUELER));
	}

	@Test
	@DisplayName("put: Außerhalb der aktuellen Planung wird die Wiederholung geschrieben, die Schülerdaten der Oberstufe bleiben")
	void testSetWiederholung() {
		final DTOGostSchuelerWiederholung wiederholung = erzeugeWiederholung(erzeugeJson(1));
		when(gostSchuelerWiederholungRepository.findById(ID_SCHUELER)).thenReturn(Optional.of(wiederholung));

		service.put(ID_SCHUELER, erzeugeSimulation(11));

		verify(gostSchuelerWiederholungRepository).update(wiederholung);
		assertThat(wiederholung.ZulassungSimulation).contains("\"notenpunkte\":11");
		verify(gostSchuelerRepository, never()).update(any(DTOGostSchueler.class));
	}

	@Test
	@DisplayName("put: In der aktuellen Planung werden die Schülerdaten der Oberstufe geschrieben, die Wiederholung bleibt")
	void testSetAktuellePlanung() {
		final DTOGostSchueler gostSchueler = erzeugeGostSchueler(erzeugeJson(1));
		when(gostSchuelerRepository.findById(ID_SCHUELER)).thenReturn(Optional.of(gostSchueler));

		erzeugeService(true).put(ID_SCHUELER, erzeugeSimulation(11));

		verify(gostSchuelerRepository).update(gostSchueler);
		assertThat(gostSchueler.ZulassungSimulation).contains("\"notenpunkte\":11");
		verifyNoInteractions(gostSchuelerWiederholungRepository);
	}

}
