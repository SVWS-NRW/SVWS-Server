package de.svws_nrw.repo.uv.zeitraster;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittZeitraster;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittZeitrasterPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Zeitraster-Zuordnungen
 * (Tabelle UV_Planungsabschnitte_Zeitraster).
 */
public final class UvPlanungsabschnittZeitrasterRepositoryImpl
		extends RepositoryBaseImpl<DTOUvPlanungsabschnittZeitraster, DTOUvPlanungsabschnittZeitrasterPK>
		implements UvPlanungsabschnittZeitrasterRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvPlanungsabschnittZeitrasterRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvPlanungsabschnittZeitraster.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOUvPlanungsabschnittZeitrasterPK id) {
		return new Object[] { id.Planungsabschnitt_ID, id.Zeitraster_ID };
	}

	@Override
	public List<DTOUvPlanungsabschnittZeitraster> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return conn.queryList(DTOUvPlanungsabschnittZeitraster.QUERY_BY_PLANUNGSABSCHNITT_ID,
				DTOUvPlanungsabschnittZeitraster.class, idPlanungsabschnitt);
	}

}
