package de.svws_nrw.repo.uv.zeitraster;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvZeitrasterEintrag;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Zeitraster-Einträge.
 */
public final class UvZeitrasterEintragRepositoryImpl extends RepositoryImpl<DTOUvZeitrasterEintrag> implements UvZeitrasterEintragRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvZeitrasterEintragRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvZeitrasterEintrag.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOUvZeitrasterEintrag> getListByZeitrasterId(final long idZeitraster) {
		return conn.queryList(DTOUvZeitrasterEintrag.QUERY_BY_ZEITRASTER_ID, DTOUvZeitrasterEintrag.class, idZeitraster);
	}

	@Override
	public List<DTOUvZeitrasterEintrag> getListByZeitrasterIds(final Collection<Long> idsZeitraster) {
		if ((idsZeitraster == null) || idsZeitraster.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOUvZeitrasterEintrag.QUERY_LIST_BY_ZEITRASTER_ID, DTOUvZeitrasterEintrag.class, idsZeitraster);
	}

}
