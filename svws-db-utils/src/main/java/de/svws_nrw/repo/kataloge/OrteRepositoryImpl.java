package de.svws_nrw.repo.kataloge;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Ortsdaten.
 */
public final class OrteRepositoryImpl extends RepositoryImpl<DTOOrt> implements OrteRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public OrteRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOOrt.class, (o, id) -> o.ID = id);
	}

}
