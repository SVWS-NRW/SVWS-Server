package de.svws_nrw.repo.uv.faecher;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvFach;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Fächer
 * (UV_Faecher) der SVWS-Datenbank.
 */
public interface UvFachRepository extends Repository<DTOUvFach> {

	/**
	 * Bestimmt die UV-Fächer für das Fach mit der übergebenen ID.
	 *
	 * @param idFach   die ID des Faches
	 *
	 * @return die Liste der UV-Fächer
	 */
	List<DTOUvFach> getListByFachId(long idFach);

}
