package de.svws_nrw.repo.enm;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsNotenmodulCredentials;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Zeitstempel für die Lehrer-Credentials des Notenmoduls.
 */
public final class NotenmodulCredentialsTimestampsRepositoryImpl extends RepositoryImpl<DTOTimestampsNotenmodulCredentials>
		implements NotenmodulCredentialsTimestampsRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public NotenmodulCredentialsTimestampsRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOTimestampsNotenmodulCredentials.class, o -> o.idLehrer, (o, id) -> o.idLehrer = id);
	}

}
