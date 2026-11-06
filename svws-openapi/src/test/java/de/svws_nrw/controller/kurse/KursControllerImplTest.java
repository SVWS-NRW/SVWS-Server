package de.svws_nrw.controller.kurse;

import static org.assertj.core.api.Assertions.assertThat;
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

import de.svws_nrw.asd.data.kurse.KursDaten;
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

}
