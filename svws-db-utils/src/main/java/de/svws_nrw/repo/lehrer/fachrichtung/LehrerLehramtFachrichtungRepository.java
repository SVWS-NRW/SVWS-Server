package de.svws_nrw.repo.lehrer.fachrichtung;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtFachrichtung;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Lehrer-Fachrichtungs-Tabelle der SVWS-Datenbank
 */
public interface LehrerLehramtFachrichtungRepository extends Repository<DTOLehrerPersonaldatenLehramtFachrichtung> {

	/**
	 * Bestimmt die Zuordnung der Fachrichtungen zu den Lehrämtern mit den übergebenen IDs.
	 *
	 * @param idsLehraemter   die IDs der Lehrämter
	 *
	 * @return die Zuordnung
	 */
	Map<Long, List<DTOLehrerPersonaldatenLehramtFachrichtung>> getLehrerFachrichtungenByIdLehramt(Collection<Long> idsLehraemter);

	/**
	 * Gibt die Liste der Fachrichtungen für die LehramtID zurück.
	 * @param idLehramt idLehramt
	 * @return die Liste der Fachrichtungen für die LehramtID.
	 */
	List<DTOLehrerPersonaldatenLehramtFachrichtung> getByLehramtId(long idLehramt);

}
