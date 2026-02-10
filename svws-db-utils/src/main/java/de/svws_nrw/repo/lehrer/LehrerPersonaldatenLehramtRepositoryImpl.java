package de.svws_nrw.repo.lehrer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramt;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff die Lehrämter eines Lehrers.
 */
public final class LehrerPersonaldatenLehramtRepositoryImpl extends RepositoryImpl<DTOLehrerPersonaldatenLehramt>
		implements LehrerPersonaldatenLehramtRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public LehrerPersonaldatenLehramtRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLehrerPersonaldatenLehramt.class, (o, id) -> o.ID = id);
	}

	@Override
	public Map<Long, List<DTOLehrerPersonaldatenLehramt>> getMapByLehrerID(final Collection<Long> idsLehrer) {
		if ((idsLehrer == null) || (idsLehrer.isEmpty()))
			return Collections.emptyMap();
		final var list = conn.queryList(DTOLehrerPersonaldatenLehramt.QUERY_LIST_BY_LEHRER_ID, DTOLehrerPersonaldatenLehramt.class, idsLehrer);
		final var map = list.stream().collect(Collectors.groupingBy(f -> f.Lehrer_ID));
		for (final long idLehrer : idsLehrer) {
			map.computeIfAbsent(idLehrer, id -> new ArrayList<>());
		}
		return map;
	}

}
