package de.svws_nrw.repo.uv.lehrer;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvLehrer;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf Lehrerdaten.
 */
public final class UvLehrerRepositoryImpl extends RepositoryImpl<DTOUvLehrer> implements UvLehrerRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvLehrerRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvLehrer.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
