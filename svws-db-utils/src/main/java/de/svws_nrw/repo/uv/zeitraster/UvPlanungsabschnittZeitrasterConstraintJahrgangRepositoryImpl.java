package de.svws_nrw.repo.uv.zeitraster;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang;
import de.svws_nrw.db.dto.current.uv.DTOUv_PlanungsabschnittZeitraster_Constraint_JahrgangPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Zeitraster-Jahrgang-Constraints
 * (Tabelle UV_PlanungsabschnitteZeitraster_Constraint_Jahrgaenge).
 */
public final class UvPlanungsabschnittZeitrasterConstraintJahrgangRepositoryImpl
		extends RepositoryBaseImpl<DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang, DTOUv_PlanungsabschnittZeitraster_Constraint_JahrgangPK>
		implements UvPlanungsabschnittZeitrasterConstraintJahrgangRepository {

	/** JPQL-Query für die Abfrage nach Planungsabschnitt und Zeitraster */
	private static final String QUERY_BY_PA_ZR =
			"SELECT e FROM DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang e "
					+ "WHERE e.Planungsabschnitt_ID = ?1 AND e.Zeitraster_ID = ?2";

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvPlanungsabschnittZeitrasterConstraintJahrgangRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOUv_PlanungsabschnittZeitraster_Constraint_JahrgangPK id) {
		return new Object[] { id.Planungsabschnitt_ID, id.Zeitraster_ID, id.Jahrgang_ID };
	}

	@Override
	public List<DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang> getListByPlanungsabschnittAndZeitraster(
			final long idPlanungsabschnitt, final long idZeitraster) {
		return conn.queryList(QUERY_BY_PA_ZR, DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang.class,
				idPlanungsabschnitt, idZeitraster);
	}

	@Override
	public List<DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return conn.queryList(DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang.QUERY_BY_PLANUNGSABSCHNITT_ID,
				DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang.class, idPlanungsabschnitt);
	}

}
