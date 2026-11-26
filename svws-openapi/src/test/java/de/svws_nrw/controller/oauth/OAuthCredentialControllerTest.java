package de.svws_nrw.controller.oauth;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.oauth2.OAuthCredentials;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.oauth.OAuthCredentialsInternalMapper;
import de.svws_nrw.oauth.internal.Credentials;
import de.svws_nrw.oauth.internal.OAuthDomain;
import de.svws_nrw.service.oauth.credential.OAuthCreateCredential;
import de.svws_nrw.service.oauth.credential.OAuthCredentialService;
import de.svws_nrw.service.oauth.credential.OAuthDomainBerechtigung;
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

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.FORBIDDEN;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("OAuthCredentialController")
class OAuthCredentialControllerTest {

	@Mock
	private OAuthCredentialService service;

	@Mock
	private OAuthCredentialsInternalMapper mapper;

	private OAuthCredentialController cut;

	@BeforeEach
	void setUp() {
		final var benutzer = mock(Benutzer.class);
		lenient().when(benutzer.istAdmin()).thenReturn(false);
		lenient().when(benutzer.pruefeKompetenz(Set.of(BenutzerKompetenz.IT_NRW_ADMINISTRIEREN))).thenReturn(true);
		cut = new OAuthCredentialController(service, mapper, new OAuthDomainBerechtigung(benutzer));
	}


	@Nested
	@DisplayName("create")
	class Create {

		@Test
		@DisplayName("gibt 201 mit den ueber fromInternal gemappten Credentials zurueck")
		void createSuccess() {
			final var input = validCreateCredential();
			final var credentials = buildCredentials(OAuthDomain.IT_NRW);
			final var external = buildApiCredentials();
			when(mapper.toInternal(input)).thenReturn(credentials);
			when(service.create(credentials)).thenReturn(credentials);
			when(mapper.fromInternal(credentials)).thenReturn(external);

			final Response response = cut.create(input);

			assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
			assertThat(response.getEntity()).isEqualTo(external);
			verify(service).create(credentials);
			verify(mapper).fromInternal(credentials);
		}

		@Test
		@DisplayName("FORBIDDEN wenn keine Berechtigung fuer die Domaene besteht")
		void createForbidden() {
			final var input = createCredentialFor("WENOM");
			when(mapper.toInternal(input)).thenReturn(buildCredentials(OAuthDomain.WENOM));

			assertThatThrownBy(() -> cut.create(input))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(FORBIDDEN);

			verifyNoInteractions(service);
		}

		@ParameterizedTest(name = "domain = ''{0}''")
		@ValueSource(strings = {"UNBEKANNT", "it_nrw", ""})
		@DisplayName("BeanValidation | BAD_REQUEST wenn die Domaene nicht existiert")
		void createValidationDomainUnbekannt(final String domain) {
			final var input = createCredentialFor(domain);

			assertThatThrownBy(() -> cut.create(input))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(BAD_REQUEST);

			verifyNoInteractions(service, mapper);
		}

		@Test
		@DisplayName("BeanValidation | BAD_REQUEST wenn clientId null ist")
		void createValidationClientIdNull() {
			final var input = validCreateCredential();
			input.clientId = null;

			assertThatThrownBy(() -> cut.create(input))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(BAD_REQUEST);

			verifyNoInteractions(service, mapper);
		}

		@Test
		@DisplayName("BeanValidation | BAD_REQUEST wenn clientId leer ist")
		void createValidationClientIdBlank() {
			final var input = validCreateCredential();
			input.clientId = "";

			assertThatThrownBy(() -> cut.create(input))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(BAD_REQUEST);

			verifyNoInteractions(service, mapper);
		}

		@Test
		@DisplayName("BeanValidation | BAD_REQUEST wenn clientSecret leer ist")
		void createValidationClientSecretBlank() {
			final var input = validCreateCredential();
			input.clientSecret = "";

			assertThatThrownBy(() -> cut.create(input))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(BAD_REQUEST);

			verifyNoInteractions(service, mapper);
		}

		@Test
		@DisplayName("BeanValidation | BAD_REQUEST wenn authServerUrl leer ist")
		void createValidationTokenUrlBlank() {
			final var input = validCreateCredential();
			input.tokenUrl = "";

			assertThatThrownBy(() -> cut.create(input))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(BAD_REQUEST);

			verifyNoInteractions(service, mapper);
		}

