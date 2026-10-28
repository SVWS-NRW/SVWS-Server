package de.svws_nrw.service.schule.katalog.ankreuzkompetenz;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.AnkreuzkompetenzJahrgangszuordnung;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzkompetenz;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzMapper;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzRepository;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepository;
import de.svws_nrw.service.schule.EigeneSchuleService;
import de.svws_nrw.service.utils.BulkDeleteUtils;
import jakarta.ws.rs.core.Response;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnkreuzkompetenzService")
class AnkreuzkompetenzServiceTest {

	private static final long ID_FACH = 10L;

	@Mock
	private AnkreuzkompetenzRepository repository;

	@Mock
	private FachRepository fachRepository;

	@Mock
	private AnkreuzkompetenzJahrgangService ankreuzkompetenzJahrgangService;

	@Mock
	private EigeneSchuleService eigeneSchuleService;

	private AnkreuzkompetenzService service;

	private MockedStatic<TransactionSupport> transactionSupport;

	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		transactionSupport = mockStatic(TransactionSupport.class);
		transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		service = new AnkreuzkompetenzService(repository, fachRepository, ankreuzkompetenzJahrgangService, eigeneSchuleService,
				AnkreuzkompetenzMapper.INSTANCE);
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	private static DTOAnkreuzkompetenz createEntity(final long id) {
		final var entity = new DTOAnkreuzkompetenz(id, 0, "Text " + id);
		entity.idFach = ID_FACH;
		entity.istAktiv = true;
		entity.istSichtbar = true;
		entity.sortierung = 1;
		return entity;
	}

	private static AnkreuzkompetenzCreateRequest createRequest() {
		final var dto = new AnkreuzkompetenzCreateRequest();
		dto.istASV = false;
		dto.idFach = ID_FACH;
		dto.floskelText = "Neuer Text";
		return dto;
	}

	private void fachExistiert() {
		when(fachRepository.findById(ID_FACH)).thenReturn(Optional.of(new DTOFach(ID_FACH, false)));
	}

	private void schuleIstBerufskolleg() {
		when(eigeneSchuleService.getSchuljahr()).thenReturn(2015);
		when(eigeneSchuleService.getSchulform()).thenReturn(Schulform.BK);
	}

	private void assertApiFehler(final Runnable aufruf, final Response.Status status, final String meldung) {
		assertThatException()
				.isThrownBy(aufruf::run)
				.isInstanceOf(ApiOperationException.class)
				.withMessage(meldung)
				.hasFieldOrPropertyWithValue("status", status);
	}

	// -------------------------------------------------------------------------
	// getAll
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getAll")
	class GetAll {

		@Test
		@DisplayName("Gibt eine leere Liste zurück, wenn keine Ankreuzkompetenzen vorhanden sind")
		void getAll_leer() {
			when(repository.getAll()).thenReturn(List.of());
			when(repository.getReferencedIds(List.of())).thenReturn(Set.of());
			when(ankreuzkompetenzJahrgangService.getAllByIdAnkreuzkompetenz()).thenReturn(Map.of());

			assertThat(service.getAll()).isEmpty();
		}

		@Test
		@DisplayName("Sortiert aufsteigend nach ID, setzt Referenz-Kennzeichen und Jahrgangszuordnungen")
		void getAll_sortiertUndErgaenzt() {
			final var zuordnung = new AnkreuzkompetenzJahrgangszuordnung();
			zuordnung.id = 100L;
			zuordnung.idAnkreuzkompetenz = 2L;
			zuordnung.idJahrgang = 5L;
			when(repository.getAll()).thenReturn(List.of(createEntity(3L), createEntity(1L), createEntity(2L)));
			when(repository.getReferencedIds(List.of(1L, 2L, 3L))).thenReturn(Set.of(3L));
			when(ankreuzkompetenzJahrgangService.getAllByIdAnkreuzkompetenz()).thenReturn(Map.of(2L, List.of(zuordnung)));

			final var result = service.getAll();

			assertThat(result).extracting(a -> a.id).containsExactly(1L, 2L, 3L);
			assertThat(result).extracting(a -> a.referenziertInAnderenTabellen).containsExactly(false, false, true);
			assertThat(result.get(0).jahrgaengezuordnung).isEmpty();
			assertThat(result.get(1).jahrgaengezuordnung).containsExactly(zuordnung);
			assertThat(result.get(2).jahrgaengezuordnung).isEmpty();
		}

