package de.svws_nrw.repo.schule.schulleitung;

import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOSchulleitung;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Schulleitungstabelle der SVWS-Datenbank
 */
public interface SchulleitungRepository extends Repository<DTOSchulleitung> {

	/**
	 * Gibt alle Schulleitungseinträge für einen bestimmten Lehrer zurück.
	 *
	 * @param idLehrer die ID des Lehrers
	 * @return Liste der Schulleitungseinträge
	 */
	List<DTOSchulleitung> getAllByIdLehrer(long idLehrer);

	/**
	 * Gibt alle Schulleitungseinträge für eine bestimmte Leitungsfunktion zurück.
	 *
	 * @param idLeitungsfunktion die ID der Leitungsfunktion
	 * @return Liste der Schulleitungseinträge
	 */
	List<DTOSchulleitung> getAllByIdLeitungsfunktion(long idLeitungsfunktion);

	/**
	 * Gibt die letzten Schulleitunseinträge für den angebenen Zeitraum zugeordnet zu der ID der Leitungsfunktion zurück.
	 *
	 * @param von   das Anfangsdatum - bei Null wird vom 1.1.1900 ausgegangen
	 * @param bis   das Enddatum - bei Null wird vom 31.12.9999 ausgegangen
	 *
	 * @return die Map mit der letzten Zuordnung der Leitungsfunktion in dem angegebenen Zeitraum
	 */
	Map<Long, DTOSchulleitung> getMapByDates(DateManager von, DateManager bis);

}
