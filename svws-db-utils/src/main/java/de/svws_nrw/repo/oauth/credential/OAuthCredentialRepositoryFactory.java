package de.svws_nrw.repo.oauth.credential;

import de.svws_nrw.repo.RepositoryFactory;

public class OAuthCredentialRepositoryFactory extends RepositoryFactory {

	/**
	 * Erzeugt neue {@link OAuthCredentialRepositoryFactory}
	 *
	 * @return {@link OAuthCredentialRepositoryFactory}
	 */
	public static OAuthCredentialRepositoryFactory getNewInstance() {
		return new OAuthCredentialRepositoryFactory();
	}

	/**
	 * Erzeugt ein neues {@link OAuthCredentialRepository} oder holt ein vorhandenes, falls innerhalb des Requests bereits ein Repository erzeugt wurde
	 *
	 * @return {@link OAuthCredentialRepository}
	 */
	public OAuthCredentialRepository getRepository() {
		return getOrCreate(OAuthCredentialRepository.class, () -> new OAuthOAuthCredentialRepositoryImpl(conn));
	}
}
