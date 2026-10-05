package de.svws_nrw.repo.schulwechsel.abgang;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchulwechselAbgang;
import de.svws_nrw.repo.RepositoryImpl;

public final class SchulwechselAbgangRepositoryImpl extends RepositoryImpl<DTOSchulwechselAbgang> implements SchulwechselAbgangRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchulwechselAbgangRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchulwechselAbgang.class, s -> s.id, (s, id) -> s.id = id);
	}

}
