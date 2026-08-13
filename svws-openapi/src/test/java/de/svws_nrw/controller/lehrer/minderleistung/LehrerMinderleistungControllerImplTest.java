package de.svws_nrw.controller.lehrer.minderleistung;

import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.lehrer.minderleistung.LehrerMinderleistungBatchPatchRequest;
import de.svws_nrw.service.lehrer.minderleistung.LehrerMinderleistungCreateRequest;
import de.svws_nrw.service.lehrer.minderleistung.LehrerMinderleistungService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LehrerMinderleistungControllerImplTest {

	@Mock
	private LehrerMinderleistungService service;

	private LehrerMinderleistungControllerImpl cut;

	@BeforeEach
	void setUp() {
		cut = new LehrerMinderleistungControllerImpl(service);
	}

	@Test
	@DisplayName("get | Success - Delegiert an Service und erzeugt Response")
	void testGetSuccess() {
		final var result = createRestResult(42L, 10L);

		when(service.get(42L)).thenReturn(result);

		final var response = cut.get(42L);

		verify(service).get(42L);
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
	}

	@Test
	@DisplayName("getList | Success - Delegiert an Service und erzeugt Response")
	void testGetListSuccess() {
		final var firstResult = createRestResult(42L, 10L);
		final var secondResult = createRestResult(43L, 10L);
		final List<Long> request = List.of(42L, 43L);

		when(service.getList(request)).thenReturn(List.of(firstResult, secondResult));

		final var response = cut.getList(request);

		verify(service).getList(request);
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
	}

	@Test
	@DisplayName("create | Bad Request - Required idAbschnittsdaten null")
	void testCreateErrorIdAbschnittsdaten() {
		final var request = validCreateRequest();
		request.idAbschnittsdaten = null;

		assertThatThrownBy(() -> cut.create(request))
				.isInstanceOf(ApiOperationException.class)
				.satisfies(ex -> assertThat(((ApiOperationException) ex).getStatus()).isEqualTo(Response.Status.BAD_REQUEST));
	}

	@Test
	@DisplayName("create | Bad Request - Required idGrund null")
	void testCreateErrorIdGrund() {
		final var request = validCreateRequest();
		request.idGrund = null;

		assertThatThrownBy(() -> cut.create(request))
				.isInstanceOf(ApiOperationException.class)
				.satisfies(ex -> assertThat(((ApiOperationException) ex).getStatus()).isEqualTo(Response.Status.BAD_REQUEST));
	}

	@Test
	@DisplayName("create | Bad Request - Anzahl negativ")
	void testCreateErrorAnzahlNegativ() {
		final var request = validCreateRequest();
		request.anzahl = -1.0;

		assertThatThrownBy(() -> cut.create(request))
				.isInstanceOf(ApiOperationException.class)
				.extracting("status")
				.isEqualTo(BAD_REQUEST);
	}

	@Test
	@DisplayName("create | Success - Delegiert an Service und erzeugt Response")
	void testCreateSuccess() {
		final var request = validCreateRequest();
		final var serviceResult = createRestResult(7L, 10L);
		when(service.create(request)).thenReturn(serviceResult);

		final var response = cut.create(request);

		verify(service).create(request);
		assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
	}

	@Test
	@DisplayName("createMultiple | BadRequest - Invalides Objekt")
	void testCreateMultipleInvalidesObject() {
		final var valid = validCreateRequest();
		final var invalid = validCreateRequest();
		invalid.idAbschnittsdaten = null;
		final List<LehrerMinderleistungCreateRequest> requests = List.of(valid, invalid);

		assertThatThrownBy(() -> cut.createMultiple(requests))
				.isInstanceOf(ApiOperationException.class)
				.extracting("status")
				.isEqualTo(BAD_REQUEST);
	}

	@Test
	@DisplayName("createMultiple | Success - delegiert und erzeugt response")
	void testCreateMultipleSuccess() {
		final var request = validCreateRequest();
		final var serviceResult = List.of(createRestResult(1L, 10L));
		when(service.createMultiple(List.of(request))).thenReturn(serviceResult);

		final var response = cut.createMultiple(List.of(request));

		verify(service).createMultiple(List.of(request));
		assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
	}

	@Test
	@DisplayName("patch | BadRequest - Required ID is null")
	void testPatchErrorIdIsNull() {
		final var request = validPatchRequest();
		request.id = null;

		assertThatThrownBy(() -> cut.patch(request, 1L))
				.isInstanceOf(ApiOperationException.class)
				.extracting("status")
				.isEqualTo(BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | BadRequest - Übertragene Anzahl ist null")
	void testPatchErrorAnzahlIsNull() {
		final var request = validPatchRequest();
		request.anzahl = JsonNullable.of(null);

		assertThatThrownBy(() -> cut.patch(request, 1L))
				.isInstanceOf(ApiOperationException.class)
				.extracting("status")
				.isEqualTo(BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | BadRequest - Übertragener idGrund ist null")
	void testPatchErrorIdGrundIsNull() {
		final var request = validPatchRequest();
		request.idGrund = JsonNullable.of(null);

		assertThatThrownBy(() -> cut.patch(request, 1L))
				.isInstanceOf(ApiOperationException.class)
				.extracting("status")
				.isEqualTo(BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Success - Delegiert und erzeugt Response")
	void testPatchSuccess() {
		final var request = validPatchRequest();
		final var serviceResult = createRestResult(5L, 10L);
		when(service.patch(request, 1L)).thenReturn(serviceResult);

		final var response = cut.patch(request, 1L);

		verify(service).patch(request, 1L);
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
	}

	@Test
	@DisplayName("patchMultiple | Error - Enthält invalides Objekt")
	void testPatchMultipleInvalidesObject() {
		final var valid = validPatchRequest();
		final var invalid = validPatchRequest();
		invalid.id = null;
		final List<LehrerMinderleistungBatchPatchRequest> patches = List.of(valid, invalid);

		assertThatThrownBy(() -> cut.patchMultiple(patches))
				.isInstanceOf(ApiOperationException.class)
				.extracting("status")
				.isEqualTo(BAD_REQUEST);
	}

	@Test
	@DisplayName("patchMultiple | Success - Delegiert und erzeugt Response")
	void testPatchMultipleSuccess() {
		final var request = validPatchRequest();
		final var serviceResult = List.of(createRestResult(5L, 10L));
		when(service.patchMultiple(List.of(request))).thenReturn(serviceResult);

		final var response = cut.patchMultiple(List.of(request));

		verify(service).patchMultiple(List.of(request));
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
	}

	@Test
	@DisplayName("delete | Success - Delegiert und erzeugt Response")
	void testDeleteSuccess() {
		when(service.delete(5L)).thenReturn(successResponse(5L));

		final var response = cut.delete(5L);

		verify(service).delete(5L);
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
	}

	@Test
	@DisplayName("deleteMultiple | Success - Delegiert und erzeugt Response")
	void testDeleteMultipleSuccess() {
		when(service.deleteMultiple(List.of(1L, 2L))).thenReturn(List.of(successResponse(1L), successResponse(2L)));

		final var response = cut.deleteMultiple(List.of(1L, 2L));

		verify(service).deleteMultiple(List.of(1L, 2L));
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
	}

	private LehrerPersonalabschnittsdatenAnrechnungsstunden createRestResult(final long id, final long idAbschnittsdaten) {
		final var result = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
		result.id = id;
		result.idAbschnittsdaten = idAbschnittsdaten;
		return result;
	}

	private SimpleOperationResponse successResponse(final long id) {
		final var response = new SimpleOperationResponse();
		response.id = id;
		response.success = true;
		return response;
	}

	private LehrerMinderleistungCreateRequest validCreateRequest() {
		final var request = new LehrerMinderleistungCreateRequest();
		request.idAbschnittsdaten = 10L;
		request.idGrund = 1L;
		request.anzahl = 2.0;
		return request;
	}

	private LehrerMinderleistungBatchPatchRequest validPatchRequest() {
		final var request = new LehrerMinderleistungBatchPatchRequest();
		request.id = 5L;
		request.anzahl = JsonNullable.of(3.0);
		request.idGrund = JsonNullable.of(1L);
		return request;
	}
}
