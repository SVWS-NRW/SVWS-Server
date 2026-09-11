package de.svws_nrw.repo.uv.schienen;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvSchiene;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Schienen (Tabelle UV_Schienen).
 */
public final class UvSchieneRepositoryImpl extends RepositoryImpl<DTOUvSchiene> implements UvSchieneRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvSchieneRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvSchiene.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOUvSchiene> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return conn.queryList(DTOUvSchiene.QUERY_BY_PLANUNGSABSCHNITT_ID, DTOUvSchiene.class, idPlanungsabschnitt);
	}

}
