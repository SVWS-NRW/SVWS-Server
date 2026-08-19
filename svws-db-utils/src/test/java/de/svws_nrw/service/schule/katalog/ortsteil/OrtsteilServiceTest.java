package de.svws_nrw.service.schule.katalog.ortsteil;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.ortsteil.OrtsteilMapper;
import de.svws_nrw.repo.schule.kataloge.ort.OrtRepository;
import de.svws_nrw.repo.schule.kataloge.ortsteil.OrtsteilRepository;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
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
class OrtsteilServiceTest {

	@Mock
	private OrtsteilRepository ortsteilRepository;

	@Mock
	private OrtRepository ortRepository;

	@Mock
	private OrtsteilMapper mapper;

	private OrtsteilService service;

	private MockedStatic<TransactionSupport> transactionSupport;

	private DTOOrtsteil entity;
	private DTOOrt ort;
	private OrtsteilKatalogEintrag apiModel;

	@BeforeEach
	void setUp() {
		entity = new DTOOrtsteil(1L, "Sieglar");
		entity.idOrt = 42L;

		ort = new DTOOrt(42L, "53840", "Troisdorf");

		apiModel = new OrtsteilKatalogEintrag();
		apiModel.id = 1L;
		apiModel.ortsteil = "Sieglar";
		apiModel.idOrt = 42L;

		transactionSupport = mockStatic(TransactionSupport.class);
		transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		service = new OrtsteilService(ortsteilRepository, ortRepository, mapper);
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private OrtsteilCreateRequest createRequest() {
		final var dto = new OrtsteilCreateRequest();
		dto.ortsteil = "Sieglar";
		dto.idOrt = 42L;
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
		@DisplayName("Gibt leere Liste zurück wenn keine Ortsteile vorhanden")
		void getAll_leer() {
			when(ortsteilRepository.getAll()).thenReturn(List.of());
			when(ortsteilRepository.getReferencedIds(List.of())).thenReturn(Set.of());
			when(ortRepository.findMapByIds(Set.of())).thenReturn(java.util.Map.of());

			assertThat(service.getAll()).isEmpty();
			verify(mapper, never()).toApi(any(), any());
		}

		@Test
		@DisplayName("Gibt alle Ortsteile gemappt zurück und setzt referenziertInAnderenTabellen korrekt")
		void getAll() {
			final var entity2 = new DTOOrtsteil(2L, "Mitte");
			entity2.idOrt = 42L;
			final var apiModel2 = new OrtsteilKatalogEintrag();
			apiModel2.id = 2L;

			when(ortsteilRepository.getAll()).thenReturn(List.of(entity, entity2));
			when(ortsteilRepository.getReferencedIds(List.of(1L, 2L))).thenReturn(Set.of(2L));
			when(ortRepository.findMapByIds(Set.of(42L))).thenReturn(java.util.Map.of(42L, ort));
			when(mapper.toApi(entity, ort)).thenReturn(apiModel);
			when(mapper.toApi(entity2, ort)).thenReturn(apiModel2);

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
		@DisplayName("Legt Ortsteil korrekt an")
		void create() {
			final var dto = createRequest();

			when(ortsteilRepository.ortsteilnameIsUniqueForIdOrtCreate(dto.ortsteil, dto.idOrt)).thenReturn(true);
			when(ortRepository.findById(dto.idOrt)).thenReturn(Optional.of(ort));
			when(mapper.toDomain(dto)).thenReturn(entity);
			when(ortsteilRepository.create(entity)).thenReturn(entity);
			when(mapper.toApi(entity, ort)).thenReturn(apiModel);

			final var result = service.create(dto);

			assertThat(result).isEqualTo(apiModel);
			verify(ortsteilRepository).create(entity);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn Ortsteilname für idOrt bereits vergeben")
		void create_ortsteilnameNichtEindeutig() {
			final var dto = createRequest();

			when(ortsteilRepository.ortsteilnameIsUniqueForIdOrtCreate(dto.ortsteil, dto.idOrt)).thenReturn(false);

			assertThatException()
					.isThrownBy(() -> service.create(dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining(dto.ortsteil)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(ortsteilRepository, never()).create(any(DTOOrtsteil.class));
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn idOrt unbekannt")
		void create_idOrtUnbekannt() {
			final var dto = createRequest();

			when(ortsteilRepository.ortsteilnameIsUniqueForIdOrtCreate(dto.ortsteil, dto.idOrt)).thenReturn(true);
			when(ortRepository.findById(dto.idOrt)).thenReturn(Optional.empty());

			assertThatException()
					.isThrownBy(() -> service.create(dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining(String.valueOf(dto.idOrt))
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(ortsteilRepository, never()).create(any(DTOOrtsteil.class));
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		@Test
		@DisplayName("Aktualisiert Ortsteil korrekt")
		void patch() {
			final var dto = new OrtsteilPatchRequest();
			dto.ortsteil = JsonNullable.of("Mitte");

			when(ortsteilRepository.getById(1L)).thenReturn(entity);
			when(ortRepository.findById(42L)).thenReturn(Optional.of(ort));
			when(ortsteilRepository.ortsteilnameIsUniqueForIdOrtPatch("Mitte", 42L, 1L)).thenReturn(true);
			when(mapper.toApi(entity, ort)).thenReturn(apiModel);

			final var result = service.patch(1L, dto);

			assertThat(result).isEqualTo(apiModel);
			verify(mapper).patch(dto, entity);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn Ortsteilname für idOrt bereits bei anderem Ortsteil vergeben")
		void patch_ortsteilnameNichtEindeutig() {
			final var dto = new OrtsteilPatchRequest();
			dto.ortsteil = JsonNullable.of("Mitte");

			when(ortsteilRepository.getById(1L)).thenReturn(entity);
			when(ortRepository.findById(42L)).thenReturn(Optional.of(ort));
			when(ortsteilRepository.ortsteilnameIsUniqueForIdOrtPatch("Mitte", 42L, 1L)).thenReturn(false);

			assertThatException()
					.isThrownBy(() -> service.patch(1L, dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("Mitte")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(mapper, never()).patch(any(), any(DTOOrtsteil.class));
		}

		@Test
		@DisplayName("Validiert Ortsteilname gegen neue idOrt wenn beide gesetzt")
		void patch_ortsteilnameGegenNeueIdOrt() {
			final var neuerOrt = new DTOOrt(99L, "50667", "Köln");
			final var dto = new OrtsteilPatchRequest();
			dto.ortsteil = JsonNullable.of("Sieglar");
			dto.idOrt = JsonNullable.of(99L);

			when(ortsteilRepository.getById(1L)).thenReturn(entity);
			when(ortRepository.findById(99L)).thenReturn(Optional.of(neuerOrt));
			when(ortsteilRepository.ortsteilnameIsUniqueForIdOrtPatch("Sieglar", 99L, 1L)).thenReturn(true);
			when(mapper.toApi(entity, neuerOrt)).thenReturn(apiModel);

			service.patch(1L, dto);

			verify(ortsteilRepository).ortsteilnameIsUniqueForIdOrtPatch("Sieglar", 99L, 1L);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn neue idOrt unbekannt")
		void patch_idOrtUnbekannt() {
			final var dto = new OrtsteilPatchRequest();
			dto.idOrt = JsonNullable.of(99L);

			when(ortsteilRepository.getById(1L)).thenReturn(entity);
			when(ortRepository.findById(99L)).thenReturn(Optional.empty());

			assertThatException()
					.isThrownBy(() -> service.patch(1L, dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("99")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(mapper, never()).patch(any(), any(DTOOrtsteil.class));
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert — kein Validierungsaufruf für Ortsteilname")
		void patch_allUndefined() {
			final var dto = new OrtsteilPatchRequest();

			when(ortsteilRepository.getById(1L)).thenReturn(entity);
			when(ortRepository.findById(42L)).thenReturn(Optional.of(ort));
			when(mapper.toApi(entity, ort)).thenReturn(apiModel);

			service.patch(1L, dto);

			verify(ortsteilRepository, never()).ortsteilnameIsUniqueForIdOrtPatch(any(), any(), any(Long.class));
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
		@DisplayName("Löscht nicht referenzierte Ortsteile und gibt sortierte Responses zurück")
		void delete() {
			final var entity2 = new DTOOrtsteil(2L, "Mitte");

			when(ortsteilRepository.getReferencedIds(List.of(2L, 1L))).thenReturn(Set.of());
			when(ortsteilRepository.findListByIds(List.of(2L, 1L))).thenReturn(List.of(entity2, entity));
			when(ortsteilRepository.delete(anyList())).thenReturn(List.of(entity2, entity));

			final var result = service.delete(List.of(2L, 1L));

			assertThat(result)
					.hasSize(2)
					.allMatch(r -> r.success);
			assertThat(result.stream().map(r -> r.id).toList())
					.containsExactly(1L, 2L);
		}

		@Test
		@DisplayName("Markiert referenzierte Ortsteile als Fehler und löscht sie nicht")
		void delete_referenziert() {
			when(ortsteilRepository.getReferencedIds(List.of(1L))).thenReturn(Set.of(1L));
			when(ortsteilRepository.findListByIds(List.of(1L))).thenReturn(List.of(entity));
			when(ortsteilRepository.delete(List.of())).thenReturn(List.of());

			final var result = service.delete(List.of(1L));

			assertThat(result).hasSize(1);
			assertThat(result.getFirst().success).isFalse();
			assertThat(result.getFirst().id).isEqualTo(1L);
			verify(ortsteilRepository).delete(List.of());
		}

		@Test
		@DisplayName("Gibt Fehler zurück wenn ID nicht gefunden")
		void delete_nichtGefunden() {
			when(ortsteilRepository.getReferencedIds(List.of(99L))).thenReturn(Set.of());
			when(ortsteilRepository.findListByIds(List.of(99L))).thenReturn(List.of());
			when(ortsteilRepository.delete(List.of())).thenReturn(List.of());

			final var result = service.delete(List.of(99L));

			assertThat(result).hasSize(1);
			assertThat(result.getFirst().success).isFalse();
			assertThat(result.getFirst().id).isEqualTo(99L);
		}

		@Test
		@DisplayName("Mischt erfolgreiche und referenzierte Einträge korrekt — sortiert nach ID")
		void delete_gemischt() {
			final var entity2 = new DTOOrtsteil(2L, "Mitte");

			when(ortsteilRepository.getReferencedIds(List.of(1L, 2L))).thenReturn(Set.of(2L));
			when(ortsteilRepository.findListByIds(List.of(1L, 2L))).thenReturn(List.of(entity, entity2));
			when(ortsteilRepository.delete(List.of(entity))).thenReturn(List.of(entity));

			final var result = service.delete(List.of(1L, 2L));

			assertThat(result).hasSize(2);
			assertThat(result.stream().map(r -> r.id).toList()).containsExactly(1L, 2L);
			assertThat(result.stream().filter(r -> r.id == 1L).findFirst().orElseThrow().success).isTrue();
			assertThat(result.stream().filter(r -> r.id == 2L).findFirst().orElseThrow().success).isFalse();
		}

		@Test
		@DisplayName("Gibt Fehler zurück wenn Ortsteil gefunden aber nicht gelöscht werden konnte")
		void delete_loeschenFehlgeschlagen() {
			when(ortsteilRepository.getReferencedIds(List.of(1L))).thenReturn(Set.of());
			when(ortsteilRepository.findListByIds(List.of(1L))).thenReturn(List.of(entity));
			when(ortsteilRepository.delete(List.of(entity))).thenReturn(List.of());

			final var result = service.delete(List.of(1L));

			assertThat(result).hasSize(1);
			assertThat(result.getFirst().success).isFalse();
			assertThat(result.getFirst().id).isEqualTo(1L);
		}
	}
}
