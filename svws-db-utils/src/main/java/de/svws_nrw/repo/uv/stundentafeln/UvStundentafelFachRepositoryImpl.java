package de.svws_nrw.repo.uv.stundentafeln;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvStundentafelFach;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Stundentafel-Fächer.
 */
public final class UvStundentafelFachRepositoryImpl extends RepositoryImpl<DTOUvStundentafelFach> implements UvStundentafelFachRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvStundentafelFachRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvStundentafelFach.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOUvStundentafelFach> getListByStundentafelIds(final Collection<Long> idsStundentafel) {
		if ((idsStundentafel == null) || idsStundentafel.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOUvStundentafelFach.QUERY_LIST_BY_STUNDENTAFEL_ID, DTOUvStundentafelFach.class, idsStundentafel);
	}

}
