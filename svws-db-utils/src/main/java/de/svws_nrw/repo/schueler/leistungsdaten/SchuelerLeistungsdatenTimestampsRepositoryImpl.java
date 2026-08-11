package de.svws_nrw.repo.schueler.leistungsdaten;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLeistungsdaten;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Zeitstempel zu den Schüler-Leistungsdaten
 */
public final class SchuelerLeistungsdatenTimestampsRepositoryImpl extends RepositoryImpl<DTOTimestampsSchuelerLeistungsdaten>
		implements SchuelerLeistungsdatenTimestampsRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerLeistungsdatenTimestampsRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOTimestampsSchuelerLeistungsdaten.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