		@Test
		@DisplayName("Wendet das Listen-Mapping an: Defaults und ausgeblendetes Fach bei ASV")
		void getAll_mapptDefaultsUndAsv() {
			final var entity = new DTOAnkreuzkompetenz(1L, 1, "Text");
			entity.idFach = ID_FACH;
			when(repository.getAll()).thenReturn(List.of(entity));
			when(repository.getReferencedIds(List.of(1L))).thenReturn(Set.of());
			when(ankreuzkompetenzJahrgangService.getAllByIdAnkreuzkompetenz()).thenReturn(Map.of());

			final var result = service.getAll().get(0);

			assertThat(result.istASV).isTrue();
			assertThat(result.idFach).isNull();
			assertThat(result.istAktiv).isFalse();
			assertThat(result.istSichtbar).isFalse();
			assertThat(result.fachSortierung).isZero();
			assertThat(result.sortierung).isEqualTo(32000);
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("create")
	class Create {

		@Test
		@DisplayName("Legt die Ankreuzkompetenz an und gibt sie ohne Referenz und Jahrgangszuordnungen zurück")
		void create_erfolg() {
			fachExistiert();
			when(repository.floskelTextIsAlreadyUsedCreate("Neuer Text", ID_FACH)).thenReturn(false);
			when(repository.create(any(DTOAnkreuzkompetenz.class))).thenAnswer(invocation -> {
				final DTOAnkreuzkompetenz entity = invocation.getArgument(0);
				entity.id = 42L;
				return entity;
			});

			final var result = service.create(createRequest());

			assertThat(result.id).isEqualTo(42L);
			assertThat(result.floskelText).isEqualTo("Neuer Text");
			assertThat(result.idFach).isEqualTo(ID_FACH);
			assertThat(result.istASV).isFalse();
			assertThat(result.referenziertInAnderenTabellen).isFalse();
			assertThat(result.jahrgaengezuordnung).isEmpty();
			final var captor = ArgumentCaptor.forClass(DTOAnkreuzkompetenz.class);
			verify(repository).create(captor.capture());
			assertThat(captor.getValue().istAktiv).isNull();
			assertThat(captor.getValue().sortierung).isNull();
		}

		@Test
		@DisplayName("Legt eine ASV-Ankreuzkompetenz ohne Fach an und prüft die Eindeutigkeit in der Gruppe ohne Fach")
		void create_asvOhneFach() {
			final var dto = createRequest();
			dto.istASV = true;
			dto.idFach = null;
			when(repository.floskelTextIsAlreadyUsedCreate("Neuer Text", null)).thenReturn(false);
			when(repository.create(any(DTOAnkreuzkompetenz.class))).thenAnswer(invocation -> invocation.getArgument(0));

			final var result = service.create(dto);

			assertThat(result.istASV).isTrue();
			assertThat(result.idFach).isNull();
			verify(fachRepository, never()).findById(anyLong());
		}

		@Test
		@DisplayName("Wirft 400, wenn einer ASV-Ankreuzkompetenz ein Fach zugeordnet ist")
		void create_asvMitFach() {
			final var dto = createRequest();
			dto.istASV = true;

			assertApiFehler(() -> service.create(dto), Response.Status.BAD_REQUEST, "Einer ASV-Ankreuzkompetenz kann kein Fach zugeordnet werden.");
			verify(repository, never()).create(any(DTOAnkreuzkompetenz.class));
		}

		@Test
		@DisplayName("Wirft 400, wenn ohne ASV kein Fach angegeben ist")
		void create_ohneAsvOhneFach() {
			final var dto = createRequest();
			dto.idFach = null;

			assertApiFehler(() -> service.create(dto), Response.Status.BAD_REQUEST, "Für diese Ankreuzkompetenz muss ein Fach ausgewählt werden.");
			verify(repository, never()).create(any(DTOAnkreuzkompetenz.class));
		}

		@Test
		@DisplayName("Wirft 404, wenn das Fach nicht existiert")
		void create_fachUnbekannt() {
			when(fachRepository.findById(ID_FACH)).thenReturn(Optional.empty());

			assertApiFehler(() -> service.create(createRequest()), Response.Status.NOT_FOUND, "Kein Fach mit der ID 10 gefunden.");
			verify(repository, never()).create(any(DTOAnkreuzkompetenz.class));
		}

		@Test
		@DisplayName("Wirft 404, wenn die Schulgliederung unbekannt ist")
		void create_schulgliederungUnbekannt() {
			fachExistiert();
			final var dto = createRequest();
			dto.schulgliederung = "A50";

			assertApiFehler(() -> service.create(dto), Response.Status.NOT_FOUND, "Keine Schulgliederung mit dem Schlüssel A50 gefunden.");
		}

		@Test
		@DisplayName("Wirft 409, wenn die Schulgliederung für die Schulform nicht gültig ist")
		void create_schulgliederungUngueltig() {
			fachExistiert();
			schuleIstBerufskolleg();
			final var dto = createRequest();
			dto.schulgliederung = "A13";

			assertApiFehler(() -> service.create(dto), Response.Status.CONFLICT, "Die Schulgliederung ist für diese Schulform nicht gültig.");
		}

		@Test
		@DisplayName("Akzeptiert eine für die Schulform gültige Schulgliederung")
		void create_schulgliederungGueltig() {
			fachExistiert();
			schuleIstBerufskolleg();
			final var dto = createRequest();
			dto.schulgliederung = "A06";
			when(repository.floskelTextIsAlreadyUsedCreate("Neuer Text", ID_FACH)).thenReturn(false);
			when(repository.create(any(DTOAnkreuzkompetenz.class))).thenAnswer(invocation -> invocation.getArgument(0));

			final var result = service.create(dto);

			assertThat(result.schulgliederung).isEqualTo("A06");
		}

		@Test
		@DisplayName("Wirft 400, wenn der Floskeltext für das Fach bereits verwendet wird")
		void create_floskelTextVergebenMitFach() {
			fachExistiert();
			when(repository.floskelTextIsAlreadyUsedCreate("Neuer Text", ID_FACH)).thenReturn(true);

			assertApiFehler(() -> service.create(createRequest()), Response.Status.BAD_REQUEST,
					"Der Floskeltext Neuer Text wird für die Fach-ID 10 bereits verwendet.");
			verify(repository, never()).create(any(DTOAnkreuzkompetenz.class));
		}

		@Test
		@DisplayName("Wirft 400, wenn der Floskeltext ohne Fach bereits verwendet wird")
		void create_floskelTextVergebenOhneFach() {
			final var dto = createRequest();
			dto.istASV = true;
			dto.idFach = null;
			when(repository.floskelTextIsAlreadyUsedCreate("Neuer Text", null)).thenReturn(true);

			assertApiFehler(() -> service.create(dto), Response.Status.BAD_REQUEST,
					"Der Floskeltext Neuer Text wird bereits für Ankreuzkompetenzen ohne Fach verwendet.");
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		private DTOAnkreuzkompetenz entity;

		@BeforeEach
		void setUp() {
			entity = createEntity(1L);
			lenient().when(repository.findById(1L)).thenReturn(Optional.of(entity));
		}

		@Test
		@DisplayName("Wirft 404, wenn die Ankreuzkompetenz nicht existiert")
		void patch_nichtGefunden() {
			when(repository.findById(99L)).thenReturn(Optional.empty());

			assertApiFehler(() -> service.patch(99L, new AnkreuzkompetenzPatchRequest()), Response.Status.NOT_FOUND,
					"Keine Ankreuzkompetenz mit der ID 99 gefunden.");
		}

		@Test
		@DisplayName("Ändert den Floskeltext, prüft die Eindeutigkeit mit Ausschluss der eigenen ID und liefert Referenz und Jahrgangszuordnungen")
		void patch_floskelText() {
			final var zuordnung = new AnkreuzkompetenzJahrgangszuordnung();
			zuordnung.idAnkreuzkompetenz = 1L;
			when(repository.floskelTextIsAlreadyUsedPatch("Neu", ID_FACH, 1L)).thenReturn(false);
			when(repository.getReferencedIds(List.of(1L))).thenReturn(Set.of(1L));
			when(ankreuzkompetenzJahrgangService.getByIdAnkreuzkompetenz(1L)).thenReturn(List.of(zuordnung));
			final var dto = new AnkreuzkompetenzPatchRequest();
			dto.floskelText = JsonNullable.of("Neu");

			final var result = service.patch(1L, dto);

			assertThat(entity.floskelText).isEqualTo("Neu");
			assertThat(result.floskelText).isEqualTo("Neu");
			assertThat(result.referenziertInAnderenTabellen).isTrue();
			assertThat(result.jahrgaengezuordnung).containsExactly(zuordnung);
		}

		@Test
		@DisplayName("Wirft 400, wenn der neue Floskeltext für das Fach bereits verwendet wird")
		void patch_floskelTextVergeben() {
			when(repository.floskelTextIsAlreadyUsedPatch("Neu", ID_FACH, 1L)).thenReturn(true);
			final var dto = new AnkreuzkompetenzPatchRequest();
			dto.floskelText = JsonNullable.of("Neu");

			assertApiFehler(() -> service.patch(1L, dto), Response.Status.BAD_REQUEST, "Der Floskeltext Neu wird für die Fach-ID 10 bereits verwendet.");
			assertThat(entity.floskelText).isEqualTo("Text 1");
		}

		@Test
		@DisplayName("Prüft bei geändertem Fach die Eindeutigkeit des bestehenden Floskeltexts für das neue Fach")
		void patch_nurFachPruftEindeutigkeit() {
			when(fachRepository.findById(20L)).thenReturn(Optional.of(new DTOFach(20L, false)));
			when(repository.floskelTextIsAlreadyUsedPatch("Text 1", 20L, 1L)).thenReturn(true);
			final var dto = new AnkreuzkompetenzPatchRequest();
			dto.idFach = JsonNullable.of(20L);

			assertApiFehler(() -> service.patch(1L, dto), Response.Status.BAD_REQUEST, "Der Floskeltext Text 1 wird für die Fach-ID 20 bereits verwendet.");
			assertThat(entity.idFach).isEqualTo(ID_FACH);
		}

		@Test
		@DisplayName("Prüft die Eindeutigkeit nicht, wenn weder Floskeltext noch Fach geändert werden")
		void patch_ohneEindeutigkeitspruefung() {
			final var dto = new AnkreuzkompetenzPatchRequest();
			dto.abschnitt = JsonNullable.of(2);

			service.patch(1L, dto);

			assertThat(entity.abschnitt).isEqualTo(2);
			verify(repository, never()).floskelTextIsAlreadyUsedPatch(anyString(), any(), anyLong());
			verify(fachRepository, never()).findById(anyLong());
		}

		@Test
		@DisplayName("Wirft 404, wenn das neue Fach nicht existiert")
		void patch_fachUnbekannt() {
			when(fachRepository.findById(999L)).thenReturn(Optional.empty());
			final var dto = new AnkreuzkompetenzPatchRequest();
			dto.idFach = JsonNullable.of(999L);

			assertApiFehler(() -> service.patch(1L, dto), Response.Status.NOT_FOUND, "Kein Fach mit der ID 999 gefunden.");
		}

		@Test
		@DisplayName("Wirft 400, wenn auf ASV umgestellt wird, obwohl noch ein Fach zugeordnet ist")
		void patch_asvMitBestehendemFach() {
			final var dto = new AnkreuzkompetenzPatchRequest();
			dto.istASV = JsonNullable.of(true);

			assertApiFehler(() -> service.patch(1L, dto), Response.Status.BAD_REQUEST, "Einer ASV-Ankreuzkompetenz kann kein Fach zugeordnet werden.");
		}

		@Test
		@DisplayName("Stellt auf ASV um, wenn das Fach im selben Patch entfernt wird")
		void patch_asvUndFachEntfernt() {
			when(repository.floskelTextIsAlreadyUsedPatch("Text 1", null, 1L)).thenReturn(false);
			final var dto = new AnkreuzkompetenzPatchRequest();
			dto.istASV = JsonNullable.of(true);
			dto.idFach = JsonNullable.of(null);

			final var result = service.patch(1L, dto);

			assertThat(entity.istASV).isEqualTo(1);
			assertThat(entity.idFach).isNull();
			assertThat(result.istASV).isTrue();
		}

		@Test
		@DisplayName("Wirft 400, wenn ASV deaktiviert wird, ohne dass ein Fach vorhanden ist")
		void patch_ohneAsvOhneFach() {
			entity.istASV = 1;
			entity.idFach = null;
			final var dto = new AnkreuzkompetenzPatchRequest();
			dto.istASV = JsonNullable.of(false);

			assertApiFehler(() -> service.patch(1L, dto), Response.Status.BAD_REQUEST, "Für diese Ankreuzkompetenz muss ein Fach ausgewählt werden.");
		}

		@Test
		@DisplayName("Wirft 400, wenn einer bestehenden ASV-Ankreuzkompetenz ein Fach zugeordnet wird")
		void patch_fachBeiAsv() {
			entity.istASV = 1;
			entity.idFach = null;
			final var dto = new AnkreuzkompetenzPatchRequest();
			dto.idFach = JsonNullable.of(ID_FACH);

			assertApiFehler(() -> service.patch(1L, dto), Response.Status.BAD_REQUEST, "Einer ASV-Ankreuzkompetenz kann kein Fach zugeordnet werden.");
		}

		@Test
		@DisplayName("Patcht Einträge ohne ASV und ohne Fach (Fach gelöscht), solange ASV und Fach nicht Teil des Requests sind")
		void patch_eintragOhneFachBleibtPatchbar() {
			entity.idFach = null;
			final var dto = new AnkreuzkompetenzPatchRequest();
			dto.istSichtbar = JsonNullable.of(false);

			service.patch(1L, dto);

			assertThat(entity.istSichtbar).isFalse();
			assertThat(entity.idFach).isNull();
		}

		@Test
		@DisplayName("Übernimmt ein explizites null bei nullable Feldern")
		void patch_explizitesNull() {
			entity.schulgliederung = "A06";
			entity.abschnitt = 1;
			final var dto = new AnkreuzkompetenzPatchRequest();
			dto.schulgliederung = JsonNullable.of(null);
			dto.abschnitt = JsonNullable.of(null);

			service.patch(1L, dto);

			assertThat(entity.schulgliederung).isNull();
			assertThat(entity.abschnitt).isNull();
		}

		@Test
		@DisplayName("Wirft 404, wenn die Schulgliederung unbekannt ist")
		void patch_schulgliederungUnbekannt() {
			final var dto = new AnkreuzkompetenzPatchRequest();
			dto.schulgliederung = JsonNullable.of("A50");

			assertApiFehler(() -> service.patch(1L, dto), Response.Status.NOT_FOUND, "Keine Schulgliederung mit dem Schlüssel A50 gefunden.");
		}

		@Test
		@DisplayName("Wirft 409, wenn die Schulgliederung für die Schulform nicht gültig ist")
		void patch_schulgliederungUngueltig() {
			schuleIstBerufskolleg();
			final var dto = new AnkreuzkompetenzPatchRequest();
			dto.schulgliederung = JsonNullable.of("A13");

			assertApiFehler(() -> service.patch(1L, dto), Response.Status.CONFLICT, "Die Schulgliederung ist für diese Schulform nicht gültig.");
		}

		@Test
		@DisplayName("Übernimmt eine für die Schulform gültige Schulgliederung")
		void patch_schulgliederungGueltig() {
			schuleIstBerufskolleg();
			final var dto = new AnkreuzkompetenzPatchRequest();
			dto.schulgliederung = JsonNullable.of("A06");

			service.patch(1L, dto);

			assertThat(entity.schulgliederung).isEqualTo("A06");
		}
	}

	// -------------------------------------------------------------------------
	// delete
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("delete")
	class Delete {

		@Test
		@DisplayName("Delegiert an BulkDeleteUtils.deleteWithReferenceCheck")
		void delete_delegiert() {
			final var ids = List.of(2L, 1L);
			final var response = mock(SimpleOperationResponse.class);

			try (MockedStatic<BulkDeleteUtils> bulkDeleteUtils = mockStatic(BulkDeleteUtils.class)) {
				bulkDeleteUtils.when(() -> BulkDeleteUtils.deleteWithReferenceCheck(eq(ids), eq(repository), any(), eq("Ankreuzkompetenz")))
						.thenReturn(List.of(response));

				final var result = service.delete(ids);

				assertThat(result).containsExactly(response);
			}
		}

		@Test
		@DisplayName("Löscht referenzierte Einträge nicht, auch nicht in einer gemischten Liste, und meldet unbekannte IDs")
		void delete_gemischteListe() {
			final var referenziert = createEntity(1L);
			final var frei = createEntity(2L);
			final var ids = List.of(1L, 2L, 3L);
			when(repository.getReferencedIds(ids)).thenReturn(Set.of(1L));
			when(repository.findListByIds(ids)).thenReturn(List.of(referenziert, frei));
			when(repository.delete(ArgumentMatchers.<List<DTOAnkreuzkompetenz>>any())).thenAnswer(invocation -> invocation.getArgument(0));

			final var result = service.delete(ids);

			@SuppressWarnings("unchecked")
			final ArgumentCaptor<List<DTOAnkreuzkompetenz>> captor = ArgumentCaptor.forClass(List.class);
			verify(repository).delete(captor.capture());
			assertThat(captor.getValue()).containsExactly(frei);
			assertThat(result).extracting(r -> r.id).containsExactly(1L, 2L, 3L);
			assertThat(result).extracting(r -> r.success).containsExactly(false, true, false);
			assertThat(result.get(0).log).anyMatch(m -> m.contains("referenziert"));
			assertThat(result.get(2).log).anyMatch(m -> m.contains("nicht gefunden"));
		}
	}
}
