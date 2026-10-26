package de.svws_nrw.repo.schueler.einwilligung;

import java.util.List;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutz;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutzPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Einwilligungen von Schülern in der SVWS-Datenbank.
 * Die ID einer Einwilligung ist zusammengesetzt aus der ID des Schülers und der ID der Einwilligungsart.
 */
public interface SchuelerEinwilligungRepository extends RepositoryBase<DTOSchuelerDatenschutz, DTOSchuelerDatenschutzPK> {

	/**
	 * Bestimmt alle Einwilligungen eines Schülers.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return die Einwilligungen des Schülers, leer wenn keine vorhanden sind
	 */
	List<DTOSchuelerDatenschutz> getListByIdSchueler(long idSchueler);

	/**
	 * Bestimmt alle Schüler-Einwilligungen zu einer Einwilligungsart.
	 *
	 * @param idEinwilligungsart   die ID der Einwilligungsart
	 *
	 * @return die Einwilligungen zur Einwilligungsart, leer wenn keine vorhanden sind
	 */
	List<DTOSchuelerDatenschutz> getListByIdEinwilligungsart(long idEinwilligungsart);

}
