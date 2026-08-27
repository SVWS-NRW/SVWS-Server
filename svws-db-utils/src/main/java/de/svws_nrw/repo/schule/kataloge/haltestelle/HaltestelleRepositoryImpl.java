package de.svws_nrw.repo.schule.kataloge.haltestelle;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Daten zu dem Katalog der Haltestellen.
 */
public final class HaltestelleRepositoryImpl extends RepositoryImpl<DTOHaltestellen> implements HaltestelleRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public HaltestelleRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOHaltestellen.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public boolean existsById(final Long idHaltestelle) {
		return conn.existsBy(DTOHaltestellen.QUERY_BY_ID, DTOHaltestellen.class, idHaltestelle);
	}

}
