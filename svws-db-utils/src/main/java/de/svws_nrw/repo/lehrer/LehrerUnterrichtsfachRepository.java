package de.svws_nrw.repo.lehrer;

import java.util.List;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerUnterrichtsfach;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die LehrerUnterrichtsfaecher-Tabelle der SVWS-Datenbank
 */
public interface LehrerUnterrichtsfachRepository extends Repository<DTOLehrerUnterrichtsfach> {

	/**
	 * Bestimmt die Unterrichtsfächer für den Lehrer mit der übergebenen ID.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die Liste der Unterrichtsfächer
	 */
	List<DTOLehrerUnterrichtsfach> getListByLehrerId(long idLehrer);

}
