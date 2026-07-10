package de.svws_nrw.repo.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.repo.RepositoryException;
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
		super(conn, DTOEigeneSchule.class, o -> o.ID, (o, id) -> o.ID = id);
	}


	@Override
	public long getIdSchuljahresabschnitt() {
		return super.getFirst().Schuljahresabschnitts_ID;
	}

	@Override
	public int getSchulnummer() {
		final var schule = super.getFirst();
		if (schule.SchulNr == null) {
			throw new RepositoryException("Die aktuelle Schule hat keine SchulNr hinterlegt");
		}
		return schule.SchulNr;
	}

}
