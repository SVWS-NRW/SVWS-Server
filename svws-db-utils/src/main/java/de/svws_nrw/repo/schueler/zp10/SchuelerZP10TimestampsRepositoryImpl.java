package de.svws_nrw.repo.schueler.zp10;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerZP10;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Zeitstempel zu den Schüler-ZP10-Daten
 */
public final class SchuelerZP10TimestampsRepositoryImpl extends RepositoryImpl<DTOTimestampsSchuelerZP10>
		implements SchuelerZP10TimestampsRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerZP10TimestampsRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOTimestampsSchuelerZP10.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
