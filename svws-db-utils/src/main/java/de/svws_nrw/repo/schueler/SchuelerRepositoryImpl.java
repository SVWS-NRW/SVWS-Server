package de.svws_nrw.repo.schueler;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf Schülerdaten.
 */
public final class SchuelerRepositoryImpl extends RepositoryImpl<DTOSchueler> implements SchuelerRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuelerRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchueler.class, o -> o.ID, (o, id) -> o.ID = id);
	}


	@Override
	public List<DTOSchueler> getListAktiveBySchuljahresabschnitt(final long idSchuljahresabschnitt) {
		return conn.queryList("SELECT e FROM DTOSchueler e WHERE e.Schuljahresabschnitts_ID = ?1 AND e.idStatus = ?2 AND e.Geloescht = ?3",
				DTOSchueler.class, idSchuljahresabschnitt, SchuelerStatus.AKTIV.historie().getLast().id, false);
	}

	@Override
	public Map<Long, DTOSchueler> getMapAktiveBySchuljahresabschnitt(final long idSchuljahresabschnitt) {
		return getListAktiveBySchuljahresabschnitt(idSchuljahresabschnitt).stream().collect(Collectors.toMap(s -> s.ID, s -> s));
	}

	@Override
	public List<DTOSchueler> getListByStatusAndSchuljahresabschnitt(final long idSchuljahresabschnitt, final Collection<Long> status) {
		if (status.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList("SELECT e FROM DTOSchueler e WHERE e.Schuljahresabschnitts_ID = ?1 AND e.idStatus IN ?2 AND e.Geloescht = ?3",
				DTOSchueler.class, idSchuljahresabschnitt, status, false);
	}

	@Override
	public Map<Long, DTOSchueler> getMapByStatusAndSchuljahresabschnitt(final long idSchuljahresabschnitt, final Collection<Long> status) {
		return getListByStatusAndSchuljahresabschnitt(idSchuljahresabschnitt, status).stream().collect(Collectors.toMap(s -> s.ID, s -> s));
	}

	@Override
	public List<DTOSchueler> getListByStatusAndSchuljahresabschnitte(final Collection<Long> idsSchuljahresabschnitte, final Collection<Long> status) {
		if (status.isEmpty() || idsSchuljahresabschnitte.isEmpty()) {
			return Collections.emptyList();
		}
		return conn.queryList("SELECT e FROM DTOSchueler e WHERE e.Schuljahresabschnitts_ID IN ?1 AND e.idStatus IN ?2 AND e.Geloescht = ?3",
				DTOSchueler.class, idsSchuljahresabschnitte, status, false);
	}

	@Override
	public Map<Long, DTOSchueler> getMapByStatusAndSchuljahresabschnitte(final Collection<Long> idsSchuljahresabschnitte, final Collection<Long> status) {
		return getListByStatusAndSchuljahresabschnitte(idsSchuljahresabschnitte, status).stream().collect(Collectors.toMap(s -> s.ID, s -> s));
	}

}
