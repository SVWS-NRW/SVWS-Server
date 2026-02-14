package de.svws_nrw.repo.kurse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf Kurse.
 */
public final class KurseRepositoryImpl extends RepositoryImpl<DTOKurs> implements KurseRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public KurseRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOKurs.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOKurs> getListBySchuljahresabschnitt(final long idSchuljahresabschnitt) {
		return conn.queryList(DTOKurs.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKurs.class, idSchuljahresabschnitt);
	}

	@Override
	public Map<Long, DTOKurs> getMapBySchuljahresabschnitt(final long idSchuljahresabschnitt) {
		return this.getListBySchuljahresabschnitt(idSchuljahresabschnitt).stream().collect(Collectors.toMap(e -> e.ID, e -> e));
	}

}
