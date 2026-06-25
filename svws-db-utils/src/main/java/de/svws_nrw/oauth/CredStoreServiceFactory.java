package de.svws_nrw.oauth;

import de.svws_nrw.oauth.internal.InMemoryCredentialStore;
import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;

/**
 * Factory fuer {@link CredStoreService}-Instanzen.
 *
 * <p>Kapselt die Verdrahtung des {@link CredStoreService} mit der zugehoerigen
 * Repository-Infrastruktur und entkoppelt so Aufrufer von Konstruktionsdetails.
 */
public class CredStoreServiceFactory {

	/**
	 * Erzeugt eine neue {@link CredStoreServiceFactory} mit einer frischen
	 * {@link BenutzerRepositoryFactory}.
	 *
	 * @return neue Instanz dieser Factory
	 */
	public static CredStoreServiceFactory getNewInstance() {
		return new CredStoreServiceFactory();
	}

	/**
	 * Erzeugt einen {@link CredStoreService} mit einem In-Memory-Credential-Store,
	 * der ueber den Schema Service befuellt wird.
	 *
	 * @return neuer {@link CredStoreService}
	 */
	public CredStoreService getCredStoreService() {
		return new CredStoreService(InMemoryCredentialStore.getInstance());
	}
}
