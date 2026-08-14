package de.svws_nrw.controller.schule.katalog.ort;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schule.katalog.ort.OrtCreateRequest;
import de.svws_nrw.service.schule.katalog.ort.OrtPatchRequest;
import de.svws_nrw.service.schule.katalog.ort.OrtService;
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
class OrtControllerImplTest {

	@Mock
	private OrtService ortService;

	@InjectMocks
	private OrtControllerImpl ortControllerImpl;

	// -------------------------------------------------------------------------
	// getAll
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		when(ortService.getAll()).thenReturn(List.of(new OrtKatalogEintrag()));

		try (var result = ortControllerImpl.getAll()) {
			assertApplicationJsonResponse(result, 200);
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("create | Erfolg")
	void create() {
		when(ortService.create(any(OrtCreateRequest.class))).thenReturn(new OrtKatalogEintrag());

		try (var result = ortControllerImpl.create(createRequest("Troisdorf", "53840"))) {
			assertApplicationJsonResponse(result, 201);
		}
	}

	@Test
	@DisplayName("create | Failed - Ortsname leer")
	void create_ortsnameInvalid() {
		assertThat(catchThrowable(() -> ortControllerImpl.create(createRequest("", "53840"))))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create | Failed - PLZ leer")
	void create_plzInvalid() {
		assertThat(catchThrowable(() -> ortControllerImpl.create(createRequest("Troisdorf", ""))))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch | Erfolg")
	void patch() {
		when(ortService.patch(anyLong(), any(OrtPatchRequest.class))).thenReturn(new OrtKatalogEintrag());

		try (var result = ortControllerImpl.patch(1L, createPatchWithOrtsname("Troisdorf"))) {
			assertApplicationJsonResponse(result, 200);
		}
	}

	@Test
	@DisplayName("patch | Failed - Ortsname leer")
	void patch_ortsnameInvalid() {
		assertThat(catchThrowable(() -> ortControllerImpl.patch(1L, createPatchWithOrtsname(""))))
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
		when(ortService.delete(ids)).thenReturn(List.of(new SimpleOperationResponse()));

		try (var result = ortControllerImpl.delete(ids)) {
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

	private OrtCreateRequest createRequest(final String ortsname, final String plz) {
		final var dto = new OrtCreateRequest();
		dto.ortsname = ortsname;
		dto.plz = plz;
		dto.sortierung = 1;
		dto.istSichtbar = true;
		dto.istAenderbar = true;
		return dto;
	}

	private OrtPatchRequest createPatchWithOrtsname(final String ortsname) {
		final var dto = new OrtPatchRequest();
		dto.ortsname = JsonNullable.of(ortsname);
		return dto;
	}

}
