package de.svws_nrw.repo.schueler.teilleistung;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerTeilleistung;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Schüler-Teilleistungen
 */
public final class SchuelerTeilleistungRepositoryImpl extends RepositoryImpl<DTOSchuelerTeilleistung>
		implements SchuelerTeilleistungRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerTeilleistungRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerTeilleistung.class, o -> o.ID, (o, id) -> o.ID = id);
	}


	@Override
	public List<DTOSchuelerTeilleistung> findListByLeistungsdaten(final Collection<Long> idsLeistungen) {
		if (idsLeistungen.isEmpty()) {
			return new ArrayList<>();
		}
		return conn.queryList(DTOSchuelerTeilleistung.QUERY_LIST_BY_LEISTUNG_ID, DTOSchuelerTeilleistung.class, idsLeistungen);
	}

}
