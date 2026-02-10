package de.svws_nrw.repo.lehrer;

import java.util.List;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Lehrer-Tabelle der SVWS-Datenbank
 */
public interface LehrerRepository extends Repository<DTOLehrer> {

	/**
	 * Gibt eine Liste aller Statistik-relevanten Lehrer zurück.
	 *
	 * @return die Liste mit den Statistik-relevanten Lehrer-DB-DTOs
	 */
	List<DTOLehrer> getAllStatistikRelevant();

}
