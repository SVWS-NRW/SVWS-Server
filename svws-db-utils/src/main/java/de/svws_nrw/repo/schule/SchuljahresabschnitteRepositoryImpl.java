package de.svws_nrw.repo.schule;

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
		super(conn, DTOSchuljahresabschnitte.class, (o, id) -> o.ID = id);
	}

}
