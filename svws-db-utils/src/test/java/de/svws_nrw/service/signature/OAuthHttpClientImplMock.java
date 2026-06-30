package de.svws_nrw.service.signature;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import javax.net.ssl.SSLSession;

import de.svws_nrw.oauth.OAuthHttpClient;
import de.svws_nrw.oauth.OAuthScope;

/**
 * Mock-OAuthHttpClient, der eine externe-API für API Tests simuliert.
 */
public final class OAuthHttpClientImplMock implements OAuthHttpClient {

	private int statusCode;
	private Object responseBody;

	@Override
	@SuppressWarnings("unchecked")
	public <R> HttpResponse<R> send(final HttpRequest baseRequest, final OAuthScope scope, final HttpResponse.BodyHandler<R> bodyHandler) {
		return new MockHttpResponse<>(statusCode, (R) responseBody, baseRequest);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <R> HttpResponse<R> send(final HttpRequest baseRequest, final OAuthScope scope, final Class<R> type) {
		return new MockHttpResponse<>(statusCode, (R) responseBody, baseRequest);
	}

	/**
	 * Konfiguriert den Mock-Client für eine erfolgreiche Antwort.
	 * @param responseBody der Response-Body
	 */
	public void respondWithSuccess(final Object responseBody) {
		this.statusCode = 200;
		this.responseBody = responseBody;
	}

	/**
	 * Konfiguriert den Mock-Client für eine Fehlerantwort.
	 *
	 * @param statusCode der HTTP-Statuscode
	 */
	public void respondWithError(final int statusCode) {
		this.statusCode = statusCode;
		this.responseBody = null;
	}

	/**
	 * Konfiguriert den Mock-Client für eine 200-Antwort mit leerem Body.
	 */
	public void respondWithEmptyBody() {
		this.statusCode = 200;
		this.responseBody = null;
	}


	private static final class MockHttpResponse<R> implements HttpResponse<R> {

		private final int statusCode;
		private final R body;
		private final HttpRequest request;

		private MockHttpResponse(final int statusCode, final R body, final HttpRequest request) {
			this.statusCode = statusCode;
			this.body = body;
			this.request = request;
		}

		@Override
		public int statusCode() {
			return statusCode;
		}

		@Override
		public R body() {
			return body;
		}

		@Override
		public HttpRequest request() {
			return request;
		}

		// Stubs:
		@Override
		public java.util.Optional<HttpResponse<R>> previousResponse() {
			return java.util.Optional.empty();
		}

		@Override
		public java.net.http.HttpHeaders headers() {
			return java.net.http.HttpHeaders.of(java.util.Map.of(), (a, b) -> true);
		}

		@Override
		public Optional<SSLSession> sslSession() {
			return java.util.Optional.empty();
		}

		@Override
		public URI uri() {
			return request.uri();
		}

		@Override
		public HttpClient.Version version() {
			return java.net.http.HttpClient.Version.HTTP_1_1;
		}
	}

}
