package de.svws_nrw.oauth;

import de.svws_nrw.oauth.internal.CredentialStore;
import de.svws_nrw.repo.oauth.credential.OAuthCredentialRepositoryFactory;
import de.svws_nrw.service.oauth.credential.OAuthCredentialServiceFactory;

/**
 * Factory fuer {@link CredentialStore}-Instanzen.
 *
 */
public final class CredentialStoreFactory {

	private CredentialStoreFactory() {
		// Instanziierung nur ueber getNewInstance()
	}

	/**
	 * Erzeugt eine neue {@link CredentialStoreFactory}.
	 *
	 * @return neue Instanz dieser Factory
	 */
	public static CredentialStoreFactory getNewInstance() {
		return new CredentialStoreFactory();
	}

	/**
	 * Erzeugt einen {@link CredentialStore}, der die Credentials aus der Datenbank bezieht.
	 *
	 * <p>Implementierung ist der {@link de.svws_nrw.service.oauth.credential.OAuthCredentialService}; das Schema
	 * ergibt sich implizit aus dessen request-gebundener Datenbank-Verbindung.
	 *
	 * @return neuer {@link CredentialStore}
	 */
	public CredentialStore getCredentialStore() {
		final OAuthCredentialRepositoryFactory credentialRepositoryFactory = OAuthCredentialRepositoryFactory.getNewInstance();

		return OAuthCredentialServiceFactory.getNewInstance(credentialRepositoryFactory)
				.getClientCredentialService();
	}
}
