package de.svws_nrw.service.schueler.schulbesuch;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.data.kataloge.DataKatalogEntlassgruende;
import de.svws_nrw.data.schule.DataSchulen;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.db.dto.current.schild.schueler.DTOEntlassarten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerAbgaenge;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.schulbesuch.SchuelerBisherigeSchuleMapper;
import de.svws_nrw.repo.schueler.schulbesuch.SchuelerBisherigeSchuleRepository;
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
class BisherigeEigeneSchuleServiceTest {

	@Mock
	private SchuelerBisherigeSchuleRepository repository;

	@Mock
	private SchuelerBisherigeSchuleMapper mapper;

	@Mock
	private DataSchulen dataSchulen;

	@Mock
	private DataKatalogEntlassgruende dataKatalogEntlassgruende;

	private SchuelerBisherigeSchuleService service;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	// Testdaten
	private DTOSchuleNRW schule;
	private DTOEntlassarten entlassgrund;

	@BeforeEach
	void setUp() {
		schule = new DTOSchuleNRW(10L, "123456");
		entlassgrund = new DTOEntlassarten(20L, "Abschluss");

		service = new SchuelerBisherigeSchuleService(repository, mapper, dataSchulen, dataKatalogEntlassgruende);
		transactionSupportMock = mockStatic(TransactionSupport.class);
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
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
		final var entity = new DTOSchuelerAbgaenge(1L, idSchueler);
		entity.schulnummer = schule.SchulNr;
		entity.bezeichnungEntlassgrund = entlassgrund.Bezeichnung;

		final var apiModel = mock(SchuelerSchulbesuchSchule.class);

		when(dataSchulen.getAllEntities()).thenReturn(List.of(schule));
		when(dataKatalogEntlassgruende.getAllEntities()).thenReturn(List.of(entlassgrund));
		when(repository.getAllByIdSchueler(idSchueler)).thenReturn(List.of(entity));
		when(mapper.toApi(entity, entlassgrund.ID, schule.ID)).thenReturn(apiModel);

		final var result = service.getAllByIdSchueler(idSchueler);

		assertThat(result)
				.isNotNull()
				.hasSize(1)
				.containsExactly(apiModel);

		verify(repository, times(1)).getAllByIdSchueler(idSchueler);
		verify(mapper, times(1)).toApi(entity, entlassgrund.ID, schule.ID);
	}

	@Test
	@DisplayName("getAllByIdSchueler | Leere Liste")
	void getAllByIdSchueler_emptyList() {
		final var idSchueler = 99L;

		when(dataSchulen.getAllEntities()).thenReturn(List.of());
		when(dataKatalogEntlassgruende.getAllEntities()).thenReturn(List.of());
		when(repository.getAllByIdSchueler(idSchueler)).thenReturn(List.of());

		final var result = service.getAllByIdSchueler(idSchueler);

		assertThat(result).isNotNull().isEmpty();
		verify(repository, times(1)).getAllByIdSchueler(idSchueler);
	}

	@Test
	@DisplayName("getAllByIdSchueler | Schule und Entlassgrund unbekannt → IDs sind null")
	void getAllByIdSchueler_unknownSchuleAndEntlassgrund_idsAreNull() {
		final var idSchueler = 1L;
		final var entity = new DTOSchuelerAbgaenge(1L, idSchueler);
		entity.schulnummer = "999999";          // nicht im Katalog
		entity.bezeichnungEntlassgrund = "Unbekannt"; // nicht im Katalog

		final var apiModel = mock(SchuelerSchulbesuchSchule.class);

		when(dataSchulen.getAllEntities()).thenReturn(List.of(schule));
		when(dataKatalogEntlassgruende.getAllEntities()).thenReturn(List.of(entlassgrund));
		when(repository.getAllByIdSchueler(idSchueler)).thenReturn(List.of(entity));
		when(mapper.toApi(entity, null, null)).thenReturn(apiModel);

		final var result = service.getAllByIdSchueler(idSchueler);

		assertThat(result).containsExactly(apiModel);
		verify(mapper, times(1)).toApi(entity, null, null);
	}

