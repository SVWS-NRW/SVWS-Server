package de.svws_nrw.service.schule;

import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;

/**
 * Eine Factory zum Erstellen der Statistik-spezifischen Services
 */
public final class SchuleServiceFactory {

	/** die Factory für die Schule-Repositories */
	private final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory;



	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param eigeneSchuleRepositoryFactory   die Factory für Schule-Repositories
	 */
	private SchuleServiceFactory(final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory) {
		this.eigeneSchuleRepositoryFactory = eigeneSchuleRepositoryFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param eigeneSchuleRepositoryFactory   die Factory für Schule-Repositories
	 *
	 * @return die neue Factory-Instanz
	 */
	public static SchuleServiceFactory getNewInstance(final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory) {
		return new SchuleServiceFactory(eigeneSchuleRepositoryFactory);
	}


	/**
	 * Erstellt einen neuen Schule-Service für den Zugriff auf die Schuldaten.
	 *
	 * @return der Service für die Schuldaten
	 */
	public SchuleService getSchuleService() {
		return new SchuleService(
				eigeneSchuleRepositoryFactory.getSchuleRepository(),
				eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository()
		);
	}

	/**
	 * Erstellt einen neuen Statistik-Service für den Zugriff auf Schuljahresabschnitte.
	 *
	 * @return der Service für die Schuljahresabschnitte
	 */
	public SchuljahresabschnittService getSchuljahresabschnittService() {
		return new SchuljahresabschnittService(eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository());
	}


}
