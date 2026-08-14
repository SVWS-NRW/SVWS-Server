package de.svws_nrw.service.schule.katalog.ort;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.ort.OrtMapper;
import de.svws_nrw.repo.schule.kataloge.ort.OrtRepository;
import de.svws_nrw.service.schule.EigeneSchuleService;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrtServiceTest {

	@Mock
	private OrtRepository ortRepository;

	@Mock
	private OrtMapper mapper;

	@Mock
	private EigeneSchuleService eigeneSchuleService;

	private OrtService service;

	private MockedStatic<TransactionSupport> transactionSupport;

	private DTOOrt entity;
	private OrtKatalogEintrag apiModel;

	private static final long VALID_ID_BUNDESLAND = 1010L;
	private static final long INVALID_ID_BUNDESLAND = -1L;

	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		entity = new DTOOrt(1L, "53840", "Troisdorf");

		apiModel = new OrtKatalogEintrag();
		apiModel.id = 1L;
		apiModel.ortsname = "Troisdorf";
		apiModel.plz = "53840";

		transactionSupport = mockStatic(TransactionSupport.class);
		transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		service = new OrtService(ortRepository, mapper, eigeneSchuleService);
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private OrtCreateRequest createRequest() {
		final var dto = new OrtCreateRequest();
		dto.plz = "53840";
		dto.ortsname = "Troisdorf";
		dto.kreis = "RSK";
		dto.idBundesland = VALID_ID_BUNDESLAND;
		dto.sortierung = 1;
		dto.istSichtbar = true;
		dto.istAenderbar = true;
		return dto;
	}

	// -------------------------------------------------------------------------
	// getAll
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getAll")
	class GetAll {

		@Test
		@DisplayName("Gibt leere Liste zurück wenn keine Orte vorhanden")
		void getAll_leer() {
			when(ortRepository.getAll()).thenReturn(List.of());
			when(ortRepository.getReferencedIds(List.of())).thenReturn(Set.of());

			assertThat(service.getAll()).isEmpty();
			verify(mapper, never()).toApi(any(), any(Integer.class));
		}

		@Test
		@DisplayName("Gibt alle Orte gemappt zurück und setzt referenziertInAnderenTabellen korrekt")
		void getAll() {
			final var entity2 = new DTOOrt(2L, "50667", "Köln");
			final var apiModel2 = new OrtKatalogEintrag();
			apiModel2.id = 2L;

			when(ortRepository.getAll()).thenReturn(List.of(entity, entity2));
			when(ortRepository.getReferencedIds(List.of(1L, 2L))).thenReturn(Set.of(2L));
			when(eigeneSchuleService.getSchuljahr()).thenReturn(2024);
			when(mapper.toApi(entity, 2024)).thenReturn(apiModel);
			when(mapper.toApi(entity2, 2024)).thenReturn(apiModel2);

			final var result = service.getAll();

			assertThat(result).containsExactly(apiModel, apiModel2);
			assertThat(apiModel.referenziertInAnderenTabellen).isFalse();
			assertThat(apiModel2.referenziertInAnderenTabellen).isTrue();
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("create")
	class Create {

		@Test
		@DisplayName("Legt Ort korrekt an")
		void create() {
			final var dto = createRequest();

			when(ortRepository.ortsnameIsUniqueForPlzCreate(dto.ortsname, dto.plz)).thenReturn(true);
			when(mapper.toDomain(dto)).thenReturn(entity);
			when(ortRepository.create(entity)).thenReturn(entity);
			when(eigeneSchuleService.getSchuljahr()).thenReturn(2024);
			when(mapper.toApi(entity, 2024)).thenReturn(apiModel);

			final var result = service.create(dto);

			assertThat(result).isEqualTo(apiModel);
			verify(ortRepository).create(entity);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn Ortsname für PLZ bereits vergeben")
		void create_ortsnameNichtEindeutig() {
			final var dto = createRequest();

			when(ortRepository.ortsnameIsUniqueForPlzCreate(dto.ortsname, dto.plz)).thenReturn(false);

			assertThatException()
					.isThrownBy(() -> service.create(dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining(dto.ortsname)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(ortRepository, never()).create(any(DTOOrt.class));
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn idBundesland unbekannt")
		void create_idBundeslandUnbekannt() {
			final var dto = createRequest();
			dto.idBundesland = INVALID_ID_BUNDESLAND;

			when(ortRepository.ortsnameIsUniqueForPlzCreate(dto.ortsname, dto.plz)).thenReturn(true);

			assertThatException()
					.isThrownBy(() -> service.create(dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining(String.valueOf(INVALID_ID_BUNDESLAND))
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(ortRepository, never()).create(any(DTOOrt.class));
		}

		@Test
		@DisplayName("Legt Ort ohne idBundesland korrekt an (null erlaubt)")
		void create_ohneIdBundesland() {
			final var dto = createRequest();
			dto.idBundesland = null;

			when(ortRepository.ortsnameIsUniqueForPlzCreate(dto.ortsname, dto.plz)).thenReturn(true);
			when(mapper.toDomain(dto)).thenReturn(entity);
			when(ortRepository.create(entity)).thenReturn(entity);
			when(eigeneSchuleService.getSchuljahr()).thenReturn(2024);
			when(mapper.toApi(entity, 2024)).thenReturn(apiModel);

			final var result = service.create(dto);

			assertThat(result).isEqualTo(apiModel);
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		@Test
		@DisplayName("Aktualisiert Ort korrekt")
		void patch() {
			final var dto = new OrtPatchRequest();
			dto.ortsname = JsonNullable.of("Neuer Ortsname");

			when(ortRepository.getById(1L)).thenReturn(entity);
			when(ortRepository.ortsnameIsUniqueForPlzPatch("Neuer Ortsname", entity.plz, 1L)).thenReturn(true);
			when(eigeneSchuleService.getSchuljahr()).thenReturn(2024);
			when(mapper.toApi(entity, 2024)).thenReturn(apiModel);

			final var result = service.patch(1L, dto);

			assertThat(result).isEqualTo(apiModel);
			verify(mapper).patch(dto, entity);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn Ortsname für PLZ bereits bei anderem Ort vergeben")
		void patch_ortsnameNichtEindeutig() {
			final var dto = new OrtPatchRequest();
			dto.ortsname = JsonNullable.of("Köln");

			when(ortRepository.getById(1L)).thenReturn(entity);
			when(ortRepository.ortsnameIsUniqueForPlzPatch("Köln", entity.plz, 1L)).thenReturn(false);

			assertThatException()
					.isThrownBy(() -> service.patch(1L, dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("Köln")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(mapper, never()).patch(any(), any(DTOOrt.class));
		}

		@Test
		@DisplayName("Validiert Ortsname gegen neue PLZ wenn beide gesetzt")
		void patch_ortsnameGegenNeuePlz() {
			final var dto = new OrtPatchRequest();
			dto.ortsname = JsonNullable.of("Troisdorf");
			dto.plz = JsonNullable.of("50667");

			when(ortRepository.getById(1L)).thenReturn(entity);
			when(ortRepository.ortsnameIsUniqueForPlzPatch("Troisdorf", "50667", 1L)).thenReturn(true);
			when(eigeneSchuleService.getSchuljahr()).thenReturn(2024);
			when(mapper.toApi(entity, 2024)).thenReturn(apiModel);

			service.patch(1L, dto);

			verify(ortRepository).ortsnameIsUniqueForPlzPatch("Troisdorf", "50667", 1L);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn idBundesland unbekannt")
		void patch_idBundeslandUnbekannt() {
			final var dto = new OrtPatchRequest();
			dto.idBundesland = JsonNullable.of(INVALID_ID_BUNDESLAND);

			when(ortRepository.getById(1L)).thenReturn(entity);

			assertThatException()
					.isThrownBy(() -> service.patch(1L, dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining(String.valueOf(INVALID_ID_BUNDESLAND))
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(mapper, never()).patch(any(), any(DTOOrt.class));
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert — kein Validierungsaufruf")
		void patch_allUndefined() {
			final var dto = new OrtPatchRequest();

			when(ortRepository.getById(1L)).thenReturn(entity);
			when(eigeneSchuleService.getSchuljahr()).thenReturn(2024);
			when(mapper.toApi(entity, 2024)).thenReturn(apiModel);

			service.patch(1L, dto);

			verify(ortRepository, never()).ortsnameIsUniqueForPlzPatch(any(), any(), any(Long.class));
			verify(mapper).patch(dto, entity);
		}
	}

	// -------------------------------------------------------------------------
	// delete
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("delete")
	class Delete {

		@Test
		@DisplayName("Löscht nicht referenzierte Orte und gibt sortierte Responses zurück")
		void delete() {
			final var entity2 = new DTOOrt(2L, "50667", "Köln");

			when(ortRepository.getReferencedIds(List.of(2L, 1L))).thenReturn(Set.of());
			when(ortRepository.findListByIds(List.of(2L, 1L))).thenReturn(List.of(entity2, entity));
			when(ortRepository.delete(anyList())).thenReturn(List.of(entity2, entity));

			final var result = service.delete(List.of(2L, 1L));

			assertThat(result)
					.hasSize(2)
					.allMatch(r -> r.success);
			assertThat(result.stream().map(r -> r.id).toList())
					.containsExactly(1L, 2L);
		}

		@Test
		@DisplayName("Markiert referenzierte Orte als Fehler und löscht sie nicht")
		void delete_referenziert() {
			when(ortRepository.getReferencedIds(List.of(1L))).thenReturn(Set.of(1L));
			when(ortRepository.findListByIds(List.of(1L))).thenReturn(List.of(entity));
			when(ortRepository.delete(List.of())).thenReturn(List.of());

			final var result = service.delete(List.of(1L));

			assertThat(result).hasSize(1);
			assertThat(result.getFirst().success).isFalse();
			assertThat(result.getFirst().id).isEqualTo(1L);
			verify(ortRepository).delete(List.of());
		}

		@Test
		@DisplayName("Gibt Fehler zurück wenn ID nicht gefunden")
		void delete_nichtGefunden() {
			when(ortRepository.getReferencedIds(List.of(99L))).thenReturn(Set.of());
			when(ortRepository.findListByIds(List.of(99L))).thenReturn(List.of());
			when(ortRepository.delete(List.of())).thenReturn(List.of());

			final var result = service.delete(List.of(99L));

			assertThat(result).hasSize(1);
			assertThat(result.getFirst().success).isFalse();
			assertThat(result.getFirst().id).isEqualTo(99L);
		}

		@Test
		@DisplayName("Mischt erfolgreiche und referenzierte Einträge korrekt — sortiert nach ID")
		void delete_gemischt() {
			final var entity2 = new DTOOrt(2L, "50667", "Köln");

			when(ortRepository.getReferencedIds(List.of(1L, 2L))).thenReturn(Set.of(2L));
			when(ortRepository.findListByIds(List.of(1L, 2L))).thenReturn(List.of(entity, entity2));
			when(ortRepository.delete(List.of(entity))).thenReturn(List.of(entity));

			final var result = service.delete(List.of(1L, 2L));

			assertThat(result).hasSize(2);
			assertThat(result.stream().map(r -> r.id).toList()).containsExactly(1L, 2L);
			assertThat(result.stream().filter(r -> r.id == 1L).findFirst().orElseThrow().success).isTrue();
			assertThat(result.stream().filter(r -> r.id == 2L).findFirst().orElseThrow().success).isFalse();
		}
	}
}
