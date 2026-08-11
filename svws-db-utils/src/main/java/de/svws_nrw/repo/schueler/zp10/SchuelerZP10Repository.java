package de.svws_nrw.repo.schueler.zp10;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerZP10;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Schüler-ZP10-Tabelle der SVWS-Datenbank
 */
public interface SchuelerZP10Repository extends Repository<DTOSchuelerZP10> {

	/**
	 * Bestimmt die Schüler-ZP10-Datenbank-Objekte für die übergebenen Schüler-IDs
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Liste mit den DB-DTOs
	 */
	List<DTOSchuelerZP10> getListBySchuelerIds(Collection<Long> idsSchueler);

}
