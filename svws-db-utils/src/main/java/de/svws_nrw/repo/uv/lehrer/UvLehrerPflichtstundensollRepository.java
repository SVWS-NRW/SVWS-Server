package de.svws_nrw.repo.uv.lehrer;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.dto.current.uv.DTOUvLehrerPflichtstundensoll;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Lehrer-Tabelle der SVWS-Datenbank
 */
public interface UvLehrerPflichtstundensollRepository extends Repository<DTOUvLehrerPflichtstundensoll> {

	/**
	 * Bestimmt die Pflichtstundensoll-Einträge für den Lehrer mit der übergebenen ID.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die Liste der Pflichtstundensoll-Einträge
	 */
	List<DTOUvLehrerPflichtstundensoll> getListByLehrerId(long idLehrer);

	/**
	 * Bestimmt die Pflichtstundensoll-Einträge für die Lehrer mit den übergebenen IDs.
	 *
	 * @param idsLehrer   die IDs der Lehrer
	 *
	 * @return die Liste der Pflichtstundensoll-Einträge
	 */
	List<DTOUvLehrerPflichtstundensoll> getListByLehrerIds(Collection<Long> idsLehrer);

	/**
	 * Bestimmt die Zuordnung der Pflichtstundensoll-Einträge zu den Lehrern mit den übergebenen IDs.
	 *
	 * @param idsLehrer   die IDs der Lehrer
	 *
	 * @return die Zuordnung der Lehrer-IDs zu deren Pflichtstundensoll-Einträge
	 */
	Map<Long, List<DTOUvLehrerPflichtstundensoll>> getMapByLehrerIds(Collection<Long> idsLehrer);

}
