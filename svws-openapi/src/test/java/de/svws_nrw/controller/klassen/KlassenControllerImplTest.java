package de.svws_nrw.controller.klassen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.klassen.KlassenService;
import jakarta.ws.rs.core.Response;

/**
 * Test für die Klasse {@link KlassenControllerImpl}
 */
@ExtendWith(MockitoExtension.class)
class KlassenControllerImplTest {

	@Mock
	private KlassenService klassenService;

	private KlassenControllerImpl controller;

	@BeforeEach
	void setUp() {
		controller = new KlassenControllerImpl(klassenService);
	}

	@Test
	@DisplayName("get | Erfolg: Die Daten der Klasse werden mit Status 200 geliefert")
	void get_success() {
		final long id = 1L;
		final KlassenDaten klasse = mock(KlassenDaten.class);
		when(klassenService.getById(id)).thenReturn(klasse);

		assertThat(controller.get(id))
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(klasse);

		verify(klassenService, times(1)).getById(id);
	}

	@Test
	@DisplayName("get | Unbekannte Klasse: Die Exception des Services wird weitergegeben")
	void get_notFound() {
		final long id = 99L;
		when(klassenService.getById(id)).thenThrow(new ApiOperationException(Response.Status.NOT_FOUND, "Keine Klasse zur ID 99 gefunden."));

		assertThatThrownBy(() -> controller.get(id))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Response.Status.NOT_FOUND));
	}

	@Test
	@DisplayName("getListBySchuljahresabschnitt | Erfolg: Die Klassen werden mit Schülern und Status 200 geliefert")
	void getListBySchuljahresabschnitt_success() {
		final long idAbschnitt = 11L;
		final List<KlassenDaten> klassen = List.of(mock(KlassenDaten.class), mock(KlassenDaten.class));
		when(klassenService.getListBySchuljahresabschnitt(idAbschnitt, true)).thenReturn(klassen);

		assertThat(controller.getListBySchuljahresabschnitt(idAbschnitt))
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(klassen);

		verify(klassenService, times(1)).getListBySchuljahresabschnitt(idAbschnitt, true);
	}

	@Test
	@DisplayName("getListBySchuljahresabschnitt | Ungültiger Abschnitt: Die Exception des Services wird weitergegeben")
	void getListBySchuljahresabschnitt_invalidAbschnitt() {
		final long idAbschnitt = 77L;
		when(klassenService.getListBySchuljahresabschnitt(idAbschnitt, true))
				.thenThrow(new ApiOperationException(Response.Status.NOT_FOUND, "Der Schuljahresabschnitt mit der ID 77 wurde nicht gefunden."));

		assertThatThrownBy(() -> controller.getListBySchuljahresabschnitt(idAbschnitt))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Response.Status.NOT_FOUND));
	}

	@Test
	@DisplayName("getList | Erfolg: Die Klassen werden mit Schülern und Status 200 geliefert")
	void getList_success() {
		final List<Long> ids = List.of(2L, 1L);
		final List<KlassenDaten> klassen = List.of(mock(KlassenDaten.class), mock(KlassenDaten.class));
		when(klassenService.getListByIdsMitSchueler(ids)).thenReturn(klassen);

		assertThat(controller.getList(ids))
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(klassen);

		verify(klassenService, times(1)).getListByIdsMitSchueler(ids);
	}

	@Test
	@DisplayName("getList | Leere ID-Liste: Eine leere Liste wird mit Status 200 geliefert")
	void getList_emptyIds() {
		when(klassenService.getListByIdsMitSchueler(List.of())).thenReturn(List.of());

		assertThat(controller.getList(List.of()))
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(List.of());
	}

	@Test
	@DisplayName("getList | Unbekannte ID: Die Exception des Services wird weitergegeben")
	void getList_notFound() {
		final List<Long> ids = List.of(1L, 99L);
		when(klassenService.getListByIdsMitSchueler(ids))
				.thenThrow(new ApiOperationException(Response.Status.NOT_FOUND, "Es wurden nicht alle Klassen zu den IDs gefunden."));

		assertThatThrownBy(() -> controller.getList(ids))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Response.Status.NOT_FOUND));
	}

}
