package de.svws_nrw.service.katalog.teilleistungsart;

import de.svws_nrw.repo.kataloge.KatalogeRepositoryFactory;

/**
 * Factory für {@link TeilleistungsartService}
 */
public final class TeilleistungsartServiceFactory {

	private final KatalogeRepositoryFactory repositoryFactory;

	private TeilleistungsartServiceFactory(final KatalogeRepositoryFactory repositoryFactory) {
		this.repositoryFactory = repositoryFactory;
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param katalogeRepositoryFactory   die Factory für Kataloge
	 * @return {@link TeilleistungsartServiceFactory} - neu erzeugte Factory
	 */
	public static TeilleistungsartServiceFactory getNewInstance(final KatalogeRepositoryFactory katalogeRepositoryFactory) {
		return new TeilleistungsartServiceFactory(katalogeRepositoryFactory);
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