		@ParameterizedTest(name = "authServerUrl = ''{0}''")
		@ValueSource(strings = {"nicht eine URI", "https://ungueltig .de"})
		@DisplayName("BeanValidation | BAD_REQUEST wenn authServerUrl keine gueltige URI ist")
		void createValidationTokenUrlUngueltig(final String ungueltigeUrl) {
			final var input = validCreateCredential();
			input.tokenUrl = ungueltigeUrl;

			assertThatThrownBy(() -> cut.create(input))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(BAD_REQUEST);

			verifyNoInteractions(service, mapper);
		}
	}


	@Nested
	@DisplayName("delete")
	class Delete {

		@Test
		@DisplayName("gibt 204 zurueck und delegiert an den Service")
		void deleteSuccess() {
			SimpleOperationResponse log = SimpleOperationResponse.ofSuccess(1L);
			when(service.get(1L)).thenReturn(Optional.of(buildCredentials(OAuthDomain.IT_NRW)));
			when(service.delete(1L)).thenReturn(log);

			final Response response = cut.delete(1L);

			assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
			assertThat(response.getEntity()).isEqualTo(log);
			verify(service).delete(1L);
		}

		@Test
		@DisplayName("FORBIDDEN und kein Loeschen wenn keine Berechtigung fuer die Domaene besteht")
		void deleteForbidden() {
			when(service.get(2L)).thenReturn(Optional.of(buildCredentials(OAuthDomain.WENOM)));

			assertThatThrownBy(() -> cut.delete(2L))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(FORBIDDEN);

			verify(service, never()).delete(2L);
		}

		@Test
		@DisplayName("NOT_FOUND wenn keine Credentials zur ID existieren")
		void deleteNotFound() {
			when(service.get(99L)).thenReturn(Optional.empty());

			assertThatThrownBy(() -> cut.delete(99L))
					.isInstanceOf(ApiOperationException.class)
					.extracting("status")
					.isEqualTo(NOT_FOUND);

			verify(service, never()).delete(99L);
		}
	}


	@Nested
	@DisplayName("getAll")
	class GetAll {

		@Test
		@DisplayName("gibt 200 mit allen gemappten Credentials zurueck")
		void getAllSuccess() {
			final var internal = buildCredentials(OAuthDomain.IT_NRW);
			final var external = buildApiCredentials();
			when(service.getAll()).thenReturn(List.of(internal));
			when(mapper.fromInternal(internal)).thenReturn(external);

			final Response response = cut.getAll();

			assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
			assertThat(response.getEntity()).isEqualTo(List.of(external));
			verify(service).getAll();
		}

		@Test
		@DisplayName("liefert nur Credentials von Domaenen, auf die der Benutzer zugreifen darf")
		void getAllFiltersByBerechtigung() {
			final var erlaubt = buildCredentials(OAuthDomain.IT_NRW);
			final var verboten = buildCredentials(OAuthDomain.WENOM);
			final var external = buildApiCredentials();
			when(service.getAll()).thenReturn(List.of(erlaubt, verboten));
			when(mapper.fromInternal(erlaubt)).thenReturn(external);

			final Response response = cut.getAll();

			assertThat(response.getEntity()).isEqualTo(List.of(external));
			verify(mapper, never()).fromInternal(verboten);
		}

		@Test
		@DisplayName("gibt 200 mit leerer Liste zurueck wenn keine Credentials vorhanden sind")
		void getAllEmpty() {
			when(service.getAll()).thenReturn(List.of());

			final Response response = cut.getAll();

			assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
			assertThat(response.getEntity()).isEqualTo(List.of());
			verifyNoInteractions(mapper);
		}
	}


	private static OAuthCreateCredential validCreateCredential() {
		final var input = new OAuthCreateCredential();
		input.clientId = "client-id";
		input.clientSecret = "client-secret";
		input.tokenUrl = "https://auth.example.com/token";
		input.requestedScope = "scope";
		input.domain = "IT_NRW";
		return input;
	}

	private static OAuthCreateCredential createCredentialFor(final String domain) {
		final var input = validCreateCredential();
		input.domain = domain;

		return input;
	}

	private static Credentials buildCredentials(final OAuthDomain domain) {
		return new Credentials("client-id", "client-secret", URI.create("https://auth.example.com/token"), "scope", domain);
	}

	private static OAuthCredentials buildApiCredentials() {
		final var credentials = new OAuthCredentials();
		credentials.clientId = "client-id";
		credentials.clientSecret = "*******";
		credentials.tokenUrl = "https://auth.example.com/token";
		credentials.requestedScope = "scope";
		credentials.domain = "IT_NRW";
		return credentials;
	}
}
