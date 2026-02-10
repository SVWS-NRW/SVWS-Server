package de.svws_nrw.repo.jahrgaenge;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Jahrgangsdaten.
 */
public final class JahrgaengeRepositoryImpl extends RepositoryImpl<DTOJahrgang> implements JahrgaengeRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public JahrgaengeRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOJahrgang.class, (o, id) -> o.ID = id);
	}

}
