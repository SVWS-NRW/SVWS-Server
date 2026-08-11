package de.svws_nrw.repo.lehrer.personalabschnittsdaten;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.repo.RepositoryImpl;

public final class LehrerPersonalabschnittsdatenRepositoryImpl extends RepositoryImpl<DTOLehrerAbschnittsdaten> implements LehrerPersonalabschnittsdatenRepository {

	/**
	 * @param conn {@link DBEntityManager}
	 */
	public LehrerPersonalabschnittsdatenRepositoryImpl(final DBEntityManager conn) {
		super(conn,
				DTOLehrerAbschnittsdaten.class,
				l -> l.ID,
				(l, id) -> l.ID = id
		);
	}

	@Override
	public List<DTOLehrerAbschnittsdaten> findByIdLehrer(final long idLehrer) {
		return conn.queryList(DTOLehrerAbschnittsdaten.QUERY_BY_LEHRER_ID, DTOLehrerAbschnittsdaten.class, idLehrer);
	}

	@Override
	public List<DTOLehrerAbschnittsdaten> getListByLehrerIdsAndSchuljahresabschnitt(final Collection<Long> idsLehrer, final long idSchuljahresabschnitt) {
		if ((idsLehrer == null) || (idsLehrer.isEmpty()) || (conn.getUser().schuleGetAbschnittById(idSchuljahresabschnitt) == null)) {
			return Collections.emptyList();
		}
		return conn.queryList("SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.Lehrer_ID IN ?1 AND e.Schuljahresabschnitts_ID = ?2",
				DTOLehrerAbschnittsdaten.class, idsLehrer, idSchuljahresabschnitt);
	}
}
