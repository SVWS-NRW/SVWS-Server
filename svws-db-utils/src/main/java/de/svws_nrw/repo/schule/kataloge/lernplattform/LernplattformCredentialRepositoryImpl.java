package de.svws_nrw.repo.schule.kataloge.lernplattform;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentialsLernplattformen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Zugangsdaten zu Lernplattformen.
 */
public final class LernplattformCredentialRepositoryImpl extends RepositoryImpl<DTOCredentialsLernplattformen>
		implements LernplattformCredentialRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public LernplattformCredentialRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOCredentialsLernplattformen.class, c -> c.ID, (c, id) -> c.ID = id);
	}

}
