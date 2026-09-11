package de.svws_nrw.repo.uv.lerngruppen;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppeSchiene;
import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppeSchienePK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Lerngruppen-Schienen-Zuordnungen
 * (Tabelle UV_LerngruppeSchiene).
 */
public final class UvLerngruppenSchieneRepositoryImpl extends RepositoryBaseImpl<DTOUvLerngruppeSchiene, DTOUvLerngruppeSchienePK>
		implements UvLerngruppenSchieneRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvLerngruppenSchieneRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvLerngruppeSchiene.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOUvLerngruppeSchienePK id) {
		return new Object[] { id.Lerngruppe_ID, id.Schiene_ID };
	}

	@Override
	public List<DTOUvLerngruppeSchiene> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return conn.queryList(DTOUvLerngruppeSchiene.QUERY_BY_PLANUNGSABSCHNITT_ID, DTOUvLerngruppeSchiene.class, idPlanungsabschnitt);
	}

}
