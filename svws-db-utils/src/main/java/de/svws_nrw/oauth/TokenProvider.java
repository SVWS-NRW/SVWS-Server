package de.svws_nrw.oauth;

import de.svws_nrw.oauth.internal.AccessToken;

/**
 * Die einzige Abstraktion, die Services sehen.
 * Services kennen weder Credentials noch Flow noch Cache.
 */
public interface TokenProvider {

	/**
	 * Liefert ein gueltiges Token fuer (schema, scope).
	 *
	 * @param schema Schema
	 * @param scope darf null/blank sein → requestedScope der Credentials wird genutzt.
	 *
	 * @return aufgelöstes {@link AccessToken}
	 */
	AccessToken getToken(Schema schema, OAuthScope scope);

	/** Invalidiert das gecachte Token (z.B. nach einem 401).
	 *
	 * @param schema Schema
	 * @param scope scope
	 */
	void invalidate(Schema schema, OAuthScope scope);
}
