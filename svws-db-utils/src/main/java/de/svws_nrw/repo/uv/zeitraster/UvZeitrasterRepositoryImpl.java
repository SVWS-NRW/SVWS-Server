package de.svws_nrw.repo.uv.zeitraster;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvZeitraster;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Zeitraster.
 */
public final class UvZeitrasterRepositoryImpl extends RepositoryImpl<DTOUvZeitraster> implements UvZeitrasterRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvZeitrasterRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvZeitraster.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
