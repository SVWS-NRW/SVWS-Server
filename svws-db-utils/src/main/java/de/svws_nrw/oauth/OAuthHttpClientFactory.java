package de.svws_nrw.oauth;

import java.net.http.HttpClient;

import de.svws_nrw.oauth.internal.CachingTokenProvider;
import de.svws_nrw.oauth.internal.ClientCredentialsFlow;
import de.svws_nrw.oauth.internal.OAuthFlow;

public final class OAuthHttpClientFactory {

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
	 * @return {@link OAuthHttpClientImpl}
	 */
	public OAuthHttpClientImpl getClient() {
		final OAuthFlow flow = new ClientCredentialsFlow();
		final TokenProvider tokenProvider = new CachingTokenProvider(CredStoreServiceFactory.getNewInstance().getCredStoreService(), flow);
		final SchemaService schemaService = schemaServiceFactory.getService();
		return new OAuthHttpClientImpl(HttpClient.newHttpClient(), tokenProvider, schemaService);
	}
}
