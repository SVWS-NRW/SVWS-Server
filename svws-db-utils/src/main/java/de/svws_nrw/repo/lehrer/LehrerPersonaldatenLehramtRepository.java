package de.svws_nrw.repo.lehrer;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramt;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Lehrer-Lehramts-Tabelle der SVWS-Datenbank
 */
public interface LehrerPersonaldatenLehramtRepository extends Repository<DTOLehrerPersonaldatenLehramt> {

	/**
	 * Bestimmt die Zuordnung der Lehrämter zu den Lehrern mit den übergebenen IDs.
	 *
	 * @param idsLehrer   die IDs der Lehrer
	 *
	 * @return die Zuordnung
	 */
	Map<Long, List<DTOLehrerPersonaldatenLehramt>> getMapByLehrerID(Collection<Long> idsLehrer);

}
