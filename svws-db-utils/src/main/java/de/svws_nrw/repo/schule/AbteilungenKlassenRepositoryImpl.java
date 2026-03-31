package de.svws_nrw.repo.schule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungsKlassen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Tabelle für die Klassenzurdnungen von Abteilungen.
 */
public final class AbteilungenKlassenRepositoryImpl extends RepositoryImpl<DTOAbteilungsKlassen> implements AbteilungenKlassenRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public AbteilungenKlassenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOAbteilungsKlassen.class, o -> o.ID, (o, id) -> o.ID = id);
	}


	@Override
	public List<DTOAbteilungsKlassen> findListByAbteilungen(final Collection<Long> idsAbteilungen) {
		if ((idsAbteilungen == null) || idsAbteilungen.isEmpty()) {
			return new ArrayList<>();
		}
		return conn.queryList(DTOAbteilungsKlassen.QUERY_LIST_BY_ABTEILUNG_ID, DTOAbteilungsKlassen.class, idsAbteilungen);
	}

}
