package de.svws_nrw.repo.schule.kataloge.fahrschuelerart;

import de.svws_nrw.db.dto.current.schild.katalog.DTOFahrschuelerart;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Fahrschuelerart-Tabelle der SVWS-Datenbank.
 */
public interface FahrschuelerartRepository extends Repository<DTOFahrschuelerart> {

	/**
	 * Prüft, ob eine Fahrschülerart mit der angegebenen ID in der Datenbank existiert.
	 *
	 * @param idFahrschuelerart die ID der Fahrschülerart
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsById(Long idFahrschuelerart);

}
