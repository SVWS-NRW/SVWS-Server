package de.svws_nrw.service.schule.katalog.einwilligungsart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.asd.types.schule.Einwilligungsschluessel;
import de.svws_nrw.core.data.schule.Einwilligungsart;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.katalog.DTOEinwilligungsart;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.einwilligungsart.EinwilligungsartMapper;
import de.svws_nrw.repo.schule.kataloge.einwilligungsart.EinwilligungsartRepository;
import de.svws_nrw.service.lehrer.einwilligung.LehrerEinwilligungService;
import de.svws_nrw.service.schueler.einwilligung.SchuelerEinwilligungService;
import jakarta.ws.rs.core.Response;

/**
 * Tests für den {@link EinwilligungsartService}.
 */
@ExtendWith(MockitoExtension.class)
class EinwilligungsartServiceTest {

	private static final String SCHLUESSEL = Einwilligungsschluessel.data().getWerte().getFirst().historie().getFirst().schluessel;

	@Mock
	private EinwilligungsartRepository repository;

	@Mock
	private SchuelerEinwilligungService schuelerEinwilligungService;

	@Mock
	private LehrerEinwilligungService lehrerEinwilligungService;

	private final EinwilligungsartMapper mapper = EinwilligungsartMapper.INSTANCE;

	private EinwilligungsartService service;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeEach
	void setUp() {
		service = new EinwilligungsartService(repository, mapper, schuelerEinwilligungService, lehrerEinwilligungService);
		transactionSupportMock = Mockito.mockStatic(TransactionSupport.class);
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}

	private static DTOEinwilligungsart dto(final long id, final String bezeichnung, final PersonTyp personTyp) {
		final DTOEinwilligungsart dto = new DTOEinwilligungsart(id, bezeichnung, true, 32000);
		dto.personTyp = personTyp;
		return dto;
	}

	private static EinwilligungsartCreateRequest createRequest(final String bezeichnung, final Integer idPersonTyp) {
		final var request = new EinwilligungsartCreateRequest();
		request.bezeichnung = bezeichnung;
		request.idPersonTyp = idPersonTyp;
		return request;
	}

	private void stubCreate(final long newId) {
		when(repository.create(any(DTOEinwilligungsart.class))).thenAnswer(invocation -> {
			final DTOEinwilligungsart created = invocation.getArgument(0);
			created.id = newId;
			return created;
		});
	}

