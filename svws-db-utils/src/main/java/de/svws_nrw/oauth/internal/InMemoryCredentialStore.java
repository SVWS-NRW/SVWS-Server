package de.svws_nrw.oauth.internal;

import java.net.URI;

import de.svws_nrw.oauth.Schema;

/**
 * In-Memory-Implementierung auf Basis ConcurrentHashMap.
 */
public final class InMemoryCredentialStore implements CredentialStore {

	private static final String ENV_PROPERTY_TOKEN_URL = "svws.itnrw.oauth.url";
	private static final String ENV_PROPERTY_CLIENT_ID = "svws.itnrw.oauth.clientId";
	private static final String ENV_PROPERTY_CLIENT_SECRET = "svws.itnrw.oauth.clientSecret";

	private static final InMemoryCredentialStore INSTANCE = new InMemoryCredentialStore();

	private Credentials storedCredentials;

	private InMemoryCredentialStore() {
	}

	/**
	 * Liefert die Instanz des In-Memory-Credential-Stores.
	 *
	 * @return Instanz des In-Memory-Credential-Stores
	 */
	public static InMemoryCredentialStore getInstance() {
		return INSTANCE;
	}

	/**
	 * TODO: Aktuell fehlt noch der Bezug zum Schema. Dies ist nur als Übergangslösung für weitere Entwicklung gedacht, im späteren Verlauf sollen die
	 * Credentials schemabezogen verschlüsselt in der DB hinterlegt werden.
	 *
	 * @param schema DB-Schema / Mandant
	 * @return Credentials
	 */
	@Override
	public Credentials forSchema(final Schema schema) {
		if (storedCredentials == null) {
			this.storedCredentials = getCredentialsFromEnv();
		}

		return storedCredentials;
	}

	Credentials getCredentialsFromEnv() {
		final String clientId = readClientId();
		if (clientId == null) {
			throw new MissingCredentialsException(ENV_PROPERTY_CLIENT_ID);
		}

		final String clientSecret = readClientSecret();
		if (clientSecret == null) {
			throw new MissingCredentialsException(ENV_PROPERTY_CLIENT_SECRET);
		}

		final String tokenUrl = readTokenUrl();
		if (tokenUrl == null) {
			throw new MissingCredentialsException(ENV_PROPERTY_TOKEN_URL);
		}

		return new Credentials(clientId, clientSecret, URI.create(tokenUrl), null, OAuthDomain.IT_NRW);
	}

	private String readClientId() {
		return System.getenv(ENV_PROPERTY_CLIENT_ID);
	}

	private String readClientSecret() {
		return System.getenv(ENV_PROPERTY_CLIENT_SECRET);
	}

	private String readTokenUrl() {
		return System.getenv(ENV_PROPERTY_TOKEN_URL);
	}

	static class MissingCredentialsException extends RuntimeException {
		MissingCredentialsException(final String envVariable) {
			super(String.format("Please set environment variable %s", envVariable));
		}
	}
}
