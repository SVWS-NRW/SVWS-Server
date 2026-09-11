package de.svws_nrw.repo.uv.planungsabschnitte;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnitt;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Planungsabschnitte
 * (UV_Planungsabschnitte) der SVWS-Datenbank.
 */
public interface UvPlanungsabschnittRepository extends Repository<DTOUvPlanungsabschnitt> {

	/**
	 * Ermittelt die UV-Planungsabschnitte eines Schuljahres. Ist das Schuljahr {@code null},
	 * so werden alle UV-Planungsabschnitte zurückgegeben.
	 *
	 * @param schuljahr   das Schuljahr oder {@code null} für alle
	 *
	 * @return die Liste der UV-Planungsabschnitte
	 */
	List<DTOUvPlanungsabschnitt> getListBySchuljahr(Integer schuljahr);

}
