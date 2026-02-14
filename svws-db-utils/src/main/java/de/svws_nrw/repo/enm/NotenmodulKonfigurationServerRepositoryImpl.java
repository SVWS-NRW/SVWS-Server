package de.svws_nrw.repo.enm;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulKonfigurationServer;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Server-spezifische Konfiguration des Notenmoduls.
 */
public final class NotenmodulKonfigurationServerRepositoryImpl extends RepositoryBaseImpl<DTONotenmodulKonfigurationServer, String>
		implements NotenmodulKonfigurationServerRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public NotenmodulKonfigurationServerRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTONotenmodulKonfigurationServer.class);
	}

	@Override
	protected Object[] mapIdToParameter(final String id) {
		return new Object[] { id };
	}

}
