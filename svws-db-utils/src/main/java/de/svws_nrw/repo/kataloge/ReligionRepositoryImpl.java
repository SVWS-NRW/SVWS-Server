package de.svws_nrw.repo.kataloge;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKonfession;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Daten zu dem Katalog der Konfessionen.
 */
public final class ReligionRepositoryImpl extends RepositoryImpl<DTOKonfession> implements ReligionRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public ReligionRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOKonfession.class, (o, id) -> o.ID = id);
	}

}
