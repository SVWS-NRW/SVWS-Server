package de.svws_nrw.repo.uv.unterrichte;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvUnterrichtRaum;
import de.svws_nrw.db.dto.current.uv.DTOUvUnterrichtRaumPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Unterricht-Raum-Zuordnungen
 * (UV_Unterrichte_Raeume) der SVWS-Datenbank.
 */
public interface UvUnterrichtRaumRepository extends RepositoryBase<DTOUvUnterrichtRaum, DTOUvUnterrichtRaumPK> {

	/**
	 * Ermittelt alle Unterricht-Raum-Zuordnungen für den angegebenen Planungsabschnitt.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Zuordnungen
	 */
	List<DTOUvUnterrichtRaum> getListByPlanungsabschnitt(long idPlanungsabschnitt);

}
