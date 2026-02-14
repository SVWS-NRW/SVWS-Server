package de.svws_nrw.repo.enm;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulKonfigurationClient;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Client-spezifische Konfiguration des Notenmoduls.
 */
public final class NotenmodulKonfigurationClientRepositoryImpl extends RepositoryBaseImpl<DTONotenmodulKonfigurationClient, String>
		implements NotenmodulKonfigurationClientRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public NotenmodulKonfigurationClientRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTONotenmodulKonfigurationClient.class);
	}

	@Override
	protected Object[] mapIdToParameter(final String id) {
		return new Object[] { id };
	}

}
