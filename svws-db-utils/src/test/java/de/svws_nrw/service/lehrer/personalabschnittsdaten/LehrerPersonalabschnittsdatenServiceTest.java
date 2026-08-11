package de.svws_nrw.service.lehrer.personalabschnittsdaten;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.LehrerPersonalabschnittsdatenMapper;
import de.svws_nrw.mapper.lehrer.LehrerPersonalabschnittsdatenMappingContext;
import de.svws_nrw.repo.RepositoryException;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.repo.schule.kataloge.schule.SchuleRepository;
import de.svws_nrw.service.lehrer.LehrerAnrechnungsstundenService;
import de.svws_nrw.service.lehrer.LehrerMehrleistungService;
import de.svws_nrw.service.lehrer.LehrerMinderleistungService;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionService;
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
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LehrerPersonalabschnittsdatenServiceTest {

	@Mock
	private LehrerPersonalabschnittsdatenRepository repo;

	@Mock
	private LehrerRepository lehrerRepo;

	@Mock
	private SchuleRepository schulenRepo;

	@Mock
	private SchuljahresabschnitteRepository schuljahresabschnitteRepo;

	@Mock
	private LehrerAnrechnungsstundenService anrechnungsService;

	@Mock
	private LehrerMehrleistungService mehrleistungService;

	@Mock
	private LehrerMinderleistungService minderleistungService;

	@Mock
	private LehrerFunktionService funktionService;

	@Mock
	private LehrerPersonalabschnittsdatenRepos repos;

	@Mock
	private LehrerPersonalabschnittsdatenSubServices subServices;

	@Mock
	private LehrerPersonalabschnittsdatenMapper mapper;

	private LehrerPersonalabschnittsdatenService service;

	private MockedStatic<TransactionSupport> transactionSupport;

	private DTOLehrerAbschnittsdaten entity;
	private LehrerPersonalabschnittsdaten apiModel;

	@BeforeEach
	void setUp() {
		entity = new DTOLehrerAbschnittsdaten(1L, 10L, 20L);
		apiModel = new LehrerPersonalabschnittsdaten();
		apiModel.id = 1L;
		apiModel.idLehrer = 10L;
		apiModel.idSchuljahresabschnitt = 20L;

		transactionSupport = mockStatic(TransactionSupport.class);
		transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		lenient().when(repos.lehrerPersonalabschnittsdatenRepo()).thenReturn(repo);
		lenient().when(repos.lehrerRepo()).thenReturn(lehrerRepo);
		lenient().when(repos.schulenRepo()).thenReturn(schulenRepo);
		lenient().when(repos.schuljahresabschnitteRepo()).thenReturn(schuljahresabschnitteRepo);

		lenient().when(subServices.anrechnungsService()).thenReturn(anrechnungsService);
		lenient().when(subServices.mehrleistungService()).thenReturn(mehrleistungService);
		lenient().when(subServices.minderleistungService()).thenReturn(minderleistungService);
		lenient().when(subServices.funktionService()).thenReturn(funktionService);

		service = new LehrerPersonalabschnittsdatenService(repos, subServices, mapper);

		// Default-Stubs für ContextLoader — alle Services liefern leere Maps
		lenient().when(anrechnungsService.getListByIdLehrerAbschnittsdaten(anyList())).thenReturn(Map.of());
		lenient().when(mehrleistungService.getListByIdLehrerAbschnittsdaten(anyList())).thenReturn(Map.of());
		lenient().when(minderleistungService.getListByIdLehrerAbschnittsdaten(anyList())).thenReturn(Map.of());
		lenient().when(funktionService.getListByIdLehrerAbschnittsdaten(anyList())).thenReturn(Map.of());
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	private void stubToApi(final LehrerPersonalabschnittsdaten result) {
		final var abschnitt = new DTOSchuljahresabschnitte(20L, 2024, 1);
		when(schuljahresabschnitteRepo.findListByIds(anyList())).thenReturn(List.of(abschnitt));
		lenient().when(mapper.toApi(any(DTOLehrerAbschnittsdaten.class), anyInt(), any(LehrerPersonalabschnittsdatenMappingContext.class)))
				.thenReturn(result);
	}

	// -------------------------------------------------------------------------
	// get
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("get - not found")
	void get_notFound() {
		when(repo.findById(99L)).thenReturn(Optional.empty());

		assertThatException()
				.isThrownBy(() -> service.get(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine Personalabschnittsdaten mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("get")
	void get() {
		when(repo.findById(1L)).thenReturn(Optional.of(entity));
		stubToApi(apiModel);

		final var result = service.get(1L);

		assertThat(result).isEqualTo(apiModel);
	}

	// -------------------------------------------------------------------------
	// getList
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getList - null")
	void getList_null() {
		assertThat(service.getList(null)).isEmpty();
		verify(repo, never()).findListByIds(anyList());
	}

	@Test
	@DisplayName("getList - leer")
	void getList_empty() {
		assertThat(service.getList(List.of())).isEmpty();
		verify(repo, never()).findListByIds(anyList());
	}

	@Test
	@DisplayName("getList")
	void getList() {
		final var entity2 = new DTOLehrerAbschnittsdaten(2L, 10L, 20L);
		final var apiModel2 = new LehrerPersonalabschnittsdaten();
		apiModel2.id = 2L;

		when(repo.findListByIds(List.of(1L, 2L))).thenReturn(List.of(entity, entity2));
		stubToApi(apiModel);
		when(mapper.toApi(any(DTOLehrerAbschnittsdaten.class), anyInt(),
				any(LehrerPersonalabschnittsdatenMappingContext.class)))
				.thenReturn(apiModel, apiModel2);

		final var result = service.getList(List.of(1L, 2L));

		assertThat(result).containsExactly(apiModel, apiModel2);
	}

	// -------------------------------------------------------------------------
	// getByIdLehrer
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getByIdLehrer - leer")
	void getByIdLehrer_empty() {
		when(repo.findByIdLehrer(10L)).thenReturn(List.of());

		final var result = service.getByIdLehrer(10L);

		assertThat(result).isEmpty();
		verify(mapper, never()).toApi(any(), anyInt(), any());
	}

	@Test
	@DisplayName("getByIdLehrer")
	void getByIdLehrer() {
		final var entity2 = new DTOLehrerAbschnittsdaten(2L, 10L, 20L);
		final var apiModel2 = new LehrerPersonalabschnittsdaten();
		apiModel2.id = 2L;

		when(repo.findByIdLehrer(10L)).thenReturn(List.of(entity, entity2));
		when(schuljahresabschnitteRepo.findListByIds(anyList()))
				.thenReturn(List.of(new DTOSchuljahresabschnitte(20L, 2024, 1)));
		when(mapper.toApi(any(DTOLehrerAbschnittsdaten.class), anyInt(),
				any(LehrerPersonalabschnittsdatenMappingContext.class)))
				.thenReturn(apiModel, apiModel2);

		final var result = service.getByIdLehrer(10L);

		assertThat(result).containsExactly(apiModel, apiModel2);
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("create - Lehrer nicht gefunden")
	void create_lehrerNotFound() {
		final var dto = new LehrerPersonalabschnittsdatenCreateRequest();
		dto.idLehrer = 10L;
		dto.idSchuljahresabschnitt = 20L;

		when(lehrerRepo.existsById(10L)).thenReturn(false);

		assertThatException()
				.isThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Kein Lehrer für die ID 10 gefunden")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create - Schuljahresabschnitt nicht gefunden")
	void create_schuljahresabschnittNotFound() {
		final var dto = new LehrerPersonalabschnittsdatenCreateRequest();
		dto.idLehrer = 10L;
		dto.idSchuljahresabschnitt = 20L;

		when(lehrerRepo.existsById(10L)).thenReturn(true);
		when(schuljahresabschnitteRepo.existsById(20L)).thenReturn(false);

		assertThatException()
				.isThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create - Stammschulnummer nicht gefunden")
	void create_stammschulnummerNotFound() {
		final var dto = new LehrerPersonalabschnittsdatenCreateRequest();
		dto.idLehrer = 10L;
		dto.idSchuljahresabschnitt = 20L;
		dto.stammschulnummer = JsonNullable.of("999999");

		when(lehrerRepo.existsById(10L)).thenReturn(true);
		when(schuljahresabschnitteRepo.existsById(20L)).thenReturn(true);
		when(repo.getNextID()).thenReturn(1L);
		when(schulenRepo.existsBySchulnummer("999999")).thenReturn(false);

		assertThatException()
				.isThrownBy(() -> service.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine Schule für die Schulnummer 999999 gefunden")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create")
	void create() {
		final var dto = new LehrerPersonalabschnittsdatenCreateRequest();
		dto.idLehrer = 10L;
		dto.idSchuljahresabschnitt = 20L;

		when(lehrerRepo.existsById(10L)).thenReturn(true);
		when(schuljahresabschnitteRepo.existsById(20L)).thenReturn(true);
		when(repo.getNextID()).thenReturn(1L);
		when(repo.create(any(DTOLehrerAbschnittsdaten.class))).thenReturn(entity);
		stubToApi(apiModel);

		final var result = service.create(dto);

		assertThat(result).isEqualTo(apiModel);
	}

	// -------------------------------------------------------------------------
	// createMultiple
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("createMultiple")
	void createMultiple() {
		final var dto1 = new LehrerPersonalabschnittsdatenCreateRequest();
		dto1.idLehrer = 10L;
		dto1.idSchuljahresabschnitt = 20L;
		final var dto2 = new LehrerPersonalabschnittsdatenCreateRequest();
		dto2.idLehrer = 11L;
		dto2.idSchuljahresabschnitt = 20L;

		final var entity2 = new DTOLehrerAbschnittsdaten(2L, 11L, 20L);
		final var apiModel2 = new LehrerPersonalabschnittsdaten();
		apiModel2.id = 2L;

		when(lehrerRepo.existsById(10L)).thenReturn(true);
		when(lehrerRepo.existsById(11L)).thenReturn(true);
		when(schuljahresabschnitteRepo.existsById(20L)).thenReturn(true);
		when(repo.getNextID()).thenReturn(1L, 2L);
		when(repo.create(anyList())).thenReturn(List.of(entity, entity2));
		when(schuljahresabschnitteRepo.findListByIds(anyList()))
				.thenReturn(List.of(new DTOSchuljahresabschnitte(20L, 2024, 1)));
		when(mapper.toApi(any(DTOLehrerAbschnittsdaten.class), anyInt(),
				any(LehrerPersonalabschnittsdatenMappingContext.class)))
				.thenReturn(apiModel, apiModel2);

		final var result = service.createMultiple(List.of(dto1, dto2));

		assertThat(result).containsExactly(apiModel, apiModel2);
	}

	@Test
	@DisplayName("createMultiple - ein DTO invalid (Lehrer fehlt) -> BAD_REQUEST")
	void createMultiple_oneInvalid_lehrerNotFound() {
		final var dto1 = new LehrerPersonalabschnittsdatenCreateRequest();
		dto1.idLehrer = 10L;
		dto1.idSchuljahresabschnitt = 20L;

		final var dto2 = new LehrerPersonalabschnittsdatenCreateRequest();
		dto2.idLehrer = 11L;
		dto2.idSchuljahresabschnitt = 20L;

		when(lehrerRepo.existsById(10L)).thenReturn(true);

		assertThatException()
				.isThrownBy(() -> service.createMultiple(List.of(dto1, dto2)))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

		verify(repo, never()).create(anyList());
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch - not found")
	void patch_notFound() {
		when(repo.findById(99L)).thenReturn(Optional.empty());

		assertThatException()
				.isThrownBy(() -> service.patch(99L, new LehrerPersonalabschnittsdatenPatchRequest()))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine Personalabschnittsdaten mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch")
	void patch() {
		final var dto = new LehrerPersonalabschnittsdatenPatchRequest();
		dto.pflichtstundensoll = JsonNullable.of(18.5);

		when(repo.findById(1L)).thenReturn(Optional.of(entity));
		stubToApi(apiModel);

		final var result = service.patch(1L, dto);

		assertThat(entity.PflichtstdSoll).isEqualTo(18.5);
		assertThat(result).isEqualTo(apiModel);
	}

	@Test
	@DisplayName("patch - stammschulnummer null setzt Feld auf null")
	void patch_stammschulnummerNull() {
		entity.StammschulNr = "123456";
		final var dto = new LehrerPersonalabschnittsdatenPatchRequest();
		dto.stammschulnummer = JsonNullable.of(null);

		when(repo.findById(1L)).thenReturn(Optional.of(entity));
		stubToApi(apiModel);

		service.patch(1L, dto);

		assertThat(entity.StammschulNr).isNull();
	}

	@Test
	@DisplayName("patch - stammschulnummer invalid -> BAD_REQUEST")
	void patch_stammschulnummerInvalid() {
		final var dto = new LehrerPersonalabschnittsdatenPatchRequest();
		dto.stammschulnummer = JsonNullable.of("999999");

		when(repo.findById(1L)).thenReturn(Optional.of(entity));
		when(schulenRepo.existsBySchulnummer("999999")).thenReturn(false);

		assertThatException()
				.isThrownBy(() -> service.patch(1L, dto))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine Schule für die Schulnummer 999999 gefunden")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch - stammschulnummer valid wird gesetzt")
	void patch_stammschulnummerValid() {
		final var dto = new LehrerPersonalabschnittsdatenPatchRequest();
		dto.stammschulnummer = JsonNullable.of("123456");

		when(repo.findById(1L)).thenReturn(Optional.of(entity));
		when(schulenRepo.existsBySchulnummer("123456")).thenReturn(true);
		stubToApi(apiModel);

		service.patch(1L, dto);

		assertThat(entity.StammschulNr).isEqualTo("123456");
	}

	@Test
	@DisplayName("patch - idRechtsverhaeltnis null setzt Feld auf null")
	void patch_idRechtsverhaeltnisNull() {
		entity.Rechtsverhaeltnis = "IRGENDWAS";
		final var dto = new LehrerPersonalabschnittsdatenPatchRequest();
		dto.idRechtsverhaeltnis = JsonNullable.of(null);

		when(repo.findById(1L)).thenReturn(Optional.of(entity));
		stubToApi(apiModel);

		service.patch(1L, dto);

		assertThat(entity.Rechtsverhaeltnis).isNull();
	}

	@Test
	@DisplayName("patch - idRechtsverhaeltnis invalid -> BAD_REQUEST")
	void patch_idRechtsverhaeltnisInvalid() {
		final var dto = new LehrerPersonalabschnittsdatenPatchRequest();
		dto.idRechtsverhaeltnis = JsonNullable.of(999999L);

		when(repo.findById(1L)).thenReturn(Optional.of(entity));

		assertThatException()
				.isThrownBy(() -> service.patch(1L, dto))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch - idBeschaeftigungsart null setzt Feld auf null")
	void patch_idBeschaeftigungsartNull() {
		entity.Beschaeftigungsart = "IRGENDWAS";
		final var dto = new LehrerPersonalabschnittsdatenPatchRequest();
		dto.idBeschaeftigungsart = JsonNullable.of(null);

		when(repo.findById(1L)).thenReturn(Optional.of(entity));
		stubToApi(apiModel);

		service.patch(1L, dto);

		assertThat(entity.Beschaeftigungsart).isNull();
	}

	@Test
	@DisplayName("patch - idBeschaeftigungsart invalid -> BAD_REQUEST")
	void patch_idBeschaeftigungsartInvalid() {
		final var dto = new LehrerPersonalabschnittsdatenPatchRequest();
		dto.idBeschaeftigungsart = JsonNullable.of(999999L);

		when(repo.findById(1L)).thenReturn(Optional.of(entity));

		assertThatException()
				.isThrownBy(() -> service.patch(1L, dto))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch - idEinsatzstatus null setzt Feld auf null")
	void patch_idEinsatzstatusNull() {
		entity.Einsatzstatus = "IRGENDWAS";
		final var dto = new LehrerPersonalabschnittsdatenPatchRequest();
		dto.idEinsatzstatus = JsonNullable.of(null);

		when(repo.findById(1L)).thenReturn(Optional.of(entity));
		stubToApi(apiModel);

		service.patch(1L, dto);

		assertThat(entity.Einsatzstatus).isNull();
	}

	@Test
	@DisplayName("patch - idEinsatzstatus invalid -> BAD_REQUEST")
	void patch_idEinsatzstatusInvalid() {
		final var dto = new LehrerPersonalabschnittsdatenPatchRequest();
		dto.idEinsatzstatus = JsonNullable.of(999999L);

		when(repo.findById(1L)).thenReturn(Optional.of(entity));

		assertThatException()
				.isThrownBy(() -> service.patch(1L, dto))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	// -------------------------------------------------------------------------
	// patchMultiple
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patchMultiple - not found")
	void patchMultiple_notFound() {
		final var dto = new LehrerPersonalabschnittsdatenBatchPatchRequest();
		dto.id = 99L;

		when(repo.findMapByIds(List.of(99L))).thenReturn(Map.of());

		assertThatException()
				.isThrownBy(() -> service.patchMultiple(List.of(dto)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine Personalabschnittsdaten mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patchMultiple")
	void patchMultiple() {
		final var dto1 = new LehrerPersonalabschnittsdatenBatchPatchRequest();
		dto1.id = 1L;
		dto1.pflichtstundensoll = JsonNullable.of(18.5);

		final var entity2 = new DTOLehrerAbschnittsdaten(2L, 10L, 20L);
		final var dto2 = new LehrerPersonalabschnittsdatenBatchPatchRequest();
		dto2.id = 2L;
		dto2.pflichtstundensoll = JsonNullable.of(20.0);

		final var apiModel2 = new LehrerPersonalabschnittsdaten();
		apiModel2.id = 2L;

		when(repo.findMapByIds(List.of(1L, 2L))).thenReturn(Map.of(1L, entity, 2L, entity2));
		when(schuljahresabschnitteRepo.findListByIds(anyList()))
				.thenReturn(List.of(new DTOSchuljahresabschnitte(20L, 2024, 1)));
		when(mapper.toApi(any(DTOLehrerAbschnittsdaten.class), anyInt(),
				any(LehrerPersonalabschnittsdatenMappingContext.class)))
				.thenReturn(apiModel, apiModel2);

		final var result = service.patchMultiple(List.of(dto1, dto2));

		assertThat(entity.PflichtstdSoll).isEqualTo(18.5);
		assertThat(entity2.PflichtstdSoll).isEqualTo(20.0);
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
		assertThat(result.log).contains("Keine Personalabschnittsdaten mit der ID 99 gefunden.");
		verify(repo, never()).delete(any(DTOLehrerAbschnittsdaten.class));
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
	@DisplayName("deleteMultiple - alle nicht gefunden")
	void deleteMultiple_noneFound() {
		when(repo.findListByIds(List.of(99L))).thenReturn(Collections.emptyList());

		final var result = service.deleteMultiple(List.of(99L));

		assertThat(result).hasSize(1);
		assertThat(result.getFirst().success).isFalse();
		assertThat(result.getFirst().id).isEqualTo(99L);
		verify(repo).delete(Collections.emptyList());
	}

	@Test
	@DisplayName("deleteMultiple - ein Eintrag nicht gefunden")
	void deleteMultiple_partialNotFound() {
		when(repo.findListByIds(List.of(1L, 99L))).thenReturn(List.of(entity));

		final var result = service.deleteMultiple(List.of(1L, 99L));

		assertThat(result).hasSize(2);
		assertThat(result.stream().filter(r -> r.success).map(r -> r.id)).containsExactly(1L);
		assertThat(result.stream().filter(r -> !r.success).map(r -> r.id)).containsExactly(99L);
	}

	@Test
	@DisplayName("deleteMultiple")
	void deleteMultiple() {
		when(repo.findListByIds(List.of(1L))).thenReturn(List.of(entity));

		final var result = service.deleteMultiple(List.of(1L));

		assertThat(result).hasSize(1);
		assertThat(result.getFirst().success).isTrue();
		assertThat(result.getFirst().id).isEqualTo(1L);
		verify(repo).delete(List.of(entity));
	}
}
