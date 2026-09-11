package de.svws_nrw.repo.uv.raeume;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvRaum;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Räume
 * (Tabelle UV_Raeume).
 */
public final class UvRaumRepositoryImpl extends RepositoryImpl<DTOUvRaum> implements UvRaumRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvRaumRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvRaum.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
