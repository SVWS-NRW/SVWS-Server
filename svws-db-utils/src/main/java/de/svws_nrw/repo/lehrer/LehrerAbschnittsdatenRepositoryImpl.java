package de.svws_nrw.repo.lehrer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Abschnittsdaten von Lehrern.
 */
public final class LehrerAbschnittsdatenRepositoryImpl extends RepositoryImpl<DTOLehrerAbschnittsdaten> implements LehrerAbschnittsdatenRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public LehrerAbschnittsdatenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOLehrerAbschnittsdaten.class, (o, id) -> o.ID = id);
	}

	@Override
	public List<DTOLehrerAbschnittsdaten> getListByLehrerIdsAndSchuljahresabschnitt(final Collection<Long> idsLehrer, final long idSchuljahresabschnitt) {
		if ((idsLehrer == null) || (idsLehrer.isEmpty()) || (conn.getUser().schuleGetAbschnittById(idSchuljahresabschnitt) == null))
			return Collections.emptyList();
		return conn.queryList("SELECT e FROM DTOLehrerAbschnittsdaten e WHERE e.Lehrer_ID IN ?1 AND e.Schuljahresabschnitts_ID = ?2",
				DTOLehrerAbschnittsdaten.class, idsLehrer, idSchuljahresabschnitt);
	}

}
