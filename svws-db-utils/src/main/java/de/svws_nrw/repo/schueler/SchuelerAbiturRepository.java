package de.svws_nrw.repo.schueler;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbitur;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Schüler-Abiturdaten-Tabelle der SVWS-Datenbank
 */
public interface SchuelerAbiturRepository extends Repository<DTOSchuelerAbitur> {

	/**
	 * Bestimmt die Schüler-Abitur-Datenbank-Objekte für die übergebenen Schüler-IDs
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Liste mit den DB-DTOs
	 */
	List<DTOSchuelerAbitur> getListBySchuelerIds(Collection<Long> idsSchueler);

}
