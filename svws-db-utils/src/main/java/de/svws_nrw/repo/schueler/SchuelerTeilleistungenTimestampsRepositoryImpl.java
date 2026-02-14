package de.svws_nrw.repo.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerTeilleistungen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Zeitstempel zu den Schüler-Teilleistungen
 */
public final class SchuelerTeilleistungenTimestampsRepositoryImpl extends RepositoryImpl<DTOTimestampsSchuelerTeilleistungen>
		implements SchuelerTeilleistungenTimestampsRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerTeilleistungenTimestampsRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOTimestampsSchuelerTeilleistungen.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
