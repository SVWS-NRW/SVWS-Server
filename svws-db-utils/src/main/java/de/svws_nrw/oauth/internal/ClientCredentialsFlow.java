package de.svws_nrw.oauth.internal;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.svws_nrw.oauth.OAuthScope;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;

/**
 * Manueller Client-Credentials-Flow ueber den JDK HttpClient.
 *
 */
public final class ClientCredentialsFlow implements OAuthFlow {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private static final Set<Integer> SUCCESS_RESPONSE_CODES = Set.of(200, 201);
	private static final String GRANT_TYPE = "client_credentials";
	private static final Duration CONNECTION_TIMEOUT_SEC = Duration.ofSeconds(10);
	private static final Duration REQUEST_TIMEOUT_SEC = Duration.ofSeconds(15);

	private final HttpClient client;
	private final Clock clock;

	/**
	 * Konstruktor
	 */
	public ClientCredentialsFlow() {
		this(HttpClient.newBuilder().connectTimeout(CONNECTION_TIMEOUT_SEC).build(), Clock.systemUTC());
	}

	/**
	 * Konstruktor
	 * @param client {@link HttpClient}
	 * @param clock {@link Clock}
	 */
	ClientCredentialsFlow(final HttpClient client, final Clock clock) {
		this.client = client;
		this.clock = clock;
	}

	@Override
	public AccessToken acquire(final Credentials creds, final OAuthScope scope) {
		final HttpRequest request = HttpRequest.newBuilder()
				.uri(creds.authServerUrl())
				.timeout(REQUEST_TIMEOUT_SEC)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)
				.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, buildBasicAuthHeader(creds))
				.POST(buildRequestBody(resolveScope(creds, scope)))
				.build();

		try {
			final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			return handleResponse(response);
		} catch (final IOException | InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new TokenRequestException("token request failed: " + creds.authServerUrl(), e);
		}
	}

	private AccessToken handleResponse(final HttpResponse<String> response) {
		if (!SUCCESS_RESPONSE_CODES.contains(response.statusCode())) {
			throw new TokenRequestException(String.format("token endpoint returned HTTP %d: %s", response.statusCode(), response.body()));
		}

		return extractAccessToken(response.body());
	}

	private static String resolveScope(final Credentials creds, final OAuthScope scope) {
		return Optional.ofNullable(scope)
				.map(OAuthScope::text)
				.orElse(creds.requestedScope());
	}

	private static String buildBasicAuthHeader(final Credentials creds) {
		final String authCreds = String.format("%s:%s", creds.clientId(), creds.clientSecret());
		final String encAuthCreds = Base64.getEncoder().encodeToString(authCreds.getBytes(StandardCharsets.UTF_8));
		return String.format("Basic %s", encAuthCreds);
	}

	private AccessToken extractAccessToken(final String json) {
		try {
			final TokenResponse tokenResponse = OBJECT_MAPPER.readValue(json, TokenResponse.class);

			if (StringUtils.isBlank(tokenResponse.accessToken)) {
				throw new TokenRequestException(String.format("no access_token in response: %s", json));
			}

			return AccessToken.of(tokenResponse.accessToken, tokenResponse.tokenType, tokenResponse.expiresIn, clock.instant());
		} catch (final JsonProcessingException e) {
			throw new TokenRequestException("failed to parse token response", e);
		}
	}

	private static HttpRequest.BodyPublisher buildRequestBody(final String scope) {
		final StringBuilder sb = new StringBuilder(String.format("grant_type=%s", GRANT_TYPE));

		if (StringUtils.isNotBlank(scope)) {
			sb.append(String.format("&scope=%s", URLEncoder.encode(scope, StandardCharsets.UTF_8)));
		}

		return HttpRequest.BodyPublishers.ofString(sb.toString());
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	private record TokenResponse(
			@JsonProperty("access_token") String accessToken,
			@JsonProperty("token_type") String tokenType,
			@JsonProperty("expires_in") long expiresIn) {
	}
}
