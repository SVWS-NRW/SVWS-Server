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

import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.kurse.KursCreateRequest;
import de.svws_nrw.service.kurse.KursPatchRequest;
import de.svws_nrw.service.kurse.KursService;
import jakarta.ws.rs.core.Response;

/**
 * Testklasse für den {@link KursControllerImpl}
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests für den KursControllerImpl")
class KursControllerImplTest {

	@Mock
	private KursService kursService;

	private KursControllerImpl controller;

	@BeforeEach
	void setUp() {
		controller = new KursControllerImpl(kursService);
	}

	@Test
	@DisplayName("get | Erfolg")
	void get_success() {
		final var kurs = mock(KursDaten.class);
		when(kursService.getById(1L)).thenReturn(kurs);

		assertThat(controller.get(1L)).isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(kurs);

		verify(kursService).getById(1L);
	}

	@Test
	@DisplayName("getListBySchuljahresabschnitt | Erfolg mit Abschnitt")
	void getListBySchuljahresabschnitt_success() {
		final var kurse = List.of(mock(KursDaten.class), mock(KursDaten.class));
		when(kursService.getListBySchuljahresabschnitt(10L, true)).thenReturn(kurse);

		assertThat(controller.getListBySchuljahresabschnitt(10L)).hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(kurse);

		verify(kursService).getListBySchuljahresabschnitt(10L, true);
	}

	@Test
	@DisplayName("getListBySchuljahresabschnitt | Ohne Abschnitt werden alle Kurse angefragt")
	void getListBySchuljahresabschnitt_abschnittNull() {
		when(kursService.getListBySchuljahresabschnitt(null, true)).thenReturn(List.of());

		assertThat(controller.getListBySchuljahresabschnitt(null)).hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(List.of());

		verify(kursService).getListBySchuljahresabschnitt(null, true);
	}

	private static KursCreateRequest gueltigerCreateRequest() {
		final var request = new KursCreateRequest();
		request.idSchuljahresabschnitt = 10L;
		request.kuerzel = "D-GK1";
		request.idFach = 3L;
		request.kursartAllg = "GK";
		return request;
	}

	@Test
	@DisplayName("create | Erfolg liefert 201 mit den Kursdaten")
	void create_success() {
		final var request = gueltigerCreateRequest();
		final var kurs = mock(KursDaten.class);
		when(kursService.create(request)).thenReturn(kurs);

		assertThat(controller.create(request)).hasFieldOrPropertyWithValue("status", Response.Status.CREATED.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(kurs);

		verify(kursService).create(request);
	}

	@Test
	@DisplayName("create | Ein ungültiger Request führt zu 400 ohne Aufruf des Services")
	void create_validationError() {
		final var request = new KursCreateRequest();

		assertThatThrownBy(() -> controller.create(request)).isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

		verify(kursService, never()).create(any(KursCreateRequest.class));
	}

	@Test
	@DisplayName("patch | Erfolg liefert 200 mit dem geänderten Kurs")
	void patch_success() {
		final var request = new KursPatchRequest();
		request.sortierung = JsonNullable.of(5);
		final var kurs = mock(KursDaten.class);
		when(kursService.patch(1L, request)).thenReturn(kurs);

		assertThat(controller.patch(1L, request)).hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(kurs);

		verify(kursService).patch(1L, request);
	}

	@Test
	@DisplayName("patch | Ein ungültiger Request führt zu 400 ohne Aufruf des Services")
	void patch_validationError() {
		final var request = new KursPatchRequest();
		request.kuerzel = JsonNullable.of(null);

		assertThatThrownBy(() -> controller.patch(1L, request)).isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

		verify(kursService, never()).patch(any(Long.class), any(KursPatchRequest.class));
	}

	@Test
	@DisplayName("delete | Erfolg liefert 200 mit dem Ergebnis je Kurs")
	void delete_success() {
		final var ergebnis = List.of(SimpleOperationResponse.ofSuccess(1L), SimpleOperationResponse.ofError(2L, "nicht gefunden"));
		when(kursService.delete(List.of(1L, 2L))).thenReturn(ergebnis);

		assertThat(controller.delete(List.of(1L, 2L))).hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(ergebnis);

		verify(kursService).delete(List.of(1L, 2L));
	}

}
