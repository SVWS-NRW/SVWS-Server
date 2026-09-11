package de.svws_nrw.repo.uv.raeume;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvRaumgruppe;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Raumgruppen
 * (Tabelle UV_Raumgruppen).
 */
public final class UvRaumgruppeRepositoryImpl extends RepositoryImpl<DTOUvRaumgruppe> implements UvRaumgruppeRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvRaumgruppeRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvRaumgruppe.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
