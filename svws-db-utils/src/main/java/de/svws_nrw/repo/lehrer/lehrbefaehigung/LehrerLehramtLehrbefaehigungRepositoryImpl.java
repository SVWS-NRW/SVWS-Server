package de.svws_nrw.repo.lehrer.lehrbefaehigung;

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
public final class LehrerLehramtLehrbefaehigungRepositoryImpl extends RepositoryImpl<DTOLehrerPersonaldatenLehramtBefaehigung>
		implements LehrerLehramtLehrbefaehigungRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public LehrerLehramtLehrbefaehigungRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLehrerPersonaldatenLehramtBefaehigung.class, o -> o.id, (o, id) -> o.id = id);
	}

	@Override
	public Map<Long, List<DTOLehrerPersonaldatenLehramtBefaehigung>> getLehrerLehrbefaehigungByIdLehramt(final Collection<Long> idsLehraemter) {
		if ((idsLehraemter == null) || (idsLehraemter.isEmpty())) {
			return Collections.emptyMap();
		}
		return conn.queryList(DTOLehrerPersonaldatenLehramtBefaehigung.QUERY_LIST_BY_IDLEHRAMT,
						DTOLehrerPersonaldatenLehramtBefaehigung.class, idsLehraemter)
				.stream()
				.collect(Collectors.groupingBy(f -> f.idLehramt));
	}

	@Override
	public List<DTOLehrerPersonaldatenLehramtBefaehigung> getByIdLehramt(final long idLehramt) {
		return conn.queryList(DTOLehrerPersonaldatenLehramtBefaehigung.QUERY_BY_IDLEHRAMT,
				DTOLehrerPersonaldatenLehramtBefaehigung.class, idLehramt);
	}

}
