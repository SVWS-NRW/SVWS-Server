package de.svws_nrw.service.schule.schulleitung;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.schule.Schulleitung;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.katalog.DTOLeitungsfunktion;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOSchulleitung;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.schulleitung.SchulleitungMapper;
import de.svws_nrw.repo.lehrer.leitungsfunktion.LehrerLeitungsfunktionRepository;
import de.svws_nrw.repo.schule.schulleitung.SchulleitungRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchulleitungServiceTest {

	@Mock
	private SchulleitungRepository repository;

	@Mock
	private LehrerLeitungsfunktionRepository lehrerLeitungsfunktionRepository;

	@Mock
	private SchulleitungMapper mapper;

	private SchulleitungService service;

	@Mock
	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeEach
	void setUp() {
		service = new SchulleitungService(repository, lehrerLeitungsfunktionRepository, mapper);
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}

	@Test
	@DisplayName("getAll | Erfolg mit mehreren Einträgen")
	void getAll_success() {
		final var entity1 = new DTOSchulleitung(1L, 1L, "Schulleitung", 10L);
		final var entity2 = new DTOSchulleitung(2L, 2L, "Koordination", 20L);
		final var apiModel1 = mock(Schulleitung.class);
		final var apiModel2 = mock(Schulleitung.class);

		when(repository.getAll()).thenReturn(List.of(entity1, entity2));
		when(mapper.toApi(entity1)).thenReturn(apiModel1);
		when(mapper.toApi(entity2)).thenReturn(apiModel2);

		final var result = service.getAll();

		assertThat(result)
				.isNotNull()
				.hasSize(2)
				.containsExactly(apiModel1, apiModel2);

		verify(repository, times(1)).getAll();
		verify(mapper, times(1)).toApi(entity1);
		verify(mapper, times(1)).toApi(entity2);
	}

	@Test
	@DisplayName("getAll | Leere Liste")
	void getAll_emptyList() {
		when(repository.getAll()).thenReturn(List.of());

		final var result = service.getAll();

		assertThat(result).isNotNull().isEmpty();
		verify(repository, times(1)).getAll();
		verify(mapper, never()).toApi(any(DTOSchulleitung.class));
	}

	@Test
	@DisplayName("getAllByIdLehrer | Erfolg mit mehreren Einträgen")
	void getAllByIdLehrer_success() {
		final var idLehrer = 42L;
		final var entity1 = new DTOSchulleitung(1L, 1L, "Schulleitung", idLehrer);
		final var entity2 = new DTOSchulleitung(2L, 2L, "Koordination", idLehrer);
		final var apiModel1 = mock(Schulleitung.class);
		final var apiModel2 = mock(Schulleitung.class);

		when(repository.getAllByIdLehrer(idLehrer)).thenReturn(List.of(entity1, entity2));
		when(mapper.toApi(entity1)).thenReturn(apiModel1);
		when(mapper.toApi(entity2)).thenReturn(apiModel2);

		final var result = service.getAllByIdLehrer(idLehrer);

		assertThat(result)
				.isNotNull()
				.hasSize(2)
				.containsExactly(apiModel1, apiModel2);

		verify(repository, times(1)).getAllByIdLehrer(idLehrer);
		verify(mapper, times(1)).toApi(entity1);
		verify(mapper, times(1)).toApi(entity2);
	}

	@Test
	@DisplayName("getAllByIdLehrer | Leere Liste")
	void getAllByIdLehrer_emptyList() {
		final var idLehrer = 99L;

		when(repository.getAllByIdLehrer(idLehrer)).thenReturn(List.of());

		final var result = service.getAllByIdLehrer(idLehrer);

		assertThat(result).isNotNull().isEmpty();
		verify(repository, times(1)).getAllByIdLehrer(idLehrer);
		verify(mapper, never()).toApi(any(DTOSchulleitung.class));
	}

	@Test
	@DisplayName("create | Erfolg")
	void create_success() {
		final var dto = new SchulleitungCreateRequest();
		dto.idLeitungsfunktion = 1L;
		dto.idLehrer = 42L;
		dto.bezeichnung = "Schulleitung";
		dto.datumBeginnLeitungsfunktion = "2023-08-01";
		dto.datumEndeLeitungsfunktion = "2024-07-31";

		final var entity = new DTOSchulleitung(1L, dto.idLeitungsfunktion, dto.bezeichnung, dto.idLehrer);
		final var apiModel = mock(Schulleitung.class);

		when(lehrerLeitungsfunktionRepository.findById(dto.idLeitungsfunktion)).thenReturn(Optional.of(mock(DTOLeitungsfunktion.class)));
		when(mapper.toDomain(dto)).thenReturn(entity);
		when(repository.create(entity)).thenReturn(entity);
		when(mapper.toApi(entity)).thenReturn(apiModel);

		final var result = service.create(dto);

		assertThat(result).isNotNull().isEqualTo(apiModel);
		verify(lehrerLeitungsfunktionRepository, times(1)).findById(dto.idLeitungsfunktion);
		verify(repository, times(1)).create(entity);
	}

	@Test
	@DisplayName("create | Erfolg ohne Datumsangaben")
	void create_successWithoutDates() {
		final var dto = new SchulleitungCreateRequest();
		dto.idLeitungsfunktion = 1L;
		dto.idLehrer = 42L;
		dto.bezeichnung = "Schulleitung";
		dto.datumBeginnLeitungsfunktion = null;
		dto.datumEndeLeitungsfunktion = null;

		final var entity = new DTOSchulleitung(1L, dto.idLeitungsfunktion, dto.bezeichnung, dto.idLehrer);
		final var apiModel = mock(Schulleitung.class);

		when(lehrerLeitungsfunktionRepository.findById(dto.idLeitungsfunktion)).thenReturn(Optional.of(mock(DTOLeitungsfunktion.class)));
		when(mapper.toDomain(dto)).thenReturn(entity);
		when(repository.create(entity)).thenReturn(entity);
		when(mapper.toApi(entity)).thenReturn(apiModel);

		assertThat(service.create(dto)).isEqualTo(apiModel);
	}

	@Test
	@DisplayName("create | Leitungsfunktion nicht gefunden")
	void create_leitungsfunktionNotFound_throwsBadRequest() {
		final var dto = new SchulleitungCreateRequest();
		dto.idLeitungsfunktion = 999L;
		dto.idLehrer = 42L;
		dto.bezeichnung = "Unbekannt";

		when(lehrerLeitungsfunktionRepository.findById(999L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("999")
				.extracting("status")
				.isEqualTo(Response.Status.BAD_REQUEST);

		verify(repository, never()).create(any(DTOSchulleitung.class));
	}

	@Test
	@DisplayName("create | datumBis vor datumVon")
	void create_datumBisBeforeDatumVon_throwsBadRequest() {
		final var dto = new SchulleitungCreateRequest();
		dto.idLeitungsfunktion = 1L;
		dto.idLehrer = 42L;
		dto.bezeichnung = "Schulleitung";
		dto.datumBeginnLeitungsfunktion = "2023-06-01";
		dto.datumEndeLeitungsfunktion = "2022-01-01";

		when(lehrerLeitungsfunktionRepository.findById(dto.idLeitungsfunktion)).thenReturn(Optional.of(mock(DTOLeitungsfunktion.class)));

		assertThatThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Enddatum")
				.extracting("status")
				.isEqualTo(Response.Status.BAD_REQUEST);

		verify(repository, never()).create(any(DTOSchulleitung.class));
	}

	@Test
	@DisplayName("create | datumVon gleich datumBis")
	void create_datumVonEqualsDatumBis_success() {
		final var dto = new SchulleitungCreateRequest();
		dto.idLeitungsfunktion = 1L;
		dto.idLehrer = 42L;
		dto.bezeichnung = "Schulleitung";
		dto.datumBeginnLeitungsfunktion = "2023-06-01";
		dto.datumEndeLeitungsfunktion = "2023-06-01";

		final var entity = new DTOSchulleitung(1L, dto.idLeitungsfunktion, dto.bezeichnung, dto.idLehrer);
		final var apiModel = mock(Schulleitung.class);

		when(lehrerLeitungsfunktionRepository.findById(dto.idLeitungsfunktion)).thenReturn(Optional.of(mock(DTOLeitungsfunktion.class)));
		when(mapper.toDomain(dto)).thenReturn(entity);
		when(repository.create(entity)).thenReturn(entity);
		when(mapper.toApi(entity)).thenReturn(apiModel);

		assertThat(service.create(dto)).isEqualTo(apiModel);
	}

	@Test
	@DisplayName("patch | Erfolg – alle Felder gesetzt")
	void patch_success_allFieldsSet() {
		final var id = 1L;
		final var dto = new SchulleitungPatchRequest();
		dto.idLeitungsfunktion = JsonNullable.of(2L);
		dto.bezeichnung = JsonNullable.of("Koordination");
		dto.datumBeginnLeitungsfunktion = JsonNullable.of("2023-09-01");
		dto.datumEndeLeitungsfunktion = JsonNullable.of("2024-06-30");

		final var entity = new DTOSchulleitung(id, 1L, "Schulleitung", 42L);
		final var apiModel = mock(Schulleitung.class);

		when(repository.getById(id)).thenReturn(entity);
		when(lehrerLeitungsfunktionRepository.findById(2L)).thenReturn(Optional.of(mock(DTOLeitungsfunktion.class)));
		when(mapper.toApi(entity)).thenReturn(apiModel);

		final var result = service.patch(id, dto);

		assertThat(result).isNotNull().isEqualTo(apiModel);
		verify(repository, times(1)).getById(id);
		verify(lehrerLeitungsfunktionRepository, times(1)).findById(2L);
		verify(mapper, times(1)).patch(dto, entity);
	}

	@Test
	@DisplayName("patch | Erfolg – keine Felder gesetzt (undefined)")
	void patch_success_noFieldsSet() {
		final var id = 1L;
		final var dto = new SchulleitungPatchRequest();

		final var entity = new DTOSchulleitung(id, 1L, "Schulleitung", 42L);
		entity.Von = "2023-09-01";
		entity.Bis = "2024-06-30";
		final var apiModel = mock(Schulleitung.class);

		when(repository.getById(id)).thenReturn(entity);
		when(mapper.toApi(entity)).thenReturn(apiModel);

		assertThat(service.patch(id, dto)).isEqualTo(apiModel);
		verify(lehrerLeitungsfunktionRepository, never()).findById(anyLong());
		verify(mapper, times(1)).patch(dto, entity);
	}

	@Test
	@DisplayName("patch | Leitungsfunktion nicht gefunden")
	void patch_leitungsfunktionNotFound_throwsBadRequest() {
		final var id = 1L;
		final var dto = new SchulleitungPatchRequest();
		dto.idLeitungsfunktion = JsonNullable.of(999L);

		final var entity = new DTOSchulleitung(id, 1L, "Schulleitung", 42L);

		when(repository.getById(id)).thenReturn(entity);
		when(lehrerLeitungsfunktionRepository.findById(999L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.patch(id, dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("999")
				.extracting("status")
				.isEqualTo(Response.Status.BAD_REQUEST);

		verify(mapper, never()).patch(any(), any());
	}

	@Test
	@DisplayName("patch | datumdatumEndeLeitungsfunktion vor datumVon")
	void patch_datumBisBeforeDatumVon_fromDto_throwsBadRequest() {
		final var id = 1L;
		final var dto = new SchulleitungPatchRequest();
		dto.datumBeginnLeitungsfunktion = JsonNullable.of("2023-06-01");
		dto.datumEndeLeitungsfunktion = JsonNullable.of("2022-01-01");

		final var entity = new DTOSchulleitung(id, 1L, "Schulleitung", 42L);

		when(repository.getById(id)).thenReturn(entity);

		assertThatThrownBy(() -> service.patch(id, dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Enddatum")
				.extracting("status")
				.isEqualTo(Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | datumBis vor entity.Von (Fallback aus Entity) → BAD_REQUEST")
	void patch_datumBisBeforeDatumVon_fromEntityFallback_throwsBadRequest() {
		final var id = 1L;
		final var dto = new SchulleitungPatchRequest();
		dto.datumEndeLeitungsfunktion = JsonNullable.of("2019-01-01");

		final var entity = new DTOSchulleitung(id, 1L, "Schulleitung", 42L);
		entity.Von = "2020-09-01";
		entity.Bis = null;

		when(repository.getById(id)).thenReturn(entity);

		assertThatThrownBy(() -> service.patch(id, dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Enddatum")
				.extracting("status")
				.isEqualTo(Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("delete | Erfolg mit mehreren IDs")
	void delete_success() {
		final var ids = List.of(1L, 2L, 3L);
		final var entities = List.of(
				new DTOSchulleitung(1L, 1L, "Schulleitung", 10L),
				new DTOSchulleitung(2L, 2L, "Koordination", 20L),
				new DTOSchulleitung(3L, 3L, "Schulverwaltung", 30L));

		when(repository.findListByIds(ids)).thenReturn(entities);
		when(repository.delete(entities)).thenReturn(entities);

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

		final var result = service.delete(ids);

		assertThat(result).isNotNull().isEmpty();
		verify(repository, times(1)).findListByIds(ids);
	}

	@Test
	@DisplayName("delete | Ergebnis ist nach ID sortiert")
	void delete_resultIsSortedById() {
		final var ids = List.of(3L, 1L, 2L);
		final var entities = List.of(
				new DTOSchulleitung(3L, 3L, "Schulverwaltung", 30L),
				new DTOSchulleitung(1L, 1L, "Schulleitung", 10L),
				new DTOSchulleitung(2L, 2L, "Koordination", 20L));

		when(repository.findListByIds(ids)).thenReturn(entities);
		when(repository.delete(entities)).thenReturn(entities);

		final var result = service.delete(ids);

		assertThat(result)
				.extracting("id", Long.class)
				.containsExactly(1L, 2L, 3L);
	}
}
