package de.svws_nrw.repo.uv.schueler;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeConstraintSchuelergruppe;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeConstraintSchuelergruppePK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Schülergruppen-Gruppen-Constraints
 * (Tabelle UV_Schuelergruppen_Constraint_Schuelergruppen).
 */
public final class UvSchuelergruppeConstraintSchuelergruppeRepositoryImpl
		extends RepositoryBaseImpl<DTOUvSchuelergruppeConstraintSchuelergruppe, DTOUvSchuelergruppeConstraintSchuelergruppePK>
		implements UvSchuelergruppeConstraintSchuelergruppeRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvSchuelergruppeConstraintSchuelergruppeRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvSchuelergruppeConstraintSchuelergruppe.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOUvSchuelergruppeConstraintSchuelergruppePK id) {
		return new Object[] { id.Schuelergruppe_ID, id.Schuelergruppe_Vaild_ID };
	}

	@Override
	public List<DTOUvSchuelergruppeConstraintSchuelergruppe> getListBySchuelergruppe(final long idSchuelergruppe) {
		return conn.queryList(DTOUvSchuelergruppeConstraintSchuelergruppe.QUERY_BY_SCHUELERGRUPPE_ID,
				DTOUvSchuelergruppeConstraintSchuelergruppe.class, idSchuelergruppe);
	}

	@Override
	public List<DTOUvSchuelergruppeConstraintSchuelergruppe> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return conn.queryList(DTOUvSchuelergruppeConstraintSchuelergruppe.QUERY_BY_PLANUNGSABSCHNITT_ID,
				DTOUvSchuelergruppeConstraintSchuelergruppe.class, idPlanungsabschnitt);
	}

}
