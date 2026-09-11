package de.svws_nrw.repo.uv.planungsabschnitte;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnitt;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Planungsabschnitte
 * (Tabelle UV_Planungsabschnitte).
 */
public final class UvPlanungsabschnittRepositoryImpl extends RepositoryImpl<DTOUvPlanungsabschnitt> implements UvPlanungsabschnittRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvPlanungsabschnittRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvPlanungsabschnitt.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOUvPlanungsabschnitt> getListBySchuljahr(final Integer schuljahr) {
		if (schuljahr == null) {
			return conn.queryAll(DTOUvPlanungsabschnitt.class);
		}
		return conn.queryList(DTOUvPlanungsabschnitt.QUERY_BY_SCHULJAHR, DTOUvPlanungsabschnitt.class, schuljahr);
	}

}
