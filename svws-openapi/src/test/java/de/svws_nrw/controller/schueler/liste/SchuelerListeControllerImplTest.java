package de.svws_nrw.controller.schueler.liste;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.core.data.schueler.SchuelerListe;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schueler.liste.SchuelerListeService;
import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
class SchuelerListeControllerImplTest {

	@Mock
	private SchuelerListeService service;

	private SchuelerListeControllerImpl controller;

	@BeforeEach
	void setUp() {
		controller = new SchuelerListeControllerImpl(service);
	}

	@Test
	@DisplayName("getBySchuljahresabschnitt | Erfolg")
	void getBySchuljahresabschnitt_success() {
		final var liste = mock(SchuelerListe.class);
		when(service.getBySchuljahresabschnitt(20L)).thenReturn(liste);

		assertThat(controller.getBySchuljahresabschnitt(20L))
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(liste);

		verify(service, times(1)).getBySchuljahresabschnitt(20L);
	}

	@Test
	@DisplayName("getBySchuljahresabschnitt | Fehler des Services werden weitergereicht")
	void getBySchuljahresabschnitt_exception() {
		when(service.getBySchuljahresabschnitt(20L)).thenThrow(new ApiOperationException(Response.Status.NOT_FOUND, "Kein Abschnitt"));

		assertThatThrownBy(() -> controller.getBySchuljahresabschnitt(20L))
				.isInstanceOf(ApiOperationException.class);
	}

}