	@Test
	@DisplayName("getAllByIdSchueler | Schule und Entlassgrund null → IDs sind null")
	void getAllByIdSchueler_nullSchuleAndEntlassgrund_idsAreNull() {
		final var idSchueler = 1L;
		final var entity = new DTOSchuelerAbgaenge(1L, idSchueler);
		entity.schulnummer = null;
		entity.bezeichnungEntlassgrund = null;

		final var apiModel = mock(SchuelerSchulbesuchSchule.class);

		when(dataSchulen.getAllEntities()).thenReturn(List.of(schule));
		when(dataKatalogEntlassgruende.getAllEntities()).thenReturn(List.of(entlassgrund));
		when(repository.getAllByIdSchueler(idSchueler)).thenReturn(List.of(entity));
		when(mapper.toApi(entity, null, null)).thenReturn(apiModel);

		final var result = service.getAllByIdSchueler(idSchueler);

		assertThat(result).containsExactly(apiModel);
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("create | Erfolg")
	void create_success() {
		final var dto = new SchuelerBisherigeSchuleCreateRequest();
		dto.idSchule = schule.ID;
		dto.idEntlassgrund = entlassgrund.ID;
		dto.datumVon = "2020-09-01";
		dto.datumBis = "2023-06-30";

		final var entity = new DTOSchuelerAbgaenge(1L, 42L);
		entity.schulnummer = schule.SchulNr;
		entity.bezeichnungEntlassgrund = entlassgrund.Bezeichnung;

		final var apiModel = mock(SchuelerSchulbesuchSchule.class);

		when(dataKatalogEntlassgruende.getEntityById(entlassgrund.ID)).thenReturn(entlassgrund);
		when(dataSchulen.getEntityById(schule.ID)).thenReturn(schule);
		when(mapper.toDomain(dto, entlassgrund.Bezeichnung, schule.SchulNr)).thenReturn(entity);
		when(repository.create(entity)).thenReturn(entity);
		when(mapper.toApi(entity, entlassgrund.ID, schule.ID)).thenReturn(apiModel);

		final var result = service.create(dto);

		assertThat(result).isNotNull().isEqualTo(apiModel);
		verify(repository, times(1)).create(entity);
	}

	@Test
	@DisplayName("create | Erfolg ohne Entlassgrund")
	void create_successWithoutEntlassgrund() {
		final var dto = new SchuelerBisherigeSchuleCreateRequest();
		dto.idSchule = schule.ID;
		dto.idEntlassgrund = null;
		dto.datumVon = null;
		dto.datumBis = null;

		final var entity = new DTOSchuelerAbgaenge(1L, 42L);
		entity.schulnummer = schule.SchulNr;
		entity.bezeichnungEntlassgrund = null;

		final var apiModel = mock(SchuelerSchulbesuchSchule.class);

		when(dataSchulen.getEntityById(schule.ID)).thenReturn(schule);
		when(mapper.toDomain(dto, null, schule.SchulNr)).thenReturn(entity);
		when(repository.create(entity)).thenReturn(entity);
		when(mapper.toApi(entity, null, schule.ID)).thenReturn(apiModel);

		final var result = service.create(dto);

		assertThat(result).isEqualTo(apiModel);
	}

	@Test
	@DisplayName("create | idSchule null → BAD_REQUEST")
	void create_idSchuleNull_throwsBadRequest() {
		final var dto = new SchuelerBisherigeSchuleCreateRequest();
		dto.idSchule = null;
		dto.idEntlassgrund = null;

		assertThatThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("idSchule ist ein Pflichtfeld")
				.extracting("status")
				.isEqualTo(Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create | idSchule nicht gefunden → NOT_FOUND")
	void create_idSchuleNotFound_throwsNotFound() {
		final var dto = new SchuelerBisherigeSchuleCreateRequest();
		dto.idSchule = 999L;
		dto.idEntlassgrund = null;

		when(dataSchulen.getEntityById(999L))
				.thenThrow(new ApiOperationException(Response.Status.NOT_FOUND, "999"));

		assertThatThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("999")
				.extracting("status")
				.isEqualTo(Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("create | idEntlassgrund nicht gefunden → NOT_FOUND")
	void create_idEntlassgrundNotFound_throwsNotFound() {
		final var dto = new SchuelerBisherigeSchuleCreateRequest();
		dto.idSchule = schule.ID;
		dto.idEntlassgrund = 999L;

		when(dataKatalogEntlassgruende.getEntityById(999L))
				.thenThrow(new ApiOperationException(Response.Status.NOT_FOUND, "999"));

		assertThatThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("999")
				.extracting("status")
				.isEqualTo(Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("create | datumBis vor datumVon → BAD_REQUEST")
	void create_datumBisBeforeDatumVon_throwsBadRequest() {
		final var dto = new SchuelerBisherigeSchuleCreateRequest();
		dto.idSchule = schule.ID;
		dto.idEntlassgrund = null;
		dto.datumVon = "2023-06-01";
		dto.datumBis = "2022-01-01";

		when(dataSchulen.getEntityById(schule.ID)).thenReturn(schule);

		assertThatThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Enddatum")
				.extracting("status")
				.isEqualTo(Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create | datumVon gleich datumBis → gültig")
	void create_datumVonEqualsDatumBis_success() {
		final var dto = new SchuelerBisherigeSchuleCreateRequest();
		dto.idSchule = schule.ID;
		dto.idEntlassgrund = null;
		dto.datumVon = "2023-06-01";
		dto.datumBis = "2023-06-01";

		final var entity = new DTOSchuelerAbgaenge(1L, 42L);
		entity.schulnummer = schule.SchulNr;

		final var apiModel = mock(SchuelerSchulbesuchSchule.class);

		when(dataSchulen.getEntityById(schule.ID)).thenReturn(schule);
		when(mapper.toDomain(dto, null, schule.SchulNr)).thenReturn(entity);
		when(repository.create(entity)).thenReturn(entity);
		when(mapper.toApi(entity, null, schule.ID)).thenReturn(apiModel);

		assertThat(service.create(dto)).isEqualTo(apiModel);
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch | Erfolg – alle Felder gesetzt")
	void patch_success_allFieldsSet() {
		final var id = 1L;
		final var dto = new SchuelerBisherigeSchulePatchRequest();
		dto.idSchule = JsonNullable.of(schule.ID);
		dto.idEntlassgrund = JsonNullable.of(entlassgrund.ID);
		dto.datumVon = JsonNullable.of("2020-09-01");
		dto.datumBis = JsonNullable.of("2023-06-30");

		final var entity = new DTOSchuelerAbgaenge(id, 42L);
		entity.schulnummer = schule.SchulNr;
		entity.bezeichnungEntlassgrund = entlassgrund.Bezeichnung;

		final var apiModel = mock(SchuelerSchulbesuchSchule.class);

		when(repository.getById(id)).thenReturn(entity);
		when(dataSchulen.getEntityById(schule.ID)).thenReturn(schule);
		when(dataKatalogEntlassgruende.getEntityById(entlassgrund.ID)).thenReturn(entlassgrund);
		when(mapper.toApi(entity, entlassgrund.ID, schule.ID)).thenReturn(apiModel);

		final var result = service.patch(id, dto);

		assertThat(result).isNotNull().isEqualTo(apiModel);
		verify(repository, times(1)).getById(id);
		verify(mapper, times(1)).patch(dto, entity);
	}

	@Test
	@DisplayName("patch | Erfolg – keine optionalen Felder gesetzt")
	void patch_success_noOptionalFieldsSet() {
		final var id = 1L;
		final var dto = new SchuelerBisherigeSchulePatchRequest();
		dto.idSchule = JsonNullable.undefined();
		dto.idEntlassgrund = JsonNullable.undefined();
		dto.datumVon = JsonNullable.undefined();
		dto.datumBis = JsonNullable.undefined();

		final var entity = new DTOSchuelerAbgaenge(id, 42L);
		entity.schulnummer = schule.SchulNr;
		entity.bezeichnungEntlassgrund = entlassgrund.Bezeichnung;
		entity.datumVon = "2020-09-01";
		entity.datumBis = "2023-06-30";

		final var apiModel = mock(SchuelerSchulbesuchSchule.class);

		when(repository.getById(id)).thenReturn(entity);
		when(dataSchulen.getEntityBySchulnummer(schule.SchulNr)).thenReturn(schule);
		when(dataKatalogEntlassgruende.getEntityByBezeichnung(entlassgrund.Bezeichnung)).thenReturn(entlassgrund);
		when(mapper.toApi(entity, entlassgrund.ID, schule.ID)).thenReturn(apiModel);

		final var result = service.patch(id, dto);

		assertThat(result).isEqualTo(apiModel);
	}

	@Test
	@DisplayName("patch | idSchule nicht gefunden → NOT_FOUND")
	void patch_idSchuleNotFound_throwsNotFound() {
		final var id = 1L;
		final var dto = new SchuelerBisherigeSchulePatchRequest();
		dto.idSchule = JsonNullable.of(999L);
		dto.idEntlassgrund = JsonNullable.undefined();
		dto.datumVon = JsonNullable.undefined();
		dto.datumBis = JsonNullable.undefined();

		final var entity = new DTOSchuelerAbgaenge(id, 42L);

		when(repository.getById(id)).thenReturn(entity);
		when(dataSchulen.getEntityById(999L))
				.thenThrow(new ApiOperationException(Response.Status.NOT_FOUND, "999"));

		assertThatThrownBy(() -> service.patch(id, dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("999")
				.extracting("status")
				.isEqualTo(Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch | idSchule null → BAD_REQUEST")
	void patch_idSchuleNull_throwsBadRequest() {
		final var id = 1L;
		final var dto = new SchuelerBisherigeSchulePatchRequest();
		dto.idSchule = JsonNullable.of(null);
		dto.idEntlassgrund = JsonNullable.undefined();
		dto.datumVon = JsonNullable.undefined();
		dto.datumBis = JsonNullable.undefined();

		final var entity = new DTOSchuelerAbgaenge(id, 42L);

		when(repository.getById(id)).thenReturn(entity);

		assertThatThrownBy(() -> service.patch(id, dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("idSchule ist ein Pflichtfeld")
				.extracting("status")
				.isEqualTo(Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | idEntlassgrund nicht gefunden → NOT_FOUND")
	void patch_idEntlassgrundNotFound_throwsNotFound() {
		final var id = 1L;
		final var dto = new SchuelerBisherigeSchulePatchRequest();
		dto.idSchule = JsonNullable.undefined();
		dto.idEntlassgrund = JsonNullable.of(999L);
		dto.datumVon = JsonNullable.undefined();
		dto.datumBis = JsonNullable.undefined();

		final var entity = new DTOSchuelerAbgaenge(id, 42L);
		entity.schulnummer = schule.SchulNr;

		when(repository.getById(id)).thenReturn(entity);
		when(dataKatalogEntlassgruende.getEntityById(999L))
				.thenThrow(new ApiOperationException(Response.Status.NOT_FOUND, "999"));

		assertThatThrownBy(() -> service.patch(id, dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("999")
				.extracting("status")
				.isEqualTo(Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch | datumBis vor datumVon (dto-Werte) → BAD_REQUEST")
	void patch_datumBisBeforeDatumVon_fromDto_throwsBadRequest() {
		final var id = 1L;
		final var dto = new SchuelerBisherigeSchulePatchRequest();
		dto.idSchule = JsonNullable.undefined();
		dto.idEntlassgrund = JsonNullable.undefined();
		dto.datumVon = JsonNullable.of("2023-06-01");
		dto.datumBis = JsonNullable.of("2022-01-01");
		final var entity = new DTOSchuelerAbgaenge(id, 42L);
		entity.schulnummer = schule.SchulNr;
		when(repository.getById(id)).thenReturn(entity);

		assertThatThrownBy(() -> service.patch(id, dto))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Enddatum")
				.extracting("status")
				.isEqualTo(Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | datumBis vor datumVon (Entity-Fallback) → BAD_REQUEST")
	void patch_datumBisBeforeDatumVon_fromEntityFallback_throwsBadRequest() {
		final var id = 1L;
		final var dto = new SchuelerBisherigeSchulePatchRequest();
		dto.idSchule = JsonNullable.undefined();
		dto.idEntlassgrund = JsonNullable.undefined();
		dto.datumVon = JsonNullable.undefined();
		dto.datumBis = JsonNullable.of("2019-01-01"); // vor entity.datumVon
		final var entity = new DTOSchuelerAbgaenge(id, 42L);
		entity.schulnummer = schule.SchulNr;
		entity.datumVon = "2020-09-01";
		entity.datumBis = null;
		when(repository.getById(id)).thenReturn(entity);



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
		final var entity1 = new DTOSchuelerAbgaenge(1L, 42L);
		final var entity2 = new DTOSchuelerAbgaenge(2L, 42L);
		final var entity3 = new DTOSchuelerAbgaenge(3L, 42L);
		final var entities = List.of(entity1, entity2, entity3);
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
		final var entity1 = new DTOSchuelerAbgaenge(3L, 42L);
		final var entity2 = new DTOSchuelerAbgaenge(1L, 42L);
		final var entity3 = new DTOSchuelerAbgaenge(2L, 42L);
		final var entities = List.of(entity1, entity2, entity3);
		when(repository.findListByIds(ids)).thenReturn(entities);
		when(repository.delete(entities)).thenReturn(entities);

		final var result = service.delete(ids);

		assertThat(result)
				.extracting("id", Long.class)
				.containsExactly(1L, 2L, 3L);
	}
}
