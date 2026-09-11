package de.svws_nrw.repo.uv.kurse;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvKurs;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Kurse
 * (UV_Kurse) der SVWS-Datenbank.
 */
public interface UvKursRepository extends Repository<DTOUvKurs> {

	/**
	 * Ermittelt alle Kurse für den angegebenen Planungsabschnitt.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Kurse
	 */
	List<DTOUvKurs> getListByPlanungsabschnitt(long idPlanungsabschnitt);

}
