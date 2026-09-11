package de.svws_nrw.repo.uv.lerngruppen;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppenLehrer;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Lerngruppen-Lehrer-Zuordnungen
 * (Tabelle UV_LerngruppenLehrer).
 */
public final class UvLerngruppenLehrerRepositoryImpl extends RepositoryImpl<DTOUvLerngruppenLehrer> implements UvLerngruppenLehrerRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvLerngruppenLehrerRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvLerngruppenLehrer.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOUvLerngruppenLehrer> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return conn.queryList(DTOUvLerngruppenLehrer.QUERY_BY_PLANUNGSABSCHNITT_ID, DTOUvLerngruppenLehrer.class, idPlanungsabschnitt);
	}

}
