package de.svws_nrw.controller.schueler.leistungsdaten;

import java.util.List;

import de.svws_nrw.asd.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schueler.leistungsdaten.SchuelerLeistungsdatenCreateRequest;
import de.svws_nrw.service.schueler.leistungsdaten.SchuelerLeistungsdatenPatchRequest;
import de.svws_nrw.service.schueler.leistungsdaten.SchuelerLeistungsdatenService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchuelerLeistungsdatenControllerImplTest {

	@Mock
	private SchuelerLeistungsdatenService service;

	private SchuelerLeistungsdatenControllerImpl controller;

	@BeforeEach
	void setUp() {
		controller = new SchuelerLeistungsdatenControllerImpl(service);
	}

	private static SchuelerLeistungsdatenCreateRequest createRequest() {
		final var dto = new SchuelerLeistungsdatenCreateRequest();
		dto.lernabschnittID = 10L;
		dto.fachID = 20L;
		return dto;
	}

	@Test
	@DisplayName("get | Erfolg")
	void get_erfolg() {
		final var daten = mock(SchuelerLeistungsdaten.class);
		when(service.getById(1L)).thenReturn(daten);

		assertThat(controller.get(1L))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(daten);

		verify(service, times(1)).getById(1L);
	}

	@Test
	@DisplayName("get | Nicht gefunden")
	void get_notFound() {
		when(service.getById(99L)).thenThrow(new ApiOperationException(Response.Status.NOT_FOUND, "nicht gefunden"));

		assertThatThrownBy(() -> controller.get(99L))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("create | Erfolg - CREATED mit den angelegten Leistungsdaten")
	void create_erfolg() {
		final var dto = createRequest();
		final var daten = mock(SchuelerLeistungsdaten.class);
		when(service.create(dto)).thenReturn(daten);

		assertThat(controller.create(dto))
				.hasFieldOrPropertyWithValue("status", Response.Status.CREATED.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(daten);

		verify(service, times(1)).create(dto);
	}

	@Test
	@DisplayName("create | Pflichtfeld fehlt - BAD_REQUEST, der Service wird nicht aufgerufen")
	void create_validierungFehlgeschlagen() {
		final var dto = createRequest();
		dto.fachID = null;

		assertThatThrownBy(() -> controller.create(dto))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
				.hasMessageContaining("fachID");

		verifyNoInteractions(service);
	}

	@Test
	@DisplayName("patch | Erfolg - OK mit den aktualisierten Leistungsdaten")
	void patch_erfolg() {
		final var dto = new SchuelerLeistungsdatenPatchRequest();
		dto.note = JsonNullable.of("2");
		final var daten = mock(SchuelerLeistungsdaten.class);
		when(service.patch(1L, dto)).thenReturn(daten);

		assertThat(controller.patch(1L, dto))
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(daten);

		verify(service, times(1)).patch(1L, dto);
	}

	@Test
	@DisplayName("patch | Ungültiger Wert - BAD_REQUEST, der Service wird nicht aufgerufen")
	void patch_validierungFehlgeschlagen() {
		final var dto = new SchuelerLeistungsdatenPatchRequest();
		dto.fehlstundenGesamt = JsonNullable.of(-1);

		assertThatThrownBy(() -> controller.patch(1L, dto))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
				.hasMessageContaining("fehlstundenGesamt");

		verifyNoInteractions(service);
	}

	@Test
	@DisplayName("patch | Keine Berechtigung")
	void patch_forbidden() {
		final var dto = new SchuelerLeistungsdatenPatchRequest();
		when(service.patch(1L, dto)).thenThrow(new ApiOperationException(Response.Status.FORBIDDEN, "verboten"));

		assertThatThrownBy(() -> controller.patch(1L, dto))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.FORBIDDEN);
	}

	@Test
	@DisplayName("deleteMultiple | Erfolg - OK mit den Ergebnissen der Löschvorgänge")
	void deleteMultiple_erfolg() {
		final var ids = List.of(1L, 2L);
		final var responses = List.of(SimpleOperationResponse.ofSuccess(1L), SimpleOperationResponse.ofError(2L, "nicht gefunden"));
		when(service.deleteMultiple(ids)).thenReturn(responses);

		assertThat(controller.deleteMultiple(ids))
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(responses);

		verify(service, times(1)).deleteMultiple(ids);
	}

	@Test
	@DisplayName("deleteMultiple | Keine Berechtigung")
	void deleteMultiple_forbidden() {
		final var ids = List.of(1L);
		when(service.deleteMultiple(ids)).thenThrow(new ApiOperationException(Response.Status.FORBIDDEN, "verboten"));

		assertThatThrownBy(() -> controller.deleteMultiple(ids))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.FORBIDDEN);
	}

}
