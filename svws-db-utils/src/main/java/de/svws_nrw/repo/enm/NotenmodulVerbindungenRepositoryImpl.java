package de.svws_nrw.repo.enm;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulVerbindungen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf Verbindungen des Notenmoduls.
 */
public final class NotenmodulVerbindungenRepositoryImpl extends RepositoryImpl<DTONotenmodulVerbindungen> implements NotenmodulVerbindungenRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public NotenmodulVerbindungenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTONotenmodulVerbindungen.class, o -> o.id, (o, id) -> o.id = id);
	}

}
