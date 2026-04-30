package de.svws_nrw.repo.wiedervorlage;

import de.svws_nrw.repo.RepositoryFactory;

/**
 * Factory für alle Repositories der Wiedervorlage-Domäne.
 */
public final class WiedervorlageRepositoryFactory extends RepositoryFactory {

	/**
	 * Erzeugt eine neue Instanz der Repository-Factory.
	 *
	 * @return neu erzeugte Repository-Factory
	 */
	public static WiedervorlageRepositoryFactory getNewInstance() {
		return new WiedervorlageRepositoryFactory();
	}

	/**
	 * Erzeugt ein neues {@link WiedervorlageRepository}.
	 *
	 * @return {@link WiedervorlageRepository}
	 */
	public WiedervorlageRepository getWiedervorlageRepository() {
		return getOrCreate(WiedervorlageRepository.class, () -> new WiedervorlageRepositoryImpl(conn));
	}

}
