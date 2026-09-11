package de.svws_nrw.repo.uv.faecher;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvFach;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Fächer
 * (Tabelle UV_Faecher).
 */
public final class UvFachRepositoryImpl extends RepositoryImpl<DTOUvFach> implements UvFachRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvFachRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvFach.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOUvFach> getListByFachId(final long idFach) {
		return conn.queryList(DTOUvFach.QUERY_BY_FACH_ID, DTOUvFach.class, idFach);
	}

}
