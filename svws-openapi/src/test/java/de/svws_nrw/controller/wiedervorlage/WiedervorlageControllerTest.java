package de.svws_nrw.controller.wiedervorlage;

import java.util.List;
import java.util.Set;

import de.svws_nrw.core.data.schule.WiedervorlageEintrag;
import de.svws_nrw.core.data.schule.WiedervorlageErledigungRequest;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.wiedervorlage.WiedervorlageCreateRequest;
import de.svws_nrw.service.wiedervorlage.WiedervorlagePatchRequest;
import de.svws_nrw.service.wiedervorlage.WiedervorlageService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("WiedervorlageController")
class WiedervorlageControllerTest {

	@Mock
	private WiedervorlageService wiedervorlageService;

	private WiedervorlageController cut;

	@BeforeEach
	void setUp() {
		cut = new WiedervorlageController(wiedervorlageService);
	}


	@Nested
	@DisplayName("getAll")
	class GetAll {

		@Test
		@DisplayName("gibt 200 mit Liste aller Eintraege zurueck")
		void getAllSuccess() throws ApiOperationException {
			final var eintrag = new WiedervorlageEintrag();
			when(wiedervorlageService.getAll()).thenReturn(List.of(eintrag));

			final Response response = cut.getAll();

			assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
			verify(wiedervorlageService).getAll();
		}
	}

	@Nested
	@DisplayName("get")
	class Get {

		@Test
		@DisplayName("gibt 200 mit Eintrag zurueck")
		void getSuccess() throws ApiOperationException {
			final var eintrag = new WiedervorlageEintrag();
			when(wiedervorlageService.get(1L)).thenReturn(eintrag);

			final Response response = cut.get(1L);

			assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
			verify(wiedervorlageService).get(1L);
		}
	}

	@Nested
	@DisplayName("create")
	class Create {

		@Test
		@DisplayName("gibt 201 zurueck bei gueltigem Request")
		void createSuccess() throws ApiOperationException {
			final var request = validCreateRequest();
			final var eintrag = new WiedervorlageEintrag();
			when(wiedervorlageService.create(request)).thenReturn(eintrag);

			final Response response = cut.create(request);

			assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
			verify(wiedervorlageService).create(request);
		}


		@Test
		@DisplayName("BeanValidation | BAD_REQUEST wenn bemerkung null ist")
		void createValidationBemerkungNull() {
			final var request = validCreateRequest();
			request.bemerkung = null;

			assertThatThrownBy(() -> cut.create(request))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(BAD_REQUEST);

			verifyNoInteractions(wiedervorlageService);
		}

		@Test
		@DisplayName("BeanValidation | BAD_REQUEST wenn bemerkung leer ist")
		void createValidationBemerkungBlank() {
			final var request = validCreateRequest();
			request.bemerkung = "   ";

			assertThatThrownBy(() -> cut.create(request))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(BAD_REQUEST);

			verifyNoInteractions(wiedervorlageService);
		}

		@ParameterizedTest(name = "idBenutzergruppe = {0}")
		@ValueSource(longs = {0L, -1L, -99L})
		@DisplayName("BeanValidation | BAD_REQUEST wenn idBenutzergruppe nicht positiv ist")
		void createValidationIdBenutzergruppeNichtPositiv(final long ungueltigeId) {
			final var request = validCreateRequest();
			request.idBenutzergruppe = ungueltigeId;

			assertThatThrownBy(() -> cut.create(request))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(BAD_REQUEST);

			verifyNoInteractions(wiedervorlageService);
		}

		@ParameterizedTest(name = "typPerson = {0}")
		@ValueSource(ints = {0, 5, -1})
		@DisplayName("BeanValidation | BAD_REQUEST wenn typPerson ausserhalb 1-4")
		void createValidationTypPersonUngueltig(final int ungueltigerTyp) {
			final var request = validCreateRequest();
			request.typPerson = ungueltigerTyp;
			request.idPerson = 1L;

			assertThatThrownBy(() -> cut.create(request))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(BAD_REQUEST);

			verifyNoInteractions(wiedervorlageService);
		}

