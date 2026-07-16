package de.svws_nrw.repo.benutzer;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.views.benutzer.DTOViewBenutzerdetails;
import de.svws_nrw.repo.RepositoryImpl;

public final class ViewBenutzerDetailsRepositoryImpl extends RepositoryImpl<DTOViewBenutzerdetails> implements ViewBenutzerDetailsRepository {

	/**
	 * Erstellt eine neue Instanz des Repositories.
	 *
	 * @param conn der {@link DBEntityManager} für den Datenbankzugriff
	 */
	public ViewBenutzerDetailsRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOViewBenutzerdetails.class, e -> e.ID, (e, id) -> e.ID = id);
	}

}
