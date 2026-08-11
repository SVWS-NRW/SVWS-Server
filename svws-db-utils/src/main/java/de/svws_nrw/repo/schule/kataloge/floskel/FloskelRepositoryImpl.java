package de.svws_nrw.repo.schule.kataloge.floskel;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.katalog.DTOFloskeln;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf den Katalog der Floskeln.
 */
public final class FloskelRepositoryImpl extends RepositoryImpl<DTOFloskeln> implements FloskelRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public FloskelRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOFloskeln.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
