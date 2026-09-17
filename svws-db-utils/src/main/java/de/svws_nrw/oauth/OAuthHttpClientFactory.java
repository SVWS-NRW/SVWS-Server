package de.svws_nrw.oauth;

import java.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import de.svws_nrw.oauth.internal.AccessToken;
import de.svws_nrw.oauth.internal.CachingTokenProvider;
import de.svws_nrw.oauth.internal.ClientCredentialsFlow;
import de.svws_nrw.oauth.internal.CredentialStore;
import de.svws_nrw.oauth.internal.OAuthDomain;
import de.svws_nrw.oauth.internal.OAuthFlow;
import de.svws_nrw.oauth.internal.TokenCacheKey;

public final class OAuthHttpClientFactory {

	/** Token-Cache fuer {@link CachingTokenProvider}; Singleton-Instanz, geteilt ueber alle Requests hinweg. */
	private static final ConcurrentMap<TokenCacheKey, CompletableFuture<AccessToken>> TOKEN_CACHE = new ConcurrentHashMap<>();

	private final SchemaServiceFactory schemaServiceFactory;

	/**
	 * Konstruktor.
	 *
	 * @param schemaServiceFactory Factory zur Beschaffung des aktiven DB-Schemas
	 */
	private OAuthHttpClientFactory(final SchemaServiceFactory schemaServiceFactory) {
		this.schemaServiceFactory = schemaServiceFactory;
	}

	/**
	 * Erzeugt eine neue {@link OAuthHttpClientFactory} mit einer frischen
	 * {@link SchemaServiceFactory}.
	 *
	 * @return neue Instanz dieser Factory
	 */
	public static OAuthHttpClientFactory getNewInstance() {
		return new OAuthHttpClientFactory(SchemaServiceFactory.getNewInstance());
	}

	/**
	 * Erzeugt neuen {@link OAuthHttpClientImpl}
	 *
	 * @param domain die OAuth-Domaene, an die der Client gebunden wird
	 *
	 * @return {@link OAuthHttpClientImpl}
	 */
	public OAuthHttpClient getClient(final OAuthDomain domain) {
		final OAuthFlow flow = new ClientCredentialsFlow();
		final CredentialStore credentialStore = CredentialStoreFactory.getNewInstance()
				.getCredentialStore();
		final TokenProvider tokenProvider = new CachingTokenProvider(credentialStore, flow, TOKEN_CACHE);
		final SchemaService schemaService = schemaServiceFactory.getService();

		return new OAuthHttpClientImpl(HttpClient.newHttpClient(), tokenProvider, schemaService, domain);
	}
}
