package de.svws_nrw.controller.lehrer.liste;

import java.util.List;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.lehrer.liste.LehrerListeService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LehrerListeControllerImplTest {

	private static final long ID_ABSCHNITT = 20L;

	@Mock
	private LehrerListeService service;

	private LehrerListeControllerImpl controller;

	@BeforeEach
	void setUp() {
		controller = new LehrerListeControllerImpl(service);
	}

	@Test
	@DisplayName("getListe | liefert die Liste mit Referenzinformation")
	void getListe_success() {
		final var liste = List.of(mock(LehrerListeEintrag.class), mock(LehrerListeEintrag.class));
		when(service.getList(true)).thenReturn(liste);

		assertThat(controller.getList())
				.isInstanceOf(Response.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(liste);

		verify(service).getList(true);
	}

	@Test
	@DisplayName("getListeFuerAbschnitt | liefert die Liste des Abschnitts mit Referenzinformation")
	void getListeFuerAbschnitt_success() {
		final var liste = List.of(mock(LehrerListeEintrag.class));
		when(service.getListBySchuljahresabschnitt(ID_ABSCHNITT, true)).thenReturn(liste);

		assertThat(controller.getListBySchuljahresabschnitt(ID_ABSCHNITT))
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(liste);

		verify(service).getListBySchuljahresabschnitt(ID_ABSCHNITT, true);
	}

	@Test
	@DisplayName("getListeFuerAbschnitt | Fehler des Services werden weitergereicht")
	void getListeFuerAbschnitt_exception() {
		when(service.getListBySchuljahresabschnitt(ID_ABSCHNITT, true))
				.thenThrow(new ApiOperationException(Response.Status.NOT_FOUND, "Der Schuljahresabschnitt wurde nicht gefunden."));

		assertThatThrownBy(() -> controller.getListBySchuljahresabschnitt(ID_ABSCHNITT))
				.isInstanceOf(ApiOperationException.class);
	}

	@Test
	@DisplayName("delete | liefert die Ergebnisse der Löschvorgänge")
	void delete_success() {
		final var ids = List.of(1L, 2L);
		final var ergebnisse = List.of(SimpleOperationResponse.ofSuccess(1L), SimpleOperationResponse.ofError(2L, "Fehler"));
		when(service.delete(ids)).thenReturn(ergebnisse);

		assertThat(controller.delete(ids))
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(ergebnisse);

		verify(service).delete(ids);
	}

}
