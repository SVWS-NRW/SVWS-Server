package de.svws_nrw.repo.lehrer.lehramt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramt;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff die Lehrämter eines Lehrers.
 */
public final class LehrerLehramtRepositoryImpl extends RepositoryImpl<DTOLehrerPersonaldatenLehramt>
		implements LehrerLehramtRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public LehrerLehramtRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLehrerPersonaldatenLehramt.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public Map<Long, List<DTOLehrerPersonaldatenLehramt>> getMapByLehrerID(final Collection<Long> idsLehrer) {
		if ((idsLehrer == null) || (idsLehrer.isEmpty())) {
			return Collections.emptyMap();
		}
		final var list = conn.queryList(DTOLehrerPersonaldatenLehramt.QUERY_LIST_BY_LEHRER_ID, DTOLehrerPersonaldatenLehramt.class, idsLehrer);
		final var map = list.stream().collect(Collectors.groupingBy(f -> f.Lehrer_ID));
		for (final long idLehrer : idsLehrer) {
			map.computeIfAbsent(idLehrer, id -> new ArrayList<>());
		}
		return map;
	}

	@Override
	public boolean existsById(final Long idLehramt) {
		return conn.existsBy(DTOLehrer.QUERY_BY_ID, DTOLehrer.class, idLehramt);
	}

}
