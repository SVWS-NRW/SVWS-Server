package de.svws_nrw.oauth.internal;

import java.util.Optional;

/**
 * Store fuer domaenenspezifische OAuth-Credentials.
 *
 * <p>Implementiert wird von {@link de.svws_nrw.service.oauth.credential.OAuthCredentialService};
 * verdrahtet wird er in der {@link de.svws_nrw.oauth.CredentialStoreFactory}.
 */
public interface CredentialStore {

	/**
	 * Sucht die Credentials fuer eine Domaene.
	 *
	 * @param domain die OAuth-Domaene
	 * @return die Credentials, sofern hinterlegt
	 */
	Optional<Credentials> get(OAuthDomain domain);
}
