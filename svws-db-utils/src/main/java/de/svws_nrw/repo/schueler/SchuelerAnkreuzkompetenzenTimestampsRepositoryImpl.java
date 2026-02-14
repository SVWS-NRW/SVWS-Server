package de.svws_nrw.repo.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerAnkreuzkompetenzen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Zeitstempel zu den Schüler-Ankreuzkompetenzen
 */
public final class SchuelerAnkreuzkompetenzenTimestampsRepositoryImpl extends RepositoryImpl<DTOTimestampsSchuelerAnkreuzkompetenzen>
		implements SchuelerAnkreuzkompetenzenTimestampsRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerAnkreuzkompetenzenTimestampsRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOTimestampsSchuelerAnkreuzkompetenzen.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
