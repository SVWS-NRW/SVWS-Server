package de.svws_nrw.repo.klassen;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf Klassendaten.
 */
public final class KlassenRepositoryImpl extends RepositoryImpl<DTOKlassen> implements KlassenRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public KlassenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOKlassen.class, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOKlassen> getListBySchuljahresabschnitt(final long idSchuljahresabschnitt) {
		return conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, idSchuljahresabschnitt);
	}

}
