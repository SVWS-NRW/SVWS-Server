package de.svws_nrw.repo.uv.lehrer;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittLehrer;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittLehrerPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Planungsabschnitt-Lehrer-Zuordnungen
 * (UV_PlanungsabschnittLehrer) der SVWS-Datenbank.
 */
public interface UvPlanungsabschnittLehrerRepository extends RepositoryBase<DTOUvPlanungsabschnittLehrer, DTOUvPlanungsabschnittLehrerPK> {

	/**
	 * Ermittelt alle Planungsabschnitt-Lehrer-Zuordnungen für den angegebenen Planungsabschnitt.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Zuordnungen
	 */
	List<DTOUvPlanungsabschnittLehrer> getListByPlanungsabschnitt(long idPlanungsabschnitt);

}
