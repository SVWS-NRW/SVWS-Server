package de.svws_nrw.service.schule;

import de.svws_nrw.repo.schule.SchuleRepositoryFactory;

/**
 * Eine Factory zum Erstellen der Statistik-spezifischen Services
 */
public final class SchuleServiceFactory {

	/** die Factory für die Schule-Repositories */
	private final SchuleRepositoryFactory schuleRepositoryFactory;



	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param schuleRepositoryFactory   die Factory für Schule-Repositories
	 */
	private SchuleServiceFactory(final SchuleRepositoryFactory schuleRepositoryFactory) {
		this.schuleRepositoryFactory = schuleRepositoryFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param schuleRepositoryFactory   die Factory für Schule-Repositories
	 *
	 * @return die neue Factory-Instanz
	 */
	public static SchuleServiceFactory getNewInstance(final SchuleRepositoryFactory schuleRepositoryFactory) {
		return new SchuleServiceFactory(schuleRepositoryFactory);
	}


	/**
	 * Erstellt einen neuen Schule-Service für den Zugriff auf die Schuldaten.
	 *
	 * @return der Service für die Schuldaten
	 */
	public SchuleService getSchuleService() {
		return new SchuleService(
				schuleRepositoryFactory.getSchuleRepository(),
				schuleRepositoryFactory.getSchuljahresabschnitteRepository()
		);
	}

	/**
	 * Erstellt einen neuen Statistik-Service für den Zugriff auf Schuljahresabschnitte.
	 *
	 * @return der Service für die Schuljahresabschnitte
	 */
	public SchuljahresabschnittService getSchuljahresabschnittService() {
		return new SchuljahresabschnittService(schuleRepositoryFactory.getSchuljahresabschnitteRepository());
	}


}
