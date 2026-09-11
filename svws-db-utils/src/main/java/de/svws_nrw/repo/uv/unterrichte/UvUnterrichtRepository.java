package de.svws_nrw.repo.uv.unterrichte;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvUnterricht;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Unterrichte
 * (UV_Unterrichte) der SVWS-Datenbank.
 */
public interface UvUnterrichtRepository extends Repository<DTOUvUnterricht> {

	/**
	 * Ermittelt alle Unterrichte für den angegebenen Planungsabschnitt.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Unterrichte
	 */
	List<DTOUvUnterricht> getListByPlanungsabschnitt(long idPlanungsabschnitt);

	/**
	 * Ermittelt alle Unterrichte für die angegebenen Lerngruppen.
	 *
	 * @param idsLerngruppen   die IDs der Lerngruppen
	 *
	 * @return die Liste der Unterrichte
	 */
	List<DTOUvUnterricht> getListByLerngruppenIds(Collection<Long> idsLerngruppen);

}
