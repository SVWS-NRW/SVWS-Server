package de.svws_nrw.oauth.internal;

import de.svws_nrw.oauth.OAuthScope;

/**
 * Strategie zum Beschaffen von OAuth-Tokens.
 *
 * <p>Dieses Interface kapselt die konkrete Flow-Implementierung (z.B. Client-Credentials).
 */
public interface OAuthFlow {

	/**
	 * Beschafft einen neuen Access-Token.
	 *
	 * @param credentials schema-spezifische OAuth-Credentials
	 * @param scope       gewuenschter Scope; {@code null} oder leer nutzt den Default-Scope der Credentials
	 * @return neuer {@link AccessToken}
	 * @throws TokenRequestException wenn der Token nicht beschafft werden kann
	 */
	AccessToken acquire(Credentials credentials, OAuthScope scope);

	/**
	 * Wirft diese Exception wenn ein Token-Request fehlschlaegt (HTTP/IO/Parsing).
	 */
	final class TokenRequestException extends RuntimeException {
		/**
		 * Konstruktor
		 * @param message message
		 */
		public TokenRequestException(final String message) {
			super(message);
		}

		/**
		 * Konstruktor
		 * @param message message
		 * @param cause {@link Throwable}
		 */
		public TokenRequestException(final String message, final Throwable cause) {
			super(message, cause);
		}
	}
}
