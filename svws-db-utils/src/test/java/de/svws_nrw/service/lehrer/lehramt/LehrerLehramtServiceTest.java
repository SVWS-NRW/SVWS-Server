package de.svws_nrw.service.lehrer.lehramt;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramt;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.lehramt.LehrerLehramtMapper;
import de.svws_nrw.mapper.lehrer.lehramt.LehrerLehramtMappingContext;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.lehrer.lehramt.LehrerLehramtRepository;
import de.svws_nrw.service.lehrer.fachrichtung.LehrerFachrichtungService;
import de.svws_nrw.service.lehrer.lehrbefaehigung.LehrerLehrbefaehigungService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests für den Service zu Lehrämtern bei Lehrern.
 */
@ExtendWith(MockitoExtension.class)
class LehrerLehramtServiceTest {

	private static final long ID                   = 1L;
	private static final long ID_LEHRER            = 4711L;
	private static final long ID_LEHRER_2          = 4712L;
	private static final long ID_KATALOG_LEHRAMT   = 82L;
	private static final long ID_ANERKENNUNGSGRUND = 1L;

	@Mock
	private LehrerLehramtRepository lehramtRepository;

	@Mock
	private LehrerRepository lehrerRepository;

	@Mock
	private LehrerFachrichtungService lehrerFachrichtungenService;

	@Mock
	private LehrerLehrbefaehigungService lehrerLehrbefaehigungenService;

	@Mock
	private LehrerLehramtMapper mapper;

	private LehrerLehramtService service;

	private MockedStatic<TransactionSupport> transactionSupport;

	private DTOLehrerPersonaldatenLehramt entity;

	private LehrerLehramtEintrag apiModel;

	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		entity = new DTOLehrerPersonaldatenLehramt(ID, ID_LEHRER);
		entity.idKatalogLehramt = ID_KATALOG_LEHRAMT;
		entity.idAnerkennungsgrund = ID_ANERKENNUNGSGRUND;

		apiModel = new LehrerLehramtEintrag();
		apiModel.id = ID;
		apiModel.idLehrer = ID_LEHRER;
		apiModel.idKatalogLehramt = ID_KATALOG_LEHRAMT;
		apiModel.idAnerkennungsgrund = ID_ANERKENNUNGSGRUND;

