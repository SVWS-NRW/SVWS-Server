package de.svws_nrw.repo.uv.schueler;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeConstraintSchuelergruppe;
import de.svws_nrw.db.dto.current.uv.DTOUvSchuelergruppeConstraintSchuelergruppePK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der UV-Schülergruppen-Gruppen-Constraints
 * (UV_Schuelergruppen_Constraint_Schuelergruppen) der SVWS-Datenbank.
 */
public interface UvSchuelergruppeConstraintSchuelergruppeRepository
		extends RepositoryBase<DTOUvSchuelergruppeConstraintSchuelergruppe, DTOUvSchuelergruppeConstraintSchuelergruppePK> {

	/**
	 * Ermittelt alle Gruppen-Constraints einer Schülergruppe.
	 *
	 * @param idSchuelergruppe   die ID der Schülergruppe
	 *
	 * @return die Liste der Constraints
	 */
	List<DTOUvSchuelergruppeConstraintSchuelergruppe> getListBySchuelergruppe(long idSchuelergruppe);

	/**
	 * Ermittelt alle Gruppen-Constraints eines Planungsabschnitts.
	 *
	 * @param idPlanungsabschnitt   die ID des Planungsabschnitts
	 *
	 * @return die Liste der Constraints
	 */
	List<DTOUvSchuelergruppeConstraintSchuelergruppe> getListByPlanungsabschnitt(long idPlanungsabschnitt);

}
