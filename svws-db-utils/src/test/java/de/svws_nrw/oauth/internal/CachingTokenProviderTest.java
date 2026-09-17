package de.svws_nrw.oauth.internal;

import java.net.URI;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import de.svws_nrw.oauth.OAuthScope;
import de.svws_nrw.oauth.Schema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit-Tests fuer {@link CachingTokenProvider}.
 */
class CachingTokenProviderTest {

	private CredentialStore credentialStore;
	private OAuthFlow flow;
	private CachingTokenProvider cut;
	private Clock clock;

	@BeforeEach
	void setup() {
		clock = Clock.fixed(Instant.parse("2026-06-18T12:00:00Z"), ZoneOffset.UTC);
		credentialStore = mock(CredentialStore.class);
		flow = mock(OAuthFlow.class);
		final ConcurrentMap<TokenCacheKey, CompletableFuture<AccessToken>> cache = new ConcurrentHashMap<>();
		cut = new CachingTokenProvider(credentialStore, flow, cache, clock);
	}

	@Test
	@DisplayName("getToken | returns cached token when valid")
	void getTokenReturnsCachedTokenWhenValid() {

		final Schema schema = new Schema("tenant_schema_a");
		final Credentials creds = new Credentials("client-id", "client-secret", URI.create("https://issuer.example/token"), null, OAuthDomain.IT_NRW);
		when(credentialStore.get(OAuthDomain.IT_NRW)).thenReturn(Optional.of(creds));

		final AccessToken token = new AccessToken("v1", "Bearer", Instant.parse("2026-06-18T12:10:00Z"));
		when(flow.acquire(creds, OAuthScope.DEFAULT)).thenReturn(token);

		final AccessToken first = cut.getToken(schema, OAuthDomain.IT_NRW, OAuthScope.DEFAULT);
		final AccessToken second = cut.getToken(schema, OAuthDomain.IT_NRW, OAuthScope.DEFAULT);

		assertSame(token, first);
		assertSame(token, second);
		verify(flow, times(1)).acquire(creds, OAuthScope.DEFAULT);
	}

	@Test
	@DisplayName("getToken | supports null scope")
	void getTokenSupportsNullScope() {

		final Schema schema = new Schema("tenant_schema_a");
		final Credentials creds = new Credentials("client-id", "client-secret", URI.create("https://issuer.example/token"), null, OAuthDomain.IT_NRW);
		when(credentialStore.get(OAuthDomain.IT_NRW)).thenReturn(Optional.of(creds));

		final AccessToken token = new AccessToken("v1", "Bearer", Instant.parse("2026-06-18T12:10:00Z"));
		when(flow.acquire(creds, null)).thenReturn(token);

		final AccessToken actual = cut.getToken(schema, OAuthDomain.IT_NRW, null);
		assertSame(token, actual);
		verify(flow, times(1)).acquire(creds, null);
	}

	@Test
	@DisplayName("getToken | fetches new token when cached token is expired")
	void getTokenFetchesNewTokenWhenCachedTokenIsExpired() {
		final Instant now = clock.instant();

		final Schema schema = new Schema("tenant_schema_a");
		final Credentials creds = new Credentials("client-id", "client-secret", URI.create("https://issuer.example/token"), null, OAuthDomain.IT_NRW);
		when(credentialStore.get(OAuthDomain.IT_NRW)).thenReturn(Optional.of(creds));

		final AccessToken expired = new AccessToken("old", "Bearer", now.minusSeconds(60));
		final AccessToken fresh = new AccessToken("new", "Bearer", now.plusSeconds(60));
		when(flow.acquire(creds, OAuthScope.DEFAULT))
				.thenReturn(expired)
				.thenReturn(fresh);

		final AccessToken first = cut.getToken(schema, OAuthDomain.IT_NRW, OAuthScope.DEFAULT);
		final AccessToken second = cut.getToken(schema, OAuthDomain.IT_NRW, OAuthScope.DEFAULT);

		assertSame(expired, first);
		assertSame(fresh, second);
		verify(flow, times(2)).acquire(creds, OAuthScope.DEFAULT);
	}

	@Test
	@DisplayName("invalidate | removes cached entry and forces refetch")
	void invalidateRemovesCachedEntryAndForcesRefetch() {

		final Schema schema = new Schema("tenant_schema_a");
		final Credentials creds = new Credentials("client-id", "client-secret", URI.create("https://issuer.example/token"), null, OAuthDomain.IT_NRW);
		when(credentialStore.get(OAuthDomain.IT_NRW)).thenReturn(Optional.of(creds));

		final AccessToken token1 = new AccessToken("v1", "Bearer", Instant.parse("2026-06-18T12:10:00Z"));
		final AccessToken token2 = new AccessToken("v2", "Bearer", Instant.parse("2026-06-18T12:10:00Z"));
		when(flow.acquire(creds, OAuthScope.DEFAULT))
				.thenReturn(token1)
				.thenReturn(token2);

		final AccessToken first = cut.getToken(schema, OAuthDomain.IT_NRW, OAuthScope.DEFAULT);
		cut.invalidate(schema, OAuthDomain.IT_NRW, OAuthScope.DEFAULT);
		final AccessToken second = cut.getToken(schema, OAuthDomain.IT_NRW, OAuthScope.DEFAULT);

		assertSame(token1, first);
		assertSame(token2, second);
		verify(flow, times(2)).acquire(creds, OAuthScope.DEFAULT);
	}

