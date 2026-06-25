package de.svws_nrw.repo.lehrer.funktion;

import de.svws_nrw.repo.RepositoryFactory;

public class LehrerFunktionRepositoryFactory  extends RepositoryFactory {

	/**
	 * @return {@link LehrerFunktionRepositoryFactory}
	 */
	public static LehrerFunktionRepositoryFactory getNewInstance() {
		return new LehrerFunktionRepositoryFactory();
	}

	/**
	 * @return {@link LehrerFunktionRepository}
	 */
	public LehrerFunktionRepository getRepository() {
		return this.getOrCreate(LehrerFunktionRepository.class, () -> new LehrerFunktionRepositoryImpl(this.conn));
	}

}
