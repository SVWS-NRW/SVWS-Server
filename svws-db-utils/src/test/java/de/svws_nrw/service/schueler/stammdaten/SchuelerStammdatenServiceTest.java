package de.svws_nrw.service.schueler.stammdaten;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.stammdaten.SchuelerStammdatenMapper;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schule.kataloge.fahrschuelerart.FahrschuelerartRepository;
import de.svws_nrw.repo.schule.kataloge.haltestelle.HaltestelleRepository;
import de.svws_nrw.repo.schule.kataloge.ort.OrtRepository;
import de.svws_nrw.repo.schule.kataloge.ortsteil.OrtsteilRepository;
import de.svws_nrw.repo.schule.kataloge.religion.ReligionRepository;
import de.svws_nrw.service.schueler.foto.SchuelerFoto;
import de.svws_nrw.service.schueler.foto.SchuelerFotoService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchuelerStammdatenServiceTest {

	// -------------------------------------------------------------------------
	// Gültige CoreType-IDs für Tests
	// -------------------------------------------------------------------------
	/** Geschlecht männlich (ID 3) */
	private static final int GESCHLECHT_M = 3;
	/** Ungültige Geschlechts-ID */
	private static final int GESCHLECHT_INVALID = 999;

	/** Schülerstatus Neuaufnahme (ID 0) */
	private static final int STATUS_NEUAUFNAHME = 0;
	/** Ungültige Status-ID */
	private static final int STATUS_INVALID = 9999;

	/** Gültige Nationalitäten-ID (Algerien) */
	private static final long NATIONALITAET_DZA = 68090065L;
	/** Ungültige Nationalitäten-ID */
	private static final long NATIONALITAET_INVALID = 999999999L;

	/** Gültige Verkehrssprachen-ID */
	private static final long VERKEHRSSPRACHE_XY = 2L;
	/** Ungültige Verkehrssprachen-ID */
	private static final long VERKEHRSSPRACHE_INVALID = 999999999L;

	// -------------------------------------------------------------------------
	// Mocks
	// -------------------------------------------------------------------------

	@Mock
	private SchuelerRepository repository;

	@Mock
	private ReligionRepository religionRepository;

	@Mock
	private OrtRepository ortRepository;

	@Mock
	private OrtsteilRepository ortsteilRepository;

	@Mock
	private FahrschuelerartRepository fahrschuelerartRepository;

	@Mock
	private HaltestelleRepository haltestelleRepository;

	@Mock
	private SchuelerStammdatenMapper mapper;

	@Mock
	private SchuelerFotoService schuelerFotoService;

	private SchuelerStammdatenService service;

	private MockedStatic<TransactionSupport> transactionSupport;

	// -------------------------------------------------------------------------
	// Testdaten
	// -------------------------------------------------------------------------

	private DTOSchueler entity;
	private SchuelerStammdaten apiModel;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		entity = new DTOSchueler(1L, "{test-guid}", false);
		apiModel = new SchuelerStammdaten();
		apiModel.id = 1L;

		transactionSupport = mockStatic(TransactionSupport.class);
		transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
		final var repositories = new SchuelerStammdatenRepositories(
				repository,
				religionRepository,
				ortRepository,
				ortsteilRepository,
				fahrschuelerartRepository,
				haltestelleRepository
		);
		service = new SchuelerStammdatenService(
				repositories,
				mapper,
				schuelerFotoService
		);

		// Default-Stub: kein Foto vorhanden
		lenient().when(schuelerFotoService.getBySchuelerIds(anyList())).thenReturn(List.of());
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	private void stubToApi(final SchuelerStammdaten result) {
		lenient().when(mapper.toApi(any(DTOSchueler.class))).thenReturn(result);
	}

	// =========================================================================
	// get
	// =========================================================================

	@Test
	@DisplayName("get - not found")
	void get_notFound() {
		when(repository.findById(99L)).thenReturn(Optional.empty());

		assertThatException()
				.isThrownBy(() -> service.get(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine SchuelerStammdaten mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("get - gefunden, kein Foto")
	void get_found_keinFoto() {
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		stubToApi(apiModel);

		final var result = service.get(1L);

		assertThat(result).isEqualTo(apiModel);
		assertThat(result.foto).isNull();
	}

	@Test
	@DisplayName("get - gefunden, mit Foto")
	void get_found_mitFoto() {
		final var foto = new SchuelerFoto(1L, "base64data");
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(schuelerFotoService.getBySchuelerIds(List.of(1L))).thenReturn(List.of(foto));
		stubToApi(apiModel);

		final var result = service.get(1L);

		assertThat(result.foto).isEqualTo("base64data");
	}

	// =========================================================================
	// getList
	// =========================================================================

	@Test
	@DisplayName("getList - null -> leere Liste")
	void getList_null() {
		assertThat(service.getList(null)).isEmpty();
		verify(repository, never()).findListByIds(anyList());
	}

	@Test
	@DisplayName("getList - leer -> leere Liste")
	void getList_empty() {
		assertThat(service.getList(List.of())).isEmpty();
		verify(repository, never()).findListByIds(anyList());
	}

	@Test
	@DisplayName("getList - zwei Einträge")
	void getList() {
		final var entity2 = new DTOSchueler(2L, "{guid-2}", false);
		final var apiModel2 = new SchuelerStammdaten();
		apiModel2.id = 2L;

		when(repository.findListByIds(List.of(1L, 2L))).thenReturn(List.of(entity, entity2));
		when(mapper.toApi(entity)).thenReturn(apiModel);
		when(mapper.toApi(entity2)).thenReturn(apiModel2);

		final var result = service.getList(List.of(1L, 2L));

		assertThat(result).containsExactly(apiModel, apiModel2);
	}

	// =========================================================================
	// create
	// =========================================================================

	@Test
	@DisplayName("create - ungültiges Geschlecht -> BAD_REQUEST")
	void create_ungueltigesGeschlecht() {
		final var dto = buildImportData(GESCHLECHT_INVALID, STATUS_NEUAUFNAHME, null);

		assertThatException()
				.isThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create - ungültiger Schülerstatus -> BAD_REQUEST")
	void create_ungueltigerStatus() {
		final var dto = buildImportData(GESCHLECHT_M, STATUS_INVALID, null);

		assertThatException()
				.isThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create - Religion nicht gefunden -> BAD_REQUEST")
	void create_religionNichtGefunden() {
		final var dto = buildImportData(GESCHLECHT_M, STATUS_NEUAUFNAHME, 42L);
		when(religionRepository.existsById(42L)).thenReturn(false);

		assertThatException()
				.isThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create - erfolgreich ohne Religion")
	void create_erfolgreich_ohneReligion() {
		final var dto = buildImportData(GESCHLECHT_M, STATUS_NEUAUFNAHME, null);
		when(mapper.toDomain(any(SchuelerImportData.class), any(String.class))).thenReturn(entity);
		when(repository.create(entity)).thenReturn(entity);
		stubToApi(apiModel);

		final var result = service.create(dto);

		assertThat(result).isEqualTo(apiModel);
	}

	@Test
	@DisplayName("create - erfolgreich mit gültiger Religion")
	void create_erfolgreich_mitReligion() {
		final var dto = buildImportData(GESCHLECHT_M, STATUS_NEUAUFNAHME, 1L);
		when(religionRepository.existsById(1L)).thenReturn(true);
		when(mapper.toDomain(any(SchuelerImportData.class), any(String.class))).thenReturn(entity);
		when(repository.create(entity)).thenReturn(entity);
		stubToApi(apiModel);

		final var result = service.create(dto);

		assertThat(result).isEqualTo(apiModel);
	}

	// =========================================================================
	// patch
	// =========================================================================

	@Test
	@DisplayName("patch - not found -> NOT_FOUND")
	void patch_notFound() {
		when(repository.findById(99L)).thenReturn(Optional.empty());

		assertThatException()
				.isThrownBy(() -> service.patch(99L, new SchuelerStammdatenPatchRequest()))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine SchuelerStammdaten mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch - ungültiges Geschlecht -> BAD_REQUEST")
	void patch_ungueltigesGeschlecht() {
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		final var req = new SchuelerStammdatenPatchRequest();
		req.geschlecht = JsonNullable.of(GESCHLECHT_INVALID);

		assertThatException()
				.isThrownBy(() -> service.patch(1L, req))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch - ungültiger Status -> BAD_REQUEST")
	void patch_ungueltigerStatus() {
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		final var req = new SchuelerStammdatenPatchRequest();
		req.status = JsonNullable.of(STATUS_INVALID);

		assertThatException()
				.isThrownBy(() -> service.patch(1L, req))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch - Religion nicht gefunden -> BAD_REQUEST")
	void patch_religionNichtGefunden() {
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(religionRepository.existsById(42L)).thenReturn(false);
		final var req = new SchuelerStammdatenPatchRequest();
		req.religionID = JsonNullable.of(42L);

		assertThatException()
				.isThrownBy(() -> service.patch(1L, req))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch - ungültige Nationalität -> BAD_REQUEST")
	void patch_ungueltigeNationalitaet() {
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		final var req = new SchuelerStammdatenPatchRequest();
		req.idStaatsangehoerigkeit = JsonNullable.of(NATIONALITAET_INVALID);

		assertThatException()
				.isThrownBy(() -> service.patch(1L, req))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch - ungültige Verkehrssprache -> BAD_REQUEST")
	void patch_ungueltigeVerkehrssprache() {
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		final var req = new SchuelerStammdatenPatchRequest();
		req.idVerkehrspracheFamilie = JsonNullable.of(VERKEHRSSPRACHE_INVALID);

		assertThatException()
				.isThrownBy(() -> service.patch(1L, req))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch - Fahrschülerart nicht gefunden -> BAD_REQUEST")
	void patch_fahrschuelerartNichtGefunden() {
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(fahrschuelerartRepository.existsById(7L)).thenReturn(false);
		final var req = new SchuelerStammdatenPatchRequest();
		req.fahrschuelerArtID = JsonNullable.of(7L);

		assertThatException()
				.isThrownBy(() -> service.patch(1L, req))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch - Haltestelle nicht gefunden -> BAD_REQUEST")
	void patch_haltestelleNichtGefunden() {
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(haltestelleRepository.existsById(5L)).thenReturn(false);
		final var req = new SchuelerStammdatenPatchRequest();
		req.haltestelleID = JsonNullable.of(5L);

		assertThatException()
				.isThrownBy(() -> service.patch(1L, req))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch - Ortsteil ohne Ort -> BAD_REQUEST")
	void patch_ortsteilOhneOrt() {
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		final var req = new SchuelerStammdatenPatchRequest();
		req.wohnortID = JsonNullable.of(null);
		req.ortsteilID = JsonNullable.of(10L);

		assertThatException()
				.isThrownBy(() -> service.patch(1L, req))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch - Ort nicht gefunden -> NOT_FOUND")
	void patch_ortNichtGefunden() {
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(ortRepository.existsById(100L)).thenReturn(false);
		final var req = new SchuelerStammdatenPatchRequest();
		req.wohnortID = JsonNullable.of(100L);
		req.ortsteilID = JsonNullable.undefined();

		assertThatException()
				.isThrownBy(() -> service.patch(1L, req))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch - Ortsteil nicht zum Ort -> BAD_REQUEST")
	void patch_ortsteilPasstNichtZuOrt() {
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(ortRepository.existsById(100L)).thenReturn(true);

		// Ortsteil gehört zu einem anderen Ort
		final var falscherOrtsteil = new de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil(20L, "Falscher Ortsteil");
		falscherOrtsteil.idOrt = 999L;
		when(ortsteilRepository.findById(20L)).thenReturn(Optional.of(falscherOrtsteil));

		final var req = new SchuelerStammdatenPatchRequest();
		req.wohnortID = JsonNullable.of(100L);
		req.ortsteilID = JsonNullable.of(20L);

		assertThatException()
				.isThrownBy(() -> service.patch(1L, req))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch - Ortsteil nicht gefunden -> NOT_FOUND")
	void patch_ortsteilNichtGefunden() {
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(ortRepository.existsById(100L)).thenReturn(true);
		when(ortsteilRepository.findById(20L)).thenReturn(Optional.empty());

		final var req = new SchuelerStammdatenPatchRequest();
		req.wohnortID = JsonNullable.of(100L);
		req.ortsteilID = JsonNullable.of(20L);

		assertThatException()
				.isThrownBy(() -> service.patch(1L, req))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch - erfolgreich, alle Felder undefined")
	void patch_erfolgreich_alleUndefined() {
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		stubToApi(apiModel);

		final var result = service.patch(1L, new SchuelerStammdatenPatchRequest());

		assertThat(result).isEqualTo(apiModel);
		verify(mapper).patch(any(SchuelerStammdatenPatchRequest.class), any(DTOSchueler.class));
	}

	@Test
	@DisplayName("patch - Foto wird an SchuelerFotoService delegiert")
	void patch_fotoDelegiert() {
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		stubToApi(apiModel);

		final var req = new SchuelerStammdatenPatchRequest();
		req.foto = JsonNullable.of("neuesFoto");

		service.patch(1L, req);

		verify(schuelerFotoService).upsertOrDelete(1L, "neuesFoto");
	}

	@Test
	@DisplayName("patch - gültige Nationalität wird akzeptiert")
	void patch_gueltigeNationalitaet() {
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		stubToApi(apiModel);

		final var req = new SchuelerStammdatenPatchRequest();
		req.idStaatsangehoerigkeit = JsonNullable.of(NATIONALITAET_DZA);

		final var result = service.patch(1L, req);

		assertThat(result).isEqualTo(apiModel);
	}

	@Test
	@DisplayName("patch - gültige Verkehrssprache wird akzeptiert")
	void patch_gueltigeVerkehrssprache() {
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		stubToApi(apiModel);

		final var req = new SchuelerStammdatenPatchRequest();
		req.idVerkehrspracheFamilie = JsonNullable.of(VERKEHRSSPRACHE_XY);

		final var result = service.patch(1L, req);

		assertThat(result).isEqualTo(apiModel);
	}

	@Test
	@DisplayName("patch - null-Nationalität wird ignoriert (kein Fehler)")
	void patch_nullNationalitaet_wirdIgnoriert() {
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		stubToApi(apiModel);

		final var req = new SchuelerStammdatenPatchRequest();
		req.idStaatsangehoerigkeit = JsonNullable.of(null);

		final var result = service.patch(1L, req);

		assertThat(result).isEqualTo(apiModel);
	}

	@Test
	@DisplayName("patch - Ort und passender Ortsteil -> erfolgreich")
	void patch_ortUndPassenderOrtsteil() {
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(ortRepository.existsById(100L)).thenReturn(true);

		final var ortsteil = new de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil(20L, "Richtiger Ortsteil");
		ortsteil.idOrt = 100L;
		when(ortsteilRepository.findById(20L)).thenReturn(Optional.of(ortsteil));
		stubToApi(apiModel);

		final var req = new SchuelerStammdatenPatchRequest();
		req.wohnortID = JsonNullable.of(100L);
		req.ortsteilID = JsonNullable.of(20L);

		final var result = service.patch(1L, req);

		assertThat(result).isEqualTo(apiModel);
	}

	// =========================================================================
	// patchMultiple
	// =========================================================================

	@Test
	@DisplayName("patchMultiple - ID nicht gefunden -> NOT_FOUND")
	void patchMultiple_notFound() {
		final var dto = new SchuelerStammdatenBatchPatchRequest();
		dto.id = 99L;

		when(repository.findMapByIds(List.of(99L))).thenReturn(Map.of());

		assertThatException()
				.isThrownBy(() -> service.patchMultiple(List.of(dto)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine SchuelerStammdaten mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patchMultiple - erfolgreich")
	void patchMultiple_erfolgreich() {
		final var entity2 = new DTOSchueler(2L, "{guid-2}", false);
		final var apiModel2 = new SchuelerStammdaten();
		apiModel2.id = 2L;

		final var dto1 = new SchuelerStammdatenBatchPatchRequest();
		dto1.id = 1L;
		final var dto2 = new SchuelerStammdatenBatchPatchRequest();
		dto2.id = 2L;

		when(repository.findMapByIds(List.of(1L, 2L))).thenReturn(Map.of(1L, entity, 2L, entity2));
		when(mapper.toApi(entity)).thenReturn(apiModel);
		when(mapper.toApi(entity2)).thenReturn(apiModel2);

		final var result = service.patchMultiple(List.of(dto1, dto2));

		assertThat(result).containsExactly(apiModel, apiModel2);
	}

	@Test
	@DisplayName("patchMultiple - ein DTO invalid (Geschlecht) -> BAD_REQUEST, kein Patch")
	void patchMultiple_einDtoInvalid() {
		final var dto1 = new SchuelerStammdatenBatchPatchRequest();
		dto1.id = 1L;
		dto1.geschlecht = JsonNullable.of(GESCHLECHT_INVALID);

		when(repository.findMapByIds(List.of(1L))).thenReturn(Map.of(1L, entity));

		assertThatException()
				.isThrownBy(() -> service.patchMultiple(List.of(dto1)))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

		verify(mapper, never()).patch(any(), any());
	}

	// =========================================================================
	// delete
	// =========================================================================

	@Test
	@DisplayName("delete - alle gefunden und gelöscht")
	void delete_erfolgreich() {
		when(repository.findListByIds(List.of(1L))).thenReturn(List.of(entity));
		when(repository.delete(List.of(entity))).thenReturn(List.of(entity));

		final var result = service.delete(List.of(1L));

		assertThat(result).hasSize(1);
		assertThat(result.getFirst().success).isTrue();
		assertThat(result.getFirst().id).isEqualTo(1L);
	}

	@Test
	@DisplayName("delete - ID nicht gefunden -> success=false")
	void delete_nichtGefunden() {
		when(repository.findListByIds(List.of(99L))).thenReturn(List.of());
		when(repository.delete(List.of())).thenReturn(List.of());

		final var result = service.delete(List.of(99L));

		assertThat(result).hasSize(1);
		assertThat(result.getFirst().success).isFalse();
		assertThat(result.getFirst().id).isEqualTo(99L);
	}

	@Test
	@DisplayName("delete - teilweise gefunden")
	void delete_teilweiseGefunden() {
		when(repository.findListByIds(List.of(1L, 99L))).thenReturn(List.of(entity));
		when(repository.delete(List.of(entity))).thenReturn(List.of(entity));

		final var result = service.delete(List.of(1L, 99L));

		assertThat(result).hasSize(2);
		assertThat(result.stream().filter(r -> r.id == 1L).findFirst().orElseThrow().success).isTrue();
		assertThat(result.stream().filter(r -> r.id == 99L).findFirst().orElseThrow().success).isFalse();
	}

	@Test
	@DisplayName("delete - Ergebnis ist aufsteigend nach ID sortiert")
	void delete_sortiertNachId() {
		final var entity2 = new DTOSchueler(2L, "{guid-2}", false);
		when(repository.findListByIds(List.of(2L, 1L))).thenReturn(List.of(entity, entity2));
		when(repository.delete(anyList())).thenReturn(List.of(entity, entity2));

		final var result = service.delete(List.of(2L, 1L));

		assertThat(result).extracting(r -> r.id).isSorted();
	}

	// =========================================================================
	// Hilfsmethoden
	// =========================================================================

	private static SchuelerImportData buildImportData(final int idGeschlecht, final int idSchuelerStatus, final Long idReligion) {
		return new SchuelerImportData(
				"Mustermann",
				"Max",
				null,
				idGeschlecht,
				"2000-01-01",
				idSchuelerStatus,
				"2024-08-01",
				"2024-08-15",
				null,
				null,
				idReligion,
				1L
		);
	}
}
