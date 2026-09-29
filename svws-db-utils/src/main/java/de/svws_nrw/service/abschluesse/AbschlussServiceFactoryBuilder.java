package de.svws_nrw.service.abschluesse;

import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;

/**
 * Erstellt eine Service-Fatcory für die Services rund um die Abschlüsse
 */
public final class AbschlussServiceFactoryBuilder {

	private AbschlussServiceFactoryBuilder() {
		throw new IllegalStateException("Instantiation not allowed.");
	}

	/**
	 * Gibt die Instanz einer {@link AbschlussServiceFactory} zurück.
	 *
	 * @return die {@link AbschlussServiceFactory}
	 */
	public static AbschlussServiceFactory getGostServiceFactory() {
		return AbschlussServiceFactory.getNewInstance(
				EigeneSchuleRepositoryFactory.getNewInstance(),
				SchuelerRepositoryFactory.getNewInstance()
		);
	}

}
