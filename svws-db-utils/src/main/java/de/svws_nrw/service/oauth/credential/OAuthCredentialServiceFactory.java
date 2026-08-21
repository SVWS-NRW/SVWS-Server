package de.svws_nrw.service.oauth.credential;

import de.svws_nrw.mapper.oauth.OAuthCredentialMapper;
import de.svws_nrw.mapper.oauth.OAuthDomainMapper;
import de.svws_nrw.repo.oauth.credential.OAuthCredentialRepositoryFactory;

public final class OAuthCredentialServiceFactory {

	private final OAuthCredentialRepositoryFactory repositoryFactory;

	private OAuthCredentialServiceFactory(final OAuthCredentialRepositoryFactory repositoryFactory) {
		this.repositoryFactory = repositoryFactory;
	}

	/**
	 * Erzeugt neue {@link OAuthCredentialServiceFactory}
	 *
	 * @param repositoryFactory {@link OAuthCredentialRepositoryFactory}
	 *
	 * @return {@link OAuthCredentialServiceFactory}
	 */
	public static OAuthCredentialServiceFactory getNewInstance(final OAuthCredentialRepositoryFactory repositoryFactory) {
		return new OAuthCredentialServiceFactory(repositoryFactory);
	}

	/**
	 * Erzeugt neuen {@link OAuthCredentialService}
	 * @return {@link OAuthCredentialService}
	 */
	public OAuthCredentialService getClientCredentialService() {
		return new OAuthCredentialService(repositoryFactory.getRepository(), OAuthCredentialMapper.INSTANCE, OAuthDomainMapper.INSTANCE);
	}
}
