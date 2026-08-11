package de.svws_nrw.repo.lehrer.unterrichtsfach;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	@Override
	public List<DTOLehrerUnterrichtsfach> getListByLehrerIds(final Collection<Long> idsLehrer) {
		if ((idsLehrer == null) || idsLehrer.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOLehrerUnterrichtsfach.QUERY_LIST_BY_LEHRER_ID, DTOLehrerUnterrichtsfach.class, idsLehrer);
	}

	@Override
	public Map<Long, List<DTOLehrerUnterrichtsfach>> getMapByLehrerIds(final Collection<Long> idsLehrer) {
		if ((idsLehrer == null) || idsLehrer.isEmpty()) {
			return Collections.emptyMap();
		}
		final var list = conn.queryList(DTOLehrerUnterrichtsfach.QUERY_LIST_BY_LEHRER_ID, DTOLehrerUnterrichtsfach.class, idsLehrer);
		final Map<Long, List<DTOLehrerUnterrichtsfach>> grouped = list.stream().collect(Collectors.groupingBy(f -> f.Lehrer_ID));
		return idsLehrer.stream().collect(Collectors.toMap(id -> id,	id -> grouped.getOrDefault(id, Collections.emptyList())));
	}

}
