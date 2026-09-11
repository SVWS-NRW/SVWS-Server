package de.svws_nrw.repo.uv.lerngruppen;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppenLehrer;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Lerngruppen-Lehrer-Zuordnungen
 * (UV_LerngruppenLehrer) der SVWS-Datenbank.
 */
public interface UvLerngruppenLehrerRepository extends Repository<DTOUvLerngruppenLehrer> {

	/**
	 * Ermittelt alle Lerngruppen-Lehrer-Zuordnungen für den angegebenen Planungsabschnitt.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Lerngruppen-Lehrer-Zuordnungen
	 */
	List<DTOUvLerngruppenLehrer> getListByPlanungsabschnitt(long idPlanungsabschnitt);

}
