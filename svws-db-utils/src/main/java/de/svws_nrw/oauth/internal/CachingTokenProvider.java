package de.svws_nrw.oauth.internal;

import java.time.Clock;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import de.svws_nrw.oauth.CredStoreService;
import de.svws_nrw.oauth.Schema;
import de.svws_nrw.oauth.TokenProvider;
import org.apache.commons.lang3.StringUtils;

/**
 * Verwaltet OAuth-Tokens: Cache pro {@code (schema, scope)}-Kombination
 * mit Single-Flight-Mechanismus gegen parallele Token-Requests.
 *
 * <p>Single-Flight: Bei gleichzeitigem Zugriff mehrerer Threads auf einen
 * fehlenden oder abgelaufenen Token laeuft nur ein einziger Token-Request
 * zum IdP. Alle weiteren Threads warten auf dasselbe {@link CompletableFuture}.
 *
 * <p>Die Gueltigkeitspruefung erfolgt ueber {@link AccessToken#isValidAt(Clock)}.
 * Abgelaufene Eintraege werden vor dem naechsten Fetch aus dem Cache entfernt.
 */
public final class CachingTokenProvider implements TokenProvider {

	private final CredStoreService credStoreService;
	private final OAuthFlow flow;
	private final Clock clock;

	private static final ConcurrentMap<TokenCacheKey, CompletableFuture<AccessToken>> CACHE = new ConcurrentHashMap<>();

	/**
	 * Konstruktor mit System-UTC-Clock.
	 *
	 * @param credStoreService Store fuer schema-spezifische OAuth-Credentials
	 * @param flow            Flow-Strategie zum Beschaffen neuer Tokens
	 */
	public CachingTokenProvider(final CredStoreService credStoreService, final OAuthFlow flow) {
		this(credStoreService, flow, Clock.systemUTC());
	}

	/**
	 * Konstruktor mit expliziter Clock (z.B. fuer Tests).
	 *
	 * @param credStoreService Store fuer schema-spezifische OAuth-Credentials
	 * @param flow            Flow-Strategie zum Beschaffen neuer Tokens
	 * @param clock           Zeitquelle fuer Gueltigkeitspruefungen
	 */
	public CachingTokenProvider(final CredStoreService credStoreService, final OAuthFlow flow, final Clock clock) {
		this.credStoreService = Objects.requireNonNull(credStoreService);
		this.flow = Objects.requireNonNull(flow);
		this.clock = Objects.requireNonNull(clock);
	}

	/**
	 * Liefert einen gueltigen {@link AccessToken} fuer {@code (schema, scope)}.
	 *
	 * <p>Gibt einen gecachten Token zurueck falls vorhanden und gueltig.
	 * Andernfalls wird ein neuer Token per {@link OAuthFlow#acquire} beschafft.
	 * Fehlgeschlagene Fetch-Versuche werden aus dem Cache entfernt.
	 *
	 * @param schema DB-Schema / Mandant
	 * @param scope  OAuth-Scope; {@code null} oder leer nutzt den Default-Scope der Credentials
	 * @return gueltiger {@link AccessToken}
	 */
	@Override
	public AccessToken getToken(final Schema schema, final String scope) {
		final TokenCacheKey key = new TokenCacheKey(schema, normalizeScope(scope));

		final CompletableFuture<AccessToken> cached = CACHE.get(key);
		if ((cached != null) && !cached.isCompletedExceptionally()) {
			final AccessToken token = cached.join();
			if (token.isValidAt(clock)) {
				return token;
			}
			CACHE.remove(key, cached);
		}

		final CompletableFuture<AccessToken> fetchedToken = CACHE.computeIfAbsent(key, this::acquireTokenAsync);
		try {
			return fetchedToken.join();
		} catch (final RuntimeException e) {
			CACHE.remove(key, fetchedToken);
			throw e;
		}
	}

	/**
	 * Entfernt den gecachten Token fuer {@code (schema, scope)}.
	 * Erzwingt beim naechsten {@link #getToken}-Aufruf einen neuen Token-Request.
	 *
	 * @param schema DB-Schema / Mandant
	 * @param scope  OAuth-Scope
	 */
	@Override
	public void invalidate(final Schema schema, final String scope) {
		CACHE.remove(new TokenCacheKey(schema, normalizeScope(scope)));
	}

	private CompletableFuture<AccessToken> acquireTokenAsync(final TokenCacheKey key) {
		return CompletableFuture.supplyAsync(() -> {
			final Credentials creds = credStoreService.getBySchema(key.schema());
			return flow.acquire(creds, key.scope());
		});
	}

	private static String normalizeScope(final String scope) {
		return StringUtils.defaultIfBlank(scope, "").trim();
	}

	private record TokenCacheKey(Schema schema, String scope) {
	}

}
