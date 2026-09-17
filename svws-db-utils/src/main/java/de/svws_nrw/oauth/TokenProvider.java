package de.svws_nrw.oauth;

import de.svws_nrw.oauth.internal.AccessToken;
import de.svws_nrw.oauth.internal.OAuthDomain;

/**
 * Die einzige Abstraktion, die Services sehen.
 * Services kennen weder Credentials noch Flow noch Cache.
 */
public interface TokenProvider {

	/**
	 * Liefert ein gueltiges Token fuer (schema, domain, scope).
	 *
	 * @param schema Schema
	 * @param domain die OAuth-Domaene
	 * @param scope darf null/blank sein → requestedScope der Credentials wird genutzt.
	 *
	 * @return aufgelöstes {@link AccessToken}
	 */
	AccessToken getToken(Schema schema, OAuthDomain domain, OAuthScope scope);

	/** Invalidiert das gecachte Token (z.B. nach einem 401).
	 *
	 * @param schema Schema
	 * @param domain die OAuth-Domaene
	 * @param scope scope
	 */
	void invalidate(Schema schema, OAuthDomain domain, OAuthScope scope);
}
