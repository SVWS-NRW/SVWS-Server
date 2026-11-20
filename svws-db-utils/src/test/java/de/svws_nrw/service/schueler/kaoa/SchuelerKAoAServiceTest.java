package de.svws_nrw.service.schueler.kaoa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerKAoADaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.kaoa.SchuelerKAoAMapper;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.kaoa.SchuelerKAoARepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests für den {@link SchuelerKAoAService}.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SchuelerKAoAServiceTest {

	private static final long ID_SCHUELER = 123L;
	private static final long ID_ANDERER_SCHUELER = 456L;
	private static final long ID_ABSCHNITT = 7L;
	private static final long ID_LERNABSCHNITT = 70L;
	private static final long ID_EINTRAG = 5L;
	private static final int SCHULJAHR = 2024;
	private static final int SCHULJAHR_ALT = 2020;
	private static final long ID_JAHRGANG = 8000000L;
	private static final long ID_JAHRGANG_IM_SCHULJAHR = 8000001L;

	@Mock
	private SchuelerKAoARepository repository;

	@Mock
	private SchuelerRepository schuelerRepository;

	@Mock
	private SchuelerLernabschnittRepository lernabschnittRepository;

	@Mock
	private SchuljahresabschnittService schuljahresabschnittService;

	private SchuelerKAoAService service;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		service = new SchuelerKAoAService(repository, schuelerRepository, lernabschnittRepository, schuljahresabschnittService,
				SchuelerKAoAMapper.INSTANCE);
		transactionSupportMock = mockStatic(TransactionSupport.class, withSettings().lenient());
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Runnable>any()))
				.thenAnswer(invocation -> {
					invocation.getArgument(0, Runnable.class).run();
					return null;
				});
		when(schuelerRepository.existsById(ID_SCHUELER)).thenReturn(true);
		when(schuljahresabschnittService.getById(ID_ABSCHNITT)).thenReturn(abschnitt(ID_ABSCHNITT));
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}

	private static Schuljahresabschnitt abschnitt(final long id) {
		return abschnitt(id, SCHULJAHR);
	}

	private static Schuljahresabschnitt abschnitt(final long id, final int schuljahr) {
		final var abschnitt = new Schuljahresabschnitt();
		abschnitt.id = id;
		abschnitt.schuljahr = schuljahr;
		return abschnitt;
	}

	private static DTOSchuelerLernabschnittsdaten lernabschnitt(final long id, final long idSchueler, final long idAbschnitt, final int wechselNr) {
		final var lernabschnitt = new DTOSchuelerLernabschnittsdaten(id, idSchueler, idAbschnitt, false, false, 1L);
		lernabschnitt.WechselNr = wechselNr;
		return lernabschnitt;
	}

	private static String asdJahrgang(final long idJahrgang) {
		return Jahrgaenge.data().getEintragByID(idJahrgang).schluessel;
	}

	// Ein Eintrag mit Bemerkung (Kategorie 10, Merkmal 51, Zusatzmerkmal 68) im Lernabschnitt des Schülers.
	private static DTOSchuelerKAoADaten eintrag(final long id) {
		final var entity = new DTOSchuelerKAoADaten(id, ID_LERNABSCHNITT, 10L);
		entity.jahrgang = asdJahrgang(ID_JAHRGANG);
		entity.idMerkmal = 51L;
		entity.idZusatzmerkmal = 68L;
		entity.bemerkung = "alt";
		return entity;
	}

	private static SchuelerKAoACreateRequest createRequest() {
		final var request = new SchuelerKAoACreateRequest();
		request.idSchuljahresabschnitt = ID_ABSCHNITT;
		request.idJahrgang = ID_JAHRGANG;
		request.idKategorie = 10L;
		request.idMerkmal = 51L;
		request.idZusatzmerkmal = 68L;
		request.bemerkung = "neu";
		return request;
	}

	private void givenEintragImLernabschnittDesSchuelers(final DTOSchuelerKAoADaten entity) {
		when(repository.findById(entity.id)).thenReturn(Optional.of(entity));
		when(lernabschnittRepository.findById(ID_LERNABSCHNITT)).thenReturn(Optional.of(lernabschnitt(ID_LERNABSCHNITT, ID_SCHUELER, ID_ABSCHNITT, 0)));
	}

	// -------------------------------------------------------------------------
	// getListByIdSchueler
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getListByIdSchueler | Unbekannter Schüler führt zu 404")
	void getListByIdSchueler_unbekannterSchueler() {
		assertThatThrownBy(() -> service.getListByIdSchueler(ID_ANDERER_SCHUELER))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getListByIdSchueler | Ohne KAoA-Daten wird eine leere Liste geliefert")
	void getListByIdSchueler_keineEintraege() {
		when(lernabschnittRepository.findListBySchuelerIDs(List.of(ID_SCHUELER)))
				.thenReturn(List.of(lernabschnitt(ID_LERNABSCHNITT, ID_SCHUELER, ID_ABSCHNITT, 0)));
		when(repository.findListByLernabschnitte(any())).thenReturn(List.of());

		assertThat(service.getListByIdSchueler(ID_SCHUELER)).isEmpty();
		verify(schuljahresabschnittService, never()).getList();
	}

	@Test
	@DisplayName("getListByIdSchueler | Mappt die Einträge mit Schuljahresabschnitt und Jahrgang und sortiert nach ID")
	void getListByIdSchueler_mapptUndSortiert() {
		final var lernabschnitt = lernabschnitt(ID_LERNABSCHNITT, ID_SCHUELER, ID_ABSCHNITT, 0);
		final var wechsel = lernabschnitt(ID_LERNABSCHNITT + 1, ID_SCHUELER, ID_ABSCHNITT, 1);
		final var zweiter = eintrag(9L);
		zweiter.idLernabschnitt = wechsel.ID;
		when(lernabschnittRepository.findListBySchuelerIDs(List.of(ID_SCHUELER))).thenReturn(List.of(lernabschnitt, wechsel));
		when(repository.findListByLernabschnitte(any())).thenReturn(List.of(zweiter, eintrag(ID_EINTRAG)));
		when(schuljahresabschnittService.getList()).thenReturn(List.of(abschnitt(ID_ABSCHNITT)));

		final var result = service.getListByIdSchueler(ID_SCHUELER);

		assertThat(result)
				.allSatisfy(k -> assertThat(k)
						.hasFieldOrPropertyWithValue("idSchuljahresabschnitt", ID_ABSCHNITT)
						.hasFieldOrPropertyWithValue("idJahrgang", ID_JAHRGANG_IM_SCHULJAHR)
						.hasFieldOrPropertyWithValue("idKategorie", 10L))
				.extracting(k -> k.id)
				.containsExactly(ID_EINTRAG, 9L);
	}

	@Test
	@DisplayName("getListByIdSchueler | Mehrere Lernabschnitte in verschiedenen Schuljahren werden gebündelt geladen (kein N+1)")
	void getListByIdSchueler_mehrereLernabschnitteOhneNPlusEins() {
		final var idLernabschnittAlt = ID_LERNABSCHNITT + 1;
		final var idAbschnittAlt = ID_ABSCHNITT + 1;
		final var alterEintrag = eintrag(9L);
		alterEintrag.idLernabschnitt = idLernabschnittAlt;
		when(lernabschnittRepository.findListBySchuelerIDs(List.of(ID_SCHUELER))).thenReturn(List.of(
				lernabschnitt(ID_LERNABSCHNITT, ID_SCHUELER, ID_ABSCHNITT, 0),
				lernabschnitt(idLernabschnittAlt, ID_SCHUELER, idAbschnittAlt, 0)));
		when(repository.findListByLernabschnitte(any())).thenReturn(List.of(eintrag(ID_EINTRAG), alterEintrag));
		when(schuljahresabschnittService.getList()).thenReturn(List.of(abschnitt(ID_ABSCHNITT), abschnitt(idAbschnittAlt, SCHULJAHR_ALT)));

		final var result = service.getListByIdSchueler(ID_SCHUELER);

		assertThat(result)
				.extracting(k -> k.idSchuljahresabschnitt, k -> k.idJahrgang)
				.containsExactly(tuple(ID_ABSCHNITT, ID_JAHRGANG_IM_SCHULJAHR), tuple(idAbschnittAlt, ID_JAHRGANG));
		verify(schuljahresabschnittService, times(1)).getList();
		verify(lernabschnittRepository, times(1)).findListBySchuelerIDs(any());
		verify(repository, times(1)).findListByLernabschnitte(any());
	}

	@Test
	@DisplayName("getListByIdSchueler | Unbekannter Schuljahresabschnitt eines Lernabschnitts führt zu 404")
	void getListByIdSchueler_unbekannterAbschnitt() {
		when(lernabschnittRepository.findListBySchuelerIDs(List.of(ID_SCHUELER)))
				.thenReturn(List.of(lernabschnitt(ID_LERNABSCHNITT, ID_SCHUELER, ID_ABSCHNITT, 0)));
		when(repository.findListByLernabschnitte(any())).thenReturn(List.of(eintrag(ID_EINTRAG)));
		when(schuljahresabschnittService.getList()).thenReturn(List.of(abschnitt(ID_ABSCHNITT + 1)));

		assertThatThrownBy(() -> service.getListByIdSchueler(ID_SCHUELER))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND)
				.hasMessageContaining("Kein Schuljahresabschnitt");
	}

	@Test
	@DisplayName("getListByIdSchueler | Ein Eintrag ohne Jahrgang führt zu 400")
	void getListByIdSchueler_jahrgangFehlt() {
		final var ohneJahrgang = eintrag(ID_EINTRAG);
		ohneJahrgang.jahrgang = null;
		when(lernabschnittRepository.findListBySchuelerIDs(List.of(ID_SCHUELER)))
				.thenReturn(List.of(lernabschnitt(ID_LERNABSCHNITT, ID_SCHUELER, ID_ABSCHNITT, 0)));
		when(repository.findListByLernabschnitte(any())).thenReturn(List.of(ohneJahrgang));
		when(schuljahresabschnittService.getList()).thenReturn(List.of(abschnitt(ID_ABSCHNITT)));

		assertThatThrownBy(() -> service.getListByIdSchueler(ID_SCHUELER))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getListByIdSchueler | Ein unbekannter Jahrgang-Schlüssel führt zu 404")
	void getListByIdSchueler_jahrgangUnbekannt() {
		final var unbekannterJahrgang = eintrag(ID_EINTRAG);
		unbekannterJahrgang.jahrgang = "ZZ";
		when(lernabschnittRepository.findListBySchuelerIDs(List.of(ID_SCHUELER)))
				.thenReturn(List.of(lernabschnitt(ID_LERNABSCHNITT, ID_SCHUELER, ID_ABSCHNITT, 0)));
		when(repository.findListByLernabschnitte(any())).thenReturn(List.of(unbekannterJahrgang));
		when(schuljahresabschnittService.getList()).thenReturn(List.of(abschnitt(ID_ABSCHNITT)));

		assertThatThrownBy(() -> service.getListByIdSchueler(ID_SCHUELER))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("create | Legt den Eintrag im Lernabschnitt (WechselNr 0) des Schuljahresabschnitts an")
	void create_success() {
		when(lernabschnittRepository.findListBySchuelerAndSchuljahresabschnitt(ID_SCHUELER, ID_ABSCHNITT))
				.thenReturn(List.of(lernabschnitt(71L, ID_SCHUELER, ID_ABSCHNITT, 1), lernabschnitt(ID_LERNABSCHNITT, ID_SCHUELER, ID_ABSCHNITT, 0)));

		final var result = service.create(ID_SCHUELER, createRequest());

		final var captor = ArgumentCaptor.forClass(DTOSchuelerKAoADaten.class);
		verify(repository).create(captor.capture());
		assertThat(captor.getValue())
				.hasFieldOrPropertyWithValue("idLernabschnitt", ID_LERNABSCHNITT)
				.hasFieldOrPropertyWithValue("jahrgang", asdJahrgang(ID_JAHRGANG))
				.hasFieldOrPropertyWithValue("idKategorie", 10L)
				.hasFieldOrPropertyWithValue("idMerkmal", 51L)
				.hasFieldOrPropertyWithValue("idZusatzmerkmal", 68L)
				.hasFieldOrPropertyWithValue("bemerkung", "neu");
		assertThat(result)
				.hasFieldOrPropertyWithValue("idSchuljahresabschnitt", ID_ABSCHNITT)
				.hasFieldOrPropertyWithValue("idJahrgang", ID_JAHRGANG_IM_SCHULJAHR)
				.hasFieldOrPropertyWithValue("bemerkung", "neu");
	}

	@Test
	@DisplayName("create | Unbekannter Schüler führt zu 404 und legt nichts an")
	void create_unbekannterSchueler() {
		final var request = createRequest();
		assertThatThrownBy(() -> service.create(ID_ANDERER_SCHUELER, request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
		verify(repository, never()).create(any(DTOSchuelerKAoADaten.class));
	}

	@Test
	@DisplayName("create | Ohne Lernabschnitt mit WechselNr 0 im Schuljahresabschnitt wird 404 geliefert")
	void create_keinAktuellerLernabschnitt() {
		when(lernabschnittRepository.findListBySchuelerAndSchuljahresabschnitt(ID_SCHUELER, ID_ABSCHNITT))
				.thenReturn(List.of(lernabschnitt(71L, ID_SCHUELER, ID_ABSCHNITT, 1)));

		final var request = createRequest();
		assertThatThrownBy(() -> service.create(ID_SCHUELER, request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND)
				.hasMessageContaining("Lernabschnittsdaten");
		verify(repository, never()).create(any(DTOSchuelerKAoADaten.class));
	}

	@Test
	@DisplayName("create | Daten, die nicht zu den Katalogen passen, führen zu 400 und werden nicht gespeichert")
	void create_ungueltigeDaten() {
		when(lernabschnittRepository.findListBySchuelerAndSchuljahresabschnitt(ID_SCHUELER, ID_ABSCHNITT))
				.thenReturn(List.of(lernabschnitt(ID_LERNABSCHNITT, ID_SCHUELER, ID_ABSCHNITT, 0)));
		final var request = createRequest();
		request.idEbene4 = 1L;

		assertThatThrownBy(() -> service.create(ID_SCHUELER, request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
		verify(repository, never()).create(any(DTOSchuelerKAoADaten.class));
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch | Unbekannter Eintrag führt zu 404")
	void patch_unbekannterEintrag() {
		when(repository.findById(ID_EINTRAG)).thenReturn(Optional.empty());

		final var request = new SchuelerKAoAPatchRequest();
		assertThatThrownBy(() -> service.patch(ID_SCHUELER, ID_EINTRAG, request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch | Ein Eintrag eines anderen Schülers wird nicht geändert (404)")
	void patch_eintragEinesAnderenSchuelers() {
		final var entity = eintrag(ID_EINTRAG);
		when(repository.findById(ID_EINTRAG)).thenReturn(Optional.of(entity));
		when(lernabschnittRepository.findById(ID_LERNABSCHNITT))
				.thenReturn(Optional.of(lernabschnitt(ID_LERNABSCHNITT, ID_ANDERER_SCHUELER, ID_ABSCHNITT, 0)));
		final var request = new SchuelerKAoAPatchRequest();
		request.bemerkung = JsonNullable.of("fremd");

		assertThatThrownBy(() -> service.patch(ID_SCHUELER, ID_EINTRAG, request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
		assertThat(entity.bemerkung).isEqualTo("alt");
	}

	@Test
	@DisplayName("patch | Ohne Lernabschnitt zum Eintrag wird 404 geliefert")
	void patch_lernabschnittFehlt() {
		when(repository.findById(ID_EINTRAG)).thenReturn(Optional.of(eintrag(ID_EINTRAG)));
		when(lernabschnittRepository.findById(ID_LERNABSCHNITT)).thenReturn(Optional.empty());

		final var request = new SchuelerKAoAPatchRequest();
		assertThatThrownBy(() -> service.patch(ID_SCHUELER, ID_EINTRAG, request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch | Ändert nur die gesetzten Felder")
	void patch_bemerkung() {
		final var entity = eintrag(ID_EINTRAG);
		givenEintragImLernabschnittDesSchuelers(entity);
		final var request = new SchuelerKAoAPatchRequest();
		request.bemerkung = JsonNullable.of("neu");

		final var result = service.patch(ID_SCHUELER, ID_EINTRAG, request);

		assertThat(entity)
				.hasFieldOrPropertyWithValue("bemerkung", "neu")
				.hasFieldOrPropertyWithValue("idLernabschnitt", ID_LERNABSCHNITT)
				.hasFieldOrPropertyWithValue("idKategorie", 10L)
				.hasFieldOrPropertyWithValue("idMerkmal", 51L)
				.hasFieldOrPropertyWithValue("idZusatzmerkmal", 68L);
		assertThat(result).hasFieldOrPropertyWithValue("bemerkung", "neu");
	}

	@Test
	@DisplayName("patch | Wechsel auf Ebene 4 setzt Merkmal, Zusatzmerkmal und Ebene 4 und leert die Bemerkung")
	void patch_wechselAufEbene4() {
		final var entity = eintrag(ID_EINTRAG);
		givenEintragImLernabschnittDesSchuelers(entity);
		final var request = new SchuelerKAoAPatchRequest();
		request.idMerkmal = JsonNullable.of(55L);
		request.idZusatzmerkmal = JsonNullable.of(80L);
		request.idEbene4 = JsonNullable.of(1L);
		request.bemerkung = JsonNullable.of(null);

		final var result = service.patch(ID_SCHUELER, ID_EINTRAG, request);

		assertThat(entity)
				.hasFieldOrPropertyWithValue("idMerkmal", 55L)
				.hasFieldOrPropertyWithValue("idZusatzmerkmal", 80L)
				.hasFieldOrPropertyWithValue("idEbene4", 1L)
				.hasFieldOrPropertyWithValue("bemerkung", null);
		assertThat(result)
				.hasFieldOrPropertyWithValue("idMerkmal", 55L)
				.hasFieldOrPropertyWithValue("idEbene4", 1L);
	}

	@Test
	@DisplayName("patch | Ein anderer Schuljahresabschnitt setzt den Lernabschnitt (WechselNr 0) dieses Abschnitts")
	void patch_anderenSchuljahresabschnitt() {
		final var entity = eintrag(ID_EINTRAG);
		givenEintragImLernabschnittDesSchuelers(entity);
		when(schuljahresabschnittService.getById(8L)).thenReturn(abschnitt(8L));
		when(lernabschnittRepository.findListBySchuelerAndSchuljahresabschnitt(ID_SCHUELER, 8L))
				.thenReturn(List.of(lernabschnitt(80L, ID_SCHUELER, 8L, 0)));
		final var request = new SchuelerKAoAPatchRequest();
		request.idSchuljahresabschnitt = JsonNullable.of(8L);

		final var result = service.patch(ID_SCHUELER, ID_EINTRAG, request);

		assertThat(entity).hasFieldOrPropertyWithValue("idLernabschnitt", 80L);
		assertThat(result).hasFieldOrPropertyWithValue("idSchuljahresabschnitt", 8L);
	}

	@Test
	@DisplayName("patch | Ein Abschnitt in einem anderen Schuljahr behält den Jahrgang-Schlüssel und liefert die Jahrgangs-ID dieses Schuljahres")
	void patch_abschnittAnderesSchuljahr() {
		final var entity = eintrag(ID_EINTRAG);
		givenEintragImLernabschnittDesSchuelers(entity);
		when(schuljahresabschnittService.getById(8L)).thenReturn(abschnitt(8L, SCHULJAHR_ALT));
		when(lernabschnittRepository.findListBySchuelerAndSchuljahresabschnitt(ID_SCHUELER, 8L))
				.thenReturn(List.of(lernabschnitt(80L, ID_SCHUELER, 8L, 0)));
		final var request = new SchuelerKAoAPatchRequest();
		request.idSchuljahresabschnitt = JsonNullable.of(8L);

		final var result = service.patch(ID_SCHUELER, ID_EINTRAG, request);

		assertThat(result)
				.hasFieldOrPropertyWithValue("idSchuljahresabschnitt", 8L)
				.hasFieldOrPropertyWithValue("idJahrgang", ID_JAHRGANG);
		assertThat(entity.jahrgang).isEqualTo(asdJahrgang(ID_JAHRGANG));
	}

	@Test
	@DisplayName("patch | Ein anderer Jahrgang setzt den Jahrgang-Schlüssel des Eintrags")
	void patch_anderenJahrgang() {
		final var entity = eintrag(ID_EINTRAG);
		givenEintragImLernabschnittDesSchuelers(entity);
		final var request = new SchuelerKAoAPatchRequest();
		request.idJahrgang = JsonNullable.of(ID_JAHRGANG);

		service.patch(ID_SCHUELER, ID_EINTRAG, request);

		assertThat(entity.jahrgang).isEqualTo(asdJahrgang(ID_JAHRGANG));
	}

	@Test
	@DisplayName("patch | Ein Patch, der zu mehreren gefüllten optionalen Feldern führt, wird mit 400 abgelehnt und ändert nichts")
	void patch_mehrereOptionaleFelder() {
		final var entity = eintrag(ID_EINTRAG);
		givenEintragImLernabschnittDesSchuelers(entity);
		final var request = new SchuelerKAoAPatchRequest();
		request.idEbene4 = JsonNullable.of(1L);

		assertThatThrownBy(() -> service.patch(ID_SCHUELER, ID_EINTRAG, request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
		assertThat(entity)
				.hasFieldOrPropertyWithValue("idEbene4", null)
				.hasFieldOrPropertyWithValue("bemerkung", "alt");
	}

	// -------------------------------------------------------------------------
	// delete
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("delete | Löscht den Eintrag des Schülers")
	void delete_success() {
		final var entity = eintrag(ID_EINTRAG);
		givenEintragImLernabschnittDesSchuelers(entity);

		final var response = service.delete(ID_SCHUELER, ID_EINTRAG);

		assertThat(response)
				.hasFieldOrPropertyWithValue("id", ID_EINTRAG)
				.hasFieldOrPropertyWithValue("success", true);
		verify(repository).delete(entity);
	}

	@Test
	@DisplayName("delete | Ein Eintrag eines anderen Schülers wird nicht gelöscht (404)")
	void delete_eintragEinesAnderenSchuelers() {
		final var entity = eintrag(ID_EINTRAG);
		givenEintragImLernabschnittDesSchuelers(entity);

		assertThatThrownBy(() -> service.delete(ID_ANDERER_SCHUELER, ID_EINTRAG))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
		verify(repository, never()).delete(any(DTOSchuelerKAoADaten.class));
	}

	@Test
	@DisplayName("delete | Ein unbekannter Eintrag führt zu 404")
	void delete_unbekannterEintrag() {
		when(repository.findById(ID_EINTRAG)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.delete(ID_SCHUELER, ID_EINTRAG))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
	}

}
