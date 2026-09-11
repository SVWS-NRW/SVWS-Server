package de.svws_nrw.repo.uv.schienen;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvSchienenConstraintJahrgang;
import de.svws_nrw.db.dto.current.uv.DTOUvSchienenConstraintJahrgangPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Tabelle der Schienen-Jahrgang-Constraints
 * (UV_SchienenConstraintJahrgang) der SVWS-Datenbank.
 */
public interface UvSchieneConstraintJahrgangRepository extends RepositoryBase<DTOUvSchienenConstraintJahrgang, DTOUvSchienenConstraintJahrgangPK> {

	/**
	 * Ermittelt alle Jahrgang-Constraints für die angegebene Schiene.
	 *
	 * @param idSchiene   die ID der Schiene
	 *
	 * @return die Liste der Constraints
	 */
	List<DTOUvSchienenConstraintJahrgang> getListBySchieneId(long idSchiene);

	/**
	 * Ermittelt alle Jahrgang-Constraints für die angegebenen Schienen-IDs.
	 *
	 * @param schieneIds   die IDs der Schienen
	 *
	 * @return die Liste der Constraints
	 */
	List<DTOUvSchienenConstraintJahrgang> getListBySchieneIds(List<Long> schieneIds);

}
