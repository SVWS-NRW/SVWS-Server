package de.svws_nrw.repo.lehrer.lehramt;

import java.util.Collections;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
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
		super(conn, DTOLehrerPersonaldatenLehramt.class, o -> o.id, (o, id) -> o.id = id);
	}

	@Override
	public List<DTOLehrerPersonaldatenLehramt> findByIdsLehrer(final List<Long> idsLehrer) {
		if ((idsLehrer == null) || (idsLehrer.isEmpty())) {
			return Collections.emptyList();
		}
		return conn.queryList(
				DTOLehrerPersonaldatenLehramt.QUERY_LIST_BY_IDLEHRER,
				DTOLehrerPersonaldatenLehramt.class,
				idsLehrer
		);
	}

	@Override
	public boolean existsById(final Long idLehramt) {
		return conn.existsBy(DTOLehrerPersonaldatenLehramt.QUERY_BY_ID, DTOLehrerPersonaldatenLehramt.class, idLehramt);
	}

}
