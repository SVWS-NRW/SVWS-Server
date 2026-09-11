package de.svws_nrw.repo.uv.lehrer;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittLehrer;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittLehrerPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Planungsabschnitt-Lehrer-Zuordnungen
 * (Tabelle UV_PlanungsabschnittLehrer).
 */
public final class UvPlanungsabschnittLehrerRepositoryImpl extends RepositoryBaseImpl<DTOUvPlanungsabschnittLehrer, DTOUvPlanungsabschnittLehrerPK>
		implements UvPlanungsabschnittLehrerRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvPlanungsabschnittLehrerRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvPlanungsabschnittLehrer.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOUvPlanungsabschnittLehrerPK id) {
		return new Object[] { id.Planungsabschnitt_ID, id.Lehrer_ID };
	}

	@Override
	public List<DTOUvPlanungsabschnittLehrer> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return conn.queryList(DTOUvPlanungsabschnittLehrer.QUERY_BY_PLANUNGSABSCHNITT_ID, DTOUvPlanungsabschnittLehrer.class, idPlanungsabschnitt);
	}

}
