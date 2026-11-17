package de.svws_nrw.controller.klassen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.openapitools.jackson.nullable.JsonNullable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.klassen.KlassenCreateRequest;
import de.svws_nrw.service.klassen.KlassenPatchRequest;
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

	private static KlassenCreateRequest createValidCreateRequest() {
		final var request = new KlassenCreateRequest();
		request.idSchuljahresabschnitt = 11L;
		request.kuerzel = "5a";
		request.idJahrgang = 4L;

		return request;
	}

	private static void assertBadRequest(final Runnable aufruf) {
		assertThatThrownBy(aufruf::run)
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Response.Status.BAD_REQUEST));
	}

	@Test
	@DisplayName("create | Erfolg: Die neue Klasse wird mit Status 201 geliefert")
	void create_success() {
		final var request = createValidCreateRequest();
		final KlassenDaten klasse = mock(KlassenDaten.class);
		when(klassenService.create(request)).thenReturn(klasse);

		assertThat(controller.create(request))
				.hasFieldOrPropertyWithValue("status", Response.Status.CREATED.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(klasse);

		verify(klassenService, times(1)).create(request);
	}

	@Test
	@DisplayName("create | Kein Request: Es wird 400 geliefert und der Service wird nicht aufgerufen")
	void create_requestNull() {
		assertBadRequest(() -> controller.create(null));

		verify(klassenService, never()).create(any());
	}

	@Test
	@DisplayName("create | Fehlendes Kürzel: Der BeanValidator liefert 400 und der Service wird nicht aufgerufen")
	void create_kuerzelFehlt() {
		final var request = createValidCreateRequest();
		request.kuerzel = null;

		assertBadRequest(() -> controller.create(request));

		verify(klassenService, never()).create(any());
	}

	@Test
	@DisplayName("create | Fehlender Schuljahresabschnitt: Der BeanValidator liefert 400 und der Service wird nicht aufgerufen")
	void create_abschnittFehlt() {
		final var request = createValidCreateRequest();
		request.idSchuljahresabschnitt = null;

		assertBadRequest(() -> controller.create(request));

		verify(klassenService, never()).create(any());
	}

	@Test
	@DisplayName("create | Fehler des Services: Die Exception wird weitergegeben")
	void create_serviceFehler() {
		final var request = createValidCreateRequest();
		when(klassenService.create(request)).thenThrow(new ApiOperationException(Response.Status.NOT_FOUND, "Der Jahrgang wurde nicht gefunden."));

		assertThatThrownBy(() -> controller.create(request))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Response.Status.NOT_FOUND));
	}

	@Test
	@DisplayName("patch | Erfolg: Die geänderte Klasse wird mit Status 200 geliefert")
	void patch_success() {
		final var request = new KlassenPatchRequest();
		request.kuerzel = JsonNullable.of("5b");
		final KlassenDaten klasse = mock(KlassenDaten.class);
		when(klassenService.patch(1L, request)).thenReturn(klasse);

		assertThat(controller.patch(1L, request))
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(klasse);

		verify(klassenService, times(1)).patch(1L, request);
	}

	@Test
	@DisplayName("patch | Leerer Patch: Es wird 200 geliefert")
	void patch_leererPatch() {
		final var request = new KlassenPatchRequest();
		final KlassenDaten klasse = mock(KlassenDaten.class);
		when(klassenService.patch(1L, request)).thenReturn(klasse);

		assertThat(controller.patch(1L, request)).hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode());

		verify(klassenService, times(1)).patch(1L, request);
	}

	@Test
	@DisplayName("patch | Parallelität in Kleinbuchstaben: Der BeanValidator liefert 400 und der Service wird nicht aufgerufen")
	void patch_parallelitaetUngueltig() {
		final var request = new KlassenPatchRequest();
		request.parallelitaet = JsonNullable.of("a");

		assertBadRequest(() -> controller.patch(1L, request));

		verify(klassenService, never()).patch(any(), any());
	}

	@Test
	@DisplayName("patch | Kein Request: Es wird 400 geliefert und der Service wird nicht aufgerufen")
	void patch_requestNull() {
		assertBadRequest(() -> controller.patch(1L, null));

		verify(klassenService, never()).patch(any(), any());
	}

	@Test
	@DisplayName("patch | Negative Sortierung: Der BeanValidator liefert 400 und der Service wird nicht aufgerufen")
	void patch_sortierungNegativ() {
		final var request = new KlassenPatchRequest();
		request.sortierung = JsonNullable.of(-1);

		assertBadRequest(() -> controller.patch(1L, request));

		verify(klassenService, never()).patch(any(), any());
	}

	@Test
	@DisplayName("delete | Erfolg: Die Ergebnisse des Services werden mit Status 200 geliefert")
	void delete_success() {
		final var ids = List.of(1L, 2L);
		final var responses = List.of(SimpleOperationResponse.ofSuccess(1L), SimpleOperationResponse.ofError(2L, "Klasse hat noch Schüler."));
		when(klassenService.delete(ids)).thenReturn(responses);

		assertThat(controller.delete(ids))
				.hasFieldOrPropertyWithValue("status", Response.Status.OK.getStatusCode())
				.extracting(Response::getEntity)
				.isEqualTo(responses);

		verify(klassenService, times(1)).delete(ids);
	}

	@Test
	@DisplayName("delete | Service-Fehler: Die Exception des Services wird weitergegeben")
	void delete_serviceFehler() {
		final var ids = List.of(99L);
		when(klassenService.delete(ids)).thenThrow(new ApiOperationException(Response.Status.NOT_FOUND, "Es wurden keine Klassen zu den IDs gefunden."));

		assertThatThrownBy(() -> controller.delete(ids))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Response.Status.NOT_FOUND));
	}

	@Test
	@DisplayName("setDefaultSortierung | Erfolg: Der Service wird aufgerufen und Status 204 geliefert")
	void setDefaultSortierung_success() {
		assertThat(controller.setDefaultSortierung(11L))
				.hasFieldOrPropertyWithValue("status", Response.Status.NO_CONTENT.getStatusCode())
				.extracting(Response::getEntity)
				.isNull();

		verify(klassenService, times(1)).setDefaultSortierung(11L);
	}

	@Test
	@DisplayName("setDefaultSortierung | Service-Fehler: Die Exception des Services wird weitergegeben")
	void setDefaultSortierung_serviceFehler() {
		doThrow(new ApiOperationException(Response.Status.NOT_FOUND, "Es wurden keine Jahrgänge gefunden.")).when(klassenService).setDefaultSortierung(11L);

		assertThatThrownBy(() -> controller.setDefaultSortierung(11L))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Response.Status.NOT_FOUND));
	}

}
