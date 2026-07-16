package de.svws_nrw.repo.benutzer;

import de.svws_nrw.repo.RepositoryFactory;

/**
 * Factory für alle Repositories der Benutzer-Domäne.
 */
public final class BenutzerRepositoryFactory extends RepositoryFactory {

	/**
	 * Erzeugt eine neue Instanz der Repository-Factory.
	 *
	 * @return neu erzeugte Repository-Factory
	 */
	public static BenutzerRepositoryFactory getNewInstance() {
		return new BenutzerRepositoryFactory();
	}

	/**
	 * Erzeugt ein neues {@link BenutzerAllgemeinRepository}.
	 *
	 * @return {@link BenutzerAllgemeinRepository}
	 */
	public BenutzerAllgemeinRepository getBenutzerAllgemeinRepository() {
		return getOrCreate(BenutzerAllgemeinRepository.class, () -> new BenutzerAllgemeinRepositoryImpl(conn));
	}

	/**
	 * Erzeugt ein neues {@link BenutzergruppeRepository}.
	 *
	 * @return {@link BenutzergruppeRepository}
	 */
	public BenutzergruppeRepository getBenutzergruppeRepository() {
		return getOrCreate(BenutzergruppeRepository.class, () -> new BenutzergruppeRepositoryImpl(conn));
	}

	/**
	 * Erzeugt ein neues {@link BenutzergruppenMitgliedRepository}.
	 *
	 * @return {@link BenutzergruppenMitgliedRepository}
	 */
	public BenutzergruppenMitgliedRepository getBenutzergruppenMitgliedRepository() {
		return getOrCreate(BenutzergruppenMitgliedRepository.class, () -> new BenutzergruppenMitgliedRepositoryImpl(conn));
	}

	/**
	 * Erzeugt ein neues {@link ViewBenutzerDetailsRepository}
	 *
	 * @return {link {@link ViewBenutzerDetailsRepository}}
	 */
	public ViewBenutzerDetailsRepository getViewBenutzerDetailsRepository() {
		return getOrCreate(ViewBenutzerDetailsRepository.class, () -> new ViewBenutzerDetailsRepositoryImpl(conn));
	}

	/**
	 * Erzeugt ein neues {@link CredentialsRepository}.
	 *
	 * @return {@link CredentialsRepository}
	 */
	public CredentialsRepository getCredentialsRepository() {
		return getOrCreate(CredentialsRepository.class, () -> new CredentialsRepositoryImpl(conn));
	}

}
