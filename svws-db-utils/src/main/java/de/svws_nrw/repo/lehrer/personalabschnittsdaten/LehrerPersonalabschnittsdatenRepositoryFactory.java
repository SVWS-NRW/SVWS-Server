package de.svws_nrw.repo.lehrer.personalabschnittsdaten;

import de.svws_nrw.repo.RepositoryFactory;

public class LehrerPersonalabschnittsdatenRepositoryFactory extends RepositoryFactory {

	/**
	 * @return {@link LehrerPersonalabschnittsdatenRepositoryFactory}
	 */
	public static LehrerPersonalabschnittsdatenRepositoryFactory getNewInstance() {
		return new LehrerPersonalabschnittsdatenRepositoryFactory();
	}

	/**
	 * @return {@link LehrerPersonalabschnittsdatenRepository}
	 */
	public LehrerPersonalabschnittsdatenRepository getRepo() {
		return this.getOrCreate(LehrerPersonalabschnittsdatenRepository.class, () -> new LehrerPersonalabschnittsdatenRepositoryImpl(conn));
	}

}
