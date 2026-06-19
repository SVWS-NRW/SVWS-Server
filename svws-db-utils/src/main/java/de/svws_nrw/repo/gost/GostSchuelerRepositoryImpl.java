package de.svws_nrw.repo.gost;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf Schülerdaten für die Gymnasiale Oberstufe.
 */
public class GostSchuelerRepositoryImpl extends RepositoryImpl<DTOGostSchueler> implements GostSchuelerRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public GostSchuelerRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOGostSchueler.class, o -> o.Schueler_ID, (o, id) -> o.Schueler_ID	 = id);
	}

}
