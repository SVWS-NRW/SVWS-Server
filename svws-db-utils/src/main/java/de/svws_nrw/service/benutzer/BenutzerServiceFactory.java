package de.svws_nrw.service.benutzer;

import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;

/**
 * Eine Factory für den Zugriff auf Benutzer-Informationen
 */
public final class BenutzerServiceFactory {

	/** die Factory für die Benutzer-Repositories */
	private final BenutzerRepositoryFactory benutzerRepositoryFactory;

	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param benutzerRepositoryFactory   die Factory für Benutzer-Repositories
	 */
	private BenutzerServiceFactory(final BenutzerRepositoryFactory benutzerRepositoryFactory) {
		this.benutzerRepositoryFactory = benutzerRepositoryFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param benutzerRepositoryFactory   die Factory für Benutzer-Repositories
	 *
	 * @return die Factory
	 */
	public static BenutzerServiceFactory getNewInstance(final BenutzerRepositoryFactory benutzerRepositoryFactory) {
		return new BenutzerServiceFactory(benutzerRepositoryFactory);
	}


	/**
	 * Erstellt einen neuen Service für Methoden zum Zugriff auf die Benutzerkompetenzen des aktuellen Benutzers
	 *
	 * @return der Service
	 */
	public BenutzerKompetenzService getBenutzerKompetenzService() {
		return new BenutzerKompetenzService(benutzerRepositoryFactory.getBenutzerAllgemeinRepository());
	}

}
