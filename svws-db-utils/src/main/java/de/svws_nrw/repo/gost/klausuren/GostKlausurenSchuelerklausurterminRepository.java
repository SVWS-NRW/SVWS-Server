package de.svws_nrw.repo.gost.klausuren;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenSchuelerklausurenTermine;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf GOSt-Schülerklausurtermine.
 */
public interface GostKlausurenSchuelerklausurterminRepository extends Repository<DTOGostKlausurenSchuelerklausurenTermine> {

	/**
	 * Ermittelt Schülerklausurtermine zu den angegebenen Schülerklausuren.
	 *
	 * @param schuelerklausurIds die IDs der Schülerklausuren
	 *
	 * @return die Liste der Schülerklausurtermine
	 */
	List<DTOGostKlausurenSchuelerklausurenTermine> getListBySchuelerklausurIds(Collection<Long> schuelerklausurIds);

	/**
	 * Ermittelt Schülerklausurtermine zu den angegebenen Klausurterminen.
	 *
	 * @param terminIds die IDs der Klausurtermine
	 *
	 * @return die Liste der Schülerklausurtermine
	 */
	List<DTOGostKlausurenSchuelerklausurenTermine> getListByTerminIds(Collection<Long> terminIds);

}
