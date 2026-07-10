package de.svws_nrw.repo.schule.kataloge.fachklasse;

import de.svws_nrw.repo.RepositoryFactory;

public class FachklasseRepositoryFactory extends RepositoryFactory {

	/**
	 * @return {@link FachklasseRepositoryFactory}
	 */
	public static FachklasseRepositoryFactory getNewInstance() {
		return new FachklasseRepositoryFactory();
	}

	/**
	 * @return {@link FachklasseRepository}
	 */
	public FachklasseRepository getRepository() {
		return this.getOrCreate(FachklasseRepository.class, () -> new FachklasseRepositoryImpl(this.conn));
	}

}
