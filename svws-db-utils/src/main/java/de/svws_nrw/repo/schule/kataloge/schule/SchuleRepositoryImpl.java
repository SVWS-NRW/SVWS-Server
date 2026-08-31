package de.svws_nrw.repo.schule.kataloge.schule;

import java.util.Map;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.repo.RepositoryImpl;

public final class SchuleRepositoryImpl extends RepositoryImpl<DTOSchuleNRW> implements SchuleRepository {

	/**
	 * @param conn {@link DBEntityManager}
	 */
	public SchuleRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuleNRW.class, s -> s.ID, (s, id) -> s.ID = id);
	}

	@Override
	public boolean existsBySchulnummer(final String schulnummer) {
		return conn.existsBy(DTOSchuleNRW.QUERY_BY_SCHULNR, DTOSchuleNRW.class, schulnummer);
	}

	@Override
	public Map<String, DTOSchuleNRW> getSchulenBySchulnummer() {
		return getMap(e -> e.SchulNr);
	}
}
