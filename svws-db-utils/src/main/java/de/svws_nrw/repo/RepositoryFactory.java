package de.svws_nrw.repo;

import de.svws_nrw.db.DBEntityManager;

/**
 * Eine Factory zum Erstellen von Repositories für Datenbank-Entitäten und ggf. auch komplexere Abfragen.
 */
public abstract class RepositoryFactory {

	/** Die Datenbank-Verbindung */
	protected final DBEntityManager conn;


	/**
	 * Erstellt eine neue Factory mit der übergebenen Datenbank-Verbindung
	 */
	protected RepositoryFactory() {
		this.conn = DbConnectionProvider.getConnection();
	}

}
