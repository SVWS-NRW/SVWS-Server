package de.svws_nrw.oauth.internal;

import java.util.Arrays;
import java.util.Optional;

/**
 * Die Domänen, für die OAuth-Credentials hinterlegt werden können.
 */
public enum OAuthDomain {
	/** Web Noten Manager */
	WENOM,

	/** Schnittstelle schulbewerbung.de */
	SCHULBEWERBUNG,

	/**
	 * IT.NRW
	 */
	IT_NRW;

	/**
	 * Ermittelt die Domäne zum übergebenen Namen.
	 *
	 * @param name   der Name der Domäne
	 *
	 * @return die zugehörige {@link OAuthDomain} oder ein leeres {@link Optional}, falls keine Domäne mit dem Namen existiert
	 */
	public static Optional<OAuthDomain> fromName(final String name) {
		return Arrays.stream(values())
				.filter(domain -> domain.name().equals(name))
				.findFirst();
	}

}
