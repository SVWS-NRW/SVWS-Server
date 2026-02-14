package de.svws_nrw.repo.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLernabschnittsdaten;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Zeitstempel zu den Schüler-Lernabschnittsdaten
 */
public final class SchuelerLernabschnittTimestampsRepositoryImpl extends RepositoryImpl<DTOTimestampsSchuelerLernabschnittsdaten>
		implements SchuelerLernabschnittTimestampsRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerLernabschnittTimestampsRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOTimestampsSchuelerLernabschnittsdaten.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
