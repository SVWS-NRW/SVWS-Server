package de.svws_nrw.repo.lehrer.lehrbefaehigung;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtBefaehigung;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf Lehrbefähigungen zu einem Lehramt eines Lehrers.
 */
public final class LehrerLehramtLehrbefaehigungenRepositoryImpl extends RepositoryImpl<DTOLehrerPersonaldatenLehramtBefaehigung>
		implements LehrerLehramtLehrbefaehigungenRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public LehrerLehramtLehrbefaehigungenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLehrerPersonaldatenLehramtBefaehigung.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public Map<Long, List<DTOLehrerPersonaldatenLehramtBefaehigung>> getMapByLehramt(final Collection<Long> idsLehraemter) {
		if ((idsLehraemter == null) || (idsLehraemter.isEmpty())) {
			return Collections.emptyMap();
		}
		final var list = conn.queryList(DTOLehrerPersonaldatenLehramtBefaehigung.QUERY_LIST_BY_LEHRERAMT_ID,
				DTOLehrerPersonaldatenLehramtBefaehigung.class, idsLehraemter);
		final var map = list.stream().collect(Collectors.groupingBy(f -> f.Lehreramt_ID));
		for (final long idLehramt : idsLehraemter) {
			map.computeIfAbsent(idLehramt, id -> new ArrayList<>());
		}
		return map;
	}

}
