package de.svws_nrw.repo.uv.lerngruppen;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppeSchiene;
import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppeSchienePK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Lerngruppen-Schienen-Zuordnungen
 * (UV_LerngruppeSchiene) der SVWS-Datenbank.
 */
public interface UvLerngruppenSchieneRepository extends RepositoryBase<DTOUvLerngruppeSchiene, DTOUvLerngruppeSchienePK> {

	/**
	 * Ermittelt alle Lerngruppen-Schienen-Zuordnungen für den angegebenen Planungsabschnitt.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Zuordnungen
	 */
	List<DTOUvLerngruppeSchiene> getListByPlanungsabschnitt(long idPlanungsabschnitt);

}
