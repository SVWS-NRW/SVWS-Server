package de.svws_nrw.service.katalog.teilleistungsart;

import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;

/**
 * Factory für {@link TeilleistungsartService}
 */
public final class TeilleistungsartServiceFactory {

	private final KatalogRepositoryFactory repositoryFactory;

	private TeilleistungsartServiceFactory(final KatalogRepositoryFactory repositoryFactory) {
		this.repositoryFactory = repositoryFactory;
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param katalogRepositoryFactory   die Factory für Kataloge
	 * @return {@link TeilleistungsartServiceFactory} - neu erzeugte Factory
	 */
	public static TeilleistungsartServiceFactory getNewInstance(final KatalogRepositoryFactory katalogRepositoryFactory) {
		return new TeilleistungsartServiceFactory(katalogRepositoryFactory);
	}

	/**
	 * Erstellt einen neuen TeilleistungsartenService für den Zugriff auf die Katalogdaten.
	 *
	 * @return {@link TeilleistungsartService} - neu erzeugter Service
	 */
	public TeilleistungsartService getTeilLeistungsartenService() {
		return new TeilleistungsartService(repositoryFactory.getTeilleistungsartRepository());
	}
}
