package de.svws_nrw.data.kataloge.teilleistungsarten;

import de.svws_nrw.repo.kataloge.KatalogeRepositoryFactory;

/**
 * Factory für {@link TeilleistungsartService}
 */
public final class TeilLeistungsartServiceFactory {

	private final KatalogeRepositoryFactory repositoryFactory;

	private TeilLeistungsartServiceFactory(final KatalogeRepositoryFactory repositoryFactory) {
		this.repositoryFactory = repositoryFactory;
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param katalogeRepositoryFactory   die Factory für Kataloge
	 * @return {@link TeilLeistungsartServiceFactory} - neu erzeugte Factory
	 */
	public static TeilLeistungsartServiceFactory getNewInstance(final KatalogeRepositoryFactory katalogeRepositoryFactory) {
		return new TeilLeistungsartServiceFactory(katalogeRepositoryFactory);
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
