package de.svws_nrw.repo.schule.leitungsfunktion;

import de.svws_nrw.db.dto.current.schild.katalog.DTOLeitungsfunktion;
import de.svws_nrw.repo.RepositoryFactory;

public class LehrerLeitungsfunktionRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz
	 *
	 * @return die neue Factory
	 */
	public static LehrerLeitungsfunktionRepositoryFactory getNewInstance() {
		return new LehrerLeitungsfunktionRepositoryFactory();
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOLeitungsfunktion}.
	 *
	 * @return das Repository-Objekt
	 */
	public LehrerLeitungsfunktionRepository getLeitungsfunktionRepository() {
		return this.getOrCreate(
				LehrerLeitungsfunktionRepository.class,
				() -> new LehrerLeitungsfunktionRepositoryImpl(this.conn)
		);
	}
}
