package de.svws_nrw.oauth;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.oauth.internal.AccessToken;
import jakarta.ws.rs.core.Response;
import org.eclipse.jetty.http.HttpHeader;

/**
 * HTTP-Client fuer OAuth-gesicherte Endpunkte.
 *
 * <p>Kapselt die gesamte OAuth-Logik transparent: Das aktive Schema wird automatisch
 * ueber den {@link SchemaService} aufgeloest, der Bearer-Token wird fuer jede Anfrage
 * gesetzt. Bei HTTP 401 wird der gecachte Token einmalig invalidiert und der Request
 * wiederholt, um serverseitig revozierte oder abgelaufene Tokens abzufangen.
 *
 * <p>Services kennen weder Credentials noch Token-Lifecycle -- sie rufen nur {@link #send} auf.
 *
 * @see TokenProvider
 * @see SchemaService
 */
public final class OAuthHttpClientImpl implements OAuthHttpClient {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private final HttpClient delegate;
	private final TokenProvider tokenProvider;
	private final SchemaService schemaService;

	/**
	 * Konstruktor.
	 *
	 * @param delegate      der zugrundeliegende {@link HttpClient} fuer die eigentliche HTTP-Kommunikation
	 * @param tokenProvider liefert gueltige Bearer-Tokens pro Schema und Scope
	 * @param schemaService liefert das aktive DB-Schema des aktuellen Requests
	 */
	public OAuthHttpClientImpl(final HttpClient delegate, final TokenProvider tokenProvider, final SchemaService schemaService) {
		this.delegate = Objects.requireNonNull(delegate);
		this.tokenProvider = Objects.requireNonNull(tokenProvider);
		this.schemaService = schemaService;
	}

	@Override
	public <T> HttpResponse<T> send(final HttpRequest baseRequest, final String scope, final HttpResponse.BodyHandler<T> bodyHandler) {
		return sendInternal(baseRequest, scope, bodyHandler);
	}

	@Override
	public <T> HttpResponse<T> send(final HttpRequest baseRequest, final String scope, final Class<T> type) {
		return sendInternal(baseRequest, scope, jsonBodyHandler(type));
	}

	private <T> HttpResponse<T> sendInternal(final HttpRequest baseRequest, final String scope, final HttpResponse.BodyHandler<T> bodyHandler) {
		final var schema = new Schema(schemaService.getActiveSchema());
		HttpResponse<T> response = sendWithToken(baseRequest, schema, scope, bodyHandler);

		if (response.statusCode() == 401) {
			tokenProvider.invalidate(schema, scope);
			response = sendWithToken(baseRequest, schema, scope, bodyHandler);
		}
		return response;
	}

	private <T> HttpResponse.BodyHandler<T> jsonBodyHandler(final Class<T> type) {
		return responseInfo -> HttpResponse.BodySubscribers.mapping(
				HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
				body -> {
					try {
						return OBJECT_MAPPER.readValue(body, type);
					} catch (final JsonProcessingException e) {
						throw new ApiOperationException(
								Response.Status.INTERNAL_SERVER_ERROR,
								String.format("Failed to deserialize response body: %s", e.getMessage())
						);
					}
				});
	}

	private <T> HttpResponse<T> sendWithToken(final HttpRequest baseRequest, final Schema schema, final String scope,
			final HttpResponse.BodyHandler<T> bodyHandler) {
		final AccessToken token = tokenProvider.getToken(schema, scope);
		final HttpRequest authedRequest = HttpRequest.newBuilder(baseRequest, (k, v) -> true)
				.setHeader(HttpHeader.AUTHORIZATION.name(), token.asAuthorizationHeader())
				.build();
		try {
			return delegate.send(authedRequest, bodyHandler);
		} catch (final InterruptedException | IOException e) {
			Thread.currentThread().interrupt();
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
