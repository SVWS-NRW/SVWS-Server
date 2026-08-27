package de.svws_nrw.repo.schule.kataloge.haltestelle;

import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Haltestellen-Tabelle der SVWS-Datenbank.
 */
public interface HaltestelleRepository extends Repository<DTOHaltestellen> {

	/**
	 * Prüft, ob eine Haltestelle mit der angegebenen ID in der Datenbank existiert.
	 *
	 * @param idHaltestelle die ID der Haltestelle
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsById(Long idHaltestelle);

}
