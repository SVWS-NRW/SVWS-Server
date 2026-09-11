package de.svws_nrw.repo.uv.schienen;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvSchiene;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Schienen
 * (UV_Schienen) der SVWS-Datenbank.
 */
public interface UvSchieneRepository extends Repository<DTOUvSchiene> {

	/**
	 * Ermittelt alle Schienen für den angegebenen Planungsabschnitt.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Schienen
	 */
	List<DTOUvSchiene> getListByPlanungsabschnitt(long idPlanungsabschnitt);

}
