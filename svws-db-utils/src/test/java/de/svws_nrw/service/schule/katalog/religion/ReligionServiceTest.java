package de.svws_nrw.service.schule.katalog.religion;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.katalog.DTOReligion;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.religion.ReligionMapper;
import de.svws_nrw.repo.schule.kataloge.religion.ReligionRepository;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReligionServiceTest {

	@Mock
	private ReligionRepository repository;

	@Mock
	private ReligionMapper mapper;

	@Mock
	private EigeneSchuleService eigeneSchuleService;

	private ReligionService service;

	private MockedStatic<TransactionSupport> transactionSupport;

	private DTOReligion entity;
	private ReligionEintrag apiModel;

	private static final long VALID_ID_RELIGION_CORETYPE = 1000L;
	private static final long INVALID_ID_RELIGION_CORETYPE = -1L;

	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		entity = new DTOReligion(1L, "röm.-kath.");
		entity.bezeichnungZeugnis = "katholisch";
		entity.sortierung = 1;
		entity.istSichtbar = true;
		entity.schluesselReligion = "AR";

		apiModel = new ReligionEintrag();
		apiModel.id = 1L;
		apiModel.bezeichnung = "röm.-kath.";

		transactionSupport = mockStatic(TransactionSupport.class);
		transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		service = new ReligionService(repository, mapper, eigeneSchuleService);
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	private ReligionCreateRequest createRequest() {
		final var dto = new ReligionCreateRequest();
		dto.bezeichnung = "röm.-kath.";
		dto.bezeichnungZeugnis = "katholisch";
		dto.idReligion = VALID_ID_RELIGION_CORETYPE;
		dto.sortierung = 1;
		dto.istSichtbar = true;
		return dto;
	}

	@Nested
	@DisplayName("getAll")
	class GetAll {

		@Test
		@DisplayName("Gibt leere Liste zurück wenn keine Religionen vorhanden")
		void getAll_leer() {
			when(eigeneSchuleService.getSchuljahr()).thenReturn(2024);
			when(repository.getAll()).thenReturn(List.of());
			when(repository.getReferencedIds(List.of())).thenReturn(Set.of());

			assertThat(service.getAll()).isEmpty();
			verify(mapper, never()).toApi(any(), any(Integer.class));
		}

		@Test
		@DisplayName("Gibt alle Religionen gemappt zurück und setzt referenziertInAnderenTabellen korrekt")
		void getAll() {
			final var entity2 = new DTOReligion(2L, "ev.");
			entity2.schluesselReligion = "AR";

			final var apiModel2 = new ReligionEintrag();
			apiModel2.id = 2L;

			when(eigeneSchuleService.getSchuljahr()).thenReturn(2024);
			when(repository.getAll()).thenReturn(List.of(entity, entity2));
			when(repository.getReferencedIds(List.of(1L, 2L))).thenReturn(Set.of(2L));
			when(mapper.toApi(entity, 2024)).thenReturn(apiModel);
			when(mapper.toApi(entity2, 2024)).thenReturn(apiModel2);

			final var result = service.getAll();

			assertThat(result).containsExactly(apiModel, apiModel2);
			assertThat(apiModel.referenziertInAnderenTabellen).isFalse();
			assertThat(apiModel2.referenziertInAnderenTabellen).isTrue();
		}
	}

	@Nested
	@DisplayName("create")
	class Create {

		@Test
		@DisplayName("Legt Religion korrekt an")
		void create() {
			final var dto = createRequest();

			when(repository.bezeichnungIstBereitsVergeben(dto.bezeichnung)).thenReturn(false);

			when(mapper.toDomain(dto)).thenReturn(entity);
			when(repository.create(entity)).thenReturn(entity);
			when(eigeneSchuleService.getSchuljahr()).thenReturn(2024);
			when(mapper.toApi(entity, 2024)).thenReturn(apiModel);

			final var result = service.create(dto);

			assertThat(result).isEqualTo(apiModel);
			verify(repository).create(entity);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn Bezeichnung bereits vergeben")
		void create_bezeichnungNichtEindeutig() {
			final var dto = createRequest();

			when(repository.bezeichnungIstBereitsVergeben(dto.bezeichnung)).thenReturn(true);

			assertThatException()
					.isThrownBy(() -> service.create(dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining(dto.bezeichnung)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(repository, never()).create(any(DTOReligion.class));
			verify(mapper, never()).toDomain(any(ReligionCreateRequest.class));
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn idReligion unbekannt")
		void create_idReligionUnbekannt() {
			final var dto = createRequest();
			dto.idReligion = INVALID_ID_RELIGION_CORETYPE;

			// Die Bezeichnung muss frei sein, damit die idReligion-Validierung erreicht wird.
			when(repository.bezeichnungIstBereitsVergeben(dto.bezeichnung)).thenReturn(false);

			assertThatException()
					.isThrownBy(() -> service.create(dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining(String.valueOf(INVALID_ID_RELIGION_CORETYPE))
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(repository, never()).create(any(DTOReligion.class));
			verify(mapper, never()).toDomain(any(ReligionCreateRequest.class));
		}
	}

	@Nested
	@DisplayName("patch")
	class Patch {

		@Test
		@DisplayName("Aktualisiert Religion korrekt (Bezeichnung gesetzt)")
		void patch_bezeichnung() {
			final var dto = new ReligionPatchRequest();
			dto.bezeichnung = JsonNullable.of("Neue Bezeichnung");

			when(repository.bezeichnungIstBereitsVergebenExceptId("Neue Bezeichnung", 1L))
					.thenReturn(false);

			when(repository.getById(1L)).thenReturn(entity);
			when(eigeneSchuleService.getSchuljahr()).thenReturn(2024);
			when(mapper.toApi(entity, 2024)).thenReturn(apiModel);

			final var result = service.patch(1L, dto);

			assertThat(result).isEqualTo(apiModel);
			verify(mapper).patch(dto, entity);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn Bezeichnung bereits vergeben")
		void patch_bezeichnungNichtEindeutig() {
			final var dto = new ReligionPatchRequest();
			dto.bezeichnung = JsonNullable.of("röm.-kath.");

			when(repository.bezeichnungIstBereitsVergebenExceptId("röm.-kath.", 1L))
					.thenReturn(true);

			assertThatException()
					.isThrownBy(() -> service.patch(1L, dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("röm.-kath.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(repository, never()).getById(anyLong());
			verify(mapper, never()).patch(any(), any(DTOReligion.class));
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn idReligion unbekannt")
		void patch_idReligionUnbekannt() {
			final var dto = new ReligionPatchRequest();
			dto.idReligion = JsonNullable.of(INVALID_ID_RELIGION_CORETYPE);

			assertThatException()
					.isThrownBy(() -> service.patch(1L, dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining(String.valueOf(INVALID_ID_RELIGION_CORETYPE))
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(mapper, never()).patch(any(), any(DTOReligion.class));
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert — kein Unique-Check")
		void patch_allUndefined() {
			final var dto = new ReligionPatchRequest();

			when(repository.getById(1L)).thenReturn(entity);
			when(eigeneSchuleService.getSchuljahr()).thenReturn(2024);
			when(mapper.toApi(entity, 2024)).thenReturn(apiModel);

			service.patch(1L, dto);

			verify(repository, never()).bezeichnungIstBereitsVergebenExceptId(any(), any(Long.class));
			verify(mapper).patch(dto, entity);
		}
	}

	@Nested
	@DisplayName("delete")
	class Delete {

		@Test
		@DisplayName("Delegiert an BulkDeleteUtils.deleteWithReferenceCheck(...) und liefert Ergebnis zurück")
		void delete_delegiert() {
			final var ids = List.of(2L, 1L);

			final var r1 = new SimpleOperationResponse();
			r1.id = 1L;
			r1.success = true;

			final var r2 = new SimpleOperationResponse();
			r2.id = 2L;
			r2.success = false;

			try (MockedStatic<BulkDeleteUtils> bulkDeleteUtils = mockStatic(BulkDeleteUtils.class)) {
				bulkDeleteUtils.when(() -> BulkDeleteUtils.deleteWithReferenceCheck(
								eq(ids),
								eq(repository),
								any(),
								eq("Religion")
						))
						.thenReturn(List.of(r1, r2));

				final var result = service.delete(ids);

				assertThat(result).containsExactly(r1, r2);
				bulkDeleteUtils.verify(() -> BulkDeleteUtils.deleteWithReferenceCheck(
						eq(ids),
						eq(repository),
						any(),
						eq("Religion")
				));
			}
		}
	}
}
