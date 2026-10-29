package de.svws_nrw.service.schule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

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

import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.SchuljahresabschnittMapper;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import jakarta.ws.rs.core.Response;

/**
 * Testet den Service für den Zugriff auf die Schuljahresabschnitte
 */
@ExtendWith(MockitoExtension.class)
class SchuljahresabschnittServiceTest {

	@Mock
	private SchuljahresabschnitteRepository repoMock;

	private SchuljahresabschnittService service;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeEach
	void setUp() {
		service = new SchuljahresabschnittService(repoMock, SchuljahresabschnittMapper.INSTANCE);
		transactionSupportMock = mockStatic(TransactionSupport.class);
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}

	private static SchuljahresabschnittCreateRequest createRequest(final int schuljahr, final int abschnitt) {
		final var request = new SchuljahresabschnittCreateRequest();
		request.schuljahr = schuljahr;
		request.abschnitt = abschnitt;
		return request;
	}

	private void assertApiError(final Runnable aufruf, final Response.Status status, final String meldung) {
		assertThatThrownBy(aufruf::run)
				.isInstanceOfSatisfying(ApiOperationException.class, e -> {
					assertThat(e.getStatus()).isEqualTo(status);
					assertThat(e.getBody()).isEqualTo(meldung);
				});
	}


	// --- getById ---

	@Test
	@DisplayName("getById | liefert korrekt gemappte Daten")
	void getById_success() {
		final long id = 42L;
		final var dto = new DTOSchuljahresabschnitte(id, 2023, 1);
		dto.VorigerAbschnitt_ID = 123L;
		dto.FolgeAbschnitt_ID = 19L;
		when(repoMock.findById(id)).thenReturn(Optional.of(dto));

		final var result = service.getById(id);

		assertThat(result)
				.extracting("id", "schuljahr", "abschnitt", "idVorigerAbschnitt", "idFolgeAbschnitt")
				.containsExactly(id, 2023, 1, 123L, 19L);
		verify(repoMock, times(1)).findById(id);
	}

	@Test
	@DisplayName("getById | unbekannte ID liefert NOT_FOUND mit deutscher Meldung")
	void getById_notFound() {
		when(repoMock.findById(42L)).thenReturn(Optional.empty());

		assertApiError(() -> service.getById(42L), Response.Status.NOT_FOUND, "Der Schuljahresabschnitt mit der ID 42 wurde nicht gefunden.");
	}


	// --- findBySchuljahrAndAbschnitt ---

	@Test
	@DisplayName("findBySchuljahrAndAbschnitt | liefert korrekt gemappte Daten")
	void findBySchuljahrAndAbschnitt_success() {
		final var dto = new DTOSchuljahresabschnitte(7L, 2025, 2);
		dto.VorigerAbschnitt_ID = 6L;
		when(repoMock.findBySchuljahrAndAbschnitt(2025, 2)).thenReturn(Optional.of(dto));

		final var result = service.findBySchuljahrAndAbschnitt(2025, 2);

		assertThat(result).get()
				.extracting("id", "schuljahr", "abschnitt", "idVorigerAbschnitt", "idFolgeAbschnitt")
				.containsExactly(7L, 2025, 2, 6L, null);
	}

	@Test
	@DisplayName("findBySchuljahrAndAbschnitt | unbekannte Kombination liefert ein leeres Optional")
	void findBySchuljahrAndAbschnitt_empty() {
		when(repoMock.findBySchuljahrAndAbschnitt(2025, 2)).thenReturn(Optional.empty());

		assertThat(service.findBySchuljahrAndAbschnitt(2025, 2)).isEmpty();
	}


	// --- getList ---

	@Test
	@DisplayName("getList | mappt die DTOs und sortiert nach Schuljahr und Abschnitt")
	void getList_sortedAndMapped() {
		final var dto1 = new DTOSchuljahresabschnitte(1L, 2026, 1);
		dto1.FolgeAbschnitt_ID = 2L;
		final var dto2 = new DTOSchuljahresabschnitte(2L, 2026, 2);
		dto2.VorigerAbschnitt_ID = 1L;
		dto2.FolgeAbschnitt_ID = 3L;
		final var dto3 = new DTOSchuljahresabschnitte(3L, 2027, 1);
		dto3.VorigerAbschnitt_ID = 2L;
		when(repoMock.getAll()).thenReturn(List.of(dto3, dto1, dto2));

		final var result = service.getList();

		assertThat(result).hasSize(3);
		assertThat(result).extracting("id").containsExactly(1L, 2L, 3L);
		assertThat(result.get(0)).extracting("schuljahr", "abschnitt", "idVorigerAbschnitt", "idFolgeAbschnitt")
				.containsExactly(2026, 1, null, 2L);
		assertThat(result.get(2)).extracting("schuljahr", "abschnitt", "idVorigerAbschnitt", "idFolgeAbschnitt")
				.containsExactly(2027, 1, 2L, null);
	}

	@Test
	@DisplayName("getList | leere Tabelle liefert eine leere Liste")
	void getList_empty() {
		when(repoMock.getAll()).thenReturn(List.of());

		assertThat(service.getList()).isEmpty();
	}


