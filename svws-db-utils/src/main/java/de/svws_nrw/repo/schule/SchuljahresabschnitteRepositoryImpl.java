package de.svws_nrw.repo.schule;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Schuljahresabschnitte.
 */
public final class SchuljahresabschnitteRepositoryImpl extends RepositoryImpl<DTOSchuljahresabschnitte> implements SchuljahresabschnitteRepository {

	/** Die Datenbankabfrage für den Schuljahresabschnitt zu einem Schuljahr und einem Abschnitt */
	static final String QUERY_BY_SCHULJAHR_AND_ABSCHNITT =
			"SELECT s FROM DTOSchuljahresabschnitte s WHERE s.Jahr = :jahr AND s.Abschnitt = :abschnitt";

	/** Die Datenbankabfrage für die Schuljahresabschnitte zu einer Liste von Schuljahren */
	static final String QUERY_LIST_BY_SCHULJAHRE = "SELECT s FROM DTOSchuljahresabschnitte s WHERE s.Jahr IN ?1";

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuljahresabschnitteRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchuljahresabschnitte.class, o -> o.ID, (o, id) -> o.ID = id);
	}

	@Override
	public boolean existsById(final Long idSchuljahresabschnitt) {
		return conn.existsBy(DTOSchuljahresabschnitte.QUERY_BY_ID, DTOSchuljahresabschnitte.class, idSchuljahresabschnitt);
	}

	@Override
	public Optional<DTOSchuljahresabschnitte> findBySchuljahrAndAbschnitt(final int schuljahr, final int abschnitt) {
		return conn.query(QUERY_BY_SCHULJAHR_AND_ABSCHNITT, DTOSchuljahresabschnitte.class)
				.setParameter("jahr", schuljahr)
				.setParameter("abschnitt", abschnitt)
				.getResultList()
				.stream()
				.findFirst();
	}

	@Override
	public List<DTOSchuljahresabschnitte> findListBySchuljahre(final Collection<Integer> schuljahre) {
		if ((schuljahre == null) || schuljahre.isEmpty()) {
			return List.of();
		}
		return conn.queryList(QUERY_LIST_BY_SCHULJAHRE, DTOSchuljahresabschnitte.class, schuljahre);
	}

}
