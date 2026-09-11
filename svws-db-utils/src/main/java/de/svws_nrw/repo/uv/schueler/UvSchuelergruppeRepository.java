package de.svws_nrw.repo.uv.schueler;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppe;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Schülergruppen
 * (UV_Schuelergruppen) der SVWS-Datenbank.
 */
public interface UvSchuelergruppeRepository extends Repository<DTOUvSchuelergruppe> {

	/**
	 * Ermittelt alle Schülergruppen für den angegebenen Planungsabschnitt.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Schülergruppen
	 */
	List<DTOUvSchuelergruppe> getListByPlanungsabschnitt(long idPlanungsabschnitt);

}
