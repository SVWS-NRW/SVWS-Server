package de.svws_nrw.oauth.internal;

/**
 * Wird geworfen, wenn keine Credentials fuer eine Domaene hinterlegt sind.
 */
public final class UnknownDomainException extends RuntimeException {

	/**
	 * Konstruktor
	 *
	 * @param domain die OAuth-Domaene
	 */
	public UnknownDomainException(final OAuthDomain domain) {
		super(String.format("No credentials registered for domain: %s", (domain == null) ? "<null>" : domain.name()));
	}
}
