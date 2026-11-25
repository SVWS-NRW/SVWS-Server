package de.svws_nrw.controller.kurse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.asd.data.kurse.KursLehrer;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.kurse.KurslehrerCreateRequest;
import de.svws_nrw.service.kurse.KurslehrerPatchRequest;
import de.svws_nrw.service.kurse.KurslehrerService;
import jakarta.ws.rs.core.Response;

/**
 * Testklasse für den {@link KurslehrerControllerImpl}
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests für den KurslehrerControllerImpl")
class KurslehrerControllerImplTest {

	@Mock
	private KurslehrerService service;

	private KurslehrerControllerImpl controller;

	@BeforeEach
	void setUp() {
		controller = new KurslehrerControllerImpl(service);
	}

	private static KurslehrerCreateRequest gueltigerKurslehrerCreateRequest() {
		final var request = new KurslehrerCreateRequest();
		request.idLehrer = 10L;
		request.wochenstundenLehrer = 2.0;
		return request;
	}

	@Test
	@DisplayName("getListByIdKurs | Erfolg liefert 200 mit den Zusatzkräften")
	void getListByIdKurs_success() {
		final var kurslehrer = List.of(mock(KursLehrer.class), mock(KursLehrer.class));
		when(service.getListByIdKurs(1L)).thenReturn(kurslehrer);

		assertThat(controller.getListByIdKurs(1L)).hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(kurslehrer);

		verify(service).getListByIdKurs(1L);
	}

	@Test
	@DisplayName("create | Erfolg liefert 201 mit der angelegten Zusatzkraft")
	void create_success() {
		final var request = gueltigerKurslehrerCreateRequest();
		final var kurslehrer = mock(KursLehrer.class);
		when(service.create(1L, request)).thenReturn(kurslehrer);

		assertThat(controller.create(1L, request)).hasFieldOrPropertyWithValue("status", Response.Status.CREATED.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(kurslehrer);

		verify(service).create(1L, request);
	}

	@Test
	@DisplayName("create | Ein ungültiger Request führt zu 400 ohne Aufruf des Services")
	void create_validationError() {
		final var request = new KurslehrerCreateRequest();

		assertThatThrownBy(() -> controller.create(1L, request)).isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

		verify(service, never()).create(any(Long.class), any(KurslehrerCreateRequest.class));
	}

	@Test
	@DisplayName("create | Negative Wochenstunden führen zu 400 ohne Aufruf des Services")
	void create_negativeWochenstunden() {
		final var request = gueltigerKurslehrerCreateRequest();
		request.wochenstundenLehrer = -1.0;

		assertThatThrownBy(() -> controller.create(1L, request)).isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

		verify(service, never()).create(any(Long.class), any(KurslehrerCreateRequest.class));
	}

	@Test
	@DisplayName("patch | Erfolg liefert 200 mit der geänderten Zusatzkraft")
	void patch_success() {
		final var request = new KurslehrerPatchRequest();
		request.wochenstundenLehrer = JsonNullable.of(3.0);
		final var kurslehrer = mock(KursLehrer.class);
		when(service.patch(1L, 10L, request)).thenReturn(kurslehrer);

		assertThat(controller.patch(1L, 10L, request)).hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(kurslehrer);

		verify(service).patch(1L, 10L, request);
	}

	@Test
	@DisplayName("patch | Ein gesetzter null-Wert für die Wochenstunden führt zu 400 ohne Aufruf des Services")
	void patch_nullWochenstunden() {
		final var request = new KurslehrerPatchRequest();
		request.wochenstundenLehrer = JsonNullable.of(null);

		assertThatThrownBy(() -> controller.patch(1L, 10L, request)).isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

		verify(service, never()).patch(any(Long.class), any(Long.class), any(KurslehrerPatchRequest.class));
	}

	@Test
	@DisplayName("patch | Ein ungültiger Request führt zu 400 ohne Aufruf des Services")
	void patch_validationError() {
		final var request = new KurslehrerPatchRequest();
		request.wochenstundenLehrer = JsonNullable.of(-1.0);

		assertThatThrownBy(() -> controller.patch(1L, 10L, request)).isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

		verify(service, never()).patch(any(Long.class), any(Long.class), any(KurslehrerPatchRequest.class));
	}

	@Test
	@DisplayName("delete | Erfolg liefert 200 mit dem Ergebnis je Lehrer")
	void delete_success() {
		final var ergebnis = List.of(SimpleOperationResponse.ofSuccess(10L), SimpleOperationResponse.ofError(11L, "nicht gefunden"));
		when(service.delete(1L, List.of(10L, 11L))).thenReturn(ergebnis);

		assertThat(controller.delete(1L, List.of(10L, 11L))).hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(ergebnis);

		verify(service).delete(1L, List.of(10L, 11L));
	}

}