		@Test
		@DisplayName("BeanValidation | BAD_REQUEST wenn idPerson negativ ist")
		void createValidationIdPersonNegativ() {
			final var request = validCreateRequest();
			request.typPerson = 1;
			request.idPerson = -5L;

			assertThatThrownBy(() -> cut.create(request))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(BAD_REQUEST);

			verifyNoInteractions(wiedervorlageService);
		}

		@Test
		@DisplayName("BeanValidation | BAD_REQUEST wenn tsWiedervorlage falsches Format hat")
		void createValidationTsWiedervolageUngueltigesFormat() {
			final var request = validCreateRequest();
			request.tsWiedervorlage = "07.04.2026";

			assertThatThrownBy(() -> cut.create(request))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(BAD_REQUEST);

			verifyNoInteractions(wiedervorlageService);
		}

		@Test
		@DisplayName("BeanValidation | BAD_REQUEST wenn typPerson gesetzt aber idPerson fehlt (CrossField)")
		void createValidationCrossFieldTypPersonOhneIdPerson() {
			final var request = validCreateRequest();
			request.typPerson = 1;
			request.idPerson = null;

			assertThatThrownBy(() -> cut.create(request))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(BAD_REQUEST);

			verifyNoInteractions(wiedervorlageService);
		}

		@Test
		@DisplayName("BeanValidation | BAD_REQUEST wenn idPerson gesetzt aber typPerson fehlt (CrossField)")
		void createValidationCrossFieldIdPersonOhneTypPerson() {
			final var request = validCreateRequest();
			request.typPerson = null;
			request.idPerson = 42L;

			assertThatThrownBy(() -> cut.create(request))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(BAD_REQUEST);

			verifyNoInteractions(wiedervorlageService);
		}

		@Test
		@DisplayName("BeanValidation | kein Fehler wenn typPerson und idPerson beide null sind")
		void createValidationCrossFieldBeideNull() throws ApiOperationException {
			final var request = validCreateRequest();
			request.typPerson = null;
			request.idPerson = null;
			when(wiedervorlageService.create(request)).thenReturn(new WiedervorlageEintrag());

			final Response response = cut.create(request);

			assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
		}

		@Test
		@DisplayName("BeanValidation | kein Fehler wenn tsWiedervorlage korrektes Format hat")
		void createValidationTsWiedervorlageKorrektesFormat() throws ApiOperationException {
			final var request = validCreateRequest();
			request.tsWiedervorlage = "2026-04-07 08:00:00";
			when(wiedervorlageService.create(request)).thenReturn(new WiedervorlageEintrag());

			final Response response = cut.create(request);

			assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
		}
	}


	@Nested
	@DisplayName("patch")
	class Patch {

		@Test
		@DisplayName("gibt 200 zurueck bei gueltigem Request")
		void patchSuccess() throws ApiOperationException {
			final var request = validPatchRequest();
			final var eintrag = new WiedervorlageEintrag();
			when(wiedervorlageService.patch(request, 1L)).thenReturn(eintrag);

			final Response response = cut.patch(1L, request);

			assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
			verify(wiedervorlageService).patch(request, 1L);
		}

		@Test
		@DisplayName("gibt 200 zurueck bei leerem (absent) PatchRequest")
		void patchAllesFelderAbsentSuccess() throws ApiOperationException {
			final var request = new WiedervorlagePatchRequest(); // alle Felder undefined
			final var eintrag = new WiedervorlageEintrag();
			when(wiedervorlageService.patch(request, 1L)).thenReturn(eintrag);

			final Response response = cut.patch(1L, request);

			assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
		}

		@Test
		@DisplayName("BeanValidation | BAD_REQUEST wenn idBenutzergruppe nicht positiv ist")
		void patchValidationIdBenutzergruppeNichtPositiv() {
			final var request = new WiedervorlagePatchRequest();
			request.idBenutzergruppe = JsonNullable.of(-1L);

			assertThatThrownBy(() -> cut.patch(1L, request))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(BAD_REQUEST);

			verifyNoInteractions(wiedervorlageService);
		}

