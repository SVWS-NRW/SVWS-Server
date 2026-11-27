package de.svws_nrw.repo.uv.regeln;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvStundenplanKonfiguration;
import de.svws_nrw.repo.RepositoryImpl;

/** Datenbankzugriff auf die Stundenplankonfigurationen der UV. */
public final class UvStundenplanKonfigurationRepositoryImpl extends RepositoryImpl<DTOUvStundenplanKonfiguration>
		implements UvStundenplanKonfigurationRepository {

	/**
	 * Erstellt das Repository.
	 *
	 * @param conn die Datenbankverbindung
	 */
	public UvStundenplanKonfigurationRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvStundenplanKonfiguration.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOUvStundenplanKonfiguration> getListByPlanungsabschnitt(final long idPlan) {
		return conn.queryList(DTOUvStundenplanKonfiguration.QUERY_BY_PLANUNGSABSCHNITT_ID, DTOUvStundenplanKonfiguration.class, idPlan);
	}
}
