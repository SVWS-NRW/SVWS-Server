package de.svws_nrw.repo.gost.klausuren;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenKursklausuren;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf GOSt-Kursklausuren.
 */
public interface GostKlausurenKursklausurRepository extends Repository<DTOGostKlausurenKursklausuren> {

	/**
	 * Ermittelt Kursklausuren zu den angegebenen Klausurvorgaben.
	 *
	 * @param vorgabeIds die IDs der Klausurvorgaben
	 *
	 * @return die Liste der Kursklausuren
	 */
	List<DTOGostKlausurenKursklausuren> getListByVorgabeIds(Collection<Long> vorgabeIds);

	/**
	 * Ermittelt Kursklausuren zu den angegebenen Klausurterminen.
	 *
	 * @param terminIds die IDs der Klausurtermine
	 *
	 * @return die Liste der Kursklausuren
	 */
	List<DTOGostKlausurenKursklausuren> getListByTerminIds(Collection<Long> terminIds);

}
