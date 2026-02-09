package de.svws_nrw.repo.lehrer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtFachrichtung;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf Fachrichtung zu einem Lehramt eines Lehrers.
 */
public final class LehrerPersonaldatenLehramtFachrichtungRepositoryImpl extends RepositoryImpl<DTOLehrerPersonaldatenLehramtFachrichtung>
		implements LehrerPersonaldatenLehramtFachrichtungRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public LehrerPersonaldatenLehramtFachrichtungRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLehrerPersonaldatenLehramtFachrichtung.class, (o, id) -> o.ID = id);
	}

	@Override
	public Map<Long, List<DTOLehrerPersonaldatenLehramtFachrichtung>> getMapByLehramt(final Collection<Long> idsLehraemter) {
		if ((idsLehraemter == null) || (idsLehraemter.isEmpty()))
			return Collections.emptyMap();
		final var list = conn.queryList(DTOLehrerPersonaldatenLehramtFachrichtung.QUERY_LIST_BY_LEHRERAMT_ID,
				DTOLehrerPersonaldatenLehramtFachrichtung.class, idsLehraemter);
		final var map = list.stream().collect(Collectors.groupingBy(f -> f.Lehreramt_ID));
		for (final long idLehramt : idsLehraemter) {
			map.computeIfAbsent(idLehramt, id -> new ArrayList<>());
		}
		return map;
	}

}
