package de.svws_nrw.repo.schule.kataloge.schuelerschwerpunkt;

import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuelerSchwerpunkt;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Schwerpunkte-Tabelle (K_Schwerpunkt) der SVWS-Datenbank
 */
public interface SchuelerschwerpunktRepository extends Repository<DTOSchuelerSchwerpunkt> {

	/**
	 * @param idSchuelerschwerpunkt {@link Long}
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsById(Long idSchuelerschwerpunkt);

}
