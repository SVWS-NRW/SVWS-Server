package de.svws_nrw.repo.benutzer;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentials;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Credentials-Tabelle in der SVWS-Datenbank
 */
public class CredentialsRepositoryImpl extends RepositoryImpl<DTOCredentials> implements CredentialsRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public CredentialsRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOCredentials.class, o -> o.ID, (o, id) -> o.ID	 = id);
	}

}
