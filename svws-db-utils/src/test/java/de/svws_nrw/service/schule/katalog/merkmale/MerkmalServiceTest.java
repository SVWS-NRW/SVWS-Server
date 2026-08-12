package de.svws_nrw.service.schule.katalog.merkmale;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.core.data.schule.Merkmal;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.mapper.schule.katalog.merkmal.MerkmalMapper;
import de.svws_nrw.db.dto.current.schild.schule.DTOMerkmale;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.schule.kataloge.merkmal.MerkmalRepository;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalCreateRequest;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalPatchRequest;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MerkmalServiceTest {

	@Mock
	private MerkmalRepository repository;

	@Mock
	private MerkmalMapper mapper;

	private MerkmalService service;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeEach
	void setUp() {
		service = new MerkmalService(repository, mapper);
		transactionSupportMock = mockStatic(TransactionSupport.class);
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll_success() {
		final var entity1 = new DTOMerkmale(1L);
		final var entity2 = new DTOMerkmale(2L);
		final var merkmal1 = mock(Merkmal.class);
		final var merkmal2 = mock(Merkmal.class);

		when(repository.getAll()).thenReturn(List.of(entity1, entity2));
		when(mapper.toApi(entity1)).thenReturn(merkmal1);
		when(mapper.toApi(entity2)).thenReturn(merkmal2);

		final var result = service.getAll();

		assertThat(result)
				.isNotNull()
				.hasSize(2)
				.containsExactly(merkmal1, merkmal2);

		verify(repository, times(1)).getAll();
		verify(mapper, times(2)).toApi(any(DTOMerkmale.class));
	}

	@Test
	@DisplayName("create | Erfolg")
	void create_success() {
		final var dto = new MerkmalCreateRequest();
		dto.kuerzel = "TEST";
		dto.bezeichnung = "Test Merkmal";
		dto.istSchulmerkmal = true;
		dto.istSchuelermerkmal = false;

		final var entity = new DTOMerkmale(1L);
		final var apiMerkmal = mock(Merkmal.class);

		when(repository.bezeichnungIsAlreadyUsedCreate(anyString())).thenReturn(false);
		when(repository.kuerzelIsAlreadyUsedCreate(anyString())).thenReturn(false);
		when(mapper.toDomain(any())).thenReturn(entity);
		when(mapper.toApi(nullable(DTOMerkmale.class))).thenReturn(apiMerkmal);

		final var result = service.create(dto);

		assertThat(result).isEqualTo(apiMerkmal);
	}


	@Test
	@DisplayName("create | Bezeichnung bereits vorhanden")
	void create_bezeichnungAlreadyExists() {
		final var dto = new MerkmalCreateRequest();
		dto.kuerzel = "TEST";
		dto.bezeichnung = "Existiert";
		dto.istSchulmerkmal = true;
		dto.istSchuelermerkmal = false;

		when(repository.bezeichnungIsAlreadyUsedCreate(dto.bezeichnung)).thenReturn(true);

		assertThatThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Die Bezeichnung Existiert wird bereits verwendet")
				.extracting("status")
				.isEqualTo(Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create | Kürzel bereits vorhanden")
	void create_kuerzelAlreadyExists() {
		final var dto = new MerkmalCreateRequest();
		dto.kuerzel = "EXIST";
		dto.bezeichnung = "Test";
		dto.istSchulmerkmal = true;
		dto.istSchuelermerkmal = false;

		when(repository.bezeichnungIsAlreadyUsedCreate(dto.bezeichnung)).thenReturn(false);
		when(repository.kuerzelIsAlreadyUsedCreate(dto.kuerzel)).thenReturn(true);

		assertThatThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Das Kürzel EXIST wird bereits verwendet")
				.extracting("status")
				.isEqualTo(Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create | Keine Merkmaltypen ausgewählt")
	void create_noMerkmalTypeSelected() {
		final var dto = new MerkmalCreateRequest();
		dto.kuerzel = "TEST";
		dto.bezeichnung = "Test";
		dto.istSchulmerkmal = false;
		dto.istSchuelermerkmal = false;

		assertThatThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Mindestens ein Merkmaltyp (Schule oder Schüler) muss ausgewählt sein")
				.extracting("status")
				.isEqualTo(Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Erfolg")
	void patch_success() {
		final var id = 1L;
		final var dto = new MerkmalPatchRequest();
		dto.bezeichnung = JsonNullable.of("Neue Bezeichnung");
		dto.istSchulmerkmal = JsonNullable.of(true);
		dto.istSchuelermerkmal = JsonNullable.of(false);

		final var entity = new DTOMerkmale(id);
		entity.kuerzel = "TEST";
		entity.bezeichnung = "Alt";
		entity.istSchulmerkmal = true;
		entity.istSchuelermerkmal = false;

		final var apiMerkmal = mock(Merkmal.class);

		when(repository.getById(id)).thenReturn(entity);
		when(repository.bezeichnungIsAlreadyUsedPatch("Neue Bezeichnung", id)).thenReturn(false);
		when(mapper.toApi(entity)).thenReturn(apiMerkmal);

		final var result = service.patch(id, dto);

		assertThat(result)
				.isNotNull()
				.isEqualTo(apiMerkmal);

		verify(repository, times(1)).getById(id);
		verify(mapper, times(1)).patch(dto, entity);
	}

	@Test
	@DisplayName("patch | Bezeichnung bereits vorhanden")
	void patch_bezeichnungAlreadyExists() {
		final var id = 1L;
		final var dto = new MerkmalPatchRequest();
		dto.bezeichnung = JsonNullable.of("Existiert");
		dto.istSchulmerkmal = JsonNullable.undefined();
		dto.istSchuelermerkmal = JsonNullable.undefined();

		final var entity = new DTOMerkmale(id);
		entity.istSchulmerkmal = true;
		entity.istSchuelermerkmal = false;

		when(repository.getById(id)).thenReturn(entity);
		when(repository.bezeichnungIsAlreadyUsedPatch("Existiert", id)).thenReturn(true);

		assertThatThrownBy(() -> service.patch(id, dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Die Bezeichnung Existiert wird bereits verwendet");
	}

	@Test
	@DisplayName("delete | Erfolg mit mehreren IDs")
	void delete_success() {
		final var ids = List.of(1L, 2L, 3L);
		final var entity1 = new DTOMerkmale(1L);
		final var entity2 = new DTOMerkmale(2L);
		final var entity3 = new DTOMerkmale(3L);

		final var entities = List.of(entity1, entity2, entity3);

		when(repository.findListByIds(ids)).thenReturn(entities);
		when(repository.delete(entities)).thenReturn(entities);

		final var result = service.delete(ids);

		assertThat(result)
				.isNotNull()
				.hasSize(3)
				.allSatisfy(response -> {
					assertThat(response.success).isTrue();
					assertThat(response.id).isIn(1L, 2L, 3L);
				})
				.extracting("id", Long.class)
				.containsExactly(1L, 2L, 3L);

		verify(repository, times(1)).findListByIds(ids);
		verify(repository, times(1)).delete(entities);
	}

	@Test
	@DisplayName("delete | Leere Liste")
	void delete_emptyList() {
		final var ids = List.<Long>of();
		when(repository.findListByIds(ids)).thenReturn(List.of());

		final var result = service.delete(ids);

		assertThat(result)
				.isNotNull()
				.isEmpty();
		verify(repository, times(1)).findListByIds(ids);
	}
}
