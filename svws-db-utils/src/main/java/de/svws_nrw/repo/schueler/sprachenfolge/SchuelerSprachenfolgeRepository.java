package de.svws_nrw.repo.schueler.sprachenfolge;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachenfolge;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Fächer-Tabelle für die Schüler-Abiturdaten der SVWS-Datenbank
 */
public interface SchuelerSprachenfolgeRepository extends Repository<DTOSchuelerSprachenfolge> {

	/**
	 * Bestimmt die Schüler-Sprachenfolge-Datenbank-Objekte für die übergebenen Schüler-IDs
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Liste mit den DB-DTOs
	 */
	List<DTOSchuelerSprachenfolge> getListBySchuelerIds(Collection<Long> idsSchueler);

	/**
	 * Bestimmt die Schüler-Sprachenfolge-Datenbank-Objekte für die übergebenen Schüler-IDs und gibt diese als Map zurück,
	 * welche die Schüler-ID als Schlüssel verwendet.
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Map mit den DB-DTOs
	 */
	Map<Long, List<DTOSchuelerSprachenfolge>> getMapBySchuelerIDs(Collection<Long> idsSchueler);

}
