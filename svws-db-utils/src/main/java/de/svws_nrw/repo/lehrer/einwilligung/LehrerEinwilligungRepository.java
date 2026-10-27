package de.svws_nrw.repo.lehrer.einwilligung;

import java.util.List;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerDatenschutz;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerDatenschutzPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Einwilligungen von Lehrern in der SVWS-Datenbank.
 * Die ID einer Einwilligung ist zusammengesetzt aus der ID des Lehrers und der ID der Einwilligungsart.
 */
public interface LehrerEinwilligungRepository extends RepositoryBase<DTOLehrerDatenschutz, DTOLehrerDatenschutzPK> {

	/**
	 * Bestimmt alle Einwilligungen eines Lehrers.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die Einwilligungen des Lehrers, leer wenn keine vorhanden sind
	 */
	List<DTOLehrerDatenschutz> getListByIdLehrer(long idLehrer);

	/**
	 * Bestimmt alle Lehrer-Einwilligungen zu einer Einwilligungsart.
	 *
	 * @param idEinwilligungsart   die ID der Einwilligungsart
	 *
	 * @return die Einwilligungen zur Einwilligungsart, leer wenn keine vorhanden sind
	 */
	List<DTOLehrerDatenschutz> getListByIdEinwilligungsart(long idEinwilligungsart);

}
