package de.svws_nrw.oauth.internal;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Base64;

import de.svws_nrw.oauth.OAuthScope;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit-Tests fuer {@link ClientCredentialsFlow}.
 */
@ExtendWith(MockitoExtension.class)
class ClientCredentialsFlowTest {

	@Mock
	private HttpClient client;


	@InjectMocks
	private ClientCredentialsFlow cut;

	@BeforeEach
	void setup() {
		client = mock(HttpClient.class);
		final Clock clock = Clock.fixed(Instant.parse("2026-06-18T12:00:00Z"), ZoneOffset.UTC);
		cut = new ClientCredentialsFlow(client, clock);
	}


	@Test
	@DisplayName("acquire | sets basic authorization header")
	void acquireSetsBasicAuthorizationHeader() throws Exception {
		final Credentials creds = createCreds();

		final HttpResponse<String> response = mockResponse(200, "{\"access_token\":\"t\",\"token_type\":\"Bearer\",\"expires_in\":60}");
		when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);

		cut.acquire(creds, OAuthScope.DEFAULT);

		final ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
		verify(client, times(1)).send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));
		final HttpRequest request = requestCaptor.getValue();

		final String expectedAuth = "Basic " + Base64.getEncoder().encodeToString("client-id:client-secret".getBytes(StandardCharsets.UTF_8));
		assertEquals(expectedAuth, request.headers().firstValue("Authorization").orElseThrow());
	}

	@Test
	@DisplayName("acquire | throws token request exception on non whitelisted status")
	void acquireThrowsTokenRequestExceptionOnNonWhitelistedStatus() throws Exception {
		final Credentials creds = createCreds();

		final HttpResponse<String> response = mockResponse(500, "oops");
		when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);

		final OAuthFlow.TokenRequestException ex = assertThrows(OAuthFlow.TokenRequestException.class, () -> cut.acquire(creds, null));
		assertTrue(ex.getMessage().contains("HTTP 500"));
		assertTrue(ex.getMessage().contains("oops"));
	}

	@Test
	@DisplayName("acquire | returns access token for valid json response")
	void acquireReturnsAccessTokenForValidJsonResponse() throws Exception {
		final Credentials creds = createCreds();

		final HttpResponse<String> response = mockResponse(200, "{\"access_token\":\"abc\",\"token_type\":\"Bearer\",\"expires_in\":90}");
		when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);

		final AccessToken token = cut.acquire(creds, null);

		assertEquals("abc", token.value());
		assertEquals("Bearer", token.tokenType());
		assertEquals(Instant.parse("2026-06-18T12:01:30Z"), token.expiresAt());
	}

	@Test
	@DisplayName("acquire | throws token request exception when access_token missing")
	void acquireThrowsTokenRequestExceptionWhenAccessTokenMissing() throws Exception {
		final Credentials creds = createCreds();

		final HttpResponse<String> response = mockResponse(200, "{\"token_type\":\"Bearer\",\"expires_in\":90}");
		when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);

		final OAuthFlow.TokenRequestException ex = assertThrows(OAuthFlow.TokenRequestException.class, () -> cut.acquire(creds, null));
		assertTrue(ex.getMessage().contains("no access_token"));
	}

	@Test
	@DisplayName("acquire | throws token request exception on json parse error")
	void acquireThrowsTokenRequestExceptionOnJsonExtractAccessTokenError() throws Exception {
		final Credentials creds = createCreds();

		final HttpResponse<String> response = mockResponse(200, "not-json");
		when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);

		final OAuthFlow.TokenRequestException ex = assertThrows(OAuthFlow.TokenRequestException.class, () -> cut.acquire(creds, null));
		assertTrue(ex.getMessage().contains("failed to parse"));
		assertNotNull(ex.getCause());
	}

	@Test
	@DisplayName("acquire | throws token request exception and preserves interrupt on io")
	void acquireThrowsTokenRequestExceptionAndPreservesInterruptOnIo() throws Exception {
		final Credentials creds = createCreds();

		when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(new IOException("io exc."));

		final OAuthFlow.TokenRequestException ex = assertThrows(OAuthFlow.TokenRequestException.class, () -> cut.acquire(creds, null));
		assertTrue(ex.getMessage().contains("token request failed"));
		assertTrue(Thread.currentThread().isInterrupted());
		assertNotNull(ex);

		Thread.interrupted();
	}

	private static HttpResponse<String> mockResponse(final int statusCode, final String body) {
		@SuppressWarnings("unchecked") final HttpResponse<String> response = (HttpResponse<String>) mock(HttpResponse.class);
		when(response.statusCode()).thenReturn(statusCode);
		when(response.body()).thenReturn(body);
		return response;
	}

	private static @NonNull Credentials createCreds() {
		return new Credentials(
				"client-id",
				"client-secret",
				URI.create("https://issuer.example/oauth2/token"),
				null,
				OAuthDomain.IT_NRW
		);
	}
}
