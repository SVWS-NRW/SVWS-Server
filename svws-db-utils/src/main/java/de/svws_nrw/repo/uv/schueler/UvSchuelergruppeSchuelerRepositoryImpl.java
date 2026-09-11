package de.svws_nrw.repo.uv.schueler;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeSchueler;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeSchuelerPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Schülergruppe-Schüler-Zuordnungen
 * (Tabelle UV_Schuelergruppen_Schueler).
 */
public final class UvSchuelergruppeSchuelerRepositoryImpl
		extends RepositoryBaseImpl<DTOUvSchuelergruppeSchueler, DTOUvSchuelergruppeSchuelerPK>
		implements UvSchuelergruppeSchuelerRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvSchuelergruppeSchuelerRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvSchuelergruppeSchueler.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOUvSchuelergruppeSchuelerPK id) {
		return new Object[] { id.Schuelergruppe_ID, id.Schueler_ID };
	}

	@Override
	public List<DTOUvSchuelergruppeSchueler> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return conn.queryList(DTOUvSchuelergruppeSchueler.QUERY_BY_PLANUNGSABSCHNITT_ID, DTOUvSchuelergruppeSchueler.class, idPlanungsabschnitt);
	}

}
