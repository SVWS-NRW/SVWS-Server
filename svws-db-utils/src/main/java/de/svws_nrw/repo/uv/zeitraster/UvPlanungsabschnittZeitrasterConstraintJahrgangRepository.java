package de.svws_nrw.repo.uv.zeitraster;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang;
import de.svws_nrw.db.dto.current.uv.DTOUv_PlanungsabschnittZeitraster_Constraint_JahrgangPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der Zeitraster-Jahrgang-Constraints
 * (UV_PlanungsabschnitteZeitraster_Constraint_Jahrgaenge) der SVWS-Datenbank.
 */
public interface UvPlanungsabschnittZeitrasterConstraintJahrgangRepository
		extends RepositoryBase<DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang, DTOUv_PlanungsabschnittZeitraster_Constraint_JahrgangPK> {

	/**
	 * Ermittelt alle Jahrgang-Constraints für den angegebenen Planungsabschnitt und das Zeitraster.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 * @param idZeitraster          die ID des Zeitrasters
	 *
	 * @return die Liste der Constraints
	 */
	List<DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang> getListByPlanungsabschnittAndZeitraster(long idPlanungsabschnitt, long idZeitraster);

	/**
	 * Ermittelt alle Jahrgang-Constraints für den angegebenen Planungsabschnitt.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Constraints
	 */
	List<DTOUv_PlanungsabschnittZeitraster_Constraint_Jahrgang> getListByPlanungsabschnitt(long idPlanungsabschnitt);

}
