package de.svws_nrw.repo.schule.kataloge.schuelerschwerpunkt;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuelerSchwerpunkt;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Daten zu den Schwerpunkten der Schüler.
 */
public final class SchuelerschwerpunktRepositoryImpl extends RepositoryImpl<DTOSchuelerSchwerpunkt> implements SchuelerschwerpunktRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerschwerpunktRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerSchwerpunkt.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public boolean existsById(final Long idSchuelerschwerpunkt) {
		return conn.existsBy(DTOSchuelerSchwerpunkt.QUERY_BY_ID, DTOSchuelerSchwerpunkt.class, idSchuelerschwerpunkt);
	}

}
