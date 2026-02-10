package de.svws_nrw.repo.kataloge;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Daten zu den Förderschwerpunkten.
 */
public final class FoerderschwerpunkteRepositoryImpl extends RepositoryImpl<DTOFoerderschwerpunkt> implements FoerderschwerpunkteRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public FoerderschwerpunkteRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOFoerderschwerpunkt.class, (o, id) -> o.ID = id);
	}

}