	// --- create ---

	@Test
	@DisplayName("create | Erfolg ohne Verknüpfungen")
	void create_success() {
		final var request = createRequest(2026, 1);
		when(repoMock.findBySchuljahrAndAbschnitt(2026, 1)).thenReturn(Optional.empty());
		when(repoMock.create(any(DTOSchuljahresabschnitte.class))).thenAnswer(invocation -> {
			final DTOSchuljahresabschnitte entity = invocation.getArgument(0);
			entity.ID = 42L;
			return entity;
		});

		final var result = service.create(request);

		assertThat(result)
				.extracting("id", "schuljahr", "abschnitt", "idVorigerAbschnitt", "idFolgeAbschnitt")
				.containsExactly(42L, 2026, 1, null, null);
		verify(repoMock, never()).existsById(any());
	}

	@Test
	@DisplayName("create | Erfolg mit existierendem Vorgänger und Folgeabschnitt")
	void create_successWithLinks() {
		final var request = createRequest(2026, 2);
		request.idVorigerAbschnitt = 41L;
		request.idFolgeAbschnitt = 43L;
		when(repoMock.findBySchuljahrAndAbschnitt(2026, 2)).thenReturn(Optional.empty());
		when(repoMock.existsById(41L)).thenReturn(true);
		when(repoMock.existsById(43L)).thenReturn(true);
		when(repoMock.create(any(DTOSchuljahresabschnitte.class))).thenAnswer(invocation -> {
			final DTOSchuljahresabschnitte entity = invocation.getArgument(0);
			entity.ID = 42L;
			return entity;
		});

		final var result = service.create(request);

		assertThat(result)
				.extracting("id", "schuljahr", "abschnitt", "idVorigerAbschnitt", "idFolgeAbschnitt")
				.containsExactly(42L, 2026, 2, 41L, 43L);
	}

	@Test
	@DisplayName("create | bereits vorhandene Kombination aus Schuljahr und Abschnitt wird abgelehnt")
	void create_duplicate() {
		final var request = createRequest(2026, 1);
		when(repoMock.findBySchuljahrAndAbschnitt(2026, 1)).thenReturn(Optional.of(new DTOSchuljahresabschnitte(5L, 2026, 1)));

		assertApiError(() -> service.create(request), Response.Status.BAD_REQUEST,
				"Der Schuljahresabschnitt für das Schuljahr 2026 und den Abschnitt 1 existiert bereits.");
		verify(repoMock, never()).create(any(DTOSchuljahresabschnitte.class));
	}

	@Test
	@DisplayName("create | unbekannter Vorgänger wird abgelehnt")
	void create_unknownVorgaenger() {
		final var request = createRequest(2026, 2);
		request.idVorigerAbschnitt = 5L;
		when(repoMock.findBySchuljahrAndAbschnitt(2026, 2)).thenReturn(Optional.empty());
		when(repoMock.existsById(5L)).thenReturn(false);

		assertApiError(() -> service.create(request), Response.Status.BAD_REQUEST,
				"Der angegebene vorherige Schuljahresabschnitt mit der ID 5 existiert nicht.");
		verify(repoMock, never()).create(any(DTOSchuljahresabschnitte.class));
	}

	@Test
	@DisplayName("create | unbekannter Folgeabschnitt wird abgelehnt")
	void create_unknownFolgeabschnitt() {
		final var request = createRequest(2026, 2);
		request.idFolgeAbschnitt = 6L;
		when(repoMock.findBySchuljahrAndAbschnitt(2026, 2)).thenReturn(Optional.empty());
		when(repoMock.existsById(6L)).thenReturn(false);

		assertApiError(() -> service.create(request), Response.Status.BAD_REQUEST,
				"Der angegebene folgende Schuljahresabschnitt mit der ID 6 existiert nicht.");
		verify(repoMock, never()).create(any(DTOSchuljahresabschnitte.class));
	}


	// --- patch ---

	@Test
	@DisplayName("patch | setzt beide Verknüpfungen")
	void patch_setsLinks() {
		final var entity = new DTOSchuljahresabschnitte(11L, 2025, 2);
		final var request = new SchuljahresabschnittPatchRequest();
		request.idVorigerAbschnitt = JsonNullable.of(10L);
		request.idFolgeAbschnitt = JsonNullable.of(12L);
		when(repoMock.findById(11L)).thenReturn(Optional.of(entity));
		when(repoMock.existsById(10L)).thenReturn(true);
		when(repoMock.existsById(12L)).thenReturn(true);

		final var result = service.patch(11L, request);

		assertThat(result)
				.extracting("id", "schuljahr", "abschnitt", "idVorigerAbschnitt", "idFolgeAbschnitt")
				.containsExactly(11L, 2025, 2, 10L, 12L);
		assertThat(entity.VorigerAbschnitt_ID).isEqualTo(10L);
		assertThat(entity.FolgeAbschnitt_ID).isEqualTo(12L);
	}

