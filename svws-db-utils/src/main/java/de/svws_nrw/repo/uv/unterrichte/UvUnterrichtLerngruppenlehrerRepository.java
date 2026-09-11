package de.svws_nrw.repo.uv.unterrichte;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvUnterrichteLerngruppenlehrer;
import de.svws_nrw.db.dto.current.uv.DTOUvUnterrichteLerngruppenlehrerPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Unterricht-Lerngruppenlehrer-Zuordnungen
 * (UV_Unterrichte_Lerngruppenlehrer) der SVWS-Datenbank.
 */
public interface UvUnterrichtLerngruppenlehrerRepository
		extends RepositoryBase<DTOUvUnterrichteLerngruppenlehrer, DTOUvUnterrichteLerngruppenlehrerPK> {

	/**
	 * Ermittelt alle Unterricht-Lerngruppenlehrer-Zuordnungen für den angegebenen Planungsabschnitt.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Zuordnungen
	 */
	List<DTOUvUnterrichteLerngruppenlehrer> getListByPlanungsabschnitt(long idPlanungsabschnitt);

}
