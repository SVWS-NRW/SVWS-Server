package de.svws_nrw.repo.schueler.teilleistung;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerTeilleistungen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Zeitstempel zu den Schüler-Teilleistungen
 */
public final class SchuelerTeilleistungTimestampRepositoryImpl extends RepositoryImpl<DTOTimestampsSchuelerTeilleistungen>
		implements SchuelerTeilleistungTimestampRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerTeilleistungTimestampRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOTimestampsSchuelerTeilleistungen.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
