package de.svws_nrw.repo.gost.klausuren;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenRaeume;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf GOSt-Klausurräume.
 */
public interface GostKlausurenRaumRepository extends Repository<DTOGostKlausurenRaeume> {

	/**
	 * Ermittelt Klausurräume zu den angegebenen Klausurterminen.
	 *
	 * @param terminIds die IDs der Klausurtermine
	 *
	 * @return die Liste der Klausurräume
	 */
	List<DTOGostKlausurenRaeume> getListByTerminIds(Collection<Long> terminIds);

}
