package de.svws_nrw.repo.schule.kataloge.floskelgruppe;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.katalog.DTOFloskelgruppen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf den Katalog der Floskelgruppen.
 */
public final class FloskelgruppeRepositoryImpl extends RepositoryImpl<DTOFloskelgruppen> implements FloskelgruppeRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public FloskelgruppeRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOFloskelgruppen.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
