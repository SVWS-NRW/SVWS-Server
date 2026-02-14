package de.svws_nrw.repo.enm;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulCredentials;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Lehrer-Credentials für das externe Notenmodul.
 */
public final class NotenmodulCredentialsRepositoryImpl extends RepositoryImpl<DTONotenmodulCredentials> implements NotenmodulCredentialsRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public NotenmodulCredentialsRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTONotenmodulCredentials.class, o -> o.idLehrer, (o, id) -> o.idLehrer = id);
	}

}
