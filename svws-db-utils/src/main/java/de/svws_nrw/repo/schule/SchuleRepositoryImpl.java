package de.svws_nrw.repo.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Schuldaten.
 */
public final class SchuleRepositoryImpl extends RepositoryImpl<DTOEigeneSchule> implements SchuleRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchuleRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOEigeneSchule.class, (o, id) -> o.ID = id);
	}


	@Override
	public long getSchuljahresabschnitt() {
		return super.getFirst().Schuljahresabschnitts_ID;
	}

}
