package de.svws_nrw.repo.schule.kataloge.floskel;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.katalog.DTOFloskelnJahrgaenge;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Jahrgangszuordnung von Floskeln.
 */
public final class FloskelJahrgangRepositoryImpl extends RepositoryImpl<DTOFloskelnJahrgaenge> implements FloskelJahrgangRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public FloskelJahrgangRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOFloskelnJahrgaenge.class, o -> o.ID, (o, id) -> o.ID = id);
	}

}
