package de.svws_nrw.service.schule.schulleitung;

import de.svws_nrw.mapper.schule.schulleitung.SchulleitungMapper;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;

/**
 * Factory für {@link SchulleitungService}
 */
public final class SchulleitungServiceFactory {

	private final EigeneSchuleRepositoryFactory schuleRepoFactory;
	private final SchulleitungMapper mapper;

	private SchulleitungServiceFactory(
			final EigeneSchuleRepositoryFactory schuleRepoFactory,
			final SchulleitungMapper mapper) {
		this.schuleRepoFactory = schuleRepoFactory;
		this.mapper = mapper;
	}

	/**
	 * Erstellt eine neue Instanz der {@code SchulleitungServiceFactory}.
	 *
	 * @param schuleRepoFactory das Repository-Factory für Schulleitung-Instanzen
	 * @param mapper                       der Mapper zur Konvertierung zwischen Entity und API-Modell
	 * @return eine neue {@code SchulleitungServiceFactory}
	 */
	public static SchulleitungServiceFactory getNewInstance(
			final EigeneSchuleRepositoryFactory schuleRepoFactory,
			final SchulleitungMapper mapper) {
		return new SchulleitungServiceFactory(schuleRepoFactory, mapper);
	}

	/**
	 * Erstellt eine neue Instanz des {@link SchulleitungService}.
	 *
	 * @return ein neuer {@code SchulleitungService} mit allen erforderlichen Abhängigkeiten
	 */
	public SchulleitungService getSchulleitungService() {
		return new SchulleitungService(
				schuleRepoFactory.getSchulleitungRepository(),
				mapper
		);
	}
}
