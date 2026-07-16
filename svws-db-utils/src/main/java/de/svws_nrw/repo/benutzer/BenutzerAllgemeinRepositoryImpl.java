package de.svws_nrw.repo.benutzer;

import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzerAllgemein;
import de.svws_nrw.repo.RepositoryImpl;

public final class BenutzerAllgemeinRepositoryImpl extends RepositoryImpl<DTOBenutzerAllgemein>
		implements BenutzerAllgemeinRepository {

	/**
	 * Erstellt eine neue Instanz des Repositories.
	 *
	 * @param conn der {@link DBEntityManager} für den Datenbankzugriff
	 */
	public BenutzerAllgemeinRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOBenutzerAllgemein.class, b -> b.ID, (b, id) -> b.ID = id);
	}

	@Override
	public long getAktuellerBenutzerId() {
		return conn.getUser().getId();
	}

	@Override
	public Benutzer getAktuellerBenutzer() {
		return conn.getUser();
	}

}
