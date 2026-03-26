package de.svws_nrw.repo.lehrer;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerUnterrichtsfach;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Unterrichtsfächer von Lehrern.
 */
public final class LehrerUnterrichtsfachRepositoryImpl extends RepositoryImpl<DTOLehrerUnterrichtsfach> implements LehrerUnterrichtsfachRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public LehrerUnterrichtsfachRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLehrerUnterrichtsfach.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOLehrerUnterrichtsfach> getListByLehrerId(final long idLehrer) {
		return conn.queryList(DTOLehrerUnterrichtsfach.QUERY_BY_LEHRER_ID, DTOLehrerUnterrichtsfach.class, idLehrer);
	}

}