		@Test
		@DisplayName("BeanValidation | BAD_REQUEST wenn bemerkung present aber leer")
		void patchValidationBemerkungPresentAberLeer() {
			final var request = new WiedervorlagePatchRequest();
			request.bemerkung = JsonNullable.of("   ");

			assertThatThrownBy(() -> cut.patch(1L, request))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(BAD_REQUEST);

			verifyNoInteractions(wiedervorlageService);
		}

		@Test
		@DisplayName("BeanValidation | BAD_REQUEST wenn tsWiedervorlage falsches Format hat")
		void patchValidationTsWiedervorlageUngueltigesFormat() {
			final var request = new WiedervorlagePatchRequest();
			request.tsWiedervorlage = JsonNullable.of("2026/04/07"); // falsches Format

			assertThatThrownBy(() -> cut.patch(1L, request))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(BAD_REQUEST);

			verifyNoInteractions(wiedervorlageService);
		}
	}

	@Nested
	@DisplayName("delete (single)")
	class DeleteSingle {

		@Test
		@DisplayName("gibt 200 mit geloeschtem Eintrag zurueck")
		void deleteSuccess() throws ApiOperationException {
			final Response response = cut.delete(1L);

			assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

			verify(wiedervorlageService).delete(1L);
		}
	}

	@Nested
	@DisplayName("delete (bulk)")
	class DeleteBulk {

		@Test
		@DisplayName("gibt 204 zurueck nach Bulk-Delete")
		void deleteBulkSuccess() throws ApiOperationException {
			final var ids = Set.of(1L, 2L, 3L);

			final Response response = cut.delete(ids);

			assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
			verify(wiedervorlageService).delete(ids);
		}
	}

	@Nested
	@DisplayName("markiereAlsErledigt")
	class MarkiereAlsErledigt {

		@Test
		@DisplayName("gibt 200 mit aktualisiertem Eintrag zurueck")
		void markiereAlsErledigtSuccess() throws ApiOperationException {
			final var eintrag = new WiedervorlageEintrag();
			when(wiedervorlageService.markiereAlsErledigt(1L)).thenReturn(eintrag);

			final Response response = cut.markiereAlsErledigt(1L);

			assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
			verify(wiedervorlageService).markiereAlsErledigt(1L);
		}
	}

	@Nested
	@DisplayName("setErledigung")
	class SetErledigung {

		@Test
		@DisplayName("gibt 200 mit aktualisiertem Eintrag zurueck wenn erledigt=true")
		void setErledigungMarkiertAlsErledigtSuccess() throws ApiOperationException {
			final var request = new WiedervorlageErledigungRequest();
			request.erledigt = true;
			final var eintrag = new WiedervorlageEintrag();
			when(wiedervorlageService.setErledigung(1L, request)).thenReturn(eintrag);

			final Response response = cut.setErledigung(1L, request);

			assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
			verify(wiedervorlageService).setErledigung(1L, request);
		}

		@Test
		@DisplayName("gibt 200 mit aktualisiertem Eintrag zurueck wenn erledigt=false")
		void setErledigungEntferntMarkierungSuccess() throws ApiOperationException {
			final var request = new WiedervorlageErledigungRequest();
			request.erledigt = false;
			final var eintrag = new WiedervorlageEintrag();
			when(wiedervorlageService.setErledigung(1L, request)).thenReturn(eintrag);

			final Response response = cut.setErledigung(1L, request);

			assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
			verify(wiedervorlageService).setErledigung(1L, request);
		}
	}


	private static WiedervorlageCreateRequest validCreateRequest() {
		final var request = new WiedervorlageCreateRequest();
		request.bemerkung = "Testbemerkung";
		request.idBenutzergruppe = null;
		request.typPerson = null;
		request.idPerson = null;
		request.tsWiedervorlage = "2026-11-14 13:12:48.543";
		request.automatischErledigt = false;
		return request;
	}

	private static WiedervorlagePatchRequest validPatchRequest() {
		final var request = new WiedervorlagePatchRequest();
		request.bemerkung = JsonNullable.of("Geaenderte Bemerkung");
		return request;
	}
}
