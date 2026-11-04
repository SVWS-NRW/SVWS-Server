package de.svws_nrw.controller.schule.teilstandort;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Teilstandort;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schule.teilstandort.TeilstandortCreateRequest;
import de.svws_nrw.service.schule.teilstandort.TeilstandortPatchRequest;
import de.svws_nrw.service.schule.teilstandort.TeilstandortService;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeilstandortControllerImplTest {

	@Mock
	private TeilstandortService teilstandortService;

	@InjectMocks
	private TeilstandortControllerImpl teilstandortControllerImpl;

	// -------------------------------------------------------------------------
	// Konstruktor
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		final var controller = new TeilstandortControllerImpl(teilstandortService);

		assertThat(controller)
				.isNotNull()
				.isInstanceOf(TeilstandortController.class);
	}

	// -------------------------------------------------------------------------
	// getAll
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		final var teilstandort = new Teilstandort();
		teilstandort.adrMerkmal = "A";
		final var daten = List.of(teilstandort);
		when(teilstandortService.getAll()).thenReturn(daten);

		try (var result = teilstandortControllerImpl.getAll()) {
			assertApplicationJsonResponse(result, 200);
			assertThat(result.getEntity()).isEqualTo(daten);
		}
		verify(teilstandortService).getAll();
	}

	@Test
	@DisplayName("getAll | Erfolg - leere Liste")
	void getAll_leer() {
		when(teilstandortService.getAll()).thenReturn(List.of());

		try (var result = teilstandortControllerImpl.getAll()) {
			assertApplicationJsonResponse(result, 200);
			assertThat(result.getEntity()).isEqualTo(List.of());
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("create | Erfolg - liefert 201 und den angelegten Teilstandort")
	void create() {
		final var created = new Teilstandort();
		created.adrMerkmal = "A";
		final var request = createRequest("A");
		when(teilstandortService.create(any(TeilstandortCreateRequest.class))).thenReturn(created);

		try (var result = teilstandortControllerImpl.create(request)) {
			assertApplicationJsonResponse(result, 201);
			assertThat(result.getEntity()).isSameAs(created);
		}

		verify(teilstandortService).create(request);
	}

	@Test
	@DisplayName("create | Erfolg - optionale Felder dürfen fehlen")
	void create_nurAdrMerkmal() {
		when(teilstandortService.create(any(TeilstandortCreateRequest.class))).thenReturn(new Teilstandort());

		try (var result = teilstandortControllerImpl.create(createRequest("A"))) {
			assertApplicationJsonResponse(result, 201);
		}
	}

	@Test
	@DisplayName("create | Failed - Adressmerkmal leer")
	void create_adrMerkmalLeer() {
		assertBadRequestOhneServiceAufruf(createRequest(""));
	}

	@Test
	@DisplayName("create | Failed - Adressmerkmal null")
	void create_adrMerkmalNull() {
		assertBadRequestOhneServiceAufruf(createRequest(null));
	}

	@Test
	@DisplayName("create | Failed - Adressmerkmal zu lang")
	void create_adrMerkmalZuLang() {
		assertBadRequestOhneServiceAufruf(createRequest("AB"));
	}

	@Test
	@DisplayName("create | Failed - Adressmerkmal nur Leerzeichen")
	void create_adrMerkmalLeerzeichen() {
		assertBadRequestOhneServiceAufruf(createRequest(" "));
	}

	@Test
	@DisplayName("create | Failed - PLZ zu lang")
	void create_plzZuLang() {
		final var request = createRequest("A");
		request.plz = "1".repeat(11);

		assertBadRequestOhneServiceAufruf(request);
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch | Erfolg - liefert 200 und den geänderten Teilstandort")
	void patch() {
		final var updated = new Teilstandort();
		updated.adrMerkmal = "A";
		final var request = patchRequest();
		when(teilstandortService.patch(eq("A"), any(TeilstandortPatchRequest.class))).thenReturn(updated);

		try (var result = teilstandortControllerImpl.patch("A", request)) {
			assertApplicationJsonResponse(result, 200);
			assertThat(result.getEntity()).isSameAs(updated);
		}

		verify(teilstandortService).patch("A", request);
	}

	@Test
	@DisplayName("patch | Erfolg - leerer Patch ist gültig")
	void patch_leererPatch() {
		when(teilstandortService.patch(eq("A"), any(TeilstandortPatchRequest.class))).thenReturn(new Teilstandort());

		try (var result = teilstandortControllerImpl.patch("A", new TeilstandortPatchRequest())) {
			assertApplicationJsonResponse(result, 200);
		}
	}

	@Test
	@DisplayName("patch | Failed - PLZ zu lang")
	void patch_plzZuLang() {
		final var request = new TeilstandortPatchRequest();
		request.plz = JsonNullable.of("1".repeat(11));

		assertThat(catchThrowable(() -> teilstandortControllerImpl.patch("A", request)))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

		verifyNoInteractions(teilstandortService);
	}

	// -------------------------------------------------------------------------
	// delete
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("delete | Erfolg - liefert 200 und die Ergebnisse der Löschvorgänge")
	void delete() {
		final List<String> ids = List.of("A", "B");
		final var responses = List.of(SimpleOperationResponse.ofSuccess(1L), SimpleOperationResponse.ofSuccess(2L));
		when(teilstandortService.deleteMultiple(ids)).thenReturn(responses);

		try (var result = teilstandortControllerImpl.delete(ids)) {
			assertApplicationJsonResponse(result, 200);
			assertThat(result.getEntity()).isSameAs(responses);
		}

		verify(teilstandortService).deleteMultiple(ids);
	}

	@Test
	@DisplayName("delete | Erfolg - leere Liste")
	void delete_leereListe() {
		when(teilstandortService.deleteMultiple(List.of())).thenReturn(List.of());

		try (var result = teilstandortControllerImpl.delete(List.of())) {
			assertApplicationJsonResponse(result, 200);
			assertThat(result.getEntity()).isEqualTo(List.of());
		}
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private void assertBadRequestOhneServiceAufruf(final TeilstandortCreateRequest request) {
		assertThat(catchThrowable(() -> teilstandortControllerImpl.create(request)))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

		verifyNoInteractions(teilstandortService);
	}

	private static TeilstandortPatchRequest patchRequest() {
		final var request = new TeilstandortPatchRequest();
		request.plz = JsonNullable.of("40213");
		return request;
	}

	private static TeilstandortCreateRequest createRequest(final String adrMerkmal) {
		final var request = new TeilstandortCreateRequest();
		request.adrMerkmal = adrMerkmal;
		return request;
	}

	private static void assertApplicationJsonResponse(
			final Response response,
			final int expectedStatus) {
		assertThat(response)
				.hasFieldOrPropertyWithValue("status", expectedStatus)
				.hasFieldOrProperty("entity");

		assertThat(response.getMediaType())
				.isNotNull()
				.satisfies(mediaType ->
						assertThat(mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)).isTrue());
	}
}
