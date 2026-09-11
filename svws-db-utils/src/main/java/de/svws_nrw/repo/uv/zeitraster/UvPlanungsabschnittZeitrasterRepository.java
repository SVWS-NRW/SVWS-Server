package de.svws_nrw.repo.uv.zeitraster;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittZeitraster;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittZeitrasterPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle UV_Planungsabschnitte_Zeitraster
 * der SVWS-Datenbank.
 */
public interface UvPlanungsabschnittZeitrasterRepository extends RepositoryBase<DTOUvPlanungsabschnittZeitraster, DTOUvPlanungsabschnittZeitrasterPK> {

	/**
	 * Ermittelt alle Zeitraster-Zuordnungen für den angegebenen Planungsabschnitt.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Zeitraster-Zuordnungen
	 */
	List<DTOUvPlanungsabschnittZeitraster> getListByPlanungsabschnitt(long idPlanungsabschnitt);

}
