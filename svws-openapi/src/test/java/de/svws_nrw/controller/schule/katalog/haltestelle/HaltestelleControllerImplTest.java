package de.svws_nrw.controller.schule.katalog.haltestelle;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Haltestelle;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schule.katalog.haltestelle.HaltestelleCreateRequest;
import de.svws_nrw.service.schule.katalog.haltestelle.HaltestellePatchRequest;
import de.svws_nrw.service.schule.katalog.haltestelle.HaltestelleService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HaltestelleControllerImplTest {

	@Mock
	private HaltestelleService haltestelleService;

	@InjectMocks
	private HaltestelleControllerImpl haltestelleControllerImpl;

	// -------------------------------------------------------------------------
	// getAll
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		when(haltestelleService.getAll())
				.thenReturn(List.of(new Haltestelle()));

		try (var result = haltestelleControllerImpl.getAll()) {
			assertApplicationJsonResponse(result, 200);
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("create | Erfolg")
	void create() {
		when(haltestelleService.create(any(HaltestelleCreateRequest.class)))
				.thenReturn(new Haltestelle());

		try (var result = haltestelleControllerImpl.create(createRequest("Troisdorf"))) {
			assertApplicationJsonResponse(result, 201);
		}
	}

	@Test
	@DisplayName("create | Erfolg - nur Bezeichnung gesetzt")
	void create_nurBezeichnung() {
		final var request = new HaltestelleCreateRequest();
		request.bezeichnung = "Troisdorf";

		when(haltestelleService.create(any(HaltestelleCreateRequest.class)))
				.thenReturn(new Haltestelle());

		try (var result = haltestelleControllerImpl.create(request)) {
			assertApplicationJsonResponse(result, 201);
		}
	}

	@Test
	@DisplayName("create | Failed - Bezeichnung leer")
	void create_bezeichnungLeer() {
		assertThat(catchThrowable(() ->
				haltestelleControllerImpl.create(createRequest(""))))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create | Failed - Bezeichnung fehlt")
	void create_bezeichnungFehlt() {
		assertThat(catchThrowable(() ->
				haltestelleControllerImpl.create(createRequest(null))))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create | Failed - Bezeichnung zu lang")
	void create_bezeichnungZuLang() {
		assertThat(catchThrowable(() ->
				haltestelleControllerImpl.create(createRequest("x".repeat(31)))))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create | Failed - Bezeichnung mit führenden Leerzeichen")
	void create_bezeichnungMitLeerzeichen() {
		assertThat(catchThrowable(() ->
				haltestelleControllerImpl.create(createRequest(" Troisdorf"))))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch | Erfolg")
	void patch() {
		when(haltestelleService.patch(anyLong(), any(HaltestellePatchRequest.class)))
				.thenReturn(new Haltestelle());

		try (var result = haltestelleControllerImpl.patch(1L, createPatchWithBezeichnung("Bonn"))) {
			assertApplicationJsonResponse(result, 200);
		}
	}

	@Test
	@DisplayName("patch | Failed - Bezeichnung leer")
	void patch_bezeichnungLeer() {
		assertThat(catchThrowable(() ->
				haltestelleControllerImpl.patch(1L, createPatchWithBezeichnung(""))))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Failed - Sortierung null")
	void patch_sortierungNull() {
		final var patch = new HaltestellePatchRequest();
		patch.sortierung = JsonNullable.of(null);

		assertThat(catchThrowable(() ->
				haltestelleControllerImpl.patch(1L, patch)))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Erfolg ohne gesetzte Felder")
	void patch_undefined() {
		when(haltestelleService.patch(anyLong(), any(HaltestellePatchRequest.class)))
				.thenReturn(new Haltestelle());

		try (var result = haltestelleControllerImpl.patch(1L, new HaltestellePatchRequest())) {
			assertApplicationJsonResponse(result, 200);
		}
	}

	// -------------------------------------------------------------------------
	// delete
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("delete | Erfolg")
	void delete() {
		final List<Long> ids = List.of(1L);

		when(haltestelleService.delete(ids))
				.thenReturn(List.of(new SimpleOperationResponse()));

		try (var result = haltestelleControllerImpl.delete(ids)) {
			assertApplicationJsonResponse(result, 200);
		}
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private static void assertApplicationJsonResponse(
			final Response response,
			final int expectedStatus) {
		assertThat(response)
				.hasFieldOrPropertyWithValue("status", expectedStatus)
				.hasFieldOrProperty("entity");

		assertThat(response.getMediaType())
				.isNotNull()
				.satisfies(mediaType ->
						assertThat(mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE))
								.isTrue());
	}

	private HaltestelleCreateRequest createRequest(final String bezeichnung) {
		final var dto = new HaltestelleCreateRequest();
		dto.bezeichnung = bezeichnung;
		dto.entfernungSchule = 7.5;
		dto.sortierung = 1;
		dto.istSichtbar = true;
		return dto;
	}

	private HaltestellePatchRequest createPatchWithBezeichnung(final String bezeichnung) {
		final var dto = new HaltestellePatchRequest();
		dto.bezeichnung = JsonNullable.of(bezeichnung);
		return dto;
	}

}
