package de.svws_nrw.repo.schule.kataloge.foerderschwerpunkt;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Daten zu den Förderschwerpunkten.
 */
public final class FoerderschwerpunktRepositoryImpl extends RepositoryImpl<DTOFoerderschwerpunkt> implements FoerderschwerpunktRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public FoerderschwerpunktRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOFoerderschwerpunkt.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
