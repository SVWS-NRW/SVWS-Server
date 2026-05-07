package de.svws_nrw.repo.erzieher;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOSchuelerErzieherAdresse;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf Erzieher.
 */
public final class ErzieherRepositoryImpl extends RepositoryImpl<DTOSchuelerErzieherAdresse> implements ErzieherRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public ErzieherRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerErzieherAdresse.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
