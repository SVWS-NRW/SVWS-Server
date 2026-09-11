package de.svws_nrw.repo.uv.kurse;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvKurs;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Kurse (Tabelle UV_Kurse).
 */
public final class UvKursRepositoryImpl extends RepositoryImpl<DTOUvKurs> implements UvKursRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvKursRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvKurs.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOUvKurs> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return conn.queryList(DTOUvKurs.QUERY_BY_PLANUNGSABSCHNITT_ID, DTOUvKurs.class, idPlanungsabschnitt);
	}

}