	@Test
	@DisplayName("getToken | different domains with same (schema, scope) yield separate tokens")
	void getTokenDifferentDomainsYieldSeparateTokens() {
		final Schema schema = new Schema("tenant_schema_a");
		final Credentials itNrwCreds = new Credentials("client-id-1", "client-secret-1", URI.create("https://issuer.example/token"), null, OAuthDomain.IT_NRW);
		final Credentials wenomCreds = new Credentials("client-id-2", "client-secret-2", URI.create("https://issuer.example/token"), null, OAuthDomain.WENOM);
		when(credentialStore.get(OAuthDomain.IT_NRW)).thenReturn(Optional.of(itNrwCreds));
		when(credentialStore.get(OAuthDomain.WENOM)).thenReturn(Optional.of(wenomCreds));

		final AccessToken itNrwToken = new AccessToken("it-nrw-token", "Bearer", Instant.parse("2026-06-18T12:10:00Z"));
		final AccessToken wenomToken = new AccessToken("wenom-token", "Bearer", Instant.parse("2026-06-18T12:10:00Z"));
		when(flow.acquire(itNrwCreds, OAuthScope.DEFAULT)).thenReturn(itNrwToken);
		when(flow.acquire(wenomCreds, OAuthScope.DEFAULT)).thenReturn(wenomToken);

		final AccessToken first = cut.getToken(schema, OAuthDomain.IT_NRW, OAuthScope.DEFAULT);
		final AccessToken second = cut.getToken(schema, OAuthDomain.WENOM, OAuthScope.DEFAULT);

		assertSame(itNrwToken, first);
		assertSame(wenomToken, second);
		verify(flow, times(1)).acquire(itNrwCreds, OAuthScope.DEFAULT);
		verify(flow, times(1)).acquire(wenomCreds, OAuthScope.DEFAULT);
	}

	@Test
	@DisplayName("getToken | passes credentials from store unchanged to flow, including tokenUrl from DB")
	void getTokenPassesCredentialsUnchangedToFlow() {
		final Schema schema = new Schema("tenant_schema_a");
		final URI dbTokenUrl = URI.create("https://db-issuer.example/token");
		final Credentials creds = new Credentials("client-id", "client-secret", dbTokenUrl, null, OAuthDomain.IT_NRW);
		when(credentialStore.get(OAuthDomain.IT_NRW)).thenReturn(Optional.of(creds));

		final AccessToken token = new AccessToken("v1", "Bearer", Instant.parse("2026-06-18T12:10:00Z"));
		when(flow.acquire(creds, OAuthScope.DEFAULT)).thenReturn(token);

		cut.getToken(schema, OAuthDomain.IT_NRW, OAuthScope.DEFAULT);

		final org.mockito.ArgumentCaptor<Credentials> credsCaptor = org.mockito.ArgumentCaptor.forClass(Credentials.class);
		verify(flow, times(1)).acquire(credsCaptor.capture(), org.mockito.ArgumentMatchers.eq(OAuthScope.DEFAULT));
		assertSame(dbTokenUrl, credsCaptor.getValue().authServerUrl());
	}

	@Test
	@DisplayName("getToken | wirft UnknownDomainException wenn der Store keine Credentials liefert")
	void getTokenThrowsWhenStoreHasNoCredentials() {
		final Schema schema = new Schema("tenant_schema_a");
		when(credentialStore.get(OAuthDomain.IT_NRW)).thenReturn(Optional.empty());

		final CompletionException thrown = assertThrows(CompletionException.class,
				() -> cut.getToken(schema, OAuthDomain.IT_NRW, OAuthScope.DEFAULT));

		assertInstanceOf(UnknownDomainException.class, thrown.getCause());
		verifyNoInteractions(flow);
	}

	@Test
	@DisplayName("getToken | wirft UnknownDomainException bei null-Domaene ohne den Store zu befragen")
	void getTokenThrowsForNullDomainWithoutQueryingStore() {
		final Schema schema = new Schema("tenant_schema_a");

		final CompletionException thrown = assertThrows(CompletionException.class,
				() -> cut.getToken(schema, null, OAuthScope.DEFAULT));

		assertInstanceOf(UnknownDomainException.class, thrown.getCause());
		verifyNoInteractions(credentialStore);
		verifyNoInteractions(flow);
	}

}
