package de.svws_nrw.repo.uv.lehrer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvLehrerPflichtstundensoll;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf Lehrerdaten.
 */
public final class UvLehrerPflichtstundensollRepositoryImpl extends RepositoryImpl<DTOUvLehrerPflichtstundensoll>
		implements UvLehrerPflichtstundensollRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvLehrerPflichtstundensollRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvLehrerPflichtstundensoll.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOUvLehrerPflichtstundensoll> getListByLehrerId(final long idLehrer) {
		return conn.queryList(DTOUvLehrerPflichtstundensoll.QUERY_BY_LEHRER_ID, DTOUvLehrerPflichtstundensoll.class, idLehrer);
	}

	@Override
	public List<DTOUvLehrerPflichtstundensoll> getListByLehrerIds(final Collection<Long> idsLehrer) {
		if ((idsLehrer == null) || idsLehrer.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOUvLehrerPflichtstundensoll.QUERY_LIST_BY_LEHRER_ID, DTOUvLehrerPflichtstundensoll.class, idsLehrer);
	}

	@Override
	public Map<Long, List<DTOUvLehrerPflichtstundensoll>> getMapByLehrerIds(final Collection<Long> idsLehrer) {
		if ((idsLehrer == null) || idsLehrer.isEmpty()) {
			return Collections.emptyMap();
		}
		final var list = conn.queryList(DTOUvLehrerPflichtstundensoll.QUERY_LIST_BY_LEHRER_ID, DTOUvLehrerPflichtstundensoll.class, idsLehrer);
		final Map<Long, List<DTOUvLehrerPflichtstundensoll>> grouped = list.stream().collect(Collectors.groupingBy(f -> f.Lehrer_ID));
		return idsLehrer.stream().collect(Collectors.toMap(id -> id, id -> grouped.getOrDefault(id, Collections.emptyList())));
	}

}
