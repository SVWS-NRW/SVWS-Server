package de.svws_nrw.repo.uv.stundentafeln;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvStundentafel;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die UV-Stundentafeln
 * (Tabelle UV_Stundentafeln).
 */
public final class UvStundentafelRepositoryImpl extends RepositoryImpl<DTOUvStundentafel> implements UvStundentafelRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public UvStundentafelRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOUvStundentafel.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
