package de.svws_nrw.controller.schule.katalog.ankreuzkompetenz;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Ankreuzkompetenz;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzCreateRequest;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzPatchRequest;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnkreuzkompetenzControllerImplTest {

	@Mock
	private AnkreuzkompetenzService ankreuzkompetenzService;

	@InjectMocks
	private AnkreuzkompetenzControllerImpl controller;

	// -------------------------------------------------------------------------
	// getAll
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		when(ankreuzkompetenzService.getAll()).thenReturn(List.of(new Ankreuzkompetenz()));

		try (var result = controller.getAll()) {
			assertApplicationJsonResponse(result, 200);
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("create | Erfolg")
	void create() {
		when(ankreuzkompetenzService.create(any(AnkreuzkompetenzCreateRequest.class))).thenReturn(new Ankreuzkompetenz());

		try (var result = controller.create(createRequest("Text"))) {
			assertApplicationJsonResponse(result, 201);
		}
	}

	@Test
	@DisplayName("create | Failed - floskelText leer")
	void create_floskelTextLeer() {
		assertValidierungsfehlerBeimAnlegen(createRequest(""));
	}

	@Test
	@DisplayName("create | Failed - floskelText zu lang")
	void create_floskelTextZuLang() {
		assertValidierungsfehlerBeimAnlegen(createRequest("a".repeat(256)));
	}

	@Test
	@DisplayName("create | Failed - istASV fehlt")
	void create_istASVFehlt() {
		final var request = createRequest("Text");
		request.istASV = null;

		assertValidierungsfehlerBeimAnlegen(request);
	}

	@Test
	@DisplayName("create | Failed - schulgliederung zu lang")
	void create_schulgliederungZuLang() {
		final var request = createRequest("Text");
		request.schulgliederung = "ABCD";

		assertValidierungsfehlerBeimAnlegen(request);
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch | Erfolg")
	void patch() {
		when(ankreuzkompetenzService.patch(anyLong(), any(AnkreuzkompetenzPatchRequest.class))).thenReturn(new Ankreuzkompetenz());

		final var patch = new AnkreuzkompetenzPatchRequest();
		patch.floskelText = JsonNullable.of("Text");

		try (var result = controller.patch(1L, patch)) {
			assertApplicationJsonResponse(result, 200);
		}
	}

	@Test
	@DisplayName("patch | Erfolg ohne gesetzte Felder")
	void patch_undefined() {
		when(ankreuzkompetenzService.patch(anyLong(), any(AnkreuzkompetenzPatchRequest.class))).thenReturn(new Ankreuzkompetenz());

		try (var result = controller.patch(1L, new AnkreuzkompetenzPatchRequest())) {
			assertApplicationJsonResponse(result, 200);
		}
	}

	@Test
	@DisplayName("patch | Erfolg mit explizitem null bei nullable Feldern")
	void patch_expliziteNull() {
		when(ankreuzkompetenzService.patch(anyLong(), any(AnkreuzkompetenzPatchRequest.class))).thenReturn(new Ankreuzkompetenz());

		final var patch = new AnkreuzkompetenzPatchRequest();
		patch.idFach = JsonNullable.of(null);
		patch.schulgliederung = JsonNullable.of(null);

		try (var result = controller.patch(1L, patch)) {
			assertApplicationJsonResponse(result, 200);
		}
	}

	@Test
	@DisplayName("patch | Failed - floskelText leer")
	void patch_floskelTextLeer() {
		final var patch = new AnkreuzkompetenzPatchRequest();
		patch.floskelText = JsonNullable.of("");

		assertValidierungsfehlerBeimPatch(patch);
	}

	@Test
	@DisplayName("patch | Failed - istSichtbar explizit null")
	void patch_istSichtbarNull() {
		final var patch = new AnkreuzkompetenzPatchRequest();
		patch.istSichtbar = JsonNullable.of(null);

		assertValidierungsfehlerBeimPatch(patch);
	}

	@Test
	@DisplayName("patch | Failed - sortierung explizit null")
	void patch_sortierungNull() {
		final var patch = new AnkreuzkompetenzPatchRequest();
		patch.sortierung = JsonNullable.of(null);

		assertValidierungsfehlerBeimPatch(patch);
	}

	@Test
	@DisplayName("patch | Failed - istASV explizit null")
	void patch_istASVNull() {
		final var patch = new AnkreuzkompetenzPatchRequest();
		patch.istASV = JsonNullable.of(null);

		assertValidierungsfehlerBeimPatch(patch);
	}

	// -------------------------------------------------------------------------
	// delete
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("delete | Erfolg")
	void delete() {
		final var ids = List.of(1L);
		when(ankreuzkompetenzService.delete(ids)).thenReturn(List.of(SimpleOperationResponse.ofSuccess(1L)));

		try (var result = controller.delete(ids)) {
			assertApplicationJsonResponse(result, 200);
		}
	}

	@Test
	@DisplayName("delete | Referenzierte Einträge werden als nicht gelöscht in der Antwort gemeldet")
	void delete_referenzierteEintraege() {
		final var ids = List.of(1L, 2L);
		final var geloescht = SimpleOperationResponse.ofSuccess(1L);
		final var referenziert = SimpleOperationResponse.ofError(2L, "Ankreuzkompetenz mit ID 2 ist referenziert und kann nicht gelöscht werden.");
		when(ankreuzkompetenzService.delete(ids)).thenReturn(List.of(geloescht, referenziert));

		try (var result = controller.delete(ids)) {
			assertApplicationJsonResponse(result, 200);
			assertThat(result.getEntity()).isEqualTo(List.of(geloescht, referenziert));
		}
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private void assertValidierungsfehlerBeimAnlegen(final AnkreuzkompetenzCreateRequest request) {
		assertThat(catchThrowable(() -> controller.create(request)))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		verify(ankreuzkompetenzService, never()).create(any(AnkreuzkompetenzCreateRequest.class));
	}

	private void assertValidierungsfehlerBeimPatch(final AnkreuzkompetenzPatchRequest patch) {
		assertThat(catchThrowable(() -> controller.patch(1L, patch)))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		verify(ankreuzkompetenzService, never()).patch(anyLong(), any(AnkreuzkompetenzPatchRequest.class));
	}

	private static void assertApplicationJsonResponse(final Response response, final int expectedStatus) {
		assertThat(response)
				.hasFieldOrPropertyWithValue("status", expectedStatus)
				.hasFieldOrProperty("entity");

		assertThat(response.getMediaType())
				.isNotNull()
				.satisfies(mediaType -> assertThat(mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)).isTrue());
	}

	private static AnkreuzkompetenzCreateRequest createRequest(final String floskelText) {
		final var dto = new AnkreuzkompetenzCreateRequest();
		dto.istASV = false;
		dto.idFach = 10L;
		dto.floskelText = floskelText;
		return dto;
	}

}
