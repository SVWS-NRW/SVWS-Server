package de.svws_nrw.repo.schueler.lernabschnitt;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Lernabschnitts-bezogene Bemerkungen zu Schülern
 */
public final class SchuelerLernabschnittBemerkungRepositoryImpl extends RepositoryImpl<DTOSchuelerPSFachBemerkungen>
		implements SchuelerLernabschnittBemerkungRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerLernabschnittBemerkungRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerPSFachBemerkungen.class, o -> o.ID, (o, id) -> o.ID = id);
	}


	@Override
	public Map<Long, DTOSchuelerPSFachBemerkungen> findMapByLernabschnittID(final Collection<Long> idsAbschnitte) {
		if (idsAbschnitte.isEmpty()) {
			return new HashMap<>();
		}
		return conn.queryList(DTOSchuelerPSFachBemerkungen.QUERY_LIST_BY_ABSCHNITT_ID, DTOSchuelerPSFachBemerkungen.class, idsAbschnitte)
				.stream().collect(Collectors.toMap(b -> b.Abschnitt_ID, b -> b));
	}

}
