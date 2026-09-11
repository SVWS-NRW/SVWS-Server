package de.svws_nrw.repo.uv.lehrer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvLehrerUnterrichtsfach;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Unterrichtsfächer
 * von UV-Lehrkräften (Tabelle UV_LehrerUnterrichtsfaecher).
 */
public final class UvLehrerUnterrichtsfachRepositoryImpl extends RepositoryImpl<DTOUvLehrerUnterrichtsfach>
		implements UvLehrerUnterrichtsfachRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvLehrerUnterrichtsfachRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvLehrerUnterrichtsfach.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOUvLehrerUnterrichtsfach> getListByLehrerId(final long idLehrer) {
		return conn.queryList(DTOUvLehrerUnterrichtsfach.QUERY_BY_LEHRER_ID, DTOUvLehrerUnterrichtsfach.class, idLehrer);
	}

	@Override
	public List<DTOUvLehrerUnterrichtsfach> getListByLehrerIds(final Collection<Long> idsLehrer) {
		if ((idsLehrer == null) || idsLehrer.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOUvLehrerUnterrichtsfach.QUERY_LIST_BY_LEHRER_ID, DTOUvLehrerUnterrichtsfach.class, idsLehrer);
	}

	@Override
	public Map<Long, List<DTOUvLehrerUnterrichtsfach>> getMapByLehrerIds(final Collection<Long> idsLehrer) {
		if ((idsLehrer == null) || idsLehrer.isEmpty()) {
			return Collections.emptyMap();
		}
		final var list = conn.queryList(DTOUvLehrerUnterrichtsfach.QUERY_BY_LEHRER_ID, DTOUvLehrerUnterrichtsfach.class, idsLehrer);
		final Map<Long, List<DTOUvLehrerUnterrichtsfach>> grouped = list.stream().collect(Collectors.groupingBy(f -> f.Lehrer_ID));
		return idsLehrer.stream().collect(Collectors.toMap(id -> id, id -> grouped.getOrDefault(id, Collections.emptyList())));
	}

}
