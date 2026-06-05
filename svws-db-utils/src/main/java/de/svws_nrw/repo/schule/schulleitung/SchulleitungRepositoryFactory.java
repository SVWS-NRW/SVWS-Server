package de.svws_nrw.repo.schule.schulleitung;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOSchulleitung;
import de.svws_nrw.repo.RepositoryFactory;

public class SchulleitungRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz
	 *
	 * @return die neue Factory
	 */
	public static SchulleitungRepositoryFactory getNewInstance() {
		return new SchulleitungRepositoryFactory();
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOSchulleitung}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchulleitungRepository getSchulleitungRepository() {
		return this.getOrCreate(
				SchulleitungRepository.class,
				() -> new SchulleitungRepositoryImpl(this.conn));
	}

}
