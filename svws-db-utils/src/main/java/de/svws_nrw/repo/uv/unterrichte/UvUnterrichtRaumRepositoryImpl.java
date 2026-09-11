package de.svws_nrw.repo.uv.unterrichte;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvUnterrichtRaum;
import de.svws_nrw.db.dto.current.uv.DTOUvUnterrichtRaumPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Unterricht-Raum-Zuordnungen
 * (Tabelle UV_Unterrichte_Raeume).
 */
public final class UvUnterrichtRaumRepositoryImpl extends RepositoryBaseImpl<DTOUvUnterrichtRaum, DTOUvUnterrichtRaumPK>
		implements UvUnterrichtRaumRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvUnterrichtRaumRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvUnterrichtRaum.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOUvUnterrichtRaumPK id) {
		return new Object[] { id.Unterricht_ID, id.Raum_ID };
	}

	@Override
	public List<DTOUvUnterrichtRaum> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return conn.queryList(DTOUvUnterrichtRaum.QUERY_BY_PLANUNGSABSCHNITT_ID, DTOUvUnterrichtRaum.class, idPlanungsabschnitt);
	}

}
