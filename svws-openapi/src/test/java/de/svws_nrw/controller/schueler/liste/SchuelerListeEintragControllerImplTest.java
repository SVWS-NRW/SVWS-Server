package de.svws_nrw.controller.schueler.liste;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schueler.liste.SchuelerListeEintragService;
import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
class SchuelerListeEintragControllerImplTest {

	@Mock
	private SchuelerListeEintragService service;

	private SchuelerListeEintragControllerImpl controller;

	@BeforeEach
	void setUp() {
		controller = new SchuelerListeEintragControllerImpl(service);
	}

	@Test
	@DisplayName("getListeAktuell | Erfolg")
	void getListeAktuell_success() {
		final var liste = List.of(mock(SchuelerListeEintrag.class), mock(SchuelerListeEintrag.class));
		when(service.getListeAktuell(false)).thenReturn(liste);

		assertThat(controller.getListeAktuell(false))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(liste);

		verify(service, times(1)).getListeAktuell(false);
	}

	@Test
	@DisplayName("getListeAktuell | Fehler des Services werden weitergereicht")
	void getListeAktuell_exception() {
		when(service.getListeAktuell(false)).thenThrow(new ApiOperationException(Response.Status.NOT_FOUND, "Keine Schule"));

		assertThatThrownBy(() -> controller.getListeAktuell(false))
				.isInstanceOf(ApiOperationException.class);
	}

	@Test
	@DisplayName("getListe | Erfolg")
	void getListe_success() {
		final var liste = List.of(mock(SchuelerListeEintrag.class));
		when(service.getListe(20L, false)).thenReturn(liste);

		assertThat(controller.getListe(20L, false))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(liste);

		verify(service, times(1)).getListe(20L, false);
	}

	@Test
	@DisplayName("getListe | Fehler des Services werden weitergereicht")
	void getListe_exception() {
		when(service.getListe(20L, true)).thenThrow(new ApiOperationException(Response.Status.NOT_FOUND, "Kein Abschnitt"));

		assertThatThrownBy(() -> controller.getListe(20L, true))
				.isInstanceOf(ApiOperationException.class);
	}

}
