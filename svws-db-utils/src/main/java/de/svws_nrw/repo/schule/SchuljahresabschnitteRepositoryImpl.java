package de.svws_nrw.repo.schule;

import java.util.Optional;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Schuljahresabschnitte.
 */
public final class SchuljahresabschnitteRepositoryImpl extends RepositoryImpl<DTOSchuljahresabschnitte> implements SchuljahresabschnitteRepository {

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
		return conn.query("SELECT s FROM DTOSchuljahresabschnitte s WHERE s.Jahr = :jahr AND s.Abschnitt = :abschnitt",
				DTOSchuljahresabschnitte.class)
				.setParameter("jahr", schuljahr)
				.setParameter("abschnitt", abschnitt)
				.getResultList()
				.stream()
				.findFirst();
	}
}
