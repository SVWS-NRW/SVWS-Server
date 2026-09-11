package de.svws_nrw.repo.uv.schueler;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppe;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Schülergruppen
 * (Tabelle UV_Schuelergruppen).
 */
public final class UvSchuelergruppeRepositoryImpl extends RepositoryImpl<DTOUvSchuelergruppe> implements UvSchuelergruppeRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvSchuelergruppeRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvSchuelergruppe.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOUvSchuelergruppe> getListByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return conn.queryList(DTOUvSchuelergruppe.QUERY_BY_PLANUNGSABSCHNITT_ID, DTOUvSchuelergruppe.class, idPlanungsabschnitt);
	}

}
