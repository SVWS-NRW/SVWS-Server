package de.svws_nrw.repo.schule;

import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.repo.RepositoryFactory;

/**
 * Eine Factory zum Erstellen von Repositories für Datenbank-Entitäten und ggf. auch komplexere Abfragen.
 */
public final class SchuleRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz
	 *
	 * @return die neue Factory
	 */
	public static SchuleRepositoryFactory getNewInstance() {
		return new SchuleRepositoryFactory();
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOEigeneSchule}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuleRepository getSchuleRepository() {
		return new SchuleRepositoryImpl(conn);
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuljahresabschnitte}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuljahresabschnitteRepository getSchuljahresabschnitteRepository() {
		return new SchuljahresabschnitteRepositoryImpl(conn);
	}

}
