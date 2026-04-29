package de.svws_nrw.repo.schueler.schulbesuch;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerAbgaenge;
import de.svws_nrw.repo.RepositoryFactory;

public class BisherigeSchuleRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz
	 *
	 * @return die neue Factory
	 */
	public static BisherigeSchuleRepositoryFactory getNewInstance() {
		return new BisherigeSchuleRepositoryFactory();
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerAbgaenge}.
	 *
	 * @return das Repository-Objekt
	 */
	public BisherigeSchuleRepository getBisherigeSchulenRepository() {
		return this.getOrCreate(BisherigeSchuleRepository.class, () -> new BisherigeSchuleRepositoryImpl(this.conn));
	}

}
