package de.svws_nrw.service.lehrer.lehrbefaehigung;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtBefaehigung;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.lehrbefaehigung.LehrerLehrbefaehigungMapper;
import de.svws_nrw.repo.lehrer.lehramt.LehrerLehramtRepository;
import de.svws_nrw.repo.lehrer.lehrbefaehigung.LehrerLehramtLehrbefaehigungRepository;
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
 * Tests für den Service zu Lehrbefähigungen bei Lehrern.
 */
@ExtendWith(MockitoExtension.class)
class LehrerLehrbefaehigungServiceTest {

	private static final long ID                   = 1L;
	private static final long ID_LEHRAMT           = 500L;
	private static final long ID_LEHRBEFAEHIGUNG   = 174L;
	private static final long ID_LEHRBEFAEHIGUNG_2 = 175L;
	private static final long ID_ANERKENNUNGSGRUND = 4L;

	@Mock
	private LehrerLehramtLehrbefaehigungRepository repo;

	@Mock
	private LehrerLehramtRepository lehrerLehramtRepository;

	@Mock
	private LehrerLehrbefaehigungMapper mapper;

	private LehrerLehrbefaehigungService service;

	private MockedStatic<TransactionSupport> transactionSupport;

	private DTOLehrerPersonaldatenLehramtBefaehigung entity;

	private LehrerLehrbefaehigungEintrag apiModel;

	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		entity = new DTOLehrerPersonaldatenLehramtBefaehigung(
				ID,
				ID_LEHRAMT,
				ID_LEHRBEFAEHIGUNG);
		entity.idAnerkennungsgrund = ID_ANERKENNUNGSGRUND;

		apiModel = new LehrerLehrbefaehigungEintrag();
		apiModel.id = ID;
		apiModel.idLehramt = ID_LEHRAMT;
		apiModel.idLehrbefaehigung = ID_LEHRBEFAEHIGUNG;
		apiModel.idAnerkennungsgrund = ID_ANERKENNUNGSGRUND;

