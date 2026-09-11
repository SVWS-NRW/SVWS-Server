package de.svws_nrw.repo.uv.unterrichte;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvUnterrichteLerngruppenlehrer;
import de.svws_nrw.db.dto.current.uv.DTOUvUnterrichteLerngruppenlehrerPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Unterricht-Lerngruppenlehrer-Zuordnungen
 * (Tabelle UV_Unterrichte_Lerngruppenlehrer).
 */
public final class UvUnterrichtLerngruppenlehrerRepositoryImpl
		extends RepositoryBaseImpl<DTOUvUnterrichteLerngruppenlehrer, DTOUvUnterrichteLerngruppenlehrerPK>
		implements UvUnterrichtLerngruppenlehrerRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvUnterrichtLerngruppenlehrerRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvUnterrichteLerngruppenlehrer.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOUvUnterrichteLerngruppenlehrerPK id) {
		return new Object[] { id.Unterricht_ID, id.LerngruppenLehrer_ID };
	}

	@Override
	public List<DTOUvUnterrichteLerngruppenlehrer> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return conn.queryList(DTOUvUnterrichteLerngruppenlehrer.QUERY_BY_PLANUNGSABSCHNITT_ID,
				DTOUvUnterrichteLerngruppenlehrer.class, idPlanungsabschnitt);
	}

}
