package de.svws_nrw.repo.lehrer.lehramt;

import java.util.List;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramt;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Lehrer-Lehramts-Tabelle der SVWS-Datenbank
 */
public interface LehrerLehramtRepository extends Repository<DTOLehrerPersonaldatenLehramt> {


	/**
	 * Gibt alle Lehrämter der Lehrer mit den angegebenen IDs zurück.
	 *
	 * @param idsLehrer   die IDs der Lehrer
	 * @return Liste der zugehörigen {@link DTOLehrerPersonaldatenLehramt}-Objekte, leer wenn keine vorhanden
	 */
	List<DTOLehrerPersonaldatenLehramt> findByIdsLehrer(List<Long> idsLehrer);

	/**
	 * @param idLehramt {@link Long}
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsById(Long idLehramt);

}
