package de.svws_nrw.service.abschluesse;

import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;

/**
 * Eine Factory zum Erstellen der Services für die Abschlüsse
 */
public final class AbschlussServiceFactory {

	/** die Factory für die Schul-Repositories */
	private final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory;

	/** die Factory für die Schüler-Repositories */
	private final SchuelerRepositoryFactory schuelerRepositoryFactory;


	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param eigeneSchuleRepositoryFactory          die Factory für Schul-Repositories
	 * @param schuelerRepositoryFactory        die Factory für Schüler-Repositories
	 */
	private AbschlussServiceFactory(final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory) {
		this.eigeneSchuleRepositoryFactory = eigeneSchuleRepositoryFactory;
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param eigeneSchuleRepositoryFactory     die Factory für Schul-Repositories
	 * @param schuelerRepositoryFactory   die Factory für Schüler-Repositories
	 *
	 * @return die Factory
	 */
	public static AbschlussServiceFactory getNewInstance(final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory) {
		return new AbschlussServiceFactory(eigeneSchuleRepositoryFactory, schuelerRepositoryFactory);
	}


	/**
	 * Erstellt einen neuen Service für die Fächer der gymnasialen Oberstufe
	 *
	 * @return der Service
	 */
	public AbschlussService getAbschlussService() {
		return new AbschlussService(eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				eigeneSchuleRepositoryFactory.getSchuleRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository());
	}

}
