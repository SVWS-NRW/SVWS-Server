package de.svws_nrw.repo.schule.kataloge.schulen;

import de.svws_nrw.repo.RepositoryFactory;

public class SchulenRepositoryFactory extends RepositoryFactory {

	/**
	 * @return {@link SchulenRepositoryFactory}
	 */
	public static SchulenRepositoryFactory getNewInstance() {
		return new SchulenRepositoryFactory();
	}

	/**
	 * @return {@link SchulenRepository}
	 */
	public SchulenRepository getRepo() {
		return this.getOrCreate(SchulenRepository.class, () -> new SchulenRepositoryImpl(conn));
	}

}
