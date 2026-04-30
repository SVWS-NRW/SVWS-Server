package de.svws_nrw.repo.benutzer;

import de.svws_nrw.db.Benutzer;

/**
 * Repository für den aktuell angemeldeten Benutzer.
 */
public interface BenutzerRepository {

	/**
	 * Gibt die ID des aktuell angemeldeten Benutzers zurück.
	 *
	 * @return die Benutzer-ID
	 */
	long getAktuellerBenutzerId();

	/**
	 * Gibt den aktuell angemeldeten Benutzer zurück.
	 *
	 * @return der aktuelle {@link Benutzer}
	 */
	Benutzer getAktuellerBenutzer();
}
