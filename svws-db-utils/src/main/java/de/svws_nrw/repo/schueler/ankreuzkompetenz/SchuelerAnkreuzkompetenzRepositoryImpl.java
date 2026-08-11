package de.svws_nrw.repo.schueler.ankreuzkompetenz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOSchuelerAnkreuzfloskeln;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Schüler-Ankreuzkompetenzen
 */
public final class SchuelerAnkreuzkompetenzRepositoryImpl extends RepositoryImpl<DTOSchuelerAnkreuzfloskeln>
		implements SchuelerAnkreuzkompetenzRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerAnkreuzkompetenzRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerAnkreuzfloskeln.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOSchuelerAnkreuzfloskeln> findListByLernabschnitt(final Collection<Long> idsLernabschnitte) {
		if (idsLernabschnitte.isEmpty()) {
			return new ArrayList<>();
		}
		return conn.queryList(DTOSchuelerAnkreuzfloskeln.QUERY_LIST_BY_ABSCHNITT_ID, DTOSchuelerAnkreuzfloskeln.class, idsLernabschnitte);
	}

}
