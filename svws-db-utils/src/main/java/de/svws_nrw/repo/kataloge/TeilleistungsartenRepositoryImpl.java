package de.svws_nrw.repo.kataloge;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOTeilleistungsarten;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Arten von Teilleistungen
 */
public final class TeilleistungsartenRepositoryImpl extends RepositoryImpl<DTOTeilleistungsarten> implements TeilleistungsartenRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public TeilleistungsartenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOTeilleistungsarten.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
