package de.svws_nrw.service.schueler.schulbesuch;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchMerkmal;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerMerkmale;
import de.svws_nrw.db.dto.current.schild.schule.DTOMerkmale;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.schulbesuch.SchuelerMerkmalMapper;
import de.svws_nrw.repo.schueler.schulbesuch.SchuelerMerkmalRepository;
import de.svws_nrw.repo.schule.kataloge.merkmal.MerkmalRepository;
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
class SchuelerMerkmalServiceTest {


	@Mock
	private SchuelerMerkmalRepository repository;

	@Mock
	private MerkmalRepository merkmalRepository;

	@Mock
	private SchuelerMerkmalMapper mapper;

	private SchuelerMerkmalService service;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	// Testdaten
	private DTOMerkmale merkmal;

	@BeforeEach
	void setUp() {
		merkmal = new DTOMerkmale(10L);
		merkmal.kuerzel = "AB";

		service = new SchuelerMerkmalService(repository, merkmalRepository, mapper);
		transactionSupportMock = mockStatic(TransactionSupport.class);
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}

	// -------------------------------------------------------------------------
	// getAllByIdSchueler
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getAllByIdSchueler | Erfolg")
	void getAllByIdSchueler_success() {
		final var idSchueler = 1L;
		final var entity = new DTOSchuelerMerkmale(1L, idSchueler);
		entity.kuerzelMerkmal = merkmal.kuerzel;

		final var apiModel = mock(SchuelerSchulbesuchMerkmal.class);

		when(repository.getAllByIdSchueler(idSchueler)).thenReturn(List.of(entity));
		when(mapper.toApi(entity, merkmal.id)).thenReturn(apiModel);
		when(merkmalRepository.getAll()).thenReturn(List.of(merkmal));

		final var result = service.getAllByIdSchueler(idSchueler);

		assertThat(result)
				.isNotNull()
				.hasSize(1)
				.containsExactly(apiModel);

		verify(repository, times(1)).getAllByIdSchueler(idSchueler);
		verify(mapper, times(1)).toApi(entity, merkmal.id);
	}

	@Test
	@DisplayName("getAllByIdSchueler | Leere Liste")
	void getAllByIdSchueler_emptyList() {
		final var idSchueler = 99L;

		when(repository.getAllByIdSchueler(idSchueler)).thenReturn(List.of());

		final var result = service.getAllByIdSchueler(idSchueler);

		assertThat(result).isNotNull().isEmpty();
		verify(repository, times(1)).getAllByIdSchueler(idSchueler);
	}

	@Test
	@DisplayName("getAllByIdSchueler | Kuerzel unbekannt → idMerkmal ist null")
	void getAllByIdSchueler_unknownKuerzel_idMerkmalIsNull() {
		final var idSchueler = 1L;
		final var entity = new DTOSchuelerMerkmale(1L, idSchueler);
		entity.kuerzelMerkmal = "UNBEKANNT";

		final var apiModel = mock(SchuelerSchulbesuchMerkmal.class);

		when(repository.getAllByIdSchueler(idSchueler)).thenReturn(List.of(entity));
		when(mapper.toApi(entity, null)).thenReturn(apiModel);

		final var result = service.getAllByIdSchueler(idSchueler);

		assertThat(result).containsExactly(apiModel);
		verify(mapper, times(1)).toApi(entity, null);
	}

