package de.svws_nrw.repo.schule.kataloge.schulen;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.repo.RepositoryImpl;

public final class SchulenRepositoryImpl extends RepositoryImpl<DTOSchuleNRW> implements SchulenRepository {

	/**
	 * @param conn {@link DBEntityManager}
	 */
	public SchulenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuleNRW.class, s -> s.ID, (s, id) -> s.ID = id);
	}

	@Override
	public boolean existsBySchulnummer(final String schulnummer) {
		return conn.existsBy(DTOSchuleNRW.QUERY_BY_SCHULNR, DTOSchuleNRW.class, schulnummer);
	}
}
