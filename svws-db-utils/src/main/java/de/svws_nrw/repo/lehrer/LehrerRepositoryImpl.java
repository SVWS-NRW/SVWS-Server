package de.svws_nrw.repo.lehrer;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf Lehrerdaten.
 */
public final class LehrerRepositoryImpl extends RepositoryImpl<DTOLehrer> implements LehrerRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public LehrerRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLehrer.class, (o, id) -> o.ID = id);
	}


	/**
	 * Gibt eine Liste aller Statistik-relevanten Lehrer zurück.
	 *
	 * @return die Liste mit den Statistik-relevanten Lehrer-DB-DTOs
	 */
	@Override
	public List<DTOLehrer> getAllStatistikRelevant() {
		return conn.queryList(DTOLehrer.QUERY_BY_STATISTIKRELEVANT, DTOLehrer.class, true);
	}

}
