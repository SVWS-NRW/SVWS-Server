package de.svws_nrw.service.schulwechsel.dokument;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.core.data.schule.SchulwechselDokument;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchulwechselDokument;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schulwechsel.dokument.SchulwechselDokumentMapper;
import de.svws_nrw.repo.schulwechsel.dokument.SchulwechselDokumentRepository;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchulwechselDokumentServiceTest {

	@Mock
	private SchulwechselDokumentRepository repository;

	@Mock
	private SchulwechselDokumentMapper mapper;

	private SchulwechselDokumentService service;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeEach
	void setUp() {
		service = new SchulwechselDokumentService(repository, mapper);
		transactionSupportMock = mockStatic(TransactionSupport.class);
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private DTOSchulwechselDokument createEntity(final long id) {
		return new DTOSchulwechselDokument(id, "test.xml", "<xml/>", "2025-01-01", "2025-06-01");
	}

	// -------------------------------------------------------------------------
	// getAll
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll_success() {
		final var entity = createEntity(1L);
		final var apiModel = mock(SchulwechselDokument.class);

		when(repository.getAll()).thenReturn(List.of(entity));
		when(mapper.toApi(entity)).thenReturn(apiModel);

		final var result = service.getAll();

		assertThat(result).isNotNull().hasSize(1).containsExactly(apiModel);
		verify(repository, times(1)).getAll();
		verify(mapper, times(1)).toApi(entity);
	}

	@Test
	@DisplayName("getAll | Leere Liste")
	void getAll_emptyList() {
		when(repository.getAll()).thenReturn(List.of());

		assertThat(service.getAll()).isNotNull().isEmpty();
		verify(repository, times(1)).getAll();
	}

	// -------------------------------------------------------------------------
	// getById
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getById | Erfolg")
	void getById_success() {
		final var entity = createEntity(1L);
		final var apiModel = mock(SchulwechselDokument.class);

		when(repository.getById(1L)).thenReturn(entity);
		when(mapper.toApi(entity)).thenReturn(apiModel);

		assertThat(service.getById(1L)).isNotNull().isEqualTo(apiModel);
		verify(repository, times(1)).getById(1L);
	}

	@Test
	@DisplayName("getById | Nicht gefunden → NOT_FOUND")
	void getById_notFound() {
		when(repository.getById(99L))
				.thenThrow(new ApiOperationException(Response.Status.NOT_FOUND, "99"));

		assertThatThrownBy(() -> service.getById(99L))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("99")
				.extracting("status")
				.isEqualTo(Response.Status.NOT_FOUND);
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("create | Erfolg")
	void create_success() {
		final var dto = new SchulwechselDokumentCreateRequest();
		dto.fileName = "schulwechsel.xml";
		dto.xmlDocument = "<xml/>";
		dto.createdAt = "2025-01-01";
		dto.lastModified = "2025-06-01";

		final var entity = createEntity(1L);
		final var apiModel = mock(SchulwechselDokument.class);

		when(mapper.toDomain(dto)).thenReturn(entity);
		when(repository.create(entity)).thenReturn(entity);
		when(mapper.toApi(entity)).thenReturn(apiModel);

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		assertThat(service.create(dto)).isNotNull().isEqualTo(apiModel);
		verify(repository, times(1)).create(entity);
	}

	@Test
	@DisplayName("create | Repository-Fehler → INTERNAL_SERVER_ERROR")
	void create_repositoryError() {
		final var dto = new SchulwechselDokumentCreateRequest();
		dto.fileName = "schulwechsel.xml";
		dto.xmlDocument = "<xml/>";
		dto.createdAt = "2025-01-01";
		dto.lastModified = "2025-06-01";

		final var entity = createEntity(1L);

		when(mapper.toDomain(dto)).thenReturn(entity);
		when(repository.create(entity))
				.thenThrow(new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Fehler"));

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		assertThatThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.extracting("status")
				.isEqualTo(Response.Status.INTERNAL_SERVER_ERROR);
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch | Erfolg")
	void patch_success() {
		final var id = 1L;
		final var dto = new SchulwechselDokumentPatchRequest();
		dto.fileName = JsonNullable.of("neu.xml");

		final var entity = createEntity(id);
		final var apiModel = mock(SchulwechselDokument.class);

		when(repository.getById(id)).thenReturn(entity);
		when(mapper.toApi(entity)).thenReturn(apiModel);

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		assertThat(service.patch(id, dto)).isNotNull().isEqualTo(apiModel);
		verify(repository, times(1)).getById(id);
		verify(mapper, times(1)).patch(dto, entity);
	}

	@Test
	@DisplayName("patch | Nicht gefunden → NOT_FOUND")
	void patch_notFound() {
		final var dto = new SchulwechselDokumentPatchRequest();

		when(repository.getById(99L))
				.thenThrow(new ApiOperationException(Response.Status.NOT_FOUND, "99"));

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		assertThatThrownBy(() -> service.patch(99L, dto))
				.isInstanceOf(ApiOperationException.class)
				.extracting("status")
				.isEqualTo(Response.Status.NOT_FOUND);
	}

	// -------------------------------------------------------------------------
	// delete
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("delete | Erfolg mit mehreren IDs")
	void delete_success() {
		final var ids = List.of(1L, 2L, 3L);
		final var entities = List.of(createEntity(1L), createEntity(2L), createEntity(3L));

		when(repository.findListByIds(ids)).thenReturn(entities);
		when(repository.delete(entities)).thenReturn(entities);

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		final var result = service.delete(ids);

		assertThat(result)
				.isNotNull()
				.hasSize(3)
				.allSatisfy(response -> assertThat(response.success).isTrue())
				.extracting("id", Long.class)
				.containsExactly(1L, 2L, 3L);

		verify(repository, times(1)).findListByIds(ids);
		verify(repository, times(1)).delete(entities);
	}

	@Test
	@DisplayName("delete | Leere Liste")
	void delete_emptyList() {
		when(repository.findListByIds(List.of())).thenReturn(List.of());
		when(repository.delete(List.of())).thenReturn(List.of());

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		assertThat(service.delete(List.of())).isNotNull().isEmpty();
	}

	@Test
	@DisplayName("delete | Ergebnis ist nach ID sortiert")
	void delete_resultIsSortedById() {
		final var ids = List.of(3L, 1L, 2L);
		final var entities = List.of(createEntity(3L), createEntity(1L), createEntity(2L));

		when(repository.findListByIds(ids)).thenReturn(entities);
		when(repository.delete(entities)).thenReturn(entities);

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		assertThat(service.delete(ids))
				.extracting("id", Long.class)
				.containsExactly(1L, 2L, 3L);
	}

	@Test
	@DisplayName("delete | Teilerfolg – nicht alle IDs gefunden")
	void delete_partialSuccess() {
		final var ids = List.of(1L, 2L, 3L);
		final var foundEntities = List.of(createEntity(1L)); // nur 1 gefunden

		when(repository.findListByIds(ids)).thenReturn(foundEntities);
		when(repository.delete(foundEntities)).thenReturn(foundEntities);

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		assertThat(service.delete(ids))
				.hasSize(3)
				.satisfiesOnlyOnce(response -> assertThat(response.success).isTrue())
				.extracting("id", Long.class)
				.containsExactly(1L, 2L, 3L);

		verify(repository, times(1)).findListByIds(ids);
		verify(repository, times(1)).delete(foundEntities);
	}
}
