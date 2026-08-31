package de.svws_nrw.repo.schule.kataloge.jahrgang;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Jahrgangsdaten.
 */
public final class JahrgangRepositoryImpl extends RepositoryImpl<DTOJahrgang> implements JahrgangRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public JahrgangRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOJahrgang.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public boolean existsById(final Long idJahrgang) {
		return conn.existsBy(DTOJahrgang.QUERY_BY_ID, DTOJahrgang.class, idJahrgang);
	}

}
