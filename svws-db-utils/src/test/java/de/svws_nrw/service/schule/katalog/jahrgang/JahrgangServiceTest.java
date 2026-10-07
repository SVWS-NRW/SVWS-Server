package de.svws_nrw.service.schule.katalog.jahrgang;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.mapper.schule.katalog.jahrgang.JahrgangMapper;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JahrgangServiceTest {

	@Mock
	private JahrgangRepository repository;

	@Mock
	private JahrgangMapper mapper;

	@Mock
	private EigeneSchuleService eigeneSchuleService;

	private JahrgangService service;

	private static final int SCHULJAHR = 2024;
	private static final Schulform SCHULFORM = Schulform.GY;

	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		service = new JahrgangService(repository, mapper, eigeneSchuleService);
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOJahrgang createEntity(final long id) {
		return new DTOJahrgang(id);
	}

	private JahrgangsDaten createApiModel(final long id) {
		final var daten = new JahrgangsDaten();
		daten.id = id;
		return daten;
	}

	// -------------------------------------------------------------------------
	// getAll
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getAll")
	class GetAll {

		@BeforeEach
		void setUp() {
			when(eigeneSchuleService.getSchuljahr()).thenReturn(SCHULJAHR);
			when(eigeneSchuleService.getSchulform()).thenReturn(SCHULFORM);
		}

		@Test
		@DisplayName("Gibt leere Liste zurück wenn keine Jahrgänge vorhanden")
		void getAll_leer() {
			when(repository.getAll()).thenReturn(List.of());
			when(repository.getReferencedIds(List.of())).thenReturn(Set.of());

			final var result = service.getAll();

			assertThat(result).isEmpty();
			verify(mapper, never()).toApi(any(), anyInt(), any());
		}

		@Test
		@DisplayName("Gibt alle Jahrgänge gemappt zurück und setzt referenziertInAnderenTabellen korrekt")
		void getAll_mapptUndSetztReferenzFlag() {
			final var entity1 = createEntity(1L);
			final var entity3 = createEntity(3L);
			final var apiModel1 = createApiModel(1L);
			final var apiModel3 = createApiModel(3L);

			when(repository.getAll()).thenReturn(List.of(entity1, entity3));
			when(repository.getReferencedIds(List.of(1L, 3L))).thenReturn(Set.of(3L));
			when(mapper.toApi(entity1, SCHULJAHR, SCHULFORM)).thenReturn(apiModel1);
			when(mapper.toApi(entity3, SCHULJAHR, SCHULFORM)).thenReturn(apiModel3);

			final var result = service.getAll();

			assertThat(result).containsExactly(apiModel1, apiModel3);
			assertThat(apiModel1.referenziertInAnderenTabellen).isFalse();
			assertThat(apiModel3.referenziertInAnderenTabellen).isTrue();
		}

		@Test
		@DisplayName("Sortiert die Jahrgänge aufsteigend nach ID")
		void getAll_sortiertNachId() {
			final var entity1 = createEntity(1L);
			final var entity3 = createEntity(3L);
			final var apiModel1 = createApiModel(1L);
			final var apiModel3 = createApiModel(3L);

			when(repository.getAll()).thenReturn(List.of(entity3, entity1));
			when(repository.getReferencedIds(List.of(3L, 1L))).thenReturn(Set.of());
			when(mapper.toApi(entity3, SCHULJAHR, SCHULFORM)).thenReturn(apiModel3);
			when(mapper.toApi(entity1, SCHULJAHR, SCHULFORM)).thenReturn(apiModel1);

			final var result = service.getAll();

			assertThat(result).containsExactly(apiModel1, apiModel3);
		}
	}

	// -------------------------------------------------------------------------
	// getById
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getById")
	class GetById {

		@BeforeEach
		void setUp() {
			when(eigeneSchuleService.getSchuljahr()).thenReturn(SCHULJAHR);
			when(eigeneSchuleService.getSchulform()).thenReturn(SCHULFORM);
		}

		@Test
		@DisplayName("Gibt den gemappten Jahrgang zurück, wenn die ID existiert")
		void getById_gefunden() {
			final var entity = createEntity(5L);
			final var apiModel = createApiModel(1L);

			when(repository.findById(5L)).thenReturn(Optional.of(entity));
			when(mapper.toApi(entity, SCHULJAHR, SCHULFORM)).thenReturn(apiModel);

			final var result = service.getById(5L);

			assertThat(result).isSameAs(apiModel);
		}

		@Test
		@DisplayName("Wirft 404, wenn kein Jahrgang zur ID existiert")
		void getById_nichtGefunden() {
			when(repository.findById(99L)).thenReturn(Optional.empty());

			assertThatThrownBy(() -> service.getById(99L))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
			verify(mapper, never()).toApi(any(), anyInt(), any());
		}
	}

	// -------------------------------------------------------------------------
	// DeletebyIds
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("delete")
	class DeletebyIds {

		private MockedStatic<TransactionSupport> transactionSupport;

		@BeforeEach
		void setUp() {
			transactionSupport = mockStatic(TransactionSupport.class);
			transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
					.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
		}

		@AfterEach
		void tearDown() {
			transactionSupport.close();
		}

		@Test
		@DisplayName("Delegiert an BulkDeleteUtils.deleteWithReferenceCheck und liefert dessen Ergebnis zurück")
		void delete_delegiert() {
			final var ids = List.of(2L, 1L);

			final var antwort1 = SimpleOperationResponse.ofSuccess(1L);
			final var antwort2 = SimpleOperationResponse.ofError(2L, "referenziert");

			try (MockedStatic<BulkDeleteUtils> bulkDeleteUtils = mockStatic(BulkDeleteUtils.class)) {
				bulkDeleteUtils.when(() -> BulkDeleteUtils.deleteWithReferenceCheck(
							eq(ids), eq(repository), any(), eq("Jahrgang")))
						.thenReturn(List.of(antwort1, antwort2));

				final var result = service.delete(ids);

				assertThat(result).containsExactly(antwort1, antwort2);
				bulkDeleteUtils.verify(() -> BulkDeleteUtils.delete(any(), any(), any(), any()), never());
			}
		}

		@Test
		@DisplayName("delete läuft innerhalb einer Transaktion")
		void delete_laeuftInTransaktion() {
			service.delete(List.of(2L, 1L));

			transactionSupport.verify(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()));
		}

		@Test
		@DisplayName("Übergibt einen ID-Extractor, der die ID aus der Entität liest")
		void delete_uebergibtIdExtractor() {
			try (MockedStatic<BulkDeleteUtils> bulkDeleteUtils = mockStatic(BulkDeleteUtils.class)) {
				final ArgumentCaptor<Function<DTOJahrgang, Long>> captor = ArgumentCaptor.captor();

				service.delete(List.of(2L));

				bulkDeleteUtils.verify(() -> BulkDeleteUtils.deleteWithReferenceCheck(any(), any(), captor.capture(), eq("Jahrgang")));
				assertThat(captor.getValue().apply(createEntity(1L))).isEqualTo(1L);
			}
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("create")
	class Create {

		private MockedStatic<TransactionSupport> transactionSupport;

		@BeforeEach
		void setUp() {
			transactionSupport = mockStatic(TransactionSupport.class);
			transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
					.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
		}

		@AfterEach
		void tearDown() {
			transactionSupport.close();
		}

		private JahrgangCreateRequest minimalRequest() {
			final var request = new JahrgangCreateRequest();
			request.kuerzel = "05";
			request.bezeichnung = "Jahrgang 5";
			request.idJahrgang = 5000000L;
			return request;
		}

		@Test
		@DisplayName("Erfolg | legt den Jahrgang an und liefert die Jahrgangsdaten zurück")
		void create_erfolg() {
			final var request = minimalRequest();

			final var jahrgang = createEntity(0L);
			final var created = createEntity(1L);
			final var erwartet = createApiModel(1L);

			// Die Prüfung soll durchgehen: Kürzel und Bezeichnung sind noch frei
			when(repository.kuerzelIsAlreadyUsedCreate("05")).thenReturn(false);
			when(repository.bezeichnungIsAlreadyUsedCreate("Jahrgang 5")).thenReturn(false);

			when(mapper.toDomain(request)).thenReturn(jahrgang);
			when(repository.create(jahrgang)).thenReturn(created);
			when(eigeneSchuleService.getSchuljahr()).thenReturn(SCHULJAHR);
			when(eigeneSchuleService.getSchulform()).thenReturn(SCHULFORM);
			when(mapper.toApi(created, SCHULJAHR, SCHULFORM)).thenReturn(erwartet);

			final var result = service.create(request);

			assertThat(result).isSameAs(erwartet);
		}

		@Test
		@DisplayName("Wirft 400, wenn das Kürzel bereits vergeben ist")
		void create_kuerzelVergeben() {
			final var request = minimalRequest();

			when(repository.kuerzelIsAlreadyUsedCreate("05")).thenReturn(true);

			assertThatThrownBy(() -> service.create(request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessageContaining("Das Kürzel 05 wird bereits verwendet");

			verify(repository, never()).create(any(DTOJahrgang.class));
		}

		@Test
		@DisplayName("Wirft 400, wenn die Bezeichnung bereits vergeben ist")
		void create_bezeichnungVergeben() {
			final var request = minimalRequest();

			when(repository.kuerzelIsAlreadyUsedCreate("05")).thenReturn(false);
			when(repository.bezeichnungIsAlreadyUsedCreate("Jahrgang 5")).thenReturn(true);

			assertThatThrownBy(() -> service.create(request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessageContaining("Die Bezeichnung Jahrgang 5 wird bereits verwendet");

			verify(repository, never()).create(any(DTOJahrgang.class));
		}

		@Test
		@DisplayName("Wirft 400, wenn die idJahrgang im Katalog nicht existiert")
		void create_idJahrgangUnbekannt() {
			final var request = minimalRequest();
			request.idJahrgang = 999999L;

			when(repository.kuerzelIsAlreadyUsedCreate("05")).thenReturn(false);
			when(repository.bezeichnungIsAlreadyUsedCreate("Jahrgang 5")).thenReturn(false);

			assertThatThrownBy(() -> service.create(request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessageContaining("Kein Jahrgang mit der ID 999999");

			verify(repository, never()).create(any(DTOJahrgang.class));
		}

		@Test
		@DisplayName("Wirft 400, wenn die idSchulgliederung im Katalog nicht existiert")
		void create_idSchulgliederungUnbekannt() {
			final var request = minimalRequest();
			request.idSchulgliederung = 999999L;

			when(repository.kuerzelIsAlreadyUsedCreate("05")).thenReturn(false);
			when(repository.bezeichnungIsAlreadyUsedCreate("Jahrgang 5")).thenReturn(false);

			assertThatThrownBy(() -> service.create(request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessageContaining("Keine Schulgliederung mit der ID 999999");

			verify(repository, never()).create(any(DTOJahrgang.class));
		}

		@Test
		@DisplayName("Wirft 400, wenn die Schulgliederung nicht zur Schulform der Schule passt")
		void create_idSchulgliederungFalscheSchulform() {
			final var request = minimalRequest();
			// A01 existiert im Katalog, gilt aber nur fuer BK und SB - die Schule ist ein GY
			request.idSchulgliederung = 1001000L;

			when(repository.kuerzelIsAlreadyUsedCreate("05")).thenReturn(false);
			when(repository.bezeichnungIsAlreadyUsedCreate("Jahrgang 5")).thenReturn(false);
			when(eigeneSchuleService.getSchuljahr()).thenReturn(SCHULJAHR);
			when(eigeneSchuleService.getSchulform()).thenReturn(SCHULFORM);

			assertThatThrownBy(() -> service.create(request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessageContaining("nicht gültig");

			verify(repository, never()).create(any(DTOJahrgang.class));
		}

		@Test
		@DisplayName("Wirft 400, wenn die idBildungsstufe im Katalog nicht existiert")
		void create_idBildungsstufeUnbekannt() {
			final var request = minimalRequest();
			request.idBildungsstufe = 999L;

			when(repository.kuerzelIsAlreadyUsedCreate("05")).thenReturn(false);
			when(repository.bezeichnungIsAlreadyUsedCreate("Jahrgang 5")).thenReturn(false);

			assertThatThrownBy(() -> service.create(request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessageContaining("Keine Bildungsstufe zur ID 999");

			verify(repository, never()).create(any(DTOJahrgang.class));
		}

		@Test
		@DisplayName("Wirft 400, wenn der Folgejahrgang nicht existiert")
		void create_idFolgejahrgangUnbekannt() {
			final var request = minimalRequest();
			request.idFolgejahrgang = 77L;

			when(repository.kuerzelIsAlreadyUsedCreate("05")).thenReturn(false);
			when(repository.bezeichnungIsAlreadyUsedCreate("Jahrgang 5")).thenReturn(false);
			when(repository.existsById(77L)).thenReturn(false);

			assertThatThrownBy(() -> service.create(request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessageContaining("Ein Folgejahrgang mit der ID 77");

			verify(repository, never()).create(any(DTOJahrgang.class));
		}

		@Test
		@DisplayName("create läuft innerhalb einer Transaktion")
		void create_laeuftInTransaktion() {
			final var request = minimalRequest();

			when(repository.kuerzelIsAlreadyUsedCreate("05")).thenReturn(false);
			when(repository.bezeichnungIsAlreadyUsedCreate("Jahrgang 5")).thenReturn(false);

			service.create(request);

			transactionSupport.verify(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()));
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		private static final long ID = 1L;

		private MockedStatic<TransactionSupport> transactionSupport;

		@BeforeEach
		void setUp() {
			transactionSupport = mockStatic(TransactionSupport.class);
			transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
					.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
		}

		@AfterEach
		void tearDown() {
			transactionSupport.close();
		}

		@Test
		@DisplayName("Erfolg | wendet die Änderung an und liefert die Jahrgangsdaten zurück")
		void patch_erfolg() {
			final var request = new JahrgangPatchRequest();
			request.kuerzel = JsonNullable.of("05");

			final var entity = createEntity(ID);
			final var erwartet = createApiModel(ID);

			when(repository.findById(ID)).thenReturn(Optional.of(entity));
			when(repository.kuerzelIsAlreadyUsedPatch("05", ID)).thenReturn(false);
			when(eigeneSchuleService.getSchuljahr()).thenReturn(SCHULJAHR);
			when(eigeneSchuleService.getSchulform()).thenReturn(SCHULFORM);
			when(mapper.toApi(entity, SCHULJAHR, SCHULFORM)).thenReturn(erwartet);

			final var result = service.patch(ID, request);

			assertThat(result).isSameAs(erwartet);
			verify(mapper).patch(request, entity);
		}

		@Test
		@DisplayName("Prüft die Eindeutigkeit unter Ausschluss des eigenen Jahrgangs")
		void patch_prueftEindeutigkeitOhneSichSelbst() {
			final var request = new JahrgangPatchRequest();
			request.kuerzel = JsonNullable.of("05");
			request.bezeichnung = JsonNullable.of("Jahrgang 5");

			final var entity = createEntity(ID);

			when(repository.findById(ID)).thenReturn(Optional.of(entity));
			when(repository.kuerzelIsAlreadyUsedPatch("05", ID)).thenReturn(false);
			when(repository.bezeichnungIsAlreadyUsedPatch("Jahrgang 5", ID)).thenReturn(false);
			when(eigeneSchuleService.getSchuljahr()).thenReturn(SCHULJAHR);
			when(eigeneSchuleService.getSchulform()).thenReturn(SCHULFORM);

			service.patch(ID, request);

			verify(repository).kuerzelIsAlreadyUsedPatch("05", ID);
			verify(repository).bezeichnungIsAlreadyUsedPatch("Jahrgang 5", ID);
			verify(repository, never()).kuerzelIsAlreadyUsedCreate(any());
			verify(repository, never()).bezeichnungIsAlreadyUsedCreate(any());
		}

		@Test
		@DisplayName("Wirft 404, wenn kein Jahrgang zu der ID existiert")
		void patch_unbekannteId() {
			final var request = new JahrgangPatchRequest();
			request.kuerzel = JsonNullable.of("05");

			when(repository.findById(ID)).thenReturn(Optional.empty());

			assertThatThrownBy(() -> service.patch(ID, request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND)
					.hasMessageContaining("Kein Jahrgang zur ID 1 gefunden.");

			verify(mapper, never()).patch(any(), any());
		}

		@Test
		@DisplayName("Wirft 400, wenn das Kürzel von einem anderen Jahrgang verwendet wird")
		void patch_kuerzelVergeben() {
			final var request = new JahrgangPatchRequest();
			request.kuerzel = JsonNullable.of("05");

			when(repository.findById(ID)).thenReturn(Optional.of(createEntity(ID)));
			when(repository.kuerzelIsAlreadyUsedPatch("05", ID)).thenReturn(true);

			assertThatThrownBy(() -> service.patch(ID, request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessageContaining("Das Kürzel 05 wird bereits verwendet");

			verify(mapper, never()).patch(any(), any());
		}

		@Test
		@DisplayName("Wirft 400, wenn die Bezeichnung von einem anderen Jahrgang verwendet wird")
		void patch_bezeichnungVergeben() {
			final var request = new JahrgangPatchRequest();
			request.bezeichnung = JsonNullable.of("Jahrgang 5");

			when(repository.findById(ID)).thenReturn(Optional.of(createEntity(ID)));
			when(repository.bezeichnungIsAlreadyUsedPatch("Jahrgang 5", ID)).thenReturn(true);

			assertThatThrownBy(() -> service.patch(ID, request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessageContaining("Die Bezeichnung Jahrgang 5 wird bereits verwendet");

			verify(mapper, never()).patch(any(), any());
		}

		@Test
		@DisplayName("Wirft 400, wenn die idJahrgang unbekannt ist")
		void patch_idJahrgangUnbekannt() {
			final var request = new JahrgangPatchRequest();
			request.idJahrgang = JsonNullable.of(999999L);

			when(repository.findById(ID)).thenReturn(Optional.of(createEntity(ID)));

			assertThatThrownBy(() -> service.patch(ID, request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessageContaining("Kein Jahrgang mit der ID 999999 gefunden.");

			verify(mapper, never()).patch(any(), any());
		}

		@Test
		@DisplayName("Wirft 400, wenn die idSchulgliederung unbekannt ist")
		void patch_idSchulgliederungUnbekannt() {
			final var request = new JahrgangPatchRequest();
			request.idSchulgliederung = JsonNullable.of(999999L);

			when(repository.findById(ID)).thenReturn(Optional.of(createEntity(ID)));

			assertThatThrownBy(() -> service.patch(ID, request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessageContaining("Keine Schulgliederung mit der ID 999999 gefunden.");

			verify(mapper, never()).patch(any(), any());
		}

		@Test
		@DisplayName("Wirft 400, wenn die Schulgliederung nicht zur Schulform passt")
		void patch_idSchulgliederungFalscheSchulform() {
			final var request = new JahrgangPatchRequest();
			request.idSchulgliederung = JsonNullable.of(1001000L);

			when(repository.findById(ID)).thenReturn(Optional.of(createEntity(ID)));
			when(eigeneSchuleService.getSchuljahr()).thenReturn(SCHULJAHR);
			when(eigeneSchuleService.getSchulform()).thenReturn(SCHULFORM);

			assertThatThrownBy(() -> service.patch(ID, request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessageContaining("Die Schulgliederung ist für diese Schulform nicht gültig.");

			verify(mapper, never()).patch(any(), any());
		}

		@Test
		@DisplayName("Wirft 400, wenn die idBildungsstufe unbekannt ist")
		void patch_idBildungsstufeUnbekannt() {
			final var request = new JahrgangPatchRequest();
			request.idBildungsstufe = JsonNullable.of(999L);

			when(repository.findById(ID)).thenReturn(Optional.of(createEntity(ID)));

			assertThatThrownBy(() -> service.patch(ID, request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessageContaining("Keine Bildungsstufe zur ID 999 gefunden.");

			verify(mapper, never()).patch(any(), any());
		}

		@Test
		@DisplayName("Wirft 400, wenn der Folgejahrgang nicht existiert")
		void patch_idFolgejahrgangUnbekannt() {
			final var request = new JahrgangPatchRequest();
			request.idFolgejahrgang = JsonNullable.of(77L);

			when(repository.findById(ID)).thenReturn(Optional.of(createEntity(ID)));
			when(repository.existsById(77L)).thenReturn(false);

			assertThatThrownBy(() -> service.patch(ID, request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessageContaining("Ein Folgejahrgang mit der ID 77 wurde nicht gefunden.");

			verify(mapper, never()).patch(any(), any());
		}

		@Test
		@DisplayName("Validiert Felder nicht, die im Request nicht gesetzt sind")
		void patch_validiertNurGesetzteFelder() {
			final var request = new JahrgangPatchRequest();
			final var entity = createEntity(ID);

			when(repository.findById(ID)).thenReturn(Optional.of(entity));
			when(eigeneSchuleService.getSchuljahr()).thenReturn(SCHULJAHR);
			when(eigeneSchuleService.getSchulform()).thenReturn(SCHULFORM);

			service.patch(ID, request);

			verify(repository, never()).kuerzelIsAlreadyUsedPatch(any(), eq(ID));
			verify(repository, never()).bezeichnungIsAlreadyUsedPatch(any(), eq(ID));
			verify(repository, never()).existsById(any());
			verify(mapper).patch(request, entity);
		}

		@Test
		@DisplayName("patch läuft innerhalb einer Transaktion")
		void patch_laeuftInTransaktion() {
			final var request = new JahrgangPatchRequest();

			when(repository.findById(ID)).thenReturn(Optional.of(createEntity(ID)));
			when(eigeneSchuleService.getSchuljahr()).thenReturn(SCHULJAHR);
			when(eigeneSchuleService.getSchulform()).thenReturn(SCHULFORM);

			service.patch(ID, request);

			transactionSupport.verify(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()));
		}
	}
}
