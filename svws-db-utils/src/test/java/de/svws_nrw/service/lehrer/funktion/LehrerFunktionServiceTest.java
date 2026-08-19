package de.svws_nrw.service.lehrer.funktion;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerFunktion;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.katalog.DTOLeitungsfunktion;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerFunktion;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.funktion.LehrerFunktionMapper;
import de.svws_nrw.repo.RepositoryException;
import de.svws_nrw.repo.lehrer.funktion.LehrerFunktionRepository;
import de.svws_nrw.repo.lehrer.leitungsfunktion.LehrerLeitungsfunktionRepository;
import de.svws_nrw.repo.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenRepository;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LehrerFunktionServiceTest {

	@Mock
	private LehrerFunktionRepository repo;

	@Mock
	private LehrerPersonalabschnittsdatenRepository abschnittsdatenRepo;

	@Mock
	private LehrerLeitungsfunktionRepository leitungsfunktionRepo;

	@Mock
	private LehrerFunktionMapper mapper;

	@Mock
	private MockedStatic<TransactionSupport> transactionSupport;

	@InjectMocks
	private LehrerFunktionService service;

	private DTOLehrerFunktion entity;
	private LehrerFunktion apiModel;

	@BeforeEach
	void setUp() {
		entity = new DTOLehrerFunktion(1L, 10L, 20L);
		apiModel = new LehrerFunktion();
		apiModel.id = 1L;
		apiModel.idAbschnittsdaten = 10L;
		apiModel.idFunktion = 20L;
		transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	// -------------------------------------------------------------------------
	// get
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("get - not found")
	void get_notFound() {
		when(repo.getById(1L)).thenThrow(new RepositoryException("not found"));

		assertThatException()
				.isThrownBy(() -> service.get(1L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine Lehrerfunktion mit der ID 1 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("get")
	void get() {
		when(repo.getById(1L)).thenReturn(entity);
		when(mapper.toApi(entity)).thenReturn(apiModel);

		final var result = service.get(1L);

		assertThat(result).isEqualTo(apiModel);
	}

	// -------------------------------------------------------------------------
	// getAll
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getAll - leer")
	void getAll_empty() {
		when(repo.getAll()).thenReturn(Collections.emptyList());

		assertThat(service.getAll()).isEmpty();
		verify(mapper, never()).toApi(any());
	}

	@Test
	@DisplayName("getAll")
	void getAll() {
		final var entity2 = new DTOLehrerFunktion(2L, 10L, 21L);
		final var apiModel2 = new LehrerFunktion();
		apiModel2.id = 2L;

		when(repo.getAll()).thenReturn(List.of(entity, entity2));
		when(mapper.toApi(entity)).thenReturn(apiModel);
		when(mapper.toApi(entity2)).thenReturn(apiModel2);

		final var result = service.getAll();

		assertThat(result).containsExactly(apiModel, apiModel2);
	}

	// -------------------------------------------------------------------------
	// getListByIdAbschnitt
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getListByIdAbschnitt - keine Eintraege")
	void getListByIdAbschnitt_empty() {
		when(repo.findAllByIdAbschnitt(10L)).thenReturn(Collections.emptyList());

		assertThat(service.getListByIdAbschnitt(10L)).isEmpty();
		verify(mapper, never()).toApi(any());
	}

	@Test
	@DisplayName("getListByIdAbschnitt")
	void getListByIdAbschnitt() {
		when(repo.findAllByIdAbschnitt(10L)).thenReturn(List.of(entity));
		when(mapper.toApi(entity)).thenReturn(apiModel);

		final var result = service.getListByIdAbschnitt(10L);

		assertThat(result).containsExactly(apiModel);
	}

	// -------------------------------------------------------------------------
	// getListByIdLehrerAbschnittsdaten
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getListByIdLehrerAbschnittsdaten - mappt gruppiert und konvertiert per Mapper")
	void getListByIdLehrerAbschnittsdaten() {
		final var dto1 = new DTOLehrerFunktion(1L, 10L, 20L);
		final var dto2 = new DTOLehrerFunktion(2L, 10L, 21L);
		final var dto3 = new DTOLehrerFunktion(3L, 11L, 22L);

		final var api1 = new LehrerFunktion(); api1.id = 1L;
		final var api2 = new LehrerFunktion(); api2.id = 2L;
		final var api3 = new LehrerFunktion(); api3.id = 3L;

		when(repo.getListByIdLehrerAbschnittsdaten(List.of(10L, 11L)))
				.thenReturn(Map.of(
						10L, List.of(dto1, dto2),
						11L, List.of(dto3)
				));

		when(mapper.toApi(dto1)).thenReturn(api1);
		when(mapper.toApi(dto2)).thenReturn(api2);
		when(mapper.toApi(dto3)).thenReturn(api3);

		final var result = service.getListByIdLehrerAbschnittsdaten(List.of(10L, 11L));

		assertThat(result).hasSize(2);
		assertThat(result.get(10L)).containsExactly(api1, api2);
		assertThat(result.get(11L)).containsExactly(api3);
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("create - Duplikat")
	void create_duplicate() {
		final var dto = new LehrerFunktionCreateRequest();
		dto.idAbschnittsdaten = 10L;
		dto.idFunktion = 20L;

		when(abschnittsdatenRepo.findById(10L)).thenReturn(Optional.of(mock(DTOLehrerAbschnittsdaten.class)));
		when(leitungsfunktionRepo.findById(20L)).thenReturn(Optional.of(mock(DTOLeitungsfunktion.class)));
		when(repo.existsByIdAbschnittAndIdFunktion(10L, 20L)).thenReturn(true);

		assertThatException()
				.isThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Lehrerfunktion mit der ID 20 im Abschnitt mit der ID 10 existiert bereits.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create")
	void create() {
		final var dto = new LehrerFunktionCreateRequest();
		dto.idAbschnittsdaten = 10L;
		dto.idFunktion = 20L;

		when(abschnittsdatenRepo.findById(10L)).thenReturn(Optional.of(mock(DTOLehrerAbschnittsdaten.class)));
		when(leitungsfunktionRepo.findById(20L)).thenReturn(Optional.of(mock(DTOLeitungsfunktion.class)));
		when(repo.existsByIdAbschnittAndIdFunktion(10L, 20L)).thenReturn(false);
		when(mapper.toDomain(dto)).thenReturn(entity);
		when(repo.create(entity)).thenReturn(entity);
		when(mapper.toApi(entity)).thenReturn(apiModel);

		final var result = service.create(dto);

		assertThat(result).isEqualTo(apiModel);
	}

	@Test
	@DisplayName("create - Abschnitt nicht gefunden")
	void create_abschnittNotFound() {
		final var dto = new LehrerFunktionCreateRequest();
		dto.idAbschnittsdaten = 10L;
		dto.idFunktion = 20L;

		when(abschnittsdatenRepo.findById(10L)).thenReturn(Optional.empty());

		assertThatException()
				.isThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine Lehrerabschnittsdaten mit der ID 10 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("create - Leitungsfunktion nicht gefunden")
	void create_leitungsfunktionNotFound() {
		final var dto = new LehrerFunktionCreateRequest();
		dto.idAbschnittsdaten = 10L;
		dto.idFunktion = 20L;

		when(abschnittsdatenRepo.findById(10L)).thenReturn(Optional.of(mock(DTOLehrerAbschnittsdaten.class)));
		when(leitungsfunktionRepo.findById(20L)).thenReturn(Optional.empty());

		assertThatException()
				.isThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine Leitungsfunktion mit der ID 20 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	// -------------------------------------------------------------------------
	// createMultiple
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("createMultiple - Duplikat in DB")
	void createMultiple_duplicateInDb() {
		final var dto = new LehrerFunktionCreateRequest();
		dto.idAbschnittsdaten = 10L;
		dto.idFunktion = 20L;

		when(abschnittsdatenRepo.findById(10L)).thenReturn(Optional.of(mock(DTOLehrerAbschnittsdaten.class)));
		when(leitungsfunktionRepo.findById(20L)).thenReturn(Optional.of(mock(DTOLeitungsfunktion.class)));
		when(repo.existsByIdAbschnittAndIdFunktion(10L, 20L)).thenReturn(true);

		assertThatException()
				.isThrownBy(() -> service.createMultiple(List.of(dto)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Lehrerfunktion mit der ID 20 im Abschnitt mit der ID 10 existiert bereits.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("createMultiple - Duplikat in Liste")
	void createMultiple_duplicateInList() {
		final var dto1 = new LehrerFunktionCreateRequest();
		dto1.idAbschnittsdaten = 10L;
		dto1.idFunktion = 20L;
		final var dto2 = new LehrerFunktionCreateRequest();
		dto2.idAbschnittsdaten = 10L;
		dto2.idFunktion = 20L;

		when(abschnittsdatenRepo.findById(10L)).thenReturn(Optional.of(mock(DTOLehrerAbschnittsdaten.class)));
		when(leitungsfunktionRepo.findById(20L)).thenReturn(Optional.of(mock(DTOLeitungsfunktion.class)));
		when(repo.existsByIdAbschnittAndIdFunktion(10L, 20L)).thenReturn(false);

		assertThatException()
				.isThrownBy(() -> service.createMultiple(List.of(dto1, dto2)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Lehrerfunktion mit der ID 20 im Abschnitt mit der ID 10 ist in der Anfrage mehrfach vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("createMultiple")
	void createMultiple() {
		final var dto1 = new LehrerFunktionCreateRequest();
		dto1.idAbschnittsdaten = 10L;
		dto1.idFunktion = 20L;
		final var dto2 = new LehrerFunktionCreateRequest();
		dto2.idAbschnittsdaten = 10L;
		dto2.idFunktion = 21L;

		final var entity2 = new DTOLehrerFunktion(2L, 10L, 21L);
		final var apiModel2 = new LehrerFunktion();
		apiModel2.id = 2L;

		when(abschnittsdatenRepo.findById(10L)).thenReturn(Optional.of(mock(DTOLehrerAbschnittsdaten.class)));
		when(leitungsfunktionRepo.findById(20L)).thenReturn(Optional.of(mock(DTOLeitungsfunktion.class)));
		when(leitungsfunktionRepo.findById(21L)).thenReturn(Optional.of(mock(DTOLeitungsfunktion.class)));
		when(repo.existsByIdAbschnittAndIdFunktion(10L, 20L)).thenReturn(false);
		when(repo.existsByIdAbschnittAndIdFunktion(10L, 21L)).thenReturn(false);
		when(mapper.toDomain(dto1)).thenReturn(entity);
		when(mapper.toDomain(dto2)).thenReturn(entity2);
		when(repo.create(anyList())).thenReturn(List.of(entity, entity2));
		when(mapper.toApi(entity)).thenReturn(apiModel);
		when(mapper.toApi(entity2)).thenReturn(apiModel2);

		final var result = service.createMultiple(List.of(dto1, dto2));

		assertThat(result).containsExactly(apiModel, apiModel2);
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch - not found")
	void patch_notFound() {
		when(repo.getById(1L)).thenThrow(new RepositoryException("not found"));

		assertThatException()
				.isThrownBy(() -> service.patch(1L, new LehrerFunktionPatchRequest()))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine Lehrerfunktion mit der ID 1 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch - Duplikat")
	void patch_duplicate() {
		final var dto = new LehrerFunktionPatchRequest();
		dto.idFunktion = JsonNullable.of(99L);

		when(repo.getById(1L)).thenReturn(entity);
		when(leitungsfunktionRepo.findById(99L)).thenReturn(Optional.of(mock(DTOLeitungsfunktion.class)));
		when(repo.existsByIdAbschnittAndIdFunktionExcludingId(10L, 99L, 1L)).thenReturn(true);

		assertThatException()
				.isThrownBy(() -> service.patch(1L, dto))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Lehrerfunktion mit der ID 99 im Abschnitt mit der ID 10 existiert bereits.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch - idFunktion undefined - keine Duplikatpruefung")
	void patch_idFunktionUndefined() {
		final var dto = new LehrerFunktionPatchRequest();
		// idFunktion bleibt JsonNullable.undefined()

		when(repo.getById(1L)).thenReturn(entity);
		when(mapper.toApi(entity)).thenReturn(apiModel);

		final var result = service.patch(1L, dto);

		assertThat(result).isEqualTo(apiModel);
		verify(repo, never()).existsByIdAbschnittAndIdFunktionExcludingId(anyLong(), anyLong(), anyLong());
	}

	@Test
	@DisplayName("patch")
	void patch() {
		final var dto = new LehrerFunktionPatchRequest();
		dto.idFunktion = JsonNullable.of(99L);

		when(repo.getById(1L)).thenReturn(entity);
		when(leitungsfunktionRepo.findById(99L)).thenReturn(Optional.of(mock(DTOLeitungsfunktion.class)));
		when(repo.existsByIdAbschnittAndIdFunktionExcludingId(10L, 99L, 1L)).thenReturn(false);
		when(mapper.toApi(entity)).thenReturn(apiModel);

		final var result = service.patch(1L, dto);

		verify(mapper).patch(dto, entity);
		assertThat(result).isEqualTo(apiModel);
	}

	@Test
	@DisplayName("patch - Leitungsfunktion nicht gefunden")
	void patch_leitungsfunktionNotFound() {
		final var dto = new LehrerFunktionPatchRequest();
		dto.idFunktion = JsonNullable.of(99L);

		when(repo.getById(1L)).thenReturn(entity);
		when(leitungsfunktionRepo.findById(99L)).thenReturn(Optional.empty());

		assertThatException()
				.isThrownBy(() -> service.patch(1L, dto))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine Leitungsfunktion mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	// -------------------------------------------------------------------------
	// patchMultiple
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patchMultiple - Duplikat in DB")
	void patchMultiple_duplicateInDb() {
		final var dto = new LehrerFunktionBatchPatchRequest();
		dto.id = 1L;
		dto.idFunktion = JsonNullable.of(99L);

		when(repo.findMapByIds(List.of(1L))).thenReturn(Map.of(1L, entity));
		when(leitungsfunktionRepo.findById(99L)).thenReturn(Optional.of(mock(DTOLeitungsfunktion.class)));
		when(repo.existsByIdAbschnittAndIdFunktionExcludingId(10L, 99L, 1L)).thenReturn(true);

		assertThatException()
				.isThrownBy(() -> service.patchMultiple(List.of(dto)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Lehrerfunktion mit der ID 99 im Abschnitt mit der ID 10 existiert bereits.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patchMultiple - Duplikat in Liste")
	void patchMultiple_duplicateInList() {
		final var dto1 = new LehrerFunktionBatchPatchRequest();
		dto1.id = 1L;
		dto1.idFunktion = JsonNullable.of(99L);
		final var entity2 = new DTOLehrerFunktion(2L, 10L, 21L);
		final var dto2 = new LehrerFunktionBatchPatchRequest();
		dto2.id = 2L;
		dto2.idFunktion = JsonNullable.of(99L);

		when(repo.findMapByIds(List.of(1L, 2L))).thenReturn(Map.of(1L, entity, 2L, entity2));
		when(leitungsfunktionRepo.findById(99L)).thenReturn(Optional.of(mock(DTOLeitungsfunktion.class)));
		when(repo.existsByIdAbschnittAndIdFunktionExcludingId(10L, 99L, 1L)).thenReturn(false);

		assertThatException()
				.isThrownBy(() -> service.patchMultiple(List.of(dto1, dto2)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Lehrerfunktion mit der ID 99 im Abschnitt mit der ID 10 ist in der Anfrage mehrfach vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patchMultiple")
	void patchMultiple() {
		final var dto1 = new LehrerFunktionBatchPatchRequest();
		dto1.id = 1L;
		dto1.idFunktion = JsonNullable.of(99L);
		final var entity2 = new DTOLehrerFunktion(2L, 10L, 21L);
		final var dto2 = new LehrerFunktionBatchPatchRequest();
		dto2.id = 2L;
		dto2.idFunktion = JsonNullable.of(98L);
		final var apiModel2 = new LehrerFunktion();
		apiModel2.id = 2L;

		when(repo.findMapByIds(List.of(1L, 2L))).thenReturn(Map.of(1L, entity, 2L, entity2));
		when(leitungsfunktionRepo.findById(99L)).thenReturn(Optional.of(mock(DTOLeitungsfunktion.class)));
		when(leitungsfunktionRepo.findById(98L)).thenReturn(Optional.of(mock(DTOLeitungsfunktion.class)));
		when(repo.existsByIdAbschnittAndIdFunktionExcludingId(10L, 99L, 1L)).thenReturn(false);
		when(repo.existsByIdAbschnittAndIdFunktionExcludingId(10L, 98L, 2L)).thenReturn(false);
		when(mapper.toApi(entity)).thenReturn(apiModel);
		when(mapper.toApi(entity2)).thenReturn(apiModel2);

		final var result = service.patchMultiple(List.of(dto1, dto2));

		verify(mapper).patch(dto1, entity);
		verify(mapper).patch(dto2, entity2);
		assertThat(result).containsExactly(apiModel, apiModel2);
	}

	// -------------------------------------------------------------------------
	// delete
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("delete - not found")
	void delete_notFound() {
		when(repo.getById(99L)).thenThrow(new RepositoryException("not found"));

		final var result = service.delete(99L);

		assertThat(result.success).isFalse();
		assertThat(result.id).isEqualTo(99L);
		assertThat(result.log).contains("Keine Lehrerfunktion mit der ID 99 gefunden.");
		verify(repo, never()).delete(any(DTOLehrerFunktion.class));
	}

	@Test
	@DisplayName("delete")
	void delete() {
		when(repo.getById(1L)).thenReturn(entity);

		final var result = service.delete(1L);

		assertThat(result.success).isTrue();
		assertThat(result.id).isEqualTo(1L);
		verify(repo).delete(entity);
	}

	// -------------------------------------------------------------------------
	// deleteMultiple
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("deleteMultiple - ein Eintrag nicht gefunden")
	void deleteMultiple_partialNotFound() {
		when(repo.delete(anyList())).thenReturn(List.of(entity));

		final var result = service.deleteMultiple(List.of(1L, 99L));

		assertThat(result).hasSize(2);
		assertThat(result.stream().filter(r -> r.success).map(r -> r.id)).containsExactly(1L);
		assertThat(result.stream().filter(r -> !r.success).map(r -> r.id)).containsExactly(99L);
		assertThat(result.stream().filter(r -> !r.success).map(r -> r.log).findFirst())
				.hasValueSatisfying(log -> assertThat(log).contains("Keine Lehrerfunktion mit der ID 99 gefunden."));
		verify(repo).delete(anyList());
	}

	@Test
	@DisplayName("deleteMultiple - alle nicht gefunden")
	void deleteMultiple_noneFound() {
		when(repo.findListByIds(List.of(99L))).thenReturn(Collections.emptyList());

		final var result = service.deleteMultiple(List.of(99L));

		assertThat(result).hasSize(1);
		assertThat(result.getFirst().success).isFalse();
	}

	@Test
	@DisplayName("deleteMultiple")
	void deleteMultiple() {
		when(repo.delete(anyList())).thenReturn(List.of(entity));


		final var result = service.deleteMultiple(List.of(1L));

		assertThat(result).hasSize(1);
		assertThat(result.getFirst().success).isTrue();
		assertThat(result.getFirst().id).isEqualTo(1L);
		verify(repo).delete(anyList());
	}

}
