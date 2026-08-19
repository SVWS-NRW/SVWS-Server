package de.svws_nrw.repo.lehrer.fachrichtung;

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
public final class LehrerLehramtFachrichtungRepositoryImpl extends RepositoryImpl<DTOLehrerPersonaldatenLehramtFachrichtung>
		implements LehrerLehramtFachrichtungRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public LehrerLehramtFachrichtungRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLehrerPersonaldatenLehramtFachrichtung.class, o -> o.id, (o, id) -> o.id = id);
	}

	@Override
	public Map<Long, List<DTOLehrerPersonaldatenLehramtFachrichtung>> getLehrerFachrichtungenByIdLehramt(final Collection<Long> idsLehraemter) {
		if ((idsLehraemter == null) || (idsLehraemter.isEmpty())) {
			return Collections.emptyMap();
		}

		return conn.queryList(DTOLehrerPersonaldatenLehramtFachrichtung.QUERY_LIST_BY_IDLEHRAMT,
						DTOLehrerPersonaldatenLehramtFachrichtung.class, idsLehraemter)
				.stream()
				.collect(Collectors.groupingBy(f -> f.idLehramt));
	}

	@Override
	public List<DTOLehrerPersonaldatenLehramtFachrichtung> getByLehramtId(final long idLehramt) {
		return conn.queryList(DTOLehrerPersonaldatenLehramtFachrichtung.QUERY_BY_IDLEHRAMT,
				DTOLehrerPersonaldatenLehramtFachrichtung.class, idLehramt);
	}

}
