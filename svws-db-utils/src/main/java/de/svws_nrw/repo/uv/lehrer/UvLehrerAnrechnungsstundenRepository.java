package de.svws_nrw.repo.uv.lehrer;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.dto.current.uv.DTOUvLehrerAnrechnungsstunden;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Lehrer-Tabelle der SVWS-Datenbank
 */
public interface UvLehrerAnrechnungsstundenRepository extends Repository<DTOUvLehrerAnrechnungsstunden> {

	/**
	 * Bestimmt die Anrechnungsstunden für den Lehrer mit der übergebenen ID.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die Liste der Anrechnungsstunden
	 */
	List<DTOUvLehrerAnrechnungsstunden> getListByLehrerId(long idLehrer);

	/**
	 * Bestimmt die Anrechnungsstunden für die Lehrer mit den übergebenen IDs.
	 *
	 * @param idsLehrer   die IDs der Lehrer
	 *
	 * @return die Liste der Anrechnungsstunden
	 */
	List<DTOUvLehrerAnrechnungsstunden> getListByLehrerIds(Collection<Long> idsLehrer);

	/**
	 * Bestimmt die Zuordnung der Anrechnungsstunden zu den Lehrern mit den übergebenen IDs.
	 *
	 * @param idsLehrer   die IDs der Lehrer
	 *
	 * @return die Zuordnung der Lehrer-IDs zu deren Anrechnungsstunden
	 */
	Map<Long, List<DTOUvLehrerAnrechnungsstunden>> getMapByLehrerIds(Collection<Long> idsLehrer);

}
