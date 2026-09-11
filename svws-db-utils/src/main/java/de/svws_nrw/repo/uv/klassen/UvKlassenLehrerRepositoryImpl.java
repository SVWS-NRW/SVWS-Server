package de.svws_nrw.repo.uv.klassen;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvKlassenLehrer;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Klassen-Lehrer-Zuordnungen
 * (Tabelle UV_KlassenLehrer).
 */
public final class UvKlassenLehrerRepositoryImpl extends RepositoryImpl<DTOUvKlassenLehrer> implements UvKlassenLehrerRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvKlassenLehrerRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvKlassenLehrer.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOUvKlassenLehrer> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return conn.queryList(DTOUvKlassenLehrer.QUERY_BY_PLANUNGSABSCHNITT_ID, DTOUvKlassenLehrer.class, idPlanungsabschnitt);
	}

}
