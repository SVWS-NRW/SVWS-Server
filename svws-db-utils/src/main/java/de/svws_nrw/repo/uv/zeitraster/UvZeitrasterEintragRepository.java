package de.svws_nrw.repo.uv.zeitraster;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvZeitrasterEintrag;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Zeitraster-Einträge
 * (UV_Zeitraster_Eintraege) der SVWS-Datenbank.
 */
public interface UvZeitrasterEintragRepository extends Repository<DTOUvZeitrasterEintrag> {

	/**
	 * Bestimmt die Zeitraster-Einträge für das Zeitraster mit der übergebenen ID.
	 *
	 * @param idZeitraster   die ID des Zeitrasters
	 *
	 * @return die Liste der Zeitraster-Einträge
	 */
	List<DTOUvZeitrasterEintrag> getListByZeitrasterId(long idZeitraster);

	/**
	 * Bestimmt die Zeitraster-Einträge für die Zeitraster mit den übergebenen IDs.
	 *
	 * @param idsZeitraster   die IDs der Zeitraster
	 *
	 * @return die Liste der Zeitraster-Einträge
	 */
	List<DTOUvZeitrasterEintrag> getListByZeitrasterIds(Collection<Long> idsZeitraster);

}
