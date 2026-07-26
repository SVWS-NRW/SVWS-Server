package de.svws_nrw.repo.gost.klausuren;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenRaumstunden;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf GOSt-Klausurraumstunden.
 */
public interface GostKlausurenRaumstundeRepository extends Repository<DTOGostKlausurenRaumstunden> {

	/**
	 * Ermittelt Klausurraumstunden zu den angegebenen Klausurräumen.
	 *
	 * @param raumIds die IDs der Klausurräume
	 *
	 * @return die Liste der Klausurraumstunden
	 */
	List<DTOGostKlausurenRaumstunden> getListByRaumIds(Collection<Long> raumIds);

	/**
	 * Ermittelt Klausurraumstunden, die von keiner Schülerklausurtermin-Raumstunden-Zuordnung mehr verwendet werden.
	 *
	 * @return die Liste der verwaisten Klausurraumstunden
	 */
	List<DTOGostKlausurenRaumstunden> getUnreferenced();

}
