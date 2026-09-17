package de.svws_nrw.oauth.internal;

import java.time.Clock;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

import de.svws_nrw.oauth.OAuthScope;
import de.svws_nrw.oauth.Schema;
import de.svws_nrw.oauth.TokenProvider;

/**
 * Verwaltet OAuth-Tokens: Cache pro {@code (schema, domain, scope)}-Kombination
 * mit Single-Flight-Mechanismus gegen parallele Token-Requests.
 *
 * <p>Single-Flight: Bei gleichzeitigem Zugriff mehrerer Threads auf einen
 * fehlenden oder abgelaufenen Token laeuft nur ein einziger Token-Request
 * zum IdP. Alle weiteren Threads warten auf dasselbe {@link CompletableFuture}.
 *
 * <p>Die Gueltigkeitspruefung erfolgt ueber {@link AccessToken#isValidAt(Clock)}.
 * Abgelaufene Eintraege werden vor dem naechsten Fetch aus dem Cache entfernt.
 *
 * <p>Der Cache wird per Konstruktor injiziert und muss von aussen als Singleton
 * verwaltet werden (eine gemeinsame {@link ConcurrentMap}-Instanz ueber alle
 * Requests hinweg), damit Tokens tatsaechlich ueber mehrere Aufrufe hinweg
 * wiederverwendet werden koennen.
 */
public final class CachingTokenProvider implements TokenProvider {

	private final CredentialStore credentialStore;
	private final OAuthFlow flow;
	private final Clock clock;
	private final ConcurrentMap<TokenCacheKey, CompletableFuture<AccessToken>> cache;

	/**
	 * Konstruktor mit System-UTC-Clock.
	 *
	 * @param credentialStore Store fuer domaenenspezifische OAuth-Credentials
	 * @param flow            Flow-Strategie zum Beschaffen neuer Tokens
	 * @param cache           Token-Cache; muss eine ueber alle Requests hinweg geteilte
	 *                        Singleton-Instanz sein
	 */
	public CachingTokenProvider(final CredentialStore credentialStore, final OAuthFlow flow,
			final ConcurrentMap<TokenCacheKey, CompletableFuture<AccessToken>> cache) {
		this(credentialStore, flow, cache, Clock.systemUTC());
	}

	/**
	 * Konstruktor mit Clock (z.B. fuer Tests).
	 *
	 * @param credentialStore Store fuer domaenenspezifische OAuth-Credentials
	 * @param flow            Flow-Strategie zum Beschaffen neuer Tokens
	 * @param cache           Token-Cache; muss eine ueber alle Requests hinweg geteilte
	 *                        Singleton-Instanz sein
	 * @param clock           Zeitquelle fuer Gueltigkeitspruefungen
	 */
	public CachingTokenProvider(final CredentialStore credentialStore, final OAuthFlow flow,
			final ConcurrentMap<TokenCacheKey, CompletableFuture<AccessToken>> cache, final Clock clock) {
		this.credentialStore = Objects.requireNonNull(credentialStore);
		this.flow = Objects.requireNonNull(flow);
		this.cache = Objects.requireNonNull(cache);
		this.clock = Objects.requireNonNull(clock);
	}

	/**
	 * Liefert einen gueltigen {@link AccessToken} fuer {@code (schema, domain, scope)}.
	 *
	 * <p>Gibt einen gecachten Token zurueck falls vorhanden und gueltig.
	 * Andernfalls wird ein neuer Token per {@link OAuthFlow#acquire} beschafft.
	 * Fehlgeschlagene Fetch-Versuche werden aus dem Cache entfernt.
	 *
	 * @param schema DB-Schema / Mandant
	 * @param domain die OAuth-Domaene
	 * @param scope  OAuth-Scope; {@code null} oder leer nutzt den Default-Scope der Credentials
	 * @return gueltiger {@link AccessToken}
	 */
	@Override
	public AccessToken getToken(final Schema schema, final OAuthDomain domain, final OAuthScope scope) {
		final TokenCacheKey key = new TokenCacheKey(schema, domain, scope);

		return getCachedToken(key).orElseGet(() -> fetchTokenAndUpdateCache(key));
	}

	private Optional<AccessToken> getCachedToken(final TokenCacheKey key) {
		final CompletableFuture<AccessToken> future = cache.get(key);
		if ((future == null) || future.isCompletedExceptionally()) {
			return Optional.empty();
		}

		final AccessToken token = future.join();
		if (!token.isValidAt(clock)) {
			cache.remove(key, future);
			return Optional.empty();
		}

		return Optional.of(token);
	}

	private AccessToken fetchTokenAndUpdateCache(final TokenCacheKey key) {
		final CompletableFuture<AccessToken> fetchedToken = cache.computeIfAbsent(key, this::acquireToken);
		try {
			return fetchedToken.join();
		} catch (final RuntimeException e) {
			cache.remove(key, fetchedToken);
			throw e;
		}
	}

	/**
	 * Entfernt den gecachten Token fuer {@code (schema, domain, scope)}.
	 * Erzwingt beim naechsten {@link #getToken}-Aufruf einen neuen Token-Request.
	 *
	 * @param schema DB-Schema / Mandant
	 * @param domain die OAuth-Domaene
	 * @param scope  OAuth-Scope
	 */
	@Override
	public void invalidate(final Schema schema, final OAuthDomain domain, final OAuthScope scope) {
		cache.remove(new TokenCacheKey(schema, domain, scope));
	}

	private CompletableFuture<AccessToken> acquireToken(final TokenCacheKey key) {
		final CompletableFuture<AccessToken> future = new CompletableFuture<>();
		try {
			future.complete(flow.acquire(getCredentials(key.domain()), key.scope()));
		} catch (final Exception e) {
			future.completeExceptionally(e);
		}
		return future;
	}

	private Credentials getCredentials(final OAuthDomain domain) {
		return Optional.ofNullable(domain)
				.flatMap(credentialStore::get)
				.orElseThrow(() -> new UnknownDomainException(domain));
	}

}
