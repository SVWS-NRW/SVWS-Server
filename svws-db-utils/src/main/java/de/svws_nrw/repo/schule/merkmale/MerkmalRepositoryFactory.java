package de.svws_nrw.repo.schule.merkmale;


import de.svws_nrw.db.dto.current.schild.schule.DTOMerkmale;
import de.svws_nrw.repo.RepositoryFactory;

public class MerkmalRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz
	 *
	 * @return die neue Factory
	 */
	public static MerkmalRepositoryFactory getNewInstance() {
		return new MerkmalRepositoryFactory();
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOMerkmale}.
	 *
	 * @return das Repository-Objekt
	 */
	public MerkmalRepository getMerkmalRepository() {
		return this.getOrCreate(MerkmalRepository.class, () -> new MerkmalRepositoryImpl(this.conn));
	}

}
