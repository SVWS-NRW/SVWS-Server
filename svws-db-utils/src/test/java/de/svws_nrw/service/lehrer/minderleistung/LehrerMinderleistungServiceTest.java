package de.svws_nrw.service.lehrer.minderleistung;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerMinderleistungsarten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerEntlastungsstunde;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.LehrerMinderleistungMapper;
import de.svws_nrw.repo.lehrer.minderleistung.LehrerMinderleistungRepository;
import de.svws_nrw.repo.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LehrerMinderleistungServiceTest {

	@Mock
	private LehrerMinderleistungRepository lehrerMinderleistungRepository;

	@Mock
	private SchuljahresabschnitteRepository schuljahresabschnitteRepository;

	@Mock
	private LehrerPersonalabschnittsdatenRepository lehrerAbschnittsdatenRepository;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	private LehrerMinderleistungService cut;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUpTest() {
		cut = new LehrerMinderleistungService(
				lehrerMinderleistungRepository,
				schuljahresabschnitteRepository,
				lehrerAbschnittsdatenRepository,
				LehrerMinderleistungMapper.INSTANCE,
				LehrerMinderleistungsarten.data()
		);
		transactionSupportMock = mockStatic(TransactionSupport.class);
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(inv -> inv.getArgument(0, Supplier.class).get());
		transactionSupportMock.when(() -> TransactionSupport.transactional(any(Runnable.class)))
				.thenAnswer(inv -> {
					inv.getArgument(0, Runnable.class).run();
					return null;
				});
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}


	@Test
	@DisplayName("getList | Success - empty")
	void testGetListSuccesEmpty() {
		when(lehrerMinderleistungRepository.findListByIds(any())).thenReturn(List.of());

		final var result = cut.getList(List.of(1L));

		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("getList | Success - daten werden korrekt gemappt")
	void testGetListSuccess() {
		final var first = createEntity(1L, 10L, 2.0, "200");
		final var second = createEntity(2L, 10L, 3.5, "210");

		when(lehrerMinderleistungRepository.findListByIds(List.of(1L, 2L))).thenReturn(List.of(first, second));
		stubSchuljahrAbschnitt(10L, 20L);

		final var result = cut.getList(List.of(1L, 2L));

		assertThat(result).hasSize(2);
		assertThat(result.getFirst())
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("idAbschnittsdaten", 10L)
				.hasFieldOrPropertyWithValue("idGrund", 37L)
				.hasFieldOrPropertyWithValue("anzahl", 2.0);
		assertThat(result.getLast())
				.hasFieldOrPropertyWithValue("id", 2L)
				.hasFieldOrPropertyWithValue("idAbschnittsdaten", 10L)
				.hasFieldOrPropertyWithValue("idGrund", 38L)
				.hasFieldOrPropertyWithValue("anzahl", 3.5);

	}

	@Test
	@DisplayName("get | Sucess - daten werden korrekt gemappt")
	void testGetSuccess() {
		final var dto = createEntity(42L, 10L, 1.5, "200");

		when(lehrerMinderleistungRepository.findById(42L)).thenReturn(Optional.of(dto));
		stubSchuljahrAbschnitt(10L, 20L);

		final var result = cut.get(42L);

		assertThat(result.id).isEqualTo(42L);
		assertThat(result.idAbschnittsdaten).isEqualTo(10L);
		assertThat(result.anzahl).isEqualTo(1.5);
		assertThat(result.idGrund).isEqualTo(37);
	}

	@Test
	@DisplayName("get | not Found")
	void testGetNotFound() {
		when(lehrerMinderleistungRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> cut.get(99L))
				.isInstanceOf(ApiOperationException.class)
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getListByLehrer | Success - daten werden korrekt gemappt")
	void testGetListByLehrerAbschnittSucess() {
		final var entity = createEntity(1L, 10L, 2.0, "200");

		when(lehrerMinderleistungRepository.getAllByLehrerAbschnittId(10L)).thenReturn(List.of(entity));
		stubSchuljahrAbschnitt(10L, 20L);

		final var result = cut.getListByLehrerabschnittsdatenId(10L);

		assertThat(result).hasSize(1);
		assertThat(result.getFirst())
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("idAbschnittsdaten", 10L)
				.hasFieldOrPropertyWithValue("idGrund", 37L)
				.hasFieldOrPropertyWithValue("anzahl", 2.0);
	}

	@Test
	@DisplayName("getListByLehrer | Success - empty")
	void testGetListByLehrerAbschnittSucessEmpty() {
		when(lehrerMinderleistungRepository.getAllByLehrerAbschnittId(anyLong())).thenReturn(List.of());

		final var result = cut.getListByLehrerabschnittsdatenId(99L);

		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("create | Success - daten werden korrekt gemappt")
	void testCreateSuccess() {
		final long nextId = 7L;
		final var request = createRequest(37, 2.5);
		final var persisted = createEntity(nextId, 10L, 2.5, "200");

		when(lehrerMinderleistungRepository.getNextID()).thenReturn(nextId);
		when(lehrerMinderleistungRepository.create(any(DTOLehrerEntlastungsstunde.class))).thenReturn(persisted);

		final var result = cut.create(request);

		verify(lehrerMinderleistungRepository).create(any(DTOLehrerEntlastungsstunde.class));
		assertThat(result.id).isEqualTo(nextId);
		assertThat(result.idGrund).isEqualTo(37);
		assertThat(result.idAbschnittsdaten).isEqualTo(10L);
		assertThat(result.anzahl).isEqualTo(2.5);
	}

	@Test
	@DisplayName("createMultiple | Success - daten werden korrekt gemappt")
	void testCreateMultipleSuccess() {
		final var firstRequest = createRequest(37L, 1.0);
		final var secondRequest = createRequest(38L, 2.0);
		final var firstPersisted = createEntity(1L, 10L, 1.0, "200");
		final var secondPersisted = createEntity(2L, 10L, 2.0, "210");
		final List<DTOLehrerEntlastungsstunde> entities = List.of(firstPersisted, secondPersisted);

		when(lehrerMinderleistungRepository.getNextID()).thenReturn(1L);
		when(lehrerMinderleistungRepository.create(entities)).thenReturn(entities);
		stubSchuljahrAbschnitt(10L, 20L);

		final var result = cut.createMultiple(List.of(firstRequest, secondRequest));

		assertThat(result).hasSize(2);
		assertThat(result.getFirst())
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("idAbschnittsdaten", 10L)
				.hasFieldOrPropertyWithValue("idGrund", 37L)
				.hasFieldOrPropertyWithValue("anzahl", 1.0);
		assertThat(result.getLast())
				.hasFieldOrPropertyWithValue("id", 2L)
				.hasFieldOrPropertyWithValue("idAbschnittsdaten", 10L)
				.hasFieldOrPropertyWithValue("idGrund", 38L)
				.hasFieldOrPropertyWithValue("anzahl", 2.0);
	}

	@Test
	@DisplayName("patch | Sucess - Werte werden korrekt übernommen")
	void testPatchAnzahl() {
		final var existing = createEntity(5L, 10L, 1.0, "200");
		final var request = patchRequest(5L, 3.0, 38L);

		when(lehrerMinderleistungRepository.getById(5L)).thenReturn(existing);
		stubSchuljahrAbschnitt(10L, 20L);

		final var result = cut.patch(request, 5L);

		assertThat(result.id).isEqualTo(5L);
		assertThat(result.anzahl).isEqualTo(3.0);
		assertThat(result.idGrund).isEqualTo(38);
	}

	@Test
	@DisplayName("patch | Sucess - unverändert bei undefined")
	void testPatchUndefined() {
		final var existing = createEntity(5L, 10L, 1.0, "200");
		final var request = patchRequest(5L, null, null);

		when(lehrerMinderleistungRepository.getById(5L)).thenReturn(existing);
		stubSchuljahrAbschnitt(10L, 20L);

		final var result = cut.patch(request, 5L);

		assertThat(result.id).isEqualTo(5L);
		assertThat(result.anzahl).isEqualTo(1.0);
		assertThat(result.idGrund).isEqualTo(37);
	}

	@Test
	@DisplayName("patchMultiple | Sucess - daten werden gemappt")
	void testPatchMultipleSuccess() {
		final var firstPersisted = createEntity(1L, 10L, 1.0, "200");
		final var secondPersisted = createEntity(2L, 10L, 2.0, "210");
		final var firstPatch = patchRequest(1L, 5.0, 38L);
		final var secondPatch = patchRequest(2L, 6.0, 37L);

		when(lehrerMinderleistungRepository.getById(1L)).thenReturn(firstPersisted);
		when(lehrerMinderleistungRepository.getById(2L)).thenReturn(secondPersisted);
		stubSchuljahrAbschnitt(10L, 20L);

		final var result = cut.patchMultiple(List.of(firstPatch, secondPatch));

		assertThat(result).hasSize(2);
		assertThat(result.getFirst())
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("idAbschnittsdaten", 10L)
				.hasFieldOrPropertyWithValue("idGrund", 38L)
				.hasFieldOrPropertyWithValue("anzahl", 5.0);
		assertThat(result.getLast())
				.hasFieldOrPropertyWithValue("id", 2L)
				.hasFieldOrPropertyWithValue("idAbschnittsdaten", 10L)
				.hasFieldOrPropertyWithValue("idGrund", 37L)
				.hasFieldOrPropertyWithValue("anzahl", 6.0);
	}

	@Test
	@DisplayName("delete | Sucess - simpleOperationResponse zurückgegeben")
	void testDeleteSuccess() {
		final var entity = createEntity(5L, 10L, 1.0, null);

		when(lehrerMinderleistungRepository.getById(5L)).thenReturn(entity);

		final var result = cut.delete(5L);

		verify(lehrerMinderleistungRepository).delete(entity);
		assertThat(result.id).isEqualTo(5L);
		assertThat(result.success).isTrue();
	}

	@Test
	@DisplayName("deleteMultiple | Sucess - Liste von SimpleOperationResponse zurückgegeben")
	void testDeleteMultipleSuccess() {
		final var firstEntity = createEntity(1L, 10L, 1.0, null);
		final var secondEntity = createEntity(2L, 10L, 2.0, null);
		when(lehrerMinderleistungRepository.findListByIds(List.of(1L, 2L))).thenReturn(List.of(firstEntity, secondEntity));

		final var result = cut.deleteMultiple(List.of(1L, 2L));

		verify(lehrerMinderleistungRepository).delete(List.of(firstEntity, secondEntity));
		assertThat(result)
				.hasSize(2)
				.allSatisfy(r -> assertThat(r.success).isTrue())
				.extracting("id")
				.containsExactly(1L, 2L);
	}

	@Test
	@DisplayName("deleteMultiple | Sucess - Empty Response bei leerer Liste")
	void testDeleteMultipleEmptyResponse() {
		when(lehrerMinderleistungRepository.findListByIds(List.of())).thenReturn(List.of());

		final var result = cut.deleteMultiple(List.of());

		assertThat(result).isEmpty();
		verify(lehrerMinderleistungRepository).delete(List.of());
	}

	@Test
	@DisplayName("getList | Bad Request - LehrerAbschnitt not found")
	void testGetListErrorAbschnittNotFound() {
		final var entity = createEntity(1L, 10L, 1.0, "KRZ");

		final List<Long> ids = List.of(1L);
		when(lehrerMinderleistungRepository.findListByIds(ids)).thenReturn(List.of(entity));
		when(lehrerAbschnittsdatenRepository.findById(10L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> cut.getList(ids))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getList | Bad Request - SchuljahresAbschnitt not found")
	void testGetListErrorSchuljahrAbschnittNotFound() {
		final var entity = createEntity(1L, 10L, 1.0, "KRZ");
		final var lehrerAbschnitt = new DTOLehrerAbschnittsdaten(10L, 0L, 20L);

		final List<Long> ids = List.of(1L);
		when(lehrerMinderleistungRepository.findListByIds(ids)).thenReturn(List.of(entity));
		when(lehrerAbschnittsdatenRepository.findById(10L)).thenReturn(Optional.of(lehrerAbschnitt));
		when(schuljahresabschnitteRepository.findById(20L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> cut.getList(ids))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getListByIdLehrerAbschnittsdaten | Success - Map wird korrekt gemappt")
	void testGetListByIdLehrerAbschnittsdatenSuccess() {
		final var e1 = createEntity(1L, 10L, 2.0, "200");
		final var e2 = createEntity(2L, 10L, 3.0, "210");
		final var e3 = createEntity(3L, 11L, 1.5, "200");

		when(lehrerMinderleistungRepository.getListByIdLehrerAbschnittsdaten(List.of(10L, 11L)))
				.thenReturn(Map.of(
						10L, List.of(e1, e2),
						11L, List.of(e3)
				));

		// Abschnitt 10 und 11 liegen in (ggf. unterschiedlichen) Schuljahren – hier beide 2024
		stubSchuljahrAbschnitt(10L, 20L);
		stubSchuljahrAbschnitt(11L, 21L);

		final var result = cut.getListByIdLehrerAbschnittsdaten(List.of(10L, 11L));

		assertThat(result).hasSize(2);
		assertThat(result.get(10L)).hasSize(2);
		assertThat(result.get(11L)).hasSize(1);
		assertThat(result.get(10L).stream().map(r -> r.id)).containsExactly(1L, 2L);
		assertThat(result.get(11L).stream().map(r -> r.id)).containsExactly(3L);
	}

	@Test
	@DisplayName("create | Bad Request - Minderleistungsgrund nicht resolvable")
	void testCreateBadRequestGrundNotResolvable() {
		final long nextId = 7L;

		final var request = createRequest(999999L, 2.5);

		when(lehrerMinderleistungRepository.getNextID()).thenReturn(nextId);

		assertThatThrownBy(() -> cut.create(request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
				.hasMessageContaining("Der Minderleistungsgrund kann nicht aufgelöst werden");
	}

	@Test
	@DisplayName("patch | Bad Request - Minderleistungsgrund nicht resolvable")
	void testPatchBadRequestGrundNotResolvable() {
		final var existing = createEntity(5L, 10L, 1.0, "200");

		final var request = new LehrerMinderleistungPatchRequest();
		request.anzahl = JsonNullable.undefined();
		request.idGrund = JsonNullable.of(999999L);

		when(lehrerMinderleistungRepository.getById(5L)).thenReturn(existing);

		assertThatThrownBy(() -> cut.patch(request, 5L))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
				.hasMessageContaining("Der Minderleistungsgrund kann nicht aufgelöst werden");
	}


	private DTOLehrerEntlastungsstunde createEntity(final long id, final long abschnittId, final Double stunden, final String kuerzel) {
		final var dto = new DTOLehrerEntlastungsstunde(id, abschnittId);
		dto.anzahl = stunden;
		dto.entlastungsgrundKrz = kuerzel;
		return dto;
	}

	private void stubSchuljahrAbschnitt(final long abschnittId, final long schuljahresabschnittsId) {
		final var lehrerAbschnitt = new DTOLehrerAbschnittsdaten(abschnittId, 0L, schuljahresabschnittsId);
		final var schuljahrAbschnitt = new DTOSchuljahresabschnitte(schuljahresabschnittsId, 2024, 1);
		when(lehrerAbschnittsdatenRepository.findById(abschnittId)).thenReturn(Optional.of(lehrerAbschnitt));
		when(schuljahresabschnitteRepository.findById(schuljahresabschnittsId)).thenReturn(Optional.of(schuljahrAbschnitt));
	}

	private LehrerMinderleistungCreateRequest createRequest(final long idGrund, final double anzahl) {
		final var request = new LehrerMinderleistungCreateRequest();
		request.idAbschnittsdaten = 10L;
		request.idGrund = idGrund;
		request.anzahl = anzahl;
		return request;
	}

	private LehrerMinderleistungBatchPatchRequest patchRequest(final long id, final Double anzahl, final Long idGrund) {
		final var request = new LehrerMinderleistungBatchPatchRequest();
		request.id = id;
		request.anzahl = (anzahl != null) ? JsonNullable.of(anzahl) : JsonNullable.undefined();
		request.idGrund = (idGrund != null) ? JsonNullable.of(idGrund) : JsonNullable.undefined();
		return request;
	}

}
