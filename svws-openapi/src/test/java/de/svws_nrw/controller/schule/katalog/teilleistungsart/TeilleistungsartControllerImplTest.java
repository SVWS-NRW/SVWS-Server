package de.svws_nrw.controller.schule.katalog.teilleistungsart;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.kataloge.Teilleistungsart;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schule.katalog.teilleistungsart.TeilleistungsartCreateRequest;
import de.svws_nrw.service.schule.katalog.teilleistungsart.TeilleistungsartPatchRequest;
import de.svws_nrw.service.schule.katalog.teilleistungsart.TeilleistungsartService;
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
class TeilleistungsartControllerImplTest {

	@Mock
	private TeilleistungsartService teilleistungsartService;
	@InjectMocks
	private TeilleistungsartControllerImpl teilLeistungsartControllerImpl;

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		when(teilleistungsartService.getAll()).thenReturn(List.of(new Teilleistungsart()));

		final var resultResponse = teilLeistungsartControllerImpl.getAll();

		assertApplicationJsonResponse(resultResponse, 200);
	}

	@Test
	@DisplayName("create | Erfolg")
	void create() {
		when(teilleistungsartService.create(any(TeilleistungsartCreateRequest.class))).thenReturn(new Teilleistungsart());

		final var resultResponse = teilLeistungsartControllerImpl.create(createRest("valid", 1));

		assertApplicationJsonResponse(resultResponse, 201);

	}

	@Test
	@DisplayName("create | Failed - Bezeichnung empty")
	void testCreateFailedBezeichnungInvalid() {
		final var restInput = createRest("", 42);

		final var throwable = catchThrowable(() -> teilLeistungsartControllerImpl.create(restInput));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

	}

	@Test
	@DisplayName("create | Failed - Sortierung invalid")
	void testCreateFailedSortierungInvalid() {
		final var restInput = createRest("bezeichnung", 32001);

		final var throwable = catchThrowable(() -> teilLeistungsartControllerImpl.create(restInput));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("delete | Erfolg")
	void testDeleteSuccess() {
		final List<Long> idsToDelete = List.of(1L);
		when(teilleistungsartService.delete(idsToDelete)).thenReturn(List.of(new SimpleOperationResponse()));

		final var resultResponse = teilLeistungsartControllerImpl.delete(idsToDelete);

		assertApplicationJsonResponse(resultResponse, 200);
	}

	@Test
	@DisplayName("patch | Validation")
	void testPatchValidation() {
		final var throwable = catchThrowable(() -> teilLeistungsartControllerImpl.patch(1L, createPatch(32000, null)));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Validation")
	void patch() {
		when(teilleistungsartService.patch(anyLong(), any(TeilleistungsartPatchRequest.class))).thenReturn(new Teilleistungsart());

		final var resultResponse = teilLeistungsartControllerImpl.patch(1L, createPatch(32000, "Bezeichnung"));

		assertApplicationJsonResponse(resultResponse, 200);
	}

	private static void assertApplicationJsonResponse(final Response resultResponse, final int value) {
		assertThat(resultResponse)
				.hasFieldOrPropertyWithValue("status", value)
				.hasFieldOrProperty("entity");

		assertThat(resultResponse.getMediaType())
				.isNotNull()
				.satisfies(mt -> assertThat(mt.isCompatible(MediaType.APPLICATION_JSON_TYPE)).isTrue()
				);
	}

	private TeilleistungsartCreateRequest createRest(final String bezeichnung, final int sortierung) {
		final var restInput = new TeilleistungsartCreateRequest();
		restInput.bezeichnung = bezeichnung;
		restInput.istSichtbar = true;
		restInput.sortierung = sortierung;

		return restInput;
	}

	private static TeilleistungsartPatchRequest createPatch(final int patchSortierung, final String patchBezeichnung) {
		final var patchRequest = new TeilleistungsartPatchRequest();
		patchRequest.sortierung = JsonNullable.of(patchSortierung);
		patchRequest.bezeichnung = JsonNullable.of(patchBezeichnung);

		return patchRequest;
	}

}
