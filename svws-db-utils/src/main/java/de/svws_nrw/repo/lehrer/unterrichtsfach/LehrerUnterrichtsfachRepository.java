package de.svws_nrw.repo.lehrer.unterrichtsfach;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerUnterrichtsfach;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die LehrerUnterrichtsfaecher-Tabelle der SVWS-Datenbank
 */
public interface LehrerUnterrichtsfachRepository extends Repository<DTOLehrerUnterrichtsfach> {

	/**
	 * Bestimmt die Unterrichtsfächer für den Lehrer mit der übergebenen ID.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die Liste der Unterrichtsfächer
	 */
	List<DTOLehrerUnterrichtsfach> getListByLehrerId(long idLehrer);

	/**
	 * Bestimmt die Unterrichtsfächer für die Lehrer mit den übergebenen IDs.
	 *
	 * @param idsLehrer   die IDs der Lehrer
	 *
	 * @return die Liste der Unterrichtsfächer
	 */
	List<DTOLehrerUnterrichtsfach> getListByLehrerIds(Collection<Long> idsLehrer);

	/**
	 * Bestimmt die Zuordnung der Unterrichtsfächer zu den Lehrern mit den übergebenen IDs.
	 *
	 * @param idsLehrer   die IDs der Lehrer
	 *
	 * @return die Zuordnung der Lehrer-IDs zu deren Unterrichtsfächern
	 */
	Map<Long, List<DTOLehrerUnterrichtsfach>> getMapByLehrerIds(Collection<Long> idsLehrer);

}
