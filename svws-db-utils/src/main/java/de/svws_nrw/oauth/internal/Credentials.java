package de.svws_nrw.oauth.internal;

import java.net.URI;
import java.util.Objects;

/**
 * OAuth-Client-Konfiguration fuer ein einzelnes Schema.
 *
 * <p>Enthaelt alle Informationen, die ein {@link OAuthFlow} benoetigt,
 * um einen Token am Token-Endpoint zu beschaffen.
 *
 *
 * @param clientId     OAuth-Client-ID
 * @param clientSecret OAuth-Client-Secret
 * @param tokenUrl     Token-Endpoint des Authorization-Servers
 * @param defaultScope Standard-Scope falls kein Scope explizit angegeben wird; darf {@code null} sein
 */
public record Credentials(String clientId, String clientSecret, URI tokenUrl, String defaultScope) {

	/**
	 * Stellt sicher, dass {@code clientId}, {@code clientSecret} und {@code tokenUrl} nicht {@code null} sind.
	 */
	public Credentials {
		Objects.requireNonNull(clientId, "clientId");
		Objects.requireNonNull(clientSecret, "clientSecret");
		Objects.requireNonNull(tokenUrl, "tokenUrl");
	}
}
