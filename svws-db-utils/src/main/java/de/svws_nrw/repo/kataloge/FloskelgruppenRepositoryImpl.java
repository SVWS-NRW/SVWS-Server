package de.svws_nrw.repo.kataloge;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.katalog.DTOFloskelgruppen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf den Katalog der Floskelgruppen.
 */
public final class FloskelgruppenRepositoryImpl extends RepositoryImpl<DTOFloskelgruppen> implements FloskelgruppenRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public FloskelgruppenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOFloskelgruppen.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
