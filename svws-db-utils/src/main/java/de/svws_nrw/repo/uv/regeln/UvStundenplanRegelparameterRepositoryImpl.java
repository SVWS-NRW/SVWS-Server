package de.svws_nrw.repo.uv.regeln;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvStundenplanRegelparameter;
import de.svws_nrw.db.dto.current.uv.DTOUvStundenplanRegelparameterPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/** Datenbankzugriff auf die Parameter der UV-Blockungsregeln. */
public final class UvStundenplanRegelparameterRepositoryImpl
		extends RepositoryBaseImpl<DTOUvStundenplanRegelparameter, DTOUvStundenplanRegelparameterPK>
		implements UvStundenplanRegelparameterRepository {

	/**
	 * Erstellt das Repository.
	 *
	 * @param conn die Datenbankverbindung
	 */
	public UvStundenplanRegelparameterRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvStundenplanRegelparameter.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOUvStundenplanRegelparameterPK id) {
		return new Object[] { id.Regel_ID, id.Nummer };
	}

	@Override
	public List<DTOUvStundenplanRegelparameter> getListByRegel(final long idRegel) {
		return conn.queryList(DTOUvStundenplanRegelparameter.QUERY_BY_REGEL_ID, DTOUvStundenplanRegelparameter.class, idRegel);
	}
}
