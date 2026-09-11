package de.svws_nrw.repo.uv.schienen;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvSchienenConstraintJahrgang;
import de.svws_nrw.db.dto.current.uv.DTOUvSchienenConstraintJahrgangPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Schienen-Jahrgang-Constraints
 * (Tabelle UV_SchienenConstraintJahrgang).
 */
public final class UvSchieneConstraintJahrgangRepositoryImpl extends RepositoryBaseImpl<DTOUvSchienenConstraintJahrgang, DTOUvSchienenConstraintJahrgangPK>
		implements UvSchieneConstraintJahrgangRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvSchieneConstraintJahrgangRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvSchienenConstraintJahrgang.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOUvSchienenConstraintJahrgangPK id) {
		return new Object[] { id.Schiene_ID, id.Jahrgang_ID };
	}

	@Override
	public List<DTOUvSchienenConstraintJahrgang> getListBySchieneId(final long idSchiene) {
		return conn.queryList(DTOUvSchienenConstraintJahrgang.QUERY_BY_SCHIENE_ID, DTOUvSchienenConstraintJahrgang.class, idSchiene);
	}

	@Override
	public List<DTOUvSchienenConstraintJahrgang> getListBySchieneIds(final List<Long> schieneIds) {
		if (schieneIds.isEmpty()) {
			return List.of();
		}
		return conn.queryList(DTOUvSchienenConstraintJahrgang.QUERY_LIST_BY_SCHIENE_ID, DTOUvSchienenConstraintJahrgang.class, schieneIds);
	}

}
