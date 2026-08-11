package de.svws_nrw.repo.schule.kataloge.ort;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Ortsdaten.
 */
public final class OrtRepositoryImpl extends RepositoryImpl<DTOOrt> implements OrtRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public OrtRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOOrt.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
