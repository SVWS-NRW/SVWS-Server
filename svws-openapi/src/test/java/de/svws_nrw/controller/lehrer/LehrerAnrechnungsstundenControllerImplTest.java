package de.svws_nrw.controller.lehrer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.service.lehrer.anrechnung.LehrerAnrechnungsstundeCreateRequest;
import de.svws_nrw.service.lehrer.anrechnung.LehrerAnrechnungsstundePatchRequest;
import de.svws_nrw.service.lehrer.anrechnung.LehrerAnrechnungsstundeService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests für LehrerAnrechnungsstundenControllerImpl")
class LehrerAnrechnungsstundenControllerImplTest {

	@Mock
	private LehrerAnrechnungsstundeService service;

	@InjectMocks
	private LehrerAnrechnungsstundenControllerImpl controller;

	@Nested
	@DisplayName("Test der GET-Methoden")
	class GetTests {
		@Test
		@DisplayName("get: Liefert 200 OK mit der Entität")
		void get_returnsOk() {
			final var daten = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
			when(service.get(1L)).thenReturn(daten);

			final Response response = controller.get(1L);

			assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
			assertThat(response.getEntity()).isSameAs(daten);
		}

		@Test
		@DisplayName("getList: Liefert 200 OK mit der Liste von Entitäten")
		void getList_returnsOk() {
			final Collection<Long> ids = List.of(1L, 2L);
			final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> daten = List.of(new LehrerPersonalabschnittsdatenAnrechnungsstunden());
			when(service.getList(ids)).thenReturn(daten);

			final Response response = controller.getList(ids);

			assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
			assertThat(response.getEntity()).isSameAs(daten);
		}
	}

	@Nested
	@DisplayName("Test der PATCH-Methoden")
	class PatchTests {
		@Test
		@DisplayName("patch: Liefert 200 OK nach Einzel-Update")
		void patch_returnsOk() {
			final var patch = new LehrerAnrechnungsstundePatchRequest();
			patch.id = 1L;
			final var daten = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
			when(service.patch(patch)).thenReturn(daten);

			final Response response = controller.patch(patch);

			assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
			assertThat(response.getEntity()).isSameAs(daten);
		}

		@Test
		@DisplayName("patchMultiple: Liefert 200 OK nach Batch-Update")
		void patchMultiple_returnsOk() {
			final var patch = new LehrerAnrechnungsstundePatchRequest();
			patch.id = 1L;
			final var patches = List.of(patch);
			final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> list = List.of(new LehrerPersonalabschnittsdatenAnrechnungsstunden());
			when(service.patchMultiple(patches)).thenReturn(list);

			final Response response = controller.patchMultiple(patches);

			assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
			assertThat(response.getEntity()).isSameAs(list);
		}
	}

	@Nested
	@DisplayName("Test der CREATE-Methoden")
	class CreateTests {
		@Test
		@DisplayName("create: Liefert 201 CREATED nach Neuanlage")
		void create_returnsCreated() {
			final var patch = new LehrerAnrechnungsstundeCreateRequest();
			final var daten = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
			when(service.create(patch)).thenReturn(daten);

			final Response response = controller.create(patch);

			assertThat(response.getStatus()).isEqualTo(Status.CREATED.getStatusCode());
			assertThat(response.getEntity()).isSameAs(daten);
		}

		@Test
		@DisplayName("createMultiple: Liefert 201 CREATED nach Neuanlage")
		void createMultiple_returnsCreated() {
			final Collection<LehrerAnrechnungsstundeCreateRequest> patches = List.of(new LehrerAnrechnungsstundeCreateRequest());
			final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> list = List.of(new LehrerPersonalabschnittsdatenAnrechnungsstunden());
			when(service.createMultiple(patches)).thenReturn(list);

			final Response response = controller.createMultiple(patches);

			assertThat(response.getStatus()).isEqualTo(Status.CREATED.getStatusCode());
			assertThat(response.getEntity()).isSameAs(list);
		}
	}

	@Nested
	@DisplayName("Test der DELETE-Methoden")
	class DeleteTests {
		@Test
		@DisplayName("delete: Liefert 200 OK nach Löschung")
		void delete_returnsOk() {
			final var daten = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
			when(service.delete(1L)).thenReturn(daten);

			final Response response = controller.delete(1L);

			assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
			assertThat(response.getEntity()).isSameAs(daten);
		}

		@Test
		@DisplayName("deleteMultiple: Liefert 200 OK nach Löschung")
		void deleteMultiple_returnsOk() {
			final var ids = List.of(1L, 2L);
			final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> list = List.of(new LehrerPersonalabschnittsdatenAnrechnungsstunden());
			when(service.deleteMultiple(ids)).thenReturn(list);

			final Response response = controller.deleteMultiple(ids);

			assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
			assertThat(response.getEntity()).isSameAs(list);
		}
	}
}
