package de.svws_nrw.repo.schueler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachpruefungen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Fachinformationen der Abiturdaten von Schülern.
 */
public final class SchuelerSprachpruefungenRepositoryImpl extends RepositoryImpl<DTOSchuelerSprachpruefungen> implements SchuelerSprachpruefungenRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerSprachpruefungenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerSprachpruefungen.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOSchuelerSprachpruefungen> getListBySchuelerIds(final Collection<Long> idsSchueler) {
		if ((idsSchueler == null) || (idsSchueler.isEmpty())) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOSchuelerSprachpruefungen.QUERY_LIST_BY_SCHUELER_ID, DTOSchuelerSprachpruefungen.class, idsSchueler);
	}

	@Override
	public Map<Long, List<DTOSchuelerSprachpruefungen>> getMapBySchuelerIDs(final Collection<Long> idsSchueler) {
		if ((idsSchueler == null) || (idsSchueler.isEmpty())) {
			return Collections.emptyMap();
		}

		final var listSprachpruefungen = conn.queryList(
				"SELECT e FROM DTOSchuelerSprachpruefungen e WHERE e.Schueler_ID IN ?1",
				DTOSchuelerSprachpruefungen.class, idsSchueler);
		final Map<Long, List<DTOSchuelerSprachpruefungen>> result = HashMap.newHashMap(idsSchueler.size());
		for (final var la : listSprachpruefungen) {
			result.computeIfAbsent(la.Schueler_ID, k -> new ArrayList<>()).add(la);
		}
		return result;
	}

}
