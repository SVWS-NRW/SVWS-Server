package de.svws_nrw.repo.schule.kataloge.foerderschwerpunkt;

import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Förderschwerpunkte-Tabelle der SVWS-Datenbank
 */
public interface FoerderschwerpunktRepository extends Repository<DTOFoerderschwerpunkt> {

	/**
	 * @param idFoerderschwerpunkt {@link Long}
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsById(Long idFoerderschwerpunkt);

}
