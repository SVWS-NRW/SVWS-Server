package de.svws_nrw.repo.schueler.zp10;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerZP10;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die ZP10-Daten von Schülern.
 */
public final class SchuelerZP10RepositoryImpl extends RepositoryImpl<DTOSchuelerZP10> implements SchuelerZP10Repository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerZP10RepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerZP10.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOSchuelerZP10> getListBySchuelerIds(final Collection<Long> idsSchueler) {
		if ((idsSchueler == null) || (idsSchueler.isEmpty())) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOSchuelerZP10.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerZP10.class, idsSchueler);
	}

}
