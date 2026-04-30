package de.svws_nrw.repo.benutzer;

import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;

public final class BenutzerRepositoryImpl implements BenutzerRepository {

	private final DBEntityManager conn;

	/**
	 * Erstellt ein neues BenutzerRepository.
	 *
	 * @param conn der Datenbankzugriff
	 */
	public BenutzerRepositoryImpl(final DBEntityManager conn) {
		this.conn = conn;
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
