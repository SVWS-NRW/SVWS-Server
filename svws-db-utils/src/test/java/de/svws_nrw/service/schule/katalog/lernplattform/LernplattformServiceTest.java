package de.svws_nrw.service.schule.katalog.lernplattform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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

import de.svws_nrw.core.data.schule.Lernplattform;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.svws.auth.DTOLernplattformen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.lernplattform.LernplattformMapper;
import de.svws_nrw.repo.schule.kataloge.lernplattform.LernplattformRepository;
import de.svws_nrw.service.lehrer.lernplattform.LehrerLernplattformService;
import de.svws_nrw.service.schueler.lernplattform.SchuelerLernplattformService;
import jakarta.ws.rs.core.Response;

/**
 * Tests für den {@link LernplattformService}.
 */
@ExtendWith(MockitoExtension.class)
class LernplattformServiceTest {

	@Mock
	private LernplattformRepository repository;

	@Mock
	private SchuelerLernplattformService schuelerLernplattformService;

	@Mock
	private LehrerLernplattformService lehrerLernplattformService;

	private final LernplattformMapper mapper = LernplattformMapper.INSTANCE;

	private LernplattformService service;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeEach
	void setUp() {
		service = new LernplattformService(repository, mapper, schuelerLernplattformService, lehrerLernplattformService);
		transactionSupportMock = Mockito.mockStatic(TransactionSupport.class);
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}

	@Test
	@DisplayName("getAll | Liefert die Lernplattformen aufsteigend nach ID sortiert mit gesetztem Referenz-Flag")
	void getAll_sortedWithReferencedFlag() {
		final var l1 = new DTOLernplattformen(1L, "A");
		final var l2 = new DTOLernplattformen(2L, "B");
		final var l3 = new DTOLernplattformen(3L, "C");
		when(repository.getAll()).thenReturn(List.of(l3, l1, l2));
		when(repository.getReferencedIds(List.of(3L, 1L, 2L))).thenReturn(Set.of(2L));

		final List<Lernplattform> result = service.getAll();

		assertThat(result).extracting(l -> l.id).containsExactly(1L, 2L, 3L);
		assertThat(result).extracting(l -> l.referenziertInAnderenTabellen).containsExactly(false, true, false);
	}

	@Test
	@DisplayName("getAll | Liefert eine leere Liste, wenn keine Lernplattformen existieren")
	void getAll_empty() {
		when(repository.getAll()).thenReturn(List.of());
		when(repository.getReferencedIds(List.of())).thenReturn(Set.of());

		assertThat(service.getAll()).isEmpty();
	}

	@Test
	@DisplayName("create | Legt die Lernplattform an und erzeugt die Zuordnungen für Schüler und Lehrer")
	void create_success() {
		final var request = new LernplattformCreateRequest();
		request.bezeichnung = "Moodle";
		when(repository.bezeichnungIsAlreadyUsedCreate("Moodle")).thenReturn(false);
		when(repository.create(any(DTOLernplattformen.class))).thenAnswer(invocation -> {
			final DTOLernplattformen dto = invocation.getArgument(0);
			dto.id = 7L;
			return dto;
		});
		when(repository.getReferencedIds(List.of(7L))).thenReturn(Set.of(7L));

		final Lernplattform result = service.create(request);

		assertThat(result.id).isEqualTo(7L);
		assertThat(result.bezeichnung).isEqualTo("Moodle");
		assertThat(result.referenziertInAnderenTabellen).isTrue();
		final var inOrder = inOrder(repository, schuelerLernplattformService, lehrerLernplattformService);
		inOrder.verify(repository).create(any(DTOLernplattformen.class));
		inOrder.verify(repository).flush();
		inOrder.verify(schuelerLernplattformService).addForLernplattform(7L);
		inOrder.verify(lehrerLernplattformService).addForLernplattform(7L);
	}

