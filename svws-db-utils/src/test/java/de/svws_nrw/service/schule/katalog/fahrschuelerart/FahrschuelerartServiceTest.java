package de.svws_nrw.service.schule.katalog.fahrschuelerart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
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

import de.svws_nrw.core.data.schule.Fahrschuelerart;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFahrschuelerart;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.katalog.fahrschuelerart.FahrschuelerartMapper;
import de.svws_nrw.repo.schule.kataloge.fahrschuelerart.FahrschuelerartRepository;
import jakarta.ws.rs.core.Response;

/**
 * Tests für den {@link FahrschuelerartService}.
 */
@ExtendWith(MockitoExtension.class)
class FahrschuelerartServiceTest {

	@Mock
	private FahrschuelerartRepository repository;

	private final FahrschuelerartMapper mapper = FahrschuelerartMapper.INSTANCE;

	private FahrschuelerartService service;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeEach
	void setUp() {
		service = new FahrschuelerartService(repository, mapper);
		transactionSupportMock = Mockito.mockStatic(TransactionSupport.class);
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}

	@Test
	@DisplayName("getAll | Liefert die Fahrschülerarten aufsteigend nach ID sortiert mit gesetztem Referenz-Flag")
	void getAll_sortedWithReferencedFlag() {
		final var f1 = new DTOFahrschuelerart(1L, "A");
		final var f2 = new DTOFahrschuelerart(2L, "B");
		final var f3 = new DTOFahrschuelerart(3L, "C");
		when(repository.getAll()).thenReturn(List.of(f3, f1, f2));
		when(repository.getReferencedIds(List.of(3L, 1L, 2L))).thenReturn(Set.of(2L));

		final List<Fahrschuelerart> result = service.getAll();

		assertThat(result)
				.extracting(f -> f.id, f -> f.referenziertInAnderenTabellen)
				.containsExactly(tuple(1L, false), tuple(2L, true), tuple(3L, false));
	}

	@Test
	@DisplayName("getAll | Liefert eine leere Liste, wenn keine Fahrschülerarten existieren")
	void getAll_empty() {
		when(repository.getAll()).thenReturn(List.of());
		when(repository.getReferencedIds(List.of())).thenReturn(Set.of());

		assertThat(service.getAll()).isEmpty();
	}

	@Test
	@DisplayName("create | Legt die Fahrschülerart mit den Werten des Requests an")
	void create_success() {
		final var request = new FahrschuelerartCreateRequest();
		request.bezeichnung = "Bus";
		request.sortierung = 5;
		request.istSichtbar = false;
		request.istAenderbar = false;
		when(repository.bezeichnungIsAlreadyUsedCreate("Bus")).thenReturn(false);
		when(repository.create(any(DTOFahrschuelerart.class))).thenAnswer(invocation -> {
			final DTOFahrschuelerart dto = invocation.getArgument(0);
			dto.id = 7L;
			return dto;
		});
		when(repository.getReferencedIds(List.of(7L))).thenReturn(Set.of(7L));

		final Fahrschuelerart result = service.create(request);

		assertThat(result)
				.returns(7L, f -> f.id)
				.returns("Bus", f -> f.bezeichnung)
				.returns(5, f -> f.sortierung)
				.returns(false, f -> f.istSichtbar)
				.returns(false, f -> f.istAenderbar)
				.returns(true, f -> f.referenziertInAnderenTabellen);
		final var inOrder = inOrder(repository);
		inOrder.verify(repository).create(any(DTOFahrschuelerart.class));
		inOrder.verify(repository).flush();
	}

