package de.svws_nrw.controller.schule.katalog.ankreuzkompetenz;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.AnkreuzkompetenzJahrgangszuordnung;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzJahrgangCreateRequest;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzJahrgangService;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnkreuzkompetenzJahrgangControllerImplTest {

	@Mock
	private AnkreuzkompetenzJahrgangService ankreuzkompetenzJahrgangService;

	@InjectMocks
	private AnkreuzkompetenzJahrgangControllerImpl ankreuzkompetenzJahrgangControllerImpl;

	// -------------------------------------------------------------------------
	// createMultiple
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("createMultiple | Erfolg")
	void createMultiple() {
		when(ankreuzkompetenzJahrgangService.createMultiple(anyList()))
				.thenReturn(List.of(new AnkreuzkompetenzJahrgangszuordnung()));

		try (var result = ankreuzkompetenzJahrgangControllerImpl.createMultiple(List.of(createRequest(1L, 2L)))) {
			assertApplicationJsonResponse(result, 201);
		}
	}

	@Test
	@DisplayName("createMultiple | Failed - idAnkreuzkompetenz fehlt")
	void createMultiple_idAnkreuzkompetenzInvalid() {
		final var request = createRequest(null, 2L);

		assertThat(catchThrowable(() ->
				ankreuzkompetenzJahrgangControllerImpl.createMultiple(List.of(request))))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("createMultiple | Failed - idJahrgang fehlt")
	void createMultiple_idJahrgangInvalid() {
		final var request = createRequest(1L, null);

		assertThat(catchThrowable(() ->
				ankreuzkompetenzJahrgangControllerImpl.createMultiple(List.of(request))))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("createMultiple | Failed - leere Liste")
	void createMultiple_emptyList() {
		when(ankreuzkompetenzJahrgangService.createMultiple(anyList()))
				.thenReturn(List.of());

		try (var result = ankreuzkompetenzJahrgangControllerImpl.createMultiple(List.of())) {
			assertApplicationJsonResponse(result, 201);
		}
	}

	// -------------------------------------------------------------------------
	// deleteMultiple
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("deleteMultiple | Erfolg")
	void deleteMultiple() {
		final List<Long> ids = List.of(1L);

		when(ankreuzkompetenzJahrgangService.delete(ids))
				.thenReturn(List.of(new SimpleOperationResponse()));

		try (var result = ankreuzkompetenzJahrgangControllerImpl.deleteMultiple(ids)) {
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

	private AnkreuzkompetenzJahrgangCreateRequest createRequest(final Long idAnkreuzkompetenz, final Long idJahrgang) {
		final var request = new AnkreuzkompetenzJahrgangCreateRequest();
		request.idAnkreuzkompetenz = idAnkreuzkompetenz;
		request.idJahrgang = idJahrgang;
		return request;
	}

}
