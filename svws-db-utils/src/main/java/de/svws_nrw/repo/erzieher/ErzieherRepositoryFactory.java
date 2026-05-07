package de.svws_nrw.repo.erzieher;

import de.svws_nrw.repo.RepositoryFactory;

/**
 * Factory für alle Repositories der Erzieher-Domäne.
 */
public final class ErzieherRepositoryFactory extends RepositoryFactory {

	/**
	 * Erzeugt eine neue Instanz der Repository-Factory.
	 *
	 * @return neu erzeugte Repository-Factory
	 */
	public static ErzieherRepositoryFactory getNewInstance() {
		return new ErzieherRepositoryFactory();
	}

	/**
	 * Erzeugt ein neues {@link ErzieherRepository}.
	 *
	 * @return {@link ErzieherRepository}
	 */
	public ErzieherRepository getErzieherRepository() {
		return getOrCreate(ErzieherRepository.class, () -> new ErzieherRepositoryImpl(conn));
	}

}
