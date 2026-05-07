package de.svws_nrw.repo.benutzer;

import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzerAllgemein;
import de.svws_nrw.repo.Repository;

/**
 * Repository für allgemeine Benutzer Informationen.
 */
public interface BenutzerRepository extends Repository<DTOBenutzerAllgemein> {

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