		transactionSupport = org.mockito.Mockito.mockStatic(TransactionSupport.class);
		transactionSupport
				.when(() -> TransactionSupport.transactional(
						org.mockito.ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation ->
						invocation.getArgument(0, Supplier.class).get());

		service = new LehrerLehrbefaehigungService(
				repo,
				lehrerLehramtRepository,
				mapper);
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private LehrerLehrbefaehigungCreateRequest createRequest() {
		final var request = new LehrerLehrbefaehigungCreateRequest();
		request.idLehramt = ID_LEHRAMT;
		request.idLehrbefaehigung = ID_LEHRBEFAEHIGUNG;
		request.idAnerkennungsgrund = ID_ANERKENNUNGSGRUND;
		return request;
	}

	private LehrerLehrbefaehigungPatchRequest createPatchRequest() {
		final var request = new LehrerLehrbefaehigungPatchRequest();
		request.idLehramt = JsonNullable.of(ID_LEHRAMT);
		request.idLehrbefaehigung = JsonNullable.of(ID_LEHRBEFAEHIGUNG);
		request.idAnerkennungsgrund = JsonNullable.of(ID_ANERKENNUNGSGRUND);
		return request;
	}

	// -------------------------------------------------------------------------
	// getAll
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getAll")
	class GetAll {

		@Test
		@DisplayName("Gibt leere Liste zurück, wenn keine Lehrbefähigungen vorhanden sind")
		void getAll_leer() {
			when(repo.getAll()).thenReturn(List.of());

			final var result = service.getAll();

			assertThat(result).isEmpty();
		}

		@Test
		@DisplayName("Gibt alle Lehrbefähigungen gemappt zurück")
		void getAll() {
			final var entity2 =
					new DTOLehrerPersonaldatenLehramtBefaehigung(
							2L,
							ID_LEHRAMT,
							ID_LEHRBEFAEHIGUNG_2);

			final var apiModel2 = new LehrerLehrbefaehigungEintrag();
			apiModel2.id = 2L;
			apiModel2.idLehramt = ID_LEHRAMT;
			apiModel2.idLehrbefaehigung = ID_LEHRBEFAEHIGUNG_2;

			when(repo.getAll()).thenReturn(List.of(entity, entity2));
			when(mapper.toApi(entity)).thenReturn(apiModel);
			when(mapper.toApi(entity2)).thenReturn(apiModel2);

			final var result = service.getAll();

			assertThat(result)
					.containsExactly(apiModel, apiModel2);

			verify(mapper).toApi(entity);
			verify(mapper).toApi(entity2);
		}
	}

	// -------------------------------------------------------------------------
	// getLehrerLehrbefaehigungByIdLehramt
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getLehrerLehrbefaehigungByIdLehramt")
	class GetLehrerLehrbefaehigungByIdLehramt {

		@Test
		@DisplayName("Mappt die Lehrbefähigungen je Lehramt")
		void getLehrerLehrbefaehigungByIdLehramt() {
			final var entity2 =
					new DTOLehrerPersonaldatenLehramtBefaehigung(
							2L,
							ID_LEHRAMT,
							ID_LEHRBEFAEHIGUNG_2);

			final var apiModel2 = new LehrerLehrbefaehigungEintrag();
			apiModel2.id = 2L;
			apiModel2.idLehramt = ID_LEHRAMT;
			apiModel2.idLehrbefaehigung = ID_LEHRBEFAEHIGUNG_2;

			when(repo.getLehrerLehrbefaehigungByIdLehramt(List.of(ID_LEHRAMT)))
					.thenReturn(Map.of(ID_LEHRAMT, List.of(entity, entity2)));
			when(mapper.toApi(entity)).thenReturn(apiModel);
			when(mapper.toApi(entity2)).thenReturn(apiModel2);

			final var result =
					service.getLehrerLehrbefaehigungByIdLehramt(List.of(ID_LEHRAMT));

			assertThat(result)
					.containsOnlyKeys(ID_LEHRAMT)
					.containsEntry(ID_LEHRAMT, List.of(apiModel, apiModel2));

			verify(repo).getLehrerLehrbefaehigungByIdLehramt(List.of(ID_LEHRAMT));
		}

		@Test
		@DisplayName("Gibt eine leere Map zurück, wenn keine Daten vorhanden sind")
		void getLehrerLehrbefaehigungByIdLehramt_leer() {
			when(repo.getLehrerLehrbefaehigungByIdLehramt(List.of(999L)))
					.thenReturn(Map.of());

			final var result =
					service.getLehrerLehrbefaehigungByIdLehramt(List.of(999L));

			assertThat(result).isEmpty();
		}
	}

	// -------------------------------------------------------------------------
	// getByIdLehramt
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getByIdLehramt")
	class GetByIdLehramt {

		@Test
		@DisplayName("Gibt alle Lehrbefähigungen eines Lehramtes gemappt zurück")
		void getByIdLehramt() {
			when(repo.getByIdLehramt(ID_LEHRAMT))
					.thenReturn(List.of(entity));
			when(mapper.toApi(entity))
					.thenReturn(apiModel);

			final var result = service.getByIdLehramt(ID_LEHRAMT);

			assertThat(result)
					.containsExactly(apiModel);

			verify(repo).getByIdLehramt(ID_LEHRAMT);
			verify(mapper).toApi(entity);
		}

		@Test
		@DisplayName("Gibt eine leere Liste zurück, wenn keine Lehrbefähigungen vorhanden sind")
		void getByIdLehramt_leer() {
			when(repo.getByIdLehramt(ID_LEHRAMT))
					.thenReturn(List.of());

			final var result = service.getByIdLehramt(ID_LEHRAMT);

			assertThat(result).isEmpty();
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("create")
	class Create {

		@Test
		@DisplayName("Legt eine Lehrerlehrbefähigung korrekt an")
		void create() {
			final var request = createRequest();

			when(lehrerLehramtRepository.existsById(ID_LEHRAMT))
					.thenReturn(true);
			when(mapper.toDomain(request))
					.thenReturn(entity);
			when(repo.create(entity))
					.thenReturn(entity);
			when(mapper.toApi(entity))
					.thenReturn(apiModel);

			final var result = service.create(request);

			assertThat(result)
					.isSameAs(apiModel);

			verify(lehrerLehramtRepository).existsById(ID_LEHRAMT);
			verify(mapper).toDomain(request);
			verify(repo).create(entity);
			verify(mapper).toApi(entity);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST bei unbekannter Lehramts-ID")
		void create_lehramtNichtGefunden() {
			final var request = createRequest();

			when(lehrerLehramtRepository.existsById(ID_LEHRAMT))
					.thenReturn(false);

			assertThatException()
					.isThrownBy(() -> service.create(request))
					.isInstanceOf(ApiOperationException.class)
					.withMessage("Kein Lehramt für die ID %d gefunden.".formatted(ID_LEHRAMT))
					.hasFieldOrPropertyWithValue(
							"status",
							Response.Status.BAD_REQUEST);

			verify(mapper, never()).toDomain(any());
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST bei unbekannter Lehrbefähigungs-ID")
		void create_lehrbefaehigungNichtGefunden() {
			final var request = createRequest();
			request.idLehrbefaehigung = -1L;

			when(lehrerLehramtRepository.existsById(ID_LEHRAMT))
					.thenReturn(true);

			assertThatException()
					.isThrownBy(() -> service.create(request))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("-1")
					.hasFieldOrPropertyWithValue(
							"status",
							Response.Status.BAD_REQUEST);

			verify(mapper, never()).toDomain(any());
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST bei unbekannter Anerkennungsgrund-ID")
		void create_anerkennungsgrundNichtGefunden() {
			final var request = createRequest();
			request.idAnerkennungsgrund = -1L;

			when(lehrerLehramtRepository.existsById(ID_LEHRAMT))
					.thenReturn(true);

			assertThatException()
					.isThrownBy(() -> service.create(request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue(
							"status",
							Response.Status.BAD_REQUEST);

			verify(mapper, never()).toDomain(any());
		}

		@Test
		@DisplayName("Erlaubt einen fehlenden Anerkennungsgrund")
		void create_anerkennungsgrundNull() {
			final var request = createRequest();
			request.idAnerkennungsgrund = null;

			when(lehrerLehramtRepository.existsById(ID_LEHRAMT))
					.thenReturn(true);
			when(mapper.toDomain(request))
					.thenReturn(entity);
			when(repo.create(entity))
					.thenReturn(entity);
			when(mapper.toApi(entity))
					.thenReturn(apiModel);

			final var result = service.create(request);

			assertThat(result)
					.isSameAs(apiModel);

			verify(repo).create(entity);
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		@Test
		@DisplayName("Aktualisiert eine Lehrerlehrbefähigung korrekt")
		void patch() {
			final var request = createPatchRequest();

			when(repo.getById(ID))
					.thenReturn(entity);
			when(lehrerLehramtRepository.existsById(ID_LEHRAMT))
					.thenReturn(true);
			when(mapper.toApi(entity))
					.thenReturn(apiModel);

			final var result = service.patch(ID, request);

			assertThat(result)
					.isSameAs(apiModel);

			verify(repo).getById(ID);
			verify(lehrerLehramtRepository).existsById(ID_LEHRAMT);
			verify(mapper).patch(request, entity);
			verify(mapper).toApi(entity);
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert")
		void patch_alleFelderUndefined() {
			final var request = new LehrerLehrbefaehigungPatchRequest();

			when(repo.getById(ID))
					.thenReturn(entity);
			when(mapper.toApi(entity))
					.thenReturn(apiModel);

			final var result = service.patch(ID, request);

			assertThat(result)
					.isSameAs(apiModel);

			verify(repo).getById(ID);
			verify(mapper).patch(request, entity);
			verify(mapper).toApi(entity);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST bei unbekannter Lehramts-ID")
		void patch_lehramtNichtGefunden() {
			final var request = new LehrerLehrbefaehigungPatchRequest();
			request.idLehramt = JsonNullable.of(-1L);

			when(repo.getById(ID))
					.thenReturn(entity);
			when(lehrerLehramtRepository.existsById(-1L))
					.thenReturn(false);

			assertThatException()
					.isThrownBy(() -> service.patch(ID, request))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("-1")
					.hasFieldOrPropertyWithValue(
							"status",
							Response.Status.BAD_REQUEST);

			verify(mapper, never()).patch(any(), any());
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST bei unbekannter Lehrbefähigungs-ID")
		void patch_lehrbefaehigungNichtGefunden() {
			final var request = new LehrerLehrbefaehigungPatchRequest();
			request.idLehrbefaehigung = JsonNullable.of(-1L);

			when(repo.getById(ID))
					.thenReturn(entity);

			assertThatException()
					.isThrownBy(() -> service.patch(ID, request))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("-1")
					.hasFieldOrPropertyWithValue(
							"status",
							Response.Status.BAD_REQUEST);

			verify(mapper, never()).patch(any(), any());
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST bei unbekannter Anerkennungsgrund-ID")
		void patch_anerkennungsgrundNichtGefunden() {
			final var request = new LehrerLehrbefaehigungPatchRequest();
			request.idAnerkennungsgrund = JsonNullable.of(-1L);

			when(repo.getById(ID))
					.thenReturn(entity);

			assertThatException()
					.isThrownBy(() -> service.patch(ID, request))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("-1")
					.hasFieldOrPropertyWithValue(
							"status",
							Response.Status.BAD_REQUEST);

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
		@DisplayName("Löscht Lehrbefähigungen und liefert nach ID sortierte Success-Responses")
		void delete_alleGefundenUndGeloescht() {
			final var entity2 =
					new DTOLehrerPersonaldatenLehramtBefaehigung(
							2L,
							ID_LEHRAMT,
							ID_LEHRBEFAEHIGUNG_2);

			when(repo.findListByIds(List.of(2L, ID)))
					.thenReturn(List.of(entity2, entity));
			when(repo.delete(List.of(entity2, entity)))
					.thenReturn(List.of(entity2, entity));

			final var result = service.delete(List.of(2L, ID));

			assertThat(result)
					.hasSize(2)
					.allMatch(r -> r.success)
					.extracting(r -> r.id)
					.containsExactly(ID, 2L);

			verify(repo).findListByIds(List.of(2L, ID));
			verify(repo).delete(List.of(entity2, entity));
		}

		@Test
		@DisplayName("Liefert Error-Response für nicht gefundene ID")
		void delete_idNichtGefunden() {
			when(repo.findListByIds(List.of(999L)))
					.thenReturn(List.of());
			when(repo.delete(List.of()))
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
			when(repo.findListByIds(List.of(ID, 999L)))
					.thenReturn(List.of(entity));
			when(repo.delete(List.of(entity)))
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
			when(repo.findListByIds(List.of()))
					.thenReturn(List.of());
			when(repo.delete(List.of()))
					.thenReturn(List.of());

			final var result = service.delete(List.of());

			assertThat(result).isEmpty();
		}
	}
}
