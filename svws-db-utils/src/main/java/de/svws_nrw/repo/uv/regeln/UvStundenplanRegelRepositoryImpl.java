package de.svws_nrw.repo.uv.regeln;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvStundenplanRegel;
import de.svws_nrw.repo.RepositoryImpl;

/** Datenbankzugriff auf die Blockungsregeln der UV. */
public final class UvStundenplanRegelRepositoryImpl extends RepositoryImpl<DTOUvStundenplanRegel> implements UvStundenplanRegelRepository {

	/**
	 * Erstellt das Repository.
	 *
	 * @param conn die Datenbankverbindung
	 */
	public UvStundenplanRegelRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvStundenplanRegel.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOUvStundenplanRegel> getListByKonfiguration(final long idKonfiguration) {
		return conn.queryList(DTOUvStundenplanRegel.QUERY_BY_STUNDENPLANKONFIGURATION_ID, DTOUvStundenplanRegel.class, idKonfiguration);
	}
}
