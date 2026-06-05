package de.svws_nrw.repo.schule.schulleitung;

import java.util.List;

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

}
