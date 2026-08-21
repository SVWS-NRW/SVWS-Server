package de.svws_nrw.oauth.internal;

import java.net.URI;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import de.svws_nrw.oauth.CredStoreService;
import de.svws_nrw.oauth.OAuthScope;
import de.svws_nrw.oauth.Schema;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit-Tests fuer {@link CachingTokenProvider}.
 */
class CachingTokenProviderTest {

	private CredStoreService credStoreService;
	private OAuthFlow flow;
	private CachingTokenProvider cut;

	@BeforeEach
	void setup() {
		final Clock clock = Clock.fixed(Instant.parse("2026-06-18T12:00:00Z"), ZoneOffset.UTC);
		credStoreService = mock(CredStoreService.class);
		flow = mock(OAuthFlow.class);
		cut = new CachingTokenProvider(credStoreService, flow, clock);
	}

	@AfterEach
	@DisplayName("invalidate | clears static cache between tests")
	void tearDown() {
		// CACHE ist static; wir invalidieren ueber den bekannten Key, um Test-Isolation zu erhalten.
		// Da wir den CacheKey-Typ nicht sehen, nutzen wir mehrere invalidates fuer die verwendeten Keys.
		final Schema schema = new Schema("tenant_schema_a");
		final CachingTokenProvider provider = new CachingTokenProvider(mock(CredStoreService.class), mock(OAuthFlow.class));
		provider.invalidate(schema, OAuthScope.DEFAULT);
		provider.invalidate(schema, null);
	}

	@Test
	@DisplayName("getToken | returns cached token when valid")
	void getTokenReturnsCachedTokenWhenValid() {

		final Schema schema = new Schema("tenant_schema_a");
		final Credentials creds = new Credentials("client-id", "client-secret", URI.create("https://issuer.example/token"), null, OAuthDomain.IT_NRW);
		when(credStoreService.getBySchema(schema)).thenReturn(creds);

		final AccessToken token = new AccessToken("v1", "Bearer", Instant.parse("2026-06-18T12:10:00Z"));
		when(flow.acquire(creds, OAuthScope.DEFAULT)).thenReturn(token);

		cut.getToken(schema, OAuthScope.DEFAULT);

		verify(flow, times(1)).acquire(creds, OAuthScope.DEFAULT);
	}

	@Test
	@DisplayName("getToken | with null scope")
	void getTokenNormalizesBlankScopeToEmptyString() {

		final Schema schema = new Schema("tenant_schema_a");
		final Credentials creds = new Credentials("client-id", "client-secret", URI.create("https://issuer.example/token"), null, OAuthDomain.IT_NRW);
		when(credStoreService.getBySchema(schema)).thenReturn(creds);

		final AccessToken token = new AccessToken("v1", "Bearer", Instant.parse("2026-06-18T12:10:00Z"));
		when(flow.acquire(creds, null)).thenReturn(token);

		final AccessToken actual = cut.getToken(schema, null);
		assertSame(token, actual);
		verify(flow, times(1)).acquire(creds, null);
	}

	@Test
	@DisplayName("getToken | fetches new token when cached token is expired")
	void getTokenFetchesNewTokenWhenCachedTokenIsExpired() {
		final Instant base = Instant.parse("2026-06-18T12:00:00Z");

		final Schema schema = new Schema("tenant_schema_a");
		final Credentials creds = new Credentials("client-id", "client-secret", URI.create("https://issuer.example/token"), null, OAuthDomain.IT_NRW);
		when(credStoreService.getBySchema(schema)).thenReturn(creds);

		final AccessToken expired = new AccessToken("old", "Bearer", base);
		final AccessToken fresh = new AccessToken("new", "Bearer", base.plusSeconds(60));
		when(flow.acquire(creds, OAuthScope.DEFAULT))
				.thenReturn(expired)
				.thenReturn(fresh);

		final AccessToken first = cut.getToken(schema, OAuthScope.DEFAULT);
		final AccessToken second = cut.getToken(schema, OAuthScope.DEFAULT);

		assertSame(expired, first);
		assertSame(fresh, second);
		verify(flow, times(2)).acquire(creds, OAuthScope.DEFAULT);
	}

	@Test
	@DisplayName("invalidate | removes cached entry and forces refetch")
	void invalidateRemovesCachedEntryAndForcesRefetch() {

		final Schema schema = new Schema("tenant_schema_a");
		final Credentials creds = new Credentials("client-id", "client-secret", URI.create("https://issuer.example/token"), null, OAuthDomain.IT_NRW);
		when(credStoreService.getBySchema(schema)).thenReturn(creds);

		final AccessToken token1 = new AccessToken("v1", "Bearer", Instant.parse("2026-06-18T12:10:00Z"));
		final AccessToken token2 = new AccessToken("v2", "Bearer", Instant.parse("2026-06-18T12:10:00Z"));
		when(flow.acquire(creds, OAuthScope.DEFAULT))
				.thenReturn(token1)
				.thenReturn(token2);

		final AccessToken first = cut.getToken(schema, OAuthScope.DEFAULT);
		cut.invalidate(schema, OAuthScope.DEFAULT);
		final AccessToken second = cut.getToken(schema, OAuthScope.DEFAULT);

		assertNotEquals(first, second);
		verify(flow, times(2)).acquire(creds, OAuthScope.DEFAULT);
	}

}
