package de.svws_nrw.repo.lehrer;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtBefaehigung;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Lehrer-Lehrbefähigungs-Tabelle der SVWS-Datenbank
 */
public interface LehrerPersonaldatenLehramtLehrbefaehigungenRepository extends Repository<DTOLehrerPersonaldatenLehramtBefaehigung> {

	/**
	 * Bestimmt die Zuordnung der Lehrbefähigungen zu den Lehrämtern mit den übergebenen IDs.
	 *
	 * @param idsLehraemter   die IDs der Lehrämter
	 *
	 * @return die Zuordnung
	 */
	Map<Long, List<DTOLehrerPersonaldatenLehramtBefaehigung>> getMapByLehramt(Collection<Long> idsLehraemter);

}
