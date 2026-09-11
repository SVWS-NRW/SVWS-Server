package de.svws_nrw.repo.uv.schueler;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeSchueler;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeSchuelerPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Schülergruppe-Schüler-Zuordnungen
 * (UV_Schuelergruppen_Schueler) der SVWS-Datenbank.
 */
public interface UvSchuelergruppeSchuelerRepository extends RepositoryBase<DTOUvSchuelergruppeSchueler, DTOUvSchuelergruppeSchuelerPK> {

	/**
	 * Ermittelt alle Schülergruppen-Schüler-Zuordnungen für den angegebenen Planungsabschnitt.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Zuordnungen
	 */
	List<DTOUvSchuelergruppeSchueler> getListByPlanungsabschnitt(long idPlanungsabschnitt);

}