	private void assertBadRequest(final Throwable throwable, final String message) {
		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining(message)
				.extracting(e -> ((ApiOperationException) e).getStatus())
				.isEqualTo(Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getAll | Liefert die Einwilligungsarten aufsteigend nach ID sortiert mit gesetztem Referenz-Flag")
	void getAll_sortedWithReferencedFlag() {
		final var e1 = dto(1L, "A", PersonTyp.SCHUELER);
		final var e2 = dto(2L, "B", PersonTyp.LEHRER);
		final var e3 = dto(3L, "C", PersonTyp.SCHUELER);
		when(repository.getAll()).thenReturn(List.of(e3, e1, e2));
		when(repository.getReferencedIds(List.of(3L, 1L, 2L))).thenReturn(Set.of(2L));

		final List<Einwilligungsart> result = service.getAll();

		assertThat(result).extracting(e -> e.id).containsExactly(1L, 2L, 3L);
		assertThat(result).extracting(e -> e.referenziertInAnderenTabellen).containsExactly(false, true, false);
		assertThat(result).extracting(e -> e.idPersonTyp).containsExactly(PersonTyp.SCHUELER.id, PersonTyp.LEHRER.id, PersonTyp.SCHUELER.id);
	}

	@Test
	@DisplayName("getAll | Liefert eine leere Liste, wenn keine Einwilligungsarten existieren")
	void getAll_empty() {
		when(repository.getAll()).thenReturn(List.of());
		when(repository.getReferencedIds(List.of())).thenReturn(Set.of());

		assertThat(service.getAll()).isEmpty();
	}

	@Test
	@DisplayName("create | Legt eine Einwilligungsart für Schüler an und erzeugt die Einwilligungen nur für Schüler")
	void create_schueler() {
		final var request = createRequest("Verwendung Foto", PersonTyp.SCHUELER.id);
		request.schluessel = SCHLUESSEL;
		stubCreate(7L);
		when(repository.getReferencedIds(List.of(7L))).thenReturn(Set.of(7L));

		final Einwilligungsart result = service.create(request);

		assertThat(result.id).isEqualTo(7L);
		assertThat(result.bezeichnung).isEqualTo("Verwendung Foto");
		assertThat(result.schluessel).isEqualTo(SCHLUESSEL);
		assertThat(result.idPersonTyp).isEqualTo(PersonTyp.SCHUELER.id);
		assertThat(result.referenziertInAnderenTabellen).isTrue();
		final var inOrder = inOrder(repository, schuelerEinwilligungService);
		inOrder.verify(repository).create(any(DTOEinwilligungsart.class));
		inOrder.verify(repository).flush();
		inOrder.verify(schuelerEinwilligungService).addForEinwilligungsart(7L);
		verify(lehrerEinwilligungService, never()).addForEinwilligungsart(anyLong());
	}

	@Test
	@DisplayName("create | Legt eine Einwilligungsart für Lehrer an und erzeugt die Einwilligungen nur für Lehrer")
	void create_lehrer() {
		final var request = createRequest("Verwendung Foto", PersonTyp.LEHRER.id);
		stubCreate(8L);
		when(repository.getReferencedIds(List.of(8L))).thenReturn(Set.of());

		final Einwilligungsart result = service.create(request);

		assertThat(result.id).isEqualTo(8L);
		assertThat(result.idPersonTyp).isEqualTo(PersonTyp.LEHRER.id);
		assertThat(result.referenziertInAnderenTabellen).isFalse();
		final var inOrder = inOrder(repository, lehrerEinwilligungService);
		inOrder.verify(repository).create(any(DTOEinwilligungsart.class));
		inOrder.verify(repository).flush();
		inOrder.verify(lehrerEinwilligungService).addForEinwilligungsart(8L);
		verify(schuelerEinwilligungService, never()).addForEinwilligungsart(anyLong());
	}

	@Test
	@DisplayName("create | Prüft den Schlüssel nicht, wenn er leer ist, und speichert ihn nicht")
	void create_blankSchluessel() {
		final var request = createRequest("Verwendung Foto", PersonTyp.SCHUELER.id);
		request.schluessel = "";
		stubCreate(9L);
		when(repository.getReferencedIds(List.of(9L))).thenReturn(Set.of());

		final Einwilligungsart result = service.create(request);

		assertThat(result.schluessel).isNull();
		verify(repository, never()).schluesselIsAlreadyUsedCreate(anyString(), any());
	}

	@Test
	@DisplayName("create | Wirft einen Fehler, wenn kein Personentyp zur ID existiert")
	void create_unknownPersonTyp() {
		final var request = createRequest("Verwendung Foto", 99);

		assertThatThrownBy(() -> service.create(request)).satisfies(e -> assertBadRequest(e, "99"));

		verify(repository, never()).create(any(DTOEinwilligungsart.class));
	}

	@Test
	@DisplayName("create | Wirft einen Fehler, wenn kein Personentyp angegeben ist")
	void create_missingPersonTyp() {
		final var request = createRequest("Verwendung Foto", null);

		assertThatThrownBy(() -> service.create(request)).satisfies(e -> assertBadRequest(e, "PersonTyp"));

		verify(repository, never()).create(any(DTOEinwilligungsart.class));
	}

	@Test
	@DisplayName("create | Wirft einen Fehler für den nicht unterstützten Personentyp Erzieher")
	void create_erzieherNotSupported() {
		final var request = createRequest("Verwendung Foto", PersonTyp.ERZIEHER.id);

		assertThatThrownBy(() -> service.create(request)).satisfies(e -> assertBadRequest(e, "Erzieher"));

		verify(repository, never()).create(any(DTOEinwilligungsart.class));
	}

	@Test
	@DisplayName("create | Wirft einen Fehler und legt nichts an, wenn die Bezeichnung für den Personentyp bereits verwendet wird")
	void create_bezeichnungAlreadyUsed() {
		final var request = createRequest("Verwendung Foto", PersonTyp.LEHRER.id);
		when(repository.bezeichnungIsAlreadyUsedCreate("Verwendung Foto", PersonTyp.LEHRER)).thenReturn(true);

		assertThatThrownBy(() -> service.create(request)).satisfies(e -> assertBadRequest(e, "Verwendung Foto"));

		verify(repository, never()).create(any(DTOEinwilligungsart.class));
		verify(schuelerEinwilligungService, never()).addForEinwilligungsart(anyLong());
		verify(lehrerEinwilligungService, never()).addForEinwilligungsart(anyLong());
	}

	@Test
	@DisplayName("create | Wirft einen Fehler, wenn der Schlüssel für den Personentyp bereits verwendet wird")
	void create_schluesselAlreadyUsed() {
		final var request = createRequest("Verwendung Foto", PersonTyp.SCHUELER.id);
		request.schluessel = SCHLUESSEL;
		when(repository.schluesselIsAlreadyUsedCreate(SCHLUESSEL, PersonTyp.SCHUELER)).thenReturn(true);

		assertThatThrownBy(() -> service.create(request)).satisfies(e -> assertBadRequest(e, SCHLUESSEL));

		verify(repository, never()).create(any(DTOEinwilligungsart.class));
	}

	@Test
	@DisplayName("create | Wirft einen Fehler, wenn der Schlüssel keinem Coretyp entspricht")
	void create_schluesselUnknown() {
		final var request = createRequest("Verwendung Foto", PersonTyp.SCHUELER.id);
		request.schluessel = "-35";

		assertThatThrownBy(() -> service.create(request)).satisfies(e -> assertBadRequest(e, "-35"));

		verify(repository, never()).schluesselIsAlreadyUsedCreate(anyString(), any());
		verify(repository, never()).create(any(DTOEinwilligungsart.class));
	}

	@Test
	@DisplayName("patch | Aktualisiert Bezeichnung und Schlüssel und prüft sie für den Personentyp des Eintrags")
	void patch_success() {
		final var entity = dto(5L, "Alt", PersonTyp.LEHRER);
		final var request = new EinwilligungsartPatchRequest();
		request.bezeichnung = JsonNullable.of("Neu");
		request.schluessel = JsonNullable.of(SCHLUESSEL);
		when(repository.findById(5L)).thenReturn(Optional.of(entity));
		when(repository.bezeichnungIsAlreadyUsedPatch("Neu", PersonTyp.LEHRER, 5L)).thenReturn(false);
		when(repository.schluesselIsAlreadyUsedPatch(SCHLUESSEL, PersonTyp.LEHRER, 5L)).thenReturn(false);
		when(repository.getReferencedIds(List.of(5L))).thenReturn(Set.of());

		final Einwilligungsart result = service.patch(5L, request);

		assertThat(entity.bezeichnung).isEqualTo("Neu");
		assertThat(entity.schluessel).isEqualTo(SCHLUESSEL);
		assertThat(result.id).isEqualTo(5L);
		assertThat(result.bezeichnung).isEqualTo("Neu");
		assertThat(result.schluessel).isEqualTo(SCHLUESSEL);
		assertThat(result.referenziertInAnderenTabellen).isFalse();
	}

	@Test
	@DisplayName("patch | Entfernt den Schlüssel ohne Prüfung, wenn er leer ist")
	void patch_blankSchluessel() {
		final var entity = dto(5L, "Alt", PersonTyp.LEHRER);
		entity.schluessel = SCHLUESSEL;
		final var request = new EinwilligungsartPatchRequest();
		request.schluessel = JsonNullable.of("");
		when(repository.findById(5L)).thenReturn(Optional.of(entity));
		when(repository.getReferencedIds(List.of(5L))).thenReturn(Set.of());

		final Einwilligungsart result = service.patch(5L, request);

		assertThat(entity.schluessel).isNull();
		assertThat(result.schluessel).isNull();
		verify(repository, never()).schluesselIsAlreadyUsedPatch(anyString(), any(), anyLong());
	}

	@Test
	@DisplayName("patch | Entfernt den Schlüssel, wenn er explizit null ist")
	void patch_nullSchluessel() {
		final var entity = dto(5L, "Alt", PersonTyp.LEHRER);
		entity.schluessel = SCHLUESSEL;
		final var request = new EinwilligungsartPatchRequest();
		request.schluessel = JsonNullable.of(null);
		when(repository.findById(5L)).thenReturn(Optional.of(entity));
		when(repository.getReferencedIds(List.of(5L))).thenReturn(Set.of());

		final Einwilligungsart result = service.patch(5L, request);

		assertThat(entity.schluessel).isNull();
		assertThat(result.schluessel).isNull();
		verify(repository, never()).schluesselIsAlreadyUsedPatch(anyString(), any(), anyLong());
	}

	@Test
	@DisplayName("patch | Prüft Bezeichnung und Schlüssel nicht, wenn sie nicht im Request enthalten sind")
	void patch_withoutBezeichnungAndSchluessel() {
		final var entity = dto(5L, "Alt", PersonTyp.SCHUELER);
		final var request = new EinwilligungsartPatchRequest();
		request.sortierung = JsonNullable.of(12);
		request.istSichtbar = JsonNullable.of(false);
		when(repository.findById(5L)).thenReturn(Optional.of(entity));
		when(repository.getReferencedIds(List.of(5L))).thenReturn(Set.of());

		final Einwilligungsart result = service.patch(5L, request);

		assertThat(result.bezeichnung).isEqualTo("Alt");
		assertThat(result.sortierung).isEqualTo(12);
		assertThat(result.istSichtbar).isFalse();
		verify(repository, never()).bezeichnungIsAlreadyUsedPatch(anyString(), any(), anyLong());
		verify(repository, never()).schluesselIsAlreadyUsedPatch(anyString(), any(), anyLong());
	}

	@Test
	@DisplayName("patch | Wirft einen Fehler, wenn die Bezeichnung bei einer anderen Einwilligungsart desselben Personentyps verwendet wird")
	void patch_bezeichnungAlreadyUsed() {
		final var entity = dto(5L, "Alt", PersonTyp.SCHUELER);
		final var request = new EinwilligungsartPatchRequest();
		request.bezeichnung = JsonNullable.of("Neu");
		when(repository.findById(5L)).thenReturn(Optional.of(entity));
		when(repository.bezeichnungIsAlreadyUsedPatch("Neu", PersonTyp.SCHUELER, 5L)).thenReturn(true);

		assertThatThrownBy(() -> service.patch(5L, request)).satisfies(e -> assertBadRequest(e, "Neu"));

		assertThat(entity.bezeichnung).isEqualTo("Alt");
	}

	@Test
	@DisplayName("patch | Wirft einen Fehler, wenn der Schlüssel bei einer anderen Einwilligungsart desselben Personentyps verwendet wird")
	void patch_schluesselAlreadyUsed() {
		final var entity = dto(5L, "Alt", PersonTyp.SCHUELER);
		final var request = new EinwilligungsartPatchRequest();
		request.schluessel = JsonNullable.of(SCHLUESSEL);
		when(repository.findById(5L)).thenReturn(Optional.of(entity));
		when(repository.schluesselIsAlreadyUsedPatch(SCHLUESSEL, PersonTyp.SCHUELER, 5L)).thenReturn(true);

		assertThatThrownBy(() -> service.patch(5L, request)).satisfies(e -> assertBadRequest(e, SCHLUESSEL));

		assertThat(entity.schluessel).isNull();
	}

	@Test
	@DisplayName("patch | Wirft einen Fehler, wenn der Schlüssel keinem Coretyp entspricht")
	void patch_schluesselUnknown() {
		final var entity = dto(5L, "Alt", PersonTyp.SCHUELER);
		final var request = new EinwilligungsartPatchRequest();
		request.schluessel = JsonNullable.of("-35");
		when(repository.findById(5L)).thenReturn(Optional.of(entity));

		assertThatThrownBy(() -> service.patch(5L, request)).satisfies(e -> assertBadRequest(e, "-35"));

		assertThat(entity.schluessel).isNull();
		verify(repository, never()).schluesselIsAlreadyUsedPatch(anyString(), any(), anyLong());
	}

	@Test
	@DisplayName("patch | Wirft einen Fehler, wenn die Einwilligungsart nicht existiert")
	void patch_notFound() {
		when(repository.findById(99L)).thenReturn(Optional.empty());
		final var request = new EinwilligungsartPatchRequest();

		assertThatThrownBy(() -> service.patch(99L, request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("99")
				.extracting(e -> ((ApiOperationException) e).getStatus())
				.isEqualTo(Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("deleteMultiple | Löscht auch referenzierte Einwilligungsarten und meldet unbekannte als Fehler")
	void deleteMultiple_deletesReferencedToo() {
		final var e1 = dto(1L, "A", PersonTyp.SCHUELER);
		final var e2 = dto(2L, "B", PersonTyp.LEHRER);
		final List<Long> ids = List.of(1L, 2L, 3L);
		when(repository.findListByIds(ids)).thenReturn(List.of(e1, e2));
		when(repository.delete(List.of(e1, e2))).thenReturn(List.of(e1, e2));

		final var result = service.deleteMultiple(ids);

		assertThat(result).extracting(r -> r.id).containsExactly(1L, 2L, 3L);
		assertThat(result).extracting(r -> r.success).containsExactly(true, true, false);
		assertThat(result.get(2).log).anyMatch(m -> m.contains("nicht gefunden"));
		verify(repository, never()).getReferencedIds(any());
	}

}
