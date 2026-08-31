package de.svws_nrw.repo.schule.kataloge.jahrgang;

import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Jahrgangs-Tabelle der SVWS-Datenbank
 */
public interface JahrgangRepository extends Repository<DTOJahrgang> {

	/**
	 * @param idJahrgang {@link Long}
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsById(Long idJahrgang);

}
