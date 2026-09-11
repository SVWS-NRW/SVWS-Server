package de.svws_nrw.repo.uv.schueler;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeConstraintJahrgang;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeConstraintJahrgangPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Schülergruppen-Jahrgang-Constraints
 * (Tabelle UV_Schuelergruppen_Constraint_Jahrgaenge).
 */
public final class UvSchuelergruppeConstraintJahrgangRepositoryImpl
		extends RepositoryBaseImpl<DTOUvSchuelergruppeConstraintJahrgang, DTOUvSchuelergruppeConstraintJahrgangPK>
		implements UvSchuelergruppeConstraintJahrgangRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvSchuelergruppeConstraintJahrgangRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvSchuelergruppeConstraintJahrgang.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOUvSchuelergruppeConstraintJahrgangPK id) {
		return new Object[] { id.Schuelergruppe_ID, id.Jahrgang_ID };
	}

	@Override
	public List<DTOUvSchuelergruppeConstraintJahrgang> getListBySchuelergruppe(final long idSchuelergruppe) {
		return conn.queryList(DTOUvSchuelergruppeConstraintJahrgang.QUERY_BY_SCHUELERGRUPPE_ID,
				DTOUvSchuelergruppeConstraintJahrgang.class, idSchuelergruppe);
	}

	@Override
	public List<DTOUvSchuelergruppeConstraintJahrgang> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return conn.queryList(DTOUvSchuelergruppeConstraintJahrgang.QUERY_BY_PLANUNGSABSCHNITT_ID,
				DTOUvSchuelergruppeConstraintJahrgang.class, idPlanungsabschnitt);
	}

}