	@Test
	@DisplayName("create | Wirft einen Fehler und legt nichts an, wenn die Bezeichnung bereits verwendet wird")
	void create_bezeichnungAlreadyUsed() {
		final var request = new LernplattformCreateRequest();
		request.bezeichnung = "Moodle";
		when(repository.bezeichnungIsAlreadyUsedCreate("Moodle")).thenReturn(true);

		assertThatThrownBy(() -> service.create(request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Moodle")
				.extracting(e -> ((ApiOperationException) e).getStatus())
				.isEqualTo(Response.Status.BAD_REQUEST);

		verify(repository, never()).create(any(DTOLernplattformen.class));
		verify(schuelerLernplattformService, never()).addForLernplattform(Mockito.anyLong());
		verify(lehrerLernplattformService, never()).addForLernplattform(Mockito.anyLong());
	}

	@Test
	@DisplayName("patch | Aktualisiert die Bezeichnung")
	void patch_success() {
		final var entity = new DTOLernplattformen(5L, "Alt");
		final var request = new LernplattformPatchRequest();
		request.bezeichnung = JsonNullable.of("Neu");
		when(repository.findById(5L)).thenReturn(Optional.of(entity));
		when(repository.bezeichnungIsAlreadyUsedPatch("Neu", 5L)).thenReturn(false);
		when(repository.getReferencedIds(List.of(5L))).thenReturn(Set.of());

		final Lernplattform result = service.patch(5L, request);

		assertThat(entity.bezeichnung).isEqualTo("Neu");
		assertThat(result.id).isEqualTo(5L);
		assertThat(result.bezeichnung).isEqualTo("Neu");
		assertThat(result.referenziertInAnderenTabellen).isFalse();
	}

	@Test
	@DisplayName("patch | Prüft die Eindeutigkeit nicht, wenn die Bezeichnung nicht im Request enthalten ist")
	void patch_withoutBezeichnung() {
		final var entity = new DTOLernplattformen(5L, "Alt");
		when(repository.findById(5L)).thenReturn(Optional.of(entity));
		when(repository.getReferencedIds(List.of(5L))).thenReturn(Set.of());

		final Lernplattform result = service.patch(5L, new LernplattformPatchRequest());

		assertThat(result.bezeichnung).isEqualTo("Alt");
		verify(repository, never()).bezeichnungIsAlreadyUsedPatch(any(), Mockito.anyLong());
	}

	@Test
	@DisplayName("patch | Wirft einen Fehler, wenn die Bezeichnung bei einer anderen Lernplattform verwendet wird")
	void patch_bezeichnungAlreadyUsed() {
		final var entity = new DTOLernplattformen(5L, "Alt");
		final var request = new LernplattformPatchRequest();
		request.bezeichnung = JsonNullable.of("Neu");
		when(repository.findById(5L)).thenReturn(Optional.of(entity));
		when(repository.bezeichnungIsAlreadyUsedPatch("Neu", 5L)).thenReturn(true);

		assertThatThrownBy(() -> service.patch(5L, request))
				.isInstanceOf(ApiOperationException.class)
				.extracting(e -> ((ApiOperationException) e).getStatus())
				.isEqualTo(Response.Status.BAD_REQUEST);

		assertThat(entity.bezeichnung).isEqualTo("Alt");
	}

	@Test
	@DisplayName("patch | Wirft einen Fehler, wenn die Lernplattform nicht existiert")
	void patch_notFound() {
		when(repository.findById(99L)).thenReturn(Optional.empty());
		final var request = new LernplattformPatchRequest();

		assertThatThrownBy(() -> service.patch(99L, request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("99")
				.extracting(e -> ((ApiOperationException) e).getStatus())
				.isEqualTo(Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("deleteMultiple | Löscht auch referenzierte Lernplattformen und meldet unbekannte als Fehler")
	void deleteMultiple_deletesReferencedToo() {
		final var l1 = new DTOLernplattformen(1L, "A");
		final var l2 = new DTOLernplattformen(2L, "B");
		final List<Long> ids = List.of(1L, 2L, 3L);
		when(repository.findListByIds(ids)).thenReturn(List.of(l1, l2));
		when(repository.delete(List.of(l1, l2))).thenReturn(List.of(l1, l2));

		final var result = service.deleteMultiple(ids);

		assertThat(result).extracting(r -> r.id).containsExactly(1L, 2L, 3L);
		assertThat(result).extracting(r -> r.success).containsExactly(true, true, false);
		assertThat(result.get(2).log).anyMatch(m -> m.contains("nicht gefunden"));
		verify(repository, never()).getReferencedIds(any());
	}

}
