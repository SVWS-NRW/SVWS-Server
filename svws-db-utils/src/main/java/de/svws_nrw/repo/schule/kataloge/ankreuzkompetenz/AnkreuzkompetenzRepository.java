package de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz;

import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzfloskeln;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf den Katalog der Ankreuzkompetenzen der SVWS-Datenbank
 */
public interface AnkreuzkompetenzRepository extends Repository<DTOAnkreuzfloskeln> {

	/**
	 * @param idAnkreuzkompetenz {@link Long}
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsById(Long idAnkreuzkompetenz);

}