	@Test
	@DisplayName("getAllByIdSchueler | kuerzelMerkmal null → idMerkmal ist null")
	void getAllByIdSchueler_nullKuerzel_idMerkmalIsNull() {
		final var idSchueler = 1L;
		final var entity = new DTOSchuelerMerkmale(1L, idSchueler);
		entity.kuerzelMerkmal = null;

		final var apiModel = mock(SchuelerSchulbesuchMerkmal.class);

		when(repository.getAllByIdSchueler(idSchueler)).thenReturn(List.of(entity));
		when(mapper.toApi(entity, null)).thenReturn(apiModel);

		final var result = service.getAllByIdSchueler(idSchueler);

		assertThat(result).containsExactly(apiModel);
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("create | Erfolg")
	void create_success() {
		final var dto = new SchuelerMerkmalCreateRequest();
		dto.idSchueler = 42L;
		dto.idMerkmal = merkmal.id;
		dto.datumVon = "2023-09-01";
		dto.datumBis = "2024-06-30";

		final var entity = new DTOSchuelerMerkmale(1L, dto.idSchueler);
		entity.kuerzelMerkmal = merkmal.kuerzel;

		final var apiModel = mock(SchuelerSchulbesuchMerkmal.class);

		when(merkmalRepository.getById(merkmal.id)).thenReturn(merkmal);
		when(mapper.toDomain(dto, merkmal.kuerzel)).thenReturn(entity);
		when(repository.create(entity)).thenReturn(entity);
		when(mapper.toApi(entity, merkmal.id)).thenReturn(apiModel);

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		final var result = service.create(dto);

		assertThat(result).isNotNull().isEqualTo(apiModel);
		verify(repository, times(1)).create(entity);
	}

	@Test
	@DisplayName("create | Erfolg ohne Datumsangaben")
	void create_successWithoutDates() {
		final var dto = new SchuelerMerkmalCreateRequest();
		dto.idSchueler = 42L;
		dto.idMerkmal = merkmal.id;
		dto.datumVon = null;
		dto.datumBis = null;

		final var entity = new DTOSchuelerMerkmale(1L, dto.idSchueler);
		entity.kuerzelMerkmal = merkmal.kuerzel;

		final var apiModel = mock(SchuelerSchulbesuchMerkmal.class);

		when(merkmalRepository.getById(merkmal.id)).thenReturn(merkmal);
		when(mapper.toDomain(dto, merkmal.kuerzel)).thenReturn(entity);
		when(repository.create(entity)).thenReturn(entity);
		when(mapper.toApi(entity, merkmal.id)).thenReturn(apiModel);

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		assertThat(service.create(dto)).isEqualTo(apiModel);
	}

	@Test
	@DisplayName("create | idMerkmal nicht gefunden → BAD_REQUEST")
	void create_idMerkmalNotFound_throwsBadRequest() {
		final var dto = new SchuelerMerkmalCreateRequest();
		dto.idSchueler = 42L;
		dto.idMerkmal = 999L;

		when(merkmalRepository.getById(999L))
				.thenThrow(new ApiOperationException(Response.Status.BAD_REQUEST, "999"));

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		assertThatThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("999")
				.extracting("status")
				.isEqualTo(Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create | datumBis vor datumVon → BAD_REQUEST")
	void create_datumBisBeforeDatumVon_throwsBadRequest() {
		final var dto = new SchuelerMerkmalCreateRequest();
		dto.idSchueler = 42L;
		dto.idMerkmal = merkmal.id;
		dto.datumVon = "2023-06-01";
		dto.datumBis = "2022-01-01";

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		assertThatThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Enddatum")
				.extracting("status")
				.isEqualTo(Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create | datumVon gleich datumBis → gültig")
	void create_datumVonEqualsDatumBis_success() {
		final var dto = new SchuelerMerkmalCreateRequest();
		dto.idSchueler = 42L;
		dto.idMerkmal = merkmal.id;
		dto.datumVon = "2023-06-01";
		dto.datumBis = "2023-06-01";

		final var entity = new DTOSchuelerMerkmale(1L, dto.idSchueler);
		entity.kuerzelMerkmal = merkmal.kuerzel;

		final var apiModel = mock(SchuelerSchulbesuchMerkmal.class);

		when(merkmalRepository.getById(merkmal.id)).thenReturn(merkmal);
		when(mapper.toDomain(dto, merkmal.kuerzel)).thenReturn(entity);
		when(repository.create(entity)).thenReturn(entity);
		when(mapper.toApi(entity, merkmal.id)).thenReturn(apiModel);

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		assertThat(service.create(dto)).isEqualTo(apiModel);
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch | Erfolg – alle Felder gesetzt")
	void patch_success_allFieldsSet() {
		final var id = 1L;
		final var dto = new SchuelerMerkmalPatchRequest();
		dto.idMerkmal = JsonNullable.of(merkmal.id);
		dto.datumVon = JsonNullable.of("2023-09-01");
		dto.datumBis = JsonNullable.of("2024-06-30");

		final var entity = new DTOSchuelerMerkmale(id, 42L);
		entity.kuerzelMerkmal = merkmal.kuerzel;

		final var apiModel = mock(SchuelerSchulbesuchMerkmal.class);

		when(repository.getById(id)).thenReturn(entity);
		when(merkmalRepository.getById(merkmal.id)).thenReturn(merkmal);
		when(mapper.toApi(entity, merkmal.id)).thenReturn(apiModel);

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		final var result = service.patch(id, dto);

		assertThat(result).isNotNull().isEqualTo(apiModel);
		verify(repository, times(1)).getById(id);
		verify(mapper, times(1)).patch(dto, entity);
	}

	@Test
	@DisplayName("patch | Erfolg – keine Felder gesetzt (undefined)")
	void patch_success_noFieldsSet() {
		final var id = 1L;
		final var dto = new SchuelerMerkmalPatchRequest();
		// alle Felder bleiben JsonNullable.undefined() per Default

		final var entity = new DTOSchuelerMerkmale(id, 42L);
		entity.kuerzelMerkmal = merkmal.kuerzel;
		entity.datumVon = "2023-09-01";
		entity.datumBis = "2024-06-30";

		final var apiModel = mock(SchuelerSchulbesuchMerkmal.class);

		when(repository.getById(id)).thenReturn(entity);
		// idMerkmal ist undefined → Fallback auf entity.kuerzelMerkmal via getByKuerzel
		when(merkmalRepository.getByKuerzel(merkmal.kuerzel)).thenReturn(Optional.ofNullable(merkmal));
		when(mapper.toApi(entity, merkmal.id)).thenReturn(apiModel);

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		assertThat(service.patch(id, dto)).isEqualTo(apiModel);
		verify(mapper, times(1)).patch(dto, entity);
	}

	@Test
	@DisplayName("patch | idMerkmal nicht gefunden → BAD_REQUEST")
	void patch_idMerkmalNotFound_throwsBadRequest() {
		final var id = 1L;
		final var dto = new SchuelerMerkmalPatchRequest();
		dto.idMerkmal = JsonNullable.of(999L);

		final var entity = new DTOSchuelerMerkmale(id, 42L);

		when(repository.getById(id)).thenReturn(entity);
		when(merkmalRepository.getById(999L))
				.thenThrow(new ApiOperationException(Response.Status.BAD_REQUEST, "999"));

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		assertThatThrownBy(() -> service.patch(id, dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("999")
				.extracting("status")
				.isEqualTo(Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | datumBis vor datumVon (dto-Werte) → BAD_REQUEST")
	void patch_datumBisBeforeDatumVon_fromDto_throwsBadRequest() {
		final var id = 1L;
		final var dto = new SchuelerMerkmalPatchRequest();
		dto.datumVon = JsonNullable.of("2023-06-01");
		dto.datumBis = JsonNullable.of("2022-01-01");

		final var entity = new DTOSchuelerMerkmale(id, 42L);

		when(repository.getById(id)).thenReturn(entity);

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		assertThatThrownBy(() -> service.patch(id, dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Enddatum")
				.extracting("status")
				.isEqualTo(Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | datumBis vor entity.datumVon (Fallback aus Entity) → BAD_REQUEST")
	void patch_datumBisBeforeDatumVon_fromEntityFallback_throwsBadRequest() {
		final var id = 1L;
		final var dto = new SchuelerMerkmalPatchRequest();
		dto.datumBis = JsonNullable.of("2019-01-01");

		final var entity = new DTOSchuelerMerkmale(id, 42L);
		entity.datumVon = "2020-09-01";
		entity.datumBis = null;

		when(repository.getById(id)).thenReturn(entity);

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		assertThatThrownBy(() -> service.patch(id, dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Enddatum")
				.extracting("status")
				.isEqualTo(Response.Status.BAD_REQUEST);
	}

	// -------------------------------------------------------------------------
	// delete
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("delete | Erfolg mit mehreren IDs")
	void delete_success() {
		final var ids = List.of(1L, 2L, 3L);
		final var entities = List.of(
				new DTOSchuelerMerkmale(1L, 42L),
				new DTOSchuelerMerkmale(2L, 42L),
				new DTOSchuelerMerkmale(3L, 42L));

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
		final var ids = List.<Long>of();

		when(repository.findListByIds(ids)).thenReturn(List.of());

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		final var result = service.delete(ids);

		assertThat(result).isNotNull().isEmpty();
		verify(repository, times(1)).findListByIds(ids);
	}

	@Test
	@DisplayName("delete | Ergebnis ist nach ID sortiert")
	void delete_resultIsSortedById() {
		final var ids = List.of(3L, 1L, 2L);
		final var entities = List.of(
				new DTOSchuelerMerkmale(3L, 42L),
				new DTOSchuelerMerkmale(1L, 42L),
				new DTOSchuelerMerkmale(2L, 42L));

		when(repository.findListByIds(ids)).thenReturn(entities);
		when(repository.delete(entities)).thenReturn(entities);

		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		final var result = service.delete(ids);

		assertThat(result)
				.extracting("id", Long.class)
				.containsExactly(1L, 2L, 3L);
	}
}
