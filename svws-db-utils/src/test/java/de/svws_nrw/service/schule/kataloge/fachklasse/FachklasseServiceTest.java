package de.svws_nrw.service.schule.kataloge.fachklasse;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.FachklasseEintrag;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOFachklassen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.kataloge.fachklasse.FachklasseMapper;
import de.svws_nrw.repo.schule.kataloge.fachklasse.FachklasseRepository;
import de.svws_nrw.service.schule.SchuleService;
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
class FachklasseServiceTest {

	@Mock
	private FachklasseRepository repo;

	@Mock
	private FachklasseMapper mapper;

	@Mock
	private SchuleService schuleService;

	private FachklasseService service;

	private MockedStatic<TransactionSupport> transactionSupport;

	private DTOFachklassen entity;
	private FachklasseEintrag apiModel;

	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		entity = new DTOFachklassen(1L);
		entity.bezeichnung = "Anlagenmechaniker/-in";
		entity.kuerzel = "AM";

		apiModel = new FachklasseEintrag();
		apiModel.id = 1L;
		apiModel.bezeichnung = "Anlagenmechaniker/-in";
		apiModel.kuerzel = "AM";

		transactionSupport = mockStatic(TransactionSupport.class);
		transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		service = new FachklasseService(repo, mapper, schuleService);
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private FachklasseEintragCreateRequest createRequest() {
		final var dto = new FachklasseEintragCreateRequest();
		dto.idFachklasse = 5000L; // Anlagenmechaniker/-in — valider CoreType
		dto.bezeichnung = "Anlagenmechaniker/-in";
		dto.kuerzel = "AM";
		dto.istSichtbar = true;
		dto.sortierung = 100;
		return dto;
	}

	// -------------------------------------------------------------------------
	// getAll
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getAll")
	class GetAll {

		@Test
		@DisplayName("Gibt leere Liste zurück wenn keine Fachklassen vorhanden")
		void getAll_leer() {
			when(repo.getAll()).thenReturn(List.of());

			assertThat(service.getAll()).isEmpty();
			verify(mapper, never()).toApi(any(), any(Integer.class));
		}

