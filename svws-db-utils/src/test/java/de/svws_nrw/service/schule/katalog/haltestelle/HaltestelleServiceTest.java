package de.svws_nrw.service.schule.katalog.haltestelle;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Haltestelle;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.haltestelle.HaltestelleMapper;
import de.svws_nrw.repo.schule.kataloge.haltestelle.HaltestelleRepository;
import de.svws_nrw.service.utils.BulkDeleteUtils;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HaltestelleServiceTest {

	@Mock
	private HaltestelleRepository repository;

	@Mock
	private HaltestelleMapper mapper;

	private HaltestelleService service;

	private MockedStatic<TransactionSupport> transactionSupport;

	private DTOHaltestellen entity;
	private Haltestelle apiModel;

	@BeforeEach
	void setUp() {
		entity = new DTOHaltestellen(1L, "Troisdorf");

		apiModel = new Haltestelle();
		apiModel.id = 1L;
		apiModel.bezeichnung = "Troisdorf";

		transactionSupport = mockStatic(TransactionSupport.class);
		transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		service = new HaltestelleService(repository, mapper);
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	private HaltestelleCreateRequest createRequest() {
		final var dto = new HaltestelleCreateRequest();
		dto.bezeichnung = "Troisdorf";
		dto.entfernungSchule = 7.5;
		dto.sortierung = 1;
		dto.istSichtbar = true;
		return dto;
	}

	// -------------------------------------------------------------------------
	// getAll
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getAll")
	class GetAll {

		@Test
		@DisplayName("Gibt leere Liste zurück, wenn keine Haltestellen vorhanden sind")
		void getAll_leer() {
			when(repository.getAll()).thenReturn(List.of());
			when(repository.getReferencedIds(List.of())).thenReturn(Set.of());

			assertThat(service.getAll()).isEmpty();
			verify(mapper, never()).toApi(any());
		}

		@Test
		@DisplayName("Gibt alle Haltestellen gemappt zurück und setzt referenziertInAnderenTabellen korrekt")
		void getAll_setztReferenzFlag() {
			final var entity2 = new DTOHaltestellen(2L, "Bonn");
			final var apiModel2 = new Haltestelle();
			apiModel2.id = 2L;

			when(repository.getAll()).thenReturn(List.of(entity, entity2));
			when(repository.getReferencedIds(List.of(1L, 2L))).thenReturn(Set.of(2L));
			when(mapper.toApi(entity)).thenReturn(apiModel);
			when(mapper.toApi(entity2)).thenReturn(apiModel2);

			final var result = service.getAll();

			assertThat(result).containsExactly(apiModel, apiModel2);
			assertThat(apiModel.referenziertInAnderenTabellen).isFalse();
			assertThat(apiModel2.referenziertInAnderenTabellen).isTrue();
		}

		@Test
		@DisplayName("Sortiert die Haltestellen aufsteigend nach ID")
		void getAll_sortiertNachId() {
			final var entity2 = new DTOHaltestellen(2L, "Bonn");
			final var apiModel2 = new Haltestelle();
			apiModel2.id = 2L;

			when(repository.getAll()).thenReturn(List.of(entity2, entity));
			when(repository.getReferencedIds(List.of(2L, 1L))).thenReturn(Set.of());
			when(mapper.toApi(entity)).thenReturn(apiModel);
			when(mapper.toApi(entity2)).thenReturn(apiModel2);

			final var result = service.getAll();

			assertThat(result).containsExactly(apiModel, apiModel2);
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("create")
	class Create {

		@Test
		@DisplayName("Legt Haltestelle korrekt an")
		void create() {
			final var dto = createRequest();

			when(repository.bezeichnungExists(dto.bezeichnung)).thenReturn(false);
			when(mapper.toDomain(dto)).thenReturn(entity);
			when(repository.create(entity)).thenReturn(entity);
			when(mapper.toApi(entity)).thenReturn(apiModel);

			final var result = service.create(dto);

			assertThat(result).isEqualTo(apiModel);
			verify(repository).create(entity);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST, wenn die Bezeichnung bereits vergeben ist")
		void create_bezeichnungNichtEindeutig() {
			final var dto = createRequest();

			when(repository.bezeichnungExists(dto.bezeichnung)).thenReturn(true);

			assertThatException()
					.isThrownBy(() -> service.create(dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining(dto.bezeichnung)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(repository, never()).create(any(DTOHaltestellen.class));
			verify(mapper, never()).toDomain(any(HaltestelleCreateRequest.class));
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		@Test
		@DisplayName("Aktualisiert Haltestelle korrekt (Bezeichnung gesetzt)")
		void patch_bezeichnung() {
			final var dto = new HaltestellePatchRequest();
			dto.bezeichnung = JsonNullable.of("Neue Bezeichnung");

			when(repository.findById(1L)).thenReturn(Optional.of(entity));
			when(repository.bezeichnungIstBereitsVergebenExceptId("Neue Bezeichnung", 1L)).thenReturn(false);
			when(mapper.toApi(entity)).thenReturn(apiModel);

			final var result = service.patch(1L, dto);

			assertThat(result).isEqualTo(apiModel);
			verify(mapper).patch(dto, entity);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST, wenn die Bezeichnung bei einer anderen Haltestelle vergeben ist")
		void patch_bezeichnungNichtEindeutig() {
			final var dto = new HaltestellePatchRequest();
			dto.bezeichnung = JsonNullable.of("Bonn");

			when(repository.findById(1L)).thenReturn(Optional.of(entity));
			when(repository.bezeichnungIstBereitsVergebenExceptId("Bonn", 1L)).thenReturn(true);

			assertThatException()
					.isThrownBy(() -> service.patch(1L, dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("Bonn")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(mapper, never()).patch(any(), any(DTOHaltestellen.class));
		}

		@Test
		@DisplayName("Prüft die Eindeutigkeit mit Ausschluss der eigenen ID")
		void patch_schliesstEigeneIdAus() {
			final var dto = new HaltestellePatchRequest();
			dto.bezeichnung = JsonNullable.of("Troisdorf");

			when(repository.findById(1L)).thenReturn(Optional.of(entity));
			when(repository.bezeichnungIstBereitsVergebenExceptId("Troisdorf", 1L)).thenReturn(false);
			when(mapper.toApi(entity)).thenReturn(apiModel);

			service.patch(1L, dto);

			verify(repository).bezeichnungIstBereitsVergebenExceptId("Troisdorf", 1L);
			verify(repository, never()).bezeichnungExists(any());
		}

		@Test
		@DisplayName("Lässt undefined Felder unverändert — keine Eindeutigkeitsprüfung")
		void patch_allUndefined() {
			final var dto = new HaltestellePatchRequest();

			when(repository.findById(1L)).thenReturn(Optional.of(entity));
			when(mapper.toApi(entity)).thenReturn(apiModel);

			service.patch(1L, dto);

			verify(repository, never()).bezeichnungIstBereitsVergebenExceptId(any(), anyLong());
			verify(mapper).patch(dto, entity);
		}

		@Test
		@DisplayName("Wirft NOT_FOUND, wenn keine Haltestelle zur ID existiert")
		void patch_unbekannteId() {
			final var dto = new HaltestellePatchRequest();
			dto.bezeichnung = JsonNullable.of("Neue Bezeichnung");

			when(repository.findById(99L)).thenReturn(Optional.empty());

			assertThatException()
					.isThrownBy(() -> service.patch(99L, dto))
					.isInstanceOf(ApiOperationException.class)
					.withMessageContaining("99")
					.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);

			verify(repository, never()).bezeichnungIstBereitsVergebenExceptId(any(), anyLong());
			verify(mapper, never()).patch(any(), any(DTOHaltestellen.class));
		}
	}

	// -------------------------------------------------------------------------
	// delete
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("delete")
	class Delete {

		@Test
		@DisplayName("Delegiert an BulkDeleteUtils.deleteWithReferenceCheck(...) und liefert das Ergebnis zurück")
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
								eq("Haltestelle")))
						.thenReturn(List.of(r1, r2));

				final var result = service.delete(ids);

				assertThat(result).containsExactly(r1, r2);
				bulkDeleteUtils.verify(() -> BulkDeleteUtils.deleteWithReferenceCheck(
						eq(ids),
						eq(repository),
						any(),
						eq("Haltestelle")));
			}
		}
	}
}
