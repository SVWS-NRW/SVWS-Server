package de.svws_nrw.repo.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOSchulleitung;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Tabelle für die Schulleitung.
 */
public final class SchulleitungRepositoryImpl extends RepositoryImpl<DTOSchulleitung> implements SchulleitungRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchulleitungRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchulleitung.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