	@Test
	@DisplayName("patch | ändert nur die im Request gesetzten Felder")
	void patch_onlyChangesPresentFields() {
		final var entity = new DTOSchuljahresabschnitte(11L, 2025, 2);
		entity.VorigerAbschnitt_ID = 10L;
		entity.FolgeAbschnitt_ID = 12L;
		final var request = new SchuljahresabschnittPatchRequest();
		request.idFolgeAbschnitt = JsonNullable.of(13L);
		when(repoMock.findById(11L)).thenReturn(Optional.of(entity));
		when(repoMock.existsById(13L)).thenReturn(true);

		final var result = service.patch(11L, request);

		assertThat(result).extracting("idVorigerAbschnitt", "idFolgeAbschnitt").containsExactly(10L, 13L);
		verify(repoMock, never()).existsById(10L);
	}

	@Test
	@DisplayName("patch | ein explizites null löst die Verknüpfung ohne Existenzprüfung")
	void patch_explicitNullClearsLink() {
		final var entity = new DTOSchuljahresabschnitte(11L, 2025, 2);
		entity.VorigerAbschnitt_ID = 10L;
		entity.FolgeAbschnitt_ID = 12L;
		final var request = new SchuljahresabschnittPatchRequest();
		request.idVorigerAbschnitt = JsonNullable.of(null);
		when(repoMock.findById(11L)).thenReturn(Optional.of(entity));

		final var result = service.patch(11L, request);

		assertThat(result).extracting("idVorigerAbschnitt", "idFolgeAbschnitt").containsExactly(null, 12L);
		verify(repoMock, never()).existsById(any());
	}

	@Test
	@DisplayName("patch | unbekannter Schuljahresabschnitt liefert NOT_FOUND")
	void patch_notFound() {
		final var request = new SchuljahresabschnittPatchRequest();
		when(repoMock.findById(11L)).thenReturn(Optional.empty());

		assertApiError(() -> service.patch(11L, request), Response.Status.NOT_FOUND, "Der Schuljahresabschnitt mit der ID 11 wurde nicht gefunden.");
	}

	@Test
	@DisplayName("patch | ein Abschnitt darf nicht sein eigener Vorgänger sein")
	void patch_ownVorgaenger() {
		final var entity = new DTOSchuljahresabschnitte(11L, 2025, 2);
		final var request = new SchuljahresabschnittPatchRequest();
		request.idVorigerAbschnitt = JsonNullable.of(11L);
		when(repoMock.findById(11L)).thenReturn(Optional.of(entity));

		assertApiError(() -> service.patch(11L, request), Response.Status.BAD_REQUEST,
				"Ein Schuljahresabschnitt darf nicht sein eigener vorheriger Schuljahresabschnitt sein.");
		assertThat(entity.VorigerAbschnitt_ID).isNull();
	}

	@Test
	@DisplayName("patch | ein Abschnitt darf nicht sein eigener Folgeabschnitt sein")
	void patch_ownFolgeabschnitt() {
		final var entity = new DTOSchuljahresabschnitte(11L, 2025, 2);
		final var request = new SchuljahresabschnittPatchRequest();
		request.idFolgeAbschnitt = JsonNullable.of(11L);
		when(repoMock.findById(11L)).thenReturn(Optional.of(entity));

		assertApiError(() -> service.patch(11L, request), Response.Status.BAD_REQUEST,
				"Ein Schuljahresabschnitt darf nicht sein eigener folgender Schuljahresabschnitt sein.");
		assertThat(entity.FolgeAbschnitt_ID).isNull();
	}

	@Test
	@DisplayName("patch | unbekannter Vorgänger wird abgelehnt")
	void patch_unknownVorgaenger() {
		final var entity = new DTOSchuljahresabschnitte(11L, 2025, 2);
		final var request = new SchuljahresabschnittPatchRequest();
		request.idVorigerAbschnitt = JsonNullable.of(5L);
		when(repoMock.findById(11L)).thenReturn(Optional.of(entity));
		when(repoMock.existsById(5L)).thenReturn(false);

		assertApiError(() -> service.patch(11L, request), Response.Status.BAD_REQUEST,
				"Der angegebene vorherige Schuljahresabschnitt mit der ID 5 existiert nicht.");
		assertThat(entity.VorigerAbschnitt_ID).isNull();
	}

	@Test
	@DisplayName("patch | unbekannter Folgeabschnitt wird abgelehnt")
	void patch_unknownFolgeabschnitt() {
		final var entity = new DTOSchuljahresabschnitte(11L, 2025, 2);
		final var request = new SchuljahresabschnittPatchRequest();
		request.idFolgeAbschnitt = JsonNullable.of(6L);
		when(repoMock.findById(11L)).thenReturn(Optional.of(entity));
		when(repoMock.existsById(6L)).thenReturn(false);

		assertApiError(() -> service.patch(11L, request), Response.Status.BAD_REQUEST,
				"Der angegebene folgende Schuljahresabschnitt mit der ID 6 existiert nicht.");
		assertThat(entity.FolgeAbschnitt_ID).isNull();
	}

}
