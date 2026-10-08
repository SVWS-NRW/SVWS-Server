package de.svws_nrw.service.oauth.credential;

import de.svws_nrw.mapper.oauth.OAuthCredentialMapper;
import de.svws_nrw.mapper.oauth.OAuthDomainMapper;
import de.svws_nrw.repo.oauth.credential.OAuthCredentialRepositoryFactory;
import de.svws_nrw.service.crypto.secret.SecretCipherFactory;

/**
 * Factory für den {@link OAuthCredentialService}.
 *
 * <p>Verdrahtet den Service mit dem Repository, den Mappern und dem {@link de.svws_nrw.service.crypto.secret.SecretCipher}
 * zur Ver- und Entschlüsselung des Client-Secrets.</p>
 */
public final class OAuthCredentialServiceFactory {

	private final OAuthCredentialRepositoryFactory repositoryFactory;
	private final SecretCipherFactory cipherFactory;

	/**
	 * Erstellt eine neue Factory mit den übergebenen Factories der abhängigen Schichten.
	 *
	 * @param repositoryFactory   die Factory zur Erzeugung des Repositories für die OAuth-Zugangsdaten
	 * @param cipherFactory       die Factory zur Erzeugung des Ciphers für das Client-Secret
	 */
	private OAuthCredentialServiceFactory(final OAuthCredentialRepositoryFactory repositoryFactory, final SecretCipherFactory cipherFactory) {
		this.repositoryFactory = repositoryFactory;
		this.cipherFactory = cipherFactory;
	}

	/**
	 * Erzeugt eine neue {@link OAuthCredentialServiceFactory}.
	 *
	 * @param repositoryFactory   die {@link OAuthCredentialRepositoryFactory} zur Erzeugung des Repositories
	 * @param cipherFactory       die {@link SecretCipherFactory} zur Erzeugung des Ciphers für das Client-Secret
	 *
	 * @return die neue {@link OAuthCredentialServiceFactory}
	 */
	public static OAuthCredentialServiceFactory getNewInstance(final OAuthCredentialRepositoryFactory repositoryFactory,
			final SecretCipherFactory cipherFactory) {
		return new OAuthCredentialServiceFactory(repositoryFactory, cipherFactory);
	}

	/**
	 * Erzeugt einen neuen {@link OAuthCredentialService}.
	 *
	 * @return der neue {@link OAuthCredentialService}
	 */
	public OAuthCredentialService getClientCredentialService() {
		return new OAuthCredentialService(
				repositoryFactory.getRepository(),
				OAuthCredentialMapper.INSTANCE,
				OAuthDomainMapper.INSTANCE,
				cipherFactory.getSecretCipher()
		);
	}
}