		transactionSupport = org.mockito.Mockito.mockStatic(TransactionSupport.class);
		transactionSupport
				.when(() -> TransactionSupport.transactional(
						org.mockito.ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation ->
						invocation.getArgument(0, Supplier.class).get());

		service = new LehrerLehramtService(
				lehramtRepository,
				lehrerRepository,
				lehrerFachrichtungenService,
				lehrerLehrbefaehigungenService,
				mapper);
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private LehrerLehramtCreateRequest createRequest() {
		final var request = new LehrerLehramtCreateRequest();
		request.idLehrer = ID_LEHRER;
		request.idKatalogLehramt = ID_KATALOG_LEHRAMT;
		request.idAnerkennungsgrund = ID_ANERKENNUNGSGRUND;
		return request;
	}

	private LehrerLehramtPatchRequest createPatchRequest() {
		final var request = new LehrerLehramtPatchRequest();
		request.idLehrer = JsonNullable.of(ID_LEHRER);
		request.idKatalogLehramt = JsonNullable.of(ID_KATALOG_LEHRAMT);
		request.idAnerkennungsgrund = JsonNullable.of(ID_ANERKENNUNGSGRUND);
		return request;
	}

	private void stubMapSingleToApi(final DTOLehrerPersonaldatenLehramt dto, final LehrerLehramtEintrag result) {
		when(lehrerLehrbefaehigungenService.getByIdLehramt(dto.id)).thenReturn(List.of());
		when(lehrerFachrichtungenService.getByIdLehramt(dto.id)).thenReturn(List.of());
		when(mapper.toApi(dto, new LehrerLehramtMappingContext(List.of(), List.of()))).thenReturn(result);
	}

	private void stubMapListToApi(final List<DTOLehrerPersonaldatenLehramt> dtos, final List<LehrerLehramtEintrag> results) {
		final var ids = dtos.stream().map(d -> d.id).toList();
		when(lehrerLehrbefaehigungenService.getLehrerLehrbefaehigungByIdLehramt(ids)).thenReturn(Map.of());
		when(lehrerFachrichtungenService.getLehrerFachrichtungenByIdLehramt(ids)).thenReturn(Map.of());
		for (int i = 0; i < dtos.size(); i++) {
			final var dto = dtos.get(i);
			when(mapper.toApi(dto, new LehrerLehramtMappingContext(List.of(), List.of()))).thenReturn(results.get(i));
		}
	}

	// -------------------------------------------------------------------------
	// getByIdLehrer
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getByIdLehrer")
	class GetByIdLehrer {

		@Test
		@DisplayName("Gibt leere Liste bei null zurück")
		void getByIdLehrer_null() {
			final var result = service.getByIdLehrer(null);

			assertThat(result).isEmpty();
		}

		@Test
		@DisplayName("Gibt leere Liste zurück, wenn keine Lehrämter vorhanden sind")
		void getByIdLehrer_leer() {
			when(lehramtRepository.findByIdsLehrer(List.of(ID_LEHRER))).thenReturn(List.of());

			final var result = service.getByIdLehrer(ID_LEHRER);

			assertThat(result).isEmpty();
		}

		@Test
		@DisplayName("Gibt alle Lehrämter des Lehrers gemappt zurück")
		void getByIdLehrer() {
			when(lehramtRepository.findByIdsLehrer(List.of(ID_LEHRER))).thenReturn(List.of(entity));
			stubMapListToApi(List.of(entity), List.of(apiModel));

			final var result = service.getByIdLehrer(ID_LEHRER);

			assertThat(result).containsExactly(apiModel);
		}
	}

	// -------------------------------------------------------------------------
	// getMapByIdLehrer
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getMapByIdLehrer")
	class GetMapByIdLehrer {

		@Test
		@DisplayName("Gibt leere Map bei null zurück")
		void getMapByIdLehrer_null() {
			final var result = service.getMapByIdLehrer(null);

			assertThat(result).isEmpty();
		}

		@Test
		@DisplayName("Gibt leere Map bei leerer Liste zurück")
		void getMapByIdLehrer_leer() {
			final var result = service.getMapByIdLehrer(List.of());

			assertThat(result).isEmpty();
		}

		@Test
		@DisplayName("Gruppiert Lehrämter nach Lehrer-ID")
		void getMapByIdLehrer() {
			final var entity2 = new DTOLehrerPersonaldatenLehramt(2L, ID_LEHRER_2);
			entity2.idKatalogLehramt = ID_KATALOG_LEHRAMT;

			final var apiModel2 = new LehrerLehramtEintrag();
			apiModel2.id = 2L;
			apiModel2.idLehrer = ID_LEHRER_2;

			when(lehramtRepository.findByIdsLehrer(List.of(ID_LEHRER, ID_LEHRER_2)))
					.thenReturn(List.of(entity, entity2));
			stubMapListToApi(List.of(entity, entity2), List.of(apiModel, apiModel2));

			final var result = service.getMapByIdLehrer(List.of(ID_LEHRER, ID_LEHRER_2));

			assertThat(result)
					.containsOnlyKeys(ID_LEHRER, ID_LEHRER_2)
					.containsEntry(ID_LEHRER, List.of(apiModel))
					.containsEntry(ID_LEHRER_2, List.of(apiModel2));
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("create")
	class Create {

		@Test
		@DisplayName("Legt ein Lehramt korrekt an")
		void create() {
			final var request = createRequest();

			when(lehrerRepository.existsById(ID_LEHRER)).thenReturn(true);
			when(mapper.toDomain(request)).thenReturn(entity);
			when(lehramtRepository.create(entity)).thenReturn(entity);
			stubMapSingleToApi(entity, apiModel);

			final var result = service.create(request);

			assertThat(result).isSameAs(apiModel);

			verify(lehrerRepository).existsById(ID_LEHRER);
			verify(mapper).toDomain(request);
			verify(lehramtRepository).create(entity);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST bei unbekannter Lehrer-ID")
		void create_lehrerNichtGefunden() {
			final var request = createRequest();

			when(lehrerRepository.existsById(ID_LEHRER)).thenReturn(false);

			assertThatException()
					.isThrownBy(() -> service.create(request))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining(String.valueOf(ID_LEHRER))
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(mapper, never()).toDomain(any());
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST bei unbekannter Katalog-Lehramt-ID")
		void create_lehramtNichtGefunden() {
			final var request = createRequest();
			request.idKatalogLehramt = -1L;

			when(lehrerRepository.existsById(ID_LEHRER)).thenReturn(true);

			assertThatException()
					.isThrownBy(() -> service.create(request))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("-1")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(mapper, never()).toDomain(any());
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST bei unbekannter Anerkennungsgrund-ID")
		void create_anerkennungsgrundNichtGefunden() {
			final var request = createRequest();
			request.idAnerkennungsgrund = -1L;

			when(lehrerRepository.existsById(ID_LEHRER)).thenReturn(true);

			assertThatException()
					.isThrownBy(() -> service.create(request))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("-1")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(mapper, never()).toDomain(any());
		}

		@Test
		@DisplayName("Erlaubt einen fehlenden Anerkennungsgrund")
		void create_anerkennungsgrundNull() {
			final var request = createRequest();
			request.idAnerkennungsgrund = null;

			when(lehrerRepository.existsById(ID_LEHRER)).thenReturn(true);
			when(mapper.toDomain(request)).thenReturn(entity);
			when(lehramtRepository.create(entity)).thenReturn(entity);
			stubMapSingleToApi(entity, apiModel);

			final var result = service.create(request);

			assertThat(result).isSameAs(apiModel);

			verify(lehramtRepository).create(entity);
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		@Test
		@DisplayName("Aktualisiert ein Lehramt korrekt")
		void patch() {
			final var request = createPatchRequest();

			when(lehramtRepository.getById(ID)).thenReturn(entity);
			when(lehrerRepository.existsById(ID_LEHRER)).thenReturn(true);
			stubMapSingleToApi(entity, apiModel);

			final var result = service.patch(ID, request);

			assertThat(result).isSameAs(apiModel);

			verify(lehramtRepository).getById(ID);
			verify(lehrerRepository).existsById(ID_LEHRER);
			verify(mapper).patch(request, entity);
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert")
		void patch_alleFelderUndefined() {
			final var request = new LehrerLehramtPatchRequest();

			when(lehramtRepository.getById(ID)).thenReturn(entity);
			stubMapSingleToApi(entity, apiModel);

			final var result = service.patch(ID, request);

			assertThat(result).isSameAs(apiModel);

			verify(lehramtRepository).getById(ID);
			verify(mapper).patch(request, entity);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST bei unbekannter Lehrer-ID")
		void patch_lehrerNichtGefunden() {
			final var request = new LehrerLehramtPatchRequest();
			request.idLehrer = JsonNullable.of(-1L);

			when(lehramtRepository.getById(ID)).thenReturn(entity);
			when(lehrerRepository.existsById(-1L)).thenReturn(false);

			assertThatException()
					.isThrownBy(() -> service.patch(ID, request))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("-1")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(mapper, never()).patch(any(), any());
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST bei unbekannter Katalog-Lehramt-ID")
		void patch_lehramtNichtGefunden() {
			final var request = new LehrerLehramtPatchRequest();
			request.idKatalogLehramt = JsonNullable.of(-1L);

			when(lehramtRepository.getById(ID)).thenReturn(entity);

			assertThatException()
					.isThrownBy(() -> service.patch(ID, request))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("-1")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(mapper, never()).patch(any(), any());
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST bei unbekannter Anerkennungsgrund-ID")
		void patch_anerkennungsgrundNichtGefunden() {
			final var request = new LehrerLehramtPatchRequest();
			request.idAnerkennungsgrund = JsonNullable.of(-1L);

			when(lehramtRepository.getById(ID)).thenReturn(entity);

			assertThatException()
					.isThrownBy(() -> service.patch(ID, request))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("-1")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(mapper, never()).patch(any(), any());
		}
	}

	// -------------------------------------------------------------------------
	// delete
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("delete")
	class Delete {

		@Test
		@DisplayName("Löscht Lehrämter und liefert nach ID sortierte Success-Responses")
		void delete_alleGefundenUndGeloescht() {
			final var entity2 = new DTOLehrerPersonaldatenLehramt(2L, ID_LEHRER_2);

			when(lehramtRepository.findListByIds(List.of(2L, ID)))
					.thenReturn(List.of(entity2, entity));
			when(lehramtRepository.delete(List.of(entity2, entity)))
					.thenReturn(List.of(entity2, entity));

			final var result = service.delete(List.of(2L, ID));

			assertThat(result)
					.hasSize(2)
					.allMatch(r -> r.success)
					.extracting(r -> r.id)
					.containsExactly(ID, 2L);

			verify(lehramtRepository).findListByIds(List.of(2L, ID));
			verify(lehramtRepository).delete(List.of(entity2, entity));
		}

		@Test
		@DisplayName("Liefert Error-Response für nicht gefundene ID")
		void delete_idNichtGefunden() {
			when(lehramtRepository.findListByIds(List.of(999L)))
					.thenReturn(List.of());
			when(lehramtRepository.delete(List.of()))
					.thenReturn(List.of());

			final var result = service.delete(List.of(999L));

			assertThat(result)
					.hasSize(1)
					.satisfiesExactly(r -> {
						assertThat(r.success).isFalse();
						assertThat(r.id).isEqualTo(999L);
						assertThat(r.log).anyMatch(m -> m.contains("nicht gefunden"));
					});
		}

		@Test
		@DisplayName("Liefert gemischte Responses bei teils gefundenen IDs")
		void delete_teilweiseGefunden() {
			when(lehramtRepository.findListByIds(List.of(ID, 999L)))
					.thenReturn(List.of(entity));
			when(lehramtRepository.delete(List.of(entity)))
					.thenReturn(List.of(entity));

			final var result = service.delete(List.of(ID, 999L));

			assertThat(result)
					.hasSize(2)
					.satisfiesExactly(
							r -> {
								assertThat(r.id).isEqualTo(ID);
								assertThat(r.success).isTrue();
							},
							r -> {
								assertThat(r.id).isEqualTo(999L);
								assertThat(r.success).isFalse();
							});
		}

		@Test
		@DisplayName("Liefert leere Liste bei leerer Eingabe")
		void delete_leereEingabe() {
			when(lehramtRepository.findListByIds(List.of()))
					.thenReturn(List.of());
			when(lehramtRepository.delete(List.of()))
					.thenReturn(List.of());

			final var result = service.delete(List.of());

			assertThat(result).isEmpty();
		}
	}
}

