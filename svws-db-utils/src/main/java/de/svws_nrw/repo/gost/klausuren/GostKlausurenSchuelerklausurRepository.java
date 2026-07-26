package de.svws_nrw.repo.gost.klausuren;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausuren;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf GOSt-Schülerklausuren.
 */
public interface GostKlausurenSchuelerklausurRepository extends Repository<DTOGostKlausurenSchuelerklausuren> {

	/**
	 * Ermittelt Schülerklausuren zu den angegebenen Kursklausuren.
	 *
	 * @param kursklausurIds die IDs der Kursklausuren
	 *
	 * @return die Liste der Schülerklausuren
	 */
	List<DTOGostKlausurenSchuelerklausuren> getListByKursklausurIds(Collection<Long> kursklausurIds);

}
