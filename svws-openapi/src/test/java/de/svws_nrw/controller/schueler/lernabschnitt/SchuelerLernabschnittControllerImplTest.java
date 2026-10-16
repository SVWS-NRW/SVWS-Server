package de.svws_nrw.controller.schueler.lernabschnitt;

import java.util.List;

import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittService;
import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittBemerkungenPatchRequest;
import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittsdatenPatchRequest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
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
class SchuelerLernabschnittControllerImplTest {

	@Mock
	private SchuelerLernabschnittService service;

	private SchuelerLernabschnittControllerImpl controller;

	@BeforeEach
	void setUp() {
		controller = new SchuelerLernabschnittControllerImpl(service);
	}

	@Test
	@DisplayName("get | Erfolg")
	void get_erfolg() {
		final var id = 1L;
		final var daten = mock(SchuelerLernabschnittsdaten.class);

		when(service.get(id)).thenReturn(daten);

		assertThat(controller.get(id))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isNotNull()
				.isEqualTo(daten);

		verify(service, times(1)).get(id);
	}

	@Test
	@DisplayName("get | Nicht gefunden")
	void get_notFound() {
		final var id = 99L;

		when(service.get(id)).thenThrow(new ApiOperationException(Response.Status.NOT_FOUND,
				"Keine Lernabschnittsdaten zur Abschnitt-ID 99 gefunden."));

		assertThatThrownBy(() -> controller.get(id))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND)
				.hasMessage("Keine Lernabschnittsdaten zur Abschnitt-ID 99 gefunden.");

		verify(service, times(1)).get(id);
	}

	@Test
	@DisplayName("getListBySchuelerAndSchuljahresabschnitt | Erfolg")
	void getList_erfolg() {
		final var idSchueler = 10L;
		final var idSchuljahresabschnitt = 20L;
		final var daten = List.of(mock(SchuelerLernabschnittsdaten.class), mock(SchuelerLernabschnittsdaten.class));

		when(service.getListBySchuelerAndSchuljahresabschnitt(idSchueler, idSchuljahresabschnitt)).thenReturn(daten);

		assertThat(controller.getListBySchuelerAndSchuljahresabschnitt(idSchueler, idSchuljahresabschnitt))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(daten);

		verify(service, times(1)).getListBySchuelerAndSchuljahresabschnitt(idSchueler, idSchuljahresabschnitt);
	}

	@Test
	@DisplayName("patch | Erfolg - NO_CONTENT")
	void patch_erfolg() {
		final var id = 1L;
		final var dto = new SchuelerLernabschnittsdatenPatchRequest();
		dto.fehlstundenGesamt = JsonNullable.of(10);

		assertThat(controller.patch(id, dto))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.NO_CONTENT.getStatusCode())
				.extracting(Response::getEntity)
				.isNull();

		verify(service, times(1)).patch(id, dto);
	}

	@Test
	@DisplayName("patch | BeanValidation - BAD_REQUEST bei negativen Fehlstunden, Service wird nicht aufgerufen")
	void patch_validierungFehlstundenNegativ() {
		final var dto = new SchuelerLernabschnittsdatenPatchRequest();
		dto.fehlstundenGesamt = JsonNullable.of(-1);

		assertThatThrownBy(() -> controller.patch(1L, dto))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

		verifyNoInteractions(service);
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   ", "123456789012345678901" })
	@DisplayName("patch | BeanValidation - BAD_REQUEST bei fehlender, leerer oder zu langer Prüfungsordnung, Service wird nicht aufgerufen")
	void patch_validierungPruefungsOrdnungUngueltig(final String pruefungsOrdnung) {
		final var dto = new SchuelerLernabschnittsdatenPatchRequest();
		dto.pruefungsOrdnung = JsonNullable.of(pruefungsOrdnung);

		assertThatThrownBy(() -> controller.patch(1L, dto))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

		verifyNoInteractions(service);
	}

	@Test
	@DisplayName("patch | BeanValidation - Prüfungsordnung mit genau 20 Zeichen ist zulässig")
	void patch_validierungPruefungsOrdnungMaxLaenge() {
		final var id = 1L;
		final var dto = new SchuelerLernabschnittsdatenPatchRequest();
		dto.pruefungsOrdnung = JsonNullable.of("12345678901234567890");

		assertThat(controller.patch(id, dto))
				.hasFieldOrPropertyWithValue("status", Response.Status.NO_CONTENT.getStatusCode());

		verify(service, times(1)).patch(id, dto);
	}

	@Test
	@DisplayName("patchBemerkungen | Erfolg - NO_CONTENT")
	void patchBemerkungen_erfolg() {
		final var id = 1L;
		final var dto = new SchuelerLernabschnittBemerkungenPatchRequest();
		dto.zeugnisASV = JsonNullable.of("Arbeitet sorgfältig.");

		assertThat(controller.patchBemerkungen(id, dto))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.NO_CONTENT.getStatusCode())
				.extracting(Response::getEntity)
				.isNull();

		verify(service, times(1)).patchBemerkungen(id, dto);
	}
}
