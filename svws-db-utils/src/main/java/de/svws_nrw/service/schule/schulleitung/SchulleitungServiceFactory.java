package de.svws_nrw.service.schule.schulleitung;

import de.svws_nrw.mapper.schule.schulleitung.SchulleitungMapper;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schule.SchuleRepositoryFactory;

/**
 * Factory für {@link SchulleitungService}
 */
public final class SchulleitungServiceFactory {

	private final SchuleRepositoryFactory schuleRepoFactory;
	private final LehrerRepositoryFactory lehrerRepoFactory;
	private final SchulleitungMapper mapper;

	private SchulleitungServiceFactory(
			final SchuleRepositoryFactory schuleRepoFactory,
			final LehrerRepositoryFactory lehrerRepoFactory,
			final SchulleitungMapper mapper) {
		this.schuleRepoFactory = schuleRepoFactory;
		this.lehrerRepoFactory = lehrerRepoFactory;
		this.mapper = mapper;
	}

	/**
	 * Erstellt eine neue Instanz der {@code SchulleitungServiceFactory}.
	 *
	 * @param schuleRepoFactory das Repository-Factory für Schulleitung-Instanzen
	 * @param lehrerRepoFactory das Repository-Factory für Leitungsfunktions-Instanzen
	 * @param mapper                       der Mapper zur Konvertierung zwischen Entity und API-Modell
	 * @return eine neue {@code SchulleitungServiceFactory}
	 */
	public static SchulleitungServiceFactory getNewInstance(
			final SchuleRepositoryFactory schuleRepoFactory,
			final LehrerRepositoryFactory lehrerRepoFactory,
			final SchulleitungMapper mapper) {
		return new SchulleitungServiceFactory(schuleRepoFactory, lehrerRepoFactory, mapper);
	}

	/**
	 * Erstellt eine neue Instanz des {@link SchulleitungService}.
	 *
	 * @return ein neuer {@code SchulleitungService} mit allen erforderlichen Abhängigkeiten
	 */
	public SchulleitungService getSchulleitungService() {
		return new SchulleitungService(
				schuleRepoFactory.getSchulleitungRepository(),
				lehrerRepoFactory.getLeitungsfunktionRepository(),
				mapper
		);
	}
}
