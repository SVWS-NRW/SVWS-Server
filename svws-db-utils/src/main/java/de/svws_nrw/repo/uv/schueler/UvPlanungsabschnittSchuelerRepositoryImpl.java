package de.svws_nrw.repo.uv.schueler;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittSchueler;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittSchuelerPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Planungsabschnitt-Schüler-Zuordnungen
 * (Tabelle UV_PlanungsabschnittSchueler).
 */
public final class UvPlanungsabschnittSchuelerRepositoryImpl
		extends RepositoryBaseImpl<DTOUvPlanungsabschnittSchueler, DTOUvPlanungsabschnittSchuelerPK>
		implements UvPlanungsabschnittSchuelerRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvPlanungsabschnittSchuelerRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvPlanungsabschnittSchueler.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOUvPlanungsabschnittSchuelerPK id) {
		return new Object[] { id.Planungsabschnitt_ID, id.Schueler_ID };
	}

	@Override
	public List<DTOUvPlanungsabschnittSchueler> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return conn.queryList(DTOUvPlanungsabschnittSchueler.QUERY_BY_PLANUNGSABSCHNITT_ID, DTOUvPlanungsabschnittSchueler.class, idPlanungsabschnitt);
	}

	@Override
	public List<DTOSchueler> getSchuelerByIds(final Collection<Long> schuelerIds) {
		if ((schuelerIds == null) || schuelerIds.isEmpty()) {
			return List.of();
		}
		return conn.queryByKeyList(DTOSchueler.class, schuelerIds);
	}

}
