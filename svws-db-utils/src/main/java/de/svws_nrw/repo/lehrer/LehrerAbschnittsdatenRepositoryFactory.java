package de.svws_nrw.repo.lehrer;

import de.svws_nrw.repo.RepositoryFactory;

public class LehrerAbschnittsdatenRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz.
	 *
	 * @return die neue Factory
	 */
	public static LehrerAbschnittsdatenRepositoryFactory getNewInstance() {
		return new LehrerAbschnittsdatenRepositoryFactory();
	}

	/**
	 * Erstellt ein neues Repository für {@link de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten}.
	 *
	 * @return das Repository-Objekt
	 */
	public LehrerAbschnittsdatenRepository getRepository() {
		return this.getOrCreate(LehrerAbschnittsdatenRepository.class, () -> new LehrerAbschnittsdatenRepositoryImpl(this.conn));
	}
}
