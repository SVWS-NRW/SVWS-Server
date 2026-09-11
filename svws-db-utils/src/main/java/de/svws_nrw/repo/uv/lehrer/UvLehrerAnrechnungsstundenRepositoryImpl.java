package de.svws_nrw.repo.uv.lehrer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvLehrerAnrechnungsstunden;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf Lehrerdaten.
 */
public final class UvLehrerAnrechnungsstundenRepositoryImpl extends RepositoryImpl<DTOUvLehrerAnrechnungsstunden>
		implements UvLehrerAnrechnungsstundenRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvLehrerAnrechnungsstundenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvLehrerAnrechnungsstunden.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOUvLehrerAnrechnungsstunden> getListByLehrerId(final long idLehrer) {
		return conn.queryList(DTOUvLehrerAnrechnungsstunden.QUERY_BY_LEHRER_ID, DTOUvLehrerAnrechnungsstunden.class, idLehrer);
	}

	@Override
	public List<DTOUvLehrerAnrechnungsstunden> getListByLehrerIds(final Collection<Long> idsLehrer) {
		if ((idsLehrer == null) || idsLehrer.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList(DTOUvLehrerAnrechnungsstunden.QUERY_LIST_BY_LEHRER_ID, DTOUvLehrerAnrechnungsstunden.class, idsLehrer);
	}

	@Override
	public Map<Long, List<DTOUvLehrerAnrechnungsstunden>> getMapByLehrerIds(final Collection<Long> idsLehrer) {
		if ((idsLehrer == null) || idsLehrer.isEmpty()) {
			return Collections.emptyMap();
		}
		final var list = conn.queryList(DTOUvLehrerAnrechnungsstunden.QUERY_LIST_BY_LEHRER_ID, DTOUvLehrerAnrechnungsstunden.class, idsLehrer);
		final Map<Long, List<DTOUvLehrerAnrechnungsstunden>> grouped = list.stream().collect(Collectors.groupingBy(f -> f.Lehrer_ID));
		return idsLehrer.stream().collect(Collectors.toMap(id -> id, id -> grouped.getOrDefault(id, Collections.emptyList())));
	}

}