	@Test
	@DisplayName("create | Wirft einen Fehler und legt nichts an, wenn die Bezeichnung bereits verwendet wird")
	void create_bezeichnungAlreadyUsed() {
		final var request = new FahrschuelerartCreateRequest();
		request.bezeichnung = "Bus";
		when(repository.bezeichnungIsAlreadyUsedCreate("Bus")).thenReturn(true);

		assertThatThrownBy(() -> service.create(request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Bezeichnung Bus ist bereits vorhanden.")
				.extracting(e -> ((ApiOperationException) e).getStatus())
				.isEqualTo(Response.Status.BAD_REQUEST);

		verify(repository, never()).create(any(DTOFahrschuelerart.class));
	}

	@Test
	@DisplayName("patch | Aktualisiert alle Felder")
	void patch_success() {
		final var entity = new DTOFahrschuelerart(5L, "Alt");
		final var request = new FahrschuelerartPatchRequest();
		request.bezeichnung = JsonNullable.of("Neu");
		request.sortierung = JsonNullable.of(3);
		request.istSichtbar = JsonNullable.of(false);
		request.istAenderbar = JsonNullable.of(false);
		when(repository.findById(5L)).thenReturn(Optional.of(entity));
		when(repository.bezeichnungIsAlreadyUsedPatch("Neu", 5L)).thenReturn(false);
		when(repository.getReferencedIds(List.of(5L))).thenReturn(Set.of());

		final Fahrschuelerart result = service.patch(5L, request);

		assertThat(entity).returns("Neu", e -> e.bezeichnung);
		assertThat(result)
				.returns(5L, f -> f.id)
				.returns("Neu", f -> f.bezeichnung)
				.returns(3, f -> f.sortierung)
				.returns(false, f -> f.istSichtbar)
				.returns(false, f -> f.istAenderbar)
				.returns(false, f -> f.referenziertInAnderenTabellen);
	}

	@Test
	@DisplayName("patch | Prüft die Eindeutigkeit nicht, wenn die Bezeichnung nicht im Request enthalten ist")
	void patch_withoutBezeichnung() {
		final var entity = new DTOFahrschuelerart(5L, "Alt");
		when(repository.findById(5L)).thenReturn(Optional.of(entity));
		when(repository.getReferencedIds(List.of(5L))).thenReturn(Set.of());

		final Fahrschuelerart result = service.patch(5L, new FahrschuelerartPatchRequest());

		assertThat(result).returns("Alt", f -> f.bezeichnung);
		verify(repository, never()).bezeichnungIsAlreadyUsedPatch(any(), Mockito.anyLong());
	}

	@Test
	@DisplayName("patch | Wirft einen Fehler, wenn die Bezeichnung bei einer anderen Fahrschülerart verwendet wird")
	void patch_bezeichnungAlreadyUsed() {
		final var entity = new DTOFahrschuelerart(5L, "Alt");
		final var request = new FahrschuelerartPatchRequest();
		request.bezeichnung = JsonNullable.of("Neu");
		when(repository.findById(5L)).thenReturn(Optional.of(entity));
		when(repository.bezeichnungIsAlreadyUsedPatch("Neu", 5L)).thenReturn(true);

		assertThatThrownBy(() -> service.patch(5L, request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Bezeichnung Neu ist bereits vorhanden.")
				.extracting(e -> ((ApiOperationException) e).getStatus())
				.isEqualTo(Response.Status.BAD_REQUEST);

		assertThat(entity).returns("Alt", e -> e.bezeichnung);
	}

	@Test
	@DisplayName("patch | Wirft einen Fehler, wenn die Fahrschülerart nicht existiert")
	void patch_notFound() {
		when(repository.findById(99L)).thenReturn(Optional.empty());
		final var request = new FahrschuelerartPatchRequest();

		assertThatThrownBy(() -> service.patch(99L, request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Es wurde keine Fahrschülerart mit der ID 99 gefunden.")
				.extracting(e -> ((ApiOperationException) e).getStatus())
				.isEqualTo(Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("deleteMultiple | Löscht auch referenzierte Fahrschülerarten und meldet unbekannte als Fehler")
	void deleteMultiple_deletesReferencedToo() {
		final var f1 = new DTOFahrschuelerart(1L, "A");
		final var f2 = new DTOFahrschuelerart(2L, "B");
		final List<Long> ids = List.of(1L, 2L, 3L);
		when(repository.findListByIds(ids)).thenReturn(List.of(f1, f2));
		when(repository.delete(List.of(f1, f2))).thenReturn(List.of(f1, f2));

		final var result = service.deleteMultiple(ids);

		assertThat(result)
				.extracting(r -> r.id, r -> r.success)
				.containsExactly(tuple(1L, true), tuple(2L, true), tuple(3L, false));
		assertThat(result.get(2).log).anyMatch(m -> m.contains("nicht gefunden"));
		verify(repository, never()).getReferencedIds(any());
	}

	@Test
	@DisplayName("deleteMultiple | Wirft 400, wenn die Liste ein null-Element enthält")
	void deleteMultiple_nullElement() {
		final List<Long> ids = Collections.singletonList(null);

		assertThatThrownBy(() -> service.deleteMultiple(ids))
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Liste der zu löschenden IDs darf nicht null sein und keine null-Werte enthalten.")
				.extracting(e -> ((ApiOperationException) e).getStatus())
				.isEqualTo(Response.Status.BAD_REQUEST);

		verifyNoInteractions(repository);
	}

	@Test
	@DisplayName("deleteMultiple | Wirft 400, wenn die Liste null ist")
	void deleteMultiple_nullListe() {
		assertThatThrownBy(() -> service.deleteMultiple(null))
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Liste der zu löschenden IDs darf nicht null sein und keine null-Werte enthalten.")
				.extracting(e -> ((ApiOperationException) e).getStatus())
				.isEqualTo(Response.Status.BAD_REQUEST);

		verifyNoInteractions(repository);
	}

}
