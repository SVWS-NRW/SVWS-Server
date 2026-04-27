package de.svws_nrw.repo.kurse;

import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.repo.RepositoryFactory;

/**
 * Eine Factory zum Erstellen von Repositories für Datenbank-Entitäten und ggf. auch komplexere Abfragen.
 */
public final class KurseRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz
	 *
	 * @return die neue Factory
	 */
	public static KurseRepositoryFactory getNewInstance() {
		return new KurseRepositoryFactory();
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOKurs}.
	 *
	 * @return das Repository-Objekt
	 */
	public KurseRepository getKurseRepository() {
		return getOrCreate(KurseRepository.class, () -> new KurseRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOKursLehrer}.
	 *
	 * @return das Repository-Objekt
	 */
	public KurslehrerRepository getKurslehrerRepository() {
		return getOrCreate(KurslehrerRepository.class, () -> new KurslehrerRepositoryImpl(conn));
	}

}
