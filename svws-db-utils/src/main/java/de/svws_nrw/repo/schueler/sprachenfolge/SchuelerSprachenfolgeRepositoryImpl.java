package de.svws_nrw.repo.schueler.sprachenfolge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachenfolge;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Fachinformationen der Abiturdaten von Schülern.
 */
public final class SchuelerSprachenfolgeRepositoryImpl extends RepositoryImpl<DTOSchuelerSprachenfolge> implements SchuelerSprachenfolgeRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerSprachenfolgeRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerSprachenfolge.class, o -> o.ID, (o, id) -> o.ID = id);
	}


	@Override
	public List<DTOSchuelerSprachenfolge> getListBySchuelerIds(final Collection<Long> idsSchueler) {
		if ((idsSchueler == null) || (idsSchueler.isEmpty())) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOSchuelerSprachenfolge.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerSprachenfolge.class, idsSchueler);
	}

	@Override
	public Map<Long, List<DTOSchuelerSprachenfolge>> getMapBySchuelerIDs(final Collection<Long> idsSchueler) {
		if ((idsSchueler == null) || (idsSchueler.isEmpty())) {
			return Collections.emptyMap();
		}

		final var listSprachenfolge = conn.queryList(
				"SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.Schueler_ID IN ?1",
				DTOSchuelerSprachenfolge.class, idsSchueler);
		final Map<Long, List<DTOSchuelerSprachenfolge>> result = HashMap.newHashMap(idsSchueler.size());
		for (final var la : listSprachenfolge) {
			result.computeIfAbsent(la.Schueler_ID, k -> new ArrayList<>()).add(la);
		}
		return result;
	}

}
