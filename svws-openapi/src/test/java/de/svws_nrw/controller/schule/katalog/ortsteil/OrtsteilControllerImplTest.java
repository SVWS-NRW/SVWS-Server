package de.svws_nrw.controller.schule.katalog.ortsteil;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schule.katalog.ortsteil.OrtsteilCreateRequest;
import de.svws_nrw.service.schule.katalog.ortsteil.OrtsteilPatchRequest;
import de.svws_nrw.service.schule.katalog.ortsteil.OrtsteilService;
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
class OrtsteilControllerImplTest {

	@Mock
	private OrtsteilService ortsteilService;

	@InjectMocks
	private OrtsteilControllerImpl ortsteilControllerImpl;

	// -------------------------------------------------------------------------
	// getAll
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		when(ortsteilService.getAll()).thenReturn(List.of(new OrtsteilKatalogEintrag()));

		try (var result = ortsteilControllerImpl.getAll()) {
			assertApplicationJsonResponse(result, 200);
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("create | Erfolg")
	void create() {
		when(ortsteilService.create(any(OrtsteilCreateRequest.class))).thenReturn(new OrtsteilKatalogEintrag());

		try (var result = ortsteilControllerImpl.create(createRequest("Sieglar", 42L))) {
			assertApplicationJsonResponse(result, 201);
		}
	}

	@Test
	@DisplayName("create | Failed - Ortsteilname leer")
	void create_ortsteilnameInvalid() {
		assertThat(catchThrowable(() -> ortsteilControllerImpl.create(createRequest("", 42L))))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create | Failed - idOrt null")
	void create_idOrtNull() {
		assertThat(catchThrowable(() -> ortsteilControllerImpl.create(createRequest("Sieglar", null))))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch | Erfolg")
	void patch() {
		when(ortsteilService.patch(anyLong(), any(OrtsteilPatchRequest.class))).thenReturn(new OrtsteilKatalogEintrag());

		try (var result = ortsteilControllerImpl.patch(1L, createPatchWithOrtsteilname("Sieglar"))) {
			assertApplicationJsonResponse(result, 200);
		}
	}

	@Test
	@DisplayName("patch | Failed - Ortsteilname leer")
	void patch_ortsteilnameInvalid() {
		assertThat(catchThrowable(() -> ortsteilControllerImpl.patch(1L, createPatchWithOrtsteilname(""))))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	// -------------------------------------------------------------------------
	// delete
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("delete | Erfolg")
	void delete() {
		final List<Long> ids = List.of(1L);
		when(ortsteilService.delete(ids)).thenReturn(List.of(new SimpleOperationResponse()));

		try (var result = ortsteilControllerImpl.delete(ids)) {
			assertApplicationJsonResponse(result, 200);
		}
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private static void assertApplicationJsonResponse(final Response response, final int expectedStatus) {
		assertThat(response)
				.hasFieldOrPropertyWithValue("status", expectedStatus)
				.hasFieldOrProperty("entity");

		assertThat(response.getMediaType())
				.isNotNull()
				.satisfies(mt -> assertThat(mt.isCompatible(MediaType.APPLICATION_JSON_TYPE)).isTrue());
	}

	private OrtsteilCreateRequest createRequest(final String ortsteil, final Long idOrt) {
		final var dto = new OrtsteilCreateRequest();
		dto.ortsteil = ortsteil;
		dto.idOrt = idOrt;
		dto.sortierung = 1;
		dto.istSichtbar = true;
		dto.istAenderbar = true;
		return dto;
	}

	private OrtsteilPatchRequest createPatchWithOrtsteilname(final String ortsteil) {
		final var dto = new OrtsteilPatchRequest();
		dto.ortsteil = JsonNullable.of(ortsteil);
		return dto;
	}

}
