package de.svws_nrw.repo.schueler.schulbesuch;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerMerkmale;
import de.svws_nrw.repo.RepositoryFactory;

public class SchuelerMerkmaleRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz
	 *
	 * @return die neue Factory
	 */
	public static SchuelerMerkmaleRepositoryFactory getNewInstance() {
		return new SchuelerMerkmaleRepositoryFactory();
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerMerkmale}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerMerkmalRepository getSchuelerMerkmaleRepository() {
		return this.getOrCreate(SchuelerMerkmalRepository.class, () -> new SchuelerMerkmalRepositoryImpl(this.conn));
	}

}
