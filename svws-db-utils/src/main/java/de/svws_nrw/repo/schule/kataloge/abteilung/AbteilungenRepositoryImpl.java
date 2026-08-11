package de.svws_nrw.repo.schule.kataloge.abteilung;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Tabelle für die Abteilungen.
 */
public final class AbteilungenRepositoryImpl extends RepositoryImpl<DTOAbteilungen> implements AbteilungenRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public AbteilungenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOAbteilungen.class, o -> o.ID, (o, id) -> o.ID = id);
	}


	@Override
	public List<DTOAbteilungen> getListBySchuljahresabschnitt(final long idSchuljahresabschnitt) {
		return conn.queryList(DTOAbteilungen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOAbteilungen.class, idSchuljahresabschnitt);
	}

}
