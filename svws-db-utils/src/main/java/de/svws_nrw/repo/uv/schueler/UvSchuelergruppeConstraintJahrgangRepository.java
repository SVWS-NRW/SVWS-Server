package de.svws_nrw.repo.uv.schueler;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeConstraintJahrgang;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeConstraintJahrgangPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Schülergruppen-Jahrgang-Constraints
 * (UV_Schuelergruppen_Constraint_Jahrgaenge) der SVWS-Datenbank.
 */
public interface UvSchuelergruppeConstraintJahrgangRepository
		extends RepositoryBase<DTOUvSchuelergruppeConstraintJahrgang, DTOUvSchuelergruppeConstraintJahrgangPK> {

	/**
	 * Ermittelt alle Jahrgang-Constraints einer Schülergruppe.
	 *
	 * @param idSchuelergruppe   die ID der Schülergruppe
	 *
	 * @return die Liste der Constraints
	 */
	List<DTOUvSchuelergruppeConstraintJahrgang> getListBySchuelergruppe(long idSchuelergruppe);

	/**
	 * Ermittelt alle Jahrgang-Constraints eines Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Constraints
	 */
	List<DTOUvSchuelergruppeConstraintJahrgang> getListByPlanungsabschnitt(long idPlanungsabschnitt);

}