		@Test
		@DisplayName("Gibt alle Fachklassen gemappt zurück")
		void getAll() {
			final var entity2 = new DTOFachklassen(2L);
			final var apiModel2 = new FachklasseEintrag();
			apiModel2.id = 2L;

			when(repo.getAll()).thenReturn(List.of(entity, entity2));
			when(mapper.toApi(entity, 0)).thenReturn(apiModel);
			when(mapper.toApi(entity2, 0)).thenReturn(apiModel2);

			assertThat(service.getAll()).containsExactly(apiModel, apiModel2);
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("create")
	class Create {

		@Test
		@DisplayName("Legt Fachklasse korrekt an")
		void create() {
			final var dto = createRequest();

			when(repo.kuerzelIsAlreadyUsedCreate(dto.kuerzel)).thenReturn(false);
			when(mapper.toDomain(dto)).thenReturn(entity);
			when(repo.create(entity)).thenReturn(entity);
			when(schuleService.getSchuljahr()).thenReturn(2024);
			when(mapper.toApi(entity, 2024)).thenReturn(apiModel);

			final var result = service.create(dto);

			assertThat(result).isEqualTo(apiModel);
			verify(repo).create(entity);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn Kürzel bereits vergeben")
		void create_kuerzelBereitsVergeben() {
			final var dto = createRequest();

			when(repo.kuerzelIsAlreadyUsedCreate(dto.kuerzel)).thenReturn(true);

			assertThatException()
					.isThrownBy(() -> service.create(dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining(dto.kuerzel)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(repo, never()).create(any(DTOFachklassen.class));
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn idFachklasse unbekannt")
		void create_idFachklasseUnbekannt() {
			final var dto = createRequest();
			dto.idFachklasse = -1L;

			when(repo.kuerzelIsAlreadyUsedCreate(dto.kuerzel)).thenReturn(false);

			assertThatException()
					.isThrownBy(() -> service.create(dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("-1")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(repo, never()).create(any(DTOFachklassen.class));
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn idDqrNiveau unbekannt")
		void create_idDqrNiveauUnbekannt() {
			final var dto = createRequest();
			dto.idDqrNiveau = -1;

			when(repo.kuerzelIsAlreadyUsedCreate(dto.kuerzel)).thenReturn(false);

			assertThatException()
					.isThrownBy(() -> service.create(dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("-1")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(repo, never()).create(any(DTOFachklassen.class));
		}

		@Test
		@DisplayName("Legt Fachklasse ohne idDqrNiveau korrekt an (null erlaubt)")
		void create_ohneIdDqrNiveau() {
			final var dto = createRequest();
			dto.idDqrNiveau = null;

			when(repo.kuerzelIsAlreadyUsedCreate(dto.kuerzel)).thenReturn(false);
			when(mapper.toDomain(dto)).thenReturn(entity);
			when(repo.create(entity)).thenReturn(entity);
			when(schuleService.getSchuljahr()).thenReturn(2024);
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
		@DisplayName("Aktualisiert Fachklasse korrekt")
		void patch() {
			final var dto = new FachklasseEintragPatchRequest();
			dto.bezeichnung = JsonNullable.of("Neue Bezeichnung");

			when(repo.getById(1L)).thenReturn(entity);
			when(schuleService.getSchuljahr()).thenReturn(2024);
			when(mapper.toApi(entity, 2024)).thenReturn(apiModel);

			final var result = service.patch(1L, dto);

			assertThat(result).isEqualTo(apiModel);
			verify(mapper).patch(dto, entity);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn Kürzel bereits bei anderer Fachklasse vergeben")
		void patch_kuerzelBereitsVergeben() {
			final var dto = new FachklasseEintragPatchRequest();
			dto.kuerzel = JsonNullable.of("DK");

			when(repo.getById(1L)).thenReturn(entity);
			when(repo.kuerzelIsAlreadyUsedPatch("DK", 1L)).thenReturn(true);

			assertThatException()
					.isThrownBy(() -> service.patch(1L, dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("DK")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(mapper, never()).patch(any(), any(DTOFachklassen.class));
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn idFachklasse unbekannt")
		void patch_idFachklasseUnbekannt() {
			final var dto = new FachklasseEintragPatchRequest();
			dto.idFachklasse = JsonNullable.of(-1L);

			when(repo.getById(1L)).thenReturn(entity);

			assertThatException()
					.isThrownBy(() -> service.patch(1L, dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("-1")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(mapper, never()).patch(any(), any(DTOFachklassen.class));
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST wenn idDqrNiveau unbekannt")
		void patch_idDqrNiveauUnbekannt() {
			final var dto = new FachklasseEintragPatchRequest();
			dto.idDqrNiveau = JsonNullable.of(-1);

			when(repo.getById(1L)).thenReturn(entity);

			assertThatException()
					.isThrownBy(() -> service.patch(1L, dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("-1")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(mapper, never()).patch(any(), any(DTOFachklassen.class));
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert — kein Validierungsaufruf")
		void patch_allUndefined() {
			final var dto = new FachklasseEintragPatchRequest();

			when(repo.getById(1L)).thenReturn(entity);
			when(schuleService.getSchuljahr()).thenReturn(2024);
			when(mapper.toApi(entity, 2024)).thenReturn(apiModel);

			service.patch(1L, dto);

			verify(repo, never()).kuerzelIsAlreadyUsedPatch(any(), any(Long.class));
			verify(mapper).patch(dto, entity);
		}

		@Test
		@DisplayName("Mischt definierte und undefined Felder korrekt")
		void patch_mischtDefinierteUndUndefinierteFelder() {
			final var dto = new FachklasseEintragPatchRequest();
			dto.bezeichnung = JsonNullable.of("Geändert");

			when(repo.getById(1L)).thenReturn(entity);
			when(schuleService.getSchuljahr()).thenReturn(2024);
			when(mapper.toApi(entity, 2024)).thenReturn(apiModel);

			service.patch(1L, dto);

			verify(mapper).patch(dto, entity);
			verify(repo, never()).kuerzelIsAlreadyUsedPatch(any(), any(Long.class));
		}
	}

	// -------------------------------------------------------------------------
	// delete
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("delete")
	class Delete {

		@Test
		@DisplayName("Löscht gefundene Fachklassen und gibt sortierte Responses zurück")
		void delete() {
			final var entity2 = new DTOFachklassen(2L);

			when(repo.findListByIds(List.of(2L, 1L))).thenReturn(List.of(entity2, entity));
			when(repo.delete(anyList())).thenReturn(List.of(entity2, entity));

			final var result = service.delete(List.of(2L, 1L));

			assertThat(result)
					.hasSize(2)
					.allMatch(r -> r.success);
			assertThat(result.stream().map(r -> r.id).toList())
					.containsExactly(1L, 2L);
		}

		@Test
		@DisplayName("Gibt leere Liste zurück wenn keine IDs gefunden")
		void delete_keineGefunden() {
			when(repo.findListByIds(List.of(99L))).thenReturn(List.of());
			when(repo.delete(List.of())).thenReturn(List.of());

			assertThat(service.delete(List.of(99L))).isEmpty();
		}
	}
}
