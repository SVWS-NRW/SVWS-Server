package de.svws_nrw.repo.gost.klausuren;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenTermine;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf GOSt-Klausurtermine.
 */
public interface GostKlausurenTerminRepository extends Repository<DTOGostKlausurenTermine> {

	/**
	 * Ermittelt die Klausurtermine eines Abiturjahrgangs und der angegebenen Halbjahre.
	 *
	 * @param abiturjahr das Abiturjahr
	 * @param halbjahre die GOSt-Halbjahre
	 *
	 * @return die Liste der Klausurtermine
	 */
	List<DTOGostKlausurenTermine> getListByAbiturjahrAndHalbjahre(int abiturjahr, Collection<GostHalbjahr> halbjahre);

	/**
	 * Ermittelt die Klausurtermine eines Abiturjahrgangs.
	 *
	 * @param abiturjahr das Abiturjahr
	 *
	 * @return die Liste der Klausurtermine
	 */
	List<DTOGostKlausurenTermine> getListByAbiturjahr(int abiturjahr);

	/**
	 * Ermittelt Klausurtermine zu den angegebenen Termin-IDs.
	 *
	 * @param ids die Termin-IDs
	 *
	 * @return die Liste der Klausurtermine
	 */
	List<DTOGostKlausurenTermine> getListByIds(Collection<Long> ids);

	/**
	 * Ermittelt Klausurtermine zu den angegebenen Datumswerten.
	 *
	 * @param datum die Datumswerte
	 *
	 * @return die Liste der Klausurtermine
	 */
	List<DTOGostKlausurenTermine> getListByDatum(Collection<String> datum);

}
