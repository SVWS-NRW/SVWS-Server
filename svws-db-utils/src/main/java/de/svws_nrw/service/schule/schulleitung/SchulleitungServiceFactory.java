package de.svws_nrw.service.schule.schulleitung;

import de.svws_nrw.mapper.schule.schulleitung.SchulleitungMapper;
import de.svws_nrw.repo.schule.leitungsfunktion.LehrerLeitungsfunktionRepositoryFactory;
import de.svws_nrw.repo.schule.schulleitung.SchulleitungRepositoryFactory;

/**
 * Factory für {@link SchulleitungService}
 */
public final class SchulleitungServiceFactory {

	private final SchulleitungRepositoryFactory repoFactory;
	private final LehrerLeitungsfunktionRepositoryFactory leitungsfunktionRepoFactory;
	private final SchulleitungMapper mapper;

	private SchulleitungServiceFactory(
			final SchulleitungRepositoryFactory repoFactory,
			final LehrerLeitungsfunktionRepositoryFactory leitungsfunktionRepoFactory,
			final SchulleitungMapper mapper) {
		this.repoFactory = repoFactory;
		this.leitungsfunktionRepoFactory = leitungsfunktionRepoFactory;
		this.mapper = mapper;
	}

	/**
	 * Erstellt eine neue Instanz der {@code SchulleitungServiceFactory}.
	 *
	 * @param repoFactory                  das Repository-Factory für Schulleitung-Instanzen
	 * @param leitungsfunktionRepoFactory  das Repository-Factory für Leitungsfunktions-Instanzen
	 * @param mapper                       der Mapper zur Konvertierung zwischen Entity und API-Modell
	 * @return eine neue {@code SchulleitungServiceFactory}
	 */
	public static SchulleitungServiceFactory getNewInstance(
			final SchulleitungRepositoryFactory repoFactory,
			final LehrerLeitungsfunktionRepositoryFactory leitungsfunktionRepoFactory,
			final SchulleitungMapper mapper) {
		return new SchulleitungServiceFactory(repoFactory, leitungsfunktionRepoFactory, mapper);
	}

	/**
	 * Erstellt eine neue Instanz des {@link SchulleitungService}.
	 *
	 * @return ein neuer {@code SchulleitungService} mit allen erforderlichen Abhängigkeiten
	 */
	public SchulleitungService getSchulleitungService() {
		return new SchulleitungService(
				repoFactory.getSchulleitungRepository(),
				leitungsfunktionRepoFactory.getLeitungsfunktionRepository(),
				mapper
		);
	}
}
