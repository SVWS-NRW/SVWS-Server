package de.svws_nrw.controller.schule.katalog.religion;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schule.katalog.religion.ReligionCreateRequest;
import de.svws_nrw.service.schule.katalog.religion.ReligionPatchRequest;
import de.svws_nrw.service.schule.katalog.religion.ReligionService;
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
class ReligionControllerImplTest {

	@Mock
	private ReligionService religionService;

	@InjectMocks
	private ReligionControllerImpl religionControllerImpl;

	// -------------------------------------------------------------------------
	// getAll
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		when(religionService.getAll())
				.thenReturn(List.of(new ReligionEintrag()));

		try (var result = religionControllerImpl.getAll()) {
			assertApplicationJsonResponse(result, 200);
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("create | Erfolg")
	void create() {
		when(religionService.create(any(ReligionCreateRequest.class)))
				.thenReturn(new ReligionEintrag());

		try (var result = religionControllerImpl.create(createRequest("röm.-kath."))) {
			assertApplicationJsonResponse(result, 201);
		}
	}

	@Test
	@DisplayName("create | Failed - Bezeichnung leer")
	void create_bezeichnungInvalid() {
		assertThat(catchThrowable(() ->
				religionControllerImpl.create(createRequest(""))))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create | Failed - idReligion fehlt")
	void create_idReligionInvalid() {
		final var request = createRequest("röm.-kath.");
		request.idReligion = null;

		assertThat(catchThrowable(() ->
				religionControllerImpl.create(request)))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("create | Failed - Sortierung fehlt")
	void create_sortierungInvalid() {
		final var request = createRequest("röm.-kath.");
		request.sortierung = null;

		assertThat(catchThrowable(() ->
				religionControllerImpl.create(request)))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch | Erfolg")
	void patch() {
		when(religionService.patch(anyLong(), any(ReligionPatchRequest.class)))
				.thenReturn(new ReligionEintrag());

		try (var result = religionControllerImpl.patch(
				1L,
				createPatchWithBezeichnung("röm.-kath."))) {
			assertApplicationJsonResponse(result, 200);
		}
	}

	@Test
	@DisplayName("patch | Failed - Bezeichnung leer")
	void patch_bezeichnungInvalid() {
		assertThat(catchThrowable(() ->
				religionControllerImpl.patch(
						1L,
						createPatchWithBezeichnung(""))))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Erfolg ohne gesetzte Felder")
	void patch_undefined() {
		when(religionService.patch(anyLong(), any(ReligionPatchRequest.class)))
				.thenReturn(new ReligionEintrag());

		try (var result = religionControllerImpl.patch(1L, new ReligionPatchRequest())) {
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

		when(religionService.delete(ids))
				.thenReturn(List.of(new SimpleOperationResponse()));

		try (var result = religionControllerImpl.delete(ids)) {
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

	private ReligionCreateRequest createRequest(final String bezeichnung) {
		final var dto = new ReligionCreateRequest();
		dto.bezeichnung = bezeichnung;
		dto.bezeichnungZeugnis = "katholisch";
		dto.idReligion = 1000L;
		dto.sortierung = 1;
		dto.istSichtbar = true;
		return dto;
	}

	private ReligionPatchRequest createPatchWithBezeichnung(final String bezeichnung) {
		final var dto = new ReligionPatchRequest();
		dto.bezeichnung = JsonNullable.of(bezeichnung);
		return dto;
	}

}
