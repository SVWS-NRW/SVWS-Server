package de.svws_nrw.oauth;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.oauth.internal.AccessToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit-Tests fuer {@link OAuthHttpClientImpl}.
 */
@ExtendWith(MockitoExtension.class)
class OAuthHttpClientImplTest {

	@Mock
	private HttpClient delegate;

	@Mock
	private TokenProvider tokenProvider;

	@Mock
	private SchemaService schemaService;

	@InjectMocks
	private OAuthHttpClientImpl cut;

	@Test
	@DisplayName("send | sets authorization header")
	void sendSetsAuthorizationHeader() throws Exception {
		when(schemaService.getActiveSchema()).thenReturn("tenant_schema_a");
		final AccessToken token = mock(AccessToken.class);
		when(token.asAuthorizationHeader()).thenReturn("Bearer test-token");
		when(tokenProvider.getToken(any(Schema.class), eq("scope-a"))).thenReturn(token);

		final HttpResponse<String> response = mock(HttpResponse.class);
		when(response.statusCode()).thenReturn(200);
		when(delegate.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);

		final HttpRequest baseRequest = HttpRequest.newBuilder(URI.create("https://api.example/resource")).GET().build();
		cut.send(baseRequest, "scope-a", HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		final ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
		verify(delegate).send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));
		final HttpRequest authed = requestCaptor.getValue();
		assertEquals("Bearer test-token", authed.headers().firstValue("Authorization").orElseThrow());

	}

	@Test
	@DisplayName("send | retries once and invalidates token on 401")
	void sendRetriesOnceAndInvalidatesTokenOn401() throws Exception {
		when(schemaService.getActiveSchema()).thenReturn("tenant_schema_a");
		final AccessToken token = mock(AccessToken.class);
		when(token.asAuthorizationHeader()).thenReturn("Bearer test-token");
		when(tokenProvider.getToken(any(Schema.class), eq("scope-a"))).thenReturn(token);

		final HttpResponse<String> response401 = mock(HttpResponse.class);
		when(response401.statusCode()).thenReturn(401);
		final HttpResponse<String> response200 = mock(HttpResponse.class);

		when(delegate.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
				.thenReturn(response401)
				.thenReturn(response200);

		final HttpRequest baseRequest = HttpRequest.newBuilder(URI.create("https://api.example/resource")).GET().build();
		final HttpResponse<String> actual = cut.send(baseRequest, "scope-a", HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

		assertSame(response200, actual);
		verify(delegate, times(2)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));

		final ArgumentCaptor<Schema> schemaCaptor = ArgumentCaptor.forClass(Schema.class);
		verify(tokenProvider, times(1)).invalidate(schemaCaptor.capture(), eq("scope-a"));
		assertEquals("tenant_schema_a", schemaCaptor.getValue().name());
	}

	@Test
	@DisplayName("send | deserializes json response body for class overload")
	void sendDeserializesJsonResponseBodyForClassOverload() throws Exception {
		when(schemaService.getActiveSchema()).thenReturn("tenant_schema_a");
		final AccessToken token = mock(AccessToken.class);
		when(token.asAuthorizationHeader()).thenReturn("Bearer test-token");
		when(tokenProvider.getToken(any(Schema.class), isNull())).thenReturn(token);

		final ArgumentCaptor<HttpResponse.BodyHandler<Object>> handlerCaptor = ArgumentCaptor.forClass(HttpResponse.BodyHandler.class);

		final HttpResponse<Object> response = mock(HttpResponse.class);
		when(response.statusCode()).thenReturn(200);
		when(delegate.send(any(HttpRequest.class), handlerCaptor.capture())).thenReturn(response);

		final HttpRequest baseRequest = HttpRequest.newBuilder(URI.create("https://api.example/resource"))
				.GET()
				.build();

		cut.send(baseRequest, null, TestDto.class);

		final HttpResponse.BodyHandler<Object> handler = handlerCaptor.getValue();
		final HttpResponse.ResponseInfo responseInfo = mock(HttpResponse.ResponseInfo.class);

		final HttpResponse.BodySubscriber<Object> subscriber = handler.apply(responseInfo);
		subscriber.onSubscribe(new java.util.concurrent.Flow.Subscription() {
			@Override
			public void request(final long n) {
				//mocked
			}

			@Override
			public void cancel() {
				//mocked
			}
		});

		final String json = "{\"name\":\"alice\",\"count\":7}";
		subscriber.onNext(java.util.List.of(java.nio.ByteBuffer.wrap(json.getBytes(StandardCharsets.UTF_8))));
		subscriber.onComplete();

		final Object decoded = subscriber.getBody().toCompletableFuture().join();
		assertInstanceOf(TestDto.class, decoded);
		final TestDto dto = (TestDto) decoded;
		assertEquals("alice", dto.name);
		assertEquals(7, dto.count);
	}

	@Test
	@DisplayName("send | throws api operation exception and preserves interrupt on io or interrupt")
	void sendThrowsApiOperationExceptionAndPreservesInterruptOnIoOrInterrupt() throws Exception {
		when(schemaService.getActiveSchema()).thenReturn("tenant_schema_a");
		final AccessToken token = mock(AccessToken.class);
		when(token.asAuthorizationHeader()).thenReturn("Bearer test-token");
		when(tokenProvider.getToken(any(Schema.class), eq("scope-a"))).thenReturn(token);

		when(delegate.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
				.thenThrow(new IOException("io boom"));

		final HttpRequest baseRequest = HttpRequest.newBuilder(URI.create("https://api.example/resource")).GET().build();
		final HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8);
		final ApiOperationException ex = assertThrows(ApiOperationException.class,
				() -> cut.send(baseRequest, "scope-a", bodyHandler));
		assertNotNull(ex);
		assertTrue(Thread.currentThread().isInterrupted());

		// cleanup interrupt for other tests
		Thread.interrupted();
	}

	private record TestDto(
			String name, int count) {
	}

}
