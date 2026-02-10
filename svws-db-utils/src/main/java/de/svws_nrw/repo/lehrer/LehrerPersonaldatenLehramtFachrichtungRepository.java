package de.svws_nrw.repo.lehrer;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtFachrichtung;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Lehrer-Fachrichtungs-Tabelle der SVWS-Datenbank
 */
public interface LehrerPersonaldatenLehramtFachrichtungRepository extends Repository<DTOLehrerPersonaldatenLehramtFachrichtung> {

	/**
	 * Bestimmt die Zuordnung der Fachrichtungen zu den Lehrämtern mit den übergebenen IDs.
	 *
	 * @param idsLehraemter   die IDs der Lehrämter
	 *
	 * @return die Zuordnung
	 */
	Map<Long, List<DTOLehrerPersonaldatenLehramtFachrichtung>> getMapByLehramt(Collection<Long> idsLehraemter);

}
