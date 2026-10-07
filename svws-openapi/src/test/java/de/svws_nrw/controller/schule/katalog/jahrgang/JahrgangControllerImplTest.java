package de.svws_nrw.controller.schule.katalog.jahrgang;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schule.katalog.jahrgang.JahrgangCreateRequest;
import de.svws_nrw.service.schule.katalog.jahrgang.JahrgangPatchRequest;
import de.svws_nrw.service.schule.katalog.jahrgang.JahrgangService;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JahrgangControllerImplTest {

	@Mock
	private JahrgangService jahrgangService;

	@InjectMocks
	private JahrgangControllerImpl jahrgangControllerImpl;

	// -------------------------------------------------------------------------
	// getAll
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		final var daten = List.of(new JahrgangsDaten());
		when(jahrgangService.getAll()).thenReturn(daten);

		try (var result = jahrgangControllerImpl.getAll()) {
			assertApplicationJsonResponse(result, 200);
			assertThat(result.getEntity()).isEqualTo(daten);
		}
	}

	@Test
	@DisplayName("getAll | leere Liste")
	void getAll_leer() {
		when(jahrgangService.getAll()).thenReturn(List.of());

		try (var result = jahrgangControllerImpl.getAll()) {
			assertApplicationJsonResponse(result, 200);
		}
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("create | Erfolg")
	void create() {
		final var request = new JahrgangCreateRequest();
		request.kuerzel = "05";
		request.bezeichnung = "Jahrgang 5";
		request.idJahrgang = 5000000L;

		final var daten = new JahrgangsDaten();
		daten.id = 7L;
		when(jahrgangService.create(request)).thenReturn(daten);

		try (var result = jahrgangControllerImpl.create(request)) {
			assertApplicationJsonResponse(result, 201);
			assertThat(result.getEntity()).isSameAs(daten);
		}
	}

	@Test
	@DisplayName("create | Wirft 400, wenn die Bean-Validation fehlschlägt")
	void create_ungueltigerRequest() {
		final var request = new JahrgangCreateRequest();

		assertThatThrownBy(() -> jahrgangControllerImpl.create(request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

		verify(jahrgangService, never()).create(any());
	}

	// -------------------------------------------------------------------------
	// create
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch | Erfolg")
	void patch_erfolg() {
		final var request = new JahrgangPatchRequest();
		request.kuerzel = JsonNullable.of("05");
		final var erwartet = new JahrgangsDaten();
		when(jahrgangService.patch(1L, request)).thenReturn(erwartet);

		final var response = jahrgangControllerImpl.patch(1L, request);

		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getEntity()).isSameAs(erwartet);
	}

	@Test
	@DisplayName("patch | Wirft 400, wenn die Bean-Validation fehlschlägt")
	void patch_beanValidationFehlgeschlagen() {
		final var request = new JahrgangPatchRequest();
		request.kuerzel = JsonNullable.of("   ");

		assertThatThrownBy(() -> jahrgangControllerImpl.patch(1L, request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

		verify(jahrgangService, never()).patch(anyLong(), any());
	}

	// -------------------------------------------------------------------------
	// delete
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("delete | Erfolg")
	void delete() {
		final List<Long> ids = List.of(1L);
		final var antworten = List.of(SimpleOperationResponse.ofSuccess(1L));

		when(jahrgangService.delete(ids)).thenReturn(antworten);

		try (var result = jahrgangControllerImpl.delete(ids)) {
			assertApplicationJsonResponse(result, 200);
			assertThat(result.getEntity()).isSameAs(antworten);
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

}
