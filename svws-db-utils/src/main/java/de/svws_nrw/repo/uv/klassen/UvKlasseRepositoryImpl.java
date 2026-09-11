package de.svws_nrw.repo.uv.klassen;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvKlasse;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Klassen
 * (Tabelle UV_Klassen).
 */
public final class UvKlasseRepositoryImpl extends RepositoryImpl<DTOUvKlasse> implements UvKlasseRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvKlasseRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvKlasse.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOUvKlasse> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return conn.queryList(DTOUvKlasse.QUERY_BY_PLANUNGSABSCHNITT_ID, DTOUvKlasse.class, idPlanungsabschnitt);
	}

}
