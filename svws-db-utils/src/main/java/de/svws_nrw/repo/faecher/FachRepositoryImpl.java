package de.svws_nrw.repo.faecher;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Fächerdaten.
 */
public final class FachRepositoryImpl extends RepositoryImpl<DTOFach> implements FachRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public FachRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOFach.class, (o, id) -> o.ID = id);
	}

}
