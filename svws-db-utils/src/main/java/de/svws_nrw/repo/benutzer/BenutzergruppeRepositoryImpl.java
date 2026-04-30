package de.svws_nrw.repo.benutzer;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppe;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Implementierung des {@link BenutzergruppeRepository}-Interfaces für den Datenbankzugriff
 * auf {@link DTOBenutzergruppe}-Entitäten.
 */
public final class BenutzergruppeRepositoryImpl extends RepositoryImpl<DTOBenutzergruppe> implements BenutzergruppeRepository {

	/**
	 * Erstellt eine neue Instanz des {@link BenutzergruppeRepositoryImpl}.
	 *
	 * @param conn der {@link DBEntityManager} für den Datenbankzugriff
	 */
	public BenutzergruppeRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOBenutzergruppe.class, e -> e.ID, (e, id) -> e.ID = id);
	}

}
