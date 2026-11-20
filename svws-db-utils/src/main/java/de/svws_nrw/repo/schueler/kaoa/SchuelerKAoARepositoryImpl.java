package de.svws_nrw.repo.schueler.kaoa;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerKAoADaten;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Die Implementierung des Repositories für die KAoA-Daten der Schüler.
 */
public final class SchuelerKAoARepositoryImpl extends RepositoryImpl<DTOSchuelerKAoADaten> implements SchuelerKAoARepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerKAoARepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuelerKAoADaten.class, e -> e.id, (e, id) -> e.id = id);
	}

	@Override
	public List<DTOSchuelerKAoADaten> findListByLernabschnitte(final Collection<Long> idsLernabschnitte) {
		if (idsLernabschnitte.isEmpty()) {
			return List.of();
		}
		return conn.queryList(DTOSchuelerKAoADaten.QUERY_LIST_BY_IDLERNABSCHNITT, DTOSchuelerKAoADaten.class, idsLernabschnitte);
	}

}
