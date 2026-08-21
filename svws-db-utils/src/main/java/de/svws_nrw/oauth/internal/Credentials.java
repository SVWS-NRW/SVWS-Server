package de.svws_nrw.oauth.internal;

import java.net.URI;
import java.util.Objects;

/**
 * OAuth-Client-Konfiguration fuer ein einzelnes Schema.
 *
 * <p>Enthaelt alle Informationen, die ein {@link OAuthFlow} benoetigt,
 * um einen Token am Token-Endpoint zu beschaffen.
 *
 * @param id die ID
 * @param clientId       OAuth-Client-ID
 * @param clientSecret   OAuth-Client-Secret
 * @param authServerUrl       Token-Endpoint des Authorization-Servers
 * @param requestedScope Standard-Scope falls kein Scope explizit angegeben wird; darf {@code null} sein
 * @param serviceDomain         die Domäne der Credentials
 */
public record Credentials(Long id, String clientId, String clientSecret, URI authServerUrl, String requestedScope, OAuthDomain serviceDomain) {

	/**
	 * Stellt sicher, dass {@code clientId}, {@code clientSecret} und {@code authServerUrl} nicht {@code null} sind.
	 */
	public Credentials {
		Objects.requireNonNull(clientId, "clientId");
		Objects.requireNonNull(clientSecret, "clientSecret");
		Objects.requireNonNull(authServerUrl, "authServerUrl");
	}

	/**
	 * OAuth-Client-Konfiguration fuer ein einzelnes Schema.
	 *
	 * @param clientId       OAuth-Client-ID
	 * @param clientSecret   OAuth-Client-Secret
	 * @param authServerUrl       Token-Endpoint des Authorization-Servers
	 * @param requestedScope Standard-Scope falls kein Scope explizit angegeben wird; darf {@code null} sein
	 * @param serviceDomain         die Domäne der Credentials
	 */
	public Credentials(final String clientId, final String clientSecret, final URI authServerUrl, final String requestedScope,
			final OAuthDomain serviceDomain) {
		this(null, clientId, clientSecret, authServerUrl, requestedScope, serviceDomain);
	}
}
